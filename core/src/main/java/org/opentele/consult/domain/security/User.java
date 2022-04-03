package org.opentele.consult.domain.security;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.framework.AbstractEntity;
import org.springframework.data.annotation.Transient;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends AbstractEntity {
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
}
