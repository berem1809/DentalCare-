package com.medical.doc4all.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorReviewRequestDTO {
    private Long bookingId;
    private int rating; // 1-5
    private String comment;
    private boolean anonymous = false;
    // getters/setters
}
