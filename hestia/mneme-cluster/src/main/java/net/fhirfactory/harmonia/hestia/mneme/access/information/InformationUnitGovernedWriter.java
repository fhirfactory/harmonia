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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.hestia.mneme.access.information;

import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.hapifhir.persistence.model.DurabilityAssurance;
import net.fhirfactory.harmonia.hestia.mnemosyne.api.GovernedInformationUnitPersistence;
import net.fhirfactory.harmonia.model.core.information.access.ActiveStateCoordinationResult;
import net.fhirfactory.harmonia.model.core.information.access.ActiveStateCoordinator;
import net.fhirfactory.harmonia.model.core.information.access.GovernedRead;
import net.fhirfactory.harmonia.model.core.information.access.GovernedWriter;
import net.fhirfactory.harmonia.model.core.information.access.ResourceKey;
import net.fhirfactory.harmonia.model.core.information.access.WriteResult;
import net.fhirfactory.harmonia.model.core.information.entity.InformationUnit;
import net.fhirfactory.harmonia.themis.api.ThemisAuthorizer;
import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisResource;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Mneme-facing governed IU writer. It authorizes at the caller boundary, consumes the established
 * active-state token for updates, and accepts only Mnemosyne-confirmed local database persistence.
 * V001 does not materialize an active IU cache, so committed writes are explicitly marked degraded.
 */
public final class InformationUnitGovernedWriter implements GovernedWriter {

    private static final String RESOURCE_TYPE = "InformationUnit";
    private static final String V001_DEGRADATION = "Local PostgreSQL transaction committed; V001 does not establish active-cache convergence or production cross-site durability";

    private final ThemisAuthorizer themisAuthorizer;
    private final ActiveStateCoordinator activeStateCoordinator;
    private final GovernedInformationUnitPersistence persistence;

    /** Creates the Mneme writer from Themis, active-state coordination, and the Mnemosyne port. */
    public InformationUnitGovernedWriter(
            ThemisAuthorizer themisAuthorizer,
            ActiveStateCoordinator activeStateCoordinator,
            GovernedInformationUnitPersistence persistence) {
        this.themisAuthorizer = Objects.requireNonNull(themisAuthorizer, "themisAuthorizer must not be null");
        this.activeStateCoordinator = Objects.requireNonNull(activeStateCoordinator, "activeStateCoordinator must not be null");
        this.persistence = Objects.requireNonNull(persistence, "persistence must not be null");
    }

    /** Authorizes and submits a new IU to Mnemosyne without treating cache acceptance as success. */
    @Override
    public <T> WriteResult<T> create(ResourceKey key, T resource, ThemisSecurityContext securityContext) {
        InformationUnit informationUnit = validate(key, resource, securityContext);
        String denied = authorizationFailure(ThemisAction.CREATE, key, securityContext);
        if (denied != null) {
            return WriteResult.notCommitted(key, denied);
        }
        return mapWriteResult(key, persistence.create(key, informationUnit, securityContext));
    }

    /**
     * Authorizes first, consumes the read's active-state token once, then conditionally appends the
     * authoritative version. A stale/unavailable active token prevents reaching Mnemosyne.
     */
    @Override
    public <T> WriteResult<T> update(GovernedRead<T> current, T proposed, ThemisSecurityContext securityContext) {
        if (current == null) {
            throw new IllegalArgumentException("GovernedRead must not be null");
        }
        InformationUnit informationUnit = validate(current.key(), proposed, securityContext);
        String denied = authorizationFailure(ThemisAction.UPDATE, current.key(), securityContext);
        if (denied != null) {
            return WriteResult.notCommitted(current.key(), denied);
        }

        ActiveStateCoordinationResult coordinationResult;
        try {
            coordinationResult = activeStateCoordinator.consume(current.key(), current.activeToken());
        } catch (RuntimeException exception) {
            return WriteResult.notCommitted(current.key(), "Mneme active-state coordination failed");
        }
        if (coordinationResult == null || coordinationResult == ActiveStateCoordinationResult.UNAVAILABLE) {
            return WriteResult.notCommitted(current.key(), "Mneme active-state coordination is unavailable");
        }
        if (coordinationResult == ActiveStateCoordinationResult.STALE) {
            return WriteResult.activeStateConflict(current.key(), "Mneme active-state token is stale");
        }
        return mapWriteResult(current.key(), persistence.update(
                current.key(), informationUnit, current.expectedAuthoritativeVersion(), securityContext));
    }

    /** Validates that this writer receives an IU under its canonical UUID resource key. */
    private InformationUnit validate(ResourceKey key, Object resource, ThemisSecurityContext securityContext) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(resource, "resource must not be null");
        Objects.requireNonNull(securityContext, "ThemisSecurityContext must not be null");
        if (!RESOURCE_TYPE.equals(key.resourceType())) {
            throw new IllegalArgumentException("Resource key type must be " + RESOURCE_TYPE);
        }
        if (!(resource instanceof InformationUnit informationUnit)) {
            throw new IllegalArgumentException("Resource payload must be an InformationUnit");
        }
        UUID identifier;
        try {
            identifier = UUID.fromString(key.id());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("InformationUnit resource key must contain a UUID", exception);
        }
        if (!identifier.toString().equalsIgnoreCase(key.id()) || !identifier.equals(informationUnit.identifier().uuid())) {
            throw new IllegalArgumentException("Resource key does not match canonical InformationUnit identity");
        }
        return informationUnit;
    }

    /** Builds a Themis request and returns a safe failure reason when evaluation does not allow access. */
    private String authorizationFailure(ThemisAction action, ResourceKey key, ThemisSecurityContext context) {
        ThemisPrincipal principal = context.requestingPrincipal() != null
                ? context.requestingPrincipal()
                : ThemisPrincipal.system("anonymous");
        ThemisAuthorizationRequest request = ThemisAuthorizationRequest.of(
                principal,
                context.authorities() == null ? Set.of() : context.authorities(),
                action,
                ThemisResource.of(key.resourceType(), key.id()),
                context);
        try {
            ThemisAuthorizationDecision decision = themisAuthorizer.authorize(request);
            return decision != null && decision.isAllowed()
                    ? null
                    : "Themis denied " + action + " for " + key;
        } catch (RuntimeException exception) {
            return "Themis authorization evaluation failed for " + action + " " + key;
        }
    }

    /** Maps Mnemosyne outcomes and rejects any commit below the V001 local-transaction minimum. */
    @SuppressWarnings("unchecked")
    private <T> WriteResult<T> mapWriteResult(
            ResourceKey key,
            AuthoritativePersistenceResult<InformationUnit> result) {
        if (result == null) {
            return WriteResult.outcomeUnknown(key, "Mnemosyne returned no persistence outcome");
        }
        if (result instanceof AuthoritativePersistenceResult.Committed<InformationUnit> committed) {
            if (!committed.durabilityAssurance().satisfies(DurabilityAssurance.LOCAL_TRANSACTION_COMMITTED)) {
                return WriteResult.outcomeUnknown(
                        key,
                        "Mnemosyne committed at insufficient durability assurance: " + committed.durabilityAssurance());
            }
            return WriteResult.committedDegraded(
                    key,
                    (T) committed.persistedResource(),
                    committed.authoritativeVersion(),
                    V001_DEGRADATION);
        }
        if (result instanceof AuthoritativePersistenceResult.Conflict<InformationUnit> conflict) {
            return WriteResult.authoritativeConflict(conflict.conflict());
        }
        if (result instanceof AuthoritativePersistenceResult.OutcomeUnknown<InformationUnit> unknown) {
            return WriteResult.outcomeUnknown(key, unknown.message());
        }
        if (result instanceof AuthoritativePersistenceResult.NotCommitted<InformationUnit> notCommitted) {
            return WriteResult.notCommitted(key, notCommitted.failureMessage());
        }
        return WriteResult.notCommitted(key, "Mnemosyne did not establish the InformationUnit write");
    }
}