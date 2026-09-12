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
import net.fhirfactory.hie.model.TaskEvent;
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

class Adt2FhirMapperTest {

    private CamelContext camelContext;
    private ProducerTemplate producerTemplate;
    private Adt2FhirMapper mapper;
    private TaskCacheService taskCacheService;
    private FhirContext fhirContext;

    private static final String FULL_ADT_A01 =
            "MSH|^~\\&|PAS_APP|FACILITY_A|HIE_APP|HIE_DEST|20260910120000||ADT^A01|MSG-CTRL-99901|P|2.4\r" +
            "EVN|A01|20260910120000|||DOC-OP-01^OPERATOR^EVA\r" +
            "PID|1||MRN-PAT-777^^^HOSP||DOE^JANE^MARY^MS^DR||19850515|F||2106-3^White^HL70005|100 MAIN ST^SUITE 200^METROPOLIS^NY^10001^USA||(555)111-2222|(555)333-4444||M||ACC-555|SSN-999-88-7777|||2135-2^Hispanic^HL70005|METROPOLIS|Y|1|USA|||20260910110000|Y\r" +
            "PD1|||CLINIC-EAST|DOC-PCP-01^SMITH^ALICE\r" +
            "NK1|1|DOE^JOHN|SPO^Spouse|100 MAIN ST^^METROPOLIS^NY^10001|(555)111-2222\r" +
            "PV1|1|I|ICU^101^B^HOSP_MAIN|||DOC-PRIOR^BROWN^CHARLIE|DOC-ATT-01^WILLIAMS^ROBERT|DOC-REF-01^JOHNSON^SARAH|DOC-CON-01^DAVIS^EMILY||||1||||DOC-ADM-01^TAYLOR^JAMES||VISIT-88801|||||||||||||||||||||||||20260910100000|20260912150000\r" +
            "PV2|||CHIEF COMPLAINT CHEST PAIN\r" +
            "DG1|1|I10|I20.0^Unstable Angina^I10|Unstable angina pectoris|20260910103000|A||||||||1\r" +
            "AL1|1|DA|ASPIRIN^Aspirin|SV^Severe|RASH^Skin rash|20260910\r" +
            "OBX|1|NM|883-9^ABO group^LN||A+|||||F|||20260910110000\r" +
            "GT1|1||DOE^JOHN||100 MAIN ST^^METROPOLIS^NY^10001|(555)111-2222|||||SPO^Spouse\r" +
            "IN1|1|PLAN-A|INS-001|BLUE CROSS BLUE SHIELD||||GRP-100|GROUP HEALTH|||||||DOE^JANE|1|||||||||||||||||||POL-998877";

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        producerTemplate = camelContext.createProducerTemplate();
        mapper = new Adt2FhirMapper();
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
        assertThat(mapper.getActivityId()).isEqualTo(Adt2FhirMapper.DEFAULT_ACTIVITY_ID);
        assertThat(mapper.getActivityName()).isEqualTo(Adt2FhirMapper.DEFAULT_ACTIVITY_NAME);
        assertThat(mapper.generateRouteId()).isEqualTo("activity-adt2fhir-mapper");
    }

    @Test
    void testMapAdtToFhirWithStringInput() {
        Task task = new Task();
        task.setId("Task/TASK-MAP-001");
        task.setStatus(Task.TaskStatus.INPROGRESS);
        task.setAuthoredOn(new Date());

        // Set ADT message as Task.input
        Task.TaskInputComponent input = task.addInput();
        input.getType().setText("Raw HL7 ADT Message").addCoding()
                .setSystem("http://terminology.hl7.org/CodeSystem/task-input-type")
                .setCode("input-adt");
        input.setValue(new StringType(FULL_ADT_A01));

        Task resultTask = mapper.mapAdtToFhir(task);

        assertThat(resultTask).isNotNull();
        assertThat(resultTask.hasContained()).isFalse();

        // 1. Verify Task outputs: exactly 2 entries (Bundle JSON and origin Task.input)
        assertThat(resultTask.getOutput()).hasSize(2);

        Task.TaskOutputComponent bundleOutput = resultTask.getOutput().get(0);
        assertThat(bundleOutput.getType().getText()).contains("Bundle");
        assertThat(bundleOutput.getValue()).isInstanceOf(StringType.class);
        String bundleJson = ((StringType) bundleOutput.getValue()).getValue();
        assertThat(bundleJson).isNotBlank();

        // 2. Parse the JSON Bundle and verify
        Bundle bundle = fhirContext.newJsonParser().parseResource(Bundle.class, bundleJson);
        assertThat(bundle.getType()).isEqualTo(Bundle.BundleType.COLLECTION);

        Task.TaskOutputComponent originInputOutput = resultTask.getOutput().get(1);
        assertThat(originInputOutput.getType().getText()).contains("Origin Task Input");
        assertThat(originInputOutput.getValue()).isInstanceOf(StringType.class);
        assertThat(((StringType) originInputOutput.getValue()).getValue()).isEqualTo(FULL_ADT_A01);

        // 3. Verify Patient in Bundle
        Optional<Patient> patientOpt = bundle.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(r -> r instanceof Patient)
                .map(r -> (Patient) r)
                .findFirst();
        assertThat(patientOpt).isPresent();
        Patient patient = patientOpt.get();
        assertThat(patient.getIdPart()).isEqualTo("MRN-PAT-777");
        assertThat(patient.getNameFirstRep().getFamily()).isEqualTo("DOE");
        assertThat(patient.getNameFirstRep().getGivenAsSingleString()).isEqualTo("JANE MARY");
        assertThat(patient.getGender()).isEqualTo(Enumerations.AdministrativeGender.FEMALE);
        assertThat(patient.getAddressFirstRep().getCity()).isEqualTo("METROPOLIS");
        assertThat(patient.getAddressFirstRep().getState()).isEqualTo("NY");

        // 4. Verify Encounter in Bundle
        Optional<Encounter> encOpt = bundle.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(r -> r instanceof Encounter)
                .map(r -> (Encounter) r)
                .findFirst();
        assertThat(encOpt).isPresent();
        Encounter enc = encOpt.get();
        assertThat(enc.getIdPart()).isEqualTo("VISIT-88801");
        assertThat(enc.getStatus()).isEqualTo(Enumerations.EncounterStatus.INPROGRESS);
        assertThat(enc.getSubject().getReference()).isEqualTo("Patient/MRN-PAT-777");

        // 5. Verify Practitioners in Bundle (Attending, Referring, Consulting, Admitting, Primary Care, Operator)
        List<Practitioner> practitioners = bundle.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(r -> r instanceof Practitioner)
                .map(r -> (Practitioner) r)
                .toList();
        assertThat(practitioners).isNotEmpty();
        assertThat(practitioners).anyMatch(p -> p.getIdPart().contains("DOC-ATT-01"));
        assertThat(practitioners).anyMatch(p -> p.getIdPart().contains("DOC-REF-01"));

        // 6. Verify RelatedPerson in Bundle (NK1, GT1)
        List<RelatedPerson> relatedPersons = bundle.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(r -> r instanceof RelatedPerson)
                .map(r -> (RelatedPerson) r)
                .toList();
        assertThat(relatedPersons).isNotEmpty();
        assertThat(relatedPersons).anyMatch(rp -> rp.getNameFirstRep().getFamily().equals("DOE"));

        // 7. Verify Condition in Bundle (DG1)
        List<Condition> conditions = bundle.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(r -> r instanceof Condition)
                .map(r -> (Condition) r)
                .toList();
        assertThat(conditions).isNotEmpty();
        assertThat(conditions.get(0).getCode().getCodingFirstRep().getCode()).isEqualTo("I20.0");

        // 8. Verify AllergyIntolerance in Bundle (AL1)
        List<AllergyIntolerance> allergies = bundle.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(r -> r instanceof AllergyIntolerance)
                .map(r -> (AllergyIntolerance) r)
                .toList();
        assertThat(allergies).isNotEmpty();
        assertThat(allergies.get(0).getCode().getCodingFirstRep().getCode()).isEqualTo("ASPIRIN");

        // 9. Verify Observation in Bundle (OBX)
        List<Observation> observations = bundle.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(r -> r instanceof Observation)
                .map(r -> (Observation) r)
                .toList();
        assertThat(observations).isNotEmpty();
        assertThat(observations.get(0).getCode().getCodingFirstRep().getCode()).isEqualTo("883-9");

        // 10. Verify Location in Bundle (PV1-3)
        List<Location> locations = bundle.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(r -> r instanceof Location)
                .map(r -> (Location) r)
                .toList();
        assertThat(locations).isNotEmpty();
        assertThat(locations.get(0).getName()).contains("ICU Room 101 Bed B");

        // 11. Verify Organization in Bundle (MSH-4, MSH-6)
        List<Organization> organizations = bundle.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(r -> r instanceof Organization)
                .map(r -> (Organization) r)
                .toList();
        assertThat(organizations).isNotEmpty();
        assertThat(organizations).anyMatch(o -> o.getIdPart().contains("FACILITY_A"));

        // 12. Verify Coverage in Bundle (IN1)
        List<Coverage> coverages = bundle.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(r -> r instanceof Coverage)
                .map(r -> (Coverage) r)
                .toList();
        assertThat(coverages).isNotEmpty();

        // 13. Verify Communication in Bundle (raw payload)
        Optional<Communication> commOpt = bundle.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(r -> r instanceof Communication)
                .map(r -> (Communication) r)
                .findFirst();
        assertThat(commOpt).isPresent();
        assertThat(commOpt.get().getPayloadFirstRep().getContent()).isInstanceOf(Attachment.class);

        // 14. Verify Provenance in Bundle
        Optional<Provenance> provOpt = bundle.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(r -> r instanceof Provenance)
                .map(r -> (Provenance) r)
                .findFirst();
        assertThat(provOpt).isPresent();
    }

    @Test
    void testMapAdtWithContainedCommunicationInput() {
        Task task = new Task();
        task.setId("Task/TASK-MAP-COMM");
        task.setStatus(Task.TaskStatus.INPROGRESS);
        task.setAuthoredOn(new Date());

        // Add contained Communication with attachment
        Communication comm = new Communication();
        comm.setId("Communication/comm-src-123");
        Communication.CommunicationPayloadComponent payload = comm.addPayload();
        Attachment att = new Attachment();
        att.setContentType("application/hl7-v2");
        att.setData(FULL_ADT_A01.getBytes(StandardCharsets.UTF_8));
        payload.setContent(att);
        task.addContained(comm);

        Task.TaskInputComponent input = task.addInput();
        input.getType().setText("Source Communication").addCoding()
                .setSystem("http://terminology.hl7.org/CodeSystem/task-input-type")
                .setCode("input-communication");
        input.setValue(new Reference("#comm-src-123"));

        Task resultTask = mapper.mapAdtToFhir(task);

        assertThat(resultTask).isNotNull();
        // Contained resources should only contain the incoming Communication, no Bundle added
        assertThat(resultTask.getContained()).hasSize(1);
        assertThat(resultTask.getContained().get(0)).isInstanceOf(Communication.class);
        assertThat(resultTask.getOutput()).hasSize(2);

        // First output is Bundle (as JSON)
        assertThat(resultTask.getOutput().get(0).getType().getText()).contains("Bundle");
        assertThat(resultTask.getOutput().get(0).getValue()).isInstanceOf(StringType.class);
        String bundleJson = ((StringType) resultTask.getOutput().get(0).getValue()).getValue();
        Bundle bundle = fhirContext.newJsonParser().parseResource(Bundle.class, bundleJson);
        assertThat(bundle.getEntry()).isNotEmpty();

        // Second output is origin input (Reference to #comm-src-123)
        assertThat(resultTask.getOutput().get(1).getValue()).isInstanceOf(Reference.class);
        Reference originRef = (Reference) resultTask.getOutput().get(1).getValue();
        assertThat(originRef.getReference()).isEqualTo("#comm-src-123");
    }

    @Test
    void testCamelExchangeExecution() throws Exception {
        Task incomingTask = new Task();
        incomingTask.setId("Task/TASK-EXCHANGE-MAP");
        incomingTask.setStatus(Task.TaskStatus.INPROGRESS);
        incomingTask.setAuthoredOn(new Date());

        Task.TaskInputComponent input = incomingTask.addInput();
        input.getType().setText("ADT Message Payload").addCoding()
                .setSystem("http://terminology.hl7.org/CodeSystem/task-input-type")
                .setCode("input-adt");
        input.setValue(new StringType(FULL_ADT_A01));

        taskCacheService.saveTask(incomingTask);

        mapper.setInputEndpoint("direct:adt2fhir-in");
        mapper.setOutputEndpoint("mock:adt2fhir-out");
        camelContext.addRoutes(mapper);
        camelContext.start();

        org.apache.camel.component.mock.MockEndpoint mockOut = camelContext.getEndpoint("mock:adt2fhir-out", org.apache.camel.component.mock.MockEndpoint.class);
        mockOut.expectedMessageCount(1);

        Exchange exchange = camelContext.getEndpoint("direct:adt2fhir-in").createExchange();
        TaskEvent taskEvent = new TaskEvent("TASK-EXCHANGE-MAP", "PROCESS", "requested");
        exchange.getMessage().setBody(taskEvent);
        exchange.getMessage().setHeader(TaskProcessingActivity.HEADER_TASK_ID, "TASK-EXCHANGE-MAP");

        Exchange resultExchange = producerTemplate.send("direct:adt2fhir-in", exchange);

        assertThat(resultExchange).isNotNull();
        assertThat(resultExchange.getException()).isNull();
        mockOut.assertIsSatisfied(2000);

        // Check cached Task after activity + egress processing
        Optional<Task> cachedTaskOpt = taskCacheService.getTask("TASK-EXCHANGE-MAP");
        assertThat(cachedTaskOpt).isPresent();
        Task cachedTask = cachedTaskOpt.get();

        assertThat(cachedTask.getOutput()).hasSize(2);
        assertThat(cachedTask.getOutput().get(0).getType().getText()).contains("Bundle");
        assertThat(cachedTask.getOutput().get(0).getValue()).isInstanceOf(StringType.class);
        String cachedBundleJson = ((StringType) cachedTask.getOutput().get(0).getValue()).getValue();
        Bundle cachedBundle = fhirContext.newJsonParser().parseResource(Bundle.class, cachedBundleJson);
        assertThat(cachedBundle.getEntry()).isNotEmpty();
        assertThat(cachedTask.getOutput().get(1).getType().getText()).contains("Origin Task Input");

        // Check discrete child Tasks generated in egress (one for Bundle JSON, one for origin input)
        @SuppressWarnings("unchecked")
        List<Task> outgoingTasks = (List<Task>) resultExchange.getProperty(TaskProcessingActivity.PROPERTY_OUTGOING_TASKS);
        assertThat(outgoingTasks).hasSize(2);
        Task bundleChildTask = outgoingTasks.get(0);
        assertThat(bundleChildTask.getInputFirstRep().getValue()).isInstanceOf(StringType.class);
        String childBundleJson = ((StringType) bundleChildTask.getInputFirstRep().getValue()).getValue();
        Bundle childBundle = fhirContext.newJsonParser().parseResource(Bundle.class, childBundleJson);
        assertThat(childBundle.getType()).isEqualTo(Bundle.BundleType.COLLECTION);

        // Check Provenances generated in egress
        @SuppressWarnings("unchecked")
        List<Provenance> provenances = (List<Provenance>) resultExchange.getProperty(TaskProcessingActivity.PROPERTY_PROVENANCES);
        assertThat(provenances).hasSize(2);

        // Check TaskEvents generated in egress
        @SuppressWarnings("unchecked")
        List<TaskEvent> taskEvents = (List<TaskEvent>) resultExchange.getProperty(TaskProcessingActivity.PROPERTY_TASK_EVENTS);
        assertThat(taskEvents).hasSize(2);
    }

    @Test
    void testMapAdtA03DischargeEvent() {
        String adtA03 =
                "MSH|^~\\&|PAS|HOSP|HIE|REC|20260912150000||ADT^A03|MSG-DISCHARGE-01|P|2.4\r" +
                "PID|||MRN-DISC-100||TAYLOR^ROBERT||19700101|M\r" +
                "PV1|1|I|MED^202^A||||DOC-01^JOHNSON^MARK||||||||||DOC-ADM^LEE^ANN||VISIT-DISC-500|||||||||||||||||||||||||20260910080000|20260912143000";

        Task task = new Task();
        task.setId("Task/TASK-DISCHARGE");
        task.setStatus(Task.TaskStatus.INPROGRESS);
        task.addInput().setValue(new StringType(adtA03));

        Task resultTask = mapper.mapAdtToFhir(task);

        assertThat(resultTask).isNotNull();
        assertThat(resultTask.hasContained()).isFalse();
        assertThat(resultTask.getOutput()).hasSize(2);

        assertThat(resultTask.getOutput().get(0).getValue()).isInstanceOf(StringType.class);
        String bundleJson = ((StringType) resultTask.getOutput().get(0).getValue()).getValue();
        Bundle bundle = fhirContext.newJsonParser().parseResource(Bundle.class, bundleJson);

        Optional<Encounter> encounterOpt = bundle.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(r -> r instanceof Encounter)
                .map(r -> (Encounter) r)
                .findFirst();

        assertThat(encounterOpt).isPresent();
        Encounter enc = encounterOpt.get();
        assertThat(enc.getStatus()).isEqualTo(Enumerations.EncounterStatus.COMPLETED);
        assertThat(enc.getActualPeriod().hasEnd()).isTrue();
    }

    @Test
    void testMapAdtEmptyOrNullFallback() {
        Task task = new Task();
        task.setId("Task/TASK-EMPTY");
        task.setStatus(Task.TaskStatus.INPROGRESS);

        Task resultTask = mapper.mapAdtToFhir(task);

        assertThat(resultTask).isNotNull();
        assertThat(resultTask.hasContained()).isFalse();
        assertThat(resultTask.getOutput()).hasSize(2);

        assertThat(resultTask.getOutput().get(0).getValue()).isInstanceOf(StringType.class);
        String bundleJson = ((StringType) resultTask.getOutput().get(0).getValue()).getValue();
        Bundle bundle = fhirContext.newJsonParser().parseResource(Bundle.class, bundleJson);
        assertThat(bundle.getEntry()).isNotEmpty();
        assertThat(bundle.getEntry().get(0).getResource()).isInstanceOf(Patient.class);
    }
}
