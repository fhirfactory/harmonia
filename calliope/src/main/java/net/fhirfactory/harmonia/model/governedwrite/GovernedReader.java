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

import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

import java.util.Optional;

/**
 * Caller-facing contract interface for governed point read access across Harmonia.
 * <p>
 * Returns an encapsulated {@link GovernedRead} preserving domain payload integrity while
 * carrying active-state (Mneme) and authoritative version (Mnemosyne) coordination tokens.
 */
public interface GovernedReader {

    /**
     * Reads a governed resource by its key within the given security context.
     *
     * @param key             target resource key
     * @param securityContext Themis security and provenance context
     * @param <T>             resource payload type
     * @return an {@link Optional} containing the {@link GovernedRead} envelope if found, or empty if not found
     */
    <T> Optional<GovernedRead<T>> read(ResourceKey key, ThemisSecurityContext securityContext);
}
