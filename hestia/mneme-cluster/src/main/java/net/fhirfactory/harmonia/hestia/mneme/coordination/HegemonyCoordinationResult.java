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

package net.fhirfactory.harmonia.hestia.mneme.coordination;

import net.fhirfactory.harmonia.model.governedwrite.ActiveStateToken;

import java.util.Objects;

/**
 * Result hierarchy for transactional Active Hegemony acquisition or confirmation.
 * <p>
 * Preserves semantic active-state generation discipline:
 * <ul>
 *   <li>{@link Acquired}: Transition from NO_HEGEMON to HEGEMON:X, advancing generation/token.</li>
 *   <li>{@link Confirmed}: Validation of existing HEGEMON:X without advancing generation or rewriting cache.</li>
 *   <li>{@link NotHegemon}: Rejection when another instance is currently the Active Hegemon.</li>
 *   <li>{@link Stale}: Rejection when the observed Active-State token version is outdated.</li>
 *   <li>{@link Unavailable}: Rejection when the coordination cache or service is unavailable.</li>
 * </ul>
 */
public sealed interface HegemonyCoordinationResult {

    record Acquired(ActiveStateToken newToken) implements HegemonyCoordinationResult {
        public Acquired {
            Objects.requireNonNull(newToken, "newToken must not be null");
        }
    }

    record Confirmed(ActiveStateToken confirmedToken) implements HegemonyCoordinationResult {
        public Confirmed {
            Objects.requireNonNull(confirmedToken, "confirmedToken must not be null");
        }
    }

    record NotHegemon(String message) implements HegemonyCoordinationResult {
        public NotHegemon {
            Objects.requireNonNull(message, "message must not be null");
        }
    }

    record Stale(String message) implements HegemonyCoordinationResult {
        public Stale {
            Objects.requireNonNull(message, "message must not be null");
        }
    }

    record Unavailable(String message) implements HegemonyCoordinationResult {
        public Unavailable {
            Objects.requireNonNull(message, "message must not be null");
        }
    }
}
