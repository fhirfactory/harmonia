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

package net.fhirfactory.harmonia.petasos.api;

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

/**
 * Primary entry point and abstraction for the Petasos messaging framework.
 * <p>
 * Provides high-level messaging operations, lifecycle management, health monitoring,
 * and topology queries while abstracting Apache ActiveMQ Artemis broker implementation details.
 */
public interface Petasos extends AutoCloseable {

    /**
     * Creates a new message producer with default configuration.
     *
     * @return a new PetasosProducer
     * @throws PetasosException if producer creation fails
     */
    PetasosProducer createProducer() throws PetasosException;

    /**
     * Creates a new message producer with custom configuration.
     *
     * @param config producer configuration
     * @return a new PetasosProducer
     * @throws PetasosException if producer creation fails
     */
    PetasosProducer createProducer(PetasosProducerConfig config) throws PetasosException;

    /**
     * Creates a new message consumer with default configuration.
     *
     * @return a new PetasosConsumer
     * @throws PetasosException if consumer creation fails
     */
    PetasosConsumer createConsumer() throws PetasosException;

    /**
     * Creates a new message consumer with custom configuration.
     *
     * @param config consumer configuration
     * @return a new PetasosConsumer
     * @throws PetasosException if consumer creation fails
     */
    PetasosConsumer createConsumer(PetasosConsumerConfig config) throws PetasosException;

    /**
     * Convenience method to send a message to a destination using a managed producer.
     *
     * @param destination target queue or topic
     * @param message     the message envelope
     * @throws PetasosException if send fails
     */
    void send(PetasosDestination destination, PetasosMessage message) throws PetasosException;

    /**
     * Convenience method to send a message using the destination configured in the envelope.
     *
     * @param message the message envelope
     * @throws PetasosException if send fails
     */
    default void send(PetasosMessage message) throws PetasosException {
        if (message == null || message.getDestination() == null) {
            throw new IllegalArgumentException("Message and destination must not be null");
        }
        send(message.getDestination(), message);
    }

    /**
     * Convenience method to subscribe a message handler to a destination.
     *
     * @param destination source queue or topic
     * @param handler     message handler
     * @return subscription handle
     * @throws PetasosException if subscription fails
     */
    PetasosSubscription receive(PetasosDestination destination, PetasosMessageHandler handler) throws PetasosException;

    /**
     * Convenience method to subscribe a message handler with custom consumer configuration.
     *
     * @param destination source queue or topic
     * @param config      consumer configuration
     * @param handler     message handler
     * @return subscription handle
     * @throws PetasosException if subscription fails
     */
    PetasosSubscription receive(PetasosDestination destination, PetasosConsumerConfig config, PetasosMessageHandler handler) throws PetasosException;

    /**
     * Checks and reports the current health of the Petasos subsystem and connected broker.
     *
     * @return PetasosHealth snapshot
     */
    PetasosHealth health();

    /**
     * Retrieves the current Artemis cluster and replication topology.
     *
     * @return PetasosBrokerTopology snapshot
     */
    PetasosBrokerTopology brokerTopology();

    /**
     * Returns operational metrics for observability.
     *
     * @return PetasosMetrics snapshot/live view
     */
    PetasosMetrics metrics();

    /**
     * Returns the active configuration.
     */
    PetasosConfig getConfig();

    /**
     * Closes the Petasos instance, shutting down managed producers, consumers, and connections.
     */
    @Override
    void close();
}
