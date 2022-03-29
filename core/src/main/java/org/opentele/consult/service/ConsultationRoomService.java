package org.opentele.consult.service;

import org.opentele.consult.domain.consultationRoom.AppointmentToken;
import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomSchedule;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.domain.teleconference.TeleConference;
import org.opentele.consult.repository.AppointmentTokenRepository;
import org.opentele.consult.repository.ConsultationRoomRepository;
import org.opentele.consult.repository.ConsultationRoomScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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

    public int schedule(int numberOfDays) {
        int roomsCreated = 0;
        List<ConsultationRoomSchedule> schedules = consultationRoomScheduleRepository.findAllBy();
        for (ConsultationRoomSchedule consultationRoomSchedule : schedules) {
            LocalDate localDate = LocalDate.now();
            for (int i = 0; i < numberOfDays; i++) {
                if (create(localDate, consultationRoomSchedule)) roomsCreated++;
                localDate = localDate.plusDays(1);
            }
        }
        return roomsCreated;
    }

    @Transactional
    protected boolean create(LocalDate date, ConsultationRoomSchedule consultationRoomSchedule) {
        List<LocalDate> nextConsultationDates = consultationRoomSchedule.getNextConsultationDates(date, date);
        if (nextConsultationDates.size() == 1 && !consultationRoomRepository.existsByConsultationRoomScheduleAndScheduledOn(consultationRoomSchedule, date)) {
            ConsultationRoom consultationRoom = consultationRoomSchedule.createRoomFor(date);
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
        return summary;
    }

    public TeleConference setup(int consultationRoomId) {
        ConsultationRoom consultationRoom = consultationRoomRepository.findEntity(consultationRoomId);
        TeleConference activeTeleConference = consultationRoom.getActiveTeleConference();
        if (activeTeleConference != null) return activeTeleConference;

        TeleConference teleConference = new TeleConference();
        teleConference.setJitsiId(UUID.randomUUID().toString());
        teleConference.setOrganisation(consultationRoom.getOrganisation());
        AppointmentToken firstAppointment = consultationRoom.getFirstAppointment();
        firstAppointment.setCurrent(true);
        consultationRoom.addTeleConference(teleConference);
        consultationRoomRepository.save(consultationRoom);
        return teleConference;
    }

    public void moveToNextToken(int consultationRoomId) {
        ConsultationRoom consultationRoom = consultationRoomRepository.findEntity(consultationRoomId);
        AppointmentToken currentAppointmentToken = consultationRoom.getCurrentAppointmentToken();
        currentAppointmentToken.setCurrent(false);
        AppointmentToken nextToken = appointmentTokenRepository.getNextToken(consultationRoom, currentAppointmentToken.getQueueNumber());
        nextToken.setCurrent(true);
        consultationRoomRepository.save(consultationRoom);
    }

    public void moveToPreviousToken(int consultationRoomId) {
        ConsultationRoom consultationRoom = consultationRoomRepository.findEntity(consultationRoomId);
        AppointmentToken currentAppointmentToken = consultationRoom.getCurrentAppointmentToken();
        currentAppointmentToken.setCurrent(false);
        AppointmentToken previousToken = appointmentTokenRepository.getPreviousToken(consultationRoom, currentAppointmentToken.getQueueNumber());
        previousToken.setCurrent(true);
        consultationRoomRepository.save(consultationRoom);
    }
}
