package org.opentele.consult.contract.client;

import org.opentele.consult.contract.framework.BaseEntityResponse;
import org.opentele.consult.domain.client.ConsultationFormRecord;
import org.opentele.consult.domain.consultationRoom.ConsultationRoom;

public class ConsultationFormRecordResponse extends BaseEntityResponse {
    private String formId;
    private String data;
    private String consultationRoomTitle;
    private long consultationRoomId;

    public String getFormId() {
        return formId;
    }

    public String getData() {
        return data;
    }

    public String getConsultationRoomTitle() {
        return consultationRoomTitle;
    }

    public long getConsultationRoomId() {
        return consultationRoomId;
    }

    public static ConsultationFormRecordResponse create(ConsultationFormRecord consultationFormRecord) {
        ConsultationFormRecordResponse response = new ConsultationFormRecordResponse();
        response.populate(consultationFormRecord);
        ConsultationRoom consultationRoom = consultationFormRecord.getConsultationRoom();
        if (consultationRoom != null) {
            response.consultationRoomTitle = consultationRoom.getTitle();
            response.consultationRoomId = consultationRoom.getId();
        }
        response.formId = consultationFormRecord.getFormId();
        response.data = consultationFormRecord.getData();
        return response;
    }
}
