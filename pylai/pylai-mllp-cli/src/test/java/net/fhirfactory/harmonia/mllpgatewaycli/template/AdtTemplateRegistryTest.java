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
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AdtTemplateRegistryTest {

    @ParameterizedTest
    @EnumSource(AdtTemplateType.class)
    @DisplayName("Every registered template is present and contains required HL7 segments")
    void testEveryTemplateIsPresent(AdtTemplateType templateType) {
        Optional<String> template = AdtTemplateRegistry.getRawTemplate(templateType);
        assertThat(template).isPresent();
        String hl7 = template.get();

        assertThat(hl7).contains("MSH|^~\\&|");
        assertThat(hl7).contains("EVN|" + templateType.getCode() + "|");
        assertThat(hl7).contains("PID|1||${mrn}");
        assertThat(hl7).contains("PV1|1|");
    }

    @ParameterizedTest
    @ValueSource(strings = {"A01", "a01", "ADT^A01", "adt^a01", "ADT_A01", "ADT-A01"})
    @DisplayName("AdtTemplateType resolves various string formats for A01")
    void testFromStringResolution(String input) {
        Optional<AdtTemplateType> result = AdtTemplateType.fromString(input);
        assertThat(result).isPresent().contains(AdtTemplateType.A01);
    }

    @Test
    @DisplayName("AdtTemplateType handles unknown inputs safely")
    void testFromStringUnknown() {
        assertThat(AdtTemplateType.fromString(null)).isEmpty();
        assertThat(AdtTemplateType.fromString("")).isEmpty();
        assertThat(AdtTemplateType.fromString("UNKNOWN_XYZ")).isEmpty();
    }

    @Test
    @DisplayName("All 11 standard ADT templates are supported in the registry")
    void testSupportedTypesCount() {
        assertThat(AdtTemplateRegistry.getSupportedTypes())
                .containsExactlyInAnyOrder(
                        AdtTemplateType.A01,
                        AdtTemplateType.A02,
                        AdtTemplateType.A03,
                        AdtTemplateType.A04,
                        AdtTemplateType.A05,
                        AdtTemplateType.A08,
                        AdtTemplateType.A11,
                        AdtTemplateType.A12,
                        AdtTemplateType.A13,
                        AdtTemplateType.A31,
                        AdtTemplateType.A40
                );
    }
}
