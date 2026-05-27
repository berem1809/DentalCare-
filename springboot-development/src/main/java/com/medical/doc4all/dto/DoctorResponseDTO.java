package com.medical.doc4all.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponseDTO {
    private Integer id;
    private String name;
    private String email;
    private String experience;
    private String specialities;
    private String education;
    // add other fields you want to expose (no sensitive data)

    // getters / setters
}