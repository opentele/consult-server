package org.opentele.consult.service;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.security.*;
import org.opentele.consult.message.MessageCodes;
import org.opentele.consult.repository.OrganisationRepository;
import org.opentele.consult.repository.OrganisationUserRepository;
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
    private final OrganisationUserRepository organisationUserRepository;

    @Autowired
    public UserService(UserRepository userRepository, OrganisationRepository organisationRepository, PasswordResetTokenRepository passwordResetTokenRepository, OrganisationUserRepository organisationUserRepository) {
        this.userRepository = userRepository;
        this.organisationRepository = organisationRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.organisationUserRepository = organisationUserRepository;
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

    public OrganisationUser getOrganisationUser(String userName, Organisation currentOrganisation) {
        User user = this.getUser(userName);
        return organisationUserRepository.findByUserAndOrganisation(user, currentOrganisation);
    }

    public OrganisationUser getOrganisationUser(int userId, Organisation currentOrganisation) {
        User user = this.getUser(userId);
        return organisationUserRepository.findByUserAndOrganisation(user, currentOrganisation);
    }

    public void createNewOrganisation(User user, Organisation organisation) {
        Organisation savedOrg = organisationRepository.save(organisation);
        User savedUser = userRepository.save(user);
        addUser(savedOrg, savedUser, UserType.OrgAdmin, ProviderType.Consultant);
    }

    public void addUser(Organisation savedOrg, User savedUser, UserType role, ProviderType providerType) {
        OrganisationUser organisationUser = new OrganisationUser();
        organisationUser.setUser(savedUser);
        organisationUser.setOrganisation(savedOrg);
        organisationUser.setUserType(role);
        organisationUser.setProviderType(providerType);
        organisationUserRepository.save(organisationUser);
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

    public User getUser(String userName) {
        return getUser(userName, userName);
    }

    public void deleteOrganisation(String orgName) {
        List<OrganisationUser> organisationUsers = organisationUserRepository.findAllByOrganisationName(orgName);
        organisationUserRepository.deleteAll(organisationUsers);
        List<User> usersWithNoOrganisation = userRepository.findUsersWithNoOrganisation();
        userRepository.deleteAll(usersWithNoOrganisation);
        organisationRepository.delete(organisationRepository.findByName(orgName));
    }

    public User getUser(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User getUser(Principal principal) {
        String name = principal.getName();
        return getUser(name, name);
    }

    public UserType getUserType(String userId, int organisationId) {
        User user = this.getUser(userId);
        OrganisationUser organisationUser = organisationUserRepository.findByUserAndOrganisationId(user, organisationId);
        if (organisationUser == null)
            throw new RuntimeException(String.format("User doesn't belong to this organisation: %d", organisationId));
        return organisationUser.getUserType();
    }

    public OrganisationUser getOrganisationUser(String email, String mobile) {
        User user = this.getUser(email, mobile);
        List<OrganisationUser> organisationUsers = organisationUserRepository.findAllByUser(user);
        if (organisationUsers.size() != 1)
            return null;

        return organisationUsers.get(0);
    }

    public List<OrganisationUser> getOrganisationUsers(String userId) {
        User user = this.getUser(userId);
        return organisationUserRepository.findAllByUser(user);
    }

    public Organisation getOrganisation(int organisationId) {
        return organisationRepository.findEntity(organisationId);
    }

    public List<OrganisationUser> findUsers(String searchParam) {
        return organisationUserRepository.findTop10ByUserEmailContainsOrUserMobileContains(searchParam, searchParam);
    }
}
