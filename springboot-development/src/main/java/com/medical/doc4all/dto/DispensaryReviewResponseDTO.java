package com.medical.doc4all.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispensaryReviewResponseDTO {
    private Long id;
    private Integer patientId; // null when anonymous
    private String patientName; // null when anonymous
    private Long dispensaryId;
    private Long bookingId;
    private int cleanliness;
    private int staffSupport;
    private int accessibility;
    private double overallRating;
    private String comment;
    private boolean anonymous;
    private LocalDateTime createdAt;
    // getters/setters
}
