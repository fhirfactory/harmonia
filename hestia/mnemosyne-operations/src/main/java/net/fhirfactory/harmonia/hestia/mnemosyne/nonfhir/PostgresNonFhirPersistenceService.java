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

import javax.sql.DataSource;
import java.util.Objects;
import java.util.UUID;

/**
 * PostgreSQL implementation of the Non-FHIR PersistenceService. It composes cohesive IU-history
 * and opaque Status stores behind the engine-neutral contract used by the GPF.
 */
public final class PostgresNonFhirPersistenceService implements NonFhirPersistenceService {

    private final PostgresInformationUnitStore informationUnitStore;
    private final PostgresOperationalStatusStore operationalStatusStore;

    /** Creates the PostgreSQL adapter with the common SQLSTATE failure classifier. */
    public PostgresNonFhirPersistenceService(DataSource dataSource) {
        Objects.requireNonNull(dataSource, "dataSource must not be null");
        PostgresFailureClassifier failureClassifier = new PostgresFailureClassifier();
        this.informationUnitStore = new PostgresInformationUnitStore(dataSource, failureClassifier);
        this.operationalStatusStore = new PostgresOperationalStatusStore(dataSource, failureClassifier);
    }

    /** Creates the first version or returns an identical create replay/conflict. */
    @Override
    public AuthoritativePersistenceResult<InformationUnitPersistenceRecord> create(InformationUnitPersistenceRecord proposed) {
        return informationUnitStore.create(proposed);
    }

    /** Appends an IU version only when its expected predecessor matches. */
    @Override
    public AuthoritativePersistenceResult<InformationUnitPersistenceRecord> update(
            InformationUnitPersistenceRecord proposed,
            ExpectedAuthoritativeVersion expectedVersion) {
        return informationUnitStore.update(proposed, expectedVersion);
    }

    /** Reads the latest committed IU revision. */
    @Override
    public AuthoritativePersistenceResult<InformationUnitPersistenceRecord> read(UUID identifier) {
        return informationUnitStore.read(identifier);
    }

    /** Reads only the latest committed IU version. */
    @Override
    public AuthoritativePersistenceResult<AuthoritativeVersion> readVersion(UUID identifier) {
        return informationUnitStore.readVersion(identifier);
    }

    /** Persists an owner-defined Status using expected-version concurrency control. */
    @Override
    public AuthoritativePersistenceResult<OperationalStatusRecord> recordStatus(
            OperationalStatusRecord proposed,
            ExpectedAuthoritativeVersion expectedVersion) {
        return operationalStatusStore.record(proposed, expectedVersion);
    }

    /** Reads the latest persisted owner-defined Status. */
    @Override
    public AuthoritativePersistenceResult<OperationalStatusRecord> readStatus(OperationalStatusKey key) {
        return operationalStatusStore.read(key);
    }
}