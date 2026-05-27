package com.medical.doc4all.repository;

import com.medical.doc4all.entity.DoctorSchedule;
import com.medical.doc4all.enums.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.Collection;
import java.util.List;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
    List<DoctorSchedule> findByDoctorId(Integer doctorId);
    List<DoctorSchedule> findByDispensaryId(Long dispensaryId);
    List<DoctorSchedule> findByDoctorIdAndDay(Integer doctorId, DayOfWeek day);

    List<DoctorSchedule> findByDispensaryIdAndStatus(Long dispensaryId, InvitationStatus status);

    List<DoctorSchedule> findByIdIn(Collection<Long> ids);


    // Optional convenience query not necessary if using in-memory checks
}

