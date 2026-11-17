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

package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.harmonia.model.core.information.operations.OperationalMetadata;
import net.fhirfactory.harmonia.model.core.information.access.AuthoritativeCommitOutcome;
import net.fhirfactory.harmonia.model.core.information.entity.factory.LifecycleContextBuilder;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationUnitValueSets;
import net.fhirfactory.harmonia.model.core.naming.Topic;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Encapsulates governed information lifecycle state (disposition, retention, archive) and optional
 * operational persistence coordination facts.
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with {@code DRP-Information-Unit-Framework.Final.001.md} (§§5.4, 17; IU-03) and
 * {@code DRP-Information-Unit-Framework.Final.002.md} (§§3, 4.3):
 * <ul>
 *   <li><b>Governed Information Lifecycle vs Middleware Handshake:</b> An IU has information-specific preservation,
 *       retention, legal hold, archive, supersession or RETIRE semantics. Those are <b>governed information concerns</b>
 *       and remain associated with the IU (OM-R09 / OM-10).</li>
 *   <li><b>Topic-Valued Classification:</b> Governed disposition status and retention policies are represented as
 *       strongly typed {@link Topic} references (e.g. {@code Harmonia.Lifecycle.Active}, {@code Harmonia.Lifecycle.Retired},
 *       {@code Harmonia.Lifecycle.Archived}, {@code Harmonia.Lifecycle.LegalHold}).</li>
 *   <li><b>Operational Handshake Migration:</b> A persistence request's acceptance, durability confirmation, or commit
 *       outcome describes a middleware operation and belongs conceptually to {@link OperationalMetadata} (OM-R07 / OM-08).
 *       Bridges are provided here for compatibility.</li>
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


    private static final LifecycleContext EMPTY = new LifecycleContext(null, null, null, null, null, false, false, Map.of());

    private final Topic dispositionStatus;
    private final Topic retentionPolicy;
    private final String lifecycleStage;
    private final Instant persistenceTimestamp;
    private final AuthoritativeCommitOutcome commitOutcome;
    private final boolean retained;
    private final boolean archived;
    private final Map<String, String> coordinationAttributes;

    /**
     * Canonical constructor establishing validated lifecycle context.
     *
     * @param dispositionStatus      governed disposition status Topic (optional)
     * @param retentionPolicy        governed retention policy Topic (optional)
     * @param lifecycleStage         the current lifecycle/persistence stage (optional)
     * @param persistenceTimestamp   timestamp of persistence/handshake operation (optional)
     * @param commitOutcome          authoritative commit outcome (optional)
     * @param retained               whether a retention hold/policy applies
     * @param archived               whether this information unit is archived
     * @param coordinationAttributes operational coordination attributes (optional)
     */
    @JsonCreator
    public LifecycleContext(
            @JsonProperty("dispositionStatus") Topic dispositionStatus,
            @JsonProperty("retentionPolicy") Topic retentionPolicy,
            @JsonProperty("lifecycleStage") String lifecycleStage,
            @JsonProperty("persistenceTimestamp") Instant persistenceTimestamp,
            @JsonProperty("commitOutcome") AuthoritativeCommitOutcome commitOutcome,
            @JsonProperty("retained") boolean retained,
            @JsonProperty("archived") boolean archived,
            @JsonProperty("coordinationAttributes") Map<String, String> coordinationAttributes) {
        this.dispositionStatus = dispositionStatus;
        this.retentionPolicy = retentionPolicy;
        this.lifecycleStage = lifecycleStage != null && !lifecycleStage.isBlank() ? lifecycleStage.trim() : null;
        this.persistenceTimestamp = persistenceTimestamp;
        this.commitOutcome = commitOutcome;
        this.retained = retained;
        this.archived = archived;
        this.coordinationAttributes = coordinationAttributes == null || coordinationAttributes.isEmpty()
                ? Map.of()
                : Collections.unmodifiableMap(new LinkedHashMap<>(coordinationAttributes));
    }

    public LifecycleContext(
            String lifecycleStage,
            Instant persistenceTimestamp,
            AuthoritativeCommitOutcome commitOutcome,
            boolean retained,
            boolean archived,
            Map<String, String> coordinationAttributes) {
        this(null, null, lifecycleStage, persistenceTimestamp, commitOutcome, retained, archived, coordinationAttributes);
    }

    public LifecycleContext(
            String lifecycleStage,
            Instant persistenceTimestamp,
            AuthoritativeCommitOutcome commitOutcome,
            boolean retained,
            Map<String, String> coordinationAttributes) {
        this(null, null, lifecycleStage, persistenceTimestamp, commitOutcome, retained, false, coordinationAttributes);
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
    public static LifecycleContextBuilder builder() {
        return new LifecycleContextBuilder();
    }

    // =========================================================================
    // Accessors
    // =========================================================================

    /**
     * Returns the governed disposition status Topic.
     *
     * @return optional disposition status Topic
     */
    @JsonIgnore
    public Optional<Topic> dispositionStatus() {
        return Optional.ofNullable(dispositionStatus);
    }

    @JsonProperty("dispositionStatus")
    public Topic getDispositionStatus() {
        return dispositionStatus;
    }

    /**
     * Returns the governed retention policy Topic.
     *
     * @return optional retention policy Topic
     */
    @JsonIgnore
    public Optional<Topic> retentionPolicy() {
        return Optional.ofNullable(retentionPolicy);
    }

    @JsonProperty("retentionPolicy")
    public Topic getRetentionPolicy() {
        return retentionPolicy;
    }

    /**
     * Returns the operational lifecycle/persistence stage name.
     *
     * @return optional lifecycle stage name
     */
    @JsonIgnore
    public Optional<String> lifecycleStage() {
        return Optional.ofNullable(lifecycleStage);
    }

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

    @JsonProperty("persistenceTimestamp")
    public Instant getPersistenceTimestamp() {
        return persistenceTimestamp;
    }

    /**
     * Returns the authoritative commit outcome, if persistence has been attempted.
     *
     * @return optional commit outcome
     */
    @JsonIgnore
    public Optional<AuthoritativeCommitOutcome> commitOutcome() {
        return Optional.ofNullable(commitOutcome);
    }

    @JsonProperty("commitOutcome")
    public AuthoritativeCommitOutcome getCommitOutcome() {
        return commitOutcome;
    }

    @JsonProperty("retained")
    public boolean isRetained() {
        return retained;
    }

    @JsonProperty("archived")
    public boolean isArchived() {
        return archived;
    }

    @JsonProperty("coordinationAttributes")
    public Map<String, String> coordinationAttributes() {
        return coordinationAttributes;
    }

    public Map<String, String> getCoordinationAttributes() {
        return coordinationAttributes;
    }

    public Optional<String> getCoordinationAttribute(String key) {
        if (key == null) return Optional.empty();
        return Optional.ofNullable(coordinationAttributes.get(key));
    }

    public Optional<String> coordinationAttribute(String key) {
        return getCoordinationAttribute(key);
    }

    @JsonIgnore
    public boolean isCommitted() {
        return commitOutcome == AuthoritativeCommitOutcome.COMMITTED
                || (lifecycleStage != null && lifecycleStage.equalsIgnoreCase("COMMITTED"));
    }

    @JsonIgnore
    public boolean isRetired() {
        return (dispositionStatus != null && dispositionStatus.equals(InformationUnitValueSets.DISPOSITION_RETIRED))
                || (lifecycleStage != null && lifecycleStage.equalsIgnoreCase("RETIRED"));
    }

    @JsonIgnore
    public boolean isEmpty() {
        return dispositionStatus == null
                && retentionPolicy == null
                && lifecycleStage == null
                && persistenceTimestamp == null
                && commitOutcome == null
                && !retained
                && !archived
                && coordinationAttributes.isEmpty();
    }

    // =========================================================================
    // Immutable Derivations ("With" Mutators)
    // =========================================================================

    public LifecycleContext withDispositionStatus(Topic dispositionStatus) {
        return new LifecycleContext(dispositionStatus, this.retentionPolicy, this.lifecycleStage, this.persistenceTimestamp, this.commitOutcome, this.retained, this.archived, this.coordinationAttributes);
    }

    public LifecycleContext withRetentionPolicy(Topic retentionPolicy) {
        return new LifecycleContext(this.dispositionStatus, retentionPolicy, this.lifecycleStage, this.persistenceTimestamp, this.commitOutcome, this.retained, this.archived, this.coordinationAttributes);
    }

    public LifecycleContext withLifecycleStage(String lifecycleStage) {
        return new LifecycleContext(this.dispositionStatus, this.retentionPolicy, lifecycleStage, this.persistenceTimestamp, this.commitOutcome, this.retained, this.archived, this.coordinationAttributes);
    }

    public LifecycleContext withPersistenceTimestamp(Instant persistenceTimestamp) {
        return new LifecycleContext(this.dispositionStatus, this.retentionPolicy, this.lifecycleStage, persistenceTimestamp, this.commitOutcome, this.retained, this.archived, this.coordinationAttributes);
    }

    public LifecycleContext withCommitOutcome(AuthoritativeCommitOutcome commitOutcome) {
        return new LifecycleContext(this.dispositionStatus, this.retentionPolicy, this.lifecycleStage, this.persistenceTimestamp, commitOutcome, this.retained, this.archived, this.coordinationAttributes);
    }

    public LifecycleContext withRetained(boolean retained) {
        return new LifecycleContext(this.dispositionStatus, this.retentionPolicy, this.lifecycleStage, this.persistenceTimestamp, this.commitOutcome, retained, this.archived, this.coordinationAttributes);
    }

    public LifecycleContext withArchived(boolean archived) {
        return new LifecycleContext(this.dispositionStatus, this.retentionPolicy, this.lifecycleStage, this.persistenceTimestamp, this.commitOutcome, this.retained, archived, this.coordinationAttributes);
    }

    public LifecycleContext withCoordinationAttributes(Map<String, String> coordinationAttributes) {
        return new LifecycleContext(this.dispositionStatus, this.retentionPolicy, this.lifecycleStage, this.persistenceTimestamp, this.commitOutcome, this.retained, this.archived, coordinationAttributes);
    }

    public LifecycleContext withCoordinationAttribute(String key, String value) {
        Objects.requireNonNull(key, "coordinationAttribute key must not be null");
        Map<String, String> updated = new LinkedHashMap<>(this.coordinationAttributes);
        if (value == null) {
            updated.remove(key);
        } else {
            updated.put(key, value);
        }
        return new LifecycleContext(this.dispositionStatus, this.retentionPolicy, this.lifecycleStage, this.persistenceTimestamp, this.commitOutcome, this.retained, this.archived, updated);
    }

    // =========================================================================
    // Standard Object Methods
    // =========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LifecycleContext that = (LifecycleContext) o;
        return retained == that.retained &&
                archived == that.archived &&
                Objects.equals(dispositionStatus, that.dispositionStatus) &&
                Objects.equals(retentionPolicy, that.retentionPolicy) &&
                Objects.equals(lifecycleStage, that.lifecycleStage) &&
                Objects.equals(persistenceTimestamp, that.persistenceTimestamp) &&
                commitOutcome == that.commitOutcome &&
                Objects.equals(coordinationAttributes, that.coordinationAttributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dispositionStatus, retentionPolicy, lifecycleStage, persistenceTimestamp, commitOutcome, retained, archived, coordinationAttributes);
    }

    @Override
    public String toString() {
        return "LifecycleContext{" +
                "hasDisposition=" + (dispositionStatus != null) +
                ", hasRetentionPolicy=" + (retentionPolicy != null) +
                ", hasStage=" + (lifecycleStage != null) +
                ", hasOutcome=" + (commitOutcome != null) +
                ", retained=" + retained +
                ", archived=" + archived +
                ", coordinationAttributeCount=" + coordinationAttributes.size() +
                '}';
    }

}
