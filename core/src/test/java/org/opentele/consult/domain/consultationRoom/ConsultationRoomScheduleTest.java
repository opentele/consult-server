package org.opentele.consult.domain.consultationRoom;

import org.dmfs.rfc5545.recur.InvalidRecurrenceRuleException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConsultationRoomScheduleTest {
    @Test
    void getConsultationRooms() throws InvalidRecurrenceRuleException {
        ConsultationRoomSchedule consultationRoomSchedule = new ConsultationRoomSchedule();
        consultationRoomSchedule.setStartTime(LocalTime.of(10, 30));
        consultationRoomSchedule.setEndTime(LocalTime.of(14, 45));
        consultationRoomSchedule.setRecurrenceRule("FREQ=WEEKLY;INTERVAL=1;BYDAY=MO,WE,TH");
        List<ConsultationRoom> consultationRooms = consultationRoomSchedule.getConsultationRooms(3, LocalDate.of(2019, 3, 10));
        assertEquals(LocalDate.of(2019, 3, 10), consultationRooms.get(0).getScheduledStartAt().toLocalDate());
        assertEquals(LocalDate.of(2019, 3, 11), consultationRooms.get(1).getScheduledStartAt().toLocalDate());
        assertEquals(LocalDate.of(2019, 3, 15), consultationRooms.get(2).getScheduledStartAt().toLocalDate());
    }
}
