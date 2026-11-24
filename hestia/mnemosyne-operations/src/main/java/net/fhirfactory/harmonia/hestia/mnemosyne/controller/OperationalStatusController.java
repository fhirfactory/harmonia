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

package net.fhirfactory.harmonia.hestia.mnemosyne.controller;

import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.hestia.mnemosyne.api.GovernedOperationalStatusPersistence;
import net.fhirfactory.harmonia.hestia.mnemosyne.api.OperationalStatusKey;
import net.fhirfactory.harmonia.hestia.mnemosyne.api.OperationalStatusRecord;
import net.fhirfactory.harmonia.hestia.mnemosyne.framework.GovernedPersistenceFramework;
import net.fhirfactory.harmonia.hestia.mnemosyne.governance.MnemosyneAuthorizationGate;
import net.fhirfactory.harmonia.model.core.information.access.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

import java.util.Objects;

/**
 * Mnemosyne PEP/controller for owner-defined operational Status. It governs availability and
 * persistence without interpreting the payload or converting it into an InformationUnit.
 */
public final class OperationalStatusController implements GovernedOperationalStatusPersistence {

    private final MnemosyneAuthorizationGate authorizationGate;
    private final GovernedPersistenceFramework persistenceFramework;

    /** Creates the controller using the existing Themis gate and GPF. */
    public OperationalStatusController(
            MnemosyneAuthorizationGate authorizationGate,
            GovernedPersistenceFramework persistenceFramework) {
        this.authorizationGate = Objects.requireNonNull(authorizationGate, "authorizationGate must not be null");
        this.persistenceFramework = Objects.requireNonNull(persistenceFramework, "persistenceFramework must not be null");
    }

    /** Persists a new or versioned Status record after the matching Themis decision. */
    @Override
    public AuthoritativePersistenceResult<OperationalStatusRecord> record(
            OperationalStatusRecord proposed,
            ExpectedAuthoritativeVersion expectedVersion,
            ThemisSecurityContext securityContext) {
        Objects.requireNonNull(proposed, "proposed must not be null");
        Objects.requireNonNull(expectedVersion, "expectedVersion must not be null");
        ThemisAction action = expectedVersion.isNone() ? ThemisAction.CREATE : ThemisAction.UPDATE;
        authorizationGate.requireAuthorized(action, proposed.key().resourceKey(), securityContext);
        return persistenceFramework.recordStatus(proposed, expectedVersion);
    }

    /** Reads one owner's current Status after Themis permits access to its opaque resource key. */
    @Override
    public AuthoritativePersistenceResult<OperationalStatusRecord> read(
            OperationalStatusKey key,
            ThemisSecurityContext securityContext) {
        Objects.requireNonNull(key, "key must not be null");
        authorizationGate.requireAuthorized(ThemisAction.READ, key.resourceKey(), securityContext);
        return persistenceFramework.readStatus(key);
    }
}