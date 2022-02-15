package org.opentele.consult.service;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.security.PasswordResetToken;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.message.MessageCodes;
import org.opentele.consult.repository.OrganisationRepository;
import org.opentele.consult.repository.PasswordResetTokenRepository;
import org.opentele.consult.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final OrganisationRepository organisationRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    public UserService(UserRepository userRepository, OrganisationRepository organisationRepository, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.userRepository = userRepository;
        this.organisationRepository = organisationRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
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
        List<PasswordResetToken> previousTokens = passwordResetTokenRepository.findAllByUserAndInactiveFalse(user);
        previousTokens.forEach(passwordResetToken -> passwordResetToken.setInactive(true));
        passwordResetTokenRepository.saveAll(previousTokens);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(resetToken);
        return resetToken;
    }

    public PasswordResetToken.TokenStatus validateToken(String token) {
        final PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token);
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
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
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

    public void deleteOrganisation(String orgName) {
        List<User> users = userRepository.findAllByOrganisationName(orgName);
        userRepository.deleteAll(users);
        organisationRepository.delete(organisationRepository.findByName(orgName));
    }

    public User getUser(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User getUser(Principal principal) {
        String name = principal.getName();
        return getUser(name, name);
    }

    public Organisation getOrganisation(Principal principal) {
        User user = getUser(principal);
        return user.getOrganisation();
    }
}
