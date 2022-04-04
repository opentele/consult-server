package org.opentele.consult.service;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.consultationRoom.Appointment;
import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomSchedule;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.domain.teleconference.TeleConference;
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
    private final AppointmentService appointmentService;

    public ConsultationRoomService(ConsultationRoomScheduleRepository consultationRoomScheduleRepository, ConsultationRoomRepository consultationRoomRepository, AppointmentService appointmentService) {
        this.consultationRoomScheduleRepository = consultationRoomScheduleRepository;
        this.consultationRoomRepository = consultationRoomRepository;
        this.appointmentService = appointmentService;
    }

    public int schedule(int numberOfDays) {
        int roomsCreated = 0;
        List<ConsultationRoomSchedule> schedules = consultationRoomScheduleRepository.findAllBy();
        for (ConsultationRoomSchedule consultationRoomSchedule : schedules) {
            LocalDate localDate = LocalDate.now().minusDays(1);
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
        teleConference.setJitsiId(String.format("%s (%d)", consultationRoom.getTitle(), consultationRoom.getId()));
        teleConference.setOrganisation(consultationRoom.getOrganisation());
        Appointment firstAppointment = consultationRoom.getFirstAppointment();
        firstAppointment.setCurrent(true);
        consultationRoom.addTeleConference(teleConference);
        consultationRoomRepository.save(consultationRoom);
        return teleConference;
    }

    public void moveToNextToken(int consultationRoomId, User user, Organisation organisation) {
        ConsultationRoom consultationRoom = consultationRoomRepository.findEntity(consultationRoomId);
        Appointment currentAppointment = consultationRoom.getCurrentAppointment();

        Appointment nextToken = appointmentService.getNextToken(user, organisation, consultationRoom);

        currentAppointment.setCurrent(false);
        nextToken.setCurrent(true);
        consultationRoomRepository.save(consultationRoom);
    }

    public void moveToPreviousToken(int consultationRoomId, User user, Organisation organisation) {
        ConsultationRoom consultationRoom = consultationRoomRepository.findEntity(consultationRoomId);
        Appointment currentAppointment = consultationRoom.getCurrentAppointment();

        Appointment previousToken = appointmentService.getPreviousToken(user, organisation, consultationRoom);

        currentAppointment.setCurrent(false);
        previousToken.setCurrent(true);
        consultationRoomRepository.save(consultationRoom);
    }

    public void appointmentMoveDown(int consultationRoomId, int tokenId, User user, Organisation organisation) {
        ConsultationRoom consultationRoom = consultationRoomRepository.findEntity(consultationRoomId);
        Appointment appointment = consultationRoom.getAppointment(tokenId);
        int queueNumber = appointment.getQueueNumber();
        Appointment nextToken = appointmentService.getNextToken(user, organisation, consultationRoom, appointment);
        int nextQueueNumber = nextToken.getQueueNumber();

        appointment.setQueueNumber(nextQueueNumber);
        nextToken.setQueueNumber(queueNumber);
        consultationRoomRepository.save(consultationRoom);
    }

    public void appointmentMoveUp(int consultationRoomId, int tokenId, User user, Organisation organisation) {
        ConsultationRoom consultationRoom = consultationRoomRepository.findEntity(consultationRoomId);
        Appointment appointment = consultationRoom.getAppointment(tokenId);
        int queueNumber = appointment.getQueueNumber();
        Appointment previousToken = appointmentService.getPreviousToken(user, organisation, consultationRoom, appointment);
        int previousQueueNumber = previousToken.getQueueNumber();

        appointment.setQueueNumber(previousQueueNumber);
        previousToken.setQueueNumber(queueNumber);
        consultationRoomRepository.save(consultationRoom);
    }
}
