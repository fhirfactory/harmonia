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

import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;

/**
 * Handle representing an active asynchronous consumer subscription.
 */
public interface PetasosSubscription extends AutoCloseable {

    /**
     * Returns the destination this subscription is listening to.
     */
    PetasosDestination getDestination();

    /**
     * Checks if the subscription is currently active and consuming.
     */
    boolean isActive();

    /**
     * Closes the consumer and cancels the message subscription.
     */
    void unsubscribe();

    @Override
    default void close() {
        unsubscribe();
    }
}
