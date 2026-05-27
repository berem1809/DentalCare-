package com.medical.doc4all.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
    private int id;
    private String name;
    private String email;
    private String type;
    private String experience;
    private String specialities;
    private String education;
}

