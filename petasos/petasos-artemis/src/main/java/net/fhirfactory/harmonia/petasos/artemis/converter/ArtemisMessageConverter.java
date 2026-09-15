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

package net.fhirfactory.harmonia.petasos.artemis.converter;

import jakarta.jms.*;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.exception.PetasosMessagingException;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessageBuilder;
import org.apache.activemq.artemis.api.core.Message;

import java.time.Instant;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Bi-directional converter between {@link PetasosMessage} envelopes and JMS messages.
 */
public final class ArtemisMessageConverter {

    public static final String HDR_PETASOS_MESSAGE_ID = "petasos_message_id";
    public static final String HDR_PETASOS_CORRELATION_ID = "petasos_correlation_id";
    public static final String HDR_PETASOS_CAUSATION_ID = "petasos_causation_id";
    public static final String HDR_PETASOS_MESSAGE_TYPE = "petasos_message_type";
    public static final String HDR_PETASOS_SOURCE = "petasos_source";
    public static final String HDR_PETASOS_CONTENT_TYPE = "petasos_content_type";
    public static final String HDR_PETASOS_SCHEMA_ID = "petasos_schema_id";
    public static final String HDR_PETASOS_SCHEMA_VER = "petasos_schema_ver";
    public static final String HDR_PETASOS_TIMESTAMP = "petasos_timestamp";
    public static final String HDR_PETASOS_DURABLE = "petasos_durable";
    public static final String HDR_PETASOS_DESTINATION_NAME = "petasos_destination_name";
    public static final String HDR_PETASOS_DESTINATION_TYPE = "petasos_destination_type";
    public static final String HDR_AMQ_DUPL_ID = Message.HDR_DUPLICATE_DETECTION_ID.toString(); // "_AMQ_DUPL_ID"

    public static jakarta.jms.Message toJmsMessage(PetasosMessage petasosMessage, Session session) throws JMSException {
        if (petasosMessage == null) {
            throw new IllegalArgumentException("PetasosMessage must not be null");
        }
        if (session == null) {
            throw new IllegalArgumentException("JMS Session must not be null");
        }

        BytesMessage jmsMessage = session.createBytesMessage();
        byte[] payload = petasosMessage.getPayload();
        if (payload != null && payload.length > 0) {
            jmsMessage.writeBytes(payload);
        }

        // Standard JMS headers
        if (petasosMessage.getCorrelationId() != null) {
            jmsMessage.setJMSCorrelationID(petasosMessage.getCorrelationId());
        }
        if (petasosMessage.getMessageType() != null) {
            jmsMessage.setJMSType(petasosMessage.getMessageType());
        }
        jmsMessage.setJMSPriority(petasosMessage.getPriority());
        jmsMessage.setJMSDeliveryMode(petasosMessage.isDurable() ? DeliveryMode.PERSISTENT : DeliveryMode.NON_PERSISTENT);

        // Artemis duplicate detection header
        if (petasosMessage.getDuplicateDetectionId() != null) {
            jmsMessage.setStringProperty(HDR_AMQ_DUPL_ID, petasosMessage.getDuplicateDetectionId());
        }

        // Petasos lineage and envelope headers
        jmsMessage.setStringProperty(HDR_PETASOS_MESSAGE_ID, petasosMessage.getMessageId());
        if (petasosMessage.getCorrelationId() != null) {
            jmsMessage.setStringProperty(HDR_PETASOS_CORRELATION_ID, petasosMessage.getCorrelationId());
        }
        if (petasosMessage.getCausationId() != null) {
            jmsMessage.setStringProperty(HDR_PETASOS_CAUSATION_ID, petasosMessage.getCausationId());
        }
        if (petasosMessage.getMessageType() != null) {
            jmsMessage.setStringProperty(HDR_PETASOS_MESSAGE_TYPE, petasosMessage.getMessageType());
        }
        if (petasosMessage.getSource() != null) {
            jmsMessage.setStringProperty(HDR_PETASOS_SOURCE, petasosMessage.getSource());
        }
        if (petasosMessage.getContentType() != null) {
            jmsMessage.setStringProperty(HDR_PETASOS_CONTENT_TYPE, petasosMessage.getContentType());
        }
        if (petasosMessage.getSchemaIdentifier() != null) {
            jmsMessage.setStringProperty(HDR_PETASOS_SCHEMA_ID, petasosMessage.getSchemaIdentifier());
        }
        if (petasosMessage.getSchemaVersion() != null) {
            jmsMessage.setStringProperty(HDR_PETASOS_SCHEMA_VER, petasosMessage.getSchemaVersion());
        }
        if (petasosMessage.getTimestamp() != null) {
            jmsMessage.setLongProperty(HDR_PETASOS_TIMESTAMP, petasosMessage.getTimestamp().toEpochMilli());
        }
        jmsMessage.setBooleanProperty(HDR_PETASOS_DURABLE, petasosMessage.isDurable());

        if (petasosMessage.getDestination() != null) {
            jmsMessage.setStringProperty(HDR_PETASOS_DESTINATION_NAME, petasosMessage.getDestination().getName());
            jmsMessage.setStringProperty(HDR_PETASOS_DESTINATION_TYPE, petasosMessage.getDestination().getType().name());
        }

        // Application metadata map
        Map<String, Object> metadata = petasosMessage.getMetadata();
        if (metadata != null) {
            for (Map.Entry<String, Object> entry : metadata.entrySet()) {
                String key = entry.getKey();
                Object val = entry.getValue();
                if (key != null && val != null) {
                    if (val instanceof String s) {
                        jmsMessage.setStringProperty(key, s);
                    } else if (val instanceof Integer i) {
                        jmsMessage.setIntProperty(key, i);
                    } else if (val instanceof Long l) {
                        jmsMessage.setLongProperty(key, l);
                    } else if (val instanceof Boolean b) {
                        jmsMessage.setBooleanProperty(key, b);
                    } else if (val instanceof Double d) {
                        jmsMessage.setDoubleProperty(key, d);
                    } else {
                        jmsMessage.setStringProperty(key, val.toString());
                    }
                }
            }
        }

        return jmsMessage;
    }

    public static PetasosMessage toPetasosMessage(jakarta.jms.Message jmsMessage) throws JMSException {
        if (jmsMessage == null) {
            throw new IllegalArgumentException("JMS Message must not be null");
        }

        PetasosMessageBuilder builder = PetasosMessage.builder();

        // Extract payload
        byte[] payload = extractPayload(jmsMessage);
        builder.payload(payload);

        // Standard properties
        String msgId = jmsMessage.getStringProperty(HDR_PETASOS_MESSAGE_ID);
        if (msgId == null || msgId.isBlank()) {
            msgId = jmsMessage.getJMSMessageID();
        }
        if (msgId != null && !msgId.isBlank()) {
            builder.messageId(msgId);
        }

        String corrId = jmsMessage.getStringProperty(HDR_PETASOS_CORRELATION_ID);
        if (corrId == null || corrId.isBlank()) {
            corrId = jmsMessage.getJMSCorrelationID();
        }
        if (corrId != null && !corrId.isBlank()) {
            builder.correlationId(corrId);
        }

        String causationId = jmsMessage.getStringProperty(HDR_PETASOS_CAUSATION_ID);
        if (causationId != null) {
            builder.causationId(causationId);
        }

        String msgType = jmsMessage.getStringProperty(HDR_PETASOS_MESSAGE_TYPE);
        if (msgType == null || msgType.isBlank()) {
            msgType = jmsMessage.getJMSType();
        }
        if (msgType != null) {
            builder.messageType(msgType);
        }

        String source = jmsMessage.getStringProperty(HDR_PETASOS_SOURCE);
        if (source != null) {
            builder.source(source);
        }

        String contentType = jmsMessage.getStringProperty(HDR_PETASOS_CONTENT_TYPE);
        if (contentType != null) {
            builder.contentType(contentType);
        }

        String schemaId = jmsMessage.getStringProperty(HDR_PETASOS_SCHEMA_ID);
        String schemaVer = jmsMessage.getStringProperty(HDR_PETASOS_SCHEMA_VER);
        if (schemaId != null) {
            builder.schema(schemaId, schemaVer);
        }

        if (jmsMessage.propertyExists(HDR_PETASOS_TIMESTAMP)) {
            builder.timestamp(Instant.ofEpochMilli(jmsMessage.getLongProperty(HDR_PETASOS_TIMESTAMP)));
        } else if (jmsMessage.getJMSTimestamp() > 0) {
            builder.timestamp(Instant.ofEpochMilli(jmsMessage.getJMSTimestamp()));
        }

        if (jmsMessage.propertyExists(HDR_PETASOS_DURABLE)) {
            builder.durable(jmsMessage.getBooleanProperty(HDR_PETASOS_DURABLE));
        } else {
            builder.durable(jmsMessage.getJMSDeliveryMode() == DeliveryMode.PERSISTENT);
        }

        builder.priority(jmsMessage.getJMSPriority());

        String duplId = jmsMessage.getStringProperty(HDR_AMQ_DUPL_ID);
        if (duplId != null) {
            builder.duplicateDetectionId(duplId);
        }

        // Destination
        String destName = jmsMessage.getStringProperty(HDR_PETASOS_DESTINATION_NAME);
        String destTypeStr = jmsMessage.getStringProperty(HDR_PETASOS_DESTINATION_TYPE);
        if (destName != null && !destName.isBlank()) {
            PetasosDestination.DestinationType type = destTypeStr != null && destTypeStr.equalsIgnoreCase("TOPIC")
                    ? PetasosDestination.DestinationType.TOPIC
                    : PetasosDestination.DestinationType.QUEUE;
            builder.destination(PetasosDestination.of(destName, type));
        } else if (jmsMessage.getJMSDestination() != null) {
            Destination jmsDest = jmsMessage.getJMSDestination();
            if (jmsDest instanceof Queue q) {
                builder.destination(PetasosDestination.queue(q.getQueueName()));
            } else if (jmsDest instanceof Topic t) {
                builder.destination(PetasosDestination.topic(t.getTopicName()));
            }
        }

        // Extract metadata (all custom properties not starting with petasos_ or _AMQ_)
        Map<String, Object> metadata = new LinkedHashMap<>();
        Enumeration<?> propertyNames = jmsMessage.getPropertyNames();
        if (propertyNames != null) {
            while (propertyNames.hasMoreElements()) {
                String name = propertyNames.nextElement().toString();
                if (!name.startsWith("petasos_") && !name.startsWith("_AMQ_") && !name.startsWith("JMS")) {
                    metadata.put(name, jmsMessage.getObjectProperty(name));
                }
            }
        }
        builder.metadata(metadata);

        return builder.build();
    }

    private static byte[] extractPayload(jakarta.jms.Message message) throws JMSException {
        if (message instanceof BytesMessage bytesMessage) {
            byte[] bytes = new byte[(int) bytesMessage.getBodyLength()];
            bytesMessage.readBytes(bytes);
            return bytes;
        } else if (message instanceof TextMessage textMessage) {
            String text = textMessage.getText();
            return text != null ? text.getBytes(java.nio.charset.StandardCharsets.UTF_8) : new byte[0];
        } else if (message instanceof ObjectMessage objectMessage) {
            try {
                java.io.Serializable obj = objectMessage.getObject();
                if (obj instanceof byte[] b) {
                    return b;
                } else if (obj != null) {
                    return obj.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
                }
            } catch (Exception e) {
                throw new PetasosMessagingException("Failed to read ObjectMessage payload: " + e.getMessage(), e);
            }
        }
        return new byte[0];
    }
}
