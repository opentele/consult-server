package org.opentele.consult.domain.consultationRoom;

import java.util.ArrayList;
import java.util.Collection;

public class ConsultationRooms extends ArrayList<ConsultationRoom> {
    public ConsultationRooms() {
    }

    public ConsultationRooms(Collection<? extends ConsultationRoom> c) {
        super(c);
    }
}
