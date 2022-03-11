package org.opentele.consult.contract.security;

import org.opentele.consult.domain.security.OrganisationUser;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.domain.security.UserType;

public class UserContract {
    private String email;
    private String mobile;
    private String name;
    private UserType userType;
    private String organisationName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public static UserContract create(OrganisationUser organisationUser) {
        UserContract userContract = new UserContract();
        User user = organisationUser.getUser();
        userContract.setEmail(user.getEmail());
        userContract.setMobile(user.getMobile());
        userContract.setUserType(organisationUser.getUserType());
        userContract.setName(user.getName());
        return userContract;
    }
}
