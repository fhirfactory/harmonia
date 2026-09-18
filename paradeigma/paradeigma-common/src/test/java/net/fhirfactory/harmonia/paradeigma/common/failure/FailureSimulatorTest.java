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

package net.fhirfactory.harmonia.paradeigma.common.failure;

import net.fhirfactory.harmonia.paradeigma.common.hl7.AckResult;
import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7AckHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FailureSimulatorTest {

    private static final String MSG = "MSH|^~\\&|PAS|FAC|HARMONIA|HIE|20260915120000||ADT^A01|MSG-FAIL-001|P|2.4\rPID|1||PAT-101^^^MRN||Smith^John\r";

    @Test
    @DisplayName("Disabled fault injection returns clean AA ACKs")
    void testDisabledSimulation() {
        FaultInjectionConfig cfg = new FaultInjectionConfig();
        cfg.setEnabled(false);
        FailureSimulator sim = new FailureSimulator(cfg);

        assertThat(sim.shouldDropConnection()).isFalse();
        assertThat(sim.shouldDropAck()).isFalse();
        assertThat(sim.shouldInjectErrorAck()).isFalse();

        String ack = sim.evaluateAck(MSG);
        assertThat(ack).isNotNull();
        AckResult res = Hl7AckHandler.parseAck(ack);
        assertThat(res.isAccept()).isTrue();
    }

    @Test
    @DisplayName("Application Error fault returns AE ACK")
    void testInjectErrorAck() {
        FaultInjectionConfig cfg = new FaultInjectionConfig();
        cfg.setEnabled(true);
        cfg.setApplicationErrorProbability(1.0); // 100% error
        FailureSimulator sim = new FailureSimulator(cfg);

        String ack = sim.evaluateAck(MSG);
        assertThat(ack).isNotNull();
        AckResult res = Hl7AckHandler.parseAck(ack);
        assertThat(res.isError()).isTrue();
        assertThat(res.getAckCode()).isEqualTo("AE");
    }

    @Test
    @DisplayName("No ACK fault drops response")
    void testDropAck() {
        FaultInjectionConfig cfg = new FaultInjectionConfig();
        cfg.setEnabled(true);
        cfg.setNoAckProbability(1.0); // 100% no-ack
        FailureSimulator sim = new FailureSimulator(cfg);

        String ack = sim.evaluateAck(MSG);
        assertThat(ack).isNull();
    }

    @Test
    @DisplayName("Corrupt payload fault alters HL7 delimiters")
    void testCorruptPayload() {
        FaultInjectionConfig cfg = new FaultInjectionConfig();
        cfg.setEnabled(true);
        cfg.setMalformedSegmentProbability(1.0);
        FailureSimulator sim = new FailureSimulator(cfg);

        String corrupted = sim.corruptMessage(MSG);
        assertThat(corrupted).contains("PID_CORRUPTED|INVALID|");
    }
}
