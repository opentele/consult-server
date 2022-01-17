package org.opentele.consult.controller.web;

import org.opentele.consult.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/test/mail")
public class MailController {
    private final MailService mailService;

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PutMapping()
    @PreAuthorize("hasAnyRole('Admin')")
    public ResponseEntity<Void> sendMail(@RequestParam("mailType") String mailType,
                                         @RequestParam("subject") String subject,
                                         @RequestParam("to") String to,
                                         @RequestBody Map<String, Object> context) throws MessagingException, IOException, URISyntaxException {
        Context templateContext = new Context();
        templateContext.setVariables(context);
        mailService.sendEmail(subject, mailType, templateContext, to);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
