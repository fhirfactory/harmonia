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
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.hie.model.TaskEvent;
import net.fhirfactory.hie.model.task.HieTaskReason;
import net.fhirfactory.hie.model.topic.Topic;
import net.fhirfactory.hie.taskprocessor.cache.TaskCacheService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.Annotation;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Optional;

@ApplicationScoped
public class TaskEventMessageProcessor implements Processor {

    private static final Logger log = LoggerFactory.getLogger(TaskEventMessageProcessor.class);

    @Inject
    private TaskCacheService taskCacheService;

    @Inject
    private FhirContext fhirContext;

    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        if (this.objectMapper == null) {
            this.objectMapper = new ObjectMapper();
        }
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Object body = exchange.getMessage().getBody();
        if (body == null) {
            log.warn("Received empty/null TaskEvent message body on Camel route");
            return;
        }

        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

        String rawContent;
        TaskEvent taskEvent = null;

        if (body instanceof TaskEvent) {
            taskEvent = (TaskEvent) body;
            rawContent = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(taskEvent);
        } else if (body instanceof byte[]) {
            rawContent = new String((byte[]) body);
            taskEvent = objectMapper.readValue(rawContent, TaskEvent.class);
        } else if (body instanceof String) {
            rawContent = (String) body;
            taskEvent = objectMapper.readValue(rawContent, TaskEvent.class);
        } else {
            rawContent = body.toString();
            try {
                taskEvent = objectMapper.readValue(rawContent, TaskEvent.class);
            } catch (Exception e) {
                log.warn("Could not parse body as TaskEvent JSON, attempting fallback: {}", e.getMessage());
                taskEvent = new TaskEvent(rawContent.trim(), "PROCESS", "COMPLETED");
            }
        }

        // Print out the message content as required
        System.out.println("=================================================");
        System.out.println("[TASK-EVENT-PROCESSOR] Incoming TaskEvent Content Received:");
        System.out.println("TaskId: " + (taskEvent != null ? taskEvent.getTaskId() : "null")
                + " | Gateway: " + (taskEvent != null ? taskEvent.getGatewayInstanceId() : "null")
                + " | Trigger: " + (taskEvent != null ? (taskEvent.getMessageType() + "^" + taskEvent.getTriggerType()) : "null")
                + " | Action: " + (taskEvent != null ? taskEvent.getAction() : "null")
                + " | Status: " + (taskEvent != null ? taskEvent.getStatus() : "null"));
        System.out.println(rawContent);
        System.out.println("=================================================");
        log.info("[TASK-EVENT-PROCESSOR] Processing incoming TaskEvent:\n{}", rawContent);

        // Populate any missing fields from exchange headers if available
        if (taskEvent != null) {
            Topic headerTopic = exchange.getMessage().getHeader("HIE_TOPIC", Topic.class);
            if (headerTopic != null) {
                taskEvent.setTopic(headerTopic);
            }
            String headerGw = exchange.getMessage().getHeader("HIE_GATEWAY_INSTANCE", String.class);
            if (StringUtils.isBlank(taskEvent.getGatewayInstanceId()) && StringUtils.isNotBlank(headerGw)) {
                taskEvent.setGatewayInstanceId(headerGw);
            }
            String headerTrigger = exchange.getMessage().getHeader("HIE_TRIGGER_TYPE", String.class);
            if (StringUtils.isBlank(taskEvent.getTriggerType()) && StringUtils.isNotBlank(headerTrigger)) {
                taskEvent.setTriggerType(headerTrigger);
            }
            String headerMsgType = exchange.getMessage().getHeader("HIE_MESSAGE_TYPE", String.class);
            if (StringUtils.isBlank(taskEvent.getMessageType()) && StringUtils.isNotBlank(headerMsgType)) {
                taskEvent.setMessageType(headerMsgType);
            }
            String headerCtrlId = exchange.getMessage().getHeader("HIE_CONTROL_ID", String.class);
            if (StringUtils.isBlank(taskEvent.getControlId()) && StringUtils.isNotBlank(headerCtrlId)) {
                taskEvent.setControlId(headerCtrlId);
            }
        }

        if (taskEvent == null || StringUtils.isBlank(taskEvent.getTaskId())) {
            log.error("TaskEvent does not contain a valid taskId: {}", rawContent);
            return;
        }

        String taskId = taskEvent.getTaskId().trim();
        if (taskId.startsWith("Task/")) {
            taskId = taskId.substring("Task/".length());
        }

        // Retrieve the Task from Infinispan cache
        Optional<Task> cachedTaskOpt = taskCacheService.getTask(taskId);
        Task task;
        if (cachedTaskOpt.isPresent()) {
            task = cachedTaskOpt.get();
            log.info("Retrieved Task/{} from cache for TaskEvent processing", taskId);
        } else {
            log.warn("Task/{} not found in cache. Creating baseline Task instance.", taskId);
            task = new Task();
            task.setId("Task/" + taskId);
            task.setAuthoredOn(new Date());
            task.setDescription("Task created from incoming TaskEvent notification");
        }
        HieTaskReason.ensureSyntheticTaskReason(task);

        // Update task status from event if specified
        if (StringUtils.isNotBlank(taskEvent.getStatus())) {
            try {
                Task.TaskStatus fhirStatus = Task.TaskStatus.fromCode(taskEvent.getStatus().toLowerCase());
                task.setStatus(fhirStatus);
            } catch (Exception ignored) {
                // If not standard FHIR status code, default to COMPLETED
                task.setStatus(Task.TaskStatus.COMPLETED);
            }
        } else {
            task.setStatus(Task.TaskStatus.COMPLETED);
        }

        task.setLastModified(new Date());

        // Update business status
        CodeableConcept businessStatus = new CodeableConcept();
        String actionText = StringUtils.isNotBlank(taskEvent.getAction()) ? taskEvent.getAction() : "PROCESSED";
        businessStatus.setText(actionText);
        businessStatus.addCoding()
                .setSystem("http://terminology.hl7.org/CodeSystem/task-business-status")
                .setCode(actionText.toLowerCase().replace(" ", "-"))
                .setDisplay("Task Event: " + actionText);
        task.setBusinessStatus(businessStatus);

        // Add execution note
        Annotation note = new Annotation();
        StringBuilder noteBuilder = new StringBuilder();
        noteBuilder.append("Processed TaskEvent [action=").append(actionText)
                .append(", status=").append(taskEvent.getStatus() != null ? taskEvent.getStatus() : "COMPLETED")
                .append("]");
        if (StringUtils.isNotBlank(taskEvent.getGatewayInstanceId())) {
            noteBuilder.append(" [gateway=").append(taskEvent.getGatewayInstanceId()).append("]");
        }
        if (StringUtils.isNotBlank(taskEvent.getTriggerType())) {
            noteBuilder.append(" [trigger=").append(taskEvent.getMessageType() != null ? taskEvent.getMessageType() : "ADT")
                    .append("^").append(taskEvent.getTriggerType()).append("]");
        }
        if (StringUtils.isNotBlank(taskEvent.getSource())) {
            noteBuilder.append(" from source [").append(taskEvent.getSource()).append("]");
        }
        if (StringUtils.isNotBlank(taskEvent.getDescription())) {
            noteBuilder.append(" - ").append(taskEvent.getDescription());
        }
        noteBuilder.append(" at ").append(new Date());
        note.setText(noteBuilder.toString());
        note.setTime(new Date());
        task.addNote(note);

        // Save updated task to Infinispan cache
        taskCacheService.saveTask(task);
        log.info("Task/{} updated and persisted to cache following TaskEvent [action={}]", taskId, actionText);

        // Output processed task JSON on exchange
        IParser parser = (fhirContext != null ? fhirContext : FhirContext.forR5()).newJsonParser().setPrettyPrint(true);
        String taskJson = parser.encodeResourceToString(task);
        exchange.getMessage().setBody(taskJson);
        exchange.getMessage().setHeader("HIE_TASK_ID", taskId);
        exchange.getMessage().setHeader("HIE_TASK_EVENT_ACTION", actionText);
        exchange.getMessage().setHeader("HIE_TASK_PROCESSED", true);
        if (StringUtils.isNotBlank(taskEvent.getGatewayInstanceId())) {
            exchange.getMessage().setHeader("HIE_GATEWAY_INSTANCE", taskEvent.getGatewayInstanceId());
        }
        if (StringUtils.isNotBlank(taskEvent.getTriggerType())) {
            exchange.getMessage().setHeader("HIE_TRIGGER_TYPE", taskEvent.getTriggerType());
        }
        if (StringUtils.isNotBlank(taskEvent.getMessageType())) {
            exchange.getMessage().setHeader("HIE_MESSAGE_TYPE", taskEvent.getMessageType());
        }
        if (StringUtils.isNotBlank(taskEvent.getControlId())) {
            exchange.getMessage().setHeader("HIE_CONTROL_ID", taskEvent.getControlId());
        }
    }

    public void setTaskCacheService(TaskCacheService taskCacheService) {
        this.taskCacheService = taskCacheService;
    }

    public void setFhirContext(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
