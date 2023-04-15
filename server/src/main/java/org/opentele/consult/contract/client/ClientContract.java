package org.opentele.consult.contract.client;

import org.opentele.consult.contract.framework.BaseAuditableEntityContract;
import org.opentele.consult.domain.client.Client;
import org.opentele.consult.domain.client.Gender;

import java.time.LocalDate;
import java.time.Period;

public class ClientContract extends BaseAuditableEntityContract {
    private String name;
    private String registrationNumber;
    private Gender gender;
    private Period age;
    private String otherDetails;
    private LocalDate dateOfBirth;

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

    public Period getAge() {
        return age;
    }

    public void setAge(Period age) {
        this.age = age;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    protected void populate(Client client) {
        super.populate(client);
        this.gender = client.getGender();
        this.name = client.getName();
        this.registrationNumber = client.getRegistrationNumber();
        this.age = client.getAge();
        this.otherDetails = client.getOtherDetails();
        this.dateOfBirth = client.getDateOfBirth();
    }
}
