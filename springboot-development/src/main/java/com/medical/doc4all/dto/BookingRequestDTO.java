package com.medical.doc4all.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestDTO {
    private Long patientId;      // set by controller from path param
    private Long scheduleId;     // the DoctorSchedule id
    private LocalDate appointmentDate;
    private LocalTime slotStartTime;

    private String paymentMethodNonce;
    // getters / setters
}

