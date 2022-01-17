package org.opentele.consult.service;

import org.opentele.consult.domain.security.User;
import org.opentele.consult.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

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

    public void sendEmail(String subject, String emailTemplateName, Context context, User user) throws MessagingException, IOException, URISyntaxException {
        String emailBody = fileUtil.getEmailBody(emailTemplateName, context);
        MimeMessage msg = javaMailSender.createMimeMessage();
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
        msg.setFrom(emailSender);
        msg.setSubject(subject);
        msg.setText(emailBody);
        msg.setContent(emailBody, "text/html");

        fileUtil.associateEmailAttachments(emailTemplateName);

        javaMailSender.send(msg);
    }
}
