package com.medical.doc4all.dto;

import com.medical.doc4all.entity.DispensaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DispensaryProfileResponse {
    private String email;
    private String phone;
    private String address;
    private String firstName;
    private String lastName;

    private String name;
    private Double longitude;
    private Double latitude;
    private String description;
    private String website;
    private DispensaryType type;
    private LocalDateTime updatedAt;

    // getters and setters
}

