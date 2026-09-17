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

package net.fhirfactory.harmonia.agora.core.reconciliation;

import net.fhirfactory.harmonia.agora.api.model.AgoraMembershipAction;
import net.fhirfactory.harmonia.agora.api.model.AgoraMembershipRequest;
import net.fhirfactory.harmonia.agora.api.model.AgoraReconciliationResult;
import net.fhirfactory.harmonia.agora.core.AgoraCoreTestApplication;
import net.fhirfactory.harmonia.agora.core.persistence.AgoraMappingEntity;
import net.fhirfactory.harmonia.agora.core.persistence.AgoraMappingRepository;
import net.fhirfactory.harmonia.agora.core.security.AgoraCollaborationPolicy;
import net.fhirfactory.harmonia.agora.core.security.AgoraSecurityUtils;
import net.fhirfactory.harmonia.agora.matrix.client.MatrixClientAdapter;
import net.fhirfactory.harmonia.agora.matrix.client.MatrixMemberDto;
import net.fhirfactory.harmonia.themis.api.ThemisAuthorizer;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import net.fhirfactory.harmonia.themis.core.evaluator.DeterministicPolicyEvaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = AgoraCoreTestApplication.class)
@Transactional
@DisplayName("AgoraMembershipReconciliationService Unit & Mock Tests")
@Timeout(10)
class AgoraMembershipReconciliationServiceTest {

    @Autowired
    private AgoraMappingRepository mappingRepository;

    private MatrixClientAdapter matrixClientAdapter;
    private DeterministicPolicyEvaluator authorizer;
    private AgoraMembershipReconciliationService productionReconciliationService;
    private ThemisSecurityContext adminContext;
    private ThemisSecurityContext unauthorizedContext;

    @BeforeEach
    void setUp() {
        mappingRepository.deleteAll();
        matrixClientAdapter = mock(MatrixClientAdapter.class);
        authorizer = DeterministicPolicyEvaluator.withDefaultPolicies();
        authorizer.registerPolicy(new AgoraCollaborationPolicy());

        // Production-wired service without test-only UserAuthorityResolver
        productionReconciliationService = new AgoraMembershipReconciliationService(
                matrixClientAdapter,
                mappingRepository,
                authorizer,
                "@_harmonia_bot:synapse"
        );

        adminContext = AgoraSecurityUtils.contextWithAuthorities(
                ThemisPrincipal.human("reconciliation-coordinator"),
                "corr-reconcile-admin",
                AgoraCollaborationPolicy.AUTH_AGORA_ADMIN
        );

        unauthorizedContext = ThemisSecurityContext.fromPrincipal(
                ThemisPrincipal.human("unauthorized-user"),
                "corr-exec-denied"
        );
    }

    @Test
    @DisplayName("Should invite missing desired member in production-wired service without authority resolver")
    void testProductionReconciliationInvitesMissingDesiredMember() {
        String roomId = "!room-tasks:synapse";
        String existingUser = "@dr-alice:synapse";
        String missingUser = "@dr-bob:synapse";

        // Live members in Matrix room
        when(matrixClientAdapter.getRoomMembers(roomId)).thenReturn(List.of(
                MatrixMemberDto.builder().userId(existingUser).membership("join").build()
        ));

        AgoraReconciliationResult result = productionReconciliationService.reconcileRoomMembership(
                roomId,
                Set.of(existingUser, missingUser),
                adminContext
        );

        assertThat(result.getRoomId()).isEqualTo(roomId);
        assertThat(result.getInvitedUsers()).containsExactly(missingUser);
        assertThat(result.getRetainedUsers()).containsExactly(existingUser);
        assertThat(result.getKickedUsers()).isEmpty();
        assertThat(result.getDeniedUsers()).isEmpty();
        assertThat(result.hasDrift()).isTrue();

        verify(matrixClientAdapter, times(1)).inviteUser(eq(roomId), eq(missingUser), anyString());
        verify(matrixClientAdapter, never()).kickUser(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Should kick unauthorized live intruder (membership drift) in production-wired service without resolver")
    void testProductionReconciliationKicksUnauthorizedLiveMember() {
        String roomId = "!room-tasks:synapse";
        String authorizedUser = "@dr-alice:synapse";
        String intruderUser = "@intruder:synapse";

        when(matrixClientAdapter.getRoomMembers(roomId)).thenReturn(List.of(
                MatrixMemberDto.builder().userId(authorizedUser).membership("join").build(),
                MatrixMemberDto.builder().userId(intruderUser).membership("join").build()
        ));

        AgoraReconciliationResult result = productionReconciliationService.reconcileRoomMembership(
                roomId,
                Set.of(authorizedUser),
                adminContext
        );

        assertThat(result.getKickedUsers()).containsExactly(intruderUser);
        assertThat(result.getRetainedUsers()).containsExactly(authorizedUser);
        assertThat(result.getDeniedUsers()).containsExactly(intruderUser);
        assertThat(result.hasDrift()).isTrue();

        verify(matrixClientAdapter, times(1)).kickUser(eq(roomId), eq(intruderUser), anyString());
    }

    @Test
    @DisplayName("Should preserve bot user and not kick service accounts during drift check in production-wired service")
    void testProductionReconciliationPreservesBotUser() {
        String roomId = "!room-chat:synapse";
        String botUser = "@_harmonia_bot:synapse";
        String userAlice = "@dr-alice:synapse";

        when(matrixClientAdapter.getRoomMembers(roomId)).thenReturn(List.of(
                MatrixMemberDto.builder().userId(botUser).membership("join").build(),
                MatrixMemberDto.builder().userId(userAlice).membership("join").build()
        ));

        AgoraReconciliationResult result = productionReconciliationService.reconcileRoomMembership(
                roomId,
                Set.of(userAlice),
                adminContext
        );

        assertThat(result.getRetainedUsers()).contains(botUser, userAlice);
        assertThat(result.getKickedUsers()).isEmpty();
        assertThat(result.hasDrift()).isFalse();

        verify(matrixClientAdapter, never()).kickUser(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Should deny reconciliation when caller lacks required admin authority under default-deny")
    void testReconciliationDeniedForUnauthorizedCaller() {
        String roomId = "!room-tasks:synapse";

        assertThatThrownBy(() -> productionReconciliationService.reconcileRoomMembership(
                roomId,
                Set.of("@dr-alice:synapse"),
                unauthorizedContext
        )).isInstanceOf(SecurityException.class)
          .hasMessageContaining("Themis security authorization denied membership reconciliation");

        assertThatThrownBy(() -> productionReconciliationService.reconcileRoomMembership(
                roomId,
                Set.of("@dr-alice:synapse"),
                null
        )).isInstanceOf(SecurityException.class)
          .hasMessageContaining("Themis security authorization denied membership reconciliation");

        assertThatThrownBy(() -> productionReconciliationService.reconcileHierarchyMembership(
                "PATIENT",
                "pat-123",
                Set.of("@dr-alice:synapse"),
                unauthorizedContext
        )).isInstanceOf(SecurityException.class)
          .hasMessageContaining("Themis security authorization denied hierarchy membership reconciliation");
    }

    @Test
    @DisplayName("Should kick live desired member when optional authorityResolver explicitly denies under Themis")
    void testReconciliationWithExplicitResolverKicksDeniedDesiredMember() {
        String roomId = "!room-tasks:synapse";
        String revokedUser = "@dr-revoked:synapse";

        AgoraMembershipReconciliationService.UserAuthorityResolver resolver = userId -> {
            if ("@dr-alice:synapse".equals(userId)) {
                return Set.of(ThemisAuthority.of(AgoraCollaborationPolicy.AUTH_AGORA_MEMBER));
            }
            return Collections.emptySet();
        };

        AgoraMembershipReconciliationService serviceWithResolver = new AgoraMembershipReconciliationService(
                matrixClientAdapter,
                mappingRepository,
                authorizer,
                "@_harmonia_bot:synapse",
                resolver
        );

        when(matrixClientAdapter.getRoomMembers(roomId)).thenReturn(List.of(
                MatrixMemberDto.builder().userId(revokedUser).membership("join").build()
        ));

        AgoraReconciliationResult result = serviceWithResolver.reconcileRoomMembership(
                roomId,
                Set.of(revokedUser),
                adminContext
        );

        assertThat(result.getKickedUsers()).containsExactly(revokedUser);
        assertThat(result.getDeniedUsers()).containsExactly(revokedUser);
        assertThat(result.hasDrift()).isTrue();

        verify(matrixClientAdapter).kickUser(eq(roomId), eq(revokedUser), anyString());
    }

    @Test
    @DisplayName("Should execute membership request under Themis authorization and deny unauthorized")
    void testExecuteMembershipRequest() {
        String roomId = "!room-exec:synapse";
        String targetUser = "@nurse-charlie:synapse";

        // Authorized request with agora.member authority
        ThemisSecurityContext authorizedContext = AgoraSecurityUtils.contextWithAuthorities(
                ThemisPrincipal.human("dr-alice"),
                "corr-exec-1",
                AgoraCollaborationPolicy.AUTH_AGORA_MEMBER
        );

        AgoraMembershipRequest inviteRequest = AgoraMembershipRequest.builder()
                .roomId(roomId)
                .userId(targetUser)
                .action(AgoraMembershipAction.INVITE)
                .reason("Consultation")
                .securityContext(authorizedContext)
                .build();

        productionReconciliationService.executeMembershipRequest(inviteRequest);
        verify(matrixClientAdapter).manageMembership(roomId, targetUser, AgoraMembershipAction.INVITE, "Consultation");

        // Unauthorized request lacking required authorities under the same production authorizer
        AgoraMembershipRequest unauthorizedRequest = AgoraMembershipRequest.builder()
                .roomId(roomId)
                .userId(targetUser)
                .action(AgoraMembershipAction.INVITE)
                .reason("Consultation")
                .securityContext(unauthorizedContext)
                .build();

        assertThatThrownBy(() -> productionReconciliationService.executeMembershipRequest(unauthorizedRequest))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Themis security authorization denied membership action");
    }

    @Test
    @DisplayName("Should reconcile membership across entire hierarchy in production-wired service without resolver")
    void testReconcileHierarchyMembership() {
        String patientId = "pat-hier";
        mappingRepository.save(new AgoraMappingEntity("PATIENT", patientId, "SPACE", "!space-1:synapse", "ACTIVE"));
        mappingRepository.save(new AgoraMappingEntity("PATIENT", patientId, "ROOM_TASKS", "!room-t1:synapse", "ACTIVE"));

        when(matrixClientAdapter.getRoomMembers(anyString())).thenReturn(List.of(
                MatrixMemberDto.builder().userId("@doctor:synapse").membership("join").build()
        ));

        List<AgoraReconciliationResult> results = productionReconciliationService.reconcileHierarchyMembership(
                "PATIENT", patientId, Set.of("@doctor:synapse", "@nurse:synapse"), adminContext
        );

        assertThat(results).hasSize(2);
        assertThat(results).allMatch(r -> r.getInvitedUsers().contains("@nurse:synapse"));
        verify(matrixClientAdapter, times(2)).inviteUser(anyString(), eq("@nurse:synapse"), anyString());
    }
}
