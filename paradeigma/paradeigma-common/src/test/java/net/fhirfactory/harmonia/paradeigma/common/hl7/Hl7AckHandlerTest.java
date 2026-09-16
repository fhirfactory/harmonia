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

package net.fhirfactory.harmonia.paradeigma.common.hl7;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Hl7AckHandlerTest {

    private static final String INCOMING_MSG = "MSH|^~\\&|PAS_SIM|FACILITY|HARMONIA|HIE|20260915120000||ADT^A01|MSG-CTRL-999|P|2.4\r"
            + "PID|1||PAT-101^^^MRN||Smith^John\r";

    @Test
    @DisplayName("Generate and parse Accept (AA) ACK")
    void testGenerateAcceptAck() {
        String ack = Hl7AckHandler.generateAcceptAck(INCOMING_MSG);
        assertThat(ack).contains("MSA|AA|MSG-CTRL-999|");

        AckResult result = Hl7AckHandler.parseAck(ack);
        assertThat(result.isAccept()).isTrue();
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getCorrelatedMessageId()).isEqualTo("MSG-CTRL-999");
        assertThat(result.matchesControlId("MSG-CTRL-999")).isTrue();
    }

    @Test
    @DisplayName("Generate and parse Error (AE) ACK")
    void testGenerateErrorAck() {
        String ack = Hl7AckHandler.generateErrorAck(INCOMING_MSG, "Patient PID segment missing");
        assertThat(ack).contains("MSA|AE|MSG-CTRL-999|Patient PID segment missing");

        AckResult result = Hl7AckHandler.parseAck(ack);
        assertThat(result.isError()).isTrue();
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getCorrelatedMessageId()).isEqualTo("MSG-CTRL-999");
        assertThat(result.getTextMessage()).isEqualTo("Patient PID segment missing");
    }

    @Test
    @DisplayName("Generate and parse Reject (AR) ACK")
    void testGenerateRejectAck() {
        String ack = Hl7AckHandler.generateRejectAck(INCOMING_MSG, "Unsupported trigger event");
        assertThat(ack).contains("MSA|AR|MSG-CTRL-999|Unsupported trigger event");

        AckResult result = Hl7AckHandler.parseAck(ack);
        assertThat(result.isReject()).isTrue();
        assertThat(result.isSuccess()).isFalse();
    }
}
