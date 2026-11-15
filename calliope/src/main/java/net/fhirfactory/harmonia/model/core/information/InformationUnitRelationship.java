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
import net.fhirfactory.harmonia.model.topic.Topic;

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

    public static final Topic TYPE_RELATES_TO = Topic.fromName("Harmonia.Relationship.RelatesTo");
    public static final Topic TYPE_PART_OF = Topic.fromName("Harmonia.Relationship.PartOf");
    public static final Topic TYPE_BASED_ON = Topic.fromName("Harmonia.Relationship.BasedOn");
    public static final Topic TYPE_DERIVED_FROM = Topic.fromName("Harmonia.Relationship.DerivedFrom");
    public static final Topic TYPE_REPLACES = Topic.fromName("Harmonia.Relationship.Replaces");

    public static final String RELATES_TO = "relatesTo";
    public static final String PART_OF = "partOf";
    public static final String BASED_ON = "basedOn";
    public static final String DERIVED_FROM = "derivedFrom";
    public static final String REPLACES = "replaces";

    private final Topic type;
    private final String targetIdentifier;
    private final boolean local;
    private final String description;

    /**
     * Canonical constructor establishing validated relationship attributes with strongly typed Topic.
     *
     * @param type             the semantic type of relationship as a Topic, must not be null
     * @param targetIdentifier target UUID, Topic name, or URI, must not be null or blank
     * @param local            true if target is a local Harmonia IU, false if external/remote
     * @param description      optional human-readable description or annotation
     */
    public InformationUnitRelationship(
            Topic type,
            String targetIdentifier,
            boolean local,
            String description) {
        Objects.requireNonNull(type, "relationship type Topic must not be null");
        if (targetIdentifier == null || targetIdentifier.isBlank()) {
            throw new IllegalArgumentException("targetIdentifier must not be null or blank");
        }
        this.type = type;
        this.targetIdentifier = targetIdentifier.trim();
        this.local = local;
        this.description = description != null && !description.isBlank() ? description.trim() : null;
    }

    /**
     * Flexible creator handling both strongly-typed Topic and String relationship types.
     */
    @JsonCreator
    public InformationUnitRelationship(
            @JsonProperty("type") Topic type,
            @JsonProperty("relationshipType") String relationshipType,
            @JsonProperty("targetIdentifier") String targetIdentifier,
            @JsonProperty("local") boolean local,
            @JsonProperty("description") String description) {
        if (type != null) {
            this.type = type;
        } else if (relationshipType != null && !relationshipType.isBlank()) {
            this.type = mapStringToTopic(relationshipType);
        } else {
            throw new IllegalArgumentException("relationship type must not be null or blank");
        }

        if (targetIdentifier == null || targetIdentifier.isBlank()) {
            throw new IllegalArgumentException("targetIdentifier must not be null or blank");
        }
        this.targetIdentifier = targetIdentifier.trim();
        this.local = local;
        this.description = description != null && !description.isBlank() ? description.trim() : null;
    }

    public InformationUnitRelationship(
            String relationshipType,
            String targetIdentifier,
            boolean local,
            String description) {
        this(null, relationshipType, targetIdentifier, local, description);
    }

    public InformationUnitRelationship(Object relationshipTypeOrTopic, String targetIdentifier) {
        this(
                relationshipTypeOrTopic instanceof Topic t ? t : null,
                relationshipTypeOrTopic instanceof String s ? s : null,
                targetIdentifier,
                true,
                null
        );
    }

    private static Topic mapStringToTopic(String relType) {
        String trimmed = relType.trim();
        return switch (trimmed) {
            case "relatesTo", "RelatesTo" -> TYPE_RELATES_TO;
            case "partOf", "PartOf" -> TYPE_PART_OF;
            case "basedOn", "BasedOn" -> TYPE_BASED_ON;
            case "derivedFrom", "DerivedFrom" -> TYPE_DERIVED_FROM;
            case "replaces", "Replaces" -> TYPE_REPLACES;
            default -> Topic.fromName(trimmed.contains(".") ? trimmed : "Harmonia.Relationship." + trimmed);
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
        return new InformationUnitRelationship(relationshipType, targetIdentifier, true, null);
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
        return new InformationUnitRelationship(relationshipType, targetUuid.toString(), true, null);
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
        return new InformationUnitRelationship(relationshipType, targetUri, false, null);
    }

    public static InformationUnitRelationship remote(Topic relationshipType, String targetUri) {
        return new InformationUnitRelationship(relationshipType, targetUri, false, null);
    }

    public static InformationUnitRelationship of(String relationshipType, String targetIdentifier, boolean local, String description) {
        return new InformationUnitRelationship(relationshipType, targetIdentifier, local, description);
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
     * Returns the semantic relationship type name (e.g. "relatesTo", "partOf").
     *
     * @return the relationship type name
     */
    @JsonProperty("relationshipType")
    public String relationshipType() {
        if (TYPE_RELATES_TO.equals(type)) return RELATES_TO;
        if (TYPE_PART_OF.equals(type)) return PART_OF;
        if (TYPE_BASED_ON.equals(type)) return BASED_ON;
        if (TYPE_DERIVED_FROM.equals(type)) return DERIVED_FROM;
        if (TYPE_REPLACES.equals(type)) return REPLACES;
        return type.getName();
    }

    public String getRelationshipType() {
        return relationshipType();
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
                "type=" + type +
                ", targetIdentifier='" + targetIdentifier + '\'' +
                ", local=" + local +
                ", description='" + description + '\'' +
                '}';
    }
}
