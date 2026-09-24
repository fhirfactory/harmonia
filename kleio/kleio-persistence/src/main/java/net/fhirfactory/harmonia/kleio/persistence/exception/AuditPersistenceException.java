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

package net.fhirfactory.harmonia.kleio.persistence.exception;

/**
 * Unchecked exception thrown when database persistence operations encounter unrecoverable errors,
 * connectivity failures, or scan ceiling violations.
 */
public class AuditPersistenceException extends RuntimeException {

    public AuditPersistenceException(String message) {
        super(message);
    }

    public AuditPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
