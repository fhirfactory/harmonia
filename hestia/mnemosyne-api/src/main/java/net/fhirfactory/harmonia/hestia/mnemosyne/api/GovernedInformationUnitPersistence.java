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

package net.fhirfactory.harmonia.hestia.mnemosyne.api;

import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.model.core.information.access.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.core.information.access.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.model.core.information.access.ResourceKey;
import net.fhirfactory.harmonia.model.core.information.entity.InformationUnit;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

/**
 * Mneme-facing governed port for authoritative InformationUnit persistence in Mnemosyne.
 * The contract exposes domain objects and outcomes, not JDBC or database implementation details.
 */
public interface GovernedInformationUnitPersistence {

    /**
     * Creates an InformationUnit only when its stable identity is absent.
     *
     * @param key IU resource key
     * @param proposed InformationUnit value
     * @param securityContext established Themis context
     * @return explicit committed, conflict, failed, absent, or indeterminate outcome
     */
    AuthoritativePersistenceResult<InformationUnit> create(
            ResourceKey key,
            InformationUnit proposed,
            ThemisSecurityContext securityContext);

    /**
     * Appends an InformationUnit version only when the expected authoritative predecessor matches.
     *
     * @param key IU resource key
     * @param proposed InformationUnit value
     * @param expectedVersion expected persisted predecessor
     * @param securityContext established Themis context
     * @return explicit committed, conflict, failed, absent, or indeterminate outcome
     */
    AuthoritativePersistenceResult<InformationUnit> update(
            ResourceKey key,
            InformationUnit proposed,
            ExpectedAuthoritativeVersion expectedVersion,
            ThemisSecurityContext securityContext);

    /**
     * Retrieves the authoritative InformationUnit by stable resource key.
     *
     * @param key IU resource key
     * @param securityContext established Themis context
     * @return explicit retrieved, absent, failed, or indeterminate outcome
     */
    AuthoritativePersistenceResult<InformationUnit> read(
            ResourceKey key,
            ThemisSecurityContext securityContext);

    /**
     * Retrieves the authoritative version without exposing persistence-engine details.
     *
     * @param key IU resource key
     * @param securityContext established Themis context
     * @return a committed version result, or the applicable failure/absence outcome
     */
    AuthoritativePersistenceResult<AuthoritativeVersion> readVersion(
            ResourceKey key,
            ThemisSecurityContext securityContext);
}