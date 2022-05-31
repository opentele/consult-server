package org.opentele.consult.contract.client;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class ConsultationSessionRecordRequest extends ConsultationSessionRecordContract {
    private List<MultipartFile> files = new ArrayList<>();

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }
}
