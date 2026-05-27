package com.medical.doc4all.service.impl;

import com.medical.doc4all.dto.DoctorScheduleDTO;
import com.medical.doc4all.entity.Doctor;
import com.medical.doc4all.entity.DoctorSchedule;
import com.medical.doc4all.enums.BookingStatus;
import com.medical.doc4all.enums.InvitationStatus;
import com.medical.doc4all.repository.BookingRepository;
import com.medical.doc4all.repository.DoctorRepository;
import com.medical.doc4all.repository.DoctorScheduleRepository;
import com.medical.doc4all.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final DoctorScheduleRepository scheduleRepo;
    private final DoctorRepository doctorRepo;

    private final BookingRepository bookingRepository;
    @Autowired
    public ScheduleServiceImpl(DoctorScheduleRepository scheduleRepo,
                               DoctorRepository doctorRepo,
                               BookingRepository bookingRepo) {
        this.doctorRepo = doctorRepo;
        this.scheduleRepo = scheduleRepo;
        this.bookingRepository = bookingRepo;
    }

    @Override
    public List<DoctorScheduleDTO> getSchedulesForDoctor(String email) {
        Doctor doctor = doctorRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Doctor not found with email: " + email));
        return scheduleRepo.findByDoctorId(doctor.getId()).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<DoctorScheduleDTO> getSchedulesForDispensary(Long dispensaryId) {
        return scheduleRepo.findByDispensaryId(dispensaryId).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public void removeSchedule(Long id, String email) {
        Doctor doctor = doctorRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Doctor not found with email: " + email));

        DoctorSchedule schedule = scheduleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found with id: " + id));

        // Only the owning doctor should be able to modify their schedule (optional check)
        if (!schedule.getDoctor().equals(doctor)) {
            throw new RuntimeException("Schedule does not belong to this doctor");
        }

        // If schedule is currently ACCEPTED, check if any confirmed bookings exist
        if (schedule.getStatus().equals(InvitationStatus.ACCEPTED)) {
            boolean hasConfirmedBookings = bookingRepository
                    .existsByScheduleAndStatusAndAppointmentDateGreaterThanEqual(
                            schedule,
                            BookingStatus.CONFIRMED,
                            LocalDate.now()
                    );

            if (hasConfirmedBookings) {
                throw new RuntimeException("Schedule cannot be set to pending: confirmed bookings exist");
            }

            // No bookings found → safe to make pending
            schedule.setStatus(InvitationStatus.PENDING);

        } else if (schedule.getStatus().equals(InvitationStatus.PENDING)) {
            // Allow re-accepting schedule
            schedule.setStatus(InvitationStatus.ACCEPTED);
        }

        scheduleRepo.save(schedule);
    }


    private DoctorScheduleDTO toDto(DoctorSchedule s) {
        DoctorScheduleDTO dto = new DoctorScheduleDTO();
        dto.setId(s.getId());
        dto.setDoctorId(s.getDoctor().getId());
        dto.setDispensaryId(s.getDispensary().getId());
        dto.setDay(s.getDay());
        dto.setStartTime(s.getStartTime());
        dto.setEndTime(s.getEndTime());
        dto.setPerPatientMinutes(s.getPerPatientMinutes());
        dto.setAgreedRate(s.getAgreedRate());
        dto.setStatus(String.valueOf(s.getStatus()));
        return dto;
    }
}
