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

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.model.praxis.PraxisDefinition;
import net.fhirfactory.harmonia.praxis.sequence.PraxisImplementation;
import org.apache.commons.lang3.StringUtils;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service managing persisted {@link PraxisDefinition} configuration objects in the Infinispan cluster.
 * <p>
 * Supports CRUD operations, HotRod remote cache synchronization with "tasksequence-cache",
 * JSON serialization, fallback memory cache, and query filters.
 */
@ApplicationScoped
public class PraxisService {

    private static final Logger log = LoggerFactory.getLogger(PraxisService.class);
    public static final String SEQUENCE_CACHE_NAME = "tasksequence-cache";

    @Inject
    private RemoteCacheManager remoteCacheManager;

    private ObjectMapper objectMapper;

    private final Map<String, String> localFallbackCache = new ConcurrentHashMap<>();

    public PraxisService() {
    }

    public PraxisService(RemoteCacheManager remoteCacheManager) {
        this.remoteCacheManager = remoteCacheManager;
        this.objectMapper = new ObjectMapper();
    }

    public PraxisService(RemoteCacheManager remoteCacheManager, ObjectMapper objectMapper) {
        this.remoteCacheManager = remoteCacheManager;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        if (this.objectMapper == null) {
            this.objectMapper = new ObjectMapper();
        }
    }

    public ObjectMapper getObjectMapper() {
        if (this.objectMapper == null) {
            this.objectMapper = new ObjectMapper();
        }
        return this.objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setRemoteCacheManager(RemoteCacheManager remoteCacheManager) {
        this.remoteCacheManager = remoteCacheManager;
    }

    private RemoteCache<String, String> getRemoteCache() {
        if (remoteCacheManager != null && remoteCacheManager.isStarted()) {
            try {
                return remoteCacheManager.getCache(SEQUENCE_CACHE_NAME);
            } catch (Exception e) {
                log.debug("Remote cache [{}] query failed: {}", SEQUENCE_CACHE_NAME, e.getMessage());
            }
        }
        return null;
    }

    public String toJson(PraxisDefinition sequence) {
        if (sequence == null) {
            return null;
        }
        try {
            return getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(sequence);
        } catch (Exception e) {
            log.error("Failed to serialize TaskSequenceDefinition [{}] to JSON: {}", sequence.getPraxisId(), e.getMessage(), e);
            throw new RuntimeException("Serialization error: " + e.getMessage(), e);
        }
    }

    public PraxisDefinition fromJson(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return getObjectMapper().readValue(json, PraxisDefinition.class);
        } catch (Exception e) {
            log.error("Failed to deserialize TaskSequenceDefinition from JSON: {}", e.getMessage(), e);
            throw new RuntimeException("Deserialization error: " + e.getMessage(), e);
        }
    }

    public <T extends PraxisDefinition> T fromJson(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return getObjectMapper().readValue(json, clazz);
        } catch (Exception e) {
            log.error("Failed to deserialize {} from JSON: {}", clazz.getSimpleName(), e.getMessage(), e);
            throw new RuntimeException("Deserialization error: " + e.getMessage(), e);
        }
    }

    public PraxisDefinition save(PraxisDefinition sequence) {
        if (sequence == null) {
            throw new IllegalArgumentException("TaskSequenceDefinition cannot be null");
        }
        String sequenceId = sequence.getPraxisId();
        if (StringUtils.isBlank(sequenceId)) {
            sequenceId = "seq-" + UUID.randomUUID().toString().substring(0, 8);
            sequence.setPraxisId(sequenceId);
        }
        if (sequence instanceof PraxisImplementation) {
            ((PraxisImplementation) sequence).syncActivityIds();
        }
        String json = toJson(sequence);

        localFallbackCache.put(sequenceId, json);
        RemoteCache<String, String> remoteCache = getRemoteCache();
        if (remoteCache != null) {
            try {
                remoteCache.put(sequenceId, json);
                log.info("Persisted TaskSequenceDefinition [{}] to remote Infinispan cache [{}]", sequenceId, SEQUENCE_CACHE_NAME);
            } catch (Exception e) {
                log.warn("Failed writing TaskSequenceDefinition [{}] to remote cache [{}]: {}. Persisted in local fallback.",
                        sequenceId, SEQUENCE_CACHE_NAME, e.getMessage());
            }
        } else {
            log.debug("Persisted TaskSequenceDefinition [{}] to local fallback cache", sequenceId);
        }
        return sequence;
    }

    public PraxisDefinition create(PraxisDefinition sequence) {
        return save(sequence);
    }

    public PraxisDefinition update(PraxisDefinition sequence) {
        return save(sequence);
    }

    public Optional<PraxisDefinition> getById(String sequenceId) {
        if (StringUtils.isBlank(sequenceId)) {
            return Optional.empty();
        }
        String cleanId = sequenceId.trim();
        RemoteCache<String, String> remoteCache = getRemoteCache();
        if (remoteCache != null) {
            try {
                String json = remoteCache.get(cleanId);
                if (json != null) {
                    return Optional.ofNullable(fromJson(json));
                }
            } catch (Exception e) {
                log.warn("Error reading TaskSequenceDefinition [{}] from remote cache [{}]: {}", cleanId, SEQUENCE_CACHE_NAME, e.getMessage());
            }
        }
        String fallbackJson = localFallbackCache.get(cleanId);
        if (fallbackJson != null) {
            return Optional.ofNullable(fromJson(fallbackJson));
        }
        return Optional.empty();
    }

    public List<PraxisDefinition> getAll() {
        Map<String, PraxisDefinition> sequenceMap = new LinkedHashMap<>();
        RemoteCache<String, String> remoteCache = getRemoteCache();
        if (remoteCache != null) {
            try {
                Collection<String> values = remoteCache.values();
                if (values != null && !values.isEmpty()) {
                    for (String json : values) {
                        if (json != null) {
                            try {
                                PraxisDefinition seq = fromJson(json);
                                if (seq != null) {
                                    sequenceMap.put(seq.getPraxisId(), seq);
                                }
                            } catch (Exception ignored) {}
                        }
                    }
                } else {
                    Set<String> keys = remoteCache.keySet();
                    if (keys != null) {
                        for (String key : keys) {
                            String json = remoteCache.get(key);
                            if (json != null) {
                                try {
                                    PraxisDefinition seq = fromJson(json);
                                    if (seq != null) {
                                        sequenceMap.put(seq.getPraxisId(), seq);
                                    }
                                } catch (Exception ignored) {}
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("Error scanning remote cache [{}] values: {}", SEQUENCE_CACHE_NAME, e.getMessage());
            }
        }
        for (Map.Entry<String, String> entry : localFallbackCache.entrySet()) {
            if (!sequenceMap.containsKey(entry.getKey())) {
                try {
                    PraxisDefinition seq = fromJson(entry.getValue());
                    if (seq != null) {
                        sequenceMap.put(seq.getPraxisId(), seq);
                    }
                } catch (Exception ignored) {}
            }
        }
        return new ArrayList<>(sequenceMap.values());
    }

    public List<PraxisDefinition> findMatching(String gatewayInstanceId, String triggerType) {
        return getAll().stream()
                .filter(PraxisDefinition::isEnabled)
                .filter(seq -> seq.matches(gatewayInstanceId, triggerType))
                .collect(Collectors.toList());
    }

    public List<PraxisDefinition> findByGateway(String gatewayInstanceId) {
        return getAll().stream()
                .filter(PraxisDefinition::isEnabled)
                .filter(seq -> seq.matches(gatewayInstanceId, null))
                .collect(Collectors.toList());
    }

    public List<PraxisDefinition> findByTrigger(String triggerType) {
        return getAll().stream()
                .filter(PraxisDefinition::isEnabled)
                .filter(seq -> seq.matches(null, triggerType))
                .collect(Collectors.toList());
    }

    public boolean delete(String sequenceId) {
        if (StringUtils.isBlank(sequenceId)) {
            return false;
        }
        String cleanId = sequenceId.trim();
        boolean removed = false;
        RemoteCache<String, String> remoteCache = getRemoteCache();
        if (remoteCache != null) {
            try {
                String prev = remoteCache.remove(cleanId);
                if (prev != null) {
                    removed = true;
                }
            } catch (Exception e) {
                log.warn("Error removing TaskSequenceDefinition [{}] from remote cache [{}]: {}", cleanId, SEQUENCE_CACHE_NAME, e.getMessage());
            }
        }
        if (localFallbackCache.remove(cleanId) != null) {
            removed = true;
        }
        return removed;
    }

    public long count() {
        return getAll().size();
    }

    public void clear() {
        RemoteCache<String, String> remoteCache = getRemoteCache();
        if (remoteCache != null) {
            try {
                remoteCache.clear();
            } catch (Exception e) {
                log.warn("Error clearing remote cache [{}]: {}", SEQUENCE_CACHE_NAME, e.getMessage());
            }
        }
        localFallbackCache.clear();
    }
}
