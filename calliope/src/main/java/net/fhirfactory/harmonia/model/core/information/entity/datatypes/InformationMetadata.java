/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import java.util.Objects;
import net.fhirfactory.harmonia.model.core.naming.Topic;

/**
 * The governed metadata of one InformationUnit revision.
 *
 * <p>Draft 002.005 §1 composes six always-initialised containers -
 * {@link PersistenceLifecycleMetadata}, {@link CacheLifecycleMetadata},
 * {@link OriginMetadata}, {@link InformationCoordinates},
 * {@link SecurityMetadata} and {@link Relationships} - and, by Solution
 * Architect disposition recorded in software HCRL 0.1.0/011, a direct
 * {@code informationAuthority : Topic}. That Topic is the IU's explicit
 * reference to the applicable governed information authority under AX-06.
 * It is deliberately separate from {@link InformationQuality}: a
 * {@code CANONICAL} grade says nothing about authority.</p>
 *
 * <p>{@link #initial(Topic)} creates every container in its "unknown" state.
 * The {@code with...} methods return new values and enforce the structural
 * rules that belong to the data itself:</p>
 * <ul>
 *   <li>persistence - a local update retains the originating Managed Revision
 *       as the expected revision for the next governed write;</li>
 *   <li>origin - established facts are never removed or changed;</li>
 *   <li>security - values are immutable, while Krites adjudicates proposed
 *       changes outside Calliope.</li>
 * </ul>
 * <p>Coordinates have no "with" method here, because a coordinate change is a
 * governed revision and is applied through {@code InformationUnit}. The
 * authority Topic changes are made only through
 * {@link #forAuthorityRevision(Topic)}, which returns a new metadata snapshot
 * for the same IU identity.</p>
 */
public final class InformationMetadata {
    private final PersistenceLifecycleMetadata persistenceLifecycle;
    private final CacheLifecycleMetadata cacheLifecycle;
    private final OriginMetadata origin;
    private final InformationCoordinates coordinates;
    private final SecurityMetadata security;
    private final Relationships relationships;
    private final Topic informationAuthority;

    private InformationMetadata(PersistenceLifecycleMetadata persistenceLifecycle,
                                CacheLifecycleMetadata cacheLifecycle, OriginMetadata origin,
                                InformationCoordinates coordinates, SecurityMetadata security,
                                Relationships relationships, Topic informationAuthority) {
        this.persistenceLifecycle = persistenceLifecycle;
        this.cacheLifecycle = cacheLifecycle;
        this.origin = origin;
        this.coordinates = coordinates;
        this.security = security;
        this.relationships = relationships;
        this.informationAuthority = informationAuthority;
    }

    /**
     * Creates metadata with every container initialised to its unknown state.
     *
     * @param informationAuthority the applicable governed information authority (AX-06)
     * @return initialised metadata
     * @throws InformationModelException {@code MISSING_INPUT} when the authority is null
     */
    public static InformationMetadata initial(Topic informationAuthority) {
        return new InformationMetadata(PersistenceLifecycleMetadata.notEstablished(),
                CacheLifecycleMetadata.notCached(), OriginMetadata.unknown(), InformationCoordinates.unplaced(),
                SecurityMetadata.none(), Relationships.none(),
                ModelChecks.required(informationAuthority, "metadata.informationAuthority"));
    }

    /**
     * Returns metadata whose persistence lifecycle is replaced, without regression.
     *
     * @param proposed new persistence lifecycle facts
     * @return new metadata
     * @throws InformationModelException {@code MISSING_INPUT} when null;
     *         {@code IMMUTABILITY_VIOLATION} when the Managed Revision would regress or disappear
     */
    public InformationMetadata withPersistenceLifecycle(PersistenceLifecycleMetadata proposed) {
        persistenceLifecycle.requireNoRegressionBy(proposed);
        return new InformationMetadata(proposed, cacheLifecycle, origin, coordinates, security, relationships,
                informationAuthority);
    }

    /**
     * Returns metadata whose cache lifecycle is replaced.
     *
     * @param proposed new cache lifecycle facts supplied by Mneme
     * @return new metadata
     * @throws InformationModelException {@code MISSING_INPUT} when null
     */
    public InformationMetadata withCacheLifecycle(CacheLifecycleMetadata proposed) {
        ModelChecks.required(proposed, "cache");
        return new InformationMetadata(persistenceLifecycle, proposed, origin, coordinates, security,
                relationships, informationAuthority);
    }

    /**
     * Returns metadata whose origin is replaced, keeping established facts.
     *
     * @param proposed origin that may add, but not change, facts
     * @return new metadata
     * @throws InformationModelException {@code MISSING_INPUT} when null;
     *         {@code IMMUTABILITY_VIOLATION} when an established fact differs
     */
    public InformationMetadata withOrigin(OriginMetadata proposed) {
        origin.requirePreservedBy(proposed);
        return new InformationMetadata(persistenceLifecycle, cacheLifecycle, proposed, coordinates, security,
                relationships, informationAuthority);
    }

    /**
     * Returns metadata carrying a proposed immutable security value.
     *
     * <p>Themis-Krites is the authority for adjudicating all proposed
     * {@link SecurityMetadata} changes, including strengthening, weakening and
     * replacement. Calliope does not classify security policy or decide
     * whether a proposal is acceptable; callers must obtain the applicable
     * Krites disposition before applying a change.</p>
     *
     * @param proposed security value approved for this update
     * @return new metadata
     * @throws InformationModelException {@code MISSING_INPUT} when null
     */
    public InformationMetadata withSecurity(SecurityMetadata proposed) {
        ModelChecks.required(proposed, "security");
        return new InformationMetadata(persistenceLifecycle, cacheLifecycle, origin, coordinates, proposed,
                relationships, informationAuthority);
    }

    /**
     * Returns metadata whose relationships are replaced.
     *
     * @param proposed new relationship list supplied by the creator or an authorised updater
     * @return new metadata
     * @throws InformationModelException {@code MISSING_INPUT} when null
     */
    public InformationMetadata withRelationships(Relationships proposed) {
        ModelChecks.required(proposed, "relationships");
        return new InformationMetadata(persistenceLifecycle, cacheLifecycle, origin, coordinates, security,
                proposed, informationAuthority);
    }

    /**
     * Returns metadata for a local revision candidate with the given coordinates.
     *
     * <p>Used by {@code InformationUnit.reviseCoordinates}. The originating
     * Managed Revision is retained as the expected revision for the subsequent
     * governed write; Mnemosyne establishes the successor Managed Revision only
     * after persistence succeeds. Every other metadata container is carried
     * forward unchanged.</p>
     *
     * @param proposed coordinates for the new revision
     * @return new metadata
     * @throws InformationModelException {@code MISSING_INPUT} when null
     */
    public InformationMetadata forCoordinateRevision(InformationCoordinates proposed) {
        ModelChecks.required(proposed, "coordinates");
        return new InformationMetadata(persistenceLifecycle, cacheLifecycle, origin, proposed,
                security, relationships, informationAuthority);
    }

    /**
     * Returns metadata for a new governed revision with a different information authority.
     *
     * <p>The current snapshot remains unchanged. This method only carries the
     * proposed authority into a replacement value; the caller is responsible
     * for the applicable governance and authorization checks. Re-stating the
     * same versioned Topic is idempotent and returns this value.</p>
     *
     * @param proposed authority Topic for the new revision
     * @return this metadata for an identical authority, otherwise a new snapshot
     * @throws InformationModelException {@code MISSING_INPUT} when null
     */
    public InformationMetadata forAuthorityRevision(Topic proposed) {
        ModelChecks.required(proposed, "metadata.informationAuthority");
        if (ModelChecks.sameVersionedTopic(informationAuthority, proposed)) {
            return this;
        }
        return new InformationMetadata(persistenceLifecycle, cacheLifecycle, origin, coordinates, security,
                relationships, proposed);
    }

    /**
     * Returns metadata for a clone successor, preserving non-lifecycle facts and existing links.
     *
     * <p>A clone has a new identity, so persistence and cache lifecycle facts
     * are reinitialised. Established origin, coordinates, security and
     * authority remain the same; the supplied provenance link is appended to
     * the ordered relationships rather than replacing them.</p>
     *
     * @param provenance provenance relationship to the source IU
     * @return metadata for the complete clone successor
     * @throws InformationModelException {@code MISSING_INPUT} when the link is null
     */
    public InformationMetadata forClonedSuccessor(InformationUnitRelationship provenance) {
        ModelChecks.required(provenance, "relationships");
        return new InformationMetadata(PersistenceLifecycleMetadata.notEstablished(),
                CacheLifecycleMetadata.notCached(), origin, coordinates, security,
                relationships.with(provenance), informationAuthority);
    }

    /**
     * Returns fresh-lifecycle metadata for a minimum-metadata successor.
     *
     * <p>Only immutable origin and security facts are inherited from this
     * source. Coordinates and authority are explicit caller inputs, and the
     * successor starts with only its source-provenance relationship. Existing
     * source relationships are intentionally excluded.</p>
     *
     * @param provenance provenance relationship to the source IU
     * @param proposedAuthority required authority for the successor
     * @param proposedCoordinates coordinates for the successor
     * @return complete metadata for the successor
     * @throws InformationModelException {@code MISSING_INPUT} when any argument is null
     */
    public InformationMetadata forMinimumMetadataSuccessor(InformationUnitRelationship provenance,
                                                           Topic proposedAuthority,
                                                           InformationCoordinates proposedCoordinates) {
        ModelChecks.required(provenance, "relationships");
        ModelChecks.required(proposedAuthority, "informationAuthority");
        ModelChecks.required(proposedCoordinates, "coordinates");
        Relationships provenanceOnly = Relationships.none().with(provenance);
        return new InformationMetadata(PersistenceLifecycleMetadata.notEstablished(),
                CacheLifecycleMetadata.notCached(), origin, proposedCoordinates, security, provenanceOnly,
                proposedAuthority);
    }

    /**
     * Returns the Mnemosyne-governed persistence lifecycle.
     *
     * @return the Mnemosyne-governed persistence lifecycle
     */
    public PersistenceLifecycleMetadata getPersistenceLifecycle() {
        return persistenceLifecycle;
    }

    /**
     * Returns the Mneme-governed cache lifecycle.
     *
     * @return the Mneme-governed cache lifecycle
     */
    public CacheLifecycleMetadata getCacheLifecycle() {
        return cacheLifecycle;
    }

    /**
     * Returns the origin facts.
     *
     * @return the origin facts
     */
    public OriginMetadata getOrigin() {
        return origin;
    }

    /**
     * Returns the coordinates of this revision.
     *
     * @return the coordinates of this revision
     */
    public InformationCoordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Returns the security requirements.
     *
     * @return the security requirements
     */
    public SecurityMetadata getSecurity() {
        return security;
    }

    /**
     * Returns the semantic relationships.
     *
     * @return the semantic relationships
     */
    public Relationships getRelationships() {
        return relationships;
    }

    /**
     * Returns the explicit reference to the applicable governed information authority (AX-06).
     *
     * @return the explicit reference to the applicable governed information authority (AX-06)
     */
    public Topic getInformationAuthority() {
        return informationAuthority;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof InformationMetadata that)) {
            return false;
        }
        return persistenceLifecycle.equals(that.persistenceLifecycle) && cacheLifecycle.equals(that.cacheLifecycle)
                && origin.equals(that.origin) && coordinates.equals(that.coordinates)
                && security.equals(that.security) && relationships.equals(that.relationships)
                && ModelChecks.sameVersionedTopic(informationAuthority, that.informationAuthority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(persistenceLifecycle, cacheLifecycle, origin, coordinates, security, relationships,
                informationAuthority);
    }

    /** Delegates to constituents that already omit sensitive values. */
    @Override
    public String toString() {
        return "InformationMetadata{" + persistenceLifecycle + ", " + cacheLifecycle + ", " + origin + ", "
                + coordinates + ", " + security + ", " + relationships + "}";
    }
}
