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

package net.fhirfactory.harmonia.erga.registry;

import jakarta.inject.Inject;
import net.fhirfactory.harmonia.erga.base.ErgonBase;
import net.fhirfactory.harmonia.hapifhir.service.FhirStorageService;
import net.fhirfactory.harmonia.hapifhir.service.ProviderRegistryReferenceValidator;
import net.fhirfactory.harmonia.hapifhir.service.ProviderRegistryReferenceValidator.ValidationResult;
import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaCheckpoint;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.model.pragma.ProviderRegistryChangePragma;
import net.fhirfactory.harmonia.model.registry.ProviderRegistryConstants;
import net.fhirfactory.harmonia.model.security.ErgonSecurityDefinition;
import net.fhirfactory.harmonia.model.topic.Topic;
import net.fhirfactory.harmonia.themis.api.ThemisService;
import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisResource;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import net.fhirfactory.harmonia.themis.core.evaluator.DeterministicPolicyEvaluator;
import net.fhirfactory.harmonia.model.security.HarmoniaAuthorityEnum;
import net.fhirfactory.harmonia.model.security.HarmoniaSecurityLabelEnum;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.OperationOutcome;
import org.hl7.fhir.r5.model.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Abstract base class for per-resource Provider Registry Change Processing Activities.
 *
 * @param <T> specific FHIR R5 Resource type
 */
public abstract class AbstractProviderRegistryChangeErgon<T extends Resource> extends ErgonBase {

    @Inject
    private FhirStorageService storageService;

    @Inject
    private ProviderRegistryReferenceValidator referenceValidator;

    @Inject
    private ThemisService themisService;

    private final Class<T> resourceClass;
    private final String expectedResourceType;

    public AbstractProviderRegistryChangeErgon(String activityId, String activityName, Class<T> resourceClass, String expectedResourceType) {
        super(null, activityId, activityName);
        this.resourceClass = resourceClass;
        this.expectedResourceType = expectedResourceType;
        this.themisService = DeterministicPolicyEvaluator.withDefaultPolicies();
        setSecurityDefinition(ErgonSecurityDefinition.forProviderRegistryChange(activityId, expectedResourceType));
    }

    public ThemisService getThemisService() {
        if (themisService == null) {
            themisService = DeterministicPolicyEvaluator.withDefaultPolicies();
        }
        return themisService;
    }

    public void setThemisService(ThemisService themisService) {
        this.themisService = themisService;
    }

    public String getExpectedResourceType() {
        return expectedResourceType;
    }

    public Class<T> getResourceClass() {
        return resourceClass;
    }

    public FhirStorageService getStorageService() {
        return storageService;
    }

    public void setStorageService(FhirStorageService storageService) {
        this.storageService = storageService;
    }

    public ProviderRegistryReferenceValidator getReferenceValidator() {
        return referenceValidator;
    }

    public void setReferenceValidator(ProviderRegistryReferenceValidator referenceValidator) {
        this.referenceValidator = referenceValidator;
    }

    @Override
    public void processErgon(Pragma pragma, Exchange exchange) throws Exception {
        if (pragma == null) {
            return;
        }

        // Check if Pragma is already in a terminal state
        if (pragma.getStatus() == PragmaStatus.COMPLETED ||
            pragma.getStatus() == PragmaStatus.REJECTED ||
            pragma.getStatus() == PragmaStatus.FAILED ||
            pragma.getStatus() == PragmaStatus.CANCELLED) {
            log.debug("[{}] Pragma/{} is already in terminal state {}, skipping",
                    getActivityName(), pragma.getPragmaId(), pragma.getStatus());
            return;
        }

        // Check if this Pragma targets the resource type managed by this Ergon
        String targetResourceType = pragma.getMetadata().get(ProviderRegistryConstants.METADATA_RESOURCE_TYPE);
        if (targetResourceType == null && pragma.getInput() != null) {
            for (ErgonPayload ep : pragma.getInput()) {
                if (ep.isFhirResource() && ep.getResourceReference() != null && ep.getResourceReference().getResource() != null) {
                    targetResourceType = ep.getResourceReference().getResource().fhirType();
                    break;
                }
            }
        }

        if (targetResourceType != null && !targetResourceType.equalsIgnoreCase(expectedResourceType)) {
            log.debug("[{}] Pragma/{} target resourceType [{}] does not match Ergon expected [{}], passing through",
                    getActivityName(), pragma.getPragmaId(), targetResourceType, expectedResourceType);
            return;
        }

        log.info("[{}] Processing Provider Registry change request for Pragma/{}",
                getActivityName(), pragma.getPragmaId());

        // Extract FHIR Resource
        T resource = extractRequestedResource(pragma);
        if (resource == null) {
            OperationOutcome outcome = new OperationOutcome();
            outcome.addIssue()
                    .setSeverity(OperationOutcome.IssueSeverity.ERROR)
                    .setCode(OperationOutcome.IssueType.STRUCTURE)
                    .setDiagnostics("[" + ProviderRegistryConstants.VAL_CODE_STRUCTURAL_ERROR + "] No valid " + expectedResourceType + " resource found in Pragma input");
            ProviderRegistryChangePragma.attachOutcome(pragma, outcome, PragmaStatus.REJECTED, "VALIDATE_STRUCTURE",
                    "No valid " + expectedResourceType + " resource payload found");
            return;
        }

        // 1. Referential Integrity Validation
        if (referenceValidator != null) {
            ValidationResult refResult = referenceValidator.validateReferences(resource);
            if (!refResult.isValid()) {
                log.warn("[{}] Referential validation failed for Pragma/{}: {}",
                        getActivityName(), pragma.getPragmaId(), refResult.getErrorMessages());
                ProviderRegistryChangePragma.attachOutcome(pragma, refResult.toOperationOutcome(), PragmaStatus.REJECTED,
                        "VALIDATE_REFERENCES", "Referential integrity check failed: " + String.join("; ", refResult.getErrorMessages()));
                return;
            }
        }

        // 2. Resource-Specific Business & Duplicate Validation
        List<String> errorMessages = new ArrayList<>();
        List<String> errorCodes = new ArrayList<>();
        validateResource(resource, pragma, errorMessages, errorCodes);

        if (!errorMessages.isEmpty()) {
            OperationOutcome outcome = new OperationOutcome();
            for (int i = 0; i < errorMessages.size(); i++) {
                String code = (i < errorCodes.size()) ? errorCodes.get(i) : ProviderRegistryConstants.VAL_CODE_BUSINESS_RULE_FAILURE;
                outcome.addIssue()
                        .setSeverity(OperationOutcome.IssueSeverity.ERROR)
                        .setCode(OperationOutcome.IssueType.INVALID)
                        .setDiagnostics("[" + code + "] " + errorMessages.get(i));
            }
            log.warn("[{}] Business validation failed for Pragma/{}: {}",
                    getActivityName(), pragma.getPragmaId(), errorMessages);
            ProviderRegistryChangePragma.attachOutcome(pragma, outcome, PragmaStatus.REJECTED,
                    "VALIDATE_BUSINESS_RULES", "Business validation failed: " + String.join("; ", errorMessages));
            return;
        }

        // 3. Automated Approval
        pragma.setStatus(PragmaStatus.IN_PROGRESS);
        pragma.addCheckpoint(PragmaCheckpoint.builder()
                .pragmaId(pragma.getPragmaId())
                .praxisId(pragma.getPraxisId())
                .stageName("APPROVE")
                .status(PragmaStatus.IN_PROGRESS)
                .statusMessage("Automated first-iteration approval granted for " + expectedResourceType)
                .stepIndex(pragma.getCheckpoints().size())
                .build());

        String operation = pragma.getMetadata().getOrDefault(ProviderRegistryConstants.METADATA_OPERATION, ProviderRegistryConstants.OPERATION_CREATE);
        String resourceId = pragma.getMetadata().get(ProviderRegistryConstants.METADATA_RESOURCE_ID);
        String ifMatch = pragma.getMetadata().get(ProviderRegistryConstants.METADATA_IF_MATCH);

        if (resourceId == null && resource.hasIdElement() && StringUtils.isNotBlank(resource.getIdElement().getIdPart())) {
            resourceId = resource.getIdElement().getIdPart();
        }

        // 3.5 Themis Independent Persistence Authorization Check (Defence-in-Depth Gate)
        ThemisAction persistAction = ProviderRegistryConstants.OPERATION_UPDATE.equalsIgnoreCase(operation)
                ? ThemisAction.UPDATE
                : ThemisAction.CREATE;

        ThemisResource targetPersistResource = ThemisResource.builder()
                .resourceType(expectedResourceType)
                .resourceId(resourceId)
                .securityDomain(HarmoniaSecurityLabelEnum.PROVIDER_REGISTRY.getCode())
                .securityLabels(Set.of(
                        HarmoniaSecurityLabelEnum.PROVIDER_REGISTRY.toThemisLabel(),
                        HarmoniaSecurityLabelEnum.INTERNAL.toThemisLabel()
                ))
                .build();

        Set<ThemisAuthority> persistenceAuthorities = getSecurityDefinition() != null
                ? getSecurityDefinition().requiredExecutionAuthorities()
                : Set.of(HarmoniaAuthorityEnum.PROVIDER_RESOURCE_CREATE.toThemisAuthority(), HarmoniaAuthorityEnum.PROVIDER_RESOURCE_UPDATE.toThemisAuthority());

        ThemisPrincipal persistPrincipal = ThemisPrincipal.of("process:provider-registry-change-processor", PrincipalType.PROCESS, "erga");
        ThemisSecurityContext persistContext = ThemisSecurityContext.fromPrincipal(persistPrincipal, pragma.getCorrelationId());

        ThemisAuthorizationRequest persistAuthReq = ThemisAuthorizationRequest.builder()
                .principal(persistPrincipal)
                .authorities(persistenceAuthorities)
                .action(persistAction)
                .target(targetPersistResource)
                .context(persistContext)
                .build();

        ThemisAuthorizationDecision persistDecision = getThemisService().authorize(persistAuthReq);
        if (persistDecision.decision() == ThemisDecision.DENY) {
            log.warn("[{}] Themis persistence authorization DENIED for Pragma/{}: policy={}, reason={}",
                    getActivityName(), pragma.getPragmaId(), persistDecision.policyId(), persistDecision.reason());
            OperationOutcome outcome = new OperationOutcome();
            outcome.addIssue()
                    .setSeverity(OperationOutcome.IssueSeverity.ERROR)
                    .setCode(OperationOutcome.IssueType.FORBIDDEN)
                    .setDiagnostics("Persistence authorization denied by Themis: " + persistDecision.reason() + " [" + persistDecision.policyId() + "]");
            ProviderRegistryChangePragma.attachOutcome(pragma, outcome, PragmaStatus.FAILED,
                    "COMMIT_DENIED_BY_THEMIS", "Persistence authorization denied: " + persistDecision.reason());
            return;
        }

        // 4. Commit to Durable Storage
        pragma.setStatus(PragmaStatus.IN_PROGRESS);
        pragma.addCheckpoint(PragmaCheckpoint.builder()
                .pragmaId(pragma.getPragmaId())
                .praxisId(pragma.getPraxisId())
                .stageName("COMMIT")
                .status(PragmaStatus.IN_PROGRESS)
                .statusMessage("Committing " + expectedResourceType + " to authoritative Mnemosyne persistence")
                .stepIndex(pragma.getCheckpoints().size())
                .build());

        try {
            T persisted;
            if (storageService != null) {
                if (ProviderRegistryConstants.OPERATION_UPDATE.equalsIgnoreCase(operation) && StringUtils.isNotBlank(resourceId)) {
                    persisted = storageService.updateResource(resourceId, resource, ifMatch);
                } else {
                    persisted = storageService.createResource(resource);
                }
            } else {
                persisted = resource;
            }

            String resultingVersion = (persisted.getMeta() != null && persisted.getMeta().getVersionId() != null)
                    ? persisted.getMeta().getVersionId() : "1";
            pragma.getMetadata().put(ProviderRegistryConstants.METADATA_RESULTING_VERSION, resultingVersion);

            Topic outputTopic = new Topic("Health", "FHIR", "R5", expectedResourceType, "PersistedState");
            pragma.addOutput(ErgonPayload.fromFhirResource(pragma.getOutput().size(), outputTopic, outputTopic, persisted));

            pragma.setStatus(PragmaStatus.COMPLETED);
            pragma.addCheckpoint(PragmaCheckpoint.builder()
                    .pragmaId(pragma.getPragmaId())
                    .praxisId(pragma.getPraxisId())
                    .stageName("COMPLETED")
                    .status(PragmaStatus.COMPLETED)
                    .statusMessage(expectedResourceType + "/" + persisted.getIdElement().getIdPart() + " committed successfully at version " + resultingVersion)
                    .stepIndex(pragma.getCheckpoints().size())
                    .build());

            log.info("[{}] Successfully completed Pragma/{} for {}/{}",
                    getActivityName(), pragma.getPragmaId(), expectedResourceType, persisted.getIdElement().getIdPart());

        } catch (ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException ex) {
            log.error("[{}] Optimistic lock conflict for Pragma/{}: {}", getActivityName(), pragma.getPragmaId(), ex.getMessage());
            OperationOutcome outcome = new OperationOutcome();
            outcome.addIssue()
                    .setSeverity(OperationOutcome.IssueSeverity.ERROR)
                    .setCode(OperationOutcome.IssueType.CONFLICT)
                    .setDiagnostics("[" + ProviderRegistryConstants.VAL_CODE_CONCURRENCY_CONFLICT + "] " + ex.getMessage());
            ProviderRegistryChangePragma.attachOutcome(pragma, outcome, PragmaStatus.FAILED, "COMMIT_CONFLICT", ex.getMessage());
        } catch (Exception ex) {
            log.error("[{}] Error committing {} for Pragma/{}: {}", getActivityName(), expectedResourceType, pragma.getPragmaId(), ex.getMessage(), ex);
            OperationOutcome outcome = new OperationOutcome();
            outcome.addIssue()
                    .setSeverity(OperationOutcome.IssueSeverity.ERROR)
                    .setCode(OperationOutcome.IssueType.TRANSIENT)
                    .setDiagnostics("[" + ProviderRegistryConstants.VAL_CODE_BUSINESS_RULE_FAILURE + "] Persistence error: " + ex.getMessage());
            ProviderRegistryChangePragma.attachOutcome(pragma, outcome, PragmaStatus.FAILED, "COMMIT_FAILURE", ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    protected T extractRequestedResource(Pragma pragma) {
        if (pragma.getInput() != null) {
            for (ErgonPayload ep : pragma.getInput()) {
                if (ep.isFhirResource() && ep.getResourceReference() != null && ep.getResourceReference().getResource() != null) {
                    IBaseResource res = ep.getResourceReference().getResource();
                    if (resourceClass.isInstance(res)) {
                        return (T) res;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Subclasses implement specific business validation, identifier uniqueness, and structural rules.
     *
     * @param resource      FHIR resource being processed
     * @param pragma        enclosing Pragma domain instance
     * @param errorMessages list to append error messages to
     * @param errorCodes    list to append corresponding error codes to
     */
    protected abstract void validateResource(T resource, Pragma pragma, List<String> errorMessages, List<String> errorCodes);
}
