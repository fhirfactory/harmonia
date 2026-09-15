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

package net.fhirfactory.harmonia.petasos.api.producer;

import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.exception.PetasosException;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;

import java.util.concurrent.CompletableFuture;

/**
 * Interface for producing and publishing Petasos messages to Artemis queues/topics.
 */
public interface PetasosProducer extends AutoCloseable {

    /**
     * Sends a message to the specified destination.
     * If message is marked as durable, delivery is confirmed persistent before returning.
     *
     * @param destination the destination queue or topic
     * @param message     the message envelope
     * @throws PetasosException if the send operation fails
     */
    void send(PetasosDestination destination, PetasosMessage message) throws PetasosException;

    /**
     * Sends a message using the destination configured in the message envelope.
     *
     * @param message the message envelope containing the destination
     * @throws PetasosException if send fails or destination is null
     */
    default void send(PetasosMessage message) throws PetasosException {
        if (message == null || message.getDestination() == null) {
            throw new IllegalArgumentException("Message and destination must not be null");
        }
        send(message.getDestination(), message);
    }

    /**
     * Explicitly forces durable delivery of a message to the specified destination.
     *
     * @param destination the destination
     * @param message     the message envelope
     * @throws PetasosException if send fails
     */
    default void sendDurable(PetasosDestination destination, PetasosMessage message) throws PetasosException {
        if (message == null) {
            throw new IllegalArgumentException("Message must not be null");
        }
        PetasosMessage durableMsg = message.isDurable() ? message : message.toBuilder().durable(true).build();
        send(destination, durableMsg);
    }

    /**
     * Asynchronously sends a message to the specified destination.
     *
     * @param destination the destination
     * @param message     the message envelope
     * @return CompletableFuture completing when the broker acknowledges the message
     */
    CompletableFuture<Void> sendAsync(PetasosDestination destination, PetasosMessage message);

    /**
     * Closes this producer and releases underlying resources.
     */
    @Override
    void close();
}
