package com.medical.doc4all.repository;

import com.medical.doc4all.entity.DispensaryType;
import com.medical.doc4all.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    // Additional query methods can be defined here if needed
    // For example, to find a doctor by email:
    Optional<Doctor> findByEmail(String email);

    List<Doctor> findByType(DispensaryType type);
}
