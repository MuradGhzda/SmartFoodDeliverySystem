package org.example.service.email;

import org.example.dto.contact.ContactMessage;

public interface EmailService {
    void sendOrderDispatchedEmail(String recipientEmail);
    void sendOrderDeliveredEmail(String recipientEmail);
    void sendOrderEmails(String email);
    void sendSimpleEmail(String toEmail, String subject, String body);
    void dispatchOrder(String userEmail);
    void registerUser(String email);
    void sendContactFormEmail(ContactMessage contactMessage);
}