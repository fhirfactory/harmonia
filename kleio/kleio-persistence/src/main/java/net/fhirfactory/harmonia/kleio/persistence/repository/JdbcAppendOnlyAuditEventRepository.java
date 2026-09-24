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

package net.fhirfactory.harmonia.kleio.persistence.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.kleio.persistence.exception.AuditPersistenceException;
import net.fhirfactory.harmonia.kleio.persistence.model.PersistedAuditEventRow;
import net.fhirfactory.harmonia.kleio.persistence.qualifier.KleioAudit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Standard JDBC implementation of {@link AppendOnlyAuditEventRepository} operating on {@code hie_fhir_resources}.
 * Enforces insert-only semantics with zero update or delete methods, relying on database-level
 * unique index conflict resolution (ON CONFLICT DO NOTHING) to ensure append durability without JTA corruption.
 */
@ApplicationScoped
public class JdbcAppendOnlyAuditEventRepository implements AppendOnlyAuditEventRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcAppendOnlyAuditEventRepository.class);

    private static final String INSERT_SQL =
            "INSERT INTO hie_fhir_resources (" +
            "resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated" +
            ") VALUES ('AuditEvent', ?, 1, ?, false, ?) " +
            "ON CONFLICT DO NOTHING";

    private static final String FIND_BY_EVENT_ID_SQL =
            "SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated " +
            "FROM hie_fhir_resources " +
            "WHERE resource_type = 'AuditEvent' AND fhir_id = ?";

    private static final String FIND_CANDIDATE_PAGE_SQL =
            "SELECT id, fhir_id, version_id, resource_json, is_deleted, last_updated " +
            "FROM hie_fhir_resources " +
            "WHERE resource_type = 'AuditEvent' AND id < ? " +
            "ORDER BY id DESC " +
            "LIMIT ?";

    private final DataSource dataSource;

    /**
     * Protected no-arg constructor required for CDI proxy generation.
     */
    protected JdbcAppendOnlyAuditEventRepository() {
        this.dataSource = null;
    }

    /**
     * CDI-injected constructor consuming the container-managed audit {@link DataSource}.
     *
     * @param dataSource container-managed audit DataSource qualified with {@link KleioAudit}
     */
    @Inject
    public JdbcAppendOnlyAuditEventRepository(@KleioAudit DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource, "dataSource must not be null");
    }

    @Override
    public boolean insertIfAbsent(String eventId, String resourceJson, Instant lastUpdated) {
        Objects.requireNonNull(eventId, "eventId must not be null");
        Objects.requireNonNull(resourceJson, "resourceJson must not be null");
        Instant persistenceTime = lastUpdated != null ? lastUpdated : Instant.now();

        log.debug("Executing append insert: eventId={}", eventId);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            ps.setString(1, eventId);
            ps.setString(2, resourceJson);
            ps.setTimestamp(3, Timestamp.from(persistenceTime));

            int rowsAffected = ps.executeUpdate();
            return rowsAffected == 1;

        } catch (SQLException e) {
            log.error("Database failure during append insert: eventId={}", eventId, e);
            throw new AuditPersistenceException("Failed to insert audit event with ID " + eventId, e);
        }
    }

    @Override
    public Optional<PersistedAuditEventRow> findByEventId(String eventId) {
        if (eventId == null || eventId.isBlank()) {
            return Optional.empty();
        }

        log.trace("Querying audit row by eventId: eventId={}", eventId);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_EVENT_ID_SQL)) {

            ps.setString(1, eventId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            log.error("Database failure during audit row lookup: eventId={}", eventId, e);
            throw new AuditPersistenceException("Failed to find audit event with ID " + eventId, e);
        }
    }

    @Override
    public List<PersistedAuditEventRow> findCandidatePage(long cursorId, int pageSize) {
        if (pageSize <= 0) {
            return List.of();
        }

        log.trace("Querying candidate audit rows page: cursorId={}, pageSize={}", cursorId, pageSize);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_CANDIDATE_PAGE_SQL)) {

            ps.setLong(1, cursorId);
            ps.setInt(2, pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                List<PersistedAuditEventRow> rows = new ArrayList<>(pageSize);
                while (rs.next()) {
                    rows.add(mapRow(rs));
                }
                return Collections.unmodifiableList(rows);
            }

        } catch (SQLException e) {
            log.error("Database failure during candidate scan: cursorId={}, pageSize={}", cursorId, pageSize, e);
            throw new AuditPersistenceException("Failed to fetch candidate audit event rows for cursor " + cursorId, e);
        }
    }

    private PersistedAuditEventRow mapRow(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String fhirId = rs.getString("fhir_id");
        long versionId = rs.getLong("version_id");
        String resourceJson = rs.getString("resource_json");
        boolean isDeleted = rs.getBoolean("is_deleted");
        Timestamp timestamp = rs.getTimestamp("last_updated");
        Instant lastUpdated = timestamp != null ? timestamp.toInstant() : null;

        return new PersistedAuditEventRow(id, fhirId, versionId, resourceJson, isDeleted, lastUpdated);
    }
}
