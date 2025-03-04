package com.mindtrack.backend.prescription.interfaces.rest;

import com.mindtrack.backend.prescription.domain.model.aggregates.Prescription;
import com.mindtrack.backend.prescription.domain.model.queries.GetAllPrescriptionByPatientId;
import com.mindtrack.backend.prescription.domain.model.queries.GetAllPrescriptionByProfessionalIdQuery;
import com.mindtrack.backend.prescription.domain.model.queries.GetAllPrescriptionByTreatmentPlanIdQuery;
import com.mindtrack.backend.prescription.domain.model.queries.GetPrescriptionByIdQuery;
import com.mindtrack.backend.prescription.domain.services.PrescriptionCommandService;
import com.mindtrack.backend.prescription.domain.services.PrescriptionQueryService;
import com.mindtrack.backend.prescription.interfaces.rest.resources.AddPillsToDescriptionResource;
import com.mindtrack.backend.prescription.interfaces.rest.resources.CreatePrescriptionResource;
import com.mindtrack.backend.prescription.interfaces.rest.resources.PrescriptionResource;
import com.mindtrack.backend.prescription.interfaces.rest.transform.AddPillsToDescriptionCommandFromResourceAssembler;
import com.mindtrack.backend.prescription.interfaces.rest.transform.CreatePrescriptionCommandFromResourceAssembler;
import com.mindtrack.backend.prescription.interfaces.rest.transform.CreatePrescriptionOfTreatmentPlanCommandFromResourceAssembler;
import com.mindtrack.backend.prescription.interfaces.rest.transform.PrescriptionResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "The request was not successful"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "An unexpected error occurred on the server side"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "The request was not authorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "The request was forbidden"),
})
@RestController
@RequestMapping(value ="/api/v1/prescriptions", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Prescription", description = "The Prescription Controller")
public class PrescriptionController {
    private final PrescriptionCommandService prescriptionCommandService;
    private final PrescriptionQueryService prescriptionQueryService;

    public PrescriptionController(PrescriptionCommandService prescriptionCommandService, PrescriptionQueryService prescriptionQueryService) {
        this.prescriptionCommandService = prescriptionCommandService;
        this.prescriptionQueryService = prescriptionQueryService;
    }

    @Operation(summary = "Create a new prescription", description = "Create a new prescription with the given data")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "The prescription was created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "The request was not successful"),
    })
    @PostMapping
    public ResponseEntity<PrescriptionResource> createPrescription(@RequestBody CreatePrescriptionResource prescriptionResource) {
        Optional<Prescription> prescription = this.prescriptionCommandService.handle(CreatePrescriptionCommandFromResourceAssembler.toCommandFromResource(prescriptionResource));
        return prescription.map(PrescriptionResourceFromEntityAssembler::toResourceFromEntity)
                .map(resource -> ResponseEntity.status(CREATED).body(resource))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Create a new prescription of a treatment", description = "Create a new prescription with the given data")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "The prescription was created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "The request was not successful"),
    })
    @PostMapping("/{treatmentId}")
    public ResponseEntity<PrescriptionResource> createPrescription(@PathVariable Long treatmentId, @RequestBody CreatePrescriptionResource prescriptionResource) {
        Optional<Prescription> prescription = this.prescriptionCommandService
                .handle(CreatePrescriptionOfTreatmentPlanCommandFromResourceAssembler.toCommandFromResource(prescriptionResource, treatmentId));
        return prescription.map(PrescriptionResourceFromEntityAssembler::toResourceFromEntity)
                .map(resource -> ResponseEntity.status(CREATED).body(resource))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Get all prescriptions of a treatment", description = "Get all prescriptions of a treatment")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "The prescriptions were obtained successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "The request was not successful"),
    })
    @GetMapping("/treatment/{treatmentId}")
    public ResponseEntity<List<PrescriptionResource>> getPrescriptionsByTreatmentId(@PathVariable Long treatmentId) {
        var query = new GetAllPrescriptionByTreatmentPlanIdQuery(treatmentId);
        List<Prescription> prescriptions = this.prescriptionQueryService.handle(query);

        if(prescriptions.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<PrescriptionResource> resources = prescriptions.stream().map(PrescriptionResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.status(OK).body(resources);
    }

    @Operation(summary = "Get all prescriptions of a professional", description = "Get all prescriptions of a professional")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "The prescriptions were obtained successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "The request was not successful"),
    })
    @GetMapping("/professional/{professionalId}")
    public ResponseEntity<List<PrescriptionResource>> getPrescriptionsByProfessionalId(@PathVariable Long professionalId) {
        var query = new GetAllPrescriptionByProfessionalIdQuery(professionalId);
        List<Prescription> prescriptions = this.prescriptionQueryService.handle(query);

        if(prescriptions.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<PrescriptionResource> resources = prescriptions.stream().map(PrescriptionResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.status(OK).body(resources);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PrescriptionResource>> getPrescriptionsByPatientId(@PathVariable Long patientId) {
        var query = new GetAllPrescriptionByPatientId(patientId);
        List<Prescription> prescriptions = this.prescriptionQueryService.handle(query);

        if(prescriptions.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<PrescriptionResource> resources = prescriptions.stream().map(PrescriptionResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.status(OK).body(resources);
    }

    @Operation(summary = "Get a prescription by its ID", description = "Get a prescription by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "The prescription was obtained successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "The request was not successful"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResource> getPrescriptionById(@PathVariable Long id) {
        var query = new GetPrescriptionByIdQuery(id);
        Optional<Prescription> prescription = this.prescriptionQueryService.handle(query);

        return prescription.map(PrescriptionResourceFromEntityAssembler::toResourceFromEntity)
                .map(resource -> ResponseEntity.status(OK).body(resource))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Add pill to prescription", description = "Add a pill to a prescription")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "The pill was added successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "The request was not successful"),
    })
    @PutMapping("/{prescriptionId}/pills")
    public ResponseEntity<PrescriptionResource> addPillToPrescription(@RequestBody AddPillsToDescriptionResource resource,
                                                                      @PathVariable Long prescriptionId) {
        Optional<Prescription> prescription = this.prescriptionCommandService
                .handle(AddPillsToDescriptionCommandFromResourceAssembler.toCommandFromResource(resource, prescriptionId));
        return prescription.map(source -> ResponseEntity.ok(PrescriptionResourceFromEntityAssembler.toResourceFromEntity(source)))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
