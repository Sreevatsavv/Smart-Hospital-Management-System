package com.hospital.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.entity.Doctor;
import com.hospital.entity.User;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.UserRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private UserRepository userRepository;

    public Doctor saveDoctor(Doctor doctor) {

        Doctor savedDoctor = doctorRepository.save(doctor);

        if(userRepository.findByEmail(doctor.getEmail()).isEmpty()){

            User user = new User();

            user.setFullName(doctor.getName());
            user.setEmail(doctor.getEmail());

            // Temporary default password
            user.setPassword("doctor123");

            user.setPhone(doctor.getPhone());

            user.setRole("Doctor");

            userRepository.save(user);
        }

        return savedDoctor;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
    
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id).orElse(null);
    }

    public Doctor updateDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

}