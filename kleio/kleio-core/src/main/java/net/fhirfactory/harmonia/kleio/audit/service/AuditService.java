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

package net.fhirfactory.harmonia.kleio.audit.service;

import net.fhirfactory.harmonia.kleio.audit.model.HarmoniaAuditEvent;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;

import java.util.List;
import java.util.Optional;

/**
 * Append-only repository service contract for recording and querying structured security decision
 * and activity audit events across Harmonia boundaries.
 */
public interface AuditService {

    /**
     * Appends an immutable audit event to the append-only repository.
     *
     * @param event the audit event to record
     * @return the recorded audit event
     * @throws AuditIntegrityException if an event with the same ID exists with divergent content
     */
    HarmoniaAuditEvent append(HarmoniaAuditEvent event);

    /**
     * Retrieves an audit event by its unique identifier.
     *
     * @param eventId event identifier
     * @return optional containing the matching audit event if found
     */
    Optional<HarmoniaAuditEvent> findById(String eventId);

    /**
     * Retrieves audit events linked to a specific correlation identifier.
     *
     * @param correlationId correlation ID
     * @return matching audit events
     */
    List<HarmoniaAuditEvent> findByCorrelationId(String correlationId);

    /**
     * Retrieves audit events associated with a specific principal ID (initiating or executing).
     *
     * @param principalId principal ID
     * @return matching audit events
     */
    List<HarmoniaAuditEvent> findByPrincipal(String principalId);

    /**
     * Retrieves recent security decision audit events up to the given limit, ordered chronologically descending.
     *
     * @param limit maximum events to return
     * @return list of audit events ordered chronologically descending
     */
    List<HarmoniaAuditEvent> getRecentEvents(int limit);

    /**
     * Convenience method to evaluate and append an audit event from an authorization request and decision pair.
     *
     * @param request  authorization request evaluated
     * @param decision authorization decision rendered
     * @return structured recorded audit event
     */
    HarmoniaAuditEvent recordDecision(ThemisAuthorizationRequest request, ThemisAuthorizationDecision decision);

    /**
     * Backward-compatible alias for {@link #findByCorrelationId(String)}.
     *
     * @param correlationId correlation ID
     * @return matching audit events
     */
    default List<HarmoniaAuditEvent> getEventsByCorrelationId(String correlationId) {
        return findByCorrelationId(correlationId);
    }

    /**
     * Backward-compatible alias for {@link #findByPrincipal(String)}.
     *
     * @param principalId principal ID
     * @return matching audit events
     */
    default List<HarmoniaAuditEvent> getEventsByPrincipal(String principalId) {
        return findByPrincipal(principalId);
    }
}
