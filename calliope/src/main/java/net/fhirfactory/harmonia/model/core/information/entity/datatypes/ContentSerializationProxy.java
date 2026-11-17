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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import net.fhirfactory.harmonia.model.core.information.entity.valuesets.ContentMode;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * Carries only validated portable Content values through Java serialization.
 * <p>
 * The proxy keeps the in-memory-only working manifestation out of serialized
 * state and reconstructs the domain value through its invariant-checking
 * constructor.
 */
final class ContentSerializationProxy implements Serializable {

    private static final long serialVersionUID = 1L;

    private final ContentMode mode;
    private final String textContent;
    private final byte[] binaryContent;
    private final String reference;

    /**
     * Captures the portable values of an already validated Content object.
     *
     * @param content portable content to capture
     */
    ContentSerializationProxy(Content content) {
        this.mode = content.mode();
        this.textContent = content.getTextContent();
        this.binaryContent = content.getBinaryContent();
        this.reference = content.getReference();
    }

    /**
     * Reconstructs Content through its validating JSON-compatible constructor.
     *
     * @return the validated Content value
     * @throws ObjectStreamException if the serialized mode and payload disagree
     */
    private Object readResolve() throws ObjectStreamException {
        try {
            return new Content(mode, textContent, binaryContent, reference);
        } catch (IllegalArgumentException exception) {
            InvalidObjectException invalidObjectException = new InvalidObjectException(exception.getMessage());
            invalidObjectException.initCause(exception);
            throw invalidObjectException;
        }
    }
}