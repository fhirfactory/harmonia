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
import net.fhirfactory.harmonia.model.topic.Topic;
import net.fhirfactory.harmonia.praxis.sequence.PraxisImplementation;
import net.fhirfactory.harmonia.praxis.service.PraxisService;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Dispatches canonical {@link Pragma} task instances to matching {@link PraxisImplementation} workflows.
 * <p>
 * Routes tasks based on explicit {@code praxisId}, topic subscriptions, or gateway/trigger criteria.
 */
@ApplicationScoped
public class PragmaWorkflowDispatcher {

    private static final Logger log = LoggerFactory.getLogger(PragmaWorkflowDispatcher.class);

    @Inject
    private CamelContext camelContext;

    @Inject
    private PraxisService praxisService;

    private final Map<String, PraxisImplementation> registeredWorkflows = new ConcurrentHashMap<>();

    public PragmaWorkflowDispatcher() {
    }

    public PragmaWorkflowDispatcher(CamelContext camelContext, PraxisService praxisService) {
        this.camelContext = camelContext;
        this.praxisService = praxisService;
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
     *
     * @param pragma canonical Pragma instance
     * @return true if successfully executed, false on failure or if no matching workflow exists
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

        String endpointUri = targetWorkflow.getPipelineInputEndpoint();
        if (camelContext == null) {
            log.error("CamelContext is not available in PragmaWorkflowDispatcher");
            return false;
        }

        ProducerTemplate template = camelContext.createProducerTemplate();
        try {
            log.info("Dispatching Pragma/{} into Praxis [{}] via endpoint [{}]",
                    pragma.getPragmaId(), targetWorkflow.getPraxisId(), endpointUri);

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
                log.error("Execution failure in Praxis [{}] for Pragma/{}: {}",
                        targetWorkflow.getPraxisId(), pragma.getPragmaId(),
                        resultExchange != null ? resultExchange.getException() : "null exchange");
            }
            return success;
        } catch (Exception e) {
            log.error("Exception dispatching Pragma/{} to Praxis [{}]: {}",
                    pragma.getPragmaId(), targetWorkflow.getPraxisId(), e.getMessage(), e);
            return false;
        } finally {
            try {
                template.stop();
            } catch (Exception ignored) {
            }
        }
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
