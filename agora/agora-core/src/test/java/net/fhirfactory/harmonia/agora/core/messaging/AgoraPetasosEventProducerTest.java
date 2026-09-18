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
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import net.fhirfactory.harmonia.petasos.api.producer.PetasosProducer;
import net.fhirfactory.harmonia.themis.api.ThemisAuthorizer;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecisionReason;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("AgoraPetasosEventProducer Tests")
@Timeout(10)
class AgoraPetasosEventProducerTest {

    private PetasosProducer petasosProducerMock;
    private ThemisAuthorizer themisAuthorizerMock;

    @BeforeEach
    void setUp() {
        petasosProducerMock = mock(PetasosProducer.class);
        themisAuthorizerMock = mock(ThemisAuthorizer.class);
    }

    @Test
    @DisplayName("Should publish event successfully when Themis evaluates ALLOW")
    void testPublishEventAuthorized() throws Exception {
        when(themisAuthorizerMock.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.allow("test-policy", "corr-prod-1"));

        AgoraPetasosEventProducer producer = new AgoraPetasosEventProducer(petasosProducerMock, themisAuthorizerMock);

        ThemisSecurityContext secContext = ThemisSecurityContext.fromPrincipal(
                ThemisPrincipal.human("user:dr-bob"), "corr-prod-1");

        AgoraCollaborationEvent event = AgoraCollaborationEvent.builder()
                .eventId("evt-001")
                .eventType("TASK_NOTE")
                .roomId("!room1:synapse")
                .roomType(AgoraRoomType.TASKS)
                .sender("@_harmonia_p_dr-bob:synapse")
                .harmoniaResourceType("Task")
                .harmoniaResourceId("task-123")
                .content("Clinical note")
                .correlationId("corr-prod-1")
                .securityContext(secContext)
                .build();

        PetasosMessage published = producer.publishCollaborationEvent(event);

        assertThat(published).isNotNull();
        assertThat(published.getMessageId()).isEqualTo("evt-001");
        assertThat(published.getCorrelationId()).isEqualTo("corr-prod-1");

        ArgumentCaptor<PetasosDestination> destCaptor = ArgumentCaptor.forClass(PetasosDestination.class);
        ArgumentCaptor<PetasosMessage> msgCaptor = ArgumentCaptor.forClass(PetasosMessage.class);

        verify(petasosProducerMock, times(1)).send(destCaptor.capture(), msgCaptor.capture());
        assertThat(destCaptor.getValue().getName()).isEqualTo(AgoraTopics.QUEUE_AGORA_INBOUND);
        assertThat(msgCaptor.getValue().getMessageId()).isEqualTo("evt-001");
    }

    @Test
    @DisplayName("Should enforce default-deny: reject publishing and throw SecurityException when Themis denies")
    void testPublishEventDeniedByThemis() {
        when(themisAuthorizerMock.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.defaultDeny("corr-deny-1", "Default deny enforced"));

        AgoraPetasosEventProducer producer = new AgoraPetasosEventProducer(petasosProducerMock, themisAuthorizerMock);

        AgoraCollaborationEvent event = AgoraCollaborationEvent.builder()
                .eventId("evt-deny")
                .eventType("TASK_NOTE")
                .roomId("!room-deny:synapse")
                .sender("@unauthorized:synapse")
                .harmoniaResourceType("Task")
                .harmoniaResourceId("task-999")
                .content("Unauthorized update")
                .correlationId("corr-deny-1")
                .build();

        assertThatThrownBy(() -> producer.publishCollaborationEvent(event))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Themis policy authorization denied");

        verify(petasosProducerMock, never()).send(any(), any());
    }

    @Test
    @DisplayName("Should publish outbound notice to outbound queue")
    void testPublishOutboundNotice() throws Exception {
        AgoraPetasosEventProducer producer = new AgoraPetasosEventProducer(petasosProducerMock);

        AgoraCollaborationEvent event = AgoraCollaborationEvent.builder()
                .eventId("evt-out-1")
                .eventType("TASK_NOTIFICATION")
                .roomId("!room-out:synapse")
                .content("Task notification outbound")
                .correlationId("corr-out-1")
                .build();

        producer.publishOutboundNotice(event);

        ArgumentCaptor<PetasosDestination> destCaptor = ArgumentCaptor.forClass(PetasosDestination.class);
        verify(petasosProducerMock, times(1)).send(destCaptor.capture(), any(PetasosMessage.class));
        assertThat(destCaptor.getValue().getName()).isEqualTo(AgoraTopics.QUEUE_AGORA_OUTBOUND);
    }
}
