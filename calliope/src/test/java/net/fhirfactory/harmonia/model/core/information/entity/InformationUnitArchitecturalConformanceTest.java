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

package net.fhirfactory.harmonia.model.core.information.entity;

import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationUnitValueSets;

import net.fhirfactory.harmonia.model.core.naming.factory.TopicMaskFactory;

import net.fhirfactory.harmonia.model.core.naming.factory.TopicFactory;

import net.fhirfactory.harmonia.model.core.information.access.AuthoritativeCommitOutcome;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.Content;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContentMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContainerMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationCoordinates;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationRepresentation;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationUnitIdentifier;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationUnitRelationship;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.LifecycleContext;
import net.fhirfactory.harmonia.model.core.information.entity.factory.ContainerMetadataBuilder;
import net.fhirfactory.harmonia.model.core.information.operations.ManagementContext;
import net.fhirfactory.harmonia.model.core.information.operations.OperationalMetadata;
import net.fhirfactory.harmonia.model.core.information.operations.OperationalMetadataMask;
import net.fhirfactory.harmonia.model.core.information.operations.OperationalRevision;
import net.fhirfactory.harmonia.model.core.information.operations.factory.ManagementContextBuilder;
import net.fhirfactory.harmonia.model.core.security.valuesets.FhirConfidentialityEnum;
import net.fhirfactory.harmonia.model.core.security.valuesets.HarmoniaSecurityLabelEnum;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.naming.TopicMask;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Architectural semantic conformance test suite verifying that the rebuilt Calliope InformationUnit model
 * and foundational elements faithfully express the accepted 1.1.25 Architecture Continuum baseline.
 */
@DisplayName("Calliope InformationUnit Architectural Semantic Conformance")
public class InformationUnitArchitecturalConformanceTest {

    // =========================================================================
    // Dimension 1: Topic as Default Identity and Classification Construct
    // =========================================================================

    @Nested
    @DisplayName("Dimension 1: Topic as Default Descriptor and Type Classifier")
    class TopicAsDefaultClassifierTests {

        @Test
        @DisplayName("ContainerMetadata represents container/envelope classification as Topic")
        void testContainerMetadataTypeIsTopic() {
            ContainerMetadata container = ContainerMetadata.builder()
                    .type(InformationUnitValueSets.TYPE_FHIR_BUNDLE)
                    .sourceTopic(TopicFactory.fromName("Harmonia.Gateway.Inbound.MLLP"))
                    .destinationTopic(TopicFactory.fromName("Harmonia.Service.Mneme.Clinical"))
                    .authorTopic(TopicFactory.fromName("External.Hospital.PAS"))
                    .recipientTopic(TopicFactory.fromName("Harmonia.System.Core"))
                    .build();

            assertThat(container.type()).isEqualTo(InformationUnitValueSets.TYPE_FHIR_BUNDLE);
            assertThat(container.type().getName()).isEqualTo("Harmonia.Container.FHIR.Bundle");
            assertThat(container.sourceTopic()).contains(TopicFactory.fromName("Harmonia.Gateway.Inbound.MLLP"));
            assertThat(container.destinationTopic()).contains(TopicFactory.fromName("Harmonia.Service.Mneme.Clinical"));
            assertThat(container.authorTopic()).contains(TopicFactory.fromName("External.Hospital.PAS"));
            assertThat(container.recipientTopic()).contains(TopicFactory.fromName("Harmonia.System.Core"));

            // Topic identity is the complete canonical path, not a generated runtime UUID.
            assertThat(Topic.fromCanonicalName(container.type().getCanonicalName())).isEqualTo(container.type());
        }

        @Test
        @DisplayName("ContentMetadata represents content structure/schema as Topic separate from Version")
        void testContentMetadataTypeIsTopicSeparateFromVersion() {
            Topic schemaTopic = TopicFactory.fromName("Health.FHIR.R5.Patient");
            String schemaVersion = "R5.0.0";

            ContentMetadata contentMeta = ContentMetadata.builder()
                    .contentStructureTopic(schemaTopic)
                    .contentStructureVersion(schemaVersion)
                    .contentFormat("application/fhir+json")
                    .contentReference("urn:uuid:external-blob-123")
                    .build();

            // Strongly typed Topic accessor
            assertThat(contentMeta.contentStructureTopic()).contains(schemaTopic);
            assertThat(contentMeta.contentStructureTopic().orElseThrow().getName()).isEqualTo("Health.FHIR.R5.Patient");

            // Associated content version remains independent of optional TopicEntry versions.
            assertThat(contentMeta.contentStructureVersion()).contains("R5.0.0");
            assertThat(contentMeta.structureVersion()).contains("R5.0.0");
        }

        @Test
        @DisplayName("InformationUnitRelationship represents relationship classification as Topic")
        void testRelationshipTypeIsTopic() {
            InformationUnitRelationship rel = InformationUnitRelationship.local(
                    InformationUnitValueSets.TYPE_BASED_ON,
                    "target-iu-uuid-456");

            assertThat(rel.type()).isEqualTo(InformationUnitValueSets.TYPE_BASED_ON);
            assertThat(rel.type().getName()).isEqualTo("Harmonia.Relationship.BasedOn");
            assertThat(rel.isLocal()).isTrue();
            assertThat(rel.isRemote()).isFalse();

            assertThat(rel.type().getName()).isEqualTo("Harmonia.Relationship.BasedOn");
        }

        @Test
        @DisplayName("LifecycleContext represents governed disposition status as Topic")
        void testDispositionStatusIsTopic() {
            LifecycleContext lifecycle = LifecycleContext.builder()
                    .dispositionStatus(InformationUnitValueSets.DISPOSITION_ACTIVE)
                    .retentionPolicy(TopicFactory.fromName("Harmonia.Policy.Retention.SevenYears"))
                    .retained(true)
                    .archived(false)
                    .build();

            assertThat(lifecycle.dispositionStatus()).contains(InformationUnitValueSets.DISPOSITION_ACTIVE);
            assertThat(lifecycle.dispositionStatus().get().getName()).isEqualTo("Harmonia.Lifecycle.Active");
            assertThat(lifecycle.retentionPolicy()).contains(TopicFactory.fromName("Harmonia.Policy.Retention.SevenYears"));
            assertThat(lifecycle.isRetained()).isTrue();
            assertThat(lifecycle.isArchived()).isFalse();
        }

        @Test
        @DisplayName("InformationMetadata represents origin as Topic")
        void testInformationOriginIsTopic() {
            Topic originTopic = TopicFactory.fromName("Harmonia.Origin.CanberraHospital.EmergencyDepartment");

            InformationMetadata metadata = InformationMetadata.builder()
                    .origin(originTopic)
                    .build();

            assertThat(metadata.origin()).contains(originTopic);
            assertThat(metadata.getOrigin()).isEqualTo(originTopic);
            assertThat(metadata.origin().get().getName()).isEqualTo("Harmonia.Origin.CanberraHospital.EmergencyDepartment");
        }
    }

    // =========================================================================
    // Dimension 2: 7-Dimensional Versioning & Operational Separation
    // =========================================================================

    @Nested
    @DisplayName("Dimension 2: Strict 7-Dimensional Versioning Separation")
    class VersioningSeparationTests {

        @Test
        @DisplayName("Authoritative persisted version, operational revision, and content versions remain distinct")
        void testSevenDimensionalVersionDistinction() {
            // 1. Authoritative Persisted IU Version (durable database version committed by Mnemosyne)
            String authoritativeVersion = "42";

            // 2. Mneme Operational Revision (active working cache concurrency revision)
            OperationalRevision operationalRevision = OperationalRevision.of("108");

            // 3. Cache Active Generation (unique identifier of active cache manifestation)
            String activeGen = "cache-gen-node-7";

            // 4. Persistence Coordination Stage & Outcome (middleware handshake)
            String lifecycleStage = "COMMITTED";
            AuthoritativeCommitOutcome commitOutcome = AuthoritativeCommitOutcome.COMMITTED;

            // 5. Governed Information Lifecycle (disposition and legal hold)
            Topic dispositionStatus = InformationUnitValueSets.DISPOSITION_ACTIVE;
            boolean retentionHold = true;

            // 6. Content Structure Version (schema definition version, e.g. "R5")
            String contentStructureVersion = "R5";

            // 7. Native Content Version (intrinsic payload version, e.g. FHIR meta.versionId)
            String nativePayload = "{\"resourceType\":\"Patient\",\"id\":\"p1\",\"meta\":{\"versionId\":\"fhir-v3\"}}";

            InformationMetadata metadata = InformationMetadata.builder()
                    .origin(TopicFactory.fromName("Hospital.Site.Canberra"))
                    .lifecycleContext(LifecycleContext.builder()
                            .dispositionStatus(dispositionStatus)
                            .retained(retentionHold)
                            .build())
                    .build();
            ContentMetadata contentMetadata = ContentMetadata.builder()
                    .contentStructureTopic(TopicFactory.fromName("Health.FHIR.R5.Patient"))
                    .contentStructureVersion(contentStructureVersion)
                    .build();
            InformationRepresentation representation = new InformationRepresentation(
                    new ContainerMetadataBuilder().type(InformationUnitValueSets.TYPE_FHIR_BUNDLE).build(),
                    contentMetadata,
                    Content.text(nativePayload));
            InformationUnit iu = new InformationUnit(InformationUnitIdentifier.random(), authoritativeVersion,
                    metadata, null, representation);

            ManagementContext managementContext = new ManagementContextBuilder()
                    .operationalRevision(operationalRevision)
                    .activeGeneration(activeGen)
                    .build();
            OperationalMetadata op = OperationalMetadata.builder()
                    .subjectTopic(iu.identifier().toTopic())
                    .operationalContext(TopicFactory.fromName("Harmonia.Mneme.Cache"))
                    .managementContext(managementContext)
                    .lifecycleStage(lifecycleStage)
                    .commitOutcome(commitOutcome)
                    .build();

            // Verify Dimension 1: Authoritative Persisted Version
            assertThat(iu.version()).isEqualTo("42");

            // Verify Dimension 2: Operational Revision (typed strictly as OperationalRevision, NOT AuthoritativeVersion)
            assertThat(op.operationalRevision()).contains(operationalRevision);
            assertThat(op.getOperationalRevision()).isEqualTo(OperationalRevision.of("108"));
            assertThat(op.managementContext()).contains(managementContext);

            // Verify Dimension 3: Active Generation
            assertThat(op.activeGeneration()).contains("cache-gen-node-7");

            // Verify Dimension 4: Persistence Coordination Handshake
            assertThat(op.lifecycleStage()).contains("COMMITTED");
            assertThat(op.commitOutcome()).contains(AuthoritativeCommitOutcome.COMMITTED);

            // Verify Dimension 5: Governed Information Lifecycle
            assertThat(iu.metadata().lifecycleContext().dispositionStatus()).contains(InformationUnitValueSets.DISPOSITION_ACTIVE);
            assertThat(iu.metadata().lifecycleContext().isRetained()).isTrue();

            // Verify Dimension 6: Content Structure Version
            assertThat(iu.representation().contentMetadata().contentStructureVersion()).contains("R5");

            // Verify Dimension 7: Native Payload Version
            assertThat(iu.representation().content().textContent()).isPresent();
            assertThat(iu.representation().content().textContent().get()).contains("\"versionId\":\"fhir-v3\"");

            // Assert that operational revisions are NOT duplicated into the IU's representation
            assertThat(iu.representation().contentMetadata().contentStructureVersion()).isNotEqualTo(java.util.Optional.of("42"));
            assertThat(iu.representation().contentMetadata().contentStructureVersion()).isNotEqualTo(java.util.Optional.of("108"));
        }
    }

    // =========================================================================
    // Dimension 3: Portable Content Model & Zero-PHI Diagnostic Logging
    // =========================================================================

    @Nested
    @DisplayName("Dimension 3: Governed Portable Content & Zero-PHI Logging")
    class PortableContentAndSecurityLoggingTests {

        @Test
        @DisplayName("Content supports portable textual, binary, and by-reference contracts")
        void testContentModesAndImmutability() {
            // Textual content
            Content textContent = Content.text("{\"key\":\"value\"}");
            assertThat(textContent.isText()).isTrue();
            assertThat(textContent.isBinary()).isFalse();
            assertThat(textContent.isReference()).isFalse();
            assertThat(textContent.textContent()).contains("{\"key\":\"value\"}");
            assertThat(textContent.textContent().orElseThrow()).isEqualTo("{\"key\":\"value\"}");

            // Binary content with defensive array copying
            byte[] original = "binary-payload-data".getBytes(StandardCharsets.UTF_8);
            Content binaryContent = Content.binary(original);
            assertThat(binaryContent.isBinary()).isTrue();
            assertThat(binaryContent.size()).isEqualTo(original.length);

            // Mutating original array must NOT affect Content immutability
            original[0] = 'X';
            assertThat(binaryContent.binaryContent().orElseThrow()).isNotEqualTo(original);

            // Pass-by-reference content
            Content refContent = Content.byReference("s3://harmonia-blobs/fhir-bundle-large.json");
            assertThat(refContent.isReference()).isTrue();
            assertThat(refContent.reference()).contains("s3://harmonia-blobs/fhir-bundle-large.json");
            assertThat(refContent.reference().orElseThrow()).isEqualTo("s3://harmonia-blobs/fhir-bundle-large.json");
        }

        @Test
        @DisplayName("Invariant 7: Zero-PHI Diagnostic Logging in Content and InformationRepresentation")
        void testZeroPhiDiagnosticLogging() {
            String clinicalPhiPayload = "{\"patientName\":\"John Doe\",\"mrn\":\"MRN-998877\",\"diagnosis\":\"Acute appendicitis\"}";
            Content content = Content.text(clinicalPhiPayload);

            String contentString = content.toString();
            // Diagnostics report mode and length only; NO raw PHI emitted
            assertThat(contentString).contains("Content[TEXT");
            assertThat(contentString).contains("length=" + clinicalPhiPayload.length());
            assertThat(contentString).doesNotContain("John Doe");
            assertThat(contentString).doesNotContain("MRN-998877");
            assertThat(contentString).doesNotContain("appendicitis");

            InformationRepresentation rep = new InformationRepresentation(
                    ContainerMetadata.empty(), ContentMetadata.empty(), content);

            String repString = rep.toString();
            assertThat(repString).doesNotContain("John Doe");
            assertThat(repString).doesNotContain("MRN-998877");
        }

        @Test
        @DisplayName("Summary projection precisely excludes Content while preserving contentReference")
        void testSummaryProjectionSemantics() {
            String externalRef = "urn:blob:archive-2026";
            ContentMetadata contentMetadata = ContentMetadata.builder()
                    .contentStructureTopic(TopicFactory.fromName("Health.FHIR.R5.Patient"))
                    .contentReference(externalRef)
                    .build();
            InformationRepresentation representation = new InformationRepresentation(
                    new ContainerMetadataBuilder().type(InformationUnitValueSets.TYPE_FHIR_BUNDLE).build(),
                    contentMetadata,
                    Content.text("Confidential Medical Text"));
            InformationUnit fullUnit = new InformationUnit(InformationUnitIdentifier.random(), null,
                    InformationMetadata.empty(), null, representation);

            assertThat(fullUnit.isSummary()).isFalse();
            assertThat(fullUnit.representation().hasContent()).isTrue();

            // Project to summary
            InformationUnit summaryUnit = fullUnit.toSummary();

            assertThat(summaryUnit.isSummary()).isTrue();
            assertThat(summaryUnit.representation().hasContent()).isFalse();
            assertThat(summaryUnit.representation().content().isEmpty()).isTrue();
            assertThat(summaryUnit.representation().content().textContent()).isEmpty();

            // Preserves container and content metadata including contentReference
            assertThat(summaryUnit.representation().containerMetadata().type()).isEqualTo(InformationUnitValueSets.TYPE_FHIR_BUNDLE);
            assertThat(summaryUnit.representation().contentMetadata().contentStructureTopic())
                    .contains(TopicFactory.fromName("Health.FHIR.R5.Patient"));
            assertThat(summaryUnit.representation().contentMetadata().contentReference()).contains(externalRef);
            assertThat(summaryUnit.representation().contentMetadata().reference()).contains(externalRef);
        }
    }

    // =========================================================================
    // Dimension 4: Multi-Facet Neutral Matching via InformationUnitMask
    // =========================================================================

    @Nested
    @DisplayName("Dimension 4: Neutral Multi-Facet Matching via InformationUnitMask")
    class InformationUnitMaskMatchingTests {

        @Test
        @DisplayName("InformationUnitMask evaluates multi-facet TopicMask criteria in conjunction")
        void testInformationUnitMaskConjunction() {
            Topic gatewayTopic = TopicFactory.fromName("Harmonia.Gateway.Primary.Inbound");
            Topic originTopic = TopicFactory.fromName("Hospital.Canberra.Emergency");
            Topic containerTopic = InformationUnitValueSets.TYPE_FHIR_BUNDLE;
            Topic contentTopic = TopicFactory.fromName("Health.FHIR.R5.Observation");

            InformationMetadata metadata = InformationMetadata.builder()
                    .origin(originTopic)
                    .coordinates(InformationCoordinates.builder().persistenceGateway(gatewayTopic).build())
                    .build();
            InformationRepresentation representation = new InformationRepresentation(
                    new ContainerMetadataBuilder().type(containerTopic).build(),
                    ContentMetadata.builder().contentStructureTopic(contentTopic).build(),
                    Content.text("Observation Data"));
            InformationUnit unit = new InformationUnit(InformationUnitIdentifier.random(), "1.0.0", metadata,
                    ManagementContext.empty(), representation);

            // Matching composite mask
            InformationUnitMask matchingMask = InformationUnitMask.builder()
                    .identityMask(TopicMaskFactory.fromNamePattern("harmonia.mneme.iu.*"))
                    .originMask(TopicMaskFactory.fromNamePattern("Hospital.Canberra.*"))
                    .containerTypeMask(TopicMaskFactory.nameBased(containerTopic))
                    .contentStructureMask(TopicMaskFactory.fromNamePattern("Health.FHIR.*"))
                    .persistenceGatewayMask(TopicMaskFactory.nameBased(gatewayTopic))
                    .version("1.0.0")
                    .build();

            assertThat(matchingMask.matches(unit)).isTrue();

            // Mask with conflicting container type
            InformationUnitMask nonMatchingContainer = InformationUnitMask.builder()
                    .containerTypeMask(TopicMaskFactory.nameBased(InformationUnitValueSets.TYPE_HL7_MESSAGE))
                    .build();
            assertThat(nonMatchingContainer.matches(unit)).isFalse();

            // Mask with conflicting origin
            InformationUnitMask nonMatchingOrigin = InformationUnitMask.builder()
                    .originMask(TopicMaskFactory.fromNamePattern("Hospital.Sydney.*"))
                    .build();
            assertThat(nonMatchingOrigin.matches(unit)).isFalse();

            // Unrestricted mask matches unconditionally
            assertThat(InformationUnitMask.unrestricted().matches(unit)).isTrue();
            assertThat(InformationUnitMask.unrestricted().matches(null)).isFalse();
        }
    }

    // =========================================================================
    // Dimension 5: Defensive Validation & Invariant Enforcement
    // =========================================================================

    @Nested
    @DisplayName("Dimension 5: Defensive Validation & Rejection of Inconsistent Inputs")
    class DefensiveValidationTests {

        @Test
        @DisplayName("Rejects null or blank OperationalRevision")
        void testOperationalRevisionValidation() {
            assertThatThrownBy(() -> OperationalRevision.of((String) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("value must not be null");

            assertThatThrownBy(() -> OperationalRevision.of("   "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("value must not be blank");
        }

        @Test
        @DisplayName("Rejects null or blank Relationship targetIdentifier")
        void testRelationshipTargetValidation() {
            assertThatThrownBy(() -> new InformationUnitRelationship(InformationUnitValueSets.TYPE_RELATES_TO, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("targetIdentifier must not be null or blank");

            assertThatThrownBy(() -> new InformationUnitRelationship(InformationUnitValueSets.TYPE_RELATES_TO, "   "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("targetIdentifier must not be null or blank");
        }

        @Test
        @DisplayName("InformationUnit requires non-null identifier, metadata, and representation")
        void testInformationUnitInvariantEnforcement() {
            assertThatThrownBy(() -> new InformationUnit(null, null, null, null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("identifier must not be null");
        }
    }
}
