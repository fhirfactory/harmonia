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

import net.fhirfactory.harmonia.hapifhir.persistence.AuthoritativePersistencePort;
import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.hapifhir.persistence.model.DurabilityAssurance;
import net.fhirfactory.harmonia.model.core.information.access.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.model.core.information.access.ResourceKey;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Objects;

/**
 * Binds the existing FHIR {@link AuthoritativePersistencePort} to the GPF without replacing or
 * exposing its JPA implementation. The declared assurance qualifies the adapter's local transaction;
 * it does not claim production cross-site durability.
 */
public final class FhirPersistenceServiceBinding {

    private final AuthoritativePersistencePort<IBaseResource> persistencePort;
    private final DurabilityAssurance committedAssurance;

    /**
     * Creates a binding to the existing FHIR persistence port with an explicit commit assurance.
     *
     * @param persistencePort existing FHIR persistence implementation boundary
     * @param committedAssurance assurance established by that boundary for a committed result
     */
    public FhirPersistenceServiceBinding(
            AuthoritativePersistencePort<IBaseResource> persistencePort,
            DurabilityAssurance committedAssurance) {
        this.persistencePort = Objects.requireNonNull(persistencePort, "persistencePort must not be null");
        this.committedAssurance = Objects.requireNonNull(committedAssurance, "committedAssurance must not be null");
        if (!committedAssurance.satisfies(DurabilityAssurance.LOCAL_TRANSACTION_COMMITTED)) {
            throw new IllegalArgumentException("FHIR binding must assert at least a committed local transaction");
        }
    }

    /** Delegates FHIR reads and labels committed rows with their established source assurance. */
    public AuthoritativePersistenceResult<IBaseResource> read(ResourceKey key) {
        Objects.requireNonNull(key, "key must not be null");
        return qualify(persistencePort.read(key));
    }

    /** Delegates an atomic FHIR create to the existing persistence port. */
    public AuthoritativePersistenceResult<IBaseResource> create(ResourceKey key, IBaseResource proposed) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(proposed, "proposed must not be null");
        return qualify(persistencePort.create(key, proposed));
    }

    /** Delegates an expected-version FHIR update to the existing persistence port. */
    public AuthoritativePersistenceResult<IBaseResource> update(
            ResourceKey key,
            IBaseResource proposed,
            ExpectedAuthoritativeVersion expectedVersion) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(proposed, "proposed must not be null");
        Objects.requireNonNull(expectedVersion, "expectedVersion must not be null");
        return qualify(persistencePort.update(key, proposed, expectedVersion));
    }

    /** Adds the explicit binding assurance without discarding a stronger assurance from the port. */
    private AuthoritativePersistenceResult<IBaseResource> qualify(
            AuthoritativePersistenceResult<IBaseResource> result) {
        if (result == null) {
            return new AuthoritativePersistenceResult.OutcomeUnknown<>("FHIR persistence port returned no outcome");
        }
        if (result instanceof AuthoritativePersistenceResult.Committed<IBaseResource> committed) {
            DurabilityAssurance observed = committed.durabilityAssurance().satisfies(committedAssurance)
                    ? committed.durabilityAssurance()
                    : committedAssurance;
            return new AuthoritativePersistenceResult.Committed<>(
                    committed.persistedResource(),
                    committed.authoritativeVersion(),
                    observed);
        }
        if (result instanceof AuthoritativePersistenceResult.OutcomeUnknown<IBaseResource> unknown) {
            return new AuthoritativePersistenceResult.OutcomeUnknown<>(
                    unknown.message(), unknown.cause(), unknown.observedAssurance());
        }
        return result;
    }
}