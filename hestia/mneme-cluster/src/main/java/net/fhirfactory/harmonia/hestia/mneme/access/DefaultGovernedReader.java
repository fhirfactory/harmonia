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

package net.fhirfactory.harmonia.hestia.mneme.access;

import net.fhirfactory.harmonia.hapifhir.persistence.AuthoritativePersistencePort;
import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.hestia.mneme.coordination.ActiveCoordinationUnavailableException;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateCoordinator;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateConvergencePort;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateToken;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.ConvergenceStatus;
import net.fhirfactory.harmonia.model.governedwrite.GovernedBoundaryValidator;
import net.fhirfactory.harmonia.model.governedwrite.GovernedRead;
import net.fhirfactory.harmonia.model.governedwrite.GovernedReader;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import net.fhirfactory.harmonia.themis.api.ThemisAuthorizer;
import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisResource;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

/**
 * Production implementation of {@link GovernedReader} under Mneme subsystem ownership.
 * <p>
 * Unifies:
 * <ol>
 *   <li>Calliope boundary validation (rejecting unmanaged types fail-closed with {@link IllegalArgumentException})</li>
 *   <li>Themis READ authorization (throwing canonical {@link SecurityException} on DENY)</li>
 *   <li>Authoritative point retrieval from Mnemosyne via {@link AuthoritativePersistencePort}</li>
 *   <li>Typed absence mapping (returning {@link Optional#empty()} iff Mnemosyne establishes resource is absent)</li>
 *   <li>Post-read active-state cache convergence via {@link ActiveStateConvergencePort}</li>
 *   <li>Post-convergence {@link ActiveStateToken} observation via {@link ActiveStateCoordinator}</li>
 * </ol>
 */
public class DefaultGovernedReader implements GovernedReader {

    private static final Logger log = LoggerFactory.getLogger(DefaultGovernedReader.class);

    private final ThemisAuthorizer themisAuthorizer;
    private final AuthoritativePersistencePort<IBaseResource> persistencePort;
    private final ActiveStateConvergencePort convergencePort;
    private final ActiveStateCoordinator activeStateCoordinator;

    public DefaultGovernedReader(
            ThemisAuthorizer themisAuthorizer,
            AuthoritativePersistencePort<IBaseResource> persistencePort,
            ActiveStateConvergencePort convergencePort,
            ActiveStateCoordinator activeStateCoordinator
    ) {
        this.themisAuthorizer = Objects.requireNonNull(themisAuthorizer, "themisAuthorizer must not be null");
        this.persistencePort = Objects.requireNonNull(persistencePort, "persistencePort must not be null");
        this.convergencePort = Objects.requireNonNull(convergencePort, "convergencePort must not be null");
        this.activeStateCoordinator = Objects.requireNonNull(activeStateCoordinator, "activeStateCoordinator must not be null");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<GovernedRead<T>> read(ResourceKey key, ThemisSecurityContext securityContext) {
        if (key == null) {
            throw new IllegalArgumentException("ResourceKey must not be null");
        }
        if (securityContext == null) {
            throw new IllegalArgumentException("ThemisSecurityContext must not be null");
        }

        // 1. Boundary Validation (Managed-type check fail-closed)
        GovernedBoundaryValidator.requireManagedType(key);

        // 2. Themis Authorization (fail-closed, throws SecurityException on DENY)
        ThemisPrincipal principal = securityContext.requestingPrincipal() != null
                ? securityContext.requestingPrincipal()
                : ThemisPrincipal.system("anonymous");
        ThemisResource target = ThemisResource.of(key.resourceType(), key.id());
        ThemisAuthorizationRequest authRequest = ThemisAuthorizationRequest.of(
                principal,
                securityContext.authorities() != null ? securityContext.authorities() : Collections.emptySet(),
                ThemisAction.READ,
                target,
                securityContext
        );

        ThemisAuthorizationDecision decision;
        try {
            decision = themisAuthorizer.authorize(authRequest);
        } catch (Exception e) {
            log.warn("Themis authorization threw exception during READ for {}: {}", key, e.getMessage());
            throw new SecurityException("Themis authorization error for READ " + key + ": " + e.getMessage(), e);
        }

        if (decision == null || decision.isDenied()) {
            String msg = (decision != null && decision.message() != null) ? decision.message() : "Authorization denied by policy";
            log.info("Themis denied READ for {} (principal={}): {}", key, principal.principalId(), msg);
            throw new SecurityException("Access denied by Themis policy for READ " + key + ": " + msg);
        }

        // 3. Authoritative Point READ
        AuthoritativePersistenceResult<IBaseResource> persistResult;
        try {
            persistResult = persistencePort.read(key);
        } catch (Exception e) {
            log.error("Mnemosyne READ persistence exception for {}: {}", key, e.getMessage(), e);
            throw new ActiveCoordinationUnavailableException("Authoritative persistence error during READ: " + e.getMessage(), e);
        }

        if (persistResult == null) {
            throw new ActiveCoordinationUnavailableException("Null persistence result received for READ " + key);
        }

        if (persistResult instanceof AuthoritativePersistenceResult.Absent) {
            log.debug("Authoritative READ established resource absent for {}", key);
            return Optional.empty();
        }

        if (persistResult instanceof AuthoritativePersistenceResult.NotCommitted<IBaseResource> nc) {
            log.warn("Authoritative READ not committed for {}: {}", key, nc.failureMessage());
            throw new ActiveCoordinationUnavailableException(
                    "Authoritative read not committed: " + nc.failureMessage(),
                    nc.cause()
            );
        }

        if (persistResult instanceof AuthoritativePersistenceResult.OutcomeUnknown<IBaseResource> ou) {
            log.error("Authoritative READ outcome unknown for {}: {}", key, ou.message());
            throw new ActiveCoordinationUnavailableException(
                    "Authoritative read outcome unknown: " + ou.message(),
                    ou.cause()
            );
        }

        if (persistResult instanceof AuthoritativePersistenceResult.Conflict) {
            log.error("Authoritative READ encountered unexpected conflict for {}", key);
            throw new ActiveCoordinationUnavailableException("Authoritative read encountered unexpected conflict");
        }

        if (persistResult instanceof AuthoritativePersistenceResult.Committed<IBaseResource> committed) {
            T payload = (T) committed.persistedResource();
            AuthoritativeVersion version = committed.authoritativeVersion();

            // 4. Mneme Convergence
            ConvergenceStatus convStatus;
            try {
                convStatus = convergencePort.converge(key, payload, version);
            } catch (Exception e) {
                log.warn("Post-read convergence failed for {}: {}", key, e.getMessage());
                convStatus = ConvergenceStatus.DEGRADED;
            }

            if (convStatus != ConvergenceStatus.CONVERGED) {
                log.warn("Active-state cache convergence degraded for {}. Failing closed.", key);
                throw new ActiveCoordinationUnavailableException("Active-state cache convergence degraded for " + key);
            }

            // 5. Observe Token (strictly after successful convergence)
            ActiveStateToken token;
            try {
                token = activeStateCoordinator.observe(key);
            } catch (Exception e) {
                log.warn("Active-state token observation threw exception for {}: {}", key, e.getMessage());
                throw new ActiveCoordinationUnavailableException("Active-state token observation failed for " + key, e);
            }

            if (token == null) {
                log.warn("Active-state coordinator returned null token for {}", key);
                throw new ActiveCoordinationUnavailableException("Active-state token observation returned null for " + key);
            }

            // 6. Return Envelope
            return Optional.of(GovernedRead.of(key, payload, token, version));
        }

        throw new ActiveCoordinationUnavailableException(
                "Unknown persistence result type: " + persistResult.getClass().getName()
        );
    }
}
