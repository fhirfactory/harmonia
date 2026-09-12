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

package net.fhirfactory.hie.mllpgateway.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.*;
import net.fhirfactory.hie.mllpgateway.config.TaskProcessorConfig;
import net.fhirfactory.hie.model.TaskEvent;
import net.fhirfactory.hie.model.topic.Topic;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@ApplicationScoped
public class TaskEventProducerService {

    public static final String HEADER_GATEWAY_INSTANCE = "HIE_GATEWAY_INSTANCE";
    public static final String HEADER_TRIGGER_TYPE = "HIE_TRIGGER_TYPE";
    public static final String HEADER_MESSAGE_TYPE = "HIE_MESSAGE_TYPE";
    public static final String HEADER_TASK_ID = "HIE_TASK_ID";
    public static final String HEADER_CONTROL_ID = "HIE_CONTROL_ID";
    public static final String HEADER_ACTION = "HIE_ACTION";
    public static final String HEADER_STATUS = "HIE_STATUS";
    public static final String HEADER_TOPIC = "HIE_TOPIC";
    public static final String HEADER_TOPIC_DOMAIN = "HIE_TOPIC_DOMAIN";
    public static final String HEADER_TOPIC_MODEL = "HIE_TOPIC_MODEL";
    public static final String HEADER_TOPIC_DATA_ELEMENT = "HIE_TOPIC_DATA_ELEMENT";
    public static final String HEADER_TOPIC_QUALIFIER = "HIE_TOPIC_QUALIFIER";

    private static final Logger log = LoggerFactory.getLogger(TaskEventProducerService.class);

    @Inject
    private TaskProcessorConfig config;

    @Inject
    private ConnectionFactory connectionFactory;

    private ObjectMapper objectMapper = new ObjectMapper();

    public TaskEventProducerService() {
    }

    public TaskEventProducerService(TaskProcessorConfig config) {
        this.config = config;
    }

    public TaskEventProducerService(TaskProcessorConfig config, ConnectionFactory connectionFactory) {
        this.config = config;
        this.connectionFactory = connectionFactory;
    }

    public void sendTaskEvent(TaskEvent event) throws Exception {
        if (event == null) {
            throw new IllegalArgumentException("TaskEvent cannot be null");
        }
        if (config != null && !config.isEventProducerEnabled()) {
            log.debug("TaskEvent production is disabled via configuration");
            return;
        }
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        String json = objectMapper.writeValueAsString(event);
        sendTaskEvent(event, json);
    }

    public void sendTaskEvent(String taskId, String action, String status, Topic topic, String controlId, String description) throws Exception {
        TaskEvent event = new TaskEvent(taskId, action, status, topic, controlId, description);
        sendTaskEvent(event);
    }

    public void sendTaskEvent(String taskId, String action, String status, String source, String description) throws Exception {
        String gatewayId = config != null ? config.getGatewayInstanceId() : TaskProcessorConfig.DEFAULT_GATEWAY_INSTANCE_ID;
        TaskEvent event = new TaskEvent(taskId, action, status, gatewayId, "ADT", "A01", null, source, description);
        sendTaskEvent(event);
    }

    public void sendTaskEvent(String taskId, String action, String status, String gatewayInstanceId,
                              String messageType, String triggerType, String controlId,
                              String source, String description) throws Exception {
        TaskEvent event = new TaskEvent(taskId, action, status, gatewayInstanceId, messageType, triggerType, controlId, source, description);
        sendTaskEvent(event);
    }

    public void sendTaskEventJson(String jsonPayload) throws Exception {
        if (StringUtils.isBlank(jsonPayload)) {
            throw new IllegalArgumentException("TaskEvent payload cannot be empty");
        }
        TaskEvent event = null;
        try {
            if (objectMapper == null) objectMapper = new ObjectMapper();
            event = objectMapper.readValue(jsonPayload, TaskEvent.class);
        } catch (Exception ignored) {}
        sendTaskEvent(event, jsonPayload);
    }

    private void sendTaskEvent(TaskEvent event, String jsonPayload) throws Exception {
        if (StringUtils.isBlank(jsonPayload)) {
            throw new IllegalArgumentException("TaskEvent payload cannot be empty");
        }

        String queueName;
        if (event != null && StringUtils.isNotBlank(event.getGatewayInstanceId()) && config != null) {
            queueName = config.getDedicatedEventQueueName(event.getGatewayInstanceId());
        } else if (config != null) {
            queueName = config.getEventQueueName();
        } else {
            queueName = TaskProcessorConfig.DEFAULT_EVENT_QUEUE_NAME;
        }

        boolean sentViaJms = false;

        ConnectionFactory cf = resolveConnectionFactory();
        if (cf != null) {
            try (JMSContext context = cf.createContext(Session.AUTO_ACKNOWLEDGE)) {
                Queue queue = context.createQueue(queueName);
                TextMessage message = context.createTextMessage(jsonPayload);

                if (event != null) {
                    if (StringUtils.isNotBlank(event.getGatewayInstanceId())) {
                        message.setStringProperty(HEADER_GATEWAY_INSTANCE, event.getGatewayInstanceId());
                    }
                    if (StringUtils.isNotBlank(event.getTriggerType())) {
                        message.setStringProperty(HEADER_TRIGGER_TYPE, event.getTriggerType());
                    }
                    if (StringUtils.isNotBlank(event.getMessageType())) {
                        message.setStringProperty(HEADER_MESSAGE_TYPE, event.getMessageType());
                    }
                    if (StringUtils.isNotBlank(event.getTaskId())) {
                        message.setStringProperty(HEADER_TASK_ID, event.getTaskId());
                    }
                    if (StringUtils.isNotBlank(event.getControlId())) {
                        message.setStringProperty(HEADER_CONTROL_ID, event.getControlId());
                    }
                    if (StringUtils.isNotBlank(event.getAction())) {
                        message.setStringProperty(HEADER_ACTION, event.getAction());
                    }
                    if (StringUtils.isNotBlank(event.getStatus())) {
                        message.setStringProperty(HEADER_STATUS, event.getStatus());
                    }
                    if (event.getTopic() != null) {
                        Topic t = event.getTopic();
                        if (t.getDomain() != null) message.setStringProperty(HEADER_TOPIC_DOMAIN, t.getDomain());
                        if (t.getModel() != null) message.setStringProperty(HEADER_TOPIC_MODEL, t.getModel());
                        if (t.getDataElement() != null) message.setStringProperty(HEADER_TOPIC_DATA_ELEMENT, t.getDataElement());
                        if (t.getDataElementQualifier() != null) message.setStringProperty(HEADER_TOPIC_QUALIFIER, t.getDataElementQualifier());
                        message.setStringProperty(HEADER_TOPIC, t.toTopicString());
                    }
                }

                context.createProducer().send(queue, message);
                log.info("MLLP Gateway sent TaskEvent to queue [{}]", queueName);
                sentViaJms = true;
            } catch (Exception e) {
                log.warn("JMS dispatch to queue [{}] failed: {}. Attempting HTTP fallback.", queueName, e.getMessage());
            }
        }

        if (!sentViaJms) {
            // Attempt HTTP REST dispatch to task-processor endpoint
            try {
                String endpointUrl = config != null ? config.getTaskProcessorUrl() : TaskProcessorConfig.DEFAULT_TASK_PROCESSOR_URL;
                HttpClient client = HttpClient.newBuilder()
                        .connectTimeout(Duration.ofSeconds(2))
                        .build();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(endpointUrl))
                        .header("Content-Type", "application/json")
                        .timeout(Duration.ofSeconds(3))
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    log.info("MLLP Gateway sent TaskEvent to task-processor via HTTP endpoint [{}]", endpointUrl);
                } else {
                    log.debug("HTTP fallback to [{}] returned status code {}", endpointUrl, response.statusCode());
                }
            } catch (Exception httpEx) {
                log.debug("HTTP fallback to task-processor was unreachable: {}", httpEx.getMessage());
            }
        }
    }

    private ConnectionFactory resolveConnectionFactory() {
        if (connectionFactory != null) {
            return connectionFactory;
        }
        if (config == null) {
            config = new TaskProcessorConfig();
        }
        try {
            String brokerUrl = config.getBrokerUrl();
            String user = config.getBrokerUsername();
            String pass = config.getBrokerPassword();
            return new ActiveMQConnectionFactory(brokerUrl, user, pass);
        } catch (Exception e) {
            log.debug("Lazy creation of ActiveMQConnectionFactory failed: {}", e.getMessage());
            return null;
        }
    }

    public TaskProcessorConfig getConfig() {
        return config;
    }

    public void setConfig(TaskProcessorConfig config) {
        this.config = config;
    }

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
}
