package com.medical.doc4all.service.impl;

import com.medical.doc4all.dto.updateLocationDTO;
import com.medical.doc4all.entity.Patient;
import com.medical.doc4all.enums.Responses;
import com.medical.doc4all.repository.PatientRepository;
import com.medical.doc4all.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientRepository patientRepository;

    @Override
    public Responses createPatient(String email, String name) {
        Optional<Patient> p = patientRepository.findByEmail(email);
        if(p.isPresent()){
            return Responses.AlreadyExists;
        }
        Patient patient = Patient.builder()
                .email(email)
                .name(name)
                .build();
        patientRepository.save(patient);
        return Responses.Success;
    }

    @Override
    public Responses updateLocation(updateLocationDTO request, String email) {
        Optional<Patient> p = patientRepository.findByEmail(email);
        if(p.isEmpty()){
            return Responses.Failure;
        }
        Patient patient = p.get();
        patient.setLongitude(String.valueOf(request.getLongitude()));
        patient.setLatitude(String.valueOf(request.getLatitude()));
        patientRepository.save(patient);
        return Responses.Success;
    }
    @Override
    public updateLocationDTO getLocation(String email){
        Optional<Patient> p = patientRepository.findByEmail(email);
        if(p.isEmpty()){
            throw new RuntimeException("Patient not found");
        }
        Patient patient = p.get();
        return updateLocationDTO.builder()
                .Latitude(Double.valueOf(patient.getLatitude()))
                .Longitude(Double.valueOf(patient.getLongitude()))
                .build();
    }
}
