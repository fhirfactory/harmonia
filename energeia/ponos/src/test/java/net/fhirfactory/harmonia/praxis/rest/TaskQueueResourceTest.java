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

package net.fhirfactory.harmonia.praxis.rest;

import ca.uhn.fhir.context.FhirContext;
import jakarta.ws.rs.core.Response;
import net.fhirfactory.harmonia.praxis.cache.TaskCacheService;
import net.fhirfactory.harmonia.praxis.config.QueueConfig;
import net.fhirfactory.harmonia.praxis.messaging.ArtemisBrokerManager;
import net.fhirfactory.harmonia.praxis.messaging.TaskQueueProducerService;
import org.hl7.fhir.r5.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class TaskQueueResourceTest {

    private TaskQueueResource resource;
    private TaskQueueProducerService producerService;
    private TaskCacheService taskCacheService;
    private QueueConfig queueConfig;
    private ArtemisBrokerManager brokerManager;
    private FhirContext fhirContext;

    @BeforeEach
    void setUp() throws Exception {
        fhirContext = FhirContext.forR5();
        taskCacheService = new TaskCacheService();
        taskCacheService.init();
        taskCacheService.clear();

        producerService = Mockito.mock(TaskQueueProducerService.class);
        brokerManager = Mockito.mock(ArtemisBrokerManager.class);
        queueConfig = new QueueConfig();

        when(brokerManager.isRunning()).thenReturn(true);
        doNothing().when(producerService).sendTask(any(Task.class));

        resource = new TaskQueueResource();
        setField(resource, "producerService", producerService);
        setField(resource, "taskCacheService", taskCacheService);
        setField(resource, "queueConfig", queueConfig);
        setField(resource, "artemisBrokerManager", brokerManager);
    }

    @Test
    @DisplayName("Should enqueue task via REST endpoint")
    void testEnqueueTask() {
        Task task = new Task();
        task.setId("Task/rest-task-1");
        task.setStatus(Task.TaskStatus.REQUESTED);
        String json = fhirContext.newJsonParser().encodeResourceToString(task);

        Response response = resource.enqueueTask(json);
        assertThat(response.getStatus()).isEqualTo(Response.Status.ACCEPTED.getStatusCode());
        Map<?, ?> entity = (Map<?, ?>) response.getEntity();
        assertThat(entity.get("status")).isEqualTo("ENQUEUED");
        assertThat(entity.get("taskId")).isEqualTo("rest-task-1");
    }

    @Test
    @DisplayName("Should enqueue task event via REST endpoint")
    void testEnqueueTaskEvent() throws Exception {
        String eventJson = "{\"taskId\":\"event-rest-100\",\"action\":\"CANCEL\",\"status\":\"cancelled\",\"source\":\"ui\"}";

        Response response = resource.enqueueTaskEvent(eventJson);
        assertThat(response.getStatus()).isEqualTo(Response.Status.ACCEPTED.getStatusCode());
        Map<?, ?> entity = (Map<?, ?>) response.getEntity();
        assertThat(entity.get("status")).isEqualTo("ENQUEUED");
        assertThat(entity.get("taskId")).isEqualTo("event-rest-100");
        assertThat(entity.get("action")).isEqualTo("CANCEL");
        assertThat(entity.get("queueName")).isEqualTo(QueueConfig.DEFAULT_EVENT_QUEUE_NAME);
    }

    @Test
    @DisplayName("Should get status of queues and module")
    void testGetStatus() {
        Response response = resource.getStatus();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        Map<?, ?> entity = (Map<?, ?>) response.getEntity();
        assertThat(entity.get("module")).isEqualTo("task-processor");
        assertThat(entity.get("taskQueueName")).isEqualTo(QueueConfig.DEFAULT_QUEUE_NAME);
        assertThat(entity.get("eventQueueName")).isEqualTo(QueueConfig.DEFAULT_EVENT_QUEUE_NAME);
        assertThat(entity.get("brokerRunning")).isEqualTo(true);
    }

    @Test
    @DisplayName("Should get cached task by ID")
    void testGetCachedTask() {
        Task task = new Task();
        task.setId("Task/rest-task-cached");
        taskCacheService.saveTask(task);

        Response response = resource.getCachedTask("rest-task-cached");
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity().toString()).contains("rest-task-cached");

        Response notFound = resource.getCachedTask("non-existent");
        assertThat(notFound.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
