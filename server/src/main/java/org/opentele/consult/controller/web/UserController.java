package org.opentele.consult.controller.web;

import org.opentele.consult.contract.OrganisationCreateRequest;
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
import org.opentele.consult.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Set;
import java.util.UUID;

@RestController
public class UserController {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MailService mailService;

    @Autowired
    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, MailService mailService) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mailService = mailService;
    }

    @RequestMapping(value = "/api/app/organisation", method = {RequestMethod.PUT})
    @Transactional
    public ResponseEntity<String> save(@RequestBody OrganisationCreateRequest organisationCreateRequest) {
        Set<String> errors = userService.validateNewOrganisation(organisationCreateRequest.getName(), organisationCreateRequest.getEmail());
        if (errors.size() != 0)
            return new ResponseEntity<>(Translator.fromErrors(errors), HttpStatus.CONFLICT);

        Organisation organisation = new Organisation();
        organisation.setName(organisationCreateRequest.getOrganisationName());


        User user = new User();
        user.setUuid(UUID.randomUUID());
        user.setPassword(bCryptPasswordEncoder.encode(organisationCreateRequest.getPassword()));
        user.setEmail(organisationCreateRequest.getEmail());
        user.setName(organisationCreateRequest.getName());
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
        userResponse.setPhone(user.getPhone());
        userResponse.setEmail(user.getEmail());
        userResponse.setOrganisationName(user.getOrganisation().getName());
        userResponse.setName(user.getName());
        return userResponse;
    }

    @PostMapping("/api/user/resetPassword")
    public ResponseEntity<String> resetPassword(HttpServletRequest request,
                                                @RequestBody ResetPasswordRequest resetPasswordRequest) {
        User user = userService.findUserByEmail(resetPasswordRequest.getEmail());
        if (user == null)
            return new ResponseEntity<>(Translator.toLocale(MessageCodes.NO_USER_WITH_ID), HttpStatus.BAD_REQUEST);

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);

        String url = request.getContextPath() + "/api/user/changePassword?token=" + token;
        String message = Translator.toLocale(MessageCodes.RESET_PASSWORD_BODY);
        String subject = Translator.toLocale(MessageCodes.RESET_PASSWORD_SUBJECT);
        mailService.sendEmail(subject, message, user);
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
