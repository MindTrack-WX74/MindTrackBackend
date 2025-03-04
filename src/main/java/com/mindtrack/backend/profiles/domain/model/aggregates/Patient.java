package com.mindtrack.backend.profiles.domain.model.aggregates;

import com.mindtrack.backend.profiles.domain.model.commands.CreatePatientCommand;
import com.mindtrack.backend.profiles.domain.model.valueobjetcs.Profile;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class Patient extends Profile {

    Long professionalId;

    private boolean clinicalHistoryStatus;

    protected Patient() {
        super();
    }

    public Patient(CreatePatientCommand command, Long professionalId) {
        super(command.fullName(), command.email(), command.phone(), command.birthDate(), command.userId());
        this.professionalId = professionalId;
        this.clinicalHistoryStatus = false;
    }
}
