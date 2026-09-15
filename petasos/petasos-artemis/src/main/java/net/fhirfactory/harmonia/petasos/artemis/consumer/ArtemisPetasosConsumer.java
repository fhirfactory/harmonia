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
import jakarta.jms.MessageConsumer;
import jakarta.jms.Session;
import net.fhirfactory.harmonia.petasos.api.config.PetasosConsumerConfig;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosConsumer;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosMessageHandler;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosSubscription;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.exception.PetasosMessagingException;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import net.fhirfactory.harmonia.petasos.artemis.connection.ArtemisConnectionManager;
import net.fhirfactory.harmonia.petasos.artemis.converter.ArtemisMessageConverter;
import net.fhirfactory.harmonia.petasos.core.dedup.DuplicateDetector;
import net.fhirfactory.harmonia.petasos.core.metrics.PetasosMetricsCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ActiveMQ Artemis implementation of {@link PetasosConsumer}.
 */
public class ArtemisPetasosConsumer implements PetasosConsumer {

    private static final Logger log = LoggerFactory.getLogger(ArtemisPetasosConsumer.class);

    private final ArtemisConnectionManager connectionManager;
    private final PetasosConsumerConfig defaultConfig;
    private final PetasosMetricsCollector metrics;
    private final DuplicateDetector duplicateDetector;
    private final List<PetasosSubscription> subscriptions = new CopyOnWriteArrayList<>();
    private final AtomicBoolean closed = new AtomicBoolean(false);

    public ArtemisPetasosConsumer(
            ArtemisConnectionManager connectionManager,
            PetasosConsumerConfig defaultConfig,
            PetasosMetricsCollector metrics,
            DuplicateDetector duplicateDetector) {
        this.connectionManager = Objects.requireNonNull(connectionManager, "connectionManager must not be null");
        this.defaultConfig = defaultConfig != null ? defaultConfig : PetasosConsumerConfig.defaultConfiguration();
        this.metrics = metrics != null ? metrics : new PetasosMetricsCollector();
        this.duplicateDetector = duplicateDetector;
    }

    @Override
    public PetasosSubscription subscribe(PetasosDestination destination, PetasosMessageHandler handler) throws PetasosMessagingException {
        return subscribe(destination, defaultConfig, handler);
    }

    @Override
    public PetasosSubscription subscribe(
            PetasosDestination destination,
            PetasosConsumerConfig config,
            PetasosMessageHandler handler) throws PetasosMessagingException {

        if (closed.get()) {
            throw new IllegalStateException("ArtemisPetasosConsumer is closed");
        }
        if (destination == null) {
            throw new IllegalArgumentException("Destination must not be null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("MessageHandler must not be null");
        }

        PetasosConsumerConfig consumerConfig = config != null ? config : defaultConfig;
        try {
            int ackMode = consumerConfig.getAcknowledgeMode() == PetasosConsumerConfig.AcknowledgeMode.AUTO_ACKNOWLEDGE
                    ? Session.AUTO_ACKNOWLEDGE
                    : Session.CLIENT_ACKNOWLEDGE;

            Session session = connectionManager.createSession(false, ackMode);
            Destination jmsDestination = destination.isQueue()
                    ? session.createQueue(destination.getName())
                    : session.createTopic(destination.getName());

            MessageConsumer messageConsumer = consumerConfig.getMessageSelector() != null
                    ? session.createConsumer(jmsDestination, consumerConfig.getMessageSelector())
                    : session.createConsumer(jmsDestination);

            ArtemisPetasosSubscription subscription = new ArtemisPetasosSubscription(
                    session,
                    messageConsumer,
                    destination,
                    handler,
                    consumerConfig,
                    connectionManager,
                    metrics,
                    duplicateDetector
            );

            subscriptions.add(subscription);
            log.info("Registered Petasos subscriber for destination {}", destination);
            return subscription;

        } catch (Exception e) {
            metrics.recordProcessingFailure();
            log.error("Failed to subscribe to destination {}: {}", destination, e.getMessage(), e);
            throw new PetasosMessagingException("Failed to subscribe to destination " + destination + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<PetasosMessage> receive(PetasosDestination destination, Duration timeout) throws PetasosMessagingException {
        if (closed.get()) {
            throw new IllegalStateException("ArtemisPetasosConsumer is closed");
        }
        if (destination == null) {
            throw new IllegalArgumentException("Destination must not be null");
        }

        long timeoutMs = timeout != null ? timeout.toMillis() : 5000L;
        try {
            Session session = connectionManager.createSession(false, Session.AUTO_ACKNOWLEDGE);
            try {
                Destination jmsDestination = destination.isQueue()
                        ? session.createQueue(destination.getName())
                        : session.createTopic(destination.getName());

                MessageConsumer consumer = session.createConsumer(jmsDestination);
                try {
                    jakarta.jms.Message jmsMessage = timeoutMs > 0 ? consumer.receive(timeoutMs) : consumer.receiveNoWait();
                    if (jmsMessage == null) {
                        return Optional.empty();
                    }
                    metrics.recordMessageReceived();
                    PetasosMessage petasosMessage = ArtemisMessageConverter.toPetasosMessage(jmsMessage);
                    return Optional.of(petasosMessage);
                } finally {
                    consumer.close();
                }
            } finally {
                session.close();
            }
        } catch (Exception e) {
            metrics.recordProcessingFailure();
            log.error("Failed to receive message from {}: {}", destination, e.getMessage(), e);
            throw new PetasosMessagingException("Failed to receive message from " + destination + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void close() {
        if (closed.compareAndSet(false, true)) {
            for (PetasosSubscription sub : subscriptions) {
                try {
                    sub.close();
                } catch (Exception e) {
                    log.debug("Error closing subscription: {}", e.getMessage());
                }
            }
            subscriptions.clear();
            log.info("ArtemisPetasosConsumer closed.");
        }
    }
}
