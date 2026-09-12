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

package net.fhirfactory.hie.befe.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.hie.model.sequence.TaskSequenceDefinition;
import net.fhirfactory.hie.model.topic.TopicSubscription;
import org.apache.commons.lang3.StringUtils;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BEFE service for accessing and managing persisted {@link TaskSequenceDefinition} configuration objects in the Infinispan cluster.
 */
@ApplicationScoped
public class TaskSequenceCacheService {

    private static final Logger log = LoggerFactory.getLogger(TaskSequenceCacheService.class);
    public static final String SEQUENCE_CACHE_NAME = "tasksequence-cache";

    @Inject
    private RemoteCacheManager remoteCacheManager;

    private ObjectMapper objectMapper;
    private final Map<String, String> localFallbackCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        this.objectMapper = new ObjectMapper();
        ensureDefaultSequences();
    }

    public synchronized void ensureDefaultSequences() {
        try {
            if (count() == 0) {
                seedDefaultSequences();
            }
        } catch (Exception e) {
            log.warn("Failed ensuring default task sequences during init: {}. Seeding local fallback.", e.getMessage());
            seedDefaultSequences();
        }
    }

    public void seedDefaultSequences() {
        // Patient Identity Update Sequence
        TaskSequenceDefinition patientIdSeq = new TaskSequenceDefinition(
                "seq-patient-identity-pipeline",
                "Patient Identity Update Sequence"
        );
        patientIdSeq.setSequenceDescription("Extracts, normalizes, and updates patient identity and demographics across all clinical messages");
        patientIdSeq.setDescription("Extracts, normalizes, and updates patient identity and demographics across all clinical messages");
        patientIdSeq.setVersion("1.0.0");
        patientIdSeq.setEnabled(true);
        patientIdSeq.setTopicSubscriptions(List.of(TopicSubscription.forAll()));
        patientIdSeq.setTargetGatewayInstances(List.of("*"));
        patientIdSeq.setTargetTriggerTypes(List.of("*"));

        Map<Integer, String> activities = new TreeMap<>();
        activities.put(0, "message-queue-to-exchange");
        activities.put(1, "patient-identity-update");
        activities.put(2, "patient-demographics-update");
        patientIdSeq.setActivityIds(activities);

        saveSequence(patientIdSeq);
        log.info("TaskSequenceCacheService seeded default Patient Identity Update Sequence (seq-patient-identity-pipeline)");
    }

    public ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
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

    public List<TaskSequenceDefinition> getAllSequences() {
        List<String> jsons = getAllSequenceJsons();
        List<TaskSequenceDefinition> list = new ArrayList<>();
        for (String json : jsons) {
            try {
                TaskSequenceDefinition def = getObjectMapper().readValue(json, TaskSequenceDefinition.class);
                if (def != null) {
                    list.add(def);
                }
            } catch (Exception e) {
                log.warn("Failed deserializing TaskSequenceDefinition: {}", e.getMessage());
            }
        }
        return list;
    }

    public List<String> getAllSequenceJsons() {
        Map<String, String> map = new LinkedHashMap<>();
        RemoteCache<String, String> cache = getRemoteCache();
        if (cache != null) {
            try {
                Collection<String> values = cache.values();
                if (values != null && !values.isEmpty()) {
                    for (String json : values) {
                        if (json != null) {
                            try {
                                JsonNode node = getObjectMapper().readTree(json);
                                String id = node.has("sequenceId") ? node.get("sequenceId").asText() : null;
                                if (id != null) {
                                    map.put(id, json);
                                } else {
                                    map.put(UUID.randomUUID().toString(), json);
                                }
                            } catch (Exception ignored) {
                                map.put(UUID.randomUUID().toString(), json);
                            }
                        }
                    }
                } else {
                    Set<String> keys = cache.keySet();
                    if (keys != null) {
                        for (String key : keys) {
                            String json = cache.get(key);
                            if (json != null) {
                                map.put(key, json);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("Error retrieving task sequences from remote cache: {}", e.getMessage());
            }
        }
        for (Map.Entry<String, String> entry : localFallbackCache.entrySet()) {
            if (!map.containsKey(entry.getKey())) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        if (map.isEmpty()) {
            seedDefaultSequences();
            return new ArrayList<>(localFallbackCache.values());
        }
        return new ArrayList<>(map.values());
    }

    public Optional<TaskSequenceDefinition> getSequence(String sequenceId) {
        return getSequenceJson(sequenceId).flatMap(json -> {
            try {
                return Optional.ofNullable(getObjectMapper().readValue(json, TaskSequenceDefinition.class));
            } catch (Exception e) {
                log.warn("Failed deserializing TaskSequenceDefinition [{}]: {}", sequenceId, e.getMessage());
                return Optional.empty();
            }
        });
    }

    public Optional<String> getSequenceJson(String sequenceId) {
        if (StringUtils.isBlank(sequenceId)) {
            return Optional.empty();
        }
        String cleanId = sequenceId.trim();
        RemoteCache<String, String> cache = getRemoteCache();
        if (cache != null) {
            try {
                String json = cache.get(cleanId);
                if (json != null) {
                    return Optional.of(json);
                }
            } catch (Exception e) {
                log.warn("Error reading sequence [{}] from remote cache: {}", cleanId, e.getMessage());
            }
        }
        return Optional.ofNullable(localFallbackCache.get(cleanId));
    }

    public TaskSequenceDefinition saveSequence(TaskSequenceDefinition definition) {
        if (definition == null) {
            throw new IllegalArgumentException("TaskSequenceDefinition cannot be null");
        }
        String id = definition.getSequenceId();
        if (StringUtils.isBlank(id)) {
            id = "seq-" + UUID.randomUUID().toString().substring(0, 8);
            definition.setSequenceId(id);
        }
        try {
            String json = getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(definition);
            saveSequence(id, json);
            return definition;
        } catch (Exception e) {
            log.error("Failed serializing TaskSequenceDefinition [{}]: {}", id, e.getMessage(), e);
            throw new RuntimeException("Serialization error: " + e.getMessage(), e);
        }
    }

    public String saveSequence(String sequenceId, String jsonPayload) {
        if (StringUtils.isBlank(jsonPayload)) {
            throw new IllegalArgumentException("Payload cannot be blank");
        }

        String actualId = sequenceId;
        try {
            JsonNode root = getObjectMapper().readTree(jsonPayload);
            if (StringUtils.isBlank(actualId) && root.has("sequenceId") && !root.get("sequenceId").asText().isBlank()) {
                actualId = root.get("sequenceId").asText();
            }
            if (StringUtils.isBlank(actualId)) {
                actualId = "seq-" + UUID.randomUUID().toString().substring(0, 8);
                if (root.isObject()) {
                    ((ObjectNode) root).put("sequenceId", actualId);
                    jsonPayload = getObjectMapper().writeValueAsString(root);
                }
            }
        } catch (Exception e) {
            log.warn("Failed inspecting sequence JSON payload: {}", e.getMessage());
            if (StringUtils.isBlank(actualId)) {
                actualId = "seq-" + UUID.randomUUID().toString().substring(0, 8);
            }
        }

        localFallbackCache.put(actualId, jsonPayload);
        RemoteCache<String, String> cache = getRemoteCache();
        if (cache != null) {
            try {
                cache.put(actualId, jsonPayload);
                log.info("BEFE saved TaskSequence [{}] to remote cache [{}]", actualId, SEQUENCE_CACHE_NAME);
            } catch (Exception e) {
                log.warn("Error writing sequence [{}] to remote cache: {}. Persisted in local fallback.", actualId, e.getMessage());
            }
        } else {
            log.info("BEFE saved TaskSequence [{}] to local fallback cache", actualId);
        }
        return jsonPayload;
    }

    public boolean deleteSequence(String sequenceId) {
        if (StringUtils.isBlank(sequenceId)) {
            return false;
        }
        String cleanId = sequenceId.trim();
        boolean removed = false;
        RemoteCache<String, String> cache = getRemoteCache();
        if (cache != null) {
            try {
                if (cache.remove(cleanId) != null) {
                    removed = true;
                }
            } catch (Exception e) {
                log.warn("Error deleting sequence [{}] from remote cache: {}", cleanId, e.getMessage());
            }
        }
        if (localFallbackCache.remove(cleanId) != null) {
            removed = true;
        }
        return removed;
    }

    public long count() {
        return getAllSequenceJsons().size();
    }

    public void setRemoteCacheManager(RemoteCacheManager remoteCacheManager) {
        this.remoteCacheManager = remoteCacheManager;
    }
}
