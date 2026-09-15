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

package net.fhirfactory.harmonia.model.status;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ModuleStatusTest {

    @Test
    public void testReadyFactoryMethod() {
        ModuleStatus status = ModuleStatus.ready("task-sequence-processor", "Task Sequence Processor", "WORKFLOW_SERVICE");
        assertNotNull(status);
        assertEquals("task-sequence-processor", status.getModuleId());
        assertEquals("Task Sequence Processor", status.getModuleName());
        assertEquals("WORKFLOW_SERVICE", status.getModuleType());
        assertEquals("READY", status.getStatus());
        assertTrue(status.isReady());
        assertNotNull(status.getStartedAt());
        assertNotNull(status.getLastUpdated());
    }

    @Test
    public void testStartingAndStoppedFactoryMethods() {
        ModuleStatus starting = ModuleStatus.starting("mllp-gateway-in", "MLLP Gateway Inbound", "INTERFACING_SERVICE");
        assertEquals("STARTING", starting.getStatus());
        assertFalse(starting.isReady());

        ModuleStatus stopped = ModuleStatus.stopped("mllp-gateway-in", "MLLP Gateway Inbound", "INTERFACING_SERVICE");
        assertEquals("STOPPED", stopped.getStatus());
        assertFalse(stopped.isReady());
    }

    @Test
    public void testJsonSerializationAndDeserialization() {
        ModuleStatus status = ModuleStatus.ready("befe", "BEFE Gateway", "PRESENTATION_SERVICE");
        status.setHost("befe-host");
        status.setPort(8090);
        status.setInstanceId("befe-inst-1");
        status.setEndpointUrl("http://localhost:8090");
        status.addDetail("activeSequences", 3);

        String json = status.toJson();
        assertNotNull(json);
        assertTrue(json.contains("befe"));
        assertTrue(json.contains("BEFE Gateway"));
        assertTrue(json.contains("8090"));

        ModuleStatus deserialized = ModuleStatus.fromJson(json);
        assertNotNull(deserialized);
        assertEquals(status.getModuleId(), deserialized.getModuleId());
        assertEquals(status.getModuleName(), deserialized.getModuleName());
        assertEquals(status.getModuleType(), deserialized.getModuleType());
        assertEquals(status.getStatus(), deserialized.getStatus());
        assertEquals(status.isReady(), deserialized.isReady());
        assertEquals(status.getHost(), deserialized.getHost());
        assertEquals(status.getPort(), deserialized.getPort());
        assertEquals(3, ((Number) deserialized.getDetails().get("activeSequences")).intValue());
    }

    @Test
    public void testTouchUpdatesTimestamp() throws InterruptedException {
        ModuleStatus status = ModuleStatus.ready("test-mod", "Test", "TEST");
        String initial = status.getLastUpdated();
        Thread.sleep(10);
        status.touch();
        assertNotEquals(initial, status.getLastUpdated());
    }
}
