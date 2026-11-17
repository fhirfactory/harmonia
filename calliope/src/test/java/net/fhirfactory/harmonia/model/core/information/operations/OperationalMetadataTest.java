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

package net.fhirfactory.harmonia.model.core.information.operations;

import net.fhirfactory.harmonia.model.core.naming.factory.TopicMaskFactory;

import net.fhirfactory.harmonia.model.core.naming.factory.TopicFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.fhirfactory.harmonia.model.core.information.access.AuthoritativeCommitOutcome;
import net.fhirfactory.harmonia.model.core.information.entity.InformationMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.InformationUnit;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.Content;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContentMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContainerMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationRepresentation;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationUnitIdentifier;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.LifecycleContext;
import net.fhirfactory.harmonia.model.core.information.operations.factory.ManagementContextBuilder;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.naming.TopicMask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OperationalMetadata typed context, round-trip, and version distinction tests")
class OperationalMetadataTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Retains JSON value kinds and typed working context through JSON and Java serialization")
    void preservesJsonTypesAndManagementContextOnRoundTrip() throws Exception {
        Instant now = Instant.parse("2026-01-02T03:04:05Z");
        ManagementContext managementContext = new ManagementContextBuilder()
                .operationalRevision(OperationalRevision.of("42"))
                .activeGeneration("generation-7")
                .cachedAt(now)
                .transientProperty("node", "cache-1")
                .build();
        ObjectNode coordinationValue = objectMapper.createObjectNode()
                .put("attempt", 2)
                .put("accepted", true);
        OperationalMetadata metadata = OperationalMetadata.builder()
                .subjectIdentifier("harmonia.mneme.iu.123e4567e89b12d3a456426614174000")
                .operationalContext(TopicFactory.fromName("Harmonia.Mneme.Operational"))
                .managementContext(managementContext)
                .lifecycleStage("COMMITTED")
                .persistenceTimestamp(now)
                .commitOutcome(AuthoritativeCommitOutcome.COMMITTED)
                .coordinationAttribute("delivery", coordinationValue)
                .temporalObservation("sequence", objectMapper.getNodeFactory().numberNode(17))
                .evidenceReference("urn:audit:operation-1")
                .build();

        String json = objectMapper.writeValueAsString(metadata);
        JsonNode jsonTree = objectMapper.readTree(json);
        assertThat(jsonTree.has("operationalRevision")).isFalse();
        assertThat(jsonTree.has("operationalVersion")).isFalse();
        assertThat(jsonTree.path("managementContext").path("operationalRevision").asText()).isEqualTo("42");
        assertThat(jsonTree.path("coordinationAttributes").path("delivery").path("attempt").isInt()).isTrue();
        assertThat(jsonTree.path("coordinationAttributes").path("delivery").path("accepted").isBoolean()).isTrue();
        assertThat(jsonTree.path("temporalObservations").path("sequence").isInt()).isTrue();

        OperationalMetadata fromJson = objectMapper.readValue(json, OperationalMetadata.class);
        assertThat(fromJson).isEqualTo(metadata);
        assertThat(fromJson.managementContext()).contains(managementContext);
        assertThat(fromJson.operationalRevision()).contains(OperationalRevision.of("42"));
        assertThat(fromJson.activeGeneration()).contains("generation-7");
        assertThat(fromJson.cachedAt()).contains(now);
        assertThat(fromJson.coordinationAttributes().get("delivery").get("attempt").isInt()).isTrue();

        assertThat(javaSerializeRoundTrip(metadata)).isEqualTo(metadata);
    }

    @Test
    @DisplayName("Keeps persisted, operational, content-structure, and native-content versions independent")
    void keepsVersionDimensionsIndependent() {
        String authoritativeVersion = "10";
        OperationalRevision operationalRevision = OperationalRevision.of("12");
        String nativePayload = "{\"resourceType\":\"Patient\",\"meta\":{\"versionId\":\"native-v2\"}}";
        ContentMetadata contentMetadata = ContentMetadata.builder()
                .contentStructureTopic(TopicFactory.fromName("Health.FHIR.R5.Patient"))
                .contentStructureVersion("R5")
                .build();
        InformationRepresentation representation = new InformationRepresentation(
                ContainerMetadata.empty(), contentMetadata, Content.text(nativePayload));
        InformationUnit unit = new InformationUnit(InformationUnitIdentifier.random(), authoritativeVersion,
                InformationMetadata.builder().lifecycleContext(LifecycleContext.builder().retained(true).build()).build(),
                null, representation);
        ManagementContext managementContext = new ManagementContextBuilder()
                .operationalRevision(operationalRevision)
                .activeGeneration("cache-generation-12")
                .build();
        OperationalMetadata operationalMetadata = OperationalMetadata.builder()
                .subjectTopic(unit.identifier().toTopic())
                .managementContext(managementContext)
                .lifecycleStage("COMMITTED")
                .commitOutcome(AuthoritativeCommitOutcome.COMMITTED)
                .build();

        assertThat(unit.version()).isEqualTo(authoritativeVersion);
        assertThat(operationalMetadata.operationalRevision()).contains(operationalRevision);
        assertThat(operationalMetadata.activeGeneration()).contains("cache-generation-12");
        assertThat(unit.metadata().lifecycleContext().isRetained()).isTrue();
        assertThat(unit.representation().contentMetadata().contentStructureVersion()).contains("R5");
        assertThat(unit.representation().content().textContent()).contains(nativePayload);
        assertThat(operationalMetadata.getCommitOutcome()).isEqualTo(AuthoritativeCommitOutcome.COMMITTED);
    }

    @Test
    @DisplayName("OperationalMetadataMask matches operational criteria and rejects absent candidates")
    void matchesOperationalCriteriaConjunctively() {
        ManagementContext context = new ManagementContextBuilder()
                .operationalRevision(OperationalRevision.of("5"))
                .activeGeneration("generation-1")
                .build();
        OperationalMetadata metadata = OperationalMetadata.builder()
                .subjectIdentifier("harmonia.mneme.iu.123e4567e89b12d3a456426614174000")
                .operationalContext(TopicFactory.fromName("Harmonia.Mneme.Operational"))
                .managementContext(context)
                .lifecycleStage("COMMITTED")
                .commitOutcome(AuthoritativeCommitOutcome.COMMITTED)
                .build();
        OperationalMetadataMask matchingMask = OperationalMetadataMask.builder()
                .subjectMask(TopicMaskFactory.fromNamePattern("harmonia.mneme.iu.*"))
                .contextMask(TopicMaskFactory.fromNamePattern("Harmonia.Mneme.*"))
                .operationalRevision("5")
                .activeGeneration("generation-1")
                .lifecycleStage("COMMITTED")
                .commitOutcome(AuthoritativeCommitOutcome.COMMITTED)
                .build();

        assertThat(matchingMask.matches(metadata)).isTrue();
        assertThat(OperationalMetadataMask.builder().operationalRevision("6").build().matches(metadata)).isFalse();
        assertThat(OperationalMetadataMask.builder().lifecycleStage("RETIRED").build().matches(metadata)).isFalse();
        assertThat(OperationalMetadataMask.builder().commitOutcome(AuthoritativeCommitOutcome.NOT_COMMITTED)
                .build().matches(metadata)).isFalse();
        assertThat(matchingMask.matches(null)).isFalse();
    }

    @Test
    @DisplayName("Operational diagnostic summary does not expose subject, evidence, or observation values")
    void diagnosticsDoNotExposeOperationalValues() {
        String privateValue = "patient-secret-42";
        ObjectNode observation = objectMapper.createObjectNode().put("name", privateValue);
        OperationalMetadata metadata = OperationalMetadata.builder()
                .subjectIdentifier(privateValue)
                .coordinationAttribute("clinical", observation)
                .evidenceReference("urn:clinical:" + privateValue)
                .build();

        assertThat(metadata.toString()).doesNotContain(privateValue).doesNotContain("clinical");
    }

    @SuppressWarnings("unchecked")
    private static <T> T javaSerializeRoundTrip(T value) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream output = new ObjectOutputStream(bytes)) {
            output.writeObject(value);
        }
        try (ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            return (T) input.readObject();
        }
    }
}
