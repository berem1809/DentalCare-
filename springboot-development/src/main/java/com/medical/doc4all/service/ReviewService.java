package com.medical.doc4all.service;

import com.medical.doc4all.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    DoctorReviewResponseDTO createDoctorReview(DoctorReviewRequestDTO dto, String email);
    DispensaryReviewResponseDTO createDispensaryReview(DispensaryReviewRequestDTO dto, String email);

    Page<DoctorReviewResponseDTO> getDoctorReviews(Long doctorId, Pageable pageable);
    Page<DispensaryReviewResponseDTO> getDispensaryReviews(Long dispensaryId, Pageable pageable);

    ReviewableDTO isBookingReviewable(Long bookingId, String email);
}

