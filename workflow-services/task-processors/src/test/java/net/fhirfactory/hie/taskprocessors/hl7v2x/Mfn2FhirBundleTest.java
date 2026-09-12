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

import ca.uhn.fhir.context.FhirContext;
import net.fhirfactory.hie.taskprocessor.cache.TaskCacheService;
import net.fhirfactory.hie.taskprocessors.base.TaskProcessingActivity;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.hl7.fhir.r5.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class Mfn2FhirBundleTest {

    private CamelContext camelContext;
    private ProducerTemplate producerTemplate;
    private Mfn2FhirBundle mapper;
    private TaskCacheService taskCacheService;
    private FhirContext fhirContext;

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

    private static final String MULTI_STF_MFN =
            "MSH|^~\\&|STAFF_APP|HOSPITAL_A|HIE_APP|HIE_DEST|20260910120000||MFN^M02|MSG-CTRL-MULTI|P|2.4\r" +
            "MFI|STF^Staff Master File|STAFF_APP|UPD|20260910120000|20260910120000|NE\r" +
            "MFE|MAD|ENTRY-1|20260910120000|DOC-001|CE\r" +
            "STF|DOC-001||DOE^JANE||F|19800101|Y|CARD|||||||||||||||||||||||||||||\r" +
            "MFE|MAD|ENTRY-2|20260910120000|DOC-002|CE\r" +
            "STF|DOC-002||ROE^RICHARD||M|19780202|Y|SURG|||||||||||||||||||||||||||||";

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        producerTemplate = camelContext.createProducerTemplate();
        mapper = new Mfn2FhirBundle();
        fhirContext = FhirContext.forR5();
        taskCacheService = new TaskCacheService(null, fhirContext);
        mapper.setTaskCacheService(taskCacheService);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (producerTemplate != null) {
            producerTemplate.stop();
        }
        if (camelContext != null) {
            camelContext.stop();
        }
    }

    @Test
    void testMapperMetadataAndInheritance() {
        assertThat(mapper).isInstanceOf(TaskProcessingActivity.class);
        assertThat(mapper).isInstanceOf(RouteBuilder.class);
        assertThat(mapper.getActivityId()).isEqualTo(Mfn2FhirBundle.DEFAULT_ACTIVITY_ID);
        assertThat(mapper.getActivityName()).isEqualTo(Mfn2FhirBundle.DEFAULT_ACTIVITY_NAME);
        assertThat(mapper.generateRouteId()).isEqualTo("activity-mfn2fhir-bundle");
    }

    @Test
    void testMapMfnToFhirWithStringInput() {
        Task task = new Task();
        task.setId("Task/TASK-MAP-001");
        task.setStatus(Task.TaskStatus.INPROGRESS);
        task.setAuthoredOn(new Date());

        // Set MFN message as Task.input
        Task.TaskInputComponent input = task.addInput();
        input.getType().setText("Raw HL7 MFN Message").addCoding()
                .setSystem("http://terminology.hl7.org/CodeSystem/task-input-type")
                .setCode("input-mfn");
        input.setValue(new StringType(FULL_MFN_M02));

        Task resultTask = mapper.mapMfnToFhir(task);

        assertThat(resultTask).isNotNull();
        assertThat(resultTask.hasContained()).isFalse();

        // Exactly 2 output entries: Bundle JSON and origin Task.input
        assertThat(resultTask.getOutput()).hasSize(2);

        // Output 1: Bundle
        Task.TaskOutputComponent bundleOutput = resultTask.getOutput().get(0);
        assertThat(bundleOutput.getType().getCodingFirstRep().getCode()).isEqualTo("Bundle");
        assertThat(bundleOutput.getValue()).isInstanceOf(StringType.class);

        String bundleJson = ((StringType) bundleOutput.getValue()).getValue();
        assertThat(bundleJson).isNotBlank();

        Bundle parsedBundle = fhirContext.newJsonParser().parseResource(Bundle.class, bundleJson);
        assertThat(parsedBundle).isNotNull();
        assertThat(parsedBundle.getType()).isEqualTo(Bundle.BundleType.COLLECTION);

        // Output 2: Origin input
        Task.TaskOutputComponent originOutput = resultTask.getOutput().get(1);
        assertThat(originOutput.getType().getCodingFirstRep().getCode()).isEqualTo("origin-input");
        assertThat(originOutput.getValue()).isInstanceOf(StringType.class);
        assertThat(((StringType) originOutput.getValue()).getValue()).isEqualTo(FULL_MFN_M02);

        // Verify task context
        assertThat(resultTask.hasFor()).isTrue();
        assertThat(resultTask.getFor().getReference()).isEqualTo("Practitioner/DOC-12345");
        assertThat(resultTask.hasFocus()).isTrue();
        assertThat(resultTask.getFocus().getReference()).isEqualTo("PractitionerRole/DOC-12345-role");
    }

    @Test
    void testMapMfnToFhirWithAttachmentInput() {
        Task task = new Task();
        task.setId("Task/TASK-MAP-002");
        task.setStatus(Task.TaskStatus.INPROGRESS);
        task.setAuthoredOn(new Date());

        Task.TaskInputComponent input = task.addInput();
        input.getType().setText("Attachment MFN Payload");
        Attachment att = new Attachment();
        att.setContentType("x-application/hl7-v2+er7");
        att.setData(FULL_MFN_M02.getBytes(StandardCharsets.UTF_8));
        input.setValue(att);

        Task resultTask = mapper.mapMfnToFhir(task);

        assertThat(resultTask).isNotNull();
        assertThat(resultTask.getOutput()).hasSize(2);

        String bundleJson = ((StringType) resultTask.getOutput().get(0).getValue()).getValue();
        Bundle bundle = fhirContext.newJsonParser().parseResource(Bundle.class, bundleJson);
        assertThat(bundle.getEntry()).isNotEmpty();
        assertThat(bundle.getEntry().stream().anyMatch(e -> e.getResource() instanceof Practitioner)).isTrue();
    }

    @Test
    void testMapMfnToFhirWithContainedCommunication() {
        Task task = new Task();
        task.setId("Task/TASK-MAP-003");
        task.setStatus(Task.TaskStatus.INPROGRESS);
        task.setAuthoredOn(new Date());

        Communication comm = new Communication();
        comm.setId("comm-input-001");
        Attachment att = new Attachment();
        att.setContentType("x-application/hl7-v2+er7");
        att.setData(FULL_MFN_M02.getBytes(StandardCharsets.UTF_8));
        comm.addPayload().setContent(att);
        task.addContained(comm);

        Task.TaskInputComponent input = task.addInput();
        input.setValue(new Reference("#comm-input-001"));

        Task resultTask = mapper.mapMfnToFhir(task);

        assertThat(resultTask).isNotNull();
        assertThat(resultTask.getOutput()).hasSize(2);

        String bundleJson = ((StringType) resultTask.getOutput().get(0).getValue()).getValue();
        Bundle bundle = fhirContext.newJsonParser().parseResource(Bundle.class, bundleJson);
        assertThat(bundle.getEntry()).isNotEmpty();
    }

    @Test
    void testProcessMfnActivityThroughCamelExchange() throws Exception {
        Exchange exchange = camelContext.getEndpoint("direct:test").createExchange();

        Task task = new Task();
        task.setId("Task/TASK-CAMEL-001");
        task.setStatus(Task.TaskStatus.INPROGRESS);
        task.addInput().setValue(new StringType(FULL_MFN_M02));

        exchange.getMessage().setBody(task);

        mapper.processActivity(exchange);

        Task processedTask = exchange.getMessage().getBody(Task.class);
        assertThat(processedTask).isNotNull();
        assertThat(processedTask.getOutput()).hasSize(2);

        // Check exchange headers
        assertThat(exchange.getMessage().getHeader(Mfn2FhirBundle.HEADER_PRACTITIONER_ID)).isEqualTo("DOC-12345");
        assertThat(exchange.getMessage().getHeader(Mfn2FhirBundle.HEADER_PRACTITIONER_NAME)).isNotNull();
    }

    @Test
    void testMapMfnWithEmptyOrNullTask() {
        Task resultTask = mapper.mapMfnToFhir(null, null);

        assertThat(resultTask).isNotNull();
        assertThat(resultTask.getOutput()).hasSize(2);

        String bundleJson = ((StringType) resultTask.getOutput().get(0).getValue()).getValue();
        Bundle bundle = fhirContext.newJsonParser().parseResource(Bundle.class, bundleJson);
        assertThat(bundle).isNotNull();
        assertThat(bundle.getEntry()).isNotEmpty();
        assertThat(bundle.getEntry().get(0).getResource()).isInstanceOf(Practitioner.class);
    }

    @Test
    void testMultipleStfSegments() {
        Task task = new Task();
        task.setId("Task/TASK-MULTI-001");
        task.addInput().setValue(new StringType(MULTI_STF_MFN));

        Task resultTask = mapper.mapMfnToFhir(task);
        assertThat(resultTask).isNotNull();

        String bundleJson = ((StringType) resultTask.getOutput().get(0).getValue()).getValue();
        Bundle bundle = fhirContext.newJsonParser().parseResource(Bundle.class, bundleJson);

        long practCount = bundle.getEntry().stream().filter(e -> e.getResource() instanceof Practitioner).count();
        long roleCount = bundle.getEntry().stream().filter(e -> e.getResource() instanceof PractitionerRole).count();

        assertThat(practCount).isEqualTo(2);
        assertThat(roleCount).isEqualTo(2);
    }
}
