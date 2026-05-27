package com.medical.doc4all.service;

import com.medical.doc4all.dto.DoctorScheduleResponseDTO;

import java.util.List;

public interface DoctorScheduleService {
    List<DoctorScheduleResponseDTO> getApprovedSchedulesByDispensary(Long dispensaryId);
}

