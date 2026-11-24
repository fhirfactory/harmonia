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

package net.fhirfactory.harmonia.hestia.mnemosyne.information;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.hestia.mnemosyne.nonfhir.InformationUnitPersistenceRecord;
import net.fhirfactory.harmonia.model.core.information.entity.InformationUnit;
import net.fhirfactory.harmonia.model.core.information.entity.InformationMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.Content;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContentMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContainerMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationRepresentation;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationUnitIdentifier;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationUnitRelationship;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationUnitValueSets;
import net.fhirfactory.harmonia.model.core.naming.factory.TopicFactory;
import net.fhirfactory.harmonia.model.core.information.operations.ManagementContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InformationUnitConversionTest {

    private final InformationUnitDisassembler disassembler = new InformationUnitDisassembler(new ObjectMapper());
    private final InformationUnitConstructor constructor = new InformationUnitConstructor(new ObjectMapper());

    @Test
    void disassemblyAndConstructionPreserveGovernedContentAndAssignPersistedVersion() {
        InformationUnit original = informationUnit("original");
        InformationUnitPersistenceRecord proposal = disassembler.disassemble(original);
        InformationUnit reconstructed = constructor.construct(proposal.withVersion(7));

        assertThat(reconstructed.identifier()).isEqualTo(original.identifier());
        assertThat(reconstructed.metadata()).isEqualTo(original.metadata());
        assertThat(reconstructed.metadata().relationships()).containsExactlyElementsOf(original.metadata().relationships());
        assertThat(reconstructed.representation()).isEqualTo(original.representation());
        assertThat(reconstructed.version()).isEqualTo("7");
        assertThat(reconstructed.getManagementContext()).isNull();
        assertThat(proposal.canonicalJson()).doesNotContain("managementContext");
    }

    @Test
    void constructionRejectsTamperedPersistedBytes() {
        InformationUnitPersistenceRecord proposal = disassembler.disassemble(informationUnit("payload"));
        InformationUnitPersistenceRecord tampered = new InformationUnitPersistenceRecord(
                proposal.identifier(),
                proposal.topicName(),
                1,
                proposal.canonicalJson() + " ",
                proposal.sha256());

        assertThatThrownBy(() -> constructor.construct(tampered))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("integrity digest");
    }

    private InformationUnit informationUnit(String payload) {
        InformationUnitIdentifier identifier = InformationUnitIdentifier.random();
        InformationMetadata metadata = InformationMetadata.builder()
                .origin(TopicFactory.fromName("Hospital.Site.Canberra"))
                .relationship(new InformationUnitRelationship(
                        InformationUnitValueSets.TYPE_RELATES_TO,
                        "related-information-unit"))
                .build();
        ContentMetadata contentMetadata = ContentMetadata.builder()
                .contentStructureTopic(TopicFactory.fromName("Health.FHIR.R5.Patient"))
                .contentStructureVersion("R5")
                .build();
        InformationRepresentation representation = new InformationRepresentation(
                ContainerMetadata.empty(),
                contentMetadata,
                Content.text(payload));
        return new InformationUnit(identifier, "caller-version", metadata, ManagementContext.empty(), representation);
    }
}