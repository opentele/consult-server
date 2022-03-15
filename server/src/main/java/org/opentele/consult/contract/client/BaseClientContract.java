package org.opentele.consult.contract.client;

import org.opentele.consult.contract.framework.BaseEntityContract;
import org.opentele.consult.domain.client.Gender;

public class BaseClientContract extends BaseEntityContract {
    private String name;
    private String registrationNumber;
    private Gender gender;

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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
