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

package net.fhirfactory.harmonia.model.security;

import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class FhirConfidentialityEnumTest {

    @Test
    @DisplayName("Verify all HL7 v3 Confidentiality enum codes exist")
    void testEnumCodes() {
        assertThat(FhirConfidentialityEnum.N.getCode()).isEqualTo("N");
        assertThat(FhirConfidentialityEnum.R.getCode()).isEqualTo("R");
        assertThat(FhirConfidentialityEnum.V.getCode()).isEqualTo("V");
        assertThat(FhirConfidentialityEnum.U.getCode()).isEqualTo("U");
        assertThat(FhirConfidentialityEnum.L.getCode()).isEqualTo("L");
        assertThat(FhirConfidentialityEnum.M.getCode()).isEqualTo("M");
    }

    @ParameterizedTest
    @EnumSource(FhirConfidentialityEnum.class)
    @DisplayName("Verify toCoding conversion contains standard system and code")
    void testToCoding(FhirConfidentialityEnum conf) {
        Coding coding = conf.toCoding();
        assertThat(coding).isNotNull();
        assertThat(coding.getSystem()).isEqualTo(FhirConfidentialityEnum.CONFIDENTIALITY_SYSTEM);
        assertThat(coding.getCode()).isEqualTo(conf.getCode());
        assertThat(coding.getDisplay()).isEqualTo(conf.getDisplay());
    }

    @ParameterizedTest
    @EnumSource(FhirConfidentialityEnum.class)
    @DisplayName("Verify toCodeableConcept wraps coding with display text")
    void testToCodeableConcept(FhirConfidentialityEnum conf) {
        CodeableConcept concept = conf.toCodeableConcept();
        assertThat(concept).isNotNull();
        assertThat(concept.getText()).isEqualTo(conf.getDisplay());
        assertThat(concept.getCoding()).hasSize(1);
        assertThat(concept.getCoding().get(0).getCode()).isEqualTo(conf.getCode());
    }

    @Test
    @DisplayName("Verify applyTo attaches security tag on Resource")
    void testApplyTo() {
        Patient patient = new Patient();
        FhirConfidentialityEnum.N.applyTo(patient);

        assertThat(patient.hasMeta()).isTrue();
        assertThat(patient.getMeta().getSecurity()).hasSize(1);
        assertThat(patient.getMeta().getSecurity().get(0).getCode()).isEqualTo("N");
        assertThat(patient.getMeta().getSecurity().get(0).getSystem()).isEqualTo(FhirConfidentialityEnum.CONFIDENTIALITY_SYSTEM);

        // Applying same code again should not create duplicate
        FhirConfidentialityEnum.N.applyTo(patient);
        assertThat(patient.getMeta().getSecurity()).hasSize(1);

        // Applying null resource returns null
        assertThat(FhirConfidentialityEnum.N.applyTo(null)).isNull();
    }

    @Test
    @DisplayName("Test fromCode and fromCoding lookups")
    void testLookups() {
        assertThat(FhirConfidentialityEnum.fromCode("N")).contains(FhirConfidentialityEnum.N);
        assertThat(FhirConfidentialityEnum.fromCode("Normal")).contains(FhirConfidentialityEnum.N);
        assertThat(FhirConfidentialityEnum.fromCode("r")).contains(FhirConfidentialityEnum.R);
        assertThat(FhirConfidentialityEnum.fromCode("V")).contains(FhirConfidentialityEnum.V);
        assertThat(FhirConfidentialityEnum.fromCode("U")).contains(FhirConfidentialityEnum.U);
        assertThat(FhirConfidentialityEnum.fromCode("L")).contains(FhirConfidentialityEnum.L);
        assertThat(FhirConfidentialityEnum.fromCode("M")).contains(FhirConfidentialityEnum.M);
        assertThat(FhirConfidentialityEnum.fromCode("unknown")).isEmpty();
        assertThat(FhirConfidentialityEnum.fromCode(null)).isEmpty();

        Coding coding = new Coding(FhirConfidentialityEnum.CONFIDENTIALITY_SYSTEM, "R", "Restricted");
        assertThat(FhirConfidentialityEnum.fromCoding(coding)).contains(FhirConfidentialityEnum.R);

        Coding customSystemCoding = new Coding("http://custom.system", "R", "Restricted");
        assertThat(FhirConfidentialityEnum.fromCoding(customSystemCoding)).isEmpty();

        assertThat(FhirConfidentialityEnum.fromCoding(null)).isEmpty();
    }
}
