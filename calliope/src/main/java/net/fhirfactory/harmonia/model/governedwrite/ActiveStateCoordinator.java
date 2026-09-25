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
 * Caller-facing coordinator interface for distributed active-state observation and progression (Mneme).
 * <p>
 * Encapsulates distributed active-state concurrency management without exposing caching mechanics,
 * transport protocols, or raw entry versions.
 */
public interface ActiveStateCoordinator {

    /**
     * Observes the current active-state token for the specified resource key.
     *
     * @param key the resource key to observe
     * @return the opaque active-state token representing the current active state in Mneme
     */
    ActiveStateToken observe(ResourceKey key);

    /**
     * Atomically consumes the observed active-state token for the specified resource key.
     *
     * @param key           the resource key
     * @param observedToken the active-state token previously observed
     * @return outcome of the consumption attempt ({@link ActiveStateCoordinationResult#CONSUMED},
     *         {@link ActiveStateCoordinationResult#STALE}, or {@link ActiveStateCoordinationResult#UNAVAILABLE})
     */
    ActiveStateCoordinationResult consume(ResourceKey key, ActiveStateToken observedToken);
}
