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

package net.fhirfactory.harmonia.hapifhir.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import net.fhirfactory.harmonia.hapifhir.model.FhirResourceEntity;
import net.fhirfactory.harmonia.hapifhir.repository.FhirResourceRepository;
import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
import net.fhirfactory.harmonia.model.security.HarmoniaAuthorityEnum;
import net.fhirfactory.harmonia.model.security.HarmoniaSecurityLabelEnum;
import net.fhirfactory.harmonia.themis.api.ThemisService;
import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisResource;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import net.fhirfactory.harmonia.themis.core.evaluator.DeterministicPolicyEvaluator;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FhirStorageService {

    private static final Logger log = LoggerFactory.getLogger(FhirStorageService.class);

    private final FhirResourceRepository repository;
    private final FhirContext fhirContext;
    private final ThemisService themisService;

    public FhirStorageService(FhirResourceRepository repository) {
        this(repository, DeterministicPolicyEvaluator.withDefaultPolicies());
    }

    @Autowired
    public FhirStorageService(FhirResourceRepository repository, @Autowired(required = false) ThemisService themisService) {
        this.repository = repository;
        this.themisService = themisService != null ? themisService : DeterministicPolicyEvaluator.withDefaultPolicies();
        this.fhirContext = FhirContext.forR5();
    }

    public FhirContext getFhirContext() {
        return fhirContext;
    }

    public ThemisService getThemisService() {
        return themisService;
    }

    /**
     * Authorizes persistence operations against Themis before database modification or query.
     */
    public void authorizePersistence(String resourceType, ThemisAction action, String resourceId, ThemisPrincipal principal, Set<ThemisAuthority> authorities, String correlationId) {
        ThemisPrincipal effPrincipal = principal != null
                ? principal
                : ThemisPrincipal.of("service:mnemosyne", PrincipalType.SERVICE, "mnemosyne");

        Set<ThemisAuthority> effAuthorities = authorities != null
                ? authorities
                : Set.of(
                        HarmoniaAuthorityEnum.PROVIDER_RESOURCE_CREATE.toThemisAuthority(),
                        HarmoniaAuthorityEnum.PROVIDER_RESOURCE_UPDATE.toThemisAuthority(),
                        ThemisAuthority.of("provider.resource.delete"),
                        HarmoniaAuthorityEnum.PROVIDER_READ.toThemisAuthority(),
                        HarmoniaAuthorityEnum.PROVIDER_SEARCH.toThemisAuthority(),
                        ThemisAuthority.of("provider.admin")
                );

        ThemisResource target = ThemisResource.builder()
                .resourceType(resourceType)
                .resourceId(resourceId)
                .securityDomain(HarmoniaSecurityLabelEnum.PROVIDER_REGISTRY.getCode())
                .securityLabels(Set.of(
                        HarmoniaSecurityLabelEnum.PROVIDER_REGISTRY.toThemisLabel(),
                        HarmoniaSecurityLabelEnum.INTERNAL.toThemisLabel()
                ))
                .build();

        ThemisSecurityContext context = ThemisSecurityContext.fromPrincipal(effPrincipal, correlationId != null ? correlationId : UUID.randomUUID().toString());

        ThemisAuthorizationRequest authReq = ThemisAuthorizationRequest.builder()
                .principal(effPrincipal)
                .authorities(effAuthorities)
                .action(action)
                .target(target)
                .context(context)
                .build();

        ThemisAuthorizationDecision decision = themisService.authorize(authReq);
        if (decision.decision() == ThemisDecision.DENY) {
            log.warn("Persistence authorization DENIED for {} performing {} on {}/{} (policy={}, reason={})",
                    effPrincipal.principalId(), action, resourceType, resourceId, decision.policyId(), decision.reason());
            throw new ForbiddenOperationException(
                    "Persistence authorization denied by Themis: " + decision.reason() + " [" + decision.policyId() + "]"
            );
        }
    }

    private IParser getJsonParser() {
        return fhirContext.newJsonParser().setPrettyPrint(true);
    }

    @Transactional
    public <T extends IBaseResource> T createResource(T resource) {
        return createResource(resource, null, null, null);
    }

    @Transactional
    public <T extends IBaseResource> T createResource(T resource, ThemisPrincipal principal, Set<ThemisAuthority> authorities, String correlationId) {
        if (resource == null) {
            throw new UnprocessableEntityException("Resource body cannot be null");
        }

        String resourceType = resource.fhirType();
        String fhirId = null;
        if (resource.getIdElement() != null && StringUtils.isNotBlank(resource.getIdElement().getIdPart())) {
            fhirId = cleanId(resource.getIdElement().getIdPart(), resourceType);
        } else if (resource instanceof Resource r && StringUtils.isNotBlank(r.getIdPart())) {
            fhirId = cleanId(r.getIdPart(), resourceType);
        } else if (resource instanceof Resource r && StringUtils.isNotBlank(r.getId())) {
            fhirId = cleanId(r.getId(), resourceType);
        }

        if (StringUtils.isBlank(fhirId)) {
            fhirId = UUID.randomUUID().toString();
        }

        // Themis Persistence Boundary Enforcement
        authorizePersistence(resourceType, ThemisAction.CREATE, fhirId, principal, authorities, correlationId);

        resource.setId(new IdType(resourceType, fhirId, "1"));

        if (resource instanceof Resource res) {
            FhirSecurityTagManager.applyDefaultSecurityTag(res);
        }

        // Set meta version and lastUpdated
        Meta meta = ((Resource) resource).getMeta();
        if (meta == null) {
            meta = new Meta();
            ((Resource) resource).setMeta(meta);
        }
        meta.setVersionId("1");
        meta.setLastUpdated(new Date());

        String json = getJsonParser().encodeResourceToString(resource);

        Optional<FhirResourceEntity> existing = repository.findByResourceTypeAndFhirId(resourceType, fhirId);
        FhirResourceEntity entity;
        if (existing.isPresent()) {
            entity = existing.get();
            entity.setVersionId(entity.getVersionId() + 1L);
            entity.setDeleted(false);
            entity.setResourceJson(json);
            entity.setLastUpdated(Instant.now());
            meta.setVersionId(String.valueOf(entity.getVersionId()));
            // re-encode with updated version
            json = getJsonParser().encodeResourceToString(resource);
            entity.setResourceJson(json);
        } else {
            entity = new FhirResourceEntity(resourceType, fhirId, 1L, json, false, Instant.now());
        }

        repository.save(entity);
        log.info("Created/Stored resource {}/{}", resourceType, fhirId);
        return resource;
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IBaseResource> T getResource(String resourceType, String fhirId) {
        authorizePersistence(resourceType, ThemisAction.READ, fhirId, null, null, null);
        FhirResourceEntity entity = repository.findByResourceTypeAndFhirId(resourceType, fhirId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource " + resourceType + "/" + fhirId + " not found"));

        if (entity.isDeleted()) {
            throw new ResourceGoneException("Resource " + resourceType + "/" + fhirId + " is deleted");
        }

        return (T) getJsonParser().parseResource(entity.getResourceJson());
    }

    @Transactional
    public <T extends IBaseResource> T updateResource(String fhirId, T resource) {
        return updateResource(fhirId, resource, null, null, null, null);
    }

    @Transactional
    public <T extends IBaseResource> T updateResource(String fhirId, T resource, String expectedVersion) {
        return updateResource(fhirId, resource, expectedVersion, null, null, null);
    }

    @Transactional
    public <T extends IBaseResource> T updateResource(String fhirId, T resource, String expectedVersion, ThemisPrincipal principal, Set<ThemisAuthority> authorities, String correlationId) {
        if (resource == null) {
            throw new UnprocessableEntityException("Resource body cannot be null");
        }
        String resourceType = resource.fhirType();

        // Themis Persistence Boundary Enforcement
        authorizePersistence(resourceType, ThemisAction.UPDATE, fhirId, principal, authorities, correlationId);

        long newVersion = 1L;
        Optional<FhirResourceEntity> existingOpt = repository.findByResourceTypeAndFhirId(resourceType, fhirId);
        FhirResourceEntity entity;
        if (existingOpt.isPresent()) {
            entity = existingOpt.get();
            if (StringUtils.isNotBlank(expectedVersion)) {
                String cleanExpected = expectedVersion.replace("W/", "").replace("\"", "").trim();
                try {
                    long expVerLong = Long.parseLong(cleanExpected);
                    if (entity.getVersionId() != expVerLong) {
                        throw new ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException(
                                "Optimistic lock conflict: current version is " + entity.getVersionId() + " but update expected " + expectedVersion);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
            newVersion = entity.getVersionId() + 1L;
            entity.setVersionId(newVersion);
            entity.setDeleted(false);
            entity.setLastUpdated(Instant.now());
        } else {
            entity = new FhirResourceEntity(resourceType, fhirId, newVersion, "", false, Instant.now());
        }

        resource.setId(new IdType(resourceType, fhirId, String.valueOf(newVersion)));
        if (resource instanceof Resource res) {
            FhirSecurityTagManager.applyDefaultSecurityTag(res);
        }
        Meta meta = ((Resource) resource).getMeta();
        if (meta == null) {
            meta = new Meta();
            ((Resource) resource).setMeta(meta);
        }
        meta.setVersionId(String.valueOf(newVersion));
        meta.setLastUpdated(new Date());

        String json = getJsonParser().encodeResourceToString(resource);
        entity.setResourceJson(json);
        repository.save(entity);
        log.info("Updated resource {}/{} to version {}", resourceType, fhirId, newVersion);
        return resource;
    }

    @Transactional
    public void deleteResource(String resourceType, String fhirId) {
        authorizePersistence(resourceType, ThemisAction.DELETE, fhirId, null, null, null);
        Optional<FhirResourceEntity> entityOpt = repository.findByResourceTypeAndFhirId(resourceType, fhirId);
        if (entityOpt.isEmpty()) {
            throw new ResourceNotFoundException("Resource " + resourceType + "/" + fhirId + " not found");
        }
        FhirResourceEntity entity = entityOpt.get();
        if (entity.isDeleted()) {
            throw new ResourceGoneException("Resource " + resourceType + "/" + fhirId + " is already deleted");
        }
        entity.setDeleted(true);
        entity.setLastUpdated(Instant.now());
        repository.save(entity);
        log.info("Soft-deleted resource {}/{}", resourceType, fhirId);
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IBaseResource> List<T> searchResources(String resourceType) {
        authorizePersistence(resourceType, ThemisAction.SEARCH, null, null, null, null);
        List<FhirResourceEntity> entities = repository.findByResourceTypeAndDeletedFalse(resourceType);
        IParser parser = getJsonParser();
        List<T> results = new ArrayList<>();
        for (FhirResourceEntity entity : entities) {
            results.add((T) parser.parseResource(entity.getResourceJson()));
        }
        return results;
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IBaseResource> List<T> searchResources(String resourceType, String id, String name, String identifier) {
        Map<String, String> params = new HashMap<>();
        if (StringUtils.isNotBlank(id)) params.put("_id", id);
        if (StringUtils.isNotBlank(name)) params.put("name", name);
        if (StringUtils.isNotBlank(identifier)) params.put("identifier", identifier);
        return searchResources(resourceType, params);
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IBaseResource> List<T> searchResources(String resourceType, Map<String, String> searchParams) {
        authorizePersistence(resourceType, ThemisAction.SEARCH, null, null, null, null);
        List<FhirResourceEntity> entities = repository.findByResourceTypeAndDeletedFalse(resourceType);
        IParser parser = getJsonParser();

        List<T> results = new ArrayList<>();
        for (FhirResourceEntity entity : entities) {
            IBaseResource res = parser.parseResource(entity.getResourceJson());
            if (matchesFilter(res, searchParams)) {
                results.add((T) res);
            }
        }
        return results;
    }

    private boolean matchesFilter(IBaseResource resource, Map<String, String> searchParams) {
        if (searchParams == null || searchParams.isEmpty()) {
            return true;
        }

        // 1. _id / id
        String idParam = searchParams.getOrDefault("_id", searchParams.get("id"));
        if (StringUtils.isNotBlank(idParam)) {
            String resourceId = resource.getIdElement().getIdPart();
            if (resourceId == null || !resourceId.equalsIgnoreCase(idParam.trim())) {
                return false;
            }
        }

        // 2. name
        String nameParam = searchParams.get("name");
        if (StringUtils.isNotBlank(nameParam)) {
            String targetName = nameParam.toLowerCase().trim();
            boolean matched = false;

            if (resource instanceof Person p) {
                matched = p.getName().stream().anyMatch(hn -> matchesHumanName(hn, targetName));
            } else if (resource instanceof RelatedPerson rp) {
                matched = rp.getName().stream().anyMatch(hn -> matchesHumanName(hn, targetName));
            } else if (resource instanceof Practitioner pr) {
                matched = pr.getName().stream().anyMatch(hn -> matchesHumanName(hn, targetName));
            } else if (resource instanceof Organization o) {
                matched = (o.getName() != null && o.getName().toLowerCase().contains(targetName)) ||
                          o.getAlias().stream().anyMatch(a -> a.getValueNotNull().toLowerCase().contains(targetName));
            } else if (resource instanceof Location l) {
                matched = (l.getName() != null && l.getName().toLowerCase().contains(targetName)) ||
                          l.getAlias().stream().anyMatch(a -> a.getValueNotNull().toLowerCase().contains(targetName));
            } else if (resource instanceof HealthcareService hs) {
                matched = hs.getName() != null && hs.getName().toLowerCase().contains(targetName);
            } else if (resource instanceof Endpoint ep) {
                matched = ep.getName() != null && ep.getName().toLowerCase().contains(targetName);
            } else if (resource instanceof Group g) {
                matched = g.getName() != null && g.getName().toLowerCase().contains(targetName);
            } else if (resource instanceof Provenance p) {
                matched = p.getTarget().stream().anyMatch(t -> t.getReference() != null && t.getReference().toLowerCase().contains(targetName)) ||
                          p.getAgent().stream().anyMatch(a -> a.getWho() != null && a.getWho().getDisplay() != null && a.getWho().getDisplay().toLowerCase().contains(targetName));
            } else if (resource instanceof AuditEvent ae) {
                matched = (ae.getAction() != null && ae.getAction().toCode() != null && ae.getAction().toCode().toLowerCase().contains(targetName)) ||
                          ae.getAgent().stream().anyMatch(a -> a.getWho() != null && a.getWho().getDisplay() != null && a.getWho().getDisplay().toLowerCase().contains(targetName));
            } else if (resource instanceof Consent c) {
                matched = c.getCategory().stream().anyMatch(cat -> cat.getText() != null && cat.getText().toLowerCase().contains(targetName));
            } else if (resource instanceof Task t) {
                matched = t.getDescription() != null && t.getDescription().toLowerCase().contains(targetName);
            } else if (resource instanceof Communication comm) {
                matched = (comm.getSubject() != null && comm.getSubject().getDisplay() != null && comm.getSubject().getDisplay().toLowerCase().contains(targetName)) ||
                          comm.getNote().stream().anyMatch(n -> n.getText() != null && n.getText().toLowerCase().contains(targetName));
            } else if (resource instanceof DocumentReference dr) {
                matched = (dr.getDescription() != null && dr.getDescription().toLowerCase().contains(targetName)) ||
                          dr.getContent().stream().anyMatch(c -> c.getAttachment() != null && c.getAttachment().getTitle() != null && c.getAttachment().getTitle().toLowerCase().contains(targetName));
            } else {
                matched = true;
            }

            if (!matched) {
                return false;
            }
        }

        // 3. identifier
        String identifierParam = searchParams.get("identifier");
        if (StringUtils.isNotBlank(identifierParam)) {
            String targetIdent = identifierParam.toLowerCase().trim();
            boolean matched = false;

            if (resource instanceof Person p) {
                matched = p.getIdentifier().stream().anyMatch(i -> matchesIdentifier(i, targetIdent));
            } else if (resource instanceof RelatedPerson rp) {
                matched = rp.getIdentifier().stream().anyMatch(i -> matchesIdentifier(i, targetIdent));
            } else if (resource instanceof Practitioner pr) {
                matched = pr.getIdentifier().stream().anyMatch(i -> matchesIdentifier(i, targetIdent));
            } else if (resource instanceof PractitionerRole prr) {
                matched = prr.getIdentifier().stream().anyMatch(i -> matchesIdentifier(i, targetIdent));
            } else if (resource instanceof Organization o) {
                matched = o.getIdentifier().stream().anyMatch(i -> matchesIdentifier(i, targetIdent));
            } else if (resource instanceof Location l) {
                matched = l.getIdentifier().stream().anyMatch(i -> matchesIdentifier(i, targetIdent));
            } else if (resource instanceof HealthcareService hs) {
                matched = hs.getIdentifier().stream().anyMatch(i -> matchesIdentifier(i, targetIdent));
            } else if (resource instanceof Endpoint ep) {
                matched = ep.getIdentifier().stream().anyMatch(i -> matchesIdentifier(i, targetIdent));
            } else if (resource instanceof Group g) {
                matched = g.getIdentifier().stream().anyMatch(i -> matchesIdentifier(i, targetIdent));
            } else if (resource instanceof Consent c) {
                matched = c.getIdentifier().stream().anyMatch(i -> matchesIdentifier(i, targetIdent));
            } else if (resource instanceof Task t) {
                matched = t.getIdentifier().stream().anyMatch(i -> matchesIdentifier(i, targetIdent));
            } else if (resource instanceof Communication comm) {
                matched = comm.getIdentifier().stream().anyMatch(i -> matchesIdentifier(i, targetIdent));
            } else if (resource instanceof DocumentReference dr) {
                matched = dr.getIdentifier().stream().anyMatch(i -> matchesIdentifier(i, targetIdent));
            } else {
                matched = true;
            }

            if (!matched) {
                return false;
            }
        }

        // 4. active
        String activeParam = searchParams.get("active");
        if (StringUtils.isNotBlank(activeParam)) {
            boolean targetActive = Boolean.parseBoolean(activeParam.trim());
            boolean matched = false;
            if (resource instanceof Practitioner pr) {
                matched = (pr.hasActive() && pr.getActive() == targetActive);
            } else if (resource instanceof PractitionerRole prr) {
                matched = (prr.hasActive() && prr.getActive() == targetActive);
            } else if (resource instanceof Organization o) {
                matched = (o.hasActive() && o.getActive() == targetActive);
            } else if (resource instanceof HealthcareService hs) {
                matched = (hs.hasActive() && hs.getActive() == targetActive);
            } else {
                matched = true;
            }
            if (!matched) {
                return false;
            }
        }

        // 5. status
        String statusParam = searchParams.get("status");
        if (StringUtils.isNotBlank(statusParam)) {
            String targetStatus = statusParam.toLowerCase().trim();
            boolean matched = false;
            if (resource instanceof Location l) {
                matched = (l.hasStatus() && l.getStatus().toCode().equalsIgnoreCase(targetStatus));
            } else if (resource instanceof Endpoint ep) {
                matched = (ep.hasStatus() && ep.getStatus().toCode().equalsIgnoreCase(targetStatus));
            } else {
                matched = true;
            }
            if (!matched) {
                return false;
            }
        }

        // 6. practitioner
        String practitionerParam = searchParams.get("practitioner");
        if (StringUtils.isNotBlank(practitionerParam)) {
            if (resource instanceof PractitionerRole prr) {
                if (!prr.hasPractitioner() || !matchesReference(prr.getPractitioner(), practitionerParam)) {
                    return false;
                }
            }
        }

        // 7. organization
        String orgParam = searchParams.get("organization");
        if (StringUtils.isNotBlank(orgParam)) {
            boolean matched = false;
            if (resource instanceof PractitionerRole prr) {
                matched = prr.hasOrganization() && matchesReference(prr.getOrganization(), orgParam);
            } else if (resource instanceof Location l) {
                matched = l.hasManagingOrganization() && matchesReference(l.getManagingOrganization(), orgParam);
            } else if (resource instanceof HealthcareService hs) {
                matched = hs.hasProvidedBy() && matchesReference(hs.getProvidedBy(), orgParam);
            } else if (resource instanceof Endpoint ep) {
                matched = ep.hasManagingOrganization() && matchesReference(ep.getManagingOrganization(), orgParam);
            } else {
                matched = true;
            }
            if (!matched) {
                return false;
            }
        }

        // 8. location
        String locParam = searchParams.get("location");
        if (StringUtils.isNotBlank(locParam)) {
            boolean matched = false;
            if (resource instanceof PractitionerRole prr) {
                matched = prr.getLocation().stream().anyMatch(ref -> matchesReference(ref, locParam));
            } else if (resource instanceof HealthcareService hs) {
                matched = hs.getLocation().stream().anyMatch(ref -> matchesReference(ref, locParam));
            } else {
                matched = true;
            }
            if (!matched) {
                return false;
            }
        }

        // 9. service
        String serviceParam = searchParams.get("service");
        if (StringUtils.isNotBlank(serviceParam)) {
            if (resource instanceof PractitionerRole prr) {
                if (prr.getHealthcareService().stream().noneMatch(ref -> matchesReference(ref, serviceParam))) {
                    return false;
                }
            }
        }

        // 10. connection-type
        String connTypeParam = searchParams.getOrDefault("connection-type", searchParams.get("connectionType"));
        if (StringUtils.isNotBlank(connTypeParam)) {
            if (resource instanceof Endpoint ep) {
                String target = connTypeParam.toLowerCase().trim();
                boolean matched = ep.getConnectionType().stream().anyMatch(cc ->
                        cc.getCoding().stream().anyMatch(c ->
                                (c.getCode() != null && c.getCode().toLowerCase().contains(target)) ||
                                (c.getDisplay() != null && c.getDisplay().toLowerCase().contains(target))));
                if (!matched) {
                    return false;
                }
            }
        }

        // 11. type
        String typeParam = searchParams.get("type");
        if (StringUtils.isNotBlank(typeParam)) {
            if (resource instanceof Group g) {
                if (!g.hasType() || !g.getType().toCode().equalsIgnoreCase(typeParam.trim())) {
                    return false;
                }
            }
        }

        // 12. actual / membership
        String actualParam = searchParams.getOrDefault("actual", searchParams.get("membership"));
        if (StringUtils.isNotBlank(actualParam)) {
            if (resource instanceof Group g) {
                if (g.hasMembership()) {
                    String memCode = g.getMembership().toCode();
                    if ("true".equalsIgnoreCase(actualParam) && "enumerated".equalsIgnoreCase(memCode)) {
                        // match
                    } else if ("false".equalsIgnoreCase(actualParam) && "definitional".equalsIgnoreCase(memCode)) {
                        // match
                    } else if (memCode.equalsIgnoreCase(actualParam)) {
                        // match
                    } else {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private String cleanId(String raw, String resourceType) {
        if (raw == null) return null;
        String s = raw.trim();
        while (s.startsWith("#")) {
            s = s.substring(1).trim();
        }
        if (s.contains("/")) {
            s = s.substring(s.lastIndexOf('/') + 1).trim();
        }
        return s;
    }

    private boolean matchesHumanName(HumanName hn, String targetName) {
        if (hn == null) return false;
        if (hn.getFamily() != null && hn.getFamily().toLowerCase().contains(targetName)) return true;
        if (hn.getGiven().stream().anyMatch(g -> g.getValueNotNull().toLowerCase().contains(targetName))) return true;
        if (hn.getText() != null && hn.getText().toLowerCase().contains(targetName)) return true;
        return false;
    }

    private boolean matchesReference(Reference ref, String target) {
        if (ref == null) return false;
        String cleanTarget = target.trim().toLowerCase();
        if (ref.getReference() != null) {
            String r = ref.getReference().toLowerCase();
            if (r.equalsIgnoreCase(cleanTarget) || r.endsWith("/" + cleanTarget) || r.contains(cleanTarget)) {
                return true;
            }
        }
        if (ref.getDisplay() != null && ref.getDisplay().toLowerCase().contains(cleanTarget)) {
            return true;
        }
        return false;
    }

    private boolean matchesIdentifier(Identifier id, String target) {
        if (id == null) return false;
        if (id.getValue() != null && id.getValue().toLowerCase().contains(target)) return true;
        if (id.getSystem() != null && id.getSystem().toLowerCase().contains(target)) return true;
        if (id.getSystem() != null && id.getValue() != null) {
            String combined = (id.getSystem() + "|" + id.getValue()).toLowerCase();
            if (combined.contains(target)) return true;
        }
        return false;
    }
}
