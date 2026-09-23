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
import net.fhirfactory.harmonia.befe.security.OidcTestTokenHelper;
import net.fhirfactory.harmonia.befe.security.ThemisClinicalAuthorizationFilter;
import net.fhirfactory.harmonia.befe.security.ThemisSecurityContextProvider;
import net.fhirfactory.harmonia.model.security.HarmoniaRoleEnum;
import net.fhirfactory.harmonia.themis.api.ThemisAuthorizer;
import net.fhirfactory.harmonia.themis.api.model.*;
import net.fhirfactory.harmonia.themis.core.identities.HarmoniaServiceIdentities;
import org.hl7.fhir.r5.model.OperationOutcome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit and anti-spoofing regression tests for {@link ThemisClinicalAuthorizationFilter}.
 *
 * Boundary & Verification Contract:
 * 1. WildFly Container-Level OIDC Authentication Boundary:
 *    - In the production WAR runtime on WildFly 31, authentication is managed exclusively by the
 *      {@code elytron-oidc-client} subsystem configured in {@code web.xml} and {@code oidc.json}.
 *    - Container authentication executes before JAX-RS {@code @PreMatching} filters. Missing,
 *      expired, or cryptographically invalid bearer tokens are rejected at the container boundary
 *      with HTTP 401 Unauthorized (RFC 6750 {@code WWW-Authenticate: Bearer}).
 * 2. JAX-RS Themis Authorization Boundary:
 *    - Valid bearer tokens establish a container {@link SecurityContext} principal populated
 *      with the validated {@code sub} claim.
 *    - {@link ThemisClinicalAuthorizationFilter} consumes {@code SecurityContext.getUserPrincipal()}
 *      as {@link PrincipalType#HUMAN} and delegates authorization to Themis.
 *    - Under default-deny governance (Task 01), authenticated clinical requests without an approved
 *      clinical authorization policy (Task 03) return HTTP 403 Forbidden with a FHIR {@link OperationOutcome}.
 * 3. Anti-Spoofing Invariants:
 *    - Caller-supplied HTTP headers (e.g. {@code X-Harmonia-*}, {@code X-Principal-Id}, {@code X-Requester})
 *      and arbitrary or raw bearer text cannot establish identity or elevate authorization.
 *    - The application filter does NOT perform in-filter token parsing or trust raw authorization headers.
 */
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

    // N. Container principal extraction correctly extracts validated subject into ThemisPrincipal(HUMAN)
    @Test
    @DisplayName("N1. Valid container principal subject is correctly mapped to ThemisPrincipal with PrincipalType.HUMAN")
    void testExtractPrincipalValidSubjectMapping() {
        SecurityContext sc = mock(SecurityContext.class);
        when(sc.getUserPrincipal()).thenReturn(() -> "sub-oidc-uuid-98765");

        ThemisPrincipal principal = filter.extractPrincipal(sc);
        assertThat(principal).isNotNull();
        assertThat(principal.principalId()).isEqualTo("sub-oidc-uuid-98765");
        assertThat(principal.principalType()).isEqualTo(PrincipalType.HUMAN);
        assertThat(principal.sourceDomain()).isEqualTo("harmonia-clinical");
        assertThat(principal.attributes()).isEmpty();
    }

    @Test
    @DisplayName("N2. Null, blank, whitespace, or anonymous container principals are rejected by extractPrincipal")
    void testExtractPrincipalRejections() {
        assertThat(filter.extractPrincipal(null)).isNull();

        SecurityContext nullPrincipalContext = mock(SecurityContext.class);
        when(nullPrincipalContext.getUserPrincipal()).thenReturn(null);
        assertThat(filter.extractPrincipal(nullPrincipalContext)).isNull();

        SecurityContext emptyPrincipalContext = mock(SecurityContext.class);
        when(emptyPrincipalContext.getUserPrincipal()).thenReturn(() -> "");
        assertThat(filter.extractPrincipal(emptyPrincipalContext)).isNull();

        SecurityContext blankPrincipalContext = mock(SecurityContext.class);
        when(blankPrincipalContext.getUserPrincipal()).thenReturn(() -> "   ");
        assertThat(filter.extractPrincipal(blankPrincipalContext)).isNull();

        SecurityContext anonymousContext = mock(SecurityContext.class);
        when(anonymousContext.getUserPrincipal()).thenReturn(() -> "anonymous");
        assertThat(filter.extractPrincipal(anonymousContext)).isNull();

        SecurityContext systemAnonymousContext = mock(SecurityContext.class);
        when(systemAnonymousContext.getUserPrincipal()).thenReturn(() -> "system:anonymous");
        assertThat(filter.extractPrincipal(systemAnonymousContext)).isNull();
    }

    // O. Valid signed JWT in Authorization header without container auth fails closed (401)
    @Test
    @DisplayName("O. Valid signed JWT in Authorization header without container SecurityContext fails closed with 401")
    void testValidJwtWithoutContainerAuthFailsClosed() throws IOException {
        String validJwt = OidcTestTokenHelper.generateValidToken("sub-clinician-123");
        configureRequest("GET", "fhir/Person/123");
        headers.putSingle("Authorization", "Bearer " + validJwt);
        // Container auth did not populate SecurityContext (or failed)
        when(securityContext.getUserPrincipal()).thenReturn(null);

        filter.filter(requestContext);

        ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
        verify(requestContext).abortWith(responseCaptor.capture());
        Response response = responseCaptor.getValue();
        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getMediaType().toString()).isEqualTo("application/fhir+json");

        String entity = (String) response.getEntity();
        OperationOutcome outcome = fhirContext.newJsonParser().parseResource(OperationOutcome.class, entity);
        assertThat(outcome.getIssueFirstRep().getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
        assertThat(outcome.getIssueFirstRep().getCode()).isEqualTo(OperationOutcome.IssueType.SECURITY);
        assertThat(outcome.getIssueFirstRep().getDiagnostics()).contains("Authentication required");

        verifyNoInteractions(mockAuthorizer);
    }

    // P. Valid container authentication evaluates to 403 Forbidden under DefaultThemisAuthorizer
    @Test
    @DisplayName("P. Authenticated caller with valid OIDC subject receives 403 OperationOutcome under default-deny")
    void testAuthenticatedCallerReceivesForbiddenDefaultDeny() throws IOException {
        String tokenSub = "sub-clinician-456";
        String validJwt = OidcTestTokenHelper.generateValidToken(tokenSub);

        ThemisClinicalAuthorizationFilter defaultFilter = new ThemisClinicalAuthorizationFilter(new DefaultThemisAuthorizer());
        configureRequest("POST", "fhir/Person");
        headers.putSingle("Authorization", "Bearer " + validJwt);
        configureContainerPrincipal(tokenSub);

        defaultFilter.filter(requestContext);

        ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
        verify(requestContext).abortWith(responseCaptor.capture());

        Response response = responseCaptor.getValue();
        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getMediaType().toString()).isEqualTo("application/fhir+json");

        String entity = (String) response.getEntity();
        OperationOutcome outcome = fhirContext.newJsonParser().parseResource(OperationOutcome.class, entity);
        assertThat(outcome.getIssueFirstRep().getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
        assertThat(outcome.getIssueFirstRep().getCode()).isEqualTo(OperationOutcome.IssueType.FORBIDDEN);
        assertThat(outcome.getIssueFirstRep().getDiagnostics()).contains("Access denied");
    }

    // Q. Comprehensive anti-spoof header immunity
    @Test
    @DisplayName("Q. Comprehensive anti-spoof: All legacy and custom headers are ignored; only container principal is used")
    void testComprehensiveAntiSpoofHeadersIgnored() throws IOException {
        configureRequest("PUT", "fhir/Person/patient-001");
        configureContainerPrincipal("sub-legitimate-user");

        // Attacker injects comprehensive spoofing headers
        headers.putSingle("X-Principal-Id", "admin-spoofed");
        headers.putSingle("X-Requester", "system:root");
        headers.putSingle("X-User-Roles", "CLINICAL_ADMIN,SYSTEM_ADMIN");
        headers.putSingle("X-Security-Scopes", "fhir.all,clinical.write");
        headers.putSingle("x-harmonia-user", "service:themis");
        headers.putSingle("x-harmonia-role", "SYS_ADM");
        headers.putSingle("x-harmonia-authorities", "admin.all");
        headers.putSingle("Authorization", "Bearer " + OidcTestTokenHelper.generateValidToken("spoofed-sub-in-jwt"));

        when(mockAuthorizer.authorize(any(ThemisAuthorizationRequest.class))).thenReturn(
                ThemisAuthorizationDecision.deny(ThemisDecisionReason.ACTION_NOT_PERMITTED, "themis.policy.clinical", "corr-q", "Denied")
        );

        filter.filter(requestContext);

        ArgumentCaptor<ThemisAuthorizationRequest> captor = ArgumentCaptor.forClass(ThemisAuthorizationRequest.class);
        verify(mockAuthorizer).authorize(captor.capture());
        ThemisAuthorizationRequest authReq = captor.getValue();

        // Must strictly bind to the container principal, never any spoofed header or raw bearer claim
        assertThat(authReq.principal().principalId()).isEqualTo("sub-legitimate-user");
        assertThat(authReq.principal().principalType()).isEqualTo(PrincipalType.HUMAN);
        assertThat(authReq.authorities()).isEmpty();
    }

    // =========================================================================
    // R. End-to-End Evaluator-Backed Clinical Policy Outcomes & Cross-Domain Isolation
    // =========================================================================

    @Test
    @DisplayName("R1. CLINICAL_READ: permits GET instance (READ) and GET search (SEARCH), denies CREATE, UPDATE, DELETE")
    void testClinicalReadRoleOutcomes() throws IOException {
        ThemisClinicalAuthorizationFilter e2eFilter = new ThemisClinicalAuthorizationFilter(new DefaultThemisAuthorizer());

        // 1. GET instance (READ) -> ALLOW
        configureRequest("GET", "fhir/Person/123");
        configureContainerPrincipal("nurse-read", HarmoniaRoleEnum.CLINICAL_READ.getRoleCode());
        e2eFilter.filter(requestContext);
        verify(requestContext, never()).abortWith(any());

        // 2. GET search (SEARCH) -> ALLOW
        reset(requestContext);
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(requestContext.getHeaders()).thenReturn(headers);
        when(requestContext.getSecurityContext()).thenReturn(securityContext);
        configureRequest("GET", "fhir/Person");
        configureContainerPrincipal("nurse-read", HarmoniaRoleEnum.CLINICAL_READ.getRoleCode());
        e2eFilter.filter(requestContext);
        verify(requestContext, never()).abortWith(any());

        // 3. POST (CREATE) -> 403 DENY
        reset(requestContext);
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(requestContext.getHeaders()).thenReturn(headers);
        when(requestContext.getSecurityContext()).thenReturn(securityContext);
        configureRequest("POST", "fhir/Person");
        configureContainerPrincipal("nurse-read", HarmoniaRoleEnum.CLINICAL_READ.getRoleCode());
        e2eFilter.filter(requestContext);
        ArgumentCaptor<Response> resCaptor = ArgumentCaptor.forClass(Response.class);
        verify(requestContext).abortWith(resCaptor.capture());
        assertThat(resCaptor.getValue().getStatus()).isEqualTo(403);

        // 4. PUT (UPDATE) -> 403 DENY
        reset(requestContext);
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(requestContext.getHeaders()).thenReturn(headers);
        when(requestContext.getSecurityContext()).thenReturn(securityContext);
        configureRequest("PUT", "fhir/Person/123");
        configureContainerPrincipal("nurse-read", HarmoniaRoleEnum.CLINICAL_READ.getRoleCode());
        e2eFilter.filter(requestContext);
        resCaptor = ArgumentCaptor.forClass(Response.class);
        verify(requestContext).abortWith(resCaptor.capture());
        assertThat(resCaptor.getValue().getStatus()).isEqualTo(403);

        // 5. PATCH (UPDATE) -> 403 DENY
        reset(requestContext);
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(requestContext.getHeaders()).thenReturn(headers);
        when(requestContext.getSecurityContext()).thenReturn(securityContext);
        configureRequest("PATCH", "fhir/Person/123");
        configureContainerPrincipal("nurse-read", HarmoniaRoleEnum.CLINICAL_READ.getRoleCode());
        e2eFilter.filter(requestContext);
        resCaptor = ArgumentCaptor.forClass(Response.class);
        verify(requestContext).abortWith(resCaptor.capture());
        assertThat(resCaptor.getValue().getStatus()).isEqualTo(403);

        // 6. DELETE -> 403 DENY
        reset(requestContext);
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(requestContext.getHeaders()).thenReturn(headers);
        when(requestContext.getSecurityContext()).thenReturn(securityContext);
        configureRequest("DELETE", "fhir/Person/123");
        configureContainerPrincipal("nurse-read", HarmoniaRoleEnum.CLINICAL_READ.getRoleCode());
        e2eFilter.filter(requestContext);
        resCaptor = ArgumentCaptor.forClass(Response.class);
        verify(requestContext).abortWith(resCaptor.capture());
        assertThat(resCaptor.getValue().getStatus()).isEqualTo(403);
    }

    @Test
    @DisplayName("R2. CLINICAL_WRITE: permits READ, SEARCH, CREATE, UPDATE (including PATCH), denies DELETE")
    void testClinicalWriteRoleOutcomes() throws IOException {
        ThemisClinicalAuthorizationFilter e2eFilter = new ThemisClinicalAuthorizationFilter(new DefaultThemisAuthorizer());

        // 1. GET instance (READ) -> ALLOW
        configureRequest("GET", "fhir/Person/123");
        configureContainerPrincipal("dr-write", HarmoniaRoleEnum.CLINICAL_WRITE.getRoleCode());
        e2eFilter.filter(requestContext);
        verify(requestContext, never()).abortWith(any());

        // 2. GET search (SEARCH) -> ALLOW
        reset(requestContext);
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(requestContext.getHeaders()).thenReturn(headers);
        when(requestContext.getSecurityContext()).thenReturn(securityContext);
        configureRequest("GET", "fhir/Person");
        configureContainerPrincipal("dr-write", HarmoniaRoleEnum.CLINICAL_WRITE.getRoleCode());
        e2eFilter.filter(requestContext);
        verify(requestContext, never()).abortWith(any());

        // 3. POST (CREATE) -> ALLOW
        reset(requestContext);
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(requestContext.getHeaders()).thenReturn(headers);
        when(requestContext.getSecurityContext()).thenReturn(securityContext);
        configureRequest("POST", "fhir/Person");
        configureContainerPrincipal("dr-write", HarmoniaRoleEnum.CLINICAL_WRITE.getRoleCode());
        e2eFilter.filter(requestContext);
        verify(requestContext, never()).abortWith(any());

        // 4. PUT (UPDATE) -> ALLOW
        reset(requestContext);
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(requestContext.getHeaders()).thenReturn(headers);
        when(requestContext.getSecurityContext()).thenReturn(securityContext);
        configureRequest("PUT", "fhir/Person/123");
        configureContainerPrincipal("dr-write", HarmoniaRoleEnum.CLINICAL_WRITE.getRoleCode());
        e2eFilter.filter(requestContext);
        verify(requestContext, never()).abortWith(any());

        // 5. PATCH (UPDATE) -> ALLOW
        reset(requestContext);
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(requestContext.getHeaders()).thenReturn(headers);
        when(requestContext.getSecurityContext()).thenReturn(securityContext);
        configureRequest("PATCH", "fhir/Person/123");
        configureContainerPrincipal("dr-write", HarmoniaRoleEnum.CLINICAL_WRITE.getRoleCode());
        e2eFilter.filter(requestContext);
        verify(requestContext, never()).abortWith(any());

        // 6. DELETE -> 403 DENY (Fail-closed)
        reset(requestContext);
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(requestContext.getHeaders()).thenReturn(headers);
        when(requestContext.getSecurityContext()).thenReturn(securityContext);
        configureRequest("DELETE", "fhir/Person/123");
        configureContainerPrincipal("dr-write", HarmoniaRoleEnum.CLINICAL_WRITE.getRoleCode());
        e2eFilter.filter(requestContext);
        ArgumentCaptor<Response> resCaptor = ArgumentCaptor.forClass(Response.class);
        verify(requestContext).abortWith(resCaptor.capture());
        assertThat(resCaptor.getValue().getStatus()).isEqualTo(403);
    }

    @Test
    @DisplayName("R3. CLINICAL_ADMIN: permits READ, SEARCH, CREATE, UPDATE (including PATCH), denies DELETE")
    void testClinicalAdminRoleOutcomes() throws IOException {
        ThemisClinicalAuthorizationFilter e2eFilter = new ThemisClinicalAuthorizationFilter(new DefaultThemisAuthorizer());

        // 1. GET instance (READ) -> ALLOW
        configureRequest("GET", "fhir/Person/123");
        configureContainerPrincipal("admin-user", HarmoniaRoleEnum.CLINICAL_ADMIN.getRoleCode());
        e2eFilter.filter(requestContext);
        verify(requestContext, never()).abortWith(any());

        // 2. GET search (SEARCH) -> ALLOW
        reset(requestContext);
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(requestContext.getHeaders()).thenReturn(headers);
        when(requestContext.getSecurityContext()).thenReturn(securityContext);
        configureRequest("GET", "fhir/Person");
        configureContainerPrincipal("admin-user", HarmoniaRoleEnum.CLINICAL_ADMIN.getRoleCode());
        e2eFilter.filter(requestContext);
        verify(requestContext, never()).abortWith(any());

        // 3. POST (CREATE) -> ALLOW
        reset(requestContext);
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(requestContext.getHeaders()).thenReturn(headers);
        when(requestContext.getSecurityContext()).thenReturn(securityContext);
        configureRequest("POST", "fhir/Person");
        configureContainerPrincipal("admin-user", HarmoniaRoleEnum.CLINICAL_ADMIN.getRoleCode());
        e2eFilter.filter(requestContext);
        verify(requestContext, never()).abortWith(any());

        // 4. PUT (UPDATE) -> ALLOW
        reset(requestContext);
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(requestContext.getHeaders()).thenReturn(headers);
        when(requestContext.getSecurityContext()).thenReturn(securityContext);
        configureRequest("PUT", "fhir/Person/123");
        configureContainerPrincipal("admin-user", HarmoniaRoleEnum.CLINICAL_ADMIN.getRoleCode());
        e2eFilter.filter(requestContext);
        verify(requestContext, never()).abortWith(any());

        // 5. PATCH (UPDATE) -> ALLOW
        reset(requestContext);
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(requestContext.getHeaders()).thenReturn(headers);
        when(requestContext.getSecurityContext()).thenReturn(securityContext);
        configureRequest("PATCH", "fhir/Person/123");
        configureContainerPrincipal("admin-user", HarmoniaRoleEnum.CLINICAL_ADMIN.getRoleCode());
        e2eFilter.filter(requestContext);
        verify(requestContext, never()).abortWith(any());

        // 6. DELETE -> 403 DENY (Fail-closed: clinical.delete does not exist)
        reset(requestContext);
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(requestContext.getHeaders()).thenReturn(headers);
        when(requestContext.getSecurityContext()).thenReturn(securityContext);
        configureRequest("DELETE", "fhir/Person/123");
        configureContainerPrincipal("admin-user", HarmoniaRoleEnum.CLINICAL_ADMIN.getRoleCode());
        e2eFilter.filter(requestContext);
        ArgumentCaptor<Response> resCaptor = ArgumentCaptor.forClass(Response.class);
        verify(requestContext).abortWith(resCaptor.capture());
        assertThat(resCaptor.getValue().getStatus()).isEqualTo(403);
    }

    @Test
    @DisplayName("R4. Authenticated caller without Clinical role is denied with 403")
    void testAuthenticatedCallerWithoutClinicalRoleDenied() throws IOException {
        ThemisClinicalAuthorizationFilter e2eFilter = new ThemisClinicalAuthorizationFilter(new DefaultThemisAuthorizer());
        configureRequest("GET", "fhir/Person/123");
        configureContainerPrincipal("no-role-user");

        e2eFilter.filter(requestContext);
        ArgumentCaptor<Response> resCaptor = ArgumentCaptor.forClass(Response.class);
        verify(requestContext).abortWith(resCaptor.capture());
        assertThat(resCaptor.getValue().getStatus()).isEqualTo(403);
    }

    @Test
    @DisplayName("R5. Caller with unknown role is denied with 403")
    void testCallerWithUnknownRoleDenied() throws IOException {
        ThemisClinicalAuthorizationFilter e2eFilter = new ThemisClinicalAuthorizationFilter(new DefaultThemisAuthorizer());
        configureRequest("GET", "fhir/Person/123");
        configureContainerPrincipal("unknown-role-user", "SOME_UNKNOWN_ROLE");

        e2eFilter.filter(requestContext);
        ArgumentCaptor<Response> resCaptor = ArgumentCaptor.forClass(Response.class);
        verify(requestContext).abortWith(resCaptor.capture());
        assertThat(resCaptor.getValue().getStatus()).isEqualTo(403);
    }

    @Test
    @DisplayName("R6. Cross-domain isolation: Unrelated Provider, Audit, and Operations roles do not grant Clinical access")
    void testUnrelatedDomainRolesDoNotGrantClinicalAccess() throws IOException {
        ThemisClinicalAuthorizationFilter e2eFilter = new ThemisClinicalAuthorizationFilter(new DefaultThemisAuthorizer());

        String[] unrelatedRoles = {
                HarmoniaRoleEnum.PRV_RDR.getRoleCode(),
                HarmoniaRoleEnum.PRV_ADM.getRoleCode(),
                HarmoniaRoleEnum.AUD_RDR.getRoleCode(),
                DefaultThemisAuthorizer.ROLE_OPS_VIEWER,
                DefaultThemisAuthorizer.ROLE_OPS_ADM,
                DefaultThemisAuthorizer.ROLE_SYS_INT
        };

        for (String role : unrelatedRoles) {
            reset(requestContext);
            when(requestContext.getUriInfo()).thenReturn(uriInfo);
            when(requestContext.getHeaders()).thenReturn(headers);
            when(requestContext.getSecurityContext()).thenReturn(securityContext);
            configureRequest("GET", "fhir/Person/123");
            configureContainerPrincipal("user-" + role, role);

            e2eFilter.filter(requestContext);

            ArgumentCaptor<Response> resCaptor = ArgumentCaptor.forClass(Response.class);
            verify(requestContext).abortWith(resCaptor.capture());
            assertThat(resCaptor.getValue().getStatus())
                    .as("Role %s must not grant Clinical access", role)
                    .isEqualTo(403);
        }
    }

    @Test
    @DisplayName("R7. Anti-spoofing: CLINICAL_READ caller cannot elevate to write/delete via headers")
    void testClinicalReadCannotElevateViaSpoofedHeaders() throws IOException {
        ThemisClinicalAuthorizationFilter e2eFilter = new ThemisClinicalAuthorizationFilter(new DefaultThemisAuthorizer());

        // Attacker with CLINICAL_READ attempts POST with spoofed write/admin headers
        configureRequest("POST", "fhir/Person");
        configureContainerPrincipal("read-only-attacker", HarmoniaRoleEnum.CLINICAL_READ.getRoleCode());

        headers.putSingle("x-harmonia-role", "CLINICAL_WRITE");
        headers.putSingle("x-harmonia-authorities", "clinical.create,clinical.update,clinical.delete");
        headers.putSingle("X-User-Roles", "CLINICAL_ADMIN");

        e2eFilter.filter(requestContext);

        ArgumentCaptor<Response> resCaptor = ArgumentCaptor.forClass(Response.class);
        verify(requestContext).abortWith(resCaptor.capture());
        assertThat(resCaptor.getValue().getStatus()).isEqualTo(403);
    }

    @Test
    @DisplayName("R8. Anti-spoofing: Unprivileged caller cannot gain read access via spoofed headers")
    void testUnprivilegedCallerCannotGainReadViaSpoofedHeaders() throws IOException {
        ThemisClinicalAuthorizationFilter e2eFilter = new ThemisClinicalAuthorizationFilter(new DefaultThemisAuthorizer());

        configureRequest("GET", "fhir/Person/123");
        configureContainerPrincipal("unprivileged-user"); // No container roles

        headers.putSingle("x-harmonia-role", "CLINICAL_READ");
        headers.putSingle("x-harmonia-authorities", "clinical.read");
        headers.putSingle("Authorization", "Bearer " + OidcTestTokenHelper.generateValidToken("spoofed"));

        e2eFilter.filter(requestContext);

        ArgumentCaptor<Response> resCaptor = ArgumentCaptor.forClass(Response.class);
        verify(requestContext).abortWith(resCaptor.capture());
        assertThat(resCaptor.getValue().getStatus()).isEqualTo(403);
    }

    @Test
    @DisplayName("R9. Unsupported HTTP method fails closed with 403 Forbidden")
    void testUnsupportedHttpMethodFailsClosed() throws IOException {
        ThemisClinicalAuthorizationFilter e2eFilter = new ThemisClinicalAuthorizationFilter(new DefaultThemisAuthorizer());

        configureRequest("TRACE", "fhir/Person/123");
        configureContainerPrincipal("admin-user", HarmoniaRoleEnum.CLINICAL_ADMIN.getRoleCode());

        e2eFilter.filter(requestContext);

        ArgumentCaptor<Response> resCaptor = ArgumentCaptor.forClass(Response.class);
        verify(requestContext).abortWith(resCaptor.capture());
        assertThat(resCaptor.getValue().getStatus()).isEqualTo(403);
    }

    // =========================================================================
    // S. Synchronous CDI Security Context Propagation Tests (Step 2)
    // =========================================================================

    @Test
    @DisplayName("S1. Authorized ingress populates ThemisSecurityContextProvider with canonical context")
    void testAuthorizedIngressPopulatesCdiContextProvider() throws IOException {
        ThemisSecurityContextProvider provider = new ThemisSecurityContextProvider();
        ThemisClinicalAuthorizationFilter authFilter = new ThemisClinicalAuthorizationFilter(mockAuthorizer, provider);

        configureRequest("GET", "fhir/Person/patient-101");
        configureContainerPrincipal("dr-alice", HarmoniaRoleEnum.CLINICAL_READ.getRoleCode());
        headers.putSingle("x-correlation-id", "corr-uuid-999");

        when(mockAuthorizer.authorize(any())).thenReturn(
                ThemisAuthorizationDecision.allow("clinical-policy-1", "corr-uuid-999", "Authorized")
        );

        authFilter.filter(requestContext);

        verify(requestContext, never()).abortWith(any());
        assertThat(provider.hasSecurityContext()).isTrue();

        ThemisSecurityContext secContext = provider.requireSecurityContext();
        assertThat(secContext.requestingPrincipal()).isNotNull();
        assertThat(secContext.requestingPrincipal().principalId()).isEqualTo("dr-alice");
        assertThat(secContext.requestingPrincipal().principalType()).isEqualTo(PrincipalType.HUMAN);
        assertThat(secContext.requestingPrincipal().sourceDomain()).isEqualTo("harmonia-clinical");

        assertThat(secContext.executingPrincipal()).isEqualTo(HarmoniaServiceIdentities.PRINCIPAL_IRIS_BEFE);
        assertThat(secContext.securityDomain()).isEqualTo("CLINICAL");
        assertThat(secContext.correlationId()).isEqualTo("corr-uuid-999");
        assertThat(secContext.requestedAt()).isNotNull();
        assertThat(secContext.authorities()).isNotEmpty();
    }

    @Test
    @DisplayName("S2. Denied request does not populate ThemisSecurityContextProvider")
    void testDeniedRequestDoesNotPopulateCdiContextProvider() throws IOException {
        ThemisSecurityContextProvider provider = new ThemisSecurityContextProvider();
        ThemisClinicalAuthorizationFilter authFilter = new ThemisClinicalAuthorizationFilter(mockAuthorizer, provider);

        configureRequest("DELETE", "fhir/Person/patient-101");
        configureContainerPrincipal("dr-alice", HarmoniaRoleEnum.CLINICAL_READ.getRoleCode());

        when(mockAuthorizer.authorize(any())).thenReturn(
                ThemisAuthorizationDecision.deny(ThemisDecisionReason.ACTION_NOT_PERMITTED, "policy", "corr-1", "Denied")
        );

        authFilter.filter(requestContext);

        verify(requestContext).abortWith(any());
        assertThat(provider.hasSecurityContext()).isFalse();
        assertThat(provider.getSecurityContext()).isEmpty();
    }

    @Test
    @DisplayName("S3. Unauthenticated request does not populate ThemisSecurityContextProvider")
    void testUnauthenticatedRequestDoesNotPopulateCdiContextProvider() throws IOException {
        ThemisSecurityContextProvider provider = new ThemisSecurityContextProvider();
        ThemisClinicalAuthorizationFilter authFilter = new ThemisClinicalAuthorizationFilter(mockAuthorizer, provider);

        configureRequest("GET", "fhir/Person/123");
        when(securityContext.getUserPrincipal()).thenReturn(null);

        authFilter.filter(requestContext);

        verify(requestContext).abortWith(any());
        assertThat(provider.hasSecurityContext()).isFalse();
        assertThat(provider.getSecurityContext()).isEmpty();
    }

    @Test
    @DisplayName("S4. Anti-spoofing: Spoofed identity headers do not taint ThemisSecurityContextProvider")
    void testSpoofedHeadersDoNotTaintCdiContextProvider() throws IOException {
        ThemisSecurityContextProvider provider = new ThemisSecurityContextProvider();
        ThemisClinicalAuthorizationFilter authFilter = new ThemisClinicalAuthorizationFilter(mockAuthorizer, provider);

        configureRequest("GET", "fhir/Person/patient-101");
        configureContainerPrincipal("dr-bob", HarmoniaRoleEnum.CLINICAL_READ.getRoleCode());

        headers.putSingle("x-harmonia-user", "root-admin");
        headers.putSingle("x-principal-id", "attacker");
        headers.putSingle("x-principal-type", "SYSTEM");
        headers.putSingle("x-harmonia-role", "SUPER_ADMIN");
        headers.putSingle("x-security-domain", "ADMIN");

        when(mockAuthorizer.authorize(any())).thenReturn(
                ThemisAuthorizationDecision.allow("clinical-policy-1", "corr-uuid-123", "Authorized")
        );

        authFilter.filter(requestContext);

        verify(requestContext, never()).abortWith(any());
        assertThat(provider.hasSecurityContext()).isTrue();

        ThemisSecurityContext secContext = provider.requireSecurityContext();
        assertThat(secContext.requestingPrincipal().principalId()).isEqualTo("dr-bob");
        assertThat(secContext.requestingPrincipal().principalType()).isEqualTo(PrincipalType.HUMAN);
        assertThat(secContext.executingPrincipal()).isEqualTo(HarmoniaServiceIdentities.PRINCIPAL_IRIS_BEFE);
        assertThat(secContext.securityDomain()).isEqualTo("CLINICAL");
    }
}
