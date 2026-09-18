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

package net.fhirfactory.harmonia.paradeigma.common.generator;

import net.fhirfactory.harmonia.paradeigma.common.model.OrderProfile;
import net.fhirfactory.harmonia.paradeigma.common.model.PatientProfile;
import net.fhirfactory.harmonia.paradeigma.common.model.ResultProfile;
import net.fhirfactory.harmonia.paradeigma.common.model.VisitProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SyntheticGeneratorDeterminismTest {

    @Test
    @DisplayName("Same random seed produces 100% identical patient and order sequences")
    void testDeterminismAcrossInstances() {
        SyntheticPatientGenerator gen1 = new SyntheticPatientGenerator(99999L);
        SyntheticPatientGenerator gen2 = new SyntheticPatientGenerator(99999L);

        for (int i = 0; i < 20; i++) {
            PatientProfile p1 = gen1.generatePatient();
            PatientProfile p2 = gen2.generatePatient();

            assertThat(p1.getPatientId()).isEqualTo(p2.getPatientId());
            assertThat(p1.getGivenName()).isEqualTo(p2.getGivenName());
            assertThat(p1.getFamilyName()).isEqualTo(p2.getFamilyName());
            assertThat(p1.getGender()).isEqualTo(p2.getGender());
            assertThat(p1.getDateOfBirth()).isEqualTo(p2.getDateOfBirth());
            assertThat(p1.getAddressLine1()).isEqualTo(p2.getAddressLine1());
        }

        SyntheticOrderGenerator oGen1 = new SyntheticOrderGenerator(88888L);
        SyntheticOrderGenerator oGen2 = new SyntheticOrderGenerator(88888L);

        PatientProfile samplePatient = gen1.generatePatient();
        VisitProfile sampleVisit = gen1.generateVisit(samplePatient);

        for (int i = 0; i < 10; i++) {
            OrderProfile o1 = oGen1.generateLabOrder(samplePatient, sampleVisit);
            OrderProfile o2 = oGen2.generateLabOrder(samplePatient, sampleVisit);

            assertThat(o1.getPlacerOrderNumber()).isEqualTo(o2.getPlacerOrderNumber());
            assertThat(o1.getUniversalServiceId()).isEqualTo(o2.getUniversalServiceId());
            assertThat(o1.getUniversalServiceText()).isEqualTo(o2.getUniversalServiceText());
            assertThat(o1.getPriority()).isEqualTo(o2.getPriority());
        }
    }

    @Test
    @DisplayName("Result generator creates realistic observations for lab and imaging orders")
    void testResultGenerator() {
        SyntheticOrderGenerator orderGen = new SyntheticOrderGenerator(777L);
        SyntheticResultGenerator resultGen = new SyntheticResultGenerator(777L);

        PatientProfile patient = new SyntheticPatientGenerator(777L).generatePatient();
        VisitProfile visit = new SyntheticPatientGenerator(777L).generateVisit(patient);

        // Lab Result
        OrderProfile labOrder = orderGen.generateLabOrder(patient, visit, "CBC");
        ResultProfile labResult = resultGen.generateResult(labOrder);

        assertThat(labResult.getPlacerOrderNumber()).isEqualTo(labOrder.getPlacerOrderNumber());
        assertThat(labResult.getObservations()).isNotEmpty();
        assertThat(labResult.getObservations().get(0).getObservationText()).isEqualTo("Haemoglobin");

        // Imaging Result
        OrderProfile imgOrder = orderGen.generateImagingOrder(patient, visit, "XR_CHEST");
        ResultProfile imgResult = resultGen.generateResult(imgOrder);

        assertThat(imgResult.getDiagnosticReportNarrative()).contains("Normal radiograph of the chest");
        assertThat(imgResult.getObservations()).isNotEmpty();
    }
}
