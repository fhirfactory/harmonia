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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.harmonia.model.core.information.entity.factory.InformationCoordinatesBuilder;
import net.fhirfactory.harmonia.model.core.naming.Topic;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * Encapsulates the four semantically distinct named Topic references providing operational resolution and ownership.
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with {@code DRP-Information-Unit-Framework.Final.001.md} (§§5.2, 19; IU-04) and
 * {@code DRP-Information-Unit-Framework.Final.002.md} (§3):
 * <ul>
 *   <li>{@code InformationCoordinates} comprises four semantically distinct named Topic references:
 *     <ol>
 *       <li><b>PersistenceGateway:</b> Strongly typed Topic describing a named PersistenceService gateway or capability.</li>
 *       <li><b>GatewayEndpoint:</b> Strongly typed Topic describing a named logical endpoint or parameter context.</li>
 *       <li><b>InformationLocation:</b> Strongly typed Topic describing the logical location or node where information lives.</li>
 *       <li><b>InformationOwner:</b> Strongly typed Topic describing the operational owner (e.g. organisation or facility).</li>
 *     </ol>
 *   </li>
 *   <li>These are <b>strongly typed Topic references</b> preserving full ordered canonical path identity, <b>not</b>
 *       inline gateway implementations, network URLs, connection credentials, or physical database addresses.</li>
 *   <li>Mnemosyne interprets these coordinates to resolve a PersistenceService and access context.</li>
 *   <li><b>CRITICAL ARCHITECTURAL INVARIANT:</b> {@code InformationCoordinates} is <b>100% independent of SecurityContext</b>.
 *       Operational ownership or location does not establish a compartment, clearance, right to read, or authorisation.</li>
 * </ul>
 * <p>
 * <b>Defensive Programming:</b>
 * <ul>
 *   <li>Immutable value object design.</li>
 *   <li>Accessors return {@link Optional} to explicitly signal that individual coordinates may be unpopulated.</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class InformationCoordinates implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final InformationCoordinates EMPTY = new InformationCoordinates(null, null, null, null);

    private final Topic persistenceGateway;
    private final Topic gatewayEndpoint;
    private final Topic informationLocation;
    private final Topic informationOwner;

    /**
     * Canonical constructor establishing validated information coordinate Topic references.
     *
     * @param persistenceGateway  the PersistenceGateway Topic reference (optional)
     * @param gatewayEndpoint     the GatewayEndpoint Topic reference (optional)
     * @param informationLocation the InformationLocation Topic reference (optional)
     * @param informationOwner    the InformationOwner Topic reference (optional)
     */
    @JsonCreator
    public InformationCoordinates(
            @JsonProperty("persistenceGateway") Topic persistenceGateway,
            @JsonProperty("gatewayEndpoint") Topic gatewayEndpoint,
            @JsonProperty("informationLocation") Topic informationLocation,
            @JsonProperty("informationOwner") Topic informationOwner) {
        this.persistenceGateway = persistenceGateway;
        this.gatewayEndpoint = gatewayEndpoint;
        this.informationLocation = informationLocation;
        this.informationOwner = informationOwner;
    }

    /**
     * Returns an empty, unpopulated {@code InformationCoordinates} instance.
     *
     * @return an empty InformationCoordinates
     */
    public static InformationCoordinates empty() {
        return EMPTY;
    }

    /**
     * Creates an {@code InformationCoordinates} instance with all four coordinate Topics.
     *
     * @param persistenceGateway  PersistenceGateway Topic
     * @param gatewayEndpoint     GatewayEndpoint Topic
     * @param informationLocation InformationLocation Topic
     * @param informationOwner    InformationOwner Topic
     * @return populated InformationCoordinates
     */
    public static InformationCoordinates of(
            Topic persistenceGateway,
            Topic gatewayEndpoint,
            Topic informationLocation,
            Topic informationOwner) {
        return new InformationCoordinates(persistenceGateway, gatewayEndpoint, informationLocation, informationOwner);
    }

    /**
     * Creates a new builder for constructing {@code InformationCoordinates} instances.
     *
     * @return a new builder
     */
    public static InformationCoordinatesBuilder builder() {
        return new InformationCoordinatesBuilder();
    }

    // =========================================================================
    // Core Accessors (Strongly Typed Topic References)
    // =========================================================================

    public Optional<Topic> persistenceGateway() {
        return Optional.ofNullable(persistenceGateway);
    }

    public Topic getPersistenceGateway() {
        return persistenceGateway;
    }

    public Optional<Topic> gatewayEndpoint() {
        return Optional.ofNullable(gatewayEndpoint);
    }

    public Topic getGatewayEndpoint() {
        return gatewayEndpoint;
    }

    public Optional<Topic> informationLocation() {
        return Optional.ofNullable(informationLocation);
    }

    public Topic getInformationLocation() {
        return informationLocation;
    }

    public Optional<Topic> informationOwner() {
        return Optional.ofNullable(informationOwner);
    }

    public Topic getInformationOwner() {
        return informationOwner;
    }

    // =========================================================================
    // Derived Convenience Accessors
    // =========================================================================

    @JsonIgnore
    public Optional<String> persistenceGatewayCanonicalName() {
        return persistenceGateway().map(Topic::getCanonicalName);
    }

    @JsonIgnore
    public Optional<String> gatewayEndpointCanonicalName() {
        return gatewayEndpoint().map(Topic::getCanonicalName);
    }

    @JsonIgnore
    public Optional<String> informationLocationCanonicalName() {
        return informationLocation().map(Topic::getCanonicalName);
    }

    @JsonIgnore
    public Optional<String> informationOwnerCanonicalName() {
        return informationOwner().map(Topic::getCanonicalName);
    }

    @JsonIgnore
    public Optional<String> persistenceGatewayName() {
        return persistenceGateway().map(Topic::getName);
    }

    @JsonIgnore
    public Optional<String> gatewayEndpointName() {
        return gatewayEndpoint().map(Topic::getName);
    }

    @JsonIgnore
    public Optional<String> informationLocationName() {
        return informationLocation().map(Topic::getName);
    }

    @JsonIgnore
    public Optional<String> informationOwnerName() {
        return informationOwner().map(Topic::getName);
    }

    @JsonIgnore
    public boolean isEmpty() {
        return persistenceGateway == null
                && gatewayEndpoint == null
                && informationLocation == null
                && informationOwner == null;
    }

    // =========================================================================
    // Immutable Derivations ("With" Mutators)
    // =========================================================================

    public InformationCoordinates withPersistenceGateway(Topic persistenceGateway) {
        return new InformationCoordinates(persistenceGateway, this.gatewayEndpoint, this.informationLocation, this.informationOwner);
    }

    public InformationCoordinates withGatewayEndpoint(Topic gatewayEndpoint) {
        return new InformationCoordinates(this.persistenceGateway, gatewayEndpoint, this.informationLocation, this.informationOwner);
    }

    public InformationCoordinates withInformationLocation(Topic informationLocation) {
        return new InformationCoordinates(this.persistenceGateway, this.gatewayEndpoint, informationLocation, this.informationOwner);
    }

    public InformationCoordinates withInformationOwner(Topic informationOwner) {
        return new InformationCoordinates(this.persistenceGateway, this.gatewayEndpoint, this.informationLocation, informationOwner);
    }

    // =========================================================================
    // Standard Object Methods
    // =========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InformationCoordinates that = (InformationCoordinates) o;
        return Objects.equals(persistenceGateway, that.persistenceGateway) &&
                Objects.equals(gatewayEndpoint, that.gatewayEndpoint) &&
                Objects.equals(informationLocation, that.informationLocation) &&
                Objects.equals(informationOwner, that.informationOwner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(persistenceGateway, gatewayEndpoint, informationLocation, informationOwner);
    }

    @Override
    public String toString() {
        return "InformationCoordinates{" +
                "gateway=" + topicSummary(persistenceGateway) +
                ", endpoint=" + topicSummary(gatewayEndpoint) +
                ", location=" + topicSummary(informationLocation) +
                ", owner=" + topicSummary(informationOwner) +
                '}';
    }

    private static String topicSummary(Topic topic) {
        return topic != null ? "Topic(depth=" + topic.getDepth() + ")" : "null";
    }

}
