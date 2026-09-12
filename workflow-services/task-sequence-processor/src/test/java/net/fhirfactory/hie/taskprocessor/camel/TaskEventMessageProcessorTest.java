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
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TaskEventMessageProcessorTest {

    private TaskCacheService taskCacheService;
    private TaskEventMessageProcessor processor;
    private FhirContext fhirContext;
    private DefaultCamelContext camelContext;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        fhirContext = FhirContext.forR5();
        taskCacheService = new TaskCacheService();
        taskCacheService.init();
        taskCacheService.clear();

        objectMapper = new ObjectMapper();

        processor = new TaskEventMessageProcessor();
        processor.setFhirContext(fhirContext);
        processor.setTaskCacheService(taskCacheService);
        processor.setObjectMapper(objectMapper);

        camelContext = new DefaultCamelContext();
    }

    @Test
    @DisplayName("Should retrieve cached Task, process TaskEvent, and update Task in cache")
    void testProcessTaskEventWithExistingCachedTask() throws Exception {
        // Pre-populate Task in cache
        Task existingTask = new Task();
        existingTask.setId("Task/event-test-1");
        existingTask.setStatus(Task.TaskStatus.INPROGRESS);
        existingTask.setIntent(Task.TaskIntent.ORDER);
        existingTask.setFor(new Reference("Patient/pat-event-1"));
        existingTask.setDescription("Initial cached task before event notification");
        taskCacheService.saveTask(existingTask);

        // Create lightweight TaskEvent
        TaskEvent event = new TaskEvent("event-test-1", "PROCESS_COMPLETED", "completed", "mllp-gateway", "Event triggered completion");

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getMessage().setBody(event);

        processor.process(exchange);

        assertThat(exchange.getMessage().getHeader("HIE_TASK_ID")).isEqualTo("event-test-1");
        assertThat(exchange.getMessage().getHeader("HIE_TASK_EVENT_ACTION")).isEqualTo("PROCESS_COMPLETED");
        assertThat(exchange.getMessage().getHeader("HIE_TASK_PROCESSED")).isEqualTo(true);

        // Verify task updated in Infinispan cache
        Optional<Task> updatedTaskOpt = taskCacheService.getTask("event-test-1");
        assertThat(updatedTaskOpt).isPresent();
        Task updatedTask = updatedTaskOpt.get();
        assertThat(updatedTask.getStatus()).isEqualTo(Task.TaskStatus.COMPLETED);
        assertThat(updatedTask.getBusinessStatus().getText()).isEqualTo("PROCESS_COMPLETED");
        assertThat(updatedTask.getFor().getReference()).isEqualTo("Patient/pat-event-1");
        assertThat(updatedTask.getNote()).isNotEmpty();
        assertThat(updatedTask.getNote().get(0).getText()).contains("Processed TaskEvent [action=PROCESS_COMPLETED, status=completed] from source [mllp-gateway]");
    }

    @Test
    @DisplayName("Should process TaskEvent JSON string payload and create/update cache")
    void testProcessTaskEventJsonString() throws Exception {
        TaskEvent event = new TaskEvent("event-test-2", "DISPATCH", "in-progress", "befe", "Task dispatched to worker");
        String jsonPayload = objectMapper.writeValueAsString(event);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getMessage().setBody(jsonPayload);

        processor.process(exchange);

        Optional<Task> cachedOpt = taskCacheService.getTask("event-test-2");
        assertThat(cachedOpt).isPresent();
        Task cachedTask = cachedOpt.get();
        assertThat(cachedTask.getStatus()).isEqualTo(Task.TaskStatus.INPROGRESS);
        assertThat(cachedTask.getBusinessStatus().getText()).isEqualTo("DISPATCH");
    }

    @Test
    @DisplayName("Should process enhanced TaskEvent with gateway, trigger, and control metadata")
    void testProcessEnhancedTaskEvent() throws Exception {
        TaskEvent event = new TaskEvent("event-test-3", "VALIDATE", "requested", "pas-gw",
                "ADT", "A01", "MSG-CTRL-001", "mllp-gateway", "Admit validation");
        String jsonPayload = objectMapper.writeValueAsString(event);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getMessage().setBody(jsonPayload);

        processor.process(exchange);

        assertThat(exchange.getMessage().getHeader("HIE_TASK_ID")).isEqualTo("event-test-3");
        assertThat(exchange.getMessage().getHeader("HIE_GATEWAY_INSTANCE")).isEqualTo("pas-gw");
        assertThat(exchange.getMessage().getHeader("HIE_TRIGGER_TYPE")).isEqualTo("A01");
        assertThat(exchange.getMessage().getHeader("HIE_MESSAGE_TYPE")).isEqualTo("ADT");
        assertThat(exchange.getMessage().getHeader("HIE_CONTROL_ID")).isEqualTo("MSG-CTRL-001");

        Optional<Task> cachedOpt = taskCacheService.getTask("event-test-3");
        assertThat(cachedOpt).isPresent();
        Task cachedTask = cachedOpt.get();
        assertThat(cachedTask.getStatus()).isEqualTo(Task.TaskStatus.REQUESTED);
        assertThat(cachedTask.getNote().get(0).getText()).contains("[gateway=pas-gw]");
        assertThat(cachedTask.getNote().get(0).getText()).contains("[trigger=ADT^A01]");
    }

    @Test
    @DisplayName("Should maintain backwards compatibility with legacy TaskEvent JSON without new fields")
    void testLegacyTaskEventJsonBackwardsCompatibility() throws Exception {
        String legacyJson = "{\"taskId\":\"legacy-task-1\",\"action\":\"PROCESS\",\"status\":\"completed\",\"source\":\"old-system\",\"description\":\"Legacy test\"}";

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getMessage().setBody(legacyJson);
        exchange.getMessage().setHeader("HIE_GATEWAY_INSTANCE", "inferred-gw");
        exchange.getMessage().setHeader("HIE_TRIGGER_TYPE", "A08");

        processor.process(exchange);

        assertThat(exchange.getMessage().getHeader("HIE_TASK_ID")).isEqualTo("legacy-task-1");
        assertThat(exchange.getMessage().getHeader("HIE_GATEWAY_INSTANCE")).isEqualTo("inferred-gw");
        assertThat(exchange.getMessage().getHeader("HIE_TRIGGER_TYPE")).isEqualTo("A08");

        Optional<Task> cachedOpt = taskCacheService.getTask("legacy-task-1");
        assertThat(cachedOpt).isPresent();
    }
}
