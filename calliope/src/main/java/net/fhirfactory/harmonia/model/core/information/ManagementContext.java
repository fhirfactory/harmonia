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

package net.fhirfactory.harmonia.model.core.information;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Encapsulates operational working information maintained principally by Mneme and exposed to appropriate clients.
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with {@code DRP-Information-Unit-Framework.Final.001.md} (§§5.1, 16; IU-03, IU-09) and
 * {@code DRP-OperationalMetadata-and-OperationalMetadataMask.Final.001.md} (§§0, 2):
 * <ul>
 *   <li>{@code ManagementContext} is dedicated to operational and working-cache data that does <b>not</b>
 *       belong to the IU's durable Information Metadata, native Content, or durable Content metadata.</li>
 *   <li><b>Operational Revision:</b> Typed as {@link OperationalRevision}, used for cache conflict detection,
 *       optimistic concurrency, and stale-update handling within Mneme.
 *       It is <b>strictly distinct</b> from durable {@link AuthoritativeVersion} committed by Mnemosyne.</li>
 *   <li><b>Active Generation:</b> Identifies an individual active manifestation of an IU in a particular distributed
 *       Mneme cache environment. Multiple cache generations may express the same operational revision without creating
 *       a new IU identity or representation.</li>
 * </ul>
 * <p>
 * <b>Defensive Programming:</b>
 * <ul>
 *   <li>All properties collections are defensively copied and unmodifiable.</li>
 *   <li>Null inputs for optional values are safely represented as {@link Optional} accessors.</li>
 *   <li>Immutable value object design ensures thread-safe sharing across distributed components.</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ManagementContext implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final ManagementContext EMPTY = new ManagementContext(null, null, null, Map.of());

    private final OperationalRevision operationalRevision;
    private final String activeGeneration;
    private final Instant cachedAt;
    private final Map<String, String> transientProperties;

    /**
     * Canonical constructor establishing validated management context attributes with {@link OperationalRevision}
     * or backward-compatible {@link AuthoritativeVersion}.
     *
     * @param operationalRevisionOrVersion the Mneme operational revision (optional)
     * @param activeGeneration             the active cache manifestation identifier (optional)
     * @param cachedAt                     the working cache timestamp (optional)
     * @param transientProperties          transient operational properties (optional)
     */
    public ManagementContext(
            Object operationalRevisionOrVersion,
            String activeGeneration,
            Instant cachedAt,
            Map<String, String> transientProperties) {
        if (operationalRevisionOrVersion instanceof OperationalRevision or) {
            this.operationalRevision = or;
        } else if (operationalRevisionOrVersion instanceof AuthoritativeVersion av) {
            this.operationalRevision = OperationalRevision.of(av.value());
        } else if (operationalRevisionOrVersion instanceof String s && !s.isBlank()) {
            this.operationalRevision = OperationalRevision.of(s.trim());
        } else {
            this.operationalRevision = null;
        }
        this.activeGeneration = activeGeneration != null && !activeGeneration.isBlank() ? activeGeneration.trim() : null;
        this.cachedAt = cachedAt;
        if (transientProperties == null || transientProperties.isEmpty()) {
            this.transientProperties = Map.of();
        } else {
            this.transientProperties = Collections.unmodifiableMap(new LinkedHashMap<>(transientProperties));
        }
    }

    /**
     * Flexible creator handling both {@link OperationalRevision} and legacy version parameters.
     */
    @JsonCreator
    public ManagementContext(
            @JsonProperty("operationalRevision") OperationalRevision operationalRevision,
            @JsonProperty("operationalVersion") Object operationalVersionRaw,
            @JsonProperty("activeGeneration") String activeGeneration,
            @JsonProperty("cachedAt") Instant cachedAt,
            @JsonProperty("transientProperties") Map<String, String> transientProperties) {
        if (operationalRevision != null) {
            this.operationalRevision = operationalRevision;
        } else if (operationalVersionRaw != null) {
            String val = operationalVersionRaw instanceof AuthoritativeVersion av ? av.value() : operationalVersionRaw.toString();
            this.operationalRevision = val != null && !val.isBlank() ? OperationalRevision.of(val.trim()) : null;
        } else {
            this.operationalRevision = null;
        }
        this.activeGeneration = activeGeneration != null && !activeGeneration.isBlank() ? activeGeneration.trim() : null;
        this.cachedAt = cachedAt;
        if (transientProperties == null || transientProperties.isEmpty()) {
            this.transientProperties = Map.of();
        } else {
            this.transientProperties = Collections.unmodifiableMap(new LinkedHashMap<>(transientProperties));
        }
    }

    /**
     * Returns an empty, default {@code ManagementContext}.
     *
     * @return an empty ManagementContext
     */
    public static ManagementContext empty() {
        return EMPTY;
    }

    /**
     * Creates a new builder for constructing {@code ManagementContext} instances.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    // =========================================================================
    // Accessors
    // =========================================================================

    /**
     * Returns the Mneme operational revision used for cache conflict detection and optimistic concurrency.
     *
     * @return optional operational revision
     */
    @JsonIgnore
    public Optional<OperationalRevision> operationalRevision() {
        return Optional.ofNullable(operationalRevision);
    }

    @JsonProperty("operationalRevision")
    public OperationalRevision getOperationalRevision() {
        return operationalRevision;
    }

    /**
     * Backward-compatible accessor returning the revision as {@link AuthoritativeVersion}.
     *
     * @deprecated Use {@link #operationalRevision()} instead to preserve architectural separation.
     */
    @Deprecated
    @JsonIgnore
    public Optional<AuthoritativeVersion> operationalVersion() {
        return operationalRevision != null ? Optional.of(AuthoritativeVersion.of(operationalRevision.value())) : Optional.empty();
    }

    @Deprecated
    @JsonProperty("operationalVersion")
    public AuthoritativeVersion getOperationalVersion() {
        return operationalRevision != null ? AuthoritativeVersion.of(operationalRevision.value()) : null;
    }

    /**
     * Returns the active generation identifier for this cache manifestation.
     *
     * @return optional active generation identifier
     */
    @JsonIgnore
    public Optional<String> activeGeneration() {
        return Optional.ofNullable(activeGeneration);
    }

    @JsonProperty("activeGeneration")
    public String getActiveGeneration() {
        return activeGeneration;
    }

    /**
     * Returns the timestamp when this Information Unit was cached in working memory.
     *
     * @return optional cached timestamp
     */
    @JsonIgnore
    public Optional<Instant> cachedAt() {
        return Optional.ofNullable(cachedAt);
    }

    @JsonProperty("cachedAt")
    public Instant getCachedAt() {
        return cachedAt;
    }

    @JsonProperty("transientProperties")
    public Map<String, String> transientProperties() {
        return transientProperties;
    }

    public Map<String, String> getTransientProperties() {
        return transientProperties;
    }

    public Optional<String> getTransientProperty(String key) {
        if (key == null) return Optional.empty();
        return Optional.ofNullable(transientProperties.get(key));
    }

    public boolean isEmpty() {
        return operationalRevision == null
                && activeGeneration == null
                && cachedAt == null
                && transientProperties.isEmpty();
    }

    // =========================================================================
    // Immutable Derivations ("With" Mutators)
    // =========================================================================

    public ManagementContext withOperationalRevision(OperationalRevision operationalRevision) {
        return new ManagementContext(operationalRevision, this.activeGeneration, this.cachedAt, this.transientProperties);
    }

    public ManagementContext withOperationalRevision(String operationalRevision) {
        OperationalRevision rev = operationalRevision != null && !operationalRevision.isBlank() ? OperationalRevision.of(operationalRevision) : null;
        return withOperationalRevision(rev);
    }

    @Deprecated
    public ManagementContext withOperationalVersion(String operationalVersion) {
        return withOperationalRevision(operationalVersion);
    }

    @Deprecated
    public ManagementContext withOperationalVersion(AuthoritativeVersion operationalVersion) {
        OperationalRevision rev = operationalVersion != null ? OperationalRevision.of(operationalVersion.value()) : null;
        return withOperationalRevision(rev);
    }

    public ManagementContext withActiveGeneration(String activeGeneration) {
        return new ManagementContext(this.operationalRevision, activeGeneration, this.cachedAt, this.transientProperties);
    }

    public ManagementContext withCachedAt(Instant cachedAt) {
        return new ManagementContext(this.operationalRevision, this.activeGeneration, cachedAt, this.transientProperties);
    }

    public ManagementContext withTransientProperties(Map<String, String> transientProperties) {
        return new ManagementContext(this.operationalRevision, this.activeGeneration, this.cachedAt, transientProperties);
    }

    public ManagementContext withTransientProperty(String key, String value) {
        Objects.requireNonNull(key, "transientProperty key must not be null");
        Map<String, String> updated = new LinkedHashMap<>(this.transientProperties);
        if (value == null) {
            updated.remove(key);
        } else {
            updated.put(key, value);
        }
        return new ManagementContext(this.operationalRevision, this.activeGeneration, this.cachedAt, updated);
    }

    // =========================================================================
    // Standard Object Methods
    // =========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManagementContext that = (ManagementContext) o;
        return Objects.equals(operationalRevision, that.operationalRevision) &&
                Objects.equals(activeGeneration, that.activeGeneration) &&
                Objects.equals(cachedAt, that.cachedAt) &&
                Objects.equals(transientProperties, that.transientProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationalRevision, activeGeneration, cachedAt, transientProperties);
    }

    @Override
    public String toString() {
        return "ManagementContext{" +
                "revision=" + operationalRevision +
                ", generation='" + activeGeneration + '\'' +
                ", cachedAt=" + cachedAt +
                ", transientProperties=" + transientProperties.size() +
                '}';
    }

    // =========================================================================
    // Builder
    // =========================================================================

    public static final class Builder {
        private OperationalRevision operationalRevision;
        private String activeGeneration;
        private Instant cachedAt;
        private final Map<String, String> transientProperties = new LinkedHashMap<>();

        private Builder() {}

        public Builder operationalRevision(OperationalRevision operationalRevision) {
            this.operationalRevision = operationalRevision;
            return this;
        }

        public Builder operationalRevision(String operationalRevision) {
            this.operationalRevision = operationalRevision != null && !operationalRevision.isBlank()
                    ? OperationalRevision.of(operationalRevision)
                    : null;
            return this;
        }

        @Deprecated
        public Builder operationalVersion(String operationalVersion) {
            return operationalRevision(operationalVersion);
        }

        @Deprecated
        public Builder operationalVersion(AuthoritativeVersion operationalVersion) {
            this.operationalRevision = operationalVersion != null ? OperationalRevision.of(operationalVersion.value()) : null;
            return this;
        }

        public Builder activeGeneration(String activeGeneration) {
            this.activeGeneration = activeGeneration;
            return this;
        }

        public Builder cachedAt(Instant cachedAt) {
            this.cachedAt = cachedAt;
            return this;
        }

        public Builder transientProperties(Map<String, String> transientProperties) {
            this.transientProperties.clear();
            if (transientProperties != null) {
                this.transientProperties.putAll(transientProperties);
            }
            return this;
        }

        public Builder transientProperty(String key, String value) {
            if (key != null) {
                if (value == null) {
                    this.transientProperties.remove(key);
                } else {
                    this.transientProperties.put(key, value);
                }
            }
            return this;
        }

        public ManagementContext build() {
            return new ManagementContext(operationalRevision, activeGeneration, cachedAt, transientProperties);
        }
    }
}
