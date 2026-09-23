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

package net.fhirfactory.harmonia.petasos.artemis.consumer;

import jakarta.jms.*;
import net.fhirfactory.harmonia.petasos.api.config.PetasosConsumerConfig;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosMessageHandler;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import net.fhirfactory.harmonia.petasos.artemis.connection.ArtemisConnectionManager;
import net.fhirfactory.harmonia.petasos.artemis.converter.ArtemisMessageConverter;
import net.fhirfactory.harmonia.petasos.core.dedup.DuplicateDetector;
import net.fhirfactory.harmonia.petasos.core.metrics.PetasosMetricsCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ArtemisPetasosSubscriptionTest {

    @Mock
    private Session session;

    @Mock
    private MessageConsumer consumer;

    @Mock
    private ArtemisConnectionManager connectionManager;

    @Mock
    private Queue dlqQueue;

    @Mock
    private MessageProducer dlqProducer;

    private DuplicateDetector duplicateDetector;
    private PetasosMetricsCollector metrics;
    private PetasosDestination destination;

    @BeforeEach
    void setUp() throws Exception {
        duplicateDetector = new DuplicateDetector(100, Duration.ofMinutes(5));
        metrics = new PetasosMetricsCollector();
        destination = PetasosDestination.queue("test.clinical.queue");

        when(session.createQueue(any())).thenReturn(dlqQueue);
        when(session.createProducer(dlqQueue)).thenReturn(dlqProducer);
    }

    private BytesMessage createMockBytesMessage(String messageId, String dedupId, String correlationId, String causationId, int deliveryCount) throws Exception {
        BytesMessage msg = mock(BytesMessage.class);
        when(msg.getStringProperty(ArtemisMessageConverter.HDR_PETASOS_MESSAGE_ID)).thenReturn(messageId);
        when(msg.getJMSMessageID()).thenReturn("ID:" + messageId);
        when(msg.getStringProperty(ArtemisMessageConverter.HDR_PETASOS_CORRELATION_ID)).thenReturn(correlationId);
        when(msg.getStringProperty(ArtemisMessageConverter.HDR_PETASOS_CAUSATION_ID)).thenReturn(causationId);
        when(msg.getStringProperty(ArtemisMessageConverter.HDR_AMQ_DUPL_ID)).thenReturn(dedupId);
        when(msg.getStringProperty(ArtemisMessageConverter.HDR_PETASOS_DESTINATION_NAME)).thenReturn("test.clinical.queue");
        when(msg.getStringProperty(ArtemisMessageConverter.HDR_PETASOS_DESTINATION_TYPE)).thenReturn("QUEUE");
        when(msg.getBodyLength()).thenReturn(0L);

        when(msg.propertyExists("JMSXDeliveryCount")).thenReturn(true);
        when(msg.getIntProperty("JMSXDeliveryCount")).thenReturn(deliveryCount);

        return msg;
    }

    @Test
    void testFirstDeliverySuccessRecordsMessageAndSubsequentDuplicateIsSkipped() throws Exception {
        AtomicInteger handlerCallCount = new AtomicInteger(0);
        AtomicReference<PetasosMessage> deliveredMessage = new AtomicReference<>();

        PetasosMessageHandler handler = (message, context) -> {
            handlerCallCount.incrementAndGet();
            deliveredMessage.set(message);
        };

        PetasosConsumerConfig config = PetasosConsumerConfig.builder()
                .autoAcknowledgeOnSuccess(true)
                .build();

        ArtemisPetasosSubscription subscription = new ArtemisPetasosSubscription(
                session, consumer, destination, handler, config, connectionManager, metrics, duplicateDetector
        );

        String messageId = "msg-001";
        String dedupId = "DEDUP-001";
        String correlationId = "corr-001";
        String causationId = "caus-001";

        BytesMessage jmsMessage1 = createMockBytesMessage(messageId, dedupId, correlationId, causationId, 1);

        // First delivery: should be delivered to handler and recorded as processed
        subscription.onMessage(jmsMessage1);

        assertThat(handlerCallCount.get()).isEqualTo(1);
        assertThat(deliveredMessage.get()).isNotNull();
        assertThat(deliveredMessage.get().getMessageId()).isEqualTo(messageId);
        assertThat(deliveredMessage.get().getCorrelationId()).isEqualTo(correlationId);
        assertThat(deliveredMessage.get().getCausationId()).isEqualTo(causationId);
        verify(jmsMessage1, times(1)).acknowledge();

        // Verify duplicate detector now knows this dedupId
        assertThat(duplicateDetector.isDuplicate(dedupId)).isTrue();

        // Second delivery (genuine duplicate): should be skipped without invoking handler
        BytesMessage jmsMessage2 = createMockBytesMessage("msg-002", dedupId, correlationId, causationId, 1);
        subscription.onMessage(jmsMessage2);

        // Handler count should still be 1 (not invoked again)
        assertThat(handlerCallCount.get()).isEqualTo(1);
        // Duplicate message is acknowledged and skipped
        verify(jmsMessage2, times(1)).acknowledge();
    }

    @Test
    void testHandlerFailureFollowedByBrokerRedeliveryIsProcessed() throws Exception {
        AtomicInteger handlerCallCount = new AtomicInteger(0);
        AtomicBoolean shouldFail = new AtomicBoolean(true);
        AtomicInteger lastDeliveryCount = new AtomicInteger(0);

        PetasosMessageHandler handler = (message, context) -> {
            int count = handlerCallCount.incrementAndGet();
            lastDeliveryCount.set(context.getRedeliveryCount());
            if (shouldFail.get()) {
                throw new RuntimeException("Simulated transient processing exception on attempt " + count);
            }
        };

        PetasosConsumerConfig config = PetasosConsumerConfig.builder()
                .autoAcknowledgeOnSuccess(true)
                .autoRejectOnError(true)
                .build();

        ArtemisPetasosSubscription subscription = new ArtemisPetasosSubscription(
                session, consumer, destination, handler, config, connectionManager, metrics, duplicateDetector
        );

        String messageId = "msg-retry-100";
        String dedupId = "DEDUP-RETRY-100";
        String correlationId = "corr-100";
        String causationId = "caus-100";

        BytesMessage firstAttemptMessage = createMockBytesMessage(messageId, dedupId, correlationId, causationId, 1);

        // First attempt fails in handler
        subscription.onMessage(firstAttemptMessage);

        assertThat(handlerCallCount.get()).isEqualTo(1);
        // Session should be recovered to trigger broker redelivery
        verify(session, times(1)).recover();
        // Crucially: duplicate detector MUST NOT have recorded this failed attempt!
        assertThat(duplicateDetector.isDuplicate(dedupId)).isFalse();

        // Broker redelivers the exact same message identity with incremented delivery count
        shouldFail.set(false); // Handler will now succeed
        BytesMessage redeliveredMessage = createMockBytesMessage(messageId, dedupId, correlationId, causationId, 2);

        subscription.onMessage(redeliveredMessage);

        // Handler was invoked a second time on redelivery!
        assertThat(handlerCallCount.get()).isEqualTo(2);
        assertThat(lastDeliveryCount.get()).isEqualTo(2);
        verify(redeliveredMessage, times(1)).acknowledge();

        // Now that redelivery succeeded, it IS recorded in duplicate detector
        assertThat(duplicateDetector.isDuplicate(dedupId)).isTrue();
    }

    @Test
    void testExplicitClientAcknowledgeRecordsMessage() throws Exception {
        AtomicInteger handlerCallCount = new AtomicInteger(0);

        PetasosMessageHandler handler = (message, context) -> {
            handlerCallCount.incrementAndGet();
            context.acknowledge();
        };

        PetasosConsumerConfig config = PetasosConsumerConfig.builder()
                .autoAcknowledgeOnSuccess(false) // Manual client acknowledge
                .build();

        ArtemisPetasosSubscription subscription = new ArtemisPetasosSubscription(
                session, consumer, destination, handler, config, connectionManager, metrics, duplicateDetector
        );

        String dedupId = "DEDUP-MANUAL-ACK";
        BytesMessage msg = createMockBytesMessage("msg-man-1", dedupId, "corr-1", "caus-1", 1);

        subscription.onMessage(msg);

        assertThat(handlerCallCount.get()).isEqualTo(1);
        verify(msg, times(1)).acknowledge();
        assertThat(duplicateDetector.isDuplicate(dedupId)).isTrue();
    }

    @Test
    void testExplicitRejectWithRequeueDoesNotRecordInDuplicateDetector() throws Exception {
        PetasosMessageHandler handler = (message, context) -> {
            context.reject(true); // Requeue = true
        };

        PetasosConsumerConfig config = PetasosConsumerConfig.builder()
                .autoAcknowledgeOnSuccess(false)
                .build();

        ArtemisPetasosSubscription subscription = new ArtemisPetasosSubscription(
                session, consumer, destination, handler, config, connectionManager, metrics, duplicateDetector
        );

        String dedupId = "DEDUP-REQUEUE";
        BytesMessage msg = createMockBytesMessage("msg-requeue", dedupId, "corr-req", "caus-req", 1);

        subscription.onMessage(msg);

        verify(session, times(1)).recover();
        assertThat(duplicateDetector.isDuplicate(dedupId)).isFalse();
    }

    @Test
    void testExplicitRejectToDlqRecordsInDuplicateDetector() throws Exception {
        PetasosMessageHandler handler = (message, context) -> {
            context.reject(false); // Requeue = false -> DLQ
        };

        PetasosConsumerConfig config = PetasosConsumerConfig.builder()
                .autoAcknowledgeOnSuccess(false)
                .build();

        ArtemisPetasosSubscription subscription = new ArtemisPetasosSubscription(
                session, consumer, destination, handler, config, connectionManager, metrics, duplicateDetector
        );

        String dedupId = "DEDUP-DLQ";
        BytesMessage msg = createMockBytesMessage("msg-dlq", dedupId, "corr-dlq", "caus-dlq", 1);

        subscription.onMessage(msg);

        verify(dlqProducer, times(1)).send(msg);
        verify(msg, times(1)).acknowledge();
        assertThat(duplicateDetector.isDuplicate(dedupId)).isTrue();
    }

    @Test
    void testUnsubscribeClosesConsumerAndSession() throws Exception {
        ArtemisPetasosSubscription subscription = new ArtemisPetasosSubscription(
                session, consumer, destination, (msg, ctx) -> {}, null, connectionManager, metrics, duplicateDetector
        );

        assertThat(subscription.isActive()).isTrue();
        subscription.unsubscribe();
        assertThat(subscription.isActive()).isFalse();

        verify(consumer, times(1)).close();
        verify(session, times(1)).close();
    }
}
