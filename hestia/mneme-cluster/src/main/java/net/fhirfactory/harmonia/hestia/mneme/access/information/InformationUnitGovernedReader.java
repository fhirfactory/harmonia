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
import net.fhirfactory.harmonia.hestia.mneme.coordination.ActiveCoordinationUnavailableException;
import net.fhirfactory.harmonia.model.core.information.access.ActiveStateCoordinator;
import net.fhirfactory.harmonia.model.core.information.access.ActiveStateToken;
import net.fhirfactory.harmonia.model.core.information.access.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.core.information.access.GovernedRead;
import net.fhirfactory.harmonia.model.core.information.access.GovernedReader;
import net.fhirfactory.harmonia.model.core.information.access.ResourceKey;
import net.fhirfactory.harmonia.model.core.information.entity.InformationUnit;
import net.fhirfactory.harmonia.themis.api.ThemisAuthorizer;
import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisResource;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Mneme point reader for authoritative InformationUnits. It does not expose a database/cache
 * implementation and fails closed unless the read establishes an authoritative version and a
 * usable active-state coordination token. V001 does not claim cache materialization.
 */
public final class InformationUnitGovernedReader implements GovernedReader {

    private final ThemisAuthorizer themisAuthorizer;
    private final ActiveStateCoordinator activeStateCoordinator;
    private final GovernedInformationUnitPersistence persistence;

    /** Creates the Mneme reader with Themis, active-state coordination and the Mnemosyne port. */
    public InformationUnitGovernedReader(
            ThemisAuthorizer themisAuthorizer,
            ActiveStateCoordinator activeStateCoordinator,
            GovernedInformationUnitPersistence persistence) {
        this.themisAuthorizer = Objects.requireNonNull(themisAuthorizer, "themisAuthorizer must not be null");
        this.activeStateCoordinator = Objects.requireNonNull(activeStateCoordinator, "activeStateCoordinator must not be null");
        this.persistence = Objects.requireNonNull(persistence, "persistence must not be null");
    }

    /** Performs a Themis-governed authoritative read and returns its version and active token. */
    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<GovernedRead<T>> read(ResourceKey key, ThemisSecurityContext securityContext) {
        validateKey(key, securityContext);
        ThemisPrincipal principal = securityContext.requestingPrincipal() != null
                ? securityContext.requestingPrincipal()
                : ThemisPrincipal.system("anonymous");
        ThemisAuthorizationRequest request = ThemisAuthorizationRequest.of(
                principal,
                securityContext.authorities() == null ? Collections.emptySet() : securityContext.authorities(),
                ThemisAction.READ,
                ThemisResource.of(key.resourceType(), key.id()),
                securityContext);
        ThemisAuthorizationDecision decision;
        try {
            decision = themisAuthorizer.authorize(request);
        } catch (RuntimeException exception) {
            throw new SecurityException("Themis authorization failed for InformationUnit READ " + key, exception);
        }
        if (decision == null || !decision.isAllowed()) {
            throw new SecurityException("Themis denied InformationUnit READ " + key);
        }

        AuthoritativePersistenceResult<InformationUnit> result;
        try {
            result = persistence.read(key, securityContext);
        } catch (RuntimeException exception) {
            throw new ActiveCoordinationUnavailableException("Mnemosyne InformationUnit READ failed", exception);
        }
        if (result == null) {
            throw new ActiveCoordinationUnavailableException("Mnemosyne returned no InformationUnit READ outcome");
        }
        if (result instanceof AuthoritativePersistenceResult.Absent<InformationUnit>) {
            return Optional.empty();
        }
        if (result instanceof AuthoritativePersistenceResult.Committed<InformationUnit> committed) {
            if (!committed.durabilityAssurance().satisfies(DurabilityAssurance.AUTHORITATIVE_CONFIRMED)) {
                throw new ActiveCoordinationUnavailableException("Mnemosyne read did not confirm an authoritative source");
            }
            InformationUnit resource = committed.persistedResource();
            AuthoritativeVersion version = committed.authoritativeVersion();
            ActiveStateToken token;
            try {
                token = activeStateCoordinator.observe(key);
            } catch (RuntimeException exception) {
                throw new ActiveCoordinationUnavailableException("Mneme active-state token observation failed", exception);
            }
            if (token == null) {
                throw new ActiveCoordinationUnavailableException("Mneme active-state token observation returned no token");
            }
            return Optional.of(GovernedRead.of(key, (T) resource, token, version));
        }
        if (result instanceof AuthoritativePersistenceResult.OutcomeUnknown<InformationUnit> unknown) {
            throw new ActiveCoordinationUnavailableException("Mnemosyne read outcome is unknown: " + unknown.message(), unknown.cause());
        }
        if (result instanceof AuthoritativePersistenceResult.NotCommitted<InformationUnit> notCommitted) {
            throw new ActiveCoordinationUnavailableException("Mnemosyne read failed: " + notCommitted.failureMessage(), notCommitted.cause());
        }
        throw new ActiveCoordinationUnavailableException("Mnemosyne returned an unexpected result for InformationUnit READ");
    }

    /** Rejects wrong resource types and missing Themis context before any external call. */
    private void validateKey(ResourceKey key, ThemisSecurityContext securityContext) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(securityContext, "ThemisSecurityContext must not be null");
        if (!"InformationUnit".equals(key.resourceType())) {
            throw new IllegalArgumentException("Resource key type must be InformationUnit");
        }
        try {
            if (!UUID.fromString(key.id()).toString().equalsIgnoreCase(key.id())) {
                throw new IllegalArgumentException("Resource key UUID must use canonical form");
            }
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("InformationUnit resource key must contain a canonical UUID", exception);
        }
    }
}