package org.opentele.consult.service;

import org.opentele.consult.domain.client.Client;
import org.opentele.consult.domain.consultationRoom.AppointmentToken;
import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomSchedule;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.repository.AppointmentTokenRepository;
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
    private final AppointmentTokenRepository appointmentTokenRepository;

    public ConsultationRoomService(ConsultationRoomScheduleRepository consultationRoomScheduleRepository, ConsultationRoomRepository consultationRoomRepository, AppointmentTokenRepository appointmentTokenRepository) {
        this.consultationRoomScheduleRepository = consultationRoomScheduleRepository;
        this.consultationRoomRepository = consultationRoomRepository;
        this.appointmentTokenRepository = appointmentTokenRepository;
    }

    public int schedule() {
        int roomsCreated = 0;
        LocalDate today = LocalDate.now();
        List<ConsultationRoomSchedule> schedules = consultationRoomScheduleRepository.findAllBy();
        for (ConsultationRoomSchedule consultationRoomSchedule : schedules) {
            if (create(today, consultationRoomSchedule)) roomsCreated++;
        }
        return roomsCreated;
    }

    @Transactional
    protected boolean create(LocalDate today, ConsultationRoomSchedule consultationRoomSchedule) {
        List<LocalDate> nextConsultationDates = consultationRoomSchedule.getNextConsultationDates(today, today);
        if (nextConsultationDates.size() == 1 && !consultationRoomRepository.existsByConsultationRoomScheduleAndScheduledOn(consultationRoomSchedule, today)) {
            ConsultationRoom consultationRoom = consultationRoomSchedule.createRoomFor(today);
            consultationRoomRepository.save(consultationRoom);
            return true;
        } else if (nextConsultationDates.size() > 1) {
            throw new RuntimeException("Issue in getting next consultation dates");
        }
        return false;
    }

    public ConsultationRoom.ConsultationRoomCurrentUserSummary getCurrentSummaryFor(User user, ConsultationRoom consultationRoom) {
        ConsultationRoom.ConsultationRoomCurrentUserSummary summary = new ConsultationRoom.ConsultationRoomCurrentUserSummary();
        summary.setNumberOfClients(consultationRoom.getNumberOfClients(user));
        AppointmentToken nextToken;
        if (consultationRoom.isProvider(user)) {
            nextToken = appointmentTokenRepository.findFirstByConsultationRoomAndQueueNumberGreaterThanOrderByQueueNumber(consultationRoom, consultationRoom.getCurrentQueueNumber());
        } else {
            nextToken = appointmentTokenRepository.findFirstByConsultationRoomAndQueueNumberGreaterThanAndAppointmentProviderOrderByQueueNumber(consultationRoom, consultationRoom.getCurrentQueueNumber(), user);
        }
        if (nextToken != null)
            summary.setNextClient(nextToken.getClient());
        return summary;
    }
}
