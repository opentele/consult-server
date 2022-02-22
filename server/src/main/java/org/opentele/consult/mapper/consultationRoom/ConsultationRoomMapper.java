package org.opentele.consult.mapper.consultationRoom;

import org.opentele.consult.contract.consultationRoom.ConsultationRoomResponse;
import org.opentele.consult.contract.security.ProviderResponse;
import org.opentele.consult.domain.client.Client;
import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.service.ConsultationRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ConsultationRoomMapper {
    private final ConsultationRoomService consultationRoomService;

    @Autowired
    public ConsultationRoomMapper(ConsultationRoomService consultationRoomService) {
        this.consultationRoomService = consultationRoomService;
    }

    public ConsultationRoomResponse map(ConsultationRoom consultationRoom, User user) {
        ConsultationRoomResponse response = new ConsultationRoomResponse();
        response.setEndTime(consultationRoom.getEndTime());
        response.setId(consultationRoom.getId());
        response.setProviders(consultationRoom.getProviders().stream().map(x -> new ProviderResponse(x.getUser().getName(), x.getUser().getId())).collect(Collectors.toList()));
        response.setScheduledEndTime(consultationRoom.getScheduledEndTime());
        response.setScheduledStartTime(consultationRoom.getScheduledStartTime());
        response.setScheduledOn(consultationRoom.getScheduledOn());
        response.setTitle(consultationRoom.getTitle());
        response.setTotalSlots(consultationRoom.getTotalSlots());
        response.setNumberOfClients(consultationRoom.getNumberOfClients());
        ConsultationRoom.ConsultationRoomCurrentUserSummary summary = consultationRoomService.getCurrentSummaryFor(user, consultationRoom);
        response.setNumberOfUserClients(summary.getNumberOfClients());
        Client nextClient = summary.getNextClient();
        if (nextClient != null)
            response.setNextClient(nextClient.getName());
        response.setNumberOfClientsPending(consultationRoom.getNumberOfPendingClients());
        response.setNumberOfUserClientsPending(consultationRoom.getNumberOfPendingUserClients(user));
        return response;
    }
}
