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

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import net.fhirfactory.hie.taskprocessor.config.QueueConfig;
import net.fhirfactory.hie.taskprocessor.sequence.TaskSequenceLoader;
import net.fhirfactory.hie.taskprocessors.base.TaskProcessingActivity;
import org.apache.camel.LoggingLevel;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Dependent
public class TaskProcessorRouteBuilder extends TaskProcessingActivity {

    @Inject
    private QueueConfig queueConfig;

    @Inject
    private TaskMessageProcessor taskMessageProcessor;

    @Inject
    private TaskEventMessageProcessor taskEventMessageProcessor;

    @Inject
    private TaskSequenceLoader taskSequenceLoader;

    public TaskProcessorRouteBuilder() {
        super("task-sequence-processor", "Task Sequence Processor Activity");
    }

    @Override
    protected void configureActivity() throws Exception {
        String queueName = queueConfig != null ? queueConfig.getQueueName() : QueueConfig.DEFAULT_QUEUE_NAME;

        // 1. Full Task Resource Queue consumer route
        from("jms:queue:" + queueName)
                .routeId("hie-task-processor-queue-route")
                .log(LoggingLevel.INFO, "Received message from Task queue [" + queueName + "]: ${body}")
                .process(taskMessageProcessor)
                .log(LoggingLevel.INFO, "Completed processing Task from queue [" + queueName + "]: ${body}");

        // Direct route for Task resource payload
        from("direct:task-processor-input")
                .routeId("hie-task-processor-direct-route")
                .log(LoggingLevel.INFO, "Direct processing Task payload: ${body}")
                .process(taskMessageProcessor)
                .log(LoggingLevel.INFO, "Completed direct processing of Task: ${body}");

        // 2. Determine all TaskEvent queues to listen to (base queue, default gateway queue, per-gateway queues)
        Set<String> eventQueues = new LinkedHashSet<>();
        if (queueConfig != null) {
            eventQueues.addAll(queueConfig.getGatewayEventQueues());
        } else {
            eventQueues.add(QueueConfig.DEFAULT_EVENT_QUEUE_NAME);
            eventQueues.add(QueueConfig.DEFAULT_EVENT_QUEUE_PREFIX + "." + QueueConfig.DEFAULT_GATEWAY_INSTANCE_ID);
        }

        if (taskSequenceLoader != null && queueConfig != null) {
            eventQueues.addAll(taskSequenceLoader.getAllTargetGatewayQueues(queueConfig.getEventQueuePrefix()));
        }

        // 3. Lightweight TaskEvent Queue consumer routes for each event queue
        for (String eq : eventQueues) {
            String routeSuffix = eq.replaceAll("[^a-zA-Z0-9-_]", "-");
            from("jms:queue:" + eq)
                    .routeId("hie-task-event-queue-route-" + routeSuffix)
                    .log(LoggingLevel.INFO, "Received message from TaskEvent queue [" + eq + "]: ${body}")
                    .process(taskEventMessageProcessor)
                    .log(LoggingLevel.INFO, "Completed processing TaskEvent from queue [" + eq + "], forwarding to sequence dispatcher")
                    .to("direct:sequence-dispatcher");
        }

        // Direct route for TaskEvent payload
        from("direct:task-event-input")
                .routeId("hie-task-event-direct-route")
                .log(LoggingLevel.INFO, "Direct processing TaskEvent payload: ${body}")
                .process(taskEventMessageProcessor)
                .log(LoggingLevel.INFO, "Completed direct processing of TaskEvent, forwarding to sequence dispatcher")
                .to("direct:sequence-dispatcher");

        // 4. Central TaskSequence Dispatcher route
        from("direct:sequence-dispatcher")
                .routeId("hie-sequence-dispatcher-route")
                .log(LoggingLevel.INFO, "Sequence dispatcher routing event [taskId=${header.HIE_TASK_ID}, gw=${header.HIE_GATEWAY_INSTANCE}, trigger=${header.HIE_TRIGGER_TYPE}]")
                .recipientList(method(this, "resolvePipelineEndpoints"))
                .parallelProcessing(false);
    }

    /**
     * Resolves the pipeline input endpoints for all active TaskSequence instances.
     */
    public List<String> resolvePipelineEndpoints() {
        if (taskSequenceLoader != null) {
            List<String> endpoints = taskSequenceLoader.getSequencePipelineEndpoints();
            if (endpoints != null && !endpoints.isEmpty()) {
                return endpoints;
            }
        }
        return Collections.emptyList();
    }

    public void setQueueConfig(QueueConfig queueConfig) {
        this.queueConfig = queueConfig;
    }

    public void setTaskMessageProcessor(TaskMessageProcessor taskMessageProcessor) {
        this.taskMessageProcessor = taskMessageProcessor;
    }

    public void setTaskEventMessageProcessor(TaskEventMessageProcessor taskEventMessageProcessor) {
        this.taskEventMessageProcessor = taskEventMessageProcessor;
    }

    public void setTaskSequenceLoader(TaskSequenceLoader taskSequenceLoader) {
        this.taskSequenceLoader = taskSequenceLoader;
    }
}
