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
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ArtemisMessageConverterTest {

    @Test
    void testToJmsAndBack() throws Exception {
        Session session = mock(Session.class);
        BytesMessage bytesMessage = mock(BytesMessage.class);
        when(session.createBytesMessage()).thenReturn(bytesMessage);

        Map<String, Object> properties = new HashMap<>();
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

        doAnswer(invocation -> {
            byte[] bytes = invocation.getArgument(0);
            byteOut.write(bytes);
            return null;
        }).when(bytesMessage).writeBytes(any(byte[].class));

        doAnswer(invocation -> {
            String key = invocation.getArgument(0);
            Object val = invocation.getArgument(1);
            properties.put(key, val);
            return null;
        }).when(bytesMessage).setStringProperty(anyString(), any());

        doAnswer(invocation -> {
            String key = invocation.getArgument(0);
            int val = invocation.getArgument(1);
            properties.put(key, val);
            return null;
        }).when(bytesMessage).setIntProperty(anyString(), anyInt());

        doAnswer(invocation -> {
            String key = invocation.getArgument(0);
            long val = invocation.getArgument(1);
            properties.put(key, val);
            return null;
        }).when(bytesMessage).setLongProperty(anyString(), anyLong());

        doAnswer(invocation -> {
            String key = invocation.getArgument(0);
            boolean val = invocation.getArgument(1);
            properties.put(key, val);
            return null;
        }).when(bytesMessage).setBooleanProperty(anyString(), anyBoolean());

        Instant now = Instant.now();
        ThemisPrincipal principal = ThemisPrincipal.of("dr.mark", PrincipalType.HUMAN, "CLINICAL");
        ThemisSecurityContext secCtx = ThemisSecurityContext.builder()
                .originatingPrincipal(principal)
                .securityDomain("CLINICAL")
                .correlationId("corr-888")
                .build();

        PetasosMessage original = PetasosMessage.builder()
                .messageId("msg-999")
                .correlationId("corr-888")
                .causationId("cause-777")
                .messageType("ObservationEvent")
                .source("lab-analyzer-1")
                .destination(PetasosDestination.queue("observation.queue"))
                .timestamp(now)
                .contentType("application/fhir+json")
                .schema("Observation", "5.0.0")
                .payload("{\"resourceType\":\"Observation\",\"id\":\"obs-1\"}")
                .header("labCode", "LAB-01")
                .securityContext(secCtx)
                .durable(true)
                .priority(8)
                .duplicateDetectionId("dedup-999")
                .build();

        jakarta.jms.Message jmsMessage = ArtemisMessageConverter.toJmsMessage(original, session);
        assertThat(jmsMessage).isNotNull();

        // Verify non-authoritative diagnostic headers and operational metadata are set
        assertThat(properties).containsEntry(ArtemisMessageConverter.HDR_HARMONIA_INITIATING_PRINCIPAL, "dr.mark");
        assertThat(properties).containsEntry(ArtemisMessageConverter.HDR_HARMONIA_SECURITY_DOMAIN, "CLINICAL");
        assertThat(properties).containsEntry(ArtemisMessageConverter.HDR_HARMONIA_CORRELATION_ID, "corr-888");
        assertThat(properties).containsEntry(ArtemisMessageConverter.HDR_PETASOS_MESSAGE_ID, "msg-999");
        assertThat(properties).containsEntry(ArtemisMessageConverter.HDR_PETASOS_CORRELATION_ID, "corr-888");
        assertThat(properties).containsEntry(ArtemisMessageConverter.HDR_PETASOS_CAUSATION_ID, "cause-777");
        assertThat(properties).containsEntry(ArtemisMessageConverter.HDR_PETASOS_MESSAGE_TYPE, "ObservationEvent");
        assertThat(properties).containsEntry(ArtemisMessageConverter.HDR_PETASOS_SOURCE, "lab-analyzer-1");
        assertThat(properties).containsEntry(ArtemisMessageConverter.HDR_PETASOS_DESTINATION_NAME, "observation.queue");
        assertThat(properties).containsEntry(ArtemisMessageConverter.HDR_PETASOS_DESTINATION_TYPE, "QUEUE");
        assertThat(properties).containsEntry(ArtemisMessageConverter.HDR_PETASOS_CONTENT_TYPE, "application/fhir+json");
        assertThat(properties).containsEntry(ArtemisMessageConverter.HDR_PETASOS_SCHEMA_ID, "Observation");
        assertThat(properties).containsEntry(ArtemisMessageConverter.HDR_PETASOS_SCHEMA_VER, "5.0.0");
        assertThat(properties).containsEntry(ArtemisMessageConverter.HDR_AMQ_DUPL_ID, "dedup-999");
        assertThat(properties).containsEntry(ArtemisMessageConverter.HDR_PETASOS_DURABLE, true);
        assertThat(properties).containsEntry(ArtemisMessageConverter.HDR_PETASOS_TIMESTAMP, now.toEpochMilli());
        assertThat(properties).containsEntry("labCode", "LAB-01");

        // Prepare mock for reading back
        when(bytesMessage.getBodyLength()).thenReturn((long) byteOut.size());
        doAnswer(invocation -> {
            byte[] target = invocation.getArgument(0);
            System.arraycopy(byteOut.toByteArray(), 0, target, 0, target.length);
            return target.length;
        }).when(bytesMessage).readBytes(any(byte[].class));

        when(bytesMessage.getStringProperty(anyString())).thenAnswer(inv -> properties.get(inv.getArgument(0)));
        when(bytesMessage.getObjectProperty(anyString())).thenAnswer(inv -> properties.get(inv.getArgument(0)));
        when(bytesMessage.propertyExists(anyString())).thenAnswer(inv -> properties.containsKey(inv.getArgument(0)));
        when(bytesMessage.getLongProperty(anyString())).thenAnswer(inv -> ((Number) properties.get(inv.getArgument(0))).longValue());
        when(bytesMessage.getBooleanProperty(anyString())).thenAnswer(inv -> (Boolean) properties.get(inv.getArgument(0)));
        when(bytesMessage.getJMSPriority()).thenReturn(8);
        when(bytesMessage.getJMSDeliveryMode()).thenReturn(DeliveryMode.PERSISTENT);
        when(bytesMessage.getPropertyNames()).thenReturn(Collections.enumeration(properties.keySet()));

        // Convert back
        PetasosMessage converted = ArtemisMessageConverter.toPetasosMessage(jmsMessage);
        assertThat(converted.getMessageId()).isEqualTo("msg-999");
        assertThat(converted.getCorrelationId()).isEqualTo("corr-888");
        assertThat(converted.getCausationId()).isEqualTo("cause-777");
        assertThat(converted.getMessageType()).isEqualTo("ObservationEvent");
        assertThat(converted.getSource()).isEqualTo("lab-analyzer-1");
        assertThat(converted.getDestination()).isEqualTo(PetasosDestination.queue("observation.queue"));
        assertThat(converted.getContentType()).isEqualTo("application/fhir+json");
        assertThat(converted.getSchemaIdentifier()).isEqualTo("Observation");
        assertThat(converted.getSchemaVersion()).isEqualTo("5.0.0");
        assertThat(converted.getPayloadAsString()).isEqualTo("{\"resourceType\":\"Observation\",\"id\":\"obs-1\"}");
        assertThat(converted.getMetadata()).containsEntry("labCode", "LAB-01");
        assertThat(converted.isDurable()).isTrue();
        assertThat(converted.getPriority()).isEqualTo(8);
        assertThat(converted.getDuplicateDetectionId()).isEqualTo("dedup-999");

        // Critical security assertion: reverse conversion MUST NOT reconstruct ThemisPrincipal or ThemisSecurityContext from transport headers
        assertThat(converted.getOriginatingPrincipal()).isNull();
        assertThat(converted.getSecurityContext()).isNull();
    }

    @Test
    void testTamperingJmsPrincipalHeadersCannotManufacturePrincipalOrSecurityContext() throws Exception {
        BytesMessage bytesMessage = mock(BytesMessage.class);
        when(bytesMessage.getBodyLength()).thenReturn(0L);

        Map<String, Object> properties = new HashMap<>();
        properties.put(ArtemisMessageConverter.HDR_PETASOS_MESSAGE_ID, "msg-attacker-1");
        properties.put(ArtemisMessageConverter.HDR_PETASOS_CORRELATION_ID, "corr-attacker-1");
        // Mallory attempts to inject administrative credentials via non-authoritative transport headers
        properties.put(ArtemisMessageConverter.HDR_HARMONIA_INITIATING_PRINCIPAL, "mallory-attacker");
        properties.put(ArtemisMessageConverter.HDR_HARMONIA_SECURITY_DOMAIN, "SUPER_ADMIN");
        properties.put("customAppHeader", "safe-value");

        when(bytesMessage.getStringProperty(anyString())).thenAnswer(inv -> properties.get(inv.getArgument(0)));
        when(bytesMessage.getObjectProperty(anyString())).thenAnswer(inv -> properties.get(inv.getArgument(0)));
        when(bytesMessage.propertyExists(anyString())).thenAnswer(inv -> properties.containsKey(inv.getArgument(0)));
        when(bytesMessage.getPropertyNames()).thenReturn(Collections.enumeration(properties.keySet()));

        PetasosMessage converted = ArtemisMessageConverter.toPetasosMessage(bytesMessage);

        // Assert that untrusted transport headers NEVER manufacture trusted security context or principal
        assertThat(converted.getOriginatingPrincipal()).isNull();
        assertThat(converted.getSecurityContext()).isNull();

        // Assert that harmonia_* transport headers are not leaked into application metadata
        assertThat(converted.getMetadata()).doesNotContainKey(ArtemisMessageConverter.HDR_HARMONIA_INITIATING_PRINCIPAL);
        assertThat(converted.getMetadata()).doesNotContainKey(ArtemisMessageConverter.HDR_HARMONIA_SECURITY_DOMAIN);
        assertThat(converted.getMetadata()).containsEntry("customAppHeader", "safe-value");
    }

    @Test
    void testToJmsWithoutSecurityContext() throws Exception {
        Session session = mock(Session.class);
        BytesMessage bytesMessage = mock(BytesMessage.class);
        when(session.createBytesMessage()).thenReturn(bytesMessage);

        Map<String, Object> properties = new HashMap<>();
        doAnswer(invocation -> {
            properties.put(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(bytesMessage).setStringProperty(anyString(), any());

        PetasosMessage message = PetasosMessage.builder()
                .messageId("msg-anon-1")
                .correlationId("corr-anon-1")
                .payload("anon-data")
                .build();

        ArtemisMessageConverter.toJmsMessage(message, session);

        assertThat(properties).doesNotContainKey(ArtemisMessageConverter.HDR_HARMONIA_INITIATING_PRINCIPAL);
        assertThat(properties).doesNotContainKey(ArtemisMessageConverter.HDR_HARMONIA_SECURITY_DOMAIN);
        assertThat(properties).containsEntry(ArtemisMessageConverter.HDR_HARMONIA_CORRELATION_ID, "corr-anon-1");
    }

    @Test
    void testToJmsWithOriginatingPrincipalOnly() throws Exception {
        Session session = mock(Session.class);
        BytesMessage bytesMessage = mock(BytesMessage.class);
        when(session.createBytesMessage()).thenReturn(bytesMessage);

        Map<String, Object> properties = new HashMap<>();
        doAnswer(invocation -> {
            properties.put(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(bytesMessage).setStringProperty(anyString(), any());

        ThemisPrincipal servicePrincipal = ThemisPrincipal.of("service:pylai-gateway", PrincipalType.SERVICE, "INTEGRATION");

        PetasosMessage message = PetasosMessage.builder()
                .messageId("msg-svc-1")
                .correlationId("corr-svc-1")
                .originatingPrincipal(servicePrincipal)
                .payload("svc-data")
                .build();

        ArtemisMessageConverter.toJmsMessage(message, session);

        assertThat(properties).containsEntry(ArtemisMessageConverter.HDR_HARMONIA_INITIATING_PRINCIPAL, "service:pylai-gateway");
        assertThat(properties).containsEntry(ArtemisMessageConverter.HDR_HARMONIA_SECURITY_DOMAIN, "INTEGRATION");
    }

    @Test
    void testPayloadExtractionFromTextMessageAndObjectMessage() throws Exception {
        // TextMessage
        TextMessage textMessage = mock(TextMessage.class);
        when(textMessage.getText()).thenReturn("Hello Text Payload");
        when(textMessage.getStringProperty(ArtemisMessageConverter.HDR_PETASOS_MESSAGE_ID)).thenReturn("msg-txt-1");

        PetasosMessage fromText = ArtemisMessageConverter.toPetasosMessage(textMessage);
        assertThat(fromText.getPayloadAsString()).isEqualTo("Hello Text Payload");

        // ObjectMessage with byte[]
        ObjectMessage objMessageBytes = mock(ObjectMessage.class);
        byte[] rawBytes = "Object Payload Bytes".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        when(objMessageBytes.getObject()).thenReturn((Serializable) rawBytes);
        when(objMessageBytes.getStringProperty(ArtemisMessageConverter.HDR_PETASOS_MESSAGE_ID)).thenReturn("msg-obj-1");

        PetasosMessage fromObjBytes = ArtemisMessageConverter.toPetasosMessage(objMessageBytes);
        assertThat(fromObjBytes.getPayloadAsString()).isEqualTo("Object Payload Bytes");

        // ObjectMessage with Object string
        ObjectMessage objMessageStr = mock(ObjectMessage.class);
        when(objMessageStr.getObject()).thenReturn("String In Object");
        when(objMessageStr.getStringProperty(ArtemisMessageConverter.HDR_PETASOS_MESSAGE_ID)).thenReturn("msg-obj-2");

        PetasosMessage fromObjStr = ArtemisMessageConverter.toPetasosMessage(objMessageStr);
        assertThat(fromObjStr.getPayloadAsString()).isEqualTo("String In Object");
    }

    @Test
    void testDestinationExtractionTopicAndQueue() throws Exception {
        // Destination by property TOPIC
        BytesMessage jmsMessageTopic = mock(BytesMessage.class);
        when(jmsMessageTopic.getBodyLength()).thenReturn(0L);
        when(jmsMessageTopic.getStringProperty(ArtemisMessageConverter.HDR_PETASOS_DESTINATION_NAME)).thenReturn("events.topic");
        when(jmsMessageTopic.getStringProperty(ArtemisMessageConverter.HDR_PETASOS_DESTINATION_TYPE)).thenReturn("TOPIC");

        PetasosMessage msgTopic = ArtemisMessageConverter.toPetasosMessage(jmsMessageTopic);
        assertThat(msgTopic.getDestination()).isEqualTo(PetasosDestination.topic("events.topic"));

        // Destination fallback by JMS Topic
        BytesMessage jmsFallbackTopic = mock(BytesMessage.class);
        Topic topic = mock(Topic.class);
        when(topic.getTopicName()).thenReturn("fallback.topic");
        when(jmsFallbackTopic.getBodyLength()).thenReturn(0L);
        when(jmsFallbackTopic.getJMSDestination()).thenReturn(topic);

        PetasosMessage msgFallbackTopic = ArtemisMessageConverter.toPetasosMessage(jmsFallbackTopic);
        assertThat(msgFallbackTopic.getDestination()).isEqualTo(PetasosDestination.topic("fallback.topic"));

        // Destination fallback by JMS Queue
        BytesMessage jmsFallbackQueue = mock(BytesMessage.class);
        jakarta.jms.Queue queue = mock(jakarta.jms.Queue.class);
        when(queue.getQueueName()).thenReturn("fallback.queue");
        when(jmsFallbackQueue.getBodyLength()).thenReturn(0L);
        when(jmsFallbackQueue.getJMSDestination()).thenReturn(queue);

        PetasosMessage msgFallbackQueue = ArtemisMessageConverter.toPetasosMessage(jmsFallbackQueue);
        assertThat(msgFallbackQueue.getDestination()).isEqualTo(PetasosDestination.queue("fallback.queue"));
    }
}
