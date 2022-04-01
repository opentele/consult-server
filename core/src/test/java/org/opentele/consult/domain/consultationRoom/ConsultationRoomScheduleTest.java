package org.opentele.consult.domain.consultationRoom;

import org.dmfs.rfc5545.recur.InvalidRecurrenceRuleException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConsultationRoomScheduleTest {
    @Test
    void getConsultationRooms() {
        ConsultationRoomSchedule consultationRoomSchedule = create("FREQ=WEEKLY;INTERVAL=1;BYDAY=MO,TU,WE,TH,FR,SA,SU");
        List<LocalDate> dates = consultationRoomSchedule.getNextConsultationDates(3, LocalDate.of(2022, 3, 30));
        assertEquals(LocalDate.of(2022, 3, 30), dates.get(0));
        assertEquals(LocalDate.of(2022, 3, 31), dates.get(1));
        assertEquals(LocalDate.of(2022, 4, 1), dates.get(2));
    }

    private ConsultationRoomSchedule create(String recurrenceRule) {
        ConsultationRoomSchedule consultationRoomSchedule = new ConsultationRoomSchedule();
        consultationRoomSchedule.setStartTime(LocalTime.of(10, 30));
        consultationRoomSchedule.setEndTime(LocalTime.of(14, 45));
        consultationRoomSchedule.setRecurrenceRule(recurrenceRule);
        return consultationRoomSchedule;
    }
}
