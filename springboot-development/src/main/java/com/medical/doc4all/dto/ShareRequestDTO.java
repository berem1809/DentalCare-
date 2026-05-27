package com.medical.doc4all.dto;

import lombok.*;

import java.util.List;

// ShareRequestDTO
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ShareRequestDTO {
    private Integer doctorId; // int because Doctor.id is int in your model
}
