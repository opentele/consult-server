package org.opentele.consult.domain.security;

import org.opentele.consult.domain.framework.AbstractEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "role")
public class Role extends AbstractEntity {
    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "role_privilege", inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"), joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Privilege> privileges = new HashSet<>();

    public Set<Privilege> getPrivileges() {
        return privileges;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Used by the web app
    public List<Integer> getPrivilegeIds() {
        return this.getPrivileges().stream().map(AbstractEntity::getId).collect(Collectors.toList());
    }

    public void removePrivilege(Privilege privilege) {
        this.privileges.remove(privilege);
    }

    public void addPrivilege(Privilege privilege) {
        this.privileges.add(privilege);
    }

    @Override
    public String toString() {
        return String.format("name='%s'", name);
    }
}
