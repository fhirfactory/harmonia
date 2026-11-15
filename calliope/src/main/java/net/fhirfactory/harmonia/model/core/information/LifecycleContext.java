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
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeCommitOutcome;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Encapsulates operational handshaking and Mneme–Mnemosyne lifecycle coordination metadata.
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with {@code DRP-Information-Unit-Framework.Final.001.md} (§§5.4, 17; IU-03):
 * <ul>
 *   <li>{@code LifecycleContext} provides information needed for Mneme–Mnemosyne lifecycle and persistence
 *       coordination, including distinguishing request, acceptance, commit, and completion stages.</li>
 *   <li>It is <b>not</b> itself a universal state machine, nor does it own the clinical/business semantic lifecycle
 *       of the underlying Content.</li>
 *   <li><b>Persistence Stage:</b> Coordinates operational persistence handshakes (e.g. {@code REQUESTED}, {@code ACCEPTED},
 *       {@code COMMITTED}, {@code COMPLETED}, {@code RETIRED}).</li>
 *   <li><b>Retirement Semantics:</b> Governed retirement (RETIRE) marks lifecycle status without physically deleting
 *       historical data or destroying externally referenced Content (ADR-020).</li>
 * </ul>
 * <p>
 * <b>Defensive Programming:</b>
 * <ul>
 *   <li>Coordination attributes map is defensively copied and unmodifiable.</li>
 *   <li>Optional fields are accessed via {@link Optional}.</li>
 *   <li>Immutable value object design.</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class LifecycleContext implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final LifecycleContext EMPTY = new LifecycleContext(null, null, null, false, Map.of());

    private final String lifecycleStage;
    private final Instant persistenceTimestamp;
    private final AuthoritativeCommitOutcome commitOutcome;
    private final boolean retained;
    private final Map<String, String> coordinationAttributes;

    /**
     * Canonical constructor establishing validated lifecycle coordination context.
     *
     * @param lifecycleStage         the current lifecycle/persistence stage (optional)
     * @param persistenceTimestamp   timestamp of persistence/handshake operation (optional)
     * @param commitOutcome          authoritative commit outcome (optional)
     * @param retained               whether a retention hold/policy applies
     * @param coordinationAttributes operational coordination attributes (optional)
     */
    @JsonCreator
    public LifecycleContext(
            @JsonProperty("lifecycleStage") String lifecycleStage,
            @JsonProperty("persistenceTimestamp") Instant persistenceTimestamp,
            @JsonProperty("commitOutcome") AuthoritativeCommitOutcome commitOutcome,
            @JsonProperty("retained") boolean retained,
            @JsonProperty("coordinationAttributes") Map<String, String> coordinationAttributes) {
        this.lifecycleStage = lifecycleStage != null && !lifecycleStage.isBlank() ? lifecycleStage.trim() : null;
        this.persistenceTimestamp = persistenceTimestamp;
        this.commitOutcome = commitOutcome;
        this.retained = retained;
        this.coordinationAttributes = coordinationAttributes == null || coordinationAttributes.isEmpty()
                ? Map.of()
                : Collections.unmodifiableMap(new LinkedHashMap<>(coordinationAttributes));
    }

    /**
     * Returns an empty, default {@code LifecycleContext}.
     *
     * @return empty LifecycleContext
     */
    public static LifecycleContext empty() {
        return EMPTY;
    }

    /**
     * Creates a new builder for constructing {@code LifecycleContext} instances.
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
     * Returns the operational lifecycle/persistence stage name.
     *
     * @return optional lifecycle stage name
     */
    @JsonIgnore
    public Optional<String> lifecycleStage() {
        return Optional.ofNullable(lifecycleStage);
    }

    /**
     * Returns the raw lifecycle stage, or null if not set.
     *
     * @return lifecycle stage, or null
     */
    @JsonProperty("lifecycleStage")
    public String getLifecycleStage() {
        return lifecycleStage;
    }

    /**
     * Returns the timestamp when the persistence operation or handshake occurred.
     *
     * @return optional persistence timestamp
     */
    @JsonIgnore
    public Optional<Instant> persistenceTimestamp() {
        return Optional.ofNullable(persistenceTimestamp);
    }

    /**
     * Returns the raw persistence timestamp, or null if not set.
     *
     * @return persistence timestamp, or null
     */
    @JsonProperty("persistenceTimestamp")
    public Instant getPersistenceTimestamp() {
        return persistenceTimestamp;
    }

    /**
     * Returns the authoritative commit outcome.
     *
     * @return optional authoritative commit outcome
     */
    @JsonIgnore
    public Optional<AuthoritativeCommitOutcome> commitOutcome() {
        return Optional.ofNullable(commitOutcome);
    }

    /**
     * Returns the raw commit outcome, or null if not set.
     *
     * @return commit outcome, or null
     */
    @JsonProperty("commitOutcome")
    public AuthoritativeCommitOutcome getCommitOutcome() {
        return commitOutcome;
    }

    /**
     * Returns whether this Information Unit is marked with an active retention policy or hold.
     *
     * @return true if retained
     */
    @JsonProperty("retained")
    public boolean isRetained() {
        return retained;
    }

    /**
     * Returns unmodifiable map of operational coordination attributes.
     *
     * @return unmodifiable map of coordination attributes
     */
    @JsonProperty("coordinationAttributes")
    public Map<String, String> coordinationAttributes() {
        return coordinationAttributes;
    }

    /**
     * Retrieves a single coordination attribute value by key.
     *
     * @param key the attribute key
     * @return optional attribute value
     */
    public Optional<String> getCoordinationAttribute(String key) {
        if (key == null) return Optional.empty();
        return Optional.ofNullable(coordinationAttributes.get(key));
    }

    // =========================================================================
    // Immutable Derivations ("With" Mutators)
    // =========================================================================

    public LifecycleContext withLifecycleStage(String stage) {
        return new LifecycleContext(stage, this.persistenceTimestamp, this.commitOutcome, this.retained, this.coordinationAttributes);
    }

    public LifecycleContext withPersistenceTimestamp(Instant timestamp) {
        return new LifecycleContext(this.lifecycleStage, timestamp, this.commitOutcome, this.retained, this.coordinationAttributes);
    }

    public LifecycleContext withCommitOutcome(AuthoritativeCommitOutcome outcome) {
        return new LifecycleContext(this.lifecycleStage, this.persistenceTimestamp, outcome, this.retained, this.coordinationAttributes);
    }

    public LifecycleContext withRetained(boolean retained) {
        return new LifecycleContext(this.lifecycleStage, this.persistenceTimestamp, this.commitOutcome, retained, this.coordinationAttributes);
    }

    public LifecycleContext withCoordinationAttributes(Map<String, String> attributes) {
        return new LifecycleContext(this.lifecycleStage, this.persistenceTimestamp, this.commitOutcome, this.retained, attributes);
    }

    public LifecycleContext withCoordinationAttribute(String key, String value) {
        Objects.requireNonNull(key, "attribute key must not be null");
        Map<String, String> updated = new LinkedHashMap<>(this.coordinationAttributes);
        if (value == null) {
            updated.remove(key);
        } else {
            updated.put(key, value);
        }
        return new LifecycleContext(this.lifecycleStage, this.persistenceTimestamp, this.commitOutcome, this.retained, updated);
    }

    // =========================================================================
    // Standard Object Methods
    // =========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LifecycleContext that = (LifecycleContext) o;
        return retained == that.retained
                && Objects.equals(lifecycleStage, that.lifecycleStage)
                && Objects.equals(persistenceTimestamp, that.persistenceTimestamp)
                && commitOutcome == that.commitOutcome
                && Objects.equals(coordinationAttributes, that.coordinationAttributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lifecycleStage, persistenceTimestamp, commitOutcome, retained, coordinationAttributes);
    }

    @Override
    public String toString() {
        return "LifecycleContext{" +
                "lifecycleStage='" + lifecycleStage + '\'' +
                ", persistenceTimestamp=" + persistenceTimestamp +
                ", commitOutcome=" + commitOutcome +
                ", retained=" + retained +
                ", coordinationAttributesCount=" + coordinationAttributes.size() +
                '}';
    }

    // =========================================================================
    // Builder
    // =========================================================================

    /**
     * Fluent builder for {@link LifecycleContext}.
     */
    public static final class Builder {
        private String lifecycleStage;
        private Instant persistenceTimestamp;
        private AuthoritativeCommitOutcome commitOutcome;
        private boolean retained;
        private final Map<String, String> coordinationAttributes = new LinkedHashMap<>();

        private Builder() {}

        public Builder lifecycleStage(String stage) {
            this.lifecycleStage = stage;
            return this;
        }

        public Builder persistenceTimestamp(Instant timestamp) {
            this.persistenceTimestamp = timestamp;
            return this;
        }

        public Builder commitOutcome(AuthoritativeCommitOutcome outcome) {
            this.commitOutcome = outcome;
            return this;
        }

        public Builder retained(boolean retained) {
            this.retained = retained;
            return this;
        }

        public Builder coordinationAttribute(String key, String value) {
            Objects.requireNonNull(key, "key must not be null");
            if (value != null) {
                this.coordinationAttributes.put(key, value);
            } else {
                this.coordinationAttributes.remove(key);
            }
            return this;
        }

        public Builder coordinationAttributes(Map<String, String> attributes) {
            this.coordinationAttributes.clear();
            if (attributes != null) {
                this.coordinationAttributes.putAll(attributes);
            }
            return this;
        }

        public LifecycleContext build() {
            return new LifecycleContext(lifecycleStage, persistenceTimestamp, commitOutcome, retained, coordinationAttributes);
        }
    }
}
