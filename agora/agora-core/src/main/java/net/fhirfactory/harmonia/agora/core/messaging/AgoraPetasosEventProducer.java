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

package net.fhirfactory.harmonia.agora.core.messaging;

import net.fhirfactory.harmonia.agora.api.model.AgoraCollaborationEvent;
import net.fhirfactory.harmonia.agora.api.topic.AgoraTopics;
import net.fhirfactory.harmonia.agora.core.security.AgoraSecurityUtils;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.exception.PetasosException;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import net.fhirfactory.harmonia.petasos.api.producer.PetasosProducer;
import net.fhirfactory.harmonia.themis.api.ThemisAuthorizer;
import net.fhirfactory.harmonia.themis.api.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Petasos transport producer for Agora collaboration events.
 * Dispatches translated domain messages onto Petasos queues with correlation propagation
 * and Themis default-deny security evaluation.
 */
public class AgoraPetasosEventProducer implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgoraPetasosEventProducer.class);

    private final PetasosProducer petasosProducer;
    private final ThemisAuthorizer themisAuthorizer;

    public AgoraPetasosEventProducer(PetasosProducer petasosProducer) {
        this(petasosProducer, null);
    }

    public AgoraPetasosEventProducer(PetasosProducer petasosProducer, ThemisAuthorizer themisAuthorizer) {
        this.petasosProducer = Objects.requireNonNull(petasosProducer, "PetasosProducer must not be null");
        this.themisAuthorizer = themisAuthorizer;
    }

    /**
     * Publishes an inbound collaboration event to the default inbound Agora queue.
     *
     * @param event the collaboration event
     * @return the dispatched Petasos message envelope
     * @throws PetasosException  if the broker send fails
     * @throws SecurityException if Themis authorization evaluates to DENY
     */
    public PetasosMessage publishCollaborationEvent(AgoraCollaborationEvent event) throws PetasosException {
        return publishCollaborationEvent(PetasosDestination.queue(AgoraTopics.QUEUE_AGORA_INBOUND), event);
    }

    /**
     * Publishes an outbound collaboration event or task notice to the outbound Agora queue.
     *
     * @param event the collaboration event
     * @return the dispatched Petasos message envelope
     * @throws PetasosException  if the broker send fails
     * @throws SecurityException if Themis authorization evaluates to DENY
     */
    public PetasosMessage publishOutboundNotice(AgoraCollaborationEvent event) throws PetasosException {
        return publishCollaborationEvent(PetasosDestination.queue(AgoraTopics.QUEUE_AGORA_OUTBOUND), event);
    }

    /**
     * Publishes an event to a specified destination after verifying Themis authorization.
     *
     * @param destination the destination queue or topic
     * @param event       the collaboration event
     * @return the dispatched Petasos message envelope
     * @throws PetasosException  if the broker send fails
     * @throws SecurityException if Themis authorization evaluates to DENY
     */
    public PetasosMessage publishCollaborationEvent(PetasosDestination destination, AgoraCollaborationEvent event) throws PetasosException {
        Objects.requireNonNull(event, "AgoraCollaborationEvent must not be null");
        Objects.requireNonNull(destination, "PetasosDestination must not be null");

        evaluateSecurityGovernance(event);

        PetasosMessage petasosMessage = AgoraPetasosMessageTranslator.toPetasosMessage(event, destination);
        petasosProducer.send(destination, petasosMessage);

        LOGGER.info("Published Agora collaboration event messageId={}, correlationId={}, destination={}",
                petasosMessage.getMessageId(), event.getCorrelationId(), destination.getName());

        return petasosMessage;
    }

    /**
     * Asynchronously publishes a collaboration event.
     *
     * @param destination the destination
     * @param event       the event
     * @return future completed when broker acknowledges message
     */
    public CompletableFuture<Void> publishAsync(PetasosDestination destination, AgoraCollaborationEvent event) {
        Objects.requireNonNull(event, "AgoraCollaborationEvent must not be null");
        Objects.requireNonNull(destination, "PetasosDestination must not be null");

        evaluateSecurityGovernance(event);

        PetasosMessage petasosMessage = AgoraPetasosMessageTranslator.toPetasosMessage(event, destination);
        return petasosProducer.sendAsync(destination, petasosMessage);
    }

    private void evaluateSecurityGovernance(AgoraCollaborationEvent event) {
        if (themisAuthorizer == null) {
            return;
        }

        ThemisPrincipal principal = (event.getSecurityContext() != null && event.getSecurityContext().requestingPrincipal() != null)
                ? event.getSecurityContext().requestingPrincipal()
                : (event.getSender() != null && !event.getSender().isBlank()
                ? ThemisPrincipal.human(event.getSender())
                : ThemisPrincipal.service("service:agora"));

        String resourceType = (event.getHarmoniaResourceType() != null && !event.getHarmoniaResourceType().isBlank())
                ? event.getHarmoniaResourceType()
                : "AgoraCollaboration";

        String resourceId = (event.getHarmoniaResourceId() != null && !event.getHarmoniaResourceId().isBlank())
                ? event.getHarmoniaResourceId()
                : (event.getRoomId() != null ? event.getRoomId() : "unknown-resource");

        ThemisResource resource = ThemisResource.of(resourceType, resourceId);

        ThemisSecurityContext secContext = event.getSecurityContext() != null
                ? event.getSecurityContext()
                : ThemisSecurityContext.fromPrincipal(principal, event.getCorrelationId());

        ThemisAuthorizationRequest authRequest = ThemisAuthorizationRequest.builder()
                .principal(principal)
                .authorities(AgoraSecurityUtils.extractAuthorities(secContext))
                .action(ThemisAction.PROCESS)
                .target(resource)
                .context(secContext)
                .build();

        ThemisAuthorizationDecision decision = themisAuthorizer.authorize(authRequest);

        if (decision == null || decision.isDenied()) {
            String reason = decision != null ? decision.reason().name() : "DEFAULT_DENY";
            LOGGER.warn("Themis security authorization DENIED for eventId={}, correlationId={}, reason={}",
                    event.getEventId(), event.getCorrelationId(), reason);
            throw new SecurityException("Themis policy authorization denied event publishing: " + reason);
        }
    }

    public ThemisAuthorizer getThemisAuthorizer() {
        return themisAuthorizer;
    }

    public PetasosProducer getPetasosProducer() {
        return petasosProducer;
    }

    @Override
    public void close() {
        petasosProducer.close();
    }
}
