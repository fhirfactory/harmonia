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

package net.fhirfactory.harmonia.model.persistence;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Generic, immutable, transport-independent persistence command envelope.
 * <p>
 * Carries durable originating provenance, authorization-decision evidence, and execution boundaries
 * for {@code CREATE}, {@code UPDATE}, and {@code DELETE} operations independently of transport mechanisms,
 * cached state, or runtime write topologies.
 *
 * @param <T> payload type (typically {@link String} representing FHIR R5 JSON; null for DELETE)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersistenceOperationEnvelope<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String operationId;
    private final PersistenceOperationType operationType;
    private final String resourceType;
    private final String resourceId;
    private final Long expectedVersion;
    private final T payload;
    private final ThemisPrincipal originatingPrincipal;
    private final Set<ThemisAuthority> originatingAuthorities;
    private final String securityDomain;
    private final String correlationId;
    private final String causationId;
    private final Instant requestedAt;
    private final ThemisAuthorizationDecision authorizationDecision;
    private final ThemisPrincipal executingPrincipal;
    private final Instant createdAt;
    private final Map<String, String> attributes;

    /**
     * Canonical constructor with full parameter validation.
     *
     * @param operationId           mandatory unique command UUID
     * @param operationType         mandatory operation type (CREATE, UPDATE, DELETE)
     * @param resourceType          mandatory FHIR resource type
     * @param resourceId            mandatory logical resource identifier
     * @param expectedVersion       optional optimistic locking version
     * @param payload               resource payload (required for CREATE/UPDATE, must be null for DELETE)
     * @param originatingPrincipal  mandatory originating requesting principal
     * @param originatingAuthorities snapshot of authorities held by originator at request time
     * @param securityDomain        mandatory security domain
     * @param correlationId         mandatory root correlation UUID
     * @param causationId           mandatory causal trigger ID
     * @param requestedAt           mandatory ingress request timestamp
     * @param authorizationDecision mandatory request-time Themis authorization decision evidence
     * @param executingPrincipal    mandatory executing service/worker principal
     * @param createdAt             envelope creation timestamp (defaults to now if null)
     * @param attributes            optional operational/tracing metadata
     */
    @JsonCreator
    public PersistenceOperationEnvelope(
            @JsonProperty("operationId") String operationId,
            @JsonProperty("operationType") PersistenceOperationType operationType,
            @JsonProperty("resourceType") String resourceType,
            @JsonProperty("resourceId") String resourceId,
            @JsonProperty("expectedVersion") Long expectedVersion,
            @JsonProperty("payload") T payload,
            @JsonProperty("originatingPrincipal") ThemisPrincipal originatingPrincipal,
            @JsonProperty("originatingAuthorities") Set<ThemisAuthority> originatingAuthorities,
            @JsonProperty("securityDomain") String securityDomain,
            @JsonProperty("correlationId") String correlationId,
            @JsonProperty("causationId") String causationId,
            @JsonProperty("requestedAt") Instant requestedAt,
            @JsonProperty("authorizationDecision") ThemisAuthorizationDecision authorizationDecision,
            @JsonProperty("executingPrincipal") ThemisPrincipal executingPrincipal,
            @JsonProperty("createdAt") Instant createdAt,
            @JsonProperty("attributes") Map<String, String> attributes
    ) {
        Objects.requireNonNull(operationId, "operationId must not be null");
        if (operationId.isBlank()) {
            throw new IllegalArgumentException("operationId must not be blank");
        }
        Objects.requireNonNull(operationType, "operationType must not be null");
        Objects.requireNonNull(resourceType, "resourceType must not be null");
        if (resourceType.isBlank()) {
            throw new IllegalArgumentException("resourceType must not be blank");
        }
        Objects.requireNonNull(resourceId, "resourceId must not be null");
        if (resourceId.isBlank()) {
            throw new IllegalArgumentException("resourceId must not be blank");
        }
        Objects.requireNonNull(originatingPrincipal, "originatingPrincipal must not be null");
        Objects.requireNonNull(securityDomain, "securityDomain must not be null");
        if (securityDomain.isBlank()) {
            throw new IllegalArgumentException("securityDomain must not be blank");
        }
        Objects.requireNonNull(correlationId, "correlationId must not be null");
        if (correlationId.isBlank()) {
            throw new IllegalArgumentException("correlationId must not be blank");
        }
        Objects.requireNonNull(causationId, "causationId must not be null");
        if (causationId.isBlank()) {
            throw new IllegalArgumentException("causationId must not be blank");
        }
        Objects.requireNonNull(requestedAt, "requestedAt must not be null");
        Objects.requireNonNull(authorizationDecision, "authorizationDecision must not be null");
        if (authorizationDecision.isDenied()) {
            throw new IllegalArgumentException("authorizationDecision must not be denied for persistence envelope creation");
        }
        Objects.requireNonNull(executingPrincipal, "executingPrincipal must not be null");

        if (operationType == PersistenceOperationType.CREATE || operationType == PersistenceOperationType.UPDATE) {
            if (payload == null) {
                throw new IllegalArgumentException("payload must not be null for " + operationType);
            }
            if (payload instanceof String str && str.isBlank()) {
                throw new IllegalArgumentException("payload must not be blank for " + operationType);
            }
        } else if (operationType == PersistenceOperationType.DELETE) {
            if (payload != null) {
                throw new IllegalArgumentException("payload must be null for DELETE operations");
            }
        }

        this.operationId = operationId;
        this.operationType = operationType;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.expectedVersion = expectedVersion;
        this.payload = payload;
        this.originatingPrincipal = originatingPrincipal;
        this.originatingAuthorities = originatingAuthorities == null
                ? Set.of()
                : Collections.unmodifiableSet(new LinkedHashSet<>(originatingAuthorities));
        this.securityDomain = securityDomain;
        this.correlationId = correlationId;
        this.causationId = causationId;
        this.requestedAt = requestedAt;
        this.authorizationDecision = authorizationDecision;
        this.executingPrincipal = executingPrincipal;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.attributes = attributes == null
                ? Map.of()
                : Collections.unmodifiableMap(new LinkedHashMap<>(attributes));
    }

    public String getOperationId() {
        return operationId;
    }

    public String operationId() {
        return operationId;
    }

    public PersistenceOperationType getOperationType() {
        return operationType;
    }

    public PersistenceOperationType operationType() {
        return operationType;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String resourceType() {
        return resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String resourceId() {
        return resourceId;
    }

    public Long getExpectedVersion() {
        return expectedVersion;
    }

    public Long expectedVersion() {
        return expectedVersion;
    }

    public T getPayload() {
        return payload;
    }

    public T payload() {
        return payload;
    }

    public ThemisPrincipal getOriginatingPrincipal() {
        return originatingPrincipal;
    }

    public ThemisPrincipal originatingPrincipal() {
        return originatingPrincipal;
    }

    public Set<ThemisAuthority> getOriginatingAuthorities() {
        return originatingAuthorities;
    }

    public Set<ThemisAuthority> originatingAuthorities() {
        return originatingAuthorities;
    }

    public String getSecurityDomain() {
        return securityDomain;
    }

    public String securityDomain() {
        return securityDomain;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String correlationId() {
        return correlationId;
    }

    public String getCausationId() {
        return causationId;
    }

    public String causationId() {
        return causationId;
    }

    public Instant getRequestedAt() {
        return requestedAt;
    }

    public Instant requestedAt() {
        return requestedAt;
    }

    public ThemisAuthorizationDecision getAuthorizationDecision() {
        return authorizationDecision;
    }

    public ThemisAuthorizationDecision authorizationDecision() {
        return authorizationDecision;
    }

    public ThemisPrincipal getExecutingPrincipal() {
        return executingPrincipal;
    }

    public ThemisPrincipal executingPrincipal() {
        return executingPrincipal;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public Map<String, String> attributes() {
        return attributes;
    }

    /**
     * Derives an immutable copy with an updated executing principal.
     * <p>
     * All originating provenance (originatingPrincipal, originatingAuthorities, securityDomain,
     * correlationId, causationId, requestedAt, authorizationDecision) remains strictly intact.
     *
     * @param newExecutingPrincipal the new active executing principal
     * @return new immutable envelope with updated executor
     */
    public PersistenceOperationEnvelope<T> withExecutingPrincipal(ThemisPrincipal newExecutingPrincipal) {
        Objects.requireNonNull(newExecutingPrincipal, "newExecutingPrincipal must not be null");
        return new PersistenceOperationEnvelope<>(
                this.operationId,
                this.operationType,
                this.resourceType,
                this.resourceId,
                this.expectedVersion,
                this.payload,
                this.originatingPrincipal,
                this.originatingAuthorities,
                this.securityDomain,
                this.correlationId,
                this.causationId,
                this.requestedAt,
                this.authorizationDecision,
                newExecutingPrincipal,
                this.createdAt,
                this.attributes
        );
    }

    /**
     * Reconstructs a {@link ThemisSecurityContext} from this envelope for dynamic Themis authorization evaluation.
     *
     * @return reconstructed {@link ThemisSecurityContext}
     */
    public ThemisSecurityContext toSecurityContext() {
        return new ThemisSecurityContext(
                this.originatingPrincipal,
                this.executingPrincipal,
                this.securityDomain,
                this.originatingAuthorities,
                this.correlationId,
                this.causationId,
                this.attributes.get("tenantId"),
                this.attributes.get("clientIp"),
                this.requestedAt,
                this.attributes
        );
    }

    /**
     * Factory method creating a persistence envelope from an active {@link ThemisSecurityContext} and authorization decision.
     *
     * @param operationType         operation type (CREATE, UPDATE, DELETE)
     * @param resourceType          FHIR resource type
     * @param resourceId            resource logical identifier
     * @param expectedVersion       optional expected version
     * @param payload               payload (null for DELETE)
     * @param securityContext       active security context
     * @param authorizationDecision request-time authorization decision
     * @param <T>                   payload type
     * @return constructed envelope
     */
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

        return PersistenceOperationEnvelope.<T>builder()
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
                .requestedAt(securityContext.requestedAt())
                .authorizationDecision(authorizationDecision)
                .executingPrincipal(execPrincipal)
                .attributes(securityContext.attributes())
                .build();
    }

    /**
     * Factory method creating a persistence envelope from an active {@link ThemisSecurityContext} without versioning.
     */
    public static <T> PersistenceOperationEnvelope<T> fromSecurityContext(
            PersistenceOperationType operationType,
            String resourceType,
            String resourceId,
            T payload,
            ThemisSecurityContext securityContext,
            ThemisAuthorizationDecision authorizationDecision
    ) {
        return fromSecurityContext(operationType, resourceType, resourceId, null, payload, securityContext, authorizationDecision);
    }

    /**
     * Factory helper for {@code CREATE} operations.
     */
    public static <T> PersistenceOperationEnvelope<T> ofCreate(
            String resourceType,
            String resourceId,
            T payload,
            ThemisSecurityContext securityContext,
            ThemisAuthorizationDecision authorizationDecision
    ) {
        return fromSecurityContext(PersistenceOperationType.CREATE, resourceType, resourceId, null, payload, securityContext, authorizationDecision);
    }

    /**
     * Factory helper for {@code UPDATE} operations with optimistic locking version.
     */
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

    /**
     * Factory helper for {@code UPDATE} operations without optimistic locking version.
     */
    public static <T> PersistenceOperationEnvelope<T> ofUpdate(
            String resourceType,
            String resourceId,
            T payload,
            ThemisSecurityContext securityContext,
            ThemisAuthorizationDecision authorizationDecision
    ) {
        return ofUpdate(resourceType, resourceId, null, payload, securityContext, authorizationDecision);
    }

    /**
     * Factory helper for {@code DELETE} operations with optimistic locking version.
     */
    public static <T> PersistenceOperationEnvelope<T> ofDelete(
            String resourceType,
            String resourceId,
            Long expectedVersion,
            ThemisSecurityContext securityContext,
            ThemisAuthorizationDecision authorizationDecision
    ) {
        return fromSecurityContext(PersistenceOperationType.DELETE, resourceType, resourceId, expectedVersion, null, securityContext, authorizationDecision);
    }

    /**
     * Factory helper for {@code DELETE} operations without optimistic locking version.
     */
    public static <T> PersistenceOperationEnvelope<T> ofDelete(
            String resourceType,
            String resourceId,
            ThemisSecurityContext securityContext,
            ThemisAuthorizationDecision authorizationDecision
    ) {
        return ofDelete(resourceType, resourceId, null, securityContext, authorizationDecision);
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private String operationId;
        private PersistenceOperationType operationType;
        private String resourceType;
        private String resourceId;
        private Long expectedVersion;
        private T payload;
        private ThemisPrincipal originatingPrincipal;
        private Set<ThemisAuthority> originatingAuthorities = new LinkedHashSet<>();
        private String securityDomain;
        private String correlationId;
        private String causationId;
        private Instant requestedAt;
        private ThemisAuthorizationDecision authorizationDecision;
        private ThemisPrincipal executingPrincipal;
        private Instant createdAt;
        private Map<String, String> attributes = new LinkedHashMap<>();

        public Builder() {
        }

        public Builder<T> operationId(String operationId) {
            this.operationId = operationId;
            return this;
        }

        public Builder<T> operationType(PersistenceOperationType operationType) {
            this.operationType = operationType;
            return this;
        }

        public Builder<T> resourceType(String resourceType) {
            this.resourceType = resourceType;
            return this;
        }

        public Builder<T> resourceId(String resourceId) {
            this.resourceId = resourceId;
            return this;
        }

        public Builder<T> expectedVersion(Long expectedVersion) {
            this.expectedVersion = expectedVersion;
            return this;
        }

        public Builder<T> payload(T payload) {
            this.payload = payload;
            return this;
        }

        public Builder<T> originatingPrincipal(ThemisPrincipal originatingPrincipal) {
            this.originatingPrincipal = originatingPrincipal;
            return this;
        }

        public Builder<T> originatingAuthorities(Set<ThemisAuthority> originatingAuthorities) {
            if (originatingAuthorities != null) {
                this.originatingAuthorities = new LinkedHashSet<>(originatingAuthorities);
            }
            return this;
        }

        public Builder<T> addOriginatingAuthority(ThemisAuthority authority) {
            if (authority != null) {
                this.originatingAuthorities.add(authority);
            }
            return this;
        }

        public Builder<T> securityDomain(String securityDomain) {
            this.securityDomain = securityDomain;
            return this;
        }

        public Builder<T> correlationId(String correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public Builder<T> causationId(String causationId) {
            this.causationId = causationId;
            return this;
        }

        public Builder<T> requestedAt(Instant requestedAt) {
            this.requestedAt = requestedAt;
            return this;
        }

        public Builder<T> authorizationDecision(ThemisAuthorizationDecision authorizationDecision) {
            this.authorizationDecision = authorizationDecision;
            return this;
        }

        public Builder<T> executingPrincipal(ThemisPrincipal executingPrincipal) {
            this.executingPrincipal = executingPrincipal;
            return this;
        }

        public Builder<T> createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder<T> attributes(Map<String, String> attributes) {
            if (attributes != null) {
                this.attributes = new LinkedHashMap<>(attributes);
            }
            return this;
        }

        public Builder<T> attribute(String key, String value) {
            if (key != null && value != null) {
                this.attributes.put(key, value);
            }
            return this;
        }

        public PersistenceOperationEnvelope<T> build() {
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
                    createdAt,
                    attributes
            );
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersistenceOperationEnvelope<?> that = (PersistenceOperationEnvelope<?>) o;
        return Objects.equals(operationId, that.operationId) &&
                operationType == that.operationType &&
                Objects.equals(resourceType, that.resourceType) &&
                Objects.equals(resourceId, that.resourceId) &&
                Objects.equals(expectedVersion, that.expectedVersion) &&
                Objects.equals(payload, that.payload) &&
                Objects.equals(originatingPrincipal, that.originatingPrincipal) &&
                Objects.equals(originatingAuthorities, that.originatingAuthorities) &&
                Objects.equals(securityDomain, that.securityDomain) &&
                Objects.equals(correlationId, that.correlationId) &&
                Objects.equals(causationId, that.causationId) &&
                Objects.equals(requestedAt, that.requestedAt) &&
                Objects.equals(authorizationDecision, that.authorizationDecision) &&
                Objects.equals(executingPrincipal, that.executingPrincipal) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(attributes, that.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
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
                createdAt,
                attributes
        );
    }

    @Override
    public String toString() {
        return "PersistenceOperationEnvelope{" +
                "operationId='" + operationId + '\'' +
                ", operationType=" + operationType +
                ", resourceType='" + resourceType + '\'' +
                ", resourceId='" + resourceId + '\'' +
                ", expectedVersion=" + expectedVersion +
                ", payloadPresent=" + (payload != null) +
                ", originatingPrincipal=" + originatingPrincipal +
                ", securityDomain='" + securityDomain + '\'' +
                ", correlationId='" + correlationId + '\'' +
                ", causationId='" + causationId + '\'' +
                ", requestedAt=" + requestedAt +
                ", executingPrincipal=" + executingPrincipal +
                ", createdAt=" + createdAt +
                '}';
    }
}
