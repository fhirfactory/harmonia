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

package net.fhirfactory.harmonia.hapifhir.governed;

import net.fhirfactory.harmonia.hapifhir.persistence.AuthoritativePersistencePort;
import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateCoordinationResult;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateCoordinator;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateConvergencePort;
import net.fhirfactory.harmonia.model.governedwrite.ConvergenceStatus;
import net.fhirfactory.harmonia.model.governedwrite.GovernedRead;
import net.fhirfactory.harmonia.model.governedwrite.GovernedWriter;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import net.fhirfactory.harmonia.model.governedwrite.WriteResult;
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

/**
 * Production orchestrator implementing the governed-write lifecycle for CREATE and UPDATE.
 * <p>
 * Unifies:
 * <ol>
 *   <li>Themis Security Authorization (strictly evaluated before active token consumption and persistence)</li>
 *   <li>Mneme Active-State Coordination (token consumption for UPDATE)</li>
 *   <li>Mnemosyne Authoritative Persistence (atomic persistence and absence/predecessor precondition verification)</li>
 *   <li>Guarded Mneme Convergence (CAS-loop post-commit cache synchronization with newer-version protection)</li>
 * </ol>
 */
public class DefaultGovernedWriter implements GovernedWriter {

    private static final Logger log = LoggerFactory.getLogger(DefaultGovernedWriter.class);

    private final ThemisAuthorizer themisAuthorizer;
    private final ActiveStateCoordinator activeStateCoordinator;
    private final AuthoritativePersistencePort<IBaseResource> persistencePort;
    private final ActiveStateConvergencePort convergencePort;

    public DefaultGovernedWriter(
            ThemisAuthorizer themisAuthorizer,
            ActiveStateCoordinator activeStateCoordinator,
            AuthoritativePersistencePort<IBaseResource> persistencePort,
            ActiveStateConvergencePort convergencePort
    ) {
        this.themisAuthorizer = Objects.requireNonNull(themisAuthorizer, "themisAuthorizer must not be null");
        this.activeStateCoordinator = Objects.requireNonNull(activeStateCoordinator, "activeStateCoordinator must not be null");
        this.persistencePort = Objects.requireNonNull(persistencePort, "persistencePort must not be null");
        this.convergencePort = Objects.requireNonNull(convergencePort, "convergencePort must not be null");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> WriteResult<T> create(ResourceKey key, T resource, ThemisSecurityContext securityContext) {
        if (key == null) {
            throw new IllegalArgumentException("ResourceKey must not be null");
        }
        if (resource == null) {
            throw new IllegalArgumentException("resource must not be null");
        }
        if (securityContext == null) {
            throw new IllegalArgumentException("ThemisSecurityContext must not be null");
        }
        if (!(resource instanceof IBaseResource baseResource)) {
            throw new IllegalArgumentException("Resource payload must implement IBaseResource");
        }

        // 1. Themis Authorization
        ThemisPrincipal principal = securityContext.requestingPrincipal() != null ? securityContext.requestingPrincipal() : ThemisPrincipal.system("anonymous");
        ThemisResource target = ThemisResource.of(key.resourceType(), key.id());
        ThemisAuthorizationRequest authRequest = ThemisAuthorizationRequest.of(
                principal,
                securityContext.authorities() != null ? securityContext.authorities() : Collections.emptySet(),
                ThemisAction.CREATE,
                target,
                securityContext
        );

        ThemisAuthorizationDecision decision;
        try {
            decision = themisAuthorizer.authorize(authRequest);
        } catch (Exception e) {
            log.warn("Themis authorization threw exception during CREATE for {}: {}", key, e.getMessage());
            return WriteResult.notCommitted(key, "Themis authorization error: " + e.getMessage());
        }

        if (decision == null || decision.isDenied()) {
            String msg = (decision != null && decision.message() != null) ? decision.message() : "Authorization denied by policy";
            log.info("Themis denied CREATE for {} (principal={}): {}", key, principal.principalId(), msg);
            return WriteResult.notCommitted(key, "Themis authorization denied: " + msg);
        }

        // 2. Authoritative Persistence (Precondition: absence enforced by Mnemosyne)
        AuthoritativePersistenceResult<IBaseResource> persistResult;
        try {
            persistResult = persistencePort.create(key, baseResource);
        } catch (Exception e) {
            log.error("Mnemosyne CREATE persistence exception for {}: {}", key, e.getMessage(), e);
            return WriteResult.notCommitted(key, "Authoritative persistence error: " + e.getMessage());
        }

        if (persistResult instanceof AuthoritativePersistenceResult.Conflict<IBaseResource> conflict) {
            log.info("Authoritative conflict on CREATE for {}: {}", key, conflict.conflict().message());
            return WriteResult.authoritativeConflict(conflict.conflict());
        } else if (persistResult instanceof AuthoritativePersistenceResult.NotCommitted<IBaseResource> notCommitted) {
            log.warn("Authoritative persistence not committed on CREATE for {}: {}", key, notCommitted.failureMessage());
            return WriteResult.notCommitted(key, notCommitted.failureMessage());
        } else if (persistResult instanceof AuthoritativePersistenceResult.OutcomeUnknown<IBaseResource> unknown) {
            log.error("Authoritative persistence outcome unknown on CREATE for {}: {}", key, unknown.message());
            return WriteResult.outcomeUnknown(key, unknown.message());
        } else if (persistResult instanceof AuthoritativePersistenceResult.Committed<IBaseResource> committed) {
            // 3. Guarded Post-Commit Convergence
            ConvergenceStatus convStatus;
            try {
                convStatus = convergencePort.converge(key, (T) committed.persistedResource(), committed.authoritativeVersion());
            } catch (Exception e) {
                log.warn("Post-commit convergence failed for {}: {}", key, e.getMessage());
                convStatus = ConvergenceStatus.DEGRADED;
            }

            if (convStatus == ConvergenceStatus.CONVERGED) {
                return WriteResult.committed(key, (T) committed.persistedResource(), committed.authoritativeVersion());
            } else {
                return WriteResult.committedDegraded(
                        key,
                        (T) committed.persistedResource(),
                        committed.authoritativeVersion(),
                        "Active-state cache convergence degraded"
                );
            }
        }

        return WriteResult.notCommitted(key, "Unknown persistence result type: " + persistResult.getClass().getName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> WriteResult<T> update(GovernedRead<T> current, T proposed, ThemisSecurityContext securityContext) {
        if (current == null) {
            throw new IllegalArgumentException("GovernedRead must not be null");
        }
        if (proposed == null) {
            throw new IllegalArgumentException("proposed resource must not be null");
        }
        if (securityContext == null) {
            throw new IllegalArgumentException("ThemisSecurityContext must not be null");
        }
        if (!(proposed instanceof IBaseResource baseProposed)) {
            throw new IllegalArgumentException("Proposed resource payload must implement IBaseResource");
        }

        ResourceKey key = current.key();

        // 1. Themis Authorization (strictly before active-state token consumption)
        ThemisPrincipal principal = securityContext.requestingPrincipal() != null ? securityContext.requestingPrincipal() : ThemisPrincipal.system("anonymous");
        ThemisResource target = ThemisResource.of(key.resourceType(), key.id());
        ThemisAuthorizationRequest authRequest = ThemisAuthorizationRequest.of(
                principal,
                securityContext.authorities() != null ? securityContext.authorities() : Collections.emptySet(),
                ThemisAction.UPDATE,
                target,
                securityContext
        );

        ThemisAuthorizationDecision decision;
        try {
            decision = themisAuthorizer.authorize(authRequest);
        } catch (Exception e) {
            log.warn("Themis authorization threw exception during UPDATE for {}: {}", key, e.getMessage());
            return WriteResult.notCommitted(key, "Themis authorization error: " + e.getMessage());
        }

        if (decision == null || decision.isDenied()) {
            String msg = (decision != null && decision.message() != null) ? decision.message() : "Authorization denied by policy";
            log.info("Themis denied UPDATE for {} (principal={}): {}", key, principal.principalId(), msg);
            return WriteResult.notCommitted(key, "Themis authorization denied: " + msg);
        }

        // 2. Active-State Coordination (consume observed token)
        ActiveStateCoordinationResult coordResult;
        try {
            coordResult = activeStateCoordinator.consume(key, current.activeToken());
        } catch (Exception e) {
            log.warn("Active-state coordinator threw exception for {}: {}", key, e.getMessage());
            coordResult = ActiveStateCoordinationResult.UNAVAILABLE;
        }

        if (coordResult == ActiveStateCoordinationResult.STALE) {
            log.info("Active-state token is STALE for {}. Rejecting update.", key);
            return WriteResult.activeStateConflict(key, "Active state token is stale");
        } else if (coordResult == ActiveStateCoordinationResult.UNAVAILABLE) {
            log.warn("Active-state coordination UNAVAILABLE for {}. Failing fast.", key);
            return WriteResult.notCommitted(key, "Active state coordination unavailable");
        }

        // 3. Authoritative Persistence (Precondition: predecessor version matches)
        AuthoritativePersistenceResult<IBaseResource> persistResult;
        try {
            persistResult = persistencePort.update(key, baseProposed, current.expectedAuthoritativeVersion());
        } catch (Exception e) {
            log.error("Mnemosyne UPDATE persistence exception for {}: {}", key, e.getMessage(), e);
            return WriteResult.notCommitted(key, "Authoritative persistence error: " + e.getMessage());
        }

        if (persistResult instanceof AuthoritativePersistenceResult.Conflict<IBaseResource> conflict) {
            log.info("Authoritative conflict on UPDATE for {}: {}", key, conflict.conflict().message());
            return WriteResult.authoritativeConflict(conflict.conflict());
        } else if (persistResult instanceof AuthoritativePersistenceResult.NotCommitted<IBaseResource> notCommitted) {
            log.warn("Authoritative persistence not committed on UPDATE for {}: {}", key, notCommitted.failureMessage());
            return WriteResult.notCommitted(key, notCommitted.failureMessage());
        } else if (persistResult instanceof AuthoritativePersistenceResult.OutcomeUnknown<IBaseResource> unknown) {
            log.error("Authoritative persistence outcome unknown on UPDATE for {}: {}", key, unknown.message());
            return WriteResult.outcomeUnknown(key, unknown.message());
        } else if (persistResult instanceof AuthoritativePersistenceResult.Committed<IBaseResource> committed) {
            // 4. Guarded Post-Commit Convergence
            ConvergenceStatus convStatus;
            try {
                convStatus = convergencePort.converge(key, (T) committed.persistedResource(), committed.authoritativeVersion());
            } catch (Exception e) {
                log.warn("Post-commit convergence failed for {}: {}", key, e.getMessage());
                convStatus = ConvergenceStatus.DEGRADED;
            }

            if (convStatus == ConvergenceStatus.CONVERGED) {
                return WriteResult.committed(key, (T) committed.persistedResource(), committed.authoritativeVersion());
            } else {
                return WriteResult.committedDegraded(
                        key,
                        (T) committed.persistedResource(),
                        committed.authoritativeVersion(),
                        "Active-state cache convergence degraded"
                );
            }
        }

        return WriteResult.notCommitted(key, "Unknown persistence result type: " + persistResult.getClass().getName());
    }
}
