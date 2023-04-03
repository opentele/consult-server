package org.opentele.consult.repository;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.client.ConsultationRecord;
import org.opentele.consult.repository.framework.AbstractRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultationSessionRecordRepository extends AbstractRepository<ConsultationRecord> {
    ConsultationRecord findByClientIdAndOrganisation(long clientId, Organisation organisation);
    ConsultationRecord findByFilesFileName(String fileName);
    ConsultationRecord findByFilesId(long id);
}
