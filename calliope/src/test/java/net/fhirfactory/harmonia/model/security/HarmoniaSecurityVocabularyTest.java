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

import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisRole;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityLabel;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Practitioner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Harmonia Security Vocabulary & FHIR Tagging Tests")
class HarmoniaSecurityVocabularyTest {

    @Test
    @DisplayName("HarmoniaSecurityLabelEnum converts to and from FHIR Coding")
    void testSecurityLabelToCoding() {
        HarmoniaSecurityLabelEnum label = HarmoniaSecurityLabelEnum.PROVIDER_REGISTRY;
        Coding coding = label.toCoding();

        assertThat(coding.getSystem()).isEqualTo(HarmoniaSecurityCodeSystem.SECURITY_LABEL_SYSTEM);
        assertThat(coding.getCode()).isEqualTo("PROVIDER_REGISTRY");
        assertThat(coding.getDisplay()).isEqualTo("Provider Registry Resource");

        var extracted = HarmoniaSecurityLabelEnum.fromCoding(coding);
        assertThat(extracted).isPresent().contains(HarmoniaSecurityLabelEnum.PROVIDER_REGISTRY);
    }

    @Test
    @DisplayName("FhirSecurityTagManager applies and checks Harmonia security labels")
    void testFhirSecurityTagManagerLabels() {
        Practitioner practitioner = new Practitioner();
        practitioner.setId("practitioner-101");

        assertThat(FhirSecurityTagManager.hasHarmoniaSecurityLabel(practitioner, HarmoniaSecurityLabelEnum.PROVIDER_REGISTRY)).isFalse();

        FhirSecurityTagManager.applyHarmoniaSecurityLabel(practitioner, HarmoniaSecurityLabelEnum.PROVIDER_REGISTRY);
        FhirSecurityTagManager.applyHarmoniaSecurityLabel(practitioner, HarmoniaSecurityLabelEnum.INTERNAL);

        assertThat(FhirSecurityTagManager.hasHarmoniaSecurityLabel(practitioner, HarmoniaSecurityLabelEnum.PROVIDER_REGISTRY)).isTrue();
        assertThat(FhirSecurityTagManager.hasHarmoniaSecurityLabel(practitioner, "PROVIDER_REGISTRY")).isTrue();
        assertThat(FhirSecurityTagManager.hasHarmoniaSecurityLabel(practitioner, "INTERNAL")).isTrue();
        assertThat(FhirSecurityTagManager.hasHarmoniaSecurityLabel(practitioner, "AUDIT")).isFalse();

        Set<ThemisSecurityLabel> labels = FhirSecurityTagManager.getHarmoniaSecurityLabels(practitioner);
        assertThat(labels).hasSize(2);
        assertThat(labels).extracting(ThemisSecurityLabel::code).containsExactlyInAnyOrder("PROVIDER_REGISTRY", "INTERNAL");
    }

    @Test
    @DisplayName("HarmoniaRoleEnum maps correctly to Themis authorities")
    void testRoleAuthoritiesMapping() {
        HarmoniaRoleEnum submitter = HarmoniaRoleEnum.PRV_SUB;
        ThemisRole themisRole = submitter.toThemisRole();

        assertThat(themisRole.roleCode()).isEqualTo("PRV_SUB");
        assertThat(themisRole.authorities())
                .extracting(ThemisAuthority::authorityCode)
                .containsExactly("provider.change.submit");

        HarmoniaRoleEnum admin = HarmoniaRoleEnum.PRV_ADM;
        assertThat(admin.getThemisAuthorities())
                .extracting(ThemisAuthority::authorityCode)
                .contains("provider.admin", "provider.read", "provider.change.submit", "provider.change.process", "provider.resource.create", "provider.resource.update");
    }
}
