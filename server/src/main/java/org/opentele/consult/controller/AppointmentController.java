package org.opentele.consult.controller;

import org.opentele.consult.contract.appointment.AppointmentRequest;
import org.opentele.consult.contract.appointment.AppointmentResponse;
import org.opentele.consult.domain.consultationRoom.AppointmentToken;
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

    @Autowired
    public AppointmentController(ConsultationRoomRepository consultationRoomRepository, UserService userService, ClientRepository clientRepository, AppointmentTokenRepository appointmentTokenRepository) {
        this.consultationRoomRepository = consultationRoomRepository;
        this.userService = userService;
        this.clientRepository = clientRepository;
        this.appointmentTokenRepository = appointmentTokenRepository;
    }

    @RequestMapping(value = "/api/appointment", method = {RequestMethod.PUT, RequestMethod.POST})
    public AppointmentResponse saveAppointment(@RequestBody AppointmentRequest request, Principal principal) {
        AppointmentToken appointmentToken = new AppointmentToken();
        appointmentToken.setAppointmentProvider(userService.getUser(principal));
        appointmentToken.setOrganisation(userService.getOrganisation(principal));
        appointmentToken.setClient(clientRepository.findEntity(request.getClientId()));
        appointmentToken.setConsultationRoom(consultationRoomRepository.findEntity(request.getConsultationRoomId()));
        appointmentToken.setQueueNumber(request.getQueueNumber());
        appointmentToken = appointmentTokenRepository.save(appointmentToken);
        return AppointmentResponse.fromEntity(appointmentToken);
    }
}
