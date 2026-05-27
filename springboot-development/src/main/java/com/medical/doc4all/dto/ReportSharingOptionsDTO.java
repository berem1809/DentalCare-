package com.medical.doc4all.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Builder
public class ReportSharingOptionsDTO {
    private List<DoctorDTO> alreadyShared;
    private List<DoctorDTO> availableToShare;

    public ReportSharingOptionsDTO(List<DoctorDTO> alreadyShared, List<DoctorDTO> availableToShare) {
        this.alreadyShared = alreadyShared;
        this.availableToShare = availableToShare;
    }
}
