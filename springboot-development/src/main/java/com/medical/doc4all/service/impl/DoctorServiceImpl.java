package com.medical.doc4all.service.impl;

import com.medical.doc4all.dto.*;
import com.medical.doc4all.entity.*;
import com.medical.doc4all.enums.Responses;
import com.medical.doc4all.exception.ResourceNotFoundException;
import com.medical.doc4all.repository.BookingRepository;
import com.medical.doc4all.repository.DispensaryRepository;
import com.medical.doc4all.repository.DoctorRepository;
import com.medical.doc4all.repository.DoctorScheduleRepository;
import com.medical.doc4all.service.DoctorService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DoctorServiceImpl implements DoctorService {

    private DoctorRepository doctorRepository;
    private DispensaryRepository dispensaryRepository;

    private DoctorScheduleRepository scheduleRepository;

    private BookingRepository bookingRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository, DispensaryRepository dispensaryRepository, DoctorScheduleRepository doctorScheduleRepository, BookingRepository bookingRepository) {
        this.dispensaryRepository = dispensaryRepository;
        this.doctorRepository = doctorRepository;
        this.scheduleRepository = doctorScheduleRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Responses createDoctor(String name, String email) {
        // Here you would typically interact with a repository to save the doctor details
        Optional<Doctor> doc = doctorRepository.findByEmail(email);
        if(doc.isPresent()) {
            log.error("Doctor with email {} already exists", email);
            return Responses.AlreadyExists;
        }

        Doctor doctor = new Doctor().builder()
                .name(name)
                .email(email)
                .build();
        // Save the doctor entity to the repository
        doctorRepository.save(doctor);
        // For now, we will just return a success message
        log.info("Doctor created successfully with name: {} and email: {}", name, email);
        return Responses.Success;
    }

    @Override
    public Responses checkProfileStatus(String email) {
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Dispensary not found with email: " + email));

        // Check if all required fields are filled
        boolean isComplete = doctor.getName() != null && !doctor.getName().isEmpty()
                && doctor.getEmail() != null && !doctor.getEmail().isEmpty()
                && doctor.getType() != null
                && doctor.getEducation() != null && !doctor.getEducation().isEmpty()
                && doctor.getSpecialities() != null && !doctor.getSpecialities().isEmpty();

        System.out.println("Name valid: " + (doctor.getName() != null && !doctor.getName().isEmpty()));
        System.out.println("Email valid: " + (doctor.getEmail() != null && !doctor.getEmail().isEmpty()));
        System.out.println("Type valid: " + (doctor.getType() != null));
        System.out.println("Education valid: " + (doctor.getEducation() != null && !doctor.getEducation().isEmpty()));
        System.out.println("Specialities valid: " + (doctor.getSpecialities() != null && !doctor.getSpecialities().isEmpty()));
        // Add other required fields as necessary
        return isComplete ? Responses.Success : Responses.Failure;
    }
    @Override
    public void updateDoctorProfile(UpdateDoctor update,String email){
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Doctor not found with email: " + email));

        // Update fields if they are provided in the request
        if (update.getType() != null) {
            doctor.setType(update.getType());
        }
        if (update.getEducation() != null) {
            doctor.setEducation(update.getEducation());
        }
        if (update.getSpecialities() != null) {
            doctor.setSpecialities(update.getSpecialities());
        }
        if (update.getExperience() != null) {
            doctor.setExperience(update.getExperience());
        }
        // Save the updated dispensary entity
        doctorRepository.save(doctor);
    }

    @Override
    @Transactional
    public List<DoctorResponseDTO> getDoctorsByEmail(String email) {
        Dispensary dispensary = dispensaryRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Dispensary not found with the email"));

        // get the type of the dispensary
        DispensaryType type = dispensary.getType();
        if (type == null) {
            // decide how you want to treat dispensaries without type
            return List.of();
        }

        List<Doctor> doctors = doctorRepository.findByType(type);

        return doctors.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private DoctorResponseDTO toResponseDto(Doctor d) {
        DoctorResponseDTO dto = new DoctorResponseDTO();
        dto.setId(d.getId());
        dto.setName(d.getName());
        dto.setEmail(d.getEmail());
        dto.setExperience(d.getExperience());
        dto.setSpecialities(d.getSpecialities());
        dto.setEducation(d.getEducation());
        return dto;
    }

    @Override
    public List<BookingResponseDTO> getBookingsForDoctor(String email) {
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Dispensary not found with email: " + email));

        List<DoctorSchedule> schedules = scheduleRepository.findByDoctorId(doctor.getId());

        List<Booking> bookings = schedules.stream()
                .flatMap(schedule -> bookingRepository.findBySchedule(schedule).stream())
                .collect(Collectors.toList());

        return bookings.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private BookingResponseDTO toResponse(Booking booking) {
        DoctorSchedule schedule = booking.getSchedule();
        Dispensary d = schedule.getDispensary();
        Doctor doctor = (schedule != null ? schedule.getDoctor() : null);
        Patient patient = booking.getPatient();

        DispensaryDetailsDTO dispensaryDetailsDTO = null;
        if (d != null) {
            dispensaryDetailsDTO = DispensaryDetailsDTO.builder()
                    .id(d.getId())
                    .name(d.getName())
                    .email(d.getEmail())
                    .type(d.getType())
                    .address(d.getAddress())
                    .contactNumber(d.getContactNumber())
                    .website(d.getWebsite())
                    .latitude(d.getLatitude())
                    .longitude(d.getLongitude())
                    .build();
        }

        return BookingResponseDTO.builder()
                .bookingId(booking.getId())
                .patientName(patient != null ? patient.getName() : null)
                .doctorName(doctor != null ? doctor.getName() : null)
                .dispensaryName(schedule != null && schedule.getDispensary() != null ? schedule.getDispensary().getName() : null)
                .appointmentDate(booking.getAppointmentDate())
                .appointmentTime(booking.getSlotStartTime())
                .status(booking.getStatus() != null ? booking.getStatus().name() : null)
                .paymentStatus(booking.getPaymentStatus() != null ? booking.getPaymentStatus().name() : null)
                .transactionId(booking.getTransactionId())
                .refundTransactionId(booking.getRefundTransactionId())
                .dispensary(dispensaryDetailsDTO)
                .build();
    }
}
