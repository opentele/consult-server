package org.opentele.consult.domain.framework;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@MappedSuperclass
public abstract class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "uuid", updatable = false, unique = true, nullable = false, columnDefinition = "uuid default uuid_generate_v4()")
    private UUID uuid;

    @Column(name = "inactive", columnDefinition = "boolean default false", nullable = false)
    private boolean inactive;

    public Long getId() {
        return id;
    }

    public boolean isNew() {
        return this.id == null;
    }

    public AbstractEntity() {
        this.uuid = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (!getClass().equals(obj.getClass())) {
            return false;
        }

        AbstractEntity rhs = (AbstractEntity) obj;
        return this.id != null && (this.id.equals(rhs.getId()));
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        if (id == null) return super.hashCode();
        return id.hashCode();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Boolean getInactive() {
        return inactive;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }
}
