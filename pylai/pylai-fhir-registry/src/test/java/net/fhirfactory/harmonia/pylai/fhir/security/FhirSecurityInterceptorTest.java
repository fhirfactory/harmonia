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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Pylai FHIR Security Interceptor & Themis Gateway Tests")
class FhirSecurityInterceptorTest {

    private final FhirSecurityInterceptor interceptor = new FhirSecurityInterceptor();

    @Test
    @DisplayName("Allows read/search when PRV_RDR mnemonic role is granted")
    void testAuthorizedPrvRdrRole() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(FhirSecurityInterceptor.HEADER_USER_ROLES, "PRV_RDR");
        request.addHeader(FhirSecurityInterceptor.HEADER_REQUESTER, "user:dr-smith");

        assertThatCode(() -> interceptor.authorize("Practitioner", "read", request))
                .doesNotThrowAnyException();
        assertThatCode(() -> interceptor.authorize("Practitioner", "search", request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Allows submit create/update when PRV_SUB mnemonic role is granted")
    void testAuthorizedPrvSubRole() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(FhirSecurityInterceptor.HEADER_USER_ROLES, "PRV_SUB");
        request.addHeader(FhirSecurityInterceptor.HEADER_REQUESTER, "user:registry-submitter");

        assertThatCode(() -> interceptor.authorize("Practitioner", "create.request", request))
                .doesNotThrowAnyException();
        assertThatCode(() -> interceptor.authorize("Practitioner", "update.request", request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Denies write interactions when caller only has PRV_RDR")
    void testDeniedWriteForPrvRdr() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(FhirSecurityInterceptor.HEADER_USER_ROLES, "PRV_RDR");
        request.addHeader(FhirSecurityInterceptor.HEADER_REQUESTER, "user:reader-only");

        assertThatThrownBy(() -> interceptor.authorize("Practitioner", "create.request", request))
                .isInstanceOf(ForbiddenOperationException.class)
                .hasMessageContaining("missing required permission [Practitioner.create.request]");
    }

    @Test
    @DisplayName("Allows interaction when specific permission is granted")
    void testAuthorizedSpecificPermission() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(FhirSecurityInterceptor.HEADER_USER_ROLES, "Practitioner.read");

        assertThatCode(() -> interceptor.authorize("Practitioner", "read", request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Allows interaction when wildcard permission is granted")
    void testAuthorizedWildcardPermission() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(FhirSecurityInterceptor.HEADER_USER_ROLES, "Practitioner.*");

        assertThatCode(() -> interceptor.authorize("Practitioner", "create.request", request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Allows interaction when admin role is present")
    void testAuthorizedAdminRole() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(FhirSecurityInterceptor.HEADER_USER_ROLES, "ROLE_ADMIN");

        assertThatCode(() -> interceptor.authorize("Endpoint", "update.request", request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Denies interaction when permission is missing (Default Deny)")
    void testDeniedMissingPermission() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(FhirSecurityInterceptor.HEADER_USER_ROLES, "Practitioner.read");

        assertThatThrownBy(() -> interceptor.authorize("Practitioner", "create.request", request))
                .isInstanceOf(ForbiddenOperationException.class)
                .hasMessageContaining("missing required permission [Practitioner.create.request]");
    }

    @Test
    @DisplayName("Throws AuthenticationException for invalid bearer token")
    void testInvalidTokenThrowsAuthenticationException() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(FhirSecurityInterceptor.HEADER_AUTH_TOKEN, "Bearer invalid-token");

        assertThatThrownBy(() -> interceptor.authorize("Practitioner", "read", request))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    @DisplayName("Extracts principal identity and attributes into request")
    void testPrincipalExtraction() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(FhirSecurityInterceptor.HEADER_PRINCIPAL_ID, "service:pylai-gateway");
        request.addHeader(FhirSecurityInterceptor.HEADER_PRINCIPAL_TYPE, "SERVICE");
        request.addHeader(FhirSecurityInterceptor.HEADER_SOURCE_DOMAIN, "gateway-cluster");

        ThemisPrincipal principal = interceptor.extractPrincipal(request);
        assertThat(principal.principalId()).isEqualTo("service:pylai-gateway");
        assertThat(principal.principalType()).isEqualTo(PrincipalType.SERVICE);
        assertThat(principal.sourceDomain()).isEqualTo("gateway-cluster");
    }
}
