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
import net.fhirfactory.harmonia.model.topic.Topic;
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
        Topic gw = Topic.fromName("Health.Gateway.A");
        Topic ep = Topic.fromName("Health.Endpoint.B");
        Topic loc = Topic.fromName("Health.Location.C");
        Topic owner = Topic.fromName("Health.Owner.D");

        InformationCoordinates coords = InformationCoordinates.of(gw, ep, loc, owner);

        // JSON
        String json = objectMapper.writeValueAsString(coords);
        InformationCoordinates fromJson = objectMapper.readValue(json, InformationCoordinates.class);
        assertThat(fromJson).isEqualTo(coords);

        // Java Serialization
        InformationCoordinates fromJava = javaSerializeRoundtrip(coords);
        assertThat(fromJava).isEqualTo(coords);
    }

    @Test
    @DisplayName("InformationItinerary: should round-trip through JSON and Java Serialization")
    void testInformationItinerarySerialization() throws Exception {
        Topic src = Topic.fromName("Health.Gateway.MllpIn");
        Topic create = Topic.fromName("Health.Ergon.Ingress");
        Topic part = Topic.fromName("Health.Ergon.Audit");

        InformationItinerary itinerary = InformationItinerary.builder()
                .sourceEntryComponent(src)
                .creationComponent(create)
                .addParticipatingComponent(part)
                .build();

        // JSON
        String json = objectMapper.writeValueAsString(itinerary);
        InformationItinerary fromJson = objectMapper.readValue(json, InformationItinerary.class);
        assertThat(fromJson).isEqualTo(itinerary);

        // Java Serialization
        InformationItinerary fromJava = javaSerializeRoundtrip(itinerary);
        assertThat(fromJava).isEqualTo(itinerary);
    }

    @Test
    @DisplayName("InformationSecurityContext: should round-trip through JSON and Java Serialization")
    void testSecurityContextSerialization() throws Exception {
        Topic unitTopic = Topic.fromName("Health.Security.ProtectedHealthInformation");
        TopicMask compMask = TopicMask.fromPattern("Health.Compartment.Oncology.*");

        InformationSecurityContext context = InformationSecurityContext.builder()
                .addUnitCriterion(unitTopic)
                .addActionMask("READ")
                .addActionMask("WRITE")
                .addCompartmentMask(compMask)
                .addSecurityLabel(HarmoniaSecurityLabelEnum.RESTRICTED)
                .confidentiality(FhirConfidentialityEnum.R)
                .putAttribute("env", "prod")
                .build();

        // JSON
        String json = objectMapper.writeValueAsString(context);
        InformationSecurityContext fromJson = objectMapper.readValue(json, InformationSecurityContext.class);
        assertThat(fromJson).isEqualTo(context);

        // Java Serialization
        InformationSecurityContext fromJava = javaSerializeRoundtrip(context);
        assertThat(fromJava).isEqualTo(context);
    }

    @Test
    @DisplayName("LifecycleContext: should round-trip through JSON and Java Serialization")
    void testLifecycleContextSerialization() throws Exception {
        LifecycleContext context = LifecycleContext.builder()
                .lifecycleStage("COMMITTED")
                .persistenceTimestamp(Instant.ofEpochMilli(1700000000000L))
                .commitOutcome(AuthoritativeCommitOutcome.COMMITTED)
                .retained(true)
                .archived(false)
                .build();

        // JSON
        String json = objectMapper.writeValueAsString(context);
        LifecycleContext fromJson = objectMapper.readValue(json, LifecycleContext.class);
        assertThat(fromJson).isEqualTo(context);

        // Java Serialization
        LifecycleContext fromJava = javaSerializeRoundtrip(context);
        assertThat(fromJava).isEqualTo(context);
    }

    @Test
    @DisplayName("InformationUnitRelationship: should round-trip through JSON and Java Serialization")
    void testRelationshipSerialization() throws Exception {
        InformationUnitRelationship rel = InformationUnitRelationship.local("relatesTo", "target-iu-001");

        // JSON
        String json = objectMapper.writeValueAsString(rel);
        InformationUnitRelationship fromJson = objectMapper.readValue(json, InformationUnitRelationship.class);
        assertThat(fromJson).isEqualTo(rel);

        // Java Serialization
        InformationUnitRelationship fromJava = javaSerializeRoundtrip(rel);
        assertThat(fromJava).isEqualTo(rel);
    }

    @Test
    @DisplayName("ContainerMetadata & ContentMetadata: should round-trip through JSON and Java Serialization")
    void testContainerAndContentMetadataSerialization() throws Exception {
        ContainerMetadata container = ContainerMetadata.builder()
                .containerType("FHIR_BUNDLE")
                .source("pas-gw")
                .destination("mneme-store")
                .author("DrSmith")
                .assemblyContext("DischargeSummary")
                .build();

        Topic contentTopic = Topic.fromName("Health.FHIR.R5.Patient");

        ContentMetadata content = ContentMetadata.builder()
                .contentStructureTopic(contentTopic)
                .contentStructureVersion("R5")
                .contentFormat("application/fhir+json")
                .contentReference("s3://bundles/b-1.json")
                .contentDigest("sha256-abcdef")
                .contentSize(4096L)
                .putAttribute("profile", "PatientProfile")
                .build();

        // JSON Container
        String cJson = objectMapper.writeValueAsString(container);
        ContainerMetadata cFromJson = objectMapper.readValue(cJson, ContainerMetadata.class);
        assertThat(cFromJson).isEqualTo(container);

        // JSON Content
        String contJson = objectMapper.writeValueAsString(content);
        ContentMetadata contFromJson = objectMapper.readValue(contJson, ContentMetadata.class);
        assertThat(contFromJson).isEqualTo(content);

        // Java Serialization
        assertThat(javaSerializeRoundtrip(container)).isEqualTo(container);
        assertThat(javaSerializeRoundtrip(content)).isEqualTo(content);
    }

    @Test
    @DisplayName("Complete InformationUnit (Full and Summary): should round-trip through JSON and Java Serialization")
    void testCompleteInformationUnitSerialization() throws Exception {
        Topic gw = Topic.fromName("Health.Gateway.A");
        Topic ep = Topic.fromName("Health.Endpoint.B");
        Topic loc = Topic.fromName("Health.Location.C");
        Topic owner = Topic.fromName("Health.Owner.D");
        Topic src = Topic.fromName("Health.Gateway.In");
        Topic contentTopic = Topic.fromName("Health.FHIR.R5.Patient");

        InformationUnit fullUnit = InformationUnit.builder()
                .randomIdentifier()
                .version("1")
                .managementContext(ManagementContext.builder()
                        .operationalVersion("100")
                        .activeGeneration("gen-alpha")
                        .build())
                .informationCoordinates(InformationCoordinates.of(gw, ep, loc, owner))
                .informationItinerary(InformationItinerary.builder().sourceEntryComponent(src).build())
                .securityContext(InformationSecurityContext.builder().addActionMask("READ").build())
                .lifecycleContext(LifecycleContext.builder().lifecycleStage("COMMITTED").build())
                .containerMetadata(ContainerMetadata.builder().containerType("FHIR_BUNDLE").build())
                .contentMetadata(ContentMetadata.builder()
                        .contentStructureTopic(contentTopic)
                        .contentStructureVersion("R5")
                        .contentFormat("application/fhir+json")
                        .build())
                .content("{\"resourceType\":\"Patient\",\"id\":\"p-1\"}")
                .build();

        // JSON Full
        String fullJson = objectMapper.writeValueAsString(fullUnit);
        InformationUnit fullFromJson = objectMapper.readValue(fullJson, InformationUnit.class);
        assertThat(fullFromJson).isEqualTo(fullUnit);
        assertThat(fullFromJson.isSummary()).isFalse();
        assertThat(fullFromJson.contentAsString()).contains("{\"resourceType\":\"Patient\",\"id\":\"p-1\"}");

        // Java Serialization Full
        InformationUnit fullFromJava = javaSerializeRoundtrip(fullUnit);
        assertThat(fullFromJava).isEqualTo(fullUnit);

        // Summary Projection
        InformationUnit summary = fullUnit.toSummary();

        // JSON Summary
        String summaryJson = objectMapper.writeValueAsString(summary);
        InformationUnit summaryFromJson = objectMapper.readValue(summaryJson, InformationUnit.class);
        assertThat(summaryFromJson).isEqualTo(summary);
        assertThat(summaryFromJson.isSummary()).isTrue();
        assertThat(summaryFromJson.content()).isEmpty();

        // Java Serialization Summary
        InformationUnit summaryFromJava = javaSerializeRoundtrip(summary);
        assertThat(summaryFromJava).isEqualTo(summary);
        assertThat(summaryFromJava.isSummary()).isTrue();
    }
}
