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

import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageListener;
import jakarta.jms.Session;
import net.fhirfactory.harmonia.petasos.api.config.PetasosConsumerConfig;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosMessageContext;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosMessageHandler;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosSubscription;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.exception.PetasosException;
import net.fhirfactory.harmonia.petasos.api.exception.PetasosMessagingException;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import net.fhirfactory.harmonia.petasos.artemis.connection.ArtemisConnectionManager;
import net.fhirfactory.harmonia.petasos.artemis.converter.ArtemisMessageConverter;
import net.fhirfactory.harmonia.petasos.core.dedup.DuplicateDetector;
import net.fhirfactory.harmonia.petasos.core.metrics.PetasosMetricsCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Artemis consumer subscription implementation managing JMS listener lifecycle,
 * acknowledgements, redelivery counts, and deduplication.
 */
public class ArtemisPetasosSubscription implements PetasosSubscription, MessageListener {

    private static final Logger log = LoggerFactory.getLogger(ArtemisPetasosSubscription.class);

    private final Session session;
    private final MessageConsumer consumer;
    private final PetasosDestination destination;
    private final PetasosMessageHandler handler;
    private final PetasosConsumerConfig config;
    private final ArtemisConnectionManager connectionManager;
    private final PetasosMetricsCollector metrics;
    private final DuplicateDetector duplicateDetector;
    private final AtomicBoolean active = new AtomicBoolean(true);

    public ArtemisPetasosSubscription(
            Session session,
            MessageConsumer consumer,
            PetasosDestination destination,
            PetasosMessageHandler handler,
            PetasosConsumerConfig config,
            ArtemisConnectionManager connectionManager,
            PetasosMetricsCollector metrics,
            DuplicateDetector duplicateDetector) throws JMSException {
        this.session = Objects.requireNonNull(session, "session must not be null");
        this.consumer = Objects.requireNonNull(consumer, "consumer must not be null");
        this.destination = Objects.requireNonNull(destination, "destination must not be null");
        this.handler = Objects.requireNonNull(handler, "handler must not be null");
        this.config = config != null ? config : PetasosConsumerConfig.defaultConfiguration();
        this.connectionManager = connectionManager;
        this.metrics = metrics != null ? metrics : new PetasosMetricsCollector();
        this.duplicateDetector = duplicateDetector;

        this.metrics.incrementConsumers();
        this.consumer.setMessageListener(this);
    }

    @Override
    public PetasosDestination getDestination() {
        return destination;
    }

    @Override
    public boolean isActive() {
        return active.get();
    }

    @Override
    public void onMessage(jakarta.jms.Message jmsMessage) {
        if (!active.get() || jmsMessage == null) {
            return;
        }

        PetasosMessage petasosMessage = null;
        try {
            petasosMessage = ArtemisMessageConverter.toPetasosMessage(jmsMessage);
            metrics.recordMessageReceived();

            // Client-side deduplication check if detector is enabled
            if (duplicateDetector != null) {
                String dedupId = petasosMessage.getDuplicateDetectionId();
                if (!duplicateDetector.isUnique(dedupId)) {
                    log.warn("Duplicate message detected by Petasos consumer: [id={}, dedupId={}]. Acknowledging and skipping.",
                            petasosMessage.getMessageId(), dedupId);
                    jmsMessage.acknowledge();
                    return;
                }
            }

            int deliveryCount = extractDeliveryCount(jmsMessage);
            if (deliveryCount > 1) {
                metrics.recordRedelivery();
            }

            PetasosMessageContextImpl context = new PetasosMessageContextImpl(
                    jmsMessage,
                    session,
                    petasosMessage,
                    destination,
                    deliveryCount,
                    connectionManager,
                    metrics
            );

            log.debug("Delivering PetasosMessage [id={}, type={}] to consumer handler for {}",
                    petasosMessage.getMessageId(), petasosMessage.getMessageType(), destination);

            handler.onMessage(petasosMessage, context);

            // Auto-acknowledge if enabled and not already acknowledged or rejected
            if (config.isAutoAcknowledgeOnSuccess() && !context.isHandled()) {
                context.acknowledge();
            }

        } catch (Exception e) {
            metrics.recordProcessingFailure();
            log.error("Error processing Petasos message from {}: {}", destination, e.getMessage(), e);

            if (config.isAutoRejectOnError()) {
                try {
                    session.recover(); // Triggers Artemis redelivery / DLQ routing based on max-delivery-attempts
                } catch (JMSException ex) {
                    log.error("Failed to recover session after error: {}", ex.getMessage(), ex);
                }
            }
        }
    }

    private int extractDeliveryCount(jakarta.jms.Message jmsMessage) {
        try {
            if (jmsMessage.propertyExists("JMSXDeliveryCount")) {
                return jmsMessage.getIntProperty("JMSXDeliveryCount");
            }
        } catch (JMSException ignored) {
        }
        return 1;
    }

    @Override
    public void unsubscribe() {
        if (active.compareAndSet(true, false)) {
            metrics.decrementConsumers();
            try {
                consumer.close();
            } catch (Exception e) {
                log.debug("Error closing JMS MessageConsumer: {}", e.getMessage());
            }
            try {
                session.close();
            } catch (Exception e) {
                log.debug("Error closing JMS Session: {}", e.getMessage());
            }
            log.debug("ArtemisPetasosSubscription closed for {}", destination);
        }
    }

    private static class PetasosMessageContextImpl implements PetasosMessageContext {
        private final jakarta.jms.Message jmsMessage;
        private final Session session;
        private final PetasosMessage message;
        private final PetasosDestination destination;
        private final int deliveryCount;
        private final ArtemisConnectionManager connectionManager;
        private final PetasosMetricsCollector metrics;
        private final AtomicBoolean handled = new AtomicBoolean(false);

        public PetasosMessageContextImpl(
                jakarta.jms.Message jmsMessage,
                Session session,
                PetasosMessage message,
                PetasosDestination destination,
                int deliveryCount,
                ArtemisConnectionManager connectionManager,
                PetasosMetricsCollector metrics) {
            this.jmsMessage = jmsMessage;
            this.session = session;
            this.message = message;
            this.destination = destination;
            this.deliveryCount = deliveryCount;
            this.connectionManager = connectionManager;
            this.metrics = metrics;
        }

        public boolean isHandled() {
            return handled.get();
        }

        @Override
        public void acknowledge() throws PetasosException {
            if (handled.compareAndSet(false, true)) {
                try {
                    jmsMessage.acknowledge();
                } catch (JMSException e) {
                    metrics.recordProcessingFailure();
                    throw new PetasosMessagingException("Failed to acknowledge message: " + e.getMessage(), e);
                }
            }
        }

        @Override
        public void reject(boolean requeue) throws PetasosException {
            if (handled.compareAndSet(false, true)) {
                metrics.recordProcessingFailure();
                try {
                    if (requeue) {
                        session.recover();
                    } else {
                        metrics.recordDeadLetter();
                        String dlqAddress = connectionManager != null && connectionManager.getConfig() != null
                                ? connectionManager.getConfig().getDeadLetterAddress()
                                : "DLQ";
                        Destination dlqDest = session.createQueue(dlqAddress);
                        jakarta.jms.MessageProducer dlqProducer = session.createProducer(dlqDest);
                        try {
                            dlqProducer.send(jmsMessage);
                        } finally {
                            dlqProducer.close();
                        }
                        jmsMessage.acknowledge();
                    }
                } catch (JMSException e) {
                    throw new PetasosMessagingException("Failed to reject message: " + e.getMessage(), e);
                }
            }
        }

        @Override
        public int getRedeliveryCount() {
            return deliveryCount;
        }

        @Override
        public String getBrokerNodeId() {
            return connectionManager != null ? connectionManager.getActiveNodeId() : null;
        }

        @Override
        public PetasosDestination getDestination() {
            return destination;
        }

        @Override
        public PetasosMessage getOriginalMessage() {
            return message;
        }
    }
}
