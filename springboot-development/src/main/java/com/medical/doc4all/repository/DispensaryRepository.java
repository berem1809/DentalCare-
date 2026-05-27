package com.medical.doc4all.repository;

import com.medical.doc4all.entity.Dispensary;
import com.medical.doc4all.entity.DispensaryType;
import com.medical.doc4all.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DispensaryRepository extends JpaRepository<Dispensary, Long> {
    Optional<Dispensary> findByEmail(String email);
    boolean existsByEmail(String email);


}