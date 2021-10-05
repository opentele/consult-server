package org.opentele.consult.controller.web;

import org.opentele.consult.contract.ApplicationStatus;
import org.opentele.consult.contract.OrganisationCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
public class SecurityController {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public SecurityController(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @RequestMapping(value = "/web/organisation", method = {RequestMethod.POST, RequestMethod.PUT})
    @Transactional
    public ResponseEntity<ApplicationStatus> createOrganisation(OrganisationCreateRequest request) {
        return null;
    }
}
