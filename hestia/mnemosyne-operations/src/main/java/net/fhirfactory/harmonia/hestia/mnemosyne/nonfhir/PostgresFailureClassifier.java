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

package net.fhirfactory.harmonia.hestia.mnemosyne.nonfhir;

import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.model.core.information.access.AuthoritativePreconditionConflict;
import net.fhirfactory.harmonia.model.core.information.access.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.core.information.access.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.model.core.information.access.ResourceKey;

import java.sql.SQLException;
import java.util.Objects;

/**
 * Maps PostgreSQL SQLSTATE and transaction phase to the shared authoritative outcome model.
 * A connection failure during COMMIT is indeterminate; it is never reported as a definite failure.
 */
public final class PostgresFailureClassifier {

    /**
     * Classifies a JDBC exception without exposing SQL or payload text in the result message.
     *
     * @param exception database exception
     * @param key affected resource key
     * @param expectedVersion expected predecessor or expected absence
     * @param currentVersion known current version, if available
     * @param commitAttempted true only after the caller began the COMMIT operation
     * @param <T> result payload type
     * @return conflict, indeterminate, or definite not-committed result
     */
    public <T> AuthoritativePersistenceResult<T> classify(
            SQLException exception,
            ResourceKey key,
            ExpectedAuthoritativeVersion expectedVersion,
            AuthoritativeVersion currentVersion,
            boolean commitAttempted) {
        Objects.requireNonNull(exception, "exception must not be null");
        Objects.requireNonNull(key, "key must not be null");
        ExpectedAuthoritativeVersion expected = expectedVersion == null
                ? ExpectedAuthoritativeVersion.none()
                : expectedVersion;
        String sqlState = sqlState(exception);

        if ("23505".equals(sqlState)) {
            AuthoritativePreconditionConflict conflict = expected.isNone()
                    ? AuthoritativePreconditionConflict.resourceAlreadyExists(key, currentVersion)
                    : AuthoritativePreconditionConflict.expectedVersionMismatch(key, expected, currentVersion);
            return new AuthoritativePersistenceResult.Conflict<>(conflict);
        }
        if (commitAttempted && isConnectionFailure(exception)) {
            return new AuthoritativePersistenceResult.OutcomeUnknown<>(
                    "PostgreSQL connection failed while COMMIT was in progress", exception);
        }
        return new AuthoritativePersistenceResult.NotCommitted<>(
                "PostgreSQL transaction did not commit" + (sqlState == null ? "" : " (SQLSTATE " + sqlState + ")"),
                exception);
    }

    /** Finds the first SQLSTATE across a JDBC exception chain and wrapped causes. */
    private String sqlState(SQLException exception) {
        for (Throwable cause = exception; cause != null; cause = cause.getCause()) {
            if (cause instanceof SQLException sqlException) {
                for (SQLException chained = sqlException; chained != null; chained = chained.getNextException()) {
                    if (chained.getSQLState() != null) {
                        return chained.getSQLState();
                    }
                }
            }
        }
        return null;
    }

    /** Recognizes PostgreSQL connection-exception SQLSTATE class 08. */
    private boolean isConnectionFailure(SQLException exception) {
        for (Throwable cause = exception; cause != null; cause = cause.getCause()) {
            if (cause instanceof SQLException sqlException) {
                for (SQLException chained = sqlException; chained != null; chained = chained.getNextException()) {
                    String state = chained.getSQLState();
                    if (state != null && state.startsWith("08")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}