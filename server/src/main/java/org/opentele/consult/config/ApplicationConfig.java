package org.opentele.consult.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConfig {
    @Value("${consult.email.location}")
    private String emailLocation;

    @Value("${email.sender}")
    private String emailSender;

    public String getEmailLocation() {
        return emailLocation;
    }

    public String getEmailSender() {
        return emailSender;
    }
}
