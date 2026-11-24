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
import net.fhirfactory.harmonia.model.core.information.access.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

/**
 * Governed port for persisting an owner's operational Status without translating it into an IU.
 */
public interface GovernedOperationalStatusPersistence {

    /**
     * Creates or conditionally advances one owner's Status record.
     *
     * @param proposed owner-defined status payload
     * @param expectedVersion expected absence or authoritative predecessor version
     * @param securityContext established Themis context
     * @return explicit committed, conflict, failed, or indeterminate outcome
     */
    AuthoritativePersistenceResult<OperationalStatusRecord> record(
            OperationalStatusRecord proposed,
            ExpectedAuthoritativeVersion expectedVersion,
            ThemisSecurityContext securityContext);

    /**
     * Retrieves an owner's current Status record.
     *
     * @param key owner/subject/type identity
     * @param securityContext established Themis context
     * @return explicit retrieved, absent, failed, or indeterminate outcome
     */
    AuthoritativePersistenceResult<OperationalStatusRecord> read(
            OperationalStatusKey key,
            ThemisSecurityContext securityContext);
}