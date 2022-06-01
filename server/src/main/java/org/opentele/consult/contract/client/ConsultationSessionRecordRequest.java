package org.opentele.consult.contract.client;

import java.util.ArrayList;
import java.util.List;

public class ConsultationSessionRecordRequest extends ConsultationSessionRecordContract {
    private List<ConsultationSessionRecordFileContract> files = new ArrayList<>();

    public List<ConsultationSessionRecordFileContract> getFiles() {
        return files;
    }

    public void setFiles(List<ConsultationSessionRecordFileContract> files) {
        this.files = files;
    }
}
