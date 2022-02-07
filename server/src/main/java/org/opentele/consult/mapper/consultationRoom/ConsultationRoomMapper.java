package org.opentele.consult.mapper.consultationRoom;

import org.opentele.consult.contract.consultationRoom.ConsultationRoomResponse;
import org.opentele.consult.contract.security.ProviderResponse;
import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomSchedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ConsultationRoomMapper {
    public static ConsultationRoomResponse map(ConsultationRoom consultationRoom) {
        ConsultationRoomResponse response = new ConsultationRoomResponse();
        response.setEndAt(consultationRoom.getEndedAt());
        response.setId(consultationRoom.getId());
        response.setProviders(consultationRoom.getProviders().stream().map(x -> new ProviderResponse(x.getUser().getName(), x.getUser().getId())).collect(Collectors.toList()));
        response.setScheduledEndAt(consultationRoom.getScheduledEndAt());
        response.setScheduledStartAt(consultationRoom.getScheduledStartAt());
        response.setStartedAt(consultationRoom.getStartedAt());
        response.setTitle(consultationRoom.getTitle());
        response.setTotalSlots(consultationRoom.getTotalSlots());
        return response;
    }

    public static ConsultationRoomResponse map(ConsultationRoomSchedule consultationRoomSchedule, LocalDate date) {
        ConsultationRoomResponse response = new ConsultationRoomResponse();
        List<ProviderResponse> providers = consultationRoomSchedule.getProviders().stream().map(x -> new ProviderResponse(x.getUser().getName(), x.getUser().getId())).collect(Collectors.toList());
        response.setProviders(providers);
        response.setScheduledEndAt(LocalDateTime.of(date, consultationRoomSchedule.getEndTime()));
        response.setScheduledStartAt(LocalDateTime.of(date, consultationRoomSchedule.getStartTime()));
        response.setTitle(consultationRoomSchedule.getTitle());
        response.setTotalSlots(consultationRoomSchedule.getTotalSlots());
        return response;
    }
}
