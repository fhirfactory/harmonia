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

package net.fhirfactory.harmonia.mllpgateway.service;

import org.hl7.fhir.r5.model.Provenance;

import java.util.List;
import java.util.Optional;

/**
 * Service contract for managing the lifecycle and persistence of FHIR Provenance resources.
 */
public interface ProvenanceService {

    /**
     * Persists a new Provenance resource into cache / storage.
     */
    Provenance create(Provenance provenance);

    /**
     * Retrieves a Provenance resource by its logical ID.
     */
    Optional<Provenance> getById(String id);

    /**
     * Updates an existing Provenance resource.
     */
    Provenance update(String id, Provenance provenance);

    /**
     * Deletes a Provenance resource by its logical ID.
     */
    boolean delete(String id);

    /**
     * Retrieves all cached Provenance resources.
     */
    List<Provenance> getAll();

    /**
     * Searches Provenance resources matching the provided criteria.
     */
    List<Provenance> search(String id, String target, String agent, String patient);

    /**
     * Counts the total number of Provenance resources.
     */
    long count();

    /**
     * Clears all in-memory and cached Provenance resources (primarily for testing).
     */
    void clear();
}
