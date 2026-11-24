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

package net.fhirfactory.harmonia.hestia.mnemosyne.framework;

import net.fhirfactory.harmonia.hapifhir.persistence.model.DurabilityAssurance;

/**
 * Minimum write assurance for one explicit deployment profile. The development profile proves
 * a local PostgreSQL transaction only; the production profile requires separate cross-site proof.
 */
public enum PersistenceDurabilityProfile {
    DEVELOPMENT_LOCAL_TRANSACTION(DurabilityAssurance.LOCAL_TRANSACTION_COMMITTED),
    PRODUCTION_CROSS_SITE(DurabilityAssurance.CROSS_SITE_CONFIRMED);

    private final DurabilityAssurance requiredAssurance;

    PersistenceDurabilityProfile(DurabilityAssurance requiredAssurance) {
        this.requiredAssurance = requiredAssurance;
    }

    /** Returns the minimum assurance a successful write must establish. */
    public DurabilityAssurance requiredAssurance() {
        return requiredAssurance;
    }
}