package com.medical.doc4all.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ShareResponseDTO {
    private Long reportId;
    private List<Integer> sharedDoctorIds;
}
