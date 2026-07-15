package com.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hospital.entity.Doctor;
import com.hospital.service.DoctorService;
import com.hospital.entity.User;
import com.hospital.service.AppointmentService;
import com.hospital.repository.DoctorRepository;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/doctor")
public class DoctorController {
	
	@Autowired
	private AppointmentService appointmentService;

	@Autowired
	private DoctorRepository doctorRepository;

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/add")
    public String showAddDoctorPage(Model model) {

        model.addAttribute("doctor", new Doctor());

        return "doctor/add-doctor";
    }

    @PostMapping("/save")
    public String saveDoctor(@ModelAttribute Doctor doctor) {

        doctorService.saveDoctor(doctor);

        return "redirect:/doctor/list";
    }

    @GetMapping("/list")
    public String listDoctors(Model model) {

        model.addAttribute("doctors",
                doctorService.getAllDoctors());

        return "doctor/view-doctors";
    }
    
    @GetMapping("/appointments")
    public String doctorAppointments(Model model,
                                     HttpSession session) {

        User loggedUser = (User) session.getAttribute("loggedUser");

        Doctor doctor =
                doctorRepository.findByEmail(loggedUser.getEmail());

        model.addAttribute("appointments",
                appointmentService.getDoctorAppointments(doctor));

        return "appointment/doctor-appointments";
    }
    

}