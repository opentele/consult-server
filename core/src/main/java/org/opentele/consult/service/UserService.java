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
import java.util.Date;
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

    public String validateNewUser(String email, String mobile) {
        User userByEmail = userRepository.getUserByEmail(email);
        if (userByEmail != null) {
            return MessageCodes.CREATE_EMAIL_USER_EXISTS;
        }

        User userByMobile = userRepository.getUserByMobile(mobile);
        if (userByMobile != null) {
            return MessageCodes.CREATE_MOBILE_USER_EXISTS;
        }
        return null;
    }

    public OrganisationUser getOrganisationUser(int userId, Organisation currentOrganisation) {
        User user = this.getUser(userId);
        return organisationUserRepository.findByUserAndOrganisation(user, currentOrganisation);
    }

    public User getAppUser() {
        return getUser(User.AppUserName);
    }

    public void addUser(Organisation organisation, User user, UserType role, ProviderType providerType) {
        OrganisationUser organisationUser = organisationUserRepository.findByUserAndOrganisation(user, organisation);
        if (organisationUser == null) {
            organisationUser = new OrganisationUser();
            organisationUser.setUser(user);
            organisationUser.setOrganisation(organisation);
        }
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
        save(user, getAppUser());
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

    public List<OrganisationUser> getOrganisationUsers(Organisation organisation, ProviderType providerType) {
        if (providerType != null)
            return organisationUserRepository.findAllByOrganisationAndProviderTypeOrderByUserName(organisation, providerType);
        return organisationUserRepository.findAllByOrganisationOrderByUserName(organisation);
    }

    public Organisation getOrganisation(int organisationId) {
        return organisationRepository.findEntity(organisationId);
    }

    public List<OrganisationUser> findUsers(String searchParam, Organisation organisation) {
        return organisationUserRepository.findTop10ByUserEmailContainsOrUserMobileContainsAndOrganisation(searchParam, searchParam, organisation);
    }

    public User save(User user, User currentUser) {
        if (user.isNew()) {
            user.setCreatedBy(currentUser);
            user.setCreatedDate(new Date());
        }
        user.setLastModifiedBy(currentUser);
        user.setLastModifiedDate(new Date());
        return userRepository.save(user);
    }

    public User createUser(String name, String email, String mobile, String hashedPassword, User currentUser) {
        User user = new User();
        setFields(name, email, mobile, hashedPassword, user);
        return this.save(user, currentUser);
    }

    private void setFields(String name, String email, String mobile, String hashedPassword, User user) {
        setBasicUserFields(name, email, mobile, user);
        user.setPassword(hashedPassword);
    }

    private void setBasicUserFields(String name, String email, String mobile, User user) {
        user.setName(name);
        user.setEmail(email);
        user.setMobile(mobile);
    }

    public User updateProfile(int id, String name, String email, String mobile, String hashedPassword) {
        User user = userRepository.findUser(id);
        this.setFields(name, email, mobile, hashedPassword, user);
        return this.save(user, user);
    }

    public User updateProfile(int id, String name, String email, String mobile) {
        User user = userRepository.findUser(id);
        this.setBasicUserFields(name, email, mobile, user);
        return this.save(user, user);
    }
}
