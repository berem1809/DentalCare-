package com.medical.doc4all.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispensaryProfileRequest {
    private String address;
    private String contactNumber;
    private String type;
    private String description;
    private double latitude;
    private double longitude;
    private String website;
    private String userEmail; // This will be populated from the header
}
