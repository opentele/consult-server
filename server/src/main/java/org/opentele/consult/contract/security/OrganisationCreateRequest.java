package org.opentele.consult.contract.security;

import org.opentele.consult.domain.security.User;

public class OrganisationCreateRequest extends OrganisationUserContract {
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User toUser(String hashedPassword) {
        User user = new User();
        user.setPassword(hashedPassword);
        this.mapToUser(user);
        return user;
    }
}
