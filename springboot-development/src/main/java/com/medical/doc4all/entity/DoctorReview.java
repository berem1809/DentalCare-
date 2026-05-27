package com.medical.doc4all.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "doctor_review", indexes = {
        @Index(name = "idx_doctor_review_doctor", columnList = "doctor_id"),
        @Index(name = "idx_doctor_review_booking", columnList = "booking_id")
})
public class DoctorReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Patient patient;

    @ManyToOne(optional = false)
    private Doctor doctor;

    @ManyToOne(optional = false)
    private Dispensary dispensary;

    @ManyToOne(optional = false)
    private Booking booking;

    private int rating; // 1-5
    @Column(length = 2000)
    private String comment;

    private boolean anonymous = false;

    private LocalDateTime createdAt = LocalDateTime.now();
}

