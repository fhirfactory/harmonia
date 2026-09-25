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
 * Status of active coordination cache (Mneme) convergence following an authoritative commit.
 */
public enum ConvergenceStatus {
    /**
     * Mneme cache entry successfully converged to the committed state.
     */
    CONVERGED,

    /**
     * Authoritatively committed to Mnemosyne, but Mneme cache update failed or was degraded.
     * The authoritative write remains valid and committed.
     */
    DEGRADED,

    /**
     * Convergence is not applicable (operation was not authoritatively committed).
     */
    NOT_APPLICABLE
}
