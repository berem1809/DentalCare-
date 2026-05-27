package com.medical.doc4all.dto;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ReportMetadataDTO {
    private Long id;
    private String originalFileName;
    private String contentType;
    private Long sizeBytes;
    private LocalDateTime createdAt;
    private boolean sharedWithMe;

    private String patientName;// for doctor-view
}
