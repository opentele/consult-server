package org.opentele.consult.controller;

import org.opentele.consult.contract.security.CurrentUserResponse;
import org.opentele.consult.contract.security.OrganisationUserResponse;
import org.opentele.consult.contract.security.OrganisationUserPutPostRequest;
import org.opentele.consult.contract.security.SearchedUserResponse;
import org.opentele.consult.domain.security.OrganisationUser;
import org.opentele.consult.domain.security.ProviderType;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.framework.UserSession;
import org.opentele.consult.service.OrganisationUserService;
import org.opentele.consult.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrganisationUserController extends BaseController {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final OrganisationUserService organisationUserService;
    private final static Logger logger = LoggerFactory.getLogger(OrganisationUserController.class);

    @Autowired
    public OrganisationUserController(UserService userService, UserSession userSession, BCryptPasswordEncoder bCryptPasswordEncoder, OrganisationUserService organisationUserService) {
        super(userService, userSession);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.organisationUserService = organisationUserService;
    }

    @RequestMapping(value = "/api/organisationUser", method = {RequestMethod.GET})
    @PreAuthorize("hasRole('User')")
    public List<OrganisationUserResponse> getUsers(@RequestParam(name = "providerType", required = false) ProviderType providerType) {
        return userService.getOrganisationUsers(getCurrentOrganisation(), providerType).stream().map(OrganisationUserResponse::from).collect(Collectors.toList());
    }

    @RequestMapping(value = "/api/organisationUser/{id}", method = {RequestMethod.GET})
    @PreAuthorize("hasRole('User')")
    public OrganisationUserResponse getOrganisationUser(@PathVariable("id") long id) {
        OrganisationUser organisationUser = userService.getOrganisationUser(id, getCurrentOrganisation());
        return OrganisationUserResponse.from(organisationUser);
    }

    @RequestMapping(value = "/api/organisationUsers", method = {RequestMethod.PUT})
    @PreAuthorize("hasRole('OrgAdmin')")
    @Transactional
    public void createUsers(@RequestBody List<OrganisationUserPutPostRequest> request) {
        request.forEach(this::createUser);
    }

    @RequestMapping(value = "/api/organisationUser", method = {RequestMethod.PUT})
    @PreAuthorize("hasRole('OrgAdmin')")
    @Transactional
    public ResponseEntity createUser(@RequestBody OrganisationUserPutPostRequest request) {
        User user;
        OrganisationUser organisationUser;
        if (request.getId() > 0) {
            user = userService.getUser(request.getId());
        } else {
            user = userService.getUser(request.getEmail(), request.getMobile());
        }

        if (user == null) {
            String error = userService.validateNewUser(request.getEmail(), request.getMobile());
            if (error != null)
                return new ResponseEntity<>(error, HttpStatus.CONFLICT);
            user = userService.createUser(request.getName(), request.getEmail(), request.getMobile(), bCryptPasswordEncoder.encode(request.getPassword()), userService.getAppUser());
        }
        organisationUser = organisationUserService.associateExistingUser(user, request.getUserType(), request.getProviderType(), getCurrentOrganisation(), request.getLanguage());
        return new ResponseEntity<>(OrganisationUserResponse.from(organisationUser), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/organisationUser", method = {RequestMethod.POST})
    @PreAuthorize("hasRole('User')")
    public OrganisationUserResponse updateUser(@RequestBody OrganisationUserPutPostRequest request) {
        User user = userService.getUser(request.getId());
        OrganisationUser organisationUser = organisationUserService.update(user, request.getUserType(), request.getProviderType(), getCurrentOrganisation());
        return OrganisationUserResponse.from(organisationUser);
    }

    @PreAuthorize("hasAnyRole('User','OrgAdmin')")
    @RequestMapping(value = "/api/organisationUser/current", method = RequestMethod.GET)
    public OrganisationUserResponse getLoggedInUser(Principal principal) {
        try {
            String name = principal.getName();
            User user = userService.getUser(name);
            OrganisationUser organisationUser = organisationUserService.getOrganisationUser(user, getCurrentOrganisation());
            return CurrentUserResponse.from(organisationUser);
        } finally {
            logger.info("Returned user info");
        }
    }

    @PreAuthorize("hasRole('User')")
    @RequestMapping(value = "/api/organisationUser/current", method = RequestMethod.POST)
    @Transactional
    public OrganisationUserResponse updateProfile(@RequestBody OrganisationUserPutPostRequest request) {
        User user;
        if (StringUtils.hasText(request.getPassword())) {
            user = userService.updateProfile(request.getId(), request.getName(), request.getEmail(), request.getMobile(), bCryptPasswordEncoder.encode(request.getPassword()), request.getIdentification(), request.getQualification());
        } else {
            user = userService.updateProfile(request.getId(), request.getName(), request.getEmail(), request.getMobile(), request.getIdentification(), request.getQualification());
        }
        OrganisationUser organisationUser = organisationUserService.update(user, request.getUserType(), request.getProviderType(), getCurrentOrganisation());
        return OrganisationUserResponse.from(organisationUser);
    }

    @PreAuthorize("hasRole('User')")
    @RequestMapping(value = "/api/organisationUser/current/language", method = RequestMethod.PATCH)
    @Transactional
    public OrganisationUserResponse patchLanguage(@RequestBody OrganisationUserPutPostRequest request, Principal principal) {
        OrganisationUser organisationUser = userService.updateLanguagePreference(getCurrentUser(principal), getCurrentOrganisation(), request.getLanguage());
        return OrganisationUserResponse.from(organisationUser);
    }

    @GetMapping("/api/user")
    @PreAuthorize("hasRole('OrgAdmin')")
    public SearchedUserResponse getUser(@RequestParam(value = "userName") String userName) {
        User user = userService.getUser(userName);
        OrganisationUser ou = organisationUserService.getOrganisationUser(user, getCurrentOrganisation());
        SearchedUserResponse response = new SearchedUserResponse();
        response.setFound(user != null);
        response.setAlreadyPartOfOrganisation(ou != null);
        if (user != null) {
            response.setUserId(user.getId());
            response.setName(user.getName());
        }
        return response;
    }
}
