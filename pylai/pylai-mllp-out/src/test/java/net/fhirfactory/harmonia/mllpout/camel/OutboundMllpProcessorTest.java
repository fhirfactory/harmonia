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
import net.fhirfactory.harmonia.model.topic.Topic;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OutboundMllpProcessorTest {

    private OutboundMllpProcessor processor;
    private MllpDestinationRegistry registry;
    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        registry = new MllpDestinationRegistry();
        MllpDestinationConfig his = new MllpDestinationConfig("HIS_NORTH", "Hospital North", "10.0.1.50", 2575);
        registry.registerDestination(his);

        processor = new OutboundMllpProcessor(registry);
        camelContext = new DefaultCamelContext();
    }

    @Test
    void testProcessOutboundMllpRequestObject() throws Exception {
        Topic topic = Topic.forEgress("ADT", "A01", "harmonia", "mllp-out", "HIS_NORTH");
        String hl7 = "MSH|^~\\&|HARMONIA|HIE|HIS_SYS|HOSP_N|20260915083000||ADT^A01|MSG-9901|P|2.4\rEVN|A01|20260915083000\r";
        OutboundMllpRequest request = new OutboundMllpRequest("MSG-9901", "HIS_NORTH", hl7, topic);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(request);

        processor.process(exchange);

        assertThat(exchange.getMessage().getHeader("HIE_DEST_HOST")).isEqualTo("10.0.1.50");
        assertThat(exchange.getMessage().getHeader("HIE_DEST_PORT")).isEqualTo(2575);
        assertThat(exchange.getMessage().getHeader("HIE_MESSAGE_CONTROL_ID")).isEqualTo("MSG-9901");
        assertThat(exchange.getMessage().getHeader("HIE_DESTINATION_ID")).isEqualTo("HIS_NORTH");
        assertThat(exchange.getMessage().getHeader("HIE_TARGET_QUEUE")).isEqualTo("petasos.queue.mllp.outbound.his_north");
        assertThat(exchange.getMessage().getBody(String.class)).isEqualTo(hl7);
    }

    @Test
    void testExtractControlIdAndDefaultDestination() throws Exception {
        String hl7 = "MSH|^~\\&|HARMONIA|HIE|HIS_SYS|HOSP_N|20260915083000||ADT^A08|CONTROL_12345|P|2.4\rEVN|A08|20260915083000\r";

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(hl7);

        processor.process(exchange);

        assertThat(exchange.getMessage().getHeader("HIE_MESSAGE_CONTROL_ID")).isEqualTo("CONTROL_12345");
        assertThat(exchange.getMessage().getHeader("HIE_DESTINATION_ID")).isEqualTo("HIS_NORTH");
        assertThat(exchange.getMessage().getHeader("HIE_DEST_HOST")).isEqualTo("10.0.1.50");
    }
}
