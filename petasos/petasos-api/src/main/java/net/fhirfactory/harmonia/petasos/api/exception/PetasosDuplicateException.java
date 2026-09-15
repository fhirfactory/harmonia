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

package net.fhirfactory.harmonia.petasos.api.exception;

/**
 * Thrown or logged when a message is identified as a duplicate and rejected/dropped.
 */
public class PetasosDuplicateException extends PetasosMessagingException {

    private final String duplicateId;

    public PetasosDuplicateException(String duplicateId, String message) {
        super(message);
        this.duplicateId = duplicateId;
    }

    public String getDuplicateId() {
        return duplicateId;
    }
}
