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

import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.hapifhir.persistence.model.DurabilityAssurance;
import net.fhirfactory.harmonia.hestia.mnemosyne.api.OperationalStatusKey;
import net.fhirfactory.harmonia.hestia.mnemosyne.api.OperationalStatusRecord;
import net.fhirfactory.harmonia.hestia.mnemosyne.nonfhir.InformationUnitPersistenceRecord;
import net.fhirfactory.harmonia.hestia.mnemosyne.nonfhir.NonFhirPersistenceService;
import net.fhirfactory.harmonia.model.core.information.access.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.core.information.access.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.model.core.information.access.ResourceKey;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Coordinates governed Non-FHIR persistence operations and applies the configured durability
 * threshold to writes. Database engine details remain behind {@link NonFhirPersistenceService}.
 */
public final class GovernedPersistenceFramework {

    private final NonFhirPersistenceService nonFhirPersistenceService;
    private final FhirPersistenceServiceBinding fhirPersistenceServiceBinding;
    private final PersistenceDurabilityProfile durabilityProfile;

    /** Creates the GPF with an explicit persistence service and assurance profile. */
    public GovernedPersistenceFramework(
            NonFhirPersistenceService nonFhirPersistenceService,
            PersistenceDurabilityProfile durabilityProfile) {
        this(nonFhirPersistenceService, null, durabilityProfile);
    }

    /** Creates the GPF with an optional existing FHIR PersistenceService binding. */
    public GovernedPersistenceFramework(
            NonFhirPersistenceService nonFhirPersistenceService,
            FhirPersistenceServiceBinding fhirPersistenceServiceBinding,
            PersistenceDurabilityProfile durabilityProfile) {
        this.nonFhirPersistenceService = Objects.requireNonNull(nonFhirPersistenceService, "nonFhirPersistenceService must not be null");
        this.fhirPersistenceServiceBinding = fhirPersistenceServiceBinding;
        this.durabilityProfile = Objects.requireNonNull(durabilityProfile, "durabilityProfile must not be null");
    }

    /** Routes FHIR create through the existing FHIR persistence port and applies the write profile. */
    public AuthoritativePersistenceResult<IBaseResource> createFhir(ResourceKey key, IBaseResource proposed) {
        if (fhirPersistenceServiceBinding == null) {
            return new AuthoritativePersistenceResult.NotCommitted<>("FHIR PersistenceService binding is not configured");
        }
        return enforceWriteDurability(protect(() -> fhirPersistenceServiceBinding.create(key, proposed)));
    }

    /** Routes an expected-version FHIR update through the existing FHIR persistence port. */
    public AuthoritativePersistenceResult<IBaseResource> updateFhir(
            ResourceKey key,
            IBaseResource proposed,
            ExpectedAuthoritativeVersion expectedVersion) {
        if (fhirPersistenceServiceBinding == null) {
            return new AuthoritativePersistenceResult.NotCommitted<>("FHIR PersistenceService binding is not configured");
        }
        return enforceWriteDurability(protect(() -> fhirPersistenceServiceBinding.update(key, proposed, expectedVersion)));
    }

    /** Routes a FHIR read through the existing FHIR persistence port. */
    public AuthoritativePersistenceResult<IBaseResource> readFhir(ResourceKey key) {
        if (fhirPersistenceServiceBinding == null) {
            return new AuthoritativePersistenceResult.NotCommitted<>("FHIR PersistenceService binding is not configured");
        }
        return protect(() -> fhirPersistenceServiceBinding.read(key));
    }

    /** Creates an IU using the Non-FHIR service and checks the required write assurance. */
    public AuthoritativePersistenceResult<InformationUnitPersistenceRecord> create(InformationUnitPersistenceRecord proposed) {
        Objects.requireNonNull(proposed, "proposed must not be null");
        return enforceWriteDurability(protect(() -> nonFhirPersistenceService.create(proposed)));
    }

    /** Appends an IU version using the expected authoritative predecessor. */
    public AuthoritativePersistenceResult<InformationUnitPersistenceRecord> update(
            InformationUnitPersistenceRecord proposed,
            ExpectedAuthoritativeVersion expectedVersion) {
        Objects.requireNonNull(proposed, "proposed must not be null");
        Objects.requireNonNull(expectedVersion, "expectedVersion must not be null");
        return enforceWriteDurability(protect(() -> nonFhirPersistenceService.update(proposed, expectedVersion)));
    }

    /** Reads the latest IU revision from the authoritative Non-FHIR service. */
    public AuthoritativePersistenceResult<InformationUnitPersistenceRecord> read(UUID identifier) {
        Objects.requireNonNull(identifier, "identifier must not be null");
        return protect(() -> nonFhirPersistenceService.read(identifier));
    }

    /** Reads only the latest authoritative IU version. */
    public AuthoritativePersistenceResult<AuthoritativeVersion> readVersion(UUID identifier) {
        Objects.requireNonNull(identifier, "identifier must not be null");
        return protect(() -> nonFhirPersistenceService.readVersion(identifier));
    }

    /** Records an owner-defined Status revision and applies the write durability profile. */
    public AuthoritativePersistenceResult<OperationalStatusRecord> recordStatus(
            OperationalStatusRecord proposed,
            ExpectedAuthoritativeVersion expectedVersion) {
        Objects.requireNonNull(proposed, "proposed must not be null");
        Objects.requireNonNull(expectedVersion, "expectedVersion must not be null");
        return enforceWriteDurability(protect(() -> nonFhirPersistenceService.recordStatus(proposed, expectedVersion)));
    }

    /** Reads the latest owner-defined Status revision. */
    public AuthoritativePersistenceResult<OperationalStatusRecord> readStatus(OperationalStatusKey key) {
        Objects.requireNonNull(key, "key must not be null");
        return protect(() -> nonFhirPersistenceService.readStatus(key));
    }

    /** Converts unexpected collaborator failures into explicit indeterminate outcomes. */
    private <T> AuthoritativePersistenceResult<T> protect(Supplier<AuthoritativePersistenceResult<T>> operation) {
        try {
            AuthoritativePersistenceResult<T> result = operation.get();
            return result == null
                    ? new AuthoritativePersistenceResult.OutcomeUnknown<>("Persistence service returned no outcome")
                    : result;
        } catch (RuntimeException exception) {
            return new AuthoritativePersistenceResult.OutcomeUnknown<>(
                    "Persistence orchestration could not establish the operation outcome",
                    exception);
        }
    }

    /** Prevents a lower assurance commit from being reported as successful persistence. */
    private <T> AuthoritativePersistenceResult<T> enforceWriteDurability(AuthoritativePersistenceResult<T> result) {
        if (result instanceof AuthoritativePersistenceResult.Committed<T> committed
                && !committed.durabilityAssurance().satisfies(durabilityProfile.requiredAssurance())) {
            DurabilityAssurance observed = committed.durabilityAssurance();
            return new AuthoritativePersistenceResult.OutcomeUnknown<>(
                    "Write reached " + observed + " but profile requires " + durabilityProfile.requiredAssurance(),
                    null,
                    observed);
        }
        return result;
    }
}