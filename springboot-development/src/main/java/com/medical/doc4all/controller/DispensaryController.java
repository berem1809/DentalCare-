package com.medical.doc4all.controller;

import com.medical.doc4all.dto.*;
import com.medical.doc4all.enums.Responses;
import com.medical.doc4all.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/dispensary")
@RequiredArgsConstructor
public class DispensaryController {

    private final DispensaryService dispensaryService;
    private final InvitationService invitationService;
    private final ScheduleService scheduleService;
    private final SlotService slotService;
    private final ReviewService reviewService;

    // Returns dispensary info + approved doctors & their weekly schedules
    @GetMapping("/{id}/details")
    public ResponseEntity<DispensaryDetailsDTO> getDetails(@PathVariable("id") Long dispensaryId,
                                                           @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        // date is optional. If front-end provides a date, you can compute availability client-side via /schedules/{scheduleId}/slots?date=...
        DispensaryDetailsDTO dto = dispensaryService.getDispensaryDetails(dispensaryId, date);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{dispensaryId}/reviews")
    public ResponseEntity<Page<DispensaryReviewResponseDTO>> getDispensaryReviews(
            @PathVariable Long dispensaryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<DispensaryReviewResponseDTO> p = reviewService.getDispensaryReviews(dispensaryId, PageRequest.of(page, size, Sort.by("createdAt").descending()));
        return ResponseEntity.ok(p);
    }

    @PostMapping
    public ResponseEntity<DispensaryResponse> createDispensary( @RequestBody DispensaryCreateRequest request) {
        DispensaryResponse response = dispensaryService.createDispensary(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    @PostMapping("/profile-status")
    public ResponseEntity<ProfileStatusResponse> checkProfileStatus(@RequestBody Email email, @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        // Check if all required fields are filled
        log.info("header email {}", userEmail);
        boolean isComplete = dispensaryService.checkProfileStatus(userEmail) == Responses.Success;
        ProfileStatusResponse p = ProfileStatusResponse.builder().isComplete(isComplete).build();

        return ResponseEntity.ok(p);
        // Return { "isComplete": true/false }
    }



    // Complete profile
   // @PostMapping("/dispensary/complete-profile")
  // public ResponseEntity<String> completeProfile(@RequestBody DispensaryProfileRequest request) {
     // Save profile data to dispensary table
       // Mark profile_completed = true
        // Return success response
  // }

    // Get profile
    @GetMapping("/profile")
    public ResponseEntity<DispensaryProfileResponse> getProfile(
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        log.info("Fetching profile for email: {}", userEmail);
        return ResponseEntity.ok(dispensaryService.getProfileByEmail(userEmail));
    }
   // Update profile
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @RequestBody DispensaryProfileRequest request,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {

        try {
            dispensaryService.updateDispensaryProfile(userEmail, request);
            return ResponseEntity.ok(Map.of("message", "Dispensary profile updated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error updating dispensary profile for user: {}", userEmail, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update profile");
        }
    }

    @PutMapping("/{email}/validity")
    public ResponseEntity<MessageResponse> updateDispensaryValidity(
            @PathVariable String email,
            @RequestParam boolean isValid) {
        MessageResponse response = dispensaryService.updateDispensaryValidity(email, isValid);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DispensaryResponse> getDispensaryByEmail(@PathVariable Long id) {
        DispensaryResponse response = dispensaryService.getDispensaryByEmail(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DispensaryResponse>> getAllDispensaries(
            @RequestHeader(value = "Authorization", required = false) String token) {
        List<DispensaryResponse> dispensaries = dispensaryService.getAllDispensaries();
        return ResponseEntity.ok(dispensaries);
    }

    //Invitation Scenerio

    @PostMapping("/invitations")
    public ResponseEntity<DoctorInvitationResponseDTO> createInvitation(@RequestBody DoctorInvitationRequestDTO req, @RequestHeader(value = "X-User-Email") String userEmail) {
        // ensure dispensaryId aligns with body
        DoctorInvitationResponseDTO resp = invitationService.createInvitation(req, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PostMapping("/revoke/{id}")
    public ResponseEntity<?> revokeInvitation(@PathVariable Long id) {
        try {
            invitationService.revokeInvitation(id);
            return ResponseEntity.ok(Map.of("message", "Invitation revoked successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error revoking invitation with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to revoke invitation");
        }
    }



    @GetMapping("/{id}/schedules")
    public ResponseEntity<List<DoctorScheduleDTO>> getSchedules(@PathVariable("id") Long dispensaryId) {
        List<DoctorScheduleDTO> schedules = scheduleService.getSchedulesForDispensary(dispensaryId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/schedules/{scheduleId}/slots")
    public ResponseEntity<List<SlotDTO>> getAvailableSlots(
            @PathVariable Long scheduleId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(slotService.getAvailableSlots(scheduleId, date));
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingResponseDTO>> getBookings(@RequestHeader(value = "X-User-Email") String userEmail) {
        List<BookingResponseDTO> resp = dispensaryService.getBookingsForDispensary(userEmail);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<BookingResponseDTO> completeBooking(@PathVariable("id") Long bookingId) {
        BookingResponseDTO resp = dispensaryService.completeBooking(bookingId);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/invitations")
    public ResponseEntity<List<DoctorInvitationResponseDTO>> getInvitations(@RequestHeader(value = "X-User-Email") String userEmail) {
        List<DoctorInvitationResponseDTO> inv = invitationService.getInvitationsForDispensary(userEmail);
        return ResponseEntity.ok(inv);
    }

    @PutMapping("/update-location")
    public ResponseEntity<?> updateLocation(
            @RequestHeader(value = "X-User-Email", required = false) String userEmail,
            @RequestBody updateLocationDTO request) {
        try {
            dispensaryService.updateLocation(userEmail, request);
            return ResponseEntity.ok(Map.of("message", "Location updated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error updating location for dispensary with email: {}", userEmail, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update location");
        }
    }
}