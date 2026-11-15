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
import net.fhirfactory.harmonia.model.topic.Topic;
import net.fhirfactory.harmonia.model.topic.TopicMask;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Focused tests for the Information Unit Summary projection contract with strongly typed Topics.
 */
class InformationUnitSummaryTest {

    @Test
    @DisplayName("Summary Projection: must exclude precisely content while preserving all metadata, strongly typed Topics, and contentReference")
    void testSummaryProjectionFullFidelity() {
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.now();

        Topic gwTopic = Topic.fromName("Health.Persistence.Gateway.Default");
        Topic epTopic = Topic.fromName("Health.Persistence.Endpoint.Rest");
        Topic locTopic = Topic.fromName("Health.Location.HospitalNorth");
        Topic ownerTopic = Topic.fromName("Health.Owner.HealthSystem");

        Topic srcEntry = Topic.fromName("Health.Gateway.MllpIn");
        Topic creationComp = Topic.fromName("Health.Ergon.Ingress");

        Topic contentTopic = Topic.fromName("Health.FHIR.R5.Bundle");
        TopicMask compMask = TopicMask.fromPattern("*.*.InformationCompartment.Clinical*");

        InformationUnit fullUnit = InformationUnit.builder()
                .identifier(uuid)
                .version("1")
                .managementContext(ManagementContext.builder()
                        .operationalVersion("42")
                        .activeGeneration("gen-007")
                        .cachedAt(now)
                        .transientProperty("clusterNode", "mneme-east-1")
                        .build())
                .informationCoordinates(InformationCoordinates.of(
                        gwTopic,
                        epTopic,
                        locTopic,
                        ownerTopic))
                .informationItinerary(InformationItinerary.builder()
                        .sourceEntryComponent(srcEntry)
                        .creationComponent(creationComp)
                        .build())
                .securityContext(InformationSecurityContext.builder()
                        .addActionMask("READ")
                        .addCompartmentMask(compMask)
                        .confidentiality(FhirConfidentialityEnum.R)
                        .build())
                .lifecycleContext(LifecycleContext.builder()
                        .lifecycleStage("COMMITTED")
                        .commitOutcome(AuthoritativeCommitOutcome.COMMITTED)
                        .persistenceTimestamp(now)
                        .retained(true)
                        .build())
                .addRelationship(InformationUnitRelationship.local("basedOn", "iu-prior-version"))
                .containerMetadata(ContainerMetadata.builder()
                        .containerType("FHIR_BUNDLE")
                        .source("pas-gw")
                        .destination("mneme-store")
                        .author("DrSmith")
                        .assemblyContext("DischargeSummary")
                        .build())
                .contentMetadata(ContentMetadata.builder()
                        .contentStructureTopic(contentTopic)
                        .contentStructureVersion("R5")
                        .contentFormat("application/fhir+json")
                        .contentReference("s3://harmonia-authoritative/bundles/b-9988.json")
                        .contentDigest("sha256-1122334455667788")
                        .contentSize(8192L)
                        .putAttribute("profile", "http://hl7.org/fhir/StructureDefinition/Bundle")
                        .build())
                .content("{\"resourceType\":\"Bundle\",\"id\":\"b-9988\",\"entry\":[]}")
                .build();

        // Perform Summary projection
        InformationUnit summary = fullUnit.toSummary();

        // 1. Content is excluded
        assertThat(summary.isSummary()).isTrue();
        assertThat(summary.hasContent()).isFalse();
        assertThat(summary.content()).isEmpty();
        assertThat(summary.contentAsString()).isEmpty();
        assertThat(summary.contentAsBytes()).isEmpty();

        // 2. Identity is preserved
        assertThat(summary.identifier()).isEqualTo(fullUnit.identifier());
        assertThat(summary.uuid()).isEqualTo(uuid);
        assertThat(summary.topicName()).isEqualTo(fullUnit.topicName());

        // 3. Metadata contexts and relationships are completely preserved
        assertThat(summary.managementContext().operationalVersion()).contains(AuthoritativeVersion.of("42"));
        assertThat(summary.managementContext().activeGeneration()).contains("gen-007");

        assertThat(summary.informationCoordinates().persistenceGateway()).contains(gwTopic);
        assertThat(summary.informationCoordinates().informationOwner()).contains(ownerTopic);

        assertThat(summary.informationItinerary().sourceEntryComponent()).contains(srcEntry);
        assertThat(summary.informationItinerary().creationComponent()).contains(creationComp);

        assertThat(summary.securityContext().actionMasks()).containsExactly("READ");
        assertThat(summary.securityContext().compartmentMasks()).containsExactly(compMask);
        assertThat(summary.securityContext().confidentiality()).contains(FhirConfidentialityEnum.R);

        assertThat(summary.lifecycleContext().lifecycleStage()).contains("COMMITTED");
        assertThat(summary.lifecycleContext().isRetained()).isTrue();

        assertThat(summary.relationships()).hasSize(1);
        assertThat(summary.relationships().get(0).targetIdentifier()).isEqualTo("iu-prior-version");

        // 4. ContainerMetadata and ContentMetadata (including contentReference) are completely preserved
        assertThat(summary.containerMetadata().containerType()).contains("FHIR_BUNDLE");
        assertThat(summary.containerMetadata().author()).contains("DrSmith");

        assertThat(summary.contentMetadata().contentStructureTopic()).contains(contentTopic);
        assertThat(summary.contentMetadata().contentStructureVersion()).contains("R5");
        assertThat(summary.contentMetadata().contentFormat()).contains("application/fhir+json");
        assertThat(summary.contentMetadata().contentReference()).contains("s3://harmonia-authoritative/bundles/b-9988.json");
        assertThat(summary.contentMetadata().contentDigest()).contains("sha256-1122334455667788");
        assertThat(summary.contentMetadata().contentSize()).contains(8192L);
        assertThat(summary.contentMetadata().attribute("profile")).contains("http://hl7.org/fhir/StructureDefinition/Bundle");

        // 5. Projection idempotency
        InformationUnit summaryOfSummary = summary.toSummary();
        assertThat(summaryOfSummary).isSameAs(summary);

        // 6. Rehydration from summary
        InformationUnit rehydrated = summary.withContent("{\"resourceType\":\"Bundle\",\"id\":\"b-9988\",\"entry\":[]}");
        assertThat(rehydrated.isSummary()).isFalse();
        assertThat(rehydrated.hasContent()).isTrue();
        assertThat(rehydrated.contentAsString().orElse("")).contains("{\"resourceType\":\"Bundle\"");
        assertThat(rehydrated).isEqualTo(fullUnit);
    }

    @Test
    @DisplayName("Summary Registration: should support GovernedSummaryWriter(CREATE) registration of external content reference")
    void testSummaryWriterCreateExternalRegistration() {
        UUID uuid = UUID.randomUUID();

        Topic gwTopic = Topic.fromName("Health.Gateway.ExternalPacs");
        Topic epTopic = Topic.fromName("Health.Endpoint.DicomWeb");
        Topic locTopic = Topic.fromName("Health.Location.RadiologyArchive");
        Topic ownerTopic = Topic.fromName("Health.Owner.DiagnosticImaging");
        Topic contentTopic = Topic.fromName("Health.DICOM.Study.CT");

        InformationUnit externalRegisteredSummary = InformationUnit.builder()
                .identifier(uuid)
                .informationCoordinates(InformationCoordinates.of(
                        gwTopic,
                        epTopic,
                        locTopic,
                        ownerTopic))
                .lifecycleContext(LifecycleContext.builder()
                        .lifecycleStage("COMMITTED")
                        .commitOutcome(AuthoritativeCommitOutcome.COMMITTED)
                        .build())
                .containerMetadata(ContainerMetadata.builder()
                        .containerType("DICOM_STUDY")
                        .source("ris-pacs-gw")
                        .build())
                .contentMetadata(ContentMetadata.builder()
                        .contentStructureTopic(contentTopic)
                        .contentStructureVersion("2024c")
                        .contentFormat("application/dicom")
                        .contentReference("wado-rs://pacs.imaging.org/studies/1.2.840.113619.2.55.3.123456")
                        .build())
                .buildSummary();

        assertThat(externalRegisteredSummary.isSummary()).isTrue();
        assertThat(externalRegisteredSummary.hasContent()).isFalse();
        assertThat(externalRegisteredSummary.contentMetadata().contentReference())
                .contains("wado-rs://pacs.imaging.org/studies/1.2.840.113619.2.55.3.123456");
        assertThat(externalRegisteredSummary.contentMetadata().hasContentReference()).isTrue();

        externalRegisteredSummary.validate();
    }
}
