package com.medical.doc4all.repository;

import com.medical.doc4all.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByPatientId(Integer patientId);

    Report getReportById(Long reportId);
}
