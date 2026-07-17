package com.hospital.controller;

import com.hospital.entity.Appointment;
import com.hospital.entity.Prescription;
import com.hospital.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.hospital.entity.Patient;
import com.hospital.repository.PrescriptionRepository;
import com.hospital.service.EmailService;

@Controller
@RequestMapping("/prescription")
public class PrescriptionController {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("/write/{appointmentId}")
    public String writePrescription(@PathVariable Long appointmentId, Model model) {

        Appointment appointment = appointmentRepository
                .findById(appointmentId)
                .orElseThrow();

        model.addAttribute("appointment", appointment);
        model.addAttribute("prescription", new Prescription());

        return "doctor/prescription";
    }
    @PostMapping("/send")
    public String sendPrescription(
            @ModelAttribute Prescription prescription,
            @RequestParam Long appointmentId) {

        Appointment appointment = appointmentRepository
                .findById(appointmentId)
                .orElseThrow();

        prescription.setAppointment(appointment);
        prescription.setDoctor(appointment.getDoctor());
        prescription.setPatient(appointment.getPatient());

        prescriptionRepository.save(prescription);

        Patient patient = appointment.getPatient();

        String subject = "Prescription from Smart Hospital";

        String body =
                "Hello " + patient.getName() + ",\n\n" +

                "Your doctor has provided a prescription.\n\n" +

                "Doctor : Dr. " + appointment.getDoctor().getName() + "\n\n" +

                "Diagnosis:\n" +
                prescription.getDiagnosis() + "\n\n" +

                "Medicines:\n" +
                prescription.getMedicines() + "\n\n" +

                "Notes:\n" +
                prescription.getNotes() + "\n\n" +

                "Thank you,\n" +
                "Smart Hospital Management System";

        emailService.sendEmail(
                patient.getEmail(),
                subject,
                body);

        return "doctor/dashboard";
    }

}