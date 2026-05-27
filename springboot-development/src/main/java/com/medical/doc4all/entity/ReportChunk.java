package com.medical.doc4all.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// ReportChunk.java (optional)
@Entity
@Table(name = "report_chunk")
@Getter
@Setter
@NoArgsConstructor
public class ReportChunk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Report report;

    private int chunkIndex; // 0,1,2...
    private String storagePath; // path to encrypted chunk file relative to storageRoot
    private Long sizeBytes;
}

