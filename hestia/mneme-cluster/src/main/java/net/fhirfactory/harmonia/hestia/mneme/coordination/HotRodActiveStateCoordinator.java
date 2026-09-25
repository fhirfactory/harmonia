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
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateCoordinator;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateToken;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateTokenBridge;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import org.infinispan.client.hotrod.MetadataValue;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.exceptions.HotRodClientException;
import org.infinispan.commons.CacheException;

import java.util.Objects;

/**
 * Production Hot Rod active-state coordinator for Mneme.
 *
 * <p>Encapsulates native Hot Rod optimistic concurrency ({@code replaceWithVersion}) into a
 * dedicated, non-authoritative active-state coordinator over {@code active-coordination-cache}.
 * All coordination state is stored as a lightweight fixed marker ({@code "ACTIVE"}).
 *
 * <p>Under cluster/network unavailability, fail-fast visible failure is enforced without any
 * silent degradation to JVM-local synchronization, CAS, or in-memory fallback maps.
 */
public class HotRodActiveStateCoordinator implements ActiveStateCoordinator {

    public static final String COORDINATION_CACHE_NAME = "active-coordination-cache";
    public static final String COORDINATION_MARKER = "ACTIVE";

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
                coordinationCache.putIfAbsent(cacheKey, COORDINATION_MARKER);
                meta = coordinationCache.getWithMetadata(cacheKey);
            }
            if (meta == null) {
                throw new IllegalStateException("Failed to observe active coordination state for " + key);
            }
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
            long version = ActiveStateTokenBridge.extractVersion(observedToken);
            String cacheKey = key.toQualifiedPath();
            boolean replaced = coordinationCache.replaceWithVersion(cacheKey, COORDINATION_MARKER, version);
            return replaced ? ActiveStateCoordinationResult.CONSUMED : ActiveStateCoordinationResult.STALE;
        } catch (HotRodClientException | CacheException e) {
            return ActiveStateCoordinationResult.UNAVAILABLE;
        }
    }

}
