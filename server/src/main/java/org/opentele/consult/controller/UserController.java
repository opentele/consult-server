package org.opentele.consult.controller;

import org.opentele.consult.contract.OrganisationPutPostRequest;
import org.opentele.consult.contract.PasswordChangeRequest;
import org.opentele.consult.contract.ResetPasswordRequest;
import org.opentele.consult.contract.UserResponse;
import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.security.PasswordResetToken;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.domain.security.UserType;
import org.opentele.consult.framework.Translator;
import org.opentele.consult.message.MessageCodes;
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
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.UUID;

@RestController
public class UserController {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MailService mailService;
    private TemplateContextFactory templateContextFactory;

    @Autowired
    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, MailService mailService, TemplateContextFactory templateContextFactory) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mailService = mailService;
        this.templateContextFactory = templateContextFactory;
    }

    @RequestMapping(value = "/api/app/organisation", method = {RequestMethod.PUT})
    @Transactional
    public ResponseEntity<String> save(@RequestBody OrganisationPutPostRequest request) {
        String error = userService.validateNewOrganisation(request.getEmail(), request.getMobile());
        if (error != null)
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);

        Organisation organisation = new Organisation();
        organisation.setName(request.getOrganisationName());

        User user = new User();
        user.setUuid(UUID.randomUUID());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        user.setName(request.getName());
        user.setUserType(UserType.OrgAdmin);

        userService.createNewOrganisation(user, organisation);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/currentUser", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('User','OrgAdmin')")
    public UserResponse loggedInUser(Principal principal) {
        String name = principal.getName();
        User user = userService.findUserByEmail(name);
        UserResponse userResponse = new UserResponse();
        userResponse.setUserType(user.getUserType().name());
        userResponse.setPhone(user.getMobile());
        userResponse.setEmail(user.getEmail());
        userResponse.setOrganisationName(user.getOrganisation().getName());
        userResponse.setName(user.getName());
        return userResponse;
    }

    @RequestMapping(value = "/api/user/loggedIn", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('User','OrgAdmin')")
    public boolean loggedIn() {
        return true;
    }

    @PostMapping("/api/user/resetPassword")
    public ResponseEntity<String> resetPassword(HttpServletRequest servletRequest,
                                                @RequestBody ResetPasswordRequest request) throws MessagingException, IOException, URISyntaxException {
        String error = userService.validateResetPassword(request.getEmail(), request.getMobile());
        if (error != null)
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

        User user = userService.getUser(request.getEmail(), request.getMobile());
        PasswordResetToken tokenForUser = userService.createPasswordResetTokenForUser(user);
        String url = servletRequest.getContextPath() + "/api/user/changePassword?token=" + tokenForUser.getToken();
        Context resetPasswordContext = templateContextFactory.createResetPasswordContext(url);
        String subject = Translator.toLocale(MessageCodes.RESET_PASSWORD_SUBJECT);
        mailService.sendEmail(subject, "reset-password", resetPasswordContext, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/user/validatePassword")
    public ResponseEntity<String> validatePasswordResetToken(@RequestParam("token") String token) {
        PasswordResetToken.TokenStatus tokenStatus = userService.validateToken(token);
        return switch (tokenStatus) {
            case Expired, NotFound -> new ResponseEntity<>(tokenStatus.name(), HttpStatus.NOT_ACCEPTABLE);
            case Valid -> new ResponseEntity<>(HttpStatus.OK);
        };
    }

    @PostMapping("/api/user/savePassword")
    public ResponseEntity<String> savePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        PasswordResetToken.TokenStatus tokenStatus = userService.validateToken(passwordChangeRequest.getToken());

        switch (tokenStatus) {
            case Expired:
            case NotFound:
                return new ResponseEntity<>(tokenStatus.name(), HttpStatus.NOT_ACCEPTABLE);
            case Valid:
            default:
                userService.updatePassword(passwordChangeRequest.getToken(), passwordChangeRequest.getPassword());
                return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
