package com.medical.doc4all.service.impl;

import com.medical.doc4all.config.CryptoUtil;
import com.medical.doc4all.dto.*;
import com.medical.doc4all.entity.Doctor;
import com.medical.doc4all.entity.Patient;
import com.medical.doc4all.entity.Report;
import com.medical.doc4all.entity.ReportShare;
import com.medical.doc4all.enums.BookingStatus;
import com.medical.doc4all.exception.BadRequestException;
import com.medical.doc4all.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Value("${reports.storage.root:/var/app/reports}")
    private String storageRoot;

    private final ReportRepository reportRepo;
    private final ReportShareRepository shareRepo;
    private final BookingRepository bookingRepo; // to validate completed bookings
    private final DoctorRepository doctorRepo;
    private final PatientRepository patientRepo;
    private final CryptoUtil crypto;

    @Autowired
    public ReportService(ReportRepository reportRepo,
                         ReportShareRepository shareRepo,
                         BookingRepository bookingRepo,
                         DoctorRepository doctorRepo,
                         PatientRepository patientRepo,
                         @Value("${reports.masterKeyBase64}") String masterKeyBase64) {
        this.reportRepo = reportRepo;
        this.shareRepo = shareRepo;
        this.bookingRepo = bookingRepo;
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
        this.crypto = new CryptoUtil(Base64.getDecoder().decode(masterKeyBase64));
    }

    @Transactional
    public UploadReportResponseDTO uploadReport(String UserEmail, MultipartFile file) throws IOException, GeneralSecurityException {
        Patient patient = patientRepo.findByEmail(UserEmail).orElseThrow(() -> new BadRequestException("Invalid patient"));

        // create storage key
        String storageKey = UUID.randomUUID().toString();
        byte[] iv = crypto.generateIv();
        Cipher cipher = crypto.initEncryptCipher(iv);

        // target path
        Path outPath = Paths.get(storageRoot, storageKey + ".enc");
        Files.createDirectories(outPath.getParent());
        try (InputStream in = file.getInputStream();
             OutputStream fout = Files.newOutputStream(outPath, StandardOpenOption.CREATE_NEW);
             CipherOutputStream cos = new CipherOutputStream(fout, cipher)) {

            byte[] buffer = new byte[8192];
            int r;
            while ((r = in.read(buffer)) != -1) {
                cos.write(buffer, 0, r);
            }
        }

        // compute checksum (optionally compute before encryption by reading file bytes; easiest: compute from MultipartFile.getInputStream() first)
        String checksum = null;
        try (InputStream is2 = file.getInputStream()) {
            checksum = CryptoUtil.sha256Hex(is2);
        } catch (NoSuchAlgorithmException e) {
            // ignore or log
        }

        Report r = new Report();
        r.setPatient(patient);
        r.setOriginalFileName(file.getOriginalFilename());
        r.setContentType(file.getContentType());
        r.setSizeBytes(file.getSize());
        r.setStorageKey(storageKey);
        r.setIvBase64(Base64.getEncoder().encodeToString(iv));
        r.setChecksum(checksum);
        r.setCreatedAt(LocalDateTime.now());

        Report saved = reportRepo.save(r);

        UploadReportResponseDTO out = new UploadReportResponseDTO();
        out.setId(saved.getId());
        out.setOriginalFileName(saved.getOriginalFileName());
        out.setSizeBytes(saved.getSizeBytes());
        out.setCreatedAt(saved.getCreatedAt());
        return out;
    }

    @Transactional
    public List<ReportMetadataDTO> listReportsForPatient(String userEmail) {
        // ensure caller is patient or admin (controller should check)
        Patient patient = patientRepo.findByEmail(userEmail).orElseThrow(() -> new BadRequestException("Invalid patient"));
        List<Report> list = reportRepo.findByPatientId(patient.getId());
        return list.stream().map(report -> {
            ReportMetadataDTO dto = new ReportMetadataDTO();
            dto.setId(report.getId());
            dto.setOriginalFileName(report.getOriginalFileName());
            dto.setContentType(report.getContentType());
            dto.setSizeBytes(report.getSizeBytes());
            dto.setCreatedAt(report.getCreatedAt());
            dto.setSharedWithMe(false);
            return dto;
        }).collect(Collectors.toList());
    }

    public ReportShareDTO shareReport(String email, Long reportId, Integer doctorId) {
        Patient patient = patientRepo.findByEmail(email).orElseThrow(()->new BadRequestException("No account for the email")); // fetch
        Report report = reportRepo.findById(reportId).orElseThrow(()-> new RuntimeException("No such report")); // fetch
        Doctor doctor = doctorRepo.findById(doctorId).orElseThrow(()-> new RuntimeException("No such doctor to share")); // fetch

        // Check booking completed
        boolean hasCompleted = bookingRepo.existsByPatientAndSchedule_DoctorAndStatus
        (patient, doctor, BookingStatus.COMPLETED);
        if (!hasCompleted) {
            throw new IllegalStateException("No completed booking with this doctor");
        }


        // Check if already active
        Optional<ReportShare> existing =
                shareRepo.findByPatientAndReportAndDoctorAndActiveTrue(patient, report, doctor);
        if (existing.isPresent()) {
            throw new IllegalStateException("Report already shared with this doctor");
        }

        ReportShare share = new ReportShare();
        share.setPatient(patient);
        share.setReport(report);
        share.setDoctor(doctor);
        share.setSharedAt(LocalDateTime.now());
        share.setActive(true);

        return toDTO(shareRepo.save(share));
    }

    private ReportShareDTO toDTO(ReportShare share) {
        ReportShareDTO dto = new ReportShareDTO();
        dto.setId(share.getId());
        dto.setReportId(share.getReport().getId());
        dto.setDoctorId((long) share.getDoctor().getId());
        dto.setActive(share.isActive());
        return dto;
    }

    @Transactional
    // Revoke report access
    public void revokeShare(Long reportId,String email, Long doctorId) {
        Patient patient = patientRepo.findByEmail(email).orElseThrow(()->new BadRequestException("No account for the email")); // fetch
        Report report = reportRepo.findById(reportId).orElseThrow(()-> new RuntimeException("No such report")); // fetch
        Doctor doctor = doctorRepo.findById(Math.toIntExact(doctorId)).orElseThrow(()-> new RuntimeException("No such doctor to share")); // fetch
        ReportShare share = shareRepo.findByPatientAndReportAndDoctor(patient, report, doctor)
                .orElseThrow(() -> new IllegalArgumentException("Share not found"));

        share.setActive(false);
        share.setRevokedAt(LocalDateTime.now());
        shareRepo.save(share);
    }

    public ReportSharingOptionsDTO getSharingOptions(String email, Long reportId) {
        Patient patient = patientRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("Invalid patient"));
        Report report = reportRepo.findById(reportId).orElseThrow(() -> new BadRequestException("Invalid report"));

        // All doctors with completed bookings
        List<Doctor> eligibleDoctors = bookingRepo
                .findCompletedDoctorsByPatient(patient);

        // All shares (past + present)
        List<ReportShare> shares = shareRepo.findByPatientAndReport(patient, report);

        List<DoctorDTO> alreadyShared = shares.stream()
                .filter(ReportShare::isActive)
                .map(share -> toDoctorDTO(share.getDoctor()))
                .toList();

        List<DoctorDTO> availableToShare = eligibleDoctors.stream()
                .filter(doc -> shares.stream().noneMatch(s -> s.isActive() && s.getDoctor().equals(doc)))
                .map(this::toDoctorDTO)
                .toList();

        return new ReportSharingOptionsDTO(alreadyShared, availableToShare);
    }

    private DoctorDTO toDoctorDTO(Doctor doctor) {
        DoctorDTO dto = new DoctorDTO();
        dto.setId( doctor.getId());
        dto.setName(doctor.getName());
        dto.setEmail(doctor.getEmail());
        return dto;
    }

    @Transactional
    public List<ReportMetadataDTO> listReportsSharedWithDoctor(String email) {
        Doctor doctor = doctorRepo.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Invalid doctor"));

        // Fetch only active shares
        List<ReportShare> shares = shareRepo.findByDoctorIdAndActiveTrue(doctor.getId());

        return shares.stream().map(s -> {
            Report r = s.getReport();
            ReportMetadataDTO dto = new ReportMetadataDTO();
            dto.setId(r.getId());
            dto.setOriginalFileName(r.getOriginalFileName());
            dto.setContentType(r.getContentType());
            dto.setSizeBytes(r.getSizeBytes());
            dto.setCreatedAt(r.getCreatedAt());
            dto.setSharedWithMe(true);
            dto.setPatientName(r.getPatient().getName());
            return dto;
        }).collect(Collectors.toList());
    }


    // Secure streaming: decrypt on the fly into provided OutputStream (e.g., HttpServletResponse.getOutputStream())
    @Transactional
    public void streamReportDecryptedTo(
            Long reportId,
            Integer requesterPatientIdOrDoctorId,
            boolean isDoctor,
            OutputStream out) throws Exception {

        Report rep = reportRepo.findById(reportId)
                .orElseThrow(() -> new BadRequestException("No such report"));

        // authorize: patient owner or shared doctor
        if (!isDoctor) {
            if (!Objects.equals(rep.getPatient().getId(), requesterPatientIdOrDoctorId)) {
                throw new BadRequestException("Not your report");
            }
        } else {
            boolean active = shareRepo.existsByReportIdAndDoctorIdAndActiveTrue(
                    reportId, requesterPatientIdOrDoctorId);
            if (!active) throw new BadRequestException("Not shared with you");
        }

        String storageKey = rep.getStorageKey();
        Path path = Paths.get(storageRoot, storageKey + ".enc");
        byte[] iv = Base64.getDecoder().decode(rep.getIvBase64());
        Cipher cipher = crypto.initDecryptCipher(iv);

        try (InputStream fis = Files.newInputStream(path, StandardOpenOption.READ);
             CipherInputStream cis = new CipherInputStream(fis, cipher)) {

            byte[] buf = new byte[8192];
            int r;
            while ((r = cis.read(buf)) != -1) {
                out.write(buf, 0, r);
            }
            out.flush(); // don't close `out`, caller (controller) owns it
        }
    }

}

