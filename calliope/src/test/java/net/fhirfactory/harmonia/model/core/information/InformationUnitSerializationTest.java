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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeCommitOutcome;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.security.FhirConfidentialityEnum;
import net.fhirfactory.harmonia.model.security.HarmoniaSecurityLabelEnum;
import net.fhirfactory.harmonia.model.topic.TopicMask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests verifying Jackson JSON and Java Standard Serialization round-trips for all Information Unit foundation classes.
 */
class InformationUnitSerializationTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @SuppressWarnings("unchecked")
    private <T extends Serializable> T javaSerializeRoundtrip(T object) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(object);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            return (T) ois.readObject();
        }
    }

    @Test
    @DisplayName("InformationUnitIdentifier: should round-trip through JSON and Java Serialization")
    void testIdentifierSerialization() throws Exception {
        InformationUnitIdentifier id = InformationUnitIdentifier.random();

        // JSON
        String json = objectMapper.writeValueAsString(id);
        InformationUnitIdentifier fromJson = objectMapper.readValue(json, InformationUnitIdentifier.class);
        assertThat(fromJson).isEqualTo(id);
        assertThat(fromJson.topicName()).isEqualTo(id.topicName());

        // Java Serialization
        InformationUnitIdentifier fromJava = javaSerializeRoundtrip(id);
        assertThat(fromJava).isEqualTo(id);
    }

    @Test
    @DisplayName("ManagementContext: should round-trip through JSON and Java Serialization")
    void testManagementContextSerialization() throws Exception {
        ManagementContext context = ManagementContext.builder()
                .operationalVersion("15")
                .activeGeneration("gen-001")
                .cachedAt(Instant.ofEpochMilli(1700000000000L))
                .transientProperty("cluster", "prod-east")
                .build();

        // JSON
        String json = objectMapper.writeValueAsString(context);
        ManagementContext fromJson = objectMapper.readValue(json, ManagementContext.class);
        assertThat(fromJson).isEqualTo(context);

        // Java Serialization
        ManagementContext fromJava = javaSerializeRoundtrip(context);
        assertThat(fromJava).isEqualTo(context);
    }

    @Test
    @DisplayName("InformationCoordinates: should round-trip through JSON and Java Serialization")
    void testInformationCoordinatesSerialization() throws Exception {
        InformationCoordinates coords = InformationCoordinates.of(
                "Health.Gateway.A", "Health.Endpoint.B", "Health.Location.C", "Health.Owner.D");

        // JSON
        String json = objectMapper.writeValueAsString(coords);
        InformationCoordinates fromJson = objectMapper.readValue(json, InformationCoordinates.class);
        assertThat(fromJson).isEqualTo(coords);

        // Java Serialization
        InformationCoordinates fromJava = javaSerializeRoundtrip(coords);
        assertThat(fromJava).isEqualTo(coords);
    }

    @Test
    @DisplayName("InformationSecurityContext: should round-trip through JSON and Java Serialization")
    void testSecurityContextSerialization() throws Exception {
        InformationSecurityContext security = InformationSecurityContext.builder()
                .addUnitCriterion("Health.Security.Criterion.A")
                .addActionMask("READ")
                .addCompartmentMask("*.*.InformationCompartment.PersonalInformation*")
                .addSecurityLabel(HarmoniaSecurityLabelEnum.RESTRICTED)
                .confidentiality(FhirConfidentialityEnum.R)
                .attribute("attrKey", "attrVal")
                .build();

        // JSON
        String json = objectMapper.writeValueAsString(security);
        InformationSecurityContext fromJson = objectMapper.readValue(json, InformationSecurityContext.class);
        assertThat(fromJson).isEqualTo(security);

        // Java Serialization
        InformationSecurityContext fromJava = javaSerializeRoundtrip(security);
        assertThat(fromJava).isEqualTo(security);
    }

    @Test
    @DisplayName("LifecycleContext: should round-trip through JSON and Java Serialization")
    void testLifecycleContextSerialization() throws Exception {
        LifecycleContext lifecycle = LifecycleContext.builder()
                .lifecycleStage("COMMITTED")
                .persistenceTimestamp(Instant.ofEpochMilli(1700000000000L))
                .commitOutcome(AuthoritativeCommitOutcome.COMMITTED)
                .retained(true)
                .coordinationAttribute("node", "mneme-01")
                .build();

        // JSON
        String json = objectMapper.writeValueAsString(lifecycle);
        LifecycleContext fromJson = objectMapper.readValue(json, LifecycleContext.class);
        assertThat(fromJson).isEqualTo(lifecycle);

        // Java Serialization
        LifecycleContext fromJava = javaSerializeRoundtrip(lifecycle);
        assertThat(fromJava).isEqualTo(lifecycle);
    }

    @Test
    @DisplayName("InformationUnitRelationship: should round-trip through JSON and Java Serialization")
    void testRelationshipSerialization() throws Exception {
        InformationUnitRelationship rel = InformationUnitRelationship.of(
                "relatesTo", "iu-target-123", true, "Primary care episode");

        // JSON
        String json = objectMapper.writeValueAsString(rel);
        InformationUnitRelationship fromJson = objectMapper.readValue(json, InformationUnitRelationship.class);
        assertThat(fromJson).isEqualTo(rel);

        // Java Serialization
        InformationUnitRelationship fromJava = javaSerializeRoundtrip(rel);
        assertThat(fromJava).isEqualTo(rel);
    }

    @Test
    @DisplayName("InformationRepresentation (Full and Summary): should round-trip through JSON and Java Serialization")
    void testRepresentationSerialization() throws Exception {
        ContainerMetadata container = ContainerMetadata.builder().containerType("FHIR_BUNDLE").build();
        ContentMetadata contentMeta = ContentMetadata.builder()
                .contentStructureTopic("Health.FHIR.R5.Patient")
                .contentStructureVersion("R5")
                .contentFormat("application/json")
                .contentReference("s3://store/p-1.json")
                .build();

        InformationRepresentation full = InformationRepresentation.of(container, contentMeta, "{\"patient\":\"123\"}");
        InformationRepresentation summary = full.toSummary();

        // Full JSON
        String fullJson = objectMapper.writeValueAsString(full);
        InformationRepresentation fullFromJson = objectMapper.readValue(fullJson, InformationRepresentation.class);
        assertThat(fullFromJson).isEqualTo(full);
        assertThat(fullFromJson.isSummary()).isFalse();

        // Summary JSON
        String summaryJson = objectMapper.writeValueAsString(summary);
        InformationRepresentation summaryFromJson = objectMapper.readValue(summaryJson, InformationRepresentation.class);
        assertThat(summaryFromJson).isEqualTo(summary);
        assertThat(summaryFromJson.isSummary()).isTrue();
        assertThat(summaryFromJson.contentMetadata().contentReference()).contains("s3://store/p-1.json");

        // Java Serialization
        assertThat(javaSerializeRoundtrip(full)).isEqualTo(full);
        assertThat(javaSerializeRoundtrip(summary)).isEqualTo(summary);
    }

    @Test
    @DisplayName("InformationUnit (Full and Summary): should round-trip through JSON and Java Serialization")
    void testInformationUnitSerialization() throws Exception {
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.ofEpochMilli(1700000000000L);

        InformationUnit fullUnit = InformationUnit.builder()
                .identifier(uuid)
                .managementContext(ManagementContext.builder()
                        .operationalVersion("1")
                        .activeGeneration("gen-001")
                        .cachedAt(now)
                        .build())
                .informationCoordinates(InformationCoordinates.of("GatewayA", "EndpointB", "LocC", "OwnerD"))
                .securityContext(InformationSecurityContext.builder().addActionMask("READ").build())
                .lifecycleContext(LifecycleContext.builder().lifecycleStage("COMMITTED").build())
                .addRelationship(InformationUnitRelationship.local("relatesTo", "iu-prev-01"))
                .containerMetadata(ContainerMetadata.builder().containerType("DOCUMENT").build())
                .contentMetadata(ContentMetadata.builder()
                        .contentStructureTopic("Health.FHIR.R5.Patient")
                        .contentStructureVersion("R5")
                        .contentReference("urn:uuid:patient-doc-ref")
                        .build())
                .content("{\"resourceType\":\"Patient\"}")
                .build();

        InformationUnit summaryUnit = fullUnit.toSummary();

        // Full JSON
        String fullJson = objectMapper.writeValueAsString(fullUnit);
        InformationUnit fullFromJson = objectMapper.readValue(fullJson, InformationUnit.class);
        assertThat(fullFromJson).isEqualTo(fullUnit);
        assertThat(fullFromJson.isSummary()).isFalse();

        // Summary JSON
        String summaryJson = objectMapper.writeValueAsString(summaryUnit);
        InformationUnit summaryFromJson = objectMapper.readValue(summaryJson, InformationUnit.class);
        assertThat(summaryFromJson).isEqualTo(summaryUnit);
        assertThat(summaryFromJson.isSummary()).isTrue();
        assertThat(summaryFromJson.contentMetadata().contentReference()).contains("urn:uuid:patient-doc-ref");

        // Java Serialization
        assertThat(javaSerializeRoundtrip(fullUnit)).isEqualTo(fullUnit);
        assertThat(javaSerializeRoundtrip(summaryUnit)).isEqualTo(summaryUnit);
    }

    @Test
    @DisplayName("TopicMask: should round-trip through JSON and Java Serialization")
    void testTopicMaskSerialization() throws Exception {
        TopicMask mask = TopicMask.of("Health.HL7.2.4.ADT.(A01|A08)");

        // JSON
        String json = objectMapper.writeValueAsString(mask);
        TopicMask fromJson = objectMapper.readValue(json, TopicMask.class);
        assertThat(fromJson).isEqualTo(mask);
        assertThat(fromJson.matches("Health.HL7.2.4.ADT.A01")).isTrue();

        // Java Serialization
        TopicMask fromJava = javaSerializeRoundtrip(mask);
        assertThat(fromJava).isEqualTo(mask);
        assertThat(fromJava.matches("Health.HL7.2.4.ADT.A01")).isTrue();
        assertThat(fromJava.matches("Health.HL7.2.4.ADT.A03")).isFalse();
    }
}
