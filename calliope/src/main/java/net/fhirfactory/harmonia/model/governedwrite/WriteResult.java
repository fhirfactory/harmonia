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

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * Sealed result model for governed write operations (CREATE and UPDATE).
 * <p>
 * Ensures invalid state combinations (e.g. marking uncommitted results as COMMITTED,
 * or constructing committed results without an authoritative version) cannot be created.
 *
 * @param <T> resource domain payload type
 */
public sealed interface WriteResult<T> extends Serializable permits
        WriteResult.Committed,
        WriteResult.ActiveConflict,
        WriteResult.AuthoritativeConflict,
        WriteResult.OutcomeUnknown,
        WriteResult.NotCommitted {

    ResourceKey key();

    AuthoritativeCommitOutcome commitOutcome();

    ConvergenceStatus convergenceStatus();

    default boolean isCommitted() {
        return commitOutcome() == AuthoritativeCommitOutcome.COMMITTED;
    }

    default boolean isOutcomeUnknown() {
        return commitOutcome() == AuthoritativeCommitOutcome.UNKNOWN;
    }

    default boolean isConflict() {
        return this instanceof ActiveConflict || this instanceof AuthoritativeConflict;
    }

    default Optional<T> committedResource() {
        if (this instanceof Committed<T> committed) {
            return Optional.of(committed.resource());
        }
        return Optional.empty();
    }

    default Optional<AuthoritativeVersion> committedVersion() {
        if (this instanceof Committed<T> committed) {
            return Optional.of(committed.version());
        }
        return Optional.empty();
    }

    default Optional<ActiveStateConflict> activeConflict() {
        if (this instanceof ActiveConflict<T> active) {
            return Optional.of(active.conflict());
        }
        return Optional.empty();
    }

    default Optional<AuthoritativePreconditionConflict> authoritativeConflict() {
        if (this instanceof AuthoritativeConflict<T> auth) {
            return Optional.of(auth.conflict());
        }
        return Optional.empty();
    }

    default Optional<String> degradationReason() {
        if (this instanceof Committed<T> committed) {
            return committed.degradationReason();
        }
        return Optional.empty();
    }

    default Optional<String> failureReason() {
        if (this instanceof NotCommitted<T> notCommitted) {
            return Optional.of(notCommitted.reason());
        }
        if (this instanceof OutcomeUnknown<T> unknown) {
            return Optional.of(unknown.message());
        }
        if (this instanceof ActiveConflict<T> active) {
            return Optional.of(active.conflict().message());
        }
        if (this instanceof AuthoritativeConflict<T> auth) {
            return Optional.of(auth.conflict().message());
        }
        return Optional.empty();
    }

    // Static factory methods

    static <T> Committed<T> committed(ResourceKey key, T resource, AuthoritativeVersion version) {
        return new Committed<>(key, resource, version, ConvergenceStatus.CONVERGED, null);
    }

    static <T> Committed<T> committedDegraded(ResourceKey key, T resource, AuthoritativeVersion version, String degradationReason) {
        return new Committed<>(key, resource, version, ConvergenceStatus.DEGRADED, degradationReason);
    }

    static <T> ActiveConflict<T> activeStateConflict(ActiveStateConflict conflict) {
        return new ActiveConflict<>(conflict);
    }

    static <T> ActiveConflict<T> activeStateConflict(ResourceKey key, String message) {
        return new ActiveConflict<>(ActiveStateConflict.of(key, message));
    }

    static <T> AuthoritativeConflict<T> authoritativeConflict(AuthoritativePreconditionConflict conflict) {
        return new AuthoritativeConflict<>(conflict);
    }

    static <T> OutcomeUnknown<T> outcomeUnknown(ResourceKey key, String message) {
        return new OutcomeUnknown<>(key, message);
    }

    static <T> NotCommitted<T> notCommitted(ResourceKey key, String reason) {
        return new NotCommitted<>(key, reason);
    }

    // Permitted sealed records

    record Committed<T>(
            ResourceKey key,
            T resource,
            AuthoritativeVersion version,
            ConvergenceStatus convergenceStatus,
            String degradationMessage
    ) implements WriteResult<T> {
        public Committed {
            Objects.requireNonNull(key, "key must not be null");
            Objects.requireNonNull(resource, "resource must not be null");
            Objects.requireNonNull(version, "version must not be null");
            Objects.requireNonNull(convergenceStatus, "convergenceStatus must not be null");
            if (convergenceStatus != ConvergenceStatus.CONVERGED && convergenceStatus != ConvergenceStatus.DEGRADED) {
                throw new IllegalArgumentException("Committed result convergence status must be CONVERGED or DEGRADED");
            }
        }

        @Override
        public AuthoritativeCommitOutcome commitOutcome() {
            return AuthoritativeCommitOutcome.COMMITTED;
        }

        @Override
        public Optional<String> degradationReason() {
            return Optional.ofNullable(degradationMessage);
        }
    }

    record ActiveConflict<T>(
            ActiveStateConflict conflict
    ) implements WriteResult<T> {
        public ActiveConflict {
            Objects.requireNonNull(conflict, "conflict must not be null");
        }

        @Override
        public ResourceKey key() {
            return conflict.key();
        }

        @Override
        public AuthoritativeCommitOutcome commitOutcome() {
            return AuthoritativeCommitOutcome.NOT_COMMITTED;
        }

        @Override
        public ConvergenceStatus convergenceStatus() {
            return ConvergenceStatus.NOT_APPLICABLE;
        }
    }

    record AuthoritativeConflict<T>(
            AuthoritativePreconditionConflict conflict
    ) implements WriteResult<T> {
        public AuthoritativeConflict {
            Objects.requireNonNull(conflict, "conflict must not be null");
        }

        @Override
        public ResourceKey key() {
            return conflict.key();
        }

        @Override
        public AuthoritativeCommitOutcome commitOutcome() {
            return AuthoritativeCommitOutcome.NOT_COMMITTED;
        }

        @Override
        public ConvergenceStatus convergenceStatus() {
            return ConvergenceStatus.NOT_APPLICABLE;
        }
    }

    record OutcomeUnknown<T>(
            ResourceKey key,
            String message
    ) implements WriteResult<T> {
        public OutcomeUnknown {
            Objects.requireNonNull(key, "key must not be null");
            Objects.requireNonNull(message, "message must not be null");
        }

        @Override
        public AuthoritativeCommitOutcome commitOutcome() {
            return AuthoritativeCommitOutcome.UNKNOWN;
        }

        @Override
        public ConvergenceStatus convergenceStatus() {
            return ConvergenceStatus.NOT_APPLICABLE;
        }
    }

    record NotCommitted<T>(
            ResourceKey key,
            String reason
    ) implements WriteResult<T> {
        public NotCommitted {
            Objects.requireNonNull(key, "key must not be null");
            Objects.requireNonNull(reason, "reason must not be null");
        }

        @Override
        public AuthoritativeCommitOutcome commitOutcome() {
            return AuthoritativeCommitOutcome.NOT_COMMITTED;
        }

        @Override
        public ConvergenceStatus convergenceStatus() {
            return ConvergenceStatus.NOT_APPLICABLE;
        }
    }
}
