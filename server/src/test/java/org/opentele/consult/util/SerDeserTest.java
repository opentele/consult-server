package org.opentele.consult.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomSchedule;

import java.time.LocalDate;
import java.time.LocalTime;

public class SerDeserTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void serialiseLocalTime() throws JsonProcessingException {
        ConsultationRoomSchedule consultationRoomSchedule = new ConsultationRoomSchedule();
        consultationRoomSchedule.setStartTime(LocalTime.NOON);
        consultationRoomSchedule.setStartDate(LocalDate.now());
        String s = objectMapper.writeValueAsString(consultationRoomSchedule);
    }
}
