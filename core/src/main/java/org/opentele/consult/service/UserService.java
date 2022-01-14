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
import java.util.UUID;

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

    public String validateNewOrganisation(String email, String mobile) {
        User userByEmail = userRepository.getUserByEmail(email);
        if (userByEmail != null) {
            return MessageCodes.CREATE_ORG_EMAIL_USER_EXISTS;
        }

        User userByMobile = userRepository.getUserByMobile(mobile);
        if (userByMobile != null) {
            return MessageCodes.CREATE_ORG_MOBILE_USER_EXISTS;
        }
        return null;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void createNewOrganisation(User user, Organisation organisation) {
        Organisation savedOrg = organisationRepository.save(organisation);
        user.setOrganisation(savedOrg);
        userRepository.save(user);
    }

    public PasswordResetToken createPasswordResetTokenForUser(User user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        passwordResetTokenRepositoryRepository.save(resetToken);
        return resetToken;
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

    public String validateResetPassword(String email, String mobile) {
        if (userRepository.findByEmail(email) == null && userRepository.findByMobile(mobile) == null)
            return MessageCodes.NO_USER_WITH_ID;
        return null;
    }

    public User getUser(String email, String mobile) {
        User byEmail = userRepository.getUserByEmail(email);
        if (byEmail != null)
            return byEmail;
        return userRepository.getUserByMobile(mobile);
    }
}
