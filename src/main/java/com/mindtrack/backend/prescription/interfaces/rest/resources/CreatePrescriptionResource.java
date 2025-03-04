package com.mindtrack.backend.prescription.interfaces.rest.resources;

import java.time.LocalDate;

public record CreatePrescriptionResource(
        Long patientId,
        Long professionalId,
        LocalDate startDate,
        LocalDate endDate
) {
}
