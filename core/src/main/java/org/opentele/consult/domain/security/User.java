package org.opentele.consult.domain.security;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.framework.AbstractAuditableEntity;
import org.opentele.consult.domain.framework.AbstractEntity;
import org.springframework.data.annotation.*;
import org.springframework.data.annotation.Transient;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends AbstractEntity implements Serializable {
    public static final String AppUserName = "server@opentele.org";

    @Column(name = "email")
    private String email;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "password", nullable = false)
    @Transient
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "user")
    private Set<OrganisationUser> organisationUsers = new HashSet<>();

    @JoinColumn(name = "created_by_id", nullable = false)
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User createdBy;

    @JoinColumn(name = "last_modified_by_id", nullable = false)
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User lastModifiedBy;

    @Column(name = "created_date", updatable = false, nullable = false, columnDefinition = "timestamp default (now()):: timestamp without time zone")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdDate;

    @Column(name = "last_modified_date", nullable = false, columnDefinition = "timestamp default (now()):: timestamp without time zone")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date lastModifiedDate;

    @Column
    private String qualification;

    @Column
    private String identification;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProviderType getProviderType(Organisation organisation) {
        OrganisationUser organisationUser = this.organisationUsers.stream().filter(ou -> ou.getOrganisation().equals(organisation)).findFirst().orElse(null);
        return organisationUser.getProviderType();
    }

    public void addProviderType(ProviderType providerType, UserType userType, Organisation organisation) {
        OrganisationUser organisationUser = new OrganisationUser();
        organisationUser.setProviderType(providerType);
        organisationUser.setOrganisation(organisation);
        organisationUser.setUserType(userType);
        this.organisationUsers.add(organisationUser);
        organisationUser.setUser(this);
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public void setLastModifiedBy(User lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getDetailsForClient() {
        return String.format("%s - %s [%s]", this.name, this.identification, this.qualification);
    }
}
