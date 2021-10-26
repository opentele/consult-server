package org.opentele.consult.service;

import org.opentele.consult.domain.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender javaMailSender;

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String subject, String body, User user) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(user.getEmail());

        msg.setSubject(subject);
        msg.setText(body);
        javaMailSender.send(msg);
    }
}
