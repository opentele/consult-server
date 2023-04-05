package org.opentele.consult.contract.client;

import org.opentele.consult.domain.client.Client;
import org.opentele.consult.domain.client.ConsultationFormRecord;
import org.opentele.consult.domain.framework.AbstractAuditableEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClientFormSessionRecordsResponse extends ClientResponse {
    private final List<ConsultationFormRecordResponse> formRecordResponses = new ArrayList<>();

    public static ClientFormSessionRecordsResponse createForRecentForms(Client client) {
        ClientFormSessionRecordsResponse response = new ClientFormSessionRecordsResponse();
        response.populate(client);
        List<ConsultationFormRecord> records = client.getConsultationFormRecords().stream().sorted(Comparator.comparing(AbstractAuditableEntity::getCreatedDate)).limit(5).toList();
        response.formRecordResponses.addAll(records.stream().map(ConsultationFormRecordResponse::create).toList());
        return response;
    }
}
