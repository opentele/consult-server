package org.opentele.consult.controller;

import org.opentele.consult.framework.FileUtil;
import org.opentele.consult.service.MailService;
import org.opentele.consult.service.TemplateContextFactory;
import org.opentele.consult.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

@RestController
public class TestController {
    private final MailService mailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final FileUtil fileUtil;
    private final TemplateContextFactory templateContextFactory;
    private final UserService userService;

    @Autowired
    public TestController(MailService mailService, BCryptPasswordEncoder bCryptPasswordEncoder,
                          FileUtil fileUtil, TemplateContextFactory templateContextFactory, UserService userService) {
        this.mailService = mailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.fileUtil = fileUtil;
        this.templateContextFactory = templateContextFactory;
        this.userService = userService;
    }

    @PutMapping()
    @PreAuthorize("hasAnyRole('Admin')")
    @RequestMapping(value = "/api/test/mail")
    public ResponseEntity<Void> sendMail(@RequestParam("mailType") String mailType,
                                         @RequestParam("subject") String subject,
                                         @RequestParam("to") String to,
                                         @RequestBody Map<String, Object> context) throws MessagingException, IOException, URISyntaxException {
        Context templateContext = new Context();
        templateContext.setVariables(context);
        mailService.sendEmail(subject, mailType, templateContext, to);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/test/passwordHash")
    public String getPasswordHash(@RequestParam("password") String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    @GetMapping("/api/test/resetPasswordTemplate")
    public String processResetPasswordTemplate(@RequestParam("url") String url) {
        Context resetPasswordContext = templateContextFactory.createResetPasswordContext(url);
        return fileUtil.processTemplate("resetPassword", resetPasswordContext);
    }

    @DeleteMapping("/api/test/organisation")
    @PreAuthorize("hasAnyRole('Admin')")
    public void deleteOrganisation(@RequestParam("orgName") String orgName) {
        userService.deleteOrganisation(orgName);
    }
}
