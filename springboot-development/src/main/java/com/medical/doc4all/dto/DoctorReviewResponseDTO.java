package com.medical.doc4all.dto;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorReviewResponseDTO {
    private Long id;
    private Integer patientId; // null if anonymous
    private String patientName; // null if anonymous
    private Long doctorId;
    private Long dispensaryId;
    private Long bookingId;
    private int rating;
    private String comment;
    private boolean anonymous;
    private LocalDateTime createdAt;
    // getters/setters
}
