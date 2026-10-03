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

package net.fhirfactory.harmonia.model.alignment;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.ContactPoint;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Contract verification test for {@link ResourceAlignmentPort}.
 */
class ResourceAlignmentPortContractTest {

    @Test
    @DisplayName("Fluent destructive merge modifies active resource in-place and preserves reference identity")
    void mergeResourceIsFluentAndDestructive() {
        Patient activePatient = new Patient();
        activePatient.setId("Patient/123");
        activePatient.addName().setFamily("Smith").addGiven("John");

        Patient variantPatient = new Patient();
        variantPatient.setId("Patient/123");
        variantPatient.addTelecom().setSystem(ContactPoint.ContactPointSystem.PHONE).setValue("555-1234");

        ResourceAlignmentPort stubPort = new ResourceAlignmentPort() {
            @Override
            public <T extends IBaseResource> AlignmentAssessment checkAlignment(T currentActiveResource, T variantResource) {
                return new AlignmentAssessment.ObjectsOrthogonal("Telecom added");
            }

            @Override
            public <T extends IBaseResource> T mergeResource(T currentActiveResource, T variantResource) {
                if (currentActiveResource instanceof Patient active && variantResource instanceof Patient variant) {
                    for (ContactPoint telecom : variant.getTelecom()) {
                        active.addTelecom(telecom.copy());
                    }
                }
                return currentActiveResource;
            }
        };

        AlignmentAssessment assessment = stubPort.checkAlignment(activePatient, variantPatient);
        assertThat(assessment).isInstanceOf(AlignmentAssessment.ObjectsOrthogonal.class);

        Patient merged = stubPort.mergeResource(activePatient, variantPatient);

        // Crucial architectural invariant: reference identity preserved
        assertSame(activePatient, merged);
        assertThat(merged).isSameAs(activePatient);
        assertThat(activePatient.getTelecom()).hasSize(1);
        assertThat(activePatient.getTelecomFirstRep().getValue()).isEqualTo("555-1234");
        assertThat(activePatient.getNameFirstRep().getFamily()).isEqualTo("Smith");
    }

    @Test
    @DisplayName("checkAlignment evaluates relationship outcomes")
    void checkAlignmentOutcomes() {
        Patient p1 = new Patient();
        p1.setId("Patient/1");

        Patient p2 = new Patient();
        p2.setId("Patient/1");

        ResourceAlignmentPort stub = new ResourceAlignmentPort() {
            @Override
            public <T extends IBaseResource> AlignmentAssessment checkAlignment(T currentActiveResource, T variantResource) {
                return new AlignmentAssessment.ObjectsCoincident("No changes detected");
            }

            @Override
            public <T extends IBaseResource> T mergeResource(T currentActiveResource, T variantResource) {
                return currentActiveResource;
            }
        };

        AlignmentAssessment assessment = stub.checkAlignment(p1, p2);
        assertThat(assessment).isInstanceOf(AlignmentAssessment.ObjectsCoincident.class);
    }
}
