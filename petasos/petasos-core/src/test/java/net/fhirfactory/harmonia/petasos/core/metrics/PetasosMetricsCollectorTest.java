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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PetasosMetricsCollectorTest {

    @Test
    void testMetricsRecording() {
        PetasosMetricsCollector metrics = new PetasosMetricsCollector();

        metrics.recordMessageSent();
        metrics.recordMessageSent();
        metrics.recordMessageReceived();
        metrics.recordProcessingFailure();
        metrics.recordReconnect();
        metrics.recordRedelivery();
        metrics.recordDeadLetter();
        metrics.incrementConsumers();
        metrics.incrementProducers();

        assertThat(metrics.getMessagesSent()).isEqualTo(2);
        assertThat(metrics.getMessagesReceived()).isEqualTo(1);
        assertThat(metrics.getProcessingFailures()).isEqualTo(1);
        assertThat(metrics.getReconnectCount()).isEqualTo(1);
        assertThat(metrics.getRedeliveries()).isEqualTo(1);
        assertThat(metrics.getDeadLetterCount()).isEqualTo(1);
        assertThat(metrics.getActiveConsumers()).isEqualTo(1);
        assertThat(metrics.getActiveProducers()).isEqualTo(1);

        metrics.decrementConsumers();
        assertThat(metrics.getActiveConsumers()).isEqualTo(0);
    }
}
