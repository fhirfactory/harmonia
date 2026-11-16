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

package net.fhirfactory.harmonia.model.core.information.factory;

import net.fhirfactory.harmonia.model.core.information.OperationalMetadata;
import net.fhirfactory.harmonia.model.core.information.OperationalRevision;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeCommitOutcome;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;

import java.time.Instant;
import java.util.*;

/**
 * Fluent builder for creating {@link OperationalMetadata} instances.
 */
public class OperationalMetadataBuilder {

    protected String subjectIdentifier;
    protected Topic operationalContext;
    protected OperationalRevision operationalRevision;
    protected String activeGeneration;
    protected Instant cachedAt;
    protected final Map<String, Object> transientProperties = new LinkedHashMap<>();

    protected String lifecycleStage;
    protected Instant persistenceTimestamp;
    protected AuthoritativeCommitOutcome commitOutcome;
    protected final Map<String, Object> coordinationAttributes = new LinkedHashMap<>();

    protected final Map<String, Object> executionCoordination = new LinkedHashMap<>();
    protected final Map<String, Object> conveyanceOperations = new LinkedHashMap<>();
    protected final Map<String, Object> temporalObservations = new LinkedHashMap<>();
    protected final List<String> evidenceReferences = new ArrayList<>();

    public OperationalMetadataBuilder() {
    }

    public OperationalMetadataBuilder subjectIdentifier(String subjectIdentifier) {
        this.subjectIdentifier = subjectIdentifier;
        return this;
    }

    public OperationalMetadataBuilder subjectIdentifier(UUID uuid) {
        this.subjectIdentifier = uuid != null ? uuid.toString() : null;
        return this;
    }

    public OperationalMetadataBuilder subjectTopic(Topic topic) {
        this.subjectIdentifier = topic != null ? topic.getName() : null;
        return this;
    }

    public OperationalMetadataBuilder operationalContext(Topic operationalContext) {
        this.operationalContext = operationalContext;
        return this;
    }

    public OperationalMetadataBuilder operationalRevision(OperationalRevision operationalRevision) {
        this.operationalRevision = operationalRevision;
        return this;
    }

    public OperationalMetadataBuilder operationalRevision(String operationalRevision) {
        this.operationalRevision = operationalRevision != null && !operationalRevision.isBlank()
                ? OperationalRevision.of(operationalRevision)
                : null;
        return this;
    }

    @Deprecated
    public OperationalMetadataBuilder operationalRevision(AuthoritativeVersion operationalVersion) {
        this.operationalRevision = operationalVersion != null
                ? OperationalRevision.of(operationalVersion.value())
                : null;
        return this;
    }

    public OperationalMetadataBuilder activeGeneration(String activeGeneration) {
        this.activeGeneration = activeGeneration;
        return this;
    }

    public OperationalMetadataBuilder cachedAt(Instant cachedAt) {
        this.cachedAt = cachedAt;
        return this;
    }

    public OperationalMetadataBuilder transientProperties(Map<String, Object> transientProperties) {
        this.transientProperties.clear();
        if (transientProperties != null) {
            this.transientProperties.putAll(transientProperties);
        }
        return this;
    }

    public OperationalMetadataBuilder putTransientProperty(String key, Object value) {
        return transientProperty(key, value);
    }

    public OperationalMetadataBuilder transientProperty(String key, Object value) {
        if (key != null) {
            if (value == null) {
                this.transientProperties.remove(key);
            } else {
                this.transientProperties.put(key, value);
            }
        }
        return this;
    }

    public OperationalMetadataBuilder lifecycleStage(String lifecycleStage) {
        this.lifecycleStage = lifecycleStage;
        return this;
    }

    public OperationalMetadataBuilder lifecycleStage(Topic stageTopic) {
        this.lifecycleStage = stageTopic != null ? stageTopic.getName() : null;
        return this;
    }

    public OperationalMetadataBuilder persistenceTimestamp(Instant persistenceTimestamp) {
        this.persistenceTimestamp = persistenceTimestamp;
        return this;
    }

    public OperationalMetadataBuilder commitOutcome(AuthoritativeCommitOutcome commitOutcome) {
        this.commitOutcome = commitOutcome;
        return this;
    }

    public OperationalMetadataBuilder coordinationAttributes(Map<String, Object> coordinationAttributes) {
        this.coordinationAttributes.clear();
        if (coordinationAttributes != null) {
            this.coordinationAttributes.putAll(coordinationAttributes);
        }
        return this;
    }

    public OperationalMetadataBuilder putCoordinationAttribute(String key, Object value) {
        return coordinationAttribute(key, value);
    }

    public OperationalMetadataBuilder coordinationAttribute(String key, Object value) {
        if (key != null) {
            if (value == null) {
                this.coordinationAttributes.remove(key);
            } else {
                this.coordinationAttributes.put(key, value);
            }
        }
        return this;
    }

    public OperationalMetadataBuilder executionCoordination(Map<String, Object> executionCoordination) {
        this.executionCoordination.clear();
        if (executionCoordination != null) {
            this.executionCoordination.putAll(executionCoordination);
        }
        return this;
    }

    public OperationalMetadataBuilder putExecutionCoordination(String key, Object value) {
        if (key != null) {
            if (value == null) {
                this.executionCoordination.remove(key);
            } else {
                this.executionCoordination.put(key, value);
            }
        }
        return this;
    }

    public OperationalMetadataBuilder conveyanceOperations(Map<String, Object> conveyanceOperations) {
        this.conveyanceOperations.clear();
        if (conveyanceOperations != null) {
            this.conveyanceOperations.putAll(conveyanceOperations);
        }
        return this;
    }

    public OperationalMetadataBuilder putConveyanceOperation(String key, Object value) {
        if (key != null) {
            if (value == null) {
                this.conveyanceOperations.remove(key);
            } else {
                this.conveyanceOperations.put(key, value);
            }
        }
        return this;
    }

    public OperationalMetadataBuilder temporalObservations(Map<String, Object> temporalObservations) {
        this.temporalObservations.clear();
        if (temporalObservations != null) {
            this.temporalObservations.putAll(temporalObservations);
        }
        return this;
    }

    public OperationalMetadataBuilder putTemporalObservation(String key, Object value) {
        if (key != null) {
            if (value == null) {
                this.temporalObservations.remove(key);
            } else {
                this.temporalObservations.put(key, value);
            }
        }
        return this;
    }

    public OperationalMetadataBuilder evidenceReferences(List<String> evidenceReferences) {
        this.evidenceReferences.clear();
        if (evidenceReferences != null) {
            this.evidenceReferences.addAll(evidenceReferences);
        }
        return this;
    }

    public OperationalMetadataBuilder addEvidenceReference(String evidenceReference) {
        return evidenceReference(evidenceReference);
    }

    public OperationalMetadataBuilder evidenceReference(String evidenceReference) {
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
                evidenceReferences
        );
    }
}
