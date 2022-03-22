package org.opentele.consult.contract.client;

import java.util.ArrayList;
import java.util.List;

public class ClientSearchResults {
    private int totalCount;
    private List<ClientSearchResponse> entities = new ArrayList<>();

    public ClientSearchResults() {
    }

    public ClientSearchResults(int totalCount, List<ClientSearchResponse> entities) {
        this.totalCount = totalCount;
        this.entities = entities;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<ClientSearchResponse> getEntities() {
        return entities;
    }

    public void setEntities(List<ClientSearchResponse> entities) {
        this.entities = entities;
    }
}
