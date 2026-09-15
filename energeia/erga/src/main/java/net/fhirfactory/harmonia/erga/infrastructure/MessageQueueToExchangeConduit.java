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

package net.fhirfactory.harmonia.erga.infrastructure;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.Dependent;
import net.fhirfactory.harmonia.model.ergon.ErgonEvent;
import net.fhirfactory.harmonia.model.topic.Topic;
import net.fhirfactory.harmonia.erga.base.ErgonBase;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * Task Processing Activity that acts as a conduit from a message queue (JMS/Artemis)
 * into the Apache Camel exchange pipeline.
 * <p>
 * Ingests incoming message payloads (such as {@link ErgonEvent} instances, JSON documents,
 * HL7 v2 messages, or FHIR payloads), normalizes message headers onto the Camel Exchange,
 * preserves raw message content, and forwards the exchange to downstream task processing activities.
 */
@Dependent
public class MessageQueueToExchangeConduit extends ErgonBase {

    private static final Logger log = LoggerFactory.getLogger(MessageQueueToExchangeConduit.class);

    public static final String DEFAULT_ACTIVITY_ID = "message-queue-to-exchange";
    public static final String DEFAULT_ACTIVITY_NAME = "Message Queue to Exchange Conduit Activity";
    public static final String DEFAULT_QUEUE_NAME = "task.event.queue";

    // Standard HIE Exchange Header constants
    public static final String HEADER_GATEWAY_INSTANCE = "HIE_GATEWAY_INSTANCE";
    public static final String HEADER_TRIGGER_TYPE = "HIE_TRIGGER_TYPE";
    public static final String HEADER_MESSAGE_TYPE = "HIE_MESSAGE_TYPE";
    public static final String HEADER_TASK_ID = "HIE_TASK_ID";
    public static final String HEADER_CONTROL_ID = "HIE_CONTROL_ID";
    public static final String HEADER_ACTION = "HIE_ACTION";
    public static final String HEADER_STATUS = "HIE_STATUS";
    public static final String HEADER_RAW_MESSAGE = "HIE_RAW_MESSAGE";
    public static final String HEADER_SOURCE_QUEUE = "HIE_SOURCE_QUEUE";
    public static final String HEADER_TOPIC = "HIE_TOPIC";
    public static final String HEADER_TOPIC_DOMAIN = "HIE_TOPIC_DOMAIN";
    public static final String HEADER_TOPIC_MODEL = "HIE_TOPIC_MODEL";
    public static final String HEADER_TOPIC_DATA_ELEMENT = "HIE_TOPIC_DATA_ELEMENT";
    public static final String HEADER_TOPIC_QUALIFIER = "HIE_TOPIC_QUALIFIER";

    private String queueName = DEFAULT_QUEUE_NAME;
    private final ObjectMapper objectMapper;

    /**
     * Default constructor.
     */
    public MessageQueueToExchangeConduit() {
        super(DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
        setActivityDescription("Acts as a conduit ingesting messages from a message queue into the Camel exchange pipeline");
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Constructor with CamelContext.
     *
     * @param context CamelContext instance
     */
    public MessageQueueToExchangeConduit(CamelContext context) {
        super(context, DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
        setActivityDescription("Acts as a conduit ingesting messages from a message queue into the Camel exchange pipeline");
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Constructor with activity identification.
     *
     * @param activityId   Unique identifier of the activity
     * @param activityName Human-readable activity name
     */
    public MessageQueueToExchangeConduit(String activityId, String activityName) {
        super(activityId, activityName);
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Constructor with activity identification and custom queue name.
     *
     * @param activityId   Unique identifier of the activity
     * @param activityName Human-readable activity name
     * @param queueName    Name of the message queue to consume from
     */
    public MessageQueueToExchangeConduit(String activityId, String activityName, String queueName) {
        super(activityId, activityName);
        this.queueName = queueName;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void processActivity(Exchange exchange) throws Exception {
        processMessageQueueToExchange(exchange);
    }

    @Override
    protected void configureActivity() throws Exception {
        String input = resolveInputEndpoint();
        String output = getOutputEndpoint();

        log.info("Configuring MessageQueueToExchangeConduit route: {} -> {}", input, (output != null && !output.isBlank()) ? output : "[terminal]");

        org.apache.camel.model.RouteDefinition route = from(input)
                .routeId(generateRouteId())
                .routeDescription(getActivityDescription() != null ? getActivityDescription() : getActivityName())
                .log(LoggingLevel.INFO, log.getName(), "MessageQueueToExchangeConduit received message from [" + input + "] on exchange ${exchangeId}")
                .process(this::processMessageQueueToExchange)
                .log(LoggingLevel.INFO, log.getName(), "MessageQueueToExchangeConduit successfully processed exchange ${exchangeId}" + (output != null && !output.isBlank() ? " and forwarding to [" + output + "]" : ""));

        if (output != null && !output.isBlank()) {
            route.to(output);
        }
    }

    /**
     * Resolves the effective Camel input endpoint URI for consuming messages.
     *
     * @return endpoint URI string
     */
    public String resolveInputEndpoint() {
        if (getInputEndpoint() != null && !getInputEndpoint().isBlank()) {
            return getInputEndpoint();
        }
        if (queueName != null && !queueName.isBlank()) {
            if (queueName.startsWith("jms:") || queueName.startsWith("direct:")) {
                return queueName;
            }
            return "jms:queue:" + queueName;
        }
        return "jms:queue:" + DEFAULT_QUEUE_NAME;
    }

    /**
     * Ingests the message from the queue, extracts metadata, populates Camel exchange headers,
     * and prepares the payload for downstream pipeline activities.
     *
     * @param exchange Camel Exchange
     */
    public void processMessageQueueToExchange(Exchange exchange) {
        if (exchange == null || exchange.getMessage() == null) {
            log.warn("MessageQueueToExchangeConduit received null exchange or message");
            return;
        }

        Object body = exchange.getMessage().getBody();
        log.debug("MessageQueueToExchangeConduit processing message payload of type [{}]",
                body != null ? body.getClass().getName() : "null");

        // Set source queue metadata header
        String sourceQueue = queueName;
        if (exchange.getMessage().getHeader("JMSDestination") != null) {
            sourceQueue = String.valueOf(exchange.getMessage().getHeader("JMSDestination"));
        }
        if (sourceQueue != null && exchange.getMessage().getHeader(HEADER_SOURCE_QUEUE) == null) {
            exchange.getMessage().setHeader(HEADER_SOURCE_QUEUE, sourceQueue);
        }

        if (body instanceof ErgonEvent) {
            processTaskEvent(exchange, (ErgonEvent) body);
        } else if (body instanceof byte[]) {
            String strBody = new String((byte[]) body, StandardCharsets.UTF_8);
            exchange.getMessage().setBody(strBody);
            processStringPayload(exchange, strBody);
        } else if (body instanceof String) {
            processStringPayload(exchange, (String) body);
        } else if (body != null) {
            // For other resource/object types, set raw message header if string representation available
            if (exchange.getMessage().getHeader(HEADER_RAW_MESSAGE) == null) {
                exchange.getMessage().setHeader(HEADER_RAW_MESSAGE, body.toString());
            }
        }
    }

    private void processTaskEvent(Exchange exchange, ErgonEvent ergonEvent) {
        if (ergonEvent == null) {
            return;
        }

        setHeaderIfAbsent(exchange, HEADER_GATEWAY_INSTANCE, ergonEvent.getGatewayInstanceId());
        setHeaderIfAbsent(exchange, HEADER_TRIGGER_TYPE, ergonEvent.getTriggerType());
        setHeaderIfAbsent(exchange, HEADER_MESSAGE_TYPE, ergonEvent.getMessageType());
        setHeaderIfAbsent(exchange, HEADER_TASK_ID, ergonEvent.getTaskId());
        setHeaderIfAbsent(exchange, HEADER_CONTROL_ID, ergonEvent.getControlId());
        setHeaderIfAbsent(exchange, HEADER_ACTION, ergonEvent.getAction());
        setHeaderIfAbsent(exchange, HEADER_STATUS, ergonEvent.getStatus());

        if (ergonEvent.getTopic() != null) {
            Topic t = ergonEvent.getTopic();
            exchange.getMessage().setHeader(HEADER_TOPIC, t);
            if (t.getDomain() != null) setHeaderIfAbsent(exchange, HEADER_TOPIC_DOMAIN, t.getDomain());
            if (t.getModel() != null) setHeaderIfAbsent(exchange, HEADER_TOPIC_MODEL, t.getModel());
            if (t.getDataElement() != null) setHeaderIfAbsent(exchange, HEADER_TOPIC_DATA_ELEMENT, t.getDataElement());
            if (t.getDataElementQualifier() != null) setHeaderIfAbsent(exchange, HEADER_TOPIC_QUALIFIER, t.getDataElementQualifier());
        }

        if (exchange.getMessage().getHeader(HEADER_RAW_MESSAGE) == null) {
            try {
                String json = objectMapper.writeValueAsString(ergonEvent);
                exchange.getMessage().setHeader(HEADER_RAW_MESSAGE, json);
            } catch (Exception ignored) {
            }
        }
    }

    private void processStringPayload(Exchange exchange, String payload) {
        if (payload == null || payload.isBlank()) {
            return;
        }

        setHeaderIfAbsent(exchange, HEADER_RAW_MESSAGE, payload);

        String trimmed = payload.trim();
        if (trimmed.startsWith("{")) {
            try {
                JsonNode node = objectMapper.readTree(trimmed);
                if (node.has("gatewayInstanceId")) {
                    setHeaderIfAbsent(exchange, HEADER_GATEWAY_INSTANCE, node.get("gatewayInstanceId").asText());
                }
                if (node.has("triggerType")) {
                    setHeaderIfAbsent(exchange, HEADER_TRIGGER_TYPE, node.get("triggerType").asText());
                }
                if (node.has("messageType")) {
                    setHeaderIfAbsent(exchange, HEADER_MESSAGE_TYPE, node.get("messageType").asText());
                }
                if (node.has("taskId")) {
                    setHeaderIfAbsent(exchange, HEADER_TASK_ID, node.get("taskId").asText());
                }
                if (node.has("controlId")) {
                    setHeaderIfAbsent(exchange, HEADER_CONTROL_ID, node.get("controlId").asText());
                }
                if (node.has("action")) {
                    setHeaderIfAbsent(exchange, HEADER_ACTION, node.get("action").asText());
                }
                if (node.has("status")) {
                    setHeaderIfAbsent(exchange, HEADER_STATUS, node.get("status").asText());
                }
                if (node.has("topic")) {
                    try {
                        Topic topic = objectMapper.treeToValue(node.get("topic"), Topic.class);
                        if (topic != null) {
                            exchange.getMessage().setHeader(HEADER_TOPIC, topic);
                            if (topic.getDomain() != null) setHeaderIfAbsent(exchange, HEADER_TOPIC_DOMAIN, topic.getDomain());
                            if (topic.getModel() != null) setHeaderIfAbsent(exchange, HEADER_TOPIC_MODEL, topic.getModel());
                            if (topic.getDataElement() != null) setHeaderIfAbsent(exchange, HEADER_TOPIC_DATA_ELEMENT, topic.getDataElement());
                            if (topic.getDataElementQualifier() != null) setHeaderIfAbsent(exchange, HEADER_TOPIC_QUALIFIER, topic.getDataElementQualifier());
                        }
                    } catch (Exception ignored) {}
                }
            } catch (Exception e) {
                log.debug("Message body is not JSON TaskEvent: {}", e.getMessage());
            }
        }
    }

    private void setHeaderIfAbsent(Exchange exchange, String headerName, String value) {
        if (value != null && !value.isBlank() && exchange.getMessage().getHeader(headerName) == null) {
            exchange.getMessage().setHeader(headerName, value);
        }
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
}
