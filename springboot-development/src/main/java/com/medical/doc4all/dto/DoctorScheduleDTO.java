package com.medical.doc4all.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorScheduleDTO {
    private Long id;
    private Integer doctorId;
    private Long dispensaryId;
    private java.time.DayOfWeek day;
    private java.time.LocalTime startTime;
    private java.time.LocalTime endTime;
    private int perPatientMinutes;
    private Double agreedRate;
    private String status; // e.g., "ACCEPTED", "PENDING"
    // getters / setters
}
