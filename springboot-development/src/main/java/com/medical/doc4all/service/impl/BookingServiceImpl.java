package com.medical.doc4all.service.impl;

import com.medical.doc4all.dto.BookingRequestDTO;
import com.medical.doc4all.dto.BookingResponseDTO;
import com.medical.doc4all.dto.DispensaryDetailsDTO;
import com.medical.doc4all.dto.DoctorDTO;
import com.medical.doc4all.entity.*;
import com.medical.doc4all.enums.BookingStatus;
import com.medical.doc4all.enums.PaymentStatus;
import com.medical.doc4all.exception.BadRequestException;
import com.medical.doc4all.exception.ConflictException;
import com.medical.doc4all.exception.PaymentException;
import com.medical.doc4all.exception.ResourceNotFoundException;
import com.medical.doc4all.repository.BookingRepository;
import com.medical.doc4all.repository.DoctorScheduleRepository;
import com.medical.doc4all.repository.PatientRepository;
import com.medical.doc4all.service.BookingService;
import com.medical.doc4all.service.PaymentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepo;
    private final DoctorScheduleRepository scheduleRepo;
    private final PatientRepository patientRepo;
    private final PaymentService paymentService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepo,
                              DoctorScheduleRepository scheduleRepo,
                              PatientRepository patientRepo,
                              PaymentService paymentService) {
        this.bookingRepo = bookingRepo;
        this.scheduleRepo = scheduleRepo;
        this.patientRepo = patientRepo;
        this.paymentService = paymentService;
    }

    @Override
    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO dto, String email) {
        // 1. load and validate domain objects
        Patient p = patientRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + email));

        dto.setPatientId((long) p.getId());
        DoctorSchedule schedule = scheduleRepo.findById(dto.getScheduleId())
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found: " + dto.getScheduleId()));

        if (schedule.getDay() != dto.getAppointmentDate().getDayOfWeek()) {
            throw new BadRequestException("Appointment date does not fall on schedule day: expected " + schedule.getDay());
        }

        LocalTime slotStart = dto.getSlotStartTime();
        LocalTime slotEnd = slotStart.plusMinutes(schedule.getPerPatientMinutes());

        if (slotStart.isBefore(schedule.getStartTime()) || slotEnd.isAfter(schedule.getEndTime()) || slotEnd.isBefore(slotStart)) {
            throw new BadRequestException("Slot outside of schedule bounds");
        }

        long minutesFromStart = Duration.between(schedule.getStartTime(), slotStart).toMinutes();
        if (minutesFromStart < 0 || (minutesFromStart % schedule.getPerPatientMinutes()) != 0) {
            throw new BadRequestException("Slot not aligned to schedule interval");
        }

        // Quick existence check
        boolean exists = bookingRepo.existsByScheduleIdAndAppointmentDateAndSlotStartTime(
                schedule.getId(), dto.getAppointmentDate(), slotStart);
        if (exists) {
            throw new ConflictException("Slot already booked");
        }

        // 2. compute price (from schedule/agreedRate)
        double amount = schedule.getAgreedRate();

        if (amount <= 0) {
            throw new BadRequestException("Invalid price for the schedule");
        }

        // 3. Charge via Braintree
        String transactionId;
        try {
            transactionId = paymentService.charge(amount, dto.getPaymentMethodNonce());
        } catch (PaymentException ex) {
            throw new BadRequestException("Payment failed: " + ex.getMessage());
        }

        // 4. Try to persist booking. If DB unique constraint triggers or save fails, refund the transaction.
        Booking booking = new Booking();
        booking.setPatient(p);
        booking.setSchedule(schedule);
        booking.setAppointmentDate(dto.getAppointmentDate());
        booking.setSlotStartTime(slotStart);
        booking.setSlotEndTime(slotEnd);
        booking.setPrice(amount);
        booking.setTransactionId(transactionId);
        booking.setPaymentStatus(PaymentStatus.PAID);
        booking.setStatus(BookingStatus.CONFIRMED);

        try {
            booking = bookingRepo.save(booking);
        } catch (DataIntegrityViolationException ex) {
            // concurrent booking happened: refund and convert to 409
            try {
                paymentService.refund(transactionId);
            } catch (PaymentException refundEx) {
                // log critical: refund failed (you may want to alert ops)
                // but still throw a conflict so frontend knows booking not created
                // rethrow or wrap
                throw new ConflictException("Slot already booked; attempted refund failed: " + refundEx.getMessage());
            }
            throw new ConflictException("Slot already booked (concurrent). Payment refunded.");
        }

        // 5. Success: return response DTO
        BookingResponseDTO resp = new BookingResponseDTO();
        resp.setBookingId(booking.getId());
        resp.setStatus(booking.getStatus().name());
        resp.setTransactionId(transactionId);
        return resp;
    }

    @Override
    @Transactional
    public List<BookingResponseDTO> getBookings(String email) {
        Patient p = patientRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + email));

        List<Booking> bookings = bookingRepo.findByPatient(p);
        return bookings.stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public BookingResponseDTO cancelBooking(Long bookingId, boolean refundRequested) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));

        // If already cancelled, return idempotent success
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return toResponse(booking);
        }

        // Business rule: only cancel if in allowed timeframe. (Optional)
         if (booking.getAppointmentDate().isBefore(LocalDate.now())
         || booking.getAppointmentDate().equals(LocalDate.now())){
             throw new BadRequestException("Booking can't be cancelled on or after the appointment date");
         }

        // If refund requested and payment exists
        if (refundRequested && booking.getTransactionId() != null && booking.getPaymentStatus() == PaymentStatus.PAID) {
            String refundTxnId = null;
            try {
                refundTxnId = paymentService.refund(booking.getTransactionId());
                booking.setPaymentStatus(PaymentStatus.REFUNDED);
                booking.setRefundTransactionId(refundTxnId);
                booking.setRefundedAt(LocalDateTime.now());
            } catch (PaymentException ex) {
                // Refund failed — choose policy:
                // Option A: mark booking CANCELLED but paymentStatus FAILED and throw Conflict so client knows.
                // Option B: keep booking as CANCELLED but paymentStatus remains PAID and schedule manual refund.
                // Here we choose to abort and inform client (so they can retry later).
                throw new PaymentException("Refund failed: " + ex.getMessage());
            }
        }

        // Mark booking cancelled and save
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setPaymentStatus(PaymentStatus.REFUNDED);
        bookingRepo.save(booking);

        return toResponse(booking);
    }

    private BookingResponseDTO toResponse(Booking booking) {
        DoctorSchedule schedule = booking.getSchedule();
        Doctor doctor = (schedule != null ? schedule.getDoctor() : null);
        Patient patient = booking.getPatient();

        // --- Build DoctorDTO ---
        DoctorDTO doctorDTO = null;
        if (doctor != null) {
            doctorDTO = DoctorDTO.builder()
                    .id(doctor.getId())
                    .name(doctor.getName())
                    .email(doctor.getEmail())
                    .type(doctor.getType() != null ? doctor.getType().name() : null)
                    .experience(doctor.getExperience())
                    .specialities(doctor.getSpecialities())
                    .education(doctor.getEducation())
                    .build();
        }

        // --- Build DispensaryDetailsDTO ---
        DispensaryDetailsDTO dispensaryDTO = null;
        if (schedule != null && schedule.getDispensary() != null) {
            Dispensary dispensary = schedule.getDispensary();
            dispensaryDTO = DispensaryDetailsDTO.builder()
                    .id(dispensary.getId())
                    .name(dispensary.getName())
                    .address(dispensary.getAddress())
                    .contactNumber(dispensary.getContactNumber())
                    // add other fields from your Dispensary entity as needed
                    .build();
        }

        // --- Build BookingResponseDTO ---
        return BookingResponseDTO.builder()
                .bookingId(booking.getId())
                .patientName(patient != null ? patient.getName() : null)
                .doctorName(doctor != null ? doctor.getName() : null)
                .doctor(doctorDTO)
                .dispensaryName(schedule != null && schedule.getDispensary() != null
                        ? schedule.getDispensary().getName() : null)
                .dispensary(dispensaryDTO) // ✅ new addition
                .appointmentDate(booking.getAppointmentDate())
                .appointmentTime(booking.getSlotStartTime())
                .status(booking.getStatus() != null ? booking.getStatus().name() : null)
                .paymentStatus(booking.getPaymentStatus() != null ? booking.getPaymentStatus().name() : null)
                .transactionId(booking.getTransactionId())
                .refundTransactionId(booking.getRefundTransactionId())
                .build();
    }

}
