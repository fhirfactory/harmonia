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

class MultiInstanceQueueIsolationTest {

    private MockMllpServer mockLisServer;
    private CamelContext hisCamelContext;
    private CamelContext lisCamelContext;
    private OutboundTaskQueueConsumer hisSenderConsumer;
    private OutboundTaskQueueConsumer lisSenderConsumer;

    @BeforeEach
    void setUp() throws Exception {
        mockLisServer = new MockMllpServer();

        // 1. Setup HIS Gateway Instance with unreachable target (simulating HIS outage)
        MllpDestinationRegistry hisRegistry = new MllpDestinationRegistry();
        MllpDestinationConfig hisConfig = new MllpDestinationConfig("HIS_NORTH", "Hospital North", "127.0.0.1", 64521);
        hisConfig.setConnectTimeoutMs(150);
        hisConfig.setReadTimeoutMs(150);
        hisRegistry.registerDestination(hisConfig);

        hisCamelContext = new DefaultCamelContext();
        hisCamelContext.addRoutes(new OutboundMllpRouteBuilder(new OutboundMllpProcessor(hisRegistry), new Hl7AckProcessor()));
        hisCamelContext.start();

        MllpOutboundConfig hisOutboundConfig = new MllpOutboundConfig("pylai-mllp-out-his", "HIS_NORTH");
        hisSenderConsumer = new OutboundTaskQueueConsumer(hisOutboundConfig, null, hisCamelContext);
        hisSenderConsumer.setLifecycleManager(new OutboundStateLifecycleManager());

        // 2. Setup LIS Gateway Instance with healthy active target
        MllpDestinationRegistry lisRegistry = new MllpDestinationRegistry();
        MllpDestinationConfig lisConfig = new MllpDestinationConfig("LIS_MAIN", "Central Lab", "127.0.0.1", mockLisServer.getPort());
        lisRegistry.registerDestination(lisConfig);

        lisCamelContext = new DefaultCamelContext();
        lisCamelContext.addRoutes(new OutboundMllpRouteBuilder(new OutboundMllpProcessor(lisRegistry), new Hl7AckProcessor()));
        lisCamelContext.start();

        MllpOutboundConfig lisOutboundConfig = new MllpOutboundConfig("pylai-mllp-out-lis", "LIS_MAIN");
        lisSenderConsumer = new OutboundTaskQueueConsumer(lisOutboundConfig, null, lisCamelContext);
        lisSenderConsumer.setLifecycleManager(new OutboundStateLifecycleManager());
    }

    @AfterEach
    void tearDown() throws Exception {
        if (hisSenderConsumer != null) hisSenderConsumer.stop();
        if (lisSenderConsumer != null) lisSenderConsumer.stop();
        if (hisCamelContext != null) hisCamelContext.stop();
        if (lisCamelContext != null) lisCamelContext.stop();
        if (mockLisServer != null) mockLisServer.close();
    }

    @Test
    void testDedicatedQueuesAreIsolated() {
        assertThat(hisSenderConsumer.getQueueName()).isEqualTo("petasos.queue.mllp.outbound.his_north");
        assertThat(lisSenderConsumer.getQueueName()).isEqualTo("petasos.queue.mllp.outbound.lis_main");
        assertThat(hisSenderConsumer.getQueueName()).isNotEqualTo(lisSenderConsumer.getQueueName());
    }

    @Test
    void testHisOutageDoesNotBlockLisTransmission() {
        // Dispatch failing message to HIS instance
        Topic hisTopic = Topic.forEgress("ADT", "A01", "harmonia", "mllp-out", "HIS_NORTH");
        String hisHl7 = "MSH|^~\\&|HARMONIA|HIE|HIS_SYS|HOSP_N|20260915083000||ADT^A01|MSG-HIS-01|P|2.4\r";
        OutboundMllpRequest hisRequest = new OutboundMllpRequest("MSG-HIS-01", "HIS_NORTH", hisHl7, hisTopic);

        OutboundMllpResponse hisResponse = hisSenderConsumer.processOutboundRequest(hisRequest);
        assertThat(hisResponse.isSuccessful()).isFalse();

        // Dispatch successful message to LIS instance concurrently
        Topic lisTopic = Topic.forEgress("ORU", "R01", "harmonia", "mllp-out", "LIS_MAIN");
        String lisHl7 = "MSH|^~\\&|HARMONIA|HIE|LIS_SYS|LAB|20260915083000||ORU^R01|MSG-LIS-01|P|2.4\r";
        OutboundMllpRequest lisRequest = new OutboundMllpRequest("MSG-LIS-01", "LIS_MAIN", lisHl7, lisTopic);

        OutboundMllpResponse lisResponse = lisSenderConsumer.processOutboundRequest(lisRequest);
        assertThat(lisResponse.isSuccessful()).isTrue();
        assertThat(lisResponse.getAckCode()).isEqualTo("AA");
        assertThat(mockLisServer.getReceivedCount()).isEqualTo(1);
    }
}
