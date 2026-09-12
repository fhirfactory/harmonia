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

package net.fhirfactory.hie.mllpgateway.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.Provenance;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@ApplicationScoped
public class DefaultProvenanceService implements ProvenanceService {

    private static final Logger log = LoggerFactory.getLogger(DefaultProvenanceService.class);
    public static final String PROVENANCE_CACHE_NAME = "provenance-cache";

    @Inject
    private RemoteCacheManager remoteCacheManager;

    @Inject
    private FhirContext fhirContext;

    private final Map<String, Provenance> provenanceStore = new ConcurrentHashMap<>();

    public DefaultProvenanceService() {
    }

    public DefaultProvenanceService(RemoteCacheManager remoteCacheManager, FhirContext fhirContext) {
        this.remoteCacheManager = remoteCacheManager;
        this.fhirContext = fhirContext;
    }

    private RemoteCache<String, String> getRemoteCache() {
        if (remoteCacheManager != null && remoteCacheManager.isStarted()) {
            try {
                return remoteCacheManager.getCache(PROVENANCE_CACHE_NAME);
            } catch (Exception e) {
                log.debug("Remote cache [{}] query failed in MLLP Gateway: {}", PROVENANCE_CACHE_NAME, e.getMessage());
            }
        }
        return null;
    }

    private IParser getJsonParser() {
        return (fhirContext != null ? fhirContext : FhirContext.forR5()).newJsonParser().setPrettyPrint(true);
    }

    @Override
    public Provenance create(Provenance provenance) {
        if (provenance == null) {
            throw new IllegalArgumentException("Provenance cannot be null");
        }
        String id = extractId(provenance);
        if (StringUtils.isBlank(id)) {
            id = UUID.randomUUID().toString();
            provenance.setId("Provenance/" + id);
        } else {
            provenance.setId("Provenance/" + id);
        }
        if (provenance.getRecorded() == null) {
            provenance.setRecorded(new Date());
        }
        provenanceStore.put(id, provenance);

        // Write Provenance to Infinispan remote cache
        RemoteCache<String, String> remoteCache = getRemoteCache();
        if (remoteCache != null) {
            try {
                String json = getJsonParser().encodeResourceToString(provenance);
                remoteCache.put(id, json);
                log.info("Persisted Provenance/{} to remote Infinispan cache [{}]", id, PROVENANCE_CACHE_NAME);
            } catch (Exception e) {
                log.warn("Could not persist Provenance/{} to remote Infinispan cache: {}", id, e.getMessage());
            }
        }

        log.info("Created Provenance with id: {}", id);
        return provenance;
    }

    @Override
    public Optional<Provenance> getById(String id) {
        if (StringUtils.isBlank(id)) {
            return Optional.empty();
        }
        String cleanId = cleanId(id);

        // Try Infinispan remote cache first
        RemoteCache<String, String> remoteCache = getRemoteCache();
        if (remoteCache != null) {
            try {
                String json = remoteCache.get(cleanId);
                if (json != null) {
                    Provenance parsed = getJsonParser().parseResource(Provenance.class, json);
                    if (parsed != null) {
                        provenanceStore.put(cleanId, parsed);
                        return Optional.of(parsed);
                    }
                }
            } catch (Exception e) {
                log.debug("Remote cache lookup for Provenance/{} failed: {}", cleanId, e.getMessage());
            }
        }

        return Optional.ofNullable(provenanceStore.get(cleanId));
    }

    @Override
    public Provenance update(String id, Provenance provenance) {
        if (StringUtils.isBlank(id) || provenance == null) {
            throw new IllegalArgumentException("ID and Provenance cannot be null/blank");
        }
        String cleanId = cleanId(id);
        provenance.setId("Provenance/" + cleanId);
        provenanceStore.put(cleanId, provenance);

        // Update in remote cache
        RemoteCache<String, String> remoteCache = getRemoteCache();
        if (remoteCache != null) {
            try {
                String json = getJsonParser().encodeResourceToString(provenance);
                remoteCache.put(cleanId, json);
                log.info("Updated Provenance/{} in remote Infinispan cache [{}]", cleanId, PROVENANCE_CACHE_NAME);
            } catch (Exception e) {
                log.warn("Could not update Provenance/{} in remote Infinispan cache: {}", cleanId, e.getMessage());
            }
        }

        return provenance;
    }

    @Override
    public boolean delete(String id) {
        if (StringUtils.isBlank(id)) {
            return false;
        }
        String cleanId = cleanId(id);
        boolean removed = provenanceStore.remove(cleanId) != null;

        // Delete from remote cache
        RemoteCache<String, String> remoteCache = getRemoteCache();
        if (remoteCache != null) {
            try {
                remoteCache.remove(cleanId);
                log.info("Deleted Provenance/{} from remote Infinispan cache [{}]", cleanId, PROVENANCE_CACHE_NAME);
            } catch (Exception e) {
                log.warn("Could not delete Provenance/{} from remote Infinispan cache: {}", cleanId, e.getMessage());
            }
        }

        return removed;
    }

    @Override
    public List<Provenance> getAll() {
        // Sync from remote cache if available
        RemoteCache<String, String> remoteCache = getRemoteCache();
        if (remoteCache != null) {
            try {
                for (Map.Entry<String, String> entry : remoteCache.entrySet()) {
                    if (!provenanceStore.containsKey(entry.getKey())) {
                        try {
                            Provenance p = getJsonParser().parseResource(Provenance.class, entry.getValue());
                            if (p != null) {
                                provenanceStore.put(entry.getKey(), p);
                            }
                        } catch (Exception ignored) {}
                    }
                }
            } catch (Exception e) {
                log.debug("Remote cache bulk sync for provenances failed: {}", e.getMessage());
            }
        }

        return new ArrayList<>(provenanceStore.values());
    }

    @Override
    public List<Provenance> search(String id, String target, String agent, String patient) {
        return getAll().stream()
                .filter(p -> StringUtils.isBlank(id) || matchesId(p, id))
                .filter(p -> StringUtils.isBlank(target) || matchesTarget(p, target))
                .filter(p -> StringUtils.isBlank(agent) || matchesAgent(p, agent))
                .filter(p -> StringUtils.isBlank(patient) || matchesPatient(p, patient))
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        RemoteCache<String, String> remoteCache = getRemoteCache();
        if (remoteCache != null) {
            try {
                return remoteCache.size();
            } catch (Exception ignored) {}
        }
        return provenanceStore.size();
    }

    @Override
    public void clear() {
        provenanceStore.clear();
        RemoteCache<String, String> remoteCache = getRemoteCache();
        if (remoteCache != null) {
            try {
                remoteCache.clear();
            } catch (Exception ignored) {}
        }
    }

    private String extractId(Provenance provenance) {
        if (provenance.getIdElement() != null && !provenance.getIdElement().isEmpty()) {
            return provenance.getIdElement().getIdPart();
        }
        if (provenance.getId() != null) {
            return cleanId(provenance.getId());
        }
        return null;
    }

    private String cleanId(String id) {
        if (id == null) return null;
        if (id.startsWith("Provenance/")) {
            return id.substring("Provenance/".length());
        }
        return id;
    }

    private boolean matchesId(Provenance provenance, String id) {
        String cleanSearchId = cleanId(id);
        String provId = extractId(provenance);
        return cleanSearchId.equalsIgnoreCase(provId);
    }

    private boolean matchesTarget(Provenance provenance, String target) {
        if (provenance.getTarget() == null || provenance.getTarget().isEmpty()) {
            return false;
        }
        String cleanTarget = target.startsWith("Task/") ? target.substring("Task/".length()) : target;
        return provenance.getTarget().stream().anyMatch(t -> {
            if (t.getReference() == null) return false;
            return t.getReference().equalsIgnoreCase(target) ||
                    t.getReference().endsWith(cleanTarget) ||
                    (t.getResource() != null && cleanTarget.equalsIgnoreCase(t.getResource().getIdElement().getIdPart()));
        });
    }

    private boolean matchesAgent(Provenance provenance, String agent) {
        if (provenance.getAgent() == null || provenance.getAgent().isEmpty()) {
            return false;
        }
        return provenance.getAgent().stream().anyMatch(a -> {
            if (a.getWho() == null) return false;
            String ref = a.getWho().getReference();
            String disp = a.getWho().getDisplay();
            return (ref != null && ref.toLowerCase().contains(agent.toLowerCase())) ||
                    (disp != null && disp.toLowerCase().contains(agent.toLowerCase()));
        });
    }

    private boolean matchesPatient(Provenance provenance, String patient) {
        if (provenance.hasPatient()) {
            String ref = provenance.getPatient().getReference();
            String disp = provenance.getPatient().getDisplay();
            String cleanPatient = patient.startsWith("Patient/") ? patient.substring("Patient/".length()) : patient;
            return (ref != null && (ref.equalsIgnoreCase(patient) || ref.endsWith(cleanPatient))) ||
                    (disp != null && disp.toLowerCase().contains(patient.toLowerCase()));
        }
        return false;
    }
}
