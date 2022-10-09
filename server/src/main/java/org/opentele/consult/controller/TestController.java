package org.opentele.consult.controller;

import org.opentele.consult.framework.FileUtil;
import org.opentele.consult.service.ConsultationRoomService;
import org.opentele.consult.service.MailService;
import org.opentele.consult.service.TemplateContextFactory;
import org.opentele.consult.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {
    private final MailService mailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final FileUtil fileUtil;
    private final TemplateContextFactory templateContextFactory;
    private final UserService userService;
    private final ConsultationRoomService consultationRoomService;

    @Autowired
    public TestController(MailService mailService, BCryptPasswordEncoder bCryptPasswordEncoder,
                          FileUtil fileUtil, TemplateContextFactory templateContextFactory, UserService userService, ConsultationRoomService consultationRoomService) {
        this.mailService = mailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.fileUtil = fileUtil;
        this.templateContextFactory = templateContextFactory;
        this.userService = userService;
        this.consultationRoomService = consultationRoomService;
    }

    @RequestMapping(value = "/api/test/open/ping")
    public String ping(HttpSession httpSession) {
        return "pong";
    }

    @GetMapping("/api/test/open/passwordHash")
    public String getPasswordHash(@RequestParam("password") String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    @RequestMapping(value = "/api/test/security/orgAdminRole")
    @PreAuthorize("hasAnyRole('OrgAdmin')")
    public String orgAdminRole(HttpSession httpSession) {
        return "Yes";
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

    @GetMapping("/api/test/resetPasswordTemplate")
    @PreAuthorize("hasAnyRole('Admin')")
    public String processResetPasswordTemplate(@RequestParam("url") String url) {
        Context resetPasswordContext = templateContextFactory.createResetPasswordContext(url);
        return fileUtil.processTemplate("resetPassword", resetPasswordContext);
    }

    @DeleteMapping("/api/test/organisation")
    @PreAuthorize("hasAnyRole('Admin')")
    public void deleteOrganisation(@RequestParam("orgName") String orgName) {
        userService.deleteOrganisation(orgName);
    }

    @GetMapping("/api/test/scheduleRooms")
    @PreAuthorize("hasAnyRole('Admin')")
    public int scheduleRooms(@RequestParam("numberOfDays") int numberOfDays) {
        return consultationRoomService.schedule(numberOfDays);
    }

    @RequestMapping(value = "/api/test/causeError", method = {RequestMethod.GET, RequestMethod.POST})
    @PreAuthorize("hasAnyRole('User')")
    public void causeError() {
        throw new RuntimeException("Throwing error to check whether Bugsnag records it");
    }
}
