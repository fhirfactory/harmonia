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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link InformationUnit}.
 * Verifies construction, invariant enforcement, core accessors, delegation, and immutable derivations with strong Topic typing.
 */
class InformationUnitTest {

    @Test
    @DisplayName("Should create complete InformationUnit using builder with strongly typed Topic references")
    void testCompleteInformationUnitCreation() {
        UUID uuid = UUID.fromString("11111111-2222-3333-4444-555555555555");
        Instant now = Instant.now();

        Topic gwTopic = Topic.fromName("Health.Gateway.Postgres");
        Topic epTopic = Topic.fromName("Health.Endpoint.Rest");
        Topic locTopic = Topic.fromName("Health.Location.HospitalA");
        Topic ownerTopic = Topic.fromName("Health.Owner.HealthSystem");

        Topic srcEntry = Topic.fromName("Health.Component.PylaiIn");
        Topic creationComp = Topic.fromName("Health.Component.IngressErgon");
        Topic prevComp = Topic.fromName("Health.Component.FilterErgon");
        Topic nextComp = Topic.fromName("Health.Component.TransformErgon");
        Topic targetExit = Topic.fromName("Health.Component.PylaiOut");
        Topic partComp = Topic.fromName("Health.Component.AuditErgon");

        Topic contentTopic = Topic.fromName("Health.FHIR.R5.Patient");

        InformationUnit unit = InformationUnit.builder()
                .identifier(uuid)
                .version("1")
                .managementContext(ManagementContext.builder()
                        .operationalVersion("10")
                        .activeGeneration("gen-alpha")
                        .cachedAt(now)
                        .transientProperty("node", "mneme-01")
                        .build())
                .informationCoordinates(InformationCoordinates.builder()
                        .persistenceGateway(gwTopic)
                        .gatewayEndpoint(epTopic)
                        .informationLocation(locTopic)
                        .informationOwner(ownerTopic)
                        .build())
                .informationItinerary(InformationItinerary.builder()
                        .sourceEntryComponent(srcEntry)
                        .creationComponent(creationComp)
                        .previousComponent(prevComp)
                        .nextComponent(nextComp)
                        .targetExitComponent(targetExit)
                        .addParticipatingComponent(partComp)
                        .build())
                .securityContext(InformationSecurityContext.builder()
                        .addActionMask("READ")
                        .confidentiality(FhirConfidentialityEnum.N)
                        .build())
                .lifecycleContext(LifecycleContext.builder()
                        .lifecycleStage("COMMITTED")
                        .commitOutcome(AuthoritativeCommitOutcome.COMMITTED)
                        .retained(true)
                        .build())
                .addRelationship(InformationUnitRelationship.local("basedOn", "target-iu-99"))
                .containerMetadata(ContainerMetadata.builder().containerType("FHIR_BUNDLE").build())
                .contentMetadata(ContentMetadata.builder()
                        .contentStructureTopic(contentTopic)
                        .contentStructureVersion("R5")
                        .contentFormat("application/fhir+json")
                        .build())
                .content("{\"resourceType\":\"Patient\",\"id\":\"p-1\"}")
                .build();

        // Identity
        assertThat(unit.uuid()).isEqualTo(uuid);
        assertThat(unit.version()).isEqualTo("1");
        assertThat(unit.topicName()).isEqualTo("harmonia.mneme.iu.11111111222233334444555555555555");
        assertThat(unit.toTopic().getDataElement()).isEqualTo("11111111222233334444555555555555");

        // Metadata Delegation & Strongly Typed Coordinates
        assertThat(unit.managementContext().operationalVersion()).contains(AuthoritativeVersion.of("10"));
        assertThat(unit.managementContext().activeGeneration()).contains("gen-alpha");
        assertThat(unit.informationCoordinates().persistenceGateway()).contains(gwTopic);
        assertThat(unit.informationCoordinates().gatewayEndpoint()).contains(epTopic);
        assertThat(unit.informationCoordinates().informationLocation()).contains(locTopic);
        assertThat(unit.informationCoordinates().informationOwner()).contains(ownerTopic);

        // Itinerary Strongly Typed Topic References
        assertThat(unit.informationItinerary().sourceEntryComponent()).contains(srcEntry);
        assertThat(unit.informationItinerary().creationComponent()).contains(creationComp);
        assertThat(unit.informationItinerary().previousComponent()).contains(prevComp);
        assertThat(unit.informationItinerary().nextComponent()).contains(nextComp);
        assertThat(unit.informationItinerary().targetExitComponent()).contains(targetExit);
        assertThat(unit.informationItinerary().participatingComponents()).containsExactly(partComp);

        assertThat(unit.securityContext().actionMasks()).containsExactly("READ");
        assertThat(unit.lifecycleContext().lifecycleStage()).contains("COMMITTED");
        assertThat(unit.relationships()).hasSize(1);
        assertThat(unit.relationships().get(0).relationshipType()).isEqualTo("basedOn");

        // Representation & Content Delegation
        assertThat(unit.containerMetadata().containerType()).contains("FHIR_BUNDLE");
        assertThat(unit.contentMetadata().contentStructureTopic()).contains(contentTopic);
        assertThat(unit.contentMetadata().contentStructureVersion()).contains("R5");
        assertThat(unit.isSummary()).isFalse();
        assertThat(unit.hasContent()).isTrue();
        assertThat(unit.contentAsString()).contains("{\"resourceType\":\"Patient\",\"id\":\"p-1\"}");

        // Invariant Validation
        unit.validate();
    }

    @Test
    @DisplayName("Should enforce core invariants during validation")
    void testCoreInvariants() {
        InformationUnitIdentifier id = InformationUnitIdentifier.random();

        // Null identifier throws NullPointerException on constructor
        assertThatThrownBy(() -> new InformationUnit(null, InformationUnitMetadata.empty(), InformationRepresentation.builder().buildSummary()))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("identifier must not be null");

        // Exactly one representation invariant
        InformationUnit unitWithNullRep = new InformationUnit(id, InformationUnitMetadata.empty(), null);
        // Defaults to valid Summary representation
        assertThat(unitWithNullRep.representation()).isNotNull();
        assertThat(unitWithNullRep.isSummary()).isTrue();
        unitWithNullRep.validate();
    }

    @Test
    @DisplayName("Should support immutable with-mutators for all contexts")
    void testImmutableWithMutators() {
        InformationUnit initial = InformationUnit.builder()
                .randomIdentifier()
                .buildSummary();

        assertThat(initial.isSummary()).isTrue();
        assertThat(initial.managementContext().operationalVersion()).isEmpty();

        // Mutate management context
        InformationUnit withMgmt = initial.withManagementContext(
                ManagementContext.builder().operationalVersion("1").build());
        assertThat(withMgmt.managementContext().operationalVersion()).contains(AuthoritativeVersion.of("1"));
        assertThat(initial.managementContext().operationalVersion()).isEmpty();

        // Mutate coordinates
        Topic locTopic = Topic.fromName("Health.Location.Loc1");
        InformationUnit withCoords = withMgmt.withInformationCoordinates(
                InformationCoordinates.builder().informationLocation(locTopic).build());
        assertThat(withCoords.informationCoordinates().informationLocation()).contains(locTopic);

        // Mutate itinerary
        Topic stepTopic = Topic.fromName("Health.Component.Step1");
        InformationUnit withItinerary = withCoords.withInformationItinerary(
                InformationItinerary.builder().sourceEntryComponent(stepTopic).build());
        assertThat(withItinerary.informationItinerary().sourceEntryComponent()).contains(stepTopic);

        // Mutate security
        InformationUnit withSec = withItinerary.withSecurityContext(
                InformationSecurityContext.builder().addActionMask("WRITE").build());
        assertThat(withSec.securityContext().actionMasks()).containsExactly("WRITE");

        // Mutate lifecycle
        InformationUnit withLife = withSec.withLifecycleContext(
                LifecycleContext.builder().lifecycleStage("ACCEPTED").build());
        assertThat(withLife.lifecycleContext().lifecycleStage()).contains("ACCEPTED");

        // Mutate relationship
        InformationUnit withRel = withLife.withRelationship(
                InformationUnitRelationship.local("relatesTo", "target-01"));
        assertThat(withRel.relationships()).hasSize(1);

        // Mutate container metadata
        InformationUnit withCont = withRel.withContainerMetadata(
                ContainerMetadata.builder().containerType("DOCUMENT").build());
        assertThat(withCont.containerMetadata().containerType()).contains("DOCUMENT");

        // Mutate content metadata
        Topic contentTopic = Topic.fromName("Health.Topic.X");
        InformationUnit withContentMeta = withCont.withContentMetadata(
                ContentMetadata.builder().contentStructureTopic(contentTopic).build());
        assertThat(withContentMeta.contentMetadata().contentStructureTopic()).contains(contentTopic);

        // Hydrate with content
        InformationUnit hydrated = withContentMeta.withContent("Hello World");
        assertThat(hydrated.isSummary()).isFalse();
        assertThat(hydrated.hasContent()).isTrue();
        assertThat(hydrated.contentAsString()).contains("Hello World");
    }

    @Test
    @DisplayName("Should support equals and hashCode consistency")
    void testEqualsAndHashCode() {
        UUID uuid = UUID.randomUUID();
        InformationUnit u1 = InformationUnit.builder().identifier(uuid).content("Test").build();
        InformationUnit u2 = InformationUnit.builder().identifier(uuid).content("Test").build();
        InformationUnit u3 = InformationUnit.builder().identifier(UUID.randomUUID()).content("Test").build();

        assertThat(u1).isEqualTo(u2);
        assertThat(u1.hashCode()).isEqualTo(u2.hashCode());
        assertThat(u1).isNotEqualTo(u3);
    }
}
