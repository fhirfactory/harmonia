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
import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
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

    public DefaultProvenanceService() {
    }

    public DefaultProvenanceService(RemoteCacheManager remoteCacheManager) {
        this.remoteCacheManager = remoteCacheManager;
    }

    public DefaultProvenanceService(RemoteCacheManager remoteCacheManager, FhirContext fhirContext) {
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
                return remoteCacheManager.getCache(PROVENANCE_CACHE_NAME);
            } catch (Exception e) {
                log.debug("Remote cache [{}] query failed in MLLP Gateway: {}", PROVENANCE_CACHE_NAME, e.getMessage());
            }
        }
        return null;
    }

    private RemoteCache<String, String> requireRemoteCache() {
        RemoteCache<String, String> cache = getRemoteCache();
        if (cache == null) {
            throw new IllegalStateException("Mneme cache [" + PROVENANCE_CACHE_NAME + "] is unavailable");
        }
        return cache;
    }

    private IParser getJsonParser() {
        return getFhirContext().newJsonParser().setPrettyPrint(true);
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
        FhirSecurityTagManager.applyDefaultSecurityTag(provenance);

        RemoteCache<String, String> remoteCache = requireRemoteCache();
        String json = getJsonParser().encodeResourceToString(provenance);
        remoteCache.put(id, json);
        log.info("Persisted Provenance/{} to remote Infinispan cache [{}]", id, PROVENANCE_CACHE_NAME);
        return provenance;
    }

    @Override
    public Optional<Provenance> getById(String id) {
        if (StringUtils.isBlank(id)) {
            return Optional.empty();
        }
        String cleanId = cleanId(id);
        RemoteCache<String, String> remoteCache = requireRemoteCache();
        String json = remoteCache.get(cleanId);
        if (json == null || json.isBlank()) {
            return Optional.empty();
        }
        Provenance parsed = getJsonParser().parseResource(Provenance.class, json);
        return Optional.ofNullable(parsed);
    }

    @Override
    public Provenance update(String id, Provenance provenance) {
        if (StringUtils.isBlank(id) || provenance == null) {
            throw new IllegalArgumentException("ID and Provenance cannot be null/blank");
        }
        String cleanId = cleanId(id);
        provenance.setId("Provenance/" + cleanId);
        FhirSecurityTagManager.applyDefaultSecurityTag(provenance);

        RemoteCache<String, String> remoteCache = requireRemoteCache();
        String json = getJsonParser().encodeResourceToString(provenance);
        remoteCache.put(cleanId, json);
        log.info("Updated Provenance/{} in remote Infinispan cache [{}]", cleanId, PROVENANCE_CACHE_NAME);
        return provenance;
    }

    @Override
    public boolean delete(String id) {
        if (StringUtils.isBlank(id)) {
            return false;
        }
        String cleanId = cleanId(id);
        RemoteCache<String, String> remoteCache = requireRemoteCache();
        String removed = remoteCache.remove(cleanId);
        log.info("Deleted Provenance/{} from remote Infinispan cache [{}]", cleanId, PROVENANCE_CACHE_NAME);
        return removed != null;
    }

    @Override
    public List<Provenance> getAll() {
        RemoteCache<String, String> remoteCache = requireRemoteCache();
        Collection<String> values = remoteCache.values();
        if (values == null || values.isEmpty()) {
            return Collections.emptyList();
        }
        List<Provenance> provenances = new ArrayList<>(values.size());
        for (String json : values) {
            if (json != null && !json.isBlank()) {
                try {
                    Provenance p = getJsonParser().parseResource(Provenance.class, json);
                    if (p != null) {
                        provenances.add(p);
                    }
                } catch (Exception e) {
                    log.debug("Failed parsing Provenance JSON from cache: {}", e.getMessage());
                }
            }
        }
        return provenances;
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
        RemoteCache<String, String> remoteCache = requireRemoteCache();
        return remoteCache.size();
    }

    @Override
    public void clear() {
        RemoteCache<String, String> remoteCache = requireRemoteCache();
        remoteCache.clear();
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
