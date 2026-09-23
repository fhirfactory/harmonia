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

import jakarta.jms.Destination;
import jakarta.jms.JMSProducer;
import jakarta.jms.Session;
import net.fhirfactory.harmonia.petasos.api.config.PetasosProducerConfig;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.exception.PetasosMessagingException;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import net.fhirfactory.harmonia.petasos.artemis.connection.ArtemisConnectionManager;
import net.fhirfactory.harmonia.petasos.artemis.converter.ArtemisMessageConverter;
import net.fhirfactory.harmonia.petasos.core.metrics.PetasosMetricsCollector;
import net.fhirfactory.harmonia.petasos.api.producer.PetasosProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ActiveMQ Artemis implementation of {@link PetasosProducer}.
 */
public class ArtemisPetasosProducer implements PetasosProducer {

    private static final Logger log = LoggerFactory.getLogger(ArtemisPetasosProducer.class);

    private final ArtemisConnectionManager connectionManager;
    private final PetasosProducerConfig config;
    private final PetasosMetricsCollector metrics;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    public ArtemisPetasosProducer(
            ArtemisConnectionManager connectionManager,
            PetasosProducerConfig config,
            PetasosMetricsCollector metrics) {
        this.connectionManager = Objects.requireNonNull(connectionManager, "connectionManager must not be null");
        this.config = config != null ? config : PetasosProducerConfig.defaultConfiguration();
        this.metrics = metrics != null ? metrics : new PetasosMetricsCollector();
        this.metrics.incrementProducers();
    }

    @Override
    public void send(PetasosDestination destination, PetasosMessage message) throws PetasosMessagingException {
        if (closed.get()) {
            throw new IllegalStateException("ArtemisPetasosProducer is closed");
        }
        if (destination == null) {
            throw new IllegalArgumentException("Destination must not be null");
        }
        if (message == null) {
            throw new IllegalArgumentException("Message must not be null");
        }

        int maxAttempts = 5;
        long backoff = 100;
        Exception lastException = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                // Use non-transacted AUTO_ACKNOWLEDGE session for producer dispatch
                Session session = connectionManager.createSession(false, Session.AUTO_ACKNOWLEDGE);
                try {
                    Destination jmsDestination = destination.isQueue()
                            ? session.createQueue(destination.getName())
                            : session.createTopic(destination.getName());

                    jakarta.jms.MessageProducer producer = session.createProducer(jmsDestination);
                    producer.setDeliveryMode(message.isDurable() ? jakarta.jms.DeliveryMode.PERSISTENT : jakarta.jms.DeliveryMode.NON_PERSISTENT);
                    producer.setPriority(message.getPriority());

                    if (message.getExpiration() != null) {
                        long ttl = java.time.Duration.between(java.time.Instant.now(), message.getExpiration()).toMillis();
                        producer.setTimeToLive(Math.max(1, ttl));
                    }

                    jakarta.jms.Message jmsMessage = ArtemisMessageConverter.toJmsMessage(message, session);
                    producer.send(jmsMessage);

                    metrics.recordMessageSent();
                    log.debug("Dispatched PetasosMessage [id={}, correlationId={}, type={}] to destination {}",
                            message.getMessageId(), message.getCorrelationId(), message.getMessageType(), destination);
                    return;

                } finally {
                    try {
                        session.close();
                    } catch (Exception ignored) {
                    }
                }
            } catch (Exception e) {
                lastException = e;
                metrics.recordProcessingFailure();
                if (attempt < maxAttempts) {
                    log.warn("Attempt {}/{} failed to send message [id={}] to destination {} [exception={}]. Retrying in {}ms...",
                            attempt, maxAttempts, message.getMessageId(), destination, e.getClass().getName(), backoff);
                    try {
                        Thread.sleep(backoff);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    backoff = Math.min(backoff * 2, 1000);
                }
            }
        }

        log.error("Failed to send message [id={}] to destination {} after {} attempts [exception={}]",
                message.getMessageId(), destination, maxAttempts, lastException != null ? lastException.getClass().getName() : "unknown");
        throw new PetasosMessagingException("Failed to send message to " + destination + " [exception="
                + (lastException != null ? lastException.getClass().getName() : "unknown") + "]", lastException);
    }

    @Override
    public CompletableFuture<Void> sendAsync(PetasosDestination destination, PetasosMessage message) {
        return CompletableFuture.runAsync(() -> send(destination, message));
    }

    @Override
    public void close() {
        if (closed.compareAndSet(false, true)) {
            metrics.decrementProducers();
            log.debug("ArtemisPetasosProducer closed.");
        }
    }
}
