package com.medical.doc4all.repository;

import com.medical.doc4all.entity.DoctorReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorReviewRepository extends JpaRepository<DoctorReview, Long> {
    boolean existsByBookingId(Long bookingId);
    Page<DoctorReview> findByDoctorId(Long doctorId, Pageable pageable);
    List<DoctorReview> findByDoctorId(Long doctorId);
}
