package com.medical.doc4all.controller;

import com.medical.doc4all.dto.*;
import com.medical.doc4all.entity.Doctor;
import com.medical.doc4all.entity.Report;
import com.medical.doc4all.enums.Responses;
import com.medical.doc4all.repository.DoctorRepository;
import com.medical.doc4all.repository.ReportRepository;
import com.medical.doc4all.service.*;
import com.medical.doc4all.service.impl.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api/doctor")
public class DoctorController {

    private DoctorService doctorService;
    private final InvitationService invitationService;
    private final ScheduleService scheduleService;

    private final ReviewService reviewService;

    private final ReportService reportService;

    private final ReportRepository reportRepository;

    private final DoctorRepository doctorRepository;

    public DoctorController(DoctorService doctorService, InvitationService invitationService, ScheduleService scheduleService,
                            ReviewService reviewService, ReportService reportService,
                            ReportRepository reportRepository, DoctorRepository doctorRepository) {
        this.invitationService = invitationService;
        this.reportRepository = reportRepository;
        this.reviewService = reviewService;
        this.scheduleService = scheduleService;
        this.doctorService = doctorService;
        this.reportService = reportService;
        this.doctorRepository = doctorRepository;
    }

    @PostMapping
    public ResponseEntity<APIResponse> createDoctor(@Valid @RequestBody DoctorCreateRequest request) {
        Responses response = doctorService.createDoctor(request.getName(), request.getEmail());
        if(response == Responses.Success) {
            return ResponseEntity.ok(new APIResponse(true,200,"Doctor created successfully"));
        } else if(response == Responses.AlreadyExists) {
            return ResponseEntity.status(409).body(new APIResponse(false,409,"Doctor already exists"));
        } else {
            return ResponseEntity.status(500).body(new APIResponse(false,500,"Failed to create doctor"));
        }

    }

    @PostMapping("/remove-schedule/{id}")
    public ResponseEntity<?> removeSchedule( @RequestHeader(value = "X-User-Email", required = false) String userEmail,@PathVariable Long id){
        try {
            scheduleService.removeSchedule(id,userEmail);
            return ResponseEntity.ok(Map.of("message", "Schedule removed successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error removing schedule with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to remove schedule");
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @RequestBody UpdateDoctor request,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {

        try {
            doctorService.updateDoctorProfile(request, userEmail);
            return ResponseEntity.ok(Map.of("message", "Dispensary profile updated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error updating dispensary profile for user: {}", userEmail, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update profile");
        }
    }



    @PostMapping("/profile-status")
    public ResponseEntity<ProfileStatusResponse> checkProfileStatus( @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        // Check if all required fields are filled
        log.info("header email {}", userEmail);
        boolean isComplete = doctorService.checkProfileStatus(userEmail) == Responses.Success;
        ProfileStatusResponse p = ProfileStatusResponse.builder().isComplete(isComplete).build();

        return ResponseEntity.ok(p);
        // Return { "isComplete": true/false }
    }

    @GetMapping("/invitations")
    public ResponseEntity<List<DoctorInvitationResponseDTO>> invitations(@RequestHeader(value = "X-User-Email") String userEmail) {
        return ResponseEntity.ok(invitationService.getInvitationsForDoctor(userEmail));
    }
    @GetMapping
    public ResponseEntity<List<DoctorResponseDTO>> getDoctorsForDispensary( @RequestHeader(value = "X-User-Email") String userEmail) {
        List<DoctorResponseDTO> doctors = doctorService.getDoctorsByEmail(userEmail);
        return ResponseEntity.ok(doctors);
    }

    @PostMapping("/invitations/respond")
    public ResponseEntity<DoctorInvitationResponseDTO> respond(@RequestBody InvitationRespondDTO dto) {
        DoctorInvitationResponseDTO resp = invitationService.respondToInvitation(dto);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{doctorId}/reviews")
    public ResponseEntity<Page<DoctorReviewResponseDTO>> getDoctorReviews(
            @PathVariable Long doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<DoctorReviewResponseDTO> p = reviewService.getDoctorReviews(doctorId, PageRequest.of(page, size, Sort.by("createdAt").descending()));
        return ResponseEntity.ok(p);
    }

//Reports
    @GetMapping("/shared-reports")
    public ResponseEntity<List<ReportMetadataDTO>> shared(@RequestHeader(value = "X-User-Email") String userEmail) {
        List<ReportMetadataDTO> list = reportService.listReportsSharedWithDoctor(userEmail);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/reports/{reportId}/download")
    public void download(@RequestHeader(value = "X-User-Email") String userEmail,@PathVariable Long reportId,
                         HttpServletResponse response) throws Exception {

        // authorize & stream
        Optional<Report> rep = reportRepository.findById(reportId);
        response.setContentType(rep.get().getContentType() == null ? "application/octet-stream" : rep.get().getContentType());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(rep.get().getOriginalFileName(), StandardCharsets.UTF_8) + "\"");
        Optional<Doctor> doctor = doctorRepository.findByEmail(userEmail);
        try (OutputStream out = response.getOutputStream()) {
            reportService.streamReportDecryptedTo(reportId, doctor.get().getId(), true, out);
        }
    }

    @GetMapping("/schedules")
    public ResponseEntity<List<DoctorScheduleDTO>> schedules(@RequestHeader(value = "X-User-Email") String userEmail) {
        return ResponseEntity.ok(scheduleService.getSchedulesForDoctor(userEmail));
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingResponseDTO>> getBookings(@RequestHeader(value = "X-User-Email") String userEmail) {
        List<BookingResponseDTO> resp = doctorService.getBookingsForDoctor(userEmail);
        return ResponseEntity.ok(resp);
    }
}
