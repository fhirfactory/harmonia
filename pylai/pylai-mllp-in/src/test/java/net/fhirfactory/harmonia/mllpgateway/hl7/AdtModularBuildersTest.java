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
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import net.fhirfactory.harmonia.mllpgateway.hl7.factories.AdtCommunicationResourceBuilder;
import net.fhirfactory.harmonia.mllpgateway.hl7.factories.AdtTaskResourceBuilder;
import net.fhirfactory.harmonia.model.security.FhirConfidentialityEnum;
import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.hl7.fhir.r5.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class AdtModularBuildersTest {

    private PipeParser pipeParser;
    private AdtMessageExtractor extractor;
    private AdtCommunicationResourceBuilder communicationBuilder;
    private AdtTaskResourceBuilder taskBuilder;

    @BeforeEach
    void setUp() {
        this.pipeParser = new DefaultHapiContext().getPipeParser();
        this.extractor = new AdtMessageExtractor();
        this.communicationBuilder = new AdtCommunicationResourceBuilder(extractor);
        this.taskBuilder = new AdtTaskResourceBuilder(extractor);
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
        assertThat(FhirSecurityTagManager.hasConfidentiality(comm, FhirConfidentialityEnum.N)).isTrue();
    }

    @Test
    void testTaskResourceBuilder() throws Exception {
        String hl7 = "MSH|^~\\&|EPIC|HOSPITAL|HIE|HIE_IM|20260907101500||ADT^A01|MSG-01|P|2.4\r";
        Message msg = pipeParser.parse(hl7);
        Terser terser = new Terser(msg);
        Topic topic = Topic.fromHl7("2.4", "ADT", "A01", "gw-1", null, "HOSPITAL", "HIE_IM");

        Communication comm = new Communication();
        comm.setId("Communication/comm-MSG-01");

        Task task = taskBuilder.buildTask(terser, topic, "MSG-01", "PAT10099", "JOHN SMITH", "V100", "I",
                "WARDA", "RM101", "BED1", "DOC01", "DR JONES", "EPIC", "HOSPITAL", "20260907101500", comm);

        assertThat(task.getId()).isEqualTo("Task/MSG-01");
        assertThat(task.getStatus()).isEqualTo(Task.TaskStatus.REQUESTED);
        assertThat(task.getFor().getReference()).isEqualTo("Patient/PAT10099");
        assertThat(task.getFocus().getReference()).isEqualTo("Encounter/V100");
        assertThat(task.getBasedOnFirstRep().getReference()).isEqualTo("Communication/comm-MSG-01");
        assertThat(task.getContained()).hasSize(1);
        assertThat(task.getContained().get(0)).isInstanceOf(Communication.class);
        assertThat(task.getInput()).hasSize(2); // location input + communication input
        Task.TaskInputComponent commInput = task.getInput().get(1);
        assertThat(commInput.getType().getCodingFirstRep().getCode()).isEqualTo("input-communication");
        assertThat(((Reference) commInput.getValue()).getReference()).isEqualTo("#comm-MSG-01");
        assertThat(FhirSecurityTagManager.hasConfidentiality(task, FhirConfidentialityEnum.N)).isTrue();
    }
}
