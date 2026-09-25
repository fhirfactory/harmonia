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

/**
 * Caller-facing contract interface for governed writes across Harmonia.
 * <p>
 * Exposes only CREATE and UPDATE operations. Governed information has no physical DELETE semantics (ADR-020).
 * Update requires a {@link GovernedRead} to guarantee predecessor concurrency verification.
 */
public interface GovernedWriter {

    /**
     * Initiates a governed CREATE operation for a new resource.
     *
     * @param key             target resource key
     * @param resource        the resource payload to create
     * @param securityContext Themis security and provenance context
     * @param <T>             resource payload type
     * @return result of the governed write
     */
    <T> WriteResult<T> create(ResourceKey key, T resource, ThemisSecurityContext securityContext);

    /**
     * Initiates a governed UPDATE operation against a previously read resource.
     *
     * @param current         current governed read state containing active and authoritative predecessor tokens
     * @param proposed        proposed updated resource payload
     * @param securityContext Themis security and provenance context
     * @param <T>             resource payload type
     * @return result of the governed write
     */
    <T> WriteResult<T> update(GovernedRead<T> current, T proposed, ThemisSecurityContext securityContext);
}
