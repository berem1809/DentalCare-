package com.medical.doc4all.dto;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// UploadReportResponseDTO
public class UploadReportResponseDTO {
    private Long id;
    private String originalFileName;
    private Long sizeBytes;
    private LocalDateTime createdAt;
}
