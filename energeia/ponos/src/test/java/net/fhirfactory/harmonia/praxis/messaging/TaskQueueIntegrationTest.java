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

package net.fhirfactory.harmonia.praxis.messaging;

import ca.uhn.fhir.context.FhirContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.util.TypeLiteral;
import jakarta.jms.ConnectionFactory;
import net.fhirfactory.harmonia.praxis.cache.TaskCacheService;
import net.fhirfactory.harmonia.praxis.camel.CamelContextManager;
import net.fhirfactory.harmonia.model.ergon.ErgonEvent;
import net.fhirfactory.harmonia.praxis.camel.TaskEventMessageProcessor;
import net.fhirfactory.harmonia.praxis.camel.TaskMessageProcessor;
import net.fhirfactory.harmonia.praxis.camel.TaskProcessorRouteBuilder;
import net.fhirfactory.harmonia.praxis.config.QueueConfig;
import net.fhirfactory.harmonia.petasos.test.harness.EmbeddedArtemisCluster;
import net.fhirfactory.harmonia.praxis.sequence.TaskSequenceLoader;
import net.fhirfactory.harmonia.praxis.service.PraxisService;
import net.fhirfactory.harmonia.erga.base.ErgonBase;
import net.fhirfactory.harmonia.erga.patient.demographics.PatientDemographicsUpdateErgon;
import net.fhirfactory.harmonia.erga.patient.identity.PatientIdentityUpdateErgon;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.Task;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TaskQueueIntegrationTest {

    private static EmbeddedArtemisCluster embeddedCluster;
    private static int brokerPort;
    private static CamelContextManager camelContextManager;
    private static TaskQueueProducerService producerService;
    private static TaskCacheService taskCacheService;
    private static PraxisService sequenceService;
    private static TaskSequenceLoader sequenceLoader;
    private static QueueConfig queueConfig;
    private static FhirContext fhirContext;
    private static ConnectionFactory connectionFactory;

    static class MockActivityInstance<T> implements Instance<T> {
        private final List<T> elements;

        public MockActivityInstance(List<T> elements) {
            this.elements = elements;
        }

        @Override
        public Instance<T> select(Annotation... qualifiers) { return this; }

        @Override
        public <U extends T> Instance<U> select(Class<U> subtype, Annotation... qualifiers) { return null; }

        @Override
        public <U extends T> Instance<U> select(TypeLiteral<U> subtype, Annotation... qualifiers) { return null; }

        @Override
        public boolean isUnsatisfied() { return elements == null || elements.isEmpty(); }

        @Override
        public boolean isAmbiguous() { return elements != null && elements.size() > 1; }

        @Override
        public void destroy(T instance) {}

        @Override
        public Handle<T> getHandle() { return null; }

        @Override
        public Iterable<? extends Handle<T>> handles() { return List.of(); }

        @Override
        public Iterator<T> iterator() { return elements.iterator(); }

        @Override
        public T get() { return elements.isEmpty() ? null : elements.get(0); }
    }

    @BeforeAll
    static void setUpAll() throws Exception {
        embeddedCluster = new EmbeddedArtemisCluster();
        brokerPort = findFreePort();
        embeddedCluster.startStandaloneBroker("test-ponos-broker", brokerPort, false);
        String brokerUrl = "tcp://127.0.0.1:" + brokerPort;

        fhirContext = FhirContext.forR5();
        queueConfig = new QueueConfig();
        queueConfig.setBrokerEnabled(false);
        queueConfig.setBrokerUrl(brokerUrl);

        // Create Connection Factory
        connectionFactory = new ActiveMQConnectionFactory(brokerUrl);

        // Cache Service
        taskCacheService = new TaskCacheService();
        taskCacheService.init();

        // Sequence Service & Loader
        sequenceService = new PraxisService();
        PatientIdentityUpdateErgon patientIdAct = new PatientIdentityUpdateErgon();
        PatientDemographicsUpdateErgon demographicsAct = new PatientDemographicsUpdateErgon();
        Instance<ErgonBase> mockActivities = new MockActivityInstance<>(List.of(patientIdAct, demographicsAct));
        sequenceLoader = new TaskSequenceLoader(sequenceService, mockActivities);

        // Processors & Route
        TaskMessageProcessor processor = new TaskMessageProcessor();
        processor.setFhirContext(fhirContext);
        processor.setTaskCacheService(taskCacheService);

        TaskEventMessageProcessor eventProcessor = new TaskEventMessageProcessor();
        eventProcessor.setFhirContext(fhirContext);
        eventProcessor.setTaskCacheService(taskCacheService);
        eventProcessor.setObjectMapper(new ObjectMapper());

        TaskProcessorRouteBuilder routeBuilder = new TaskProcessorRouteBuilder();
        routeBuilder.setQueueConfig(queueConfig);
        routeBuilder.setTaskMessageProcessor(processor);
        routeBuilder.setTaskEventMessageProcessor(eventProcessor);
        routeBuilder.setTaskSequenceLoader(sequenceLoader);

        // Camel Manager
        camelContextManager = new CamelContextManager();
        setField(camelContextManager, "taskProcessorRouteBuilder", routeBuilder);
        setField(camelContextManager, "taskSequenceLoader", sequenceLoader);
        setField(camelContextManager, "connectionFactory", connectionFactory);
        camelContextManager.startCamel();

        // Producer Service
        producerService = new TaskQueueProducerService();
        setField(producerService, "connectionFactory", connectionFactory);
        setField(producerService, "queueConfig", queueConfig);
        setField(producerService, "fhirContext", fhirContext);
    }

    @BeforeEach
    void setUp() {
        if (taskCacheService != null) {
            taskCacheService.clear();
        }
    }

    @AfterAll
    static void tearDownAll() {
        if (camelContextManager != null) {
            camelContextManager.stopCamel();
        }
        if (connectionFactory instanceof AutoCloseable closeable) {
            try {
                closeable.close();
            } catch (Exception ignored) {
            }
        }
        if (embeddedCluster != null) {
            try {
                embeddedCluster.close();
            } catch (Exception ignored) {
            }
        }
    }

    private static int findFreePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            socket.setReuseAddress(true);
            return socket.getLocalPort();
        }
    }

    @Test
    @DisplayName("Should send Task to named message queue, route via Camel, print content, and update Infinispan cache")
    void testEndToEndQueueToCacheProcessing() throws Exception {
        Task task = new Task();
        task.setId("Task/integration-task-500");
        task.setStatus(Task.TaskStatus.RECEIVED);
        task.setIntent(Task.TaskIntent.ORDER);
        task.setFor(new Reference("Patient/pat-integration-1"));
        task.setDescription("Full End-to-End Task Processor Integration Test");

        // Send to named message queue
        producerService.sendTask(task);

        // Wait for Camel route to consume from queue and update Infinispan cache
        Task processedTask = null;
        for (int i = 0; i < 40; i++) {
            Optional<Task> taskOpt = taskCacheService.getTask("integration-task-500");
            if (taskOpt.isPresent() && taskOpt.get().getStatus() == Task.TaskStatus.COMPLETED) {
                processedTask = taskOpt.get();
                break;
            }
            Thread.sleep(100);
        }

        assertThat(processedTask).isNotNull();
        assertThat(processedTask.getIdPart()).isEqualTo("integration-task-500");
        assertThat(processedTask.getStatus()).isEqualTo(Task.TaskStatus.COMPLETED);
        assertThat(processedTask.getBusinessStatus().getText()).isEqualTo("PROCESSED");
        assertThat(processedTask.getNote()).isNotEmpty();
        assertThat(processedTask.getNote().get(0).getText()).contains("Processed by HIE task-processor");
    }

    @Test
    @DisplayName("Should send TaskEvent to 2nd named queue, retrieve Task from cache, process event, and update cache")
    void testEndToEndTaskEventQueueToCacheProcessing() throws Exception {
        // Pre-cache task
        Task existing = new Task();
        existing.setId("Task/event-queue-task-700");
        existing.setStatus(Task.TaskStatus.INPROGRESS);
        existing.setIntent(Task.TaskIntent.ORDER);
        existing.setFor(new Reference("Patient/pat-event-queue"));
        existing.setDescription("Pre-cached task for 2nd queue event test");
        taskCacheService.saveTask(existing);

        // Send lightweight TaskEvent to 2nd queue
        ErgonEvent event = new ErgonEvent("event-queue-task-700", "TRANSITION_COMPLETE", "completed", "gateway-service", "Event queue processed");
        producerService.sendTaskEvent(event);

        // Wait for Camel route on 2nd queue to consume and update cache
        Task processedTask = null;
        for (int i = 0; i < 40; i++) {
            Optional<Task> taskOpt = taskCacheService.getTask("event-queue-task-700");
            if (taskOpt.isPresent() && "TRANSITION_COMPLETE".equals(taskOpt.get().getBusinessStatus().getText())) {
                processedTask = taskOpt.get();
                break;
            }
            Thread.sleep(100);
        }

        assertThat(processedTask).isNotNull();
        assertThat(processedTask.getIdPart()).isEqualTo("event-queue-task-700");
        assertThat(processedTask.getStatus()).isEqualTo(Task.TaskStatus.COMPLETED);
        assertThat(processedTask.getBusinessStatus().getText()).isEqualTo("TRANSITION_COMPLETE");
        assertThat(processedTask.getFor().getReference()).isEqualTo("Patient/pat-event-queue");
        assertThat(processedTask.getNote()).isNotEmpty();
        assertThat(processedTask.getNote().get(0).getText()).contains("Processed TaskEvent [action=TRANSITION_COMPLETE, status=completed]");
    }

    @Test
    @DisplayName("Should route TaskEvent from JMS queue through sequence dispatcher into TaskSequence pipeline and execute activities")
    void testEndToEndTaskEventQueueToTaskSequenceActivityExecution() throws Exception {
        // Pre-cache task with HL7 message description
        Task existing = new Task();
        existing.setId("Task/seq-dispatch-task-888");
        existing.setStatus(Task.TaskStatus.REQUESTED);
        existing.setIntent(Task.TaskIntent.ORDER);
        String hl7 = "MSH|^~\\&|PAS|HOSP|HIE|REC|20260908120000||ADT^A01|MSG-SEQ-888|P|2.4\r" +
                "PID|||MRN-QUEUE-999^^^HOSP||ANDERSON^JAMES^T^MR||19790322|M||2106-3^White|789 BROADWAY^^SEATTLE^WA^98101^USA||(206)555-1234||ENG^English|M^Married";
        existing.setDescription(hl7);
        taskCacheService.saveTask(existing);

        // Send lightweight TaskEvent with gateway and trigger headers to event queue
        ErgonEvent event = new ErgonEvent("seq-dispatch-task-888", "PROCESS", "completed",
                "mllp-gateway-default", "ADT", "A01", "MSG-SEQ-888", "mllp-gateway", hl7);
        producerService.sendTaskEvent(event);

        // Wait for Camel routes (queue consumer -> processor -> dispatcher -> sequence pipeline -> activities)
        Task processedTask = null;
        for (int i = 0; i < 40; i++) {
            Optional<Task> taskOpt = taskCacheService.getTask("seq-dispatch-task-888");
            if (taskOpt.isPresent() && taskOpt.get().getBusinessStatus() != null && "PROCESS".equals(taskOpt.get().getBusinessStatus().getText())) {
                processedTask = taskOpt.get();
                break;
            }
            Thread.sleep(100);
        }

        assertThat(processedTask).isNotNull();
        assertThat(processedTask.getIdPart()).isEqualTo("seq-dispatch-task-888");
        assertThat(processedTask.getStatus()).isEqualTo(Task.TaskStatus.COMPLETED);
        assertThat(processedTask.getBusinessStatus().getText()).isEqualTo("PROCESS");
    }

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
