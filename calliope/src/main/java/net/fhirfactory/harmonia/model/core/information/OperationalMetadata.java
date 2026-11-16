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
import net.fhirfactory.harmonia.model.core.information.factory.OperationalMetadataBuilder;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeCommitOutcome;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Encapsulates the peer operational metadata associated with middleware operations upon and manifestations
 * of information, parcels, and work.
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with the accepted 1.1.25 Architecture Continuum ({@code model.core.information.InformationUnit})
 * and {@code DRP-OperationalMetadata-and-OperationalMetadataMask.Final.001.md} (§§0, 2):
 * <ul>
 *   <li><b>Peer Operational Concern:</b> {@code OperationalMetadata} is strictly separated from {@link InformationMetadata}.
 *       It is not an internal constituent or identity of an {@link InformationUnit}, nor does it require co-persistence.</li>
 *   <li><b>Clear Separation of Versioning Dimensions:</b>
 *     <ol>
 *       <li><b>Authoritative Persisted IU Version ({@link InformationUnit#version()}):</b> Durable database version committed by Mnemosyne.</li>
 *       <li><b>Mneme Operational Revision ({@link #operationalRevision()}):</b> Active working version typed as {@link OperationalRevision}
 *           for cache optimistic concurrency.</li>
 *       <li><b>Cache Active Generation ({@link #activeGeneration()}):</b> Unique identifier of an active manifestation in a distributed cache.</li>
 *       <li><b>Mneme–Mnemosyne Persistence Coordination ({@link #lifecycleStage()}, {@link #commitOutcome()}):</b> Handshake operation facts.</li>
 *       <li><b>Governed Information Lifecycle ({@link LifecycleContext}):</b> Governed disposition and retention hold of the information.</li>
 *       <li><b>Content Structure Version ({@link ContentMetadata#contentStructureVersion()}):</b> Schema/model definition version.</li>
 *       <li><b>Native Content Version:</b> Intrinsic payload version (e.g. FHIR {@code meta.versionId}) inside native Content.</li>
 *     </ol>
 *   </li>
 * </ul>
 * <p>
 * <b>Defensive Programming:</b>
 * <ul>
 *   <li>Immutable value object design.</li>
 *   <li>All collections are defensively copied and unmodifiable.</li>
 *   <li>Optional fields are exposed via {@link Optional} accessors.</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class OperationalMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final OperationalMetadata EMPTY = new OperationalMetadata(
            null, null, null, null, null, Map.of(), null, null, null, Map.of(), Map.of(), Map.of(), Map.of(), List.of());

    private final String subjectIdentifier;
    private final Topic operationalContext;
    private final OperationalRevision operationalRevision;
    private final String activeGeneration;
    private final Instant cachedAt;
    private final Map<String, Object> transientProperties;

    private final String lifecycleStage;
    private final Instant persistenceTimestamp;
    private final AuthoritativeCommitOutcome commitOutcome;
    private final Map<String, Object> coordinationAttributes;

    private final Map<String, Object> executionCoordination;
    private final Map<String, Object> conveyanceOperations;
    private final Map<String, Object> temporalObservations;
    private final List<String> evidenceReferences;

    /**
     * Canonical constructor establishing validated operational metadata with {@link OperationalRevision}.
     */
    public OperationalMetadata(
            String subjectIdentifier,
            Topic operationalContext,
            OperationalRevision operationalRevision,
            String activeGeneration,
            Instant cachedAt,
            Map<String, Object> transientProperties,
            String lifecycleStage,
            Instant persistenceTimestamp,
            AuthoritativeCommitOutcome commitOutcome,
            Map<String, Object> coordinationAttributes,
            Map<String, Object> executionCoordination,
            Map<String, Object> conveyanceOperations,
            Map<String, Object> temporalObservations,
            List<String> evidenceReferences) {
        this.subjectIdentifier = sanitize(subjectIdentifier);
        this.operationalContext = operationalContext;
        this.operationalRevision = operationalRevision;
        this.activeGeneration = sanitize(activeGeneration);
        this.cachedAt = cachedAt;
        this.transientProperties = copyMap(transientProperties);

        this.lifecycleStage = sanitize(lifecycleStage);
        this.persistenceTimestamp = persistenceTimestamp;
        this.commitOutcome = commitOutcome;
        this.coordinationAttributes = copyMap(coordinationAttributes);

        this.executionCoordination = copyMap(executionCoordination);
        this.conveyanceOperations = copyMap(conveyanceOperations);
        this.temporalObservations = copyMap(temporalObservations);

        if (evidenceReferences == null || evidenceReferences.isEmpty()) {
            this.evidenceReferences = List.of();
        } else {
            List<String> list = new ArrayList<>();
            for (String ref : evidenceReferences) {
                String s = sanitize(ref);
                if (s != null) {
                    list.add(s);
                }
            }
            this.evidenceReferences = Collections.unmodifiableList(list);
        }
    }

    /**
     * Flexible creator handling both {@link OperationalRevision} and legacy parameters.
     */
    @JsonCreator
    public OperationalMetadata(
            @JsonProperty("subjectIdentifier") String subjectIdentifier,
            @JsonProperty("operationalContext") Topic operationalContext,
            @JsonProperty("operationalRevision") Object operationalRevisionRaw,
            @JsonProperty("activeGeneration") String activeGeneration,
            @JsonProperty("cachedAt") Instant cachedAt,
            @JsonProperty("transientProperties") Map<String, Object> transientProperties,
            @JsonProperty("lifecycleStage") String lifecycleStage,
            @JsonProperty("persistenceTimestamp") Instant persistenceTimestamp,
            @JsonProperty("commitOutcome") AuthoritativeCommitOutcome commitOutcome,
            @JsonProperty("coordinationAttributes") Map<String, Object> coordinationAttributes,
            @JsonProperty("executionCoordination") Map<String, Object> executionCoordination,
            @JsonProperty("conveyanceOperations") Map<String, Object> conveyanceOperations,
            @JsonProperty("temporalObservations") Map<String, Object> temporalObservations,
            @JsonProperty("evidenceReferences") List<String> evidenceReferences) {
        this(
                subjectIdentifier,
                operationalContext,
                parseRevision(operationalRevisionRaw),
                activeGeneration,
                cachedAt,
                transientProperties,
                lifecycleStage,
                persistenceTimestamp,
                commitOutcome,
                coordinationAttributes,
                executionCoordination,
                conveyanceOperations,
                temporalObservations,
                evidenceReferences
        );
    }

    private static OperationalRevision parseRevision(Object raw) {
        if (raw == null) {
            return null;
        }
        if (raw instanceof OperationalRevision or) {
            return or;
        }
        if (raw instanceof AuthoritativeVersion av) {
            return OperationalRevision.of(av.value());
        }
        String str = raw.toString().trim();
        return str.isEmpty() ? null : OperationalRevision.of(str);
    }

    private static String sanitize(String input) {
        return input != null && !input.isBlank() ? input.trim() : null;
    }

    private static Map<String, Object> copyMap(Map<String, Object> input) {
        if (input == null || input.isEmpty()) {
            return Map.of();
        }
        return Collections.unmodifiableMap(new LinkedHashMap<>(input));
    }

    /**
     * Returns an empty {@code OperationalMetadata} instance.
     *
     * @return empty OperationalMetadata
     */
    public static OperationalMetadata empty() {
        return EMPTY;
    }

    /**
     * Creates a new builder for constructing {@code OperationalMetadata}.
     *
     * @return a new Builder instance
     */
    public static OperationalMetadataBuilder builder() {
        return new OperationalMetadataBuilder();
    }

    // =========================================================================
    // Accessors
    // =========================================================================

    @JsonIgnore
    public Optional<String> subjectIdentifier() {
        return Optional.ofNullable(subjectIdentifier);
    }

    @JsonProperty("subjectIdentifier")
    public String getSubjectIdentifier() {
        return subjectIdentifier;
    }

    @JsonIgnore
    public Optional<Topic> subjectTopic() {
        if (subjectIdentifier == null) return Optional.empty();
        try {
            return Optional.of(Topic.fromName(subjectIdentifier));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @JsonIgnore
    public Optional<UUID> subjectUuid() {
        if (subjectIdentifier == null) return Optional.empty();
        try {
            return Optional.of(UUID.fromString(subjectIdentifier));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @JsonIgnore
    public Optional<Topic> operationalContext() {
        return Optional.ofNullable(operationalContext);
    }

    @JsonProperty("operationalContext")
    public Topic getOperationalContext() {
        return operationalContext;
    }

    @JsonIgnore
    public Optional<OperationalRevision> operationalRevision() {
        return Optional.ofNullable(operationalRevision);
    }

    @JsonProperty("operationalRevision")
    public OperationalRevision getOperationalRevision() {
        return operationalRevision;
    }

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

    @JsonIgnore
    public Optional<String> activeGeneration() {
        return Optional.ofNullable(activeGeneration);
    }

    @JsonProperty("activeGeneration")
    public String getActiveGeneration() {
        return activeGeneration;
    }

    @JsonIgnore
    public Optional<Instant> cachedAt() {
        return Optional.ofNullable(cachedAt);
    }

    @JsonProperty("cachedAt")
    public Instant getCachedAt() {
        return cachedAt;
    }

    @JsonProperty("transientProperties")
    public Map<String, Object> transientProperties() {
        return transientProperties;
    }

    public Map<String, Object> getTransientProperties() {
        return transientProperties;
    }

    @JsonIgnore
    public Optional<String> lifecycleStage() {
        return Optional.ofNullable(lifecycleStage);
    }

    @JsonProperty("lifecycleStage")
    public String getLifecycleStage() {
        return lifecycleStage;
    }

    @JsonIgnore
    public Optional<Topic> lifecycleStageTopic() {
        if (lifecycleStage == null) return Optional.empty();
        return Optional.of(Topic.fromName("Harmonia.Persistence.Stage." + lifecycleStage));
    }

    @JsonIgnore
    public Optional<Instant> persistenceTimestamp() {
        return Optional.ofNullable(persistenceTimestamp);
    }

    @JsonProperty("persistenceTimestamp")
    public Instant getPersistenceTimestamp() {
        return persistenceTimestamp;
    }

    @JsonIgnore
    public Optional<AuthoritativeCommitOutcome> commitOutcome() {
        return Optional.ofNullable(commitOutcome);
    }

    @JsonProperty("commitOutcome")
    public AuthoritativeCommitOutcome getCommitOutcome() {
        return commitOutcome;
    }

    @JsonProperty("coordinationAttributes")
    public Map<String, Object> coordinationAttributes() {
        return coordinationAttributes;
    }

    public Map<String, Object> getCoordinationAttributes() {
        return coordinationAttributes;
    }

    @JsonProperty("executionCoordination")
    public Map<String, Object> executionCoordination() {
        return executionCoordination;
    }

    public Map<String, Object> getExecutionCoordination() {
        return executionCoordination;
    }

    @JsonProperty("conveyanceOperations")
    public Map<String, Object> conveyanceOperations() {
        return conveyanceOperations;
    }

    public Map<String, Object> getConveyanceOperations() {
        return conveyanceOperations;
    }

    @JsonProperty("temporalObservations")
    public Map<String, Object> temporalObservations() {
        return temporalObservations;
    }

    public Map<String, Object> getTemporalObservations() {
        return temporalObservations;
    }

    @JsonProperty("evidenceReferences")
    public List<String> evidenceReferences() {
        return evidenceReferences;
    }

    public List<String> getEvidenceReferences() {
        return evidenceReferences;
    }

    // =========================================================================
    // Standard Object Methods
    // =========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationalMetadata that = (OperationalMetadata) o;
        return Objects.equals(subjectIdentifier, that.subjectIdentifier) &&
                Objects.equals(operationalContext, that.operationalContext) &&
                Objects.equals(operationalRevision, that.operationalRevision) &&
                Objects.equals(activeGeneration, that.activeGeneration) &&
                Objects.equals(cachedAt, that.cachedAt) &&
                Objects.equals(transientProperties, that.transientProperties) &&
                Objects.equals(lifecycleStage, that.lifecycleStage) &&
                Objects.equals(persistenceTimestamp, that.persistenceTimestamp) &&
                commitOutcome == that.commitOutcome &&
                Objects.equals(coordinationAttributes, that.coordinationAttributes) &&
                Objects.equals(executionCoordination, that.executionCoordination) &&
                Objects.equals(conveyanceOperations, that.conveyanceOperations) &&
                Objects.equals(temporalObservations, that.temporalObservations) &&
                Objects.equals(evidenceReferences, that.evidenceReferences);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectIdentifier, operationalContext, operationalRevision, activeGeneration,
                cachedAt, transientProperties, lifecycleStage, persistenceTimestamp, commitOutcome,
                coordinationAttributes, executionCoordination, conveyanceOperations,
                temporalObservations, evidenceReferences);
    }

    @Override
    public String toString() {
        return "OperationalMetadata{" +
                "subject='" + subjectIdentifier + '\'' +
                ", context=" + operationalContext +
                ", revision=" + operationalRevision +
                ", generation='" + activeGeneration + '\'' +
                ", stage='" + lifecycleStage + '\'' +
                ", outcome=" + commitOutcome +
                '}';
    }

    // =========================================================================
    // Builder
    // =========================================================================

    public static final class Builder extends OperationalMetadataBuilder {
    }
}
