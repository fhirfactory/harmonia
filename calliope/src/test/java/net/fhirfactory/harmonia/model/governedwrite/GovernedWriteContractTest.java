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

package net.fhirfactory.harmonia.model.governedwrite;

import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests verifying the foundational governed-write contracts and API semantics.
 */
class GovernedWriteContractTest {

    private final ResourceKey sampleKey = ResourceKey.of("Practitioner", "prac-123");
    private final ThemisSecurityContext sampleContext = new ThemisSecurityContext(
            ThemisPrincipal.human("user-1"),
            "corr-1",
            "caus-1",
            "tenant-1",
            "127.0.0.1",
            null,
            null
    );

    @Test
    @DisplayName("1. GovernedRead carries active and authoritative concurrency context")
    void governedReadCarriesActiveAndAuthoritativeConcurrencyContext() {
        ActiveStateToken activeToken = ActiveStateTokenBridge.create(1001L);
        AuthoritativeVersion authVersion = AuthoritativeVersion.of(5L);
        String payload = "{\"resourceType\":\"Practitioner\",\"id\":\"prac-123\"}";

        GovernedRead<String> read = GovernedRead.of(sampleKey, payload, activeToken, authVersion);

        assertThat(read.key()).isEqualTo(sampleKey);
        assertThat(read.resource()).isEqualTo(payload);
        assertThat(read.activeToken()).isEqualTo(activeToken);
        assertThat(read.authoritativeVersion()).isEqualTo(authVersion);
        assertThat(read.expectedAuthoritativeVersion().value()).contains("5");
        assertThat(read.expectedAuthoritativeVersion().isNone()).isFalse();
    }

    @Test
    @DisplayName("2. ActiveStateToken is opaque with no arithmetic or ordering and bridged version access")
    void activeStateTokenIsOpaque() {
        ActiveStateToken token1 = ActiveStateTokenBridge.create(1001L);
        ActiveStateToken token2 = ActiveStateTokenBridge.create(1001L);
        ActiveStateToken token3 = ActiveStateTokenBridge.create(2002L);

        assertThat(token1).isEqualTo(token2);
        assertThat(token1).isNotEqualTo(token3);
        assertThat(token1.toString()).doesNotContain("1001");
        assertThat(token1.toString()).isEqualTo("ActiveStateToken[opaque]");

        // Verify invalid inputs are rejected
        assertThatThrownBy(() -> ActiveStateTokenBridge.create(-1L))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> ActiveStateTokenBridge.extractVersion(null))
                .isInstanceOf(NullPointerException.class);

        // Verify bridge extracts expected version
        assertThat(ActiveStateTokenBridge.extractVersion(token1)).isEqualTo(1001L);
        assertThat(ActiveStateTokenBridge.extractVersion(token3)).isEqualTo(2002L);

        // Verify token class has no public constructors
        assertThat(ActiveStateToken.class.getConstructors()).isEmpty();

        // Verify token class does not implement Comparable or expose numeric manipulation or unwrapping
        assertThat(Comparable.class.isAssignableFrom(ActiveStateToken.class)).isFalse();
        List<String> methodNames = Arrays.stream(ActiveStateToken.class.getDeclaredMethods())
                .map(Method::getName)
                .toList();
        assertThat(methodNames).doesNotContain(
                "none", "isPresent", "asOpaqueString", "increment", "add", "plus", "next",
                "compareTo", "getVersion", "longValue", "intValue", "asLong", "raw", "unwrap", "of"
        );
    }

    @Test
    @DisplayName("3. CREATE conflict can express RESOURCE_ALREADY_EXISTS")
    void createConflictCanExpressResourceAlreadyExists() {
        AuthoritativePreconditionConflict conflict = AuthoritativePreconditionConflict.resourceAlreadyExists(
                sampleKey,
                AuthoritativeVersion.of(1L)
        );

        assertThat(conflict.key()).isEqualTo(sampleKey);
        assertThat(conflict.reason()).isEqualTo(PreconditionFailureReason.RESOURCE_ALREADY_EXISTS);
        assertThat(conflict.expectedVersion().isNone()).isTrue();
        assertThat(conflict.currentVersionOptional()).contains(AuthoritativeVersion.of("1"));

        WriteResult<String> result = WriteResult.authoritativeConflict(conflict);
        assertThat(result.isCommitted()).isFalse();
        assertThat(result.isConflict()).isTrue();
        assertThat(result.commitOutcome()).isEqualTo(AuthoritativeCommitOutcome.NOT_COMMITTED);
        assertThat(result.authoritativeConflict()).contains(conflict);
        assertThat(result.activeConflict()).isEmpty();
    }

    @Test
    @DisplayName("4. UPDATE conflict can express EXPECTED_VERSION_MISMATCH")
    void updateConflictCanExpressExpectedVersionMismatch() {
        ExpectedAuthoritativeVersion expected = ExpectedAuthoritativeVersion.of(2L);
        AuthoritativeVersion current = AuthoritativeVersion.of(3L);

        AuthoritativePreconditionConflict conflict = AuthoritativePreconditionConflict.expectedVersionMismatch(
                sampleKey,
                expected,
                current
        );

        assertThat(conflict.key()).isEqualTo(sampleKey);
        assertThat(conflict.reason()).isEqualTo(PreconditionFailureReason.EXPECTED_VERSION_MISMATCH);
        assertThat(conflict.expectedVersion()).isEqualTo(expected);
        assertThat(conflict.currentVersionOptional()).contains(current);

        WriteResult<String> result = WriteResult.authoritativeConflict(conflict);
        assertThat(result.isCommitted()).isFalse();
        assertThat(result.isConflict()).isTrue();
        assertThat(result.commitOutcome()).isEqualTo(AuthoritativeCommitOutcome.NOT_COMMITTED);
        assertThat(result.authoritativeConflict()).contains(conflict);
    }

    @Test
    @DisplayName("5. Active conflict is distinct from authoritative conflict")
    void activeConflictIsDistinctFromAuthoritativeConflict() {
        ActiveStateConflict active = ActiveStateConflict.of(sampleKey, "Concurrent token update in progress");
        AuthoritativePreconditionConflict auth = AuthoritativePreconditionConflict.resourceAlreadyExists(sampleKey);

        WriteResult<String> activeResult = WriteResult.activeStateConflict(active);
        WriteResult<String> authResult = WriteResult.authoritativeConflict(auth);

        assertThat(activeResult).isNotEqualTo(authResult);
        assertThat(activeResult.activeConflict()).contains(active);
        assertThat(activeResult.authoritativeConflict()).isEmpty();

        assertThat(authResult.authoritativeConflict()).contains(auth);
        assertThat(authResult.activeConflict()).isEmpty();

        assertThat(activeResult.isConflict()).isTrue();
        assertThat(authResult.isConflict()).isTrue();
        assertThat(activeResult).isInstanceOf(WriteResult.ActiveConflict.class);
        assertThat(authResult).isInstanceOf(WriteResult.AuthoritativeConflict.class);
    }

    @Test
    @DisplayName("6. COMMITTED / NOT_COMMITTED / UNKNOWN are distinct outcomes")
    void committedNotCommittedAndUnknownAreDistinct() {
        assertThat(AuthoritativeCommitOutcome.COMMITTED).isNotEqualTo(AuthoritativeCommitOutcome.NOT_COMMITTED);
        assertThat(AuthoritativeCommitOutcome.COMMITTED).isNotEqualTo(AuthoritativeCommitOutcome.UNKNOWN);
        assertThat(AuthoritativeCommitOutcome.NOT_COMMITTED).isNotEqualTo(AuthoritativeCommitOutcome.UNKNOWN);

        WriteResult<String> committedResult = WriteResult.committed(sampleKey, "res", AuthoritativeVersion.of(1L));
        WriteResult<String> notCommittedResult = WriteResult.notCommitted(sampleKey, "validation failure");
        WriteResult<String> unknownResult = WriteResult.outcomeUnknown(sampleKey, "persistence connection timeout");

        assertThat(committedResult.commitOutcome()).isEqualTo(AuthoritativeCommitOutcome.COMMITTED);
        assertThat(committedResult.isCommitted()).isTrue();
        assertThat(committedResult.isOutcomeUnknown()).isFalse();

        assertThat(notCommittedResult.commitOutcome()).isEqualTo(AuthoritativeCommitOutcome.NOT_COMMITTED);
        assertThat(notCommittedResult.isCommitted()).isFalse();
        assertThat(notCommittedResult.isOutcomeUnknown()).isFalse();

        assertThat(unknownResult.commitOutcome()).isEqualTo(AuthoritativeCommitOutcome.UNKNOWN);
        assertThat(unknownResult.isCommitted()).isFalse();
        assertThat(unknownResult.isOutcomeUnknown()).isTrue();
    }

    @Test
    @DisplayName("7. Committed + degraded convergence is representable as committed")
    void committedPlusDegradedConvergenceIsRepresentableAsCommitted() {
        WriteResult<String> result = WriteResult.committedDegraded(
                sampleKey,
                "res-payload",
                AuthoritativeVersion.of(2L),
                "Mneme node communication failure during post-commit cache update"
        );

        assertThat(result.isCommitted()).isTrue();
        assertThat(result.commitOutcome()).isEqualTo(AuthoritativeCommitOutcome.COMMITTED);
        assertThat(result.convergenceStatus()).isEqualTo(ConvergenceStatus.DEGRADED);
        assertThat(result.committedResource()).contains("res-payload");
        assertThat(result.committedVersion()).contains(AuthoritativeVersion.of(2L));
        assertThat(result.degradationReason()).contains("Mneme node communication failure during post-commit cache update");
        assertThat(result.isOutcomeUnknown()).isFalse();
    }

    @Test
    @DisplayName("8. No governed DELETE exists on GovernedWriter")
    void noGovernedDeleteExistsOnGovernedWriter() {
        Method[] methods = GovernedWriter.class.getMethods();
        List<String> methodNames = Arrays.stream(methods)
                .map(Method::getName)
                .toList();

        assertThat(methodNames).containsExactlyInAnyOrder("create", "update");
        assertThat(methodNames).noneMatch(name -> name.toLowerCase().contains("delete") || name.toLowerCase().contains("remove"));
    }

    @Test
    @DisplayName("9. Invalid WriteResult combinations cannot readily be constructed")
    void invalidWriteResultCombinationsCannotReadilyBeConstructed() {
        // Null checks on record constructors
        assertThatThrownBy(() -> new WriteResult.Committed<>(null, "res", AuthoritativeVersion.of(1L), ConvergenceStatus.CONVERGED, null))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new WriteResult.Committed<>(sampleKey, null, AuthoritativeVersion.of(1L), ConvergenceStatus.CONVERGED, null))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new WriteResult.Committed<>(sampleKey, "res", null, ConvergenceStatus.CONVERGED, null))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new WriteResult.Committed<>(sampleKey, "res", AuthoritativeVersion.of(1L), null, null))
                .isInstanceOf(NullPointerException.class);

        // Committed cannot have NOT_APPLICABLE convergence status
        assertThatThrownBy(() -> new WriteResult.Committed<>(sampleKey, "res", AuthoritativeVersion.of(1L), ConvergenceStatus.NOT_APPLICABLE, null))
                .isInstanceOf(IllegalArgumentException.class);

        // Conflicts require non-null conflict objects
        assertThatThrownBy(() -> new WriteResult.ActiveConflict<String>(null))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new WriteResult.AuthoritativeConflict<String>(null))
                .isInstanceOf(NullPointerException.class);

        // GovernedRead requires non-null fields
        ActiveStateToken validToken = ActiveStateTokenBridge.create(1001L);
        assertThatThrownBy(() -> GovernedRead.of(null, "res", validToken, AuthoritativeVersion.of(1L)))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> GovernedRead.of(sampleKey, null, validToken, AuthoritativeVersion.of(1L)))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> GovernedRead.of(sampleKey, "res", null, AuthoritativeVersion.of(1L)))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> GovernedRead.of(sampleKey, "res", validToken, null))
                .isInstanceOf(NullPointerException.class);

        // ResourceKey blank checks
        assertThatThrownBy(() -> ResourceKey.of("", "id-1"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> ResourceKey.of("Practitioner", "   "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("10. ActiveStateCoordinationResult declares distinct outcomes")
    void activeStateCoordinationResultDeclaresDistinctOutcomes() {
        assertThat(ActiveStateCoordinationResult.values())
                .containsExactlyInAnyOrder(
                        ActiveStateCoordinationResult.CONSUMED,
                        ActiveStateCoordinationResult.STALE,
                        ActiveStateCoordinationResult.UNAVAILABLE
                );

        assertThat(ActiveStateCoordinationResult.valueOf("CONSUMED"))
                .isEqualTo(ActiveStateCoordinationResult.CONSUMED);
        assertThat(ActiveStateCoordinationResult.valueOf("STALE"))
                .isEqualTo(ActiveStateCoordinationResult.STALE);
        assertThat(ActiveStateCoordinationResult.valueOf("UNAVAILABLE"))
                .isEqualTo(ActiveStateCoordinationResult.UNAVAILABLE);

        assertThat(ActiveStateCoordinationResult.CONSUMED)
                .isNotEqualTo(ActiveStateCoordinationResult.STALE);
        assertThat(ActiveStateCoordinationResult.CONSUMED)
                .isNotEqualTo(ActiveStateCoordinationResult.UNAVAILABLE);
        assertThat(ActiveStateCoordinationResult.STALE)
                .isNotEqualTo(ActiveStateCoordinationResult.UNAVAILABLE);
    }

    @Test
    @DisplayName("11. ActiveStateCoordinator declares pure coordination methods without payload retrieval or delete")
    void activeStateCoordinatorDeclaresPureCoordinationMethods() throws NoSuchMethodException {
        assertThat(ActiveStateCoordinator.class.isInterface()).isTrue();

        Method observeMethod = ActiveStateCoordinator.class.getMethod("observe", ResourceKey.class);
        assertThat(observeMethod.getReturnType()).isEqualTo(ActiveStateToken.class);

        Method consumeMethod = ActiveStateCoordinator.class.getMethod("consume", ResourceKey.class, ActiveStateToken.class);
        assertThat(consumeMethod.getReturnType()).isEqualTo(ActiveStateCoordinationResult.class);

        Method[] methods = ActiveStateCoordinator.class.getMethods();
        List<String> methodNames = Arrays.stream(methods)
                .map(Method::getName)
                .toList();

        assertThat(methodNames).containsExactlyInAnyOrder("observe", "consume");
        assertThat(methodNames).noneMatch(name -> name.toLowerCase().contains("delete") || name.toLowerCase().contains("remove"));
    }

    @Test
    @DisplayName("12. ActiveStateConvergencePort declares pure convergence method without delete or payload eviction")
    void activeStateConvergencePortDeclaresPureConvergenceMethod() throws NoSuchMethodException {
        assertThat(ActiveStateConvergencePort.class.isInterface()).isTrue();

        Method convergeMethod = ActiveStateConvergencePort.class.getMethod("converge", ResourceKey.class, Object.class, AuthoritativeVersion.class);
        assertThat(convergeMethod.getReturnType()).isEqualTo(ConvergenceStatus.class);

        Method[] methods = ActiveStateConvergencePort.class.getMethods();
        List<String> methodNames = Arrays.stream(methods)
                .map(Method::getName)
                .toList();

        assertThat(methodNames).containsExactlyInAnyOrder("converge");
        assertThat(methodNames).noneMatch(name -> name.toLowerCase().contains("delete") || name.toLowerCase().contains("remove") || name.toLowerCase().contains("evict"));
    }

    @Test
    @DisplayName("13. ResourceKey enforces structural validity while remaining policy-neutral")
    void resourceKeyEnforcesStructuralValidityWhileRemainingPolicyNeutral() {
        // Structurally valid Provider Registry types succeed
        ResourceKey keyPrac = ResourceKey.of("Practitioner", "prac-1");
        assertThat(keyPrac.resourceType()).isEqualTo("Practitioner");
        assertThat(keyPrac.id()).isEqualTo("prac-1");
        assertThat(keyPrac.toQualifiedPath()).isEqualTo("Practitioner/prac-1");

        // Structurally valid non-Provider-Registry / unclassified types also succeed at construction
        ResourceKey keyPatient = ResourceKey.of("Patient", "pat-999");
        assertThat(keyPatient.resourceType()).isEqualTo("Patient");
        assertThat(keyPatient.id()).isEqualTo("pat-999");
        assertThat(keyPatient.toQualifiedPath()).isEqualTo("Patient/pat-999");

        ResourceKey keyCustom = ResourceKey.of("CustomDomainEntity", "custom-42");
        assertThat(keyCustom.resourceType()).isEqualTo("CustomDomainEntity");
        assertThat(keyCustom.id()).isEqualTo("custom-42");
        assertThat(keyCustom.toQualifiedPath()).isEqualTo("CustomDomainEntity/custom-42");

        // Null checks
        assertThatThrownBy(() -> ResourceKey.of(null, "id-1"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("resourceType");
        assertThatThrownBy(() -> ResourceKey.of("Practitioner", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("id");

        // Blank checks
        assertThatThrownBy(() -> ResourceKey.of("", "id-1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("resourceType");
        assertThatThrownBy(() -> ResourceKey.of("   ", "id-1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("resourceType");
        assertThatThrownBy(() -> ResourceKey.of("Practitioner", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id");
        assertThatThrownBy(() -> ResourceKey.of("Practitioner", "   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id");
    }

    @Test
    @DisplayName("14. GovernedBoundaryValidator enforces fail-closed classification on managed types")
    void governedBoundaryValidatorEnforcesFailClosedClassification() {
        // Supported Provider Registry types are accepted
        List<String> supportedTypes = List.of(
                "Practitioner",
                "PractitionerRole",
                "Organization",
                "Location",
                "HealthcareService",
                "Endpoint",
                "Group"
        );
        for (String type : supportedTypes) {
            assertThat(GovernedBoundaryValidator.isManagedType(type)).isTrue();
            ResourceKey key = ResourceKey.of(type, "id-test");
            assertThat(GovernedBoundaryValidator.isManagedType(key)).isTrue();
            // requireManagedType must not throw
            GovernedBoundaryValidator.requireManagedType(key);
            GovernedBoundaryValidator.requireManagedType(type);
        }

        // Unclassified / unsupported types are rejected fail-closed
        List<String> unsupportedTypes = List.of(
                "Patient",
                "Observation",
                "Encounter",
                "Condition",
                "UnclassifiedType",
                "ArbitraryEntity"
        );
        for (String type : unsupportedTypes) {
            assertThat(GovernedBoundaryValidator.isManagedType(type)).isFalse();
            ResourceKey key = ResourceKey.of(type, "id-test");
            assertThat(GovernedBoundaryValidator.isManagedType(key)).isFalse();

            assertThatThrownBy(() -> GovernedBoundaryValidator.requireManagedType(key))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("not a supported managed type");
            assertThatThrownBy(() -> GovernedBoundaryValidator.requireManagedType(type))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("not a supported managed type");
        }

        // Null and blank handling
        assertThat(GovernedBoundaryValidator.isManagedType((String) null)).isFalse();
        assertThat(GovernedBoundaryValidator.isManagedType((ResourceKey) null)).isFalse();
        assertThatThrownBy(() -> GovernedBoundaryValidator.requireManagedType((ResourceKey) null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> GovernedBoundaryValidator.requireManagedType((String) null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> GovernedBoundaryValidator.requireManagedType("   "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("15. GovernedReader declares pure point read without delete or mutation")
    void governedReaderDeclaresPurePointRead() throws NoSuchMethodException {
        assertThat(GovernedReader.class.isInterface()).isTrue();

        Method readMethod = GovernedReader.class.getMethod("read", ResourceKey.class, ThemisSecurityContext.class);
        assertThat(readMethod.getReturnType()).isEqualTo(Optional.class);

        Method[] methods = GovernedReader.class.getMethods();
        List<String> methodNames = Arrays.stream(methods)
                .map(Method::getName)
                .toList();

        assertThat(methodNames).containsExactlyInAnyOrder("read");
        assertThat(methodNames).noneMatch(name ->
                name.toLowerCase().contains("delete")
                        || name.toLowerCase().contains("remove")
                        || name.toLowerCase().contains("purge")
                        || name.toLowerCase().contains("evict")
        );
    }

    @Test
    @DisplayName("16. GovernedAccess composes GovernedReader and GovernedWriter with zero delete methods")
    void governedAccessComposesReaderAndWriterWithZeroDeleteMethods() {
        assertThat(GovernedAccess.class.isInterface()).isTrue();
        assertThat(GovernedReader.class.isAssignableFrom(GovernedAccess.class)).isTrue();
        assertThat(GovernedWriter.class.isAssignableFrom(GovernedAccess.class)).isTrue();

        Method[] methods = GovernedAccess.class.getMethods();
        List<String> methodNames = Arrays.stream(methods)
                .map(Method::getName)
                .toList();

        assertThat(methodNames).containsExactlyInAnyOrder("read", "create", "update");
        assertThat(methodNames).noneMatch(name ->
                name.toLowerCase().contains("delete")
                        || name.toLowerCase().contains("remove")
                        || name.toLowerCase().contains("purge")
                        || name.toLowerCase().contains("evict")
        );
    }

    @Test
    @DisplayName("17. GovernedRead preserves domain payload without destructive mutation")
    void governedReadPreservesDomainPayloadWithoutDestructiveMutation() {
        record ComplexDomainResource(String id, String name, String telecom, boolean active) {}

        ComplexDomainResource domainObj = new ComplexDomainResource("prac-42", "Dr. Jane Doe", "555-1234", true);
        ActiveStateToken activeToken = ActiveStateTokenBridge.create(9999L);
        AuthoritativeVersion authVersion = AuthoritativeVersion.of(7L);
        ResourceKey key = ResourceKey.of("Practitioner", "prac-42");

        GovernedRead<ComplexDomainResource> readEnvelope = GovernedRead.of(key, domainObj, activeToken, authVersion);

        // Domain object is intact and unmodified
        assertThat(readEnvelope.resource()).isSameAs(domainObj);
        assertThat(readEnvelope.resource().id()).isEqualTo("prac-42");
        assertThat(readEnvelope.resource().name()).isEqualTo("Dr. Jane Doe");
        assertThat(readEnvelope.resource().telecom()).isEqualTo("555-1234");
        assertThat(readEnvelope.resource().active()).isTrue();

        // Envelope carries active and authoritative coordination tokens
        assertThat(readEnvelope.key()).isEqualTo(key);
        assertThat(readEnvelope.activeToken()).isEqualTo(activeToken);
        assertThat(readEnvelope.authoritativeVersion()).isEqualTo(authVersion);
        assertThat(readEnvelope.expectedAuthoritativeVersion().value()).contains("7");
    }

    @Test
    @DisplayName("18. GovernedAccess boundary rejects unclassified types fail-closed")
    void governedAccessBoundaryRejectsUnclassifiedTypesFailClosed() {
        // Boundary implementation enforcing GovernedBoundaryValidator
        GovernedAccess boundaryAccess = new GovernedAccess() {
            @Override
            public <T> Optional<GovernedRead<T>> read(ResourceKey key, ThemisSecurityContext securityContext) {
                GovernedBoundaryValidator.requireManagedType(key);
                return Optional.empty();
            }

            @Override
            public <T> WriteResult<T> create(ResourceKey key, T resource, ThemisSecurityContext securityContext) {
                if (!GovernedBoundaryValidator.isManagedType(key)) {
                    return WriteResult.notCommitted(key, "Resource type [" + key.resourceType() + "] is unclassified");
                }
                return WriteResult.committed(key, resource, AuthoritativeVersion.of(1L));
            }

            @Override
            public <T> WriteResult<T> update(GovernedRead<T> current, T proposed, ThemisSecurityContext securityContext) {
                if (!GovernedBoundaryValidator.isManagedType(current.key())) {
                    return WriteResult.notCommitted(current.key(), "Resource type [" + current.key().resourceType() + "] is unclassified");
                }
                return WriteResult.committed(current.key(), proposed, AuthoritativeVersion.of(current.authoritativeVersion().longValue() + 1));
            }
        };

        // Classified types pass boundary validation
        ResourceKey validKey = ResourceKey.of("Practitioner", "prac-1");
        assertThat(boundaryAccess.read(validKey, sampleContext)).isEmpty();
        WriteResult<String> createResult = boundaryAccess.create(validKey, "payload", sampleContext);
        assertThat(createResult.isCommitted()).isTrue();

        GovernedRead<String> validRead = GovernedRead.of(
                validKey,
                "payload",
                ActiveStateTokenBridge.create(100L),
                AuthoritativeVersion.of(1L)
        );
        WriteResult<String> updateResult = boundaryAccess.update(validRead, "updated-payload", sampleContext);
        assertThat(updateResult.isCommitted()).isTrue();

        // Unclassified types fail closed at boundary
        ResourceKey unclassifiedKey = ResourceKey.of("Patient", "pat-1");
        assertThatThrownBy(() -> boundaryAccess.read(unclassifiedKey, sampleContext))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not a supported managed type");

        WriteResult<String> unclassifiedCreate = boundaryAccess.create(unclassifiedKey, "payload", sampleContext);
        assertThat(unclassifiedCreate.isCommitted()).isFalse();
        assertThat(unclassifiedCreate.commitOutcome()).isEqualTo(AuthoritativeCommitOutcome.NOT_COMMITTED);
    }
}
