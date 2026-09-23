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
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.logging.PhiLogger;
import net.fhirfactory.harmonia.logging.PhiLoggerFactory;
import net.fhirfactory.harmonia.praxis.cache.TaskCacheService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.CodeableReference;
import org.hl7.fhir.r5.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class TaskMessageProcessor implements Processor {

    private static final Logger log = LoggerFactory.getLogger(TaskMessageProcessor.class);
    private static final PhiLogger phiLog = PhiLoggerFactory.getLogger(TaskMessageProcessor.class);

    @Inject
    private TaskCacheService taskCacheService;

    @Inject
    private FhirContext fhirContext;

    @Override
    public void process(Exchange exchange) throws Exception {
        Object body = exchange.getMessage().getBody();
        if (body == null) {
            log.warn("Received empty/null message body on Camel route [category=EMPTY_PAYLOAD, stage=RECEIVED]");
            return;
        }

        String rawContent;
        Task task = null;

        if (body instanceof Task) {
            task = (Task) body;
            IParser parser = (fhirContext != null ? fhirContext : FhirContext.forR5()).newJsonParser().setPrettyPrint(true);
            rawContent = parser.encodeResourceToString(task);
        } else if (body instanceof byte[]) {
            rawContent = new String((byte[]) body);
        } else {
            rawContent = body.toString();
        }

        // Diagnostic payload inspection routed exclusively to PhiLogger at DEBUG
        phiLog.debug("[TASK-PROCESSOR] Incoming Task message payload: {}", rawContent);

        // Parse to FHIR Task if not already parsed
        if (task == null && StringUtils.isNotBlank(rawContent)) {
            try {
                IParser parser = (fhirContext != null ? fhirContext : FhirContext.forR5()).newJsonParser();
                if (rawContent.trim().startsWith("<")) {
                    parser = (fhirContext != null ? fhirContext : FhirContext.forR5()).newXmlParser();
                }
                task = parser.parseResource(Task.class, rawContent);
            } catch (Exception e) {
                log.warn("Payload is not directly parseable as a FHIR Task resource [exception={}, category=TASK_PARSE_FAILURE]. Checking if it is a Task ID.", e.getClass().getName());
                // Fallback: check if content is a Task ID
                String cleanId = rawContent.trim().replace("\"", "");
                if (cleanId.contains("{") || cleanId.contains("\n") || cleanId.length() > 128) {
                    cleanId = "unknown";
                }
                task = taskCacheService.getTask(cleanId).orElse(null);
                if (task == null && !"unknown".equals(cleanId)) {
                    task = new Task();
                    task.setId("Task/" + cleanId);
                }
            }
        }

        if (task == null) {
            log.error("Could not obtain or create a Task resource [category=TASK_CREATION_FAILURE, stage=PARSE, payloadLength={}]",
                    rawContent != null ? rawContent.length() : 0);
            return;
        }

        // Extract safe operational metadata for logging
        String taskId = task.hasId() ? task.getIdElement().getIdPart() : "unknown";
        String businessStatus = task.hasBusinessStatus() ? task.getBusinessStatus().getText() : "none";
        String priority = (task.hasPriority() && task.getPriority() != null) ? task.getPriority().toCode() : "none";
        String eventType = exchange.getMessage().getHeader("HIE_MESSAGE_TYPE", String.class);
        if (StringUtils.isBlank(eventType)) {
            eventType = "Task";
        }
        String triggerReason = exchange.getMessage().getHeader("HIE_TRIGGER_TYPE", String.class);
        if (StringUtils.isBlank(triggerReason)) {
            triggerReason = exchange.getMessage().getHeader("HIE_TASK_EVENT_ACTION", String.class);
        }
        if (StringUtils.isBlank(triggerReason) && task.hasReason() && !task.getReason().isEmpty()) {
            CodeableReference ref = task.getReason().get(0);
            if (ref.hasConcept() && ref.getConcept().hasText()) {
                triggerReason = ref.getConcept().getText();
            } else if (ref.hasConcept() && !ref.getConcept().getCoding().isEmpty()) {
                triggerReason = ref.getConcept().getCoding().get(0).getCode();
            }
        }
        if (StringUtils.isBlank(triggerReason)) {
            triggerReason = "none";
        }

        log.info("[TASK-PROCESSOR] Processing incoming Task: [taskId={}, businessStatus={}, priority={}, eventType={}, triggerReason={}, stage=PROCESSING]",
                taskId, businessStatus, priority, eventType, triggerReason);

        // Update the Task persisted within the Infinispan cache to indicate that it has been processed
        Task processedTask = taskCacheService.markTaskAsProcessed(task);

        log.info("Task [id={}, status={}, businessStatus={}, stage=COMPLETED] successfully updated and marked as processed in Infinispan cache",
                processedTask.getId(),
                processedTask.getStatus(),
                processedTask.hasBusinessStatus() ? processedTask.getBusinessStatus().getText() : "none");

        // Set the processed Task and JSON as output on the exchange
        IParser prettyParser = (fhirContext != null ? fhirContext : FhirContext.forR5()).newJsonParser().setPrettyPrint(true);
        String updatedJson = prettyParser.encodeResourceToString(processedTask);
        phiLog.trace("[TASK-PROCESSOR] Processed Task output payload: {}", updatedJson);

        exchange.getMessage().setBody(updatedJson);
        exchange.getMessage().setHeader("HIE_TASK_ID", processedTask.getIdElement().getIdPart());
        exchange.getMessage().setHeader("HIE_TASK_PROCESSED", true);
    }

    public void setTaskCacheService(TaskCacheService taskCacheService) {
        this.taskCacheService = taskCacheService;
    }

    public void setFhirContext(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
    }
}
