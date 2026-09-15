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
import net.fhirfactory.harmonia.mllpgateway.service.ModuleStatusService;
import net.fhirfactory.harmonia.mllpout.config.MllpOutboundConfig;
import net.fhirfactory.harmonia.mllpout.consumer.OutboundTaskQueueConsumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CamelContextManagerTest {

    @Mock
    private ModuleStatusService moduleStatusService;

    @Mock
    private OutboundTaskQueueConsumer outboundTaskQueueConsumer;

    private CamelContextManager contextManager;
    private MllpOutboundConfig outboundConfig;

    @BeforeEach
    void setUp() {
        MllpDestinationRegistry registry = new MllpDestinationRegistry();
        registry.registerDestination(new MllpDestinationConfig("HIS_NORTH", "127.0.0.1", 2575));

        OutboundMllpProcessor processor = new OutboundMllpProcessor(registry);
        Hl7AckProcessor ackProcessor = new Hl7AckProcessor();
        OutboundMllpRouteBuilder routeBuilder = new OutboundMllpRouteBuilder(processor, ackProcessor);

        outboundConfig = new MllpOutboundConfig("mllp-sender-his", "HIS_NORTH");

        contextManager = new CamelContextManager();
        contextManager.setOutboundMllpRouteBuilder(routeBuilder);
        contextManager.setOutboundTaskQueueConsumer(outboundTaskQueueConsumer);
        contextManager.setModuleStatusService(moduleStatusService);
        contextManager.setOutboundConfig(outboundConfig);
    }

    @AfterEach
    void tearDown() {
        if (contextManager != null) {
            contextManager.stop(null);
        }
    }

    @Test
    void testStartupAndShutdown() {
        contextManager.start();

        assertThat(contextManager.getCamelContext()).isNotNull();
        assertThat(contextManager.getCamelContext().isStarted()).isTrue();

        verify(outboundTaskQueueConsumer).setCamelContext(any());
        verify(outboundTaskQueueConsumer).start();
        verify(moduleStatusService).registerModule(eq("mllp-gateway-out-mllp-sender-his"), eq("MLLP Gateway Outbound"),
                eq("EGRESS_GATEWAY"), eq("READY"), eq(true), any());

        contextManager.stop(null);
        verify(outboundTaskQueueConsumer).stop();
        verify(moduleStatusService).unregisterModule(eq("mllp-gateway-out-mllp-sender-his"));
    }
}
