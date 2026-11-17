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

package net.fhirfactory.harmonia.model.core.information.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.harmonia.model.core.information.operations.factory.ManagementContextBuilder;

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
 *       It is <b>strictly distinct</b> from the durable authoritative version committed by Mnemosyne.</li>
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
     * Creates an immutable operational view with its distinct cache revision.
     *
     * @param operationalRevision Mneme operational revision, never an authoritative persistence version
     * @param activeGeneration active cache manifestation identifier
     * @param cachedAt working-cache timestamp
     * @param transientProperties transient operational properties
     */
    @JsonCreator
    public ManagementContext(
            @JsonProperty("operationalRevision") OperationalRevision operationalRevision,
            @JsonProperty("activeGeneration") String activeGeneration,
            @JsonProperty("cachedAt") Instant cachedAt,
            @JsonProperty("transientProperties") Map<String, String> transientProperties) {
        this.operationalRevision = operationalRevision;
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
    public static ManagementContextBuilder builder() {
        return new ManagementContextBuilder();
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

    @JsonIgnore
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
                "hasRevision=" + (operationalRevision != null) +
                ", hasGeneration=" + (activeGeneration != null) +
                ", hasCachedAt=" + (cachedAt != null) +
                ", transientPropertyCount=" + transientProperties.size() +
                '}';
    }
}
