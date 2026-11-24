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

package net.fhirfactory.harmonia.model.core.naming.factory;

import net.fhirfactory.harmonia.model.core.naming.Topic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Topic compatibility factory")
class TopicFactoryTest {

    @Test
    @DisplayName("Unqualified names retain the IU and HL7 projections without runtime Topic identifiers")
    void legacyUnqualifiedNameProjection() {
        Topic topic = TopicFactory.fromName("Health.HL7.2.4.ADT.A01");

        assertThat(topic.getDepth()).isEqualTo(5);
        assertThat(topic.getName()).isEqualTo("Health.HL7.2.4.ADT.A01");
        assertThat(topic.getEntries()).extracting(entry -> entry.getQualifier())
                .containsExactly("N0", "N1", "N2", "N3", "N4");
        assertThat(TopicFactory.forInformationUnit(UUID.fromString("01234567-89ab-cdef-0123-456789abcdef"))
                .getName()).isEqualTo("harmonia.mneme.iu.0123456789abcdef0123456789abcdef");
    }

    @Test
    @DisplayName("Canonical qualified input delegates to strict Topic parsing")
    void canonicalNameInput() {
        Topic topic = TopicFactory.fromName("Domain=Health.Model=FHIR.ModelVersion=R5.DataElement=Practitioner");

        assertThat(topic.getName()).isEqualTo("Health.FHIR.R5.Practitioner");
        assertThat(topic.getEntries().getFirst().getQualifier()).isEqualTo("Domain");
        assertThat(topic.getEntries().get(3).getQualifier()).isEqualTo("DataElement");
        assertThat(TopicFactory.fromName(topic.getCanonicalName())).isEqualTo(topic);
    }

    @Test
    @DisplayName("HL7 and FHIR adapters preserve established unqualified names")
    void standardTopicAdapters() {
        Topic hl7 = TopicFactory.fromHl7("ADT", "A01");
        Topic versionedHl7 = TopicFactory.fromHl7("2.5", "ORU", "R01");
        Topic composite = TopicFactory.fromHl7("ORM^O01");
        Topic fhir = TopicFactory.fromFhir("Patient", "UPDATE");

        assertThat(hl7.getName()).isEqualTo("Health.HL7.2.4.ADT.A01");
        assertThat(hl7.getCompositeTrigger()).isEqualTo("ADT^A01");
        assertThat(versionedHl7.getName()).isEqualTo("Health.HL7.2.5.ORU.R01");
        assertThat(composite.getName()).isEqualTo("Health.HL7.2.4.ORM.O01");
        assertThat(fhir.getName()).isEqualTo("Health.FHIR.R5.Patient.UPDATE");
    }

    @Test
    @DisplayName("Legacy name inputs are strict and never trimmed")
    void rejectsBlankAndAmbiguousNames() {
        for (String invalid : new String[]{null, "", "   ", "Health..ADT", "Health\\.ADT"}) {
            assertThatThrownBy(() -> TopicFactory.fromName(invalid))
                    .isInstanceOf(IllegalArgumentException.class);
        }
        assertThatThrownBy(() -> TopicFactory.fromHl7("ADT^"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> TopicFactory.forInformationUnit("123--456"))
                .isInstanceOf(IllegalArgumentException.class);
        String excessiveDepth = String.join(".", java.util.Collections.nCopies(34, "value"));
        assertThatThrownBy(() -> TopicFactory.fromName(excessiveDepth))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
