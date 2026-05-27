package com.medical.doc4all.service.impl;

import com.medical.doc4all.dto.DoctorScheduleResponseDTO;
import com.medical.doc4all.entity.DoctorSchedule;
import com.medical.doc4all.enums.InvitationStatus;
import com.medical.doc4all.repository.DoctorScheduleRepository;
import com.medical.doc4all.service.DoctorScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorScheduleServiceImpl implements DoctorScheduleService {
    @Autowired
    private DoctorScheduleRepository scheduleRepo;

    @Override
    public List<DoctorScheduleResponseDTO> getApprovedSchedulesByDispensary(Long dispensaryId) {
        List<DoctorSchedule> schedules =
                scheduleRepo.findByDispensaryIdAndStatus(dispensaryId, InvitationStatus.ACCEPTED);

        return schedules.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private DoctorScheduleResponseDTO toDTO(DoctorSchedule s) {
        DoctorScheduleResponseDTO dto = new DoctorScheduleResponseDTO();
        dto.setDoctorId(s.getDoctor().getId());
        dto.setDoctorName(s.getDoctor().getName());
        dto.setSpecialities(s.getDoctor().getSpecialities());
        dto.setEducation(s.getDoctor().getEducation());
        dto.setDay(s.getDay());
        dto.setStartTime(LocalTime.parse(s.getStartTime().toString()));
        dto.setEndTime(LocalTime.parse(s.getEndTime().toString()));
        dto.setPerPatientMinutes(s.getPerPatientMinutes());
        dto.setRate(s.getAgreedRate());
        return dto;
    }
}
