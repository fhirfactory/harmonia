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

package net.fhirfactory.hie.taskprocessor.rest;

import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.fhirfactory.hie.model.TaskEvent;
import net.fhirfactory.hie.model.task.HieTaskReason;
import net.fhirfactory.hie.model.topic.Topic;
import net.fhirfactory.hie.taskprocessor.cache.TaskCacheService;
import net.fhirfactory.hie.taskprocessor.config.QueueConfig;
import net.fhirfactory.hie.taskprocessor.messaging.ArtemisBrokerManager;
import net.fhirfactory.hie.taskprocessor.messaging.TaskQueueProducerService;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Path("/queue")
@RequestScoped
@Produces({"application/fhir+json", MediaType.APPLICATION_JSON})
@Consumes({"application/fhir+json", MediaType.APPLICATION_JSON})
public class TaskQueueResource {

    private static final Logger log = LoggerFactory.getLogger(TaskQueueResource.class);

    @Inject
    private TaskQueueProducerService producerService;

    @Inject
    private TaskCacheService taskCacheService;

    @Inject
    private QueueConfig queueConfig;

    @Inject
    private ArtemisBrokerManager artemisBrokerManager;

    @Inject
    private net.fhirfactory.hie.taskprocessor.camel.CamelContextManager camelContextManager;

    @Inject
    private net.fhirfactory.hie.taskprocessor.sequence.TaskSequenceLoader taskSequenceLoader;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @POST
    @Path("/task")
    public Response enqueueTask(String payload) {
        if (StringUtils.isBlank(payload)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Task payload cannot be empty\"}")
                    .build();
        }
        try {
            IParser parser = taskCacheService.getJsonParser();
            Task task = parser.parseResource(Task.class, payload);
            HieTaskReason.ensureSyntheticTaskReason(task);

            // Send to the named task queue
            producerService.sendTask(task);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "ENQUEUED");
            result.put("queueName", queueConfig.getQueueName());
            result.put("taskId", task.getIdElement() != null ? task.getIdElement().getIdPart() : task.getId());
            return Response.accepted(result).build();
        } catch (Exception e) {
            log.error("Failed to enqueue task", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @POST
    @Path("/event")
    public Response enqueueTaskEvent(String payload) {
        if (StringUtils.isBlank(payload)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"TaskEvent payload cannot be empty\"}")
                    .build();
        }
        try {
            TaskEvent taskEvent = objectMapper.readValue(payload, TaskEvent.class);
            if (StringUtils.isBlank(taskEvent.getTaskId())) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"TaskEvent must specify a valid taskId\"}")
                        .build();
            }

            // Send to the 2nd named event queue
            producerService.sendTaskEvent(taskEvent);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "ENQUEUED");
            result.put("queueName", queueConfig.getEventQueueName());
            result.put("taskId", taskEvent.getTaskId());
            result.put("action", taskEvent.getAction());
            return Response.accepted(result).build();
        } catch (Exception e) {
            log.error("Failed to enqueue task event", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @POST
    @Path("/sync")
    public Response sync() {
        return reload();
    }

    @POST
    @Path("/reload")
    public Response reload() {
        try {
            java.util.List<String> queues = artemisBrokerManager != null ? artemisBrokerManager.syncQueues() : java.util.Collections.emptyList();
            boolean seqsReloaded = camelContextManager != null && camelContextManager.reloadSequences();
            Map<String, Object> result = new HashMap<>();
            result.put("status", "SYNCHRONIZED");
            result.put("queues", queues);
            result.put("sequencesReloaded", seqsReloaded);
            return Response.ok(result).build();
        } catch (Exception e) {
            log.error("Failed to sync queues/sequences", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/status")
    public Response getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("module", "task-processor");
        status.put("taskQueueName", queueConfig.getQueueName());
        status.put("eventQueueName", queueConfig.getEventQueueName());
        status.put("brokerRunning", artemisBrokerManager.isRunning());
        status.put("cachedTasksCount", taskCacheService.count());
        return Response.ok(status).build();
    }

    @GET
    @Path("/task/{id}")
    public Response getCachedTask(@PathParam("id") String id) {
        String json = taskCacheService.getTaskJson(id);
        if (json == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Task not found in cache: " + id + "\"}")
                    .build();
        }
        return Response.ok(json).build();
    }
}
