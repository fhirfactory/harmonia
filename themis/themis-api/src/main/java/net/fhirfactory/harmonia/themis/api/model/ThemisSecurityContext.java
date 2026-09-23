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

package net.fhirfactory.harmonia.themis.api.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Encapsulates contextual, identity, authority, and environmental attributes for authorization requests and distributed execution.
 * <p>
 * Supports distinct tracking of the initiating/originating principal (e.g. {@link PrincipalType#HUMAN})
 * and the active executing/delegated principal (e.g. {@link PrincipalType#PROCESS} or {@link PrincipalType#SERVICE}).
 */
public record ThemisSecurityContext(
        ThemisPrincipal requestingPrincipal,
        ThemisPrincipal executingPrincipal,
        String securityDomain,
        Set<ThemisAuthority> authorities,
        String correlationId,
        String causationId,
        String tenantId,
        String clientIp,
        Instant requestedAt,
        Map<String, String> attributes
) implements Serializable {

    public ThemisSecurityContext {
        requestedAt = requestedAt == null ? Instant.now() : requestedAt;
        authorities = authorities == null ? Set.of() : Collections.unmodifiableSet(Set.copyOf(authorities));
        attributes = attributes == null ? Map.of() : Collections.unmodifiableMap(Map.copyOf(attributes));
    }

    /**
     * Backward-compatible 7-parameter constructor.
     */
    public ThemisSecurityContext(
            ThemisPrincipal requestingPrincipal,
            String correlationId,
            String causationId,
            String tenantId,
            String clientIp,
            Instant requestedAt,
            Map<String, String> attributes
    ) {
        this(requestingPrincipal, null, null, Set.of(), correlationId, causationId, tenantId, clientIp, requestedAt, attributes);
    }

    /**
     * Canonical alias for {@link #requestingPrincipal()} providing initiating/originating principal semantics.
     *
     * @return the initiating/originating principal
     */
    public ThemisPrincipal originatingPrincipal() {
        return requestingPrincipal;
    }

    /**
     * Derives an immutable copy with an updated executing/delegated principal.
     *
     * @param newExecutingPrincipal active executing/worker principal
     * @return new {@link ThemisSecurityContext} with updated executing principal
     */
    public ThemisSecurityContext withExecutingPrincipal(ThemisPrincipal newExecutingPrincipal) {
        return new ThemisSecurityContext(
                this.requestingPrincipal,
                newExecutingPrincipal,
                this.securityDomain,
                this.authorities,
                this.correlationId,
                this.causationId,
                this.tenantId,
                this.clientIp,
                this.requestedAt,
                this.attributes
        );
    }

    /**
     * Derives an immutable copy with an updated causation ID.
     *
     * @param newCausationId causation/parent message ID
     * @return new {@link ThemisSecurityContext} with updated causation ID
     */
    public ThemisSecurityContext withCausationId(String newCausationId) {
        return new ThemisSecurityContext(
                this.requestingPrincipal,
                this.executingPrincipal,
                this.securityDomain,
                this.authorities,
                this.correlationId,
                newCausationId,
                this.tenantId,
                this.clientIp,
                this.requestedAt,
                this.attributes
        );
    }

    @Override
    public String toString() {
        return "ThemisSecurityContext[" +
                "originatingPrincipal=" + requestingPrincipal +
                ", executingPrincipal=" + executingPrincipal +
                ", securityDomain=" + securityDomain +
                ", correlationId=" + correlationId +
                ", causationId=" + causationId +
                ", tenantId=" + tenantId +
                ", clientIp=" + clientIp +
                ", requestedAt=" + requestedAt +
                ", authoritiesCount=" + (authorities != null ? authorities.size() : 0) +
                ", attributeCount=" + (attributes != null ? attributes.size() : 0) +
                "]";
    }

    public Builder toBuilder() {
        return builder().from(this);
    }

    public static ThemisSecurityContext anonymous() {
        return builder().build();
    }

    public static ThemisSecurityContext fromPrincipal(ThemisPrincipal principal, String correlationId) {
        return builder()
                .requestingPrincipal(principal)
                .securityDomain(principal != null ? principal.sourceDomain() : null)
                .correlationId(correlationId)
                .build();
    }

    public static ThemisSecurityContext of(ThemisPrincipal principal, String correlationId, String causationId, Map<String, String> attributes) {
        return builder().requestingPrincipal(principal).correlationId(correlationId).causationId(causationId).attributes(attributes).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ThemisPrincipal requestingPrincipal;
        private ThemisPrincipal executingPrincipal;
        private String securityDomain;
        private Set<ThemisAuthority> authorities = Set.of();
        private String correlationId;
        private String causationId;
        private String tenantId;
        private String clientIp;
        private Instant requestedAt;
        private Map<String, String> attributes = Map.of();

        public Builder from(ThemisSecurityContext context) {
            if (context != null) {
                this.requestingPrincipal = context.requestingPrincipal();
                this.executingPrincipal = context.executingPrincipal();
                this.securityDomain = context.securityDomain();
                this.authorities = context.authorities();
                this.correlationId = context.correlationId();
                this.causationId = context.causationId();
                this.tenantId = context.tenantId();
                this.clientIp = context.clientIp();
                this.requestedAt = context.requestedAt();
                this.attributes = context.attributes();
            }
            return this;
        }

        public Builder principal(ThemisPrincipal principal) {
            this.requestingPrincipal = principal;
            return this;
        }

        public Builder requestingPrincipal(ThemisPrincipal principal) {
            this.requestingPrincipal = principal;
            return this;
        }

        public Builder originatingPrincipal(ThemisPrincipal principal) {
            this.requestingPrincipal = principal;
            return this;
        }

        public Builder executingPrincipal(ThemisPrincipal executingPrincipal) {
            this.executingPrincipal = executingPrincipal;
            return this;
        }

        public Builder securityDomain(String securityDomain) {
            this.securityDomain = securityDomain;
            return this;
        }

        public Builder authorities(Set<ThemisAuthority> authorities) {
            this.authorities = authorities != null ? Set.copyOf(authorities) : Set.of();
            return this;
        }

        public Builder authorities(Collection<ThemisAuthority> authorities) {
            this.authorities = authorities != null ? Set.copyOf(authorities) : Set.of();
            return this;
        }

        public Builder addAuthority(ThemisAuthority authority) {
            if (authority != null) {
                Set<ThemisAuthority> updated = new HashSet<>(this.authorities);
                updated.add(authority);
                this.authorities = Collections.unmodifiableSet(updated);
            }
            return this;
        }

        public Builder addAuthority(String authorityCode) {
            if (authorityCode != null && !authorityCode.isBlank()) {
                Set<ThemisAuthority> updated = new HashSet<>(this.authorities);
                updated.add(ThemisAuthority.of(authorityCode));
                this.authorities = Collections.unmodifiableSet(updated);
            }
            return this;
        }

        public Builder correlationId(String correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public Builder causationId(String causationId) {
            this.causationId = causationId;
            return this;
        }

        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder clientIp(String clientIp) {
            this.clientIp = clientIp;
            return this;
        }

        public Builder requestedAt(Instant requestedAt) {
            this.requestedAt = requestedAt;
            return this;
        }

        public Builder attributes(Map<String, String> attributes) {
            this.attributes = attributes != null ? Map.copyOf(attributes) : Map.of();
            return this;
        }

        public Builder addAttribute(String key, String value) {
            if (key != null && value != null) {
                Map<String, String> updated = new LinkedHashMap<>(this.attributes);
                updated.put(key, value);
                this.attributes = Collections.unmodifiableMap(updated);
            }
            return this;
        }

        public ThemisSecurityContext build() {
            return new ThemisSecurityContext(
                    requestingPrincipal,
                    executingPrincipal,
                    securityDomain,
                    authorities,
                    correlationId,
                    causationId,
                    tenantId,
                    clientIp,
                    requestedAt,
                    attributes
            );
        }
    }
}
