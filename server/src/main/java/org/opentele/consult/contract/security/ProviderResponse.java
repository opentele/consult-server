package org.opentele.consult.contract.security;

import org.opentele.consult.contract.framework.BaseResponse;

public class ProviderResponse extends BaseResponse {
    private String name;

    public ProviderResponse() {
    }

    public ProviderResponse(String name, int id) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
