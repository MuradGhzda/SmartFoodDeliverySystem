package org.example.service.email.impl;

import org.example.dto.contact.ContactMessage;
import org.example.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOrderDispatchedEmail(String recipientEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Your Order Has Been Dispatched");
        message.setText("Dear customer, your order has been dispatched and will arrive soon. Thank you for ordering with us!");

        mailSender.send(message);
    }

    public void sendOrderDeliveredEmail(String recipientEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Your Order Has Been Delivered");
        message.setText("Dear customer, your order has been successfully delivered. Enjoy your meal!");

        mailSender.send(message);
    }

    // Removed the self-injection of EmailServiceImpl
    // @Autowired
    // private EmailServiceImpl emailService;

    public void sendOrderEmails(String email) {
        // Call methods directly
        sendOrderDispatchedEmail(email);
        sendOrderDeliveredEmail(email);
    }

    public void sendSimpleEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("muradagazade167@gmail.com");


            mailSender.send(message);
            System.out.println("Email sent successfully to " + toEmail);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }

    public void dispatchOrder(String userEmail) {
        // Logic to dispatch the order

        // Send dispatch notification email
        sendOrderDispatchedEmail(userEmail);
    }

    public void registerUser(String email) {
        // Logic to register the user

        // Send welcome email
        sendSimpleEmail(email, "Welcome!", "Thank you for registering with us!");
    }
    public void sendContactFormEmail(ContactMessage contactMessage) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("muradagazade167@gmail.com");
            message.setTo("muradagazade167@gmail.com"); // Şirket email adresi
            message.setSubject("İletişim Formu: " + contactMessage.getSubject());

            String emailBody = String.format("""
            Yeni İletişim Formu Mesajı:
            
            Gönderen: %s
            Email: %s
            
            Mesaj:
            %s
            """,
                    contactMessage.getName(),
                    contactMessage.getEmail(),
                    contactMessage.getMessage()
            );

            message.setText(emailBody);
            mailSender.send(message);

            // Gönderene otomatik yanıt
            sendAutoReplyEmail(contactMessage.getEmail(), contactMessage.getName());

        } catch (Exception e) {
            System.err.println("İletişim formu emaili gönderilirken hata: " + e.getMessage());
            throw e;
        }
    }

    private void sendAutoReplyEmail(String email, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("muradagazade167@gmail.com");
        message.setTo(email);
        message.setSubject("SmartFood - Mesajınız Alındı");

        String autoReplyText = String.format("""
        Sayın %s,
        
        İletişim formu mesajınız başarıyla alınmıştır. 
        En kısa sürede size geri dönüş yapacağız.
        
        İyi günler dileriz,
        SmartFood Ekibi
        """,
                name
        );

        message.setText(autoReplyText);
        mailSender.send(message);
    }

}
