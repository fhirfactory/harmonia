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

package net.fhirfactory.harmonia.mllpout.consumer;

import jakarta.jms.TextMessage;
import net.fhirfactory.harmonia.mllpgateway.model.OutboundMllpRequest;
import net.fhirfactory.harmonia.mllpgateway.model.OutboundMllpResponse;
import net.fhirfactory.harmonia.mllpout.config.MllpOutboundConfig;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OutboundTaskQueueConsumerTest {

    @Mock
    private ProducerTemplate producerTemplate;

    @Mock
    private TextMessage textMessage;

    private MllpOutboundConfig config;
    private OutboundTaskQueueConsumer consumer;

    @BeforeEach
    void setUp() {
        config = new MllpOutboundConfig("mllp-sender-lis", "LIS_MAIN");
        consumer = new OutboundTaskQueueConsumer(config, null, null);
        consumer.setProducerTemplate(producerTemplate);
    }

    @Test
    void testProcessOutboundRequest() {
        OutboundMllpRequest request = new OutboundMllpRequest("MSG-001", "LIS_MAIN", "MSH|...", null);
        OutboundMllpResponse mockResponse = OutboundMllpResponse.success("MSG-001", "AA", "MSA|AA|MSG-001", 25L);

        when(producerTemplate.requestBody(eq("direct:mllp-outbound-send"), any(OutboundMllpRequest.class), eq(OutboundMllpResponse.class)))
                .thenReturn(mockResponse);

        OutboundMllpResponse response = consumer.processOutboundRequest(request);

        assertThat(response).isNotNull();
        assertThat(response.isSuccessful()).isTrue();
        assertThat(response.getMessageControlId()).isEqualTo("MSG-001");
    }

    @Test
    void testOnMessageTextMessage() throws Exception {
        String hl7 = "MSH|^~\\&|HARMONIA|HIE|LIS_SYS|LAB|20260915083000||ORU^R01|MSG-ORU-1|P|2.4\r";
        when(textMessage.getText()).thenReturn(hl7);

        OutboundMllpResponse mockResponse = OutboundMllpResponse.success("MSG-ORU-1", "AA", "MSA|AA|MSG-ORU-1", 15L);
        when(producerTemplate.requestBody(eq("direct:mllp-outbound-send"), any(OutboundMllpRequest.class), eq(OutboundMllpResponse.class)))
                .thenReturn(mockResponse);

        consumer.onMessage(textMessage);

        verify(producerTemplate).requestBody(eq("direct:mllp-outbound-send"), any(OutboundMllpRequest.class), eq(OutboundMllpResponse.class));
    }
}
