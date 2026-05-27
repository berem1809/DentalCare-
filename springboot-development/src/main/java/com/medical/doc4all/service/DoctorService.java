package com.medical.doc4all.service;

import com.medical.doc4all.dto.BookingResponseDTO;
import com.medical.doc4all.dto.DoctorResponseDTO;
import com.medical.doc4all.dto.UpdateDoctor;
import com.medical.doc4all.enums.Responses;

import java.util.List;

public interface DoctorService {
    Responses createDoctor(String name, String email);

    Responses checkProfileStatus(String email);

    List<DoctorResponseDTO> getDoctorsByEmail(String email);

    List<BookingResponseDTO> getBookingsForDoctor(String email);

    void updateDoctorProfile(UpdateDoctor update, String email);
}


