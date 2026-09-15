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
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.apache.commons.lang3.StringUtils;
import net.fhirfactory.harmonia.hapifhir.model.FhirResourceEntity;
import net.fhirfactory.harmonia.hapifhir.repository.FhirResourceRepository;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public FhirStorageService(FhirResourceRepository repository) {
        this.repository = repository;
        this.fhirContext = FhirContext.forR5();
    }

    public FhirContext getFhirContext() {
        return fhirContext;
    }

    private IParser getJsonParser() {
        return fhirContext.newJsonParser().setPrettyPrint(true);
    }

    @Transactional
    public <T extends IBaseResource> T createResource(T resource) {
        if (resource == null) {
            throw new UnprocessableEntityException("Resource body cannot be null");
        }

        String resourceType = resource.fhirType();
        String fhirId = resource.getIdElement().getIdPart();
        if (StringUtils.isBlank(fhirId)) {
            fhirId = UUID.randomUUID().toString();
            resource.setId(new IdType(resourceType, fhirId, "1"));
        } else {
            resource.setId(new IdType(resourceType, fhirId, "1"));
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
        FhirResourceEntity entity = repository.findByResourceTypeAndFhirId(resourceType, fhirId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource " + resourceType + "/" + fhirId + " not found"));

        if (entity.isDeleted()) {
            throw new ResourceGoneException("Resource " + resourceType + "/" + fhirId + " is deleted");
        }

        return (T) getJsonParser().parseResource(entity.getResourceJson());
    }

    @Transactional
    public <T extends IBaseResource> T updateResource(String fhirId, T resource) {
        if (resource == null) {
            throw new UnprocessableEntityException("Resource body cannot be null");
        }
        String resourceType = resource.fhirType();

        Optional<FhirResourceEntity> existingOpt = repository.findByResourceTypeAndFhirId(resourceType, fhirId);
        long newVersion = 1L;
        FhirResourceEntity entity;
        if (existingOpt.isPresent()) {
            entity = existingOpt.get();
            newVersion = entity.getVersionId() + 1L;
            entity.setVersionId(newVersion);
            entity.setDeleted(false);
            entity.setLastUpdated(Instant.now());
        } else {
            entity = new FhirResourceEntity(resourceType, fhirId, newVersion, "", false, Instant.now());
        }

        resource.setId(new IdType(resourceType, fhirId, String.valueOf(newVersion)));
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
        List<FhirResourceEntity> entities = repository.findByResourceTypeAndDeletedFalse(resourceType);
        IParser parser = getJsonParser();
        return entities.stream()
                .map(e -> (T) parser.parseResource(e.getResourceJson()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IBaseResource> List<T> searchResources(String resourceType, String id, String name, String identifier) {
        List<FhirResourceEntity> entities = repository.findByResourceTypeAndDeletedFalse(resourceType);
        IParser parser = getJsonParser();

        return entities.stream()
                .map(e -> (T) parser.parseResource(e.getResourceJson()))
                .filter(res -> matchesFilter(res, id, name, identifier))
                .collect(Collectors.toList());
    }

    private boolean matchesFilter(IBaseResource resource, String id, String name, String identifier) {
        if (StringUtils.isNotBlank(id)) {
            String resourceId = resource.getIdElement().getIdPart();
            if (resourceId == null || !resourceId.equalsIgnoreCase(id)) {
                return false;
            }
        }

        if (StringUtils.isNotBlank(name)) {
            String targetName = name.toLowerCase();
            boolean matched = false;

            if (resource instanceof Person p) {
                matched = p.getName().stream().anyMatch(hn -> 
                    (hn.getFamily() != null && hn.getFamily().toLowerCase().contains(targetName)) ||
                    hn.getGiven().stream().anyMatch(g -> g.getValueNotNull().toLowerCase().contains(targetName)) ||
                    (hn.getText() != null && hn.getText().toLowerCase().contains(targetName))
                );
            } else if (resource instanceof RelatedPerson rp) {
                matched = rp.getName().stream().anyMatch(hn -> 
                    (hn.getFamily() != null && hn.getFamily().toLowerCase().contains(targetName)) ||
                    hn.getGiven().stream().anyMatch(g -> g.getValueNotNull().toLowerCase().contains(targetName)) ||
                    (hn.getText() != null && hn.getText().toLowerCase().contains(targetName))
                );
            } else if (resource instanceof Practitioner pr) {
                matched = pr.getName().stream().anyMatch(hn -> 
                    (hn.getFamily() != null && hn.getFamily().toLowerCase().contains(targetName)) ||
                    hn.getGiven().stream().anyMatch(g -> g.getValueNotNull().toLowerCase().contains(targetName)) ||
                    (hn.getText() != null && hn.getText().toLowerCase().contains(targetName))
                );
            } else if (resource instanceof Organization o) {
                matched = (o.getName() != null && o.getName().toLowerCase().contains(targetName)) ||
                          o.getAlias().stream().anyMatch(a -> a.getValueNotNull().toLowerCase().contains(targetName));
            } else if (resource instanceof Location l) {
                matched = (l.getName() != null && l.getName().toLowerCase().contains(targetName)) ||
                          l.getAlias().stream().anyMatch(a -> a.getValueNotNull().toLowerCase().contains(targetName));
            } else if (resource instanceof HealthcareService hs) {
                matched = hs.getName() != null && hs.getName().toLowerCase().contains(targetName);
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

        if (StringUtils.isNotBlank(identifier)) {
            String targetIdent = identifier.toLowerCase();
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

        return true;
    }

    private boolean matchesIdentifier(Identifier id, String target) {
        if (id == null) return false;
        if (id.getValue() != null && id.getValue().toLowerCase().contains(target)) return true;
        if (id.getSystem() != null && id.getSystem().toLowerCase().contains(target)) return true;
        return false;
    }
}
