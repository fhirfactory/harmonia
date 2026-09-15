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

package net.fhirfactory.harmonia.mllpout.integration;

import net.fhirfactory.harmonia.mllpgateway.config.MllpDestinationConfig;
import net.fhirfactory.harmonia.mllpgateway.config.MllpDestinationRegistry;
import net.fhirfactory.harmonia.mllpgateway.model.OutboundMllpRequest;
import net.fhirfactory.harmonia.mllpgateway.model.OutboundMllpResponse;
import net.fhirfactory.harmonia.mllpout.camel.Hl7AckProcessor;
import net.fhirfactory.harmonia.mllpout.camel.OutboundMllpProcessor;
import net.fhirfactory.harmonia.mllpout.camel.OutboundMllpRouteBuilder;
import net.fhirfactory.harmonia.mllpout.config.MllpOutboundConfig;
import net.fhirfactory.harmonia.mllpout.consumer.OutboundTaskQueueConsumer;
import net.fhirfactory.harmonia.mllpout.lifecycle.OutboundStateLifecycleManager;
import net.fhirfactory.harmonia.mllpout.test.MockMllpServer;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OutboundMllpIntegrationTest {

    private MockMllpServer mockHisServer;
    private MockMllpServer mockLisServer;
    private CamelContext camelContext;
    private MllpDestinationRegistry destinationRegistry;
    private OutboundTaskQueueConsumer consumer;

    @BeforeEach
    void setUp() throws Exception {
        mockHisServer = new MockMllpServer();
        mockLisServer = new MockMllpServer();

        destinationRegistry = new MllpDestinationRegistry();

        MllpDestinationConfig hisConfig = new MllpDestinationConfig("HIS_NORTH", "Hospital North", "127.0.0.1", mockHisServer.getPort());
        MllpDestinationConfig lisConfig = new MllpDestinationConfig("LIS_MAIN", "Central Lab", "127.0.0.1", mockLisServer.getPort());

        destinationRegistry.registerDestination(hisConfig);
        destinationRegistry.registerDestination(lisConfig);

        OutboundMllpProcessor outboundProcessor = new OutboundMllpProcessor(destinationRegistry);
        Hl7AckProcessor ackProcessor = new Hl7AckProcessor();
        OutboundMllpRouteBuilder routeBuilder = new OutboundMllpRouteBuilder(outboundProcessor, ackProcessor);

        camelContext = new DefaultCamelContext();
        camelContext.addRoutes(routeBuilder);
        camelContext.start();

        MllpOutboundConfig outboundConfig = new MllpOutboundConfig("mllp-sender-main", null);
        consumer = new OutboundTaskQueueConsumer(outboundConfig, null, camelContext);
        consumer.setLifecycleManager(new OutboundStateLifecycleManager());
    }

    @AfterEach
    void tearDown() throws Exception {
        if (consumer != null) {
            consumer.stop();
        }
        if (camelContext != null) {
            camelContext.stop();
        }
        if (mockHisServer != null) {
            mockHisServer.close();
        }
        if (mockLisServer != null) {
            mockLisServer.close();
        }
    }

    @Test
    void testDispatchToHisSuccess() {
        Topic topic = Topic.forEgress("ADT", "A01", "harmonia", "mllp-out", "HIS_NORTH");
        String hl7 = "MSH|^~\\&|HARMONIA|HIE|HIS_SYS|HOSP_N|20260915083000||ADT^A01|MSG-INTEG-001|P|2.4\r" +
                "PID|1||MRN12345^^^HOSPITAL^MR||SMITH^JOHN^^^^||19800101|M\r" +
                "PV1|1|I|WARD1^RM1^BED1\r";
        OutboundMllpRequest request = new OutboundMllpRequest("MSG-INTEG-001", "HIS_NORTH", hl7, topic);

        OutboundMllpResponse response = consumer.processOutboundRequest(request);

        assertThat(response).isNotNull();
        assertThat(response.isSuccessful()).isTrue();
        assertThat(response.getAckCode()).isEqualTo("AA");
        assertThat(response.getMessageControlId()).isEqualTo("MSG-INTEG-001");
        assertThat(response.getDestinationId()).isEqualTo("HIS_NORTH");
        assertThat(mockHisServer.getReceivedCount()).isEqualTo(1);
        assertThat(mockLisServer.getReceivedCount()).isEqualTo(0);
    }

    @Test
    void testDispatchToLisWithApplicationErrorNack() {
        mockLisServer.setAckCode("AE");
        mockLisServer.setAckTextMessage("Order number not found");

        Topic topic = Topic.forEgress("ORU", "R01", "harmonia", "mllp-out", "LIS_MAIN");
        String hl7 = "MSH|^~\\&|HARMONIA|HIE|LIS_SYS|LAB|20260915083000||ORU^R01|MSG-INTEG-002|P|2.4\r";
        OutboundMllpRequest request = new OutboundMllpRequest("MSG-INTEG-002", "LIS_MAIN", hl7, topic);

        OutboundMllpResponse response = consumer.processOutboundRequest(request);

        assertThat(response).isNotNull();
        assertThat(response.isSuccessful()).isFalse();
        assertThat(response.getAckCode()).isEqualTo("AE");
        assertThat(response.getErrorMessage()).contains("Order number not found");
        assertThat(mockLisServer.getReceivedCount()).isGreaterThanOrEqualTo(1);
    }
}
