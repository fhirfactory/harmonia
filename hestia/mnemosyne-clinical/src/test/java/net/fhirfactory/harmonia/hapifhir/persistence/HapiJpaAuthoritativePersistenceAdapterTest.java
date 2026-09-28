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
import org.hl7.fhir.r5.model.Organization;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HapiJpaAuthoritativePersistenceAdapterTest {

    @Autowired
    private DaoRegistry daoRegistry;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private HapiJpaAuthoritativePersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new HapiJpaAuthoritativePersistenceAdapter(daoRegistry, transactionManager);
    }

    @Test
    @DisplayName("1. CREATE on Absent Resource: successfully creates entity at version 1")
    void testCreateAbsentResourceSuccess() {
        String id = "pat-" + UUID.randomUUID();
        ResourceKey key = ResourceKey.of("Patient", id);

        Patient patient = new Patient();
        patient.setActive(true);
        patient.addName(new HumanName().setFamily("Smith").addGiven("Alice"));

        AuthoritativePersistenceResult<IBaseResource> result = adapter.create(key, patient);

        assertThat(result).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
        assertThat(result.outcome()).isEqualTo(AuthoritativeCommitOutcome.COMMITTED);
        assertThat(result.isCommitted()).isTrue();

        AuthoritativePersistenceResult.Committed<IBaseResource> committed =
                (AuthoritativePersistenceResult.Committed<IBaseResource>) result;
        assertThat(committed.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of(1L));

        Patient persisted = (Patient) committed.persistedResource();
        assertThat(persisted.getIdElement().getIdPart()).isEqualTo(id);
        assertThat(persisted.getIdElement().getVersionIdPart()).isEqualTo("1");
    }

    @Test
    @DisplayName("2. CREATE on Existing Resource: rejected with Conflict(RESOURCE_ALREADY_EXISTS)")
    void testCreateExistingResourceConflict() {
        String id = "org-" + UUID.randomUUID();
        ResourceKey key = ResourceKey.of("Organization", id);

        Organization org1 = new Organization();
        org1.setName("First Org");
        AuthoritativePersistenceResult<IBaseResource> result1 = adapter.create(key, org1);
        assertThat(result1.isCommitted()).isTrue();

        Organization org2 = new Organization();
        org2.setName("Duplicate Org");
        AuthoritativePersistenceResult<IBaseResource> result2 = adapter.create(key, org2);

        assertThat(result2).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);
        assertThat(result2.outcome()).isEqualTo(AuthoritativeCommitOutcome.NOT_COMMITTED);
        assertThat(result2.isCommitted()).isFalse();

        AuthoritativePersistenceResult.Conflict<IBaseResource> conflict =
                (AuthoritativePersistenceResult.Conflict<IBaseResource>) result2;
        assertThat(conflict.conflict().reason()).isEqualTo(PreconditionFailureReason.RESOURCE_ALREADY_EXISTS);
        assertThat(conflict.conflict().key()).isEqualTo(key);
    }

    @Test
    @DisplayName("3. CREATE rejected on immutable resource type (AuditEvent)")
    void testCreateImmutableResourceRejected() {
        String id = "audit-" + UUID.randomUUID();
        ResourceKey key = ResourceKey.of("AuditEvent", id);

        AuditEvent auditEvent = new AuditEvent();
        AuthoritativePersistenceResult<IBaseResource> result = adapter.create(key, auditEvent);

        assertThat(result).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
        assertThat(result.outcome()).isEqualTo(AuthoritativeCommitOutcome.NOT_COMMITTED);
    }

    @Test
    @DisplayName("4. CREATE rejected on type mismatch between key and payload")
    void testCreateTypeMismatchRejected() {
        String id = "pat-" + UUID.randomUUID();
        ResourceKey key = ResourceKey.of("Patient", id);

        Organization org = new Organization();
        org.setName("Mismatched Body");

        AuthoritativePersistenceResult<IBaseResource> result = adapter.create(key, org);
        assertThat(result).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
    }

    @Test
    @DisplayName("5. CREATE rejected on null key or null body")
    void testCreateNullInputsRejected() {
        Patient patient = new Patient();
        AuthoritativePersistenceResult<IBaseResource> resultNullKey = adapter.create(null, patient);
        assertThat(resultNullKey).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);

        ResourceKey key = ResourceKey.of("Patient", "pat-1");
        AuthoritativePersistenceResult<IBaseResource> resultNullBody = adapter.create(key, null);
        assertThat(resultNullBody).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
    }

    @Test
    @DisplayName("6. UPDATE with matching predecessor version: commits incremented version 2")
    void testUpdateMatchingPredecessorSuccess() {
        String id = "pat-" + UUID.randomUUID();
        ResourceKey key = ResourceKey.of("Patient", id);

        Patient patient = new Patient();
        patient.setActive(true);
        patient.addName(new HumanName().setFamily("Original"));

        AuthoritativePersistenceResult<IBaseResource> createRes = adapter.create(key, patient);
        assertThat(createRes.isCommitted()).isTrue();

        Patient updateState = new Patient();
        updateState.setActive(false);
        updateState.addName(new HumanName().setFamily("Updated"));

        AuthoritativePersistenceResult<IBaseResource> updateRes =
                adapter.update(key, updateState, ExpectedAuthoritativeVersion.of(1L));

        assertThat(updateRes).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
        assertThat(updateRes.isCommitted()).isTrue();

        AuthoritativePersistenceResult.Committed<IBaseResource> committed =
                (AuthoritativePersistenceResult.Committed<IBaseResource>) updateRes;
        assertThat(committed.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of(2L));
        assertThat(committed.persistedResource().getIdElement().getVersionIdPart()).isEqualTo("2");
    }

    @Test
    @DisplayName("7. UPDATE with predecessor mismatch: rejected with Conflict(EXPECTED_VERSION_MISMATCH)")
    void testUpdatePredecessorMismatchConflict() {
        String id = "pat-" + UUID.randomUUID();
        ResourceKey key = ResourceKey.of("Patient", id);

        Patient patient = new Patient();
        patient.setActive(true);
        patient.addName(new HumanName().setFamily("Original"));
        AuthoritativePersistenceResult<IBaseResource> createRes = adapter.create(key, patient);
        assertThat(createRes.isCommitted()).isTrue();

        Patient updateState = new Patient();
        updateState.setActive(false);

        // Expected 2 but current is 1
        AuthoritativePersistenceResult<IBaseResource> updateRes =
                adapter.update(key, updateState, ExpectedAuthoritativeVersion.of(2L));

        assertThat(updateRes).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);
        assertThat(updateRes.isCommitted()).isFalse();

        AuthoritativePersistenceResult.Conflict<IBaseResource> conflict =
                (AuthoritativePersistenceResult.Conflict<IBaseResource>) updateRes;
        assertThat(conflict.conflict().reason()).isEqualTo(PreconditionFailureReason.EXPECTED_VERSION_MISMATCH);
        assertThat(conflict.conflict().key()).isEqualTo(key);
    }

    @Test
    @DisplayName("8. UPDATE on non-existent resource: rejected with Conflict(EXPECTED_VERSION_MISMATCH)")
    void testUpdateNonExistentResourceConflict() {
        String id = "absent-" + UUID.randomUUID();
        ResourceKey key = ResourceKey.of("Patient", id);

        Patient updateState = new Patient();
        updateState.setActive(true);

        AuthoritativePersistenceResult<IBaseResource> updateRes =
                adapter.update(key, updateState, ExpectedAuthoritativeVersion.of(1L));

        assertThat(updateRes).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);
        AuthoritativePersistenceResult.Conflict<IBaseResource> conflict =
                (AuthoritativePersistenceResult.Conflict<IBaseResource>) updateRes;
        assertThat(conflict.conflict().reason()).isEqualTo(PreconditionFailureReason.EXPECTED_VERSION_MISMATCH);
    }

    @Test
    @DisplayName("9. UPDATE rejected on missing or malformed expected version")
    void testUpdateMissingOrMalformedExpectedVersion() {
        ResourceKey key = ResourceKey.of("Patient", "pat-" + UUID.randomUUID());
        Patient patient = new Patient();

        AuthoritativePersistenceResult<IBaseResource> resNull = adapter.update(key, patient, null);
        assertThat(resNull).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);

        AuthoritativePersistenceResult<IBaseResource> resNone = adapter.update(key, patient, ExpectedAuthoritativeVersion.none());
        assertThat(resNone).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);

        AuthoritativePersistenceResult<IBaseResource> resMalformed = adapter.update(key, patient, ExpectedAuthoritativeVersion.of("invalid-ver"));
        assertThat(resMalformed).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);
    }

    @Test
    @DisplayName("10. UPDATE rejected on immutable resource type (AuditEvent)")
    void testUpdateImmutableResourceRejected() {
        String id = "audit-" + UUID.randomUUID();
        ResourceKey key = ResourceKey.of("AuditEvent", id);

        AuditEvent auditEvent = new AuditEvent();
        AuthoritativePersistenceResult<IBaseResource> result =
                adapter.update(key, auditEvent, ExpectedAuthoritativeVersion.of(1L));

        assertThat(result).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
    }

    @Test
    @DisplayName("11. READ: returns Committed for existing resource with matching state and version")
    void testReadExistingResource() {
        String id = "pat-" + UUID.randomUUID();
        ResourceKey key = ResourceKey.of("Patient", id);

        Patient patient = new Patient();
        patient.setActive(true);
        patient.addName(new HumanName().setFamily("Taylor").addGiven("Sam"));

        AuthoritativePersistenceResult<IBaseResource> createRes = adapter.create(key, patient);
        assertThat(createRes.isCommitted()).isTrue();

        AuthoritativePersistenceResult<IBaseResource> readRes = adapter.read(key);
        assertThat(readRes).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
        assertThat(readRes.isCommitted()).isTrue();

        AuthoritativePersistenceResult.Committed<IBaseResource> committed =
                (AuthoritativePersistenceResult.Committed<IBaseResource>) readRes;
        assertThat(committed.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of(1L));
        Patient readPatient = (Patient) committed.persistedResource();
        assertThat(readPatient.getIdElement().getIdPart()).isEqualTo(id);
    }

    @Test
    @DisplayName("12. READ: returns NotCommitted for non-existent resource or null key")
    void testReadNonExistentResource() {
        ResourceKey absentKey = ResourceKey.of("Patient", "absent-" + UUID.randomUUID());
        AuthoritativePersistenceResult<IBaseResource> readRes = adapter.read(absentKey);
        assertThat(readRes).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);

        AuthoritativePersistenceResult<IBaseResource> nullKeyRes = adapter.read(null);
        assertThat(nullKeyRes).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
    }

    @Test
    @DisplayName("13. Sequential UPDATEs: monotonic version progression V1 -> V2 -> V3 -> V4 -> V5")
    void testSequentialUpdatesProgression() {
        String id = "pat-" + UUID.randomUUID();
        ResourceKey key = ResourceKey.of("Patient", id);

        Patient state = new Patient();
        state.setActive(true);
        state.addName(new HumanName().setFamily("Version-1"));

        AuthoritativePersistenceResult<IBaseResource> res = adapter.create(key, state);
        assertThat(res.isCommitted()).isTrue();
        assertThat(((AuthoritativePersistenceResult.Committed<IBaseResource>) res).authoritativeVersion())
                .isEqualTo(AuthoritativeVersion.of(1L));

        for (long currentVer = 1; currentVer < 5; currentVer++) {
            long nextVer = currentVer + 1;
            Patient nextState = new Patient();
            nextState.setActive(true);
            nextState.addName(new HumanName().setFamily("Version-" + nextVer));

            AuthoritativePersistenceResult<IBaseResource> updateRes =
                    adapter.update(key, nextState, ExpectedAuthoritativeVersion.of(currentVer));

            assertThat(updateRes.isCommitted()).isTrue();
            assertThat(((AuthoritativePersistenceResult.Committed<IBaseResource>) updateRes).authoritativeVersion())
                    .isEqualTo(AuthoritativeVersion.of(nextVer));
        }

        AuthoritativePersistenceResult<IBaseResource> finalRead = adapter.read(key);
        assertThat(finalRead.isCommitted()).isTrue();
        assertThat(((AuthoritativePersistenceResult.Committed<IBaseResource>) finalRead).authoritativeVersion())
                .isEqualTo(AuthoritativeVersion.of(5L));
    }
}
