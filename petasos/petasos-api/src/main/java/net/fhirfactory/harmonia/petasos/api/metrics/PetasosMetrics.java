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

package net.fhirfactory.harmonia.petasos.api.metrics;

/**
 * Exposes Petasos operational metrics for Harmonia platform observability.
 */
public interface PetasosMetrics {

    /**
     * Total number of messages successfully dispatched to Artemis brokers.
     */
    long getMessagesSent();

    /**
     * Total number of messages received and processed by Petasos consumers.
     */
    long getMessagesReceived();

    /**
     * Total count of message processing or transmission failures.
     */
    long getProcessingFailures();

    /**
     * Total count of broker failovers or reconnection events.
     */
    long getReconnectCount();

    /**
     * Total count of message redeliveries observed.
     */
    long getRedeliveries();

    /**
     * Count of messages routed to the Dead Letter Queue (DLQ).
     */
    long getDeadLetterCount();

    /**
     * Number of currently active consumers.
     */
    int getActiveConsumers();

    /**
     * Number of currently active producers.
     */
    int getActiveProducers();
}
