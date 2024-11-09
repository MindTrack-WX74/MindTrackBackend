package com.mindtrack.backend.session.domain.services;

import com.mindtrack.backend.session.domain.model.aggregates.Session;
import com.mindtrack.backend.session.domain.model.entities.Note;
import com.mindtrack.backend.session.domain.model.queries.GetAllNotesBySessionIdQuery;
import com.mindtrack.backend.session.domain.model.queries.GetAllSessionByTreatmentPlanIdQuery;
import com.mindtrack.backend.session.domain.model.queries.GetAllSessionsByProfessionalIdQuery;
import com.mindtrack.backend.session.domain.model.queries.GetSessionByIdQuery;

import java.util.List;
import java.util.Optional;

public interface SessionQueryService {
    Optional<Session> handle(GetSessionByIdQuery query);
    List<Session> handle(GetAllSessionsByProfessionalIdQuery query);
    List<Session> handle(GetAllSessionByTreatmentPlanIdQuery query);
    List<Note> handle(GetAllNotesBySessionIdQuery query);
}
