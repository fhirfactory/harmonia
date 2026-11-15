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

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.model.security.FhirConfidentialityEnum;
import net.fhirfactory.harmonia.model.security.HarmoniaSecurityLabelEnum;
import net.fhirfactory.harmonia.model.topic.Topic;
import net.fhirfactory.harmonia.model.topic.TopicMask;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("InformationSecurityContext Strongly Typed Security Criteria Tests")
public class InformationSecurityContextTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Should construct InformationSecurityContext with strongly typed Topic criteria and TopicMasks")
    void testSecurityContextConstructionWithTypedTopics() {
        Topic unitTopic = Topic.fromName("Health.Security.ProtectedHealthInformation");
        Topic contTopic = Topic.fromName("Health.Security.Container.Egress");
        Topic contentTopic = Topic.fromName("Health.Security.Content.Clinical");
        TopicMask compMask = TopicMask.fromPattern("Health.Compartment.Oncology.*");

        InformationSecurityContext sec = InformationSecurityContext.builder()
                .addUnitCriterion(unitTopic)
                .addContainerCriterion(contTopic)
                .addContentCriterion(contentTopic)
                .addActionMask("READ")
                .addActionMask("WRITE")
                .addCompartmentMask(compMask)
                .addSecurityLabel(HarmoniaSecurityLabelEnum.RESTRICTED)
                .confidentiality(FhirConfidentialityEnum.R)
                .putAttribute("legalHold", "true")
                .build();

        assertThat(sec.unitCriteria()).containsExactly(unitTopic);
        assertThat(sec.containerCriteria()).containsExactly(contTopic);
        assertThat(sec.contentCriteria()).containsExactly(contentTopic);
        assertThat(sec.actionMasks()).containsExactly("READ", "WRITE");
        assertThat(sec.compartmentMasks()).containsExactly(compMask);
        assertThat(sec.securityLabels()).containsExactly(HarmoniaSecurityLabelEnum.RESTRICTED);
        assertThat(sec.confidentiality()).contains(FhirConfidentialityEnum.R);
        assertThat(sec.attribute("legalHold")).contains("true");
    }

    @Test
    @DisplayName("Should enforce unmodifiable collection immutability")
    void testCollectionImmutability() {
        Topic unitTopic = Topic.fromName("Health.Security.PHI");
        InformationSecurityContext sec = InformationSecurityContext.builder()
                .addUnitCriterion(unitTopic)
                .build();

        assertThatThrownBy(() -> sec.unitCriteria().add(Topic.fromName("Health.Security.Other")))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("Should serialize and deserialize InformationSecurityContext preserving typed Topic criteria and TopicMasks")
    void testSecurityContextJsonRoundtrip() throws Exception {
        Topic unitTopic = Topic.fromName("Health.Security.ProtectedHealthInformation");
        TopicMask compMask = TopicMask.fromPattern("Health.Compartment.Oncology.*");

        InformationSecurityContext sec = InformationSecurityContext.builder()
                .addUnitCriterion(unitTopic)
                .addCompartmentMask(compMask)
                .addActionMask("EXECUTE")
                .confidentiality(FhirConfidentialityEnum.V)
                .build();

        String json = objectMapper.writeValueAsString(sec);
        InformationSecurityContext deserialized = objectMapper.readValue(json, InformationSecurityContext.class);

        assertThat(deserialized.unitCriteria()).isEqualTo(sec.unitCriteria());
        assertThat(deserialized.compartmentMasks()).isEqualTo(sec.compartmentMasks());
        assertThat(deserialized.actionMasks()).isEqualTo(sec.actionMasks());
        assertThat(deserialized.confidentiality()).isEqualTo(sec.confidentiality());
    }
}
