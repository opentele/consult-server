package org.opentele.consult.contract.security;

import org.opentele.consult.contract.framework.BaseEntityContract;
import org.opentele.consult.domain.security.User;

public class UserContract extends BaseEntityContract {
    private String email;
    private String mobile;
    private String name;
    private String password;
    private String qualification;
    private String identification;

    public static UserContract from(User user) {
        UserContract userContract = new UserContract();
        from(user, userContract);
        return userContract;
    }

    public static void from(User user, UserContract userContract) {
        userContract.setId(user.getId());
        userContract.setName(user.getName());
        userContract.setMobile(user.getMobile());
        userContract.setEmail(user.getEmail());
        userContract.setIdentification(user.getIdentification());
        userContract.setQualification(user.getQualification());
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    protected void mapToUser(User user) {
        user.setMobile(mobile);
        user.setEmail(email);
        user.setName(name);
    }
}
