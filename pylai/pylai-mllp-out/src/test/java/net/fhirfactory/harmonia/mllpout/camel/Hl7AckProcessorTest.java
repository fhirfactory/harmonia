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

package net.fhirfactory.harmonia.mllpout.camel;

import net.fhirfactory.harmonia.mllpgateway.model.OutboundMllpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Hl7AckProcessorTest {

    private Hl7AckProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new Hl7AckProcessor();
    }

    @Test
    void testParseApplicationAcceptAck() {
        String rawAck = "MSH|^~\\&|HIS_SYS|HOSP_N|HARMONIA|HIE|20260915083000||ACK^A01|ACK001|P|2.4\r" +
                "MSA|AA|MSG-1001|Message accepted successfully\r";

        OutboundMllpResponse response = processor.parseAndValidateAck(rawAck, "MSG-1001", "HIS_NORTH", 35L);

        assertThat(response.isSuccessful()).isTrue();
        assertThat(response.getAckCode()).isEqualTo("AA");
        assertThat(response.getMessageControlId()).isEqualTo("MSG-1001");
        assertThat(response.getDestinationId()).isEqualTo("HIS_NORTH");
        assertThat(response.getAckText()).isEqualTo("Message accepted successfully");
        assertThat(response.getDurationMs()).isEqualTo(35L);
    }

    @Test
    void testParseCommitAcceptAck() {
        String rawAck = "MSH|^~\\&|LIS_SYS|LAB_MAIN|HARMONIA|HIE|20260915083000||ACK^R01|ACK002|P|2.4\r" +
                "MSA|CA|MSG-1002\r";

        OutboundMllpResponse response = processor.parseAndValidateAck(rawAck, "MSG-1002", "LIS_MAIN", 20L);

        assertThat(response.isSuccessful()).isTrue();
        assertThat(response.getAckCode()).isEqualTo("CA");
        assertThat(response.getMessageControlId()).isEqualTo("MSG-1002");
    }

    @Test
    void testParseApplicationErrorNack() {
        String rawAck = "MSH|^~\\&|HIS_SYS|HOSP_N|HARMONIA|HIE|20260915083000||ACK^A01|ACK003|P|2.4\r" +
                "MSA|AE|MSG-1003|Patient MRN not found in registry\r";

        OutboundMllpResponse response = processor.parseAndValidateAck(rawAck, "MSG-1003", "HIS_NORTH", 50L);

        assertThat(response.isSuccessful()).isFalse();
        assertThat(response.getAckCode()).isEqualTo("AE");
        assertThat(response.getErrorMessage()).contains("Patient MRN not found");
    }

    @Test
    void testParseApplicationRejectNack() {
        String rawAck = "MSH|^~\\&|HIS_SYS|HOSP_N|HARMONIA|HIE|20260915083000||ACK^A01|ACK004|P|2.4\r" +
                "MSA|AR|MSG-1004|Invalid segment order\r";

        OutboundMllpResponse response = processor.parseAndValidateAck(rawAck, "MSG-1004", "HIS_NORTH", 50L);

        assertThat(response.isSuccessful()).isFalse();
        assertThat(response.getAckCode()).isEqualTo("AR");
        assertThat(response.getErrorMessage()).contains("Invalid segment order");
    }

    @Test
    void testControlIdMismatch() {
        String rawAck = "MSH|^~\\&|HIS_SYS|HOSP_N|HARMONIA|HIE|20260915083000||ACK^A01|ACK005|P|2.4\r" +
                "MSA|AA|WRONG_CONTROL_ID\r";

        OutboundMllpResponse response = processor.parseAndValidateAck(rawAck, "MSG-EXPECTED", "HIS_NORTH", 30L);

        assertThat(response.isSuccessful()).isFalse();
        assertThat(response.getErrorMessage()).contains("Control ID mismatch");
    }

    @Test
    void testMalformedOrEmptyAck() {
        OutboundMllpResponse empty = processor.parseAndValidateAck("", "MSG-1", "HIS", 10L);
        assertThat(empty.isSuccessful()).isFalse();

        OutboundMllpResponse nonHl7 = processor.parseAndValidateAck("HTTP 500 Internal Server Error", "MSG-1", "HIS", 10L);
        assertThat(nonHl7.isSuccessful()).isFalse();
        assertThat(nonHl7.getErrorMessage()).contains("missing MSA segment");
    }
}
