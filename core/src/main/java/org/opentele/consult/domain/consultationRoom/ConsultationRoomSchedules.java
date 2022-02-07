package org.opentele.consult.domain.consultationRoom;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class ConsultationRoomSchedules extends ArrayList<ConsultationRoomSchedule> {
    public ConsultationRoomSchedules(Collection<? extends ConsultationRoomSchedule> c) {
        super(c);
    }

    public Map<ConsultationRoomSchedule, List<LocalDate>> getScheduledDates(LocalDateTime today) {
        Map<ConsultationRoomSchedule, List<LocalDate>> map = new HashMap<>();
        this.forEach(consultationRoomSchedule -> map.put(consultationRoomSchedule, consultationRoomSchedule.getNextConsultationDates(1, today.toLocalDate())));
        return map;
    }
}
