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

package net.fhirfactory.harmonia.kleio.persistence;

import net.fhirfactory.harmonia.kleio.audit.model.AuditAction;
import net.fhirfactory.harmonia.kleio.audit.model.AuditClassification;
import net.fhirfactory.harmonia.kleio.audit.model.AuditOutcome;
import net.fhirfactory.harmonia.kleio.audit.model.HarmoniaAuditEvent;
import net.fhirfactory.harmonia.kleio.audit.service.AuditIntegrityException;
import net.fhirfactory.harmonia.kleio.persistence.repository.JdbcAppendOnlyAuditEventRepository;
import net.fhirfactory.harmonia.kleio.persistence.service.DurableAuditService;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

@Testcontainers
@DisplayName("Durable Audit Service Real PostgreSQL Concurrency Tests")
class DurableAuditServicePostgreSqlConcurrencyTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("harmonia_audit_test")
            .withUsername("harmonia")
            .withPassword("harmonia");

    private static DataSource dataSource;
    private JdbcAppendOnlyAuditEventRepository repository;
    private DurableAuditService auditService;

    @BeforeAll
    static void initDataSource() throws SQLException {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setUrl(postgres.getJdbcUrl());
        ds.setUser(postgres.getUsername());
        ds.setPassword(postgres.getPassword());
        dataSource = ds;

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS hie_fhir_resources (
                    id BIGSERIAL PRIMARY KEY,
                    resource_type VARCHAR(64) NOT NULL,
                    fhir_id VARCHAR(128) NOT NULL,
                    version_id BIGINT NOT NULL DEFAULT 1,
                    resource_json TEXT NOT NULL,
                    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                    last_updated TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    CONSTRAINT uk_resource_type_fhir_id UNIQUE (resource_type, fhir_id)
                )
            """);
        }
    }

    @BeforeEach
    void setUp() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE hie_fhir_resources RESTART IDENTITY");
        }

        this.repository = new JdbcAppendOnlyAuditEventRepository(dataSource);
        this.auditService = new DurableAuditService(repository);
    }

    private HarmoniaAuditEvent createSampleEvent(String eventId, String principalId, Instant recordedAt) {
        return HarmoniaAuditEvent.builder()
                .eventId(eventId)
                .recordedAt(recordedAt)
                .classification(AuditClassification.SECURITY)
                .action(AuditAction.AUTHORIZE)
                .outcome(AuditOutcome.SUCCESS)
                .initiatingPrincipal(ThemisPrincipal.human(principalId))
                .securityDomain("CLINICAL")
                .correlationId("corr-" + eventId)
                .operationId("op-" + eventId)
                .build();
    }

    @Test
    @DisplayName("Scenario A: Concurrent Identical Append — 10 threads submit identical evidence; exactly 1 row inserted, all succeed idempotently")
    void testScenarioA_ConcurrentIdenticalAppend() throws Exception {
        int threadCount = 10;
        String eventId = "EVT-CONC-IDENT-" + UUID.randomUUID().toString().substring(0, 8);
        Instant recordedAt = Instant.parse("2026-09-24T10:00:00Z");
        HarmoniaAuditEvent identicalEvent = createSampleEvent(eventId, "user:dr-smith", recordedAt);

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        List<Throwable> errors = Collections.synchronizedList(new ArrayList<>());
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await();
                    HarmoniaAuditEvent result = auditService.append(identicalEvent);
                    if (result != null && result.equals(identicalEvent)) {
                        successCount.incrementAndGet();
                    }
                } catch (Throwable t) {
                    errors.add(t);
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await(5, TimeUnit.SECONDS);
        startLatch.countDown(); // Release all threads simultaneously
        boolean completed = doneLatch.await(15, TimeUnit.SECONDS);
        executor.shutdown();

        assertThat(completed).isTrue();
        assertThat(errors).isEmpty();
        assertThat(successCount.get()).isEqualTo(threadCount);

        // Verify database: exactly 1 physical row exists with version_id = 1, is_deleted = false
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT count(*), min(version_id), min(is_deleted::int) FROM hie_fhir_resources WHERE fhir_id = ?")) {
            ps.setString(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                assertThat(rs.next()).isTrue();
                assertThat(rs.getInt(1)).isEqualTo(1); // exactly 1 physical row
                assertThat(rs.getLong(2)).isEqualTo(1L); // version_id = 1
                assertThat(rs.getInt(3)).isEqualTo(0); // is_deleted = false
            }
        }
    }

    @Test
    @DisplayName("Scenario B: Concurrent Divergent Append — 10 threads submit conflicting payloads; exactly 1 winner succeeds, 9 fail with AuditIntegrityException")
    void testScenarioB_ConcurrentDivergentAppend() throws Exception {
        int threadCount = 10;
        String eventId = "EVT-CONC-DIV-" + UUID.randomUUID().toString().substring(0, 8);
        Instant recordedAt = Instant.parse("2026-09-24T10:00:00Z");

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        List<HarmoniaAuditEvent> winners = Collections.synchronizedList(new ArrayList<>());
        List<Throwable> integrityExceptions = Collections.synchronizedList(new ArrayList<>());
        List<Throwable> unexpectedErrors = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executor.submit(() -> {
                HarmoniaAuditEvent divergentEvent = HarmoniaAuditEvent.builder()
                        .eventId(eventId)
                        .recordedAt(recordedAt)
                        .classification(AuditClassification.SECURITY)
                        .action(AuditAction.AUTHORIZE)
                        .outcome(index % 2 == 0 ? AuditOutcome.SUCCESS : AuditOutcome.DENIED)
                        .initiatingPrincipal(ThemisPrincipal.human("user:actor-" + index))
                        .securityDomain("DOMAIN-" + index)
                        .correlationId("corr-" + index)
                        .operationId("op-" + index)
                        .build();

                readyLatch.countDown();
                try {
                    startLatch.await();
                    HarmoniaAuditEvent result = auditService.append(divergentEvent);
                    winners.add(result);
                } catch (AuditIntegrityException e) {
                    integrityExceptions.add(e);
                } catch (Throwable t) {
                    unexpectedErrors.add(t);
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await(5, TimeUnit.SECONDS);
        startLatch.countDown();
        boolean completed = doneLatch.await(15, TimeUnit.SECONDS);
        executor.shutdown();

        assertThat(completed).isTrue();
        assertThat(unexpectedErrors).isEmpty();
        assertThat(winners).hasSize(1);
        assertThat(integrityExceptions).hasSize(threadCount - 1);

        // Verify persisted evidence matches the single winner without overwrite
        HarmoniaAuditEvent winner = winners.get(0);
        Optional<HarmoniaAuditEvent> persisted = auditService.findById(eventId);
        assertThat(persisted).isPresent().contains(winner);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT count(*) FROM hie_fhir_resources WHERE fhir_id = ?")) {
            ps.setString(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                assertThat(rs.next()).isTrue();
                assertThat(rs.getInt(1)).isEqualTo(1);
            }
        }
    }

    @Test
    @DisplayName("Scenario C: ON CONFLICT DO NOTHING does not corrupt PostgreSQL transaction or cause rollback-only state")
    void testScenarioC_OnConflictTransactionNonCorruption() throws SQLException {
        String eventId = "EVT-TX-NONCORRUPT-" + UUID.randomUUID().toString().substring(0, 8);
        Instant now = Instant.now();

        // 1. Durably append the original event
        HarmoniaAuditEvent original = createSampleEvent(eventId, "user:first", now);
        auditService.append(original);

        // 2. Open an uncommitted manual transaction
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            // Execute insert on the same eventId (should return 0 rows affected due to conflict)
            String insertSql = "INSERT INTO hie_fhir_resources (" +
                    "resource_type, fhir_id, version_id, resource_json, is_deleted, last_updated" +
                    ") VALUES ('AuditEvent', ?, 1, '{}', false, ?) ON CONFLICT DO NOTHING";

            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setString(1, eventId);
                ps.setTimestamp(2, java.sql.Timestamp.from(now));
                int rowsAffected = ps.executeUpdate();
                assertThat(rowsAffected).isEqualTo(0);
            }

            // Immediately query the existing row within the SAME transaction
            String selectSql = "SELECT id, fhir_id, version_id, is_deleted FROM hie_fhir_resources WHERE fhir_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setString(1, eventId);
                try (ResultSet rs = ps.executeQuery()) {
                    // MUST succeed: in JPA/Hibernate, a constraint violation aborts the transaction.
                    // Native PostgreSQL ON CONFLICT DO NOTHING leaves transaction fully alive and non-aborted.
                    assertThat(rs.next()).isTrue();
                    assertThat(rs.getString("fhir_id")).isEqualTo(eventId);
                    assertThat(rs.getLong("version_id")).isEqualTo(1L);
                    assertThat(rs.getBoolean("is_deleted")).isFalse();
                }
            }

            conn.commit();
        }
    }

    @Test
    @DisplayName("Scenario D: Speculative Locking & Visibility — Concurrent appends serialize without deadlock and observe winning evidence")
    void testScenarioD_SpeculativeLockingAndVisibility() {
        assertTimeoutPreemptively(Duration.ofSeconds(15), () -> {
            int iterations = 5;
            for (int iter = 0; iter < iterations; iter++) {
                int threadCount = 6;
                String eventId = "EVT-SPEC-LOCK-" + iter + "-" + UUID.randomUUID().toString().substring(0, 6);
                Instant recordedAt = Instant.now();
                HarmoniaAuditEvent sharedEvent = createSampleEvent(eventId, "user:concurrency-probe", recordedAt);

                ExecutorService executor = Executors.newFixedThreadPool(threadCount);
                CountDownLatch startLatch = new CountDownLatch(1);
                CountDownLatch doneLatch = new CountDownLatch(threadCount);

                AtomicInteger completedSuccess = new AtomicInteger(0);
                List<Throwable> failures = Collections.synchronizedList(new ArrayList<>());

                for (int t = 0; t < threadCount; t++) {
                    executor.submit(() -> {
                        try {
                            startLatch.await();
                            HarmoniaAuditEvent ev = auditService.append(sharedEvent);
                            if (ev != null && ev.equals(sharedEvent)) {
                                completedSuccess.incrementAndGet();
                            }
                        } catch (Throwable ex) {
                            failures.add(ex);
                        } finally {
                            doneLatch.countDown();
                        }
                    });
                }

                startLatch.countDown();
                boolean finished = doneLatch.await(10, TimeUnit.SECONDS);
                executor.shutdown();

                assertThat(finished).isTrue();
                assertThat(failures).isEmpty();
                assertThat(completedSuccess.get()).isEqualTo(threadCount);

                // Winning evidence is observable
                Optional<HarmoniaAuditEvent> found = auditService.findById(eventId);
                assertThat(found).isPresent().contains(sharedEvent);
            }
        });
    }
}
