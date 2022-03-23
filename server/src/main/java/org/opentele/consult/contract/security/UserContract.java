package org.opentele.consult.contract.security;

import org.opentele.consult.contract.framework.BaseEntityContract;
import org.opentele.consult.domain.security.User;

public class UserContract extends BaseEntityContract {
    private String email;
    private String mobile;
    private String name;

    public static UserContract from(User user) {
        UserContract userContract = new UserContract();
        userContract.setName(user.getName());
        userContract.setMobile(user.getMobile());
        userContract.setEmail(user.getEmail());
        return userContract;
    }

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
}
