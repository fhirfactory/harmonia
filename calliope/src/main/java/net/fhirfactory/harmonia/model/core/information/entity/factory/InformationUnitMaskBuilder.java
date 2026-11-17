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

import net.fhirfactory.harmonia.model.core.information.entity.InformationUnitMask;
import net.fhirfactory.harmonia.model.core.naming.TopicMask;

import java.util.UUID;

/** Assembles a neutral multi-facet selector for candidate Information Units. */
public final class InformationUnitMaskBuilder {

    private UUID id;
    private TopicMask identityMask;
    private TopicMask containerTypeMask;
    private TopicMask contentStructureMask;
    private TopicMask originMask;
    private TopicMask persistenceGatewayMask;
    private TopicMask gatewayEndpointMask;
    private TopicMask informationLocationMask;
    private TopicMask informationOwnerMask;
    private String version;

    /** Sets the selector identity. */
    public InformationUnitMaskBuilder id(UUID id) {
        this.id = id;
        return this;
    }

    /** Sets the IU identity Topic mask. */
    public InformationUnitMaskBuilder identityMask(TopicMask mask) {
        identityMask = mask;
        return this;
    }

    /** Sets the container type mask. */
    public InformationUnitMaskBuilder containerTypeMask(TopicMask mask) {
        containerTypeMask = mask;
        return this;
    }

    /** Sets the content structure mask. */
    public InformationUnitMaskBuilder contentStructureMask(TopicMask mask) {
        contentStructureMask = mask;
        return this;
    }

    /** Sets the origin mask. */
    public InformationUnitMaskBuilder originMask(TopicMask mask) {
        originMask = mask;
        return this;
    }

    /** Sets the persistence gateway mask. */
    public InformationUnitMaskBuilder persistenceGatewayMask(TopicMask mask) {
        persistenceGatewayMask = mask;
        return this;
    }

    /** Sets the gateway endpoint mask. */
    public InformationUnitMaskBuilder gatewayEndpointMask(TopicMask mask) {
        gatewayEndpointMask = mask;
        return this;
    }

    /** Sets the information location mask. */
    public InformationUnitMaskBuilder informationLocationMask(TopicMask mask) {
        informationLocationMask = mask;
        return this;
    }

    /** Sets the information owner mask. */
    public InformationUnitMaskBuilder informationOwnerMask(TopicMask mask) {
        informationOwnerMask = mask;
        return this;
    }

    /** Sets the authoritative persisted version constraint. */
    public InformationUnitMaskBuilder version(String version) {
        this.version = version;
        return this;
    }

    /** Builds an immutable InformationUnitMask. */
    public InformationUnitMask build() {
        return new InformationUnitMask(id, identityMask, containerTypeMask, contentStructureMask, originMask,
                persistenceGatewayMask, gatewayEndpointMask, informationLocationMask, informationOwnerMask, version);
    }
}