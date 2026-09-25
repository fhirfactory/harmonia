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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.model.ergon.ErgonReasonEnum;
import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.Task;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link TaskService} managing FHIR Task (Pragma) caching and retrieval.
 * <p>
 * <b>Pragma — Task Instance/State</b>: This particular piece of work as it currently exists - will be used
 * wherever Task (as a synthetic FHIR::Task resource) is used within the codebase.
 */
@ApplicationScoped
public class DefaultTaskService implements TaskService {

    private static final Logger log = LoggerFactory.getLogger(DefaultTaskService.class);
    public static final String TASK_CACHE_NAME = "task-cache";

    @Inject
    private RemoteCacheManager remoteCacheManager;

    @Inject
    private FhirContext fhirContext;

    public DefaultTaskService() {
    }

    public DefaultTaskService(RemoteCacheManager remoteCacheManager) {
        this.remoteCacheManager = remoteCacheManager;
    }

    public DefaultTaskService(RemoteCacheManager remoteCacheManager, FhirContext fhirContext) {
        this.remoteCacheManager = remoteCacheManager;
        this.fhirContext = fhirContext;
    }

    public RemoteCacheManager getRemoteCacheManager() {
        return remoteCacheManager;
    }

    public void setRemoteCacheManager(RemoteCacheManager remoteCacheManager) {
        this.remoteCacheManager = remoteCacheManager;
    }

    public FhirContext getFhirContext() {
        if (fhirContext == null) {
            fhirContext = FhirContext.forR5();
        }
        return fhirContext;
    }

    public void setFhirContext(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
    }

    private RemoteCache<String, String> getRemoteCache() {
        if (remoteCacheManager != null && remoteCacheManager.isStarted()) {
            try {
                return remoteCacheManager.getCache(TASK_CACHE_NAME);
            } catch (Exception e) {
                log.debug("Remote cache [{}] query failed in MLLP Gateway: {}", TASK_CACHE_NAME, e.getMessage());
            }
        }
        return null;
    }

    private RemoteCache<String, String> requireRemoteCache() {
        RemoteCache<String, String> cache = getRemoteCache();
        if (cache == null) {
            throw new IllegalStateException("Mneme cache [" + TASK_CACHE_NAME + "] is unavailable");
        }
        return cache;
    }

    private IParser getJsonParser() {
        return getFhirContext().newJsonParser().setPrettyPrint(true);
    }

    @Override
    public Task create(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        String id = extractId(task);
        if (StringUtils.isBlank(id)) {
            id = UUID.randomUUID().toString();
            task.setId("Task/" + id);
        } else {
            task.setId("Task/" + id);
        }
        task.setLastModified(new Date());
        if (task.getAuthoredOn() == null) {
            task.setAuthoredOn(new Date());
        }
        ErgonReasonEnum.ensureSyntheticTaskReason(task);
        FhirSecurityTagManager.applyDefaultSecurityTag(task);

        RemoteCache<String, String> remoteCache = requireRemoteCache();
        String json = getJsonParser().encodeResourceToString(task);
        remoteCache.put(id, json);
        log.info("Persisted Task/{} to remote Infinispan cache [{}]", id, TASK_CACHE_NAME);
        return task;
    }

    @Override
    public Optional<Task> getById(String id) {
        if (StringUtils.isBlank(id)) {
            return Optional.empty();
        }
        String cleanId = cleanId(id);
        RemoteCache<String, String> remoteCache = requireRemoteCache();
        String json = remoteCache.get(cleanId);
        if (json == null || json.isBlank()) {
            return Optional.empty();
        }
        Task parsed = getJsonParser().parseResource(Task.class, json);
        return Optional.ofNullable(parsed);
    }

    @Override
    public Task update(String id, Task task) {
        if (StringUtils.isBlank(id) || task == null) {
            throw new IllegalArgumentException("ID and Task cannot be null/blank");
        }
        String cleanId = cleanId(id);
        task.setId("Task/" + cleanId);
        task.setLastModified(new Date());
        ErgonReasonEnum.ensureSyntheticTaskReason(task);
        FhirSecurityTagManager.applyDefaultSecurityTag(task);

        RemoteCache<String, String> remoteCache = requireRemoteCache();
        String json = getJsonParser().encodeResourceToString(task);
        remoteCache.put(cleanId, json);
        log.info("Updated Task/{} in remote Infinispan cache [{}]", cleanId, TASK_CACHE_NAME);
        return task;
    }

    @Override
    public boolean delete(String id) {
        if (StringUtils.isBlank(id)) {
            return false;
        }
        String cleanId = cleanId(id);
        RemoteCache<String, String> remoteCache = requireRemoteCache();
        String removed = remoteCache.remove(cleanId);
        log.info("Deleted Task/{} from remote Infinispan cache [{}]", cleanId, TASK_CACHE_NAME);
        return removed != null;
    }

    public List<Task> getAll() {
        RemoteCache<String, String> remoteCache = requireRemoteCache();
        Collection<String> values = remoteCache.values();
        if (values == null || values.isEmpty()) {
            return Collections.emptyList();
        }
        List<Task> tasks = new ArrayList<>(values.size());
        for (String json : values) {
            if (json != null && !json.isBlank()) {
                try {
                    Task t = getJsonParser().parseResource(Task.class, json);
                    if (t != null) {
                        tasks.add(t);
                    }
                } catch (Exception e) {
                    log.debug("Failed parsing Task JSON from cache: {}", e.getMessage());
                }
            }
        }
        return tasks;
    }

    @Override
    public List<Task> search(String id, String status, String priority, String patient, String identifier, String text) {
        return getAll().stream()
                .filter(t -> StringUtils.isBlank(id) || matchesId(t, id))
                .filter(t -> StringUtils.isBlank(status) || matchesStatus(t, status))
                .filter(t -> StringUtils.isBlank(priority) || matchesPriority(t, priority))
                .filter(t -> StringUtils.isBlank(patient) || matchesPatient(t, patient))
                .filter(t -> StringUtils.isBlank(identifier) || matchesIdentifier(t, identifier))
                .filter(t -> StringUtils.isBlank(text) || matchesText(t, text))
                .collect(Collectors.toList());
    }

    @Override
    public int count() {
        RemoteCache<String, String> remoteCache = requireRemoteCache();
        return (int) remoteCache.size();
    }

    @Override
    public void clear() {
        RemoteCache<String, String> remoteCache = requireRemoteCache();
        remoteCache.clear();
    }

    private String extractId(Task task) {
        if (task.getIdElement() != null && !task.getIdElement().isEmpty()) {
            return task.getIdElement().getIdPart();
        }
        if (task.getId() != null) {
            return cleanId(task.getId());
        }
        return null;
    }

    private String cleanId(String id) {
        if (id == null) return null;
        if (id.startsWith("Task/")) {
            return id.substring("Task/".length());
        }
        return id;
    }

    private boolean matchesId(Task task, String id) {
        String cleanSearchId = cleanId(id);
        String taskId = extractId(task);
        return cleanSearchId.equalsIgnoreCase(taskId);
    }

    private boolean matchesStatus(Task task, String status) {
        if (task.getStatus() == null) return false;
        return status.equalsIgnoreCase(task.getStatus().toCode());
    }

    private boolean matchesPriority(Task task, String priority) {
        if (task.getPriority() == null) return false;
        return priority.equalsIgnoreCase(task.getPriority().toCode());
    }

    private boolean matchesPatient(Task task, String patient) {
        if (task.getFor() == null) return false;
        String ref = task.getFor().getReference();
        String disp = task.getFor().getDisplay();
        String cleanPatient = patient.startsWith("Patient/") ? patient.substring("Patient/".length()) : patient;
        return (ref != null && (ref.equalsIgnoreCase(patient) || ref.endsWith(cleanPatient))) ||
                (disp != null && disp.toLowerCase().contains(patient.toLowerCase()));
    }

    private boolean matchesIdentifier(Task task, String identifier) {
        if (task.getIdentifier() == null) return false;
        return task.getIdentifier().stream()
                .anyMatch(i -> identifier.equalsIgnoreCase(i.getValue()));
    }

    private boolean matchesText(Task task, String text) {
        String lowerText = text.toLowerCase();
        if (task.getDescription() != null && task.getDescription().toLowerCase().contains(lowerText)) {
            return true;
        }
        if (task.getCode() != null && task.getCode().getText() != null &&
                task.getCode().getText().toLowerCase().contains(lowerText)) {
            return true;
        }
        return false;
    }
}
