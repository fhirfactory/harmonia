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

package net.fhirfactory.harmonia.mllpgateway.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OutboundMllpModelsTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testOutboundMllpRequestSerialization() throws Exception {
        Topic topic = Topic.forEgress("ADT", "A01", "harmonia", "mllp-out", "HIS_NORTH");
        OutboundMllpRequest request = new OutboundMllpRequest("MSG-1001", "HIS_NORTH", "MSH|^~\\&|...", topic);
        request.setTargetQueue("petasos.queue.mllp.outbound.his_north");
        request.setFacility("HOSP_N");
        request.setTaskId("TASK-999");

        String json = objectMapper.writeValueAsString(request);
        OutboundMllpRequest deserialized = objectMapper.readValue(json, OutboundMllpRequest.class);

        assertThat(deserialized.getRequestId()).isEqualTo(request.getRequestId());
        assertThat(deserialized.getMessageControlId()).isEqualTo("MSG-1001");
        assertThat(deserialized.getDestinationId()).isEqualTo("HIS_NORTH");
        assertThat(deserialized.getRawMessage()).isEqualTo("MSH|^~\\&|...");
        assertThat(deserialized.getTargetQueue()).isEqualTo("petasos.queue.mllp.outbound.his_north");
        assertThat(deserialized.getFacility()).isEqualTo("HOSP_N");
        assertThat(deserialized.getTaskId()).isEqualTo("TASK-999");
        assertThat(deserialized.getTopic().getDestination()).isEqualTo("HIS_NORTH");
    }

    @Test
    void testOutboundMllpResponseSuccessFactory() throws Exception {
        OutboundMllpResponse response = OutboundMllpResponse.success("REQ-1", "MSG-1001", "HIS_NORTH", "AA", "MSH|...MSA|AA|MSG-1001", 45L);

        assertThat(response.isSuccessful()).isTrue();
        assertThat(response.getAckCode()).isEqualTo("AA");
        assertThat(response.getMessageControlId()).isEqualTo("MSG-1001");
        assertThat(response.getDestinationId()).isEqualTo("HIS_NORTH");
        assertThat(response.getDurationMs()).isEqualTo(45L);
        assertThat(response.getAcknowledgedAt()).isNotNull();

        String json = objectMapper.writeValueAsString(response);
        OutboundMllpResponse deserialized = objectMapper.readValue(json, OutboundMllpResponse.class);
        assertThat(deserialized.getMessageControlId()).isEqualTo("MSG-1001");
        assertThat(deserialized.getAckCode()).isEqualTo("AA");
        assertThat(deserialized.isSuccessful()).isTrue();
    }

    @Test
    void testOutboundMllpResponseNackAndFailure() {
        OutboundMllpResponse nack = OutboundMllpResponse.nack("MSG-1002", "AE", "Invalid Patient ID", "MSH|...MSA|AE|MSG-1002|Invalid Patient ID", 60L);
        assertThat(nack.isSuccessful()).isFalse();
        assertThat(nack.getAckCode()).isEqualTo("AE");
        assertThat(nack.getErrorMessage()).isEqualTo("Invalid Patient ID");

        OutboundMllpResponse failure = OutboundMllpResponse.failure("REQ-2", "MSG-1003", "LIS", "Connection refused: localhost:2576", 5000L);
        assertThat(failure.isSuccessful()).isFalse();
        assertThat(failure.getErrorMessage()).contains("Connection refused");
    }
}
