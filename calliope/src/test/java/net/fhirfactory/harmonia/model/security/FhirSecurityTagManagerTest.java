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

import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Encounter;
import org.hl7.fhir.r5.model.Meta;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Practitioner;
import org.hl7.fhir.r5.model.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class FhirSecurityTagManagerTest {

    @Test
    @DisplayName("Verify applyDefaultSecurityTag assigns 'N' when no security tags present")
    void testApplyDefaultSecurityTag() {
        Patient patient = new Patient();
        assertThat(FhirSecurityTagManager.hasSecurityTag(patient)).isFalse();

        Patient tagged = FhirSecurityTagManager.applyDefaultSecurityTag(patient);
        assertThat(tagged).isSameAs(patient);
        assertThat(FhirSecurityTagManager.hasSecurityTag(patient)).isTrue();
        assertThat(FhirSecurityTagManager.hasConfidentiality(patient, FhirConfidentialityEnum.N)).isTrue();
        assertThat(FhirSecurityTagManager.getConfidentiality(patient)).contains(FhirConfidentialityEnum.N);
    }

    @Test
    @DisplayName("Verify existing confidentiality security tags are preserved and not overwritten")
    void testPreserveExistingConfidentiality() {
        Patient patient = new Patient();
        patient.setMeta(new Meta());
        patient.getMeta().addSecurity(new Coding(FhirConfidentialityEnum.CONFIDENTIALITY_SYSTEM, "R", "Restricted"));

        FhirSecurityTagManager.applyDefaultSecurityTag(patient);

        assertThat(patient.getMeta().getSecurity()).hasSize(1);
        assertThat(FhirSecurityTagManager.hasConfidentiality(patient, FhirConfidentialityEnum.R)).isTrue();
        assertThat(FhirSecurityTagManager.hasConfidentiality(patient, FhirConfidentialityEnum.N)).isFalse();
        assertThat(FhirSecurityTagManager.getConfidentiality(patient)).contains(FhirConfidentialityEnum.R);
    }

    @Test
    @DisplayName("Verify applySecurityTag with enum and string code")
    void testApplySecurityTag() {
        Encounter encounter = new Encounter();
        FhirSecurityTagManager.applySecurityTag(encounter, FhirConfidentialityEnum.V);

        assertThat(FhirSecurityTagManager.hasConfidentiality(encounter, FhirConfidentialityEnum.V)).isTrue();
        assertThat(FhirSecurityTagManager.getConfidentiality(encounter)).contains(FhirConfidentialityEnum.V);

        Practitioner practitioner = new Practitioner();
        FhirSecurityTagManager.applySecurityTag(practitioner, "M");
        assertThat(FhirSecurityTagManager.hasConfidentiality(practitioner, FhirConfidentialityEnum.M)).isTrue();

        // Invalid code defaults to N
        Practitioner pr2 = new Practitioner();
        FhirSecurityTagManager.applySecurityTag(pr2, "INVALID");
        assertThat(FhirSecurityTagManager.hasConfidentiality(pr2, FhirConfidentialityEnum.N)).isTrue();
    }

    @Test
    @DisplayName("Verify applySecurityTags on Bundle applies tags to bundle and all entries")
    void testApplySecurityTagsToBundle() {
        Bundle bundle = new Bundle();
        Patient p1 = new Patient();
        Encounter e1 = new Encounter();

        bundle.addEntry().setResource(p1);
        bundle.addEntry().setResource(e1);

        Bundle taggedBundle = FhirSecurityTagManager.applySecurityTags(bundle);
        assertThat(taggedBundle).isSameAs(bundle);

        assertThat(FhirSecurityTagManager.hasConfidentiality(bundle, FhirConfidentialityEnum.N)).isTrue();
        assertThat(FhirSecurityTagManager.hasConfidentiality(p1, FhirConfidentialityEnum.N)).isTrue();
        assertThat(FhirSecurityTagManager.hasConfidentiality(e1, FhirConfidentialityEnum.N)).isTrue();
    }

    @Test
    @DisplayName("Verify applySecurityTags on Bundle with custom confidentiality")
    void testApplyCustomSecurityTagsToBundle() {
        Bundle bundle = new Bundle();
        Patient p1 = new Patient();
        bundle.addEntry().setResource(p1);

        FhirSecurityTagManager.applySecurityTags(bundle, FhirConfidentialityEnum.R);

        assertThat(FhirSecurityTagManager.hasConfidentiality(bundle, FhirConfidentialityEnum.R)).isTrue();
        assertThat(FhirSecurityTagManager.hasConfidentiality(p1, FhirConfidentialityEnum.N)).isTrue();
    }

    @Test
    @DisplayName("Verify null safety on all operations")
    void testNullSafety() {
        Resource nullResource = null;
        Bundle nullBundle = null;
        assertThat(FhirSecurityTagManager.applyDefaultSecurityTag(nullResource)).isNull();
        assertThat(FhirSecurityTagManager.applySecurityTag(nullResource, FhirConfidentialityEnum.N)).isNull();
        assertThat(FhirSecurityTagManager.applySecurityTag(nullResource, "N")).isNull();
        assertThat(FhirSecurityTagManager.applySecurityTags(nullBundle)).isNull();
        assertThat(FhirSecurityTagManager.applySecurityTags(nullBundle, FhirConfidentialityEnum.N)).isNull();
        assertThat(FhirSecurityTagManager.hasSecurityTag(null)).isFalse();
        assertThat(FhirSecurityTagManager.hasConfidentialitySecurityTag(null)).isFalse();
        assertThat(FhirSecurityTagManager.hasConfidentiality(null, FhirConfidentialityEnum.N)).isFalse();
        assertThat(FhirSecurityTagManager.getConfidentiality(null)).isEmpty();
        assertThat(FhirSecurityTagManager.getConfidentialityOrDefault(null, FhirConfidentialityEnum.L)).isEqualTo(FhirConfidentialityEnum.L);
    }
}
