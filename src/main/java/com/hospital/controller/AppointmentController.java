package com.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hospital.entity.Appointment;
import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import com.hospital.entity.User;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.PatientRepository;
import com.hospital.service.AppointmentService;
import com.hospital.service.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private EmailService emailService;

    @GetMapping("/add")
    public String addAppointment(Model model, HttpSession session) {

        User loggedUser = (User) session.getAttribute("loggedUser");

        if (loggedUser == null) {
            System.out.println("LOGGED USER IS NULL");
            return "redirect:/login";
        }

        System.out.println("Logged User Email: " + loggedUser.getEmail());

        Patient patient = patientRepository.findByEmail(loggedUser.getEmail());

        if (patient == null) {
            System.out.println("PATIENT NOT FOUND");
        } else {
            System.out.println("PATIENT FOUND: " + patient.getName());
        }

        model.addAttribute("appointment", new Appointment());
        model.addAttribute("doctors", doctorRepository.findAll());
        model.addAttribute("patient", patient);

        return "appointment/add-appointment";
    }

    @PostMapping("/save")
    public String saveAppointment(@RequestParam Long doctorId,
                                  @RequestParam Long patientId,
                                  @ModelAttribute Appointment appointment) {

        appointment.setDoctor(
                doctorRepository.findById(doctorId).orElse(null));

        appointment.setPatient(
                patientRepository.findById(patientId).orElse(null));

        appointment.setStatus("PENDING");

        appointmentService.saveAppointment(appointment);

        // Send email to doctor
        Doctor doctor = appointment.getDoctor();
        Patient patient = appointment.getPatient();

        String subject = "New Appointment Request";

        String body =
                "Hello Dr. " + doctor.getName() + ",\n\n" +
                "A new appointment has been booked.\n\n" +

                "Patient : " + patient.getName() + "\n" +
                "Date : " + appointment.getAppointmentDate() + "\n" +
                "Time : " + appointment.getAppointmentTime() + "\n" +
                "Reason : " + appointment.getReason() + "\n\n" +

                "Please login to Smart Hospital Management System to accept or reject the appointment.\n\n" +

                "Regards,\n" +
                "Smart Hospital Management System";
        
        System.out.println("Doctor Email: " + doctor.getEmail());
        emailService.sendEmail(
                doctor.getEmail(),
                subject,
                body);

        return "redirect:/patient/appointments";
    }

    @GetMapping("/list")
    public String listAppointments(Model model) {

        model.addAttribute("appointments",
                appointmentService.getAllAppointments());

        return "appointment/view-appointments";
    }

    @GetMapping("/accept/{id}")
    public String acceptAppointment(@PathVariable Long id) {

        appointmentService.acceptAppointment(id);

        return "redirect:/doctor/appointments";
    }

    @GetMapping("/reject/{id}")
    public String rejectAppointment(@PathVariable Long id) {

        appointmentService.rejectAppointment(id);

        return "redirect:/doctor/appointments";
    }

    @GetMapping("/delete/{id}")
    public String deleteAppointment(@PathVariable Long id) {

        appointmentService.deleteAppointment(id);

        return "redirect:/appointment/list";
    }

}