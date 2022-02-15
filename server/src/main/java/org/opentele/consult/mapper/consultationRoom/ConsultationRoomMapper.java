package org.opentele.consult.mapper.consultationRoom;

import org.opentele.consult.contract.consultationRoom.ConsultationRoomResponse;
import org.opentele.consult.contract.security.ProviderResponse;
import org.opentele.consult.domain.consultationRoom.ConsultationRoom;

import java.util.stream.Collectors;

public class ConsultationRoomMapper {
    public static ConsultationRoomResponse map(ConsultationRoom consultationRoom) {
        ConsultationRoomResponse response = new ConsultationRoomResponse();
        response.setEndTime(consultationRoom.getEndTime());
        response.setId(consultationRoom.getId());
        response.setProviders(consultationRoom.getProviders().stream().map(x -> new ProviderResponse(x.getUser().getName(), x.getUser().getId())).collect(Collectors.toList()));
        response.setScheduledEndTime(consultationRoom.getScheduledEndTime());
        response.setScheduledStartTime(consultationRoom.getScheduledStartTime());
        response.setScheduledOn(consultationRoom.getScheduledOn());
        response.setTitle(consultationRoom.getTitle());
        response.setTotalSlots(consultationRoom.getTotalSlots());
        return response;
    }
}
