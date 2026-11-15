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

package net.fhirfactory.harmonia.model.topic;

import net.fhirfactory.harmonia.model.core.information.InformationUnitIdentifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link TopicMask}.
 * Verifies element-wise regular expression matching, strict dot-boundary enforcement,
 * wildcard shorthand expansion, and defensive compilation failure handling.
 */
class TopicMaskTest {

    @Test
    @DisplayName("Should perform exact and wildcard element-wise matching on canonical IU topics")
    void testCanonicalIuTopicMatching() {
        UUID uuid = UUID.fromString("01234567-89ab-cdef-0123-456789abcdef");
        InformationUnitIdentifier id = InformationUnitIdentifier.fromUuid(uuid);
        String topicStr = id.topicName(); // "harmonia.mneme.iu.0123456789abcdef0123456789abcdef"

        // 1. Wildcard shorthand for hex segment
        TopicMask iuWildcardMask = TopicMask.of("harmonia.mneme.iu.*");
        assertThat(iuWildcardMask.matches(topicStr)).isTrue();
        assertThat(iuWildcardMask.matches(id)).isTrue();

        // 2. Exact match mask
        TopicMask exactMask = TopicMask.exact(topicStr);
        assertThat(exactMask.matches(topicStr)).isTrue();
        assertThat(exactMask.matches(id)).isTrue();

        // 3. Different UUID mismatch
        InformationUnitIdentifier otherId = InformationUnitIdentifier.random();
        assertThat(exactMask.matches(otherId)).isFalse();

        // 4. Wildcard matching otherId
        assertThat(iuWildcardMask.matches(otherId)).isTrue();
    }

    @Test
    @DisplayName("Should evaluate element-wise regex across business domain and compartment topics")
    void testElementWiseRegexMatching() {
        // Illustrative mask from DRP §12: *.*.InformationCompartment.PersonalInformation*
        TopicMask compartmentMask = TopicMask.of("*.*.InformationCompartment.PersonalInformation.*");

        assertThat(compartmentMask.matches("Health.HL7.InformationCompartment.PersonalInformation.Demographics")).isTrue();
        assertThat(compartmentMask.matches("Admin.FHIR.InformationCompartment.PersonalInformation.Address")).isTrue();
        assertThat(compartmentMask.matches("Health.HL7.OtherCompartment.PersonalInformation.Demographics")).isFalse();

        // Element-level regex with alternatives (6 dot-delimited elements)
        TopicMask hl7VersionMask = TopicMask.of("Health.HL7.2.4.ADT.(A01|A08)");
        assertThat(hl7VersionMask.matches("Health.HL7.2.4.ADT.A01")).isTrue();
        assertThat(hl7VersionMask.matches("Health.HL7.2.4.ADT.A08")).isTrue();
        assertThat(hl7VersionMask.matches("Health.HL7.2.4.ADT.A03")).isFalse();
        assertThat(hl7VersionMask.matches("Health.HL7.2.3.ADT.A01")).isFalse();
        assertThat(hl7VersionMask.matches("Health.HL7.R5.ADT.A01")).isFalse();

        // Match against Calliope Topic object
        Topic topic = Topic.fromHl7("2.4", "ADT", "A01", "gw", "target", "orig", "dest");
        assertThat(hl7VersionMask.matches(topic)).isTrue();
    }

    @Test
    @DisplayName("Should enforce strict dot boundaries without letting wildcards silently span elements")
    void testStrictDotBoundaries() {
        // 2-element mask must not match 4-element topic
        TopicMask shortMask = TopicMask.of("harmonia.*");
        assertThat(shortMask.matches("harmonia.mneme")).isTrue();
        assertThat(shortMask.matches("harmonia.mneme.iu.1234567890abcdef1234567890abcdef")).isFalse();

        // 5-element mask must not match 4-element topic
        TopicMask longMask = TopicMask.of("harmonia.mneme.iu.*.extra");
        assertThat(longMask.matches("harmonia.mneme.iu.1234567890abcdef1234567890abcdef")).isFalse();
    }

    @Test
    @DisplayName("Should safely handle null and blank match candidates")
    void testNullAndBlankCandidates() {
        TopicMask mask = TopicMask.of("harmonia.mneme.iu.*");

        assertThat(mask.matches((String) null)).isFalse();
        assertThat(mask.matches("")).isFalse();
        assertThat(mask.matches("   ")).isFalse();
        assertThat(mask.matches((Topic) null)).isFalse();
        assertThat(mask.matches((InformationUnitIdentifier) null)).isFalse();
    }

    @Test
    @DisplayName("Should reject invalid mask patterns during compilation")
    void testInvalidMaskPatternRejection() {
        // Null or blank
        assertThatThrownBy(() -> TopicMask.of(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> TopicMask.of("   "))
                .isInstanceOf(IllegalArgumentException.class);

        // Invalid regex syntax in an element
        assertThatThrownBy(() -> TopicMask.of("Health.HL7.[unclosed-bracket.ADT"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid regular expression in mask element");
    }

    @Test
    @DisplayName("Should support equality, hashCode, and toString")
    void testObjectMethods() {
        TopicMask m1 = TopicMask.of("Health.HL7.2.4.ADT.*");
        TopicMask m2 = TopicMask.of("Health.HL7.2.4.ADT.*");
        TopicMask m3 = TopicMask.of("Health.FHIR.R5.Patient.*");

        assertThat(m1).isEqualTo(m2);
        assertThat(m1.hashCode()).isEqualTo(m2.hashCode());
        assertThat(m1).isNotEqualTo(m3);
        assertThat(m1.toString()).isEqualTo("Health.HL7.2.4.ADT.*");
        assertThat(m1.elementCount()).isEqualTo(6);
        assertThat(m1.elementPatterns()).containsExactly("Health", "HL7", "2", "4", "ADT", "*");
    }
}
