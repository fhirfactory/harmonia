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
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Representation of a target resource, entity, or domain object evaluated during authorization.
 */
public record ThemisResource(
        String resourceType,
        String resourceId,
        String securityDomain,
        Set<ThemisSecurityLabel> labels,
        Map<String, String> attributes
) implements Serializable {

    public ThemisResource {
        Objects.requireNonNull(resourceType, "resourceType must not be null");
        resourceType = resourceType.trim();
        labels = labels == null ? Set.of() : Collections.unmodifiableSet(Set.copyOf(labels));
        attributes = attributes == null ? Map.of() : Collections.unmodifiableMap(Map.copyOf(attributes));
    }

    public static ThemisResource of(String resourceType, String resourceId) {
        return new ThemisResource(resourceType, resourceId, null, Set.of(), Map.of());
    }

    public static ThemisResource of(String resourceType, String resourceId, String securityDomain, Set<ThemisSecurityLabel> labels) {
        return new ThemisResource(resourceType, resourceId, securityDomain, labels, Map.of());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String resourceType;
        private String resourceId;
        private String securityDomain;
        private Set<ThemisSecurityLabel> labels = Set.of();
        private Map<String, String> attributes = Map.of();

        public Builder resourceType(String resourceType) {
            this.resourceType = resourceType;
            return this;
        }

        public Builder resourceId(String resourceId) {
            this.resourceId = resourceId;
            return this;
        }

        public Builder securityDomain(String securityDomain) {
            this.securityDomain = securityDomain;
            return this;
        }

        public Builder securityLabels(Set<ThemisSecurityLabel> labels) {
            this.labels = labels != null ? labels : Set.of();
            return this;
        }

        public Builder attributes(Map<String, String> attributes) {
            this.attributes = attributes != null ? attributes : Map.of();
            return this;
        }

        public ThemisResource build() {
            return new ThemisResource(resourceType, resourceId, securityDomain, labels, attributes);
        }
    }

    public Set<ThemisSecurityLabel> securityLabels() {
        return labels;
    }

    public boolean hasSecurityLabel(String labelCode) {
        if (labelCode == null) {
            return false;
        }
        return labels.stream().anyMatch(l -> l.matchesCode(labelCode));
    }
}
