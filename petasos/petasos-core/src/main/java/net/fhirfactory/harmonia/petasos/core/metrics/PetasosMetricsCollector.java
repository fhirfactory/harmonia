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

package net.fhirfactory.harmonia.petasos.core.metrics;

import net.fhirfactory.harmonia.petasos.api.metrics.PetasosMetrics;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * High-performance thread-safe metrics collector implementing {@link PetasosMetrics}.
 */
public final class PetasosMetricsCollector implements PetasosMetrics {

    private final LongAdder messagesSent = new LongAdder();
    private final LongAdder messagesReceived = new LongAdder();
    private final LongAdder processingFailures = new LongAdder();
    private final LongAdder reconnectCount = new LongAdder();
    private final LongAdder redeliveries = new LongAdder();
    private final LongAdder deadLetterCount = new LongAdder();
    private final AtomicInteger activeConsumers = new AtomicInteger(0);
    private final AtomicInteger activeProducers = new AtomicInteger(0);

    public void recordMessageSent() {
        messagesSent.increment();
    }

    public void recordMessageReceived() {
        messagesReceived.increment();
    }

    public void recordProcessingFailure() {
        processingFailures.increment();
    }

    public void recordReconnect() {
        reconnectCount.increment();
    }

    public void recordRedelivery() {
        redeliveries.increment();
    }

    public void recordDeadLetter() {
        deadLetterCount.increment();
    }

    public void incrementConsumers() {
        activeConsumers.incrementAndGet();
    }

    public void decrementConsumers() {
        activeConsumers.updateAndGet(c -> Math.max(0, c - 1));
    }

    public void incrementProducers() {
        activeProducers.incrementAndGet();
    }

    public void decrementProducers() {
        activeProducers.updateAndGet(p -> Math.max(0, p - 1));
    }

    @Override
    public long getMessagesSent() {
        return messagesSent.sum();
    }

    @Override
    public long getMessagesReceived() {
        return messagesReceived.sum();
    }

    @Override
    public long getProcessingFailures() {
        return processingFailures.sum();
    }

    @Override
    public long getReconnectCount() {
        return reconnectCount.sum();
    }

    @Override
    public long getRedeliveries() {
        return redeliveries.sum();
    }

    @Override
    public long getDeadLetterCount() {
        return deadLetterCount.sum();
    }

    @Override
    public int getActiveConsumers() {
        return activeConsumers.get();
    }

    @Override
    public int getActiveProducers() {
        return activeProducers.get();
    }

    public void reset() {
        messagesSent.reset();
        messagesReceived.reset();
        processingFailures.reset();
        reconnectCount.reset();
        redeliveries.reset();
        deadLetterCount.reset();
        activeConsumers.set(0);
        activeProducers.set(0);
    }

    @Override
    public String toString() {
        return "PetasosMetrics{" +
                "sent=" + getMessagesSent() +
                ", received=" + getMessagesReceived() +
                ", failures=" + getProcessingFailures() +
                ", reconnects=" + getReconnectCount() +
                ", redeliveries=" + getRedeliveries() +
                ", dlq=" + getDeadLetterCount() +
                ", activeConsumers=" + getActiveConsumers() +
                ", activeProducers=" + getActiveProducers() +
                '}';
    }
}
