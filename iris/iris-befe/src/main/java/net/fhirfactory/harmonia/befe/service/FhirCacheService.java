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

package net.fhirfactory.harmonia.befe.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.befe.security.ThemisSecurityContextProvider;
import net.fhirfactory.harmonia.model.ergon.ErgonReasonEnum;
import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@ApplicationScoped
public class FhirCacheService {

    private static final Logger log = LoggerFactory.getLogger(FhirCacheService.class);

    @Inject
    private RemoteCacheManager remoteCacheManager;

    @Inject
    private ThemisSecurityContextProvider securityContextProvider;

    private FhirContext fhirContext;
    private final Map<String, Map<String, String>> localFallbackCaches = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        this.fhirContext = FhirContext.forR5();
    }

    public ThemisSecurityContextProvider getSecurityContextProvider() {
        return securityContextProvider;
    }

    public void setSecurityContextProvider(ThemisSecurityContextProvider securityContextProvider) {
        this.securityContextProvider = securityContextProvider;
    }

    public Optional<ThemisSecurityContext> getActiveSecurityContext() {
        return securityContextProvider != null ? securityContextProvider.getSecurityContext() : Optional.empty();
    }

    public FhirContext getFhirContext() {
        if (fhirContext == null) {
            fhirContext = FhirContext.forR5();
        }
        return fhirContext;
    }

    public IParser getJsonParser() {
        return getFhirContext().newJsonParser().setPrettyPrint(true);
    }

    public String resolveCacheName(String resourceType) {
        if (resourceType == null) return "person-cache";
        String normalized = resourceType.toLowerCase().replace("-", "");
        return switch (normalized) {
            case "person" -> "person-cache";
            case "relatedperson" -> "relatedperson-cache";
            case "practitioner" -> "practitioner-cache";
            case "practitionerrole" -> "practitionerrole-cache";
            case "organization", "organisation" -> "organization-cache";
            case "location" -> "location-cache";
            case "healthcareservice" -> "healthcareservice-cache";
            case "group" -> "group-cache";
            case "provenance" -> "provenance-cache";
            case "auditevent" -> "auditevent-cache";
            case "consent" -> "consent-cache";
            case "task" -> "task-cache";
            case "communication" -> "communication-cache";
            case "documentreference" -> "documentreference-cache";
            default -> resourceType.toLowerCase() + "-cache";
        };
    }

    private RemoteCache<String, String> getRemoteCache(String cacheName) {
        if (remoteCacheManager != null && remoteCacheManager.isStarted()) {
            try {
                return remoteCacheManager.getCache(cacheName);
            } catch (Exception e) {
                log.debug("Remote cache [{}] query failed: {}", cacheName, e.getMessage());
            }
        }
        return null;
    }

    public String getResourceJson(String resourceType, String id) {
        String cacheName = resolveCacheName(resourceType);
        RemoteCache<String, String> remoteCache = getRemoteCache(cacheName);
        if (remoteCache != null) {
            try {
                return remoteCache.get(id);
            } catch (Exception e) {
                log.warn("Error reading from remote cache [{}]: {}", cacheName, e.getMessage());
            }
        }
        Map<String, String> localMap = localFallbackCaches.get(cacheName);
        return localMap != null ? localMap.get(id) : null;
    }

    public <T extends IBaseResource> T getResource(String resourceType, String id, Class<T> resourceClass) {
        String json = getResourceJson(resourceType, id);
        if (json == null) {
            return null;
        }
        return getJsonParser().parseResource(resourceClass, json);
    }

    public String putResourceJson(String resourceType, String id, String jsonPayload) {
        String cacheName = resolveCacheName(resourceType);
        RemoteCache<String, String> remoteCache = getRemoteCache(cacheName);
        if (remoteCache != null) {
            try {
                remoteCache.put(id, jsonPayload);
                log.info("BEFE cached {}/{} to remote Infinispan cache [{}]", resourceType, id, cacheName);
                return jsonPayload;
            } catch (Exception e) {
                log.warn("Error putting to remote cache [{}]: {}", cacheName, e.getMessage());
            }
        }
        localFallbackCaches.computeIfAbsent(cacheName, k -> new ConcurrentHashMap<>()).put(id, jsonPayload);
        log.info("BEFE cached {}/{} to local memory cache [{}]", resourceType, id, cacheName);
        return jsonPayload;
    }

    public <T extends IBaseResource> T saveResource(T resource) {
        String resourceType = resource.fhirType();
        String id = resource.getIdElement().getIdPart();
        if (StringUtils.isBlank(id)) {
            id = UUID.randomUUID().toString();
            resource.setId(new IdType(resourceType, id, "1"));
        }

        if (resource instanceof Task task) {
            ErgonReasonEnum.ensureSyntheticTaskReason(task);
        }

        if (resource instanceof Resource res) {
            FhirSecurityTagManager.applyDefaultSecurityTag(res);
        }

        Meta meta = ((Resource) resource).getMeta();
        if (meta == null) {
            meta = new Meta();
            ((Resource) resource).setMeta(meta);
        }
        long version = 1;
        if (StringUtils.isNotBlank(meta.getVersionId())) {
            try {
                version = Long.parseLong(meta.getVersionId()) + 1;
            } catch (NumberFormatException ignored) {}
        }
        meta.setVersionId(String.valueOf(version));
        meta.setLastUpdated(new Date());

        String json = getJsonParser().encodeResourceToString(resource);
        putResourceJson(resourceType, id, json);
        return resource;
    }

    public boolean deleteResource(String resourceType, String id) {
        String cacheName = resolveCacheName(resourceType);
        RemoteCache<String, String> remoteCache = getRemoteCache(cacheName);
        if (remoteCache != null) {
            try {
                return remoteCache.remove(id) != null;
            } catch (Exception e) {
                log.warn("Error deleting from remote cache [{}]: {}", cacheName, e.getMessage());
            }
        }
        Map<String, String> localMap = localFallbackCaches.get(cacheName);
        return localMap != null && localMap.remove(id) != null;
    }

    public Bundle searchAsBundle(String resourceType, String id, String name, String identifier) {
        List<IBaseResource> resources = searchResources(resourceType, id, name, identifier);
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.SEARCHSET);
        bundle.setTotal(resources.size());

        for (IBaseResource res : resources) {
            bundle.addEntry().setResource((Resource) res);
        }
        return bundle;
    }

    public List<IBaseResource> searchResources(String resourceType, String id, String name, String identifier) {
        String cacheName = resolveCacheName(resourceType);
        RemoteCache<String, String> remoteCache = getRemoteCache(cacheName);
        Collection<String> jsonValues;

        if (remoteCache != null) {
            try {
                jsonValues = new ArrayList<>(remoteCache.values());
            } catch (Exception e) {
                log.warn("Error listing values from remote cache [{}]: {}", cacheName, e.getMessage());
                jsonValues = new ArrayList<>(
                        localFallbackCaches.computeIfAbsent(cacheName, k -> new ConcurrentHashMap<>()).values()
                );
            }
        } else {
            jsonValues = new ArrayList<>(
                    localFallbackCaches.computeIfAbsent(cacheName, k -> new ConcurrentHashMap<>()).values()
            );
        }

        IParser parser = getJsonParser();
        return jsonValues.stream()
                .map(parser::parseResource)
                .filter(res -> matchesFilter(res, id, name, identifier))
                .collect(Collectors.toList());
    }

    private boolean matchesFilter(IBaseResource resource, String id, String name, String identifier) {
        if (StringUtils.isNotBlank(id)) {
            String resId = resource.getIdElement().getIdPart();
            if (resId == null || !resId.equalsIgnoreCase(id)) return false;
        }

        if (StringUtils.isNotBlank(name)) {
            String target = name.toLowerCase();
            boolean matched = false;
            if (resource instanceof Person p) {
                matched = p.getName().stream().anyMatch(hn -> matchesHumanName(hn, target));
            } else if (resource instanceof RelatedPerson rp) {
                matched = rp.getName().stream().anyMatch(hn -> matchesHumanName(hn, target));
            } else if (resource instanceof Practitioner pr) {
                matched = pr.getName().stream().anyMatch(hn -> matchesHumanName(hn, target));
            } else if (resource instanceof Organization o) {
                matched = (o.getName() != null && o.getName().toLowerCase().contains(target)) ||
                          o.getAlias().stream().anyMatch(a -> a.getValueNotNull().toLowerCase().contains(target));
            } else if (resource instanceof Location l) {
                matched = (l.getName() != null && l.getName().toLowerCase().contains(target)) ||
                          l.getAlias().stream().anyMatch(a -> a.getValueNotNull().toLowerCase().contains(target));
            } else if (resource instanceof HealthcareService hs) {
                matched = hs.getName() != null && hs.getName().toLowerCase().contains(target);
            } else if (resource instanceof Group g) {
                matched = g.getName() != null && g.getName().toLowerCase().contains(target);
            } else if (resource instanceof Provenance p) {
                matched = p.getTarget().stream().anyMatch(t -> t.getReference() != null && t.getReference().toLowerCase().contains(target)) ||
                          p.getAgent().stream().anyMatch(a -> a.getWho() != null && a.getWho().getDisplay() != null && a.getWho().getDisplay().toLowerCase().contains(target));
            } else if (resource instanceof AuditEvent ae) {
                matched = (ae.getAction() != null && ae.getAction().toCode() != null && ae.getAction().toCode().toLowerCase().contains(target)) ||
                          ae.getAgent().stream().anyMatch(a -> a.getWho() != null && a.getWho().getDisplay() != null && a.getWho().getDisplay().toLowerCase().contains(target));
            } else if (resource instanceof Consent c) {
                matched = c.getCategory().stream().anyMatch(cat -> cat.getText() != null && cat.getText().toLowerCase().contains(target));
            } else if (resource instanceof Task t) {
                matched = t.getDescription() != null && t.getDescription().toLowerCase().contains(target);
            } else if (resource instanceof Communication comm) {
                matched = (comm.getSubject() != null && comm.getSubject().getDisplay() != null && comm.getSubject().getDisplay().toLowerCase().contains(target)) ||
                          comm.getNote().stream().anyMatch(n -> n.getText() != null && n.getText().toLowerCase().contains(target));
            } else if (resource instanceof DocumentReference dr) {
                matched = (dr.getDescription() != null && dr.getDescription().toLowerCase().contains(target)) ||
                          dr.getContent().stream().anyMatch(c -> c.getAttachment() != null && c.getAttachment().getTitle() != null && c.getAttachment().getTitle().toLowerCase().contains(target));
            }
            if (!matched) return false;
        }

        if (StringUtils.isNotBlank(identifier)) {
            String target = identifier.toLowerCase();
            boolean matched = false;
            if (resource instanceof Person p) {
                matched = p.getIdentifier().stream().anyMatch(i -> matchesId(i, target));
            } else if (resource instanceof RelatedPerson rp) {
                matched = rp.getIdentifier().stream().anyMatch(i -> matchesId(i, target));
            } else if (resource instanceof Practitioner pr) {
                matched = pr.getIdentifier().stream().anyMatch(i -> matchesId(i, target));
            } else if (resource instanceof PractitionerRole prr) {
                matched = prr.getIdentifier().stream().anyMatch(i -> matchesId(i, target));
            } else if (resource instanceof Organization o) {
                matched = o.getIdentifier().stream().anyMatch(i -> matchesId(i, target));
            } else if (resource instanceof Location l) {
                matched = l.getIdentifier().stream().anyMatch(i -> matchesId(i, target));
            } else if (resource instanceof HealthcareService hs) {
                matched = hs.getIdentifier().stream().anyMatch(i -> matchesId(i, target));
            } else if (resource instanceof Group g) {
                matched = g.getIdentifier().stream().anyMatch(i -> matchesId(i, target));
            } else if (resource instanceof Consent c) {
                matched = c.getIdentifier().stream().anyMatch(i -> matchesId(i, target));
            } else if (resource instanceof Task t) {
                matched = t.getIdentifier().stream().anyMatch(i -> matchesId(i, target));
            } else if (resource instanceof Communication comm) {
                matched = comm.getIdentifier().stream().anyMatch(i -> matchesId(i, target));
            } else if (resource instanceof DocumentReference dr) {
                matched = dr.getIdentifier().stream().anyMatch(i -> matchesId(i, target));
            }
            if (!matched) return false;
        }

        return true;
    }

    private boolean matchesHumanName(HumanName hn, String target) {
        if (hn == null) return false;
        if (hn.getFamily() != null && hn.getFamily().toLowerCase().contains(target)) return true;
        if (hn.getText() != null && hn.getText().toLowerCase().contains(target)) return true;
        return hn.getGiven().stream().anyMatch(g -> g.getValueNotNull().toLowerCase().contains(target));
    }

    private boolean matchesId(Identifier id, String target) {
        if (id == null) return false;
        if (id.getValue() != null && id.getValue().toLowerCase().contains(target)) return true;
        return id.getSystem() != null && id.getSystem().toLowerCase().contains(target);
    }

    public void setRemoteCacheManager(RemoteCacheManager remoteCacheManager) {
        this.remoteCacheManager = remoteCacheManager;
    }
}
