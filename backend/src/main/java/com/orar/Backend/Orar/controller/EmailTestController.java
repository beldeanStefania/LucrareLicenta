package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test-email")
public class EmailTestController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send")
    public String sendTestEmail(@RequestParam String to) {
        String subject = "Test Email de la Orar App 📧";
        String body = "Salut!\n\nAcesta este un email de test trimis din aplicația ta Spring Boot.";

        emailService.sendSimpleEmail(to, subject, body);
        return "Email de test trimis către: " + to;
    }
}
