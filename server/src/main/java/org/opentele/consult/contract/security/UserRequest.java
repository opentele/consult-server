package org.opentele.consult.contract.security;

public class UserRequest extends UserContract {
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
