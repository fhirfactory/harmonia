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

package net.fhirfactory.harmonia.mllpgateway.hl7;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.NoValidation;
import net.fhirfactory.harmonia.mllpgateway.hl7.factories.MfnCommunicationResourceBuilder;
import net.fhirfactory.harmonia.mllpgateway.hl7.factories.MfnTaskResourceBuilder;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.hl7.fhir.r5.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class MfnModularBuildersTest {

    private PipeParser pipeParser;
    private MfnMessageExtractor extractor;
    private MfnCommunicationResourceBuilder communicationBuilder;
    private MfnTaskResourceBuilder taskBuilder;

    private static final String FULL_MFN_M02 =
            "MSH|^~\\&|STAFF_APP|HOSPITAL_A|HIE_APP|HIE_DEST|20260910120000||MFN^M02|MSG-CTRL-99901|P|2.4\r" +
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
    void setUp() {
        HapiContext hapiContext = new DefaultHapiContext();
        hapiContext.setValidationContext(new NoValidation());
        this.pipeParser = hapiContext.getPipeParser();
        this.extractor = new MfnMessageExtractor();
        this.communicationBuilder = new MfnCommunicationResourceBuilder(extractor);
        this.taskBuilder = new MfnTaskResourceBuilder(extractor);
    }

    @Test
    @DisplayName("Test MfnMessageExtractor helper and mapping functions")
    void testExtractorHelpers() {
        assertThat(extractor.mapAdministrativeGender("M")).isEqualTo(Enumerations.AdministrativeGender.MALE);
        assertThat(extractor.mapAdministrativeGender("female")).isEqualTo(Enumerations.AdministrativeGender.FEMALE);
        assertThat(extractor.mapAdministrativeGender("o")).isEqualTo(Enumerations.AdministrativeGender.OTHER);
        assertThat(extractor.mapAdministrativeGender("unknown")).isEqualTo(Enumerations.AdministrativeGender.UNKNOWN);
        assertThat(extractor.mapAdministrativeGender(null)).isEqualTo(Enumerations.AdministrativeGender.UNKNOWN);

        assertThat(extractor.mapMaritalStatus("M").getCodingFirstRep().getCode()).isEqualTo("M");
        assertThat(extractor.mapMaritalStatus("S").getText()).isEqualTo("Never Married");
        assertThat(extractor.mapMaritalStatus(null).getCoding()).isEmpty();

        assertThat(extractor.determineTaskStatus("MAD")).isEqualTo(Task.TaskStatus.REQUESTED);
        assertThat(extractor.determineTaskStatus("MUP")).isEqualTo(Task.TaskStatus.INPROGRESS);
        assertThat(extractor.determineTaskStatus("MDL")).isEqualTo(Task.TaskStatus.COMPLETED);
        assertThat(extractor.determineTaskStatus("MDC")).isEqualTo(Task.TaskStatus.COMPLETED);
        assertThat(extractor.determineTaskStatus(null)).isEqualTo(Task.TaskStatus.REQUESTED);

        assertThat(extractor.getTriggerEventDescription("M02")).contains("Staff/Practitioner");
        assertThat(extractor.cleanPhoneNumber("(555) 555-1234")).isEqualTo("(555) 555-1234");
        assertThat(extractor.cleanPhoneNumber(null)).isEmpty();

        assertThat(extractor.parseHl7Date("20260910120000")).isNotNull();
        assertThat(extractor.parseHl7Date(null)).isNull();

        String ack = extractor.generateFallbackAck("MSG-MFN-1", "M02", "AE", "Error detail");
        assertThat(ack).contains("MSA|AE|MSG-MFN-1|Error detail");
    }

    @Test
    @DisplayName("Test Communication and Task resource builders")
    void testCommunicationAndTaskBuilders() throws Exception {
        Message msg = pipeParser.parse(FULL_MFN_M02);
        Terser terser = new Terser(msg);
        Topic topic = Topic.fromHl7("2.4", "MFN", "M02", "gw-default", null, "HOSPITAL_A", "HIE_DEST");

        Communication comm = communicationBuilder.buildCommunication(terser, topic, FULL_MFN_M02, "MSG-CTRL-99901",
                "DOC-12345", "DR. JOHN SMITH", "STAFF_APP", "HOSPITAL_A", "20260910120000");
        assertThat(comm.getId()).isEqualTo("Communication/comm-MSG-CTRL-99901");
        assertThat(comm.getSubject().getReference()).isEqualTo("Practitioner/DOC-12345");
        assertThat(comm.getPayloadFirstRep().getContent()).isInstanceOf(Attachment.class);

        Task task = taskBuilder.buildTask(terser, topic, "MSG-CTRL-99901", "DOC-12345", "DR. JOHN SMITH",
                "STAFF_APP", "HOSPITAL_A", "20260910120000", comm);
        assertThat(task.getId()).isEqualTo("Task/MSG-CTRL-99901");
        assertThat(task.getFor().getReference()).isEqualTo("Practitioner/DOC-12345");
        assertThat(task.getBasedOnFirstRep().getReference()).isEqualTo("Communication/comm-MSG-CTRL-99901");
        assertThat(task.getContained()).hasSize(1);
        assertThat(task.getContained().get(0)).isInstanceOf(Communication.class);
        assertThat(task.getInput()).hasSize(1);
        Task.TaskInputComponent commInput = task.getInputFirstRep();
        assertThat(commInput.getType().getCodingFirstRep().getCode()).isEqualTo("input-communication");
        assertThat(((Reference) commInput.getValue()).getReference()).isEqualTo("#comm-MSG-CTRL-99901");
    }
}
