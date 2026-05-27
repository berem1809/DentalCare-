package com.medical.doc4all.entity;

import com.medical.doc4all.enums.BookingStatus;
import com.medical.doc4all.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private DoctorSchedule schedule;

    private LocalDate appointmentDate; // actual date chosen
    private LocalTime slotStartTime;   // computed based on perPatientMinutes
    private LocalTime slotEndTime;

    private Double price;
    private String transactionId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    // New fields:
    private String refundTransactionId;
    private LocalDateTime refundedAt;

    @Enumerated(EnumType.STRING)
    private BookingStatus status; // PENDING, CONFIRMED, CANCELLED
}

