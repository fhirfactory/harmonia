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

@DisplayName("TopicFactory Creation Tests")
public class TopicFactoryTest {

    @Test
    @DisplayName("Should build Topic from plain dot-separated name")
    void testFromNamePlain() {
        UUID id = UUID.randomUUID();
        Topic topic = TopicFactory.fromName(id, "Health.HL7.2.4.ADT.A01");

        assertThat(topic.getId()).isEqualTo(id);
        assertThat(topic.getDepth()).isEqualTo(5);
        assertThat(topic.getName()).isEqualTo("Health.HL7.2.4.ADT.A01");
        assertThat(topic.getDomain()).isEqualTo("Health");
        assertThat(topic.getModel()).isEqualTo("HL7");
        assertThat(topic.getModelVersion()).isEqualTo("2.4");
        assertThat(topic.getDataElement()).isEqualTo("ADT");
        assertThat(topic.getDataElementQualifier()).isEqualTo("A01");
    }

    @Test
    @DisplayName("Should build Topic from qualified canonical string")
    void testFromNameQualified() {
        Topic topic = TopicFactory.fromName("Domain=Health.Model=FHIR.ModelVersion=R5.DataElement=Practitioner.DataElementQualifier=CREATE");

        assertThat(topic.getDepth()).isEqualTo(5);
        assertThat(topic.getName()).isEqualTo("Health.FHIR.R5.Practitioner.CREATE");
        assertThat(topic.getEntry(0).getQualifier()).isEqualTo("Domain");
        assertThat(topic.getEntry(0).getName()).isEqualTo("Health");
        assertThat(topic.getEntry(3).getQualifier()).isEqualTo("DataElement");
        assertThat(topic.getEntry(3).getName()).isEqualTo("Practitioner");
    }

    @Test
    @DisplayName("Should build Topic for InformationUnit")
    void testForInformationUnit() {
        UUID iuId = UUID.randomUUID();
        Topic topic = TopicFactory.forInformationUnit(iuId);

        assertThat(topic.getDepth()).isEqualTo(4);
        assertThat(topic.getDomain()).isEqualTo("harmonia");
        assertThat(topic.getModel()).isEqualTo("mneme");
        assertThat(topic.getModelVersion()).isEqualTo("iu");
        assertThat(topic.getDataElement()).isEqualTo(iuId.toString().replace("-", "").toLowerCase());
    }

    @Test
    @DisplayName("Should build Topic for HL7 message event")
    void testFromHl7() {
        Topic t1 = TopicFactory.fromHl7("ADT", "A01");
        assertThat(t1.getName()).isEqualTo("Health.HL7.2.4.ADT.A01");
        assertThat(t1.getCompositeTrigger()).isEqualTo("ADT^A01");

        Topic t2 = TopicFactory.fromHl7("2.5", "ORU", "R01");
        assertThat(t2.getName()).isEqualTo("Health.HL7.2.5.ORU.R01");
        assertThat(t2.getModelVersion()).isEqualTo("2.5");

        Topic t3 = TopicFactory.fromHl7("ORM^O01");
        assertThat(t3.getName()).isEqualTo("Health.HL7.2.4.ORM.O01");
    }

    @Test
    @DisplayName("Should build Topic for FHIR resource interaction")
    void testFromFhir() {
        Topic topic = TopicFactory.fromFhir("Patient", "UPDATE");
        assertThat(topic.getName()).isEqualTo("Health.FHIR.R5.Patient.UPDATE");
        assertThat(topic.getDataElement()).isEqualTo("Patient");
        assertThat(topic.getDataElementQualifier()).isEqualTo("UPDATE");
    }

    @Test
    @DisplayName("Should reject blank name in fromName")
    void testRejectBlankName() {
        assertThatThrownBy(() -> TopicFactory.fromName("   "))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
