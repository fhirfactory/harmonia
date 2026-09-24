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

import net.fhirfactory.harmonia.kleio.persistence.exception.AuditPersistenceException;
import net.fhirfactory.harmonia.kleio.persistence.model.PersistedAuditEventRow;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JDBC Append-Only Audit Event Repository Tests")
class JdbcAppendOnlyAuditEventRepositoryTest {

    private DataSource dataSource;
    private JdbcAppendOnlyAuditEventRepository repository;

    @BeforeEach
    void setUp() throws SQLException {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:audit_repo_" + UUID.randomUUID().toString().replace("-", "") +
                ";MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH;DB_CLOSE_DELAY=-1");
        ds.setUser("sa");
        ds.setPassword("");
        this.dataSource = ds;

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE hie_fhir_resources (
                    id BIGSERIAL PRIMARY KEY,
                    resource_type VARCHAR(64) NOT NULL,
                    fhir_id VARCHAR(128) NOT NULL,
                    version_id BIGINT NOT NULL DEFAULT 1,
                    resource_json TEXT NOT NULL,
                    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                    last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    CONSTRAINT uk_resource_type_fhir_id UNIQUE (resource_type, fhir_id)
                )
            """);
        }

        this.repository = new JdbcAppendOnlyAuditEventRepository(dataSource);
    }

    @Test
    @DisplayName("insertIfAbsent inserts new row and returns true")
    void testInsertIfAbsentSuccess() {
        Instant now = Instant.parse("2026-09-24T10:00:00Z");
        boolean inserted = repository.insertIfAbsent("evt-001", "{\"resourceType\":\"AuditEvent\",\"id\":\"evt-001\"}", now);

        assertThat(inserted).isTrue();

        Optional<PersistedAuditEventRow> rowOpt = repository.findByEventId("evt-001");
        assertThat(rowOpt).isPresent();

        PersistedAuditEventRow row = rowOpt.get();
        assertThat(row.id()).isPositive();
        assertThat(row.fhirId()).isEqualTo("evt-001");
        assertThat(row.versionId()).isEqualTo(1L);
        assertThat(row.resourceJson()).isEqualTo("{\"resourceType\":\"AuditEvent\",\"id\":\"evt-001\"}");
        assertThat(row.isDeleted()).isFalse();
        assertThat(row.lastUpdated()).isEqualTo(now);
    }

    @Test
    @DisplayName("insertIfAbsent on conflict returns false and does not mutate row")
    void testInsertIfAbsentConflictReturnsFalse() {
        Instant t1 = Instant.parse("2026-09-24T10:00:00Z");
        Instant t2 = Instant.parse("2026-09-24T11:00:00Z");

        boolean firstInsert = repository.insertIfAbsent("evt-dup-001", "{\"orig\":true}", t1);
        assertThat(firstInsert).isTrue();

        boolean secondInsert = repository.insertIfAbsent("evt-dup-001", "{\"divergent\":true}", t2);
        assertThat(secondInsert).isFalse();

        Optional<PersistedAuditEventRow> rowOpt = repository.findByEventId("evt-dup-001");
        assertThat(rowOpt).isPresent();
        assertThat(rowOpt.get().resourceJson()).isEqualTo("{\"orig\":true}");
        assertThat(rowOpt.get().lastUpdated()).isEqualTo(t1);
        assertThat(rowOpt.get().versionId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("findByEventId returns empty when row does not exist")
    void testFindByEventIdNotFound() {
        assertThat(repository.findByEventId("non-existent")).isEmpty();
        assertThat(repository.findByEventId(null)).isEmpty();
        assertThat(repository.findByEventId("   ")).isEmpty();
    }

    @Test
    @DisplayName("findCandidatePage returns rows descending by ID strictly less than cursorId")
    void testFindCandidatePage() {
        for (int i = 1; i <= 5; i++) {
            repository.insertIfAbsent("evt-" + i, "{\"id\":\"evt-" + i + "\"}", Instant.now());
        }

        List<PersistedAuditEventRow> firstPage = repository.findCandidatePage(Long.MAX_VALUE, 3);
        assertThat(firstPage).hasSize(3);
        assertThat(firstPage.get(0).fhirId()).isEqualTo("evt-5");
        assertThat(firstPage.get(1).fhirId()).isEqualTo("evt-4");
        assertThat(firstPage.get(2).fhirId()).isEqualTo("evt-3");

        long nextCursor = firstPage.get(2).id();
        List<PersistedAuditEventRow> secondPage = repository.findCandidatePage(nextCursor, 3);
        assertThat(secondPage).hasSize(2);
        assertThat(secondPage.get(0).fhirId()).isEqualTo("evt-2");
        assertThat(secondPage.get(1).fhirId()).isEqualTo("evt-1");

        long lastCursor = secondPage.get(1).id();
        List<PersistedAuditEventRow> emptyPage = repository.findCandidatePage(lastCursor, 3);
        assertThat(emptyPage).isEmpty();

        assertThat(repository.hasMoreRows(nextCursor)).isTrue();
        assertThat(repository.hasMoreRows(lastCursor)).isFalse();
    }

    @Test
    @DisplayName("SQLException translates into AuditPersistenceException")
    void testExceptionTranslation() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE hie_fhir_resources");
        }

        assertThatThrownBy(() -> repository.insertIfAbsent("evt-err", "{}", Instant.now()))
                .isInstanceOf(AuditPersistenceException.class)
                .hasMessageContaining("Failed to insert audit event with ID evt-err")
                .hasCauseInstanceOf(SQLException.class);

        assertThatThrownBy(() -> repository.findByEventId("evt-err"))
                .isInstanceOf(AuditPersistenceException.class)
                .hasMessageContaining("Failed to find audit event with ID evt-err")
                .hasCauseInstanceOf(SQLException.class);

        assertThatThrownBy(() -> repository.findCandidatePage(100L, 10))
                .isInstanceOf(AuditPersistenceException.class)
                .hasMessageContaining("Failed to fetch candidate audit event rows for cursor 100")
                .hasCauseInstanceOf(SQLException.class);
    }
}
