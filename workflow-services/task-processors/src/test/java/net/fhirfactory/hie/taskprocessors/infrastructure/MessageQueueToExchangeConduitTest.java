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

package net.fhirfactory.hie.taskprocessors.infrastructure;

import net.fhirfactory.hie.model.TaskEvent;
import net.fhirfactory.hie.taskprocessors.base.TaskProcessingActivity;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class MessageQueueToExchangeConduitTest {

    private CamelContext camelContext;
    private ProducerTemplate producerTemplate;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        producerTemplate = camelContext.createProducerTemplate();
    }

    @AfterEach
    void tearDown() throws Exception {
        if (producerTemplate != null) {
            producerTemplate.stop();
        }
        if (camelContext != null) {
            camelContext.stop();
        }
    }

    @Test
    @DisplayName("Test MessageQueueToExchangeConduit properties and inheritance")
    void testPropertiesAndInheritance() {
        MessageQueueToExchangeConduit conduit = new MessageQueueToExchangeConduit();
        assertThat(conduit).isInstanceOf(TaskProcessingActivity.class);
        assertThat(conduit.getActivityId()).isEqualTo(MessageQueueToExchangeConduit.DEFAULT_ACTIVITY_ID);
        assertThat(conduit.getActivityName()).isEqualTo(MessageQueueToExchangeConduit.DEFAULT_ACTIVITY_NAME);
        assertThat(conduit.getQueueName()).isEqualTo(MessageQueueToExchangeConduit.DEFAULT_QUEUE_NAME);
        assertThat(conduit.resolveInputEndpoint()).isEqualTo("jms:queue:task.event.queue");

        conduit.setQueueName("task.event.queue.pas-gw");
        assertThat(conduit.resolveInputEndpoint()).isEqualTo("jms:queue:task.event.queue.pas-gw");

        conduit.setInputEndpoint("direct:custom-in");
        assertThat(conduit.resolveInputEndpoint()).isEqualTo("direct:custom-in");
    }

    @Test
    @DisplayName("Test MessageQueueToExchangeConduit constructors")
    void testConstructors() {
        MessageQueueToExchangeConduit ctxConduit = new MessageQueueToExchangeConduit(camelContext);
        assertThat(ctxConduit.getCamelContext()).isEqualTo(camelContext);

        MessageQueueToExchangeConduit customConduit = new MessageQueueToExchangeConduit("custom-conduit", "Custom Conduit Name");
        assertThat(customConduit.getActivityId()).isEqualTo("custom-conduit");
        assertThat(customConduit.getActivityName()).isEqualTo("Custom Conduit Name");

        MessageQueueToExchangeConduit queueConduit = new MessageQueueToExchangeConduit("queue-conduit", "Queue Conduit", "my.custom.queue");
        assertThat(queueConduit.getQueueName()).isEqualTo("my.custom.queue");
        assertThat(queueConduit.resolveInputEndpoint()).isEqualTo("jms:queue:my.custom.queue");
    }

    @Test
    @DisplayName("Test processing TaskEvent object into Camel Exchange headers")
    void testProcessTaskEventObject() throws Exception {
        MessageQueueToExchangeConduit conduit = new MessageQueueToExchangeConduit();
        conduit.setInputEndpoint("direct:conduit-taskevent-in");
        conduit.setOutputEndpoint("mock:conduit-taskevent-out");

        camelContext.addRoutes(conduit);
        camelContext.start();

        MockEndpoint mockOut = camelContext.getEndpoint("mock:conduit-taskevent-out", MockEndpoint.class);
        mockOut.expectedMessageCount(1);

        TaskEvent taskEvent = new TaskEvent();
        taskEvent.setTaskId("TASK-98765");
        taskEvent.setGatewayInstanceId("pas-gw");
        taskEvent.setTriggerType("ADT^A01");
        taskEvent.setMessageType("ADT");
        taskEvent.setControlId("MSG-12345");
        taskEvent.setAction("PROCESS");
        taskEvent.setStatus("REQUESTED");

        producerTemplate.sendBody("direct:conduit-taskevent-in", taskEvent);

        mockOut.assertIsSatisfied();
        Exchange resultExchange = mockOut.getExchanges().get(0);

        assertThat(resultExchange.getMessage().getHeader(MessageQueueToExchangeConduit.HEADER_TASK_ID)).isEqualTo("TASK-98765");
        assertThat(resultExchange.getMessage().getHeader(MessageQueueToExchangeConduit.HEADER_GATEWAY_INSTANCE)).isEqualTo("pas-gw");
        assertThat(resultExchange.getMessage().getHeader(MessageQueueToExchangeConduit.HEADER_TRIGGER_TYPE)).isEqualTo("ADT^A01");
        assertThat(resultExchange.getMessage().getHeader(MessageQueueToExchangeConduit.HEADER_MESSAGE_TYPE)).isEqualTo("ADT");
        assertThat(resultExchange.getMessage().getHeader(MessageQueueToExchangeConduit.HEADER_CONTROL_ID)).isEqualTo("MSG-12345");
        assertThat(resultExchange.getMessage().getHeader(MessageQueueToExchangeConduit.HEADER_ACTION)).isEqualTo("PROCESS");
        assertThat(resultExchange.getMessage().getHeader(MessageQueueToExchangeConduit.HEADER_STATUS)).isEqualTo("REQUESTED");
        assertThat(resultExchange.getMessage().getHeader(MessageQueueToExchangeConduit.HEADER_RAW_MESSAGE)).isNotNull();
    }

    @Test
    @DisplayName("Test processing JSON TaskEvent payload into Camel Exchange headers")
    void testProcessJsonStringPayload() throws Exception {
        MessageQueueToExchangeConduit conduit = new MessageQueueToExchangeConduit();
        conduit.setInputEndpoint("direct:conduit-json-in");
        conduit.setOutputEndpoint("mock:conduit-json-out");

        camelContext.addRoutes(conduit);
        camelContext.start();

        MockEndpoint mockOut = camelContext.getEndpoint("mock:conduit-json-out", MockEndpoint.class);
        mockOut.expectedMessageCount(1);

        String jsonEvent = """
        {
          "taskId": "TASK-JSON-001",
          "gatewayInstanceId": "lims-gw",
          "triggerType": "ORU^R01",
          "messageType": "ORU",
          "controlId": "CTRL-999",
          "action": "PROCESS",
          "status": "IN_PROGRESS"
        }
        """;

        producerTemplate.sendBody("direct:conduit-json-in", jsonEvent);

        mockOut.assertIsSatisfied();
        Exchange resultExchange = mockOut.getExchanges().get(0);

        assertThat(resultExchange.getMessage().getHeader(MessageQueueToExchangeConduit.HEADER_TASK_ID)).isEqualTo("TASK-JSON-001");
        assertThat(resultExchange.getMessage().getHeader(MessageQueueToExchangeConduit.HEADER_GATEWAY_INSTANCE)).isEqualTo("lims-gw");
        assertThat(resultExchange.getMessage().getHeader(MessageQueueToExchangeConduit.HEADER_TRIGGER_TYPE)).isEqualTo("ORU^R01");
        assertThat(resultExchange.getMessage().getHeader(MessageQueueToExchangeConduit.HEADER_MESSAGE_TYPE)).isEqualTo("ORU");
        assertThat(resultExchange.getMessage().getHeader(MessageQueueToExchangeConduit.HEADER_CONTROL_ID)).isEqualTo("CTRL-999");
        assertThat(resultExchange.getMessage().getHeader(MessageQueueToExchangeConduit.HEADER_ACTION)).isEqualTo("PROCESS");
        assertThat(resultExchange.getMessage().getHeader(MessageQueueToExchangeConduit.HEADER_STATUS)).isEqualTo("IN_PROGRESS");
        assertThat(resultExchange.getMessage().getHeader(MessageQueueToExchangeConduit.HEADER_RAW_MESSAGE)).isEqualTo(jsonEvent);
    }

    @Test
    @DisplayName("Test processing byte array payload into Camel Exchange headers")
    void testProcessByteArrayPayload() throws Exception {
        MessageQueueToExchangeConduit conduit = new MessageQueueToExchangeConduit();
        conduit.setInputEndpoint("direct:conduit-bytes-in");
        conduit.setOutputEndpoint("mock:conduit-bytes-out");

        camelContext.addRoutes(conduit);
        camelContext.start();

        MockEndpoint mockOut = camelContext.getEndpoint("mock:conduit-bytes-out", MockEndpoint.class);
        mockOut.expectedMessageCount(1);

        String jsonEvent = "{\"taskId\":\"TASK-BYTES-002\",\"gatewayInstanceId\":\"pas-gw\",\"triggerType\":\"ADT^A08\"}";
        byte[] bytes = jsonEvent.getBytes(StandardCharsets.UTF_8);

        producerTemplate.sendBody("direct:conduit-bytes-in", bytes);

        mockOut.assertIsSatisfied();
        Exchange resultExchange = mockOut.getExchanges().get(0);

        assertThat(resultExchange.getMessage().getHeader(MessageQueueToExchangeConduit.HEADER_TASK_ID)).isEqualTo("TASK-BYTES-002");
        assertThat(resultExchange.getMessage().getHeader(MessageQueueToExchangeConduit.HEADER_GATEWAY_INSTANCE)).isEqualTo("pas-gw");
        assertThat(resultExchange.getMessage().getHeader(MessageQueueToExchangeConduit.HEADER_TRIGGER_TYPE)).isEqualTo("ADT^A08");
        assertThat(resultExchange.getMessage().getBody(String.class)).isEqualTo(jsonEvent);
    }
}
