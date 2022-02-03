package org.opentele.consult.contract.consultationRoom;

import java.time.LocalTime;

public class ConsultationRoomScheduleResponse extends BaseConsultationRoomResponse {
    private String recurrenceRule;
    private LocalTime startTime;
    private LocalTime endTime;
}
