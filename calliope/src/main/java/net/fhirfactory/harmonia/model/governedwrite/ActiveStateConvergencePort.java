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
 * Domain port interface for post-commit active-state cache convergence in Mneme.
 * <p>
 * Ensures that after an authoritative commit in Mnemosyne, the active coordination
 * cache entry in Mneme is safely converged without allowing older commits to overwrite
 * or invalidate newer cached versions.
 */
public interface ActiveStateConvergencePort {

    /**
     * Converges the active-state cache entry to reflect the newly committed authoritative state.
     *
     * @param key               the resource key
     * @param committedResource the committed resource payload
     * @param committedVersion  the authoritative version assigned upon commit
     * @param <T>               the resource payload type
     * @return the convergence outcome ({@link ConvergenceStatus#CONVERGED} or {@link ConvergenceStatus#DEGRADED})
     */
    <T> ConvergenceStatus converge(ResourceKey key, T committedResource, AuthoritativeVersion committedVersion);
}
