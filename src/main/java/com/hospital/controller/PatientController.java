package com.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hospital.entity.Patient;
import com.hospital.service.PatientService;
import com.hospital.entity.User;
import com.hospital.service.AppointmentService;
import com.hospital.repository.PatientRepository;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/patient")
public class PatientController {
	
	@Autowired
	private AppointmentService appointmentService;

	@Autowired
	private PatientRepository patientRepository;

    @Autowired
    private PatientService patientService;

    @GetMapping("/add")
    public String showAddPatientPage(Model model) {

        model.addAttribute("patient", new Patient());

        return "patient/add-patient";
    }

    @PostMapping("/save")
    public String savePatient(@ModelAttribute Patient patient) {

        patientService.savePatient(patient);

        return "redirect:/patient/list";
    }

    @GetMapping("/list")
    public String viewPatients(Model model) {

        model.addAttribute("patients", patientService.getAllPatients());

        return "patient/view-patients";
    }

    @GetMapping("/edit/{id}")
    public String editPatient(@PathVariable Long id, Model model) {

        model.addAttribute("patient", patientService.getPatientById(id));

        return "patient/edit-patient";
    }

    @PostMapping("/update")
    public String updatePatient(@ModelAttribute Patient patient) {

        patientService.updatePatient(patient);

        return "redirect:/patient/list";
    }

    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable Long id) {

        patientService.deletePatient(id);

        return "redirect:/patient/list";
    }
    
    @GetMapping("/appointments")
    public String patientAppointments(Model model,
                                      HttpSession session) {

        User loggedUser = (User) session.getAttribute("loggedUser");

        Patient patient =
                patientRepository.findByEmail(loggedUser.getEmail());

        model.addAttribute("appointments",
                appointmentService.getPatientAppointments(patient));

        return "appointment/patient-appointments";
    }
    

}