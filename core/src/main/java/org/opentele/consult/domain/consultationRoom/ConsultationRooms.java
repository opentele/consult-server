package org.opentele.consult.domain.consultationRoom;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ConsultationRooms extends ArrayList<ConsultationRoom> {
    public ConsultationRooms() {
    }

    public ConsultationRooms(Collection<? extends ConsultationRoom> c) {
        super(c);
    }

    public List<Integer> getScheduleIds() {
        return this.stream().map(consultationRoom -> consultationRoom.getSchedule().getId()).collect(Collectors.toList());
    }

    public boolean isAlreadyScheduled(ConsultationRoomSchedule consultationRoomSchedule, LocalDate date) {
        ConsultationRoom consultationRoom = this.stream().filter(x -> x.getSchedule().equals(consultationRoomSchedule) && date.equals(x.getScheduledStartAt().toLocalDate())).findFirst().orElse(null);
        return consultationRoom != null;
    }
}
