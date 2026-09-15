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

package net.fhirfactory.harmonia.petasos.artemis;

import net.fhirfactory.harmonia.petasos.api.Petasos;
import net.fhirfactory.harmonia.petasos.api.config.PetasosConfig;
import net.fhirfactory.harmonia.petasos.api.config.PetasosConsumerConfig;
import net.fhirfactory.harmonia.petasos.api.config.PetasosProducerConfig;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosConsumer;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosMessageHandler;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosSubscription;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.exception.PetasosException;
import net.fhirfactory.harmonia.petasos.api.health.PetasosHealth;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import net.fhirfactory.harmonia.petasos.api.metrics.PetasosMetrics;
import net.fhirfactory.harmonia.petasos.api.producer.PetasosProducer;
import net.fhirfactory.harmonia.petasos.api.topology.PetasosBrokerTopology;
import net.fhirfactory.harmonia.petasos.artemis.connection.ArtemisConnectionManager;
import net.fhirfactory.harmonia.petasos.artemis.consumer.ArtemisPetasosConsumer;
import net.fhirfactory.harmonia.petasos.artemis.producer.ArtemisPetasosProducer;
import net.fhirfactory.harmonia.petasos.core.dedup.DuplicateDetector;
import net.fhirfactory.harmonia.petasos.core.metrics.PetasosMetricsCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Main implementation of the {@link Petasos} messaging facade backed by Apache ActiveMQ Artemis.
 */
public class ArtemisPetasos implements Petasos {

    private static final Logger log = LoggerFactory.getLogger(ArtemisPetasos.class);

    private final PetasosConfig config;
    private final ArtemisConnectionManager connectionManager;
    private final PetasosMetricsCollector metrics;
    private final DuplicateDetector duplicateDetector;
    private final ArtemisPetasosProducer defaultProducer;
    private final ArtemisPetasosConsumer defaultConsumer;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    public ArtemisPetasos(PetasosConfig config) {
        this.config = Objects.requireNonNull(config, "PetasosConfig must not be null");
        this.metrics = new PetasosMetricsCollector();
        this.duplicateDetector = config.isDuplicateDetectionEnabled() ? new DuplicateDetector() : null;
        this.connectionManager = new ArtemisConnectionManager(config, metrics);
        this.defaultProducer = new ArtemisPetasosProducer(connectionManager, PetasosProducerConfig.defaultConfiguration(), metrics);
        this.defaultConsumer = new ArtemisPetasosConsumer(connectionManager, PetasosConsumerConfig.defaultConfiguration(), metrics, duplicateDetector);
    }

    public static ArtemisPetasos create() {
        return new ArtemisPetasos(PetasosConfig.fromEnvironment());
    }

    public static ArtemisPetasos create(PetasosConfig config) {
        return new ArtemisPetasos(config);
    }

    public static ArtemisPetasos create(String brokerUrl) {
        PetasosConfig config = PetasosConfig.builder().addBrokerUrl(brokerUrl).build();
        return new ArtemisPetasos(config);
    }

    @Override
    public PetasosProducer createProducer() throws PetasosException {
        return createProducer(PetasosProducerConfig.defaultConfiguration());
    }

    @Override
    public PetasosProducer createProducer(PetasosProducerConfig producerConfig) throws PetasosException {
        if (closed.get()) {
            throw new IllegalStateException("ArtemisPetasos is closed");
        }
        return new ArtemisPetasosProducer(connectionManager, producerConfig, metrics);
    }

    @Override
    public PetasosConsumer createConsumer() throws PetasosException {
        return createConsumer(PetasosConsumerConfig.defaultConfiguration());
    }

    @Override
    public PetasosConsumer createConsumer(PetasosConsumerConfig consumerConfig) throws PetasosException {
        if (closed.get()) {
            throw new IllegalStateException("ArtemisPetasos is closed");
        }
        return new ArtemisPetasosConsumer(connectionManager, consumerConfig, metrics, duplicateDetector);
    }

    @Override
    public void send(PetasosDestination destination, PetasosMessage message) throws PetasosException {
        defaultProducer.send(destination, message);
    }

    @Override
    public PetasosSubscription receive(PetasosDestination destination, PetasosMessageHandler handler) throws PetasosException {
        return defaultConsumer.subscribe(destination, handler);
    }

    @Override
    public PetasosSubscription receive(PetasosDestination destination, PetasosConsumerConfig consumerConfig, PetasosMessageHandler handler) throws PetasosException {
        return defaultConsumer.subscribe(destination, consumerConfig, handler);
    }

    @Override
    public PetasosHealth health() {
        return connectionManager.health();
    }

    @Override
    public PetasosBrokerTopology brokerTopology() {
        return connectionManager.brokerTopology();
    }

    @Override
    public PetasosMetrics metrics() {
        return metrics;
    }

    @Override
    public PetasosConfig getConfig() {
        return config;
    }

    @Override
    public void close() {
        if (closed.compareAndSet(false, true)) {
            log.info("Shutting down ArtemisPetasos messaging subsystem...");
            try {
                defaultConsumer.close();
            } catch (Exception e) {
                log.debug("Error closing default consumer: {}", e.getMessage());
            }
            try {
                defaultProducer.close();
            } catch (Exception e) {
                log.debug("Error closing default producer: {}", e.getMessage());
            }
            try {
                connectionManager.close();
            } catch (Exception e) {
                log.debug("Error closing connection manager: {}", e.getMessage());
            }
            if (duplicateDetector != null) {
                duplicateDetector.clear();
            }
            log.info("ArtemisPetasos messaging subsystem shutdown completed.");
        }
    }
}
