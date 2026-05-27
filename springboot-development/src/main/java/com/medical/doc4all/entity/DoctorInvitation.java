package com.medical.doc4all.entity;

import com.medical.doc4all.enums.InvitationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DoctorInvitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Dispensary dispensary;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    private DayOfWeek day;   // recurring day of week

    private LocalTime startTime;
    private LocalTime endTime;
    private int perPatientMinutes;  // 15, 30, etc.
    private Double offerRate; // amount offered to doctor
    private Double perPatientRate; // amount charged to patient

    @Enumerated(EnumType.STRING)
    private InvitationStatus status; // PENDING, ACCEPTED, REJECTED

}


