package org.opentele.consult.service;

import org.opentele.consult.domain.security.User;
import org.opentele.consult.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
public class MailService {
    private final JavaMailSender javaMailSender;
    private FileUtil fileUtil;
    @Value("${email.sender}")
    private String emailSender;

    @Autowired
    public MailService(JavaMailSender javaMailSender, FileUtil fileUtil) {
        this.javaMailSender = javaMailSender;
        this.fileUtil = fileUtil;
    }

    public void sendEmail(String subject, String emailTemplateName, Context context, User user) {
        String emailBody = fileUtil.getEmailBody(emailTemplateName, context);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(user.getEmail());
        msg.setFrom(emailSender);
        msg.setSubject(subject);
        msg.setText(emailBody);
        javaMailSender.send(msg);
    }
}
