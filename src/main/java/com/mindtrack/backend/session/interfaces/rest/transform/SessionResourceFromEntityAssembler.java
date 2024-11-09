package com.mindtrack.backend.session.interfaces.rest.transform;

import com.mindtrack.backend.session.domain.model.aggregates.Session;
import com.mindtrack.backend.session.interfaces.rest.resources.NoteResource;
import com.mindtrack.backend.session.interfaces.rest.resources.SessionResource;

import java.util.List;

public class SessionResourceFromEntityAssembler {
    public static SessionResource toResourceFromEntity(Session entity) {
        return new SessionResource(
                entity.getId(),
                entity.getPatientId(),
                entity.getProfessionalId(),
                entity.getSessionDate(),
                entity.getTreatmentPlanId()
        );
    }
}
