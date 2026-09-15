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

package net.fhirfactory.harmonia.erga.fhir;

import ca.uhn.fhir.context.FhirContext;
import net.fhirfactory.harmonia.model.ergon.ErgonEvent;
import net.fhirfactory.harmonia.praxis.cache.TaskCacheService;
import net.fhirfactory.harmonia.erga.base.ErgonBase;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
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

class ExtractPersonFromBundleTest {

    private CamelContext camelContext;
    private ProducerTemplate producerTemplate;
    private ExtractPersonFromBundle activity;
    private TaskCacheService taskCacheService;
    private FhirContext fhirContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        producerTemplate = camelContext.createProducerTemplate();
        activity = new ExtractPersonFromBundle();
        fhirContext = FhirContext.forR5();
        taskCacheService = new TaskCacheService(null, fhirContext);
        activity.setTaskCacheService(taskCacheService);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (camelContext != null) {
            camelContext.stop();
        }
    }

    private Bundle createSampleBundleWithPerson(String personId, String family, String given, String identifier) {
        Bundle bundle = new Bundle();
        bundle.setId("bundle-1");
        bundle.setType(Bundle.BundleType.COLLECTION);

        Person person = new Person();
        person.setId(personId);
        person.getNameFirstRep().setFamily(family).addGiven(given);
        person.setGender(Enumerations.AdministrativeGender.FEMALE);
        if (identifier != null) {
            person.addIdentifier().setSystem("http://hospital.example.org/persons").setValue(identifier);
        }

        bundle.addEntry().setResource(person);
        return bundle;
    }

    @Test
    void testExtractPersonFromBundleStringInput() {
        Bundle bundle = createSampleBundleWithPerson("person-100", "Smith", "Alice", "PER10001");
        String bundleJson = fhirContext.newJsonParser().encodeResourceToString(bundle);

        Task task = new Task();
        task.setId("Task/task-1");
        task.setStatus(Task.TaskStatus.INPROGRESS);
        task.setAuthoredOn(new Date());

        task.addInput().setValue(new StringType(bundleJson));

        Task result = activity.extractPersonsToTaskOutput(task);

        assertThat(result.getOutput()).hasSize(1);
        Task.TaskOutputComponent output = result.getOutput().get(0);
        assertThat(output.getType().getText()).isEqualTo("Person Resource");
        assertThat(output.getValue()).isInstanceOf(StringType.class);

        String personJson = ((StringType) output.getValue()).getValue();
        Person extracted = (Person) fhirContext.newJsonParser().parseResource(personJson);
        assertThat(extracted.getIdPart()).isEqualTo("person-100");
        assertThat(extracted.getNameFirstRep().getFamily()).isEqualTo("Smith");
        assertThat(extracted.getNameFirstRep().getGiven().get(0).getValue()).isEqualTo("Alice");
        assertThat(extracted.getIdentifierFirstRep().getValue()).isEqualTo("PER10001");

        assertThat(result.hasFor()).isTrue();
        assertThat(result.getFor().getReference()).isEqualTo("Person/person-100");
        assertThat(result.getFor().getDisplay()).isEqualTo("Alice Smith");
    }

    @Test
    void testExtractMultiplePersonsFromBundle() {
        Bundle bundle = new Bundle();
        bundle.setId("bundle-multi");
        bundle.setType(Bundle.BundleType.COLLECTION);

        Person p1 = new Person();
        p1.setId("person-1");
        p1.getNameFirstRep().setFamily("Doe").addGiven("John");
        p1.addIdentifier().setValue("PER-1");

        Person p2 = new Person();
        p2.setId("person-2");
        p2.getNameFirstRep().setFamily("Doe").addGiven("Jane");
        p2.addIdentifier().setValue("PER-2");

        bundle.addEntry().setResource(p1);
        bundle.addEntry().setResource(p2);

        String bundleJson = fhirContext.newJsonParser().encodeResourceToString(bundle);

        Task task = new Task();
        task.setId("Task/task-multi");
        task.addInput().setValue(new StringType(bundleJson));

        Task result = activity.extractPersonsToTaskOutput(task);

        assertThat(result.getOutput()).hasSize(2);

        Person extracted1 = (Person) fhirContext.newJsonParser().parseResource(((StringType) result.getOutput().get(0).getValue()).getValue());
        Person extracted2 = (Person) fhirContext.newJsonParser().parseResource(((StringType) result.getOutput().get(1).getValue()).getValue());

        assertThat(extracted1.getIdPart()).isEqualTo("person-1");
        assertThat(extracted2.getIdPart()).isEqualTo("person-2");
    }

    @Test
    void testExtractPersonsFromNestedBundles() {
        Bundle outerBundle = new Bundle();
        outerBundle.setId("bundle-outer");

        Bundle innerBundle = createSampleBundleWithPerson("person-nested", "Johnson", "Bob", "PER-NEST");
        outerBundle.addEntry().setResource(innerBundle);

        String bundleJson = fhirContext.newJsonParser().encodeResourceToString(outerBundle);

        Task task = new Task();
        task.setId("Task/task-nested");
        task.addInput().setValue(new StringType(bundleJson));

        List<Person> extracted = activity.extractPersonsFromTaskInputs(task);
        assertThat(extracted).hasSize(1);
        assertThat(extracted.get(0).getIdPart()).isEqualTo("person-nested");
    }

    @Test
    void testExtractPersonsFromAttachmentInput() {
        Bundle bundle = createSampleBundleWithPerson("person-attachment", "Williams", "Charlie", "PER-ATT");
        String bundleJson = fhirContext.newJsonParser().encodeResourceToString(bundle);

        Task task = new Task();
        task.setId("Task/task-att");

        Attachment attachment = new Attachment();
        attachment.setContentType("application/fhir+json");
        attachment.setData(bundleJson.getBytes(StandardCharsets.UTF_8));
        task.addInput().setValue(attachment);

        List<Person> extracted = activity.extractPersonsFromTaskInputs(task);
        assertThat(extracted).hasSize(1);
        assertThat(extracted.get(0).getIdPart()).isEqualTo("person-attachment");
    }

    @Test
    void testExtractFromContainedBundleReference() {
        Bundle bundle = new Bundle();
        bundle.setId("contained-bundle-person");
        Person person = new Person();
        person.setId("Person/CONTAINED-PER");
        bundle.addEntry().setResource(person);

        Task task = new Task();
        task.setId("Task/TASK-CONTAINED");
        task.addContained(bundle);

        Task.TaskInputComponent input = task.addInput();
        input.setValue(new Reference("#contained-bundle-person"));

        Task resultTask = activity.extractPersonsToTaskOutput(task);

        assertThat(resultTask.getOutput()).hasSize(1);
        Person extracted = fhirContext.newJsonParser().parseResource(Person.class, ((StringType) resultTask.getOutput().get(0).getValue()).getValue());
        assertThat(extracted.getIdPart()).isEqualTo("CONTAINED-PER");
    }

    @Test
    void testExtractWhenNoPersonInBundle() {
        Bundle bundle = new Bundle();
        Patient pat = new Patient();
        pat.setId("Patient/PAT-01");
        bundle.addEntry().setResource(pat);

        Task task = new Task();
        task.setId("Task/TASK-NO-PERSON");
        task.addInput().setValue(new StringType(fhirContext.newJsonParser().encodeResourceToString(bundle)));

        Task resultTask = activity.extractPersonsToTaskOutput(task);

        assertThat(resultTask.getOutput()).isEmpty();
    }

    @Test
    void testProcessPersonResourceExtractionHeaders() {
        Bundle bundle = createSampleBundleWithPerson("person-300", "Taylor", "Sarah", "PER-TAYLOR");
        String bundleJson = fhirContext.newJsonParser().encodeResourceToString(bundle);

        Task task = new Task();
        task.setId("Task/task-hdr");
        task.addInput().setValue(new StringType(bundleJson));

        Exchange exchange = camelContext.getCamelContextExtension().getExchangeFactory().create(camelContext.getEndpoint("direct:test"), false);
        exchange.getMessage().setBody(task);

        activity.processPersonResourceExtraction(exchange);

        assertThat(exchange.getMessage().getHeader(ExtractPersonFromBundle.HEADER_PERSON_ID, String.class)).isEqualTo("person-300");
        assertThat(exchange.getMessage().getHeader(ExtractPersonFromBundle.HEADER_PERSON_NAME, String.class)).isEqualTo("Sarah Taylor");
        assertThat(exchange.getMessage().getHeader(ExtractPersonFromBundle.HEADER_PERSON_IDENTIFIER, String.class)).isEqualTo("PER-TAYLOR");
        assertThat(exchange.getMessage().getHeader(ExtractPersonFromBundle.HEADER_PERSON_COUNT, Integer.class)).isEqualTo(1);
    }

    @Test
    void testCamelRouteExecution() throws Exception {
        activity.setInputEndpoint("direct:test-bundle-person-in");
        activity.setOutputEndpoint("mock:test-bundle-person-out");

        camelContext.addRoutes(activity);
        camelContext.start();

        Bundle bundle = new Bundle();
        Person p1 = new Person();
        p1.setId("Person/PER-ROUTE-1");
        p1.addName().setFamily("DOE").addGiven("JOHN").setText("JOHN DOE");
        p1.addIdentifier().setSystem("http://hospital.example.org/persons").setValue("PER-1001");
        bundle.addEntry().setResource(p1);

        Person p2 = new Person();
        p2.setId("Person/PER-ROUTE-2");
        p2.addName().setFamily("DOE").addGiven("MARY").setText("MARY DOE");
        p2.addIdentifier().setSystem("http://hospital.example.org/persons").setValue("PER-1002");
        bundle.addEntry().setResource(p2);

        String bundleJson = fhirContext.newJsonParser().encodeResourceToString(bundle);

        Task initialTask = new Task();
        initialTask.setId("Task/TASK-PER-ROUTE-001");
        initialTask.setStatus(Task.TaskStatus.INPROGRESS);
        initialTask.addInput().setValue(new StringType(bundleJson));

        taskCacheService.saveTask(initialTask);

        Exchange exchange = camelContext.getEndpoint("direct:test-bundle-person-in").createExchange();
        exchange.getMessage().setHeader(ErgonBase.HEADER_TASK_ID, "TASK-PER-ROUTE-001");
        exchange.getMessage().setBody(initialTask);

        Exchange resultExchange = producerTemplate.send("direct:test-bundle-person-in", exchange);

        assertThat(resultExchange.isFailed()).isFalse();

        Object body = resultExchange.getMessage().getBody();
        assertThat(body).isInstanceOf(ErgonEvent.class);
        ErgonEvent primaryEvent = (ErgonEvent) body;
        assertThat(primaryEvent.getTaskId()).startsWith("TASK-PER-ROUTE-001-out-");

        @SuppressWarnings("unchecked")
        List<Task> outgoingTasks = (List<Task>) resultExchange.getProperty(ErgonBase.PROPERTY_OUTGOING_TASKS);
        assertThat(outgoingTasks).hasSize(2);

        Optional<Task> cachedMainTask = taskCacheService.getTask("TASK-PER-ROUTE-001");
        assertThat(cachedMainTask).isPresent();
        assertThat(cachedMainTask.get().getOutput()).hasSize(2);
    }

    @Test
    void testConstructors() {
        ExtractPersonFromBundle a1 = new ExtractPersonFromBundle();
        assertThat(a1.getActivityId()).isEqualTo(ExtractPersonFromBundle.DEFAULT_ACTIVITY_ID);
        assertThat(a1.getActivityName()).isEqualTo(ExtractPersonFromBundle.DEFAULT_ACTIVITY_NAME);

        ExtractPersonFromBundle a2 = new ExtractPersonFromBundle(camelContext);
        assertThat(a2.getCamelContext()).isEqualTo(camelContext);

        ExtractPersonFromBundle a3 = new ExtractPersonFromBundle("custom-id", "Custom Name");
        assertThat(a3.getActivityId()).isEqualTo("custom-id");
        assertThat(a3.getActivityName()).isEqualTo("Custom Name");

        ExtractPersonFromBundle a4 = new ExtractPersonFromBundle(camelContext, "custom-id-2", "Custom Name 2");
        assertThat(a4.getActivityId()).isEqualTo("custom-id-2");

        ExtractPersonFromBundle a5 = new ExtractPersonFromBundle(fhirContext);
        assertThat(a5.getFhirContext()).isEqualTo(fhirContext);
    }
}
