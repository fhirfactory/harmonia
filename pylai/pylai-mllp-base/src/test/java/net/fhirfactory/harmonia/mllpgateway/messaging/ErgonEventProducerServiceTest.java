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

package net.fhirfactory.harmonia.mllpgateway.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.*;
import net.fhirfactory.harmonia.mllpgateway.config.TaskProcessorConfig;
import net.fhirfactory.harmonia.model.ergon.ErgonEvent;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ErgonEventProducerServiceTest {

    private TaskProcessorConfig config;
    private ConnectionFactory connectionFactory;
    private JMSContext jmsContext;
    private JMSProducer jmsProducer;
    private Queue queue;
    private TextMessage textMessage;
    private TaskEventProducerService producerService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        config = new TaskProcessorConfig();
        connectionFactory = mock(ConnectionFactory.class);
        jmsContext = mock(JMSContext.class);
        jmsProducer = mock(JMSProducer.class);
        queue = mock(Queue.class);
        textMessage = mock(TextMessage.class);
        objectMapper = new ObjectMapper();

        when(connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE)).thenReturn(jmsContext);
        when(jmsContext.createQueue(anyString())).thenReturn(queue);
        when(jmsContext.createTextMessage(anyString())).thenReturn(textMessage);
        when(jmsContext.createProducer()).thenReturn(jmsProducer);

        producerService = new TaskEventProducerService(config, connectionFactory);
    }

    @Test
    void testSendTaskEventSuccess() throws Exception {
        ErgonEvent event = new ErgonEvent("TASK-12345", "PROCESS", "REQUESTED", "pas-gateway",
                "ADT", "A01", "MSG-001", "mllp-gateway", "ADT A01 Received");
        producerService.sendTaskEvent(event);

        verify(connectionFactory).createContext(Session.AUTO_ACKNOWLEDGE);
        verify(jmsContext).createQueue("task.event.queue.pas-gateway");

        ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
        verify(jmsContext).createTextMessage(payloadCaptor.capture());
        verify(textMessage).setStringProperty(TaskEventProducerService.HEADER_GATEWAY_INSTANCE, "pas-gateway");
        verify(textMessage).setStringProperty(TaskEventProducerService.HEADER_TRIGGER_TYPE, "A01");
        verify(textMessage).setStringProperty(TaskEventProducerService.HEADER_MESSAGE_TYPE, "ADT");
        verify(textMessage).setStringProperty(TaskEventProducerService.HEADER_TASK_ID, "TASK-12345");
        verify(textMessage).setStringProperty(TaskEventProducerService.HEADER_CONTROL_ID, "MSG-001");
        verify(textMessage).setStringProperty(TaskEventProducerService.HEADER_ACTION, "PROCESS");
        verify(textMessage).setStringProperty(TaskEventProducerService.HEADER_STATUS, "REQUESTED");
        verify(jmsProducer).send(queue, textMessage);

        String sentJson = payloadCaptor.getValue();
        assertThat(sentJson).contains("TASK-12345");
        assertThat(sentJson).contains("PROCESS");
        assertThat(sentJson).contains("REQUESTED");
        assertThat(sentJson).contains("pas-gateway");
        assertThat(sentJson).contains("A01");

        ErgonEvent deserialized = objectMapper.readValue(sentJson, ErgonEvent.class);
        assertThat(deserialized.getTaskId()).isEqualTo("TASK-12345");
        assertThat(deserialized.getAction()).isEqualTo("PROCESS");
        assertThat(deserialized.getStatus()).isEqualTo("REQUESTED");
        assertThat(deserialized.getGatewayInstanceId()).isEqualTo("pas-gateway");
        assertThat(deserialized.getTriggerType()).isEqualTo("A01");
    }

    @Test
    void testSendTaskEventDefaultQueue() throws Exception {
        ErgonEvent event = new ErgonEvent("TASK-DEFAULT", "PROCESS", "REQUESTED");
        producerService.sendTaskEvent(event);

        verify(jmsContext).createQueue("task.event.queue.mllp-gateway-default");
        verify(jmsProducer).send(queue, textMessage);
    }

    @Test
    void testSendTaskEventWithTopic() throws Exception {
        Topic topic = Topic.fromHl7("ADT", "A08", "pas-gw");
        producerService.sendTaskEvent("TASK-TOPIC-01", "PROCESS", "REQUESTED", topic, "MSG-008", "Topic Test");

        verify(jmsContext).createQueue("task.event.queue.pas-gw");
        verify(textMessage).setStringProperty(TaskEventProducerService.HEADER_GATEWAY_INSTANCE, "pas-gw");
        verify(textMessage).setStringProperty(TaskEventProducerService.HEADER_TRIGGER_TYPE, "A08");
        verify(textMessage).setStringProperty(TaskEventProducerService.HEADER_MESSAGE_TYPE, "ADT");
        verify(textMessage).setStringProperty(TaskEventProducerService.HEADER_TOPIC_DATA_ELEMENT, "ADT");
        verify(textMessage).setStringProperty(TaskEventProducerService.HEADER_TOPIC_QUALIFIER, "A08");
        verify(jmsProducer).send(queue, textMessage);
    }

    @Test
    void testSendTaskEventWithConvenienceMethod() throws Exception {
        producerService.sendTaskEvent("TASK-CONV-01", "PROCESS", "INPROGRESS", "mllp-gateway", "Convenience test");

        ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
        verify(jmsContext).createTextMessage(payloadCaptor.capture());
        verify(jmsProducer).send(queue, textMessage);

        String json = payloadCaptor.getValue();
        assertThat(json).contains("TASK-CONV-01");
        assertThat(json).contains("INPROGRESS");
    }

    @Test
    void testSendTaskEventWithFullMetadata() throws Exception {
        producerService.sendTaskEvent("TASK-FULL-01", "PROCESS", "REQUESTED", "lims-gw",
                "ORU", "R01", "MSG-R01-99", "mllp-gateway", "Lab results");

        verify(jmsContext).createQueue("task.event.queue.lims-gw");
        verify(textMessage).setStringProperty(TaskEventProducerService.HEADER_GATEWAY_INSTANCE, "lims-gw");
        verify(textMessage).setStringProperty(TaskEventProducerService.HEADER_TRIGGER_TYPE, "R01");
        verify(textMessage).setStringProperty(TaskEventProducerService.HEADER_MESSAGE_TYPE, "ORU");
        verify(jmsProducer).send(queue, textMessage);
    }

    @Test
    void testSendTaskEventNullThrowsException() {
        assertThatThrownBy(() -> producerService.sendTaskEvent(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testSendTaskEventJsonBlankThrowsException() {
        assertThatThrownBy(() -> producerService.sendTaskEventJson(""))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> producerService.sendTaskEventJson(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
