package com.hospital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/admin")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/doctor")
    public String doctorDashboard() {
        return "doctor/dashboard";
    }

    @GetMapping("/patient")
    public String patientDashboard() {
        return "patient/dashboard";
    }
}