package com.medical.doc4all.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorScheduleResponseDTO {
    private Long scheduleId;
    private Integer doctorId;
    private String doctorName;
    private String doctorEmail;
    private String specialities;
    private String education;
    private DayOfWeek day;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer perPatientMinutes;
    private Double rate; // agreed rate
    // getters / setters
}

