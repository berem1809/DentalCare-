package com.medical.doc4all.dto;

import com.medical.doc4all.entity.DispensaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDoctor {
    private String education;
    private String experience;
    private String specialities;
    private DispensaryType type;
}
