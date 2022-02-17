package org.opentele.consult.contract.framework;

public class BaseEntityContract {
    private int id;

    public BaseEntityContract() {
    }

    public BaseEntityContract(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
