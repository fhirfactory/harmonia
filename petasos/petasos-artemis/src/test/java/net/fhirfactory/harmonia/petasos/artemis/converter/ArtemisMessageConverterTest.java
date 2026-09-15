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

import jakarta.jms.BytesMessage;
import jakarta.jms.DeliveryMode;
import jakarta.jms.Session;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
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
                .durable(true)
                .priority(8)
                .duplicateDetectionId("dedup-999")
                .build();

        jakarta.jms.Message jmsMessage = ArtemisMessageConverter.toJmsMessage(original, session);
        assertThat(jmsMessage).isNotNull();

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
    }
}
