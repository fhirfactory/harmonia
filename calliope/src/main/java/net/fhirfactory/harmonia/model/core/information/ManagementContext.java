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
 * In accordance with {@code DRP-Information-Unit-Framework.Final.001.md} (§§5.1, 16; IU-03, IU-09):
 * <ul>
 *   <li>{@code ManagementContext} is one of the four distinct metadata contexts within an Information Unit.</li>
 *   <li>It is dedicated to operational and working-cache data that does <b>not</b> belong to the IU's identity,
 *       native Content, or durable Content metadata.</li>
 *   <li><b>Operational Version:</b> Used for cache conflict detection, optimistic concurrency, and stale-update
 *       handling within Mneme. Distinct from both Content Structure Version and payload-native instance version.</li>
 *   <li><b>Active Generation:</b> Identifies an individual active manifestation of an IU in a particular distributed
 *       Mneme cache environment. Multiple cache generations may express the same operational Version without creating
 *       a new IU identity or representation.</li>
 *   <li><b>Transient Properties:</b> Properties in {@code ManagementContext} may be expressly transient.
 *       Their presence here does not automatically create a durable persistence obligation.</li>
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

    private final AuthoritativeVersion operationalVersion;
    private final String activeGeneration;
    private final Instant cachedAt;
    private final Map<String, String> transientProperties;

    /**
     * Canonical constructor establishing validated management context attributes.
     *
     * @param operationalVersion  the Mneme operational version for concurrency/staleness control (optional)
     * @param activeGeneration   the active cache manifestation identifier (optional)
     * @param cachedAt           the working cache timestamp (optional)
     * @param transientProperties transient operational properties (optional, will be wrapped in unmodifiable map)
     */
    @JsonCreator
    public ManagementContext(
            @JsonProperty("operationalVersion") AuthoritativeVersion operationalVersion,
            @JsonProperty("activeGeneration") String activeGeneration,
            @JsonProperty("cachedAt") Instant cachedAt,
            @JsonProperty("transientProperties") Map<String, String> transientProperties) {
        this.operationalVersion = operationalVersion;
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
     * Returns the Mneme operational version used for cache conflict detection and optimistic concurrency.
     *
     * @return optional operational version
     */
    @JsonIgnore
    public Optional<AuthoritativeVersion> operationalVersion() {
        return Optional.ofNullable(operationalVersion);
    }

    /**
     * Returns the raw operational version value, or null if not set.
     *
     * @return the operational version, or null
     */
    @JsonProperty("operationalVersion")
    public AuthoritativeVersion getOperationalVersion() {
        return operationalVersion;
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

    /**
     * Returns the raw active generation identifier, or null if not set.
     *
     * @return the active generation, or null
     */
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

    /**
     * Returns the raw cached timestamp, or null if not set.
     *
     * @return the cached timestamp, or null
     */
    @JsonProperty("cachedAt")
    public Instant getCachedAt() {
        return cachedAt;
    }

    /**
     * Returns the unmodifiable map of transient operational working properties.
     *
     * @return unmodifiable map of transient properties
     */
    @JsonProperty("transientProperties")
    public Map<String, String> transientProperties() {
        return transientProperties;
    }

    /**
     * Retrieves a single transient property value by key.
     *
     * @param key the property key
     * @return optional property value
     */
    public Optional<String> getTransientProperty(String key) {
        if (key == null) return Optional.empty();
        return Optional.ofNullable(transientProperties.get(key));
    }

    // =========================================================================
    // Immutable Derivations ("With" Mutators)
    // =========================================================================

    /**
     * Derives an immutable copy with an updated operational version.
     *
     * @param version new operational version
     * @return new ManagementContext
     */
    public ManagementContext withOperationalVersion(AuthoritativeVersion version) {
        return new ManagementContext(version, this.activeGeneration, this.cachedAt, this.transientProperties);
    }

    /**
     * Derives an immutable copy with an updated active generation identifier.
     *
     * @param generation new active generation
     * @return new ManagementContext
     */
    public ManagementContext withActiveGeneration(String generation) {
        return new ManagementContext(this.operationalVersion, generation, this.cachedAt, this.transientProperties);
    }

    /**
     * Derives an immutable copy with an updated cached timestamp.
     *
     * @param timestamp new cache timestamp
     * @return new ManagementContext
     */
    public ManagementContext withCachedAt(Instant timestamp) {
        return new ManagementContext(this.operationalVersion, this.activeGeneration, timestamp, this.transientProperties);
    }

    /**
     * Derives an immutable copy with updated transient properties.
     *
     * @param properties new transient properties map
     * @return new ManagementContext
     */
    public ManagementContext withTransientProperties(Map<String, String> properties) {
        return new ManagementContext(this.operationalVersion, this.activeGeneration, this.cachedAt, properties);
    }

    /**
     * Derives an immutable copy with an added or updated transient property.
     *
     * @param key   property key, must not be null
     * @param value property value
     * @return new ManagementContext
     */
    public ManagementContext withTransientProperty(String key, String value) {
        Objects.requireNonNull(key, "property key must not be null");
        Map<String, String> updated = new LinkedHashMap<>(this.transientProperties);
        if (value == null) {
            updated.remove(key);
        } else {
            updated.put(key, value);
        }
        return new ManagementContext(this.operationalVersion, this.activeGeneration, this.cachedAt, updated);
    }

    // =========================================================================
    // Standard Object Methods
    // =========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManagementContext that = (ManagementContext) o;
        return Objects.equals(operationalVersion, that.operationalVersion)
                && Objects.equals(activeGeneration, that.activeGeneration)
                && Objects.equals(cachedAt, that.cachedAt)
                && Objects.equals(transientProperties, that.transientProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationalVersion, activeGeneration, cachedAt, transientProperties);
    }

    @Override
    public String toString() {
        return "ManagementContext{" +
                "operationalVersion=" + operationalVersion +
                ", activeGeneration='" + activeGeneration + '\'' +
                ", cachedAt=" + cachedAt +
                ", transientPropertiesCount=" + transientProperties.size() +
                '}';
    }

    // =========================================================================
    // Builder
    // =========================================================================

    /**
     * Fluent builder for {@link ManagementContext}.
     */
    public static final class Builder {
        private AuthoritativeVersion operationalVersion;
        private String activeGeneration;
        private Instant cachedAt;
        private final Map<String, String> transientProperties = new LinkedHashMap<>();

        private Builder() {}

        public Builder operationalVersion(AuthoritativeVersion operationalVersion) {
            this.operationalVersion = operationalVersion;
            return this;
        }

        public Builder operationalVersion(String version) {
            this.operationalVersion = version != null && !version.isBlank() ? AuthoritativeVersion.of(version) : null;
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

        public Builder transientProperties(Map<String, String> properties) {
            this.transientProperties.clear();
            if (properties != null) {
                this.transientProperties.putAll(properties);
            }
            return this;
        }

        public Builder transientProperty(String key, String value) {
            Objects.requireNonNull(key, "key must not be null");
            if (value != null) {
                this.transientProperties.put(key, value);
            } else {
                this.transientProperties.remove(key);
            }
            return this;
        }

        public ManagementContext build() {
            return new ManagementContext(operationalVersion, activeGeneration, cachedAt, transientProperties);
        }
    }
}
