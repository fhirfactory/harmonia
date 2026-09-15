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

import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.exception.PetasosException;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;

/**
 * Context provided to {@link PetasosMessageHandler} for message acknowledgement,
 * rejection, redelivery inspection, and topology tracking.
 */
public interface PetasosMessageContext {

    /**
     * Acknowledges that the message has been successfully processed.
     * Removes the message from the Petasos broker queue.
     *
     * @throws PetasosException if acknowledgement fails
     */
    void acknowledge() throws PetasosException;

    /**
     * Rejects the message and routes it to the Dead-Letter Queue (DLQ) or triggers
     * Artemis redelivery based on broker configuration.
     *
     * @throws PetasosException if rejection fails
     */
    default void reject() throws PetasosException {
        reject(false);
    }

    /**
     * Rejects the message.
     *
     * @param requeue true to request redelivery immediately, false to forward to DLQ / fail delivery
     * @throws PetasosException if rejection fails
     */
    void reject(boolean requeue) throws PetasosException;

    /**
     * Returns the delivery attempt count for this message.
     */
    int getRedeliveryCount();

    /**
     * Returns true if this message is a redelivery.
     */
    default boolean isRedelivered() {
        return getRedeliveryCount() > 1;
    }

    /**
     * Returns the ID or name of the broker node that delivered this message.
     */
    String getBrokerNodeId();

    /**
     * Returns the destination from which the message was consumed.
     */
    PetasosDestination getDestination();

    /**
     * Returns the received message envelope.
     */
    PetasosMessage getOriginalMessage();
}
