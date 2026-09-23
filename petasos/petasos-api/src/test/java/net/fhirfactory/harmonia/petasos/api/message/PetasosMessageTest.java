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

package net.fhirfactory.harmonia.petasos.api.message;

import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PetasosMessageTest {

    @Test
    void testMessageEnvelopeBuilderAndImmutability() {
        PetasosDestination destination = PetasosDestination.queue("clinical.events.inbound");
        Instant now = Instant.now();

        PetasosMessage message = PetasosMessage.builder()
                .messageId("msg-12345")
                .correlationId("corr-999")
                .causationId("cause-888")
                .messageType("EncounterNotification")
                .source("pylai-mllp-gateway")
                .destination(destination)
                .timestamp(now)
                .contentType("application/fhir+json")
                .schema("http://hl7.org/fhir/StructureDefinition/Encounter", "5.0.0")
                .payload("{\"resourceType\":\"Encounter\",\"id\":\"enc-1\"}")
                .header("X-Facility-ID", "FAC-001")
                .header("X-Patient-MRN", "MRN-555")
                .durable(true)
                .priority(5)
                .ttl(Duration.ofMinutes(10))
                .duplicateDetectionId("dedup-12345")
                .build();

        assertThat(message.getMessageId()).isEqualTo("msg-12345");
        assertThat(message.getCorrelationId()).isEqualTo("corr-999");
        assertThat(message.getCausationId()).isEqualTo("cause-888");
        assertThat(message.getMessageType()).isEqualTo("EncounterNotification");
        assertThat(message.getSource()).isEqualTo("pylai-mllp-gateway");
        assertThat(message.getDestination()).isEqualTo(destination);
        assertThat(message.getTimestamp()).isEqualTo(now);
        assertThat(message.getContentType()).isEqualTo("application/fhir+json");
        assertThat(message.getSchemaIdentifier()).isEqualTo("http://hl7.org/fhir/StructureDefinition/Encounter");
        assertThat(message.getSchemaVersion()).isEqualTo("5.0.0");
        assertThat(message.getPayloadAsString()).isEqualTo("{\"resourceType\":\"Encounter\",\"id\":\"enc-1\"}");
        assertThat(message.getMetadata()).containsEntry("X-Facility-ID", "FAC-001")
                .containsEntry("X-Patient-MRN", "MRN-555");
        assertThat(message.isDurable()).isTrue();
        assertThat(message.getPriority()).isEqualTo(5);
        assertThat(message.getExpiration()).isNotNull();
        assertThat(message.getDuplicateDetectionId()).isEqualTo("dedup-12345");

        // Verify toString does NOT print payload
        assertThat(message.toString()).doesNotContain("resourceType");
        assertThat(message.toString()).contains("payloadSizeBytes=");

        // Verify payload array defensive copying
        byte[] payloadBytes = message.getPayload();
        payloadBytes[0] = 'X';
        assertThat(message.getPayloadAsString()).isEqualTo("{\"resourceType\":\"Encounter\",\"id\":\"enc-1\"}");
    }

    @Test
    void testToBuilderPreservesValues() {
        PetasosMessage original = PetasosMessage.builder()
                .messageId("orig-1")
                .payload("test")
                .header("key1", "val1")
                .build();

        PetasosMessage copy = original.toBuilder()
                .messageId("new-id")
                .header("key2", "val2")
                .build();

        assertThat(copy.getMessageId()).isEqualTo("new-id");
        assertThat(copy.getPayloadAsString()).isEqualTo("test");
        assertThat(copy.getMetadata()).containsEntry("key1", "val1").containsEntry("key2", "val2");
        assertThat(original.getMessageId()).isEqualTo("orig-1");
        assertThat(original.getMetadata()).doesNotContainKey("key2");
    }

    @Test
    void testSecurityContextPropagationOnMessage() {
        ThemisPrincipal human = ThemisPrincipal.of("dr.mark", PrincipalType.HUMAN, "CLINICAL");
        ThemisSecurityContext secCtx = ThemisSecurityContext.builder()
                .originatingPrincipal(human)
                .securityDomain("CLINICAL")
                .addAuthority("clinical.create")
                .correlationId("corr-msg-1")
                .build();

        PetasosMessage message = PetasosMessage.builder()
                .messageId("msg-sec-1")
                .correlationId("corr-msg-1")
                .securityContext(secCtx)
                .payload("{\"resourceType\":\"Patient\"}")
                .build();

        assertThat(message.getSecurityContext()).isNotNull();
        assertThat(message.getSecurityContext().originatingPrincipal()).isEqualTo(human);
        assertThat(message.getOriginatingPrincipal()).isEqualTo(human);
        assertThat(message.getSecurityContext().authorities()).extracting(ThemisAuthority::authorityCode).contains("clinical.create");

        // Verify toBuilder retains security context
        PetasosMessage cloned = message.toBuilder().causationId("parent-msg-1").build();
        assertThat(cloned.getSecurityContext()).isNotNull();
        assertThat(cloned.getOriginatingPrincipal()).isEqualTo(human);
        assertThat(cloned.getCausationId()).isEqualTo("parent-msg-1");
    }
}
