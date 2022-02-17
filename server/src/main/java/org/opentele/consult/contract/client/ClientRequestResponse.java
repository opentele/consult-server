package org.opentele.consult.contract.client;

import org.opentele.consult.contract.framework.BaseEntityContract;
import org.opentele.consult.domain.client.Gender;

import java.time.LocalDate;

public class ClientRequestResponse extends BaseEntityContract {
    private int id;
    private String name;
    private String registrationNumber;
    private LocalDate dateOfBirth;
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
