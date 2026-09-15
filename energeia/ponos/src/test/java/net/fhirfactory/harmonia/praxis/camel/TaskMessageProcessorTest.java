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

package net.fhirfactory.harmonia.praxis.camel;

import ca.uhn.fhir.context.FhirContext;
import net.fhirfactory.harmonia.praxis.cache.TaskCacheService;
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

class TaskMessageProcessorTest {

    private TaskCacheService taskCacheService;
    private TaskMessageProcessor processor;
    private FhirContext fhirContext;
    private DefaultCamelContext camelContext;

    @BeforeEach
    void setUp() {
        fhirContext = FhirContext.forR5();
        taskCacheService = new TaskCacheService();
        taskCacheService.init();
        taskCacheService.clear();

        processor = new TaskMessageProcessor();
        processor.setFhirContext(fhirContext);
        processor.setTaskCacheService(taskCacheService);

        camelContext = new DefaultCamelContext();
    }

    @Test
    @DisplayName("Should process Task object payload and update cache")
    void testProcessTaskObject() throws Exception {
        Task task = new Task();
        task.setId("Task/proc-test-1");
        task.setStatus(Task.TaskStatus.RECEIVED);
        task.setIntent(Task.TaskIntent.ORDER);
        task.setDescription("Test task processing");

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getMessage().setBody(task);

        processor.process(exchange);

        assertThat(exchange.getMessage().getHeader("HIE_TASK_ID")).isEqualTo("proc-test-1");
        assertThat(exchange.getMessage().getHeader("HIE_TASK_PROCESSED")).isEqualTo(true);

        // Verify task updated in cache
        Optional<Task> cached = taskCacheService.getTask("proc-test-1");
        assertThat(cached).isPresent();
        assertThat(cached.get().getStatus()).isEqualTo(Task.TaskStatus.COMPLETED);
        assertThat(cached.get().getBusinessStatus().getText()).isEqualTo("PROCESSED");
        assertThat(cached.get().getNote()).isNotEmpty();
    }

    @Test
    @DisplayName("Should process JSON string payload and update cache")
    void testProcessJsonStringPayload() throws Exception {
        Task task = new Task();
        task.setId("Task/proc-test-2");
        task.setStatus(Task.TaskStatus.REQUESTED);
        task.setFor(new Reference("Patient/pat-99"));
        task.setDescription("JSON payload test");

        String json = fhirContext.newJsonParser().encodeResourceToString(task);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getMessage().setBody(json);

        processor.process(exchange);

        Optional<Task> cached = taskCacheService.getTask("proc-test-2");
        assertThat(cached).isPresent();
        assertThat(cached.get().getStatus()).isEqualTo(Task.TaskStatus.COMPLETED);
        assertThat(cached.get().getFor().getReference()).isEqualTo("Patient/pat-99");
    }
}
