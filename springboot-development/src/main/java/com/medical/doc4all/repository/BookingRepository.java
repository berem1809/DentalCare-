package com.medical.doc4all.repository;

import com.medical.doc4all.entity.Booking;
import com.medical.doc4all.entity.Doctor;
import com.medical.doc4all.entity.DoctorSchedule;
import com.medical.doc4all.entity.Patient;
import com.medical.doc4all.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.schedule.doctor.id = :doctorId AND b.appointmentDate = :date")
    List<Booking> findByDoctorIdAndAppointmentDate(Long doctorId, LocalDate date);

    boolean existsByPatientIdAndScheduleDoctorIdAndStatus(Integer patientId, Integer doctorId, BookingStatus status);
    List<Booking> findByScheduleIdAndAppointmentDate(Long id, LocalDate appointmentDate);

    boolean existsByScheduleIdAndAppointmentDateAndSlotStartTime(
            Long scheduleId, LocalDate appointmentDate, LocalTime slotStartTime
    );

    List<Booking> findByPatient(Patient patient);

    List<Booking> findBySchedule(DoctorSchedule schedule);

    boolean existsByPatientAndSchedule_DoctorAndStatus(Patient patient, Doctor doctor, BookingStatus status);

    @Query("SELECT DISTINCT b.schedule.doctor FROM Booking b WHERE b.patient = :patient AND b.status = com.medical.doc4all.enums.BookingStatus.COMPLETED")
    List<Doctor> findCompletedDoctorsByPatient(@Param("patient") Patient patient);

    boolean existsByScheduleAndStatusAndAppointmentDateGreaterThanEqual(DoctorSchedule schedule, BookingStatus bookingStatus, LocalDate now);
}



