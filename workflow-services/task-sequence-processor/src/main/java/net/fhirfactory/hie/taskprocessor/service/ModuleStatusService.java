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

package net.fhirfactory.hie.taskprocessor.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.hie.model.status.ModuleStatus;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service managing module status and readiness notifications within the Infinispan cluster.
 */
@ApplicationScoped
public class ModuleStatusService {

    private static final Logger log = LoggerFactory.getLogger(ModuleStatusService.class);
    public static final String MODULE_STATUS_CACHE_NAME = "modulestatus-cache";

    @Inject
    private RemoteCacheManager remoteCacheManager;

    private final Map<String, ModuleStatus> localFallbackCache = new ConcurrentHashMap<>();

    public ModuleStatusService() {
    }

    public ModuleStatusService(RemoteCacheManager remoteCacheManager) {
        this.remoteCacheManager = remoteCacheManager;
    }

    private RemoteCache<String, String> getRemoteCache() {
        if (remoteCacheManager != null && remoteCacheManager.isStarted()) {
            try {
                return remoteCacheManager.getCache(MODULE_STATUS_CACHE_NAME);
            } catch (Exception e) {
                log.debug("Remote cache [{}] query failed in TaskProcessor ModuleStatusService: {}", MODULE_STATUS_CACHE_NAME, e.getMessage());
            }
        }
        return null;
    }

    public void updateStatus(ModuleStatus status) {
        if (status == null || status.getModuleId() == null || status.getModuleId().isBlank()) {
            return;
        }
        status.touch();
        localFallbackCache.put(status.getModuleId(), status);

        RemoteCache<String, String> cache = getRemoteCache();
        if (cache != null) {
            try {
                cache.put(status.getModuleId(), status.toJson());
                log.info("Persisted ModuleStatus for [{}] [status={}, ready={}] to Infinispan cache [{}]",
                        status.getModuleId(), status.getStatus(), status.isReady(), MODULE_STATUS_CACHE_NAME);
            } catch (Exception e) {
                log.warn("Failed to persist ModuleStatus for [{}] to Infinispan cache: {}", status.getModuleId(), e.getMessage());
            }
        }
    }

    public ModuleStatus registerModule(String moduleId, String moduleName, String moduleType, boolean ready) {
        ModuleStatus status = new ModuleStatus(moduleId, moduleName, moduleType, ready ? "READY" : "STARTING", ready);
        updateStatus(status);
        return status;
    }

    public ModuleStatus registerModule(String moduleId, String moduleName, String moduleType, String statusStr, boolean ready, Map<String, Object> details) {
        ModuleStatus status = new ModuleStatus(moduleId, moduleName, moduleType, statusStr, ready);
        if (details != null) {
            status.setDetails(new HashMap<>(details));
        }
        updateStatus(status);
        return status;
    }

    public void unregisterModule(String moduleId) {
        if (moduleId == null || moduleId.isBlank()) {
            return;
        }
        ModuleStatus existing = getModuleStatus(moduleId).orElse(null);
        if (existing != null) {
            existing.setStatus("STOPPED");
            existing.setReady(false);
            updateStatus(existing);
        } else {
            ModuleStatus stopped = ModuleStatus.stopped(moduleId, moduleId, "UNKNOWN");
            updateStatus(stopped);
        }
    }

    public Optional<ModuleStatus> getModuleStatus(String moduleId) {
        if (moduleId == null || moduleId.isBlank()) {
            return Optional.empty();
        }
        RemoteCache<String, String> cache = getRemoteCache();
        if (cache != null) {
            try {
                String json = cache.get(moduleId);
                if (json != null && !json.isBlank()) {
                    ModuleStatus status = ModuleStatus.fromJson(json);
                    if (status != null) {
                        localFallbackCache.put(moduleId, status);
                        return Optional.of(status);
                    }
                }
            } catch (Exception e) {
                log.debug("Failed to get ModuleStatus [{}] from remote cache: {}", moduleId, e.getMessage());
            }
        }
        return Optional.ofNullable(localFallbackCache.get(moduleId));
    }

    public List<ModuleStatus> getAllModuleStatuses() {
        Map<String, ModuleStatus> combined = new LinkedHashMap<>(localFallbackCache);

        RemoteCache<String, String> cache = getRemoteCache();
        if (cache != null) {
            try {
                for (String json : cache.values()) {
                    if (json != null && !json.isBlank()) {
                        try {
                            ModuleStatus ms = ModuleStatus.fromJson(json);
                            if (ms != null && ms.getModuleId() != null) {
                                combined.put(ms.getModuleId(), ms);
                            }
                        } catch (Exception e) {
                            log.debug("Failed to deserialize ModuleStatus entry: {}", e.getMessage());
                        }
                    }
                }
            } catch (Exception e) {
                log.debug("Failed to fetch all ModuleStatuses from remote cache: {}", e.getMessage());
            }
        }
        return new ArrayList<>(combined.values());
    }

    public boolean isModuleReady(String moduleId) {
        return getModuleStatus(moduleId)
                .map(ModuleStatus::isReady)
                .orElse(false);
    }

    public boolean waitForModuleReady(String moduleId, int maxWaitSeconds) {
        long deadline = System.currentTimeMillis() + (maxWaitSeconds * 1000L);
        log.info("Awaiting cluster detection for module [{}] to be UP and READY (timeout: {}s)...", moduleId, maxWaitSeconds);

        while (System.currentTimeMillis() < deadline) {
            if (isModuleReady(moduleId)) {
                log.info("Detected module [{}] is UP and READY in cluster cache.", moduleId);
                return true;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Interrupted while awaiting module [{}] readiness", moduleId);
                return isModuleReady(moduleId);
            }
        }
        boolean ready = isModuleReady(moduleId);
        if (!ready) {
            log.warn("Timed out after {}s waiting for module [{}] to become READY.", maxWaitSeconds, moduleId);
        }
        return ready;
    }

    public void setRemoteCacheManager(RemoteCacheManager remoteCacheManager) {
        this.remoteCacheManager = remoteCacheManager;
    }
}
