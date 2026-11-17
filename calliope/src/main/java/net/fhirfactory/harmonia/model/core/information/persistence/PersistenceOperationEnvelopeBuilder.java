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
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;

import java.time.Instant;
import java.util.*;

/**
 * Fluent builder for creating {@link PersistenceOperationEnvelope} instances.
 *
 * @param <T> payload type
 */
public class PersistenceOperationEnvelopeBuilder<T> {

    protected String operationId;
    protected PersistenceOperationType operationType;
    protected String resourceType;
    protected String resourceId;
    protected Long expectedVersion;
    protected T payload;
    protected ThemisPrincipal originatingPrincipal;
    protected Set<ThemisAuthority> originatingAuthorities = new LinkedHashSet<>();
    protected String securityDomain;
    protected String correlationId;
    protected String causationId;
    protected Instant requestedAt;
    protected ThemisAuthorizationDecision authorizationDecision;
    protected ThemisPrincipal executingPrincipal;
    protected Instant createdAt;
    protected Map<String, String> attributes = new LinkedHashMap<>();

    public PersistenceOperationEnvelopeBuilder() {
    }

    public PersistenceOperationEnvelopeBuilder<T> operationId(String operationId) {
        this.operationId = operationId;
        return this;
    }

    public PersistenceOperationEnvelopeBuilder<T> operationType(PersistenceOperationType operationType) {
        this.operationType = operationType;
        return this;
    }

    public PersistenceOperationEnvelopeBuilder<T> resourceType(String resourceType) {
        this.resourceType = resourceType;
        return this;
    }

    public PersistenceOperationEnvelopeBuilder<T> resourceId(String resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    public PersistenceOperationEnvelopeBuilder<T> expectedVersion(Long expectedVersion) {
        this.expectedVersion = expectedVersion;
        return this;
    }

    public PersistenceOperationEnvelopeBuilder<T> payload(T payload) {
        this.payload = payload;
        return this;
    }

    public PersistenceOperationEnvelopeBuilder<T> originatingPrincipal(ThemisPrincipal originatingPrincipal) {
        this.originatingPrincipal = originatingPrincipal;
        return this;
    }

    public PersistenceOperationEnvelopeBuilder<T> originatingAuthorities(Set<ThemisAuthority> originatingAuthorities) {
        if (originatingAuthorities != null) {
            this.originatingAuthorities = new LinkedHashSet<>(originatingAuthorities);
        }
        return this;
    }

    public PersistenceOperationEnvelopeBuilder<T> addOriginatingAuthority(ThemisAuthority authority) {
        if (authority != null) {
            this.originatingAuthorities.add(authority);
        }
        return this;
    }

    public PersistenceOperationEnvelopeBuilder<T> addOriginatingAuthority(String authorityCode) {
        if (authorityCode != null && !authorityCode.isBlank()) {
            this.originatingAuthorities.add(ThemisAuthority.of(authorityCode));
        }
        return this;
    }

    public PersistenceOperationEnvelopeBuilder<T> securityDomain(String securityDomain) {
        this.securityDomain = securityDomain;
        return this;
    }

    public PersistenceOperationEnvelopeBuilder<T> correlationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    public PersistenceOperationEnvelopeBuilder<T> causationId(String causationId) {
        this.causationId = causationId;
        return this;
    }

    public PersistenceOperationEnvelopeBuilder<T> requestedAt(Instant requestedAt) {
        this.requestedAt = requestedAt;
        return this;
    }

    public PersistenceOperationEnvelopeBuilder<T> authorizationDecision(ThemisAuthorizationDecision authorizationDecision) {
        this.authorizationDecision = authorizationDecision;
        return this;
    }

    public PersistenceOperationEnvelopeBuilder<T> executingPrincipal(ThemisPrincipal executingPrincipal) {
        this.executingPrincipal = executingPrincipal;
        return this;
    }

    public PersistenceOperationEnvelopeBuilder<T> createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public PersistenceOperationEnvelopeBuilder<T> attributes(Map<String, String> attributes) {
        if (attributes != null) {
            this.attributes = new LinkedHashMap<>(attributes);
        }
        return this;
    }

    public PersistenceOperationEnvelopeBuilder<T> addAttribute(String key, String value) {
        if (key != null && value != null) {
            this.attributes.put(key, value);
        }
        return this;
    }

    public PersistenceOperationEnvelopeBuilder<T> attribute(String key, String value) {
        return addAttribute(key, value);
    }

    public PersistenceOperationEnvelope<T> build() {
        Objects.requireNonNull(operationId, "operationId must not be null");
        if (operationId.isBlank()) {
            throw new IllegalArgumentException("operationId must not be blank");
        }
        Objects.requireNonNull(operationType, "operationType must not be null");
        Objects.requireNonNull(resourceType, "resourceType must not be null");
        Objects.requireNonNull(resourceId, "resourceId must not be null");
        Objects.requireNonNull(originatingPrincipal, "originatingPrincipal must not be null");
        Objects.requireNonNull(securityDomain, "securityDomain must not be null");
        Objects.requireNonNull(correlationId, "correlationId must not be null");
        Objects.requireNonNull(causationId, "causationId must not be null");
        Objects.requireNonNull(requestedAt, "requestedAt must not be null");
        Objects.requireNonNull(authorizationDecision, "authorizationDecision must not be null");
        Objects.requireNonNull(executingPrincipal, "executingPrincipal must not be null");

        return new PersistenceOperationEnvelope<>(
                operationId,
                operationType,
                resourceType,
                resourceId,
                expectedVersion,
                payload,
                originatingPrincipal,
                originatingAuthorities,
                securityDomain,
                correlationId,
                causationId,
                requestedAt,
                authorizationDecision,
                executingPrincipal,
                createdAt != null ? createdAt : Instant.now(),
                attributes
        );
    }
}
