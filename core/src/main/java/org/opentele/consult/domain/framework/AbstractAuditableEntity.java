package org.opentele.consult.domain.framework;

import org.opentele.consult.domain.security.User;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public abstract class AbstractAuditableEntity extends AbstractEntity {
    @CreatedBy
    @JoinColumn(name = "created_by_id", nullable = false)
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User createdBy;

    @LastModifiedBy
    @JoinColumn(name = "last_modified_by_id", nullable = false)
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User lastModifiedBy;

    @CreatedDate
    @Column(name = "created_date", updatable = false, nullable = false, columnDefinition = "timestamp default (now()):: timestamp without time zone")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", nullable = false, columnDefinition = "timestamp default (now()):: timestamp without time zone")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date lastModifiedDate;

    public User getCreatedBy() {
        return createdBy;
    }

    public User getLastModifiedBy() {
        return lastModifiedBy;
    }

    public java.util.Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public java.util.Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
