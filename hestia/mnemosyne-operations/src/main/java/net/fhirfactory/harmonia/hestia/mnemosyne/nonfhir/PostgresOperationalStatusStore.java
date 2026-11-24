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
import net.fhirfactory.harmonia.hapifhir.persistence.model.DurabilityAssurance;
import net.fhirfactory.harmonia.hestia.mnemosyne.api.OperationalStatusKey;
import net.fhirfactory.harmonia.hestia.mnemosyne.api.OperationalStatusRecord;
import net.fhirfactory.harmonia.model.core.information.access.AuthoritativePreconditionConflict;
import net.fhirfactory.harmonia.model.core.information.access.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.core.information.access.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.model.core.information.access.ResourceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * PostgreSQL adapter for opaque, owner-defined operational Status. Each state transition is
 * append-only and guarded by the expected version; the adapter does not interpret payload content.
 */
final class PostgresOperationalStatusStore {

    private static final Logger log = LoggerFactory.getLogger(PostgresOperationalStatusStore.class);
    private static final String CURRENT_SQL = "SELECT version, payload FROM mnemosyne_operational_status_versions "
            + "WHERE owner_id = ? AND subject_id = ? AND status_type = ? ORDER BY version DESC LIMIT 1";

    private final DataSource dataSource;
    private final PostgresFailureClassifier failureClassifier;

    /** Creates the store with a configured datasource and shared failure classifier. */
    PostgresOperationalStatusStore(DataSource dataSource, PostgresFailureClassifier failureClassifier) {
        this.dataSource = Objects.requireNonNull(dataSource, "dataSource must not be null");
        this.failureClassifier = Objects.requireNonNull(failureClassifier, "failureClassifier must not be null");
    }

    /** Creates a Status record or appends a version matching the expected predecessor. */
    AuthoritativePersistenceResult<OperationalStatusRecord> record(
            OperationalStatusRecord proposed,
            ExpectedAuthoritativeVersion expectedVersion) {
        Objects.requireNonNull(proposed, "proposed must not be null");
        Objects.requireNonNull(expectedVersion, "expectedVersion must not be null");
        ResourceKey resourceKey = proposed.key().resourceKey();
        Connection connection = null;
        AuthoritativeVersion currentVersion = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            StoredStatus current = findCurrent(connection, proposed.key(), true);
            if (expectedVersion.isNone()) {
                if (current != null) {
                    rollback(connection);
                    return replayOrConflict(proposed, current, resourceKey);
                }
                return insertAndCommit(connection, proposed, 1);
            }
            if (current == null) {
                rollback(connection);
                return conflict(resourceKey, expectedVersion, null);
            }
            currentVersion = AuthoritativeVersion.of(current.version());
            long expected = expectedVersion.version().orElseThrow().longValue();
            if (expected < 1 || expected != current.version()) {
                rollback(connection);
                return conflict(resourceKey, expectedVersion, currentVersion);
            }
            long nextVersion = Math.addExact(current.version(), 1L);
            return insertAndCommit(connection, proposed, nextVersion);
        } catch (ArithmeticException exception) {
            rollback(connection);
            return new AuthoritativePersistenceResult.NotCommitted<>("Operational Status version range is exhausted", exception);
        } catch (SQLException exception) {
            rollback(connection);
            if ("23505".equals(exception.getSQLState())) {
                AuthoritativePersistenceResult<OperationalStatusRecord> latest = read(proposed.key());
                if (expectedVersion.isNone()
                        && latest instanceof AuthoritativePersistenceResult.Committed<OperationalStatusRecord> committed
                        && committed.persistedResource().payload().equals(proposed.payload())) {
                    return new AuthoritativePersistenceResult.Committed<>(
                            committed.persistedResource(),
                            committed.authoritativeVersion(),
                            DurabilityAssurance.LOCAL_TRANSACTION_COMMITTED);
                }
                if (latest instanceof AuthoritativePersistenceResult.Committed<OperationalStatusRecord> committed) {
                    currentVersion = committed.authoritativeVersion();
                }
            }
            boolean commitWasAttempted = exception instanceof CommitAttemptSQLException;
            return failureClassifier.classify(exception, resourceKey, expectedVersion, currentVersion, commitWasAttempted);
        } finally {
            close(connection);
        }
    }

    /** Reads the latest status record for an owner/subject/type tuple. */
    AuthoritativePersistenceResult<OperationalStatusRecord> read(OperationalStatusKey key) {
        Objects.requireNonNull(key, "key must not be null");
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            StoredStatus current = findCurrent(connection, key, false);
            if (current == null) {
                return new AuthoritativePersistenceResult.Absent<>("Operational Status is absent: " + key.resourceKey());
            }
            AuthoritativeVersion version = AuthoritativeVersion.of(current.version());
            OperationalStatusRecord record = new OperationalStatusRecord(key, current.payload(), version);
            return new AuthoritativePersistenceResult.Committed<>(record, version, DurabilityAssurance.AUTHORITATIVE_CONFIRMED);
        } catch (SQLException exception) {
            return new AuthoritativePersistenceResult.NotCommitted<>("PostgreSQL Operational Status read failed", exception);
        } finally {
            close(connection);
        }
    }

    /** Inserts and commits one immutable Status version. */
    private AuthoritativePersistenceResult<OperationalStatusRecord> insertAndCommit(
            Connection connection,
            OperationalStatusRecord proposed,
            long version) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO mnemosyne_operational_status_versions "
                        + "(owner_id, subject_id, status_type, version, payload) VALUES (?, ?, ?, ?, ?)")) {
            statement.setString(1, proposed.key().owner());
            statement.setString(2, proposed.key().subject());
            statement.setString(3, proposed.key().statusType());
            statement.setLong(4, version);
            statement.setString(5, proposed.payload());
            statement.executeUpdate();
        }
        try {
            connection.commit();
        } catch (SQLException exception) {
            throw new CommitAttemptSQLException(exception);
        }
        AuthoritativeVersion authoritativeVersion = AuthoritativeVersion.of(version);
        OperationalStatusRecord committed = new OperationalStatusRecord(proposed.key(), proposed.payload(), authoritativeVersion);
        return new AuthoritativePersistenceResult.Committed<>(
                committed,
                authoritativeVersion,
                DurabilityAssurance.LOCAL_TRANSACTION_COMMITTED);
    }

    /** Returns a replay only when an unversioned create has exactly the existing opaque payload. */
    private AuthoritativePersistenceResult<OperationalStatusRecord> replayOrConflict(
            OperationalStatusRecord proposed,
            StoredStatus current,
            ResourceKey resourceKey) {
        AuthoritativeVersion version = AuthoritativeVersion.of(current.version());
        if (current.payload().equals(proposed.payload())) {
            OperationalStatusRecord replay = new OperationalStatusRecord(proposed.key(), current.payload(), version);
            return new AuthoritativePersistenceResult.Committed<>(replay, version, DurabilityAssurance.LOCAL_TRANSACTION_COMMITTED);
        }
        return new AuthoritativePersistenceResult.Conflict<>(
                AuthoritativePreconditionConflict.resourceAlreadyExists(resourceKey, version));
    }

    /** Reads the current status version and optionally locks it for conditional append. */
    private StoredStatus findCurrent(Connection connection, OperationalStatusKey key, boolean lock) throws SQLException {
        String sql = CURRENT_SQL + (lock ? " FOR UPDATE" : "");
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, key.owner());
            statement.setString(2, key.subject());
            statement.setString(3, key.statusType());
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? new StoredStatus(resultSet.getLong(1), resultSet.getString(2)) : null;
            }
        }
    }

    /** Creates a conflict result while retaining the opaque target and known current version. */
    private AuthoritativePersistenceResult.Conflict<OperationalStatusRecord> conflict(
            ResourceKey key,
            ExpectedAuthoritativeVersion expected,
            AuthoritativeVersion current) {
        return new AuthoritativePersistenceResult.Conflict<>(
                AuthoritativePreconditionConflict.expectedVersionMismatch(key, expected, current));
    }

    /** Rolls back an open transaction without replacing the original persistence outcome. */
    private void rollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException exception) {
                log.warn("PostgreSQL Status rollback failed (SQLSTATE {})", exception.getSQLState());
            }
        }
    }

    /** Closes a connection without changing a result already established by COMMIT. */
    private void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException exception) {
                log.warn("PostgreSQL Status connection close failed (SQLSTATE {})", exception.getSQLState());
            }
        }
    }

    /** Carries an explicit marker so callers can distinguish an error during COMMIT. */
    private static final class CommitAttemptSQLException extends SQLException {
        private CommitAttemptSQLException(SQLException cause) {
            super(cause.getMessage(), cause.getSQLState(), cause.getErrorCode(), cause);
        }
    }

    private record StoredStatus(long version, String payload) {
    }
}