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

package net.fhirfactory.harmonia.hapifhir.governed;

import net.fhirfactory.harmonia.hapifhir.persistence.AuthoritativePersistencePort;
import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateCoordinationResult;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateCoordinator;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateConvergencePort;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateToken;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateTokenBridge;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativePreconditionConflict;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.ConvergenceStatus;
import net.fhirfactory.harmonia.model.governedwrite.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.GovernedRead;
import net.fhirfactory.harmonia.model.governedwrite.PreconditionFailureReason;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import net.fhirfactory.harmonia.model.governedwrite.WriteResult;
import net.fhirfactory.harmonia.themis.api.ThemisAuthorizer;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultGovernedWriterTest {

    @Mock
    private ThemisAuthorizer themisAuthorizer;

    @Mock
    private ActiveStateCoordinator activeStateCoordinator;

    @Mock
    private AuthoritativePersistencePort<IBaseResource> persistencePort;

    @Mock
    private ActiveStateConvergencePort convergencePort;

    private DefaultGovernedWriter governedWriter;

    private final ResourceKey sampleKey = ResourceKey.of("Patient", "pat-100");
    private final ThemisSecurityContext sampleContext = new ThemisSecurityContext(
            ThemisPrincipal.human("dr-alice"),
            "corr-123",
            "caus-456",
            "hospital-a",
            "192.168.1.1",
            null,
            null
    );

    @BeforeEach
    void setUp() {
        governedWriter = new DefaultGovernedWriter(
                themisAuthorizer,
                activeStateCoordinator,
                persistencePort,
                convergencePort
        );
    }

    @Test
    @DisplayName("Fail-closed validation on null parameters")
    void testFailClosedParameterValidation() {
        Patient patient = new Patient();
        ActiveStateToken token = ActiveStateTokenBridge.create(1L);
        GovernedRead<Patient> read = GovernedRead.of(sampleKey, patient, token, AuthoritativeVersion.of(1L));

        assertThatThrownBy(() -> governedWriter.create(null, patient, sampleContext))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> governedWriter.create(sampleKey, null, sampleContext))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> governedWriter.create(sampleKey, patient, null))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> governedWriter.update(null, patient, sampleContext))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> governedWriter.update(read, null, sampleContext))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> governedWriter.update(read, patient, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ==================== UPDATE SCENARIOS ====================

    @Test
    @DisplayName("UPDATE Scenario 1: Authorized + Consumed + Committed + Converged -> Committed(CONVERGED)")
    void testUpdateHappyPath() {
        Patient currentPatient = new Patient();
        currentPatient.setId(sampleKey.id());
        ActiveStateToken token = ActiveStateTokenBridge.create(100L);
        GovernedRead<Patient> read = GovernedRead.of(sampleKey, currentPatient, token, AuthoritativeVersion.of(1L));

        Patient proposedPatient = new Patient();
        proposedPatient.setId(sampleKey.id());
        proposedPatient.addName(new HumanName().setFamily("Updated"));

        // 1. Themis allows
        when(themisAuthorizer.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.allow("allow-all", "corr-123"));

        // 2. Active token consumed
        when(activeStateCoordinator.consume(sampleKey, token))
                .thenReturn(ActiveStateCoordinationResult.CONSUMED);

        // 3. Mnemosyne persists V2
        when(persistencePort.update(eq(sampleKey), eq(proposedPatient), eq(ExpectedAuthoritativeVersion.of(AuthoritativeVersion.of(1L)))))
                .thenReturn(new AuthoritativePersistenceResult.Committed<>(proposedPatient, AuthoritativeVersion.of(2L)));

        // 4. Convergence succeeds
        when(convergencePort.converge(eq(sampleKey), eq(proposedPatient), eq(AuthoritativeVersion.of(2L))))
                .thenReturn(ConvergenceStatus.CONVERGED);

        WriteResult<Patient> result = governedWriter.update(read, proposedPatient, sampleContext);

        assertThat(result.isCommitted()).isTrue();
        assertThat(result.convergenceStatus()).isEqualTo(ConvergenceStatus.CONVERGED);
        assertThat(result.committedVersion()).contains(AuthoritativeVersion.of(2L));
        assertThat(result.committedResource()).contains(proposedPatient);
    }

    @Test
    @DisplayName("UPDATE Scenario 2: Themis Denies -> NotCommitted (Zero coordination or persistence)")
    void testUpdateThemisDenied() {
        Patient currentPatient = new Patient();
        ActiveStateToken token = ActiveStateTokenBridge.create(100L);
        GovernedRead<Patient> read = GovernedRead.of(sampleKey, currentPatient, token, AuthoritativeVersion.of(1L));
        Patient proposed = new Patient();

        when(themisAuthorizer.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.defaultDeny("corr-123", "Forbidden role"));

        WriteResult<Patient> result = governedWriter.update(read, proposed, sampleContext);

        assertThat(result.isCommitted()).isFalse();
        assertThat(result).isInstanceOf(WriteResult.NotCommitted.class);
        assertThat(result.failureReason()).contains("Themis authorization denied: Forbidden role");

        verify(activeStateCoordinator, never()).consume(any(), any());
        verify(persistencePort, never()).update(any(), any(), any());
        verify(convergencePort, never()).converge(any(), any(), any());
    }

    @Test
    @DisplayName("UPDATE Scenario 3: Stale ActiveStateToken -> ActiveConflict (Zero persistence)")
    void testUpdateStaleActiveToken() {
        Patient currentPatient = new Patient();
        ActiveStateToken token = ActiveStateTokenBridge.create(100L);
        GovernedRead<Patient> read = GovernedRead.of(sampleKey, currentPatient, token, AuthoritativeVersion.of(1L));
        Patient proposed = new Patient();

        when(themisAuthorizer.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.allow("allow-all", "corr-123"));
        when(activeStateCoordinator.consume(sampleKey, token))
                .thenReturn(ActiveStateCoordinationResult.STALE);

        WriteResult<Patient> result = governedWriter.update(read, proposed, sampleContext);

        assertThat(result.isCommitted()).isFalse();
        assertThat(result).isInstanceOf(WriteResult.ActiveConflict.class);
        assertThat(result.isConflict()).isTrue();

        verify(persistencePort, never()).update(any(), any(), any());
        verify(convergencePort, never()).converge(any(), any(), any());
    }

    @Test
    @DisplayName("UPDATE Scenario 4: Mneme Unavailable -> NotCommitted fail-fast without local fallback")
    void testUpdateMnemeUnavailable() {
        Patient currentPatient = new Patient();
        ActiveStateToken token = ActiveStateTokenBridge.create(100L);
        GovernedRead<Patient> read = GovernedRead.of(sampleKey, currentPatient, token, AuthoritativeVersion.of(1L));
        Patient proposed = new Patient();

        when(themisAuthorizer.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.allow("allow-all", "corr-123"));
        when(activeStateCoordinator.consume(sampleKey, token))
                .thenReturn(ActiveStateCoordinationResult.UNAVAILABLE);

        WriteResult<Patient> result = governedWriter.update(read, proposed, sampleContext);

        assertThat(result.isCommitted()).isFalse();
        assertThat(result).isInstanceOf(WriteResult.NotCommitted.class);
        assertThat(result.failureReason()).contains("Active state coordination unavailable");

        verify(persistencePort, never()).update(any(), any(), any());
    }

    @Test
    @DisplayName("UPDATE Scenario 5: Authoritative Expected Version Mismatch -> AuthoritativeConflict (Token remains consumed)")
    void testUpdateAuthoritativePreconditionConflict() {
        Patient currentPatient = new Patient();
        ActiveStateToken token = ActiveStateTokenBridge.create(100L);
        GovernedRead<Patient> read = GovernedRead.of(sampleKey, currentPatient, token, AuthoritativeVersion.of(1L));
        Patient proposed = new Patient();

        when(themisAuthorizer.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.allow("allow-all", "corr-123"));
        when(activeStateCoordinator.consume(sampleKey, token))
                .thenReturn(ActiveStateCoordinationResult.CONSUMED);

        AuthoritativePreconditionConflict conflict = AuthoritativePreconditionConflict.expectedVersionMismatch(
                sampleKey,
                ExpectedAuthoritativeVersion.of(AuthoritativeVersion.of(1L)),
                AuthoritativeVersion.of(3L)
        );
        when(persistencePort.update(eq(sampleKey), eq(proposed), any()))
                .thenReturn(new AuthoritativePersistenceResult.Conflict<>(conflict));

        WriteResult<Patient> result = governedWriter.update(read, proposed, sampleContext);

        assertThat(result.isCommitted()).isFalse();
        assertThat(result).isInstanceOf(WriteResult.AuthoritativeConflict.class);
        assertThat(result.authoritativeConflict()).contains(conflict);

        verify(convergencePort, never()).converge(any(), any(), any());
    }

    @Test
    @DisplayName("UPDATE Scenario 6: Persistence Not Committed -> NotCommitted")
    void testUpdatePersistenceNotCommitted() {
        Patient currentPatient = new Patient();
        ActiveStateToken token = ActiveStateTokenBridge.create(100L);
        GovernedRead<Patient> read = GovernedRead.of(sampleKey, currentPatient, token, AuthoritativeVersion.of(1L));
        Patient proposed = new Patient();

        when(themisAuthorizer.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.allow("allow-all", "corr-123"));
        when(activeStateCoordinator.consume(sampleKey, token))
                .thenReturn(ActiveStateCoordinationResult.CONSUMED);

        when(persistencePort.update(eq(sampleKey), eq(proposed), any()))
                .thenReturn(new AuthoritativePersistenceResult.NotCommitted<>("Database transaction deadlock"));

        WriteResult<Patient> result = governedWriter.update(read, proposed, sampleContext);

        assertThat(result.isCommitted()).isFalse();
        assertThat(result).isInstanceOf(WriteResult.NotCommitted.class);
        assertThat(result.failureReason()).contains("Database transaction deadlock");

        verify(convergencePort, never()).converge(any(), any(), any());
    }

    @Test
    @DisplayName("UPDATE Scenario 7: Persistence Unknown -> OutcomeUnknown (No automatic retry or rollback)")
    void testUpdatePersistenceOutcomeUnknown() {
        Patient currentPatient = new Patient();
        ActiveStateToken token = ActiveStateTokenBridge.create(100L);
        GovernedRead<Patient> read = GovernedRead.of(sampleKey, currentPatient, token, AuthoritativeVersion.of(1L));
        Patient proposed = new Patient();

        when(themisAuthorizer.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.allow("allow-all", "corr-123"));
        when(activeStateCoordinator.consume(sampleKey, token))
                .thenReturn(ActiveStateCoordinationResult.CONSUMED);

        when(persistencePort.update(eq(sampleKey), eq(proposed), any()))
                .thenReturn(new AuthoritativePersistenceResult.OutcomeUnknown<>("Database commit ACK timed out"));

        WriteResult<Patient> result = governedWriter.update(read, proposed, sampleContext);

        assertThat(result.isCommitted()).isFalse();
        assertThat(result.isOutcomeUnknown()).isTrue();
        assertThat(result).isInstanceOf(WriteResult.OutcomeUnknown.class);
        assertThat(result.failureReason()).contains("Database commit ACK timed out");

        verify(convergencePort, never()).converge(any(), any(), any());
    }

    @Test
    @DisplayName("UPDATE Scenario 8: Committed + Degraded Convergence -> Committed(DEGRADED)")
    void testUpdateCommittedWithDegradedConvergence() {
        Patient currentPatient = new Patient();
        ActiveStateToken token = ActiveStateTokenBridge.create(100L);
        GovernedRead<Patient> read = GovernedRead.of(sampleKey, currentPatient, token, AuthoritativeVersion.of(1L));
        Patient proposed = new Patient();
        proposed.setId(sampleKey.id());

        when(themisAuthorizer.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.allow("allow-all", "corr-123"));
        when(activeStateCoordinator.consume(sampleKey, token))
                .thenReturn(ActiveStateCoordinationResult.CONSUMED);

        when(persistencePort.update(eq(sampleKey), eq(proposed), any()))
                .thenReturn(new AuthoritativePersistenceResult.Committed<>(proposed, AuthoritativeVersion.of(2L)));

        // Convergence returns DEGRADED
        when(convergencePort.converge(eq(sampleKey), eq(proposed), eq(AuthoritativeVersion.of(2L))))
                .thenReturn(ConvergenceStatus.DEGRADED);

        WriteResult<Patient> result = governedWriter.update(read, proposed, sampleContext);

        assertThat(result.isCommitted()).isTrue();
        assertThat(result.convergenceStatus()).isEqualTo(ConvergenceStatus.DEGRADED);
        assertThat(result.committedVersion()).contains(AuthoritativeVersion.of(2L));
        assertThat(result.degradationReason()).contains("Active-state cache convergence degraded");
    }

    // ==================== CREATE SCENARIOS ====================

    @Test
    @DisplayName("CREATE Scenario 9: Authorized + Committed + Converged -> Committed(CONVERGED)")
    void testCreateHappyPath() {
        Patient newPatient = new Patient();
        newPatient.setId(sampleKey.id());
        newPatient.addName(new HumanName().setFamily("Newborn"));

        when(themisAuthorizer.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.allow("allow-all", "corr-123"));

        when(persistencePort.create(eq(sampleKey), eq(newPatient)))
                .thenReturn(new AuthoritativePersistenceResult.Committed<>(newPatient, AuthoritativeVersion.of(1L)));

        when(convergencePort.converge(eq(sampleKey), eq(newPatient), eq(AuthoritativeVersion.of(1L))))
                .thenReturn(ConvergenceStatus.CONVERGED);

        WriteResult<Patient> result = governedWriter.create(sampleKey, newPatient, sampleContext);

        assertThat(result.isCommitted()).isTrue();
        assertThat(result.convergenceStatus()).isEqualTo(ConvergenceStatus.CONVERGED);
        assertThat(result.committedVersion()).contains(AuthoritativeVersion.of(1L));
        assertThat(result.committedResource()).contains(newPatient);

        // CREATE deliberately performs no pre-persistence active token coordination
        verify(activeStateCoordinator, never()).consume(any(), any());
    }

    @Test
    @DisplayName("CREATE Scenario 10: Duplicate Resource -> AuthoritativeConflict(RESOURCE_ALREADY_EXISTS)")
    void testCreateDuplicateResourceConflict() {
        Patient newPatient = new Patient();
        newPatient.setId(sampleKey.id());

        when(themisAuthorizer.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.allow("allow-all", "corr-123"));

        AuthoritativePreconditionConflict conflict = AuthoritativePreconditionConflict.resourceAlreadyExists(
                sampleKey,
                AuthoritativeVersion.of(1L)
        );
        when(persistencePort.create(eq(sampleKey), eq(newPatient)))
                .thenReturn(new AuthoritativePersistenceResult.Conflict<>(conflict));

        WriteResult<Patient> result = governedWriter.create(sampleKey, newPatient, sampleContext);

        assertThat(result.isCommitted()).isFalse();
        assertThat(result).isInstanceOf(WriteResult.AuthoritativeConflict.class);
        assertThat(result.authoritativeConflict()).contains(conflict);
        assertThat(conflict.reason()).isEqualTo(PreconditionFailureReason.RESOURCE_ALREADY_EXISTS);

        verify(convergencePort, never()).converge(any(), any(), any());
    }

    @Test
    @DisplayName("CREATE Scenario 11: Themis Denied -> NotCommitted (Zero persistence or convergence)")
    void testCreateThemisDenied() {
        Patient newPatient = new Patient();
        newPatient.setId(sampleKey.id());

        when(themisAuthorizer.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.defaultDeny("corr-123", "Policy check failed"));

        WriteResult<Patient> result = governedWriter.create(sampleKey, newPatient, sampleContext);

        assertThat(result.isCommitted()).isFalse();
        assertThat(result).isInstanceOf(WriteResult.NotCommitted.class);
        assertThat(result.failureReason()).contains("Themis authorization denied: Policy check failed");

        verify(persistencePort, never()).create(any(), any());
        verify(convergencePort, never()).converge(any(), any(), any());
    }

    @Test
    @DisplayName("CREATE Scenario 12: Persistence Outcome Unknown -> OutcomeUnknown")
    void testCreatePersistenceOutcomeUnknown() {
        Patient newPatient = new Patient();
        newPatient.setId(sampleKey.id());

        when(themisAuthorizer.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.allow("allow-all", "corr-123"));

        when(persistencePort.create(eq(sampleKey), eq(newPatient)))
                .thenReturn(new AuthoritativePersistenceResult.OutcomeUnknown<>("Database cluster partition"));

        WriteResult<Patient> result = governedWriter.create(sampleKey, newPatient, sampleContext);

        assertThat(result.isCommitted()).isFalse();
        assertThat(result.isOutcomeUnknown()).isTrue();
        assertThat(result).isInstanceOf(WriteResult.OutcomeUnknown.class);

        verify(convergencePort, never()).converge(any(), any(), any());
    }

    @Test
    @DisplayName("CREATE Scenario 13: Committed + Degraded Convergence -> Committed(DEGRADED)")
    void testCreateCommittedWithDegradedConvergence() {
        Patient newPatient = new Patient();
        newPatient.setId(sampleKey.id());

        when(themisAuthorizer.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.allow("allow-all", "corr-123"));

        when(persistencePort.create(eq(sampleKey), eq(newPatient)))
                .thenReturn(new AuthoritativePersistenceResult.Committed<>(newPatient, AuthoritativeVersion.of(1L)));

        when(convergencePort.converge(eq(sampleKey), eq(newPatient), eq(AuthoritativeVersion.of(1L))))
                .thenReturn(ConvergenceStatus.DEGRADED);

        WriteResult<Patient> result = governedWriter.create(sampleKey, newPatient, sampleContext);

        assertThat(result.isCommitted()).isTrue();
        assertThat(result.convergenceStatus()).isEqualTo(ConvergenceStatus.DEGRADED);
        assertThat(result.committedVersion()).contains(AuthoritativeVersion.of(1L));
    }
}
