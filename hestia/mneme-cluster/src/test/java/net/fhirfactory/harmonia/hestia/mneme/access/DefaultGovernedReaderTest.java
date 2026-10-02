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

package net.fhirfactory.harmonia.hestia.mneme.access;

import net.fhirfactory.harmonia.hapifhir.persistence.AuthoritativePersistencePort;
import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.hestia.mneme.coordination.ActiveCoordinationUnavailableException;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateCoordinator;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateConvergencePort;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateToken;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateTokenBridge;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativePreconditionConflict;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.ConvergenceStatus;
import net.fhirfactory.harmonia.model.governedwrite.GovernedRead;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import net.fhirfactory.harmonia.themis.api.ThemisAuthorizer;
import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Practitioner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultGovernedReaderTest {

    @Mock
    private ThemisAuthorizer themisAuthorizer;

    @Mock
    private AuthoritativePersistencePort<IBaseResource> persistencePort;

    @Mock
    private ActiveStateConvergencePort convergencePort;

    @Mock
    private ActiveStateCoordinator activeStateCoordinator;

    private DefaultGovernedReader governedReader;

    private final ResourceKey managedKey = ResourceKey.of("Practitioner", "pr-100");
    private final ResourceKey unmanagedKey = ResourceKey.of("Patient", "pat-100");
    private final ThemisSecurityContext sampleContext = ThemisSecurityContext.builder()
            .correlationId("corr-123")
            .requestingPrincipal(ThemisPrincipal.human("prac-456"))
            .clientIp("127.0.0.1")
            .tenantId("HarmoniaApp")
            .build();

    @BeforeEach
    void setUp() {
        governedReader = new DefaultGovernedReader(
                themisAuthorizer,
                persistencePort,
                convergencePort,
                activeStateCoordinator
        );
    }

    // ==================== ARGUMENT VALIDATION ====================

    @Test
    @DisplayName("Null arguments fail fast with IllegalArgumentException")
    void read_nullArguments_throwsException() {
        assertThatThrownBy(() -> governedReader.read(null, sampleContext))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ResourceKey must not be null");

        assertThatThrownBy(() -> governedReader.read(managedKey, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ThemisSecurityContext must not be null");
    }

    @Test
    @DisplayName("Unmanaged resource type rejected fail-closed via Calliope boundary validator")
    void read_unmanagedType_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> governedReader.read(unmanagedKey, sampleContext))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not a supported managed type");

        verify(themisAuthorizer, never()).authorize(any());
        verify(persistencePort, never()).read(any());
        verify(convergencePort, never()).converge(any(), any(), any());
        verify(activeStateCoordinator, never()).observe(any());
    }

    // ==================== THEMIS AUTHORIZATION ====================

    @Test
    @DisplayName("Themis policy denial throws canonical SecurityException (AccessDenied != ResourceAbsent)")
    void read_themisDenied_throwsSecurityException() {
        when(themisAuthorizer.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.defaultDeny("corr-123", "Policy violation: role not allowed"));

        assertThatThrownBy(() -> governedReader.read(managedKey, sampleContext))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Access denied by Themis policy for READ")
                .hasMessageContaining("Policy violation: role not allowed");

        verify(persistencePort, never()).read(any());
        verify(convergencePort, never()).converge(any(), any(), any());
        verify(activeStateCoordinator, never()).observe(any());
    }

    @Test
    @DisplayName("Themis evaluation exception throws canonical SecurityException fail-closed")
    void read_themisException_throwsSecurityException() {
        when(themisAuthorizer.authorize(any(ThemisAuthorizationRequest.class)))
                .thenThrow(new RuntimeException("Themis PDP communication failure"));

        assertThatThrownBy(() -> governedReader.read(managedKey, sampleContext))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Themis authorization error for READ")
                .hasMessageContaining("Themis PDP communication failure");

        verify(persistencePort, never()).read(any());
        verify(convergencePort, never()).converge(any(), any(), any());
        verify(activeStateCoordinator, never()).observe(any());
    }

    // ==================== AUTHORITATIVE RETRIEVAL & ABSENCE ====================

    @Test
    @DisplayName("Authoritative Absent outcome maps strictly to Optional.empty() without convergence or observation")
    void read_absentResource_returnsEmptyOptional() {
        when(themisAuthorizer.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.allow("allow-all", "corr-123"));

        when(persistencePort.read(managedKey))
                .thenReturn(new AuthoritativePersistenceResult.Absent<>("Resource not found on authoritative server"));

        Optional<GovernedRead<Practitioner>> result = governedReader.read(managedKey, sampleContext);

        assertThat(result).isEmpty();

        // Strict invariant: Zero convergence and zero token observation on absent resource
        verify(convergencePort, never()).converge(any(), any(), any());
        verify(activeStateCoordinator, never()).observe(any());
    }

    @Test
    @DisplayName("Authoritative NotCommitted fails closed with ActiveCoordinationUnavailableException")
    void read_notCommitted_throwsActiveCoordinationUnavailableException() {
        when(themisAuthorizer.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.allow("allow-all", "corr-123"));

        when(persistencePort.read(managedKey))
                .thenReturn(new AuthoritativePersistenceResult.NotCommitted<>("Authorization denied by Mnemosyne (HTTP 401)"));

        assertThatThrownBy(() -> governedReader.read(managedKey, sampleContext))
                .isInstanceOf(ActiveCoordinationUnavailableException.class)
                .hasMessageContaining("Authoritative read not committed: Authorization denied by Mnemosyne (HTTP 401)");

        verify(convergencePort, never()).converge(any(), any(), any());
        verify(activeStateCoordinator, never()).observe(any());
    }

    @Test
    @DisplayName("Authoritative OutcomeUnknown fails closed with ActiveCoordinationUnavailableException")
    void read_outcomeUnknown_throwsActiveCoordinationUnavailableException() {
        when(themisAuthorizer.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.allow("allow-all", "corr-123"));

        when(persistencePort.read(managedKey))
                .thenReturn(new AuthoritativePersistenceResult.OutcomeUnknown<>("Connection reset by peer"));

        assertThatThrownBy(() -> governedReader.read(managedKey, sampleContext))
                .isInstanceOf(ActiveCoordinationUnavailableException.class)
                .hasMessageContaining("Authoritative read outcome unknown: Connection reset by peer");

        verify(convergencePort, never()).converge(any(), any(), any());
        verify(activeStateCoordinator, never()).observe(any());
    }

    @Test
    @DisplayName("Unexpected authoritative conflict fails closed with ActiveCoordinationUnavailableException")
    void read_unexpectedConflict_throwsActiveCoordinationUnavailableException() {
        when(themisAuthorizer.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.allow("allow-all", "corr-123"));

        AuthoritativePreconditionConflict conflict = AuthoritativePreconditionConflict.resourceAlreadyExists(
                managedKey, AuthoritativeVersion.of(1L));
        when(persistencePort.read(managedKey))
                .thenReturn(new AuthoritativePersistenceResult.Conflict<>(conflict));

        assertThatThrownBy(() -> governedReader.read(managedKey, sampleContext))
                .isInstanceOf(ActiveCoordinationUnavailableException.class)
                .hasMessageContaining("Authoritative read encountered unexpected conflict");

        verify(convergencePort, never()).converge(any(), any(), any());
        verify(activeStateCoordinator, never()).observe(any());
    }

    @Test
    @DisplayName("Persistence transport exception throws ActiveCoordinationUnavailableException fail-closed")
    void read_persistenceThrowsException_throwsActiveCoordinationUnavailableException() {
        when(themisAuthorizer.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.allow("allow-all", "corr-123"));

        when(persistencePort.read(managedKey))
                .thenThrow(new RuntimeException("Socket timeout during READ"));

        assertThatThrownBy(() -> governedReader.read(managedKey, sampleContext))
                .isInstanceOf(ActiveCoordinationUnavailableException.class)
                .hasMessageContaining("Authoritative persistence error during READ: Socket timeout during READ");

        verify(convergencePort, never()).converge(any(), any(), any());
        verify(activeStateCoordinator, never()).observe(any());
    }

    // ==================== CONVERGENCE & TOKEN INVARIANTS ====================

    @Test
    @DisplayName("Authorized existing resource executes convergence then observes token and returns GovernedRead")
    void read_validResource_returnsGovernedReadWithPostConvergenceToken() {
        Practitioner practitioner = new Practitioner();
        practitioner.setId(managedKey.id());
        practitioner.addName(new HumanName().setFamily("Smith").addGiven("Alice"));
        AuthoritativeVersion authVersion = AuthoritativeVersion.of(5L);
        ActiveStateToken observedToken = ActiveStateTokenBridge.create(42L);

        // 1. Themis allows
        when(themisAuthorizer.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.allow("allow-all", "corr-123"));

        // 2. Authoritative READ returns Committed
        when(persistencePort.read(managedKey))
                .thenReturn(new AuthoritativePersistenceResult.Committed<>(practitioner, authVersion));

        // 3. Mneme convergence succeeds
        when(convergencePort.converge(eq(managedKey), eq(practitioner), eq(authVersion)))
                .thenReturn(ConvergenceStatus.CONVERGED);

        // 4. Token observed strictly after convergence
        when(activeStateCoordinator.observe(managedKey))
                .thenReturn(observedToken);

        Optional<GovernedRead<Practitioner>> result = governedReader.read(managedKey, sampleContext);

        assertThat(result).isPresent();
        GovernedRead<Practitioner> governedRead = result.get();
        assertThat(governedRead.key()).isEqualTo(managedKey);
        assertThat(governedRead.resource()).isSameAs(practitioner);
        assertThat(governedRead.authoritativeVersion()).isEqualTo(authVersion);
        assertThat(governedRead.activeToken()).isEqualTo(observedToken);

        // Verify strict execution ordering: Themis -> Persistence -> Convergence -> Observe Token
        InOrder inOrder = inOrder(themisAuthorizer, persistencePort, convergencePort, activeStateCoordinator);
        inOrder.verify(themisAuthorizer).authorize(any());
        inOrder.verify(persistencePort).read(managedKey);
        inOrder.verify(convergencePort).converge(managedKey, practitioner, authVersion);
        inOrder.verify(activeStateCoordinator).observe(managedKey);

        // Verify Themis was invoked with READ action
        ArgumentCaptor<ThemisAuthorizationRequest> captor = ArgumentCaptor.forClass(ThemisAuthorizationRequest.class);
        verify(themisAuthorizer).authorize(captor.capture());
        assertThat(captor.getValue().action()).isEqualTo(ThemisAction.READ);
    }

    @Test
    @DisplayName("Degraded convergence throws ActiveCoordinationUnavailableException fail-closed (no fabricated token)")
    void read_convergenceDegraded_throwsActiveCoordinationUnavailableException() {
        Practitioner practitioner = new Practitioner();
        practitioner.setId(managedKey.id());
        AuthoritativeVersion authVersion = AuthoritativeVersion.of(1L);

        when(themisAuthorizer.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.allow("allow-all", "corr-123"));

        when(persistencePort.read(managedKey))
                .thenReturn(new AuthoritativePersistenceResult.Committed<>(practitioner, authVersion));

        when(convergencePort.converge(eq(managedKey), eq(practitioner), eq(authVersion)))
                .thenReturn(ConvergenceStatus.DEGRADED);

        assertThatThrownBy(() -> governedReader.read(managedKey, sampleContext))
                .isInstanceOf(ActiveCoordinationUnavailableException.class)
                .hasMessageContaining("Active-state cache convergence degraded for " + managedKey);

        // Strict invariant: Token must NEVER be observed or emitted on degraded convergence
        verify(activeStateCoordinator, never()).observe(any());
    }

    @Test
    @DisplayName("Convergence exception throws ActiveCoordinationUnavailableException fail-closed")
    void read_convergenceThrowsException_throwsActiveCoordinationUnavailableException() {
        Practitioner practitioner = new Practitioner();
        practitioner.setId(managedKey.id());
        AuthoritativeVersion authVersion = AuthoritativeVersion.of(1L);

        when(themisAuthorizer.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.allow("allow-all", "corr-123"));

        when(persistencePort.read(managedKey))
                .thenReturn(new AuthoritativePersistenceResult.Committed<>(practitioner, authVersion));

        when(convergencePort.converge(eq(managedKey), eq(practitioner), eq(authVersion)))
                .thenThrow(new RuntimeException("Infinispan cluster timeout"));

        assertThatThrownBy(() -> governedReader.read(managedKey, sampleContext))
                .isInstanceOf(ActiveCoordinationUnavailableException.class)
                .hasMessageContaining("Active-state cache convergence degraded for " + managedKey);

        verify(activeStateCoordinator, never()).observe(any());
    }

    @Test
    @DisplayName("Null observed token throws ActiveCoordinationUnavailableException fail-closed")
    void read_nullTokenObserved_throwsActiveCoordinationUnavailableException() {
        Practitioner practitioner = new Practitioner();
        practitioner.setId(managedKey.id());
        AuthoritativeVersion authVersion = AuthoritativeVersion.of(1L);

        when(themisAuthorizer.authorize(any(ThemisAuthorizationRequest.class)))
                .thenReturn(ThemisAuthorizationDecision.allow("allow-all", "corr-123"));

        when(persistencePort.read(managedKey))
                .thenReturn(new AuthoritativePersistenceResult.Committed<>(practitioner, authVersion));

        when(convergencePort.converge(eq(managedKey), eq(practitioner), eq(authVersion)))
                .thenReturn(ConvergenceStatus.CONVERGED);

        when(activeStateCoordinator.observe(managedKey))
                .thenReturn(null);

        assertThatThrownBy(() -> governedReader.read(managedKey, sampleContext))
                .isInstanceOf(ActiveCoordinationUnavailableException.class)
                .hasMessageContaining("Active-state token observation returned null for " + managedKey);
    }
}
