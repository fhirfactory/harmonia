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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.hapifhir.persistence;

import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.model.governedwrite.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * Authoritative persistence port enforcing atomic precondition verification and durable persistence
 * at the Mnemosyne storage boundary.
 *
 * @param <T> FHIR resource type
 */
public interface AuthoritativePersistencePort<T extends IBaseResource> {

    /**
     * Atomically creates a new authoritative resource.
     * Fails with conflict if the resource already exists.
     *
     * @param key unique resource key (type + id)
     * @param proposedState proposed initial resource state
     * @return result indicating COMMITTED or conflict/failure
     */
    AuthoritativePersistenceResult<T> create(ResourceKey key, T proposedState);

    /**
     * Atomically updates an existing authoritative resource if and only if the persisted version
     * matches the expected predecessor version.
     *
     * @param key unique resource key (type + id)
     * @param proposedState proposed updated resource state
     * @param expectedVersion expected authoritative predecessor version
     * @return result indicating COMMITTED or conflict/failure
     */
    AuthoritativePersistenceResult<T> update(
            ResourceKey key,
            T proposedState,
            ExpectedAuthoritativeVersion expectedVersion);
}
