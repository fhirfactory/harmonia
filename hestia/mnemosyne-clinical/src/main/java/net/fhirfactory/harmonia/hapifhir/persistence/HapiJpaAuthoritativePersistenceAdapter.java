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

package net.fhirfactory.harmonia.hapifhir.persistence;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativePreconditionConflict;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.PreconditionFailureReason;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.IdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;

/**
 * Experimental Authoritative Persistence Adapter bridging Harmonia's authoritative persistence port
 * to native HAPI FHIR JPA persistence machinery ({@link DaoRegistry} and {@link IFhirResourceDao}).
 * <p>
 * Enforces atomic CREATE-if-absent and predecessor-matched conditional UPDATE via relational constraints
 * and HAPI FHIR JPA optimistic locking mechanisms without application-level locks.
 */
@Component("hapiJpaAuthoritativePersistenceAdapter")
public class HapiJpaAuthoritativePersistenceAdapter implements AuthoritativePersistencePort<IBaseResource> {

    private static final Logger log = LoggerFactory.getLogger(HapiJpaAuthoritativePersistenceAdapter.class);

    private final DaoRegistry daoRegistry;
    private final TransactionTemplate transactionTemplate;

    @Autowired
    public HapiJpaAuthoritativePersistenceAdapter(
            DaoRegistry daoRegistry,
            PlatformTransactionManager transactionManager) {
        this.daoRegistry = Objects.requireNonNull(daoRegistry, "daoRegistry must not be null");
        Objects.requireNonNull(transactionManager, "transactionManager must not be null");
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public HapiJpaAuthoritativePersistenceAdapter(
            DaoRegistry daoRegistry,
            TransactionTemplate transactionTemplate) {
        this.daoRegistry = Objects.requireNonNull(daoRegistry, "daoRegistry must not be null");
        this.transactionTemplate = Objects.requireNonNull(transactionTemplate, "transactionTemplate must not be null");
    }

    @SuppressWarnings("unchecked")
    private IFhirResourceDao<IBaseResource> resolveDao(String resourceType) {
        if (resourceType == null || resourceType.isBlank()) {
            throw new IllegalArgumentException("Resource type must not be null or blank");
        }
        IFhirResourceDao<?> dao = daoRegistry.getResourceDao(resourceType);
        if (dao == null) {
            throw new IllegalArgumentException("No HAPI FHIR DAO registered for resource type: " + resourceType);
        }
        return (IFhirResourceDao<IBaseResource>) dao;
    }

    private boolean isImmutableResourceType(String resourceType) {
        return "AuditEvent".equalsIgnoreCase(resourceType);
    }

    @Override
    public AuthoritativePersistenceResult<IBaseResource> read(ResourceKey key) {
        if (key == null) {
            return new AuthoritativePersistenceResult.NotCommitted<>("Resource key must not be null");
        }

        IFhirResourceDao<IBaseResource> dao;
        try {
            dao = resolveDao(key.resourceType());
        } catch (Exception e) {
            log.error("Failed to resolve DAO for READ {}: {}", key, e.getMessage());
            return new AuthoritativePersistenceResult.NotCommitted<>(
                    "Unsupported or unresolvable resource type: " + key.resourceType(), e);
        }

        try {
            IBaseResource resource = dao.read(new IdType(key.resourceType(), key.id()));
            if (resource == null) {
                return new AuthoritativePersistenceResult.Absent<>(
                        "Resource not found: " + key.toQualifiedPath());
            }
            String versionId = resource.getIdElement() != null ? resource.getIdElement().getVersionIdPart() : null;
            if (versionId == null || versionId.isBlank()) {
                return new AuthoritativePersistenceResult.NotCommitted<>(
                        "Resource has no version ID: " + key.toQualifiedPath());
            }
            AuthoritativeVersion version = AuthoritativeVersion.of(versionId);
            return new AuthoritativePersistenceResult.Committed<>(resource, version);
        } catch (ResourceNotFoundException rnfe) {
            return new AuthoritativePersistenceResult.Absent<>(
                    "Resource not found: " + key.toQualifiedPath());
        } catch (ResourceGoneException rge) {
            return new AuthoritativePersistenceResult.NotCommitted<>(
                    "Resource is deleted: " + key.toQualifiedPath(), rge);
        } catch (CannotCreateTransactionException ccte) {
            log.error("Cannot connect to transaction coordinator for READ {}: {}", key, ccte.getMessage(), ccte);
            return new AuthoritativePersistenceResult.OutcomeUnknown<>(
                    "Transaction coordinator connection failure: " + ccte.getMessage(), ccte);
        } catch (TransactionSystemException tse) {
            log.error("Transaction outcome unknown for READ {}: {}", key, tse.getMessage(), tse);
            return new AuthoritativePersistenceResult.OutcomeUnknown<>(
                    "Transaction outcome is unknown: " + tse.getMessage(), tse);
        } catch (Exception e) {
            log.error("Persistence failure reading {}: {}", key, e.getMessage(), e);
            return new AuthoritativePersistenceResult.NotCommitted<>("Persistence failure: " + e.getMessage(), e);
        }
    }

    @Override
    public AuthoritativePersistenceResult<IBaseResource> create(ResourceKey key, IBaseResource proposedState) {
        if (key == null) {
            return new AuthoritativePersistenceResult.NotCommitted<>("Resource key must not be null");
        }
        if (proposedState == null) {
            return new AuthoritativePersistenceResult.NotCommitted<>("Proposed state must not be null");
        }

        if (isImmutableResourceType(key.resourceType())) {
            log.warn("Rejected create on immutable resource type: {}", key.resourceType());
            return new AuthoritativePersistenceResult.NotCommitted<>(
                    "AuditEvent is immutable and cannot be created via generic FHIR storage");
        }

        if (!key.resourceType().equalsIgnoreCase(proposedState.fhirType())) {
            return new AuthoritativePersistenceResult.NotCommitted<>(
                    "Resource key type (" + key.resourceType() + ") does not match resource body type (" + proposedState.fhirType() + ")");
        }

        IFhirResourceDao<IBaseResource> dao;
        try {
            dao = resolveDao(key.resourceType());
        } catch (Exception e) {
            log.error("Failed to resolve DAO for CREATE {}: {}", key, e.getMessage());
            return new AuthoritativePersistenceResult.NotCommitted<>(
                    "Unsupported or unresolvable resource type: " + key.resourceType(), e);
        }

        // Assign client-defined logical ID for CREATE-if-absent candidate
        proposedState.setId(new IdType(key.resourceType(), key.id()));
        SystemRequestDetails requestDetails = new SystemRequestDetails();
        requestDetails.addHeader(Constants.HEADER_IF_NONE_MATCH, "*");

        try {
            return transactionTemplate.execute(status -> {
                DaoMethodOutcome outcome = dao.update(proposedState, requestDetails);
                if (Boolean.FALSE.equals(outcome.getCreated())) {
                    status.setRollbackOnly();
                    AuthoritativeVersion currentVer = null;
                    try {
                        IBaseResource existing = dao.read(new IdType(key.resourceType(), key.id()));
                        if (existing != null && existing.getIdElement() != null && existing.getIdElement().getVersionIdPart() != null) {
                            currentVer = AuthoritativeVersion.of(existing.getIdElement().getVersionIdPart());
                        }
                    } catch (Exception ignored) {
                    }
                    return new AuthoritativePersistenceResult.Conflict<>(
                            AuthoritativePreconditionConflict.resourceAlreadyExists(key, currentVer));
                }
                IBaseResource persisted = outcome.getResource() != null ? outcome.getResource() : proposedState;
                String versionId = outcome.getId() != null ? outcome.getId().getVersionIdPart() : null;
                if (versionId == null && persisted.getIdElement() != null) {
                    versionId = persisted.getIdElement().getVersionIdPart();
                }
                if (versionId == null || versionId.isBlank()) {
                    versionId = "1";
                }
                AuthoritativeVersion version = AuthoritativeVersion.of(versionId);
                log.info("Authoritatively committed CREATE via HAPI JPA for {}/{} with version {}",
                        key.resourceType(), key.id(), versionId);
                return new AuthoritativePersistenceResult.Committed<>(persisted, version);
            });
        } catch (ResourceVersionConflictException | PreconditionFailedException | DataIntegrityViolationException dive) {
            log.warn("Conflict/uniqueness violation during CREATE for {}: {}", key, dive.getMessage());
            AuthoritativeVersion currentVer = null;
            try {
                IBaseResource existing = dao.read(new IdType(key.resourceType(), key.id()));
                if (existing != null && existing.getIdElement() != null && existing.getIdElement().getVersionIdPart() != null) {
                    currentVer = AuthoritativeVersion.of(existing.getIdElement().getVersionIdPart());
                }
            } catch (Exception ignored) {
            }
            return new AuthoritativePersistenceResult.Conflict<>(
                    AuthoritativePreconditionConflict.resourceAlreadyExists(key, currentVer));
        } catch (CannotCreateTransactionException ccte) {
            log.error("Cannot connect to transaction coordinator for CREATE {}: {}", key, ccte.getMessage(), ccte);
            return new AuthoritativePersistenceResult.OutcomeUnknown<>(
                    "Transaction coordinator connection failure: " + ccte.getMessage(), ccte);
        } catch (TransactionSystemException tse) {
            log.error("Transaction commit outcome unknown for CREATE {}: {}", key, tse.getMessage(), tse);
            return new AuthoritativePersistenceResult.OutcomeUnknown<>(
                    "Transaction commit outcome is unknown: " + tse.getMessage(), tse);
        } catch (Exception e) {
            log.error("Persistence failure creating {}: {}", key, e.getMessage(), e);
            return new AuthoritativePersistenceResult.NotCommitted<>("Persistence failure: " + e.getMessage(), e);
        }
    }

    @Override
    public AuthoritativePersistenceResult<IBaseResource> update(
            ResourceKey key,
            IBaseResource proposedState,
            ExpectedAuthoritativeVersion expectedVersion) {

        if (key == null) {
            return new AuthoritativePersistenceResult.NotCommitted<>("Resource key must not be null");
        }
        if (proposedState == null) {
            return new AuthoritativePersistenceResult.NotCommitted<>("Proposed state must not be null");
        }
        if (expectedVersion == null || expectedVersion.isNone()) {
            return new AuthoritativePersistenceResult.Conflict<>(
                    new AuthoritativePreconditionConflict(
                            key,
                            PreconditionFailureReason.EXPECTED_VERSION_MISMATCH,
                            expectedVersion != null ? expectedVersion : ExpectedAuthoritativeVersion.none(),
                            null,
                            "Expected authoritative version must be specified for update"
                    )
            );
        }

        if (isImmutableResourceType(key.resourceType())) {
            log.warn("Rejected update on immutable resource type: {}", key.resourceType());
            return new AuthoritativePersistenceResult.NotCommitted<>(
                    "AuditEvent is immutable and cannot be updated via generic FHIR storage");
        }

        if (!key.resourceType().equalsIgnoreCase(proposedState.fhirType())) {
            return new AuthoritativePersistenceResult.NotCommitted<>(
                    "Resource key type (" + key.resourceType() + ") does not match resource body type (" + proposedState.fhirType() + ")");
        }

        long expectedVerLong;
        try {
            String cleanExpected = expectedVersion.value().orElseThrow().replace("W/", "").replace("\"", "").trim();
            expectedVerLong = Long.parseLong(cleanExpected);
            if (expectedVerLong < 1) {
                throw new NumberFormatException("Version must be positive");
            }
        } catch (Exception e) {
            log.warn("Rejected update for {} due to malformed expected version: {}", key, expectedVersion);
            return new AuthoritativePersistenceResult.Conflict<>(
                    new AuthoritativePreconditionConflict(
                            key,
                            PreconditionFailureReason.EXPECTED_VERSION_MISMATCH,
                            expectedVersion,
                            null,
                            "Malformed or non-numeric expected authoritative version: " + expectedVersion
                    )
            );
        }

        IFhirResourceDao<IBaseResource> dao;
        try {
            dao = resolveDao(key.resourceType());
        } catch (Exception e) {
            log.error("Failed to resolve DAO for UPDATE {}: {}", key, e.getMessage());
            return new AuthoritativePersistenceResult.NotCommitted<>(
                    "Unsupported or unresolvable resource type: " + key.resourceType(), e);
        }

        // Set ID with expected predecessor version for HAPI FHIR optimistic version locking
        proposedState.setId(new IdType(key.resourceType(), key.id(), String.valueOf(expectedVerLong)));
        SystemRequestDetails requestDetails = new SystemRequestDetails();
        requestDetails.addHeader(Constants.HEADER_IF_MATCH, "W/\"" + expectedVerLong + "\"");

        try {
            return transactionTemplate.execute(status -> {
                DaoMethodOutcome outcome = dao.update(proposedState, requestDetails);
                if (Boolean.TRUE.equals(outcome.getCreated())) {
                    status.setRollbackOnly();
                    log.warn("UPDATE precondition failed: resource {} did not exist before update", key);
                    return new AuthoritativePersistenceResult.Conflict<>(
                            new AuthoritativePreconditionConflict(
                                    key,
                                    PreconditionFailureReason.EXPECTED_VERSION_MISMATCH,
                                    expectedVersion,
                                    null,
                                    "Resource " + key + " does not exist or is deleted"
                            )
                    );
                }
                IBaseResource persisted = outcome.getResource() != null ? outcome.getResource() : proposedState;
                String versionId = outcome.getId() != null ? outcome.getId().getVersionIdPart() : null;
                if (versionId == null && persisted.getIdElement() != null) {
                    versionId = persisted.getIdElement().getVersionIdPart();
                }
                if (versionId == null || versionId.isBlank()) {
                    versionId = String.valueOf(expectedVerLong + 1L);
                }
                AuthoritativeVersion nextVersion = AuthoritativeVersion.of(versionId);
                log.info("Authoritatively committed UPDATE via HAPI JPA for {}/{} to version {}",
                        key.resourceType(), key.id(), versionId);
                return new AuthoritativePersistenceResult.Committed<>(persisted, nextVersion);
            });
        } catch (ResourceNotFoundException | ResourceGoneException rnfe) {
            log.warn("UPDATE precondition failed: resource {} does not exist or is deleted", key);
            return new AuthoritativePersistenceResult.Conflict<>(
                    new AuthoritativePreconditionConflict(
                            key,
                            PreconditionFailureReason.EXPECTED_VERSION_MISMATCH,
                            expectedVersion,
                            null,
                            "Resource " + key + " does not exist or is deleted"
                    )
            );
        } catch (ResourceVersionConflictException | PreconditionFailedException | DataIntegrityViolationException
                 | ObjectOptimisticLockingFailureException e) {
            log.warn("UPDATE precondition mismatch / conflict for {}: {}", key, e.getMessage());
            AuthoritativeVersion currentVer = null;
            try {
                IBaseResource current = dao.read(new IdType(key.resourceType(), key.id()));
                if (current != null && current.getIdElement() != null && current.getIdElement().getVersionIdPart() != null) {
                    currentVer = AuthoritativeVersion.of(current.getIdElement().getVersionIdPart());
                }
            } catch (ResourceNotFoundException | ResourceGoneException rnfe) {
                return new AuthoritativePersistenceResult.Conflict<>(
                        new AuthoritativePreconditionConflict(
                                key,
                                PreconditionFailureReason.EXPECTED_VERSION_MISMATCH,
                                expectedVersion,
                                null,
                                "Resource " + key + " does not exist or is deleted"
                        )
                );
            } catch (Exception ignored) {
            }
            return new AuthoritativePersistenceResult.Conflict<>(
                    AuthoritativePreconditionConflict.expectedVersionMismatch(key, expectedVersion, currentVer)
            );
        } catch (CannotCreateTransactionException ccte) {
            log.error("Cannot connect to transaction coordinator for UPDATE {}: {}", key, ccte.getMessage(), ccte);
            return new AuthoritativePersistenceResult.OutcomeUnknown<>(
                    "Transaction coordinator connection failure: " + ccte.getMessage(), ccte);
        } catch (TransactionSystemException tse) {
            log.error("Transaction commit outcome unknown for UPDATE {}: {}", key, tse.getMessage(), tse);
            return new AuthoritativePersistenceResult.OutcomeUnknown<>(
                    "Transaction commit outcome is unknown: " + tse.getMessage(), tse);
        } catch (Exception e) {
            log.error("Persistence failure updating {}: {}", key, e.getMessage(), e);
            return new AuthoritativePersistenceResult.NotCommitted<>("Persistence failure: " + e.getMessage(), e);
        }
    }
}
