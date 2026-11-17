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

package net.fhirfactory.harmonia.model.core.information.persistence;

import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Factory helpers for constructing {@link PersistenceOperationEnvelope} instances.
 */
public final class PersistenceOperationEnvelopeFactory {

    private PersistenceOperationEnvelopeFactory() {
        // Utility factory
    }

    public static <T> PersistenceOperationEnvelope<T> fromSecurityContext(
            PersistenceOperationType operationType,
            String resourceType,
            String resourceId,
            Long expectedVersion,
            T payload,
            ThemisSecurityContext securityContext,
            ThemisAuthorizationDecision authorizationDecision
    ) {
        Objects.requireNonNull(securityContext, "securityContext must not be null");
        Objects.requireNonNull(authorizationDecision, "authorizationDecision must not be null");

        ThemisPrincipal origPrincipal = securityContext.originatingPrincipal();
        if (origPrincipal == null) {
            origPrincipal = securityContext.requestingPrincipal();
        }
        ThemisPrincipal execPrincipal = securityContext.executingPrincipal();
        if (execPrincipal == null) {
            execPrincipal = origPrincipal;
        }
        String causId = securityContext.causationId();
        if (causId == null || causId.isBlank()) {
            causId = securityContext.correlationId();
        }

        return new PersistenceOperationEnvelopeBuilder<T>()
                .operationId(UUID.randomUUID().toString())
                .operationType(operationType)
                .resourceType(resourceType)
                .resourceId(resourceId)
                .expectedVersion(expectedVersion)
                .payload(payload)
                .originatingPrincipal(origPrincipal)
                .originatingAuthorities(securityContext.authorities())
                .securityDomain(securityContext.securityDomain())
                .correlationId(securityContext.correlationId())
                .causationId(causId)
                .requestedAt(securityContext.requestedAt() != null ? securityContext.requestedAt() : Instant.now())
                .authorizationDecision(authorizationDecision)
                .executingPrincipal(execPrincipal)
                .attributes(securityContext.attributes())
                .build();
    }

    public static <T> PersistenceOperationEnvelope<T> ofCreate(
            String resourceType,
            String resourceId,
            T payload,
            ThemisSecurityContext securityContext,
            ThemisAuthorizationDecision authorizationDecision
    ) {
        return fromSecurityContext(PersistenceOperationType.CREATE, resourceType, resourceId, null, payload, securityContext, authorizationDecision);
    }

    public static <T> PersistenceOperationEnvelope<T> ofUpdate(
            String resourceType,
            String resourceId,
            Long expectedVersion,
            T payload,
            ThemisSecurityContext securityContext,
            ThemisAuthorizationDecision authorizationDecision
    ) {
        return fromSecurityContext(PersistenceOperationType.UPDATE, resourceType, resourceId, expectedVersion, payload, securityContext, authorizationDecision);
    }

    public static <T> PersistenceOperationEnvelope<T> ofUpdate(
            String resourceType,
            String resourceId,
            T payload,
            ThemisSecurityContext securityContext,
            ThemisAuthorizationDecision authorizationDecision
    ) {
        return ofUpdate(resourceType, resourceId, null, payload, securityContext, authorizationDecision);
    }

    public static <T> PersistenceOperationEnvelope<T> ofDelete(
            String resourceType,
            String resourceId,
            Long expectedVersion,
            ThemisSecurityContext securityContext,
            ThemisAuthorizationDecision authorizationDecision
    ) {
        return fromSecurityContext(PersistenceOperationType.DELETE, resourceType, resourceId, expectedVersion, null, securityContext, authorizationDecision);
    }

    public static <T> PersistenceOperationEnvelope<T> ofDelete(
            String resourceType,
            String resourceId,
            ThemisSecurityContext securityContext,
            ThemisAuthorizationDecision authorizationDecision
    ) {
        return ofDelete(resourceType, resourceId, null, securityContext, authorizationDecision);
    }
}
