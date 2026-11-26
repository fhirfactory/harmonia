/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity;

import java.util.Objects;
import java.util.UUID;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.CacheLifecycleMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationCoordinates;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationModelException;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationQuality;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationRepresentation;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationUnitRelationship;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ModelChecks;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.OriginMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.PersistenceLifecycleMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.Relationships;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.SecurityMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationFailureCategory;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.RelationshipRole;
import net.fhirfactory.harmonia.model.core.naming.Topic;

/**
 * Harmonia's atomic, individually governed unit of information (IU).
 *
 * <h2>What an instance is</h2>
 * <p>An {@code InformationUnit} object is an immutable <em>snapshot</em> of
 * one IU at one governed revision. It is composed as agreed in Draft 002.007
 * §1:</p>
 * <pre>
 * InformationUnit
 * ├── id : UUID                    sole instance identifier, immutable
 * ├── informationConcept : Topic   classification, immutable (replaces ConceptFDN)
 * ├── description : String
 * ├── InformationQuality           lifetime-immutable
 * ├── InformationMetadata          lifecycle, origin, coordinates, security,
 * │                                relationships, informationAuthority
 * └── InformationRepresentation    exactly one per IU, preserved by revisions
 * </pre>
 *
 * <h2>Identity and name</h2>
 * <p>{@link #getId()} is the only instance identity. {@link #getName()} is a
 * designation, {@code informationConcept.getName() + id}, with no separator;
 * it is not an identity and must not be used as a relationship target. The
 * qualified {@link #getInformationConcept()} Topic is the value a Store
 * matches against its supported TopicMasks (MI-05).</p>
 *
 * <h2>Controlled change</h2>
 * <p>Every change returns a new snapshot and leaves this one untouched, so
 * earlier revisions (for example earlier coordinates) remain available to
 * whoever holds them:</p>
 * <ul>
 *   <li>{@link #reviseCoordinates(InformationCoordinates)} and
 *       {@link #reviseInformationAuthority(Topic)} - governed revisions under
 *       the same UUID that retain the originating Managed Revision as the
 *       expected revision for a subsequent write;</li>
 *   <li>{@code with...} methods - lifecycle, origin, security, relationship and
 *       description updates; Themis-Krites adjudicates security changes outside
 *       Calliope;</li>
 *   <li>{@link #deriveSuccessorAsClone(UUID, RelationshipRole)} copies the
 *       complete IU data and non-lifecycle metadata while adding provenance;</li>
 *   <li>{@link #deriveSuccessorWithMinimumMetadata(UUID, RelationshipRole)} starts
 *       a caller-built IU that inherits only origin and security besides provenance.</li>
 * </ul>
 * <p>There is intentionally no method that replaces the identity, concept,
 * quality or representation of an existing IU.</p>
 *
 * <h2>What this class does not do</h2>
 * <p>It does not authorise operations (Themis), adjudicate security proposals
 * (Themis-Krites), persist or cache anything (Mnemosyne and Mneme), resolve
 * references, build Summaries, or serialise itself. Instances are safe to
 * share between threads; {@link #toString()} reveals only the UUID.</p>
 */
public final class InformationUnit {
    private final UUID id;
    private final Topic informationConcept;
    private final String description;
    private final InformationQuality quality;
    private final InformationMetadata metadata;
    private final InformationRepresentation representation;

    /** Captures values already validated by {@link InformationUnitBuilder#build()}. */
    InformationUnit(UUID id, Topic informationConcept, String description, InformationQuality quality,
                    InformationMetadata metadata, InformationRepresentation representation) {
        this.id = id;
        this.informationConcept = informationConcept;
        this.description = description;
        this.quality = quality;
        this.metadata = metadata;
        this.representation = representation;
    }

    /**
     * Returns a new, caller-confined builder.
     *
     * @return an empty builder
     */
    public static InformationUnitBuilder builder() {
        return new InformationUnitBuilder();
    }

    /**
     * Returns the IU's sole, immutable instance identifier.
     *
     * @return the IU's sole, immutable instance identifier
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the immutable qualified classification Topic, used for MI-05 mask matching.
     *
     * @return the immutable qualified classification Topic, used for MI-05 mask matching
     */
    public Topic getInformationConcept() {
        return informationConcept;
    }

    /**
     * Returns the free-text description; may be empty, never null.
     *
     * @return the free-text description; may be empty, never null
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the current designation: the concept's raw name followed directly by the UUID.
     *
     * <p>For concept {@code System=Harmonia.Concept=Observation} and UUID
     * {@code 0f...}, the result is {@code Harmonia.Observation0f...}. Qualifiers
     * and entry versions never appear.</p>
     *
     * @return {@code informationConcept.getName() + id.toString()}
     */
    public String getName() {
        return informationConcept.getName() + id;
    }

    /**
     * Returns the lifetime-immutable quality assessment.
     *
     * @return the lifetime-immutable quality assessment
     */
    public InformationQuality getQuality() {
        return quality;
    }

    /**
     * Returns the governed metadata of this revision.
     *
     * @return the governed metadata of this revision
     */
    public InformationMetadata getMetadata() {
        return metadata;
    }

    /**
     * Returns the single governed representation.
     *
     * @return the single governed representation
     */
    public InformationRepresentation getRepresentation() {
        return representation;
    }

    /**
     * Returns a new local revision candidate with different coordinates and the same UUID.
     *
     * <p>Only coordinates change. The originating Managed Revision is retained
     * as the expected revision for the subsequent governed write. Mnemosyne
     * establishes the successor Managed Revision only after persistence
     * succeeds. Disposition, legal hold, cache, origin, security, relationships,
     * quality and representation are carried forward unchanged. This snapshot
     * keeps its coordinates.</p>
     *
     * @param coordinates coordinates of the new revision
     * @return the new revision snapshot
     * @throws InformationModelException {@code MISSING_INPUT} when null
     */
    public InformationUnit reviseCoordinates(InformationCoordinates coordinates) {
        return withMetadata(metadata.forCoordinateRevision(coordinates));
    }

    /**
     * Returns a new governed revision with a different information authority and the same UUID.
     *
     * <p>The existing snapshot and its authority remain unchanged. The
     * originating Managed Revision is retained as the expected revision for
     * the subsequent governed write. This method carries a proposed authority
     * value only; it does not determine whether the change is authorized.</p>
     *
     * @param informationAuthority authority Topic for the proposed revision
     * @return the new revision snapshot, or this snapshot for an identical versioned Topic
     * @throws InformationModelException {@code MISSING_INPUT} when null
     */
    public InformationUnit reviseInformationAuthority(Topic informationAuthority) {
        InformationMetadata revisedMetadata = metadata.forAuthorityRevision(informationAuthority);
        return revisedMetadata == metadata ? this : withMetadata(revisedMetadata);
    }

    /**
     * Records Mnemosyne-established persistence lifecycle facts.
     *
     * @param persistenceLifecycle facts that do not regress the Managed Revision
     * @return the updated snapshot
     * @throws InformationModelException {@code MISSING_INPUT} or {@code IMMUTABILITY_VIOLATION}
     */
    public InformationUnit withPersistenceLifecycle(PersistenceLifecycleMetadata persistenceLifecycle) {
        return withMetadata(metadata.withPersistenceLifecycle(persistenceLifecycle));
    }

    /**
     * Records Mneme cache lifecycle facts.
     *
     * @param cacheLifecycle cache state and Active Generation
     * @return the updated snapshot
     * @throws InformationModelException {@code MISSING_INPUT} when null
     */
    public InformationUnit withCacheLifecycle(CacheLifecycleMetadata cacheLifecycle) {
        return withMetadata(metadata.withCacheLifecycle(cacheLifecycle));
    }

    /**
     * Establishes previously unknown origin facts.
     *
     * @param origin origin that keeps every established fact
     * @return the updated snapshot
     * @throws InformationModelException {@code MISSING_INPUT} or {@code IMMUTABILITY_VIOLATION}
     */
    public InformationUnit withOrigin(OriginMetadata origin) {
        return withMetadata(metadata.withOrigin(origin));
    }

    /**
     * Applies a proposed immutable set of security requirements.
     *
     * <p>Themis-Krites adjudicates all proposed changes, including strengthening,
     * weakening and replacement. The caller must obtain and apply that
     * disposition; this model does not make the security-policy decision.</p>
     *
     * @param security requirements approved for the update
     * @return the updated snapshot
     * @throws InformationModelException {@code MISSING_INPUT} when null
     */
    public InformationUnit withSecurity(SecurityMetadata security) {
        return withMetadata(metadata.withSecurity(security));
    }

    /**
     * Replaces the semantic relationships supplied by the creator or an authorised updater.
     *
     * @param relationships new relationship list
     * @return the updated snapshot
     * @throws InformationModelException {@code MISSING_INPUT} when null
     */
    public InformationUnit withRelationships(Relationships relationships) {
        return withMetadata(metadata.withRelationships(relationships));
    }

    /**
     * Replaces the free-text description.
     *
     * @param description new description; may be empty
     * @return the updated snapshot
     * @throws InformationModelException {@code MISSING_INPUT} when null
     */
    public InformationUnit withDescription(String description) {
        ModelChecks.required(description, "description");
        return new InformationUnit(id, informationConcept, description, quality, metadata, representation);
    }

    /**
     * Creates a new IU by copying this complete snapshot and adding provenance.
     *
     * <p>The new identity receives this IU's concept, description, quality,
     * representation, authority, coordinates, origin, security and all
     * existing relationships. One {@code INTERNAL_IU} provenance relationship
     * to this source is appended. Persistence and cache lifecycle metadata are
     * reinitialised because they describe the source manifestation rather than
     * the new IU. The source remains unchanged.</p>
     *
     * @param successorId UUID for the new IU; must differ from this IU's UUID
     * @param role provenance role, normally {@link RelationshipRole#DERIVED_FROM}
     * @return the complete immutable cloned IU
     * @throws InformationModelException {@code MISSING_INPUT} for null arguments;
     *         {@code INCONSISTENT_INPUT} when the UUID is not new or an inherited relationship
     *         would target the successor itself
     */
    public InformationUnit deriveSuccessorAsClone(UUID successorId, RelationshipRole role) {
        requireNewSuccessorIdentity(successorId, role);
        boolean inheritedSelfReference = metadata.getRelationships().getRelationships().stream()
                .map(InformationUnitRelationship::getTargetInformationUnitId)
                .anyMatch(target -> target.filter(successorId::equals).isPresent());
        if (inheritedSelfReference) {
            throw InformationModelException.of(InformationFailureCategory.INCONSISTENT_INPUT, "derivation.id");
        }
        InformationUnitRelationship provenance = InformationUnitRelationship.toInformationUnit(role, id);
        InformationMetadata clonedMetadata = metadata.forClonedSuccessor(provenance);
        return builder().id(successorId).informationConcept(informationConcept).description(description)
                .quality(quality).metadata(clonedMetadata).representation(representation).build();
    }

    /**
     * Starts building a new IU with only the source origin, security and provenance inherited.
     *
     * <p>The caller must provide a new concept, description, quality,
     * information authority and complete representation. Coordinates are
     * optional and default to the unplaced value. Existing relationships are
     * deliberately not inherited; the builder installs only the provenance
     * link to this source. Persistence and cache lifecycle metadata start in
     * their unestablished states, while the established origin and security
     * requirements are carried forward unchanged.</p>
     *
     * <p>Use this operation when a transformed or validated IU is not a clone
     * and the caller must explicitly establish its remaining semantic data.
     * A failed {@link MinimumMetadataSuccessorBuilder#build()} leaves that
     * caller-confined builder intact for correction and retry.</p>
     *
     * @param successorId UUID for the new IU; must differ from this IU's UUID
     * @param role provenance role, normally {@link RelationshipRole#DERIVED_FROM}
     * @return a builder whose inherited origin, security and provenance cannot be overridden
     * @throws InformationModelException {@code MISSING_INPUT} for null arguments;
     *         {@code INCONSISTENT_INPUT} when the UUID is not new
     */
    public MinimumMetadataSuccessorBuilder deriveSuccessorWithMinimumMetadata(UUID successorId,
                                                                                RelationshipRole role) {
        requireNewSuccessorIdentity(successorId, role);
        InformationUnitRelationship provenance = InformationUnitRelationship.toInformationUnit(role, id);
        return new MinimumMetadataSuccessorBuilder(successorId, metadata, provenance);
    }

    /** Compares complete snapshots, including Topic entry versions of the concept. */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof InformationUnit that)) {
            return false;
        }
        return id.equals(that.id) && ModelChecks.sameVersionedTopic(informationConcept, that.informationConcept)
                && description.equals(that.description) && quality.equals(that.quality)
                && metadata.equals(that.metadata) && representation.equals(that.representation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, informationConcept, description, quality, metadata, representation);
    }

    /** Reveals only the UUID; descriptions, Content and metadata values can be sensitive. */
    @Override
    public String toString() {
        return "InformationUnit{id=" + id + "}";
    }

    private InformationUnit withMetadata(InformationMetadata updated) {
        return new InformationUnit(id, informationConcept, description, quality, updated, representation);
    }

    /** Checks inputs common to the two new-identity derivation operations. */
    private void requireNewSuccessorIdentity(UUID successorId, RelationshipRole role) {
        ModelChecks.required(successorId, "derivation.id");
        ModelChecks.required(role, "derivation.role");
        if (successorId.equals(id)) {
            throw InformationModelException.of(InformationFailureCategory.INCONSISTENT_INPUT, "derivation.id");
        }
    }
}
