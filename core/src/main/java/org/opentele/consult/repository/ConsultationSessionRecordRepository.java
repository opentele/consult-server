package org.opentele.consult.repository;

import org.opentele.consult.domain.client.ConsultationSessionRecord;
import org.opentele.consult.repository.framework.AbstractRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultationSessionRecordRepository extends AbstractRepository<ConsultationSessionRecord> {
    ConsultationSessionRecord findByClientId(int clientId);
}
