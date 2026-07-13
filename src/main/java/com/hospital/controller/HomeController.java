package com.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hospital.entity.User;
import com.hospital.service.UserService;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "dashboard";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model) {

        User user = userService.loginUser(email, password);

        if(user == null) {
            model.addAttribute("message", "Invalid Email or Password");
            return "login";
        }

        switch(user.getRole()) {

            case "Administrator":
                return "redirect:/admin";

            case "Doctor":
                return "redirect:/doctor";

            case "Receptionist":
                return "redirect:/receptionist";

            default:
                return "redirect:/patient";
        }
    }
}