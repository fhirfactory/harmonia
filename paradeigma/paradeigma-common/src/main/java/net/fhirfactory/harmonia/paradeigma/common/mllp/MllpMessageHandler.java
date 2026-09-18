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
 * Functional interface for handling an incoming MLLP message and returning an HL7 ACK response.
 */
@FunctionalInterface
public interface MllpMessageHandler {

    /**
     * Process an incoming HL7 message received over MLLP.
     *
     * @param rawHl7Message the raw un-framed HL7 payload
     * @return the raw un-framed HL7 ACK message to return to the sender, or null if no ACK should be returned
     * @throws Exception if an error occurs during message handling
     */
    String handleMessage(String rawHl7Message) throws Exception;
}
