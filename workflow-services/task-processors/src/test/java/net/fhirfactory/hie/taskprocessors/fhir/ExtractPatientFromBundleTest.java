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

package net.fhirfactory.hie.taskprocessors.fhir;

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

class ExtractPatientFromBundleTest {

    private CamelContext camelContext;
    private ProducerTemplate producerTemplate;
    private ExtractPatientFromBundle activity;
    private TaskCacheService taskCacheService;
    private FhirContext fhirContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        producerTemplate = camelContext.createProducerTemplate();
        activity = new ExtractPatientFromBundle();
        fhirContext = FhirContext.forR5();
        taskCacheService = new TaskCacheService(null, fhirContext);
        activity.setTaskCacheService(taskCacheService);
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
    void testActivityMetadataAndInheritance() {
        assertThat(activity).isInstanceOf(TaskProcessingActivity.class);
        assertThat(activity).isInstanceOf(RouteBuilder.class);
        assertThat(activity.getActivityId()).isEqualTo(ExtractPatientFromBundle.DEFAULT_ACTIVITY_ID);
        assertThat(activity.getActivityName()).isEqualTo(ExtractPatientFromBundle.DEFAULT_ACTIVITY_NAME);
        assertThat(activity.generateRouteId()).isEqualTo("activity-extract-patient-from-bundle");
    }

    @Test
    void testExtractSinglePatientFromBundleInTaskInput() {
        // Construct a Bundle with 1 Patient and 1 Observation
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.COLLECTION);
        bundle.setId("Bundle/bundle-test-01");

        Patient patient = new Patient();
        patient.setId("Patient/PAT-1001");
        patient.addIdentifier().setSystem("http://example.org/patients").setValue("PAT-1001");
        patient.addName().setFamily("DOE").addGiven("JANE").setText("JANE DOE");
        patient.setGender(Enumerations.AdministrativeGender.FEMALE);
        bundle.addEntry().setResource(patient);

        Observation observation = new Observation();
        observation.setId("Observation/OBS-01");
        observation.setStatus(Enumerations.ObservationStatus.FINAL);
        bundle.addEntry().setResource(observation);

        String bundleJson = fhirContext.newJsonParser().encodeResourceToString(bundle);

        Task task = new Task();
        task.setId("Task/TASK-BUNDLE-01");
        task.setStatus(Task.TaskStatus.INPROGRESS);
        task.setAuthoredOn(new Date());

        Task.TaskInputComponent input = task.addInput();
        input.getType().setText("FHIR Bundle Payload").addCoding()
                .setSystem("http://hl7.org/fhir/resource-types")
                .setCode("Bundle");
        input.setValue(new StringType(bundleJson));

        Task resultTask = activity.extractPatientsToTaskOutput(task);

        assertThat(resultTask).isNotNull();
        assertThat(resultTask.getOutput()).hasSize(1);

        Task.TaskOutputComponent output = resultTask.getOutput().get(0);
        assertThat(output.getType().getText()).isEqualTo("Patient Resource");
        assertThat(output.getValue()).isInstanceOf(StringType.class);

        String patientJson = ((StringType) output.getValue()).getValue();
        Patient parsedPat = fhirContext.newJsonParser().parseResource(Patient.class, patientJson);
        assertThat(parsedPat).isNotNull();
        assertThat(parsedPat.getIdPart()).isEqualTo("PAT-1001");
        assertThat(parsedPat.getNameFirstRep().getFamily()).isEqualTo("DOE");
        assertThat(parsedPat.getNameFirstRep().getGivenAsSingleString()).isEqualTo("JANE");

        assertThat(resultTask.hasFor()).isTrue();
        assertThat(resultTask.getFor().getReference()).isEqualTo("Patient/PAT-1001");
    }

    @Test
    void testExtractMultiplePatientsFromBundle() {
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.TRANSACTION);

        Patient p1 = new Patient();
        p1.setId("Patient/PAT-AAA");
        p1.addName().setFamily("SMITH").addGiven("ALICE").setText("ALICE SMITH");
        bundle.addEntry().setResource(p1);

        Patient p2 = new Patient();
        p2.setId("Patient/PAT-BBB");
        p2.addName().setFamily("JONES").addGiven("BOB").setText("BOB JONES");
        bundle.addEntry().setResource(p2);

        Patient p3 = new Patient();
        p3.setId("Patient/PAT-CCC");
        p3.addName().setFamily("BROWN").addGiven("CHARLIE").setText("CHARLIE BROWN");
        bundle.addEntry().setResource(p3);

        String bundleJson = fhirContext.newJsonParser().encodeResourceToString(bundle);

        Task task = new Task();
        task.setId("Task/TASK-MULTI-PAT");
        task.setStatus(Task.TaskStatus.INPROGRESS);

        task.addInput().setValue(new StringType(bundleJson));

        Task resultTask = activity.extractPatientsToTaskOutput(task);

        assertThat(resultTask.getOutput()).hasSize(3);

        for (int i = 0; i < 3; i++) {
            Task.TaskOutputComponent out = resultTask.getOutput().get(i);
            assertThat(out.getType().getText()).isEqualTo("Patient Resource");
            String json = ((StringType) out.getValue()).getValue();
            Patient pat = fhirContext.newJsonParser().parseResource(Patient.class, json);
            assertThat(pat).isNotNull();
        }

        Patient first = fhirContext.newJsonParser().parseResource(Patient.class, ((StringType) resultTask.getOutput().get(0).getValue()).getValue());
        Patient second = fhirContext.newJsonParser().parseResource(Patient.class, ((StringType) resultTask.getOutput().get(1).getValue()).getValue());
        Patient third = fhirContext.newJsonParser().parseResource(Patient.class, ((StringType) resultTask.getOutput().get(2).getValue()).getValue());

        assertThat(first.getIdPart()).isEqualTo("PAT-AAA");
        assertThat(second.getIdPart()).isEqualTo("PAT-BBB");
        assertThat(third.getIdPart()).isEqualTo("PAT-CCC");
    }

    @Test
    void testExtractFromMultipleTaskInputs() {
        Bundle bundle1 = new Bundle();
        Patient p1 = new Patient();
        p1.setId("Patient/P1");
        bundle1.addEntry().setResource(p1);

        Bundle bundle2 = new Bundle();
        Patient p2 = new Patient();
        p2.setId("Patient/P2");
        bundle2.addEntry().setResource(p2);

        Task task = new Task();
        task.setId("Task/TASK-2-INPUTS");
        task.addInput().setValue(new StringType(fhirContext.newJsonParser().encodeResourceToString(bundle1)));
        task.addInput().setValue(new StringType(fhirContext.newJsonParser().encodeResourceToString(bundle2)));

        Task resultTask = activity.extractPatientsToTaskOutput(task);

        assertThat(resultTask.getOutput()).hasSize(2);
    }

    @Test
    void testExtractFromAttachmentInput() {
        Bundle bundle = new Bundle();
        Patient p = new Patient();
        p.setId("Patient/ATTACH-PAT");
        bundle.addEntry().setResource(p);

        String bundleJson = fhirContext.newJsonParser().encodeResourceToString(bundle);

        Task task = new Task();
        task.setId("Task/TASK-ATTACH");
        Attachment attachment = new Attachment();
        attachment.setContentType("application/fhir+json");
        attachment.setData(bundleJson.getBytes(StandardCharsets.UTF_8));
        task.addInput().setValue(attachment);

        Task resultTask = activity.extractPatientsToTaskOutput(task);

        assertThat(resultTask.getOutput()).hasSize(1);
        Patient extracted = fhirContext.newJsonParser().parseResource(Patient.class, ((StringType) resultTask.getOutput().get(0).getValue()).getValue());
        assertThat(extracted.getIdPart()).isEqualTo("ATTACH-PAT");
    }

    @Test
    void testExtractFromContainedBundleReference() {
        Bundle bundle = new Bundle();
        bundle.setId("contained-bundle-1");
        Patient p = new Patient();
        p.setId("Patient/CONTAINED-PAT");
        bundle.addEntry().setResource(p);

        Task task = new Task();
        task.setId("Task/TASK-CONTAINED");
        task.addContained(bundle);

        Task.TaskInputComponent input = task.addInput();
        input.setValue(new Reference("#contained-bundle-1"));

        Task resultTask = activity.extractPatientsToTaskOutput(task);

        assertThat(resultTask.getOutput()).hasSize(1);
        Patient extracted = fhirContext.newJsonParser().parseResource(Patient.class, ((StringType) resultTask.getOutput().get(0).getValue()).getValue());
        assertThat(extracted.getIdPart()).isEqualTo("CONTAINED-PAT");
    }

    @Test
    void testExtractWhenNoPatientInBundle() {
        Bundle bundle = new Bundle();
        Encounter enc = new Encounter();
        enc.setId("Encounter/ENC-01");
        bundle.addEntry().setResource(enc);

        Task task = new Task();
        task.setId("Task/TASK-NO-PAT");
        task.addInput().setValue(new StringType(fhirContext.newJsonParser().encodeResourceToString(bundle)));

        Task resultTask = activity.extractPatientsToTaskOutput(task);

        assertThat(resultTask.getOutput()).isEmpty();
    }

    @Test
    void testCamelRouteExecution() throws Exception {
        activity.setInputEndpoint("direct:test-bundle-patient-in");
        activity.setOutputEndpoint("mock:test-bundle-patient-out");

        camelContext.addRoutes(activity);
        camelContext.start();

        // Create initial Task with a Bundle containing 2 Patients
        Bundle bundle = new Bundle();
        Patient p1 = new Patient();
        p1.setId("Patient/PAT-ROUTE-1");
        p1.addName().setFamily("DOE").addGiven("JANE").setText("JANE DOE");
        p1.addIdentifier().setType(new CodeableConcept().addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "MR", "MRN"))).setValue("MRN-901");
        bundle.addEntry().setResource(p1);

        Patient p2 = new Patient();
        p2.setId("Patient/PAT-ROUTE-2");
        p2.addName().setFamily("DOE").addGiven("JOHN").setText("JOHN DOE");
        bundle.addEntry().setResource(p2);

        String bundleJson = fhirContext.newJsonParser().encodeResourceToString(bundle);

        Task initialTask = new Task();
        initialTask.setId("Task/TASK-ROUTE-001");
        initialTask.setStatus(Task.TaskStatus.INPROGRESS);
        initialTask.addInput().setValue(new StringType(bundleJson));

        // Save to cache so ingress can retrieve it
        taskCacheService.saveTask(initialTask);

        Exchange exchange = camelContext.getEndpoint("direct:test-bundle-patient-in").createExchange();
        exchange.getMessage().setHeader(TaskProcessingActivity.HEADER_TASK_ID, "TASK-ROUTE-001");
        exchange.getMessage().setBody(initialTask);

        Exchange resultExchange = producerTemplate.send("direct:test-bundle-patient-in", exchange);

        assertThat(resultExchange.isFailed()).isFalse();

        // Egress verification
        Object body = resultExchange.getMessage().getBody();
        assertThat(body).isInstanceOf(TaskEvent.class);
        TaskEvent primaryEvent = (TaskEvent) body;
        assertThat(primaryEvent.getTaskId()).startsWith("TASK-ROUTE-001-out-");

        // Verify outgoing tasks in exchange properties
        @SuppressWarnings("unchecked")
        List<Task> outgoingTasks = (List<Task>) resultExchange.getProperty(TaskProcessingActivity.PROPERTY_OUTGOING_TASKS);
        assertThat(outgoingTasks).hasSize(2);

        // Verify task outputs persisted in cache
        Optional<Task> cachedMainTask = taskCacheService.getTask("TASK-ROUTE-001");
        assertThat(cachedMainTask).isPresent();
        assertThat(cachedMainTask.get().getOutput()).hasSize(2);

        // Verify discrete child tasks persisted in cache
        Optional<Task> child1 = taskCacheService.getTask("TASK-ROUTE-001-out-1");
        Optional<Task> child2 = taskCacheService.getTask("TASK-ROUTE-001-out-2");
        assertThat(child1).isPresent();
        assertThat(child2).isPresent();

        // Verify child task inputs contain the patient JSON
        assertThat(child1.get().getInput()).hasSize(1);
        String p1Json = ((StringType) child1.get().getInputFirstRep().getValue()).getValue();
        Patient cachedP1 = fhirContext.newJsonParser().parseResource(Patient.class, p1Json);
        assertThat(cachedP1.getIdPart()).isEqualTo("PAT-ROUTE-1");

        // Verify exchange headers
        assertThat(resultExchange.getMessage().getHeader(ExtractPatientFromBundle.HEADER_PATIENT_COUNT)).isEqualTo(2);
        assertThat(resultExchange.getMessage().getHeader(ExtractPatientFromBundle.HEADER_PATIENT_ID)).isEqualTo("PAT-ROUTE-1");
        assertThat(resultExchange.getMessage().getHeader(ExtractPatientFromBundle.HEADER_PATIENT_NAME)).isEqualTo("JANE DOE");
        assertThat(resultExchange.getMessage().getHeader(ExtractPatientFromBundle.HEADER_PATIENT_MRN)).isEqualTo("MRN-901");
    }
}
