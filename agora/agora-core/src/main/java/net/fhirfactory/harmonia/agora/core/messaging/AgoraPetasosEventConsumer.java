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
import net.fhirfactory.harmonia.agora.core.security.AgoraSecurityUtils;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosConsumer;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosMessageContext;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosMessageHandler;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosSubscription;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.exception.PetasosException;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import net.fhirfactory.harmonia.themis.api.ThemisAuthorizer;
import net.fhirfactory.harmonia.themis.api.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Petasos transport consumer for Agora outbound collaboration notifications.
 * Consumes messages from Petasos queues, validates them against Themis default-deny security,
 * and passes the translated domain events to registered handlers without direct Ponos dependency.
 */
public class AgoraPetasosEventConsumer implements PetasosMessageHandler, AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgoraPetasosEventConsumer.class);

    private final Consumer<AgoraCollaborationEvent> eventHandler;
    private final ThemisAuthorizer themisAuthorizer;
    private PetasosSubscription subscription;

    public AgoraPetasosEventConsumer(Consumer<AgoraCollaborationEvent> eventHandler) {
        this(eventHandler, null);
    }

    public AgoraPetasosEventConsumer(Consumer<AgoraCollaborationEvent> eventHandler, ThemisAuthorizer themisAuthorizer) {
        this.eventHandler = Objects.requireNonNull(eventHandler, "eventHandler Consumer must not be null");
        this.themisAuthorizer = themisAuthorizer;
    }

    @Override
    public void onMessage(PetasosMessage message, PetasosMessageContext context) throws PetasosException {
        if (message == null || message.getPayload() == null || message.getPayload().length == 0) {
            if (context != null) {
                context.acknowledge();
            }
            return;
        }

        AgoraCollaborationEvent event;
        try {
            event = AgoraPetasosMessageTranslator.fromPetasosMessage(message);
        } catch (Exception e) {
            LOGGER.error("Failed to deserialize Petasos message payload messageId={}: {}",
                    message.getMessageId(), e.getMessage());
            if (context != null) {
                context.reject(false);
            }
            return;
        }

        if (themisAuthorizer != null) {
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
                LOGGER.warn("Themis authorization DENIED during consumption for messageId={}, correlationId={}, reason={}",
                        message.getMessageId(), event.getCorrelationId(), reason);
                if (context != null) {
                    context.reject(false);
                }
                return;
            }
        }

        if (context != null) {
            context.acknowledge();
        }

        LOGGER.info("Delivering Agora collaboration event eventId={}, correlationId={}",
                event.getEventId(), event.getCorrelationId());
        eventHandler.accept(event);
    }

    /**
     * Subscribes this consumer to a Petasos destination.
     *
     * @param petasosConsumer the Petasos consumer runtime
     * @param destination     the destination queue or topic
     * @return the subscription handle
     * @throws PetasosException if subscription fails
     */
    public PetasosSubscription subscribe(PetasosConsumer petasosConsumer, PetasosDestination destination) throws PetasosException {
        Objects.requireNonNull(petasosConsumer, "PetasosConsumer must not be null");
        Objects.requireNonNull(destination, "PetasosDestination must not be null");

        this.subscription = petasosConsumer.subscribe(destination, this);
        return this.subscription;
    }

    public ThemisAuthorizer getThemisAuthorizer() {
        return themisAuthorizer;
    }

    public Consumer<AgoraCollaborationEvent> getEventHandler() {
        return eventHandler;
    }

    public PetasosSubscription getSubscription() {
        return subscription;
    }

    @Override
    public void close() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}
