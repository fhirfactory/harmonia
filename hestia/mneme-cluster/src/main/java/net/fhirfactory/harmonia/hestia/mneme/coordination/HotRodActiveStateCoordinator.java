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

package net.fhirfactory.harmonia.hestia.mneme.coordination;

import net.fhirfactory.harmonia.model.governedwrite.ActiveStateCoordinationResult;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateToken;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateTokenBridge;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import org.infinispan.client.hotrod.MetadataValue;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.exceptions.HotRodClientException;
import org.infinispan.commons.CacheException;

/**
 * Production Hot Rod active-state coordinator for Mneme.
 *
 * <p>Encapsulates native Hot Rod optimistic concurrency ({@code replaceWithVersion}) into a
 * dedicated, non-authoritative active-state coordinator over {@code active-coordination-cache}.
 * All coordination state is stored as an explicit {@link ActiveCoordinationRecord} encoding
 * {@code NO_HEGEMON} or {@code HEGEMON:<instance-id>}.
 *
 * <p>Under cluster/network unavailability, fail-fast visible failure is enforced without any
 * silent degradation to JVM-local synchronization, CAS, or in-memory fallback maps.
 */
public class HotRodActiveStateCoordinator implements MnemeActiveStateCoordinator {

    public static final String COORDINATION_CACHE_NAME = "active-coordination-cache";
    public static final String NO_HEGEMON_MARKER = ActiveCoordinationRecord.NO_HEGEMON_MARKER;

    private final RemoteCache<String, String> coordinationCache;

    public HotRodActiveStateCoordinator(RemoteCacheManager cacheManager) {
        if (cacheManager == null) {
            throw new IllegalArgumentException("cacheManager must not be null");
        }
        RemoteCache<String, String> cache = cacheManager.getCache(COORDINATION_CACHE_NAME);
        if (cache == null) {
            throw new IllegalStateException("Coordination cache '" + COORDINATION_CACHE_NAME + "' is not available");
        }
        this.coordinationCache = cache;
    }

    public HotRodActiveStateCoordinator(RemoteCache<String, String> coordinationCache) {
        if (coordinationCache == null) {
            throw new IllegalArgumentException("coordinationCache must not be null");
        }
        this.coordinationCache = coordinationCache;
    }

    @Override
    public ActiveStateToken observe(ResourceKey key) {
        if (key == null) {
            throw new IllegalArgumentException("ResourceKey must not be null");
        }
        try {
            String cacheKey = key.toQualifiedPath();
            MetadataValue<String> meta = coordinationCache.getWithMetadata(cacheKey);
            if (meta == null) {
                coordinationCache.putIfAbsent(cacheKey, NO_HEGEMON_MARKER);
                meta = coordinationCache.getWithMetadata(cacheKey);
            }
            if (meta == null) {
                throw new IllegalStateException("Failed to observe active coordination state for " + key);
            }
            ActiveCoordinationRecord.decode(meta.getValue());
            return ActiveStateTokenBridge.create(meta.getVersion());
        } catch (HotRodClientException | CacheException e) {
            throw new ActiveCoordinationUnavailableException(
                    "Coordination cache unavailable while observing key: " + key, e);
        }
    }

    @Override
    public ActiveStateCoordinationResult consume(ResourceKey key, ActiveStateToken observedToken) {
        if (key == null) {
            throw new IllegalArgumentException("ResourceKey must not be null");
        }
        if (observedToken == null) {
            throw new IllegalArgumentException("observedToken must not be null");
        }
        try {
            String cacheKey = key.toQualifiedPath();
            MetadataValue<String> meta = coordinationCache.getWithMetadata(cacheKey);
            if (meta == null) {
                return ActiveStateCoordinationResult.STALE;
            }
            ActiveCoordinationRecord.decode(meta.getValue());
            long version = ActiveStateTokenBridge.extractVersion(observedToken);
            boolean replaced = coordinationCache.replaceWithVersion(cacheKey, meta.getValue(), version);
            return replaced ? ActiveStateCoordinationResult.CONSUMED : ActiveStateCoordinationResult.STALE;
        } catch (HotRodClientException | CacheException e) {
            return ActiveStateCoordinationResult.UNAVAILABLE;
        }
    }

    @Override
    public ResourceHegemonStatus checkHegemonStatus(ResourceKey key, InstanceId instanceId) {
        if (key == null) {
            throw new IllegalArgumentException("ResourceKey must not be null");
        }
        if (instanceId == null) {
            throw new IllegalArgumentException("instanceId must not be null");
        }
        try {
            String cacheKey = key.toQualifiedPath();
            MetadataValue<String> meta = coordinationCache.getWithMetadata(cacheKey);
            if (meta == null) {
                // Cold key / absent entry -> treated as NO_RESOURCE_IS_HEGEMON
                // Strictly observational: zero cache writes
                return ResourceHegemonStatus.NO_RESOURCE_IS_HEGEMON;
            }
            ActiveCoordinationRecord record = ActiveCoordinationRecord.decode(meta.getValue());
            if (!record.activeHegemon().isPresent()) {
                return ResourceHegemonStatus.NO_RESOURCE_IS_HEGEMON;
            }
            if (instanceId.equals(record.activeHegemon().instanceId())) {
                return ResourceHegemonStatus.RESOURCE_IS_HEGEMON;
            }
            return ResourceHegemonStatus.RESOURCE_IS_NOT_HEGEMON;
        } catch (HotRodClientException | CacheException e) {
            throw new ActiveCoordinationUnavailableException(
                    "Coordination cache unavailable while checking Hegemon status for key: " + key, e);
        }
    }

    @Override
    public HegemonyCoordinationResult acquireOrConfirmHegemony(ResourceKey key, InstanceId instanceId, ActiveStateToken observedToken) {
        if (key == null) {
            throw new IllegalArgumentException("ResourceKey must not be null");
        }
        if (instanceId == null) {
            throw new IllegalArgumentException("instanceId must not be null");
        }
        if (observedToken == null) {
            throw new IllegalArgumentException("observedToken must not be null");
        }
        try {
            String cacheKey = key.toQualifiedPath();
            MetadataValue<String> meta = coordinationCache.getWithMetadata(cacheKey);
            if (meta == null) {
                return new HegemonyCoordinationResult.Stale("Coordination entry is absent for key: " + key);
            }
            ActiveCoordinationRecord record = ActiveCoordinationRecord.decode(meta.getValue());
            long currentVersion = meta.getVersion();
            long expectedVersion = ActiveStateTokenBridge.extractVersion(observedToken);

            // Case 1: Resource currently has NO_HEGEMON
            if (!record.activeHegemon().isPresent()) {
                if (currentVersion != expectedVersion) {
                    return new HegemonyCoordinationResult.Stale(
                            "Observed token version " + expectedVersion + " does not match current version " + currentVersion);
                }
                String newRecordValue = ActiveCoordinationRecord.of(instanceId).encode();
                boolean replaced = coordinationCache.replaceWithVersion(cacheKey, newRecordValue, expectedVersion);
                if (!replaced) {
                    return new HegemonyCoordinationResult.Stale(
                            "Concurrent update detected during Hegemony acquisition for key: " + key);
                }
                MetadataValue<String> updatedMeta = coordinationCache.getWithMetadata(cacheKey);
                if (updatedMeta == null) {
                    throw new IllegalStateException("Failed to read updated metadata after Hegemony acquisition for " + key);
                }
                ActiveStateToken newToken = ActiveStateTokenBridge.create(updatedMeta.getVersion());
                return new HegemonyCoordinationResult.Acquired(newToken);
            }

            // Case 2: Submitting instance is already Hegemon
            if (instanceId.equals(record.activeHegemon().instanceId())) {
                if (currentVersion != expectedVersion) {
                    return new HegemonyCoordinationResult.Stale(
                            "Observed token version " + expectedVersion + " does not match current version " + currentVersion);
                }
                // Read-validated confirmation: NO cache write, NO token advancement
                return new HegemonyCoordinationResult.Confirmed(observedToken);
            }

            // Case 3: Another instance is Hegemon
            return new HegemonyCoordinationResult.NotHegemon(
                    "Resource is currently held by another Hegemon: " + record.activeHegemon().instanceId().value());

        } catch (HotRodClientException | CacheException e) {
            return new HegemonyCoordinationResult.Unavailable(
                    "Coordination cache unavailable: " + e.getMessage());
        }
    }
}
