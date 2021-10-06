package org.opentele.consult.domain.consultation;

import com.sun.istack.NotNull;
import org.opentele.consult.domain.consultant.Consultant;
import org.opentele.consult.domain.facility.ConsultationSchedule;
import org.opentele.consult.domain.framework.OrganisationalEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "consultation_session")
public class ConsultationSession extends OrganisationalEntity {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "clientTokens")
    private Set<ClientToken> clientTokens = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "primary_consultant_id")
    @NotNull
    private Consultant primaryConsultant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "consultation_schedule_id")
    private ConsultationSchedule consultationSchedule;
}
