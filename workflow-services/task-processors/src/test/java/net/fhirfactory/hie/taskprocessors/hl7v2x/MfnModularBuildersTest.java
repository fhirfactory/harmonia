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
import net.fhirfactory.hie.taskprocessors.hl7v2x.factories.Mfn2FhirBundleBuilder;
import net.fhirfactory.hie.taskprocessors.hl7v2x.factories.MfnAdministrativeResourceBuilder;
import net.fhirfactory.hie.taskprocessors.hl7v2x.factories.MfnMetadataResourceBuilder;
import net.fhirfactory.hie.taskprocessors.hl7v2x.factories.MfnPractitionerResourceBuilder;
import org.hl7.fhir.r5.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MfnModularBuildersTest {

    private PipeParser pipeParser;
    private Terser terser;

    private static final String FULL_MFN_M02 =
            "MSH|^~\\&|STAFF_APP|HOSPITAL_A|HIE_APP|HIE_DEST|20260910120000||MFN^M02|MSG-CTRL-88801|P|2.4\r" +
            "MFI|PRA^Practitioner Master File|STAFF_APP|UPD|20260910120000|20260910120000|NE\r" +
            "MFE|MUP|MFN-ENTRY-001|20260910120000|DOC-12345^SMITH^JOHN|CE\r" +
            "STF|DOC-12345^SMITH^JOHN|NPI-9876543210^^^NPI^NPI~DOC-12345^^^HOSP^MD~TAX-123^^^TAX^TAX|SMITH^JOHN^ROBERT^JR^DR^MD|PHY^Physician^HL70182|M|19750820|Y|CARD^Cardiology Department|MED^Internal Medicine|(555)555-1234^PRN^PH^^^555^5551234~(555)555-4321^WPN^FX~drsmith@hospital.org^NET^Internet^drsmith@hospital.org|100 MEDICAL PKWY^SUITE 300^METROPOLIS^NY^10001^USA^O|20200101000000|20301231235959||dr.smith@metropolishospital.org|||M^Married|Chief of Cardiology|CARDIO-CHIEF^Attending Physician|FT^Full Time|||||||||||207RC0000X^Cardiovascular Disease|USA^United States|||en^English^ISO639||2186-5^Not Hispanic or Latino\r" +
            "PRA|DOC-12345|CARD-GRP^Metro Cardiology Associates|MD^Medical Doctor|Y|CARD^Cardiology^HL70265~EP^Electrophysiology^HL70265|LIC-998877^MD^NY^20281231~DEA-123456^DEA^US^20270630|ADM^Admitting Privileges^HOSP|20050701\r" +
            "ORG|1|DEPT|ORG-CARD-01|DEPT|Metro Cardiology Clinic|200 HEALTH BLVD^^METROPOLIS^NY^10001^USA|(555)555-9000\r" +
            "AFF|1|American College of Cardiology|2400 N Street NW^Washington^DC^20037^USA|20100101^20301231\r" +
            "LAN|1|en^English^ISO639|1^Read^HL70403|1^Fluent^HL70404\r" +
            "LAN|2|es^Spanish^ISO639|3^Speak^HL70403|2^Good^HL70404\r" +
            "EDU|1|Cardiology|MD^Doctor of Medicine|20000520|Johns Hopkins University School of Medicine|733 N Broadway^Baltimore^MD^21205^USA\r" +
            "CER|1|BC-CARD^Board Certified in Cardiovascular Disease|CERT-887766|American Board of Internal Medicine\r" +
            "NTE|1|P|Specializes in interventional cardiac procedures and electrophysiology.";

    @BeforeEach
    void setUp() throws Exception {
        DefaultHapiContext hapiContext = new DefaultHapiContext();
        pipeParser = hapiContext.getPipeParser();
        try {
            Message msg = pipeParser.parse(FULL_MFN_M02);
            terser = new Terser(msg);
        } catch (Exception ignored) {
        }
    }

    @Test
    void testHl7v2ParsingSupportForPractitioner() {
        assertThat(Hl7v2ParsingSupport.isHl7Message(FULL_MFN_M02)).isTrue();
        assertThat(Hl7v2ParsingSupport.cleanId("Practitioner/DOC-12345")).isEqualTo("DOC-12345");
        assertThat(Hl7v2ParsingSupport.cleanId("PractitionerRole/DOC-12345-role")).isEqualTo("DOC-12345-role");
        assertThat(Hl7v2ParsingSupport.cleanId("Organization/hospital_a")).isEqualTo("hospital_a");
        assertThat(Hl7v2ParsingSupport.cleanId("Location/loc-card")).isEqualTo("loc-card");
        assertThat(Hl7v2ParsingSupport.cleanPhoneNumber("(555) 555-1234^PRN")).isEqualTo("(555) 555-1234");
        assertThat(Hl7v2ParsingSupport.mapAdministrativeGender("M")).isEqualTo(Enumerations.AdministrativeGender.MALE);
        assertThat(Hl7v2ParsingSupport.mapAdministrativeGender("F")).isEqualTo(Enumerations.AdministrativeGender.FEMALE);
    }

    @Test
    void testMfnPractitionerResourceBuilder() {
        MfnPractitionerResourceBuilder builder = new MfnPractitionerResourceBuilder();
        List<Practitioner> practitioners = builder.buildPractitioners(terser, FULL_MFN_M02, "MSG-CTRL-88801");

        assertThat(practitioners).hasSize(1);
        Practitioner practitioner = practitioners.get(0);
        assertThat(practitioner.getIdPart()).isEqualTo("DOC-12345");
        assertThat(practitioner.getActive()).isTrue();

        // Name
        assertThat(practitioner.getName()).isNotEmpty();
        HumanName name = practitioner.getNameFirstRep();
        assertThat(name.getFamily()).isEqualTo("SMITH");
        assertThat(name.getGivenAsSingleString()).contains("JOHN");
        assertThat(name.getPrefixAsSingleString()).contains("DR");
        assertThat(name.getSuffixAsSingleString()).contains("JR");

        // Identifiers
        assertThat(practitioner.getIdentifier()).isNotEmpty();
        assertThat(practitioner.getIdentifier().stream().anyMatch(id -> "DOC-12345".equals(id.getValue()))).isTrue();
        assertThat(practitioner.getIdentifier().stream().anyMatch(id -> "NPI-9876543210".equals(id.getValue()))).isTrue();

        // Gender and BirthDate
        assertThat(practitioner.getGender()).isEqualTo(Enumerations.AdministrativeGender.MALE);
        assertThat(practitioner.getBirthDate()).isNotNull();

        // Telecom and Address
        assertThat(practitioner.getTelecom()).isNotEmpty();
        assertThat(practitioner.getTelecom().stream().anyMatch(t -> t.getSystem() == ContactPoint.ContactPointSystem.EMAIL)).isTrue();
        assertThat(practitioner.getAddress()).isNotEmpty();
        assertThat(practitioner.getAddressFirstRep().getCity()).isEqualTo("METROPOLIS");

        // Languages
        assertThat(practitioner.getCommunication()).isNotEmpty();

        // Qualifications
        assertThat(practitioner.getQualification()).isNotEmpty();

        // Extensions
        assertThat(practitioner.getExtension()).isNotEmpty();
    }

    @Test
    void testMfnPractitionerRoleBuilder() {
        MfnPractitionerResourceBuilder practitionerBuilder = new MfnPractitionerResourceBuilder();
        MfnAdministrativeResourceBuilder adminBuilder = new MfnAdministrativeResourceBuilder();

        List<Practitioner> practitioners = practitionerBuilder.buildPractitioners(terser, FULL_MFN_M02, "MSG-CTRL-88801");
        List<Organization> orgs = adminBuilder.buildOrganizations(terser, FULL_MFN_M02, "HOSPITAL_A", "HIE_DEST");
        List<Location> locs = adminBuilder.buildLocations(terser, FULL_MFN_M02);

        List<PractitionerRole> roles = practitionerBuilder.buildPractitionerRoles(terser, FULL_MFN_M02, practitioners, orgs, locs);
        assertThat(roles).hasSize(1);

        PractitionerRole role = roles.get(0);
        assertThat(role.getIdPart()).isEqualTo("DOC-12345-role");
        assertThat(role.getPractitioner().getReference()).isEqualTo("Practitioner/DOC-12345");
        assertThat(role.getActive()).isTrue();
        assertThat(role.getOrganization()).isNotNull();
        assertThat(role.getCode()).isNotEmpty();
        assertThat(role.getSpecialty()).isNotEmpty();
        assertThat(role.getPeriod()).isNotNull();
    }

    @Test
    void testMfnAdministrativeResourceBuilder() {
        MfnAdministrativeResourceBuilder builder = new MfnAdministrativeResourceBuilder();
        List<Organization> orgs = builder.buildOrganizations(terser, FULL_MFN_M02, "HOSPITAL_A", "HIE_DEST");
        List<Location> locs = builder.buildLocations(terser, FULL_MFN_M02);

        assertThat(orgs).isNotEmpty();
        assertThat(orgs.stream().anyMatch(o -> o.getName().contains("HOSPITAL_A"))).isTrue();
        assertThat(orgs.stream().anyMatch(o -> o.getName().contains("Cardiology"))).isTrue();
        assertThat(orgs.stream().anyMatch(o -> o.getName().contains("Johns Hopkins"))).isTrue();

        assertThat(locs).isNotEmpty();
        assertThat(locs.stream().anyMatch(l -> l.getName().contains("Cardiology") || l.getName().contains("Clinic"))).isTrue();
    }

    @Test
    void testMfnMetadataResourceBuilder() {
        MfnMetadataResourceBuilder builder = new MfnMetadataResourceBuilder();
        Communication comm = builder.buildCommunication(terser, FULL_MFN_M02, "MSG-CTRL-88801", "M02",
                "DOC-12345", "DR JOHN ROBERT SMITH JR MD", "STAFF_APP", "HOSPITAL_A", "20260910120000");

        assertThat(comm).isNotNull();
        assertThat(comm.getIdPart()).isEqualTo("comm-MSG-CTRL-88801");
        assertThat(comm.getPayload()).isNotEmpty();
        assertThat(comm.getSubject().getReference()).isEqualTo("Practitioner/DOC-12345");

        Bundle bundle = new Bundle();
        bundle.setId("Bundle/bundle-001");
        Practitioner p = new Practitioner();
        p.setId("Practitioner/DOC-12345");

        Provenance prov = builder.buildProvenance(bundle, p, "MSG-CTRL-88801", "M02", "STAFF_APP", "HOSPITAL_A", "mfn2fhir-bundle", "MFN to FHIR Bundle Activity");
        assertThat(prov).isNotNull();
        assertThat(prov.getTarget()).isNotEmpty();
        assertThat(prov.getAgent()).isNotEmpty();
    }

    @Test
    void testMfn2FhirBundleBuilder() {
        Mfn2FhirBundleBuilder bundleBuilder = new Mfn2FhirBundleBuilder();
        Task parentTask = new Task();
        parentTask.setId("Task/TASK-MFN-001");

        Bundle bundle = bundleBuilder.createBundleFromMfn(FULL_MFN_M02, parentTask, "mfn2fhir-bundle", "MFN to FHIR Bundle Activity");
        assertThat(bundle).isNotNull();
        assertThat(bundle.getType()).isEqualTo(Bundle.BundleType.COLLECTION);
        assertThat(bundle.getEntry()).isNotEmpty();

        assertThat(parentTask.getFor().getReference()).isEqualTo("Practitioner/DOC-12345");
        assertThat(parentTask.getFocus().getReference()).isEqualTo("PractitionerRole/DOC-12345-role");

        // Verify resource types in bundle
        long practCount = bundle.getEntry().stream().filter(e -> e.getResource() instanceof Practitioner).count();
        long roleCount = bundle.getEntry().stream().filter(e -> e.getResource() instanceof PractitionerRole).count();
        long orgCount = bundle.getEntry().stream().filter(e -> e.getResource() instanceof Organization).count();
        long locCount = bundle.getEntry().stream().filter(e -> e.getResource() instanceof Location).count();
        long commCount = bundle.getEntry().stream().filter(e -> e.getResource() instanceof Communication).count();
        long provCount = bundle.getEntry().stream().filter(e -> e.getResource() instanceof Provenance).count();

        assertThat(practCount).isEqualTo(1);
        assertThat(roleCount).isEqualTo(1);
        assertThat(orgCount).isGreaterThanOrEqualTo(1);
        assertThat(locCount).isGreaterThanOrEqualTo(1);
        assertThat(commCount).isEqualTo(1);
        assertThat(provCount).isEqualTo(1);
    }

    @Test
    void testMfnMessageExtractor() {
        MfnMessageExtractor extractor = new MfnMessageExtractor();

        // 1. String input
        Task task1 = new Task();
        Task.TaskInputComponent in1 = task1.addInput();
        in1.setValue(new StringType(FULL_MFN_M02));
        MfnMessageExtractor.ExtractedInputResult res1 = extractor.extractMfnMessageFromTask(task1, null);
        assertThat(res1.getRawMessage()).isEqualTo(FULL_MFN_M02);
        assertThat(res1.getOriginInput()).isEqualTo(in1);

        // 2. Attachment input
        Task task2 = new Task();
        Task.TaskInputComponent in2 = task2.addInput();
        Attachment att = new Attachment();
        att.setData(FULL_MFN_M02.getBytes(StandardCharsets.UTF_8));
        in2.setValue(att);
        MfnMessageExtractor.ExtractedInputResult res2 = extractor.extractMfnMessageFromTask(task2, null);
        assertThat(res2.getRawMessage()).isEqualTo(FULL_MFN_M02);

        // 3. Fallback input
        Task task3 = new Task();
        MfnMessageExtractor.ExtractedInputResult res3 = extractor.extractMfnMessageFromTask(task3, FULL_MFN_M02);
        assertThat(res3.getRawMessage()).isEqualTo(FULL_MFN_M02);
    }

    @Test
    void testDeactivationEventCode() {
        String deactivatedMsg = FULL_MFN_M02.replace("MFE|MUP|", "MFE|MDL|");
        MfnPractitionerResourceBuilder builder = new MfnPractitionerResourceBuilder();
        List<Practitioner> practitioners = builder.buildPractitioners(null, deactivatedMsg, "MSG-CTRL-88801");
        assertThat(practitioners).hasSize(1);
        assertThat(practitioners.get(0).getActive()).isFalse();
    }
}
