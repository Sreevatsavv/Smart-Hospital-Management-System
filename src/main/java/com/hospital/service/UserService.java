package com.hospital.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.entity.Patient;
import com.hospital.entity.User;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public String registerUser(User user) {

        Optional<User> existingUser =
                userRepository.findByEmail(user.getEmail());

        if(existingUser.isPresent()) {
            return "Email already exists!";
        }

        userRepository.save(user);

        // Automatically create Patient
        if(user.getRole().equals("Patient")){

            Patient patient = new Patient();

            patient.setName(user.getFullName());
            patient.setEmail(user.getEmail());
            patient.setPhone(user.getPhone());

            patientRepository.save(patient);

        }

        return "Registration Successful!";
    }
    public User loginUser(String email, String password) {

        return userRepository.findByEmailAndPassword(email, password)
                .orElse(null);
    }
}