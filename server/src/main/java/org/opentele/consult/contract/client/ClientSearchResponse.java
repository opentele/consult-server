package org.opentele.consult.contract.client;

import org.opentele.consult.contract.framework.BaseEntityContract;

public class ClientSearchResponse extends BaseEntityContract {
    private String name;
    private String registrationNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
}
