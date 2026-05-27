package com.medical.doc4all.service;

import com.medical.doc4all.dto.updateLocationDTO;
import com.medical.doc4all.enums.Responses;

public interface PatientService {
    Responses createPatient(String email, String name);
    Responses updateLocation(updateLocationDTO request, String email);
    updateLocationDTO getLocation(String email);
}
