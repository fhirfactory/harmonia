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

package net.fhirfactory.harmonia.kleio.persistence.repository;

import net.fhirfactory.harmonia.kleio.persistence.model.PersistedAuditEventRow;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Append-only repository interface for persisting and querying raw AuditEvent rows in PostgreSQL.
 * By design, exposes zero update or delete methods to preserve audit evidence immutability.
 */
public interface AppendOnlyAuditEventRepository {

    /**
     * Attempts to atomically insert an audit event row if not already present.
     * Uses database-level unique constraint collision resolution (ON CONFLICT DO NOTHING).
     *
     * @param eventId      canonical unique event identifier (maps to fhir_id)
     * @param resourceJson serialized FHIR R5 AuditEvent JSON payload
     * @param lastUpdated  timestamp of persistence
     * @return true if a new physical row was inserted (rowsAffected == 1),
     *         false if a conflicting row already existed (rowsAffected == 0)
     */
    boolean insertIfAbsent(String eventId, String resourceJson, Instant lastUpdated);

    /**
     * Looks up an audit event row by its canonical event ID.
     *
     * @param eventId canonical unique event identifier
     * @return optional containing the persisted row if found
     */
    Optional<PersistedAuditEventRow> findByEventId(String eventId);

    /**
     * Retrieves a descending keyset page of candidate audit event rows with IDs strictly less than cursorId.
     *
     * @param cursorId keyset cursor identifier; only rows with id &lt; cursorId are returned
     * @param pageSize maximum number of candidate rows to fetch in this page
     * @return list of candidate rows ordered by id DESC
     */
    List<PersistedAuditEventRow> findCandidatePage(long cursorId, int pageSize);

    /**
     * Checks if there are more candidate rows with IDs strictly less than cursorId.
     *
     * @param cursorId keyset cursor identifier
     * @return true if at least one row exists with id &lt; cursorId
     */
    default boolean hasMoreRows(long cursorId) {
        return !findCandidatePage(cursorId, 1).isEmpty();
    }
}
