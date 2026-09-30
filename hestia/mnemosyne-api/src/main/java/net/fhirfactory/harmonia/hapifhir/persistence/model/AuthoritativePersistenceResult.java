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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.hapifhir.persistence.model;

import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeCommitOutcome;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativePreconditionConflict;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * Type-safe sealed result hierarchy representing the outcome of an authoritative persistence operation.
 *
 * @param <T> resource type
 */
public sealed interface AuthoritativePersistenceResult<T> extends Serializable
        permits AuthoritativePersistenceResult.Committed,
                AuthoritativePersistenceResult.Conflict,
                AuthoritativePersistenceResult.NotCommitted,
                AuthoritativePersistenceResult.OutcomeUnknown {

    AuthoritativeCommitOutcome outcome();

    default boolean isCommitted() {
        return outcome() == AuthoritativeCommitOutcome.COMMITTED;
    }

    record Committed<T>(
            T persistedResource,
            AuthoritativeVersion authoritativeVersion
    ) implements AuthoritativePersistenceResult<T> {
        public Committed {
            Objects.requireNonNull(persistedResource, "persistedResource must not be null");
            Objects.requireNonNull(authoritativeVersion, "authoritativeVersion must not be null");
        }

        @Override
        public AuthoritativeCommitOutcome outcome() {
            return AuthoritativeCommitOutcome.COMMITTED;
        }
    }

    record Conflict<T>(
            AuthoritativePreconditionConflict conflict
    ) implements AuthoritativePersistenceResult<T> {
        public Conflict {
            Objects.requireNonNull(conflict, "conflict must not be null");
        }

        @Override
        public AuthoritativeCommitOutcome outcome() {
            return AuthoritativeCommitOutcome.NOT_COMMITTED;
        }
    }

    record NotCommitted<T>(
            String failureMessage,
            Throwable cause
    ) implements AuthoritativePersistenceResult<T> {
        public NotCommitted {
            Objects.requireNonNull(failureMessage, "failureMessage must not be null");
        }

        public NotCommitted(String failureMessage) {
            this(failureMessage, null);
        }

        @Override
        public AuthoritativeCommitOutcome outcome() {
            return AuthoritativeCommitOutcome.NOT_COMMITTED;
        }

        public Optional<Throwable> causeOptional() {
            return Optional.ofNullable(cause);
        }
    }

    record OutcomeUnknown<T>(
            String message,
            Throwable cause
    ) implements AuthoritativePersistenceResult<T> {
        public OutcomeUnknown {
            Objects.requireNonNull(message, "message must not be null");
        }

        public OutcomeUnknown(String message) {
            this(message, null);
        }

        @Override
        public AuthoritativeCommitOutcome outcome() {
            return AuthoritativeCommitOutcome.UNKNOWN;
        }

        public Optional<Throwable> causeOptional() {
            return Optional.ofNullable(cause);
        }
    }
}
