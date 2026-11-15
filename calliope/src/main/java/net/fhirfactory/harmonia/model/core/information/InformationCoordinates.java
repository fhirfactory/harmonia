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

/**
 * Encapsulates the four semantically distinct named Topic references providing operational resolution and ownership.
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with {@code DRP-Information-Unit-Framework.Final.001.md} (§§5.2, 19; IU-04):
 * <ul>
 *   <li>{@code InformationCoordinates} comprises four semantically distinct named Topic references:
 *     <ol>
 *       <li><b>PersistenceGateway:</b> Topic identifier/name describing a named PersistenceService gateway or capability.</li>
 *       <li><b>GatewayEndpoint:</b> Topic identifier/name describing a named logical endpoint or parameter context.</li>
 *       <li><b>InformationLocation:</b> Topic identifier/name describing the logical location or node where information lives.</li>
 *       <li><b>InformationOwner:</b> Topic identifier/name describing the operational owner (e.g. organisation or facility).</li>
 *     </ol>
 *   </li>
 *   <li>These are <b>Topic names/identities</b> within governed containment hierarchies, <b>not</b> inline gateway implementations,
 *       network URLs, connection credentials, or physical database addresses.</li>
 *   <li>Mnemosyne interprets these coordinates to resolve a PersistenceService and access context.</li>
 *   <li><b>CRITICAL ARCHITECTURAL INVARIANT:</b> {@code InformationCoordinates} is <b>100% independent of SecurityContext</b>.
 *       Operational ownership or location does not establish a compartment, clearance, right to read, or authorisation.</li>
 * </ul>
 * <p>
 * <b>Defensive Programming:</b>
 * <ul>
 *   <li>Immutable value object design.</li>
 *   <li>Coordinate fields are trimmed and sanitized; empty strings are treated as absent.</li>
 *   <li>Accessors return {@link Optional} to explicitly signal that individual coordinates may be unpopulated.</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class InformationCoordinates implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final InformationCoordinates EMPTY = new InformationCoordinates(null, null, null, null);

    private final String persistenceGateway;
    private final String gatewayEndpoint;
    private final String informationLocation;
    private final String informationOwner;

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
            @JsonProperty("persistenceGateway") String persistenceGateway,
            @JsonProperty("gatewayEndpoint") String gatewayEndpoint,
            @JsonProperty("informationLocation") String informationLocation,
            @JsonProperty("informationOwner") String informationOwner) {
        this.persistenceGateway = sanitize(persistenceGateway);
        this.gatewayEndpoint = sanitize(gatewayEndpoint);
        this.informationLocation = sanitize(informationLocation);
        this.informationOwner = sanitize(informationOwner);
    }

    private static String sanitize(String input) {
        return input != null && !input.isBlank() ? input.trim() : null;
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
            String persistenceGateway,
            String gatewayEndpoint,
            String informationLocation,
            String informationOwner) {
        return new InformationCoordinates(persistenceGateway, gatewayEndpoint, informationLocation, informationOwner);
    }

    /**
     * Creates a new builder for constructing {@code InformationCoordinates} instances.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    // =========================================================================
    // Accessors
    // =========================================================================

    /**
     * Returns the PersistenceGateway Topic reference.
     *
     * @return optional PersistenceGateway Topic reference
     */
    @JsonIgnore
    public Optional<String> persistenceGateway() {
        return Optional.ofNullable(persistenceGateway);
    }

    /**
     * Returns the raw PersistenceGateway Topic reference, or null if not set.
     *
     * @return PersistenceGateway, or null
     */
    @JsonProperty("persistenceGateway")
    public String getPersistenceGateway() {
        return persistenceGateway;
    }

    /**
     * Returns the GatewayEndpoint Topic reference.
     *
     * @return optional GatewayEndpoint Topic reference
     */
    @JsonIgnore
    public Optional<String> gatewayEndpoint() {
        return Optional.ofNullable(gatewayEndpoint);
    }

    /**
     * Returns the raw GatewayEndpoint Topic reference, or null if not set.
     *
     * @return GatewayEndpoint, or null
     */
    @JsonProperty("gatewayEndpoint")
    public String getGatewayEndpoint() {
        return gatewayEndpoint;
    }

    /**
     * Returns the InformationLocation Topic reference.
     *
     * @return optional InformationLocation Topic reference
     */
    @JsonIgnore
    public Optional<String> informationLocation() {
        return Optional.ofNullable(informationLocation);
    }

    /**
     * Returns the raw InformationLocation Topic reference, or null if not set.
     *
     * @return InformationLocation, or null
     */
    @JsonProperty("informationLocation")
    public String getInformationLocation() {
        return informationLocation;
    }

    /**
     * Returns the InformationOwner Topic reference.
     *
     * @return optional InformationOwner Topic reference
     */
    @JsonIgnore
    public Optional<String> informationOwner() {
        return Optional.ofNullable(informationOwner);
    }

    /**
     * Returns the raw InformationOwner Topic reference, or null if not set.
     *
     * @return InformationOwner, or null
     */
    @JsonProperty("informationOwner")
    public String getInformationOwner() {
        return informationOwner;
    }

    // =========================================================================
    // Immutable Derivations ("With" Mutators)
    // =========================================================================

    /**
     * Derives an immutable copy with an updated PersistenceGateway Topic reference.
     *
     * @param gateway new PersistenceGateway Topic
     * @return new InformationCoordinates
     */
    public InformationCoordinates withPersistenceGateway(String gateway) {
        return new InformationCoordinates(gateway, this.gatewayEndpoint, this.informationLocation, this.informationOwner);
    }

    /**
     * Derives an immutable copy with an updated GatewayEndpoint Topic reference.
     *
     * @param endpoint new GatewayEndpoint Topic
     * @return new InformationCoordinates
     */
    public InformationCoordinates withGatewayEndpoint(String endpoint) {
        return new InformationCoordinates(this.persistenceGateway, endpoint, this.informationLocation, this.informationOwner);
    }

    /**
     * Derives an immutable copy with an updated InformationLocation Topic reference.
     *
     * @param location new InformationLocation Topic
     * @return new InformationCoordinates
     */
    public InformationCoordinates withInformationLocation(String location) {
        return new InformationCoordinates(this.persistenceGateway, this.gatewayEndpoint, location, this.informationOwner);
    }

    /**
     * Derives an immutable copy with an updated InformationOwner Topic reference.
     *
     * @param owner new InformationOwner Topic
     * @return new InformationCoordinates
     */
    public InformationCoordinates withInformationOwner(String owner) {
        return new InformationCoordinates(this.persistenceGateway, this.gatewayEndpoint, this.informationLocation, owner);
    }

    // =========================================================================
    // Standard Object Methods
    // =========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InformationCoordinates that = (InformationCoordinates) o;
        return Objects.equals(persistenceGateway, that.persistenceGateway)
                && Objects.equals(gatewayEndpoint, that.gatewayEndpoint)
                && Objects.equals(informationLocation, that.informationLocation)
                && Objects.equals(informationOwner, that.informationOwner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(persistenceGateway, gatewayEndpoint, informationLocation, informationOwner);
    }

    @Override
    public String toString() {
        return "InformationCoordinates{" +
                "persistenceGateway='" + persistenceGateway + '\'' +
                ", gatewayEndpoint='" + gatewayEndpoint + '\'' +
                ", informationLocation='" + informationLocation + '\'' +
                ", informationOwner='" + informationOwner + '\'' +
                '}';
    }

    // =========================================================================
    // Builder
    // =========================================================================

    /**
     * Fluent builder for {@link InformationCoordinates}.
     */
    public static final class Builder {
        private String persistenceGateway;
        private String gatewayEndpoint;
        private String informationLocation;
        private String informationOwner;

        private Builder() {}

        public Builder persistenceGateway(String persistenceGateway) {
            this.persistenceGateway = persistenceGateway;
            return this;
        }

        public Builder gatewayEndpoint(String gatewayEndpoint) {
            this.gatewayEndpoint = gatewayEndpoint;
            return this;
        }

        public Builder informationLocation(String informationLocation) {
            this.informationLocation = informationLocation;
            return this;
        }

        public Builder informationOwner(String informationOwner) {
            this.informationOwner = informationOwner;
            return this;
        }

        public InformationCoordinates build() {
            return new InformationCoordinates(persistenceGateway, gatewayEndpoint, informationLocation, informationOwner);
        }
    }
}
