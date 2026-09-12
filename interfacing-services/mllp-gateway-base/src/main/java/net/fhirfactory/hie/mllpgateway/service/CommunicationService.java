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

package net.fhirfactory.hie.mllpgateway.service;

import org.hl7.fhir.r5.model.Communication;

import java.util.List;
import java.util.Optional;

/**
 * Service contract for managing the lifecycle and persistence of FHIR Communication resources.
 */
public interface CommunicationService {

    /**
     * Persists a new Communication resource into cache / storage.
     */
    Communication create(Communication communication);

    /**
     * Retrieves a Communication resource by its logical ID.
     */
    Optional<Communication> getById(String id);

    /**
     * Updates an existing Communication resource.
     */
    Communication update(String id, Communication communication);

    /**
     * Deletes a Communication resource by its logical ID.
     */
    boolean delete(String id);

    /**
     * Retrieves all cached Communication resources.
     */
    List<Communication> getAll();

    /**
     * Searches Communication resources matching the provided criteria.
     */
    List<Communication> search(String id, String patientId, String identifier);

    /**
     * Counts the total number of Communication resources.
     */
    long count();

    /**
     * Clears all in-memory Communication resources (primarily for testing).
     */
    void clear();
}
