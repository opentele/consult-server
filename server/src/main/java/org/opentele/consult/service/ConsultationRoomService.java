package org.opentele.consult.service;

import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomSchedule;
import org.opentele.consult.repository.ConsultationRoomRepository;
import org.opentele.consult.repository.ConsultationRoomScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ConsultationRoomService {
    private final ConsultationRoomScheduleRepository consultationRoomScheduleRepository;
    private final ConsultationRoomRepository consultationRoomRepository;

    public ConsultationRoomService(ConsultationRoomScheduleRepository consultationRoomScheduleRepository, ConsultationRoomRepository consultationRoomRepository) {
        this.consultationRoomScheduleRepository = consultationRoomScheduleRepository;
        this.consultationRoomRepository = consultationRoomRepository;
    }

    public void schedule() {
        LocalDate today = LocalDate.now();
        List<ConsultationRoomSchedule> schedules = consultationRoomScheduleRepository.findAllBy();
        schedules.forEach(consultationRoomSchedule -> create(today, consultationRoomSchedule));
    }

    @Transactional
    protected void create(LocalDate today, ConsultationRoomSchedule consultationRoomSchedule) {
        List<LocalDate> nextConsultationDates = consultationRoomSchedule.getNextConsultationDates(today, today);
        if (nextConsultationDates.size() == 1 && !consultationRoomRepository.existsByConsultationRoomScheduleAndScheduledOn(consultationRoomSchedule, today)) {
            ConsultationRoom consultationRoom = consultationRoomSchedule.schedule(today);
            consultationRoomRepository.save(consultationRoom);
        }
    }
}
