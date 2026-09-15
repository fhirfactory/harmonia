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

package net.fhirfactory.harmonia.erga.base;

/**
 * Thrown when an {@link ErgonBase} activity encounters a processing, parsing, or validation error.
 */
public class ErgonException extends RuntimeException {

    private final String ergonId;
    private final String pragmaId;

    public ErgonException(String message) {
        super(message);
        this.ergonId = null;
        this.pragmaId = null;
    }

    public ErgonException(String message, Throwable cause) {
        super(message, cause);
        this.ergonId = null;
        this.pragmaId = null;
    }

    public ErgonException(String ergonId, String pragmaId, String message) {
        super(String.format("[%s] Error processing Pragma/%s: %s", ergonId, pragmaId, message));
        this.ergonId = ergonId;
        this.pragmaId = pragmaId;
    }

    public ErgonException(String ergonId, String pragmaId, String message, Throwable cause) {
        super(String.format("[%s] Error processing Pragma/%s: %s", ergonId, pragmaId, message), cause);
        this.ergonId = ergonId;
        this.pragmaId = pragmaId;
    }

    public String getErgonId() {
        return ergonId;
    }

    public String getPragmaId() {
        return pragmaId;
    }
}
