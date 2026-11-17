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

package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationUnitValueSets;

import net.fhirfactory.harmonia.model.core.naming.factory.TopicFactory;

import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.information.entity.factory.ContainerMetadataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link InformationRepresentation}, {@link ContainerMetadata}, and {@link ContentMetadata}.
 * Verifies single governed representation semantics, content exclusion in Summary projection,
 * content reference preservation, and defensive immutability with strongly typed Topics.
 */
class InformationRepresentationTest {

    @Test
    @DisplayName("ContainerMetadata: should encapsulate envelope attributes and enforce immutability")
    void testContainerMetadata() {
        Topic containerType = InformationUnitValueSets.TYPE_FHIR_BUNDLE;
        Topic source = TopicFactory.fromName("Harmonia.Gateway.PAS");
        Topic destination = TopicFactory.fromName("Harmonia.Service.Mneme.Inbound");
        Topic author = TopicFactory.fromName("Harmonia.Author.Clinical");
        Topic recipient = TopicFactory.fromName("Harmonia.Recipient.CareTeam");
        ContainerMetadata container = new ContainerMetadataBuilder()
                .type(containerType)
                .sourceTopic(source)
                .destinationTopic(destination)
                .authorTopic(author)
                .recipientTopic(recipient)
                .assemblyContext("AdmissionEvent")
                .interactionMechanism("REST")
                .envelopeStructure("Bundle/collection")
                .putAttribute("correlationTag", "TAG-123")
                .build();

        assertThat(container.type()).isEqualTo(containerType);
        assertThat(container.sourceTopic()).contains(source);
        assertThat(container.destinationTopic()).contains(destination);
        assertThat(container.authorTopic()).contains(author);
        assertThat(container.recipientTopic()).contains(recipient);
        assertThat(container.assemblyContext()).contains("AdmissionEvent");
        assertThat(container.interactionMechanism()).contains("REST");
        assertThat(container.envelopeStructure()).contains("Bundle/collection");
        assertThat(container.getAttribute("correlationTag")).contains("TAG-123");

        // Immutability
        assertThatThrownBy(() -> container.attributes().put("k", "v"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("ContentMetadata: should distinguish Content Structure Topic from Content Format and preserve contentReference")
    void testContentMetadata() {
        Topic patientTopic = TopicFactory.fromName("Health.FHIR.R5.Patient");

        ContentMetadata contentMeta = ContentMetadata.builder()
                .contentStructureTopic(patientTopic)
                .contentStructureVersion("R5")
                .contentFormat("application/fhir+json")
                .contentReference("s3://clinical-store/fhir/patient-001.json")
                .contentDigest("sha256-abcdef1234567890")
                .contentSize(2048L)
                .putAttribute("profile", "http://hl7.org/fhir/StructureDefinition/Patient")
                .build();

        assertThat(contentMeta.contentStructureTopic()).contains(patientTopic);
        assertThat(contentMeta.contentStructureTopicName()).contains("Health.FHIR.R5.Patient");
        assertThat(contentMeta.contentStructureVersion()).contains("R5");
        assertThat(contentMeta.contentFormat()).contains("application/fhir+json");
        assertThat(contentMeta.contentReference()).contains("s3://clinical-store/fhir/patient-001.json");
        assertThat(contentMeta.hasContentReference()).isTrue();
        assertThat(contentMeta.contentDigest()).contains("sha256-abcdef1234567890");
        assertThat(contentMeta.contentSize()).contains(2048L);
        assertThat(contentMeta.getAttribute("profile")).contains("http://hl7.org/fhir/StructureDefinition/Patient");

        // Negative size rejection
        assertThatThrownBy(() -> ContentMetadata.builder().contentSize(-10L).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("contentSize must not be negative");

        // With mutators
        ContentMetadata updated = contentMeta.withContentReference("s3://clinical-store/fhir/patient-002.json");
        assertThat(updated.contentReference()).contains("s3://clinical-store/fhir/patient-002.json");
        assertThat(updated.contentStructureTopic()).contains(patientTopic);
    }

    @Test
    @DisplayName("InformationRepresentation: should handle hydrated representation with String content")
    void testHydratedStringRepresentation() {
        ContainerMetadata container = new ContainerMetadataBuilder().type(InformationUnitValueSets.TYPE_DOCUMENT).build();
        Topic obsTopic = TopicFactory.fromName("Health.FHIR.R5.Observation");
        ContentMetadata contentMeta = ContentMetadata.builder()
                .contentStructureTopic(obsTopic)
                .contentStructureVersion("R5")
                .contentFormat("application/json")
                .build();

        String jsonPayload = "{\"resourceType\":\"Observation\",\"id\":\"obs-1\"}";
        InformationRepresentation rep = new InformationRepresentation(container, contentMeta, Content.text(jsonPayload));

        assertThat(rep.isSummary()).isFalse();
        assertThat(rep.hasContent()).isTrue();
        assertThat(rep.content().textContent().orElseThrow()).isEqualTo(jsonPayload);
        assertThat(rep.content().textContent()).contains(jsonPayload);
        assertThat(rep.content().binaryContent()).contains(jsonPayload.getBytes(StandardCharsets.UTF_8));
        assertThat(rep.content().as(String.class)).contains(jsonPayload);
        assertThat(rep.content().as(Integer.class)).isEmpty();
    }

    @Test
    @DisplayName("InformationRepresentation: should handle hydrated representation with byte array and guarantee defensive cloning")
    void testHydratedByteArrayRepresentation() {
        ContainerMetadata container = new ContainerMetadataBuilder()
                .type(TopicFactory.fromName("Harmonia.Container.Binary"))
                .build();
        ContentMetadata contentMeta = ContentMetadata.builder().contentFormat("application/octet-stream").build();

        byte[] original = new byte[]{1, 2, 3, 4, 5};
        InformationRepresentation rep = new InformationRepresentation(container, contentMeta, Content.binary(original));

        // Mutating original array must not alter representation
        original[0] = 99;
        byte[] extracted = rep.content().binaryContent().orElseThrow();
        assertThat(extracted[0]).isEqualTo((byte) 1);

        // Mutating extracted array must not alter representation
        extracted[0] = 88;
        assertThat(rep.content().binaryContent().orElseThrow()[0]).isEqualTo((byte) 1);
    }

    @Test
    @DisplayName("InformationRepresentation: toSummary() must exclude content precisely while preserving all metadata")
    void testSummaryProjection() {
        ContainerMetadata container = new ContainerMetadataBuilder()
                .type(InformationUnitValueSets.TYPE_FHIR_BUNDLE)
                .sourceTopic(TopicFactory.fromName("Harmonia.Gateway.PAS"))
                .build();
        Topic patientTopic = TopicFactory.fromName("Health.FHIR.R5.Patient");
        ContentMetadata contentMeta = ContentMetadata.builder()
                .contentStructureTopic(patientTopic)
                .contentStructureVersion("R5")
                .contentReference("urn:uuid:opaque-ref-1234")
                .contentSize(4096L)
                .build();

        InformationRepresentation full = new InformationRepresentation(
                container, contentMeta, Content.text("{\"patient\":\"content\"}"));
        InformationRepresentation summary = full.toSummary();

        assertThat(summary.isSummary()).isTrue();
        assertThat(summary.hasContent()).isFalse();
        assertThat(summary.content().isEmpty()).isTrue();
        assertThat(summary.content().textContent()).isEmpty();
        assertThat(summary.content().binaryContent()).isEmpty();

        // Metadata must be completely preserved
        assertThat(summary.containerMetadata()).isEqualTo(container);
        assertThat(summary.contentMetadata()).isEqualTo(contentMeta);
        assertThat(summary.contentMetadata().contentReference()).contains("urn:uuid:opaque-ref-1234");
        assertThat(summary.contentMetadata().contentStructureTopic()).contains(patientTopic);

        // Hydrating from summary
        InformationRepresentation rehydrated = summary.withContent(Content.text("{\"patient\":\"new-content\"}"));
        assertThat(rehydrated.isSummary()).isFalse();
        assertThat(rehydrated.content().textContent()).contains("{\"patient\":\"new-content\"}");
    }
}
