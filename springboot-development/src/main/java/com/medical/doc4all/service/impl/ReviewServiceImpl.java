package com.medical.doc4all.service.impl;

import com.medical.doc4all.dto.*;
import com.medical.doc4all.entity.*;
import com.medical.doc4all.enums.BookingStatus;
import com.medical.doc4all.exception.BadRequestException;
import com.medical.doc4all.exception.ResourceNotFoundException;
import com.medical.doc4all.repository.*;
import com.medical.doc4all.service.ReviewService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final DoctorReviewRepository doctorReviewRepo;
    private final DispensaryReviewRepository dispensaryReviewRepo;
    private final BookingRepository bookingRepo;
    private final DoctorRepository doctorRepo;
    private final DispensaryRepository dispensaryRepo;
    private final PatientRepository patientRepo;

    @Autowired
    public ReviewServiceImpl(DoctorReviewRepository doctorReviewRepo,
                             DispensaryReviewRepository dispensaryReviewRepo,
                             BookingRepository bookingRepo,
                             DoctorRepository doctorRepo,
                             DispensaryRepository dispensaryRepo,
                             PatientRepository patientRepo) {
        this.doctorReviewRepo = doctorReviewRepo;
        this.dispensaryReviewRepo = dispensaryReviewRepo;
        this.bookingRepo = bookingRepo;
        this.doctorRepo = doctorRepo;
        this.dispensaryRepo = dispensaryRepo;
        this.patientRepo = patientRepo;
    }

    @Override
    @Transactional
    public DoctorReviewResponseDTO createDoctorReview(DoctorReviewRequestDTO dto, String email) {
        // load booking and validate
        Booking booking = bookingRepo.findById(dto.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new BadRequestException("Only completed bookings can be reviewed");
        }

        Patient p = patientRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with email: " + email));

        // ensure patient is owner of booking (or has admin rights)
        if (booking.getPatient().getId() != (p.getId())) {
            throw new BadRequestException("You can only review bookings you made");
        }

        if (doctorReviewRepo.existsByBookingId(booking.getId())) {
            throw new BadRequestException("Doctor review already exists for this booking");
        }

        // validate rating
        if (dto.getRating() < 1 || dto.getRating() > 5) throw new BadRequestException("Rating must be 1..5");

        // build review
        DoctorReview review = new DoctorReview();
        review.setBooking(booking);
        review.setPatient(booking.getPatient());
        review.setDoctor(booking.getSchedule().getDoctor());
        review.setDispensary(booking.getSchedule().getDispensary());
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setAnonymous(dto.isAnonymous());

        DoctorReview saved = doctorReviewRepo.save(review);
        return mapToDoctorResponse(saved);
    }

    @Override
    @Transactional
    public DispensaryReviewResponseDTO createDispensaryReview(DispensaryReviewRequestDTO dto, String email) {
        Booking booking = bookingRepo.findById(dto.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new BadRequestException("Only completed bookings can be reviewed");
        }

        Patient patient = patientRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with email: " + email));

        if (booking.getPatient().getId() != patient.getId()) {
            throw new BadRequestException("You can only review bookings you made");
        }

        if (dispensaryReviewRepo.existsByBookingId(booking.getId())) {
            throw new BadRequestException("Dispensary review already exists for this booking");
        }

        // validate category ratings
        validateCategory(dto.getCleanliness(), "cleanliness");
        validateCategory(dto.getStaffSupport(), "staffSupport");
        validateCategory(dto.getAccessibility(), "accessibility");

        Dispensary dispensary = booking.getSchedule().getDispensary();

        DispensaryReview review = new DispensaryReview();
        review.setBooking(booking);
        review.setPatient(booking.getPatient());
        review.setDispensary(dispensary);
        review.setCleanliness(dto.getCleanliness());
        review.setStaffSupport(dto.getStaffSupport());
        review.setAccessibility(dto.getAccessibility());
        review.setComment(dto.getComment());
        review.setAnonymous(dto.isAnonymous());

        DispensaryReview saved = dispensaryReviewRepo.save(review);

        // Update dispensary rating incrementally
        updateDispensaryAverageRatingOnNewReview(dispensary, saved.getOverallRating());

        return mapToDispensaryResponse(saved);
    }

    private void validateCategory(int v, String name) {
        if (v < 1 || v > 5) throw new BadRequestException(name + " rating must be 1..5");
    }

    private void updateDispensaryAverageRatingOnNewReview(Dispensary dispensary, double newOverall) {
        // ensure fields not null
        int currentCount = dispensary.getReviewCount() == null ? 0 : dispensary.getReviewCount();
        double currentAvg = dispensary.getRating() == null ? 0.0 : dispensary.getRating();

        double updatedAvg = (currentAvg * currentCount + newOverall) / (currentCount + 1);
        dispensary.setRating((float) updatedAvg);
        dispensary.setReviewCount(currentCount + 1);

        dispensaryRepo.save(dispensary);
    }

    @Override
    public Page<DoctorReviewResponseDTO> getDoctorReviews(Long doctorId, Pageable pageable) {
        Page<DoctorReview> page = doctorReviewRepo.findByDoctorId(doctorId, pageable);
        return page.map(this::mapToDoctorResponse);
    }

    @Override
    public Page<DispensaryReviewResponseDTO> getDispensaryReviews(Long dispensaryId, Pageable pageable) {
        Page<DispensaryReview> page = dispensaryReviewRepo.findByDispensaryId(dispensaryId, pageable);
        return page.map(this::mapToDispensaryResponse);
    }

    @Override
    public ReviewableDTO isBookingReviewable(Long bookingId, String email){
        Patient p = patientRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with email: " + email));

        boolean doctorreviewable = doctorReviewRepo.existsByBookingId(bookingId);
        boolean dispensaryreviewable = dispensaryReviewRepo.existsByBookingId(bookingId);

        ReviewableDTO resp = new ReviewableDTO();
        resp.setDispensaryreviewable(!dispensaryreviewable);
        resp.setDoctorreviewable(!doctorreviewable);

        return resp;
    }

    // mapping helpers
    private DoctorReviewResponseDTO mapToDoctorResponse(DoctorReview r) {
        DoctorReviewResponseDTO dto = new DoctorReviewResponseDTO();
        dto.setId(r.getId());
        dto.setDoctorId((long) r.getDoctor().getId());
        dto.setDispensaryId(r.getDispensary().getId());
        dto.setBookingId(r.getBooking().getId());
        dto.setRating(r.getRating());
        dto.setComment(r.getComment());
        dto.setAnonymous(r.isAnonymous());
        dto.setCreatedAt(r.getCreatedAt());
        if (!r.isAnonymous()) {
            dto.setPatientId((int) r.getPatient().getId());
            dto.setPatientName(r.getPatient().getName());
        }
        return dto;
    }

    private DispensaryReviewResponseDTO mapToDispensaryResponse(DispensaryReview r) {
        DispensaryReviewResponseDTO dto = new DispensaryReviewResponseDTO();
        dto.setId(r.getId());
        dto.setDispensaryId(r.getDispensary().getId());
        dto.setBookingId(r.getBooking().getId());
        dto.setCleanliness(r.getCleanliness());
        dto.setStaffSupport(r.getStaffSupport());
        dto.setAccessibility(r.getAccessibility());
        dto.setOverallRating(r.getOverallRating());
        dto.setComment(r.getComment());
        dto.setAnonymous(r.isAnonymous());
        dto.setCreatedAt(r.getCreatedAt());
        if (!r.isAnonymous()) {
            dto.setPatientId((int) r.getPatient().getId());
            dto.setPatientName(r.getPatient().getName());
        }
        return dto;
    }
}

