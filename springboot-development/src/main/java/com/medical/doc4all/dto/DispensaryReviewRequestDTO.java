package com.medical.doc4all.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispensaryReviewRequestDTO {
    private Long bookingId;
    private int cleanliness;
    private int staffSupport;
    private int accessibility;
    private String comment;
    private boolean anonymous = false;
    // getters/setters
}

