package com.medical.doc4all.dto;

import com.medical.doc4all.entity.Doctor;
import com.medical.doc4all.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {
    private Long bookingId;
    private String patientName;
    private String doctorName;
    private String dispensaryName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String status;
    private String paymentStatus;
    private String transactionId;
    private String refundTransactionId;

    private DoctorDTO doctor;
    private DispensaryDetailsDTO dispensary;
}

