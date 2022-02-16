package org.opentele.consult.domain.consultationRoom;

import org.opentele.consult.domain.framework.OrganisationalEntity;
import org.opentele.consult.domain.security.User;

import javax.persistence.*;

@Entity
@Table(name = "appointment_token_user")
public class AppointmentTokenUser extends OrganisationalEntity {
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "integer not null")
    private User user;

    @ManyToOne(targetEntity = AppointmentToken.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_token_id", columnDefinition = "integer not null")
    private AppointmentToken appointmentToken;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AppointmentToken getAppointmentToken() {
        return appointmentToken;
    }

    public void setAppointmentToken(AppointmentToken appointmentToken) {
        this.appointmentToken = appointmentToken;
    }
}
