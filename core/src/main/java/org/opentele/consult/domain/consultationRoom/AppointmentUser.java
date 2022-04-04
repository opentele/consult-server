package org.opentele.consult.domain.consultationRoom;

import org.opentele.consult.domain.framework.OrganisationalEntity;
import org.opentele.consult.domain.security.User;

import javax.persistence.*;

@Entity
@Table(name = "appointment_user")
public class AppointmentUser extends OrganisationalEntity {
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "integer not null")
    private User user;

    @ManyToOne(targetEntity = Appointment.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", columnDefinition = "integer not null")
    private Appointment appointment;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}
