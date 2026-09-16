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

package net.fhirfactory.harmonia.themis.audit.service;

import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.audit.model.ThemisAuditEvent;

import java.util.List;

/**
 * Service contract for structured security decision auditing across Harmonia boundaries.
 */
public interface ThemisAuditService {

    /**
     * Records an authorization decision event evaluated by Themis.
     *
     * @param request  authorization request evaluated
     * @param decision authorization decision rendered
     * @return structured recorded audit event
     */
    ThemisAuditEvent recordDecision(ThemisAuthorizationRequest request, ThemisAuthorizationDecision decision);

    /**
     * Records a pre-built audit event.
     *
     * @param event audit event
     */
    void recordEvent(ThemisAuditEvent event);

    /**
     * Retrieves recent security decision audit events up to the given limit.
     *
     * @param limit maximum events to return
     * @return list of audit events ordered chronologically descending
     */
    List<ThemisAuditEvent> getRecentEvents(int limit);

    /**
     * Retrieves audit events linked to a specific correlation identifier.
     *
     * @param correlationId correlation ID
     * @return matching audit events
     */
    List<ThemisAuditEvent> getEventsByCorrelationId(String correlationId);

    /**
     * Retrieves audit events initiated by a specific principal ID.
     *
     * @param principalId principal ID
     * @return matching audit events
     */
    List<ThemisAuditEvent> getEventsByPrincipal(String principalId);

    /**
     * Clears all recorded audit events (primarily for testing and resets).
     */
    void clear();
}
