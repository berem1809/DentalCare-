package com.medical.doc4all.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// Report.java
@Entity
@Table(name = "report")
@Getter
@Setter
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Patient patient;

    private String originalFileName;
    private String contentType;
    private Long sizeBytes;

    // storage reference (UUID file base name), e.g. "a1b2c3d4-..."; actual file(s) stored under storageRoot/<storageKey>.enc or parts.
    @Column(nullable = false, unique = true)
    private String storageKey;

    // AES-GCM IV (base64) used when encrypting this file
    private String ivBase64;

    // optional checksum of original file (sha256 hex)
    private String checksum;

    private LocalDateTime createdAt = LocalDateTime.now();
}

