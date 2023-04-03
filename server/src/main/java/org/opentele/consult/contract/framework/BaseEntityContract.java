package org.opentele.consult.contract.framework;

import org.opentele.consult.domain.framework.AbstractEntity;

public class BaseEntityContract {
    private long id;

    public BaseEntityContract() {
    }

    public BaseEntityContract(long id) {
        this.id = id;
    }

    public BaseEntityContract(AbstractEntity abstractEntity) {
        this.id = abstractEntity.getId();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
