package org.opentele.consult.mapper.consultationRoom;

import org.opentele.consult.contract.consultationRoom.ConsultationRoomScheduleRequest;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomScheduleResponse;
import org.opentele.consult.contract.security.ProviderResponse;
import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomSchedule;
import org.opentele.consult.domain.security.OrganisationUser;
import org.opentele.consult.domain.security.User;

import java.util.stream.Collectors;

public class ConsultationRoomScheduleMapper {
    public static void map(ConsultationRoomScheduleRequest request, Organisation organisation, ConsultationRoomSchedule consultationRoomSchedule) {
        consultationRoomSchedule.setEndTime(request.getEndTime());
        consultationRoomSchedule.setOrganisation(organisation);
        consultationRoomSchedule.setRecurrenceRule(request.getRecurrenceRule());
        consultationRoomSchedule.setStartDate(request.getStartDate());
        consultationRoomSchedule.setStartTime(request.getStartTime());
        consultationRoomSchedule.setTitle(request.getTitle());
        consultationRoomSchedule.setTotalSlots(request.getTotalSlots());
    }

    public static ConsultationRoomScheduleResponse map(ConsultationRoomSchedule consultationRoomSchedule) {
        ConsultationRoomScheduleResponse response = new ConsultationRoomScheduleResponse();
        response.setEndTime(consultationRoomSchedule.getEndTime());
        response.setId(consultationRoomSchedule.getId());
        response.setRecurrenceRule(consultationRoomSchedule.getRecurrenceRule());
        response.setStartDate(consultationRoomSchedule.getStartDate());
        response.setStartTime(consultationRoomSchedule.getStartTime());
        response.setTitle(consultationRoomSchedule.getTitle());
        response.setTotalSlots(consultationRoomSchedule.getTotalSlots());
        response.setProviders(consultationRoomSchedule.getProviders().stream().map(x -> new ProviderResponse(x.getUser().getName(), x.getUser().getId())).collect(Collectors.toList()));
        return response;
    }
}
