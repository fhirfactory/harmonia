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
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.Communication;
import org.hl7.fhir.r5.model.Identifier;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@ApplicationScoped
public class DefaultCommunicationService implements CommunicationService {

    private static final Logger log = LoggerFactory.getLogger(DefaultCommunicationService.class);
    public static final String COMMUNICATION_CACHE_NAME = "communication-cache";

    @Inject
    private RemoteCacheManager remoteCacheManager;

    @Inject
    private FhirContext fhirContext;

    private final Map<String, Communication> communicationStore = new ConcurrentHashMap<>();

    public DefaultCommunicationService() {
    }

    public DefaultCommunicationService(RemoteCacheManager remoteCacheManager, FhirContext fhirContext) {
        this.remoteCacheManager = remoteCacheManager;
        this.fhirContext = fhirContext;
    }

    private RemoteCache<String, String> getRemoteCache() {
        if (remoteCacheManager != null && remoteCacheManager.isStarted()) {
            try {
                return remoteCacheManager.getCache(COMMUNICATION_CACHE_NAME);
            } catch (Exception e) {
                log.debug("Remote cache [{}] query failed in MLLP Gateway: {}", COMMUNICATION_CACHE_NAME, e.getMessage());
            }
        }
        return null;
    }

    private IParser getJsonParser() {
        return (fhirContext != null ? fhirContext : FhirContext.forR5()).newJsonParser().setPrettyPrint(true);
    }

    @Override
    public Communication create(Communication communication) {
        if (communication == null) {
            throw new IllegalArgumentException("Communication cannot be null");
        }
        String id = extractId(communication);
        if (StringUtils.isBlank(id)) {
            id = UUID.randomUUID().toString();
            communication.setId("Communication/" + id);
        } else {
            communication.setId("Communication/" + id);
        }
        if (communication.getReceived() == null) {
            communication.setReceived(new Date());
        }
        communicationStore.put(id, communication);

        // Write Communication to Infinispan remote cache
        RemoteCache<String, String> remoteCache = getRemoteCache();
        if (remoteCache != null) {
            try {
                String json = getJsonParser().encodeResourceToString(communication);
                remoteCache.put(id, json);
                log.info("Persisted Communication/{} to remote Infinispan cache [{}]", id, COMMUNICATION_CACHE_NAME);
            } catch (Exception e) {
                log.warn("Could not persist Communication/{} to remote Infinispan cache: {}", id, e.getMessage());
            }
        }

        log.info("Created Communication with id: {}", id);
        return communication;
    }

    @Override
    public Optional<Communication> getById(String id) {
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
                    Communication parsed = getJsonParser().parseResource(Communication.class, json);
                    if (parsed != null) {
                        communicationStore.put(cleanId, parsed);
                        return Optional.of(parsed);
                    }
                }
            } catch (Exception e) {
                log.debug("Remote cache lookup for Communication/{} failed: {}", cleanId, e.getMessage());
            }
        }

        return Optional.ofNullable(communicationStore.get(cleanId));
    }

    @Override
    public Communication update(String id, Communication communication) {
        if (StringUtils.isBlank(id) || communication == null) {
            throw new IllegalArgumentException("ID and Communication cannot be null/blank");
        }
        String cleanId = cleanId(id);
        communication.setId("Communication/" + cleanId);
        communicationStore.put(cleanId, communication);

        // Update in remote cache
        RemoteCache<String, String> remoteCache = getRemoteCache();
        if (remoteCache != null) {
            try {
                String json = getJsonParser().encodeResourceToString(communication);
                remoteCache.put(cleanId, json);
                log.info("Updated Communication/{} in remote Infinispan cache [{}]", cleanId, COMMUNICATION_CACHE_NAME);
            } catch (Exception e) {
                log.warn("Could not update Communication/{} in remote Infinispan cache: {}", cleanId, e.getMessage());
            }
        }

        return communication;
    }

    @Override
    public boolean delete(String id) {
        if (StringUtils.isBlank(id)) {
            return false;
        }
        String cleanId = cleanId(id);
        boolean removed = communicationStore.remove(cleanId) != null;

        // Delete from remote cache
        RemoteCache<String, String> remoteCache = getRemoteCache();
        if (remoteCache != null) {
            try {
                remoteCache.remove(cleanId);
                log.info("Deleted Communication/{} from remote Infinispan cache [{}]", cleanId, COMMUNICATION_CACHE_NAME);
            } catch (Exception e) {
                log.warn("Could not delete Communication/{} from remote Infinispan cache: {}", cleanId, e.getMessage());
            }
        }

        return removed;
    }

    @Override
    public List<Communication> getAll() {
        // Sync from remote cache if available
        RemoteCache<String, String> remoteCache = getRemoteCache();
        if (remoteCache != null) {
            try {
                for (Map.Entry<String, String> entry : remoteCache.entrySet()) {
                    if (!communicationStore.containsKey(entry.getKey())) {
                        try {
                            Communication c = getJsonParser().parseResource(Communication.class, entry.getValue());
                            if (c != null) {
                                communicationStore.put(entry.getKey(), c);
                            }
                        } catch (Exception ignored) {}
                    }
                }
            } catch (Exception e) {
                log.debug("Remote cache bulk sync for communications failed: {}", e.getMessage());
            }
        }

        return new ArrayList<>(communicationStore.values());
    }

    @Override
    public List<Communication> search(String id, String patientId, String identifier) {
        return getAll().stream()
                .filter(c -> StringUtils.isBlank(id) || matchesId(c, id))
                .filter(c -> StringUtils.isBlank(patientId) || matchesPatient(c, patientId))
                .filter(c -> StringUtils.isBlank(identifier) || matchesIdentifier(c, identifier))
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
        return communicationStore.size();
    }

    @Override
    public void clear() {
        communicationStore.clear();
        RemoteCache<String, String> remoteCache = getRemoteCache();
        if (remoteCache != null) {
            try {
                remoteCache.clear();
            } catch (Exception ignored) {}
        }
    }

    private String extractId(Communication communication) {
        if (communication.getIdElement() != null && !communication.getIdElement().isEmpty()) {
            return communication.getIdElement().getIdPart();
        }
        if (communication.getId() != null) {
            return cleanId(communication.getId());
        }
        return null;
    }

    private String cleanId(String id) {
        if (id == null) return null;
        if (id.startsWith("Communication/")) {
            return id.substring("Communication/".length());
        }
        return id;
    }

    private boolean matchesId(Communication communication, String id) {
        String cleanSearchId = cleanId(id);
        String commId = extractId(communication);
        return cleanSearchId.equalsIgnoreCase(commId);
    }

    private boolean matchesPatient(Communication communication, String patientId) {
        if (communication.getSubject() == null) {
            return false;
        }
        String ref = communication.getSubject().getReference();
        if (StringUtils.isBlank(ref)) {
            return false;
        }
        String cleanPatientId = patientId.startsWith("Patient/") ? patientId.substring("Patient/".length()) : patientId;
        return ref.endsWith(cleanPatientId) || ref.equalsIgnoreCase(patientId);
    }

    private boolean matchesIdentifier(Communication communication, String identifier) {
        if (communication.getIdentifier() == null) {
            return false;
        }
        for (Identifier id : communication.getIdentifier()) {
            if (identifier.equalsIgnoreCase(id.getValue())) {
                return true;
            }
        }
        return false;
    }
}
