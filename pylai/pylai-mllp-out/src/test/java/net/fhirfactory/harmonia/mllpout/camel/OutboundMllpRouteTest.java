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

package net.fhirfactory.harmonia.mllpout.camel;

import net.fhirfactory.harmonia.mllpgateway.config.MllpDestinationConfig;
import net.fhirfactory.harmonia.mllpgateway.config.MllpDestinationRegistry;
import net.fhirfactory.harmonia.mllpgateway.model.OutboundMllpRequest;
import net.fhirfactory.harmonia.mllpgateway.model.OutboundMllpResponse;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OutboundMllpRouteTest {

    private CamelContext camelContext;
    private ProducerTemplate producerTemplate;
    private MllpDestinationRegistry registry;

    @BeforeEach
    void setUp() throws Exception {
        registry = new MllpDestinationRegistry();
        // Register an unreachable destination for error handling test
        MllpDestinationConfig unreachable = new MllpDestinationConfig("UNREACHABLE_DEST", "127.0.0.1", 64532);
        unreachable.setConnectTimeoutMs(200);
        unreachable.setReadTimeoutMs(200);
        registry.registerDestination(unreachable);

        OutboundMllpProcessor outboundProcessor = new OutboundMllpProcessor(registry);
        Hl7AckProcessor ackProcessor = new Hl7AckProcessor();
        OutboundMllpRouteBuilder routeBuilder = new OutboundMllpRouteBuilder(outboundProcessor, ackProcessor);

        camelContext = new DefaultCamelContext();
        camelContext.addRoutes(routeBuilder);
        camelContext.start();

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
    void testRouteUnreachableDestinationGracefulFailure() {
        Topic topic = Topic.forEgress("ADT", "A01", "harmonia", "mllp-out", "UNREACHABLE_DEST");
        String hl7 = "MSH|^~\\&|HARMONIA|HIE|REC_SYS|REC_FAC|20260915083000||ADT^A01|MSG-TEST-001|P|2.4\rEVN|A01|20260915083000\r";
        OutboundMllpRequest request = new OutboundMllpRequest("MSG-TEST-001", "UNREACHABLE_DEST", hl7, topic);

        OutboundMllpResponse response = producerTemplate.requestBody("direct:mllp-outbound-send", request, OutboundMllpResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.isSuccessful()).isFalse();
        assertThat(response.getMessageControlId()).isEqualTo("MSG-TEST-001");
        assertThat(response.getDestinationId()).isEqualTo("UNREACHABLE_DEST");
        assertThat(response.getErrorMessage()).isNotBlank();
    }
}
