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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.model.core.information;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link InformationRepresentation}, {@link ContainerMetadata}, and {@link ContentMetadata}.
 * Verifies single governed representation semantics, content exclusion in Summary projection,
 * content reference preservation, and defensive immutability.
 */
class InformationRepresentationTest {

    @Test
    @DisplayName("ContainerMetadata: should encapsulate envelope attributes and enforce immutability")
    void testContainerMetadata() {
        ContainerMetadata container = ContainerMetadata.builder()
                .containerType("FHIR_BUNDLE")
                .source("pas-gw")
                .destination("mneme-inbound")
                .author("ClinicalAuthor")
                .recipient("CareTeam")
                .assemblyContext("AdmissionEvent")
                .interactionMechanism("REST")
                .envelopeStructure("Bundle/collection")
                .attribute("correlationTag", "TAG-123")
                .build();

        assertThat(container.containerType()).contains("FHIR_BUNDLE");
        assertThat(container.source()).contains("pas-gw");
        assertThat(container.destination()).contains("mneme-inbound");
        assertThat(container.author()).contains("ClinicalAuthor");
        assertThat(container.recipient()).contains("CareTeam");
        assertThat(container.assemblyContext()).contains("AdmissionEvent");
        assertThat(container.interactionMechanism()).contains("REST");
        assertThat(container.envelopeStructure()).contains("Bundle/collection");
        assertThat(container.getAttribute("correlationTag")).contains("TAG-123");

        // Immutability
        assertThatThrownBy(() -> container.attributes().put("k", "v"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("ContentMetadata: should distinguish Content Structure from Content Format and preserve contentReference")
    void testContentMetadata() {
        ContentMetadata contentMeta = ContentMetadata.builder()
                .contentStructureTopic("Health.FHIR.R5.Patient")
                .contentStructureVersion("R5")
                .contentFormat("application/fhir+json")
                .contentReference("s3://clinical-store/fhir/patient-001.json")
                .contentDigest("sha256-abcdef1234567890")
                .contentSize(2048L)
                .attribute("profile", "http://hl7.org/fhir/StructureDefinition/Patient")
                .build();

        assertThat(contentMeta.contentStructureTopic()).contains("Health.FHIR.R5.Patient");
        assertThat(contentMeta.contentStructureVersion()).contains("R5");
        assertThat(contentMeta.contentFormat()).contains("application/fhir+json");
        assertThat(contentMeta.contentReference()).contains("s3://clinical-store/fhir/patient-001.json");
        assertThat(contentMeta.hasContentReference()).isTrue();
        assertThat(contentMeta.contentDigest()).contains("sha256-abcdef1234567890");
        assertThat(contentMeta.contentSize()).contains(2048L);
        assertThat(contentMeta.getAttribute("profile")).contains("http://hl7.org/fhir/StructureDefinition/Patient");

        // With mutators
        ContentMetadata updated = contentMeta.withContentReference("s3://clinical-store/fhir/patient-002.json");
        assertThat(updated.contentReference()).contains("s3://clinical-store/fhir/patient-002.json");
        assertThat(updated.contentStructureTopic()).contains("Health.FHIR.R5.Patient");
    }

    @Test
    @DisplayName("InformationRepresentation: should handle hydrated representation with String content")
    void testHydratedStringRepresentation() {
        ContainerMetadata container = ContainerMetadata.builder().containerType("DOCUMENT").build();
        ContentMetadata contentMeta = ContentMetadata.builder()
                .contentStructureTopic("Health.FHIR.R5.Observation")
                .contentStructureVersion("R5")
                .contentFormat("application/json")
                .build();

        String jsonPayload = "{\"resourceType\":\"Observation\",\"id\":\"obs-1\"}";
        InformationRepresentation rep = InformationRepresentation.of(container, contentMeta, jsonPayload);

        assertThat(rep.isSummary()).isFalse();
        assertThat(rep.hasContent()).isTrue();
        assertThat(rep.content()).contains(jsonPayload);
        assertThat(rep.contentAsString()).contains(jsonPayload);
        assertThat(rep.contentAsBytes()).contains(jsonPayload.getBytes(StandardCharsets.UTF_8));
        assertThat(rep.contentAs(String.class)).contains(jsonPayload);
        assertThat(rep.contentAs(Integer.class)).isEmpty();
    }

    @Test
    @DisplayName("InformationRepresentation: should handle hydrated representation with byte array and guarantee defensive cloning")
    void testHydratedByteArrayRepresentation() {
        ContainerMetadata container = ContainerMetadata.builder().containerType("BINARY").build();
        ContentMetadata contentMeta = ContentMetadata.builder().contentFormat("application/octet-stream").build();

        byte[] original = new byte[]{1, 2, 3, 4, 5};
        InformationRepresentation rep = InformationRepresentation.of(container, contentMeta, original);

        // Mutating original array must not alter representation
        original[0] = 99;
        byte[] extracted = rep.contentAsBytes().orElseThrow();
        assertThat(extracted[0]).isEqualTo((byte) 1);

        // Mutating extracted array must not alter representation
        extracted[0] = 88;
        assertThat(rep.contentAsBytes().orElseThrow()[0]).isEqualTo((byte) 1);
    }

    @Test
    @DisplayName("InformationRepresentation: toSummary() must exclude content precisely while preserving all metadata")
    void testSummaryProjection() {
        ContainerMetadata container = ContainerMetadata.builder()
                .containerType("FHIR_BUNDLE")
                .source("pas-gw")
                .build();
        ContentMetadata contentMeta = ContentMetadata.builder()
                .contentStructureTopic("Health.FHIR.R5.Patient")
                .contentStructureVersion("R5")
                .contentReference("urn:uuid:opaque-ref-1234")
                .contentSize(4096L)
                .build();

        InformationRepresentation full = InformationRepresentation.of(container, contentMeta, "{\"patient\":\"content\"}");
        InformationRepresentation summary = full.toSummary();

        assertThat(summary.isSummary()).isTrue();
        assertThat(summary.hasContent()).isFalse();
        assertThat(summary.content()).isEmpty();
        assertThat(summary.contentAsString()).isEmpty();
        assertThat(summary.contentAsBytes()).isEmpty();

        // Metadata must be completely preserved
        assertThat(summary.containerMetadata()).isEqualTo(container);
        assertThat(summary.contentMetadata()).isEqualTo(contentMeta);
        assertThat(summary.contentMetadata().contentReference()).contains("urn:uuid:opaque-ref-1234");
        assertThat(summary.contentMetadata().contentStructureTopic()).contains("Health.FHIR.R5.Patient");

        // Hydrating from summary
        InformationRepresentation rehydrated = summary.withContent("{\"patient\":\"new-content\"}");
        assertThat(rehydrated.isSummary()).isFalse();
        assertThat(rehydrated.contentAsString()).contains("{\"patient\":\"new-content\"}");
    }
}
