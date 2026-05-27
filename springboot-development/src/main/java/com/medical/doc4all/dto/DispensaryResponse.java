package com.medical.doc4all.dto;

import com.medical.doc4all.entity.DispensaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispensaryResponse {

    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String type;
    private Double longitude;
    private Double latitude;
    private String address;
    private String description;
    private Float rating;
    private Boolean isValid;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer review_count;
}
