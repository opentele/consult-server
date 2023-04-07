package org.opentele.consult.contract.framework;

import org.opentele.consult.domain.framework.OrganisationalEntity;
import org.opentele.consult.util.DateTimeUtil;

import java.time.LocalDateTime;

public class BaseEntityResponse extends BaseEntityContract {
    private long createdById;
    private String createdBy;
    private long lastModifiedById;
    private String lastModifiedBy;
    private LocalDateTime createdDateTime;
    private LocalDateTime lastModifiedDateTime;

    public long getCreatedById() {
        return createdById;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public long getLastModifiedById() {
        return lastModifiedById;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public LocalDateTime getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    protected void populate(OrganisationalEntity organisationalEntity) {
        super.populate(organisationalEntity);
        this.createdById = organisationalEntity.getCreatedBy().getId();
        this.createdBy = organisationalEntity.getCreatedBy().getName();
        this.lastModifiedById = organisationalEntity.getLastModifiedBy().getId();
        this.lastModifiedBy = organisationalEntity.getLastModifiedBy().getName();
        this.createdDateTime = LocalDateTime.from(DateTimeUtil.fromDate(organisationalEntity.getCreatedDate()));
        this.lastModifiedDateTime = LocalDateTime.from(DateTimeUtil.fromDate(organisationalEntity.getLastModifiedDate()));
    }
}
