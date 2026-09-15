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

package net.fhirfactory.harmonia.mllpgateway.camel;

import net.fhirfactory.harmonia.mllpgateway.config.MllpConfig;
import net.fhirfactory.harmonia.mllpgateway.service.DefaultModuleStatusService;
import net.fhirfactory.harmonia.mllpgateway.service.ModuleStatusService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CamelContextManagerTest {

    private CamelContextManager manager;
    private ModuleStatusService moduleStatusService;

    @BeforeEach
    void setUp() {
        moduleStatusService = new DefaultModuleStatusService();
        manager = new CamelContextManager();
        manager.setModuleStatusService(moduleStatusService);
        manager.setMllpConfig(new MllpConfig("127.0.0.1", 25750, true));
        manager.setAwaitTimeoutSeconds(1);
    }

    @AfterEach
    void tearDown() {
        if (manager != null) {
            manager.stop(null);
        }
    }

    @Test
    @DisplayName("Starts routes and registers module status after task processor is ready")
    void testStartWhenTaskProcessorReady() {
        // Pre-register task-sequence-processor as ready
        moduleStatusService.registerModule("task-sequence-processor", "Task Sequence Processor", "WORKFLOW_SERVICE", true);

        manager.start();

        assertNotNull(manager.getCamelContext());
        assertTrue(manager.getCamelContext().isStarted());

        assertTrue(moduleStatusService.isModuleReady("mllp-gateway-in"));
    }

    @Test
    @DisplayName("Starts both ADT and MFN MLLP route builders when provided")
    void testStartWithBothAdtAndMfnRoutes() {
        moduleStatusService.registerModule("task-sequence-processor", "Task Sequence Processor", "WORKFLOW_SERVICE", true);
        MllpConfig config = new MllpConfig("127.0.0.1", 25770, 25771, true);
        manager.setMllpConfig(config);

        IncomingAdtMessageProcessorWrapper adtWrapper = new IncomingAdtMessageProcessorWrapper();
        IncomingMfnMessageProcessorWrapper mfnWrapper = new IncomingMfnMessageProcessorWrapper();

        manager.setAdtMllpRouteBuilder(new IncomingAdtMessageMllpRouteBuilder(config, adtWrapper));
        manager.setMfnMllpRouteBuilder(new IncomingMfnMessageMllpRouteBuilder(config, mfnWrapper));

        manager.start();

        assertNotNull(manager.getCamelContext());
        assertTrue(manager.getCamelContext().isStarted());
        assertEquals(4, manager.getCamelContext().getRoutes().size());
        assertNotNull(manager.getCamelContext().getRoute("hl7-mllp-adt-receiver"));
        assertNotNull(manager.getCamelContext().getRoute("direct-adt-processor"));
        assertNotNull(manager.getCamelContext().getRoute("hl7-mllp-mfn-receiver"));
        assertNotNull(manager.getCamelContext().getRoute("direct-mfn-processor"));
    }

    @Test
    @DisplayName("Unregisters module status on stop")
    void testStopUnregistersModule() {
        moduleStatusService.registerModule("task-sequence-processor", "Task Sequence Processor", "WORKFLOW_SERVICE", true);
        manager.start();
        assertTrue(moduleStatusService.isModuleReady("mllp-gateway-in"));

        manager.stop(null);
        assertFalse(moduleStatusService.isModuleReady("mllp-gateway-in"));
    }
}
