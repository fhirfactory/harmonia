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

package net.fhirfactory.harmonia.pylai.fhir.security;

import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Pylai FHIR Security Interceptor & Themis Gateway Tests")
class FhirSecurityInterceptorTest {

    private final FhirSecurityInterceptor interceptor = new FhirSecurityInterceptor();

    @Test
    @DisplayName("Allows read/search when PRV_RDR mnemonic role is container-authenticated")
    void testAuthorizedPrvRdrRole() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user:dr-smith");
        request.addUserRole("PRV_RDR");

        assertThatCode(() -> interceptor.authorize("Practitioner", "read", request))
                .doesNotThrowAnyException();
        assertThatCode(() -> interceptor.authorize("Practitioner", "search", request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Allows submit create/update when PRV_SUB mnemonic role is container-authenticated")
    void testAuthorizedPrvSubRole() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user:registry-submitter");
        request.addUserRole("PRV_SUB");

        assertThatCode(() -> interceptor.authorize("Practitioner", "create.request", request))
                .doesNotThrowAnyException();
        assertThatCode(() -> interceptor.authorize("Practitioner", "update.request", request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Denies write interactions when caller only has container role PRV_RDR")
    void testDeniedWriteForPrvRdr() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user:reader-only");
        request.addUserRole("PRV_RDR");

        assertThatThrownBy(() -> interceptor.authorize("Practitioner", "create.request", request))
                .isInstanceOf(ForbiddenOperationException.class)
                .hasMessageContaining("missing required permission [Practitioner.create.request]");
    }

    @Test
    @DisplayName("Allows interaction when PRV_ADM role is container-authenticated")
    void testAuthorizedAdminRole() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user:admin");
        request.addUserRole("PRV_ADM");

        assertThatCode(() -> interceptor.authorize("Endpoint", "update.request", request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Fails closed with AuthenticationException when request is unauthenticated")
    void testUnauthenticatedFailsClosed() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        assertThatThrownBy(() -> interceptor.authorize("Practitioner", "read", request))
                .isInstanceOf(AuthenticationException.class)
                .hasMessageContaining("Trusted caller identity not established");
    }

    @Test
    @DisplayName("Fails closed when principal is system:anonymous or blank")
    void testAnonymousPrincipalFailsClosed() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "system:anonymous");

        assertThatThrownBy(() -> interceptor.authorize("Practitioner", "read", request))
                .isInstanceOf(AuthenticationException.class)
                .hasMessageContaining("Trusted caller identity not established");

        MockHttpServletRequest blankRequest = new MockHttpServletRequest();
        blankRequest.setUserPrincipal(() -> "  ");

        assertThatThrownBy(() -> interceptor.authorize("Practitioner", "read", blankRequest))
                .isInstanceOf(AuthenticationException.class)
                .hasMessageContaining("Trusted caller identity not established");
    }

    @Test
    @DisplayName("Rejects spoofed identity/role headers when container principal is absent")
    void testHeaderSpoofingRejectedWithoutContainerPrincipal() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(FhirSecurityInterceptor.HEADER_PRINCIPAL_ID, "admin");
        request.addHeader(FhirSecurityInterceptor.HEADER_REQUESTER, "superuser");
        request.addHeader(FhirSecurityInterceptor.HEADER_USER_ROLES, "ROLE_ADMIN, SYS_ADM, *");
        request.addHeader(FhirSecurityInterceptor.HEADER_SECURITY_SCOPES, "system/*.*");

        assertThatThrownBy(() -> interceptor.authorize("Practitioner", "read", request))
                .isInstanceOf(AuthenticationException.class)
                .hasMessageContaining("Trusted caller identity not established");
    }

    @Test
    @DisplayName("Ignores injected role headers and evaluates strictly against container roles")
    void testRoleHeaderInjectionIgnored() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user:dr-smith");
        request.addUserRole("PRV_RDR");
        // Caller attempts to inject admin roles via HTTP headers
        request.addHeader(FhirSecurityInterceptor.HEADER_USER_ROLES, "PRV_ADM, SYS_ADM, ROLE_ADMIN, *");
        request.addHeader(FhirSecurityInterceptor.HEADER_SECURITY_SCOPES, "system/*.*");

        // Read is permitted because container has PRV_RDR
        assertThatCode(() -> interceptor.authorize("Practitioner", "read", request))
                .doesNotThrowAnyException();

        // Write is denied despite header injection because container only has PRV_RDR
        assertThatThrownBy(() -> interceptor.authorize("Practitioner", "create.request", request))
                .isInstanceOf(ForbiddenOperationException.class)
                .hasMessageContaining("missing required permission [Practitioner.create.request]");
    }

    @Test
    @DisplayName("Allows unauthenticated metadata and CapabilityStatement discovery")
    void testMetadataUnauthenticatedDiscoveryAllowed() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        assertThatCode(() -> interceptor.authorize("metadata", "read", request))
                .doesNotThrowAnyException();
        assertThatCode(() -> interceptor.authorize("CapabilityStatement", "read", request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Extracts principal identity and attributes from container principal")
    void testPrincipalExtraction() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "service:pylai-gateway");
        request.addHeader(FhirSecurityInterceptor.HEADER_SOURCE_DOMAIN, "gateway-cluster");

        ThemisPrincipal principal = interceptor.extractPrincipal(request);
        assertThat(principal).isNotNull();
        assertThat(principal.principalId()).isEqualTo("service:pylai-gateway");
        assertThat(principal.principalType()).isEqualTo(PrincipalType.SERVICE);
        assertThat(principal.sourceDomain()).isEqualTo("gateway-cluster");
    }

    @Test
    @DisplayName("Maps container roles with ROLE_ prefix correctly")
    void testRolePrefixMapping() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user:dr-smith");
        request.addUserRole("ROLE_PRV_RDR");

        assertThatCode(() -> interceptor.authorize("Practitioner", "read", request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Extracts multiple container roles into aggregated authorities")
    void testExtractAuthoritiesMultipleRoles() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user:power-user");
        request.addUserRole("PRV_RDR");
        request.addUserRole("PRV_SUB");

        assertThatCode(() -> interceptor.authorize("Practitioner", "read", request))
                .doesNotThrowAnyException();
        assertThatCode(() -> interceptor.authorize("Practitioner", "create.request", request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Populates ThemisSecurityContext with correlation ID and transport metadata")
    void testSecurityContextPopulation() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user:dr-smith");
        request.addUserRole("PRV_RDR");
        request.addHeader(FhirSecurityInterceptor.HEADER_CORRELATION_ID, "corr-abc-123");
        request.addHeader(FhirSecurityInterceptor.HEADER_SOURCE_SYSTEM, "ehr-system");

        interceptor.authorize("Practitioner", "read", request);

        assertThat(request.getAttribute(FhirSecurityInterceptor.ATTR_THEMIS_CONTEXT)).isNotNull();
        assertThat(request.getAttribute(FhirSecurityInterceptor.ATTR_THEMIS_PRINCIPAL)).isNotNull();
        assertThat(request.getAttribute(FhirSecurityInterceptor.ATTR_THEMIS_AUTHORITIES)).isNotNull();
    }
}
