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
import net.fhirfactory.harmonia.hapifhir.model.FhirResourceEntity;
import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.hapifhir.repository.FhirResourceRepository;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeCommitOutcome;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.PreconditionFailureReason;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
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

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
@DisplayName("Mnemosyne Authoritative Persistence PostgreSQL Concurrency Tests")
class AuthoritativePersistencePostgreSqlConcurrencyTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("harmonia_fhir_concurrency")
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
    private FhirResourceRepository repository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private AuthoritativePersistenceService persistenceService;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        persistenceService = new AuthoritativePersistenceService(repository, transactionManager, FhirContext.forR5());
    }

    @Test
    @DisplayName("Scenario 1: Concurrent CREATE Collision — 10 threads attempt simultaneous CREATE for same ResourceKey; exactly 1 commits, 9 receive RESOURCE_ALREADY_EXISTS")
    void testConcurrentCreateCollision() {
        assertTimeoutPreemptively(Duration.ofSeconds(20), () -> {
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
                        AuthoritativePersistenceResult<IBaseResource> result = persistenceService.create(key, patient);
                        if (result instanceof AuthoritativePersistenceResult.Committed<IBaseResource> committed) {
                            committedResults.add(committed);
                        } else if (result instanceof AuthoritativePersistenceResult.Conflict<IBaseResource> conflict) {
                            conflictResults.add(conflict);
                        } else {
                            unexpectedErrors.add(new IllegalStateException("Unexpected result outcome: " + result.outcome()));
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
            boolean finished = doneLatch.await(15, TimeUnit.SECONDS);
            executor.shutdown();

            assertThat(finished).isTrue();
            assertThat(unexpectedErrors).isEmpty();

            // Architectural Invariant: AT MOST ONE participant may commit (and under healthy conditions, exactly 1 succeeds)
            assertThat(committedResults).hasSize(1);
            assertThat(conflictResults).hasSize(threadCount - 1);

            // Verify all conflicts are RESOURCE_ALREADY_EXISTS
            for (AuthoritativePersistenceResult.Conflict<IBaseResource> conflict : conflictResults) {
                assertThat(conflict.conflict().reason()).isEqualTo(PreconditionFailureReason.RESOURCE_ALREADY_EXISTS);
                assertThat(conflict.conflict().key()).isEqualTo(key);
            }

            // Verify database state: exactly 1 physical row exists at version 1
            Optional<FhirResourceEntity> entityOpt = repository.findByResourceTypeAndFhirId("Patient", resourceId);
            assertThat(entityOpt).isPresent();
            assertThat(entityOpt.get().getVersionId()).isEqualTo(1L);
            assertThat(entityOpt.get().isDeleted()).isFalse();
        });
    }

    @Test
    @DisplayName("Scenario 2: Concurrent UPDATE Race — 10 threads attempt simultaneous UPDATE from same predecessor V1; exactly 1 commits, 9 receive EXPECTED_VERSION_MISMATCH")
    void testConcurrentUpdateRaceFromSamePredecessor() {
        assertTimeoutPreemptively(Duration.ofSeconds(20), () -> {
            int threadCount = 10;
            String resourceId = "pat-update-conc-" + UUID.randomUUID().toString().substring(0, 8);
            ResourceKey key = ResourceKey.of("Patient", resourceId);

            // Establish initial resource at version 1
            Patient seedPatient = new Patient();
            seedPatient.setActive(true);
            seedPatient.addName(new HumanName().setFamily("Initial").addGiven("Seed"));
            AuthoritativePersistenceResult<IBaseResource> createRes = persistenceService.create(key, seedPatient);
            assertThat(createRes.isCommitted()).isTrue();

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
                                persistenceService.update(key, updated, sharedPredecessor);

                        if (result instanceof AuthoritativePersistenceResult.Committed<IBaseResource> committed) {
                            committedResults.add(committed);
                        } else if (result instanceof AuthoritativePersistenceResult.Conflict<IBaseResource> conflict) {
                            conflictResults.add(conflict);
                        } else {
                            unexpectedErrors.add(new IllegalStateException("Unexpected result outcome: " + result.outcome()));
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
            boolean finished = doneLatch.await(15, TimeUnit.SECONDS);
            executor.shutdown();

            assertThat(finished).isTrue();
            assertThat(unexpectedErrors).isEmpty();

            // Architectural Invariant: AT MOST ONE participant may commit from predecessor V1
            assertThat(committedResults).hasSize(1);
            assertThat(conflictResults).hasSize(threadCount - 1);

            AuthoritativePersistenceResult.Committed<IBaseResource> winner = committedResults.get(0);
            assertThat(winner.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of(2L));

            // Verify all conflicts are EXPECTED_VERSION_MISMATCH
            for (AuthoritativePersistenceResult.Conflict<IBaseResource> conflict : conflictResults) {
                assertThat(conflict.conflict().reason()).isEqualTo(PreconditionFailureReason.EXPECTED_VERSION_MISMATCH);
                assertThat(conflict.conflict().expectedVersion()).isEqualTo(sharedPredecessor);
                assertThat(conflict.conflict().currentVersion()).isEqualTo(AuthoritativeVersion.of(2L));
            }

            // Verify database state: version advanced to exactly 2
            Optional<FhirResourceEntity> entityOpt = repository.findByResourceTypeAndFhirId("Patient", resourceId);
            assertThat(entityOpt).isPresent();
            assertThat(entityOpt.get().getVersionId()).isEqualTo(2L);
            assertThat(entityOpt.get().isDeleted()).isFalse();
        });
    }

    @Test
    @DisplayName("Scenario 3: Chained Sequential Progression — Monotonically incrementing versions V1 -> V2 -> V3 -> V4 -> V5 succeed")
    void testChainedSequentialProgression() {
        String resourceId = "pat-chain-" + UUID.randomUUID().toString().substring(0, 8);
        ResourceKey key = ResourceKey.of("Patient", resourceId);

        // CREATE -> v1
        Patient initial = new Patient();
        initial.addName(new HumanName().setFamily("Base"));
        AuthoritativePersistenceResult<IBaseResource> createRes = persistenceService.create(key, initial);
        assertThat(createRes.isCommitted()).isTrue();
        assertThat(((AuthoritativePersistenceResult.Committed<IBaseResource>) createRes).authoritativeVersion())
                .isEqualTo(AuthoritativeVersion.of(1L));

        // Sequential updates V1 -> V2 -> V3 -> V4 -> V5
        for (long v = 1L; v < 5L; v++) {
            Patient update = new Patient();
            update.addName(new HumanName().setFamily("Version-" + (v + 1)));

            AuthoritativePersistenceResult<IBaseResource> updateRes =
                    persistenceService.update(key, update, ExpectedAuthoritativeVersion.of(v));

            assertThat(updateRes).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
            AuthoritativePersistenceResult.Committed<IBaseResource> committed =
                    (AuthoritativePersistenceResult.Committed<IBaseResource>) updateRes;
            assertThat(committed.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of(v + 1L));
        }

        // Final verification in database
        Optional<FhirResourceEntity> entityOpt = repository.findByResourceTypeAndFhirId("Patient", resourceId);
        assertThat(entityOpt).isPresent();
        assertThat(entityOpt.get().getVersionId()).isEqualTo(5L);
        assertThat(entityOpt.get().getResourceJson()).contains("Version-5");
    }
}
