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

package net.fhirfactory.harmonia.model.core.workflow.tasks;

import net.fhirfactory.harmonia.model.core.naming.Topic;

import java.util.Objects;
import java.util.Optional;

/** Queries the live input/output payload lists of one mutable Pragma interchange object. */
public final class PragmaPayloadSelector {

    private final Pragma pragma;

    /**
     * Creates a selector over the task's current payload lists.
     *
     * @param pragma task whose current payloads are queried
     * @throws NullPointerException if pragma is null
     */
    public PragmaPayloadSelector(Pragma pragma) {
        this.pragma = Objects.requireNonNull(pragma, "pragma must not be null");
    }

    /** Finds the first input matching the supplied non-null criteria; null criteria are wildcards. */
    public Optional<ErgonPayload> findInput(Topic container, Topic content) {
        return pragma.getInput().stream()
                .filter(payload -> container == null || Objects.equals(payload.getPayloadContainer(), container))
                .filter(payload -> content == null || Objects.equals(payload.getPayloadContent(), content))
                .findFirst();
    }

    /** Finds the first output matching the supplied non-null criteria; null criteria are wildcards. */
    public Optional<ErgonPayload> findOutput(Topic container, Topic content) {
        return pragma.getOutput().stream()
                .filter(payload -> container == null || Objects.equals(payload.getPayloadContainer(), container))
                .filter(payload -> content == null || Objects.equals(payload.getPayloadContent(), content))
                .findFirst();
    }

    /** Finds the first input at the specified zero-based payload order. */
    public Optional<ErgonPayload> findInputByOrder(int order) {
        return pragma.getInput().stream()
                .filter(payload -> payload.getPayloadOrder() != null && payload.getPayloadOrder() == order)
                .findFirst();
    }

    /** Finds the first output at the specified zero-based payload order. */
    public Optional<ErgonPayload> findOutputByOrder(int order) {
        return pragma.getOutput().stream()
                .filter(payload -> payload.getPayloadOrder() != null && payload.getPayloadOrder() == order)
                .findFirst();
    }
}