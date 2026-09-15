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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.mllpgatewaycli.template;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class AdtMessageBuilderTest {

    @Test
    @DisplayName("Builds A01 message with specified MRN, firstName, lastName, and date of birth")
    void testBuildA01WithMessageParameters() {
        AdtMessageParameters params = AdtMessageParameters.builder()
                .mrn("PAT88822")
                .firstName("Sarah")
                .lastName("Connor")
                .dateOfBirth("1984-05-12")
                .gender("F")
                .messageControlId("MSG-TEST-001")
                .sendingApp("TEST_APP")
                .sendingFacility("TEST_FACILITY")
                .currentLocation("ICU^RM10^BED2")
                .attendingDoctor("DOC99^BROWN^EMMETT^^DR")
                .visitNumber("VN777888")
                .build();

        String hl7 = AdtMessageBuilder.buildMessage(AdtTemplateType.A01, params);

        assertThat(hl7).isNotNull();
        assertThat(hl7).contains("MSH|^~\\&|TEST_APP|TEST_FACILITY|HIE|HIE_IM|");
        assertThat(hl7).contains("|ADT^A01|MSG-TEST-001|P|2.4\r");
        assertThat(hl7).contains("EVN|A01|");
        assertThat(hl7).contains("PID|1||PAT88822^^^HOSPITAL^MR||CONNOR^SARAH^^^^||19840512|F");
        assertThat(hl7).contains("PV1|1|I|ICU^RM10^BED2||||DOC99^BROWN^EMMETT^^DR|||||||||||VN777888");
    }

    @ParameterizedTest
    @EnumSource(AdtTemplateType.class)
    @DisplayName("Builds valid messages for all template types with default parameters")
    void testBuildAllTemplatesWithDefaults(AdtTemplateType type) {
        AdtMessageParameters params = new AdtMessageParameters();
        String hl7 = AdtMessageBuilder.buildMessage(type, params);

        assertThat(hl7).isNotEmpty();
        assertThat(hl7).contains("MSH|^~\\&|");
        assertThat(hl7).contains("ADT^" + type.getCode());
        assertThat(hl7).contains("PID|1||PAT10001^^^HOSPITAL^MR||DOE^JOHN^^^^||19800101|M");
    }

    @Test
    @DisplayName("Date of Birth normalizes various input formats to YYYYMMDD")
    void testDateOfBirthNormalization() {
        assertThat(AdtMessageParameters.normalizeDate("1995-12-25", "default")).isEqualTo("19951225");
        assertThat(AdtMessageParameters.normalizeDate("1995/12/25", "default")).isEqualTo("19951225");
        assertThat(AdtMessageParameters.normalizeDate("12/25/1995", "default")).isEqualTo("19951225");
        assertThat(AdtMessageParameters.normalizeDate("19951225", "default")).isEqualTo("19951225");
        assertThat(AdtMessageParameters.normalizeDate("", "19800101")).isEqualTo("19800101");
        assertThat(AdtMessageParameters.normalizeDate(null, "19800101")).isEqualTo("19800101");
    }

    @Test
    @DisplayName("Gender normalizes correctly to single character HL7 code")
    void testGenderNormalization() {
        AdtMessageParameters p1 = AdtMessageParameters.builder().gender("female").build();
        assertThat(p1.getGender()).isEqualTo("F");

        AdtMessageParameters p2 = AdtMessageParameters.builder().gender("Male").build();
        assertThat(p2.getGender()).isEqualTo("M");

        AdtMessageParameters p3 = AdtMessageParameters.builder().gender("other").build();
        assertThat(p3.getGender()).isEqualTo("O");

        AdtMessageParameters p4 = AdtMessageParameters.builder().gender("unknown").build();
        assertThat(p4.getGender()).isEqualTo("U");
    }

    @Test
    @DisplayName("Customizes raw HL7 message string with parameter overrides")
    void testCustomizeRawHl7Message() {
        String raw = "MSH|^~\\&|OLD_APP|OLD_FAC|HIE|HIE_IM|20260101||ADT^A01|OLD-MSG-01|P|2.4\r" +
                "PID|1||OLD_MRN^^^HOSPITAL^MR||OLD_LAST^OLD_FIRST^^^^||19700101|M\r" +
                "PV1|1|I|OLD_LOC||||OLD_DOC\r";

        AdtMessageParameters params = AdtMessageParameters.builder()
                .mrn("NEW_MRN_123")
                .firstName("Alice")
                .lastName("Wonderland")
                .dateOfBirth("1992-03-15")
                .gender("F")
                .messageControlId("NEW-MSG-999")
                .build();

        String customized = AdtMessageBuilder.customizeRawHl7Message(raw, params);

        assertThat(customized).contains("NEW-MSG-999");
        assertThat(customized).contains("NEW_MRN_123^^^HOSPITAL^MR");
        assertThat(customized).contains("WONDERLAND^ALICE^^^^");
        assertThat(customized).contains("19920315");
        assertThat(customized).contains("|F\r");
    }
}
