package com.medical.doc4all.controller;

import com.medical.doc4all.dto.*;
import com.medical.doc4all.enums.Responses;
import com.medical.doc4all.service.BookingService;
import com.medical.doc4all.service.PatientService;
import com.medical.doc4all.service.impl.ReportService;
import com.medical.doc4all.service.ReviewService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.medical.doc4all.dto.ReviewableDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@NoArgsConstructor
@Slf4j
public class PatientController {
    @Autowired
    private PatientService patientService;

    @Autowired
    private BookingService bookingService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ReportService reportService;
    @PostMapping
    public String createPatient(@RequestBody DispensaryCreateRequest request) {
        patientService.createPatient(request.getEmail(), request.getName());
        return "Patient created successfully";
    }


    @PostMapping("/update-location")
    public ResponseEntity<APIResponse> updateLocation(@RequestBody updateLocationDTO request, @RequestHeader(value = "X-User-Email", required = false) String userEmail){
        Responses r = patientService.updateLocation(request, userEmail);
        if (r.equals(Responses.Success)) {
            return ResponseEntity.ok(new APIResponse(true,200, "Location updated"));
        } else {
            return ResponseEntity.status(400).body(new APIResponse(false,400, "Failed to update location"));
        }
    }

    @GetMapping("/location")
    public ResponseEntity<updateLocationDTO> updateLocation( @RequestHeader(value = "X-User-Email", required = false) String userEmail){
        updateLocationDTO get = patientService.getLocation(userEmail);
        return ResponseEntity.ok(get);
    }

    @PostMapping("/bookings")
    public ResponseEntity<BookingResponseDTO> book(@RequestHeader(value = "X-User-Email", required = false) String userEmail,
                                                   @RequestBody BookingRequestDTO req) {// aligns patient id
        BookingResponseDTO resp = bookingService.createBooking(req, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping("/bookings")
    public ResponseEntity<Iterable<BookingResponseDTO>> getBookings(@RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        Iterable<BookingResponseDTO> resp = bookingService.getBookings(userEmail);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable("id") Long bookingId,
                                                            @RequestParam(name="refund", defaultValue = "true") boolean refund) {
        BookingResponseDTO resp = bookingService.cancelBooking(bookingId, refund);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{bookingId}/is-reviewable")
    public ResponseEntity<ReviewableDTO> isBookingReviewable(@PathVariable Long bookingId,
                                                                  @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        ReviewableDTO resp = reviewService.isBookingReviewable(bookingId, userEmail);
        return ResponseEntity.ok(resp);
    }

    //Review controllers

    @PostMapping("/reviews/doctor")
    public ResponseEntity<DoctorReviewResponseDTO> createDoctorReview(@RequestHeader(value = "X-User-Email", required = false) String userEmail,
                                                                      @RequestBody DoctorReviewRequestDTO req) {
        DoctorReviewResponseDTO resp = reviewService.createDoctorReview(req, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    // POST /api/bookings/{bookingId}/reviews/dispensary
    @PostMapping("/reviews/dispensary")
    public ResponseEntity<DispensaryReviewResponseDTO> createDispensaryReview(@RequestBody DispensaryReviewRequestDTO req, @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        DispensaryReviewResponseDTO resp = reviewService.createDispensaryReview(req, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    //report

    @PostMapping("/reports")
    public ResponseEntity<UploadReportResponseDTO> upload(@RequestHeader(value = "X-User-Email", required = false) String userEmail, @RequestPart("file") MultipartFile file) throws Exception {
        UploadReportResponseDTO dto = reportService.uploadReport(userEmail,file);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/reports")
    public ResponseEntity<List<ReportMetadataDTO>> list(@RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        List<ReportMetadataDTO> list = reportService.listReportsForPatient(userEmail);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/{reportId}/share")
    public ResponseEntity<ReportShareDTO> share(@RequestHeader(value = "X-User-Email", required = false) String userEmail,
                                                  @PathVariable Long reportId,
                                                  @RequestBody ShareRequestDTO req) {
        ReportShareDTO out = reportService.shareReport(userEmail, reportId, req.getDoctorId());
        return ResponseEntity.ok(out);
    }

    @PostMapping("/{reportId}/revoke")
    public ResponseEntity<Void> revoke(@RequestHeader(value = "X-User-Email", required = false) String userEmail,
                                       @PathVariable Long reportId,
                                       @RequestBody ShareRequestDTO req
                                       ) {
        reportService.revokeShare(reportId, userEmail, Long.valueOf(req.getDoctorId()));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{reportId}/sharing-options")
    public ResponseEntity<ReportSharingOptionsDTO> getSharingOptions(
            @PathVariable Long reportId,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        return ResponseEntity.ok(reportService.getSharingOptions(userEmail, reportId));
    }

}
