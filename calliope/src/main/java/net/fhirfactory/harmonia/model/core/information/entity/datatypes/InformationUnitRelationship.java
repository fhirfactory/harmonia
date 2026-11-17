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

import net.fhirfactory.harmonia.model.core.naming.factory.TopicFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationUnitValueSets;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Encapsulates an optional creator- or updater-supplied relationship between this Information Unit and another
 * Information Unit (local reference) or an external entity (remote reference).
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with {@code DRP-Information-Unit-Framework.Final.001.md} (§14; IU-07):
 * <ul>
 *   <li>Optional IU-to-IU or IU-to-external-resource relationships are attributes or references carried within
 *       the IU when supplied by its creator or authorised updater.</li>
 *   <li>The Information Unit is <b>not a graph coordinator</b>: it does not automatically track derived copies,
 *       generate reciprocal links, or synchronise referents.</li>
 *   <li><b>Topic-Valued Classification:</b> In accordance with the fundamental Harmonia architectural principle,
 *       relationship types (e.g. {@code RelatesTo}, {@code PartOf}, {@code BasedOn}, {@code DerivedFrom}, {@code Replaces})
 *       are represented as strongly typed {@link Topic} instances.</li>
 *   <li><b>Local Reference ({@code local = true}):</b> Identifies another Information Unit in Harmonia (by UUID or Topic).
 *       Mneme validates local reference acceptability under the applicable local-reference contract.</li>
 *   <li><b>Remote Reference ({@code local = false}):</b> Identifies an external resource (by URI or identifier).
 *       Mneme preserves the reference without assuming responsibility for validating remote availability.</li>
 * </ul>
 * <p>
 * <b>Defensive Programming:</b>
 * <ul>
 *   <li>Relationship type Topic and targetIdentifier must not be null or blank.</li>
 *   <li>Immutable value object design.</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class InformationUnitRelationship implements Serializable {

    private static final long serialVersionUID = 1L;


    private final Topic type;
    private final String targetIdentifier;
    private final boolean local;
    private final String description;

    /** Creates a relationship whose semantic type is a governed Topic. */
    public InformationUnitRelationship(
            @JsonProperty("type") Topic type,
            @JsonProperty("targetIdentifier") String targetIdentifier,
            @JsonProperty("local") boolean local,
            @JsonProperty("description") String description) {
        if (type == null) {
            throw new IllegalArgumentException("relationship type Topic must not be null");
        }
        if (targetIdentifier == null || targetIdentifier.isBlank()) {
            throw new IllegalArgumentException("targetIdentifier must not be null or blank");
        }
        this.type = type;
        this.targetIdentifier = targetIdentifier.trim();
        this.local = local;
        this.description = description != null && !description.isBlank() ? description.trim() : null;
    }

    /** Creates a local relationship to the supplied target. */
    public InformationUnitRelationship(Topic type, String targetIdentifier) {
        this(type, targetIdentifier, true, null);
    }

    /**
     * Reconstructs both the canonical typed relationship and the historical string projection.
     *
     * @param type canonical Topic-valued relationship type
     * @param relationshipType legacy string-valued relationship projection
     * @param targetIdentifier target identity or external URI
     * @param local whether the target is local to Harmonia
     * @param description optional human-readable annotation
     * @return validated relationship with a typed Topic
     */
    @JsonCreator
    public static InformationUnitRelationship fromJson(
            @JsonProperty("type") Topic type,
            @JsonProperty("relationshipType") String relationshipType,
            @JsonProperty("targetIdentifier") String targetIdentifier,
            @JsonProperty("local") boolean local,
            @JsonProperty("description") String description) {
        Topic resolvedType = type;
        if (resolvedType == null && relationshipType != null && !relationshipType.isBlank()) {
            resolvedType = mapStringToTopic(relationshipType);
        }
        if (resolvedType == null) {
            throw new IllegalArgumentException("relationship type must not be null or blank");
        }
        return new InformationUnitRelationship(resolvedType, targetIdentifier, local, description);
    }

    private static Topic mapStringToTopic(String relType) {
        String trimmed = relType.trim();
        return switch (trimmed) {
            case InformationUnitValueSets.RELATES_TO, "RelatesTo" -> InformationUnitValueSets.TYPE_RELATES_TO;
            case InformationUnitValueSets.PART_OF, "PartOf" -> InformationUnitValueSets.TYPE_PART_OF;
            case InformationUnitValueSets.BASED_ON, "BasedOn" -> InformationUnitValueSets.TYPE_BASED_ON;
            case InformationUnitValueSets.DERIVED_FROM, "DerivedFrom" -> InformationUnitValueSets.TYPE_DERIVED_FROM;
            case InformationUnitValueSets.REPLACES, "Replaces" -> InformationUnitValueSets.TYPE_REPLACES;
            default -> TopicFactory.fromName(trimmed.contains(".") ? trimmed : "Harmonia.Relationship." + trimmed);
        };
    }

    /**
     * Creates a local relationship to another Information Unit by target identifier string (UUID or Topic).
     *
     * @param relationshipType semantic relationship type
     * @param targetIdentifier target UUID or Topic string
     * @return populated local relationship
     */
    public static InformationUnitRelationship local(String relationshipType, String targetIdentifier) {
        return local(mapStringToTopic(relationshipType), targetIdentifier);
    }

    public static InformationUnitRelationship local(Topic relationshipType, String targetIdentifier) {
        return new InformationUnitRelationship(relationshipType, targetIdentifier, true, null);
    }

    /**
     * Creates a local relationship to another Information Unit by target UUID.
     *
     * @param relationshipType semantic relationship type
     * @param targetUuid       target UUID
     * @return populated local relationship
     */
    public static InformationUnitRelationship local(String relationshipType, UUID targetUuid) {
        Objects.requireNonNull(targetUuid, "targetUuid must not be null");
        return local(mapStringToTopic(relationshipType), targetUuid.toString());
    }

    public static InformationUnitRelationship local(Topic relationshipType, UUID targetUuid) {
        Objects.requireNonNull(targetUuid, "targetUuid must not be null");
        return new InformationUnitRelationship(relationshipType, targetUuid.toString(), true, null);
    }

    /**
     * Creates a remote relationship to an external resource by URI or external identifier.
     *
     * @param relationshipType semantic relationship type
     * @param targetUri        external URI or identifier
     * @return populated remote relationship
     */
    public static InformationUnitRelationship remote(String relationshipType, String targetUri) {
        return remote(mapStringToTopic(relationshipType), targetUri);
    }

    public static InformationUnitRelationship remote(Topic relationshipType, String targetUri) {
        return new InformationUnitRelationship(relationshipType, targetUri, false, null);
    }

    public static InformationUnitRelationship of(String relationshipType, String targetIdentifier, boolean local, String description) {
        return of(mapStringToTopic(relationshipType), targetIdentifier, local, description);
    }

    public static InformationUnitRelationship of(Topic relationshipType, String targetIdentifier, boolean local, String description) {
        return new InformationUnitRelationship(relationshipType, targetIdentifier, local, description);
    }

    // =========================================================================
    // Accessors
    // =========================================================================

    /**
     * Returns the strongly typed {@link Topic} classifying the relationship.
     *
     * @return the relationship type Topic, never null
     */
    @JsonProperty("type")
    public Topic type() {
        return type;
    }

    public Topic getType() {
        return type;
    }


    /**
     * Returns the target identifier (UUID, Topic, or URI).
     *
     * @return the target identifier, never null
     */
    @JsonProperty("targetIdentifier")
    public String targetIdentifier() {
        return targetIdentifier;
    }

    public String getTargetIdentifier() {
        return targetIdentifier;
    }

    /**
     * Attempts to parse the target identifier as a UUID.
     *
     * @return optional parsed UUID
     */
    @JsonIgnore
    public Optional<UUID> targetUuid() {
        try {
            return Optional.of(UUID.fromString(targetIdentifier));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    /**
     * Returns true if this relationship targets a local Harmonia Information Unit.
     *
     * @return true for local, false for remote
     */
    @JsonProperty("local")
    public boolean local() {
        return local;
    }

    public boolean isLocal() {
        return local;
    }

    public boolean isRemote() {
        return !local;
    }

    /**
     * Returns the human-readable description or annotation, if present.
     *
     * @return optional description
     */
    @JsonIgnore
    public Optional<String> description() {
        return Optional.ofNullable(description);
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    // =========================================================================
    // Standard Object Methods
    // =========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InformationUnitRelationship that = (InformationUnitRelationship) o;
        return local == that.local &&
                Objects.equals(type, that.type) &&
                Objects.equals(targetIdentifier, that.targetIdentifier) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, targetIdentifier, local, description);
    }

    @Override
    public String toString() {
        return "InformationUnitRelationship{" +
                "typeId=" + type.getShortId() +
                ", local=" + local +
                ", hasTarget=" + (targetIdentifier != null) +
                ", hasDescription=" + (description != null) +
                '}';
    }
}
