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

package net.fhirfactory.harmonia.paradeigma.common.mllp;

/**
 * Thrown when an MLLP TCP socket connection cannot be established, is rejected, or is abruptly dropped.
 */
public class MllpConnectionException extends MllpException {

    public MllpConnectionException(String message) {
        super(message);
    }

    public MllpConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
