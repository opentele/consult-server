package org.opentele.consult.contract.framework;

import org.opentele.consult.domain.framework.AbstractEntity;

public class BaseEntityContract {
    private int id;

    public BaseEntityContract() {
    }

    public BaseEntityContract(int id) {
        this.id = id;
    }

    public BaseEntityContract(AbstractEntity abstractEntity) {
        this.id = abstractEntity.getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
