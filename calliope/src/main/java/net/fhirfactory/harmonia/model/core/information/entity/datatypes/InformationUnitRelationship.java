/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationFailureCategory;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.RelationshipRole;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.RelationshipType;

/**
 * One typed semantic link from an InformationUnit to an internal or external target.
 *
 * <p>Draft 002.005 §1.4 defines the shape {@code type}, {@code role} and
 * {@code target : URI}. The target must be an absolute URI, which supports
 * URN-based persistent identities ({@code urn:uuid:...}, {@code urn:oid:...})
 * as well as URLs; {@link #getTargetText()} gives its string form and
 * {@link #parse(RelationshipType, RelationshipRole, String)} reads it back.</p>
 *
 * <p>An {@code INTERNAL_IU} target must identify the IU by its UUID, in the
 * {@code urn:uuid:} form. The IU's classification-derived name
 * ({@code InformationUnit.getName()}) is a designation, not an identity, and
 * is rejected as a target. Calliope checks only this local structure. It does
 * not resolve targets, confirm that a remote target exists, or create
 * reciprocal links: Mneme validates local references where applicable and
 * preserves remote references as supplied. These semantic links are also
 * separate from {@code Content.reference}, which only locates persisted
 * Content.</p>
 */
public final class InformationUnitRelationship {
    private static final String UUID_URN_PREFIX = "urn:uuid:";

    private final RelationshipType type;
    private final RelationshipRole role;
    private final URI target;

    private InformationUnitRelationship(RelationshipType type, RelationshipRole role, URI target) {
        this.type = type;
        this.role = role;
        this.target = target;
    }

    /**
     * Creates a relationship to any supported target.
     *
     * @param type kind of target
     * @param role semantic role of the link
     * @param target absolute target URI; {@code urn:uuid:<uuid>} for {@code INTERNAL_IU}
     * @return an immutable relationship
     * @throws InformationModelException {@code MISSING_INPUT} for null arguments;
     *         {@code INVALID_INPUT} for a relative target or a malformed IU UUID URN
     */
    public static InformationUnitRelationship of(RelationshipType type, RelationshipRole role, URI target) {
        ModelChecks.required(type, "relationship.type");
        ModelChecks.required(role, "relationship.role");
        ModelChecks.required(target, "relationship.target");
        if (!target.isAbsolute()) {
            throw InformationModelException.of(InformationFailureCategory.INVALID_INPUT, "relationship.target");
        }
        if (type == RelationshipType.INTERNAL_IU && uuidOf(target) == null) {
            throw InformationModelException.of(InformationFailureCategory.INVALID_INPUT, "relationship.target");
        }
        return new InformationUnitRelationship(type, role, target);
    }

    /**
     * Parses a target from its string form and creates the relationship.
     *
     * @param type kind of target
     * @param role semantic role of the link
     * @param target absolute URI text
     * @return an immutable relationship
     * @throws InformationModelException {@code MISSING_INPUT} for null; {@code INVALID_INPUT} when
     *         the text is not a URI or violates {@link #of(RelationshipType, RelationshipRole, URI)}
     */
    public static InformationUnitRelationship parse(RelationshipType type, RelationshipRole role, String target) {
        ModelChecks.required(target, "relationship.target");
        try {
            return of(type, role, new URI(target));
        } catch (URISyntaxException malformed) {
            throw InformationModelException.of(InformationFailureCategory.INVALID_INPUT, "relationship.target");
        }
    }

    /**
     * Creates an {@code INTERNAL_IU} relationship to another IU identified by UUID.
     *
     * @param role semantic role of the link
     * @param informationUnitId the target IU's sole instance identifier
     * @return a relationship whose target is {@code urn:uuid:<id>}
     * @throws InformationModelException {@code MISSING_INPUT} for null arguments
     */
    public static InformationUnitRelationship toInformationUnit(RelationshipRole role, UUID informationUnitId) {
        ModelChecks.required(informationUnitId, "relationship.target");
        return of(RelationshipType.INTERNAL_IU, role, URI.create(UUID_URN_PREFIX + informationUnitId));
    }

    /**
     * Returns the kind of target.
     *
     * @return the kind of target
     */
    public RelationshipType getType() {
        return type;
    }

    /**
     * Returns the semantic role.
     *
     * @return the semantic role
     */
    public RelationshipRole getRole() {
        return role;
    }

    /**
     * Returns the absolute target URI, preserved as supplied.
     *
     * @return the absolute target URI, preserved as supplied
     */
    public URI getTarget() {
        return target;
    }

    /**
     * Returns the target's string representation, which {@link #parse} accepts.
     *
     * @return the target's string representation, which {@link #parse} accepts
     */
    public String getTargetText() {
        return target.toString();
    }

    /**
     * Returns the target IU's UUID for an {@code INTERNAL_IU} relationship.
     *
     * @return the UUID, or empty for every other relationship type
     */
    public Optional<UUID> getTargetInformationUnitId() {
        return type == RelationshipType.INTERNAL_IU ? Optional.of(uuidOf(target)) : Optional.empty();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof InformationUnitRelationship that)) {
            return false;
        }
        return type == that.type && role == that.role && target.equals(that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, role, target);
    }

    /** Omits the target, because external locators can identify people or records. */
    @Override
    public String toString() {
        return "InformationUnitRelationship{type=" + type + ", role=" + role + "}";
    }

    /** Returns the UUID of a well-formed {@code urn:uuid:} URI, or null. */
    private static UUID uuidOf(URI target) {
        String text = target.toString();
        if (text.length() != UUID_URN_PREFIX.length() + 36
                || !text.substring(0, UUID_URN_PREFIX.length()).toLowerCase(Locale.ROOT).equals(UUID_URN_PREFIX)) {
            return null;
        }
        String candidate = text.substring(UUID_URN_PREFIX.length());
        try {
            UUID parsed = UUID.fromString(candidate);
            return parsed.toString().equalsIgnoreCase(candidate) ? parsed : null;
        } catch (IllegalArgumentException malformed) {
            return null;
        }
    }
}
