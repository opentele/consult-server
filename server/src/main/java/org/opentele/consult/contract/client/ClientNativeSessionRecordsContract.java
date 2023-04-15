package org.opentele.consult.contract.client;

import org.opentele.consult.domain.client.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClientNativeSessionRecordsContract extends ClientContract {
    private List<ConsultationSessionRecordContract> consultationSessionRecords = new ArrayList<>();

    public static ClientNativeSessionRecordsContract createForSessionRecords(Client client) {
        ClientNativeSessionRecordsContract contract = from(client);
        mapChildren(client, contract);
        return contract;
    }

    public static ClientNativeSessionRecordsContract from(Client client) {
        ClientNativeSessionRecordsContract response = new ClientNativeSessionRecordsContract();
        response.populate(client);
        return response;
    }

    public static void mapChildren(Client client, ClientNativeSessionRecordsContract response) {
        response.setConsultationSessionRecords(client.getConsultationRecords().stream().map(ConsultationSessionRecordResponse::from).collect(Collectors.toList()));
    }

    public List<ConsultationSessionRecordContract> getConsultationSessionRecords() {
        return consultationSessionRecords;
    }

    public void setConsultationSessionRecords(List<ConsultationSessionRecordContract> consultationSessionRecords) {
        this.consultationSessionRecords = consultationSessionRecords;
    }
}
