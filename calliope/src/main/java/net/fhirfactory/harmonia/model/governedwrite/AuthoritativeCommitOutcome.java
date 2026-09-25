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
 * First-class authoritative commit outcome of a governed write operation.
 */
public enum AuthoritativeCommitOutcome {
    /**
     * Authoritatively committed and persisted to the durable storage boundary (Mnemosyne).
     */
    COMMITTED,

    /**
     * Authoritatively confirmed not committed (e.g. active conflict, precondition mismatch, validation rejection).
     */
    NOT_COMMITTED,

    /**
     * Authoritative commit outcome is unknown (e.g. database network timeout after write dispatch).
     * Must NOT be assumed failed or retried blindly.
     */
    UNKNOWN
}
