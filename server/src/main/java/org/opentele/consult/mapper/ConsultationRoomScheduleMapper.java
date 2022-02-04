package org.opentele.consult.mapper;

import org.opentele.consult.contract.consultationRoom.ConsultationRoomScheduleRequest;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomScheduleResponse;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomSchedule;
import org.opentele.consult.domain.security.User;

public class ConsultationRoomScheduleMapper {
    public static ConsultationRoomSchedule mapNew(ConsultationRoomScheduleRequest request, User user) {
        ConsultationRoomSchedule consultationRoomSchedule = new ConsultationRoomSchedule();
        consultationRoomSchedule.setTitle(request.getRecurrenceRule());
        consultationRoomSchedule.setRecurrenceRule(request.getRecurrenceRule());
        consultationRoomSchedule.setStartTime(request.getStartTime());
        consultationRoomSchedule.setEndTime(request.getEndTime());
        consultationRoomSchedule.setOrganisation(user.getOrganisation());
        consultationRoomSchedule.setStartDate(request.getStartDate());
        consultationRoomSchedule.setTotalSlots(request.getTotalSlots());
        return consultationRoomSchedule;
    }

    public static ConsultationRoomScheduleResponse map(ConsultationRoomSchedule consultationRoomSchedule) {
        ConsultationRoomScheduleResponse response = new ConsultationRoomScheduleResponse();
        response.setId(consultationRoomSchedule.getId());
        response.setEndTime(consultationRoomSchedule.getEndTime());
        response.setStartTime(consultationRoomSchedule.getStartTime());
        response.setRecurrenceRule(consultationRoomSchedule.getRecurrenceRule());
        return response;
    }
}
