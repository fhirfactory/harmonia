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

package net.fhirfactory.harmonia.petasos.api.consumer;

import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;

/**
 * Functional interface for receiving and processing messages delivered by Petasos.
 */
@FunctionalInterface
public interface PetasosMessageHandler {

    /**
     * Invoked when a message is delivered from a subscribed Petasos destination.
     *
     * @param message the received Petasos envelope containing headers and opaque payload
     * @param context the context used to acknowledge, reject, or inspect delivery status
     * @throws Exception if processing fails (triggering consumer error handling)
     */
    void onMessage(PetasosMessage message, PetasosMessageContext context) throws Exception;
}
