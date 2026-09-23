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

/**
 * Unchecked exception thrown when an audit event append operation violates repository integrity,
 * such as when attempting to append an event whose ID already exists with conflicting or divergent content.
 */
public class AuditIntegrityException extends IllegalStateException {

    private final String eventId;

    public AuditIntegrityException(String message) {
        super(message);
        this.eventId = null;
    }

    public AuditIntegrityException(String eventId, String message) {
        super(message);
        this.eventId = eventId;
    }

    public AuditIntegrityException(String message, Throwable cause) {
        super(message, cause);
        this.eventId = null;
    }

    public AuditIntegrityException(String eventId, String message, Throwable cause) {
        super(message, cause);
        this.eventId = eventId;
    }

    /**
     * Gets the conflicting audit event identifier, if known.
     *
     * @return the conflicting event ID, or null
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Alias for {@link #getEventId()}.
     *
     * @return the conflicting event ID, or null
     */
    public String eventId() {
        return eventId;
    }
}
