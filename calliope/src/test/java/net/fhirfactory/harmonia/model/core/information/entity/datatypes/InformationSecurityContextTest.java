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

package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.naming.TopicMask;
import net.fhirfactory.harmonia.model.core.naming.factory.TopicFactory;
import net.fhirfactory.harmonia.model.core.naming.factory.TopicMaskFactory;
import net.fhirfactory.harmonia.model.core.security.valuesets.FhirConfidentialityEnum;
import net.fhirfactory.harmonia.model.core.security.valuesets.HarmoniaSecurityLabelEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("InformationSecurityContext Strongly Typed Security Criteria Tests")
public class InformationSecurityContextTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Should construct InformationSecurityContext with strongly typed Topic criteria and TopicMasks")
    void testSecurityContextConstructionWithTypedTopics() {
        Topic unitTopic = TopicFactory.fromName("Health.Security.ProtectedHealthInformation");
        Topic contTopic = TopicFactory.fromName("Health.Security.Container.Egress");
        Topic contentTopic = TopicFactory.fromName("Health.Security.Content.Clinical");
        TopicMask compMask = TopicMaskFactory.fromNamePattern("Health.Compartment.Oncology.*");

        InformationSecurityContext sec = InformationSecurityContext.builder()
                .addUnitCriterion(unitTopic)
                .addContainerCriterion(contTopic)
                .addContentCriterion(contentTopic)
                .addActionMask("READ")
                .addActionMask("WRITE")
                .addCompartmentMask(compMask)
                .addSecurityLabel(HarmoniaSecurityLabelEnum.RESTRICTED)
                .confidentiality(FhirConfidentialityEnum.R)
                .attribute("legalHold", "true")
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
        Topic unitTopic = TopicFactory.fromName("Health.Security.PHI");
        InformationSecurityContext sec = InformationSecurityContext.builder()
                .addUnitCriterion(unitTopic)
                .build();

        assertThatThrownBy(() -> sec.unitCriteria().add(TopicFactory.fromName("Health.Security.Other")))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("Action adapters trim the display name but compile a literal value-path selector")
    void testActionMaskCompatibilityAndLiteralSelection() throws Exception {
        InformationSecurityContext context = InformationSecurityContext.builder()
                .addActionMask("  EXPORT  ")
                .build();

        assertThat(context.actionMasks()).containsExactly("EXPORT");
        assertThat(context.actionTopicMasks()).singleElement().satisfies(mask -> {
            assertThat(mask.matches(TopicFactory.fromName("Harmonia.Security.Action.EXPORT"))).isTrue();
            assertThat(mask.matches(TopicFactory.fromName("Harmonia.Security.Action.EXPORTER"))).isFalse();
        });

        String escapedAction = "EXPORT\\E";
        TopicMask literalMask = TopicMaskFactory.forLiteralValuePath(
                List.of("Harmonia", "Security", "Action", escapedAction));
        assertThat(TopicMaskFactory.literalValuePath(literalMask))
                .contains(List.of("Harmonia", "Security", "Action", escapedAction));
        InformationSecurityContext maskOnly = new InformationSecurityContext(null, null, null, null,
                List.of(literalMask), null, null, null, null);
        assertThat(maskOnly.actionMasks()).isEmpty();
        assertThat(maskOnly.actionTopicMasks()).containsExactly(literalMask);
        InformationSecurityContext restored = objectMapper.readValue(
                objectMapper.writeValueAsBytes(maskOnly), InformationSecurityContext.class);
        assertThat(restored.actionMasks()).isEmpty();
        assertThat(restored.actionTopicMasks()).containsExactly(literalMask);
    }

    @Test
    @DisplayName("Should serialize and deserialize InformationSecurityContext preserving typed Topic criteria and TopicMasks")
    void testSecurityContextJsonRoundtrip() throws Exception {
        Topic unitTopic = TopicFactory.fromName("Health.Security.ProtectedHealthInformation");
        TopicMask compMask = TopicMaskFactory.fromNamePattern("Health.Compartment.Oncology.*");

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

    @Test
    @DisplayName("Typed action masks preserve exact, inclusive, and version selection through JSON")
    void typedActionMasksRetainTheirSelectionSemantics() throws Exception {
        TopicMask exactMask = TopicMask.builder().addRegex("Domain", "Alpha", "v1").build();
        TopicMask inclusiveMask = TopicMask.builder().addRegex("Domain", "Alpha", "v1")
                .includeContainedElements(true).build();
        InformationSecurityContext exact = new InformationSecurityContext(null, null, null, null,
                List.of(exactMask), null, null, null, null);
        InformationSecurityContext inclusive = new InformationSecurityContext(null, null, null, null,
                List.of(inclusiveMask), null, null, null, null);

        assertThat(exact.isEmpty()).isFalse();
        assertThat(exact).isNotEqualTo(inclusive);
        assertThat(new HashSet<>(List.of(exact, inclusive))).hasSize(2);
        String json = objectMapper.writeValueAsString(exact);
        assertThat(json).contains("actionTopicMasks");
        InformationSecurityContext restored = objectMapper.readValue(json, InformationSecurityContext.class);
        assertThat(restored.actionTopicMasks()).containsExactly(exactMask);
        assertThat(restored).isEqualTo(exact);
        assertThat(restored.hashCode()).isEqualTo(exact.hashCode());
        assertThat(restored.isEmpty()).isFalse();
        assertThat(restored.actionTopicMasks().iterator().next().getEntries().getFirst()
                .getVersionRegex()).contains("v1");

        Topic exactTopic = Topic.builder().addEntry("Domain", "Alpha", "v1").build();
        Topic wrongVersion = Topic.builder().addEntry("Domain", "Alpha", "v2").build();
        Topic appended = Topic.builder().addEntry("Domain", "Alpha", "v1").addEntry("Model", "FHIR").build();
        assertThat(restored.actionTopicMasks()).allSatisfy(mask -> {
            assertThat(mask.matches(exactTopic)).isTrue();
            assertThat(mask.matches(wrongVersion)).isFalse();
            assertThat(mask.matches(appended)).isFalse();
        });

        InformationSecurityContext restoredInclusive = objectMapper.readValue(
                objectMapper.writeValueAsString(inclusive), InformationSecurityContext.class);
        assertThat(restoredInclusive.actionTopicMasks()).singleElement()
                .satisfies(mask -> assertThat(mask.isIncludeContainedElements()).isTrue());
        assertThat(restoredInclusive.actionTopicMasks()).allSatisfy(mask ->
                assertThat(mask.matches(appended)).isTrue());
    }

    @Test
    @DisplayName("Ambiguous legacy action strings are rejected instead of becoming a narrower literal path")
    void ambiguousLegacyActionStringsFailClosed() {
        assertThatThrownBy(() -> InformationSecurityContext.builder().addActionMask("Read.Detail").build())
                .isInstanceOf(IllegalArgumentException.class);
    }
}
