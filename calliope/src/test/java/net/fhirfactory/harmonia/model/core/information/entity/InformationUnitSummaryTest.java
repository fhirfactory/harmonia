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
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationItinerary;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationRepresentation;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationSecurityContext;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationUnitIdentifier;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationUnitRelationship;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.LifecycleContext;
import net.fhirfactory.harmonia.model.core.information.entity.factory.ContainerMetadataBuilder;
import net.fhirfactory.harmonia.model.core.information.operations.ManagementContext;
import net.fhirfactory.harmonia.model.core.information.operations.OperationalRevision;
import net.fhirfactory.harmonia.model.core.information.operations.factory.ManagementContextBuilder;
import net.fhirfactory.harmonia.model.core.security.valuesets.FhirConfidentialityEnum;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.naming.TopicMask;
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

        Topic gwTopic = TopicFactory.fromName("Health.Persistence.Gateway.Default");
        Topic epTopic = TopicFactory.fromName("Health.Persistence.Endpoint.Rest");
        Topic locTopic = TopicFactory.fromName("Health.Location.HospitalNorth");
        Topic ownerTopic = TopicFactory.fromName("Health.Owner.HealthSystem");

        Topic srcEntry = TopicFactory.fromName("Health.Gateway.MllpIn");
        Topic creationComp = TopicFactory.fromName("Health.Ergon.Ingress");

        Topic contentTopic = TopicFactory.fromName("Health.FHIR.R5.Bundle");
        TopicMask compMask = TopicMaskFactory.fromNamePattern("*.*.InformationCompartment.Clinical*");

        ManagementContext managementContext = new ManagementContextBuilder()
                .operationalRevision(OperationalRevision.of("42"))
                .activeGeneration("gen-007")
                .cachedAt(now)
                .transientProperty("clusterNode", "mneme-east-1")
                .build();
        InformationItinerary itinerary = InformationItinerary.builder()
                        .sourceEntryComponent(srcEntry)
                        .creationComponent(creationComp)
                        .build();
        InformationSecurityContext securityContext = InformationSecurityContext.builder()
                        .addActionMask("READ")
                        .addCompartmentMask(compMask)
                        .confidentiality(FhirConfidentialityEnum.R)
                        .build();
        LifecycleContext lifecycleContext = LifecycleContext.builder()
                        .lifecycleStage("COMMITTED")
                        .commitOutcome(AuthoritativeCommitOutcome.COMMITTED)
                        .persistenceTimestamp(now)
                        .retained(true)
                        .build();
        InformationMetadata metadata = InformationMetadata.builder()
                .coordinates(InformationCoordinates.of(gwTopic, epTopic, locTopic, ownerTopic))
                .itinerary(itinerary)
                .securityContext(securityContext)
                .lifecycleContext(lifecycleContext)
                .relationship(InformationUnitRelationship.local("basedOn", "iu-prior-version"))
                .build();
        ContainerMetadata containerMetadata = new ContainerMetadataBuilder()
                .type(InformationUnitValueSets.TYPE_FHIR_BUNDLE)
                .sourceTopic(TopicFactory.fromName("Harmonia.Gateway.Pas"))
                .destinationTopic(TopicFactory.fromName("Harmonia.Service.Mneme.Store"))
                .authorTopic(TopicFactory.fromName("Harmonia.Author.Clinical"))
                .assemblyContext("DischargeSummary")
                .build();
        ContentMetadata contentMetadata = ContentMetadata.builder()
                        .contentStructureTopic(contentTopic)
                        .contentStructureVersion("R5")
                        .contentFormat("application/fhir+json")
                        .contentReference("s3://harmonia-authoritative/bundles/b-9988.json")
                        .contentDigest("sha256-1122334455667788")
                        .contentSize(8192L)
                        .putAttribute("profile", "http://hl7.org/fhir/StructureDefinition/Bundle")
                        .build();
        InformationRepresentation representation = new InformationRepresentation(containerMetadata, contentMetadata,
                Content.text("{\"resourceType\":\"Bundle\",\"id\":\"b-9988\",\"entry\":[]}"));
        InformationUnit fullUnit = new InformationUnit(InformationUnitIdentifier.fromUuid(uuid), "1", metadata,
                managementContext, representation);

        // Perform Summary projection
        InformationUnit summary = fullUnit.toSummary();

        // 1. Content is excluded
        assertThat(summary.isSummary()).isTrue();
        assertThat(summary.representation().hasContent()).isFalse();
        assertThat(summary.representation().content().isEmpty()).isTrue();
        assertThat(summary.representation().content().textContent()).isEmpty();
        assertThat(summary.representation().content().binaryContent()).isEmpty();

        // 2. Identity is preserved
        assertThat(summary.identifier()).isEqualTo(fullUnit.identifier());
        assertThat(summary.identifier().uuid()).isEqualTo(uuid);
        assertThat(summary.identifier().topicName()).isEqualTo(fullUnit.identifier().topicName());

        // 3. Metadata contexts and relationships are completely preserved
        assertThat(summary.managementContext().operationalRevision()).contains(OperationalRevision.of("42"));
        assertThat(summary.managementContext().activeGeneration()).contains("gen-007");

        assertThat(summary.metadata().coordinates().persistenceGateway()).contains(gwTopic);
        assertThat(summary.metadata().coordinates().informationOwner()).contains(ownerTopic);

        assertThat(summary.metadata().itinerary().sourceEntryComponent()).contains(srcEntry);
        assertThat(summary.metadata().itinerary().creationComponent()).contains(creationComp);

        assertThat(summary.metadata().securityContext().actionMasks()).containsExactly("READ");
        assertThat(summary.metadata().securityContext().compartmentMasks()).containsExactly(compMask);
        assertThat(summary.metadata().securityContext().confidentiality()).contains(FhirConfidentialityEnum.R);

        assertThat(summary.metadata().lifecycleContext().lifecycleStage()).contains("COMMITTED");
        assertThat(summary.metadata().lifecycleContext().isRetained()).isTrue();

        assertThat(summary.metadata().relationships()).hasSize(1);
        assertThat(summary.metadata().relationships().get(0).targetIdentifier()).isEqualTo("iu-prior-version");

        // 4. ContainerMetadata and ContentMetadata (including contentReference) are completely preserved
        assertThat(summary.representation().containerMetadata().type()).isEqualTo(InformationUnitValueSets.TYPE_FHIR_BUNDLE);
        assertThat(summary.representation().containerMetadata().authorTopic()).contains(TopicFactory.fromName("Harmonia.Author.Clinical"));

        assertThat(summary.representation().contentMetadata().contentStructureTopic()).contains(contentTopic);
        assertThat(summary.representation().contentMetadata().contentStructureVersion()).contains("R5");
        assertThat(summary.representation().contentMetadata().contentFormat()).contains("application/fhir+json");
        assertThat(summary.representation().contentMetadata().contentReference()).contains("s3://harmonia-authoritative/bundles/b-9988.json");
        assertThat(summary.representation().contentMetadata().contentDigest()).contains("sha256-1122334455667788");
        assertThat(summary.representation().contentMetadata().contentSize()).contains(8192L);
        assertThat(summary.representation().contentMetadata().attribute("profile")).contains("http://hl7.org/fhir/StructureDefinition/Bundle");

        // 5. Projection idempotency
        InformationUnit summaryOfSummary = summary.toSummary();
        assertThat(summaryOfSummary).isSameAs(summary);

        // 6. Rehydration from summary
        InformationUnit rehydrated = summary.withRepresentation(summary.representation().withContent(
                Content.text("{\"resourceType\":\"Bundle\",\"id\":\"b-9988\",\"entry\":[]}")));
        assertThat(rehydrated.isSummary()).isFalse();
        assertThat(rehydrated.representation().hasContent()).isTrue();
        assertThat(rehydrated.representation().content().textContent().orElse("")).contains("{\"resourceType\":\"Bundle\"");
        assertThat(rehydrated).isEqualTo(fullUnit);
    }

    @Test
    @DisplayName("Summary Registration: should support GovernedSummaryWriter(CREATE) registration of external content reference")
    void testSummaryWriterCreateExternalRegistration() {
        UUID uuid = UUID.randomUUID();

        Topic gwTopic = TopicFactory.fromName("Health.Gateway.ExternalPacs");
        Topic epTopic = TopicFactory.fromName("Health.Endpoint.DicomWeb");
        Topic locTopic = TopicFactory.fromName("Health.Location.RadiologyArchive");
        Topic ownerTopic = TopicFactory.fromName("Health.Owner.DiagnosticImaging");
        Topic contentTopic = TopicFactory.fromName("Health.DICOM.Study.CT");

        InformationMetadata metadata = InformationMetadata.builder()
                .coordinates(InformationCoordinates.of(gwTopic, epTopic, locTopic, ownerTopic))
                .lifecycleContext(LifecycleContext.builder()
                        .lifecycleStage("COMMITTED")
                        .commitOutcome(AuthoritativeCommitOutcome.COMMITTED)
                        .build())
                .build();
        ContainerMetadata containerMetadata = new ContainerMetadataBuilder()
                .type(TopicFactory.fromName("Harmonia.Container.DICOM.Study"))
                .sourceTopic(TopicFactory.fromName("Harmonia.Gateway.RIS.PACS"))
                .build();
        ContentMetadata contentMetadata = ContentMetadata.builder()
                        .contentStructureTopic(contentTopic)
                        .contentStructureVersion("2024c")
                        .contentFormat("application/dicom")
                        .contentReference("wado-rs://pacs.imaging.org/studies/1.2.840.113619.2.55.3.123456")
                        .build();
        InformationRepresentation representation = new InformationRepresentation(
                containerMetadata, contentMetadata, Content.empty());
        InformationUnit externalRegisteredSummary = new InformationUnit(
                InformationUnitIdentifier.fromUuid(uuid), null, metadata, null, representation);

        assertThat(externalRegisteredSummary.isSummary()).isTrue();
        assertThat(externalRegisteredSummary.representation().hasContent()).isFalse();
        assertThat(externalRegisteredSummary.representation().contentMetadata().contentReference())
                .contains("wado-rs://pacs.imaging.org/studies/1.2.840.113619.2.55.3.123456");
        assertThat(externalRegisteredSummary.representation().contentMetadata().hasContentReference()).isTrue();
    }
}
