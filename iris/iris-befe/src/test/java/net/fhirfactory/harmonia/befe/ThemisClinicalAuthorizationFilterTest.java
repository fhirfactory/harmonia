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

import ca.uhn.fhir.context.FhirContext;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import net.fhirfactory.harmonia.befe.security.DefaultThemisAuthorizer;
import net.fhirfactory.harmonia.befe.security.ThemisClinicalAuthorizationFilter;
import net.fhirfactory.harmonia.themis.api.ThemisAuthorizer;
import net.fhirfactory.harmonia.themis.api.model.*;
import org.hl7.fhir.r5.model.OperationOutcome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ThemisClinicalAuthorizationFilterTest {

    private ThemisAuthorizer mockAuthorizer;
    private ThemisClinicalAuthorizationFilter filter;
    private ContainerRequestContext requestContext;
    private UriInfo uriInfo;
    private SecurityContext securityContext;
    private MultivaluedMap<String, String> headers;
    private final FhirContext fhirContext = FhirContext.forR5();

    @BeforeEach
    void setUp() {
        mockAuthorizer = mock(ThemisAuthorizer.class);
        filter = new ThemisClinicalAuthorizationFilter(mockAuthorizer);

        requestContext = mock(ContainerRequestContext.class);
        uriInfo = mock(UriInfo.class);
        securityContext = mock(SecurityContext.class);
        headers = new MultivaluedHashMap<>();

        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(requestContext.getHeaders()).thenReturn(headers);
        when(requestContext.getSecurityContext()).thenReturn(securityContext);
    }

    private void configureRequest(String method, String relativePath) {
        when(requestContext.getMethod()).thenReturn(method);
        when(uriInfo.getPath()).thenReturn(relativePath);
        when(uriInfo.getRequestUri()).thenReturn(URI.create("http://localhost:8080/api/" + relativePath));
    }

    private void configureContainerPrincipal(String username, String... roles) {
        Principal principal = () -> username;
        when(securityContext.getUserPrincipal()).thenReturn(principal);
        if (roles != null) {
            for (String role : roles) {
                when(securityContext.isUserInRole(role)).thenReturn(true);
                when(securityContext.isUserInRole("ROLE_" + role)).thenReturn(true);
            }
        }
    }

    // A. Unauthenticated / No usable principal -> Denied (401)
    @Test
    @DisplayName("A. Unauthenticated request without container principal is denied with 401 and OperationOutcome")
    void testUnauthenticatedRequestDenied() throws IOException {
        configureRequest("GET", "fhir/Person");
        when(securityContext.getUserPrincipal()).thenReturn(null);

        filter.filter(requestContext);

        ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
        verify(requestContext).abortWith(responseCaptor.capture());

        Response response = responseCaptor.getValue();
        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getMediaType().toString()).isEqualTo("application/fhir+json");

        String entity = (String) response.getEntity();
        OperationOutcome outcome = fhirContext.newJsonParser().parseResource(OperationOutcome.class, entity);
        assertThat(outcome.getIssue()).isNotEmpty();
        assertThat(outcome.getIssueFirstRep().getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
        assertThat(outcome.getIssueFirstRep().getCode()).isEqualTo(OperationOutcome.IssueType.SECURITY);
        assertThat(outcome.getIssueFirstRep().getDiagnostics()).contains("Authentication required");
    }

    // B1. Anti-spoof regression test: Client sending self-asserted credentials without container authentication -> 401
    @Test
    @DisplayName("B1. Anti-spoof: Client-supplied headers/bearer tokens without container SecurityContext fail closed (401)")
    void testClientSuppliedHeadersWithoutContainerPrincipalDenied() throws IOException {
        configureRequest("GET", "fhir/Person/123");
        when(securityContext.getUserPrincipal()).thenReturn(null);
        headers.putSingle("Authorization", "Bearer attacker:CLINICAL_ADMIN");
        headers.putSingle("x-harmonia-user", "admin");
        headers.putSingle("x-harmonia-role", "SYS_ADM");
        headers.putSingle("x-harmonia-authorities", "system.admin,clinical.delete");

        filter.filter(requestContext);

        ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
        verify(requestContext).abortWith(responseCaptor.capture());
        assertThat(responseCaptor.getValue().getStatus()).isEqualTo(401);
        verifyNoInteractions(mockAuthorizer);
    }

    // B2. Anonymous principal identity is denied with 401
    @Test
    @DisplayName("B2. Anonymous container principal identity is denied with 401")
    void testAnonymousPrincipalDenied() throws IOException {
        configureRequest("GET", "fhir/Task/xyz");
        configureContainerPrincipal("anonymous");

        filter.filter(requestContext);

        ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
        verify(requestContext).abortWith(responseCaptor.capture());
        assertThat(responseCaptor.getValue().getStatus()).isEqualTo(401);
        verifyNoInteractions(mockAuthorizer);
    }

    // C1. Authenticated principal denied by Themis -> 403 Forbidden with OperationOutcome
    @Test
    @DisplayName("C1. Authenticated principal without required authority is denied with 403 and OperationOutcome")
    void testAuthenticatedPrincipalDeniedForbidden() throws IOException {
        configureRequest("DELETE", "fhir/Person/abc123");
        configureContainerPrincipal("dr-smith");

        when(mockAuthorizer.authorize(any(ThemisAuthorizationRequest.class))).thenReturn(
                ThemisAuthorizationDecision.deny(
                        ThemisDecisionReason.ACTION_NOT_PERMITTED,
                        "themis.policy.clinical",
                        "corr-1",
                        "Insufficient authorization for DELETE on Person"
                )
        );

        filter.filter(requestContext);

        ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
        verify(requestContext).abortWith(responseCaptor.capture());

        Response response = responseCaptor.getValue();
        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getMediaType().toString()).isEqualTo("application/fhir+json");

        String entity = (String) response.getEntity();
        OperationOutcome outcome = fhirContext.newJsonParser().parseResource(OperationOutcome.class, entity);
        assertThat(outcome.getIssueFirstRep().getCode()).isEqualTo(OperationOutcome.IssueType.FORBIDDEN);
        assertThat(outcome.getIssueFirstRep().getDiagnostics()).contains("insufficient authorization");
    }

    // C2. Authenticated principal with DefaultThemisAuthorizer fails closed (403)
    @Test
    @DisplayName("C2. Authenticated principal evaluated by DefaultThemisAuthorizer fails closed with 403")
    void testDefaultAuthorizerClinicalFailsClosed() throws IOException {
        ThemisClinicalAuthorizationFilter defaultFilter = new ThemisClinicalAuthorizationFilter(new DefaultThemisAuthorizer());
        configureRequest("GET", "fhir/Person");
        configureContainerPrincipal("dr-smith");

        defaultFilter.filter(requestContext);

        ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
        verify(requestContext).abortWith(responseCaptor.capture());
        assertThat(responseCaptor.getValue().getStatus()).isEqualTo(403);
    }

    // D. Authorised SEARCH -> reaches resource
    @Test
    @DisplayName("D. Authorised SEARCH: GET /api/fhir/Person is permitted when Themis authorizer allows")
    void testAuthorisedSearchAllowed() throws IOException {
        configureRequest("GET", "fhir/Person");
        configureContainerPrincipal("nurse-ann");

        when(mockAuthorizer.authorize(any(ThemisAuthorizationRequest.class))).thenReturn(
                ThemisAuthorizationDecision.allow("themis.policy.clinical", "corr-d", "Access granted")
        );

        filter.filter(requestContext);

        verify(requestContext, never()).abortWith(any());
        verify(requestContext).setProperty(eq(ThemisClinicalAuthorizationFilter.ATTR_THEMIS_PRINCIPAL), argThat(p -> ((ThemisPrincipal) p).principalId().equals("nurse-ann")));
        verify(requestContext).setProperty(eq(ThemisClinicalAuthorizationFilter.ATTR_THEMIS_DECISION), argThat(d -> ((ThemisAuthorizationDecision) d).isAllowed()));

        ArgumentCaptor<ThemisAuthorizationRequest> captor = ArgumentCaptor.forClass(ThemisAuthorizationRequest.class);
        verify(mockAuthorizer).authorize(captor.capture());
        ThemisAuthorizationRequest authReq = captor.getValue();
        assertThat(authReq.action()).isEqualTo(ThemisAction.SEARCH);
        assertThat(authReq.target().resourceType()).isEqualTo("Person");
        assertThat(authReq.target().resourceId()).isNull();
        assertThat(authReq.target().securityDomain()).isEqualTo("CLINICAL");
    }

    // E. Authorised READ -> reaches resource
    @Test
    @DisplayName("E. Authorised READ: GET /api/fhir/Person/abc123 is permitted when Themis authorizer allows")
    void testAuthorisedReadAllowed() throws IOException {
        configureRequest("GET", "fhir/Person/abc123");
        configureContainerPrincipal("nurse-ann");

        when(mockAuthorizer.authorize(any(ThemisAuthorizationRequest.class))).thenReturn(
                ThemisAuthorizationDecision.allow("themis.policy.clinical", "corr-e", "Access granted")
        );

        filter.filter(requestContext);

        verify(requestContext, never()).abortWith(any());

        ArgumentCaptor<ThemisAuthorizationRequest> captor = ArgumentCaptor.forClass(ThemisAuthorizationRequest.class);
        verify(mockAuthorizer).authorize(captor.capture());
        ThemisAuthorizationRequest authReq = captor.getValue();
        assertThat(authReq.action()).isEqualTo(ThemisAction.READ);
        assertThat(authReq.target().resourceType()).isEqualTo("Person");
        assertThat(authReq.target().resourceId()).isEqualTo("abc123");
        assertThat(authReq.target().securityDomain()).isEqualTo("CLINICAL");
    }

    // F. Authorised CREATE -> reaches resource
    @Test
    @DisplayName("F. Authorised CREATE: POST /api/fhir/Person is permitted when Themis authorizer allows")
    void testAuthorisedCreateAllowed() throws IOException {
        configureRequest("POST", "fhir/Person");
        configureContainerPrincipal("dr-jones");

        when(mockAuthorizer.authorize(any(ThemisAuthorizationRequest.class))).thenReturn(
                ThemisAuthorizationDecision.allow("themis.policy.clinical", "corr-f", "Access granted")
        );

        filter.filter(requestContext);

        verify(requestContext, never()).abortWith(any());

        ArgumentCaptor<ThemisAuthorizationRequest> captor = ArgumentCaptor.forClass(ThemisAuthorizationRequest.class);
        verify(mockAuthorizer).authorize(captor.capture());
        ThemisAuthorizationRequest authReq = captor.getValue();
        assertThat(authReq.action()).isEqualTo(ThemisAction.CREATE);
        assertThat(authReq.target().resourceType()).isEqualTo("Person");
        assertThat(authReq.target().resourceId()).isNull();
    }

    // G. Authorised UPDATE -> reaches resource
    @Test
    @DisplayName("G. Authorised UPDATE: PUT /api/fhir/Task/xyz789 is permitted when Themis authorizer allows")
    void testAuthorisedUpdateAllowed() throws IOException {
        configureRequest("PUT", "fhir/Task/xyz789");
        configureContainerPrincipal("dr-jones");

        when(mockAuthorizer.authorize(any(ThemisAuthorizationRequest.class))).thenReturn(
                ThemisAuthorizationDecision.allow("themis.policy.clinical", "corr-g", "Access granted")
        );

        filter.filter(requestContext);

        verify(requestContext, never()).abortWith(any());

        ArgumentCaptor<ThemisAuthorizationRequest> captor = ArgumentCaptor.forClass(ThemisAuthorizationRequest.class);
        verify(mockAuthorizer).authorize(captor.capture());
        ThemisAuthorizationRequest authReq = captor.getValue();
        assertThat(authReq.action()).isEqualTo(ThemisAction.UPDATE);
        assertThat(authReq.target().resourceType()).isEqualTo("Task");
        assertThat(authReq.target().resourceId()).isEqualTo("xyz789");
    }

    // H. Authorised DELETE -> reaches resource
    @Test
    @DisplayName("H. Authorised DELETE: DELETE /api/fhir/Person/abc123 is permitted when Themis authorizer allows")
    void testAuthorisedDeleteAllowed() throws IOException {
        configureRequest("DELETE", "fhir/Person/abc123");
        configureContainerPrincipal("admin-chief");

        when(mockAuthorizer.authorize(any(ThemisAuthorizationRequest.class))).thenReturn(
                ThemisAuthorizationDecision.allow("themis.policy.clinical", "corr-h", "Access granted")
        );

        filter.filter(requestContext);

        verify(requestContext, never()).abortWith(any());

        ArgumentCaptor<ThemisAuthorizationRequest> captor = ArgumentCaptor.forClass(ThemisAuthorizationRequest.class);
        verify(mockAuthorizer).authorize(captor.capture());
        ThemisAuthorizationRequest authReq = captor.getValue();
        assertThat(authReq.action()).isEqualTo(ThemisAction.DELETE);
        assertThat(authReq.target().resourceType()).isEqualTo("Person");
        assertThat(authReq.target().resourceId()).isEqualTo("abc123");
    }

    // I. Unknown/new Clinical resource path -> does not bypass security boundary
    @Test
    @DisplayName("I1. Unknown Clinical resource path without authentication is denied with 401")
    void testUnknownClinicalResourcePathUnauthenticatedDenied() throws IOException {
        configureRequest("GET", "fhir/FutureObservation/future123");
        when(securityContext.getUserPrincipal()).thenReturn(null);

        filter.filter(requestContext);

        ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
        verify(requestContext).abortWith(responseCaptor.capture());
        assertThat(responseCaptor.getValue().getStatus()).isEqualTo(401);
    }

    @Test
    @DisplayName("I2. Unknown Clinical resource path with unprivileged role is denied with 403")
    void testUnknownClinicalResourcePathUnauthorizedDenied() throws IOException {
        configureRequest("DELETE", "fhir/FutureObservation/future123");
        configureContainerPrincipal("user");

        when(mockAuthorizer.authorize(any(ThemisAuthorizationRequest.class))).thenReturn(
                ThemisAuthorizationDecision.deny(
                        ThemisDecisionReason.ACTION_NOT_PERMITTED,
                        "themis.policy.clinical",
                        "corr-i2",
                        "Denied"
                )
        );

        filter.filter(requestContext);

        ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
        verify(requestContext).abortWith(responseCaptor.capture());
        assertThat(responseCaptor.getValue().getStatus()).isEqualTo(403);
    }

    // J. Non-Clinical BEFE endpoints are not unintentionally altered by this filter
    @Test
    @DisplayName("J1. Non-clinical endpoint /api/operations/summary is ignored by clinical filter")
    void testNonClinicalOperationsEndpointIgnored() throws IOException {
        configureRequest("GET", "operations/summary");

        filter.filter(requestContext);

        verify(requestContext, never()).abortWith(any());
        verify(requestContext, never()).setProperty(anyString(), any());
        verifyNoInteractions(mockAuthorizer);
    }

    @Test
    @DisplayName("J2. Non-clinical endpoint /api/status is ignored by clinical filter")
    void testNonClinicalStatusEndpointIgnored() throws IOException {
        configureRequest("GET", "status");

        filter.filter(requestContext);

        verify(requestContext, never()).abortWith(any());
        verifyNoInteractions(mockAuthorizer);
    }

    // K. OPTIONS method for CORS preflight
    @Test
    @DisplayName("K. OPTIONS requests for CORS preflight pass through without auth denial")
    void testOptionsRequestPassesThrough() throws IOException {
        configureRequest("OPTIONS", "fhir/Person");

        filter.filter(requestContext);

        verify(requestContext, never()).abortWith(any());
        verifyNoInteractions(mockAuthorizer);
    }

    // L. Anti-spoof regression test: Self-asserted privilege escalation headers are ignored when container principal is unprivileged
    @Test
    @DisplayName("L. Anti-spoof: Client-supplied headers cannot escalate privileges of container principal")
    void testHeaderPrivilegeEscalationIgnored() throws IOException {
        configureRequest("DELETE", "fhir/Person/abc123");
        configureContainerPrincipal("regular-user");

        // Client attempts privilege escalation via request headers
        headers.putSingle("x-harmonia-role", "SYS_ADM");
        headers.putSingle("x-harmonia-authorities", "clinical.admin,clinical.delete,system.admin");
        headers.putSingle("Authorization", "Bearer regular-user:SYS_ADM");

        when(mockAuthorizer.authorize(any(ThemisAuthorizationRequest.class))).thenReturn(
                ThemisAuthorizationDecision.deny(ThemisDecisionReason.ACTION_NOT_PERMITTED, "themis.policy.clinical", "corr-l", "Denied")
        );

        filter.filter(requestContext);

        ArgumentCaptor<ThemisAuthorizationRequest> captor = ArgumentCaptor.forClass(ThemisAuthorizationRequest.class);
        verify(mockAuthorizer).authorize(captor.capture());
        ThemisAuthorizationRequest authReq = captor.getValue();

        // Principal must match container principal, not any spoofed header
        assertThat(authReq.principal().principalId()).isEqualTo("regular-user");

        // Authorities must be empty since container securityContext has no roles mapped
        assertThat(authReq.authorities()).isEmpty();
    }

    // M. Action mapping helper test
    @Test
    @DisplayName("M. Action mapping correctly maps HTTP methods and resourceId")
    void testActionMapping() {
        assertThat(filter.mapMethodToAction("GET", null)).isEqualTo(ThemisAction.SEARCH);
        assertThat(filter.mapMethodToAction("GET", "123")).isEqualTo(ThemisAction.READ);
        assertThat(filter.mapMethodToAction("POST", null)).isEqualTo(ThemisAction.CREATE);
        assertThat(filter.mapMethodToAction("PUT", "123")).isEqualTo(ThemisAction.UPDATE);
        assertThat(filter.mapMethodToAction("DELETE", "123")).isEqualTo(ThemisAction.DELETE);
        assertThat(filter.mapMethodToAction("HEAD", null)).isEqualTo(ThemisAction.SEARCH);
        assertThat(filter.mapMethodToAction("HEAD", "123")).isEqualTo(ThemisAction.READ);
        assertThat(filter.mapMethodToAction("PATCH", "123")).isEqualTo(ThemisAction.UPDATE);
        assertThat(filter.mapMethodToAction("INVALID", null)).isNull();
    }
}
