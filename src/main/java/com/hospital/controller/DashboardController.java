package com.hospital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/doctor")
    public String doctor() {
        return "doctor";
    }

    @GetMapping("/receptionist")
    public String receptionist() {
        return "receptionist";
    }

    @GetMapping("/patient")
    public String patient() {
        return "patient";
    }
}