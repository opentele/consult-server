package org.opentele.consult.service;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.security.PasswordResetToken;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.message.MessageCodes;
import org.opentele.consult.repository.OrganisationRepository;
import org.opentele.consult.repository.PasswordResetTokenRepositoryRepository;
import org.opentele.consult.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final OrganisationRepository organisationRepository;
    private final PasswordResetTokenRepositoryRepository passwordResetTokenRepositoryRepository;

    @Autowired
    public UserService(UserRepository userRepository, OrganisationRepository organisationRepository, PasswordResetTokenRepositoryRepository passwordResetTokenRepositoryRepository) {
        this.userRepository = userRepository;
        this.organisationRepository = organisationRepository;
        this.passwordResetTokenRepositoryRepository = passwordResetTokenRepositoryRepository;
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

    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        passwordResetTokenRepositoryRepository.save(resetToken);
    }

    public PasswordResetToken.TokenStatus validateToken(String token) {
        final PasswordResetToken passToken = passwordResetTokenRepositoryRepository.findByToken(token);
        return isTokenFound(passToken) ? (isTokenExpired(passToken) ? PasswordResetToken.TokenStatus.Expired
                : PasswordResetToken.TokenStatus.Valid) : PasswordResetToken.TokenStatus.NotFound;
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }

    public void updatePassword(String token, String newPassword) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepositoryRepository.findByToken(token);
        User user = passwordResetToken.getUser();
        user.setPassword(newPassword);
        userRepository.save(user);
    }
}
