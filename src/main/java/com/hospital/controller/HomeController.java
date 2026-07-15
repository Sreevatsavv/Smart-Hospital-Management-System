package com.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hospital.entity.User;
import com.hospital.service.EmailService;
import com.hospital.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private EmailService emailService;

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    System.out.println("Logged in user: " + user.getEmail());
    
    @GetMapping("/testmail")
    @ResponseBody
    public String testMail() {

        emailService.sendEmail(
                "sreevatsav.ch@gmail.com",
                "Spring Boot Test",
                "Congratulations! Your Smart Hospital email service is working.");

        return "Email Sent Successfully!";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {

        User user = userService.loginUser(email, password);

        if(user == null) {
            model.addAttribute("message", "Invalid Email or Password");
            return "login";
        }

        // Store logged-in user
        session.setAttribute("loggedUser", user);

        switch(user.getRole()) {

        case "Administrator":
            return "redirect:/admin";

        case "Doctor":
            return "redirect:/doctor";

        case "Patient":
            return "redirect:/patient";

        default:
            return "redirect:/login";
    }
    }
}