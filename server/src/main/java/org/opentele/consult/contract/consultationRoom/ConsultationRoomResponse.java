package org.opentele.consult.contract.consultationRoom;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ConsultationRoomResponse extends BaseConsultationRoomResponse {
    private LocalDateTime scheduledStartAt;
    private LocalDateTime scheduledEndAt;
    private LocalTime startedAt;
    private LocalTime endAt;
}
