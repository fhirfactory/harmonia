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

package net.fhirfactory.harmonia.paradeigma.common.hl7;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;
import net.fhirfactory.harmonia.paradeigma.common.generator.SyntheticOrderGenerator;
import net.fhirfactory.harmonia.paradeigma.common.generator.SyntheticPatientGenerator;
import net.fhirfactory.harmonia.paradeigma.common.generator.SyntheticResultGenerator;
import net.fhirfactory.harmonia.paradeigma.common.model.OrderProfile;
import net.fhirfactory.harmonia.paradeigma.common.model.PatientProfile;
import net.fhirfactory.harmonia.paradeigma.common.model.ResultProfile;
import net.fhirfactory.harmonia.paradeigma.common.model.VisitProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Hl7MessageBuildersTest {

    private SyntheticPatientGenerator patientGen;
    private SyntheticOrderGenerator orderGen;
    private SyntheticResultGenerator resultGen;

    @BeforeEach
    void setUp() {
        patientGen = new SyntheticPatientGenerator(12345L);
        orderGen = new SyntheticOrderGenerator(12345L);
        resultGen = new SyntheticResultGenerator(12345L);
    }

    @Test
    @DisplayName("Build and parse valid ADT^A04 (Registration)")
    void testBuildAdtA04() throws HL7Exception {
        PatientProfile patient = patientGen.generatePatient("PAT-100001");
        VisitProfile visit = patientGen.generateVisit(patient);

        String raw = Hl7MessageBuilders.buildAdtA04(patient, visit);
        assertThat(raw).contains("ADT^A04");
        assertThat(raw).contains("PAT-100001");

        Message parsed = Hl7Parsers.parse(raw);
        Terser terser = new Terser(parsed);

        assertThat(terser.get("/.MSH-9-1")).isEqualTo("ADT");
        assertThat(terser.get("/.MSH-9-2")).isEqualTo("A04");
        assertThat(terser.get("/.PID-3-1")).isEqualTo("PAT-100001");
        assertThat(terser.get("/.PID-5-1")).isEqualTo(patient.getFamilyName());
        assertThat(terser.get("/.PID-5-2")).isEqualTo(patient.getGivenName());
    }

    @Test
    @DisplayName("Build and parse valid ADT^A01 (Admit) and ADT^A02 (Transfer) and ADT^A03 (Discharge)")
    void testBuildAdtLifecycle() throws HL7Exception {
        PatientProfile patient = patientGen.generatePatient("PAT-100002");
        VisitProfile visit = patientGen.generateVisit(patient);

        // A01 Admit
        String a01 = Hl7MessageBuilders.buildAdtA01(patient, visit);
        assertThat(a01).contains("ADT^A01");
        Message parsedA01 = Hl7Parsers.parse(a01);
        assertThat(new Terser(parsedA01).get("/.MSH-9-2")).isEqualTo("A01");

        // A02 Transfer
        String a02 = Hl7MessageBuilders.buildAdtA02(patient, visit, "ICU-EAST", "401", "B");
        assertThat(a02).contains("ADT^A02");
        assertThat(a02).contains("ICU-EAST");
        Message parsedA02 = Hl7Parsers.parse(a02);
        assertThat(new Terser(parsedA02).get("/.MSH-9-2")).isEqualTo("A02");
        assertThat(new Terser(parsedA02).get("/.PV1-3-1")).isEqualTo("ICU-EAST");

        // A08 Update
        String a08 = Hl7MessageBuilders.buildAdtA08(patient, visit);
        assertThat(a08).contains("ADT^A08");

        // A03 Discharge
        String a03 = Hl7MessageBuilders.buildAdtA03(patient, visit);
        assertThat(a03).contains("ADT^A03");
        Message parsedA03 = Hl7Parsers.parse(a03);
        assertThat(new Terser(parsedA03).get("/.MSH-9-2")).isEqualTo("A03");
    }

    @Test
    @DisplayName("Build and parse valid ORM^O01 for Laboratory and Diagnostic Imaging")
    void testBuildOrmO01() throws HL7Exception {
        PatientProfile patient = patientGen.generatePatient("PAT-100003");
        VisitProfile visit = patientGen.generateVisit(patient);

        // Lab Order
        OrderProfile labOrder = orderGen.generateLabOrder(patient, visit, "CBC");
        String labOrm = Hl7MessageBuilders.buildOrmO01(labOrder, patient, visit);
        assertThat(labOrm).contains("ORM^O01");
        assertThat(labOrm).contains("CBC");

        Message parsedLab = Hl7Parsers.parse(labOrm);
        Terser terserLab = new Terser(parsedLab);
        assertThat(terserLab.get("/.MSH-9-1")).isEqualTo("ORM");
        assertThat(terserLab.get("/.MSH-9-2")).isEqualTo("O01");
        assertThat(terserLab.get("/.OBR-4-1")).isEqualTo("CBC");

        // Imaging Order
        OrderProfile radOrder = orderGen.generateImagingOrder(patient, visit, "XR_CHEST");
        String radOrm = Hl7MessageBuilders.buildOrmO01(radOrder, patient, visit);
        assertThat(radOrm).contains("XR_CHEST");

        Message parsedRad = Hl7Parsers.parse(radOrm);
        Terser terserRad = new Terser(parsedRad);
        assertThat(terserRad.get("/.OBR-4-1")).isEqualTo("XR_CHEST");
    }

    @Test
    @DisplayName("Build and parse valid ORU^R01 with multiple OBX observations")
    void testBuildOruR01() throws HL7Exception {
        PatientProfile patient = patientGen.generatePatient("PAT-100004");
        VisitProfile visit = patientGen.generateVisit(patient);
        OrderProfile labOrder = orderGen.generateLabOrder(patient, visit, "CBC");
        ResultProfile result = resultGen.generateResult(labOrder);

        String oru = Hl7MessageBuilders.buildOruR01(result, patient, visit);
        assertThat(oru).contains("ORU^R01");
        assertThat(oru).contains("OBX|1|");
        assertThat(oru).contains("Haemoglobin");

        Message parsed = Hl7Parsers.parse(oru);
        Terser terser = new Terser(parsed);
        assertThat(terser.get("/.MSH-9-1")).isEqualTo("ORU");
        assertThat(terser.get("/.MSH-9-2")).isEqualTo("R01");
        assertThat(terser.get("/.OBR-4-1")).isEqualTo("CBC");
        assertThat(terser.get("/.OBX(0)-3-2")).isEqualTo("Haemoglobin");
    }
}
