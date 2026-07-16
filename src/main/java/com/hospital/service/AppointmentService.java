package com.hospital.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.entity.Appointment;
import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import com.hospital.repository.AppointmentRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private EmailService emailService;

    public Appointment saveAppointment(Appointment appointment) {

        appointment.setStatus("PENDING");

        return appointmentRepository.save(appointment);
    }

    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment getAppointment(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    public List<Appointment> getPendingAppointments() {
        return appointmentRepository.findByStatus("PENDING");
    }

    public List<Appointment> getDoctorAppointments(Doctor doctor) {
        return appointmentRepository.findByDoctor(doctor);
    }

    public List<Appointment> getPatientAppointments(Patient patient) {
        return appointmentRepository.findByPatient(patient);
    }

    public void acceptAppointment(Long id) {

        Appointment appointment =
                appointmentRepository.findById(id).orElse(null);

        if (appointment != null) {

            appointment.setStatus("ACCEPTED");
            appointmentRepository.save(appointment);

            Patient patient = appointment.getPatient();
            Doctor doctor = appointment.getDoctor();

            String subject = "Appointment Accepted";

            String body =
                    "Hello " + patient.getName() + ",\n\n" +
                    "Your appointment has been ACCEPTED.\n\n" +
                    "Doctor : Dr. " + doctor.getName() + "\n" +
                    "Date : " + appointment.getAppointmentDate() + "\n" +
                    "Time : " + appointment.getAppointmentTime() + "\n\n" +
                    "Thank you,\n" +
                    "Smart Hospital Management System";

            emailService.sendEmail(
                    patient.getEmail(),
                    subject,
                    body);
        }
    }

    public void rejectAppointment(Long id) {

        Appointment appointment =
                appointmentRepository.findById(id).orElse(null);

        if (appointment != null) {

            appointment.setStatus("REJECTED");
            appointmentRepository.save(appointment);

            Patient patient = appointment.getPatient();

            String subject = "Appointment Rejected";

            String body =
                    "Hello " + patient.getName() + ",\n\n" +
                    "Unfortunately your appointment has been rejected.\n\n" +
                    "Please login and book another appointment.\n\n" +
                    "Regards,\n" +
                    "Smart Hospital Management System";

            emailService.sendEmail(
                    patient.getEmail(),
                    subject,
                    body);
        }
    }

}