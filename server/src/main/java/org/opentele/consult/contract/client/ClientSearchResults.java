package org.opentele.consult.contract.client;

import java.util.ArrayList;
import java.util.List;

public class ClientSearchResults {
    private int totalCount;
    private List<ClientSearchResponse> clients = new ArrayList<>();

    public ClientSearchResults() {
    }

    public ClientSearchResults(int totalCount, List<ClientSearchResponse> clients) {
        this.totalCount = totalCount;
        this.clients = clients;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<ClientSearchResponse> getClients() {
        return clients;
    }

    public void setClients(List<ClientSearchResponse> clients) {
        this.clients = clients;
    }
}
