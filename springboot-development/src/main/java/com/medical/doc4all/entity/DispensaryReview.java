package com.medical.doc4all.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "dispensary_review", indexes = {
        @Index(name = "idx_disp_review_dispensary", columnList = "dispensary_id"),
        @Index(name = "idx_disp_review_booking", columnList = "booking_id")
})
public class DispensaryReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Patient patient;

    @ManyToOne(optional = false)
    private Dispensary dispensary;

    @ManyToOne(optional = false)
    private Booking booking;

    private int cleanliness;    // 1-5
    private int staffSupport;   // 1-5
    private int accessibility;  // 1-5

    @Transient
    public double getOverallRating() {
        return (cleanliness + staffSupport + accessibility) / 3.0;
    }

    @Column(length = 2000)
    private String comment;

    private boolean anonymous = false;

    private LocalDateTime createdAt = LocalDateTime.now();
}

