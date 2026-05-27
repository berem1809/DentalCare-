package com.medical.doc4all.service.impl;

import com.medical.doc4all.dto.DoctorInvitationRequestDTO;
import com.medical.doc4all.dto.DoctorInvitationResponseDTO;
import com.medical.doc4all.entity.Dispensary;
import com.medical.doc4all.entity.Doctor;
import com.medical.doc4all.entity.DoctorInvitation;
import com.medical.doc4all.entity.DoctorSchedule;
import com.medical.doc4all.enums.InvitationStatus;
import com.medical.doc4all.exception.BadRequestException;
import com.medical.doc4all.exception.ConflictException;
import com.medical.doc4all.exception.ResourceNotFoundException;
import com.medical.doc4all.repository.DispensaryRepository;
import com.medical.doc4all.repository.DoctorInvitationRepository;
import com.medical.doc4all.repository.DoctorRepository;
import com.medical.doc4all.repository.DoctorScheduleRepository;
import com.medical.doc4all.service.InvitationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.medical.doc4all.dto.InvitationRespondDTO;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvitationServiceImpl implements InvitationService {

    private final DoctorInvitationRepository invitationRepo;
    private final DoctorRepository doctorRepo;
    private final DispensaryRepository dispensaryRepo;
    private final DoctorScheduleRepository scheduleRepo;

    @Autowired
    public InvitationServiceImpl(DoctorInvitationRepository invitationRepo,
                                 DoctorRepository doctorRepo,
                                 DispensaryRepository dispensaryRepo,
                                 DoctorScheduleRepository scheduleRepo) {
        this.invitationRepo = invitationRepo;
        this.doctorRepo = doctorRepo;
        this.dispensaryRepo = dispensaryRepo;
        this.scheduleRepo = scheduleRepo;
    }

    @Override
    @Transactional
    public DoctorInvitationResponseDTO createInvitation(DoctorInvitationRequestDTO dto, String email) {

        Dispensary dispensary = dispensaryRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Dispensary not found: " + dto.getDispensaryId()));
        dto.setDispensaryId(dispensary.getId());
        Doctor doctor = doctorRepo.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + dto.getDoctorId()));

        // Basic validations
        if (dto.getStartTime().isAfter(dto.getEndTime()) || dto.getStartTime().equals(dto.getEndTime())) {
            throw new BadRequestException("Start time must be before end time");
        }
        if (dto.getPerPatientMinutes() <= 0) throw new BadRequestException("perPatientMinutes must be > 0");

        DoctorInvitation inv = new DoctorInvitation();
        inv.setDispensary(dispensary);
        inv.setDoctor(doctor);
        inv.setDay(dto.getDay());
        inv.setStartTime(dto.getStartTime());
        inv.setEndTime(dto.getEndTime());
        inv.setPerPatientMinutes(dto.getPerPatientMinutes());
        inv.setOfferRate(dto.getOfferRate());
        inv.setStatus(InvitationStatus.PENDING);
        inv.setPerPatientRate(dto.getPerPatientRate());

        DoctorInvitation saved = invitationRepo.save(inv);
        return toResponseDTO(saved);
    }

    @Override
    @Transactional
    public void revokeInvitation(Long invitationId) {
        DoctorInvitation inv = invitationRepo.findById(invitationId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found: " + invitationId));
        if (inv.getStatus() != InvitationStatus.PENDING) {
            throw new BadRequestException("Only pending invitations can be revoked");
        }
        invitationRepo.delete(inv);
    }

    @Override
    @Transactional
    public DoctorInvitationResponseDTO respondToInvitation(InvitationRespondDTO dto) {
        DoctorInvitation inv = invitationRepo.findById(dto.getInvitationId())
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found: " + dto.getInvitationId()));

        if (!"ACCEPT".equalsIgnoreCase(dto.getResponse()) && !"REJECT".equalsIgnoreCase(dto.getResponse())) {
            throw new BadRequestException("Response must be ACCEPT or REJECT");
        }

        if (inv.getStatus() != InvitationStatus.PENDING) {
            throw new BadRequestException("Invitation already responded: " + inv.getStatus());
        }

        if ("REJECT".equalsIgnoreCase(dto.getResponse())) {
            inv.setStatus(InvitationStatus.REJECTED);
            invitationRepo.save(inv);
            return toResponseDTO(inv);
        }

        // ACCEPT path -> check conflicts with existing schedules for that doctor on same day
        Integer doctorId = inv.getDoctor().getId();
        java.time.DayOfWeek day = inv.getDay();
        java.time.LocalTime s = inv.getStartTime();
        java.time.LocalTime e = inv.getEndTime();

        List<DoctorSchedule> schedulesForDay = scheduleRepo.findByDoctorIdAndDay(doctorId, day);
        boolean conflict = schedulesForDay.stream().anyMatch(ds -> timesOverlap(ds.getStartTime(), ds.getEndTime(), s, e));

        if (conflict) {
            throw new ConflictException("Doctor already has a schedule overlapping this time on " + day);
        }

        // create schedule and update invitation status
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setDoctor(inv.getDoctor());
        schedule.setDispensary(inv.getDispensary());
        schedule.setDay(inv.getDay());
        schedule.setStartTime(inv.getStartTime());
        schedule.setEndTime(inv.getEndTime());
        schedule.setPerPatientMinutes(inv.getPerPatientMinutes());
        schedule.setAgreedRate(inv.getPerPatientRate());

        scheduleRepo.save(schedule);

        inv.setStatus(InvitationStatus.ACCEPTED);
        invitationRepo.save(inv);

        return toResponseDTO(inv);
    }

    @Override
    public List<DoctorInvitationResponseDTO> getInvitationsForDoctor(String email) {
        Doctor doctor = doctorRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with email: " + email));
        return invitationRepo.findByDoctorId(doctor.getId()).stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<DoctorInvitationResponseDTO> getInvitationsForDispensary(String email) {
        Dispensary dispensary = dispensaryRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Dispensary not found with email: " + email));
        return invitationRepo.findByDispensaryId(dispensary.getId()).stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    private boolean timesOverlap(java.time.LocalTime aStart, java.time.LocalTime aEnd,
                                 java.time.LocalTime bStart, java.time.LocalTime bEnd) {
        return aStart.isBefore(bEnd) && bStart.isBefore(aEnd);
    }

    private DoctorInvitationResponseDTO toResponseDTO(DoctorInvitation inv) {
        DoctorInvitationResponseDTO r = new DoctorInvitationResponseDTO();
        r.setId(inv.getId());
        r.setDispensaryId(inv.getDispensary().getId());
        r.setDoctorId(inv.getDoctor().getId());
        r.setDay(inv.getDay());
        r.setStartTime(inv.getStartTime());
        r.setEndTime(inv.getEndTime());
        r.setPerPatientMinutes(inv.getPerPatientMinutes());
        r.setOfferRate(inv.getOfferRate());
        r.setStatus(inv.getStatus().name());
        return r;
    }
}

