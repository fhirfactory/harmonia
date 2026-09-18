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
import java.util.Collections;
import java.util.Map;

/**
 * Encapsulates contextual and environmental attributes for authorization requests.
 */
public record ThemisSecurityContext(
        ThemisPrincipal requestingPrincipal,
        String correlationId,
        String causationId,
        String tenantId,
        String clientIp,
        Instant requestedAt,
        Map<String, String> attributes
) implements Serializable {

    public ThemisSecurityContext {
        requestedAt = requestedAt == null ? Instant.now() : requestedAt;
        attributes = attributes == null ? Map.of() : Collections.unmodifiableMap(Map.copyOf(attributes));
    }

    public static ThemisSecurityContext anonymous() {
        return new ThemisSecurityContext(null, null, null, null, null, Instant.now(), Map.of());
    }

    public static ThemisSecurityContext fromPrincipal(ThemisPrincipal principal, String correlationId) {
        return new ThemisSecurityContext(principal, correlationId, null, null, null, Instant.now(), Map.of());
    }

    public static ThemisSecurityContext of(ThemisPrincipal principal, String correlationId, String causationId, Map<String, String> attributes) {
        return new ThemisSecurityContext(principal, correlationId, causationId, null, null, Instant.now(), attributes);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ThemisPrincipal principal;
        private String correlationId;
        private String causationId;
        private String tenantId;
        private String clientIp;
        private Instant requestedAt;
        private Map<String, String> attributes = Map.of();

        public Builder principal(ThemisPrincipal principal) {
            this.principal = principal;
            return this;
        }

        public Builder requestingPrincipal(ThemisPrincipal principal) {
            this.principal = principal;
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
            this.attributes = attributes != null ? attributes : Map.of();
            return this;
        }

        public ThemisSecurityContext build() {
            return new ThemisSecurityContext(principal, correlationId, causationId, tenantId, clientIp, requestedAt, attributes);
        }
    }
}
