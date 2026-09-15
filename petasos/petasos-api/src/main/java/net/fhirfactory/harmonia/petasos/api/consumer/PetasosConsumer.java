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

package net.fhirfactory.harmonia.petasos.api.consumer;

import net.fhirfactory.harmonia.petasos.api.config.PetasosConsumerConfig;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.exception.PetasosException;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;

import java.time.Duration;
import java.util.Optional;

/**
 * Interface for consuming messages from Petasos queues and topics.
 */
public interface PetasosConsumer extends AutoCloseable {

    /**
     * Subscribes an asynchronous message handler to the specified destination with default configuration.
     *
     * @param destination the destination queue or topic
     * @param handler     the handler called for each incoming message
     * @return a subscription handle
     * @throws PetasosException if subscription fails
     */
    PetasosSubscription subscribe(PetasosDestination destination, PetasosMessageHandler handler) throws PetasosException;

    /**
     * Subscribes an asynchronous message handler with custom consumer configuration.
     *
     * @param destination the destination queue or topic
     * @param config      the consumer configuration (concurrency, prefetch, filter, etc.)
     * @param handler     the handler called for each incoming message
     * @return a subscription handle
     * @throws PetasosException if subscription fails
     */
    PetasosSubscription subscribe(PetasosDestination destination, PetasosConsumerConfig config, PetasosMessageHandler handler) throws PetasosException;

    /**
     * Synchronously receives a single message from the specified destination waiting up to the given timeout.
     *
     * @param destination the destination
     * @param timeout     maximum wait duration
     * @return optional containing the received message or empty if timeout elapsed
     * @throws PetasosException if receiving fails
     */
    Optional<PetasosMessage> receive(PetasosDestination destination, Duration timeout) throws PetasosException;

    /**
     * Closes this consumer and cancels all active subscriptions.
     */
    @Override
    void close();
}
