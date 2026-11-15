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
 * Tests for the four Information Unit metadata contexts and relationships:
 * {@link ManagementContext}, {@link InformationCoordinates}, {@link InformationSecurityContext},
 * {@link LifecycleContext}, {@link InformationUnitRelationship}, and {@link InformationUnitMetadata}.
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
    // InformationCoordinates Tests
    // =========================================================================

    @Test
    @DisplayName("InformationCoordinates: should hold four distinct named Topic references and preserve independence from security")
    void testInformationCoordinates() {
        InformationCoordinates coords = InformationCoordinates.of(
                "Health.Persistence.Gateway.Default",
                "Health.Persistence.Endpoint.RestFhir",
                "Health.Location.HospitalNorth.Room101",
                "Health.Owner.MetropolitanHealthDistrict");

        assertThat(coords.persistenceGateway()).contains("Health.Persistence.Gateway.Default");
        assertThat(coords.gatewayEndpoint()).contains("Health.Persistence.Endpoint.RestFhir");
        assertThat(coords.informationLocation()).contains("Health.Location.HospitalNorth.Room101");
        assertThat(coords.informationOwner()).contains("Health.Owner.MetropolitanHealthDistrict");

        // With mutators
        InformationCoordinates updated = coords
                .withPersistenceGateway("Health.Persistence.Gateway.Secondary")
                .withGatewayEndpoint("Health.Persistence.Endpoint.DirectDb")
                .withInformationLocation("Health.Location.HospitalSouth")
                .withInformationOwner("Health.Owner.RegionalHealthDistrict");

        assertThat(updated.persistenceGateway()).contains("Health.Persistence.Gateway.Secondary");
        assertThat(updated.gatewayEndpoint()).contains("Health.Persistence.Endpoint.DirectDb");
        assertThat(updated.informationLocation()).contains("Health.Location.HospitalSouth");
        assertThat(updated.informationOwner()).contains("Health.Owner.RegionalHealthDistrict");

        // Trimming of empty / whitespace strings
        InformationCoordinates emptyCoords = new InformationCoordinates("  ", "\t", null, "");
        assertThat(emptyCoords.persistenceGateway()).isEmpty();
        assertThat(emptyCoords.gatewayEndpoint()).isEmpty();
        assertThat(emptyCoords.informationLocation()).isEmpty();
        assertThat(emptyCoords.informationOwner()).isEmpty();
    }

    // =========================================================================
    // InformationSecurityContext Tests
    // =========================================================================

    @Test
    @DisplayName("InformationSecurityContext: should encapsulate IU-side security requirements")
    void testInformationSecurityContext() {
        InformationSecurityContext security = InformationSecurityContext.builder()
                .addUnitCriterion("Health.Security.Criterion.HighAssurance")
                .addContainerCriterion("Health.Security.Container.EncryptedEnvelope")
                .addContentCriterion("Health.Security.Content.ClinicalSensitive")
                .addActionMask("READ")
                .addActionMask("ACCESS")
                .addCompartmentMask("*.*.InformationCompartment.PersonalInformation*")
                .addSecurityLabel(HarmoniaSecurityLabelEnum.RESTRICTED)
                .confidentiality(FhirConfidentialityEnum.R)
                .attribute("retentionClearance", "Level3")
                .build();

        assertThat(security.unitCriteria()).containsExactly("Health.Security.Criterion.HighAssurance");
        assertThat(security.containerCriteria()).containsExactly("Health.Security.Container.EncryptedEnvelope");
        assertThat(security.contentCriteria()).containsExactly("Health.Security.Content.ClinicalSensitive");
        assertThat(security.actionMasks()).containsExactlyInAnyOrder("READ", "ACCESS");
        assertThat(security.compartmentMasks()).containsExactly("*.*.InformationCompartment.PersonalInformation*");
        assertThat(security.securityLabels()).containsExactly(HarmoniaSecurityLabelEnum.RESTRICTED);
        assertThat(security.confidentiality()).contains(FhirConfidentialityEnum.R);
        assertThat(security.attributes()).containsEntry("retentionClearance", "Level3");

        // Immutability
        assertThatThrownBy(() -> security.actionMasks().add("WRITE"))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> security.attributes().put("foo", "bar"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    // =========================================================================
    // LifecycleContext Tests
    // =========================================================================

    @Test
    @DisplayName("LifecycleContext: should encapsulate lifecycle stage, timestamps, outcomes, and retention")
    void testLifecycleContext() {
        Instant now = Instant.now();
        LifecycleContext lifecycle = LifecycleContext.builder()
                .lifecycleStage("COMMITTED")
                .persistenceTimestamp(now)
                .commitOutcome(AuthoritativeCommitOutcome.COMMITTED)
                .retained(true)
                .coordinationAttribute("storageTier", "hot")
                .build();

        assertThat(lifecycle.lifecycleStage()).contains("COMMITTED");
        assertThat(lifecycle.persistenceTimestamp()).contains(now);
        assertThat(lifecycle.commitOutcome()).contains(AuthoritativeCommitOutcome.COMMITTED);
        assertThat(lifecycle.isRetained()).isTrue();
        assertThat(lifecycle.coordinationAttributes()).containsEntry("storageTier", "hot");

        // With mutator for retirement (RETIRE)
        LifecycleContext retired = lifecycle
                .withLifecycleStage("RETIRED")
                .withRetained(false);
        assertThat(retired.lifecycleStage()).contains("RETIRED");
        assertThat(retired.isRetained()).isFalse();
    }

    // =========================================================================
    // InformationUnitRelationship Tests
    // =========================================================================

    @Test
    @DisplayName("InformationUnitRelationship: should support local and remote relationships with defensive validation")
    void testInformationUnitRelationship() {
        UUID targetUuid = UUID.randomUUID();
        InformationUnitRelationship localRel = InformationUnitRelationship.local(
                InformationUnitRelationship.TYPE_RELATES_TO, targetUuid);

        assertThat(localRel.relationshipType()).isEqualTo(InformationUnitRelationship.TYPE_RELATES_TO);
        assertThat(localRel.targetIdentifier()).isEqualTo(targetUuid.toString());
        assertThat(localRel.isLocal()).isTrue();
        assertThat(localRel.description()).isEmpty();

        InformationUnitRelationship remoteRel = InformationUnitRelationship.remote(
                InformationUnitRelationship.TYPE_BASED_ON, "https://external-ehr.org/Observation/9988");

        assertThat(remoteRel.relationshipType()).isEqualTo(InformationUnitRelationship.TYPE_BASED_ON);
        assertThat(remoteRel.targetIdentifier()).isEqualTo("https://external-ehr.org/Observation/9988");
        assertThat(remoteRel.isLocal()).isFalse();

        // Validation unhappy paths
        assertThatThrownBy(() -> new InformationUnitRelationship(null, "target", true, null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new InformationUnitRelationship("relatesTo", "", true, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // =========================================================================
    // InformationUnitMetadata Composite Tests
    // =========================================================================

    @Test
    @DisplayName("InformationUnitMetadata: should compose all four contexts and relationships with empty defaults")
    void testInformationUnitMetadataComposition() {
        InformationUnitMetadata empty = InformationUnitMetadata.empty();
        assertThat(empty.managementContext()).isNotNull();
        assertThat(empty.informationCoordinates()).isNotNull();
        assertThat(empty.securityContext()).isNotNull();
        assertThat(empty.lifecycleContext()).isNotNull();
        assertThat(empty.relationships()).isEmpty();

        InformationUnitRelationship rel1 = InformationUnitRelationship.local("partOf", "target-001");
        InformationUnitMetadata populated = InformationUnitMetadata.builder()
                .managementContext(ManagementContext.builder().activeGeneration("gen-1").build())
                .informationCoordinates(InformationCoordinates.builder().informationOwner("OwnerOrg").build())
                .securityContext(InformationSecurityContext.builder().addActionMask("ACCESS").build())
                .lifecycleContext(LifecycleContext.builder().lifecycleStage("CURRENT").build())
                .addRelationship(rel1)
                .build();

        assertThat(populated.managementContext().activeGeneration()).contains("gen-1");
        assertThat(populated.informationCoordinates().informationOwner()).contains("OwnerOrg");
        assertThat(populated.securityContext().actionMasks()).containsExactly("ACCESS");
        assertThat(populated.lifecycleContext().lifecycleStage()).contains("CURRENT");
        assertThat(populated.relationships()).containsExactly(rel1);

        // Immutability of relationships list
        assertThatThrownBy(() -> populated.relationships().add(InformationUnitRelationship.local("relatesTo", "target-002")))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
