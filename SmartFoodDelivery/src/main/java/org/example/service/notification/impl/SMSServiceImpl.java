package org.example.service.notification.impl;

import org.example.service.notification.SMSService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SMSServiceImpl implements SMSService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    public void sendOrderDispatchedSMS(String toPhoneNumber) {
        Twilio.init(accountSid, authToken);

        Message.creator(
                new PhoneNumber(toPhoneNumber),
                new PhoneNumber(fromPhoneNumber),
                "Your order has been dispatched! It will arrive soon."
        ).create();
    }

    public void sendOrderDeliveredSMS(String toPhoneNumber) {
        Twilio.init(accountSid, authToken);

        Message.creator(
                new PhoneNumber(toPhoneNumber),
                new PhoneNumber(fromPhoneNumber),
                "Your order has been delivered! Enjoy your meal!"
        ).create();
    }
}
