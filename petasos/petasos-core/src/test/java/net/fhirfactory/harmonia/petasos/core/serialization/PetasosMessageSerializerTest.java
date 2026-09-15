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

package net.fhirfactory.harmonia.petasos.core.serialization;

import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class PetasosMessageSerializerTest {

    @Test
    void testSerializationRoundtrip() {
        PetasosMessage original = PetasosMessage.builder()
                .messageId("msg-abc-123")
                .correlationId("corr-777")
                .causationId("cause-666")
                .messageType("DiagnosticReportDispatch")
                .source("pylai-mllp-in")
                .destination(PetasosDestination.queue("task.event.queue"))
                .contentType("application/json")
                .schema("http://hl7.org/fhir/StructureDefinition/DiagnosticReport", "5.0.0")
                .payload("{\"status\":\"final\",\"code\":\"11502-2\"}")
                .header("tenant", "hospital-alpha")
                .durable(true)
                .priority(7)
                .ttl(Duration.ofMinutes(5))
                .duplicateDetectionId("dedup-999")
                .build();

        String json = PetasosMessageSerializer.serializeToString(original);
        assertThat(json).isNotBlank();
        assertThat(json).contains("msg-abc-123");
        assertThat(json).contains("DiagnosticReportDispatch");

        PetasosMessage deserialized = PetasosMessageSerializer.deserialize(json);
        assertThat(deserialized.getMessageId()).isEqualTo(original.getMessageId());
        assertThat(deserialized.getCorrelationId()).isEqualTo(original.getCorrelationId());
        assertThat(deserialized.getCausationId()).isEqualTo(original.getCausationId());
        assertThat(deserialized.getMessageType()).isEqualTo(original.getMessageType());
        assertThat(deserialized.getSource()).isEqualTo(original.getSource());
        assertThat(deserialized.getDestination()).isEqualTo(original.getDestination());
        assertThat(deserialized.getContentType()).isEqualTo(original.getContentType());
        assertThat(deserialized.getSchemaIdentifier()).isEqualTo(original.getSchemaIdentifier());
        assertThat(deserialized.getSchemaVersion()).isEqualTo(original.getSchemaVersion());
        assertThat(deserialized.getPayloadAsString()).isEqualTo(original.getPayloadAsString());
        assertThat(deserialized.getMetadata()).containsEntry("tenant", "hospital-alpha");
        assertThat(deserialized.isDurable()).isEqualTo(original.isDurable());
        assertThat(deserialized.getPriority()).isEqualTo(original.getPriority());
        assertThat(deserialized.getDuplicateDetectionId()).isEqualTo(original.getDuplicateDetectionId());
    }

    @Test
    void testBytesSerializationRoundtrip() {
        PetasosMessage original = PetasosMessage.of("Simple byte test payload");
        byte[] bytes = PetasosMessageSerializer.serializeToBytes(original);
        PetasosMessage recovered = PetasosMessageSerializer.deserialize(bytes);

        assertThat(recovered.getMessageId()).isEqualTo(original.getMessageId());
        assertThat(recovered.getPayloadAsString()).isEqualTo("Simple byte test payload");
    }
}
