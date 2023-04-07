package org.opentele.consult.controller;

import org.opentele.consult.contract.security.*;
import org.opentele.consult.domain.Language;
import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.security.*;
import org.opentele.consult.framework.Translator;
import org.opentele.consult.framework.UserSession;
import org.opentele.consult.message.MessageCodes;
import org.opentele.consult.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController extends BaseController {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MailService mailService;
    private final TemplateContextFactory templateContextFactory;
    private final OrganisationService organisationService;
    private final OrganisationUserService organisationUserService;
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, MailService mailService, TemplateContextFactory templateContextFactory, UserSession userSession, OrganisationService organisationService, OrganisationUserService organisationUserService) {
        super(userService, userSession);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mailService = mailService;
        this.templateContextFactory = templateContextFactory;
        this.organisationService = organisationService;
        this.organisationUserService = organisationUserService;
    }

    @RequestMapping(value = "/api/user", method = {RequestMethod.PUT})
    @Transactional
    public ResponseEntity<String> save(@RequestBody UserRequest request) {
        String error = userService.validateNewUser(request.getEmail(), request.getMobile());
        if (error != null)
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);

        User user = request.toUser(bCryptPasswordEncoder.encode(request.getPassword()));
        userService.save(user, userService.getAppUser());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/users", method = {RequestMethod.PUT})
    public ResponseEntity<String> save(@RequestBody List<UserRequest> requests) {
        requests.forEach(this::save);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/user/organisation", method = {RequestMethod.POST})
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> setOrganisation(@RequestParam(name = "organisationId") long organisationId, Principal principal) {
        String userId = principal.getName();
        UserType userType = userService.getUserType(userId, organisationId);
        if (userType.equals(UserType.OrgAdmin))
            SecurityService.elevateToOrgAdminRole();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/user/loggedIn", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('User','OrgAdmin')")
    public boolean loggedIn() {
        logger.info("User is logged in");
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

    @GetMapping("/api/user/search")
    @PreAuthorize("hasRole('User')")
    public List<OrganisationUserResponse> search(@RequestParam("q") String searchParam) {
        return userService.findUsers(searchParam, getCurrentOrganisation()).stream().map(OrganisationUserResponse::from).collect(Collectors.toList());
    }

    @PutMapping("/api/user/organisation")
    @PreAuthorize("hasRole('User')")
    public void registerOrganisation(@RequestBody OrganisationCreateRequest request, Principal principal) {
        Organisation currentOrganisation = getCurrentOrganisation();
        if (currentOrganisation != null)
            throw new RuntimeException("The user is already is part of organisation");
        User currentUser = getCurrentUser(principal);
        Organisation organisation = organisationService.createOrg(request.getName(), currentUser, request.getFormIoProjectId(), request.getFormUsageMode());
        OrganisationUser organisationUser = organisationUserService.associateExistingUser(currentUser, UserType.OrgAdmin, ProviderType.None, organisation, Language.en);
        SecurityService.elevateToRole(organisationUser.getUserType());
        setCurrentOrganisationId(organisationUser.getOrganisation().getId());
    }
}
