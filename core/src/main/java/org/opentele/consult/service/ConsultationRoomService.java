package org.opentele.consult.service;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.client.Client;
import org.opentele.consult.domain.consultationRoom.Appointment;
import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomSchedule;
import org.opentele.consult.domain.framework.AbstractEntity;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.domain.teleconference.TeleConference;
import org.opentele.consult.repository.ClientRepository;
import org.opentele.consult.repository.ConsultationRoomRepository;
import org.opentele.consult.repository.ConsultationRoomScheduleRepository;
import org.opentele.consult.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultationRoomService {
    private final ConsultationRoomScheduleRepository consultationRoomScheduleRepository;
    private final ConsultationRoomRepository consultationRoomRepository;
    private final AppointmentService appointmentService;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    public ConsultationRoomService(ConsultationRoomScheduleRepository consultationRoomScheduleRepository, ConsultationRoomRepository consultationRoomRepository, AppointmentService appointmentService, UserRepository userRepository, ClientRepository clientRepository) {
        this.consultationRoomScheduleRepository = consultationRoomScheduleRepository;
        this.consultationRoomRepository = consultationRoomRepository;
        this.appointmentService = appointmentService;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
    }

    public int schedule(int numberOfDays) {
        int roomsCreated = 0;
        List<Long> scheduleIds = consultationRoomScheduleRepository.findAllBy().stream().map(AbstractEntity::getId).collect(Collectors.toList());
        for (long consultationRoomScheduleId : scheduleIds) {
            roomsCreated += create(consultationRoomScheduleId, numberOfDays);
        }
        return roomsCreated;
    }

    protected int create(long consultationRoomScheduleId, int numberOfDays) {
        LocalDate date = LocalDate.now().minusDays(1);
        int roomsCreated = 0;
        ConsultationRoomSchedule consultationRoomSchedule = consultationRoomScheduleRepository.findEntityInternal(consultationRoomScheduleId);
        for (int i = 0; i < numberOfDays; i++) {
            List<LocalDate> nextConsultationDates = consultationRoomSchedule.getNextConsultationDates(date, date);
            if (nextConsultationDates.size() == 1 && !consultationRoomRepository.existsByConsultationRoomScheduleAndScheduledOn(consultationRoomSchedule, date)) {
                ConsultationRoom consultationRoom = consultationRoomSchedule.createRoomFor(date, userRepository.getUserForAudit("Super_Admin@example.com"));
                consultationRoomRepository.save(consultationRoom);
                roomsCreated++;
            } else if (nextConsultationDates.size() > 1) {
                throw new RuntimeException("Issue in getting next consultation dates");
            }
            date = date.plusDays(1);
        }
        return roomsCreated;
    }

    public ConsultationRoom.ConsultationRoomCurrentUserSummary getCurrentSummaryFor(User user, ConsultationRoom consultationRoom) {
        ConsultationRoom.ConsultationRoomCurrentUserSummary summary = new ConsultationRoom.ConsultationRoomCurrentUserSummary();
        summary.setNumberOfClients(consultationRoom.getNumberOfClients(user));
        return summary;
    }

    public TeleConference setup(long consultationRoomId, Organisation organisation) {
        ConsultationRoom consultationRoom = consultationRoomRepository.findEntity(consultationRoomId, organisation);
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

    public void moveToNextToken(long consultationRoomId, User user, Organisation organisation) {
        ConsultationRoom consultationRoom = consultationRoomRepository.findEntity(consultationRoomId, organisation);
        Appointment currentAppointment = consultationRoom.getCurrentAppointment();

        Appointment nextToken = appointmentService.getNextToken(user, organisation, consultationRoom);

        currentAppointment.setCurrent(false);
        nextToken.setCurrent(true);
        consultationRoomRepository.save(consultationRoom);
    }

    public void moveToPreviousToken(long consultationRoomId, User user, Organisation organisation) {
        ConsultationRoom consultationRoom = consultationRoomRepository.findEntity(consultationRoomId, organisation);
        Appointment currentAppointment = consultationRoom.getCurrentAppointment();

        Appointment previousToken = appointmentService.getPreviousToken(user, organisation, consultationRoom);

        currentAppointment.setCurrent(false);
        previousToken.setCurrent(true);
        consultationRoomRepository.save(consultationRoom);
    }

    public void appointmentMoveDown(long consultationRoomId, int tokenId, User user, Organisation organisation) {
        ConsultationRoom consultationRoom = consultationRoomRepository.findEntity(consultationRoomId, organisation);
        Appointment appointment = consultationRoom.getAppointment(tokenId);
        int queueNumber = appointment.getQueueNumber();
        Appointment nextToken = appointmentService.getNextToken(user, organisation, consultationRoom, appointment);
        int nextQueueNumber = nextToken.getQueueNumber();

        appointment.setQueueNumber(nextQueueNumber);
        nextToken.setQueueNumber(queueNumber);
        consultationRoomRepository.save(consultationRoom);
    }

    public void appointmentMoveUp(long consultationRoomId, int appointmentId, User user, Organisation organisation) {
        ConsultationRoom consultationRoom = consultationRoomRepository.findEntity(consultationRoomId, organisation);
        Appointment appointment = consultationRoom.getAppointment(appointmentId);
        int queueNumber = appointment.getQueueNumber();
        Appointment previousToken = appointmentService.getPreviousToken(user, organisation, consultationRoom, appointment);
        int previousQueueNumber = previousToken.getQueueNumber();

        appointment.setQueueNumber(previousQueueNumber);
        previousToken.setQueueNumber(queueNumber);
        consultationRoomRepository.save(consultationRoom);
    }

    public void setCurrentAppointment(long consultationRoomId, int appointmentId, Organisation organisation) {
        ConsultationRoom consultationRoom = consultationRoomRepository.findEntity(consultationRoomId, organisation);
        Appointment appointment = consultationRoom.getAppointment(appointmentId);
        consultationRoom.getCurrentAppointment().setCurrent(false);
        appointment.setCurrent(true);
        consultationRoomRepository.save(consultationRoom);
    }

    public Appointment removeAppointment(long consultationRoomId, long clientId, Organisation organisation) {
        ConsultationRoom consultationRoom = consultationRoomRepository.findEntity(consultationRoomId, organisation);
        Client client = clientRepository.findEntity(clientId, organisation);
        Appointment removedAppointment = consultationRoom.removeAppointFor(client);
        appointmentService.delete(removedAppointment);
        consultationRoomRepository.save(consultationRoom);
        return removedAppointment;
    }
}
