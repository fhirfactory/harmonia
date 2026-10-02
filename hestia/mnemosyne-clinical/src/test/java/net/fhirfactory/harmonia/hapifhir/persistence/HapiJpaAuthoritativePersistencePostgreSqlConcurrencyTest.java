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

package net.fhirfactory.harmonia.hapifhir.persistence;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeCommitOutcome;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.PreconditionFailureReason;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.AuditEvent;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Testcontainers(disabledWithoutDocker = true)
@DisplayName("HAPI FHIR JPA Authoritative Persistence PostgreSQL Concurrency Tests")
class HapiJpaAuthoritativePersistencePostgreSqlConcurrencyTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("harmonia_hapi_concurrency")
            .withUsername("fhir_user")
            .withPassword("fhir_password");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    @Qualifier("hapiJpaAuthoritativePersistenceAdapter")
    private AuthoritativePersistencePort<IBaseResource> adapter;

    @Autowired
    private DaoRegistry daoRegistry;

    @Autowired
    private DataSource dataSource;

    private static final FhirContext fhirContext = FhirContext.forR5();

    @Test
    @DisplayName("Scenario 1: Concurrent CREATE Collision — 10 threads attempt simultaneous CREATE for same ResourceKey; exactly 1 commits, 9 receive RESOURCE_ALREADY_EXISTS")
    void testConcurrentCreateCollision() {
        assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
            int threadCount = 10;
            String resourceId = "pat-create-conc-" + UUID.randomUUID().toString().substring(0, 8);
            ResourceKey key = ResourceKey.of("Patient", resourceId);

            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch readyLatch = new CountDownLatch(threadCount);
            CountDownLatch startLatch = new CountDownLatch(1);
            CountDownLatch doneLatch = new CountDownLatch(threadCount);

            List<AuthoritativePersistenceResult.Committed<IBaseResource>> committedResults =
                    Collections.synchronizedList(new ArrayList<>());
            List<AuthoritativePersistenceResult.Conflict<IBaseResource>> conflictResults =
                    Collections.synchronizedList(new ArrayList<>());
            List<Throwable> unexpectedErrors = Collections.synchronizedList(new ArrayList<>());

            for (int i = 0; i < threadCount; i++) {
                final int threadNum = i;
                executor.submit(() -> {
                    Patient patient = new Patient();
                    patient.setActive(true);
                    patient.addName(new HumanName().setFamily("Family-" + threadNum).addGiven("Given-" + threadNum));

                    readyLatch.countDown();
                    try {
                        startLatch.await();
                        AuthoritativePersistenceResult<IBaseResource> result = adapter.create(key, patient);
                        if (result instanceof AuthoritativePersistenceResult.Committed<IBaseResource> committed) {
                            committedResults.add(committed);
                        } else if (result instanceof AuthoritativePersistenceResult.Conflict<IBaseResource> conflict) {
                            conflictResults.add(conflict);
                        } else {
                            unexpectedErrors.add(new IllegalStateException("Unexpected result: " + result));
                        }
                    } catch (Throwable t) {
                        unexpectedErrors.add(t);
                    } finally {
                        doneLatch.countDown();
                    }
                });
            }

            readyLatch.await(5, TimeUnit.SECONDS);
            startLatch.countDown(); // Trigger all threads simultaneously
            boolean finished = doneLatch.await(20, TimeUnit.SECONDS);
            executor.shutdown();

            assertThat(finished).isTrue();
            assertThat(unexpectedErrors).isEmpty();

            // Invariant: Exactly 1 participant commits at version 1, all others receive RESOURCE_ALREADY_EXISTS
            assertThat(committedResults).hasSize(1);
            AuthoritativePersistenceResult.Committed<IBaseResource> winner = committedResults.get(0);
            assertThat(winner.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of(1L));

            assertThat(conflictResults).hasSize(threadCount - 1);
            for (AuthoritativePersistenceResult.Conflict<IBaseResource> conflict : conflictResults) {
                assertThat(conflict.conflict().reason()).isEqualTo(PreconditionFailureReason.RESOURCE_ALREADY_EXISTS);
                assertThat(conflict.conflict().key()).isEqualTo(key);
            }

            // Verify database state in PostgreSQL HFJ_RESOURCE and HFJ_RES_VER
            int resourceRowCount = countRows("hfj_resource", "res_type = ? AND fhir_id = ?", key.resourceType(), key.id());
            assertThat(resourceRowCount).isEqualTo(1);

            Long currentVer = queryLong("SELECT res_ver FROM hfj_resource WHERE res_type = ? AND fhir_id = ?", key.resourceType(), key.id());
            assertThat(currentVer).isEqualTo(1L);

            List<Long> historyVersions = queryLongList(
                    "SELECT v.res_ver FROM hfj_res_ver v JOIN hfj_resource r ON v.res_id = r.res_id WHERE r.res_type = ? AND r.fhir_id = ? ORDER BY v.res_ver ASC",
                    key.resourceType(), key.id());
            assertThat(historyVersions).containsExactly(1L);

            // Verify point read matches winner
            AuthoritativePersistenceResult<IBaseResource> readResult = adapter.read(key);
            assertThat(readResult.isCommitted()).isTrue();
            AuthoritativePersistenceResult.Committed<IBaseResource> readCommitted = (AuthoritativePersistenceResult.Committed<IBaseResource>) readResult;
            assertThat(readCommitted.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of(1L));

            Patient readPatient = (Patient) readCommitted.persistedResource();
            Patient winnerPatient = (Patient) winner.persistedResource();
            assertThat(readPatient.getNameFirstRep().getFamily()).isEqualTo(winnerPatient.getNameFirstRep().getFamily());
        });
    }

    @Test
    @DisplayName("Scenario 2: Concurrent UPDATE Race — 10 threads attempt simultaneous UPDATE from predecessor V1; exactly 1 commits, 9 receive EXPECTED_VERSION_MISMATCH")
    void testConcurrentUpdateRaceFromSamePredecessor() {
        assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
            int threadCount = 10;
            String resourceId = "pat-update-conc-" + UUID.randomUUID().toString().substring(0, 8);
            ResourceKey key = ResourceKey.of("Patient", resourceId);

            // Establish initial resource at version 1
            Patient seedPatient = new Patient();
            seedPatient.setActive(true);
            seedPatient.addName(new HumanName().setFamily("Initial").addGiven("Seed"));
            AuthoritativePersistenceResult<IBaseResource> createRes = adapter.create(key, seedPatient);
            assertThat(createRes.isCommitted()).isTrue();
            assertThat(((AuthoritativePersistenceResult.Committed<IBaseResource>) createRes).authoritativeVersion())
                    .isEqualTo(AuthoritativeVersion.of(1L));

            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch readyLatch = new CountDownLatch(threadCount);
            CountDownLatch startLatch = new CountDownLatch(1);
            CountDownLatch doneLatch = new CountDownLatch(threadCount);

            List<AuthoritativePersistenceResult.Committed<IBaseResource>> committedResults =
                    Collections.synchronizedList(new ArrayList<>());
            List<AuthoritativePersistenceResult.Conflict<IBaseResource>> conflictResults =
                    Collections.synchronizedList(new ArrayList<>());
            List<Throwable> unexpectedErrors = Collections.synchronizedList(new ArrayList<>());

            ExpectedAuthoritativeVersion sharedPredecessor = ExpectedAuthoritativeVersion.of(1L);

            for (int i = 0; i < threadCount; i++) {
                final int threadNum = i;
                executor.submit(() -> {
                    Patient updated = new Patient();
                    updated.setActive(true);
                    updated.addName(new HumanName().setFamily("DivergentFamily-" + threadNum).addGiven("Given-" + threadNum));

                    readyLatch.countDown();
                    try {
                        startLatch.await();
                        AuthoritativePersistenceResult<IBaseResource> result =
                                adapter.update(key, updated, sharedPredecessor);

                        if (result instanceof AuthoritativePersistenceResult.Committed<IBaseResource> committed) {
                            committedResults.add(committed);
                        } else if (result instanceof AuthoritativePersistenceResult.Conflict<IBaseResource> conflict) {
                            conflictResults.add(conflict);
                        } else {
                            unexpectedErrors.add(new IllegalStateException("Unexpected result: " + result));
                        }
                    } catch (Throwable t) {
                        unexpectedErrors.add(t);
                    } finally {
                        doneLatch.countDown();
                    }
                });
            }

            readyLatch.await(5, TimeUnit.SECONDS);
            startLatch.countDown(); // Trigger all threads simultaneously
            boolean finished = doneLatch.await(20, TimeUnit.SECONDS);
            executor.shutdown();

            assertThat(finished).isTrue();
            assertThat(unexpectedErrors).isEmpty();

            // Invariant: Exactly 1 thread establishes version 2; all 9 others receive EXPECTED_VERSION_MISMATCH
            assertThat(committedResults).hasSize(1);
            AuthoritativePersistenceResult.Committed<IBaseResource> winner = committedResults.get(0);
            assertThat(winner.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of(2L));

            assertThat(conflictResults).hasSize(threadCount - 1);
            for (AuthoritativePersistenceResult.Conflict<IBaseResource> conflict : conflictResults) {
                assertThat(conflict.conflict().reason()).isEqualTo(PreconditionFailureReason.EXPECTED_VERSION_MISMATCH);
                assertThat(conflict.conflict().key()).isEqualTo(key);
                assertThat(conflict.conflict().expectedVersion()).isEqualTo(sharedPredecessor);
            }

            // Verify database state: exactly 1 physical resource row at version 2
            Long currentVer = queryLong("SELECT res_ver FROM hfj_resource WHERE res_type = ? AND fhir_id = ?", key.resourceType(), key.id());
            assertThat(currentVer).isEqualTo(2L);

            // Verify native HAPI FHIR history: exactly 2 rows in HFJ_RES_VER (v1 and v2), zero phantom rows
            List<Long> historyVersions = queryLongList(
                    "SELECT v.res_ver FROM hfj_res_ver v JOIN hfj_resource r ON v.res_id = r.res_id WHERE r.res_type = ? AND r.fhir_id = ? ORDER BY v.res_ver ASC",
                    key.resourceType(), key.id());
            assertThat(historyVersions).containsExactly(1L, 2L);

            // Verify point read matches the winning updated payload
            AuthoritativePersistenceResult<IBaseResource> readResult = adapter.read(key);
            assertThat(readResult.isCommitted()).isTrue();
            Patient readPatient = (Patient) ((AuthoritativePersistenceResult.Committed<IBaseResource>) readResult).persistedResource();
            Patient winnerPatient = (Patient) winner.persistedResource();
            assertThat(readPatient.getNameFirstRep().getFamily()).isEqualTo(winnerPatient.getNameFirstRep().getFamily());
        });
    }

    @Test
    @DisplayName("Scenario 3: Sequential Version Progression — V1 through V5 sequential updates commit monotonically")
    void testSequentialVersionProgression() {
        assertTimeoutPreemptively(Duration.ofSeconds(20), () -> {
            String resourceId = "pat-seq-" + UUID.randomUUID().toString().substring(0, 8);
            ResourceKey key = ResourceKey.of("Patient", resourceId);

            Patient v1 = new Patient();
            v1.setActive(true);
            v1.addName(new HumanName().setFamily("SeqInitial").addGiven("V1"));
            AuthoritativePersistenceResult<IBaseResource> createRes = adapter.create(key, v1);
            assertThat(createRes.isCommitted()).isTrue();
            assertThat(((AuthoritativePersistenceResult.Committed<IBaseResource>) createRes).authoritativeVersion())
                    .isEqualTo(AuthoritativeVersion.of(1L));

            for (long prevVer = 1; prevVer < 5; prevVer++) {
                Patient nextState = new Patient();
                nextState.setActive(true);
                nextState.addName(new HumanName().setFamily("SeqFamily").addGiven("V" + (prevVer + 1)));

                AuthoritativePersistenceResult<IBaseResource> updateRes =
                        adapter.update(key, nextState, ExpectedAuthoritativeVersion.of(prevVer));

                assertThat(updateRes.isCommitted()).isTrue();
                assertThat(((AuthoritativePersistenceResult.Committed<IBaseResource>) updateRes).authoritativeVersion())
                        .isEqualTo(AuthoritativeVersion.of(prevVer + 1));
            }

            // Verify latest version is 5
            AuthoritativePersistenceResult<IBaseResource> readRes = adapter.read(key);
            assertThat(readRes.isCommitted()).isTrue();
            assertThat(((AuthoritativePersistenceResult.Committed<IBaseResource>) readRes).authoritativeVersion())
                    .isEqualTo(AuthoritativeVersion.of(5L));

            // Verify history has 1, 2, 3, 4, 5
            List<Long> historyVersions = queryLongList(
                    "SELECT v.res_ver FROM hfj_res_ver v JOIN hfj_resource r ON v.res_id = r.res_id WHERE r.res_type = ? AND r.fhir_id = ? ORDER BY v.res_ver ASC",
                    key.resourceType(), key.id());
            assertThat(historyVersions).containsExactly(1L, 2L, 3L, 4L, 5L);
        });
    }

    @Test
    @DisplayName("Scenario 4: Authoritative Point READ Verification")
    void testPointRead() {
        String resourceId = "pat-read-" + UUID.randomUUID().toString().substring(0, 8);
        ResourceKey key = ResourceKey.of("Patient", resourceId);

        // Read absent resource
        AuthoritativePersistenceResult<IBaseResource> absentRead = adapter.read(key);
        assertThat(absentRead.isCommitted()).isFalse();
        assertThat(absentRead).isInstanceOf(AuthoritativePersistenceResult.Absent.class);

        // Create resource
        Patient patient = new Patient();
        patient.setActive(true);
        patient.addName(new HumanName().setFamily("ReadTest").addGiven("John"));
        AuthoritativePersistenceResult<IBaseResource> createRes = adapter.create(key, patient);
        assertThat(createRes.isCommitted()).isTrue();

        // Read present resource
        AuthoritativePersistenceResult<IBaseResource> presentRead = adapter.read(key);
        assertThat(presentRead.isCommitted()).isTrue();
        AuthoritativePersistenceResult.Committed<IBaseResource> committed = (AuthoritativePersistenceResult.Committed<IBaseResource>) presentRead;
        assertThat(committed.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of(1L));
        Patient loaded = (Patient) committed.persistedResource();
        assertThat(loaded.getNameFirstRep().getFamily()).isEqualTo("ReadTest");
        assertThat(loaded.getNameFirstRep().getGivenAsSingleString()).isEqualTo("John");
    }

    @Test
    @DisplayName("Scenario 5: Precondition & Failure Diagnostics")
    void testPreconditionAndFailureDiagnostics() {
        String resourceId = "pat-diag-" + UUID.randomUUID().toString().substring(0, 8);
        ResourceKey key = ResourceKey.of("Patient", resourceId);

        Patient patient = new Patient();
        patient.setActive(true);

        // 1. Null key / null payload
        assertThat(adapter.read(null).isCommitted()).isFalse();
        assertThat(adapter.create(null, patient).isCommitted()).isFalse();
        assertThat(adapter.create(key, null).isCommitted()).isFalse();
        assertThat(adapter.update(null, patient, ExpectedAuthoritativeVersion.of(1L)).isCommitted()).isFalse();
        assertThat(adapter.update(key, null, ExpectedAuthoritativeVersion.of(1L)).isCommitted()).isFalse();

        // 2. Immutable resource (AuditEvent)
        ResourceKey auditKey = ResourceKey.of("AuditEvent", "audit-123");
        AuditEvent auditEvent = new AuditEvent();
        AuthoritativePersistenceResult<IBaseResource> auditCreate = adapter.create(auditKey, auditEvent);
        assertThat(auditCreate.isCommitted()).isFalse();
        AuthoritativePersistenceResult<IBaseResource> auditUpdate = adapter.update(auditKey, auditEvent, ExpectedAuthoritativeVersion.of(1L));
        assertThat(auditUpdate.isCommitted()).isFalse();

        // 3. Resource type mismatch between key and body
        Observation obs = new Observation();
        AuthoritativePersistenceResult<IBaseResource> typeMismatchCreate = adapter.create(key, obs);
        assertThat(typeMismatchCreate.isCommitted()).isFalse();
        AuthoritativePersistenceResult<IBaseResource> typeMismatchUpdate = adapter.update(key, obs, ExpectedAuthoritativeVersion.of(1L));
        assertThat(typeMismatchUpdate.isCommitted()).isFalse();

        // 4. Update on non-existent resource returns EXPECTED_VERSION_MISMATCH
        AuthoritativePersistenceResult<IBaseResource> updateAbsent = adapter.update(key, patient, ExpectedAuthoritativeVersion.of(1L));
        assertThat(updateAbsent).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);
        AuthoritativePersistenceResult.Conflict<IBaseResource> absentConflict = (AuthoritativePersistenceResult.Conflict<IBaseResource>) updateAbsent;
        assertThat(absentConflict.conflict().reason()).isEqualTo(PreconditionFailureReason.EXPECTED_VERSION_MISMATCH);

        // 5. Update with invalid/malformed expected versions
        AuthoritativePersistenceResult<IBaseResource> updateNone = adapter.update(key, patient, ExpectedAuthoritativeVersion.none());
        assertThat(updateNone).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);

        AuthoritativePersistenceResult<IBaseResource> updateNull = adapter.update(key, patient, null);
        assertThat(updateNull).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);

        AuthoritativePersistenceResult<IBaseResource> updateMalformed = adapter.update(key, patient, ExpectedAuthoritativeVersion.of("invalid-ver"));
        assertThat(updateMalformed).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);

        AuthoritativePersistenceResult<IBaseResource> updateNegative = adapter.update(key, patient, ExpectedAuthoritativeVersion.of(-5L));
        assertThat(updateNegative).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);

        // 6. Create resource, then attempt update with mismatched predecessor version
        adapter.create(key, patient);
        AuthoritativePersistenceResult<IBaseResource> mismatchUpdate = adapter.update(key, patient, ExpectedAuthoritativeVersion.of(99L));
        assertThat(mismatchUpdate).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);
        AuthoritativePersistenceResult.Conflict<IBaseResource> mismatchConflict = (AuthoritativePersistenceResult.Conflict<IBaseResource>) mismatchUpdate;
        assertThat(mismatchConflict.conflict().reason()).isEqualTo(PreconditionFailureReason.EXPECTED_VERSION_MISMATCH);
        assertThat(mismatchConflict.conflict().expectedVersion()).isEqualTo(ExpectedAuthoritativeVersion.of(99L));
        assertThat(mismatchConflict.conflict().currentVersion()).isEqualTo(AuthoritativeVersion.of(1L));
    }

    private int countRows(String tableName, String whereClause, Object... params) {
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + whereClause;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to query row count: " + sql, e);
        }
        return 0;
    }

    private Long queryLong(String sql, Object... params) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to query long: " + sql, e);
        }
        return null;
    }

    private List<Long> queryLongList(String sql, Object... params) {
        List<Long> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to query long list: " + sql, e);
        }
        return result;
    }
}
