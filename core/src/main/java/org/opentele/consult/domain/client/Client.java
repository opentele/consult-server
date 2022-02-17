package org.opentele.consult.domain.client;

import org.opentele.consult.domain.framework.OrganisationalEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "client")
public class Client extends OrganisationalEntity {
    @Column
    private String name;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(columnDefinition = "varchar(100) not null")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(columnDefinition = "varchar(100) not null")
    private String registrationNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
}
