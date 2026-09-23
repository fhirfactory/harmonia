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
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.logging.PhiLogger;
import net.fhirfactory.harmonia.logging.PhiLoggerFactory;
import net.fhirfactory.harmonia.model.ergon.ErgonEvent;
import net.fhirfactory.harmonia.model.ergon.ErgonReasonEnum;
import net.fhirfactory.harmonia.model.topic.Topic;
import net.fhirfactory.harmonia.praxis.cache.TaskCacheService;
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
    private static final PhiLogger phiLog = PhiLoggerFactory.getLogger(TaskEventMessageProcessor.class);

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
            log.warn("Received empty/null TaskEvent message body on Camel route [category=EMPTY_PAYLOAD, stage=RECEIVED]");
            return;
        }

        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

        String rawContent;
        ErgonEvent ergonEvent = null;

        if (body instanceof ErgonEvent) {
            ergonEvent = (ErgonEvent) body;
            rawContent = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(ergonEvent);
        } else {
            if (body instanceof byte[]) {
                rawContent = new String((byte[]) body);
            } else if (body instanceof String) {
                rawContent = (String) body;
            } else {
                rawContent = body.toString();
            }

            try {
                ergonEvent = objectMapper.readValue(rawContent, ErgonEvent.class);
            } catch (Exception e) {
                log.warn("Could not parse body as TaskEvent JSON [exception={}, category=TASK_EVENT_PARSE_FAILURE], attempting fallback", e.getClass().getName());
                String fallbackId = rawContent.trim();
                if (fallbackId.contains("{") || fallbackId.contains("\n") || fallbackId.length() > 128) {
                    fallbackId = "unknown";
                }
                ergonEvent = new ErgonEvent(fallbackId, "PROCESS", "COMPLETED");
            }
        }

        // Diagnostic payload inspection routed exclusively to PhiLogger at DEBUG
        phiLog.debug("[TASK-EVENT-PROCESSOR] Incoming TaskEvent payload: {}", rawContent);

        // Populate any missing fields from exchange headers if available
        if (ergonEvent != null) {
            Topic headerTopic = exchange.getMessage().getHeader("HIE_TOPIC", Topic.class);
            if (headerTopic != null) {
                ergonEvent.setTopic(headerTopic);
            }
            String headerGw = exchange.getMessage().getHeader("HIE_GATEWAY_INSTANCE", String.class);
            if (StringUtils.isBlank(ergonEvent.getGatewayInstanceId()) && StringUtils.isNotBlank(headerGw)) {
                ergonEvent.setGatewayInstanceId(headerGw);
            }
            String headerTrigger = exchange.getMessage().getHeader("HIE_TRIGGER_TYPE", String.class);
            if (StringUtils.isBlank(ergonEvent.getTriggerType()) && StringUtils.isNotBlank(headerTrigger)) {
                ergonEvent.setTriggerType(headerTrigger);
            }
            String headerMsgType = exchange.getMessage().getHeader("HIE_MESSAGE_TYPE", String.class);
            if (StringUtils.isBlank(ergonEvent.getMessageType()) && StringUtils.isNotBlank(headerMsgType)) {
                ergonEvent.setMessageType(headerMsgType);
            }
            String headerCtrlId = exchange.getMessage().getHeader("HIE_CONTROL_ID", String.class);
            if (StringUtils.isBlank(ergonEvent.getControlId()) && StringUtils.isNotBlank(headerCtrlId)) {
                ergonEvent.setControlId(headerCtrlId);
            }
        }

        if (ergonEvent == null || StringUtils.isBlank(ergonEvent.getTaskId()) || "unknown".equals(ergonEvent.getTaskId())) {
            log.error("TaskEvent does not contain a valid taskId [category=INVALID_TASK_EVENT, stage=VALIDATION, payloadLength={}]",
                    rawContent != null ? rawContent.length() : 0);
            return;
        }

        String eventTaskId = ergonEvent.getTaskId().trim();
        String eventBusinessStatus = StringUtils.isNotBlank(ergonEvent.getAction()) ? ergonEvent.getAction() : "none";
        String eventPriority = "none";
        String eventType = StringUtils.isNotBlank(ergonEvent.getMessageType()) ? ergonEvent.getMessageType() : "ErgonEvent";
        String triggerReason = StringUtils.isNotBlank(ergonEvent.getTriggerType()) ? ergonEvent.getTriggerType() : eventBusinessStatus;

        log.info("[TASK-EVENT-PROCESSOR] Processing incoming TaskEvent: [taskId={}, businessStatus={}, priority={}, eventType={}, triggerReason={}, stage=PROCESSING]",
                eventTaskId, eventBusinessStatus, eventPriority, eventType, triggerReason);

        String taskId = eventTaskId;
        if (taskId.startsWith("Task/")) {
            taskId = taskId.substring("Task/".length());
        }

        // Retrieve the Task from Infinispan cache
        Optional<Task> cachedTaskOpt = taskCacheService.getTask(taskId);
        Task task;
        if (cachedTaskOpt.isPresent()) {
            task = cachedTaskOpt.get();
            log.info("Retrieved Task/{} from cache for TaskEvent processing [stage=CACHE_LOOKUP]", taskId);
        } else {
            log.warn("Task/{} not found in cache. Creating baseline Task instance [category=TASK_CACHE_MISS, stage=CACHE_LOOKUP].", taskId);
            task = new Task();
            task.setId("Task/" + taskId);
            task.setAuthoredOn(new Date());
            task.setDescription("Task created from incoming TaskEvent notification");
        }
        ErgonReasonEnum.ensureSyntheticTaskReason(task);

        // Update task status from event if specified
        if (StringUtils.isNotBlank(ergonEvent.getStatus())) {
            try {
                Task.TaskStatus fhirStatus = Task.TaskStatus.fromCode(ergonEvent.getStatus().toLowerCase());
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
        String actionText = StringUtils.isNotBlank(ergonEvent.getAction()) ? ergonEvent.getAction() : "PROCESSED";
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
                .append(", status=").append(ergonEvent.getStatus() != null ? ergonEvent.getStatus() : "COMPLETED")
                .append("]");
        if (StringUtils.isNotBlank(ergonEvent.getGatewayInstanceId())) {
            noteBuilder.append(" [gateway=").append(ergonEvent.getGatewayInstanceId()).append("]");
        }
        if (StringUtils.isNotBlank(ergonEvent.getTriggerType())) {
            noteBuilder.append(" [trigger=").append(ergonEvent.getMessageType() != null ? ergonEvent.getMessageType() : "ADT")
                    .append("^").append(ergonEvent.getTriggerType()).append("]");
        }
        if (StringUtils.isNotBlank(ergonEvent.getSource())) {
            noteBuilder.append(" from source [").append(ergonEvent.getSource()).append("]");
        }
        if (StringUtils.isNotBlank(ergonEvent.getDescription())) {
            noteBuilder.append(" - ").append(ergonEvent.getDescription());
        }
        noteBuilder.append(" at ").append(new Date());
        note.setText(noteBuilder.toString());
        note.setTime(new Date());
        task.addNote(note);

        // Save updated task to Infinispan cache
        taskCacheService.saveTask(task);
        log.info("Task/{} updated and persisted to cache following TaskEvent [action={}, stage=COMPLETED]", taskId, actionText);

        // Output processed task JSON on exchange
        IParser parser = (fhirContext != null ? fhirContext : FhirContext.forR5()).newJsonParser().setPrettyPrint(true);
        String taskJson = parser.encodeResourceToString(task);
        phiLog.trace("[TASK-EVENT-PROCESSOR] Updated Task output payload: {}", taskJson);

        exchange.getMessage().setBody(taskJson);
        exchange.getMessage().setHeader("HIE_TASK_ID", taskId);
        exchange.getMessage().setHeader("HIE_TASK_EVENT_ACTION", actionText);
        exchange.getMessage().setHeader("HIE_TASK_PROCESSED", true);
        if (StringUtils.isNotBlank(ergonEvent.getGatewayInstanceId())) {
            exchange.getMessage().setHeader("HIE_GATEWAY_INSTANCE", ergonEvent.getGatewayInstanceId());
        }
        if (StringUtils.isNotBlank(ergonEvent.getTriggerType())) {
            exchange.getMessage().setHeader("HIE_TRIGGER_TYPE", ergonEvent.getTriggerType());
        }
        if (StringUtils.isNotBlank(ergonEvent.getMessageType())) {
            exchange.getMessage().setHeader("HIE_MESSAGE_TYPE", ergonEvent.getMessageType());
        }
        if (StringUtils.isNotBlank(ergonEvent.getControlId())) {
            exchange.getMessage().setHeader("HIE_CONTROL_ID", ergonEvent.getControlId());
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
