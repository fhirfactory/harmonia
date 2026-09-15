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

package net.fhirfactory.harmonia.mllpout.rest;

import jakarta.ws.rs.core.Response;
import net.fhirfactory.harmonia.mllpgateway.config.MllpDestinationConfig;
import net.fhirfactory.harmonia.mllpgateway.config.MllpDestinationRegistry;
import net.fhirfactory.harmonia.mllpgateway.model.OutboundMllpRequest;
import net.fhirfactory.harmonia.mllpgateway.model.OutboundMllpResponse;
import net.fhirfactory.harmonia.mllpout.config.MllpOutboundConfig;
import net.fhirfactory.harmonia.mllpout.consumer.OutboundTaskQueueConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OutboundMllpResourceTest {

    @Mock
    private OutboundTaskQueueConsumer consumer;

    private MllpDestinationRegistry destinationRegistry;
    private MllpOutboundConfig outboundConfig;
    private OutboundMllpResource resource;

    @BeforeEach
    void setUp() {
        destinationRegistry = new MllpDestinationRegistry();
        outboundConfig = new MllpOutboundConfig("mllp-sender-his", "HIS_NORTH");
        resource = new OutboundMllpResource(consumer, destinationRegistry, outboundConfig);
    }

    @Test
    void testSendSuccess() {
        OutboundMllpRequest request = new OutboundMllpRequest("MSG-01", "HIS_NORTH", "MSH|...", null);
        OutboundMllpResponse mockResp = OutboundMllpResponse.success("MSG-01", "AA", "MSA|AA|MSG-01", 30L);

        when(consumer.processOutboundRequest(any(OutboundMllpRequest.class))).thenReturn(mockResp);

        Response response = resource.send(request);
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getEntity()).isEqualTo(mockResp);
    }

    @Test
    void testSendBadRequest() {
        Response response = resource.send(new OutboundMllpRequest());
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    void testHealthEndpoint() {
        Response response = resource.health();
        assertThat(response.getStatus()).isEqualTo(200);
        Map<?, ?> body = (Map<?, ?>) response.getEntity();
        assertThat(body.get("status")).isEqualTo("UP");
        assertThat(body.get("instanceId")).isEqualTo("mllp-sender-his");
        assertThat(body.get("dedicatedQueue")).isEqualTo("petasos.queue.mllp.outbound.his_north");
    }

    @Test
    void testDestinationsCrud() {
        MllpDestinationConfig dest = new MllpDestinationConfig("LIS_MAIN", "10.0.0.1", 2576);
        Response created = resource.registerDestination(dest);
        assertThat(created.getStatus()).isEqualTo(201);

        Response listResp = resource.getDestinations();
        assertThat(listResp.getStatus()).isEqualTo(200);
        List<?> list = (List<?>) listResp.getEntity();
        assertThat(list).hasSize(1);

        Response getResp = resource.getDestinationById("LIS_MAIN");
        assertThat(getResp.getStatus()).isEqualTo(200);

        Response delResp = resource.deleteDestination("LIS_MAIN");
        assertThat(delResp.getStatus()).isEqualTo(200);
    }
}
