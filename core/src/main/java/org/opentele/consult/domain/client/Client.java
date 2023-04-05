package org.opentele.consult.domain.client;

import org.opentele.consult.domain.framework.OrganisationalEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

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

    @Column(columnDefinition = "varchar(100)")
    private String registrationNumber;

    @Column(columnDefinition = "varchar(1000)")
    private String otherDetails;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "client")
    private Set<ConsultationRecord> consultationRecords = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "client")
    private Set<ConsultationFormRecord> consultationFormRecords = new HashSet<>();

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

    public Period getAge() {
        return Period.between(LocalDate.now(), this.getDateOfBirth());
    }

    public Set<ConsultationRecord> getConsultationRecords() {
        return consultationRecords;
    }

    public void setConsultationRecords(Set<ConsultationRecord> consultationRecords) {
        this.consultationRecords = consultationRecords;
    }

    public void addConsultationRecord(ConsultationRecord entity) {
        consultationRecords.add(entity);
        entity.setClient(this);
    }

    public void addConsultationFormRecord(ConsultationFormRecord entity) {
        consultationFormRecords.add(entity);
        entity.setClient(this);
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }

    public Set<ConsultationFormRecord> getConsultationFormRecords() {
        return consultationFormRecords;
    }
}
