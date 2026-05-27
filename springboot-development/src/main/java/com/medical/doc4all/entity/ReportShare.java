package com.medical.doc4all.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// ReportShare.java
@Entity
@Table(name = "report_share")
@Getter
@Setter
@NoArgsConstructor
public class ReportShare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private Report report;

    @ManyToOne
    private Doctor doctor;

    private LocalDateTime sharedAt;

    private LocalDateTime revokedAt;

    // For soft-delete control
    private boolean active;
}


