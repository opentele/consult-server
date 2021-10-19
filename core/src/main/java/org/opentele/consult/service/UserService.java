package org.opentele.consult.service;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.message.MessageCodes;
import org.opentele.consult.repository.OrganisationRepository;
import org.opentele.consult.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final OrganisationRepository organisationRepository;

    @Autowired
    public UserService(UserRepository userRepository, OrganisationRepository organisationRepository) {
        this.userRepository = userRepository;
        this.organisationRepository = organisationRepository;
    }

    public Set<String> validateNewOrganisation(String name, String email) {
        User user = userRepository.findByEmail(email);
        Set<String> errors = new HashSet<>();
        if (user != null) errors.add(MessageCodes.CREATE_OR_USER_EXISTS);
        return errors;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void createNewOrganisation(User user, Organisation organisation) {
        Organisation savedOrg = organisationRepository.save(organisation);
        user.setOrganisation(savedOrg);
        userRepository.save(user);
    }
}
