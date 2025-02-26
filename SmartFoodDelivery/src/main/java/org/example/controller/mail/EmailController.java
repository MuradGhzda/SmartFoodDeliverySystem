package org.example.controller.mail;

import org.example.service.email.impl.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private EmailServiceImpl emailServiceImpl;

    @GetMapping("/send-email")
    public String sendEmail(@RequestParam String toEmail) {
        emailServiceImpl.sendSimpleEmail(toEmail, "Test Email", "This is a test email sent from Spring Boot.");
        return "Email sent to " + toEmail;
    }
}
