package com.medical.doc4all.repository;

import com.medical.doc4all.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient,Integer> {
    Optional<Patient> findByEmail(String email);
}
