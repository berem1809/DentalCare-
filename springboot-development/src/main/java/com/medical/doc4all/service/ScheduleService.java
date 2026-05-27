package com.medical.doc4all.service;

import com.medical.doc4all.dto.DoctorScheduleDTO;

import java.util.List;

public interface ScheduleService {
    List<DoctorScheduleDTO> getSchedulesForDoctor(String Email);
    List<DoctorScheduleDTO> getSchedulesForDispensary(Long dispensaryId);

    void removeSchedule(Long id, String email);
}
