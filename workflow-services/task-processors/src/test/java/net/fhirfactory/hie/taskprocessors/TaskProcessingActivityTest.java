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

package net.fhirfactory.hie.taskprocessors;

import net.fhirfactory.hie.model.TaskEvent;
import net.fhirfactory.hie.model.topic.Topic;
import net.fhirfactory.hie.taskprocessor.cache.TaskCacheService;
import net.fhirfactory.hie.taskprocessors.base.TaskProcessingActivity;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.hl7.fhir.r5.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TaskProcessingActivityTest {

    private CamelContext camelContext;
    private ProducerTemplate producerTemplate;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        producerTemplate = camelContext.createProducerTemplate();
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

    static class SampleTaskActivity extends TaskProcessingActivity {
        private boolean activityConfigured = false;

        public SampleTaskActivity() {
            super("sample-activity-1", "Sample Task Activity");
            setInputEndpoint("direct:sample-input");
            setOutputEndpoint("mock:sample-output");
        }

        public SampleTaskActivity(CamelContext context) {
            super(context, "sample-activity-ctx", "Sample Context Activity");
        }

        @Override
        protected void configureActivity() throws Exception {
            this.activityConfigured = true;
            applyStandardActivityDecorations(
                    from(getInputEndpoint() != null ? getInputEndpoint() : "direct:default-in")
            ).transform().simple("Processed: ${body}")
             .to(getOutputEndpoint() != null ? getOutputEndpoint() : "mock:default-out");
        }

        public boolean isActivityConfigured() {
            return activityConfigured;
        }
    }

    static class DummyProcessorActivity extends TaskProcessingActivity {
        public DummyProcessorActivity() {
            super("dummy-processor", "Dummy Task Processor");
            setInputEndpoint("direct:dummy-in");
            setOutputEndpoint("mock:dummy-out");
        }

        @Override
        protected void processActivity(Exchange exchange) throws Exception {
            // Retrieve ingress Task from message body
            Task task = exchange.getMessage().getBody(Task.class);
            assertThat(task).isNotNull();

            // Perform business processing: attach patient and add discrete outputs
            Patient patient = new Patient();
            patient.setId("Patient/PAT-TEST-001");
            patient.addName().setFamily("Smith").addGiven("John").setText("John Smith");
            task.addContained(patient);
            task.setFor(new Reference("Patient/PAT-TEST-001").setDisplay("John Smith"));

            // Discrete Output 1: Clinical Observation Output
            Task.TaskOutputComponent out1 = task.addOutput();
            out1.getType().setText("Observation Resource").addCoding()
                    .setSystem("http://hl7.org/fhir/resource-types")
                    .setCode("Observation");
            out1.setValue(new Reference("Observation/OBS-101").setDisplay("Vital Signs Observation"));

            // Discrete Output 2: Diagnostic Report Output
            Task.TaskOutputComponent out2 = task.addOutput();
            out2.getType().setText("DiagnosticReport Resource").addCoding()
                    .setSystem("http://hl7.org/fhir/resource-types")
                    .setCode("DiagnosticReport");
            out2.setValue(new Reference("DiagnosticReport/REP-202").setDisplay("CBC Diagnostic Report"));

            task.setStatus(Task.TaskStatus.COMPLETED);

            // Set Task as OUT body for egress processing
            exchange.getMessage().setBody(task);
        }
    }

    @Test
    void testInheritanceAndProperties() {
        SampleTaskActivity activity = new SampleTaskActivity();
        assertThat(activity).isInstanceOf(RouteBuilder.class);
        assertThat(activity).isInstanceOf(TaskProcessingActivity.class);

        assertThat(activity.getActivityId()).isEqualTo("sample-activity-1");
        assertThat(activity.getActivityName()).isEqualTo("Sample Task Activity");
        assertThat(activity.getVersion()).isEqualTo("1.0.0");
        assertThat(activity.isEnabled()).isTrue();

        activity.setActivityDescription("Sample description");
        activity.setVersion("2.1.0");
        activity.setErrorEndpoint("direct:error-queue");

        assertThat(activity.getActivityDescription()).isEqualTo("Sample description");
        assertThat(activity.getVersion()).isEqualTo("2.1.0");
        assertThat(activity.getErrorEndpoint()).isEqualTo("direct:error-queue");
        assertThat(activity.generateRouteId()).isEqualTo("activity-sample-activity-1");
        assertThat(activity.toString()).contains("sample-activity-1").contains("2.1.0");
    }

    @Test
    void testDefaultConstructors() {
        TaskProcessingActivity defaultActivity = new TaskProcessingActivity() {
            @Override
            protected void configureActivity() {
            }
        };

        assertThat(defaultActivity.getActivityName()).isEqualTo(defaultActivity.getClass().getSimpleName());
        assertThat(defaultActivity.getActivityId()).isEqualTo(defaultActivity.getClass().getName());
        assertThat(defaultActivity.generateRouteId()).startsWith("activity-");
    }

    @Test
    void testContextConstructors() {
        SampleTaskActivity activity = new SampleTaskActivity(camelContext);
        assertThat(activity.getCamelContext()).isEqualTo(camelContext);
        assertThat(activity.getActivityId()).isEqualTo("sample-activity-ctx");
        assertThat(activity.getActivityName()).isEqualTo("Sample Context Activity");
    }

    @Test
    void testRouteExecutionInCamelContext() throws Exception {
        SampleTaskActivity activity = new SampleTaskActivity();
        camelContext.addRoutes(activity);
        camelContext.start();

        assertThat(activity.isActivityConfigured()).isTrue();
        assertThat(camelContext.getRoute("activity-sample-activity-1")).isNotNull();

        String result = producerTemplate.requestBody("direct:sample-input", "Task Payload 123", String.class);
        assertThat(result).isEqualTo("Processed: Task Payload 123");
    }

    @Test
    void testDisabledActivitySkipsConfiguration() throws Exception {
        SampleTaskActivity activity = new SampleTaskActivity();
        activity.setEnabled(false);
        camelContext.addRoutes(activity);
        camelContext.start();

        assertThat(activity.isActivityConfigured()).isFalse();
        assertThat(camelContext.getRoutes()).isEmpty();
    }

    @Test
    @DisplayName("Requirement 1: TaskProcessingActivity retrieves Task from cache on ingress and passes it as message body")
    void testTaskRetrievalFromCacheOnIngress() throws Exception {
        DummyProcessorActivity activity = new DummyProcessorActivity();
        TaskCacheService cacheService = activity.getTaskCacheService();

        // Seed an existing Task into the cache
        Task initialTask = new Task();
        initialTask.setId("Task/TASK-CACHE-INGRESS-001");
        initialTask.setStatus(Task.TaskStatus.REQUESTED);
        initialTask.setDescription("Pre-existing cached task");
        initialTask.setAuthoredOn(new Date());
        cacheService.saveTask(initialTask);

        Exchange exchange = new org.apache.camel.support.DefaultExchange(camelContext);
        exchange.getMessage().setHeader(TaskProcessingActivity.HEADER_TASK_ID, "TASK-CACHE-INGRESS-001");

        // Run ingress
        activity.processIngress(exchange);

        // Verify that the retrieved Task from cache is now the message body
        Object body = exchange.getMessage().getBody();
        assertThat(body).isInstanceOf(Task.class);
        Task ingressTask = (Task) body;
        assertThat(ingressTask.getIdPart()).isEqualTo("TASK-CACHE-INGRESS-001");
        assertThat(ingressTask.getDescription()).isEqualTo("Pre-existing cached task");
        assertThat(exchange.getProperty(TaskProcessingActivity.PROPERTY_INCOMING_TASK)).isEqualTo(ingressTask);
    }

    @Test
    @DisplayName("Requirement 1: TaskProcessingActivity creates baseline Task if not already in cache on ingress")
    void testBaselineTaskCreatedIfNotInCacheOnIngress() throws Exception {
        DummyProcessorActivity activity = new DummyProcessorActivity();

        Exchange exchange = new org.apache.camel.support.DefaultExchange(camelContext);
        exchange.getMessage().setHeader(TaskProcessingActivity.HEADER_TASK_ID, "TASK-NEW-002");
        exchange.getMessage().setBody("Raw HL7 or JSON string payload");

        // Run ingress
        activity.processIngress(exchange);

        // Verify baseline Task was created and saved to cache
        Object body = exchange.getMessage().getBody();
        assertThat(body).isInstanceOf(Task.class);
        Task ingressTask = (Task) body;
        assertThat(ingressTask.getIdPart()).isEqualTo("TASK-NEW-002");
        assertThat(ingressTask.getStatus()).isEqualTo(Task.TaskStatus.REQUESTED);

        Optional<Task> inCache = activity.getTaskCacheService().getTask("TASK-NEW-002");
        assertThat(inCache).isPresent();
    }

    @Test
    @DisplayName("Requirement 2: TaskProcessingActivity receives Task from processing, writes to cache, creates child Tasks for discrete outputs, creates Provenance, and emits TaskEvent")
    void testEgressProcessingAndProvenanceAndTaskEvents() throws Exception {
        DummyProcessorActivity activity = new DummyProcessorActivity();
        TaskCacheService cacheService = activity.getTaskCacheService();

        // Create Task with 2 discrete outputs in Task.output
        Task processedTask = new Task();
        processedTask.setId("Task/TASK-PARENT-888");
        processedTask.setStatus(Task.TaskStatus.COMPLETED);
        processedTask.setAuthoredOn(new Date());
        processedTask.setFor(new Reference("Patient/PAT-999").setDisplay("Jane Doe"));

        // Output 1
        Task.TaskOutputComponent out1 = processedTask.addOutput();
        out1.getType().setText("Lab Result").addCoding().setCode("Observation").setDisplay("Observation");
        out1.setValue(new Reference("Observation/OBS-1").setDisplay("Blood Glucose"));

        // Output 2
        Task.TaskOutputComponent out2 = processedTask.addOutput();
        out2.getType().setText("Prescription").addCoding().setCode("MedicationRequest").setDisplay("MedicationRequest");
        out2.setValue(new Reference("MedicationRequest/MED-2").setDisplay("Metformin"));

        Exchange exchange = new org.apache.camel.support.DefaultExchange(camelContext);
        exchange.getMessage().setBody(processedTask);
        exchange.getMessage().setHeader(TaskProcessingActivity.HEADER_TASK_ID, "TASK-PARENT-888");
        exchange.getMessage().setHeader(TaskProcessingActivity.HEADER_GATEWAY_INSTANCE, "mllp-gw-test");
        exchange.getMessage().setHeader(TaskProcessingActivity.HEADER_TRIGGER_TYPE, "A01");

        // Execute egress processing
        activity.processEgress(exchange);

        // (a) Task is written to cache
        Optional<Task> cachedParent = cacheService.getTask("TASK-PARENT-888");
        assertThat(cachedParent).isPresent();
        assertThat(cachedParent.get().getStatus()).isEqualTo(Task.TaskStatus.COMPLETED);

        // (b) New Task resource created for each discrete object contained within Task.output
        List<Task> createdOutgoingTasks = activity.createOutgoingTasks(processedTask);
        assertThat(createdOutgoingTasks).hasSize(2);

        Task childTask1 = createdOutgoingTasks.get(0);
        assertThat(childTask1.getIdPart()).isEqualTo("TASK-PARENT-888-out-1");
        assertThat(childTask1.getPartOfFirstRep().getReference()).isEqualTo("Task/TASK-PARENT-888");
        assertThat(childTask1.getFor().getReference()).isEqualTo("Patient/PAT-999");
        assertThat(childTask1.getInput()).hasSize(1);
        assertThat(childTask1.getInputFirstRep().getValue()).isInstanceOf(Reference.class);
        assertThat(((Reference) childTask1.getInputFirstRep().getValue()).getReference()).isEqualTo("Observation/OBS-1");

        Task childTask2 = createdOutgoingTasks.get(1);
        assertThat(childTask2.getIdPart()).isEqualTo("TASK-PARENT-888-out-2");
        assertThat(childTask2.getPartOfFirstRep().getReference()).isEqualTo("Task/TASK-PARENT-888");
        assertThat(childTask2.getInputFirstRep().getValue()).isInstanceOf(Reference.class);
        assertThat(((Reference) childTask2.getInputFirstRep().getValue()).getReference()).isEqualTo("MedicationRequest/MED-2");

        // Verify both child tasks are saved in cache
        assertThat(cacheService.getTask("TASK-PARENT-888-out-1")).isPresent();
        assertThat(cacheService.getTask("TASK-PARENT-888-out-2")).isPresent();

        // (c) Provenance object traces relationship between incoming Task and created outgoing tasks
        @SuppressWarnings("unchecked")
        List<Provenance> provenances = (List<Provenance>) exchange.getProperty(TaskProcessingActivity.PROPERTY_PROVENANCES);
        assertThat(provenances).hasSize(2);

        Provenance prov1 = provenances.get(0);
        assertThat(prov1.getTarget()).hasSize(1);
        assertThat(prov1.getTarget().get(0).getReference()).isEqualTo("Task/TASK-PARENT-888-out-1");
        assertThat(prov1.getEntityFirstRep().getWhat().getReference()).isEqualTo("Task/TASK-PARENT-888");
        assertThat(prov1.getEntityFirstRep().getRole()).isEqualTo(Provenance.ProvenanceEntityRole.SOURCE);
        assertThat(prov1.getActivity().getText()).isEqualTo(activity.getActivityName());

        Provenance prov2 = provenances.get(1);
        assertThat(prov2.getTarget()).hasSize(1);
        assertThat(prov2.getTarget().get(0).getReference()).isEqualTo("Task/TASK-PARENT-888-out-2");
        assertThat(prov2.getEntityFirstRep().getWhat().getReference()).isEqualTo("Task/TASK-PARENT-888");

        // Verify both provenances are stored in cache
        assertThat(cacheService.getProvenance(prov1.getIdPart())).isPresent();
        assertThat(cacheService.getProvenance(prov2.getIdPart())).isPresent();

        Provenance singleProv = (Provenance) exchange.getProperty(TaskProcessingActivity.PROPERTY_PROVENANCE);
        assertThat(singleProv).isNotNull();
        assertThat(singleProv.getTarget().get(0).getReference()).isEqualTo("Task/TASK-PARENT-888-out-1");

        // (d) TaskEvent created for forwarding into next route element
        @SuppressWarnings("unchecked")
        List<TaskEvent> taskEvents = (List<TaskEvent>) exchange.getProperty(TaskProcessingActivity.PROPERTY_TASK_EVENTS);
        assertThat(taskEvents).hasSize(2);
        assertThat(taskEvents.get(0).getTaskId()).isEqualTo("TASK-PARENT-888-out-1");
        assertThat(taskEvents.get(1).getTaskId()).isEqualTo("TASK-PARENT-888-out-2");

        Object egressBody = exchange.getMessage().getBody();
        assertThat(egressBody).isInstanceOf(TaskEvent.class);
        TaskEvent event = (TaskEvent) egressBody;
        assertThat(event.getTaskId()).isEqualTo("TASK-PARENT-888-out-1");
        assertThat(event.getAction()).isEqualTo("PROCESS");
        assertThat(event.getStatus()).isEqualTo("requested");
        assertThat(event.getGatewayInstanceId()).isEqualTo("mllp-gw-test");
        assertThat(event.getTriggerType()).isEqualTo("A01");
        assertThat(exchange.getMessage().getHeader(TaskProcessingActivity.HEADER_TASK_PROCESSED)).isEqualTo(true);
    }

    @Test
    @DisplayName("Full Activity Route: ingress -> processActivity -> egress executes end-to-end")
    void testFullActivityRouteExecution() throws Exception {
        DummyProcessorActivity activity = new DummyProcessorActivity();
        camelContext.addRoutes(activity);
        camelContext.start();

        MockEndpoint mockOut = camelContext.getEndpoint("mock:dummy-out", MockEndpoint.class);
        mockOut.expectedMessageCount(1);

        Exchange resultExchange = producerTemplate.send("direct:dummy-in", exchange -> {
            exchange.getMessage().setHeader(TaskProcessingActivity.HEADER_TASK_ID, "TASK-E2E-001");
            exchange.getMessage().setHeader(TaskProcessingActivity.HEADER_GATEWAY_INSTANCE, "mllp-gateway-default");
            exchange.getMessage().setHeader(TaskProcessingActivity.HEADER_TRIGGER_TYPE, "A01");
            Topic topic = new Topic();
            topic.setDomain("Health");
            topic.setModel("HL7");
            topic.setModelVersion("2.4");
            topic.setDataElement("ADT");
            topic.setDataElementQualifier("A01");
            exchange.getMessage().setHeader(TaskProcessingActivity.HEADER_TOPIC, topic);
        });

        mockOut.assertIsSatisfied(2000);

        // Body out of route is TaskEvent
        TaskEvent emittedEvent = mockOut.getExchanges().get(0).getMessage().getBody(TaskEvent.class);
        assertThat(emittedEvent).isNotNull();
        assertThat(emittedEvent.getAction()).isEqualTo("PROCESS");
        assertThat(emittedEvent.getStatus()).isEqualTo("requested");
        assertThat(emittedEvent.getTaskId()).startsWith("TASK-E2E-001-out-1");
        assertThat(emittedEvent.getGatewayInstanceId()).isEqualTo("mllp-gateway-default");
        assertThat(emittedEvent.getTriggerType()).isEqualTo("A01");
        assertThat(emittedEvent.getTopic()).isNotNull();
        assertThat(emittedEvent.getTopic().getDataElementQualifier()).isEqualTo("A01");

        // Verify parent and child tasks in cache
        Task parentTask = activity.getTaskCacheService().getTask("TASK-E2E-001").orElse(null);
        assertThat(parentTask).isNotNull();
        assertThat(parentTask.getFor().getDisplay()).isEqualTo("John Smith");
        assertThat(parentTask.getOutput()).hasSize(2);

        Task childTask1 = activity.getTaskCacheService().getTask("TASK-E2E-001-out-1").orElse(null);
        assertThat(childTask1).isNotNull();
        assertThat(childTask1.getPartOfFirstRep().getReference()).isEqualTo("Task/TASK-E2E-001");

        Task childTask2 = activity.getTaskCacheService().getTask("TASK-E2E-001-out-2").orElse(null);
        assertThat(childTask2).isNotNull();
        assertThat(childTask2.getPartOfFirstRep().getReference()).isEqualTo("Task/TASK-E2E-001");

        // Verify Provenance
        @SuppressWarnings("unchecked")
        List<Provenance> provList = (List<Provenance>) resultExchange.getProperty(TaskProcessingActivity.PROPERTY_PROVENANCES);
        assertThat(provList).hasSize(2);
        assertThat(provList.get(0).getTargetFirstRep().getReference()).isEqualTo("Task/TASK-E2E-001-out-1");
        assertThat(provList.get(0).getEntityFirstRep().getWhat().getReference()).isEqualTo("Task/TASK-E2E-001");
        assertThat(provList.get(1).getTargetFirstRep().getReference()).isEqualTo("Task/TASK-E2E-001-out-2");
        assertThat(provList.get(1).getEntityFirstRep().getWhat().getReference()).isEqualTo("Task/TASK-E2E-001");

        @SuppressWarnings("unchecked")
        List<TaskEvent> eventList = (List<TaskEvent>) resultExchange.getProperty(TaskProcessingActivity.PROPERTY_TASK_EVENTS);
        assertThat(eventList).hasSize(2);
        assertThat(eventList.get(0).getTaskId()).isEqualTo("TASK-E2E-001-out-1");
        assertThat(eventList.get(1).getTaskId()).isEqualTo("TASK-E2E-001-out-2");
    }
}
