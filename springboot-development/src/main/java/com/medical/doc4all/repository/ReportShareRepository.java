package com.medical.doc4all.repository;

import com.medical.doc4all.entity.Doctor;
import com.medical.doc4all.entity.Patient;
import com.medical.doc4all.entity.Report;
import com.medical.doc4all.entity.ReportShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ReportShareRepository extends JpaRepository<ReportShare, Long> {
    List<ReportShare> findByPatientAndReportAndActiveTrue(Patient patient, Report report);

    Optional<ReportShare> findByPatientAndReportAndDoctorAndActiveTrue(
            Patient patient, Report report, Doctor doctor);

    List<ReportShare> findByPatientAndReport(Patient patient, Report report);

    List<ReportShare> findByDoctorIdAndActiveTrue(int id);

    boolean existsByReportIdAndDoctorIdAndActiveTrue(Long reportId, Integer requesterPatientIdOrDoctorId);

    Optional<ReportShare> findByPatientAndReportAndDoctor(Patient patient, Report report, Doctor doctor);
}
