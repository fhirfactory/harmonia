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

import net.fhirfactory.harmonia.model.core.naming.factory.TopicFactory;

import net.fhirfactory.harmonia.model.core.information.access.AuthoritativeCommitOutcome;
import net.fhirfactory.harmonia.model.core.information.operations.ManagementContext;
import net.fhirfactory.harmonia.model.core.information.operations.OperationalRevision;
import net.fhirfactory.harmonia.model.core.information.operations.factory.ManagementContextBuilder;
import net.fhirfactory.harmonia.model.core.security.valuesets.FhirConfidentialityEnum;
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
import net.fhirfactory.harmonia.model.core.naming.Topic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Verifies InformationUnit construction, independent metadata concerns, immutable derivation and diagnostics.
 */
class InformationUnitTest {

    @Test
    @DisplayName("Constructs an IU from its identity, metadata, operational view and singular representation")
    void constructsCompleteInformationUnitFromTypedConstituents() {
        UUID uuid = UUID.fromString("11111111-2222-3333-4444-555555555555");
        Instant now = Instant.now();

        Topic gwTopic = TopicFactory.fromName("Health.Gateway.Postgres");
        Topic epTopic = TopicFactory.fromName("Health.Endpoint.Rest");
        Topic locTopic = TopicFactory.fromName("Health.Location.HospitalA");
        Topic ownerTopic = TopicFactory.fromName("Health.Owner.HealthSystem");

        Topic srcEntry = TopicFactory.fromName("Health.Component.PylaiIn");
        Topic creationComp = TopicFactory.fromName("Health.Component.IngressErgon");
        Topic prevComp = TopicFactory.fromName("Health.Component.FilterErgon");
        Topic nextComp = TopicFactory.fromName("Health.Component.TransformErgon");
        Topic targetExit = TopicFactory.fromName("Health.Component.PylaiOut");
        Topic partComp = TopicFactory.fromName("Health.Component.AuditErgon");

        Topic contentTopic = TopicFactory.fromName("Health.FHIR.R5.Patient");

        ManagementContext managementContext = new ManagementContextBuilder()
                .operationalRevision(OperationalRevision.of("10"))
                .activeGeneration("gen-alpha")
                .cachedAt(now)
                .transientProperty("node", "mneme-01")
                .build();
        InformationCoordinates coordinates = InformationCoordinates.builder()
                        .persistenceGateway(gwTopic)
                        .gatewayEndpoint(epTopic)
                        .informationLocation(locTopic)
                        .informationOwner(ownerTopic)
                        .build();
        InformationItinerary itinerary = InformationItinerary.builder()
                        .sourceEntryComponent(srcEntry)
                        .creationComponent(creationComp)
                        .previousComponent(prevComp)
                        .nextComponent(nextComp)
                        .targetExitComponent(targetExit)
                        .addParticipatingComponent(partComp)
                        .build();
        InformationSecurityContext securityContext = InformationSecurityContext.builder()
                        .addActionMask("READ")
                        .confidentiality(FhirConfidentialityEnum.N)
                        .build();
        LifecycleContext lifecycleContext = LifecycleContext.builder()
                        .lifecycleStage("COMMITTED")
                        .commitOutcome(AuthoritativeCommitOutcome.COMMITTED)
                        .retained(true)
                        .build();
        InformationUnitRelationship relationship = InformationUnitRelationship.local("basedOn", "target-iu-99");
        InformationMetadata metadata = InformationMetadata.builder()
                .coordinates(coordinates)
                .itinerary(itinerary)
                .securityContext(securityContext)
                .lifecycleContext(lifecycleContext)
                .relationship(relationship)
                .build();
        ContainerMetadata containerMetadata = new ContainerMetadataBuilder()
                .type(InformationUnitValueSets.TYPE_FHIR_BUNDLE)
                .build();
        ContentMetadata contentMetadata = ContentMetadata.builder()
                        .contentStructureTopic(contentTopic)
                        .contentStructureVersion("R5")
                        .contentFormat("application/fhir+json")
                        .build();
        InformationRepresentation representation = new InformationRepresentation(
                containerMetadata, contentMetadata, Content.text("{\"resourceType\":\"Patient\",\"id\":\"p-1\"}"));
        InformationUnitIdentifier identifier = InformationUnitIdentifier.fromUuid(uuid);
        InformationUnit unit = new InformationUnit(identifier, "1", metadata, managementContext, representation);

        assertThat(unit.identifier().uuid()).isEqualTo(uuid);
        assertThat(unit.version()).isEqualTo("1");
        assertThat(unit.identifier().topicName()).isEqualTo("harmonia.mneme.iu.11111111222233334444555555555555");

        assertThat(unit.managementContext().operationalRevision()).contains(OperationalRevision.of("10"));
        assertThat(unit.managementContext().activeGeneration()).contains("gen-alpha");
        assertThat(unit.metadata().coordinates().persistenceGateway()).contains(gwTopic);
        assertThat(unit.metadata().coordinates().gatewayEndpoint()).contains(epTopic);
        assertThat(unit.metadata().coordinates().informationLocation()).contains(locTopic);
        assertThat(unit.metadata().coordinates().informationOwner()).contains(ownerTopic);

        assertThat(unit.metadata().itinerary().sourceEntryComponent()).contains(srcEntry);
        assertThat(unit.metadata().itinerary().creationComponent()).contains(creationComp);
        assertThat(unit.metadata().itinerary().previousComponent()).contains(prevComp);
        assertThat(unit.metadata().itinerary().nextComponent()).contains(nextComp);
        assertThat(unit.metadata().itinerary().targetExitComponent()).contains(targetExit);
        assertThat(unit.metadata().itinerary().participatingComponents()).containsExactly(partComp);

        assertThat(unit.metadata().securityContext().actionMasks()).containsExactly("READ");
        assertThat(unit.metadata().lifecycleContext().lifecycleStage()).contains("COMMITTED");
        assertThat(unit.metadata().relationships()).containsExactly(relationship);

        assertThat(unit.representation().containerMetadata().type()).isEqualTo(InformationUnitValueSets.TYPE_FHIR_BUNDLE);
        assertThat(unit.representation().contentMetadata().contentStructureTopic()).contains(contentTopic);
        assertThat(unit.representation().contentMetadata().contentStructureVersion()).contains("R5");
        assertThat(unit.isSummary()).isFalse();
        assertThat(unit.representation().hasContent()).isTrue();
        assertThat(unit.representation().content().textContent()).contains("{\"resourceType\":\"Patient\",\"id\":\"p-1\"}");
    }

    @Test
    @DisplayName("Should enforce core invariants during validation")
    void testCoreInvariants() {
        InformationUnitIdentifier id = InformationUnitIdentifier.random();

        assertThatThrownBy(() -> new InformationUnit(null, null, null, null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("identifier must not be null");

        InformationUnit unitWithNullRep = new InformationUnit(id, null, null, null, null);
        assertThat(unitWithNullRep.representation()).isNotNull();
        assertThat(unitWithNullRep.isSummary()).isTrue();
        assertThat(unitWithNullRep.metadata()).isEqualTo(InformationMetadata.empty());
        assertThat(unitWithNullRep.managementContext()).isEqualTo(ManagementContext.empty());
    }

    @Test
    @DisplayName("Should support immutable with-mutators for all contexts")
    void testImmutableWithMutators() {
        InformationUnitIdentifier identifier = InformationUnitIdentifier.random();
        InformationUnit initial = new InformationUnit(identifier, null, null, null, null);

        assertThat(initial.isSummary()).isTrue();
        assertThat(initial.managementContext().operationalRevision()).isEmpty();

        InformationUnit withMgmt = initial.withManagementContext(
                ManagementContext.builder().operationalRevision(OperationalRevision.of("1")).build());
        assertThat(withMgmt.managementContext().operationalRevision()).contains(OperationalRevision.of("1"));
        assertThat(initial.managementContext().operationalRevision()).isEmpty();

        Topic locTopic = TopicFactory.fromName("Health.Location.Loc1");
        InformationUnit withCoords = withMgmt.withMetadata(withMgmt.metadata().withCoordinates(
                InformationCoordinates.builder().informationLocation(locTopic).build()));
        assertThat(withCoords.metadata().coordinates().informationLocation()).contains(locTopic);

        Topic stepTopic = TopicFactory.fromName("Health.Component.Step1");
        InformationUnit withItinerary = withCoords.withMetadata(withCoords.metadata().withItinerary(
                InformationItinerary.builder().sourceEntryComponent(stepTopic).build()));
        assertThat(withItinerary.metadata().itinerary().sourceEntryComponent()).contains(stepTopic);

        InformationUnit withSec = withItinerary.withMetadata(withItinerary.metadata().withSecurityContext(
                InformationSecurityContext.builder().addActionMask("WRITE").build()));
        assertThat(withSec.metadata().securityContext().actionMasks()).containsExactly("WRITE");

        InformationUnit withLife = withSec.withMetadata(withSec.metadata().withLifecycleContext(
                LifecycleContext.builder().lifecycleStage("ACCEPTED").build()));
        assertThat(withLife.metadata().lifecycleContext().lifecycleStage()).contains("ACCEPTED");

        InformationUnit withRel = withLife.withMetadata(withLife.metadata().withRelationship(
                InformationUnitRelationship.local("relatesTo", "target-01")));
        assertThat(withRel.metadata().relationships()).hasSize(1);

        ContainerMetadata container = new ContainerMetadataBuilder().type(InformationUnitValueSets.TYPE_DOCUMENT).build();
        InformationUnit withCont = withRel.withRepresentation(withRel.representation().withContainerMetadata(container));
        assertThat(withCont.representation().containerMetadata().type()).isEqualTo(InformationUnitValueSets.TYPE_DOCUMENT);

        Topic contentTopic = TopicFactory.fromName("Health.Topic.X");
        InformationUnit withContentMeta = withCont.withRepresentation(withCont.representation().withContentMetadata(
                ContentMetadata.builder().contentStructureTopic(contentTopic).build()));
        assertThat(withContentMeta.representation().contentMetadata().contentStructureTopic()).contains(contentTopic);

        InformationUnit hydrated = withContentMeta.withRepresentation(
                withContentMeta.representation().withContent(Content.text("Hello World")));
        assertThat(hydrated.isSummary()).isFalse();
        assertThat(hydrated.representation().hasContent()).isTrue();
        assertThat(hydrated.representation().content().textContent()).contains("Hello World");
    }

    @Test
    @DisplayName("Should support equals and hashCode consistency")
    void testEqualsAndHashCode() {
        UUID uuid = UUID.randomUUID();
        InformationRepresentation representation = new InformationRepresentation(
                ContainerMetadata.empty(), ContentMetadata.empty(), Content.text("Test"));
        InformationUnit u1 = new InformationUnit(InformationUnitIdentifier.fromUuid(uuid), null, null, null, representation);
        InformationUnit u2 = new InformationUnit(InformationUnitIdentifier.fromUuid(uuid), null, null, null, representation);
        InformationUnit u3 = new InformationUnit(InformationUnitIdentifier.random(), null, null, null, representation);

        assertThat(u1).isEqualTo(u2);
        assertThat(u1.hashCode()).isEqualTo(u2.hashCode());
        assertThat(u1).isNotEqualTo(u3);
    }

    @Test
    @DisplayName("Diagnostics omit represented clinical content")
    void diagnosticsDoNotExposeContent() {
        String secret = "patient-secret-42";
        InformationRepresentation representation = new InformationRepresentation(
                ContainerMetadata.empty(), ContentMetadata.empty(), Content.text(secret));
        InformationUnit unit = new InformationUnit(InformationUnitIdentifier.random(), null, null, null, representation);
        InformationUnitRelationship relationship = new InformationUnitRelationship(
                InformationUnitValueSets.TYPE_RELATES_TO, secret, true, secret);
        InformationSecurityContext securityContext = InformationSecurityContext.builder()
                .addActionMask(secret)
                .build();
        LifecycleContext lifecycleContext = LifecycleContext.builder()
                .lifecycleStage(secret)
                .build();

        assertThat(unit.toString()).doesNotContain(secret);
        assertThat(unit.representation().toString()).doesNotContain(secret);
        assertThat(relationship.toString()).doesNotContain(secret);
        assertThat(securityContext.toString()).doesNotContain(secret);
        assertThat(lifecycleContext.toString()).doesNotContain(secret);
    }
}
