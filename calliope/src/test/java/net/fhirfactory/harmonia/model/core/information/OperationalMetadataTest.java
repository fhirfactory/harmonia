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
import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.naming.TopicMask;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OperationalMetadata Foundation and Versioning Distinction Tests")
public class OperationalMetadataTest {

    @Test
    @DisplayName("OperationalMetadata construction, immutability, and accessors")
    void testOperationalMetadataConstructionAndAccessors() {
        Topic contextTopic = Topic.fromName("harmonia.mneme.operational");
        Instant now = Instant.now();
        UUID iuUuid = UUID.randomUUID();
        String subjectId = "harmonia.mneme.iu." + iuUuid.toString().replace("-", "");

        OperationalMetadata op = OperationalMetadata.builder()
                .subjectIdentifier(subjectId)
                .operationalContext(contextTopic)
                .operationalRevision("42")
                .activeGeneration("gen-alpha-001")
                .cachedAt(now)
                .putTransientProperty("nodeId", "mneme-node-1")
                .lifecycleStage("COMMITTED")
                .persistenceTimestamp(now)
                .commitOutcome(AuthoritativeCommitOutcome.COMMITTED)
                .putCoordinationAttribute("leaseOwner", "node-1")
                .addEvidenceReference("urn:audit:tx-1002")
                .build();

        assertThat(op.getSubjectIdentifier()).isEqualTo(subjectId);
        assertThat(op.getOperationalContext()).isEqualTo(contextTopic);
        assertThat(op.getOperationalRevision()).isEqualTo(OperationalRevision.of("42"));
        assertThat(op.getOperationalVersion()).isEqualTo(AuthoritativeVersion.of("42"));
        assertThat(op.getActiveGeneration()).isEqualTo("gen-alpha-001");
        assertThat(op.getCachedAt()).isEqualTo(now);
        assertThat(op.getTransientProperties()).containsEntry("nodeId", "mneme-node-1");
        assertThat(op.getLifecycleStage()).isEqualTo("COMMITTED");
        assertThat(op.getCommitOutcome()).isEqualTo(AuthoritativeCommitOutcome.COMMITTED);
        assertThat(op.getCoordinationAttributes()).containsEntry("leaseOwner", "node-1");
        assertThat(op.getEvidenceReferences()).containsExactly("urn:audit:tx-1002");
    }

    @Test
    @DisplayName("Separation of all seven distinct versioning and operational concerns")
    void testVersionAndOperationalDimensionsSeparation() {
        UUID iuUuid = UUID.fromString("11111111-2222-3333-4444-555555555555");

        // 1. Authoritative Persisted IU Version (established/returned by Mnemosyne)
        String authoritativeVersion = "10";

        // 2. Mneme Operational Revision (for cache optimistic concurrency)
        OperationalRevision operationalRev = OperationalRevision.of("12");

        // 3. Cache Active Generation (cache manifestation instance)
        String activeGen = "cache-gen-east-99";

        // 4. Mneme-Mnemosyne Persistence Coordination (handshake facts)
        String lifecycleStage = "COMMITTED";
        AuthoritativeCommitOutcome commitOutcome = AuthoritativeCommitOutcome.COMMITTED;

        // 5. Governed Information Lifecycle (disposition and retention)
        boolean retained = true;

        // 6. Content Structure Version (schema/profile version)
        String contentStructureVersion = "R5";

        // 7. Native Content Version (embedded inside payload)
        String nativePayload = "{\"resourceType\":\"Patient\",\"id\":\"pat-1\",\"meta\":{\"versionId\":\"v2.1\"}}";

        // Construct InformationUnit
        InformationUnit unit = InformationUnit.builder()
                .identifier(iuUuid)
                .version(authoritativeVersion)
                .lifecycleContext(LifecycleContext.builder().retained(retained).build())
                .contentMetadata(ContentMetadata.builder()
                        .contentStructureTopic(Topic.fromName("Health.FHIR.R5.Patient"))
                        .contentStructureVersion(contentStructureVersion)
                        .build())
                .content(nativePayload)
                .build();

        // Construct peer OperationalMetadata
        OperationalMetadata op = OperationalMetadata.builder()
                .subjectIdentifier(unit.topicName())
                .operationalContext(Topic.fromName("harmonia.mneme.cache"))
                .operationalRevision(operationalRev)
                .activeGeneration(activeGen)
                .lifecycleStage(lifecycleStage)
                .commitOutcome(commitOutcome)
                .build();

        // Assert independence
        assertThat(unit.version()).isEqualTo("10");
        assertThat(op.getOperationalRevision()).isEqualTo(OperationalRevision.of("12"));
        assertThat(op.getOperationalVersion()).isEqualTo(AuthoritativeVersion.of("12"));
        assertThat(op.getActiveGeneration()).isEqualTo("cache-gen-east-99");
        assertThat(op.getLifecycleStage()).isEqualTo("COMMITTED");
        assertThat(op.getCommitOutcome()).isEqualTo(AuthoritativeCommitOutcome.COMMITTED);
        assertThat(unit.lifecycleContext().isRetained()).isTrue();
        assertThat(unit.contentMetadata().contentStructureVersion()).contains("R5");
        assertThat(unit.contentAsString().orElse("")).contains("\"versionId\":\"v2.1\"");

        // Operational facts are not duplicated as InformationUnit identity or representation
        assertThat(unit.representation().contentMetadata().contentStructureVersion()).contains("R5");
        assertThat(unit.representation().contentMetadata().contentStructureVersion()).isNotEqualTo(java.util.Optional.of("10"));
        assertThat(unit.representation().contentMetadata().contentStructureVersion()).isNotEqualTo(java.util.Optional.of("12"));
    }

    @Test
    @DisplayName("OperationalMetadataMask matches operational criteria conjunctions")
    void testOperationalMetadataMaskMatching() {
        Topic contextTopic = Topic.fromName("harmonia.mneme.operational");
        String subjectId = "harmonia.mneme.iu.123e4567e89b12d3a456426614174000";

        OperationalMetadata metadata = OperationalMetadata.builder()
                .subjectIdentifier(subjectId)
                .operationalContext(contextTopic)
                .operationalRevision("5")
                .activeGeneration("gen-001")
                .lifecycleStage("COMMITTED")
                .commitOutcome(AuthoritativeCommitOutcome.COMMITTED)
                .build();

        // Matching mask
        OperationalMetadataMask matchingMask = OperationalMetadataMask.builder()
                .subjectMask(TopicMask.fromPattern("harmonia.mneme.iu.*"))
                .contextMask(TopicMask.fromPattern("harmonia.mneme.*"))
                .operationalRevision("5")
                .activeGeneration("gen-001")
                .lifecycleStage("COMMITTED")
                .commitOutcome(AuthoritativeCommitOutcome.COMMITTED)
                .build();

        assertThat(matchingMask.matches(metadata)).isTrue();

        // Non-matching revision
        OperationalMetadataMask wrongRevision = OperationalMetadataMask.builder()
                .operationalRevision("6")
                .build();
        assertThat(wrongRevision.matches(metadata)).isFalse();

        // Non-matching lifecycle stage
        OperationalMetadataMask wrongStage = OperationalMetadataMask.builder()
                .lifecycleStage("RETIRED")
                .build();
        assertThat(wrongStage.matches(metadata)).isFalse();

        // Non-matching commit outcome
        OperationalMetadataMask wrongOutcome = OperationalMetadataMask.builder()
                .commitOutcome(AuthoritativeCommitOutcome.NOT_COMMITTED)
                .build();
        assertThat(wrongOutcome.matches(metadata)).isFalse();

        // Null metadata safety
        assertThat(matchingMask.matches(null)).isFalse();
    }
}
