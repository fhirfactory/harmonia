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

package net.fhirfactory.harmonia.model.core.information.entity.factory;

import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationCoordinates;
import net.fhirfactory.harmonia.model.core.naming.Topic;

/** Assembles the four independent Topic coordinates of governed information. */
public final class InformationCoordinatesBuilder {

    private Topic persistenceGateway;
    private Topic gatewayEndpoint;
    private Topic informationLocation;
    private Topic informationOwner;

    /** Sets the persistence gateway Topic. */
    public InformationCoordinatesBuilder persistenceGateway(Topic persistenceGateway) {
        this.persistenceGateway = persistenceGateway;
        return this;
    }

    /** Sets the gateway endpoint Topic. */
    public InformationCoordinatesBuilder gatewayEndpoint(Topic gatewayEndpoint) {
        this.gatewayEndpoint = gatewayEndpoint;
        return this;
    }

    /** Sets the information location Topic. */
    public InformationCoordinatesBuilder informationLocation(Topic informationLocation) {
        this.informationLocation = informationLocation;
        return this;
    }

    /** Sets the information owner Topic. */
    public InformationCoordinatesBuilder informationOwner(Topic informationOwner) {
        this.informationOwner = informationOwner;
        return this;
    }

    /** Builds an immutable coordinate value. */
    public InformationCoordinates build() {
        return new InformationCoordinates(persistenceGateway, gatewayEndpoint, informationLocation, informationOwner);
    }
}