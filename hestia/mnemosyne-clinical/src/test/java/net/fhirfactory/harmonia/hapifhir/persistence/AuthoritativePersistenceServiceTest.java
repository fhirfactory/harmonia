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
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativePreconditionConflict;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.PreconditionFailureReason;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.AuditEvent;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Organization;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthoritativePersistenceServiceTest {

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
    @DisplayName("1. CREATE on Absent Resource: successfully creates entity at version 1 and sets FHIR meta.versionId")
    void testCreateAbsentResourceSuccess() {
        String id = "pat-" + UUID.randomUUID();
        ResourceKey key = ResourceKey.of("Patient", id);

        Patient patient = new Patient();
        patient.setActive(true);
        patient.addName(new HumanName().setFamily("Smith").addGiven("Alice"));

        AuthoritativePersistenceResult<IBaseResource> result = persistenceService.create(key, patient);

        assertThat(result).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
        assertThat(result.outcome()).isEqualTo(AuthoritativeCommitOutcome.COMMITTED);
        assertThat(result.isCommitted()).isTrue();

        AuthoritativePersistenceResult.Committed<IBaseResource> committed =
                (AuthoritativePersistenceResult.Committed<IBaseResource>) result;
        assertThat(committed.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of(1L));

        Patient persisted = (Patient) committed.persistedResource();
        assertThat(persisted.getIdElement().getIdPart()).isEqualTo(id);
        assertThat(persisted.getMeta().getVersionId()).isEqualTo("1");

        // Verify entity in repository
        Optional<FhirResourceEntity> entityOpt = repository.findByResourceTypeAndFhirId("Patient", id);
        assertThat(entityOpt).isPresent();
        assertThat(entityOpt.get().getVersionId()).isEqualTo(1L);
        assertThat(entityOpt.get().isDeleted()).isFalse();
    }

    @Test
    @DisplayName("2. CREATE on Existing Resource: rejected with Conflict(RESOURCE_ALREADY_EXISTS) without overwrite")
    void testCreateExistingResourceConflict() {
        String id = "org-" + UUID.randomUUID();
        ResourceKey key = ResourceKey.of("Organization", id);

        Organization org1 = new Organization();
        org1.setName("First Org");
        AuthoritativePersistenceResult<IBaseResource> result1 = persistenceService.create(key, org1);
        assertThat(result1.isCommitted()).isTrue();

        Organization org2 = new Organization();
        org2.setName("Second Conflicting Org");
        AuthoritativePersistenceResult<IBaseResource> result2 = persistenceService.create(key, org2);

        assertThat(result2).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);
        assertThat(result2.outcome()).isEqualTo(AuthoritativeCommitOutcome.NOT_COMMITTED);

        AuthoritativePersistenceResult.Conflict<IBaseResource> conflict =
                (AuthoritativePersistenceResult.Conflict<IBaseResource>) result2;
        assertThat(conflict.conflict().reason()).isEqualTo(PreconditionFailureReason.RESOURCE_ALREADY_EXISTS);
        assertThat(conflict.conflict().key()).isEqualTo(key);
        assertThat(conflict.conflict().currentVersion()).isEqualTo(AuthoritativeVersion.of(1L));

        // Verify persisted state remains original without overwrite
        Optional<FhirResourceEntity> entityOpt = repository.findByResourceTypeAndFhirId("Organization", id);
        assertThat(entityOpt).isPresent();
        assertThat(entityOpt.get().getResourceJson()).contains("First Org");
        assertThat(entityOpt.get().getResourceJson()).doesNotContain("Second Conflicting Org");
        assertThat(entityOpt.get().getVersionId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("3. UPDATE with Matching Predecessor: atomically updates and increments version monotonically")
    void testUpdateMatchingPredecessorSuccess() {
        String id = "person-" + UUID.randomUUID();
        ResourceKey key = ResourceKey.of("Person", id);

        Person initial = new Person();
        initial.addName(new HumanName().setFamily("Doe").addGiven("John"));
        initial.setGender(Enumerations.AdministrativeGender.MALE);

        AuthoritativePersistenceResult<IBaseResource> createRes = persistenceService.create(key, initial);
        assertThat(createRes.isCommitted()).isTrue();

        Person updated = new Person();
        updated.addName(new HumanName().setFamily("Doe").addGiven("Jonathan"));
        updated.setGender(Enumerations.AdministrativeGender.MALE);

        ExpectedAuthoritativeVersion expectedVersion = ExpectedAuthoritativeVersion.of(1L);
        AuthoritativePersistenceResult<IBaseResource> updateRes = persistenceService.update(key, updated, expectedVersion);

        assertThat(updateRes).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
        assertThat(updateRes.outcome()).isEqualTo(AuthoritativeCommitOutcome.COMMITTED);

        AuthoritativePersistenceResult.Committed<IBaseResource> committed =
                (AuthoritativePersistenceResult.Committed<IBaseResource>) updateRes;
        assertThat(committed.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of(2L));

        Person committedPerson = (Person) committed.persistedResource();
        assertThat(committedPerson.getMeta().getVersionId()).isEqualTo("2");

        // Verify repository state
        Optional<FhirResourceEntity> entityOpt = repository.findByResourceTypeAndFhirId("Person", id);
        assertThat(entityOpt).isPresent();
        assertThat(entityOpt.get().getVersionId()).isEqualTo(2L);
        assertThat(entityOpt.get().getResourceJson()).contains("Jonathan");
    }

    @Test
    @DisplayName("4. UPDATE with Stale Predecessor: rejected with Conflict(EXPECTED_VERSION_MISMATCH) and persisted state unchanged")
    void testUpdateStalePredecessorConflict() {
        String id = "pat-" + UUID.randomUUID();
        ResourceKey key = ResourceKey.of("Patient", id);

        Patient patient = new Patient();
        patient.setActive(true);
        persistenceService.create(key, patient);

        // Advance version to 2
        Patient update1 = new Patient();
        update1.setActive(false);
        persistenceService.update(key, update1, ExpectedAuthoritativeVersion.of(1L));

        // Now attempt update with stale predecessor version 1
        Patient staleUpdate = new Patient();
        staleUpdate.setActive(true);
        AuthoritativePersistenceResult<IBaseResource> staleRes =
                persistenceService.update(key, staleUpdate, ExpectedAuthoritativeVersion.of(1L));

        assertThat(staleRes).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);
        assertThat(staleRes.outcome()).isEqualTo(AuthoritativeCommitOutcome.NOT_COMMITTED);

        AuthoritativePersistenceResult.Conflict<IBaseResource> conflict =
                (AuthoritativePersistenceResult.Conflict<IBaseResource>) staleRes;
        assertThat(conflict.conflict().reason()).isEqualTo(PreconditionFailureReason.EXPECTED_VERSION_MISMATCH);
        assertThat(conflict.conflict().expectedVersion()).isEqualTo(ExpectedAuthoritativeVersion.of(1L));
        assertThat(conflict.conflict().currentVersion()).isEqualTo(AuthoritativeVersion.of(2L));

        // Verify DB version remains 2
        Optional<FhirResourceEntity> entityOpt = repository.findByResourceTypeAndFhirId("Patient", id);
        assertThat(entityOpt).isPresent();
        assertThat(entityOpt.get().getVersionId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("5. UPDATE on Absent Resource: rejected without creating the resource (Strict non-upsert)")
    void testUpdateAbsentResourceConflict() {
        String id = "absent-" + UUID.randomUUID();
        ResourceKey key = ResourceKey.of("Patient", id);

        Patient patient = new Patient();
        patient.setActive(true);

        AuthoritativePersistenceResult<IBaseResource> res =
                persistenceService.update(key, patient, ExpectedAuthoritativeVersion.of(1L));

        assertThat(res).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);
        assertThat(res.outcome()).isEqualTo(AuthoritativeCommitOutcome.NOT_COMMITTED);

        AuthoritativePersistenceResult.Conflict<IBaseResource> conflict =
                (AuthoritativePersistenceResult.Conflict<IBaseResource>) res;
        assertThat(conflict.conflict().reason()).isEqualTo(PreconditionFailureReason.EXPECTED_VERSION_MISMATCH);

        // Verify resource was NOT created
        assertThat(repository.findByResourceTypeAndFhirId("Patient", id)).isEmpty();
    }

    @Test
    @DisplayName("6. Fail-Fast on Malformed or None Expected Version")
    void testFailFastOnMalformedExpectedVersion() {
        String id = "pat-" + UUID.randomUUID();
        ResourceKey key = ResourceKey.of("Patient", id);
        Patient patient = new Patient();

        // None expected version
        AuthoritativePersistenceResult<IBaseResource> noneRes =
                persistenceService.update(key, patient, ExpectedAuthoritativeVersion.none());
        assertThat(noneRes).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);

        // Malformed non-numeric expected version
        AuthoritativePersistenceResult<IBaseResource> malformedRes =
                persistenceService.update(key, patient, ExpectedAuthoritativeVersion.of("invalid_version_string"));
        assertThat(malformedRes).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);
    }

    @Test
    @DisplayName("7. Immutable AuditEvent Guard: rejection of CREATE and UPDATE on AuditEvent")
    void testImmutableAuditEventGuard() {
        String id = "audit-" + UUID.randomUUID();
        ResourceKey key = ResourceKey.of("AuditEvent", id);

        AuditEvent auditEvent = new AuditEvent();

        AuthoritativePersistenceResult<IBaseResource> createRes = persistenceService.create(key, auditEvent);
        assertThat(createRes).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
        assertThat(((AuthoritativePersistenceResult.NotCommitted<IBaseResource>) createRes).failureMessage())
                .contains("AuditEvent is immutable");

        AuthoritativePersistenceResult<IBaseResource> updateRes =
                persistenceService.update(key, auditEvent, ExpectedAuthoritativeVersion.of(1L));
        assertThat(updateRes).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
        assertThat(((AuthoritativePersistenceResult.NotCommitted<IBaseResource>) updateRes).failureMessage())
                .contains("AuditEvent is immutable");
    }

    @Test
    @DisplayName("8. Type Mismatch Guard: resource key type must match FHIR resource body type")
    void testTypeMismatchGuard() {
        String id = "mismatch-" + UUID.randomUUID();
        ResourceKey key = ResourceKey.of("Patient", id);
        Organization org = new Organization(); // Mismatched type

        AuthoritativePersistenceResult<IBaseResource> res = persistenceService.create(key, org);
        assertThat(res).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
        assertThat(((AuthoritativePersistenceResult.NotCommitted<IBaseResource>) res).failureMessage())
                .contains("does not match resource body type");
    }

    @Test
    @DisplayName("9. Indeterminate Commit Failure yields OutcomeUnknown")
    void testIndeterminateCommitFailurePreservesOutcomeUnknown() {
        TransactionTemplate failingTxTemplate = new TransactionTemplate(transactionManager) {
            @Override
            public <T> T execute(TransactionCallback<T> action) throws TransactionException {
                throw new TransactionSystemException("Simulated commit-phase transport failure");
            }
        };

        AuthoritativePersistenceService serviceWithFailingTx =
                new AuthoritativePersistenceService(repository, failingTxTemplate, FhirContext.forR5());

        String id = "tx-fail-" + UUID.randomUUID();
        ResourceKey key = ResourceKey.of("Patient", id);
        Patient patient = new Patient();

        AuthoritativePersistenceResult<IBaseResource> createRes = serviceWithFailingTx.create(key, patient);
        assertThat(createRes).isInstanceOf(AuthoritativePersistenceResult.OutcomeUnknown.class);
        assertThat(createRes.outcome()).isEqualTo(AuthoritativeCommitOutcome.UNKNOWN);

        AuthoritativePersistenceResult<IBaseResource> updateRes =
                serviceWithFailingTx.update(key, patient, ExpectedAuthoritativeVersion.of(1L));
        assertThat(updateRes).isInstanceOf(AuthoritativePersistenceResult.OutcomeUnknown.class);
        assertThat(updateRes.outcome()).isEqualTo(AuthoritativeCommitOutcome.UNKNOWN);
    }

    @Test
    @DisplayName("10. Connection Failure yields OutcomeUnknown")
    void testConnectionFailurePreservesOutcomeUnknown() {
        TransactionTemplate connectionFailingTxTemplate = new TransactionTemplate(transactionManager) {
            @Override
            public <T> T execute(TransactionCallback<T> action) throws TransactionException {
                throw new CannotCreateTransactionException("Simulated database unreachable");
            }
        };

        AuthoritativePersistenceService serviceWithConnFail =
                new AuthoritativePersistenceService(repository, connectionFailingTxTemplate, FhirContext.forR5());

        String id = "conn-fail-" + UUID.randomUUID();
        ResourceKey key = ResourceKey.of("Patient", id);
        Patient patient = new Patient();

        AuthoritativePersistenceResult<IBaseResource> createRes = serviceWithConnFail.create(key, patient);
        assertThat(createRes).isInstanceOf(AuthoritativePersistenceResult.OutcomeUnknown.class);
        assertThat(createRes.outcome()).isEqualTo(AuthoritativeCommitOutcome.UNKNOWN);
    }

    @Test
    @DisplayName("11. High-Concurrency CREATE Race: 10 threads attempt simultaneous CREATE for same ResourceKey; exactly 1 commits, 9 receive RESOURCE_ALREADY_EXISTS")
    void testConcurrentCreateCollision() throws Exception {
        int threadCount = 10;
        String resourceId = "pat-conc-create-" + UUID.randomUUID().toString().substring(0, 8);
        ResourceKey key = ResourceKey.of("Patient", resourceId);

        java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(threadCount);
        java.util.concurrent.CountDownLatch readyLatch = new java.util.concurrent.CountDownLatch(threadCount);
        java.util.concurrent.CountDownLatch startLatch = new java.util.concurrent.CountDownLatch(1);
        java.util.concurrent.CountDownLatch doneLatch = new java.util.concurrent.CountDownLatch(threadCount);

        java.util.List<AuthoritativePersistenceResult.Committed<IBaseResource>> committedResults =
                java.util.Collections.synchronizedList(new java.util.ArrayList<>());
        java.util.List<AuthoritativePersistenceResult.Conflict<IBaseResource>> conflictResults =
                java.util.Collections.synchronizedList(new java.util.ArrayList<>());
        java.util.List<Throwable> unexpectedErrors = java.util.Collections.synchronizedList(new java.util.ArrayList<>());

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

        readyLatch.await(5, java.util.concurrent.TimeUnit.SECONDS);
        startLatch.countDown();
        boolean finished = doneLatch.await(15, java.util.concurrent.TimeUnit.SECONDS);
        executor.shutdown();

        assertThat(finished).isTrue();
        assertThat(unexpectedErrors).isEmpty();
        assertThat(committedResults).hasSize(1);
        assertThat(conflictResults).hasSize(threadCount - 1);

        for (AuthoritativePersistenceResult.Conflict<IBaseResource> conflict : conflictResults) {
            assertThat(conflict.conflict().reason()).isEqualTo(PreconditionFailureReason.RESOURCE_ALREADY_EXISTS);
            assertThat(conflict.conflict().key()).isEqualTo(key);
        }

        Optional<FhirResourceEntity> entityOpt = repository.findByResourceTypeAndFhirId("Patient", resourceId);
        assertThat(entityOpt).isPresent();
        assertThat(entityOpt.get().getVersionId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("12. High-Concurrency UPDATE Race: 10 threads attempt simultaneous UPDATE from same predecessor V1; exactly 1 commits, 9 receive EXPECTED_VERSION_MISMATCH")
    void testConcurrentUpdateRace() throws Exception {
        int threadCount = 10;
        String resourceId = "pat-conc-update-" + UUID.randomUUID().toString().substring(0, 8);
        ResourceKey key = ResourceKey.of("Patient", resourceId);

        Patient seed = new Patient();
        seed.setActive(true);
        seed.addName(new HumanName().setFamily("Initial"));
        AuthoritativePersistenceResult<IBaseResource> createRes = persistenceService.create(key, seed);
        assertThat(createRes.isCommitted()).isTrue();

        java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(threadCount);
        java.util.concurrent.CountDownLatch readyLatch = new java.util.concurrent.CountDownLatch(threadCount);
        java.util.concurrent.CountDownLatch startLatch = new java.util.concurrent.CountDownLatch(1);
        java.util.concurrent.CountDownLatch doneLatch = new java.util.concurrent.CountDownLatch(threadCount);

        java.util.List<AuthoritativePersistenceResult.Committed<IBaseResource>> committedResults =
                java.util.Collections.synchronizedList(new java.util.ArrayList<>());
        java.util.List<AuthoritativePersistenceResult.Conflict<IBaseResource>> conflictResults =
                java.util.Collections.synchronizedList(new java.util.ArrayList<>());
        java.util.List<Throwable> unexpectedErrors = java.util.Collections.synchronizedList(new java.util.ArrayList<>());

        ExpectedAuthoritativeVersion sharedPredecessor = ExpectedAuthoritativeVersion.of(1L);

        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            executor.submit(() -> {
                Patient update = new Patient();
                update.setActive(true);
                update.addName(new HumanName().setFamily("Divergent-" + threadNum));

                readyLatch.countDown();
                try {
                    startLatch.await();
                    AuthoritativePersistenceResult<IBaseResource> result =
                            persistenceService.update(key, update, sharedPredecessor);
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

        readyLatch.await(5, java.util.concurrent.TimeUnit.SECONDS);
        startLatch.countDown();
        boolean finished = doneLatch.await(15, java.util.concurrent.TimeUnit.SECONDS);
        executor.shutdown();

        assertThat(finished).isTrue();
        assertThat(unexpectedErrors).isEmpty();
        assertThat(committedResults).hasSize(1);
        assertThat(conflictResults).hasSize(threadCount - 1);

        for (AuthoritativePersistenceResult.Conflict<IBaseResource> conflict : conflictResults) {
            assertThat(conflict.conflict().reason()).isEqualTo(PreconditionFailureReason.EXPECTED_VERSION_MISMATCH);
            assertThat(conflict.conflict().expectedVersion()).isEqualTo(sharedPredecessor);
        }

        Optional<FhirResourceEntity> entityOpt = repository.findByResourceTypeAndFhirId("Patient", resourceId);
        assertThat(entityOpt).isPresent();
        assertThat(entityOpt.get().getVersionId()).isEqualTo(2L);
    }
}
