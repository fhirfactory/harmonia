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
import net.fhirfactory.harmonia.agora.api.model.AgoraRoomType;
import net.fhirfactory.harmonia.agora.api.topic.AgoraTopics;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosConsumer;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosMessageContext;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosSubscription;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import net.fhirfactory.harmonia.themis.api.ThemisAuthorizer;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("AgoraPetasosEventConsumer Tests")
@Timeout(10)
class AgoraPetasosEventConsumerTest {

    private ThemisAuthorizer themisAuthorizerMock;
    private PetasosMessageContext contextMock;

    @BeforeEach
    void setUp() {
        themisAuthorizerMock = mock(ThemisAuthorizer.class);
        contextMock = mock(PetasosMessageContext.class);
    }

    @Test
    @DisplayName("Should deliver event to handler and ack context when Themis evaluates ALLOW")
    void testConsumeEventAuthorized() throws Exception {
        when(themisAuthorizerMock.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.allow("allow-policy", "corr-cons-1"));

        AtomicReference<AgoraCollaborationEvent> receivedEvent = new AtomicReference<>();
        AgoraPetasosEventConsumer consumer = new AgoraPetasosEventConsumer(receivedEvent::set, themisAuthorizerMock);

        ThemisSecurityContext secContext = ThemisSecurityContext.fromPrincipal(
                ThemisPrincipal.human("user:dr-alice"), "corr-cons-1");

        AgoraCollaborationEvent event = AgoraCollaborationEvent.builder()
                .eventId("evt-cons-1")
                .eventType("TASK_NOTE")
                .roomId("!room-cons:synapse")
                .roomType(AgoraRoomType.TASKS)
                .sender("@_harmonia_p_dr-alice:synapse")
                .harmoniaResourceType("Task")
                .harmoniaResourceId("task-555")
                .content("Lab results noted")
                .correlationId("corr-cons-1")
                .securityContext(secContext)
                .build();

        PetasosMessage message = AgoraPetasosMessageTranslator.toPetasosMessage(
                event, PetasosDestination.queue(AgoraTopics.QUEUE_AGORA_OUTBOUND));

        consumer.onMessage(message, contextMock);

        assertThat(receivedEvent.get()).isNotNull();
        assertThat(receivedEvent.get().getEventId()).isEqualTo("evt-cons-1");
        assertThat(receivedEvent.get().getContent()).isEqualTo("Lab results noted");

        verify(contextMock, times(1)).acknowledge();
        verify(contextMock, never()).reject(anyBoolean());
    }

    @Test
    @DisplayName("Should enforce default-deny: reject message and nack without invoking handler when Themis denies")
    void testConsumeEventDeniedByThemis() throws Exception {
        when(themisAuthorizerMock.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.defaultDeny("corr-deny-cons", "Default deny"));

        AtomicReference<AgoraCollaborationEvent> receivedEvent = new AtomicReference<>();
        AgoraPetasosEventConsumer consumer = new AgoraPetasosEventConsumer(receivedEvent::set, themisAuthorizerMock);

        AgoraCollaborationEvent event = AgoraCollaborationEvent.builder()
                .eventId("evt-deny-cons")
                .eventType("TASK_NOTE")
                .roomId("!room-deny:synapse")
                .sender("@unauthorized:synapse")
                .harmoniaResourceType("Task")
                .harmoniaResourceId("task-888")
                .content("Unauthorized change")
                .correlationId("corr-deny-cons")
                .build();

        PetasosMessage message = AgoraPetasosMessageTranslator.toPetasosMessage(
                event, PetasosDestination.queue(AgoraTopics.QUEUE_AGORA_OUTBOUND));

        consumer.onMessage(message, contextMock);

        assertThat(receivedEvent.get()).isNull();
        verify(contextMock, times(1)).reject(false);
        verify(contextMock, never()).acknowledge();
    }

    @Test
    @DisplayName("Should subscribe to PetasosConsumer and manage subscription handle")
    void testSubscribe() throws Exception {
        PetasosConsumer petasosConsumerMock = mock(PetasosConsumer.class);
        PetasosSubscription subscriptionMock = mock(PetasosSubscription.class);
        PetasosDestination destination = PetasosDestination.queue(AgoraTopics.QUEUE_AGORA_OUTBOUND);

        when(petasosConsumerMock.subscribe(eq(destination), any(AgoraPetasosEventConsumer.class)))
                .thenReturn(subscriptionMock);

        AgoraPetasosEventConsumer consumer = new AgoraPetasosEventConsumer(event -> {});
        PetasosSubscription sub = consumer.subscribe(petasosConsumerMock, destination);

        assertThat(sub).isEqualTo(subscriptionMock);
        assertThat(consumer.getSubscription()).isEqualTo(subscriptionMock);

        consumer.close();
        verify(subscriptionMock, times(1)).unsubscribe();
    }
}
