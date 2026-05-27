package com.medical.doc4all.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorInvitationResponseDTO {
    private Long id;
    private Long dispensaryId;
    private Integer doctorId;
    private java.time.DayOfWeek day;
    private java.time.LocalTime startTime;
    private java.time.LocalTime endTime;
    private int perPatientMinutes;
    private Double offerRate;
    private String status;
    // getters / setters
}
