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

package net.fhirfactory.harmonia.praxis.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaCheckpoint;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.praxis.cache.TaskCacheService;
import org.apache.commons.lang3.StringUtils;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Dedicated cache service for persisting and retrieving canonical {@link Pragma} workflow instances
 * and checkpoint state within the Mneme Infinispan cluster.
 * <p>
 * Checkpointed Pragma instances pushed to this service are stored in the {@code task-cache} / {@code pragma-cache}
 * and synchronized with {@code mnemosyne-operations} via Infinispan write-behind stores.
 */
@ApplicationScoped
public class PragmaCacheService {

    private static final Logger log = LoggerFactory.getLogger(PragmaCacheService.class);

    public static final String PRAGMA_CACHE_NAME = "task-cache";

    @Inject
    private RemoteCacheManager remoteCacheManager;

    @Inject
    private TaskCacheService taskCacheService;

    private RemoteCache<String, String> pragmaCache;
    private final Map<String, Pragma> localFallbackCache = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public PragmaCacheService() {
    }

    public PragmaCacheService(RemoteCacheManager remoteCacheManager, TaskCacheService taskCacheService) {
        this.remoteCacheManager = remoteCacheManager;
        this.taskCacheService = taskCacheService;
        init();
    }

    @PostConstruct
    public void init() {
        if (remoteCacheManager != null) {
            try {
                this.pragmaCache = remoteCacheManager.getCache(PRAGMA_CACHE_NAME);
                if (this.pragmaCache != null) {
                    log.info("Initialized RemoteCache [{}] for Pragma persistence in Mneme", PRAGMA_CACHE_NAME);
                } else {
                    log.warn("RemoteCache [{}] unavailable from manager, using local fallback store", PRAGMA_CACHE_NAME);
                }
            } catch (Exception e) {
                log.warn("Could not connect to Mneme cluster for [{}]: {}. Using local fallback store.",
                        PRAGMA_CACHE_NAME, e.getMessage());
            }
        } else {
            log.info("No RemoteCacheManager injected, PragmaCacheService running with local in-memory store");
        }
    }

    /**
     * Persists or updates a {@link Pragma} in the cache, updating its lastModified timestamp.
     *
     * @param pragma Pragma instance to save
     * @return persisted Pragma instance
     */
    public Pragma savePragma(Pragma pragma) {
        if (pragma == null) {
            throw new IllegalArgumentException("Pragma cannot be null");
        }
        if (StringUtils.isBlank(pragma.getPragmaId())) {
            pragma.setPragmaId(UUID.randomUUID().toString());
        }
        pragma.touch();

        String id = cleanId(pragma.getPragmaId());
        localFallbackCache.put(id, new Pragma(pragma));

        try {
            String json = objectMapper.writeValueAsString(pragma);
            if (pragmaCache != null) {
                pragmaCache.put(id, json);
                log.debug("Saved Pragma/{} to remote cache [{}]", id, PRAGMA_CACHE_NAME);
            }
        } catch (Exception e) {
            log.warn("Failed to serialize or store Pragma/{} in remote cache: {}", id, e.getMessage());
        }

        if (taskCacheService != null) {
            try {
                taskCacheService.savePragma(pragma);
            } catch (Exception ignored) {
            }
        }

        return pragma;
    }

    /**
     * Retrieves a {@link Pragma} by unique identifier from the cache.
     *
     * @param pragmaId unique Pragma ID
     * @return optional containing the Pragma if found
     */
    public Optional<Pragma> getPragma(String pragmaId) {
        if (StringUtils.isBlank(pragmaId)) {
            return Optional.empty();
        }
        String id = cleanId(pragmaId);

        if (pragmaCache != null) {
            try {
                String json = pragmaCache.get(id);
                if (StringUtils.isNotBlank(json)) {
                    Pragma pragma = objectMapper.readValue(json, Pragma.class);
                    return Optional.ofNullable(pragma);
                }
            } catch (Exception e) {
                log.warn("Error reading Pragma/{} from remote cache: {}", id, e.getMessage());
            }
        }

        Pragma local = localFallbackCache.get(id);
        if (local != null) {
            return Optional.of(new Pragma(local));
        }

        if (taskCacheService != null) {
            return taskCacheService.getPragma(id);
        }

        return Optional.empty();
    }

    /**
     * Checks if a Pragma exists in the cache.
     *
     * @param pragmaId Pragma ID
     * @return true if present
     */
    public boolean containsPragma(String pragmaId) {
        return getPragma(pragmaId).isPresent();
    }

    /**
     * Deletes a Pragma from the cache.
     *
     * @param pragmaId Pragma ID to remove
     * @return true if deleted
     */
    public boolean deletePragma(String pragmaId) {
        if (StringUtils.isBlank(pragmaId)) {
            return false;
        }
        String id = cleanId(pragmaId);
        boolean removed = localFallbackCache.remove(id) != null;

        if (pragmaCache != null) {
            try {
                pragmaCache.remove(id);
                removed = true;
            } catch (Exception e) {
                log.warn("Error deleting Pragma/{} from remote cache: {}", id, e.getMessage());
            }
        }

        if (taskCacheService != null) {
            taskCacheService.deletePragma(id);
        }

        return removed;
    }

    /**
     * Returns all checkpoints recorded for a specific Pragma.
     *
     * @param pragmaId Pragma ID
     * @return list of checkpoints, or empty list if not found
     */
    public List<PragmaCheckpoint> getCheckpoints(String pragmaId) {
        Optional<Pragma> pragmaOpt = getPragma(pragmaId);
        return pragmaOpt.map(Pragma::getCheckpoints).orElseGet(Collections::emptyList);
    }

    /**
     * Clears all local cached entries.
     */
    public void clear() {
        localFallbackCache.clear();
        if (pragmaCache != null) {
            try {
                pragmaCache.clear();
            } catch (Exception ignored) {
            }
        }
    }

    private String cleanId(String rawId) {
        if (rawId == null) {
            return null;
        }
        return rawId.replace("Task/", "").replace("Pragma/", "").trim();
    }
}
