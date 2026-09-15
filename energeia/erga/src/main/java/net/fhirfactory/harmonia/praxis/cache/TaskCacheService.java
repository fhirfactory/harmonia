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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.model.ergon.ErgonReasonEnum;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaFhirConverter;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.Annotation;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Provenance;
import org.hl7.fhir.r5.model.Task;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for caching and managing Task (Pragma) and Provenance resources within Infinispan (Mneme) and local fallback storage.
 * <p>
 * <b>Pragma — Task Instance/State</b>: This particular piece of work as it currently exists - will be used
 * wherever Task (as a synthetic FHIR::Task resource) is used within the codebase.
 */
@ApplicationScoped
public class TaskCacheService {

    private static final Logger log = LoggerFactory.getLogger(TaskCacheService.class);
    public static final String TASK_CACHE_NAME = "task-cache";
    public static final String PROVENANCE_CACHE_NAME = "provenance-cache";

    @Inject
    private RemoteCacheManager remoteCacheManager;

    @Inject
    private FhirContext fhirContext;

    private final Map<String, String> localFallbackCache = new ConcurrentHashMap<>();
    private final Map<String, String> localFallbackProvenanceCache = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public TaskCacheService() {
    }

    public TaskCacheService(RemoteCacheManager remoteCacheManager, FhirContext fhirContext) {
        this.remoteCacheManager = remoteCacheManager;
        this.fhirContext = fhirContext;
    }

    @PostConstruct
    public void init() {
        if (this.fhirContext == null) {
            this.fhirContext = FhirContext.forR5();
        }
    }

    public IParser getJsonParser() {
        return (fhirContext != null ? fhirContext : FhirContext.forR5()).newJsonParser().setPrettyPrint(true);
    }

    private RemoteCache<String, String> getRemoteCache() {
        if (remoteCacheManager != null && remoteCacheManager.isStarted()) {
            try {
                return remoteCacheManager.getCache(TASK_CACHE_NAME);
            } catch (Exception e) {
                log.debug("Remote cache [{}] query failed: {}", TASK_CACHE_NAME, e.getMessage());
            }
        }
        return null;
    }

    private RemoteCache<String, String> getProvenanceRemoteCache() {
        if (remoteCacheManager != null && remoteCacheManager.isStarted()) {
            try {
                return remoteCacheManager.getCache(PROVENANCE_CACHE_NAME);
            } catch (Exception e) {
                log.debug("Remote cache [{}] query failed: {}", PROVENANCE_CACHE_NAME, e.getMessage());
            }
        }
        return null;
    }

    public String getTaskJson(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        String cleanId = cleanId(id);
        RemoteCache<String, String> remoteCache = getRemoteCache();
        if (remoteCache != null) {
            try {
                String json = remoteCache.get(cleanId);
                if (json != null) {
                    return json;
                }
            } catch (Exception e) {
                log.warn("Error reading Task/{} from remote cache [{}]: {}", cleanId, TASK_CACHE_NAME, e.getMessage());
            }
        }
        return localFallbackCache.get(cleanId);
    }

    public Optional<Task> getTask(String id) {
        String json = getTaskJson(id);
        if (StringUtils.isBlank(json)) {
            return Optional.empty();
        }
        try {
            Task task = getJsonParser().parseResource(Task.class, json);
            return Optional.ofNullable(task);
        } catch (Exception e) {
            log.error("Failed to parse cached Task JSON for id: {}", id, e);
            return Optional.empty();
        }
    }

    public Pragma savePragma(Pragma pragma) {
        if (pragma == null) {
            throw new IllegalArgumentException("Pragma cannot be null");
        }
        if (StringUtils.isBlank(pragma.getPragmaId())) {
            pragma.setPragmaId(UUID.randomUUID().toString());
        }
        pragma.touch();

        try {
            String json = objectMapper.writeValueAsString(pragma);
            putTaskJson(pragma.getPragmaId(), json);
            log.info("Persisted Pragma/{} to cache [{}]", pragma.getPragmaId(), TASK_CACHE_NAME);
        } catch (Exception e) {
            log.error("Failed to serialize Pragma/{} for caching", pragma.getPragmaId(), e);
        }

        return pragma;
    }

    public Optional<Pragma> getPragma(String id) {
        String json = getTaskJson(id);
        if (StringUtils.isBlank(json)) {
            return Optional.empty();
        }
        try {
            if (json.contains("\"pragmaId\"") || json.contains("\"checkpoints\"")) {
                Pragma pragma = objectMapper.readValue(json, Pragma.class);
                return Optional.ofNullable(pragma);
            } else if (json.contains("\"resourceType\"") && json.contains("\"Task\"")) {
                Task task = getJsonParser().parseResource(Task.class, json);
                return Optional.ofNullable(PragmaFhirConverter.fromFhirTask(task));
            }
        } catch (Exception e) {
            log.warn("Failed to parse cached Pragma JSON for id {}: {}", id, e.getMessage());
        }
        return Optional.empty();
    }

    public boolean deletePragma(String id) {
        return deleteTask(id);
    }

    public Task saveTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        String cleanId = extractId(task);
        if (StringUtils.isBlank(cleanId)) {
            cleanId = UUID.randomUUID().toString();
        }
        task.setId("Task/" + cleanId);
        if (task.getLastModified() == null) {
            task.setLastModified(new Date());
        }
        if (task.getAuthoredOn() == null) {
            task.setAuthoredOn(new Date());
        }
        ErgonReasonEnum.ensureSyntheticTaskReason(task);

        String json = getJsonParser().encodeResourceToString(task);
        putTaskJson(cleanId, json);
        return task;
    }

    public void putTaskJson(String id, String jsonPayload) {
        String cleanId = cleanId(id);
        RemoteCache<String, String> remoteCache = getRemoteCache();
        if (remoteCache != null) {
            try {
                remoteCache.put(cleanId, jsonPayload);
                log.info("Persisted Task/{} to Infinispan remote cache [{}]", cleanId, TASK_CACHE_NAME);
            } catch (Exception e) {
                log.warn("Error putting Task/{} to remote Infinispan cache, falling back to local: {}", cleanId, e.getMessage());
                localFallbackCache.put(cleanId, jsonPayload);
            }
        } else {
            localFallbackCache.put(cleanId, jsonPayload);
            log.info("Persisted Task/{} to local fallback cache [{}]", cleanId, TASK_CACHE_NAME);
        }
    }

    public Provenance saveProvenance(Provenance provenance) {
        if (provenance == null) {
            throw new IllegalArgumentException("Provenance cannot be null");
        }
        String cleanId = extractProvenanceId(provenance);
        if (StringUtils.isBlank(cleanId)) {
            cleanId = UUID.randomUUID().toString();
        }
        provenance.setId("Provenance/" + cleanId);
        if (provenance.getRecorded() == null) {
            provenance.setRecorded(new Date());
        }

        String json = getJsonParser().encodeResourceToString(provenance);
        putProvenanceJson(cleanId, json);
        return provenance;
    }

    public void putProvenanceJson(String id, String jsonPayload) {
        String cleanId = cleanProvenanceId(id);
        RemoteCache<String, String> remoteCache = getProvenanceRemoteCache();
        if (remoteCache != null) {
            try {
                remoteCache.put(cleanId, jsonPayload);
                log.info("Persisted Provenance/{} to Infinispan remote cache [{}]", cleanId, PROVENANCE_CACHE_NAME);
            } catch (Exception e) {
                log.warn("Error putting Provenance/{} to remote Infinispan cache, falling back to local: {}", cleanId, e.getMessage());
                localFallbackProvenanceCache.put(cleanId, jsonPayload);
            }
        } else {
            localFallbackProvenanceCache.put(cleanId, jsonPayload);
            log.info("Persisted Provenance/{} to local fallback cache [{}]", cleanId, PROVENANCE_CACHE_NAME);
        }
    }

    public Optional<Provenance> getProvenance(String id) {
        String json = getProvenanceJson(id);
        if (StringUtils.isBlank(json)) {
            return Optional.empty();
        }
        try {
            Provenance provenance = getJsonParser().parseResource(Provenance.class, json);
            return Optional.ofNullable(provenance);
        } catch (Exception e) {
            log.error("Failed to parse cached Provenance JSON for id: {}", id, e);
            return Optional.empty();
        }
    }

    public String getProvenanceJson(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        String cleanId = cleanProvenanceId(id);
        RemoteCache<String, String> remoteCache = getProvenanceRemoteCache();
        if (remoteCache != null) {
            try {
                String json = remoteCache.get(cleanId);
                if (json != null) {
                    return json;
                }
            } catch (Exception e) {
                log.warn("Error reading Provenance/{} from remote cache [{}]: {}", cleanId, PROVENANCE_CACHE_NAME, e.getMessage());
            }
        }
        return localFallbackProvenanceCache.get(cleanId);
    }

    public boolean deleteProvenance(String id) {
        if (StringUtils.isBlank(id)) {
            return false;
        }
        String cleanId = cleanProvenanceId(id);
        boolean removed = localFallbackProvenanceCache.remove(cleanId) != null;
        RemoteCache<String, String> remoteCache = getProvenanceRemoteCache();
        if (remoteCache != null) {
            try {
                String prev = remoteCache.remove(cleanId);
                if (prev != null) removed = true;
            } catch (Exception e) {
                log.warn("Error removing Provenance/{} from remote Infinispan cache: {}", cleanId, e.getMessage());
            }
        }
        return removed;
    }

    public Task markTaskAsProcessed(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }

        String cleanId = extractId(task);
        if (StringUtils.isBlank(cleanId)) {
            cleanId = UUID.randomUUID().toString();
            task.setId("Task/" + cleanId);
        } else {
            task.setId("Task/" + cleanId);
        }

        Optional<Task> existing = getTask(cleanId);
        if (existing.isPresent()) {
            Task existingTask = existing.get();
            if (task.getAuthoredOn() == null && existingTask.getAuthoredOn() != null) {
                task.setAuthoredOn(existingTask.getAuthoredOn());
            }
            if (task.getFor() == null && existingTask.getFor() != null) {
                task.setFor(existingTask.getFor());
            }
            if (task.getFocus() == null && existingTask.getFocus() != null) {
                task.setFocus(existingTask.getFocus());
            }
            if (task.getContained() == null || task.getContained().isEmpty()) {
                if (existingTask.getContained() != null && !existingTask.getContained().isEmpty()) {
                    existingTask.getContained().forEach(task::addContained);
                }
            }
        }

        task.setStatus(Task.TaskStatus.COMPLETED);
        task.setLastModified(new Date());
        ErgonReasonEnum.ensureSyntheticTaskReason(task);

        CodeableConcept businessStatus = new CodeableConcept();
        businessStatus.setText("PROCESSED");
        businessStatus.addCoding()
                .setSystem("http://terminology.hl7.org/CodeSystem/task-business-status")
                .setCode("processed")
                .setDisplay("Task Processed");
        task.setBusinessStatus(businessStatus);

        Annotation note = new Annotation();
        note.setText("Processed by HIE task-processor at " + new Date());
        note.setTime(new Date());
        task.addNote(note);

        saveTask(task);
        log.info("Successfully marked Task/{} as PROCESSED in Infinispan cache", cleanId);
        return task;
    }

    public Optional<Task> markTaskAsProcessed(String id) {
        if (StringUtils.isBlank(id)) {
            return Optional.empty();
        }
        String cleanId = cleanId(id);
        Optional<Task> existing = getTask(cleanId);
        if (existing.isEmpty()) {
            Task task = new Task();
            task.setId("Task/" + cleanId);
            task.setAuthoredOn(new Date());
            return Optional.of(markTaskAsProcessed(task));
        }
        return Optional.of(markTaskAsProcessed(existing.get()));
    }

    public boolean deleteTask(String id) {
        if (StringUtils.isBlank(id)) {
            return false;
        }
        String cleanId = cleanId(id);
        boolean removed = localFallbackCache.remove(cleanId) != null;
        RemoteCache<String, String> remoteCache = getRemoteCache();
        if (remoteCache != null) {
            try {
                String prev = remoteCache.remove(cleanId);
                if (prev != null) removed = true;
            } catch (Exception e) {
                log.warn("Error removing Task/{} from remote Infinispan cache: {}", cleanId, e.getMessage());
            }
        }
        return removed;
    }

    public void clear() {
        localFallbackCache.clear();
        localFallbackProvenanceCache.clear();
        RemoteCache<String, String> remoteCache = getRemoteCache();
        if (remoteCache != null) {
            try {
                remoteCache.clear();
            } catch (Exception ignored) {
            }
        }
        RemoteCache<String, String> provCache = getProvenanceRemoteCache();
        if (provCache != null) {
            try {
                provCache.clear();
            } catch (Exception ignored) {
            }
        }
    }

    public int count() {
        return localFallbackCache.size();
    }

    private String extractId(Task task) {
        if (task.getIdElement() != null && StringUtils.isNotBlank(task.getIdElement().getIdPart())) {
            return cleanId(task.getIdElement().getIdPart());
        }
        if (task.getId() != null && StringUtils.isNotBlank(task.getId())) {
            return cleanId(task.getId());
        }
        return null;
    }

    private String cleanId(String id) {
        if (id == null) return null;
        String clean = id.trim();
        if (clean.startsWith("Task/")) {
            clean = clean.substring("Task/".length());
        }
        return clean;
    }

    private String extractProvenanceId(Provenance provenance) {
        if (provenance.getIdElement() != null && StringUtils.isNotBlank(provenance.getIdElement().getIdPart())) {
            return cleanProvenanceId(provenance.getIdElement().getIdPart());
        }
        if (provenance.getId() != null && StringUtils.isNotBlank(provenance.getId())) {
            return cleanProvenanceId(provenance.getId());
        }
        return null;
    }

    private String cleanProvenanceId(String id) {
        if (id == null) return null;
        String clean = id.trim();
        if (clean.startsWith("Provenance/")) {
            clean = clean.substring("Provenance/".length());
        }
        return clean;
    }
}
