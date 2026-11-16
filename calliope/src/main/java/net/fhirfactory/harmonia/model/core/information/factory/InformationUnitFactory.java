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

package net.fhirfactory.harmonia.model.core.information.factory;

import net.fhirfactory.harmonia.model.core.information.*;

import java.util.UUID;

/**
 * Factory for creating standard {@link InformationUnit} instances.
 */
public final class InformationUnitFactory {

    private InformationUnitFactory() {
        // Utility factory
    }

    public static InformationUnit create(UUID uuid, InformationMetadata metadata, InformationRepresentation representation) {
        return new InformationUnit(InformationUnitIdentifier.fromUuid(uuid), null, metadata, representation);
    }

    public static InformationUnit create(UUID uuid, InformationUnitMetadata metadata, InformationRepresentation representation) {
        return new InformationUnit(InformationUnitIdentifier.fromUuid(uuid), null, metadata, representation);
    }

    public static InformationUnit create(UUID uuid, String version, InformationMetadata metadata, InformationRepresentation representation) {
        return new InformationUnit(InformationUnitIdentifier.fromUuid(uuid), version, metadata, representation);
    }

    public static InformationUnit create(UUID uuid, String version, InformationUnitMetadata metadata, InformationRepresentation representation) {
        return new InformationUnit(InformationUnitIdentifier.fromUuid(uuid), version, metadata, representation);
    }

    public static InformationUnit random(InformationMetadata metadata, InformationRepresentation representation) {
        return new InformationUnit(InformationUnitIdentifier.random(), null, metadata, representation);
    }

    public static InformationUnit random(InformationUnitMetadata metadata, InformationRepresentation representation) {
        return new InformationUnit(InformationUnitIdentifier.random(), null, metadata, representation);
    }
}
