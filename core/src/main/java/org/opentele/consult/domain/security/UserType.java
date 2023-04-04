package org.opentele.consult.domain.security;

public enum UserType {
    Admin(4), OrgAdmin(2), User(1), NonOrgUser(0);

    private int accessLevel;

    UserType(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public int getAccessLevel() {
        return accessLevel;
    }
}
