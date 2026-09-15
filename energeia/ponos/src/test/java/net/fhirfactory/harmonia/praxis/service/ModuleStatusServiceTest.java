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

package net.fhirfactory.harmonia.praxis.service;

import net.fhirfactory.harmonia.model.status.ModuleStatus;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ModuleStatusServiceTest {

    private RemoteCacheManager mockCacheManager;
    private RemoteCache<String, String> mockCache;
    private ModuleStatusService statusService;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setUp() {
        mockCacheManager = mock(RemoteCacheManager.class);
        mockCache = mock(RemoteCache.class);

        when(mockCacheManager.isStarted()).thenReturn(true);
        doReturn(mockCache).when(mockCacheManager).getCache(eq(ModuleStatusService.MODULE_STATUS_CACHE_NAME));

        statusService = new ModuleStatusService(mockCacheManager);
    }

    @Test
    public void testRegisterAndGetModuleStatus() {
        ModuleStatus registered = statusService.registerModule("task-sequence-processor", "Task Sequence Processor", "WORKFLOW_SERVICE", true);
        assertNotNull(registered);
        assertTrue(registered.isReady());
        assertEquals("READY", registered.getStatus());

        verify(mockCache).put(eq("task-sequence-processor"), anyString());

        Optional<ModuleStatus> status = statusService.getModuleStatus("task-sequence-processor");
        assertTrue(status.isPresent());
        assertEquals("task-sequence-processor", status.get().getModuleId());
        assertTrue(status.get().isReady());
    }

    @Test
    public void testIsModuleReadyAndUnregister() {
        statusService.registerModule("task-sequence-processor", "Task Sequence Processor", "WORKFLOW_SERVICE", true);
        assertTrue(statusService.isModuleReady("task-sequence-processor"));

        statusService.unregisterModule("task-sequence-processor");
        assertFalse(statusService.isModuleReady("task-sequence-processor"));
    }

    @Test
    public void testWaitForModuleReady() {
        statusService.registerModule("task-sequence-processor", "Task Sequence Processor", "WORKFLOW_SERVICE", true);
        boolean ready = statusService.waitForModuleReady("task-sequence-processor", 2);
        assertTrue(ready);
    }

    @Test
    public void testWaitForModuleReadyTimeout() {
        boolean ready = statusService.waitForModuleReady("non-existent-module", 1);
        assertFalse(ready);
    }

    @Test
    public void testGetAllModuleStatuses() {
        statusService.registerModule("mod-1", "Module 1", "TYPE_1", true);
        statusService.registerModule("mod-2", "Module 2", "TYPE_2", false);

        List<ModuleStatus> all = statusService.getAllModuleStatuses();
        assertNotNull(all);
        assertTrue(all.size() >= 2);
    }
}
