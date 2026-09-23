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

package net.fhirfactory.harmonia.agora.core.identity;

import net.fhirfactory.harmonia.agora.core.AgoraCoreTestApplication;
import net.fhirfactory.harmonia.agora.core.persistence.AgoraMappingEntity;
import net.fhirfactory.harmonia.agora.core.persistence.AgoraMappingRepository;
import net.fhirfactory.harmonia.agora.core.security.AgoraCollaborationPolicy;
import net.fhirfactory.harmonia.agora.core.security.AgoraSecurityUtils;
import net.fhirfactory.harmonia.agora.matrix.admin.MatrixUserDto;
import net.fhirfactory.harmonia.agora.matrix.admin.SynapseAdminException;
import net.fhirfactory.harmonia.agora.matrix.admin.SynapseAdministrationGateway;
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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = AgoraCoreTestApplication.class)
@Transactional
@DisplayName("AgoraIdentityService Unit & Mock Tests")
@Timeout(10)
class AgoraIdentityServiceTest {

    @Autowired
    private AgoraMappingRepository mappingRepository;

    private SynapseAdministrationGateway adminGateway;
    private DeterministicPolicyEvaluator authorizer;
    private AgoraIdentityService identityService;
    private ThemisSecurityContext adminContext;

    @BeforeEach
    void setUp() {
        mappingRepository.deleteAll();
        adminGateway = mock(SynapseAdministrationGateway.class);
        authorizer = DeterministicPolicyEvaluator.withDefaultPolicies();
        authorizer.registerPolicy(new AgoraCollaborationPolicy());

        identityService = new AgoraIdentityService(
                adminGateway,
                mappingRepository,
                authorizer,
                "synapse",
                3
        );

        adminContext = AgoraSecurityUtils.contextWithAuthorities(
                ThemisPrincipal.human("admin-user"),
                "corr-admin-1",
                AgoraCollaborationPolicy.AUTH_AGORA_ADMIN
        );
    }

    @Test
    @DisplayName("Should derive deterministic localparts and user IDs")
    void testDeriveDeterministicIdentifiers() {
        String uuidStr = "550e8400-e29b-41d4-a716-446655440000";
        String localpart1 = identityService.deriveLocalpart(uuidStr);
        String localpart2 = identityService.deriveLocalpart("Practitioner/" + uuidStr);

        assertThat(localpart1).isEqualTo("_harmonia_p_" + uuidStr);
        assertThat(localpart2).isEqualTo("_harmonia_p_" + uuidStr);

        String userId = identityService.deriveMatrixUserId(uuidStr);
        assertThat(userId).isEqualTo("@_harmonia_p_" + uuidStr + ":synapse");

        String customUserId = identityService.deriveMatrixUserId(uuidStr, "custom.org");
        assertThat(customUserId).isEqualTo("@_harmonia_p_" + uuidStr + ":custom.org");

        // Non-UUID string derives deterministic UUID
        String arbitraryId = "dr-alice-smith";
        String derived1 = identityService.deriveLocalpart(arbitraryId);
        String derived2 = identityService.deriveLocalpart(arbitraryId);
        assertThat(derived1).isEqualTo(derived2);
        assertThat(derived1).startsWith("_harmonia_p_");
    }

    @Test
    @DisplayName("Should provision local user and persist durable Mnemosyne mapping")
    void testProvisionUserSuccess() {
        String principalId = "practitioner-777";
        String expectedUserId = identityService.deriveMatrixUserId(principalId);

        when(adminGateway.createOrUpdateUser(any(MatrixUserDto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MatrixUserDto provisioned = identityService.provisionUser(
                "PRACTITIONER",
                principalId,
                "Dr. Alice",
                adminContext
        );

        assertThat(provisioned).isNotNull();
        assertThat(provisioned.getUserId()).isEqualTo(expectedUserId);
        assertThat(provisioned.getDisplayName()).isEqualTo("Dr. Alice");

        verify(adminGateway, times(1)).createOrUpdateUser(any(MatrixUserDto.class));

        Optional<AgoraMappingEntity> mappingOpt = mappingRepository
                .findByHarmoniaResourceTypeAndHarmoniaResourceIdAndMatrixEntityType(
                        "PRACTITIONER", principalId, "USER"
                );

        assertThat(mappingOpt).isPresent();
        assertThat(mappingOpt.get().getMatrixEntityId()).isEqualTo(expectedUserId);
        assertThat(mappingOpt.get().getStatus()).isEqualTo("ACTIVE");

        // Querying mapped user
        assertThat(identityService.getMappedUserId(principalId)).contains(expectedUserId);
        assertThat(identityService.getOrCreateUser(principalId, "Dr. Alice", adminContext)).isEqualTo(expectedUserId);
        // Verify gateway was not called again since already mapped
        verify(adminGateway, times(1)).createOrUpdateUser(any(MatrixUserDto.class));
    }

    @Test
    @DisplayName("Should retry on transient SynapseAdminException and succeed")
    void testProvisionUserWithRetrySuccess() {
        String principalId = "practitioner-retry";
        String expectedUserId = identityService.deriveMatrixUserId(principalId);

        AtomicInteger callCount = new AtomicInteger(0);
        when(adminGateway.createOrUpdateUser(any(MatrixUserDto.class)))
                .thenAnswer(invocation -> {
                    if (callCount.incrementAndGet() == 1) {
                        throw new SynapseAdminException(503, "M_UNAVAILABLE", "Transient error", "Gateway busy");
                    }
                    return invocation.getArgument(0);
                });

        MatrixUserDto result = identityService.provisionUser(
                "PRACTITIONER",
                principalId,
                "Dr. Retry",
                adminContext
        );

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(expectedUserId);
        assertThat(callCount.get()).isEqualTo(2);

        verify(adminGateway, times(2)).createOrUpdateUser(any(MatrixUserDto.class));
        assertThat(mappingRepository.existsByMatrixEntityId(expectedUserId)).isTrue();
    }

    @Test
    @DisplayName("Should fail when max retries exceeded and not persist invalid mapping")
    void testProvisionUserMaxRetriesExceeded() {
        String principalId = "practitioner-fail";

        when(adminGateway.createOrUpdateUser(any(MatrixUserDto.class)))
                .thenThrow(new SynapseAdminException(500, "M_UNKNOWN", "Server Error", "Fatal"));

        assertThatThrownBy(() -> identityService.provisionUser(
                "PRACTITIONER",
                principalId,
                "Dr. Fail",
                adminContext
        ))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Failed to provision Synapse user after 3 attempts");

        verify(adminGateway, times(3)).createOrUpdateUser(any(MatrixUserDto.class));

        assertThat(identityService.getMappedUserId(principalId)).isEmpty();
    }

    @Test
    @DisplayName("Should enforce Themis default-deny when unauthorized under production evaluator")
    void testProvisionUserThemisDefaultDeny() {
        // Evaluator configured exactly as in production (with AgoraCollaborationPolicy)
        ThemisSecurityContext unauthorizedContext = ThemisSecurityContext.fromPrincipal(
                ThemisPrincipal.human("unauthorized-user"),
                "corr-123"
        );

        // Unauthorized principal lacking agora.admin authority must be denied
        assertThatThrownBy(() -> identityService.provisionUser("PRACTITIONER", "prv-denied", "Denied", unauthorizedContext))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Themis security authorization denied user provisioning");

        // Null context (anonymous caller) must also be denied
        assertThatThrownBy(() -> identityService.provisionUser("PRACTITIONER", "prv-denied-anon", "Denied", null))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Themis security authorization denied user provisioning");

        verifyNoInteractions(adminGateway);
        assertThat(identityService.getMappedUserId("prv-denied")).isEmpty();

        // But with authorized context carrying agora.admin, provisioning succeeds
        when(adminGateway.createOrUpdateUser(any(MatrixUserDto.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        MatrixUserDto provisioned = identityService.provisionUser("PRACTITIONER", "prv-allowed", "Allowed", adminContext);
        assertThat(provisioned).isNotNull();
        assertThat(identityService.getMappedUserId("prv-allowed")).isPresent();
    }

    @Test
    @DisplayName("Should deactivate user and update mapping status")
    void testDeactivateUser() {
        String principalId = "practitioner-deact";
        String expectedUserId = identityService.deriveMatrixUserId(principalId);

        when(adminGateway.createOrUpdateUser(any(MatrixUserDto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        identityService.provisionUser("PRACTITIONER", principalId, "Dr. Deactivate", adminContext);
        assertThat(identityService.getMappedUserId(principalId)).isPresent();

        identityService.deactivateUser(principalId, true, adminContext);

        verify(adminGateway).deactivateUser(expectedUserId, true);

        Optional<AgoraMappingEntity> mappingOpt = mappingRepository
                .findByHarmoniaResourceTypeAndHarmoniaResourceIdAndMatrixEntityType("PRACTITIONER", principalId, "USER");
        assertThat(mappingOpt).isPresent();
        assertThat(mappingOpt.get().getStatus()).isEqualTo("DEACTIVATED");
    }

    @Test
    @DisplayName("Provisioning retry logging suppresses PHI and secret markers in SynapseAdminException")
    void testProvisionUserRetryLoggingSuppressesPhiAndSecrets() {
        String principalId = "practitioner-phi-retry";
        String expectedUserId = identityService.deriveMatrixUserId(principalId);

        Logger logger = (Logger) LoggerFactory.getLogger(AgoraIdentityService.class);
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);

        try {
            when(adminGateway.createOrUpdateUser(any(MatrixUserDto.class)))
                    .thenThrow(new SynapseAdminException(503, "M_UNAVAILABLE", "Downstream error",
                            "Echo: PATIENT-PHI-MARKER-92831 TOKEN-SECRET-MARKER-81742"));

            assertThatThrownBy(() -> identityService.provisionUser("PRACTITIONER", principalId, "Dr. Secret", adminContext))
                    .isInstanceOf(IllegalStateException.class);

            List<ILoggingEvent> warnEvents = appender.list.stream()
                    .filter(e -> e.getLevel() == Level.WARN)
                    .toList();

            assertThat(warnEvents).isNotEmpty();
            for (ILoggingEvent event : warnEvents) {
                assertThat(event.getFormattedMessage())
                        .contains("userId=" + expectedUserId)
                        .contains("status=503")
                        .contains("errcode=M_UNAVAILABLE")
                        .doesNotContain("PATIENT-PHI-MARKER-92831")
                        .doesNotContain("TOKEN-SECRET-MARKER-81742");
            }
        } finally {
            logger.detachAppender(appender);
            appender.stop();
        }
    }

    @Test
    @DisplayName("Deactivation error logging suppresses PHI and secret markers in SynapseAdminException")
    void testDeactivateUserLoggingSuppressesPhiAndSecrets() {
        String principalId = "practitioner-phi-deact";
        String expectedUserId = identityService.deriveMatrixUserId(principalId);

        Logger logger = (Logger) LoggerFactory.getLogger(AgoraIdentityService.class);
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);

        try {
            doThrow(new SynapseAdminException(500, "M_UNKNOWN", "Downstream error",
                    "Echo: PATIENT-PHI-MARKER-92831 TOKEN-SECRET-MARKER-81742"))
                    .when(adminGateway).deactivateUser(expectedUserId, false);

            assertThatThrownBy(() -> identityService.deactivateUser(principalId, false, adminContext))
                    .isInstanceOf(SynapseAdminException.class);

            List<ILoggingEvent> warnEvents = appender.list.stream()
                    .filter(e -> e.getLevel() == Level.WARN)
                    .toList();

            assertThat(warnEvents).isNotEmpty();
            for (ILoggingEvent event : warnEvents) {
                assertThat(event.getFormattedMessage())
                        .contains("userId=" + expectedUserId)
                        .contains("status=500")
                        .contains("errcode=M_UNKNOWN")
                        .doesNotContain("PATIENT-PHI-MARKER-92831")
                        .doesNotContain("TOKEN-SECRET-MARKER-81742");
            }
        } finally {
            logger.detachAppender(appender);
            appender.stop();
        }
    }
}
