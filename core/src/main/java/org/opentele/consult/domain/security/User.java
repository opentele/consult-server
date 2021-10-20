package org.opentele.consult.domain.security;

import org.opentele.consult.domain.framework.AbstractEntity;
import org.opentele.consult.domain.framework.OrganisationalEntity;
import org.springframework.data.annotation.Transient;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User extends OrganisationalEntity {
    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password")
    @Transient
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public Set<Role> getRoles() {
        return this.roles == null ? new HashSet<>() : this.roles;
    }

    public List<Integer> getRoleIds() {
        Set<Role> roles = this.getRoles() == null ? new HashSet<>() : this.getRoles();
        return roles.stream().map(AbstractEntity::getId).collect(Collectors.toList());
    }

    // Used by web app to get the privileges for the user
    public Set<Privilege> getPrivileges() {
        Set<Privilege> privileges = new HashSet<>();
        roles.forEach(role -> {
            privileges.addAll(role.getPrivileges());
        });
        return privileges;
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
