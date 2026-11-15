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

import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeCommitOutcome;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.security.FhirConfidentialityEnum;
import net.fhirfactory.harmonia.model.security.HarmoniaSecurityLabelEnum;
import net.fhirfactory.harmonia.model.topic.Topic;
import net.fhirfactory.harmonia.model.topic.TopicMask;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
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
                    .type(ContainerMetadata.TYPE_FHIR_BUNDLE)
                    .sourceTopic(Topic.fromName("Harmonia.Gateway.Inbound.MLLP"))
                    .destinationTopic(Topic.fromName("Harmonia.Service.Mneme.Clinical"))
                    .authorTopic(Topic.fromName("External.Hospital.PAS"))
                    .recipientTopic(Topic.fromName("Harmonia.System.Core"))
                    .build();

            assertThat(container.type()).isEqualTo(ContainerMetadata.TYPE_FHIR_BUNDLE);
            assertThat(container.type().getName()).isEqualTo("Harmonia.Container.FHIR.Bundle");
            assertThat(container.sourceTopic()).contains(Topic.fromName("Harmonia.Gateway.Inbound.MLLP"));
            assertThat(container.destinationTopic()).contains(Topic.fromName("Harmonia.Service.Mneme.Clinical"));
            assertThat(container.authorTopic()).contains(Topic.fromName("External.Hospital.PAS"));
            assertThat(container.recipientTopic()).contains(Topic.fromName("Harmonia.System.Core"));

            // Topic identity preservation (Topic UUID is preserved, not degraded to string)
            assertThat(container.type().getId()).isNotNull();
        }

        @Test
        @DisplayName("ContentMetadata represents content structure/schema as Topic separate from Version")
        void testContentMetadataTypeIsTopicSeparateFromVersion() {
            Topic schemaTopic = Topic.fromName("Health.FHIR.R5.Patient");
            String schemaVersion = "R5.0.0";

            ContentMetadata contentMeta = ContentMetadata.builder()
                    .contentStructureTopic(schemaTopic)
                    .contentStructureVersion(schemaVersion)
                    .contentFormat("application/fhir+json")
                    .contentReference("urn:uuid:external-blob-123")
                    .build();

            // Strongly typed Topic accessor
            assertThat(contentMeta.type()).isEqualTo(schemaTopic);
            assertThat(contentMeta.type().getName()).isEqualTo("Health.FHIR.R5.Patient");

            // Architectural Principle 3: Associated version describes the version of the identified concept; Topic has no version
            assertThat(contentMeta.contentStructureVersion()).contains("R5.0.0");
            assertThat(contentMeta.structureVersion()).contains("R5.0.0");
        }

        @Test
        @DisplayName("InformationUnitRelationship represents relationship classification as Topic")
        void testRelationshipTypeIsTopic() {
            InformationUnitRelationship rel = InformationUnitRelationship.local(
                    InformationUnitRelationship.TYPE_BASED_ON,
                    "target-iu-uuid-456");

            assertThat(rel.type()).isEqualTo(InformationUnitRelationship.TYPE_BASED_ON);
            assertThat(rel.type().getName()).isEqualTo("Harmonia.Relationship.BasedOn");
            assertThat(rel.isLocal()).isTrue();
            assertThat(rel.isRemote()).isFalse();

            // Backward-compatible short-name bridge for conventional logging/assertions
            assertThat(rel.relationshipType()).isEqualTo("basedOn");
        }

        @Test
        @DisplayName("LifecycleContext represents governed disposition status as Topic")
        void testDispositionStatusIsTopic() {
            LifecycleContext lifecycle = LifecycleContext.builder()
                    .dispositionStatus(LifecycleContext.DISPOSITION_ACTIVE)
                    .retentionPolicy(Topic.fromName("Harmonia.Policy.Retention.SevenYears"))
                    .retained(true)
                    .archived(false)
                    .build();

            assertThat(lifecycle.dispositionStatus()).contains(LifecycleContext.DISPOSITION_ACTIVE);
            assertThat(lifecycle.dispositionStatus().get().getName()).isEqualTo("Harmonia.Lifecycle.Active");
            assertThat(lifecycle.retentionPolicy()).contains(Topic.fromName("Harmonia.Policy.Retention.SevenYears"));
            assertThat(lifecycle.isRetained()).isTrue();
            assertThat(lifecycle.isArchived()).isFalse();
        }

        @Test
        @DisplayName("InformationMetadata represents origin as Topic")
        void testInformationOriginIsTopic() {
            Topic originTopic = Topic.fromName("Harmonia.Origin.CanberraHospital.EmergencyDepartment");

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
            Topic dispositionStatus = LifecycleContext.DISPOSITION_ACTIVE;
            boolean retentionHold = true;

            // 6. Content Structure Version (schema definition version, e.g. "R5")
            String contentStructureVersion = "R5";

            // 7. Native Content Version (intrinsic payload version, e.g. FHIR meta.versionId)
            String nativePayload = "{\"resourceType\":\"Patient\",\"id\":\"p1\",\"meta\":{\"versionId\":\"fhir-v3\"}}";

            InformationUnit iu = InformationUnit.builder()
                    .randomIdentifier()
                    .version(authoritativeVersion)
                    .origin(Topic.fromName("Hospital.Site.Canberra"))
                    .lifecycleContext(LifecycleContext.builder()
                            .dispositionStatus(dispositionStatus)
                            .retained(retentionHold)
                            .build())
                    .containerMetadata(ContainerMetadata.builder().type(ContainerMetadata.TYPE_FHIR_BUNDLE).build())
                    .contentMetadata(ContentMetadata.builder()
                            .contentStructureTopic(Topic.fromName("Health.FHIR.R5.Patient"))
                            .contentStructureVersion(contentStructureVersion)
                            .build())
                    .content(Content.text(nativePayload))
                    .build();

            OperationalMetadata op = OperationalMetadata.builder()
                    .subjectTopic(iu.toTopic())
                    .operationalContext(Topic.fromName("Harmonia.Mneme.Cache"))
                    .operationalRevision(operationalRevision)
                    .activeGeneration(activeGen)
                    .lifecycleStage(lifecycleStage)
                    .commitOutcome(commitOutcome)
                    .build();

            // Verify Dimension 1: Authoritative Persisted Version
            assertThat(iu.version()).isEqualTo("42");
            assertThat(iu.getVersion()).isEqualTo("42");

            // Verify Dimension 2: Operational Revision (typed strictly as OperationalRevision, NOT AuthoritativeVersion)
            assertThat(op.operationalRevision()).contains(operationalRevision);
            assertThat(op.getOperationalRevision()).isEqualTo(OperationalRevision.of("108"));
            // Backward-compatibility bridge
            assertThat(op.operationalVersion()).contains(AuthoritativeVersion.of("108"));

            // Verify Dimension 3: Active Generation
            assertThat(op.activeGeneration()).contains("cache-gen-node-7");

            // Verify Dimension 4: Persistence Coordination Handshake
            assertThat(op.lifecycleStage()).contains("COMMITTED");
            assertThat(op.commitOutcome()).contains(AuthoritativeCommitOutcome.COMMITTED);

            // Verify Dimension 5: Governed Information Lifecycle
            assertThat(iu.lifecycleContext().dispositionStatus()).contains(LifecycleContext.DISPOSITION_ACTIVE);
            assertThat(iu.lifecycleContext().isRetained()).isTrue();

            // Verify Dimension 6: Content Structure Version
            assertThat(iu.contentMetadata().contentStructureVersion()).contains("R5");

            // Verify Dimension 7: Native Payload Version
            assertThat(iu.contentAsString()).isPresent();
            assertThat(iu.contentAsString().get()).contains("\"versionId\":\"fhir-v3\"");

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
            assertThat(textContent.asString()).isEqualTo("{\"key\":\"value\"}");

            // Binary content with defensive array copying
            byte[] original = "binary-payload-data".getBytes(StandardCharsets.UTF_8);
            Content binaryContent = Content.binary(original);
            assertThat(binaryContent.isBinary()).isTrue();
            assertThat(binaryContent.size()).isEqualTo(original.length);

            // Mutating original array must NOT affect Content immutability
            original[0] = 'X';
            assertThat(binaryContent.asBytes()).isNotEqualTo(original);

            // Pass-by-reference content
            Content refContent = Content.byReference("s3://harmonia-blobs/fhir-bundle-large.json");
            assertThat(refContent.isReference()).isTrue();
            assertThat(refContent.reference()).contains("s3://harmonia-blobs/fhir-bundle-large.json");
            assertThat(refContent.asReference()).isEqualTo("s3://harmonia-blobs/fhir-bundle-large.json");
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

            InformationRepresentation rep = InformationRepresentation.of(
                    ContainerMetadata.empty(),
                    ContentMetadata.empty(),
                    content);

            String repString = rep.toString();
            assertThat(repString).doesNotContain("John Doe");
            assertThat(repString).doesNotContain("MRN-998877");
        }

        @Test
        @DisplayName("Summary projection precisely excludes Content while preserving contentReference")
        void testSummaryProjectionSemantics() {
            String externalRef = "urn:blob:archive-2026";
            InformationUnit fullUnit = InformationUnit.builder()
                    .randomIdentifier()
                    .containerMetadata(ContainerMetadata.builder().type(ContainerMetadata.TYPE_FHIR_BUNDLE).build())
                    .contentMetadata(ContentMetadata.builder()
                            .contentStructureTopic(Topic.fromName("Health.FHIR.R5.Patient"))
                            .contentReference(externalRef)
                            .build())
                    .content("Confidential Medical Text")
                    .build();

            assertThat(fullUnit.isSummary()).isFalse();
            assertThat(fullUnit.hasContent()).isTrue();

            // Project to summary
            InformationUnit summaryUnit = fullUnit.toSummary();

            assertThat(summaryUnit.isSummary()).isTrue();
            assertThat(summaryUnit.hasContent()).isFalse();
            assertThat(summaryUnit.content()).isEmpty();
            assertThat(summaryUnit.contentAsString()).isEmpty();

            // Preserves container and content metadata including contentReference
            assertThat(summaryUnit.containerMetadata().type()).isEqualTo(ContainerMetadata.TYPE_FHIR_BUNDLE);
            assertThat(summaryUnit.contentMetadata().contentStructureTopic()).contains(Topic.fromName("Health.FHIR.R5.Patient"));
            assertThat(summaryUnit.contentMetadata().contentReference()).contains(externalRef);
            assertThat(summaryUnit.contentMetadata().reference()).contains(externalRef);
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
            Topic gatewayTopic = Topic.fromName("Harmonia.Gateway.Primary.Inbound");
            Topic originTopic = Topic.fromName("Hospital.Canberra.Emergency");
            Topic containerTopic = ContainerMetadata.TYPE_FHIR_BUNDLE;
            Topic contentTopic = Topic.fromName("Health.FHIR.R5.Observation");

            InformationUnit unit = InformationUnit.builder()
                    .randomIdentifier()
                    .version("1.0.0")
                    .origin(originTopic)
                    .coordinates(InformationCoordinates.builder().persistenceGateway(gatewayTopic).build())
                    .containerMetadata(ContainerMetadata.builder().type(containerTopic).build())
                    .contentMetadata(ContentMetadata.builder().contentStructureTopic(contentTopic).build())
                    .content("Observation Data")
                    .build();

            // Matching composite mask
            InformationUnitMask matchingMask = InformationUnitMask.builder()
                    .identityMask(TopicMask.fromPattern("harmonia.mneme.iu.*"))
                    .originMask(TopicMask.fromPattern("Hospital.Canberra.*"))
                    .containerTypeMask(TopicMask.nameBased(containerTopic))
                    .contentStructureMask(TopicMask.fromPattern("Health.FHIR.*"))
                    .persistenceGatewayMask(TopicMask.nameBased(gatewayTopic))
                    .version("1.0.0")
                    .build();

            assertThat(matchingMask.matches(unit)).isTrue();

            // Mask with conflicting container type
            InformationUnitMask nonMatchingContainer = InformationUnitMask.builder()
                    .containerTypeMask(TopicMask.nameBased(ContainerMetadata.TYPE_HL7_MESSAGE))
                    .build();
            assertThat(nonMatchingContainer.matches(unit)).isFalse();

            // Mask with conflicting origin
            InformationUnitMask nonMatchingOrigin = InformationUnitMask.builder()
                    .originMask(TopicMask.fromPattern("Hospital.Sydney.*"))
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
            assertThatThrownBy(() -> new InformationUnitRelationship(InformationUnitRelationship.TYPE_RELATES_TO, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("targetIdentifier must not be null or blank");

            assertThatThrownBy(() -> new InformationUnitRelationship(InformationUnitRelationship.TYPE_RELATES_TO, "   "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("targetIdentifier must not be null or blank");
        }

        @Test
        @DisplayName("InformationUnit requires non-null identifier, metadata, and representation")
        void testInformationUnitInvariantEnforcement() {
            assertThatThrownBy(() -> new InformationUnit(null, InformationMetadata.empty(), InformationRepresentation.builder().buildSummary()))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("identifier must not be null");
        }
    }
}
