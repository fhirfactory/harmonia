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

package net.fhirfactory.harmonia.mllpgateway.service;

import net.fhirfactory.harmonia.model.status.ModuleStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service interface for publishing and querying cluster module status in the Infinispan cluster.
 */
public interface ModuleStatusService {

    String MODULE_STATUS_CACHE_NAME = "modulestatus-cache";

    /**
     * Updates or creates the module status in the Infinispan cache.
     */
    void updateStatus(ModuleStatus status);

    /**
     * Registers a module as READY with default details.
     */
    ModuleStatus registerModule(String moduleId, String moduleName, String moduleType, boolean ready);

    /**
     * Registers a module with full details.
     */
    ModuleStatus registerModule(String moduleId, String moduleName, String moduleType, String status, boolean ready, Map<String, Object> details);

    /**
     * Marks a module as STOPPED.
     */
    void unregisterModule(String moduleId);

    /**
     * Retrieves the status of a specific module.
     */
    Optional<ModuleStatus> getModuleStatus(String moduleId);

    /**
     * Retrieves all active module statuses.
     */
    List<ModuleStatus> getAllModuleStatuses();

    /**
     * Checks if a target module is currently registered as UP and READY.
     */
    boolean isModuleReady(String moduleId);

    /**
     * Synchronously waits for a target module to become UP and READY within the cluster cache.
     *
     * @param moduleId       ID of the target module (e.g. "task-sequence-processor")
     * @param maxWaitSeconds maximum duration to wait
     * @return true if ready, false if timeout elapsed
     */
    boolean waitForModuleReady(String moduleId, int maxWaitSeconds);
}
