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

package net.fhirfactory.harmonia.kleio.persistence.service;

import net.fhirfactory.harmonia.kleio.audit.model.AuditAction;
import net.fhirfactory.harmonia.kleio.audit.model.AuditClassification;
import net.fhirfactory.harmonia.kleio.audit.model.AuditOutcome;
import net.fhirfactory.harmonia.kleio.audit.model.AuditQuery;
import net.fhirfactory.harmonia.kleio.audit.model.AuditTarget;
import net.fhirfactory.harmonia.kleio.audit.model.HarmoniaAuditEvent;
import net.fhirfactory.harmonia.kleio.audit.service.AuditIntegrityException;
import net.fhirfactory.harmonia.kleio.persistence.exception.AuditPersistenceException;
import net.fhirfactory.harmonia.kleio.persistence.repository.JdbcAppendOnlyAuditEventRepository;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Durable Audit Service H2 Functional and Durability Tests")
class DurableAuditServiceH2Test {

    private DataSource dataSource;
    private JdbcAppendOnlyAuditEventRepository repository;
    private DurableAuditService auditService;

    @BeforeEach
    void setUp() throws SQLException {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:durable_audit_" + UUID.randomUUID().toString().replace("-", "") +
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
        this.auditService = new DurableAuditService(repository);
    }

    private HarmoniaAuditEvent createSampleEvent(String eventId, Instant recordedAt) {
        return HarmoniaAuditEvent.builder()
                .eventId(eventId)
                .recordedAt(recordedAt)
                .classification(AuditClassification.SECURITY)
                .action(AuditAction.AUTHORIZE)
                .outcome(AuditOutcome.SUCCESS)
                .initiatingPrincipal(ThemisPrincipal.human("user:dr-smith"))
                .securityDomain("CLINICAL")
                .correlationId("corr-" + eventId)
                .operationId("op-" + eventId)
                .build();
    }

    @Test
    @DisplayName("Appends new event durably with version_id = 1, is_deleted = false")
    void testAppendNewEvent() throws SQLException {
        Instant recordedAt = Instant.parse("2026-09-24T10:15:30Z");
        HarmoniaAuditEvent event = createSampleEvent("EVT-APP-001", recordedAt);

        HarmoniaAuditEvent appended = auditService.append(event);
        assertThat(appended).isEqualTo(event);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT version_id, is_deleted, last_updated FROM hie_fhir_resources WHERE fhir_id = ?")) {
            ps.setString(1, "EVT-APP-001");
            try (ResultSet rs = ps.executeQuery()) {
                assertThat(rs.next()).isTrue();
                assertThat(rs.getLong("version_id")).isEqualTo(1L);
                assertThat(rs.getBoolean("is_deleted")).isFalse();
                assertThat(rs.getTimestamp("last_updated")).isNotNull();
            }
        }
    }

    @Test
    @DisplayName("Point read findById and get equality with canonical event")
    void testPointReadEquality() {
        HarmoniaAuditEvent event = createSampleEvent("EVT-READ-001", Instant.parse("2026-09-24T10:00:00Z"));
        auditService.append(event);

        Optional<HarmoniaAuditEvent> byId = auditService.findById("EVT-READ-001");
        assertThat(byId).isPresent().contains(event);

        Optional<HarmoniaAuditEvent> byGet = auditService.get("EVT-READ-001");
        assertThat(byGet).isPresent().contains(event);

        assertThat(auditService.findById("EVT-MISSING")).isEmpty();
        assertThat(auditService.get("EVT-MISSING")).isEmpty();
    }

    @Test
    @DisplayName("Identical replay is idempotent, returns success without mutating database row")
    void testIdenticalReplayIdempotency() throws SQLException {
        Instant recordedAt = Instant.parse("2026-09-24T10:00:00Z");
        HarmoniaAuditEvent event = createSampleEvent("EVT-REPLAY-001", recordedAt);

        auditService.append(event);

        Timestamp originalTimestamp;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT last_updated FROM hie_fhir_resources WHERE fhir_id = ?")) {
            ps.setString(1, "EVT-REPLAY-001");
            try (ResultSet rs = ps.executeQuery()) {
                assertThat(rs.next()).isTrue();
                originalTimestamp = rs.getTimestamp("last_updated");
            }
        }

        // Replay identical event
        HarmoniaAuditEvent replayed = auditService.append(event);
        assertThat(replayed).isEqualTo(event);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT count(*), version_id, is_deleted, last_updated FROM hie_fhir_resources WHERE fhir_id = ? GROUP BY version_id, is_deleted, last_updated")) {
            ps.setString(1, "EVT-REPLAY-001");
            try (ResultSet rs = ps.executeQuery()) {
                assertThat(rs.next()).isTrue();
                assertThat(rs.getInt(1)).isEqualTo(1);
                assertThat(rs.getLong("version_id")).isEqualTo(1L);
                assertThat(rs.getBoolean("is_deleted")).isFalse();
                assertThat(rs.getTimestamp("last_updated")).isEqualTo(originalTimestamp);
            }
        }
    }

    @Test
    @DisplayName("Divergent event collision throws AuditIntegrityException without mutating stored row")
    void testDivergentEventCollision() throws SQLException {
        Instant recordedAt = Instant.parse("2026-09-24T10:00:00Z");
        HarmoniaAuditEvent original = createSampleEvent("EVT-DIV-001", recordedAt);
        auditService.append(original);

        HarmoniaAuditEvent divergent = HarmoniaAuditEvent.builder()
                .eventId("EVT-DIV-001")
                .recordedAt(recordedAt)
                .classification(AuditClassification.SECURITY)
                .action(AuditAction.DELETE) // Divergent action
                .outcome(AuditOutcome.DENIED) // Divergent outcome
                .initiatingPrincipal(ThemisPrincipal.human("user:attacker"))
                .securityDomain("CLINICAL")
                .build();

        assertThatThrownBy(() -> auditService.append(divergent))
                .isInstanceOf(AuditIntegrityException.class)
                .hasMessageContaining("Conflicting audit event with ID EVT-DIV-001 already exists with divergent content");

        // Verify stored evidence is untouched
        Optional<HarmoniaAuditEvent> retrieved = auditService.findById("EVT-DIV-001");
        assertThat(retrieved).isPresent().contains(original);
    }

    @Test
    @DisplayName("Appending on existing soft-deleted row throws AuditIntegrityException (fail-closed)")
    void testSoftDeletedRowCollision() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO hie_fhir_resources (resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated) VALUES ('AuditEvent', 'EVT-DEL-001', 1, '{}', true, CURRENT_TIMESTAMP)")) {
            ps.executeUpdate();
        }

        HarmoniaAuditEvent event = createSampleEvent("EVT-DEL-001", Instant.now());

        assertThatThrownBy(() -> auditService.append(event))
                .isInstanceOf(AuditIntegrityException.class)
                .hasMessageContaining("Audit integrity violation: event with ID EVT-DEL-001 exists but is marked deleted");

        // Verify soft-deleted flag is still true
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT is_deleted FROM hie_fhir_resources WHERE fhir_id = 'EVT-DEL-001'")) {
            try (ResultSet rs = ps.executeQuery()) {
                assertThat(rs.next()).isTrue();
                assertThat(rs.getBoolean("is_deleted")).isTrue();
            }
        }
    }

    @Test
    @DisplayName("Point read on soft-deleted row throws AuditIntegrityException")
    void testPointReadOnSoftDeletedRow() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO hie_fhir_resources (resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated) VALUES ('AuditEvent', 'EVT-SD-READ', 1, '{}', true, CURRENT_TIMESTAMP)")) {
            ps.executeUpdate();
        }

        assertThatThrownBy(() -> auditService.findById("EVT-SD-READ"))
                .isInstanceOf(AuditIntegrityException.class)
                .hasMessageContaining("Audit integrity violation: event with ID EVT-SD-READ exists but is marked deleted");

        assertThatThrownBy(() -> auditService.get("EVT-SD-READ"))
                .isInstanceOf(AuditIntegrityException.class)
                .hasMessageContaining("Audit integrity violation: event with ID EVT-SD-READ exists but is marked deleted");
    }

    @Test
    @DisplayName("Malformed JSON in database triggers AuditIntegrityException on read and replay")
    void testMalformedJsonHandling() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO hie_fhir_resources (resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated) VALUES ('AuditEvent', 'EVT-CORRUPT', 1, 'NOT_VALID_JSON', false, CURRENT_TIMESTAMP)")) {
            ps.executeUpdate();
        }

        assertThatThrownBy(() -> auditService.findById("EVT-CORRUPT"))
                .isInstanceOf(AuditIntegrityException.class)
                .hasMessageContaining("contains malformed JSON");

        HarmoniaAuditEvent event = createSampleEvent("EVT-CORRUPT", Instant.now());
        assertThatThrownBy(() -> auditService.append(event))
                .isInstanceOf(AuditIntegrityException.class)
                .hasMessageContaining("contains malformed JSON");
    }

    @Test
    @DisplayName("find(AuditQuery) filters strictly on canonical recordedAt rather than persistence last_updated")
    void testCanonicalTimeFilteringDecoupledFromLastUpdated() throws SQLException {
        Instant pastTime = Instant.parse("2026-09-20T12:00:00Z");
        Instant recentTime = Instant.parse("2026-09-24T12:00:00Z");

        HarmoniaAuditEvent pastEvent = createSampleEvent("EVT-PAST", pastTime);
        HarmoniaAuditEvent recentEvent = createSampleEvent("EVT-RECENT", recentTime);

        // Both are appended now (their last_updated in DB is current timestamp)
        auditService.append(pastEvent);
        auditService.append(recentEvent);

        // Query window matching pastTime [2026-09-19, 2026-09-21]
        List<HarmoniaAuditEvent> pastResults = auditService.find(AuditQuery.builder()
                .startTime(Instant.parse("2026-09-19T00:00:00Z"))
                .endTime(Instant.parse("2026-09-21T00:00:00Z"))
                .build());

        assertThat(pastResults).containsExactly(pastEvent);

        // Query window matching recentTime [2026-09-23, 2026-09-25]
        List<HarmoniaAuditEvent> recentResults = auditService.find(AuditQuery.builder()
                .startTime(Instant.parse("2026-09-23T00:00:00Z"))
                .endTime(Instant.parse("2026-09-25T00:00:00Z"))
                .build());

        assertThat(recentResults).containsExactly(recentEvent);
    }

    @Test
    @DisplayName("find(AuditQuery) matches predicates: classification, principalId, targetId, action, outcome, correlationId, operationId")
    void testPredicateFiltering() {
        Instant now = Instant.parse("2026-09-24T10:00:00Z");

        HarmoniaAuditEvent targetEvent = HarmoniaAuditEvent.builder()
                .eventId("EVT-PRED-TARGET")
                .recordedAt(now)
                .classification(AuditClassification.CLINICAL)
                .action(AuditAction.READ)
                .outcome(AuditOutcome.SUCCESS)
                .initiatingPrincipal(ThemisPrincipal.human("user:nurse-jack"))
                .target(AuditTarget.of("Observation", "OBS-99", "CLINICAL"))
                .securityDomain("CLINICAL")
                .correlationId("corr-pred-1")
                .operationId("op-pred-1")
                .build();

        HarmoniaAuditEvent otherEvent = HarmoniaAuditEvent.builder()
                .eventId("EVT-PRED-OTHER")
                .recordedAt(now)
                .classification(AuditClassification.SECURITY)
                .action(AuditAction.AUTHORIZE)
                .outcome(AuditOutcome.DENIED)
                .initiatingPrincipal(ThemisPrincipal.system("sys:auth"))
                .target(AuditTarget.of("Patient", "PAT-11", "ADMIN"))
                .securityDomain("ADMIN")
                .correlationId("corr-pred-2")
                .operationId("op-pred-2")
                .build();

        auditService.append(targetEvent);
        auditService.append(otherEvent);

        // Verify each predicate independently
        assertThat(auditService.find(AuditQuery.builder().classification(AuditClassification.CLINICAL).build()))
                .containsExactly(targetEvent);

        assertThat(auditService.find(AuditQuery.builder().principalId("user:nurse-jack").build()))
                .containsExactly(targetEvent);

        assertThat(auditService.find(AuditQuery.builder().targetId("OBS-99").build()))
                .containsExactly(targetEvent);

        assertThat(auditService.find(AuditQuery.builder().action(AuditAction.READ).build()))
                .containsExactly(targetEvent);

        assertThat(auditService.find(AuditQuery.builder().outcome(AuditOutcome.SUCCESS).build()))
                .containsExactly(targetEvent);

        assertThat(auditService.findByCorrelationId("corr-pred-1"))
                .containsExactly(targetEvent);

        assertThat(auditService.findByPrincipal("user:nurse-jack"))
                .containsExactly(targetEvent);
    }

    @Test
    @DisplayName("Keyset paging enforces deterministic canonical ordering: recordedAt DESC, eventId DESC")
    void testKeysetPagingDeterministicCanonicalOrdering() {
        Instant commonTimestamp = Instant.parse("2026-09-24T12:00:00Z");

        // Events with equal recordedAt but different eventId
        HarmoniaAuditEvent evtA = createSampleEvent("EVT-ORD-A", commonTimestamp);
        HarmoniaAuditEvent evtB = createSampleEvent("EVT-ORD-B", commonTimestamp);
        HarmoniaAuditEvent evtC = createSampleEvent("EVT-ORD-C", commonTimestamp);
        HarmoniaAuditEvent evtNewer = createSampleEvent("EVT-ORD-NEWER", commonTimestamp.plusSeconds(60));
        HarmoniaAuditEvent evtOlder = createSampleEvent("EVT-ORD-OLDER", commonTimestamp.minusSeconds(60));

        auditService.append(evtA);
        auditService.append(evtC);
        auditService.append(evtB);
        auditService.append(evtNewer);
        auditService.append(evtOlder);

        List<HarmoniaAuditEvent> results = auditService.find(AuditQuery.builder().limit(10).build());

        // Expected order:
        // 1. EVT-ORD-NEWER (higher timestamp)
        // 2. EVT-ORD-C (common timestamp, eventId tie-breaker DESC: C > B > A)
        // 3. EVT-ORD-B
        // 4. EVT-ORD-A
        // 5. EVT-ORD-OLDER (lower timestamp)
        assertThat(results).containsExactly(evtNewer, evtC, evtB, evtA, evtOlder);
    }

    @Test
    @DisplayName("Soft-deleted row encountered in candidate scan throws AuditIntegrityException")
    void testSoftDeletedRowInCandidateStream() throws SQLException {
        HarmoniaAuditEvent e1 = createSampleEvent("EVT-SCAN-01", Instant.now());
        auditService.append(e1);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO hie_fhir_resources (resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated) VALUES ('AuditEvent', 'EVT-SCAN-DEL', 1, '{}', true, CURRENT_TIMESTAMP)")) {
            ps.executeUpdate();
        }

        assertThatThrownBy(() -> auditService.find(AuditQuery.builder().build()))
                .isInstanceOf(AuditIntegrityException.class)
                .hasMessageContaining("in query stream is marked deleted");
    }

    @Test
    @DisplayName("Defensive scan ceiling of 10000 rows throws AuditPersistenceException")
    void testDefensiveScanCeilingEnforcement() throws SQLException {
        HarmoniaAuditEvent dummyTemplate = HarmoniaAuditEvent.builder()
                .eventId("DUMMY-BASE")
                .recordedAt(Instant.parse("2026-09-24T00:00:00Z"))
                .classification(AuditClassification.SYSTEM)
                .action(AuditAction.EXECUTE)
                .outcome(AuditOutcome.SUCCESS)
                .initiatingPrincipal(ThemisPrincipal.system("sys:daemon"))
                .securityDomain("OPS")
                .build();
        String validJson = ca.uhn.fhir.context.FhirContext.forR5().newJsonParser()
                .encodeResourceToString(new net.fhirfactory.harmonia.kleio.fhir.mapper.HarmoniaAuditEventMapper().toFhir(dummyTemplate));

        // Insert 10,005 dummy audit event rows that will not match query (SYSTEM vs CLINICAL)
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO hie_fhir_resources (resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated) VALUES ('AuditEvent', ?, 1, ?, false, CURRENT_TIMESTAMP)")) {

            for (int i = 1; i <= 10005; i++) {
                ps.setString(1, "DUMMY-" + i);
                ps.setString(2, validJson.replace("DUMMY-BASE", "DUMMY-" + i));
                ps.addBatch();
                if (i % 1000 == 0) {
                    ps.executeBatch();
                }
            }
            ps.executeBatch();
        }

        // Search for classification CLINICAL with limit 10
        AuditQuery query = AuditQuery.builder()
                .classification(AuditClassification.CLINICAL)
                .limit(10)
                .build();

        assertThatThrownBy(() -> auditService.find(query))
                .isInstanceOf(AuditPersistenceException.class)
                .hasMessageContaining("scan ceiling of 10000 rows exceeded before query criteria could be completed");
    }

    @Test
    @DisplayName("Process restart durability simulation recovers persisted evidence across service instances")
    void testProcessRestartDurabilitySimulation() {
        // 1. Service instance 1 records evidence
        HarmoniaAuditEvent event = createSampleEvent("EVT-RESTART-001", Instant.parse("2026-09-24T10:30:00Z"));
        auditService.append(event);

        // 2. Terminate instance 1
        auditService = null;
        repository = null;

        // 3. Fresh instance 2 starts against the same underlying data store
        JdbcAppendOnlyAuditEventRepository repoInstance2 = new JdbcAppendOnlyAuditEventRepository(dataSource);
        DurableAuditService serviceInstance2 = new DurableAuditService(repoInstance2);

        // 4. Retrieve and verify canonical equality
        Optional<HarmoniaAuditEvent> recoveredOpt = serviceInstance2.findById("EVT-RESTART-001");
        assertThat(recoveredOpt).isPresent().contains(event);
    }
}
