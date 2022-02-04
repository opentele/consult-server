package org.opentele.consult.contract.framework;

public class BaseResponse {
    private int id;

    public BaseResponse() {
    }

    public BaseResponse(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
