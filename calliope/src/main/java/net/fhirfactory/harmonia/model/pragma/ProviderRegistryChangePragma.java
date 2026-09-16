/*
 * Copyright (c) 2026 Mark Hunter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.model.pragma;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.registry.ProviderRegistryConstants;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.OperationOutcome;
import org.hl7.fhir.r5.model.Resource;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain model wrapper and factory for FHIR Provider Registry change requests managed via {@link Pragma}.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProviderRegistryChangePragma implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Pragma pragma;

    public ProviderRegistryChangePragma(Pragma pragma) {
        this.pragma = Objects.requireNonNull(pragma, "Pragma cannot be null");
    }

    public Pragma getPragma() {
        return pragma;
    }

    public String getPragmaId() {
        return pragma.getPragmaId();
    }

    public String getCorrelationId() {
        return pragma.getCorrelationId();
    }

    public String getPraxisId() {
        return pragma.getPraxisId();
    }

    public PragmaStatus getStatus() {
        return pragma.getStatus();
    }

    public String getOperation() {
        return pragma.getMetadata().get(ProviderRegistryConstants.METADATA_OPERATION);
    }

    public String getResourceType() {
        return pragma.getMetadata().get(ProviderRegistryConstants.METADATA_RESOURCE_TYPE);
    }

    public String getResourceId() {
        return pragma.getMetadata().get(ProviderRegistryConstants.METADATA_RESOURCE_ID);
    }

    public String getRequester() {
        return pragma.getMetadata().get(ProviderRegistryConstants.METADATA_REQUESTER);
    }

    public String getSourceSystem() {
        return pragma.getMetadata().get(ProviderRegistryConstants.METADATA_SOURCE_SYSTEM);
    }

    public String getSubmittedAt() {
        return pragma.getMetadata().get(ProviderRegistryConstants.METADATA_SUBMITTED_AT);
    }

    public String getIfMatch() {
        return pragma.getMetadata().get(ProviderRegistryConstants.METADATA_IF_MATCH);
    }

    public String getValidationOutcome() {
        return pragma.getMetadata().get(ProviderRegistryConstants.METADATA_VALIDATION_OUTCOME);
    }

    public String getProcessingOutcome() {
        return pragma.getMetadata().get(ProviderRegistryConstants.METADATA_PROCESSING_OUTCOME);
    }

    public String getResultingResourceVersion() {
        return pragma.getMetadata().get(ProviderRegistryConstants.METADATA_RESULTING_VERSION);
    }

    /**
     * Retrieves the submitted FHIR resource from the Pragma input payload.
     *
     * @return FHIR Resource if present, or null
     */
    @JsonIgnore
    public Resource getRequestedResource() {
        if (pragma.getInput() != null && !pragma.getInput().isEmpty()) {
            for (ErgonPayload payload : pragma.getInput()) {
                if (payload.isFhirResource() && payload.getResourceReference() != null && payload.getResourceReference().getResource() instanceof Resource r) {
                    return r;
                }
            }
        }
        return null;
    }

    /**
     * Factory method to build a new {@link Pragma} for a Provider Registry change request.
     */
    public static Pragma buildChangeRequestPragma(
            String operation,
            Resource resource,
            String requester,
            String sourceSystem,
            String correlationId,
            String ifMatch) {

        String pragmaId = UUID.randomUUID().toString();
        String finalCorrId = (correlationId != null && !correlationId.trim().isEmpty()) ? correlationId : UUID.randomUUID().toString();
        String resourceType = resource != null ? resource.fhirType() : "Unknown";
        String resourceId = resource != null && resource.getIdElement() != null ? resource.getIdElement().getIdPart() : null;

        Topic topic = new Topic("Health", "FHIR", "R5", resourceType, operation);
        topic.setSource(sourceSystem != null ? sourceSystem : "pylai-fhir-registry");
        topic.setTarget("energeia-ponos");
        topic.setReceivedDate(new Date());

        ErgonPayload inputPayload = resource != null ?
                ErgonPayload.fromFhirResource(0, topic, topic, resource) : null;

        Pragma pragma = Pragma.builder()
                .pragmaId(pragmaId)
                .correlationId(finalCorrId)
                .praxisId(ProviderRegistryConstants.PRAXIS_PROVIDER_REGISTRY_CHANGE_PIPELINE)
                .status(PragmaStatus.ACCEPTED)
                .source(sourceSystem != null ? sourceSystem : "pylai-fhir-registry")
                .destination(ProviderRegistryConstants.QUEUE_PROVIDER_REGISTRY_CHANGE_REQUEST)
                .priority(50)
                .priorityCode("routine")
                .authoredOn(new Date())
                .addMetadata(ProviderRegistryConstants.METADATA_OPERATION, operation)
                .addMetadata(ProviderRegistryConstants.METADATA_RESOURCE_TYPE, resourceType)
                .addMetadata(ProviderRegistryConstants.METADATA_REQUESTER, requester != null ? requester : "system")
                .addMetadata(ProviderRegistryConstants.METADATA_SOURCE_SYSTEM, sourceSystem != null ? sourceSystem : "pylai-fhir-registry")
                .addMetadata(ProviderRegistryConstants.METADATA_SUBMITTED_AT, Instant.now().toString())
                .addMetadata(ProviderRegistryConstants.METADATA_CORRELATION_ID, finalCorrId)
                .build();

        if (resourceId != null) {
            pragma.addMetadata(ProviderRegistryConstants.METADATA_RESOURCE_ID, resourceId);
        }
        if (ifMatch != null) {
            pragma.addMetadata(ProviderRegistryConstants.METADATA_IF_MATCH, ifMatch);
        }
        if (inputPayload != null) {
            pragma.addInput(inputPayload);
        }

        // Add initial checkpoint
        PragmaCheckpoint checkpoint = PragmaCheckpoint.builder()
                .pragmaId(pragmaId)
                .praxisId(pragma.getPraxisId())
                .stageName("INGEST")
                .status(PragmaStatus.ACCEPTED)
                .statusMessage("Change request received and queued for processing")
                .stepIndex(0)
                .build();
        pragma.addCheckpoint(checkpoint);

        return pragma;
    }

    /**
     * Attaches an {@link OperationOutcome} to the Pragma output and checkpoints.
     */
    public static void attachOutcome(Pragma pragma, OperationOutcome outcome, PragmaStatus newStatus, String stage, String message) {
        if (pragma == null) return;
        if (newStatus != null) {
            pragma.setStatus(newStatus);
        }
        if (outcome != null) {
            Topic topic = new Topic("Health", "FHIR", "R5", "OperationOutcome", "ValidationResult");
            pragma.addOutput(ErgonPayload.fromFhirResource(pragma.getOutput().size(), topic, topic, outcome));
        }
        int step = pragma.getCheckpoints().size();
        PragmaCheckpoint cp = PragmaCheckpoint.builder()
                .pragmaId(pragma.getPragmaId())
                .praxisId(pragma.getPraxisId())
                .stageName(stage != null ? stage : "VALIDATE")
                .status(newStatus != null ? newStatus : PragmaStatus.IN_PROGRESS)
                .statusMessage(message != null ? message : (outcome != null ? "Outcome recorded" : "Status updated"))
                .stepIndex(step)
                .build();
        pragma.addCheckpoint(cp);
    }
}
