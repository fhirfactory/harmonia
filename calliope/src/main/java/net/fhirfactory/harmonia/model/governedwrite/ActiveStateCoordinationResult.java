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

package net.fhirfactory.harmonia.model.governedwrite;

/**
 * Outcome of an atomic active-state coordination progression attempt (Mneme).
 */
public enum ActiveStateCoordinationResult {
    /**
     * The observed active-state token was successfully and atomically consumed.
     */
    CONSUMED,

    /**
     * The observed active-state token was stale or already consumed by a competing participant.
     */
    STALE,

    /**
     * Active-state coordination is unavailable (e.g. cluster communication failure or timeout).
     */
    UNAVAILABLE
}
