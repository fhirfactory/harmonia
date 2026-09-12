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

package net.fhirfactory.hie.mllpgateway.hl7;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import net.fhirfactory.hie.mllpgateway.hl7.factories.*;
import net.fhirfactory.hie.model.topic.Topic;
import org.hl7.fhir.r5.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AdtModularBuildersTest {

    private PipeParser pipeParser;
    private AdtMessageExtractor extractor;
    private AdtPatientResourceBuilder patientBuilder;
    private AdtRelatedPersonResourceBuilder relatedPersonBuilder;
    private AdtPractitionerResourceBuilder practitionerBuilder;
    private AdtEncounterResourceBuilder encounterBuilder;
    private AdtBundleResourceBuilder bundleBuilder;
    private AdtCommunicationResourceBuilder communicationBuilder;
    private AdtTaskResourceBuilder taskBuilder;
    private AdtProvenanceResourceBuilder provenanceBuilder;

    @BeforeEach
    void setUp() {
        this.pipeParser = new DefaultHapiContext().getPipeParser();
        this.extractor = new AdtMessageExtractor();
        this.patientBuilder = new AdtPatientResourceBuilder(extractor);
        this.relatedPersonBuilder = new AdtRelatedPersonResourceBuilder(extractor);
        this.practitionerBuilder = new AdtPractitionerResourceBuilder(extractor);
        this.encounterBuilder = new AdtEncounterResourceBuilder(extractor);
        this.bundleBuilder = new AdtBundleResourceBuilder();
        this.communicationBuilder = new AdtCommunicationResourceBuilder(extractor);
        this.taskBuilder = new AdtTaskResourceBuilder(extractor);
        this.provenanceBuilder = new AdtProvenanceResourceBuilder(extractor);
    }

    @Test
    void testExtractorHelpers() {
        assertThat(extractor.mapAdministrativeGender("M")).isEqualTo(Enumerations.AdministrativeGender.MALE);
        assertThat(extractor.mapAdministrativeGender("female")).isEqualTo(Enumerations.AdministrativeGender.FEMALE);
        assertThat(extractor.mapAdministrativeGender("o")).isEqualTo(Enumerations.AdministrativeGender.OTHER);
        assertThat(extractor.mapAdministrativeGender("unknown")).isEqualTo(Enumerations.AdministrativeGender.UNKNOWN);
        assertThat(extractor.mapAdministrativeGender(null)).isEqualTo(Enumerations.AdministrativeGender.UNKNOWN);

        assertThat(extractor.mapMaritalStatus("M").getCodingFirstRep().getCode()).isEqualTo("M");
        assertThat(extractor.mapMaritalStatus("S").getText()).isEqualTo("Never Married");
        assertThat(extractor.mapMaritalStatus(null).getCoding()).isEmpty();

        assertThat(extractor.determineTaskStatus("A01")).isEqualTo(Task.TaskStatus.REQUESTED);
        assertThat(extractor.determineTaskStatus("A02")).isEqualTo(Task.TaskStatus.INPROGRESS);
        assertThat(extractor.determineTaskStatus("A03")).isEqualTo(Task.TaskStatus.COMPLETED);
        assertThat(extractor.determineTaskStatus("A08")).isEqualTo(Task.TaskStatus.INPROGRESS);

        assertThat(extractor.determinePriority("E")).isEqualTo(Enumerations.RequestPriority.STAT);
        assertThat(extractor.determinePriority("U")).isEqualTo(Enumerations.RequestPriority.URGENT);
        assertThat(extractor.determinePriority("I")).isEqualTo(Enumerations.RequestPriority.ROUTINE);

        assertThat(extractor.mapEncounterStatus("A01")).isEqualTo(Enumerations.EncounterStatus.INPROGRESS);
        assertThat(extractor.mapEncounterStatus("A03")).isEqualTo(Enumerations.EncounterStatus.COMPLETED);
        assertThat(extractor.mapEncounterStatus("A05")).isEqualTo(Enumerations.EncounterStatus.PLANNED);
        assertThat(extractor.mapEncounterStatus("A11")).isEqualTo(Enumerations.EncounterStatus.CANCELLED);

        assertThat(extractor.cleanPhoneNumber("(555) 123-4567")).isEqualTo("(555) 123-4567");
        assertThat(extractor.cleanPhoneNumber(null)).isEmpty();

        assertThat(extractor.parseHl7Date("20260907101500")).isNotNull();
        assertThat(extractor.parseHl7Date(null)).isNull();

        String ack = extractor.generateFallbackAck("MSG1", "A01", "AE", "Error");
        assertThat(ack).contains("MSA|AE|MSG1|Error");
    }

    @Test
    void testPatientResourceBuilder() throws Exception {
        String hl7 = "MSH|^~\\&|EPIC|HOSPITAL|HIE|HIE_IM|20260907101500||ADT^A01|MSG-01|P|2.4\r" +
                "PID|1||PAT10099^^^HOSPITAL^MR||SMITH^JOHN^A^JR^DR||19800512|M|||123 MAIN ST^APT 4B^SPRINGFIELD^IL^62701^USA||555-1234|555-5678||M|123456789|987654321\r";
        Message msg = pipeParser.parse(hl7);
        Terser terser = new Terser(msg);

        String patientId = extractor.extractPatientId(hl7, terser);
        Patient patient = patientBuilder.buildPatient(terser, hl7, patientId);

        assertThat(patient.getId()).isEqualTo("Patient/PAT10099");
        assertThat(patient.getActive()).isTrue();
        assertThat(patient.getNameFirstRep().getFamily()).isEqualTo("SMITH");
        assertThat(patient.getNameFirstRep().getGivenAsSingleString()).isEqualTo("JOHN A");
        assertThat(patient.getNameFirstRep().getPrefixAsSingleString()).isEqualTo("DR");
        assertThat(patient.getNameFirstRep().getSuffixAsSingleString()).isEqualTo("JR");
        assertThat(patient.getGender()).isEqualTo(Enumerations.AdministrativeGender.MALE);
        assertThat(patient.getBirthDate()).isNotNull();
        assertThat(patient.getAddressFirstRep().getCity()).isEqualTo("SPRINGFIELD");
        assertThat(patient.getAddressFirstRep().getState()).isEqualTo("IL");
        assertThat(patient.getAddressFirstRep().getPostalCode()).isEqualTo("62701");
        assertThat(patient.getTelecom()).hasSize(2);
        assertThat(patient.getMaritalStatus().getCodingFirstRep().getCode()).isEqualTo("M");
    }

    @Test
    void testRelatedPersonResourceBuilder() throws Exception {
        String hl7 = "MSH|^~\\&|EPIC|HOSPITAL|HIE|HIE_IM|20260907101500||ADT^A01|MSG-01|P|2.4\r" +
                "PID|1||PAT10099\r" +
                "NK1|1|SMITH^MARY|WIFE^Wife|123 MAIN ST^^SPRINGFIELD^IL^62701|555-1234\r" +
                "NK1|2|SMITH^JANE|DAU^Daughter||555-5678\r";
        Message msg = pipeParser.parse(hl7);
        Terser terser = new Terser(msg);

        List<RelatedPerson> rps = relatedPersonBuilder.buildRelatedPersons(terser, hl7, "PAT10099");
        assertThat(rps).hasSize(2);

        RelatedPerson rp1 = rps.get(0);
        assertThat(rp1.getId()).isEqualTo("RelatedPerson/PAT10099-nk1");
        assertThat(rp1.getPatient().getReference()).isEqualTo("Patient/PAT10099");
        assertThat(rp1.getNameFirstRep().getFamily()).isEqualTo("SMITH");
        assertThat(rp1.getNameFirstRep().getGivenAsSingleString()).isEqualTo("MARY");
        assertThat(rp1.getRelationshipFirstRep().getText()).isEqualTo("Wife");
        assertThat(rp1.getTelecomFirstRep().getValue()).isEqualTo("555-1234");

        RelatedPerson rp2 = rps.get(1);
        assertThat(rp2.getId()).isEqualTo("RelatedPerson/PAT10099-nk2");
        assertThat(rp2.getNameFirstRep().getGivenAsSingleString()).isEqualTo("JANE");
    }

    @Test
    void testPractitionerResourceBuilder() throws Exception {
        String hl7 = "MSH|^~\\&|EPIC|HOSPITAL|HIE|HIE_IM|20260907101500||ADT^A01|MSG-01|P|2.4\r" +
                "PID|1||PAT10099\r" +
                "PV1|1|I|WARDA||||DOC01^JONES^ROBERT^^DR|DOC02^WILLIAMS^CLARA^^DR|DOC03^ADAMS^JOHN^^DR\r";
        Message msg = pipeParser.parse(hl7);
        Terser terser = new Terser(msg);

        List<Practitioner> practitioners = practitionerBuilder.buildPractitioners(terser, hl7);
        assertThat(practitioners).hasSize(3);

        Practitioner p1 = practitioners.get(0);
        assertThat(p1.getId()).isEqualTo("Practitioner/DOC01");
        assertThat(p1.getNameFirstRep().getFamily()).isEqualTo("JONES");
        assertThat(p1.getNameFirstRep().getGivenAsSingleString()).isEqualTo("ROBERT");

        Practitioner p2 = practitioners.get(1);
        assertThat(p2.getId()).isEqualTo("Practitioner/DOC02");
        assertThat(p2.getNameFirstRep().getFamily()).isEqualTo("WILLIAMS");

        Practitioner p3 = practitioners.get(2);
        assertThat(p3.getId()).isEqualTo("Practitioner/DOC03");
        assertThat(p3.getNameFirstRep().getFamily()).isEqualTo("ADAMS");
    }

    @Test
    void testEncounterResourceBuilder() throws Exception {
        String hl7 = "MSH|^~\\&|EPIC|HOSPITAL|HIE|HIE_IM|20260907101500||ADT^A01|MSG-01|P|2.4\r" +
                "PID|1||PAT10099\r" +
                "PV1|1|I|WARDA^RM101^BED1||||DOC01^JONES^ROBERT^^DR||||||||||||V20260907-01|||||||||||||||||||||||||20260907100000|20260907180000\r";
        Message msg = pipeParser.parse(hl7);
        Terser terser = new Terser(msg);

        List<Practitioner> practitioners = practitionerBuilder.buildPractitioners(terser, hl7);
        Encounter enc = encounterBuilder.buildEncounter(terser, hl7, "MSG-01", "A01", "PAT10099", "V20260907-01", "I", practitioners);

        assertThat(enc.getId()).isEqualTo("Encounter/V20260907-01");
        assertThat(enc.getStatus()).isEqualTo(Enumerations.EncounterStatus.INPROGRESS);
        assertThat(enc.getSubject().getReference()).isEqualTo("Patient/PAT10099");
        assertThat(enc.getIdentifierFirstRep().getValue()).isEqualTo("V20260907-01");
        assertThat(enc.getParticipant()).hasSize(1);
        assertThat(enc.getParticipantFirstRep().getActor().getReference()).isEqualTo("Practitioner/DOC01");
        assertThat(enc.getActualPeriod().getStart()).isNotNull();
        assertThat(enc.getActualPeriod().getEnd()).isNotNull();
    }

    @Test
    void testBundleResourceBuilder() {
        Patient patient = new Patient();
        patient.setId("Patient/P1");
        RelatedPerson rp = new RelatedPerson();
        rp.setId("RelatedPerson/RP1");
        Practitioner pr = new Practitioner();
        pr.setId("Practitioner/PR1");
        Encounter enc = new Encounter();
        enc.setId("Encounter/E1");

        Bundle bundle = bundleBuilder.buildBundle("MSG-01", patient, List.of(rp), List.of(pr), enc);

        assertThat(bundle.getId()).isEqualTo("bundle-MSG-01");
        assertThat(bundle.getType()).isEqualTo(Bundle.BundleType.COLLECTION);
        assertThat(bundle.getEntry()).hasSize(4);
    }

    @Test
    void testCommunicationResourceBuilder() throws Exception {
        String hl7 = "MSH|^~\\&|EPIC|HOSPITAL|HIE|HIE_IM|20260907101500||ADT^A01|MSG-01|P|2.4\r";
        Message msg = pipeParser.parse(hl7);
        Terser terser = new Terser(msg);
        Topic topic = Topic.fromHl7("2.4", "ADT", "A01", "gw-1", null, "HOSPITAL", "HIE_IM");

        Communication comm = communicationBuilder.buildCommunication(terser, topic, hl7, "MSG-01", "PAT10099", "JOHN SMITH", "EPIC", "HOSPITAL", "20260907101500");

        assertThat(comm.getId()).isEqualTo("Communication/comm-MSG-01");
        assertThat(comm.getStatus()).isEqualTo(Enumerations.EventStatus.COMPLETED);
        assertThat(comm.getPriority()).isEqualTo(Enumerations.RequestPriority.ROUTINE);
        assertThat(comm.getSubject().getReference()).isEqualTo("Patient/PAT10099");
        assertThat(comm.getSubject().getDisplay()).isEqualTo("JOHN SMITH");
        assertThat(comm.getPayload()).hasSize(1);
        Attachment att = (Attachment) comm.getPayloadFirstRep().getContent();
        assertThat(new String(att.getData(), StandardCharsets.UTF_8)).isEqualTo(hl7);
    }

    @Test
    void testTaskResourceBuilder() throws Exception {
        String hl7 = "MSH|^~\\&|EPIC|HOSPITAL|HIE|HIE_IM|20260907101500||ADT^A01|MSG-01|P|2.4\r";
        Message msg = pipeParser.parse(hl7);
        Terser terser = new Terser(msg);
        Topic topic = Topic.fromHl7("2.4", "ADT", "A01", "gw-1", null, "HOSPITAL", "HIE_IM");

        Communication comm = new Communication();
        comm.setId("Communication/comm-MSG-01");

        Bundle bundle = new Bundle();
        bundle.setId("Bundle/bundle-MSG-01");

        Task task = taskBuilder.buildTask(terser, topic, "MSG-01", "PAT10099", "JOHN SMITH", "V100", "I",
                "WARDA", "RM101", "BED1", "DOC01", "DR JONES", "EPIC", "HOSPITAL", "20260907101500", bundle, comm);

        assertThat(task.getId()).isEqualTo("Task/MSG-01");
        assertThat(task.getStatus()).isEqualTo(Task.TaskStatus.REQUESTED);
        assertThat(task.getFor().getReference()).isEqualTo("Patient/PAT10099");
        assertThat(task.getFocus().getReference()).isEqualTo("Encounter/V100");
        assertThat(task.getBasedOnFirstRep().getReference()).isEqualTo("Communication/comm-MSG-01");
        assertThat(task.getContained()).hasSize(1);
        assertThat(task.getInput()).hasSize(2); // location input + bundle payload input
    }

    @Test
    void testProvenanceResourceBuilder() throws Exception {
        String hl7 = "MSH|^~\\&|EPIC|HOSPITAL|HIE|HIE_IM|20260907101500||ADT^A01|MSG-01|P|2.4\r";
        Message msg = pipeParser.parse(hl7);
        Terser terser = new Terser(msg);
        Topic topic = Topic.fromHl7("2.4", "ADT", "A01", "gw-1", null, "HOSPITAL", "HIE_IM");

        Task task = new Task();
        task.setId("Task/MSG-01");
        Communication comm = new Communication();
        comm.setId("Communication/comm-MSG-01");

        Provenance prov = provenanceBuilder.buildProvenance(terser, topic, "MSG-01", "PAT10099", "JOHN SMITH",
                "EPIC", "HOSPITAL", "20260907101500", "WARDA", "DOC01", "DR JONES", task, comm);

        assertThat(prov.getId()).isEqualTo("Provenance/prov-MSG-01");
        assertThat(prov.getTargetFirstRep().getReference()).isEqualTo("Task/MSG-01");
        assertThat(prov.getEntityFirstRep().getWhat().getReference()).isEqualTo("Communication/comm-MSG-01");
        assertThat(prov.getPatient().getReference()).isEqualTo("Patient/PAT10099");
        assertThat(prov.getAgent()).hasSize(3); // Transmitter, Assembler, Author
    }
}
