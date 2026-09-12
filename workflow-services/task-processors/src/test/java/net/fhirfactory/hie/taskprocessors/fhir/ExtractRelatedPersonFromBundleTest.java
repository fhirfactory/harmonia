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

class ExtractRelatedPersonFromBundleTest {

    private CamelContext camelContext;
    private ProducerTemplate producerTemplate;
    private ExtractRelatedPersonFromBundle activity;
    private TaskCacheService taskCacheService;
    private FhirContext fhirContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        producerTemplate = camelContext.createProducerTemplate();
        activity = new ExtractRelatedPersonFromBundle();
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
        assertThat(activity.getActivityId()).isEqualTo(ExtractRelatedPersonFromBundle.DEFAULT_ACTIVITY_ID);
        assertThat(activity.getActivityName()).isEqualTo(ExtractRelatedPersonFromBundle.DEFAULT_ACTIVITY_NAME);
        assertThat(activity.generateRouteId()).isEqualTo("activity-extract-related-person-from-bundle");
    }

    @Test
    void testExtractSingleRelatedPersonFromBundleInTaskInput() {
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.COLLECTION);
        bundle.setId("Bundle/bundle-rp-01");

        RelatedPerson rp = new RelatedPerson();
        rp.setId("RelatedPerson/RP-1001");
        rp.addName().setFamily("DOE").addGiven("JOHN").setText("JOHN DOE");
        rp.setPatient(new Reference("Patient/PAT-1001").setDisplay("JANE DOE"));
        rp.addRelationship().addCoding()
                .setSystem("http://terminology.hl7.org/CodeSystem/v3-RoleCode")
                .setCode("SPS")
                .setDisplay("Spouse");
        bundle.addEntry().setResource(rp);

        Observation observation = new Observation();
        observation.setId("Observation/OBS-01");
        observation.setStatus(Enumerations.ObservationStatus.FINAL);
        bundle.addEntry().setResource(observation);

        String bundleJson = fhirContext.newJsonParser().encodeResourceToString(bundle);

        Task task = new Task();
        task.setId("Task/TASK-BUNDLE-RP-01");
        task.setStatus(Task.TaskStatus.INPROGRESS);
        task.setAuthoredOn(new Date());

        Task.TaskInputComponent input = task.addInput();
        input.getType().setText("FHIR Bundle Payload").addCoding()
                .setSystem("http://hl7.org/fhir/resource-types")
                .setCode("Bundle");
        input.setValue(new StringType(bundleJson));

        Task resultTask = activity.extractRelatedPersonsToTaskOutput(task);

        assertThat(resultTask).isNotNull();
        assertThat(resultTask.getOutput()).hasSize(1);

        Task.TaskOutputComponent output = resultTask.getOutput().get(0);
        assertThat(output.getType().getText()).isEqualTo("RelatedPerson Resource");
        assertThat(output.getValue()).isInstanceOf(StringType.class);

        String rpJson = ((StringType) output.getValue()).getValue();
        RelatedPerson parsedRp = fhirContext.newJsonParser().parseResource(RelatedPerson.class, rpJson);
        assertThat(parsedRp).isNotNull();
        assertThat(parsedRp.getIdPart()).isEqualTo("RP-1001");
        assertThat(parsedRp.getNameFirstRep().getFamily()).isEqualTo("DOE");
        assertThat(parsedRp.getNameFirstRep().getGivenAsSingleString()).isEqualTo("JOHN");
        assertThat(parsedRp.getPatient().getReference()).isEqualTo("Patient/PAT-1001");

        assertThat(resultTask.hasFor()).isTrue();
        assertThat(resultTask.getFor().getReference()).isEqualTo("Patient/PAT-1001");
    }

    @Test
    void testExtractMultipleRelatedPersonsFromBundle() {
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.TRANSACTION);

        RelatedPerson rp1 = new RelatedPerson();
        rp1.setId("RelatedPerson/RP-AAA");
        rp1.addName().setFamily("SMITH").addGiven("ALICE").setText("ALICE SMITH");
        rp1.setPatient(new Reference("Patient/PAT-001"));
        bundle.addEntry().setResource(rp1);

        RelatedPerson rp2 = new RelatedPerson();
        rp2.setId("RelatedPerson/RP-BBB");
        rp2.addName().setFamily("JONES").addGiven("BOB").setText("BOB JONES");
        bundle.addEntry().setResource(rp2);

        RelatedPerson rp3 = new RelatedPerson();
        rp3.setId("RelatedPerson/RP-CCC");
        rp3.addName().setFamily("BROWN").addGiven("CHARLIE").setText("CHARLIE BROWN");
        bundle.addEntry().setResource(rp3);

        String bundleJson = fhirContext.newJsonParser().encodeResourceToString(bundle);

        Task task = new Task();
        task.setId("Task/TASK-MULTI-RP");
        task.setStatus(Task.TaskStatus.INPROGRESS);

        task.addInput().setValue(new StringType(bundleJson));

        Task resultTask = activity.extractRelatedPersonsToTaskOutput(task);

        assertThat(resultTask.getOutput()).hasSize(3);

        for (int i = 0; i < 3; i++) {
            Task.TaskOutputComponent out = resultTask.getOutput().get(i);
            assertThat(out.getType().getText()).isEqualTo("RelatedPerson Resource");
            String json = ((StringType) out.getValue()).getValue();
            RelatedPerson rp = fhirContext.newJsonParser().parseResource(RelatedPerson.class, json);
            assertThat(rp).isNotNull();
        }

        RelatedPerson first = fhirContext.newJsonParser().parseResource(RelatedPerson.class, ((StringType) resultTask.getOutput().get(0).getValue()).getValue());
        RelatedPerson second = fhirContext.newJsonParser().parseResource(RelatedPerson.class, ((StringType) resultTask.getOutput().get(1).getValue()).getValue());
        RelatedPerson third = fhirContext.newJsonParser().parseResource(RelatedPerson.class, ((StringType) resultTask.getOutput().get(2).getValue()).getValue());

        assertThat(first.getIdPart()).isEqualTo("RP-AAA");
        assertThat(second.getIdPart()).isEqualTo("RP-BBB");
        assertThat(third.getIdPart()).isEqualTo("RP-CCC");
    }

    @Test
    void testExtractFromMultipleTaskInputs() {
        Bundle bundle1 = new Bundle();
        RelatedPerson rp1 = new RelatedPerson();
        rp1.setId("RelatedPerson/RP1");
        bundle1.addEntry().setResource(rp1);

        Bundle bundle2 = new Bundle();
        RelatedPerson rp2 = new RelatedPerson();
        rp2.setId("RelatedPerson/RP2");
        bundle2.addEntry().setResource(rp2);

        Task task = new Task();
        task.setId("Task/TASK-2-INPUTS");
        task.addInput().setValue(new StringType(fhirContext.newJsonParser().encodeResourceToString(bundle1)));
        task.addInput().setValue(new StringType(fhirContext.newJsonParser().encodeResourceToString(bundle2)));

        Task resultTask = activity.extractRelatedPersonsToTaskOutput(task);

        assertThat(resultTask.getOutput()).hasSize(2);
    }

    @Test
    void testExtractFromAttachmentInput() {
        Bundle bundle = new Bundle();
        RelatedPerson rp = new RelatedPerson();
        rp.setId("RelatedPerson/ATTACH-RP");
        bundle.addEntry().setResource(rp);

        String bundleJson = fhirContext.newJsonParser().encodeResourceToString(bundle);

        Task task = new Task();
        task.setId("Task/TASK-ATTACH");
        Attachment attachment = new Attachment();
        attachment.setContentType("application/fhir+json");
        attachment.setData(bundleJson.getBytes(StandardCharsets.UTF_8));
        task.addInput().setValue(attachment);

        Task resultTask = activity.extractRelatedPersonsToTaskOutput(task);

        assertThat(resultTask.getOutput()).hasSize(1);
        RelatedPerson extracted = fhirContext.newJsonParser().parseResource(RelatedPerson.class, ((StringType) resultTask.getOutput().get(0).getValue()).getValue());
        assertThat(extracted.getIdPart()).isEqualTo("ATTACH-RP");
    }

    @Test
    void testExtractFromContainedBundleReference() {
        Bundle bundle = new Bundle();
        bundle.setId("contained-bundle-rp");
        RelatedPerson rp = new RelatedPerson();
        rp.setId("RelatedPerson/CONTAINED-RP");
        bundle.addEntry().setResource(rp);

        Task task = new Task();
        task.setId("Task/TASK-CONTAINED");
        task.addContained(bundle);

        Task.TaskInputComponent input = task.addInput();
        input.setValue(new Reference("#contained-bundle-rp"));

        Task resultTask = activity.extractRelatedPersonsToTaskOutput(task);

        assertThat(resultTask.getOutput()).hasSize(1);
        RelatedPerson extracted = fhirContext.newJsonParser().parseResource(RelatedPerson.class, ((StringType) resultTask.getOutput().get(0).getValue()).getValue());
        assertThat(extracted.getIdPart()).isEqualTo("CONTAINED-RP");
    }

    @Test
    void testExtractWhenNoRelatedPersonInBundle() {
        Bundle bundle = new Bundle();
        Patient pat = new Patient();
        pat.setId("Patient/PAT-01");
        bundle.addEntry().setResource(pat);

        Task task = new Task();
        task.setId("Task/TASK-NO-RP");
        task.addInput().setValue(new StringType(fhirContext.newJsonParser().encodeResourceToString(bundle)));

        Task resultTask = activity.extractRelatedPersonsToTaskOutput(task);

        assertThat(resultTask.getOutput()).isEmpty();
    }

    @Test
    void testCamelRouteExecution() throws Exception {
        activity.setInputEndpoint("direct:test-bundle-rp-in");
        activity.setOutputEndpoint("mock:test-bundle-rp-out");

        camelContext.addRoutes(activity);
        camelContext.start();

        // Create initial Task with a Bundle containing 2 RelatedPersons
        Bundle bundle = new Bundle();
        RelatedPerson rp1 = new RelatedPerson();
        rp1.setId("RelatedPerson/RP-ROUTE-1");
        rp1.addName().setFamily("DOE").addGiven("JOHN").setText("JOHN DOE");
        rp1.setPatient(new Reference("Patient/PAT-1001"));
        rp1.addRelationship().addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v3-RoleCode", "SPS", "Spouse"));
        bundle.addEntry().setResource(rp1);

        RelatedPerson rp2 = new RelatedPerson();
        rp2.setId("RelatedPerson/RP-ROUTE-2");
        rp2.addName().setFamily("DOE").addGiven("MARY").setText("MARY DOE");
        rp2.setPatient(new Reference("Patient/PAT-1001"));
        rp2.addRelationship().addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v3-RoleCode", "MTH", "Mother"));
        bundle.addEntry().setResource(rp2);

        String bundleJson = fhirContext.newJsonParser().encodeResourceToString(bundle);

        Task initialTask = new Task();
        initialTask.setId("Task/TASK-RP-ROUTE-001");
        initialTask.setStatus(Task.TaskStatus.INPROGRESS);
        initialTask.addInput().setValue(new StringType(bundleJson));

        // Save to cache so ingress can retrieve it
        taskCacheService.saveTask(initialTask);

        Exchange exchange = camelContext.getEndpoint("direct:test-bundle-rp-in").createExchange();
        exchange.getMessage().setHeader(TaskProcessingActivity.HEADER_TASK_ID, "TASK-RP-ROUTE-001");
        exchange.getMessage().setBody(initialTask);

        Exchange resultExchange = producerTemplate.send("direct:test-bundle-rp-in", exchange);

        assertThat(resultExchange.isFailed()).isFalse();

        // Egress verification
        Object body = resultExchange.getMessage().getBody();
        assertThat(body).isInstanceOf(TaskEvent.class);
        TaskEvent primaryEvent = (TaskEvent) body;
        assertThat(primaryEvent.getTaskId()).startsWith("TASK-RP-ROUTE-001-out-");

        // Verify outgoing tasks in exchange properties
        @SuppressWarnings("unchecked")
        List<Task> outgoingTasks = (List<Task>) resultExchange.getProperty(TaskProcessingActivity.PROPERTY_OUTGOING_TASKS);
        assertThat(outgoingTasks).hasSize(2);

        // Verify task outputs persisted in cache
        Optional<Task> cachedMainTask = taskCacheService.getTask("TASK-RP-ROUTE-001");
        assertThat(cachedMainTask).isPresent();
        assertThat(cachedMainTask.get().getOutput()).hasSize(2);

        // Verify discrete child tasks persisted in cache
        Optional<Task> child1 = taskCacheService.getTask("TASK-RP-ROUTE-001-out-1");
        Optional<Task> child2 = taskCacheService.getTask("TASK-RP-ROUTE-001-out-2");
        assertThat(child1).isPresent();
        assertThat(child2).isPresent();

        // Verify child task inputs contain the related person JSON
        assertThat(child1.get().getInput()).hasSize(1);
        String rp1Json = ((StringType) child1.get().getInputFirstRep().getValue()).getValue();
        RelatedPerson cachedRp1 = fhirContext.newJsonParser().parseResource(RelatedPerson.class, rp1Json);
        assertThat(cachedRp1.getIdPart()).isEqualTo("RP-ROUTE-1");

        // Verify exchange headers
        assertThat(resultExchange.getMessage().getHeader(ExtractRelatedPersonFromBundle.HEADER_RELATED_PERSON_COUNT)).isEqualTo(2);
        assertThat(resultExchange.getMessage().getHeader(ExtractRelatedPersonFromBundle.HEADER_RELATED_PERSON_ID)).isEqualTo("RP-ROUTE-1");
        assertThat(resultExchange.getMessage().getHeader(ExtractRelatedPersonFromBundle.HEADER_RELATED_PERSON_NAME)).isEqualTo("JOHN DOE");
        assertThat(resultExchange.getMessage().getHeader(ExtractRelatedPersonFromBundle.HEADER_RELATED_PERSON_PATIENT)).isEqualTo("Patient/PAT-1001");
        assertThat(resultExchange.getMessage().getHeader(ExtractRelatedPersonFromBundle.HEADER_RELATED_PERSON_RELATIONSHIP)).isEqualTo("Spouse");
    }
}
