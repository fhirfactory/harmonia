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

package net.fhirfactory.harmonia.model.core.information.operations.factory;

import com.fasterxml.jackson.databind.JsonNode;
import net.fhirfactory.harmonia.model.core.information.access.AuthoritativeCommitOutcome;
import net.fhirfactory.harmonia.model.core.information.operations.ManagementContext;
import net.fhirfactory.harmonia.model.core.information.operations.OperationalMetadata;
import net.fhirfactory.harmonia.model.core.naming.Topic;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Assembles peer operational observations while keeping cache state in {@link ManagementContext}.
 * <p>
 * Observation values use Jackson's JSON tree type so that their JSON kinds survive
 * serialization without inventing a fixed schema for the open observation categories.
 */
public final class OperationalMetadataBuilder {

    private String subjectIdentifier;
    private Topic operationalContext;
    private ManagementContext managementContext;
    private String lifecycleStage;
    private Instant persistenceTimestamp;
    private AuthoritativeCommitOutcome commitOutcome;
    private final Map<String, JsonNode> coordinationAttributes = new LinkedHashMap<>();
    private final Map<String, JsonNode> executionCoordination = new LinkedHashMap<>();
    private final Map<String, JsonNode> conveyanceOperations = new LinkedHashMap<>();
    private final Map<String, JsonNode> temporalObservations = new LinkedHashMap<>();
    private final List<String> evidenceReferences = new ArrayList<>();

    /** Sets the operational subject identifier. */
    public OperationalMetadataBuilder subjectIdentifier(String subjectIdentifier) {
        this.subjectIdentifier = subjectIdentifier;
        return this;
    }

    /** Sets the operational subject identifier from a UUID. */
    public OperationalMetadataBuilder subjectIdentifier(UUID uuid) {
        this.subjectIdentifier = uuid != null ? uuid.toString() : null;
        return this;
    }

    /** Sets the operational subject identifier from a Topic. */
    public OperationalMetadataBuilder subjectTopic(Topic topic) {
        this.subjectIdentifier = topic != null ? topic.getName() : null;
        return this;
    }

    /** Sets the Topic describing the operational context. */
    public OperationalMetadataBuilder operationalContext(Topic operationalContext) {
        this.operationalContext = operationalContext;
        return this;
    }

    /** Sets the peer working/cache context without copying its values into this object. */
    public OperationalMetadataBuilder managementContext(ManagementContext managementContext) {
        this.managementContext = managementContext;
        return this;
    }

    /** Sets a persistence-handshake stage, not an information disposition. */
    public OperationalMetadataBuilder lifecycleStage(String lifecycleStage) {
        this.lifecycleStage = lifecycleStage;
        return this;
    }

    /** Sets a persistence stage using its Topic name. */
    public OperationalMetadataBuilder lifecycleStage(Topic stageTopic) {
        this.lifecycleStage = stageTopic != null ? stageTopic.getName() : null;
        return this;
    }

    /** Sets the timestamp associated with the persistence operation. */
    public OperationalMetadataBuilder persistenceTimestamp(Instant persistenceTimestamp) {
        this.persistenceTimestamp = persistenceTimestamp;
        return this;
    }

    /** Sets the persistence commit outcome. */
    public OperationalMetadataBuilder commitOutcome(AuthoritativeCommitOutcome commitOutcome) {
        this.commitOutcome = commitOutcome;
        return this;
    }

    /** Replaces coordination observations; null clears the category. */
    public OperationalMetadataBuilder coordinationAttributes(Map<String, JsonNode> values) {
        replace(coordinationAttributes, values);
        return this;
    }

    /** Adds or removes one coordination observation. */
    public OperationalMetadataBuilder coordinationAttribute(String key, JsonNode value) {
        put(coordinationAttributes, key, value);
        return this;
    }

    /** Replaces execution observations; null clears the category. */
    public OperationalMetadataBuilder executionCoordination(Map<String, JsonNode> values) {
        replace(executionCoordination, values);
        return this;
    }

    /** Adds or removes one execution observation. */
    public OperationalMetadataBuilder executionObservation(String key, JsonNode value) {
        put(executionCoordination, key, value);
        return this;
    }

    /** Replaces conveyance observations; null clears the category. */
    public OperationalMetadataBuilder conveyanceOperations(Map<String, JsonNode> values) {
        replace(conveyanceOperations, values);
        return this;
    }

    /** Adds or removes one conveyance observation. */
    public OperationalMetadataBuilder conveyanceOperation(String key, JsonNode value) {
        put(conveyanceOperations, key, value);
        return this;
    }

    /** Replaces temporal observations; null clears the category. */
    public OperationalMetadataBuilder temporalObservations(Map<String, JsonNode> values) {
        replace(temporalObservations, values);
        return this;
    }

    /** Adds or removes one temporal observation. */
    public OperationalMetadataBuilder temporalObservation(String key, JsonNode value) {
        put(temporalObservations, key, value);
        return this;
    }

    /** Replaces evidence references; null clears the list. */
    public OperationalMetadataBuilder evidenceReferences(List<String> references) {
        evidenceReferences.clear();
        if (references != null) {
            evidenceReferences.addAll(references);
        }
        return this;
    }

    /** Adds one non-blank evidence reference. */
    public OperationalMetadataBuilder evidenceReference(String reference) {
        if (reference != null && !reference.isBlank()) {
            evidenceReferences.add(reference.trim());
        }
        return this;
    }

    /** Builds an immutable operational metadata value. */
    public OperationalMetadata build() {
        return new OperationalMetadata(subjectIdentifier, operationalContext, managementContext, lifecycleStage,
                persistenceTimestamp, commitOutcome, coordinationAttributes, executionCoordination,
                conveyanceOperations, temporalObservations, evidenceReferences);
    }

    private static void replace(Map<String, JsonNode> destination, Map<String, JsonNode> source) {
        destination.clear();
        if (source != null) {
            destination.putAll(source);
        }
    }

    private static void put(Map<String, JsonNode> destination, String key, JsonNode value) {
        if (key == null) {
            return;
        }
        if (value == null) {
            destination.remove(key);
        } else {
            destination.put(key, value);
        }
    }
}