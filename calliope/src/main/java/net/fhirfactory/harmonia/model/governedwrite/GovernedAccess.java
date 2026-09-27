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
 * Unified application-facing managed-information boundary composing read and write operations.
 * <p>
 * Applications interact exclusively through this managed boundary without interacting directly
 * with raw infrastructure caches or authoritative persistence stores.
 * Governed information has no physical DELETE semantics (ADR-020).
 */
public interface GovernedAccess extends GovernedReader, GovernedWriter {
}
