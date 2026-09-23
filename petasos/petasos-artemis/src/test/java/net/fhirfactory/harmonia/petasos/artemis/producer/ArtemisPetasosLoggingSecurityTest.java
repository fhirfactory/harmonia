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

package net.fhirfactory.harmonia.petasos.artemis.producer;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import jakarta.jms.*;
import net.fhirfactory.harmonia.petasos.api.config.PetasosConsumerConfig;
import net.fhirfactory.harmonia.petasos.api.config.PetasosProducerConfig;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosMessageHandler;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.exception.PetasosMessagingException;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import net.fhirfactory.harmonia.petasos.artemis.connection.ArtemisConnectionManager;
import net.fhirfactory.harmonia.petasos.artemis.consumer.ArtemisPetasosSubscription;
import net.fhirfactory.harmonia.petasos.artemis.converter.ArtemisMessageConverter;
import net.fhirfactory.harmonia.petasos.core.dedup.DuplicateDetector;
import net.fhirfactory.harmonia.petasos.core.metrics.PetasosMetricsCollector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ArtemisPetasosLoggingSecurityTest {

    private static final String PHI_MARKER = "PATIENT-PHI-MARKER-92831";
    private static final String SECRET_MARKER = "TOKEN-SECRET-MARKER-81742";

    @Mock
    private ArtemisConnectionManager connectionManager;

    @Mock
    private Session session;

    @Mock
    private MessageConsumer consumer;

    private ListAppender<ILoggingEvent> producerAppender;
    private ListAppender<ILoggingEvent> subscriptionAppender;
    private Logger producerLogger;
    private Logger subscriptionLogger;

    @BeforeEach
    void setUp() {
        producerLogger = (Logger) LoggerFactory.getLogger(ArtemisPetasosProducer.class);
        producerAppender = new ListAppender<>();
        producerAppender.start();
        producerLogger.addAppender(producerAppender);

        subscriptionLogger = (Logger) LoggerFactory.getLogger(ArtemisPetasosSubscription.class);
        subscriptionAppender = new ListAppender<>();
        subscriptionAppender.start();
        subscriptionLogger.addAppender(subscriptionAppender);
    }

    @AfterEach
    void tearDown() {
        if (producerLogger != null && producerAppender != null) {
            producerLogger.detachAppender(producerAppender);
            producerAppender.stop();
        }
        if (subscriptionLogger != null && subscriptionAppender != null) {
            subscriptionLogger.detachAppender(subscriptionAppender);
            subscriptionAppender.stop();
        }
    }

    @Test
    @DisplayName("ArtemisPetasosProducer sanitizes JMS exception logging and suppresses PHI and secret markers")
    void testProducerLoggingSuppressesPhiAndSecrets() throws Exception {
        when(connectionManager.createSession(anyBoolean(), anyInt()))
                .thenThrow(new JMSException("Artemis failure with payload " + PHI_MARKER + " and secret " + SECRET_MARKER));

        PetasosProducerConfig config = PetasosProducerConfig.builder()
                .build();
        ArtemisPetasosProducer producer = new ArtemisPetasosProducer(connectionManager, config, new PetasosMetricsCollector());

        PetasosMessage message = PetasosMessage.builder()
                .messageId("msg-sec-001")
                .correlationId("corr-sec-001")
                .messageType("ADT^A01")
                .payload("safe-payload")
                .build();

        PetasosDestination destination = PetasosDestination.queue("test.clinical.send");

        assertThatThrownBy(() -> producer.send(destination, message))
                .isInstanceOf(PetasosMessagingException.class)
                .satisfies(ex -> {
                    assertThat(ex.getMessage())
                            .doesNotContain(PHI_MARKER)
                            .doesNotContain(SECRET_MARKER)
                            .contains("jakarta.jms.JMSException");
                });

        List<ILoggingEvent> events = producerAppender.list;
        assertThat(events).isNotEmpty();

        for (ILoggingEvent event : events) {
            assertThat(event.getFormattedMessage())
                    .doesNotContain(PHI_MARKER)
                    .doesNotContain(SECRET_MARKER);
            if (event.getLevel() == Level.WARN || event.getLevel() == Level.ERROR) {
                assertThat(event.getFormattedMessage())
                        .contains("exception=jakarta.jms.JMSException")
                        .contains("msg-sec-001");
            }
        }
    }

    @Test
    @DisplayName("ArtemisPetasosSubscription sanitizes handler exception logging and suppresses PHI and secret markers")
    void testSubscriptionLoggingSuppressesPhiAndSecrets() throws Exception {
        PetasosMessageHandler handler = (message, context) -> {
            throw new RuntimeException("Handler crash echoing " + PHI_MARKER + " and token " + SECRET_MARKER);
        };

        PetasosConsumerConfig config = PetasosConsumerConfig.builder()
                .autoAcknowledgeOnSuccess(false)
                .autoRejectOnError(true)
                .build();

        PetasosDestination destination = PetasosDestination.queue("test.clinical.consume");
        DuplicateDetector duplicateDetector = new DuplicateDetector(100, Duration.ofMinutes(5));
        PetasosMetricsCollector metrics = new PetasosMetricsCollector();

        ArtemisPetasosSubscription subscription = new ArtemisPetasosSubscription(
                session, consumer, destination, handler, config, connectionManager, metrics, duplicateDetector
        );

        BytesMessage msg = mock(BytesMessage.class);
        when(msg.getStringProperty(ArtemisMessageConverter.HDR_PETASOS_MESSAGE_ID)).thenReturn("msg-sub-001");
        when(msg.getJMSMessageID()).thenReturn("ID:msg-sub-001");
        when(msg.getStringProperty(ArtemisMessageConverter.HDR_PETASOS_CORRELATION_ID)).thenReturn("corr-sub-001");
        when(msg.getStringProperty(ArtemisMessageConverter.HDR_PETASOS_DESTINATION_NAME)).thenReturn("test.clinical.consume");
        when(msg.getStringProperty(ArtemisMessageConverter.HDR_PETASOS_DESTINATION_TYPE)).thenReturn("QUEUE");
        when(msg.getBodyLength()).thenReturn(0L);

        subscription.onMessage(msg);

        List<ILoggingEvent> events = subscriptionAppender.list;
        assertThat(events).isNotEmpty();

        for (ILoggingEvent event : events) {
            assertThat(event.getFormattedMessage())
                    .doesNotContain(PHI_MARKER)
                    .doesNotContain(SECRET_MARKER);
            if (event.getLevel() == Level.ERROR) {
                assertThat(event.getFormattedMessage())
                        .contains("exception=java.lang.RuntimeException")
                        .contains("test.clinical.consume")
                        .contains("msg-sub-001");
            }
        }

        verify(session, times(1)).recover();
    }
}
