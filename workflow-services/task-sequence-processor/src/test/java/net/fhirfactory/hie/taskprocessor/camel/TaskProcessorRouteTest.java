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

package net.fhirfactory.hie.taskprocessor.camel;

import ca.uhn.fhir.context.FhirContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.hie.model.TaskEvent;
import net.fhirfactory.hie.model.topic.Topic;
import net.fhirfactory.hie.taskprocessor.cache.TaskCacheService;
import net.fhirfactory.hie.taskprocessor.config.QueueConfig;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.hl7.fhir.r5.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TaskProcessorRouteTest {

    private CamelContext camelContext;
    private ProducerTemplate producerTemplate;
    private TaskCacheService taskCacheService;
    private FhirContext fhirContext;

    @BeforeEach
    void setUp() throws Exception {
        fhirContext = FhirContext.forR5();
        taskCacheService = new TaskCacheService();
        taskCacheService.init();
        taskCacheService.clear();

        TaskMessageProcessor processor = new TaskMessageProcessor();
        processor.setFhirContext(fhirContext);
        processor.setTaskCacheService(taskCacheService);

        TaskEventMessageProcessor eventProcessor = new TaskEventMessageProcessor();
        eventProcessor.setFhirContext(fhirContext);
        eventProcessor.setTaskCacheService(taskCacheService);
        eventProcessor.setObjectMapper(new ObjectMapper());

        QueueConfig queueConfig = new QueueConfig();

        TaskProcessorRouteBuilder routeBuilder = new TaskProcessorRouteBuilder();
        routeBuilder.setQueueConfig(queueConfig);
        routeBuilder.setTaskMessageProcessor(processor);
        routeBuilder.setTaskEventMessageProcessor(eventProcessor);

        camelContext = new DefaultCamelContext();
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:task-processor-input")
                        .routeId("test-direct-route")
                        .process(processor);

                from("direct:task-event-input")
                        .routeId("test-direct-event-route")
                        .process(eventProcessor);
            }
        });
        camelContext.start();
        producerTemplate = camelContext.createProducerTemplate();
    }

    @AfterEach
    void tearDown() throws Exception {
        if (producerTemplate != null) {
            producerTemplate.stop();
        }
        if (camelContext != null) {
            camelContext.stop();
            camelContext.close();
        }
    }

    @Test
    @DisplayName("Should route Task through Camel and update cache")
    void testDirectRouteProcessing() {
        Task task = new Task();
        task.setId("Task/direct-route-101");
        task.setStatus(Task.TaskStatus.INPROGRESS);
        task.setIntent(Task.TaskIntent.PROPOSAL);
        task.setDescription("Camel Direct Route Task Processing");

        String json = fhirContext.newJsonParser().encodeResourceToString(task);
        Object response = producerTemplate.requestBody("direct:task-processor-input", json);

        assertThat(response).isNotNull();

        Optional<Task> cached = taskCacheService.getTask("direct-route-101");
        assertThat(cached).isPresent();
        assertThat(cached.get().getStatus()).isEqualTo(Task.TaskStatus.COMPLETED);
        assertThat(cached.get().getBusinessStatus().getText()).isEqualTo("PROCESSED");
    }

    @Test
    @DisplayName("Should route TaskEvent through Camel direct route, retrieve from cache and update")
    void testDirectEventRouteProcessing() throws Exception {
        // Pre-cache task
        Task task = new Task();
        task.setId("Task/direct-event-202");
        task.setStatus(Task.TaskStatus.READY);
        taskCacheService.saveTask(task);

        TaskEvent event = new TaskEvent("direct-event-202", "EXECUTE_JOB", "completed", "test-runner", "Job executed");
        String eventJson = new ObjectMapper().writeValueAsString(event);

        Object response = producerTemplate.requestBody("direct:task-event-input", eventJson);
        assertThat(response).isNotNull();

        Optional<Task> cached = taskCacheService.getTask("direct-event-202");
        assertThat(cached).isPresent();
        assertThat(cached.get().getStatus()).isEqualTo(Task.TaskStatus.COMPLETED);
        assertThat(cached.get().getBusinessStatus().getText()).isEqualTo("EXECUTE_JOB");
    }
}
