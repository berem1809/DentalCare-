package com.medical.doc4all.repository;

import com.medical.doc4all.entity.DoctorInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorInvitationRepository extends JpaRepository<DoctorInvitation, Long> {
    List<DoctorInvitation> findByDispensaryId(Long dispensaryId);
    List<DoctorInvitation> findByDoctorId(Integer doctorId);
}
