/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import java.util.Objects;
import java.util.Optional;
import net.fhirfactory.harmonia.model.core.naming.Topic;

/**
 * The logical resolution and ownership coordinates of one IU revision.
 *
 * <p>Draft 002.005 §1.3 retains four coordinates: {@code PersistenceGateway},
 * {@code GatewayEndpoint}, {@code InformationLocation} and
 * {@code InformationOwner}. Each is an optional qualified Topic, because a
 * newly created IU may not yet have been placed. Coordinates are immutable
 * within each governed revision: an authorised relocation or ownership change
 * is expressed as a whole new value and applied through
 * {@code InformationUnit.reviseCoordinates(...)}, which yields a new revision
 * snapshot under the same UUID while the earlier snapshot keeps its
 * coordinates.</p>
 *
 * <p>Coordinates describe logical resolution and ownership only. They are
 * independent of security requirements, and {@code InformationOwner} is
 * distinct from the quality-assessment owner in {@link InformationQuality}.</p>
 */
public final class InformationCoordinates {
    private static final InformationCoordinates UNPLACED = new InformationCoordinates(null, null, null, null);

    private final Topic persistenceGateway;
    private final Topic gatewayEndpoint;
    private final Topic informationLocation;
    private final Topic informationOwner;

    private InformationCoordinates(Topic persistenceGateway, Topic gatewayEndpoint, Topic informationLocation,
                                   Topic informationOwner) {
        this.persistenceGateway = persistenceGateway;
        this.gatewayEndpoint = gatewayEndpoint;
        this.informationLocation = informationLocation;
        this.informationOwner = informationOwner;
    }

    /**
     * Returns the initialised value with every coordinate unknown.
     *
     * @return the initialised value with every coordinate unknown
     */
    public static InformationCoordinates unplaced() {
        return UNPLACED;
    }

    /**
     * Creates a complete coordinate value; any coordinate may be null when unknown.
     *
     * @param persistenceGateway logical persistence gateway, or null
     * @param gatewayEndpoint endpoint within that gateway, or null
     * @param informationLocation logical information location, or null
     * @param informationOwner governed logical owner, or null
     * @return an immutable value
     */
    public static InformationCoordinates of(Topic persistenceGateway, Topic gatewayEndpoint,
                                            Topic informationLocation, Topic informationOwner) {
        return new InformationCoordinates(persistenceGateway, gatewayEndpoint, informationLocation, informationOwner);
    }

    /**
     * Returns the persistence gateway, when known.
     *
     * @return the persistence gateway, when known
     */
    public Optional<Topic> getPersistenceGateway() {
        return Optional.ofNullable(persistenceGateway);
    }

    /**
     * Returns the gateway endpoint, when known.
     *
     * @return the gateway endpoint, when known
     */
    public Optional<Topic> getGatewayEndpoint() {
        return Optional.ofNullable(gatewayEndpoint);
    }

    /**
     * Returns the logical information location, when known.
     *
     * @return the logical information location, when known
     */
    public Optional<Topic> getInformationLocation() {
        return Optional.ofNullable(informationLocation);
    }

    /**
     * Returns the governed logical information owner, when known.
     *
     * @return the governed logical information owner, when known
     */
    public Optional<Topic> getInformationOwner() {
        return Optional.ofNullable(informationOwner);
    }

    /** Compares every coordinate including entry versions. */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof InformationCoordinates that)) {
            return false;
        }
        return ModelChecks.sameVersionedTopic(persistenceGateway, that.persistenceGateway)
                && ModelChecks.sameVersionedTopic(gatewayEndpoint, that.gatewayEndpoint)
                && ModelChecks.sameVersionedTopic(informationLocation, that.informationLocation)
                && ModelChecks.sameVersionedTopic(informationOwner, that.informationOwner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(persistenceGateway, gatewayEndpoint, informationLocation, informationOwner);
    }

    /** Reports which coordinates are known, not their values. */
    @Override
    public String toString() {
        return "InformationCoordinates{gateway=" + (persistenceGateway != null)
                + ", endpoint=" + (gatewayEndpoint != null) + ", location=" + (informationLocation != null)
                + ", owner=" + (informationOwner != null) + "}";
    }
}
