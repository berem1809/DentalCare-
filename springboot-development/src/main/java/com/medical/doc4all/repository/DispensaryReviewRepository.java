package com.medical.doc4all.repository;

import com.medical.doc4all.entity.DispensaryReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DispensaryReviewRepository extends JpaRepository<DispensaryReview, Long> {
    boolean existsByBookingId(Long bookingId);
    Page<DispensaryReview> findByDispensaryId(Long dispensaryId, Pageable pageable);
    List<DispensaryReview> findByDispensaryId(Long dispensaryId);
}
