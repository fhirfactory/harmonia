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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeCommitOutcome;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.topic.Topic;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Encapsulates the peer operational metadata associated with middleware operations upon and manifestations
 * of information, parcels, and work.
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with the accepted 1.1.25 Architecture Continuum ({@code model.core.information.InformationUnit}):
 * <ul>
 *   <li><b>Peer Operational Concern:</b> {@code OperationalMetadata} is strictly separated from {@link InformationUnitMetadata}.
 *       It is not an internal constituent or identity of an {@link InformationUnit}, nor does it require co-persistence.</li>
 *   <li><b>Clear Separation of Versioning Dimensions:</b>
 *     <ol>
 *       <li><b>Authoritative Persisted IU Version ({@link InformationUnit#version()}):</b> Durable database version committed by Mnemosyne.</li>
 *       <li><b>Mneme Operational Revision ({@link #operationalRevision()}):</b> Active working version for cache optimistic concurrency.</li>
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
    private final AuthoritativeVersion operationalRevision;
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
     * Canonical constructor establishing validated operational metadata.
     */
    @JsonCreator
    public OperationalMetadata(
            @JsonProperty("subjectIdentifier") String subjectIdentifier,
            @JsonProperty("operationalContext") Topic operationalContext,
            @JsonProperty("operationalRevision") AuthoritativeVersion operationalRevision,
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
    public static Builder builder() {
        return new Builder();
    }

    // =========================================================================
    // Accessors
    // =========================================================================

    public Optional<String> subjectIdentifier() {
        return Optional.ofNullable(subjectIdentifier);
    }

    public String getSubjectIdentifier() {
        return subjectIdentifier;
    }

    public Optional<Topic> operationalContext() {
        return Optional.ofNullable(operationalContext);
    }

    public Topic getOperationalContext() {
        return operationalContext;
    }

    public Optional<AuthoritativeVersion> operationalRevision() {
        return Optional.ofNullable(operationalRevision);
    }

    public AuthoritativeVersion getOperationalRevision() {
        return operationalRevision;
    }

    public Optional<String> activeGeneration() {
        return Optional.ofNullable(activeGeneration);
    }

    public String getActiveGeneration() {
        return activeGeneration;
    }

    public Optional<Instant> cachedAt() {
        return Optional.ofNullable(cachedAt);
    }

    public Instant getCachedAt() {
        return cachedAt;
    }

    public Map<String, Object> transientProperties() {
        return transientProperties;
    }

    public Map<String, Object> getTransientProperties() {
        return transientProperties;
    }

    public Optional<String> lifecycleStage() {
        return Optional.ofNullable(lifecycleStage);
    }

    public String getLifecycleStage() {
        return lifecycleStage;
    }

    public Optional<Instant> persistenceTimestamp() {
        return Optional.ofNullable(persistenceTimestamp);
    }

    public Instant getPersistenceTimestamp() {
        return persistenceTimestamp;
    }

    public Optional<AuthoritativeCommitOutcome> commitOutcome() {
        return Optional.ofNullable(commitOutcome);
    }

    public AuthoritativeCommitOutcome getCommitOutcome() {
        return commitOutcome;
    }

    public Map<String, Object> coordinationAttributes() {
        return coordinationAttributes;
    }

    public Map<String, Object> getCoordinationAttributes() {
        return coordinationAttributes;
    }

    public Map<String, Object> executionCoordination() {
        return executionCoordination;
    }

    public Map<String, Object> getExecutionCoordination() {
        return executionCoordination;
    }

    public Map<String, Object> conveyanceOperations() {
        return conveyanceOperations;
    }

    public Map<String, Object> getConveyanceOperations() {
        return conveyanceOperations;
    }

    public Map<String, Object> temporalObservations() {
        return temporalObservations;
    }

    public Map<String, Object> getTemporalObservations() {
        return temporalObservations;
    }

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
        return Objects.hash(subjectIdentifier, operationalContext, operationalRevision, activeGeneration, cachedAt,
                transientProperties, lifecycleStage, persistenceTimestamp, commitOutcome, coordinationAttributes,
                executionCoordination, conveyanceOperations, temporalObservations, evidenceReferences);
    }

    @Override
    public String toString() {
        return "OperationalMetadata{" +
                "subject='" + subjectIdentifier + '\'' +
                ", context=" + (operationalContext != null ? operationalContext.getName() : "null") +
                ", revision=" + operationalRevision +
                ", generation='" + activeGeneration + '\'' +
                ", stage='" + lifecycleStage + '\'' +
                ", outcome=" + commitOutcome +
                '}';
    }

    // =========================================================================
    // Builder
    // =========================================================================

    public static final class Builder {

        private String subjectIdentifier;
        private Topic operationalContext;
        private AuthoritativeVersion operationalRevision;
        private String activeGeneration;
        private Instant cachedAt;
        private final Map<String, Object> transientProperties = new LinkedHashMap<>();

        private String lifecycleStage;
        private Instant persistenceTimestamp;
        private AuthoritativeCommitOutcome commitOutcome;
        private final Map<String, Object> coordinationAttributes = new LinkedHashMap<>();

        private final Map<String, Object> executionCoordination = new LinkedHashMap<>();
        private final Map<String, Object> conveyanceOperations = new LinkedHashMap<>();
        private final Map<String, Object> temporalObservations = new LinkedHashMap<>();
        private final List<String> evidenceReferences = new ArrayList<>();

        public Builder subjectIdentifier(String subjectIdentifier) {
            this.subjectIdentifier = subjectIdentifier;
            return this;
        }

        public Builder operationalContext(Topic operationalContext) {
            this.operationalContext = operationalContext;
            return this;
        }

        public Builder operationalRevision(AuthoritativeVersion operationalRevision) {
            this.operationalRevision = operationalRevision;
            return this;
        }

        public Builder operationalRevision(String operationalRevision) {
            this.operationalRevision = operationalRevision != null ? AuthoritativeVersion.of(operationalRevision) : null;
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

        public Builder putTransientProperty(String key, Object value) {
            if (key != null) {
                this.transientProperties.put(key, value);
            }
            return this;
        }

        public Builder transientProperties(Map<String, Object> properties) {
            this.transientProperties.clear();
            if (properties != null) {
                this.transientProperties.putAll(properties);
            }
            return this;
        }

        public Builder lifecycleStage(String lifecycleStage) {
            this.lifecycleStage = lifecycleStage;
            return this;
        }

        public Builder persistenceTimestamp(Instant persistenceTimestamp) {
            this.persistenceTimestamp = persistenceTimestamp;
            return this;
        }

        public Builder commitOutcome(AuthoritativeCommitOutcome commitOutcome) {
            this.commitOutcome = commitOutcome;
            return this;
        }

        public Builder putCoordinationAttribute(String key, Object value) {
            if (key != null) {
                this.coordinationAttributes.put(key, value);
            }
            return this;
        }

        public Builder coordinationAttributes(Map<String, Object> attributes) {
            this.coordinationAttributes.clear();
            if (attributes != null) {
                this.coordinationAttributes.putAll(attributes);
            }
            return this;
        }

        public Builder putExecutionCoordination(String key, Object value) {
            if (key != null) {
                this.executionCoordination.put(key, value);
            }
            return this;
        }

        public Builder putConveyanceOperation(String key, Object value) {
            if (key != null) {
                this.conveyanceOperations.put(key, value);
            }
            return this;
        }

        public Builder putTemporalObservation(String key, Object value) {
            if (key != null) {
                this.temporalObservations.put(key, value);
            }
            return this;
        }

        public Builder addEvidenceReference(String evidenceReference) {
            if (evidenceReference != null && !evidenceReference.isBlank()) {
                this.evidenceReferences.add(evidenceReference.trim());
            }
            return this;
        }

        public OperationalMetadata build() {
            return new OperationalMetadata(
                    subjectIdentifier,
                    operationalContext,
                    operationalRevision,
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
                    evidenceReferences);
        }
    }
}
