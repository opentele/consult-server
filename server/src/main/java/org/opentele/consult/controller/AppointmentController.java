package org.opentele.consult.controller;

import org.opentele.consult.contract.appointment.AppointmentRequest;
import org.opentele.consult.contract.appointment.AppointmentResponse;
import org.opentele.consult.domain.consultationRoom.AppointmentToken;
import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.opentele.consult.framework.UserSession;
import org.opentele.consult.repository.AppointmentTokenRepository;
import org.opentele.consult.repository.ClientRepository;
import org.opentele.consult.repository.ConsultationRoomRepository;
import org.opentele.consult.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@PreAuthorize("hasRole('User')")
public class AppointmentController {
    private final ConsultationRoomRepository consultationRoomRepository;
    private final UserService userService;
    private final ClientRepository clientRepository;
    private final AppointmentTokenRepository appointmentTokenRepository;
    private UserSession userSession;

    @Autowired
    public AppointmentController(ConsultationRoomRepository consultationRoomRepository, UserService userService, ClientRepository clientRepository, AppointmentTokenRepository appointmentTokenRepository, UserSession userSession) {
        this.consultationRoomRepository = consultationRoomRepository;
        this.userService = userService;
        this.clientRepository = clientRepository;
        this.appointmentTokenRepository = appointmentTokenRepository;
        this.userSession = userSession;
    }

    @RequestMapping(value = "/api/appointment", method = {RequestMethod.PUT, RequestMethod.POST})
    public AppointmentResponse saveAppointment(@RequestBody AppointmentRequest request, Principal principal) {
        AppointmentToken appointmentToken = new AppointmentToken();
        appointmentToken.setAppointmentProvider(userService.getUser(principal));
        appointmentToken.setOrganisation(userSession.getCurrentOrganisation());
        appointmentToken.setClient(clientRepository.findEntity(request.getClientId()));
        ConsultationRoom consultationRoom = consultationRoomRepository.findEntity(request.getConsultationRoomId());
        appointmentToken.setConsultationRoom(consultationRoom);
        if (request.getQueueNumber() <= 0) {
            String lockString = String.format("consultationRoom-%d", request.getConsultationRoomId());
            synchronized (lockString.intern()) {
                int queueNumber = appointmentTokenRepository.getMostRecentAppointmentQueueNumber(consultationRoom);
                appointmentToken.setQueueNumber(queueNumber + 1);
                appointmentToken = appointmentTokenRepository.save(appointmentToken);
            }
        } else {
            appointmentToken.setQueueNumber(request.getQueueNumber());
            appointmentToken = appointmentTokenRepository.save(appointmentToken);
        }
        return AppointmentResponse.fromEntity(appointmentToken);
    }
}
