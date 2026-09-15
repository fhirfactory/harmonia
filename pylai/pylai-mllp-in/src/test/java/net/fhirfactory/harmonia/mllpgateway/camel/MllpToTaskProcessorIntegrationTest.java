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

package net.fhirfactory.harmonia.mllpgateway.camel;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.mllpgateway.config.TaskProcessorConfig;
import net.fhirfactory.harmonia.mllpgateway.hl7.AdtProcessingResult;
import net.fhirfactory.harmonia.mllpgateway.hl7.IncomingAdtMessageProcessor;
import net.fhirfactory.harmonia.mllpgateway.messaging.TaskEventProducerService;
import net.fhirfactory.harmonia.model.ergon.ErgonEvent;
import net.fhirfactory.harmonia.mllpgateway.service.CommunicationService;
import net.fhirfactory.harmonia.mllpgateway.service.DefaultCommunicationService;
import net.fhirfactory.harmonia.mllpgateway.service.DefaultTaskService;
import net.fhirfactory.harmonia.mllpgateway.service.TaskService;
import net.fhirfactory.harmonia.model.security.FhirConfidentialityEnum;
import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
import org.apache.activemq.artemis.api.core.QueueConfiguration;
import org.apache.activemq.artemis.api.core.RoutingType;
import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.hl7.fhir.r5.model.Annotation;
import org.hl7.fhir.r5.model.Communication;
import org.hl7.fhir.r5.model.Task;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class MllpToTaskProcessorIntegrationTest {

    private static final String EVENT_QUEUE_NAME = "task.event.queue.mllp-gateway-default";
    private static EmbeddedActiveMQ embeddedBroker;
    private static ActiveMQConnectionFactory connectionFactory;

    private TaskService taskService;
    private CommunicationService communicationService;
    private TaskProcessorConfig config;
    private TaskEventProducerService producerService;
    private IncomingAdtMessageProcessor transformer;
    private CamelContext consumerCamelContext;
    private CountDownLatch taskProcessedLatch;
    private ObjectMapper objectMapper;

    @BeforeAll
    static void startBroker() throws Exception {
        Configuration configuration = new ConfigurationImpl();
        configuration.setPersistenceEnabled(false);
        configuration.setJournalDirectory(new File(System.getProperty("java.io.tmpdir"), "artemis-mllp-integ-journal").getAbsolutePath());
        configuration.setSecurityEnabled(false);
        configuration.addAcceptorConfiguration("in-vm", "vm://0");

        QueueConfiguration queueConfig = new QueueConfiguration(EVENT_QUEUE_NAME)
                .setRoutingType(RoutingType.ANYCAST)
                .setAddress(EVENT_QUEUE_NAME)
                .setDurable(false);
        configuration.addQueueConfiguration(queueConfig);

        embeddedBroker = new EmbeddedActiveMQ();
        embeddedBroker.setConfiguration(configuration);
        embeddedBroker.start();

        connectionFactory = new ActiveMQConnectionFactory("vm://0");
    }

    @AfterAll
    static void stopBroker() throws Exception {
        if (connectionFactory != null) {
            connectionFactory.close();
        }
        if (embeddedBroker != null) {
            embeddedBroker.stop();
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        taskService = new DefaultTaskService();
        taskService.clear();
        communicationService = new DefaultCommunicationService();
        communicationService.clear();
        objectMapper = new ObjectMapper();
        taskProcessedLatch = new CountDownLatch(1);

        config = new TaskProcessorConfig();
        producerService = new TaskEventProducerService(config, connectionFactory);
        transformer = new IncomingAdtMessageProcessor(taskService, communicationService, null, producerService);

        // Start a mock Task Processor Camel route that consumes TaskEvents from the queue
        // and simulates Task Processor enriching the Task in cache
        consumerCamelContext = new DefaultCamelContext();
        consumerCamelContext.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        consumerCamelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("jms:queue:" + EVENT_QUEUE_NAME)
                        .routeId("mock-task-processor-consumer")
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {
                                String body = exchange.getMessage().getBody(String.class);
                                ErgonEvent event = objectMapper.readValue(body, ErgonEvent.class);

                                // Retrieve Task from shared cache/service
                                Optional<Task> optTask = taskService.getById(event.getTaskId());
                                if (optTask.isPresent()) {
                                    Task task = optTask.get();
                                    task.setStatus(Task.TaskStatus.INPROGRESS);
                                    Annotation processingNote = task.addNote();
                                    processingNote.setText("TaskProcessor picked up Task via ActiveMQ event queue: " + EVENT_QUEUE_NAME);
                                    processingNote.setTime(new Date());
                                    taskService.update(event.getTaskId(), task);
                                }
                                taskProcessedLatch.countDown();
                            }
                        });
            }
        });

        consumerCamelContext.start();
    }

    @AfterEach
    void tearDown() throws Exception {
        if (consumerCamelContext != null) {
            consumerCamelContext.stop();
            consumerCamelContext.close();
        }
    }

    @Test
    @DisplayName("MLLP Gateway creates Task in cache and dispatches event to Artemis, and TaskProcessor consumes and updates Task")
    void testEndToEndMllpToTaskProcessorFlow() throws Exception {
        String hl7A01 = "MSH|^~\\&|EPIC_PAS|REGIONAL_HOSPITAL|HIE|HIE_IM|20260907160000||ADT^A01|MSG-E2E-001|P|2.4\r" +
                "EVN|A01|20260907160000\r" +
                "PID|1||PAT88899^^^REGIONAL_HOSPITAL^MR||DAVIS^EMMA||20010915|F|||789 PINE RD^^CHICAGO^IL^60601\r" +
                "PV1|1|I|CARDIO^RM301^BED1^REGIONAL_HOSPITAL||||DOC15^TAYLOR^SARAH^^DR|||||||||||V998811\r";

        // 1. MLLP Gateway processes HL7 message
        AdtProcessingResult result = transformer.processAdtMessage(hl7A01);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getMessageControlId()).isEqualTo("MSG-E2E-001");
        assertThat(result.getPatientName()).isEqualTo("EMMA DAVIS");

        // Verify initial state in cache
        Optional<Task> initialTask = taskService.getById("MSG-E2E-001");
        assertThat(initialTask).isPresent();
        assertThat(initialTask.get().getStatus()).isIn(Task.TaskStatus.REQUESTED, Task.TaskStatus.INPROGRESS);
        assertThat(FhirSecurityTagManager.hasConfidentiality(initialTask.get(), FhirConfidentialityEnum.N)).isTrue();

        // Verify Communication was persisted in cache
        Optional<Communication> initialComm = communicationService.getById("comm-MSG-E2E-001");
        assertThat(initialComm).isPresent();
        assertThat(initialComm.get().getSubject().getReference()).isEqualTo("Patient/PAT88899");
        assertThat(FhirSecurityTagManager.hasConfidentiality(initialComm.get(), FhirConfidentialityEnum.N)).isTrue();

        // 2. Wait for mock Task Processor to consume TaskEvent from ActiveMQ and update Task
        boolean processed = taskProcessedLatch.await(5, TimeUnit.SECONDS);
        assertThat(processed).isTrue();

        // 3. Verify Task was updated in cache by TaskProcessor: INPROGRESS + Note added
        Optional<Task> processedTask = taskService.getById("MSG-E2E-001");
        assertThat(processedTask).isPresent();
        assertThat(processedTask.get().getStatus()).isEqualTo(Task.TaskStatus.INPROGRESS);
        assertThat(processedTask.get().getNote()).hasSizeGreaterThanOrEqualTo(2);
        assertThat(processedTask.get().getNote().stream()
                .anyMatch(n -> n.getText().contains("TaskProcessor picked up Task via ActiveMQ event queue")))
                .isTrue();
    }
}
