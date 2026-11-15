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
 *   <li><b>Local Reference ({@code local = true}):</b> Identifies another Information Unit in Harmonia (by UUID or Topic).
 *       Mneme validates local reference acceptability under the applicable local-reference contract.</li>
 *   <li><b>Remote Reference ({@code local = false}):</b> Identifies an external resource (by URI or identifier).
 *       Mneme preserves the reference without assuming responsibility for validating remote availability.</li>
 *   <li>Relationship types (e.g. {@code relatesTo}, {@code partOf}, {@code basedOn}, {@code derivedFrom}) are illustrative
 *       semantic classifications supplied by the creator.</li>
 * </ul>
 * <p>
 * <b>Defensive Programming:</b>
 * <ul>
 *   <li>{@code relationshipType} and {@code targetIdentifier} must not be null or blank.</li>
 *   <li>Immutable value object design.</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class InformationUnitRelationship implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String TYPE_RELATES_TO = "relatesTo";
    public static final String TYPE_PART_OF = "partOf";
    public static final String TYPE_BASED_ON = "basedOn";
    public static final String TYPE_DERIVED_FROM = "derivedFrom";
    public static final String TYPE_REPLACES = "replaces";

    private final String relationshipType;
    private final String targetIdentifier;
    private final boolean local;
    private final String description;

    /**
     * Canonical constructor establishing validated relationship attributes.
     *
     * @param relationshipType the semantic type of relationship (e.g. "relatesTo", "partOf"), must not be null or blank
     * @param targetIdentifier target UUID, Topic, or URI, must not be null or blank
     * @param local            true if target is a local Harmonia IU, false if external/remote
     * @param description      optional human-readable description or annotation
     */
    @JsonCreator
    public InformationUnitRelationship(
            @JsonProperty("relationshipType") String relationshipType,
            @JsonProperty("targetIdentifier") String targetIdentifier,
            @JsonProperty("local") boolean local,
            @JsonProperty("description") String description) {
        if (relationshipType == null || relationshipType.isBlank()) {
            throw new IllegalArgumentException("relationshipType must not be null or blank");
        }
        if (targetIdentifier == null || targetIdentifier.isBlank()) {
            throw new IllegalArgumentException("targetIdentifier must not be null or blank");
        }
        this.relationshipType = relationshipType.trim();
        this.targetIdentifier = targetIdentifier.trim();
        this.local = local;
        this.description = description != null && !description.isBlank() ? description.trim() : null;
    }

    public InformationUnitRelationship(String relationshipType, String targetIdentifier) {
        this(relationshipType, targetIdentifier, true, null);
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

    /**
     * Full factory constructor.
     *
     * @param relationshipType relationship type
     * @param targetIdentifier target identifier
     * @param local            local vs remote
     * @param description      description
     * @return populated relationship
     */
    public static InformationUnitRelationship of(String relationshipType, String targetIdentifier, boolean local, String description) {
        return new InformationUnitRelationship(relationshipType, targetIdentifier, local, description);
    }

    // =========================================================================
    // Accessors
    // =========================================================================

    /**
     * Returns the semantic relationship type (e.g. "relatesTo", "partOf", "basedOn").
     *
     * @return the relationship type
     */
    @JsonProperty("relationshipType")
    public String relationshipType() {
        return relationshipType;
    }

    /**
     * Returns the target identifier (UUID, Topic, or URI).
     *
     * @return the target identifier
     */
    @JsonProperty("targetIdentifier")
    public String targetIdentifier() {
        return targetIdentifier;
    }

    /**
     * Returns whether this relationship references a local Harmonia Information Unit (true) or remote entity (false).
     *
     * @return true if local, false if remote
     */
    @JsonProperty("local")
    public boolean isLocal() {
        return local;
    }

    @JsonIgnore
    public boolean isRemote() {
        return !local;
    }

    /**
     * Returns the optional human-readable description of this relationship.
     *
     * @return optional description
     */
    @JsonIgnore
    public Optional<String> description() {
        return Optional.ofNullable(description);
    }

    /**
     * Returns the raw description string, or null if not set.
     *
     * @return description, or null
     */
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
        return local == that.local
                && relationshipType.equalsIgnoreCase(that.relationshipType)
                && targetIdentifier.equalsIgnoreCase(that.targetIdentifier)
                && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(relationshipType.toLowerCase(), targetIdentifier.toLowerCase(), local, description);
    }

    @Override
    public String toString() {
        return "InformationUnitRelationship{" +
                "type='" + relationshipType + '\'' +
                ", target='" + targetIdentifier + '\'' +
                ", local=" + local +
                (description != null ? ", description='" + description + '\'' : "") +
                '}';
    }
}
