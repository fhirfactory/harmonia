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

package net.fhirfactory.harmonia.praxis.conduit;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.erga.base.ErgonBase;
import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaCheckpoint;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.model.security.HarmoniaAuthorityEnum;
import net.fhirfactory.harmonia.model.security.HarmoniaSecurityLabelEnum;
import net.fhirfactory.harmonia.model.topic.Topic;
import net.fhirfactory.harmonia.praxis.cache.PragmaCacheService;
import net.fhirfactory.harmonia.praxis.sequence.PraxisImplementation;
import net.fhirfactory.harmonia.praxis.service.PraxisService;
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
import net.fhirfactory.harmonia.themis.core.identities.HarmoniaServiceIdentities;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Dispatches canonical {@link Pragma} task instances to matching {@link PraxisImplementation} workflows.
 * <p>
 * Enforces Themis dual-authority policy evaluation (originating requester authority + Ergon execution authority)
 * before dispatching tasks into Camel route execution pipelines.
 */
@ApplicationScoped
public class PragmaWorkflowDispatcher {

    private static final Logger log = LoggerFactory.getLogger(PragmaWorkflowDispatcher.class);

    @Inject
    private CamelContext camelContext;

    @Inject
    private PraxisService praxisService;

    @Inject
    private ThemisService themisService;

    @Inject
    private PragmaCacheService pragmaCacheService;

    private final Map<String, PraxisImplementation> registeredWorkflows = new ConcurrentHashMap<>();

    public PragmaWorkflowDispatcher() {
        this.themisService = DeterministicPolicyEvaluator.withDefaultPolicies();
    }

    public PragmaWorkflowDispatcher(CamelContext camelContext, PraxisService praxisService) {
        this(camelContext, praxisService, DeterministicPolicyEvaluator.withDefaultPolicies(), null);
    }

    public PragmaWorkflowDispatcher(CamelContext camelContext, PraxisService praxisService, ThemisService themisService) {
        this(camelContext, praxisService, themisService, null);
    }

    public PragmaWorkflowDispatcher(CamelContext camelContext, PraxisService praxisService, ThemisService themisService, PragmaCacheService pragmaCacheService) {
        this.camelContext = camelContext;
        this.praxisService = praxisService;
        this.themisService = themisService != null ? themisService : DeterministicPolicyEvaluator.withDefaultPolicies();
        this.pragmaCacheService = pragmaCacheService;
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

    public PragmaCacheService getPragmaCacheService() {
        return pragmaCacheService;
    }

    public void setPragmaCacheService(PragmaCacheService pragmaCacheService) {
        this.pragmaCacheService = pragmaCacheService;
    }

    /**
     * Registers an active Praxis workflow implementation for dynamic dispatching.
     *
     * @param praxis Praxis workflow instance
     */
    public void registerWorkflow(PraxisImplementation praxis) {
        if (praxis != null && StringUtils.isNotBlank(praxis.getPraxisId())) {
            registeredWorkflows.put(praxis.getPraxisId(), praxis);
            log.info("Registered Praxis workflow [{}] in PragmaWorkflowDispatcher", praxis.getPraxisId());
        }
    }

    /**
     * Unregisters a Praxis workflow.
     *
     * @param praxisId workflow ID
     */
    public void unregisterWorkflow(String praxisId) {
        if (praxisId != null) {
            registeredWorkflows.remove(praxisId);
            log.info("Unregistered Praxis workflow [{}]", praxisId);
        }
    }

    /**
     * Dispatches a {@link Pragma} to the most suitable Praxis workflow pipeline.
     * Evaluates Themis authorization before allowing workflow execution.
     *
     * @param pragma canonical Pragma instance
     * @return true if successfully executed, false on authorization failure or execution error
     */
    public boolean dispatchPragma(Pragma pragma) {
        if (pragma == null) {
            return false;
        }

        PraxisImplementation targetWorkflow = resolveWorkflow(pragma);
        if (targetWorkflow == null) {
            log.warn("No active Praxis workflow matched for Pragma/{} (praxisId={})",
                    pragma.getPragmaId(), pragma.getPraxisId());
            return false;
        }

        // =========================================================================
        // Themis Defence-in-Depth Policy Checkpoints
        // =========================================================================

        // 1. Originating Requester Security Evaluation
        ThemisPrincipal originatingPrincipal = pragma.getOriginatingPrincipal();
        Set<ThemisAuthority> originatingAuthorities = pragma.getOriginatingAuthorities();

        String resourceType = extractResourceType(pragma, targetWorkflow);
        ThemisResource targetResource = ThemisResource.builder()
                .resourceType(resourceType)
                .securityDomain(HarmoniaSecurityLabelEnum.PROVIDER_REGISTRY.getCode())
                .securityLabels(Set.of(
                        HarmoniaSecurityLabelEnum.PROVIDER_REGISTRY.toThemisLabel(),
                        HarmoniaSecurityLabelEnum.INTERNAL.toThemisLabel()
                ))
                .build();

        ThemisSecurityContext origContext = pragma.getOriginatingSecurityContext() != null
                ? pragma.getOriginatingSecurityContext()
                : (originatingPrincipal != null
                        ? ThemisSecurityContext.builder()
                                .originatingPrincipal(originatingPrincipal)
                                .correlationId(pragma.getCorrelationId())
                                .causationId(pragma.getCausationId())
                                .build()
                        : null);

        ThemisAuthorizationRequest origAuthReq = ThemisAuthorizationRequest.builder()
                .principal(originatingPrincipal)
                .authorities(originatingAuthorities != null ? originatingAuthorities : Set.of())
                .action(ThemisAction.SUBMIT_UPDATE)
                .target(targetResource)
                .context(origContext)
                .build();

        ThemisAuthorizationDecision origDecision = getThemisService().authorize(origAuthReq);
        if (origDecision.decision() == ThemisDecision.DENY) {
            log.warn("Pragma/{} originating requester [{}] denied authority on [{}] (policy={}, reason={})",
                    pragma.getPragmaId(),
                    originatingPrincipal != null ? originatingPrincipal.principalId() : "unauthenticated",
                    resourceType,
                    origDecision.policyId(),
                    origDecision.reason());
            markPragmaAuthorizationFailed(pragma, "Originating requester authority denied: " + origDecision.reason());
            return false;
        }

        // 2. Ergon Execution Authority Evaluation (Ponos WorkEngine Gate)
        Set<ThemisAuthority> executionAuthorities = extractExecutionAuthorities(targetWorkflow);
        ThemisPrincipal executionPrincipal = HarmoniaServiceIdentities.PRINCIPAL_PONOS_PROCESS;
        ThemisSecurityContext execContext = ThemisSecurityContext.builder()
                .principal(executionPrincipal)
                .correlationId(pragma.getCorrelationId())
                .causationId(pragma.getCausationId())
                .build();

        ThemisAuthorizationRequest execAuthReq = ThemisAuthorizationRequest.builder()
                .principal(executionPrincipal)
                .authorities(executionAuthorities)
                .action(ThemisAction.PROCESS)
                .target(targetResource)
                .context(execContext)
                .build();

        ThemisAuthorizationDecision execDecision = getThemisService().authorize(execAuthReq);
        if (execDecision.decision() == ThemisDecision.DENY) {
            log.warn("Pragma/{} execution authority denied for Praxis [{}] (policy={}, reason={})",
                    pragma.getPragmaId(), targetWorkflow.getPraxisId(), execDecision.policyId(), execDecision.reason());
            markPragmaAuthorizationFailed(pragma, "Ergon execution authority denied: " + execDecision.reason());
            return false;
        }

        // =========================================================================
        // Transition Executing Principal to Ponos Engine on Dual-Gate Success
        // =========================================================================
        pragma.setExecutingPrincipal(HarmoniaServiceIdentities.PRINCIPAL_PONOS_PROCESS);
        ThemisSecurityContext activeContext = origContext != null
                ? origContext.withExecutingPrincipal(HarmoniaServiceIdentities.PRINCIPAL_PONOS_PROCESS)
                : ThemisSecurityContext.builder()
                        .originatingPrincipal(originatingPrincipal)
                        .executingPrincipal(HarmoniaServiceIdentities.PRINCIPAL_PONOS_PROCESS)
                        .authorities(originatingAuthorities != null ? originatingAuthorities : Set.of())
                        .correlationId(pragma.getCorrelationId())
                        .causationId(pragma.getCausationId())
                        .securityDomain(HarmoniaSecurityLabelEnum.PROVIDER_REGISTRY.getCode())
                        .build();
        if (activeContext.causationId() == null && pragma.getCausationId() != null) {
            activeContext = activeContext.withCausationId(pragma.getCausationId());
        }
        pragma.setOriginatingSecurityContext(activeContext);

        // =========================================================================
        // Route Dispatching via Apache Camel
        // =========================================================================

        String endpointUri = targetWorkflow.getPipelineInputEndpoint();
        if (camelContext == null) {
            log.error("CamelContext is not available in PragmaWorkflowDispatcher");
            return false;
        }

        ProducerTemplate template = camelContext.createProducerTemplate();
        try {
            log.info("Dispatching Pragma/{} into Praxis [{}] via endpoint [{}] (Originating: {}, Execution: AUTHORIZED)",
                    pragma.getPragmaId(), targetWorkflow.getPraxisId(), endpointUri,
                    originatingPrincipal != null ? originatingPrincipal.principalId() : "unauthenticated");

            Exchange resultExchange = template.request(endpointUri, exchange -> {
                exchange.getMessage().setBody(pragma);
                exchange.setProperty(ErgonBase.PROPERTY_PRAGMA, pragma);
                exchange.getMessage().setHeader(ErgonBase.HEADER_PRAGMA_ID, pragma.getPragmaId());
                exchange.getMessage().setHeader(ErgonBase.HEADER_TASK_ID, pragma.getPragmaId());
                if (StringUtils.isNotBlank(pragma.getSource())) {
                    exchange.getMessage().setHeader("HIE_GATEWAY_INSTANCE", pragma.getSource());
                }
            });

            boolean success = resultExchange != null && !resultExchange.isFailed() && resultExchange.getException() == null;
            if (success) {
                log.info("Successfully executed Praxis [{}] for Pragma/{}", targetWorkflow.getPraxisId(), pragma.getPragmaId());
            } else {
                String exceptionClass = (resultExchange != null && resultExchange.getException() != null)
                        ? resultExchange.getException().getClass().getName()
                        : "None";
                log.error("Execution failure in Praxis [{}] for Pragma/{} [exception={}, category=WORKFLOW_EXECUTION_FAILURE]",
                        targetWorkflow.getPraxisId(), pragma.getPragmaId(), exceptionClass);
            }
            return success;
        } catch (Exception e) {
            log.error("Exception dispatching Pragma/{} to Praxis [{}] [exception={}, category=DISPATCH_FAILURE]",
                    pragma.getPragmaId(), targetWorkflow.getPraxisId(), e.getClass().getName());
            return false;
        } finally {
            try {
                template.stop();
            } catch (Exception ignored) {
            }
        }
    }

    private void markPragmaAuthorizationFailed(Pragma pragma, String reason) {
        pragma.setStatus(PragmaStatus.FAILED);
        PragmaCheckpoint checkpoint = PragmaCheckpoint.builder()
                .pragmaId(pragma.getPragmaId())
                .stageName("THEMIS_EXECUTION_GATE")
                .status(PragmaStatus.FAILED)
                .statusMessage(reason)
                .build();
        pragma.addCheckpoint(checkpoint);
        if (pragmaCacheService != null) {
            try {
                pragmaCacheService.savePragma(pragma);
            } catch (Exception e) {
                log.warn("Failed to persist failed Pragma status to cache for Pragma/{} [exception={}, category=CACHE_PERSIST_FAILURE]",
                        pragma.getPragmaId(), e.getClass().getName());
            }
        }
    }

    private String extractResourceType(Pragma pragma, PraxisImplementation workflow) {
        if (workflow != null && workflow.getActivities() != null) {
            for (ErgonBase activity : workflow.getActivities().values()) {
                if (activity.getSecurityDefinition() != null && !activity.getSecurityDefinition().permittedResourceTypes().isEmpty()) {
                    return activity.getSecurityDefinition().permittedResourceTypes().iterator().next();
                }
            }
        }
        return "Practitioner";
    }

    private Set<ThemisAuthority> extractExecutionAuthorities(PraxisImplementation workflow) {
        Set<ThemisAuthority> authorities = new HashSet<>();
        boolean hasSecDef = false;
        if (workflow != null && workflow.getActivities() != null && !workflow.getActivities().isEmpty()) {
            for (ErgonBase activity : workflow.getActivities().values()) {
                if (activity.getSecurityDefinition() != null) {
                    hasSecDef = true;
                    authorities.addAll(activity.getSecurityDefinition().requiredExecutionAuthorities());
                }
            }
        }
        if (!hasSecDef && authorities.isEmpty()) {
            authorities.add(HarmoniaAuthorityEnum.PROVIDER_CHANGE_PROCESS.toThemisAuthority());
        }
        return authorities;
    }

    /**
     * Resolves matching Praxis workflow for the given Pragma.
     */
    public PraxisImplementation resolveWorkflow(Pragma pragma) {
        if (pragma == null) {
            return null;
        }

        // 1. Explicit praxisId match in local map
        if (StringUtils.isNotBlank(pragma.getPraxisId())) {
            PraxisImplementation direct = registeredWorkflows.get(pragma.getPraxisId());
            if (direct != null && direct.isEnabled()) {
                return direct;
            }
        }

        // 2. Lookup via PraxisService
        if (praxisService != null && StringUtils.isNotBlank(pragma.getPraxisId())) {
            Optional<net.fhirfactory.harmonia.model.praxis.PraxisDefinition> servicePraxis = praxisService.getById(pragma.getPraxisId());
            if (servicePraxis.isPresent() && servicePraxis.get().isEnabled() && servicePraxis.get() instanceof PraxisImplementation) {
                return (PraxisImplementation) servicePraxis.get();
            }
        }

        // 3. Match by Topic subscription
        for (ErgonPayload inputPayload : pragma.getInput()) {
            if (inputPayload.getPayloadContainer() != null) {
                Topic topic = inputPayload.getPayloadContainer();
                for (PraxisImplementation workflow : registeredWorkflows.values()) {
                    if (workflow.isEnabled() && workflow.matches(topic)) {
                        return workflow;
                    }
                }
            }
        }

        // 4. Fallback to any registered workflow if only one exists
        if (registeredWorkflows.size() == 1) {
            return registeredWorkflows.values().iterator().next();
        }

        return null;
    }

    public Map<String, PraxisImplementation> getRegisteredWorkflows() {
        return registeredWorkflows;
    }

    public CamelContext getCamelContext() {
        return camelContext;
    }

    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    public PraxisService getPraxisService() {
        return praxisService;
    }

    public void setPraxisService(PraxisService praxisService) {
        this.praxisService = praxisService;
    }
}
