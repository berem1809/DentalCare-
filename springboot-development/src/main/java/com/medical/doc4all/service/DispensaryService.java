package com.medical.doc4all.service;

import com.medical.doc4all.dto.*;
import com.medical.doc4all.enums.Responses;

import java.time.LocalDate;
import java.util.List;

public interface DispensaryService {
    DispensaryResponse createDispensary(DispensaryCreateRequest request);
    MessageResponse updateDispensaryValidity(String email, boolean isValid);

    Responses checkProfileStatus(String email);

    void updateDispensaryProfile(String userEmail, DispensaryProfileRequest request);
    DispensaryResponse getDispensaryByEmail(Long id);

    DispensaryDetailsDTO getDispensaryDetails(Long dispensaryId, LocalDate forDate);
     DispensaryProfileResponse getProfileByEmail(String email);

    List<BookingResponseDTO> getBookingsForDispensary(String email);
    List<DispensaryResponse> getAllDispensaries();

    BookingResponseDTO completeBooking(Long bookingId);

    void updateLocation(String userEmail, updateLocationDTO request);
}
