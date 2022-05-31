package org.opentele.consult.mapper.consultationRoom;

import org.opentele.consult.contract.appointment.AppointmentDetailResponse;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomConferenceResponse;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomContract;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomDetailResponse;
import org.opentele.consult.contract.security.ProviderResponse;
import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.domain.teleconference.TeleConference;
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
        mapDetails(consultationRoom, user, response);
        return response;
    }

    private void mapDetails(ConsultationRoom consultationRoom, User user, ConsultationRoomDetailResponse response) {
        response.setNumberOfClients(consultationRoom.getNumberOfClients());
        ConsultationRoom.ConsultationRoomCurrentUserSummary summary = consultationRoomService.getCurrentSummaryFor(user, consultationRoom);
        response.setNumberOfUserClients(summary.getNumberOfClients());
        response.setNumberOfClientsPending(consultationRoom.getNumberOfPendingClients());
        response.setNumberOfUserClientsPending(consultationRoom.getNumberOfPendingUserClients(user));
        TeleConference activeTeleConference = consultationRoom.getActiveTeleConference();
        response.setActiveTeleConferenceId(activeTeleConference == null ? null : activeTeleConference.getJitsiId());
    }

    private void mapCore(ConsultationRoomContract contract, ConsultationRoom consultationRoom) {
        contract.setEndTime(consultationRoom.getEndTime());
        contract.setId(consultationRoom.getId());
        contract.setProviders(consultationRoom.getProviders().stream().map(x -> new ProviderResponse(x.getUser())).collect(Collectors.toList()));
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

    public ConsultationRoomConferenceResponse mapForConference(ConsultationRoom consultationRoom, User user) {
        ConsultationRoomConferenceResponse response = new ConsultationRoomConferenceResponse();
        mapCore(response, consultationRoom);
        mapDetails(consultationRoom, user, response);
        response.setAppointments(consultationRoom.getAppointmentInOrder().stream().map(AppointmentDetailResponse::fromEntity).collect(Collectors.toList()));
        return response;
    }
}
