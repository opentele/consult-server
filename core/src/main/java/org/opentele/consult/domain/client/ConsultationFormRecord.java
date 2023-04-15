package org.opentele.consult.domain.client;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.opentele.consult.domain.framework.OrganisationalEntity;

import javax.persistence.*;

@Table(name = "consultation_form_record")
@Entity
@TypeDefs({
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class ConsultationFormRecord extends OrganisationalEntity {
    @Column(columnDefinition = "varchar(100) not null")
    private String formId;

    @Column(columnDefinition = "jsonb not null")
    @Type(type = "jsonb")
    private String data;

    @ManyToOne(targetEntity = ConsultationRoom.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_room_id", columnDefinition = "integer")
    private ConsultationRoom consultationRoom;

    @ManyToOne(targetEntity = Client.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", columnDefinition = "integer not null")
    private Client client;

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFormId() {
        return formId;
    }

    public String getData() {
        return data;
    }

    public ConsultationRoom getConsultationRoom() {
        return consultationRoom;
    }

    public void setConsultationRoom(ConsultationRoom consultationRoom) {
        this.consultationRoom = consultationRoom;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
