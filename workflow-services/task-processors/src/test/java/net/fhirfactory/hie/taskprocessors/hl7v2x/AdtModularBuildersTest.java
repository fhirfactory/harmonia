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

package net.fhirfactory.hie.taskprocessors.hl7v2x;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import net.fhirfactory.hie.taskprocessors.hl7v2x.common.Hl7v2ParsingSupport;
import net.fhirfactory.hie.taskprocessors.hl7v2x.factories.*;
import org.hl7.fhir.r5.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AdtModularBuildersTest {

    private PipeParser pipeParser;
    private Terser terser;

    private static final String SAMPLE_HL7 =
            "MSH|^~\\&|PAS_APP|FAC_A|HIE_APP|HIE_FAC|20260910120000||ADT^A01|MSG-CTRL-1234|P|2.4\r" +
            "EVN|A01|20260910120000|||DOC-OP-01^OPERATOR^EVA\r" +
            "PID|1||MRN-9999^^^HOSP||SMITH^JOHN^A^MR^JR||19800101|M||2106-3^White^HL70005|123 MAIN ST^^BOSTON^MA^02101^USA||(555)000-1111|||S||ACC-123|SSN-111-22-3333|||2135-2^Not Hispanic^HL70005|BOSTON|Y|2|USA|||20260910110000|N\r" +
            "PD1||||DOC-PCP-01^WELBY^MARCUS\r" +
            "NK1|1|SMITH^JANE|SPO^Spouse|123 MAIN ST^^BOSTON^MA^02101|(555)000-2222\r" +
            "PV1|1|I|WARD3^301^A^HOSP_MAIN|||DOC-PRIOR^BROWN^CHARLIE|DOC-ATT-01^WILLIAMS^ROBERT|DOC-REF-01^JOHNSON^SARAH|DOC-CON-01^DAVIS^EMILY||||1||||DOC-ADM-01^TAYLOR^JAMES||VISIT-777|||||||||||||||||||||||||20260910100000|20260912150000\r" +
            "DG1|1|I10|R07.9^Chest Pain^I10|Chest pain unspecified|20260910103000|A\r" +
            "AL1|1|DA|PENICILLIN^Penicillin|SV^Severe|ANAPHYLAXIS^Anaphylaxis|20260910\r" +
            "OBX|1|NM|883-9^ABO group^LN||O+|||||F|||20260910110000\r" +
            "GT1|1||SMITH^JOHN||123 MAIN ST^^BOSTON^MA^02101|(555)000-1111|||||SELF\r" +
            "IN1|1|PLAN-B|INS-99|AETNA||||GRP-200|GROUP HEALTH|||||||SMITH^JOHN|1|||||||||||||||||||POL-445566";

    @BeforeEach
    void setUp() throws Exception {
        DefaultHapiContext hapiContext = new DefaultHapiContext();
        pipeParser = hapiContext.getPipeParser();
        Message msg = pipeParser.parse(SAMPLE_HL7);
        terser = new Terser(msg);
    }

    @Test
    void testHl7v2ParsingSupport() {
        assertThat(Hl7v2ParsingSupport.isHl7Message(SAMPLE_HL7)).isTrue();
        assertThat(Hl7v2ParsingSupport.cleanId("Patient/12345")).isEqualTo("12345");
        assertThat(Hl7v2ParsingSupport.cleanPhoneNumber("(555) 123-4567^EXT")).isEqualTo("(555) 123-4567");
        assertThat(Hl7v2ParsingSupport.mapAdministrativeGender("M")).isEqualTo(Enumerations.AdministrativeGender.MALE);
        assertThat(Hl7v2ParsingSupport.mapAdministrativeGender("F")).isEqualTo(Enumerations.AdministrativeGender.FEMALE);
        assertThat(Hl7v2ParsingSupport.mapMaritalStatus("M").getText()).isEqualTo("Married");
        assertThat(Hl7v2ParsingSupport.mapEncounterStatus("A03")).isEqualTo(Enumerations.EncounterStatus.COMPLETED);
        assertThat(Hl7v2ParsingSupport.extractPatientId(terser, SAMPLE_HL7)).isEqualTo("MRN-9999");
        assertThat(Hl7v2ParsingSupport.extractVisitNumber(terser, SAMPLE_HL7)).isEqualTo("VISIT-777");
    }

    @Test
    void testAdtPatientResourceBuilder() {
        AdtPatientResourceBuilder builder = new AdtPatientResourceBuilder();
        Patient patient = builder.buildPatient(terser, SAMPLE_HL7, "MSG-CTRL-1234");

        assertThat(patient).isNotNull();
        assertThat(patient.getIdPart()).isEqualTo("MRN-9999");
        assertThat(patient.getNameFirstRep().getFamily()).isEqualTo("SMITH");
        assertThat(patient.getNameFirstRep().getGivenAsSingleString()).contains("JOHN");
        assertThat(patient.getGender()).isEqualTo(Enumerations.AdministrativeGender.MALE);
        assertThat(patient.getExtension()).isNotEmpty();
    }

    @Test
    void testAdtEncounterResourceBuilder() {
        AdtEncounterResourceBuilder builder = new AdtEncounterResourceBuilder();
        List<Practitioner> practitioners = builder.buildPractitioners(terser, SAMPLE_HL7);
        List<Location> locations = builder.buildLocations(terser, SAMPLE_HL7);
        Encounter encounter = builder.buildEncounter(terser, SAMPLE_HL7, "MSG-CTRL-1234", "A01", "MRN-9999", practitioners, locations);

        assertThat(practitioners).isNotEmpty();
        assertThat(locations).isNotEmpty();
        assertThat(encounter).isNotNull();
        assertThat(encounter.getSubject().getReference()).isEqualTo("Patient/MRN-9999");
        assertThat(encounter.getParticipant()).hasSize(practitioners.size());
        assertThat(encounter.getLocation()).hasSize(locations.size());
    }

    @Test
    void testAdtAdministrativeResourceBuilder() {
        AdtAdministrativeResourceBuilder builder = new AdtAdministrativeResourceBuilder();
        List<RelatedPerson> relatedPersons = builder.buildRelatedPersons(terser, SAMPLE_HL7, "MRN-9999");
        List<Organization> orgs = builder.buildOrganizations(terser, SAMPLE_HL7, "FAC_A", "HIE_FAC");

        assertThat(relatedPersons).hasSize(2); // NK1 and GT1
        assertThat(orgs).hasSize(2);
    }

    @Test
    void testAdtClinicalResourceBuilder() {
        AdtClinicalResourceBuilder builder = new AdtClinicalResourceBuilder();
        Encounter enc = new Encounter();
        enc.setId("Encounter/enc-001");

        List<Condition> conditions = builder.buildConditions(terser, SAMPLE_HL7, "MRN-9999", enc);
        List<AllergyIntolerance> allergies = builder.buildAllergies(terser, SAMPLE_HL7, "MRN-9999");
        List<Observation> observations = builder.buildObservations(terser, SAMPLE_HL7, "MRN-9999", enc);
        List<Coverage> coverages = builder.buildCoverages(terser, SAMPLE_HL7, "MRN-9999");

        assertThat(conditions).isNotEmpty();
        assertThat(conditions.get(0).getEncounter().getReference()).isEqualTo("Encounter/enc-001");
        assertThat(allergies).isNotEmpty();
        assertThat(observations).isNotEmpty();
        assertThat(coverages).isNotEmpty();
    }

    @Test
    void testAdtMetadataResourceBuilder() {
        AdtMetadataResourceBuilder builder = new AdtMetadataResourceBuilder();
        Communication comm = builder.buildCommunication(terser, SAMPLE_HL7, "MSG-CTRL-1234", "A01",
                "MRN-9999", "JOHN SMITH", "PAS_APP", "FAC_A", "20260910120000");
        assertThat(comm).isNotNull();
        assertThat(comm.getIdPart()).isEqualTo("comm-MSG-CTRL-1234");
        assertThat(comm.getPayload()).isNotEmpty();

        Bundle bundle = new Bundle();
        bundle.setId("Bundle/bundle-001");
        Patient p = new Patient();
        p.setId("Patient/MRN-9999");

        Provenance prov = builder.buildProvenance(bundle, p, "MSG-CTRL-1234", "A01", "PAS_APP", "FAC_A", "adt2fhir-mapper", "ADT to FHIR Mapper Activity");
        assertThat(prov).isNotNull();
        assertThat(prov.getTarget()).isNotEmpty();
        assertThat(prov.getPatient().getReference()).isEqualTo("Patient/MRN-9999");
    }

    @Test
    void testAdt2FhirBundleBuilder() {
        Adt2FhirBundleBuilder bundleBuilder = new Adt2FhirBundleBuilder();
        Task parentTask = new Task();
        parentTask.setId("Task/T-001");

        Bundle bundle = bundleBuilder.createBundleFromAdt(SAMPLE_HL7, parentTask, "adt2fhir-mapper", "ADT to FHIR Mapper Activity");
        assertThat(bundle).isNotNull();
        assertThat(bundle.getType()).isEqualTo(Bundle.BundleType.COLLECTION);
        assertThat(bundle.getEntry()).isNotEmpty();
        assertThat(parentTask.getFor().getReference()).isEqualTo("Patient/MRN-9999");
        assertThat(parentTask.getFocus().getReference()).startsWith("Encounter/");
    }
}
