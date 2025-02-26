package org.example.service.notification;

public interface SMSService {
    void sendOrderDispatchedSMS(String toPhoneNumber);
    void sendOrderDeliveredSMS(String toPhoneNumber);
}