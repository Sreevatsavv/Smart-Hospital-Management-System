package com.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long>{

    List<Patient> findAll();
    Patient findByEmail(String email);

}