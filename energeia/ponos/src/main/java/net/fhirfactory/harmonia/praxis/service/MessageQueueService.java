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
import net.fhirfactory.harmonia.model.petasos.PetasosQueueDefinition;
import net.fhirfactory.harmonia.praxis.config.QueueConfig;
import org.apache.commons.lang3.StringUtils;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service managing persisted {@link PetasosQueueDefinition} configurations in the Infinispan cluster
 * and HIE Operations JPA store.
 */
@ApplicationScoped
public class MessageQueueService {

    private static final Logger log = LoggerFactory.getLogger(MessageQueueService.class);
    public static final String QUEUE_CACHE_NAME = "messagequeue-cache";

    @Inject
    private RemoteCacheManager remoteCacheManager;

    @Inject
    private QueueConfig queueConfig;

    private ObjectMapper objectMapper;
    private final Map<String, String> localFallbackCache = new ConcurrentHashMap<>();

    public MessageQueueService() {
    }

    public MessageQueueService(RemoteCacheManager remoteCacheManager) {
        this.remoteCacheManager = remoteCacheManager;
        this.objectMapper = new ObjectMapper();
    }

    public MessageQueueService(RemoteCacheManager remoteCacheManager, QueueConfig queueConfig) {
        this.remoteCacheManager = remoteCacheManager;
        this.queueConfig = queueConfig;
        this.objectMapper = new ObjectMapper();
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

    public void setQueueConfig(QueueConfig queueConfig) {
        this.queueConfig = queueConfig;
    }

    private RemoteCache<String, String> getRemoteCache() {
        if (remoteCacheManager != null && remoteCacheManager.isStarted()) {
            try {
                return remoteCacheManager.getCache(QUEUE_CACHE_NAME);
            } catch (Exception e) {
                log.debug("Remote cache [{}] query failed: {}", QUEUE_CACHE_NAME, e.getMessage());
            }
        }
        return null;
    }

    public String toJson(PetasosQueueDefinition queueDef) {
        if (queueDef == null) {
            return null;
        }
        try {
            return getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(queueDef);
        } catch (Exception e) {
            log.error("Failed to serialize MessageQueueDefinition [{}] to JSON: {}", queueDef.getQueueId(), e.getMessage(), e);
            throw new RuntimeException("Serialization error: " + e.getMessage(), e);
        }
    }

    public PetasosQueueDefinition fromJson(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return getObjectMapper().readValue(json, PetasosQueueDefinition.class);
        } catch (Exception e) {
            log.error("Failed to deserialize MessageQueueDefinition JSON: {}", e.getMessage(), e);
            return null;
        }
    }

    public PetasosQueueDefinition save(PetasosQueueDefinition queueDef) {
        if (queueDef == null) {
            throw new IllegalArgumentException("MessageQueueDefinition must not be null");
        }
        String queueId = queueDef.getQueueId();
        if (StringUtils.isBlank(queueId)) {
            throw new IllegalArgumentException("MessageQueueDefinition queueId/queueName must not be blank");
        }

        String json = toJson(queueDef);
        localFallbackCache.put(queueId, json);

        RemoteCache<String, String> cache = getRemoteCache();
        if (cache != null) {
            try {
                cache.put(queueId, json);
                log.info("Persisted MessageQueueDefinition [{}] to remote Infinispan cache [{}]", queueId, QUEUE_CACHE_NAME);
            } catch (Exception e) {
                log.warn("Failed persisting MessageQueueDefinition [{}] to remote cache: {}", queueId, e.getMessage());
            }
        } else {
            log.debug("Remote cache unavailable; saved MessageQueueDefinition [{}] to local fallback cache", queueId);
        }
        return queueDef;
    }

    public Optional<PetasosQueueDefinition> getById(String queueId) {
        if (StringUtils.isBlank(queueId)) {
            return Optional.empty();
        }

        RemoteCache<String, String> cache = getRemoteCache();
        if (cache != null) {
            try {
                String json = cache.get(queueId);
                if (StringUtils.isNotBlank(json)) {
                    localFallbackCache.put(queueId, json);
                    return Optional.ofNullable(fromJson(json));
                }
            } catch (Exception e) {
                log.debug("Remote cache lookup failed for MessageQueueDefinition [{}]: {}", queueId, e.getMessage());
            }
        }

        String fallbackJson = localFallbackCache.get(queueId);
        if (StringUtils.isNotBlank(fallbackJson)) {
            return Optional.ofNullable(fromJson(fallbackJson));
        }

        return Optional.empty();
    }

    public boolean delete(String queueId) {
        if (StringUtils.isBlank(queueId)) {
            return false;
        }
        boolean removed = localFallbackCache.remove(queueId) != null;
        RemoteCache<String, String> cache = getRemoteCache();
        if (cache != null) {
            try {
                String old = cache.remove(queueId);
                if (old != null) {
                    removed = true;
                }
                log.info("Deleted MessageQueueDefinition [{}] from remote Infinispan cache [{}]", queueId, QUEUE_CACHE_NAME);
            } catch (Exception e) {
                log.warn("Failed deleting MessageQueueDefinition [{}] from remote cache: {}", queueId, e.getMessage());
            }
        }
        return removed;
    }

    public List<PetasosQueueDefinition> getAll() {
        Map<String, PetasosQueueDefinition> resultMap = new LinkedHashMap<>();

        RemoteCache<String, String> cache = getRemoteCache();
        if (cache != null) {
            try {
                Collection<String> values = cache.values();
                if (values != null && !values.isEmpty()) {
                    for (String json : values) {
                        PetasosQueueDefinition def = fromJson(json);
                        if (def != null && def.getQueueId() != null) {
                            resultMap.put(def.getQueueId(), def);
                            localFallbackCache.put(def.getQueueId(), json);
                        }
                    }
                }
            } catch (Exception e) {
                log.debug("Remote cache values query failed for [{}]: {}", QUEUE_CACHE_NAME, e.getMessage());
            }

            if (resultMap.isEmpty()) {
                try {
                    Set<String> keys = cache.keySet();
                    if (keys != null) {
                        for (String key : keys) {
                            String json = cache.get(key);
                            if (StringUtils.isNotBlank(json)) {
                                PetasosQueueDefinition def = fromJson(json);
                                if (def != null && def.getQueueId() != null) {
                                    resultMap.put(def.getQueueId(), def);
                                    localFallbackCache.put(def.getQueueId(), json);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    log.debug("Remote cache keySet scan failed for [{}]: {}", QUEUE_CACHE_NAME, e.getMessage());
                }
            }
        }

        for (Map.Entry<String, String> entry : localFallbackCache.entrySet()) {
            if (!resultMap.containsKey(entry.getKey())) {
                PetasosQueueDefinition def = fromJson(entry.getValue());
                if (def != null && def.getQueueId() != null) {
                    resultMap.put(def.getQueueId(), def);
                }
            }
        }

        if (resultMap.isEmpty()) {
            log.info("No MessageQueueDefinitions found in cache/JPA store. Seeding default queue definitions...");
            List<PetasosQueueDefinition> seeded = seedDefaultQueues(queueConfig);
            for (PetasosQueueDefinition q : seeded) {
                resultMap.put(q.getQueueId(), q);
            }
        }

        return new ArrayList<>(resultMap.values());
    }

    public synchronized List<PetasosQueueDefinition> seedDefaultQueues(QueueConfig config) {
        List<PetasosQueueDefinition> seededList = new ArrayList<>();

        String taskQueue = (config != null) ? config.getQueueName() : QueueConfig.DEFAULT_QUEUE_NAME;
        PetasosQueueDefinition processingQueue = new PetasosQueueDefinition(
                taskQueue, taskQueue, taskQueue, "ANYCAST", false, "HIE Asynchronous Task Processing Queue", null
        );
        seededList.add(save(processingQueue));

        String eventQueue = (config != null) ? config.getEventQueueName() : QueueConfig.DEFAULT_EVENT_QUEUE_NAME;
        PetasosQueueDefinition baseEventQueue = new PetasosQueueDefinition(
                eventQueue, eventQueue, eventQueue, "ANYCAST", false, "HIE Baseline Ingestion Task Event Queue", null
        );
        seededList.add(save(baseEventQueue));

        String prQueue = (config != null) ? config.getProviderRegistryChangeQueue() : QueueConfig.DEFAULT_PROVIDER_REGISTRY_QUEUE;
        PetasosQueueDefinition prQueueDef = new PetasosQueueDefinition(
                prQueue, prQueue, prQueue, "ANYCAST", true, "FHIR Provider Registry Governed Change Request Queue", null
        );
        seededList.add(save(prQueueDef));

        String defaultGateway = (config != null)
                ? config.getDedicatedEventQueueName(QueueConfig.DEFAULT_GATEWAY_INSTANCE_ID)
                : QueueConfig.DEFAULT_EVENT_QUEUE_PREFIX + "." + QueueConfig.DEFAULT_GATEWAY_INSTANCE_ID;
        PetasosQueueDefinition defaultGwQueue = new PetasosQueueDefinition(
                defaultGateway, defaultGateway, defaultGateway, "ANYCAST", false, "Default MLLP Gateway Dedicated Task Event Queue", QueueConfig.DEFAULT_GATEWAY_INSTANCE_ID
        );
        seededList.add(save(defaultGwQueue));

        if (config != null) {
            for (String qName : config.getGatewayEventQueues()) {
                if (!qName.equals(taskQueue) && !qName.equals(eventQueue) && !qName.equals(defaultGateway)) {
                    PetasosQueueDefinition extraQueue = new PetasosQueueDefinition(
                            qName, qName, qName, "ANYCAST", false, "Configured Gateway Task Event Queue: " + qName, null
                    );
                    seededList.add(save(extraQueue));
                }
            }
        }

        log.info("Seeded and persisted {} default MessageQueueDefinition(s) to cache/JPA store", seededList.size());
        return seededList;
    }
}
