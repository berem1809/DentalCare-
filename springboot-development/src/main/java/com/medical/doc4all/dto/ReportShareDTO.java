package com.medical.doc4all.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportShareDTO {
    private Long id;
    private Long doctorId;
    private Long reportId;
    private boolean active;
}
