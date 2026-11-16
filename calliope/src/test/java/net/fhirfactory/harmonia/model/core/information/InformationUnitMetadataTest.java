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
import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.naming.TopicMask;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for the Information Unit metadata contexts and relationships:
 * {@link ManagementContext}, {@link InformationCoordinates}, {@link InformationItinerary},
 * {@link InformationSecurityContext}, {@link LifecycleContext}, {@link InformationUnitRelationship}, and {@link InformationUnitMetadata}.
 */
class InformationUnitMetadataTest {

    // =========================================================================
    // ManagementContext Tests
    // =========================================================================

    @Test
    @DisplayName("ManagementContext: should construct with operational version, generation, timestamp, and transient properties")
    void testManagementContextConstruction() {
        AuthoritativeVersion version = AuthoritativeVersion.of(5L);
        Instant now = Instant.now();
        Map<String, String> props = Map.of("cacheNode", "node-1", "sessionId", "sess-99");

        ManagementContext context = new ManagementContext(version, "gen-001", now, props);

        assertThat(context.operationalVersion()).contains(version);
        assertThat(context.getOperationalVersion()).isEqualTo(version);
        assertThat(context.activeGeneration()).contains("gen-001");
        assertThat(context.getActiveGeneration()).isEqualTo("gen-001");
        assertThat(context.cachedAt()).contains(now);
        assertThat(context.getCachedAt()).isEqualTo(now);
        assertThat(context.transientProperties()).containsEntry("cacheNode", "node-1");
        assertThat(context.getTransientProperty("sessionId")).contains("sess-99");
        assertThat(context.getTransientProperty("unknown")).isEmpty();
    }

    @Test
    @DisplayName("ManagementContext: should enforce immutability on transient properties")
    void testManagementContextImmutability() {
        Map<String, String> mutableMap = new HashMap<>();
        mutableMap.put("k1", "v1");

        ManagementContext context = new ManagementContext(null, null, null, mutableMap);
        mutableMap.put("k2", "v2");

        assertThat(context.transientProperties()).hasSize(1);
        assertThatThrownBy(() -> context.transientProperties().put("k3", "v3"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("ManagementContext: builder and with-mutators should derive updated copies safely")
    void testManagementContextWithMutatorsAndBuilder() {
        ManagementContext initial = ManagementContext.empty();
        assertThat(initial.operationalVersion()).isEmpty();
        assertThat(initial.activeGeneration()).isEmpty();

        AuthoritativeVersion v1 = AuthoritativeVersion.of("1");
        ManagementContext updated = initial
                .withOperationalVersion(v1)
                .withActiveGeneration("gen-alpha")
                .withCachedAt(Instant.EPOCH)
                .withTransientProperty("tempKey", "tempVal");

        assertThat(updated.operationalVersion()).contains(v1);
        assertThat(updated.activeGeneration()).contains("gen-alpha");
        assertThat(updated.cachedAt()).contains(Instant.EPOCH);
        assertThat(updated.getTransientProperty("tempKey")).contains("tempVal");

        // Builder
        ManagementContext built = ManagementContext.builder()
                .operationalVersion("42")
                .activeGeneration("gen-beta")
                .cachedAt(Instant.EPOCH)
                .transientProperty("bKey", "bVal")
                .build();

        assertThat(built.operationalVersion()).contains(AuthoritativeVersion.of("42"));
        assertThat(built.activeGeneration()).contains("gen-beta");
        assertThat(built.getTransientProperty("bKey")).contains("bVal");
    }

    // =========================================================================
    // InformationCoordinates Tests (Strongly Typed Topics)
    // =========================================================================

    @Test
    @DisplayName("InformationCoordinates: should construct with four strongly typed Topic references and support accessors")
    void testInformationCoordinatesConstruction() {
        Topic gw = Topic.fromName("Health.Persistence.Gateway.Default");
        Topic ep = Topic.fromName("Health.Persistence.Endpoint.Rest");
        Topic loc = Topic.fromName("Health.Location.HospitalNorth");
        Topic owner = Topic.fromName("Health.Owner.HealthSystem");

        InformationCoordinates coords = InformationCoordinates.of(gw, ep, loc, owner);

        assertThat(coords.persistenceGateway()).contains(gw);
        assertThat(coords.gatewayEndpoint()).contains(ep);
        assertThat(coords.informationLocation()).contains(loc);
        assertThat(coords.informationOwner()).contains(owner);

        // Name helpers
        assertThat(coords.persistenceGatewayName()).contains("Health.Persistence.Gateway.Default");
        assertThat(coords.gatewayEndpointName()).contains("Health.Persistence.Endpoint.Rest");
        assertThat(coords.informationLocationName()).contains("Health.Location.HospitalNorth");
        assertThat(coords.informationOwnerName()).contains("Health.Owner.HealthSystem");

        // Builder
        InformationCoordinates built = InformationCoordinates.builder()
                .persistenceGateway(gw)
                .informationOwner(owner)
                .build();

        assertThat(built.persistenceGateway()).contains(gw);
        assertThat(built.gatewayEndpoint()).isEmpty();
        assertThat(built.informationLocation()).isEmpty();
        assertThat(built.informationOwner()).contains(owner);
    }

    // =========================================================================
    // InformationSecurityContext Tests (Strongly Typed Topics)
    // =========================================================================

    @Test
    @DisplayName("InformationSecurityContext: should express required criteria and compartments with Topics and TopicMasks")
    void testInformationSecurityContext() {
        Topic unitTopic = Topic.fromName("Health.Security.PHI");
        Topic contTopic = Topic.fromName("Health.Security.Container.Egress");
        Topic contentTopic = Topic.fromName("Health.Security.Content.Sensitive");
        TopicMask compMask = TopicMask.fromPattern("Health.Compartment.Oncology.*");

        InformationSecurityContext sec = InformationSecurityContext.builder()
                .addUnitCriterion(unitTopic)
                .addContainerCriterion(contTopic)
                .addContentCriterion(contentTopic)
                .addActionMask("READ")
                .addActionMask("EXPORT")
                .addCompartmentMask(compMask)
                .addSecurityLabel(HarmoniaSecurityLabelEnum.RESTRICTED)
                .confidentiality(FhirConfidentialityEnum.R)
                .putAttribute("legalHold", "true")
                .build();

        assertThat(sec.unitCriteria()).containsExactly(unitTopic);
        assertThat(sec.containerCriteria()).containsExactly(contTopic);
        assertThat(sec.contentCriteria()).containsExactly(contentTopic);
        assertThat(sec.actionMasks()).containsExactly("READ", "EXPORT");
        assertThat(sec.compartmentMasks()).containsExactly(compMask);
        assertThat(sec.securityLabels()).containsExactly(HarmoniaSecurityLabelEnum.RESTRICTED);
        assertThat(sec.confidentiality()).contains(FhirConfidentialityEnum.R);
        assertThat(sec.attribute("legalHold")).contains("true");
    }

    // =========================================================================
    // LifecycleContext Tests
    // =========================================================================

    @Test
    @DisplayName("LifecycleContext: should coordinate lifecycle stages and retention hold")
    void testLifecycleContext() {
        Instant now = Instant.now();
        LifecycleContext context = LifecycleContext.builder()
                .lifecycleStage("COMMITTED")
                .persistenceTimestamp(now)
                .commitOutcome(AuthoritativeCommitOutcome.COMMITTED)
                .retained(true)
                .archived(false)
                .putCoordinationAttribute("leaseId", "L-9988")
                .build();

        assertThat(context.lifecycleStage()).contains("COMMITTED");
        assertThat(context.persistenceTimestamp()).contains(now);
        assertThat(context.commitOutcome()).contains(AuthoritativeCommitOutcome.COMMITTED);
        assertThat(context.isRetained()).isTrue();
        assertThat(context.isArchived()).isFalse();
        assertThat(context.coordinationAttribute("leaseId")).contains("L-9988");
    }

    // =========================================================================
    // InformationUnitRelationship Tests
    // =========================================================================

    @Test
    @DisplayName("InformationUnitRelationship: should support local and remote target references")
    void testRelationships() {
        InformationUnitRelationship localRel = InformationUnitRelationship.local("basedOn", "iu-001");
        assertThat(localRel.isLocal()).isTrue();
        assertThat(localRel.isRemote()).isFalse();
        assertThat(localRel.targetIdentifier()).isEqualTo("iu-001");

        InformationUnitRelationship remoteRel = InformationUnitRelationship.remote("derivedFrom", "http://external.hie/iu/ext-99");
        assertThat(remoteRel.isRemote()).isTrue();
        assertThat(remoteRel.isLocal()).isFalse();
        assertThat(remoteRel.targetIdentifier()).isEqualTo("http://external.hie/iu/ext-99");

        // Null relationshipType or targetIdentifier rejected
        assertThatThrownBy(() -> new InformationUnitRelationship(null, "target"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new InformationUnitRelationship("rel", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // =========================================================================
    // Composite InformationUnitMetadata Tests
    // =========================================================================

    @Test
    @DisplayName("InformationUnitMetadata: should compose all contexts and enforce immutability")
    void testInformationUnitMetadataComposition() {
        ManagementContext mgmt = ManagementContext.builder().operationalVersion("1").build();
        Topic gw = Topic.fromName("Health.Gateway.A");
        InformationCoordinates coords = InformationCoordinates.builder().persistenceGateway(gw).build();
        Topic src = Topic.fromName("Health.Component.In");
        InformationItinerary itinerary = InformationItinerary.builder().sourceEntryComponent(src).build();
        InformationSecurityContext sec = InformationSecurityContext.builder().addActionMask("READ").build();
        LifecycleContext life = LifecycleContext.builder().lifecycleStage("ACCEPTED").build();
        InformationUnitRelationship rel = InformationUnitRelationship.local("relatesTo", "iu-002");

        InformationUnitMetadata metadata = new InformationUnitMetadata(mgmt, coords, itinerary, sec, life, List.of(rel));

        assertThat(metadata.managementContext()).isEqualTo(mgmt);
        assertThat(metadata.informationCoordinates()).isEqualTo(coords);
        assertThat(metadata.informationItinerary()).isEqualTo(itinerary);
        assertThat(metadata.securityContext()).isEqualTo(sec);
        assertThat(metadata.lifecycleContext()).isEqualTo(life);
        assertThat(metadata.relationships()).containsExactly(rel);

        // Immutability on relationships
        assertThatThrownBy(() -> metadata.relationships().add(InformationUnitRelationship.local("x", "y")))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
