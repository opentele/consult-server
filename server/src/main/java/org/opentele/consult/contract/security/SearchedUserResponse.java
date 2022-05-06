package org.opentele.consult.contract.security;

public class SearchedUserResponse {
    private int userId;
    private String name;
    private boolean alreadyPartOfOrganisation;
    private boolean found;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAlreadyPartOfOrganisation() {
        return alreadyPartOfOrganisation;
    }

    public void setAlreadyPartOfOrganisation(boolean alreadyPartOfOrganisation) {
        this.alreadyPartOfOrganisation = alreadyPartOfOrganisation;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
