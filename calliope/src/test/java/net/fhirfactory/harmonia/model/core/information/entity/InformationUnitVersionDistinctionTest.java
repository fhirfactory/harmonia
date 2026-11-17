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

import net.fhirfactory.harmonia.model.core.naming.factory.TopicFactory;

import net.fhirfactory.harmonia.model.core.information.entity.datatypes.Content;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContentMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContainerMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationRepresentation;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationUnitIdentifier;
import net.fhirfactory.harmonia.model.core.information.operations.ManagementContext;
import net.fhirfactory.harmonia.model.core.information.operations.OperationalRevision;
import net.fhirfactory.harmonia.model.core.information.operations.factory.ManagementContextBuilder;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests verifying that the Information Unit framework preserves the distinct separation among:
 * <ol>
 *   <li><b>IU Identity:</b> Persistent UUID and canonical Topic ({@code harmonia.mneme.iu.<hex>}).</li>
 *   <li><b>Authoritative Persisted IU Version:</b> {@code InformationUnit.version} (committed by Mnemosyne).</li>
 *   <li><b>Operational Version:</b> {@code ManagementContext.operationalVersion} / {@code OperationalMetadata.operationalRevision}.</li>
 *   <li><b>Active Generation:</b> {@code ManagementContext.activeGeneration} (cache manifestation instance).</li>
 *   <li><b>Content Structure Version:</b> {@code ContentMetadata.contentStructureVersion} (schema/profile version).</li>
 *   <li><b>Native Content Version:</b> Embedded within native payload (if supported by payload).</li>
 * </ol>
 */
class InformationUnitVersionDistinctionTest {

    @Test
    @DisplayName("Should maintain strict separation between all version concepts")
    void testVersionConceptsIndependence() {
        UUID iuUuid = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee");

        // Authoritative Persisted Version (Mnemosyne)
        String authVersion = "1";

        // Operational Version 5 in Mneme cache
        OperationalRevision operationalRevision = OperationalRevision.of("5");

        // Active Generation of this local node
        String activeGen = "node-alpha-manifestation-001";

        // Content Structure Topic & Version: FHIR R5 schema
        Topic contentStructureTopic = TopicFactory.fromName("Health.FHIR.R5.Patient");
        String contentStructureVer = "R5";

        // Native payload with its own internal versionId (e.g. "v1.2.0")
        String nativePayload = "{\"resourceType\":\"Patient\",\"id\":\"pat-100\",\"meta\":{\"versionId\":\"v1.2.0\"}}";

        ManagementContext managementContext = new ManagementContextBuilder()
                .operationalRevision(operationalRevision)
                .activeGeneration(activeGen)
                .build();
        ContentMetadata contentMetadata = ContentMetadata.builder()
                        .contentStructureTopic(contentStructureTopic)
                        .contentStructureVersion(contentStructureVer)
                        .contentFormat("application/fhir+json")
                        .build();
        InformationRepresentation representation = new InformationRepresentation(
                ContainerMetadata.empty(), contentMetadata, Content.text(nativePayload));
        InformationUnit unit = new InformationUnit(InformationUnitIdentifier.fromUuid(iuUuid), authVersion,
                InformationMetadata.empty(), managementContext, representation);

        assertThat(unit.identifier().uuid()).isEqualTo(iuUuid);
        assertThat(unit.identifier().topicName()).isEqualTo("harmonia.mneme.iu.aaaaaaaabbbbccccddddeeeeeeeeeeee");

        // 2. Authoritative Persisted Version
        assertThat(unit.version()).isEqualTo("1");

        // 3. Operational Version
        assertThat(unit.managementContext().operationalRevision()).contains(operationalRevision);
        assertThat(unit.managementContext().operationalRevision().orElseThrow().value()).isEqualTo("5");

        // 4. Active Generation
        assertThat(unit.managementContext().activeGeneration()).contains(activeGen);

        // 5. Content Structure Topic & Version
        assertThat(unit.representation().contentMetadata().contentStructureTopic()).contains(contentStructureTopic);
        assertThat(unit.representation().contentMetadata().contentStructureVersion()).contains("R5");

        // 6. Native Payload content
        assertThat(unit.representation().content().textContent().orElse("")).contains("\"versionId\":\"v1.2.0\"");

        // Mutation of operational version must not alter Content Structure Version, Authoritative Version, or Identity
        InformationUnit updatedOperational = unit.withManagementContext(
                unit.managementContext().withOperationalRevision(OperationalRevision.of("6")));

        assertThat(updatedOperational.managementContext().operationalRevision()).contains(OperationalRevision.of("6"));
        assertThat(updatedOperational.version()).isEqualTo("1");
        assertThat(updatedOperational.representation().contentMetadata().contentStructureVersion()).contains("R5");
        assertThat(updatedOperational.identifier().uuid()).isEqualTo(iuUuid);

        // Mutation of active generation must not alter operational version or Identity
        InformationUnit updatedGeneration = unit.withManagementContext(
                unit.managementContext().withActiveGeneration("node-beta-manifestation-002"));

        assertThat(updatedGeneration.managementContext().activeGeneration()).contains("node-beta-manifestation-002");
        assertThat(updatedGeneration.managementContext().operationalRevision()).contains(operationalRevision);
        assertThat(updatedGeneration.version()).isEqualTo("1");
        assertThat(updatedGeneration.identifier().uuid()).isEqualTo(iuUuid);
    }
}
