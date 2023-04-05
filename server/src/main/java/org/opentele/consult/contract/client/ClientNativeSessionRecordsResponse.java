package org.opentele.consult.contract.client;

import org.opentele.consult.domain.client.Client;
import org.opentele.consult.util.DateTimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClientNativeSessionRecordsResponse extends ClientResponse {
    private List<ConsultationSessionRecordContract> consultationSessionRecords = new ArrayList<>();

    public static ClientNativeSessionRecordsResponse createForSessionRecords(Client client) {
        ClientNativeSessionRecordsResponse contract = from(client);
        mapChildren(client, contract);
        return contract;
    }

    public static ClientNativeSessionRecordsResponse from(Client client) {
        ClientNativeSessionRecordsResponse response = new ClientNativeSessionRecordsResponse();
        response.populate(client);
        return response;
    }

    public static void mapChildren(Client client, ClientNativeSessionRecordsResponse response) {
        response.setConsultationSessionRecords(client.getConsultationRecords().stream().map(ConsultationSessionRecordResponse::from).collect(Collectors.toList()));
    }

    public List<ConsultationSessionRecordContract> getConsultationSessionRecords() {
        return consultationSessionRecords;
    }

    public void setConsultationSessionRecords(List<ConsultationSessionRecordContract> consultationSessionRecords) {
        this.consultationSessionRecords = consultationSessionRecords;
    }
}
