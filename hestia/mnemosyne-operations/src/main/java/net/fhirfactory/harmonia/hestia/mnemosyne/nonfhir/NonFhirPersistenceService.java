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

package net.fhirfactory.harmonia.hestia.mnemosyne.nonfhir;

import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.hestia.mnemosyne.api.OperationalStatusKey;
import net.fhirfactory.harmonia.hestia.mnemosyne.api.OperationalStatusRecord;
import net.fhirfactory.harmonia.model.core.information.access.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.core.information.access.ExpectedAuthoritativeVersion;

import java.util.UUID;

/**
 * Engine-neutral Non-FHIR persistence behaviour used by the GPF. Implementations establish
 * transactional IU history and owner-defined Status without exposing database mechanics here.
 */
public interface NonFhirPersistenceService {

    /** Creates the first authoritative IU revision, or returns a typed duplicate conflict/replay. */
    AuthoritativePersistenceResult<InformationUnitPersistenceRecord> create(InformationUnitPersistenceRecord proposed);

    /** Appends an IU revision only when the expected current version matches. */
    AuthoritativePersistenceResult<InformationUnitPersistenceRecord> update(
            InformationUnitPersistenceRecord proposed,
            ExpectedAuthoritativeVersion expectedVersion);

    /** Reads the latest committed IU revision by stable UUID. */
    AuthoritativePersistenceResult<InformationUnitPersistenceRecord> read(UUID identifier);

    /** Reads only the latest committed IU version when callers do not require the representation. */
    AuthoritativePersistenceResult<AuthoritativeVersion> readVersion(UUID identifier);

    /** Creates or appends an owner-defined Status revision with optimistic version control. */
    AuthoritativePersistenceResult<OperationalStatusRecord> recordStatus(
            OperationalStatusRecord proposed,
            ExpectedAuthoritativeVersion expectedVersion);

    /** Reads the latest committed owner-defined Status revision. */
    AuthoritativePersistenceResult<OperationalStatusRecord> readStatus(OperationalStatusKey key);
}