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

import net.fhirfactory.harmonia.model.core.naming.factory.TopicFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import net.fhirfactory.harmonia.model.core.information.access.AuthoritativeCommitOutcome;
import net.fhirfactory.harmonia.model.core.information.operations.factory.OperationalMetadataBuilder;
import net.fhirfactory.harmonia.model.core.naming.Topic;

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
 * Groups operational observations associated with an IU, manifestation or operation.
 * <p>
 * The embedded {@link ManagementContext} retains the existing cache/concurrency view.
 * Persistence-handshake observations remain distinct from governed information
 * lifecycle state, while category maps retain their JSON value types as {@link JsonNode}s.
 * This value does not establish durable authority or infer a new operational schema.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class OperationalMetadata implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final OperationalMetadata EMPTY = new OperationalMetadata(
            null, null, null, null, null, null, Map.of(), Map.of(), Map.of(), Map.of(), List.of());

    private final String subjectIdentifier;
    private final Topic operationalContext;
    private final ManagementContext managementContext;
    private final String lifecycleStage;
    private final Instant persistenceTimestamp;
    private final AuthoritativeCommitOutcome commitOutcome;
    private final Map<String, JsonNode> coordinationAttributes;
    private final Map<String, JsonNode> executionCoordination;
    private final Map<String, JsonNode> conveyanceOperations;
    private final Map<String, JsonNode> temporalObservations;
    private final List<String> evidenceReferences;

    /**
     * Creates operational metadata from typed working context and JSON-valued observations.
     *
     * @param subjectIdentifier operational subject identifier
     * @param operationalContext operational context Topic
     * @param managementContext optional cache and operational-revision view
     * @param lifecycleStage persistence-handshake stage, not governed disposition
     * @param persistenceTimestamp operation timestamp
     * @param commitOutcome persistence commit result
     * @param coordinationAttributes coordination observation values
     * @param executionCoordination execution observation values
     * @param conveyanceOperations conveyance observation values
     * @param temporalObservations temporal observation values
     * @param evidenceReferences references supporting these observations
     */
    @JsonCreator
    public OperationalMetadata(
            @JsonProperty("subjectIdentifier") String subjectIdentifier,
            @JsonProperty("operationalContext") Topic operationalContext,
            @JsonProperty("managementContext") ManagementContext managementContext,
            @JsonProperty("lifecycleStage") String lifecycleStage,
            @JsonProperty("persistenceTimestamp") Instant persistenceTimestamp,
            @JsonProperty("commitOutcome") AuthoritativeCommitOutcome commitOutcome,
            @JsonProperty("coordinationAttributes") Map<String, JsonNode> coordinationAttributes,
            @JsonProperty("executionCoordination") Map<String, JsonNode> executionCoordination,
            @JsonProperty("conveyanceOperations") Map<String, JsonNode> conveyanceOperations,
            @JsonProperty("temporalObservations") Map<String, JsonNode> temporalObservations,
            @JsonProperty("evidenceReferences") List<String> evidenceReferences) {
        this.subjectIdentifier = sanitize(subjectIdentifier);
        this.operationalContext = operationalContext;
        this.managementContext = managementContext;
        this.lifecycleStage = sanitize(lifecycleStage);
        this.persistenceTimestamp = persistenceTimestamp;
        this.commitOutcome = commitOutcome;
        this.coordinationAttributes = copyJsonMap(coordinationAttributes);
        this.executionCoordination = copyJsonMap(executionCoordination);
        this.conveyanceOperations = copyJsonMap(conveyanceOperations);
        this.temporalObservations = copyJsonMap(temporalObservations);
        this.evidenceReferences = copyReferences(evidenceReferences);
    }

    /** Returns the shared empty operational observation value. */
    public static OperationalMetadata empty() {
        return EMPTY;
    }

    /** Returns a builder for assembling operational observations. */
    public static OperationalMetadataBuilder builder() {
        return new OperationalMetadataBuilder();
    }

    /** Returns the optional operational subject identifier. */
    @JsonIgnore
    public Optional<String> subjectIdentifier() {
        return Optional.ofNullable(subjectIdentifier);
    }

    /** Returns the serialized operational subject identifier. */
    @JsonProperty("subjectIdentifier")
    public String getSubjectIdentifier() {
        return subjectIdentifier;
    }

    /** Returns the subject identifier as a Topic when it has a valid Topic name. */
    @JsonIgnore
    public Optional<Topic> subjectTopic() {
        if (subjectIdentifier == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(TopicFactory.fromName(subjectIdentifier));
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }

    /** Returns the subject identifier as a UUID when it has a valid UUID form. */
    @JsonIgnore
    public Optional<UUID> subjectUuid() {
        if (subjectIdentifier == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(UUID.fromString(subjectIdentifier));
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }

    /** Returns the optional operational context Topic. */
    @JsonIgnore
    public Optional<Topic> operationalContext() {
        return Optional.ofNullable(operationalContext);
    }

    /** Returns the serialized operational context Topic. */
    @JsonProperty("operationalContext")
    public Topic getOperationalContext() {
        return operationalContext;
    }

    /** Returns the optional working/cache context. */
    @JsonIgnore
    public Optional<ManagementContext> managementContext() {
        return Optional.ofNullable(managementContext);
    }

    /** Returns the serialized working/cache context. */
    @JsonProperty("managementContext")
    public ManagementContext getManagementContext() {
        return managementContext;
    }

    /** Returns the operational revision through the retained ManagementContext projection. */
    @JsonIgnore
    public Optional<OperationalRevision> operationalRevision() {
        return managementContext != null ? managementContext.operationalRevision() : Optional.empty();
    }

    /** Returns the operational revision for existing Java callers without adding a JSON alias. */
    @JsonIgnore
    public OperationalRevision getOperationalRevision() {
        return operationalRevision().orElse(null);
    }

    /** Returns the active generation through the retained ManagementContext projection. */
    @JsonIgnore
    public Optional<String> activeGeneration() {
        return managementContext != null ? managementContext.activeGeneration() : Optional.empty();
    }

    /** Returns the active generation for existing Java callers without adding a JSON alias. */
    @JsonIgnore
    public String getActiveGeneration() {
        return activeGeneration().orElse(null);
    }

    /** Returns the working-cache timestamp through the retained ManagementContext projection. */
    @JsonIgnore
    public Optional<Instant> cachedAt() {
        return managementContext != null ? managementContext.cachedAt() : Optional.empty();
    }

    /** Returns transient working properties through the retained ManagementContext projection. */
    @JsonIgnore
    public Map<String, String> transientProperties() {
        return managementContext != null ? managementContext.transientProperties() : Map.of();
    }

    /** Returns the optional persistence-handshake stage. */
    @JsonIgnore
    public Optional<String> lifecycleStage() {
        return Optional.ofNullable(lifecycleStage);
    }

    /** Returns the serialized persistence-handshake stage. */
    @JsonProperty("lifecycleStage")
    public String getLifecycleStage() {
        return lifecycleStage;
    }

    /** Returns the stage as its canonical persistence Topic, when present. */
    @JsonIgnore
    public Optional<Topic> lifecycleStageTopic() {
        return lifecycleStage != null
                ? Optional.of(TopicFactory.fromName("Harmonia.Persistence.Stage." + lifecycleStage))
                : Optional.empty();
    }

    /** Returns the optional persistence operation timestamp. */
    @JsonIgnore
    public Optional<Instant> persistenceTimestamp() {
        return Optional.ofNullable(persistenceTimestamp);
    }

    /** Returns the serialized persistence operation timestamp. */
    @JsonProperty("persistenceTimestamp")
    public Instant getPersistenceTimestamp() {
        return persistenceTimestamp;
    }

    /** Returns the optional persistence commit outcome. */
    @JsonIgnore
    public Optional<AuthoritativeCommitOutcome> commitOutcome() {
        return Optional.ofNullable(commitOutcome);
    }

    /** Returns the serialized persistence commit outcome. */
    @JsonProperty("commitOutcome")
    public AuthoritativeCommitOutcome getCommitOutcome() {
        return commitOutcome;
    }

    /** Returns immutable coordination values, preserving their JSON types. */
    @JsonProperty("coordinationAttributes")
    public Map<String, JsonNode> coordinationAttributes() {
        return coordinationAttributes;
    }

    /** Returns immutable execution-coordination values, preserving their JSON types. */
    @JsonProperty("executionCoordination")
    public Map<String, JsonNode> executionCoordination() {
        return executionCoordination;
    }

    /** Returns immutable conveyance-operation values, preserving their JSON types. */
    @JsonProperty("conveyanceOperations")
    public Map<String, JsonNode> conveyanceOperations() {
        return conveyanceOperations;
    }

    /** Returns immutable temporal observations, preserving their JSON types. */
    @JsonProperty("temporalObservations")
    public Map<String, JsonNode> temporalObservations() {
        return temporalObservations;
    }

    /** Returns immutable evidence references. */
    @JsonProperty("evidenceReferences")
    public List<String> evidenceReferences() {
        return evidenceReferences;
    }

    /** Indicates whether every operational fact is absent. */
    @JsonIgnore
    public boolean isEmpty() {
        return subjectIdentifier == null
                && operationalContext == null
                && (managementContext == null || managementContext.isEmpty())
                && lifecycleStage == null
                && persistenceTimestamp == null
                && commitOutcome == null
                && coordinationAttributes.isEmpty()
                && executionCoordination.isEmpty()
                && conveyanceOperations.isEmpty()
                && temporalObservations.isEmpty()
                && evidenceReferences.isEmpty();
    }

    /** Returns a copy with a replacement working/cache context. */
    public OperationalMetadata withManagementContext(ManagementContext managementContext) {
        return new OperationalMetadata(subjectIdentifier, operationalContext, managementContext, lifecycleStage,
                persistenceTimestamp, commitOutcome, coordinationAttributes, executionCoordination,
                conveyanceOperations, temporalObservations, evidenceReferences);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        OperationalMetadata that = (OperationalMetadata) other;
        return Objects.equals(subjectIdentifier, that.subjectIdentifier)
                && Objects.equals(operationalContext, that.operationalContext)
                && Objects.equals(managementContext, that.managementContext)
                && Objects.equals(lifecycleStage, that.lifecycleStage)
                && Objects.equals(persistenceTimestamp, that.persistenceTimestamp)
                && commitOutcome == that.commitOutcome
                && Objects.equals(coordinationAttributes, that.coordinationAttributes)
                && Objects.equals(executionCoordination, that.executionCoordination)
                && Objects.equals(conveyanceOperations, that.conveyanceOperations)
                && Objects.equals(temporalObservations, that.temporalObservations)
                && Objects.equals(evidenceReferences, that.evidenceReferences);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectIdentifier, operationalContext, managementContext, lifecycleStage,
                persistenceTimestamp, commitOutcome, coordinationAttributes, executionCoordination,
                conveyanceOperations, temporalObservations, evidenceReferences);
    }

    /** Returns a PHI-safe summary containing only presence and collection counts. */
    @Override
    public String toString() {
        return "OperationalMetadata{" +
                "hasSubject=" + (subjectIdentifier != null) +
                ", hasContext=" + (operationalContext != null) +
                ", hasManagementContext=" + (managementContext != null && !managementContext.isEmpty()) +
                ", hasLifecycleStage=" + (lifecycleStage != null) +
                ", coordinationCount=" + coordinationAttributes.size() +
                ", executionCount=" + executionCoordination.size() +
                ", conveyanceCount=" + conveyanceOperations.size() +
                ", temporalCount=" + temporalObservations.size() +
                ", evidenceReferenceCount=" + evidenceReferences.size() +
                '}';
    }

    private static String sanitize(String value) {
        return value != null && !value.isBlank() ? value.trim() : null;
    }

    private static Map<String, JsonNode> copyJsonMap(Map<String, JsonNode> values) {
        if (values == null || values.isEmpty()) {
            return Map.of();
        }
        Map<String, JsonNode> copy = new LinkedHashMap<>();
        values.forEach((key, value) -> {
            if (key != null && !key.isBlank() && value != null) {
                copy.put(key.trim(), value.deepCopy());
            }
        });
        return copy.isEmpty() ? Map.of() : Collections.unmodifiableMap(copy);
    }

    private static List<String> copyReferences(List<String> references) {
        if (references == null || references.isEmpty()) {
            return List.of();
        }
        List<String> copy = new ArrayList<>();
        for (String reference : references) {
            String value = sanitize(reference);
            if (value != null) {
                copy.add(value);
            }
        }
        return copy.isEmpty() ? List.of() : Collections.unmodifiableList(copy);
    }
}