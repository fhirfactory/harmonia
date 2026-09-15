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

package net.fhirfactory.harmonia.praxis.messaging;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.*;
import net.fhirfactory.harmonia.model.ergon.ErgonEvent;
import net.fhirfactory.harmonia.model.topic.Topic;
import net.fhirfactory.harmonia.praxis.config.QueueConfig;
import org.hl7.fhir.r5.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class TaskQueueProducerService {

    private static final Logger log = LoggerFactory.getLogger(TaskQueueProducerService.class);

    @Inject
    private ConnectionFactory connectionFactory;

    @Inject
    private QueueConfig queueConfig;

    @Inject
    private FhirContext fhirContext;

    private ObjectMapper objectMapper = new ObjectMapper();

    public void sendTask(Task task) throws JMSException {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        IParser parser = (fhirContext != null ? fhirContext : FhirContext.forR5()).newJsonParser().setPrettyPrint(false);
        String json = parser.encodeResourceToString(task);
        sendTaskJson(json);
    }

    public void sendTaskJson(String jsonPayload) throws JMSException {
        if (jsonPayload == null) {
            throw new IllegalArgumentException("Payload cannot be null");
        }
        String queueName = queueConfig.getQueueName();
        try (JMSContext context = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE)) {
            Queue queue = context.createQueue(queueName);
            TextMessage message = context.createTextMessage(jsonPayload);
            context.createProducer().send(queue, message);
            log.info("Sent Task payload to named message queue [{}]", queueName);
        }
    }

    public void sendTaskEvent(ErgonEvent event) throws Exception {
        if (event == null) {
            throw new IllegalArgumentException("TaskEvent cannot be null");
        }
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        String json = objectMapper.writeValueAsString(event);
        sendTaskEvent(event, json);
    }

    public void sendTaskEventJson(String jsonPayload) throws JMSException {
        if (jsonPayload == null) {
            throw new IllegalArgumentException("TaskEvent payload cannot be null");
        }
        ErgonEvent event = null;
        try {
            if (objectMapper == null) objectMapper = new ObjectMapper();
            event = objectMapper.readValue(jsonPayload, ErgonEvent.class);
        } catch (Exception ignored) {}
        sendTaskEvent(event, jsonPayload);
    }

    private void sendTaskEvent(ErgonEvent event, String jsonPayload) throws JMSException {
        if (jsonPayload == null) {
            throw new IllegalArgumentException("TaskEvent payload cannot be null");
        }
        String queueName;
        if (event != null && org.apache.commons.lang3.StringUtils.isNotBlank(event.getGatewayInstanceId()) && queueConfig != null) {
            queueName = queueConfig.getDedicatedEventQueueName(event.getGatewayInstanceId());
        } else if (queueConfig != null) {
            queueName = queueConfig.getEventQueueName();
        } else {
            queueName = QueueConfig.DEFAULT_EVENT_QUEUE_NAME;
        }

        try (JMSContext context = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE)) {
            Queue queue = context.createQueue(queueName);
            TextMessage message = context.createTextMessage(jsonPayload);

            if (event != null) {
                if (org.apache.commons.lang3.StringUtils.isNotBlank(event.getGatewayInstanceId())) {
                    message.setStringProperty("HIE_GATEWAY_INSTANCE", event.getGatewayInstanceId());
                }
                if (org.apache.commons.lang3.StringUtils.isNotBlank(event.getTriggerType())) {
                    message.setStringProperty("HIE_TRIGGER_TYPE", event.getTriggerType());
                }
                if (org.apache.commons.lang3.StringUtils.isNotBlank(event.getMessageType())) {
                    message.setStringProperty("HIE_MESSAGE_TYPE", event.getMessageType());
                }
                if (org.apache.commons.lang3.StringUtils.isNotBlank(event.getTaskId())) {
                    message.setStringProperty("HIE_TASK_ID", event.getTaskId());
                }
                if (org.apache.commons.lang3.StringUtils.isNotBlank(event.getControlId())) {
                    message.setStringProperty("HIE_CONTROL_ID", event.getControlId());
                }
                if (org.apache.commons.lang3.StringUtils.isNotBlank(event.getAction())) {
                    message.setStringProperty("HIE_ACTION", event.getAction());
                }
                if (org.apache.commons.lang3.StringUtils.isNotBlank(event.getStatus())) {
                    message.setStringProperty("HIE_STATUS", event.getStatus());
                }
                if (event.getTopic() != null) {
                    Topic t = event.getTopic();
                    if (t.getDomain() != null) message.setStringProperty("HIE_TOPIC_DOMAIN", t.getDomain());
                    if (t.getModel() != null) message.setStringProperty("HIE_TOPIC_MODEL", t.getModel());
                    if (t.getDataElement() != null) message.setStringProperty("HIE_TOPIC_DATA_ELEMENT", t.getDataElement());
                    if (t.getDataElementQualifier() != null) message.setStringProperty("HIE_TOPIC_QUALIFIER", t.getDataElementQualifier());
                    message.setStringProperty("HIE_TOPIC", t.toTopicString());
                }
            }

            context.createProducer().send(queue, message);
            log.info("Sent TaskEvent payload to named event queue [{}]", queueName);
        }
    }
}
