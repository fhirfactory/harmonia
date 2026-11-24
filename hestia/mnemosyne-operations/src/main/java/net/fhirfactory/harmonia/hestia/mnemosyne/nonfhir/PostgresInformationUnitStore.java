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
import java.util.UUID;

/**
 * PostgreSQL IU persistence adapter. It commits identity and the first version atomically, appends
 * later versions without overwriting history, and uses optimistic predecessor checks for updates.
 */
final class PostgresInformationUnitStore {

    private static final Logger log = LoggerFactory.getLogger(PostgresInformationUnitStore.class);
    private static final String CURRENT_SQL = "SELECT i.topic_name, v.version, v.canonical_json, v.content_sha256 "
            + "FROM mnemosyne_iu_identity i JOIN mnemosyne_iu_versions v ON v.iu_uuid = i.iu_uuid "
            + "WHERE i.iu_uuid = ? ORDER BY v.version DESC LIMIT 1";

    private final DataSource dataSource;
    private final PostgresFailureClassifier failureClassifier;

    /** Creates the store with a configured PostgreSQL datasource and shared failure classifier. */
    PostgresInformationUnitStore(DataSource dataSource, PostgresFailureClassifier failureClassifier) {
        this.dataSource = Objects.requireNonNull(dataSource, "dataSource must not be null");
        this.failureClassifier = Objects.requireNonNull(failureClassifier, "failureClassifier must not be null");
    }

    /** Creates version 1 or recognizes an identical replay of an already-created IU. */
    AuthoritativePersistenceResult<InformationUnitPersistenceRecord> create(InformationUnitPersistenceRecord proposed) {
        Objects.requireNonNull(proposed, "proposed must not be null");
        requireProposal(proposed);
        ResourceKey key = resourceKey(proposed.identifier());
        Connection connection = null;
        boolean commitAttempted = false;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            StoredVersion existing = findCurrent(connection, proposed.identifier(), false);
            if (existing != null) {
                rollback(connection);
                return replayOrConflict(proposed, existing, key);
            }

            insertIdentity(connection, proposed);
            insertVersion(connection, proposed.withVersion(1));
            commitAttempted = true;
            connection.commit();
            InformationUnitPersistenceRecord committed = proposed.withVersion(1);
            return committed(committed, 1, DurabilityAssurance.LOCAL_TRANSACTION_COMMITTED);
        } catch (SQLException exception) {
            rollback(connection);
            if ("23505".equals(exception.getSQLState())) {
                return verifyCreateReplay(proposed, key, exception);
            }
            return failureClassifier.classify(
                    exception, key, ExpectedAuthoritativeVersion.none(), null, commitAttempted);
        } finally {
            close(connection);
        }
    }

    /** Appends the next IU version only when the persisted predecessor equals the expected version. */
    AuthoritativePersistenceResult<InformationUnitPersistenceRecord> update(
            InformationUnitPersistenceRecord proposed,
            ExpectedAuthoritativeVersion expectedVersion) {
        Objects.requireNonNull(proposed, "proposed must not be null");
        Objects.requireNonNull(expectedVersion, "expectedVersion must not be null");
        requireProposal(proposed);
        ResourceKey key = resourceKey(proposed.identifier());
        if (expectedVersion.isNone()) {
            return new AuthoritativePersistenceResult.Conflict<>(
                    AuthoritativePreconditionConflict.expectedVersionMismatch(key, expectedVersion, null));
        }

        Connection connection = null;
        boolean commitAttempted = false;
        AuthoritativeVersion currentVersion = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            StoredVersion current = findCurrent(connection, proposed.identifier(), true);
            if (current == null) {
                rollback(connection);
                return conflict(key, expectedVersion, null);
            }
            currentVersion = AuthoritativeVersion.of(current.version());
            if (!current.topicName().equals(proposed.topicName())
                    || current.version() != expectedVersion.version().orElseThrow().longValue()) {
                rollback(connection);
                return conflict(key, expectedVersion, currentVersion);
            }

            long nextVersion = Math.addExact(current.version(), 1L);
            InformationUnitPersistenceRecord next = proposed.withVersion(nextVersion);
            insertVersion(connection, next);
            commitAttempted = true;
            connection.commit();
            return committed(next, nextVersion, DurabilityAssurance.LOCAL_TRANSACTION_COMMITTED);
        } catch (ArithmeticException exception) {
            rollback(connection);
            return new AuthoritativePersistenceResult.NotCommitted<>("InformationUnit version range is exhausted", exception);
        } catch (SQLException exception) {
            rollback(connection);
            if ("23505".equals(exception.getSQLState())) {
                AuthoritativePersistenceResult<InformationUnitPersistenceRecord> latest = read(proposed.identifier());
                AuthoritativeVersion actual = latest instanceof AuthoritativePersistenceResult.Committed<InformationUnitPersistenceRecord> committed
                        ? committed.authoritativeVersion()
                        : currentVersion;
                return conflict(key, expectedVersion, actual);
            }
            return failureClassifier.classify(exception, key, expectedVersion, currentVersion, commitAttempted);
        } finally {
            close(connection);
        }
    }

    /** Reads and returns the latest IU revision, or a typed absence/failure result. */
    AuthoritativePersistenceResult<InformationUnitPersistenceRecord> read(UUID identifier) {
        Objects.requireNonNull(identifier, "identifier must not be null");
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            StoredVersion current = findCurrent(connection, identifier, false);
            if (current == null) {
                return new AuthoritativePersistenceResult.Absent<>("InformationUnit is absent: " + identifier);
            }
            InformationUnitPersistenceRecord record = current.toRecord(identifier);
            return committed(record, current.version(), DurabilityAssurance.AUTHORITATIVE_CONFIRMED);
        } catch (SQLException exception) {
            return new AuthoritativePersistenceResult.NotCommitted<>("PostgreSQL InformationUnit read failed", exception);
        } finally {
            close(connection);
        }
    }

    /** Reads only the latest authoritative IU version. */
    AuthoritativePersistenceResult<AuthoritativeVersion> readVersion(UUID identifier) {
        Objects.requireNonNull(identifier, "identifier must not be null");
        Connection connection = null;
        String sql = "SELECT MAX(version) FROM mnemosyne_iu_versions WHERE iu_uuid = ?";
        try {
            connection = dataSource.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setObject(1, identifier);
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    long version = resultSet.getLong(1);
                    if (resultSet.wasNull()) {
                        return new AuthoritativePersistenceResult.Absent<>("InformationUnit is absent: " + identifier);
                    }
                    AuthoritativeVersion authoritativeVersion = AuthoritativeVersion.of(version);
                    return new AuthoritativePersistenceResult.Committed<>(
                            authoritativeVersion,
                            authoritativeVersion,
                            DurabilityAssurance.AUTHORITATIVE_CONFIRMED);
                }
            }
        } catch (SQLException exception) {
            return new AuthoritativePersistenceResult.NotCommitted<>("PostgreSQL InformationUnit version read failed", exception);
        } finally {
            close(connection);
        }
    }

    /** Re-reads a concurrently created IU after a uniqueness conflict to verify an identical replay. */
    private AuthoritativePersistenceResult<InformationUnitPersistenceRecord> verifyCreateReplay(
            InformationUnitPersistenceRecord proposed,
            ResourceKey key,
            SQLException originalException) {
        AuthoritativePersistenceResult<InformationUnitPersistenceRecord> existing = read(proposed.identifier());
        if (existing instanceof AuthoritativePersistenceResult.Committed<InformationUnitPersistenceRecord> committed) {
            return replayOrConflict(proposed, new StoredVersion(
                    committed.persistedResource().topicName(),
                    committed.persistedResource().version(),
                    committed.persistedResource().canonicalJson(),
                    committed.persistedResource().sha256()), key);
        }
        if (existing instanceof AuthoritativePersistenceResult.Conflict<InformationUnitPersistenceRecord> conflict) {
            return conflict;
        }
        return new AuthoritativePersistenceResult.OutcomeUnknown<>(
                "Could not verify an IU after a PostgreSQL uniqueness conflict: " + key,
                originalException);
    }

    /** Returns committed for byte-identical create replay, otherwise a resource-exists conflict. */
    private AuthoritativePersistenceResult<InformationUnitPersistenceRecord> replayOrConflict(
            InformationUnitPersistenceRecord proposed,
            StoredVersion existing,
            ResourceKey key) {
        if (proposed.topicName().equals(existing.topicName())
                && proposed.sha256().equals(existing.sha256())) {
            InformationUnitPersistenceRecord replay = existing.toRecord(proposed.identifier());
            return committed(replay, existing.version(), DurabilityAssurance.LOCAL_TRANSACTION_COMMITTED);
        }
        return new AuthoritativePersistenceResult.Conflict<>(
                AuthoritativePreconditionConflict.resourceAlreadyExists(key, AuthoritativeVersion.of(existing.version())));
    }

    /** Reads the latest row and optionally locks it for a conditional append. */
    private StoredVersion findCurrent(Connection connection, UUID identifier, boolean lock) throws SQLException {
        String sql = CURRENT_SQL + (lock ? " FOR UPDATE OF v" : "");
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, identifier);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next()
                        ? new StoredVersion(resultSet.getString(1), resultSet.getLong(2), resultSet.getString(3), resultSet.getString(4).trim())
                        : null;
            }
        }
    }

    /** Inserts the stable IU identity row within the caller's transaction. */
    private void insertIdentity(Connection connection, InformationUnitPersistenceRecord proposed) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO mnemosyne_iu_identity (iu_uuid, topic_name) VALUES (?, ?)")) {
            statement.setObject(1, proposed.identifier());
            statement.setString(2, proposed.topicName());
            statement.executeUpdate();
        }
    }

    /** Inserts one immutable IU revision within the caller's transaction. */
    private void insertVersion(Connection connection, InformationUnitPersistenceRecord record) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO mnemosyne_iu_versions (iu_uuid, version, canonical_json, content_sha256) VALUES (?, ?, ?, ?)")) {
            statement.setObject(1, record.identifier());
            statement.setLong(2, record.version());
            statement.setString(3, record.canonicalJson());
            statement.setString(4, record.sha256());
            statement.executeUpdate();
        }
    }

    /** Ensures persistence inputs are disassembled, unversioned proposals. */
    private void requireProposal(InformationUnitPersistenceRecord proposed) {
        if (proposed.version() != 0) {
            throw new IllegalArgumentException("IU persistence proposal must not contain a caller-assigned version");
        }
    }

    /** Builds a Calliope key without placing representation content in diagnostics. */
    private ResourceKey resourceKey(UUID identifier) {
        return ResourceKey.of("InformationUnit", identifier.toString());
    }

    /** Creates an expected-version conflict for an IU update. */
    private AuthoritativePersistenceResult<InformationUnitPersistenceRecord> conflict(
            ResourceKey key,
            ExpectedAuthoritativeVersion expected,
            AuthoritativeVersion current) {
        return new AuthoritativePersistenceResult.Conflict<>(
                AuthoritativePreconditionConflict.expectedVersionMismatch(key, expected, current));
    }

    /** Builds a typed committed result with its observed durability. */
    private AuthoritativePersistenceResult.Committed<InformationUnitPersistenceRecord> committed(
            InformationUnitPersistenceRecord record,
            long version,
            DurabilityAssurance assurance) {
        return new AuthoritativePersistenceResult.Committed<>(record, AuthoritativeVersion.of(version), assurance);
    }

    /** Rolls back only when a connection exists; rollback errors do not hide the original outcome. */
    private void rollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException exception) {
                log.warn("PostgreSQL IU rollback failed (SQLSTATE {})", exception.getSQLState());
            }
        }
    }

    /** Closes a connection without replacing a previously established commit result. */
    private void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException exception) {
                log.warn("PostgreSQL IU connection close failed (SQLSTATE {})", exception.getSQLState());
            }
        }
    }

    /** Latest version row materialized without the JSON body in diagnostics. */
    private record StoredVersion(String topicName, long version, String canonicalJson, String sha256) {
        private InformationUnitPersistenceRecord toRecord(UUID identifier) {
            return new InformationUnitPersistenceRecord(identifier, topicName, version, canonicalJson, sha256);
        }
    }
}