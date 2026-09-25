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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import net.fhirfactory.harmonia.hapifhir.model.FhirResourceEntity;
import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.hapifhir.repository.FhirResourceRepository;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativePreconditionConflict;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.PreconditionFailureReason;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Meta;
import org.hl7.fhir.r5.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * Authoritative persistence service enforcing atomic CREATE uniqueness and conditional UPDATE predecessor matching
 * within programmatic transaction boundaries (TransactionTemplate).
 */
@Service
public class AuthoritativePersistenceService implements AuthoritativePersistencePort<IBaseResource> {

    private static final Logger log = LoggerFactory.getLogger(AuthoritativePersistenceService.class);

    private final FhirResourceRepository repository;
    private final TransactionTemplate transactionTemplate;
    private final FhirContext fhirContext;

    public AuthoritativePersistenceService(FhirResourceRepository repository, PlatformTransactionManager transactionManager) {
        this(repository, transactionManager, FhirContext.forR5());
    }

    @Autowired
    public AuthoritativePersistenceService(
            FhirResourceRepository repository,
            PlatformTransactionManager transactionManager,
            @Autowired(required = false) FhirContext fhirContext) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
        this.transactionTemplate = new TransactionTemplate(Objects.requireNonNull(transactionManager, "transactionManager must not be null"));
        this.fhirContext = fhirContext != null ? fhirContext : FhirContext.forR5();
    }

    public AuthoritativePersistenceService(
            FhirResourceRepository repository,
            TransactionTemplate transactionTemplate,
            FhirContext fhirContext) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
        this.transactionTemplate = Objects.requireNonNull(transactionTemplate, "transactionTemplate must not be null");
        this.fhirContext = fhirContext != null ? fhirContext : FhirContext.forR5();
    }

    private IParser getJsonParser() {
        return fhirContext.newJsonParser().setPrettyPrint(false);
    }

    private boolean isImmutableResourceType(String resourceType) {
        return "AuditEvent".equalsIgnoreCase(resourceType);
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

        // Apply FHIR ID and metadata for initial version (1)
        proposedState.setId(new IdType(key.resourceType(), key.id(), "1"));

        if (proposedState instanceof Resource res) {
            FhirSecurityTagManager.applyDefaultSecurityTag(res);
            Meta meta = res.getMeta();
            if (meta == null) {
                meta = new Meta();
                res.setMeta(meta);
            }
            meta.setVersionId("1");
            meta.setLastUpdated(new Date());
        }

        String json = getJsonParser().encodeResourceToString(proposedState);
        FhirResourceEntity entity = new FhirResourceEntity(key.resourceType(), key.id(), 1L, json, false, Instant.now());

        try {
            transactionTemplate.execute(status -> {
                repository.saveAndFlush(entity);
                return null;
            });
            log.info("Authoritatively committed CREATE for {}/{} with version 1", key.resourceType(), key.id());
            return new AuthoritativePersistenceResult.Committed<>(proposedState, AuthoritativeVersion.of(1L));
        } catch (DataIntegrityViolationException dive) {
            log.warn("Constraint violation during CREATE for {}: {}", key, dive.getMessage());
            Optional<FhirResourceEntity> existing = repository.findByResourceTypeAndFhirId(key.resourceType(), key.id());
            AuthoritativeVersion currentVer = existing.map(e -> AuthoritativeVersion.of(e.getVersionId())).orElse(null);
            return new AuthoritativePersistenceResult.Conflict<>(
                    AuthoritativePreconditionConflict.resourceAlreadyExists(key, currentVer));
        } catch (TransactionSystemException tse) {
            log.error("Transaction commit outcome unknown for CREATE {}: {}", key, tse.getMessage(), tse);
            return new AuthoritativePersistenceResult.OutcomeUnknown<>(
                    "Transaction commit outcome is unknown: " + tse.getMessage(), tse);
        } catch (CannotCreateTransactionException ccte) {
            log.error("Cannot connect to transaction coordinator for CREATE {}: {}", key, ccte.getMessage(), ccte);
            return new AuthoritativePersistenceResult.OutcomeUnknown<>(
                    "Transaction coordinator connection failure: " + ccte.getMessage(), ccte);
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

        // Fail-fast on malformed/non-numeric expected version
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

        // Mnemosyne monotonic version generation policy: nextVersion = currentVersion + 1
        long nextVersionLong = expectedVerLong + 1L;
        AuthoritativeVersion nextVersion = AuthoritativeVersion.of(nextVersionLong);

        // Update FHIR resource metadata with the authoritative next version
        proposedState.setId(new IdType(key.resourceType(), key.id(), String.valueOf(nextVersionLong)));
        if (proposedState instanceof Resource res) {
            FhirSecurityTagManager.applyDefaultSecurityTag(res);
            Meta meta = res.getMeta();
            if (meta == null) {
                meta = new Meta();
                res.setMeta(meta);
            }
            meta.setVersionId(String.valueOf(nextVersionLong));
            meta.setLastUpdated(new Date());
        }

        String json = getJsonParser().encodeResourceToString(proposedState);

        try {
            return transactionTemplate.execute(status -> {
                int updatedRows = repository.updateIfVersionMatches(
                        key.resourceType(),
                        key.id(),
                        expectedVerLong,
                        nextVersionLong,
                        json,
                        Instant.now());

                if (updatedRows == 1) {
                    log.info("Authoritatively committed UPDATE for {}/{} from version {} to {}",
                            key.resourceType(), key.id(), expectedVerLong, nextVersionLong);
                    return new AuthoritativePersistenceResult.Committed<>(proposedState, nextVersion);
                }

                // Precondition check failed (0 rows updated): diagnose whether absent, deleted, or version mismatch
                Optional<FhirResourceEntity> currentEntityOpt = repository.findByResourceTypeAndFhirId(key.resourceType(), key.id());
                if (currentEntityOpt.isEmpty() || currentEntityOpt.get().isDeleted()) {
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
                }

                FhirResourceEntity currentEntity = currentEntityOpt.get();
                AuthoritativeVersion currentVer = AuthoritativeVersion.of(currentEntity.getVersionId());
                log.warn("UPDATE precondition failed: expected version {} did not match current version {} for {}",
                        expectedVersion, currentVer, key);
                return new AuthoritativePersistenceResult.Conflict<>(
                        AuthoritativePreconditionConflict.expectedVersionMismatch(key, expectedVersion, currentVer)
                );
            });
        } catch (TransactionSystemException tse) {
            log.error("Transaction commit outcome unknown for UPDATE {}: {}", key, tse.getMessage(), tse);
            return new AuthoritativePersistenceResult.OutcomeUnknown<>(
                    "Transaction commit outcome is unknown: " + tse.getMessage(), tse);
        } catch (CannotCreateTransactionException ccte) {
            log.error("Cannot connect to transaction coordinator for UPDATE {}: {}", key, ccte.getMessage(), ccte);
            return new AuthoritativePersistenceResult.OutcomeUnknown<>(
                    "Transaction coordinator connection failure: " + ccte.getMessage(), ccte);
        } catch (Exception e) {
            log.error("Persistence failure updating {}: {}", key, e.getMessage(), e);
            return new AuthoritativePersistenceResult.NotCommitted<>("Persistence failure: " + e.getMessage(), e);
        }
    }
}
