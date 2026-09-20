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

package net.fhirfactory.harmonia.befe;

import net.fhirfactory.harmonia.befe.security.DefaultThemisAuthorizer;
import net.fhirfactory.harmonia.befe.security.ThemisOperationsAuthorizer;
import net.fhirfactory.harmonia.themis.api.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThemisOperationsAuthorizerTest {

    private DefaultThemisAuthorizer defaultAuthorizer;
    private ThemisOperationsAuthorizer operationsAuthorizer;

    @BeforeEach
    void setUp() {
        defaultAuthorizer = new DefaultThemisAuthorizer();
        operationsAuthorizer = new ThemisOperationsAuthorizer(defaultAuthorizer);
    }

    @Test
    @DisplayName("1. Default-Deny: unauthenticated request is denied")
    void testUnauthenticatedRequestDenied() {
        ThemisAuthorizationDecision decision = operationsAuthorizer.authorizeRequest(
                Map.of(), "/api/operations/summary", "GET"
        );

        assertThat(decision.isDenied()).isTrue();
        assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.PRINCIPAL_MISSING);
    }

    @Test
    @DisplayName("2. Default-Deny: anonymous principal is rejected")
    void testAnonymousPrincipalDenied() {
        ThemisPrincipal principal = ThemisPrincipal.human("anonymous");
        ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                .principal(principal)
                .action(ThemisAction.READ)
                .target(ThemisResource.of("OperationsResource", "/api/operations/subsystems"))
                .build();

        ThemisAuthorizationDecision decision = defaultAuthorizer.authorize(request);
        assertThat(decision.isDenied()).isTrue();
    }

    @Test
    @DisplayName("3. Unauthorized role: principal with unprivileged role is denied")
    void testUnauthorizedRoleDenied() {
        ThemisPrincipal principal = new ThemisPrincipal("test-user", PrincipalType.HUMAN, "test", Map.of("role", "CLINICAL_VIEWER"));
        ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                .principal(principal)
                .action(ThemisAction.READ)
                .target(ThemisResource.of("OperationsResource", "/api/operations/queues"))
                .build();

        ThemisAuthorizationDecision decision = defaultAuthorizer.authorize(request);
        assertThat(decision.isDenied()).isTrue();
        assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.ACTION_NOT_PERMITTED);
    }

    @Test
    @DisplayName("4. Authorized roles: SYS_ADM and OPS_VIEWER are granted access")
    void testAuthorizedRolesAllowed() {
        ThemisPrincipal admin = new ThemisPrincipal("admin-user", PrincipalType.HUMAN, "test", Map.of("role", "SYS_ADM"));
        ThemisAuthorizationRequest adminReq = ThemisAuthorizationRequest.builder()
                .principal(admin)
                .action(ThemisAction.READ)
                .target(ThemisResource.of("OperationsResource", "/api/operations/summary"))
                .build();

        ThemisAuthorizationDecision adminDec = defaultAuthorizer.authorize(adminReq);
        assertThat(adminDec.isAllowed()).isTrue();

        ThemisPrincipal viewer = new ThemisPrincipal("ops-user", PrincipalType.HUMAN, "test", Map.of("role", "OPS_VIEWER"));
        ThemisAuthorizationRequest viewerReq = ThemisAuthorizationRequest.builder()
                .principal(viewer)
                .action(ThemisAction.READ)
                .target(ThemisResource.of("OperationsResource", "/api/operations/summary"))
                .build();

        ThemisAuthorizationDecision viewerDec = defaultAuthorizer.authorize(viewerReq);
        assertThat(viewerDec.isAllowed()).isTrue();
    }

    @Test
    @DisplayName("5. Authority matching: system.admin and operations.read grant access")
    void testAuthorityMatchingAllowed() {
        ThemisPrincipal service = ThemisPrincipal.service("service:pylai");
        ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                .principal(service)
                .authorities(Set.of(ThemisAuthority.of(DefaultThemisAuthorizer.AUTH_OPERATIONS_READ)))
                .action(ThemisAction.READ)
                .target(ThemisResource.of("OperationsResource", "/api/operations/workflows"))
                .build();

        ThemisAuthorizationDecision decision = defaultAuthorizer.authorize(request);
        assertThat(decision.isAllowed()).isTrue();
    }

    @Test
    @DisplayName("6. Header parsing and assertAuthorized enforcement")
    void testHeaderParsingAndAssertion() {
        // Bearer token with admin role
        Map<String, String> adminHeaders = Map.of(
                "authorization", "Bearer sys_adm_token",
                "x-correlation-id", "corr-test-123"
        );
        ThemisAuthorizationDecision dec = operationsAuthorizer.authorizeRequest(adminHeaders, "/api/operations/subsystems", "GET");
        assertThat(dec.isAllowed()).isTrue();
        assertThat(dec.correlationId()).isEqualTo("corr-test-123");

        // Should not throw
        operationsAuthorizer.assertAuthorized(adminHeaders, "/api/operations/subsystems", "GET");

        // Missing headers throws SecurityException
        assertThatThrownBy(() -> operationsAuthorizer.assertAuthorized(Map.of(), "/api/operations/subsystems", "GET"))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Access Denied");
    }

    @Test
    @DisplayName("7. Case-insensitive header parsing and mixed casing support")
    void testCaseInsensitiveHeaderParsing() {
        Map<String, String> mixedCaseHeaders = Map.of(
                "Authorization", "Bearer ops_admin_user",
                "X-Correlation-Id", "corr-mixed-456"
        );
        ThemisAuthorizationDecision decision = operationsAuthorizer.authorizeRequest(mixedCaseHeaders, "/api/operations/queues", "GET");
        assertThat(decision.isAllowed()).isTrue();
        assertThat(decision.correlationId()).isEqualTo("corr-mixed-456");
    }

    @Test
    @DisplayName("8. Default-Deny: unprivileged bearer token is denied")
    void testUnprivilegedBearerTokenDenied() {
        Map<String, String> unprivilegedHeaders = Map.of(
                "authorization", "Bearer guest_user",
                "x-correlation-id", "corr-unprivileged-789"
        );
        ThemisAuthorizationDecision decision = operationsAuthorizer.authorizeRequest(unprivilegedHeaders, "/api/operations/subsystems", "GET");
        assertThat(decision.isDenied()).isTrue();
        assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.ACTION_NOT_PERMITTED);

        Map<String, String> clinicalBearerHeaders = Map.of(
                "authorization", "Bearer nurse:CLINICAL_VIEWER"
        );
        ThemisAuthorizationDecision clinicalDecision = operationsAuthorizer.authorizeRequest(clinicalBearerHeaders, "/api/operations/subsystems", "GET");
        assertThat(clinicalDecision.isDenied()).isTrue();
    }

    @Test
    @DisplayName("9. Default-Deny: X-Harmonia-User with CLINICAL_VIEWER role is denied")
    void testClinicalViewerUserDenied() {
        Map<String, String> clinicalUserHeaders = Map.of(
                "x-harmonia-user", "dr-smith",
                "x-harmonia-role", "CLINICAL_VIEWER"
        );
        ThemisAuthorizationDecision decision = operationsAuthorizer.authorizeRequest(clinicalUserHeaders, "/api/operations/alerts", "GET");
        assertThat(decision.isDenied()).isTrue();
        assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.ACTION_NOT_PERMITTED);
    }
}
