package org.opentele.consult.mapper.consultationRoom;

import org.opentele.consult.contract.consultationRoom.ConsultationRoomContract;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomDetailResponse;
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

    public ConsultationRoomDetailResponse map(ConsultationRoom consultationRoom, User user) {
        ConsultationRoomDetailResponse response = new ConsultationRoomDetailResponse();
        mapCore(response, consultationRoom);
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

    private static void mapCore(ConsultationRoomContract contract, ConsultationRoom consultationRoom) {
        contract.setEndTime(consultationRoom.getEndTime());
        contract.setId(consultationRoom.getId());
        contract.setProviders(consultationRoom.getProviders().stream().map(x -> new ProviderResponse(x.getUser().getName(), x.getUser().getId())).collect(Collectors.toList()));
        contract.setScheduledEndTime(consultationRoom.getScheduledEndTime());
        contract.setScheduledStartTime(consultationRoom.getScheduledStartTime());
        contract.setScheduledOn(consultationRoom.getScheduledOn());
        contract.setTitle(consultationRoom.getTitle());
        contract.setTotalSlots(consultationRoom.getTotalSlots());
    }

    public ConsultationRoomContract map(ConsultationRoom consultationRoom) {
        ConsultationRoomContract contract = new ConsultationRoomContract();
        mapCore(contract, consultationRoom);
        return contract;
    }
}
