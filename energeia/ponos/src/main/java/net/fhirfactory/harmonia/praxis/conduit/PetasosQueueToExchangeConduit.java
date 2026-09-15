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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.erga.base.ErgonBase;
import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaFhirConverter;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.model.topic.Topic;
import net.fhirfactory.harmonia.petasos.api.Petasos;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosConsumer;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosMessageContext;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosMessageHandler;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosSubscription;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Ingress Conduit bridging Petasos messaging queues to Apache Camel workflow execution pipelines.
 * <p>
 * Consumes {@link PetasosMessage} envelopes from configured Petasos queues via {@link PetasosConsumer},
 * translates envelopes to canonical {@link Pragma} instances, dispatches exchanges into Camel routes,
 * and manages lifecycle delivery acknowledgements upon successful execution or durable checkpoint persistence.
 */
@ApplicationScoped
public class PetasosQueueToExchangeConduit {

    private static final Logger log = LoggerFactory.getLogger(PetasosQueueToExchangeConduit.class);

    @Inject
    private Petasos petasos;

    @Inject
    private CamelContext camelContext;

    @Inject
    private PragmaWorkflowDispatcher workflowDispatcher;

    private PetasosSubscription petasosSubscription;
    private String destinationQueue = "petasos.queue.tasks";
    private String targetEndpointUri;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public PetasosQueueToExchangeConduit() {
    }

    public PetasosQueueToExchangeConduit(Petasos petasos, CamelContext camelContext, String destinationQueue, String targetEndpointUri) {
        this.petasos = petasos;
        this.camelContext = camelContext;
        this.destinationQueue = destinationQueue != null ? destinationQueue : "petasos.queue.tasks";
        this.targetEndpointUri = targetEndpointUri;
    }

    /**
     * Starts listening to the configured Petasos queue and dispatching messages into Camel routes.
     */
    public synchronized void start() {
        if (running.get()) {
            return;
        }

        if (petasos == null) {
            log.warn("Petasos messaging service is not available, conduit start deferred");
            return;
        }

        try {
            petasosSubscription = petasos.receive(PetasosDestination.queue(destinationQueue), this::handleIncomingPetasosMessage);
            running.set(true);
            log.info("Started PetasosQueueToExchangeConduit on queue [{}] -> target [{}]",
                    destinationQueue, targetEndpointUri != null ? targetEndpointUri : "dynamic-dispatcher");
        } catch (Exception e) {
            log.error("Failed to start Petasos consumer for queue [{}]: {}", destinationQueue, e.getMessage(), e);
        }
    }

    /**
     * Stops listening to the Petasos queue and closes active subscriptions.
     */
    public synchronized void stop() {
        if (!running.get()) {
            return;
        }

        try {
            if (petasosSubscription != null) {
                petasosSubscription.unsubscribe();
            }
            running.set(false);
            log.info("Stopped PetasosQueueToExchangeConduit on queue [{}]", destinationQueue);
        } catch (Exception e) {
            log.warn("Error stopping PetasosQueueToExchangeConduit: {}", e.getMessage());
        }
    }

    /**
     * Handler invoked when a {@link PetasosMessage} is received from the queue.
     *
     * @param message incoming Petasos message envelope
     * @param context Petasos message context for ack/nack
     */
    public void handleIncomingPetasosMessage(PetasosMessage message, PetasosMessageContext context) {
        if (message == null) {
            return;
        }

        log.info("Received Petasos message [id={}, correlationId={}, type={}] from queue [{}]",
                message.getMessageId(), message.getCorrelationId(), message.getMessageType(), destinationQueue);

        Pragma pragma = null;
        try {
            pragma = convertToPragma(message);
            boolean success = dispatchPragma(pragma, message);

            if (success) {
                if (context != null) {
                    context.acknowledge();
                }
                log.info("Acknowledged Petasos message [id={}] following successful Pragma/{} processing",
                        message.getMessageId(), pragma.getPragmaId());
            } else {
                if (context != null) {
                    context.reject();
                }
                log.warn("Rejected Petasos message [id={}] due to workflow processing failure", message.getMessageId());
            }
        } catch (Exception e) {
            log.error("Error dispatching Petasos message [id={}]: {}", message.getMessageId(), e.getMessage(), e);
            if (context != null) {
                try {
                    context.reject();
                } catch (Exception ignored) {
                }
            }
        }
    }

    /**
     * Converts a {@link PetasosMessage} into a canonical {@link Pragma} instance.
     *
     * @param message incoming message envelope
     * @return canonical Pragma
     */
    public Pragma convertToPragma(PetasosMessage message) {
        if (message == null) {
            return null;
        }

        String payloadStr = message.getPayloadAsString() != null ? message.getPayloadAsString() : "";
        Pragma pragma = null;

        if (StringUtils.isNotBlank(payloadStr) && payloadStr.trim().startsWith("{")) {
            String trimmed = payloadStr.trim();
            if (trimmed.contains("\"pragmaId\"") || trimmed.contains("\"checkpoints\"")) {
                try {
                    pragma = objectMapper.readValue(trimmed, Pragma.class);
                } catch (Exception ignored) {
                }
            } else if (trimmed.contains("\"resourceType\"") && trimmed.contains("\"Task\"")) {
                try {
                    ca.uhn.fhir.context.FhirContext ctx = ca.uhn.fhir.context.FhirContext.forR5();
                    org.hl7.fhir.r5.model.Task task = ctx.newJsonParser().parseResource(org.hl7.fhir.r5.model.Task.class, trimmed);
                    pragma = PragmaFhirConverter.fromFhirTask(task);
                } catch (Exception ignored) {
                }
            }
        }

        if (pragma == null) {
            String pragmaId = StringUtils.isNotBlank(message.getMessageId()) ? message.getMessageId() : UUID.randomUUID().toString();
            pragma = new Pragma(pragmaId, null, PragmaStatus.REQUESTED);
            pragma.setAuthoredOn(message.getTimestamp() != null ? Date.from(message.getTimestamp()) : new Date());

            if (StringUtils.isNotBlank(payloadStr)) {
                Topic container = new Topic("Health", "Petasos", "1.0", "Envelope", message.getMessageType());
                Topic content = new Topic("Health", "Payload", "1.0", "MessageContent", message.getContentType());
                pragma.addInput(ErgonPayload.fromJson(0, container, content, payloadStr));
            }
        }

        // Propagate Petasos envelope metadata
        if (StringUtils.isNotBlank(message.getCorrelationId())) {
            pragma.setCorrelationId(message.getCorrelationId());
        }
        if (StringUtils.isNotBlank(message.getCausationId())) {
            pragma.setCausationId(message.getCausationId());
        }
        if (StringUtils.isNotBlank(message.getSource())) {
            pragma.setSource(message.getSource());
        }
        if (message.getDestination() != null) {
            pragma.setDestination(message.getDestination().getName());
        }
        pragma.setPriority(message.getPriority());
        if (message.getMetadata() != null) {
            for (var entry : message.getMetadata().entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    pragma.addMetadata(entry.getKey(), entry.getValue().toString());
                }
            }
        }

        return pragma;
    }

    /**
     * Dispatches the Pragma into the target Camel endpoint or active Praxis workflow.
     */
    protected boolean dispatchPragma(Pragma pragma, PetasosMessage originalMessage) {
        if (pragma == null) {
            return false;
        }

        if (StringUtils.isNotBlank(targetEndpointUri) && camelContext != null) {
            ProducerTemplate template = camelContext.createProducerTemplate();
            try {
                Exchange result = template.request(targetEndpointUri, exchange -> {
                    exchange.getMessage().setBody(pragma);
                    exchange.setProperty(ErgonBase.PROPERTY_PRAGMA, pragma);
                    exchange.getMessage().setHeader(ErgonBase.HEADER_PRAGMA_ID, pragma.getPragmaId());
                    exchange.getMessage().setHeader("PETASOS_MESSAGE_ID", originalMessage.getMessageId());
                    if (originalMessage.getCorrelationId() != null) {
                        exchange.getMessage().setHeader("PETASOS_CORRELATION_ID", originalMessage.getCorrelationId());
                    }
                });
                return result != null && !result.isFailed() && result.getException() == null;
            } finally {
                try {
                    template.stop();
                } catch (Exception ignored) {
                }
            }
        }

        if (workflowDispatcher != null) {
            return workflowDispatcher.dispatchPragma(pragma);
        }

        log.warn("No target endpoint URI or workflow dispatcher configured for Pragma/{}", pragma.getPragmaId());
        return true;
    }

    // =========================================================================
    // Getters and Setters
    // =========================================================================

    public boolean isRunning() {
        return running.get();
    }

    public String getDestinationQueue() {
        return destinationQueue;
    }

    public void setDestinationQueue(String destinationQueue) {
        this.destinationQueue = destinationQueue;
    }

    public String getTargetEndpointUri() {
        return targetEndpointUri;
    }

    public void setTargetEndpointUri(String targetEndpointUri) {
        this.targetEndpointUri = targetEndpointUri;
    }

    public Petasos getPetasos() {
        return petasos;
    }

    public void setPetasos(Petasos petasos) {
        this.petasos = petasos;
    }

    public CamelContext getCamelContext() {
        return camelContext;
    }

    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    public PragmaWorkflowDispatcher getWorkflowDispatcher() {
        return workflowDispatcher;
    }

    public void setWorkflowDispatcher(PragmaWorkflowDispatcher workflowDispatcher) {
        this.workflowDispatcher = workflowDispatcher;
    }
}
