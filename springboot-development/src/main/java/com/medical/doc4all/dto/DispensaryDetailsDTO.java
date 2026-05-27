package com.medical.doc4all.dto;

import com.medical.doc4all.entity.DispensaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispensaryDetailsDTO {
    private Long id;
    private String name;
    private String email;
    private String address;
    private Double longitude;
    private Double latitude;
    private Float rating;
    private String description;
    private String contactNumber;
    private String website;
    private DispensaryType type;

    private List<DoctorScheduleResponseDTO> approvedDoctors; // schedules with doctor info

    // getters / setters
}
