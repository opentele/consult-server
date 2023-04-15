package org.opentele.consult.controller;

import org.opentele.consult.contract.appointment.AppointmentContract;
import org.opentele.consult.domain.consultationRoom.Appointment;
import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.opentele.consult.framework.UserSession;
import org.opentele.consult.repository.AppointmentRepository;
import org.opentele.consult.repository.ClientRepository;
import org.opentele.consult.repository.ConsultationRoomRepository;
import org.opentele.consult.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasRole('User')")
public class AppointmentController extends BaseController {
    private final ConsultationRoomRepository consultationRoomRepository;
    private final ClientRepository clientRepository;
    private final AppointmentRepository appointmentRepository;

    private static final String AppointmentBaseEndpoint = "/api/appointment";

    @Autowired
    public AppointmentController(ConsultationRoomRepository consultationRoomRepository, UserService userService, ClientRepository clientRepository, AppointmentRepository appointmentRepository, UserSession userSession) {
        super(userService, userSession);
        this.consultationRoomRepository = consultationRoomRepository;
        this.clientRepository = clientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @RequestMapping(value = AppointmentBaseEndpoint, method = {RequestMethod.PUT, RequestMethod.POST})
    public AppointmentContract saveAppointment(@RequestBody AppointmentContract request, Principal principal) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentProvider(getCurrentUser(principal));
        appointment.setOrganisation(getCurrentOrganisation());
        appointment.setClient(clientRepository.findEntity(request.getClientId(), getCurrentOrganisation()));
        ConsultationRoom consultationRoom = consultationRoomRepository.findEntity(request.getConsultationRoomId(), getCurrentOrganisation());
        appointment.setConsultationRoom(consultationRoom);
        if (request.getQueueNumber() <= 0) {
            String lockString = String.format("consultationRoom-%d", request.getConsultationRoomId());
            synchronized (lockString.intern()) {
                int queueNumber = appointmentRepository.getMostRecentAppointmentQueueNumber(consultationRoom);
                appointment.setQueueNumber(queueNumber + 1);
                appointment = appointmentRepository.save(appointment);
            }
        } else {
            appointment.setQueueNumber(request.getQueueNumber());
            appointment = appointmentRepository.save(appointment);
        }
        return AppointmentContract.fromEntity(appointment);
    }

    @RequestMapping(value = AppointmentBaseEndpoint + "s", method = {RequestMethod.PUT, RequestMethod.POST})
    public List<AppointmentContract> saveAppointments(@RequestBody List<AppointmentContract> requests, Principal principal) {
        return  requests.stream().map(appointmentContract -> this.saveAppointment(appointmentContract, principal)).collect(Collectors.toList());
    }
}
