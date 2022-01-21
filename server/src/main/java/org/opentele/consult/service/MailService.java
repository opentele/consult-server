package org.opentele.consult.service;

import org.opentele.consult.config.ApplicationConfig;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.framework.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class MailService {
    private final JavaMailSender javaMailSender;
    private final FileUtil fileUtil;
    private final ApplicationConfig applicationConfig;

    @Autowired
    public MailService(JavaMailSender javaMailSender, FileUtil fileUtil, ApplicationConfig applicationConfig) {
        this.javaMailSender = javaMailSender;
        this.fileUtil = fileUtil;
        this.applicationConfig = applicationConfig;
    }

    public void sendEmail(String subject, String emailTemplateName, Context context, User user) throws MessagingException, IOException, URISyntaxException {
        this.sendEmail(subject, emailTemplateName, context, user.getEmail());
    }

    public void sendEmail(String subject, String emailTemplateName, Context context, String to) throws MessagingException, IOException, URISyntaxException {
        String emailBody = fileUtil.processTemplate(emailTemplateName, context);
        MimeMessage msg = javaMailSender.createMimeMessage();
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setFrom(applicationConfig.getEmailSender());
        msg.setSubject(subject);

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(emailBody, "text/html; charset=utf8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        msg.setContent(multipart);

        fileUtil.associateEmailAttachments(emailTemplateName, multipart);
        javaMailSender.send(msg);
    }
}
