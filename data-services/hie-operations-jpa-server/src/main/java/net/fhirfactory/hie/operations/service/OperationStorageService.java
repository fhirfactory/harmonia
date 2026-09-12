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

package net.fhirfactory.hie.operations.service;

import net.fhirfactory.hie.operations.model.OperationResourceEntity;
import net.fhirfactory.hie.operations.repository.OperationResourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OperationStorageService {

    private static final Logger log = LoggerFactory.getLogger(OperationStorageService.class);

    private final OperationResourceRepository repository;

    public OperationStorageService(OperationResourceRepository repository) {
        this.repository = repository;
    }

    public OperationResourceEntity saveResource(String objectType, String objectId, String dataJson) {
        if (objectType == null || objectType.isBlank()) {
            throw new IllegalArgumentException("objectType cannot be null or blank");
        }
        if (objectId == null || objectId.isBlank()) {
            throw new IllegalArgumentException("objectId cannot be null or blank");
        }
        if (dataJson == null) {
            dataJson = "";
        }

        String cleanType = normalizeObjectType(objectType);
        String cleanId = objectId.trim();

        Optional<OperationResourceEntity> existing = repository.findByObjectTypeAndObjectId(cleanType, cleanId);
        OperationResourceEntity entity;
        if (existing.isPresent()) {
            entity = existing.get();
            entity.setDataJson(dataJson);
            entity.setDeleted(false);
            entity.setVersionId(entity.getVersionId() != null ? entity.getVersionId() + 1 : 1L);
            entity.setLastUpdated(Instant.now());
            log.info("Updating existing non-FHIR operational entity [{}/{}] (v{})", cleanType, cleanId, entity.getVersionId());
        } else {
            entity = new OperationResourceEntity(cleanType, cleanId, 1L, dataJson, false, Instant.now(), Instant.now());
            log.info("Creating new non-FHIR operational entity [{}/{}]", cleanType, cleanId);
        }

        return repository.save(entity);
    }

    @Transactional(readOnly = true)
    public Optional<OperationResourceEntity> getResource(String objectType, String objectId) {
        if (objectType == null || objectId == null) {
            return Optional.empty();
        }
        String cleanType = normalizeObjectType(objectType);
        String cleanId = objectId.trim();
        return repository.findByObjectTypeAndObjectIdAndDeletedFalse(cleanType, cleanId);
    }

    @Transactional(readOnly = true)
    public Optional<String> getResourceJson(String objectType, String objectId) {
        return getResource(objectType, objectId).map(OperationResourceEntity::getDataJson);
    }

    public boolean deleteResource(String objectType, String objectId) {
        if (objectType == null || objectId == null) {
            return false;
        }
        String cleanType = normalizeObjectType(objectType);
        String cleanId = objectId.trim();

        Optional<OperationResourceEntity> existing = repository.findByObjectTypeAndObjectIdAndDeletedFalse(cleanType, cleanId);
        if (existing.isPresent()) {
            OperationResourceEntity entity = existing.get();
            entity.setDeleted(true);
            entity.setLastUpdated(Instant.now());
            repository.save(entity);
            log.info("Soft-deleted non-FHIR operational entity [{}/{}]", cleanType, cleanId);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public boolean containsResource(String objectType, String objectId) {
        if (objectType == null || objectId == null) {
            return false;
        }
        String cleanType = normalizeObjectType(objectType);
        String cleanId = objectId.trim();
        return repository.existsByObjectTypeAndObjectIdAndDeletedFalse(cleanType, cleanId);
    }

    @Transactional(readOnly = true)
    public List<OperationResourceEntity> listByObjectType(String objectType) {
        if (objectType == null || objectType.isBlank()) {
            return repository.findByDeletedFalse();
        }
        String cleanType = normalizeObjectType(objectType);
        return repository.findByObjectTypeAndDeletedFalse(cleanType);
    }

    @Transactional(readOnly = true)
    public List<OperationResourceEntity> listAll() {
        return repository.findByDeletedFalse();
    }

    @Transactional(readOnly = true)
    public long countByObjectType(String objectType) {
        if (objectType == null || objectType.isBlank()) {
            return repository.countByDeletedFalse();
        }
        String cleanType = normalizeObjectType(objectType);
        return repository.countByObjectTypeAndDeletedFalse(cleanType);
    }

    public static String normalizeObjectType(String objectType) {
        if (objectType == null) {
            return "operations";
        }
        String clean = objectType.trim().toLowerCase();
        if (clean.endsWith("-cache")) {
            clean = clean.substring(0, clean.length() - "-cache".length());
        }
        if (clean.endsWith("_cache")) {
            clean = clean.substring(0, clean.length() - "_cache".length());
        }
        return clean;
    }
}
