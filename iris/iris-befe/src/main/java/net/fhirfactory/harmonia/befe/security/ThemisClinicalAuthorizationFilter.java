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

package net.fhirfactory.harmonia.befe.security;

import ca.uhn.fhir.context.FhirContext;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import net.fhirfactory.harmonia.model.security.HarmoniaRoleEnum;
import net.fhirfactory.harmonia.model.security.HarmoniaSecurityLabelEnum;
import net.fhirfactory.harmonia.themis.api.ThemisAuthorizer;
import net.fhirfactory.harmonia.themis.api.model.*;
import net.fhirfactory.harmonia.themis.core.identities.HarmoniaServiceIdentities;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.OperationOutcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

/**
 * Shared JAX-RS request filter enforcing centralized Themis default-deny authorization
 * for the Iris BEFE Clinical FHIR REST API under /api/fhir/*.
 *
 * Enforces Invariant 6 (Default-Deny Security Governance).
 * Ensures no FHIR Resource method can execute without a positive Themis authorization decision.
 *
 * Authentication & Authorization Boundary:
 * - Authentication is managed upstream at the WildFly container layer via elytron-oidc-client
 *   (configured via WEB-INF/web.xml and WEB-INF/oidc.json).
 * - Container authentication executes prior to JAX-RS filters, validating bearer JWT signatures,
 *   issuer, audience, and expiration, and populating SecurityContext.getUserPrincipal() with
 *   the validated subject identifier (sub claim).
 * - This filter consumes the container-authenticated principal as PrincipalType.HUMAN, performs
 *   defense-in-depth validation against missing/anonymous identities, and delegates authorization
 *   decisions to Themis (default-deny 403 Forbidden until clinical policies are provisioned in Task 03).
 * - Caller-supplied identity/role headers (e.g. X-Harmonia-*, X-Principal-Id) are strictly ignored.
 */
@Provider
@PreMatching
@Priority(Priorities.AUTHORIZATION)
@ApplicationScoped
public class ThemisClinicalAuthorizationFilter implements ContainerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(ThemisClinicalAuthorizationFilter.class);
    private static final String FHIR_PATH_PREFIX = "fhir";
    private static final String FHIR_JSON_MEDIA_TYPE = "application/fhir+json";
    private static final FhirContext FHIR_CONTEXT = FhirContext.forR5();

    public static final String ATTR_THEMIS_PRINCIPAL = "THEMIS_PRINCIPAL";
    public static final String ATTR_THEMIS_AUTHORITIES = "THEMIS_AUTHORITIES";
    public static final String ATTR_THEMIS_CONTEXT = "THEMIS_CONTEXT";
    public static final String ATTR_THEMIS_DECISION = "THEMIS_DECISION";

    @Inject
    private ThemisAuthorizer themisAuthorizer;

    @Inject
    private ThemisSecurityContextProvider contextProvider;

    public ThemisClinicalAuthorizationFilter() {
    }

    public ThemisClinicalAuthorizationFilter(ThemisAuthorizer themisAuthorizer) {
        this.themisAuthorizer = themisAuthorizer;
    }

    public ThemisClinicalAuthorizationFilter(ThemisAuthorizer themisAuthorizer, ThemisSecurityContextProvider contextProvider) {
        this.themisAuthorizer = themisAuthorizer;
        this.contextProvider = contextProvider;
    }

    public ThemisSecurityContextProvider getContextProvider() {
        return contextProvider;
    }

    public void setContextProvider(ThemisSecurityContextProvider contextProvider) {
        this.contextProvider = contextProvider;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (requestContext == null) {
            return;
        }

        String method = requestContext.getMethod();
        if ("OPTIONS".equalsIgnoreCase(method)) {
            // Allow CORS preflight requests to pass through to CORS filter
            return;
        }

        String path = extractRelativePath(requestContext);
        if (!isClinicalFhirPath(path)) {
            // Non-clinical endpoint: not governed by this clinical filter
            return;
        }

        Map<String, String> headers = normalizeHeaders(requestContext.getHeaders());
        String correlationId = headers.getOrDefault("x-correlation-id", UUID.randomUUID().toString());

        // 1. Path Parsing: Extract Resource Type and Resource ID
        String fhirSubPath = extractFhirSubPath(path);
        String[] pathSegments = parsePathSegments(fhirSubPath);
        String resourceType = pathSegments.length > 0 && !pathSegments[0].isBlank() ? pathSegments[0] : null;
        String resourceId = pathSegments.length > 1 && !pathSegments[1].isBlank() ? pathSegments[1] : null;

        // 2. Action Mapping
        ThemisAction action = mapMethodToAction(method, resourceId);

        // 3. Fail-closed if resourceType or action is missing
        if (StringUtils.isBlank(resourceType) || action == null) {
            log.warn("Clinical authorization DENIED: missing resource type or unsupported method [{}] on path [{}]", method, path);
            abortWithOutcome(requestContext, Response.Status.FORBIDDEN, ThemisDecisionReason.ACTION_NOT_PERMITTED,
                    "Invalid or unsupported clinical FHIR request");
            return;
        }

        // 4. Extract Principal from trusted container SecurityContext and Validate Authentication
        ThemisPrincipal principal = extractPrincipal(requestContext.getSecurityContext());
        if (principal == null || StringUtils.isBlank(principal.principalId())
                || "anonymous".equalsIgnoreCase(principal.principalId())
                || "system:anonymous".equalsIgnoreCase(principal.principalId())) {
            log.warn("Clinical authorization DENIED [401]: unauthenticated or invalid identity for path [{}]", path);
            abortWithOutcome(requestContext, Response.Status.UNAUTHORIZED, ThemisDecisionReason.PRINCIPAL_MISSING,
                    "Authentication required: principal is missing or invalid");
            return;
        }

        // 5. Extract Authorities from trusted SecurityContext / canonical roles
        Set<ThemisAuthority> authorities = extractAuthorities(requestContext.getSecurityContext(), principal);

        // 6. Build Themis Resource Context
        ThemisResource target = ThemisResource.builder()
                .resourceType(resourceType)
                .resourceId(resourceId)
                .securityDomain(HarmoniaSecurityLabelEnum.CLINICAL.getCode())
                .securityLabels(Set.of(HarmoniaSecurityLabelEnum.CLINICAL.toThemisLabel()))
                .build();

        // 7. Build Security Context and Authorization Request
        ThemisSecurityContext secContext = ThemisSecurityContext.builder()
                .requestingPrincipal(principal)
                .executingPrincipal(HarmoniaServiceIdentities.PRINCIPAL_IRIS_BEFE)
                .securityDomain(HarmoniaSecurityLabelEnum.CLINICAL.getCode())
                .authorities(authorities)
                .correlationId(correlationId)
                .requestedAt(Instant.now())
                .build();
        ThemisAuthorizationRequest authRequest = ThemisAuthorizationRequest.builder()
                .principal(principal)
                .authorities(authorities)
                .action(action)
                .target(target)
                .context(secContext)
                .build();

        // 8. Evaluate Authorization via Themis
        ThemisAuthorizer authorizer = getAuthorizer();
        ThemisAuthorizationDecision decision = authorizer.authorize(authRequest);
        if (decision == null) {
            decision = ThemisAuthorizationDecision.defaultDeny(correlationId, "Authorizer returned null decision (default deny)");
        }

        // 9. Enforce Decision
        if (decision.isDenied()) {
            log.warn("Themis clinical authorization DENIED for principal [{}] performing [{}] on [{}{}] - reason: {}, policy: {}",
                    principal.principalId(), action, resourceType, resourceId != null ? "/" + resourceId : "",
                    decision.reason(), decision.policyId());

            Response.Status status = (decision.reason() == ThemisDecisionReason.PRINCIPAL_MISSING)
                    ? Response.Status.UNAUTHORIZED
                    : Response.Status.FORBIDDEN;

            abortWithOutcome(requestContext, status, decision.reason(),
                    "Access denied: insufficient authorization for operation on " + resourceType);
            return;
        }

        // 10. Store Security Attributes for Downstream Processing
        requestContext.setProperty(ATTR_THEMIS_PRINCIPAL, principal);
        requestContext.setProperty(ATTR_THEMIS_AUTHORITIES, authorities);
        requestContext.setProperty(ATTR_THEMIS_CONTEXT, secContext);
        requestContext.setProperty(ATTR_THEMIS_DECISION, decision);

        if (contextProvider != null) {
            contextProvider.setSecurityContext(secContext);
        }

        log.debug("Themis clinical authorization GRANTED for principal [{}] performing [{}] on [{}{}]",
                principal.principalId(), action, resourceType, resourceId != null ? "/" + resourceId : "");
    }

    public ThemisAuthorizer getAuthorizer() {
        if (themisAuthorizer != null) {
            return themisAuthorizer;
        }
        return new DefaultThemisAuthorizer();
    }

    public ThemisAction mapMethodToAction(String httpMethod, String resourceId) {
        if (httpMethod == null) {
            return null;
        }
        return switch (httpMethod.trim().toUpperCase()) {
            case "GET", "HEAD" -> (resourceId != null && !resourceId.isBlank()) ? ThemisAction.READ : ThemisAction.SEARCH;
            case "POST" -> ThemisAction.CREATE;
            case "PUT", "PATCH" -> ThemisAction.UPDATE;
            case "DELETE" -> ThemisAction.DELETE;
            default -> null;
        };
    }

    public boolean isClinicalFhirPath(String path) {
        if (path == null) {
            return false;
        }
        String clean = path.startsWith("/") ? path.substring(1) : path;
        return clean.startsWith(FHIR_PATH_PREFIX + "/") || clean.equals(FHIR_PATH_PREFIX)
                || clean.startsWith("api/" + FHIR_PATH_PREFIX + "/") || clean.equals("api/" + FHIR_PATH_PREFIX);
    }

    public String extractRelativePath(ContainerRequestContext requestContext) {
        if (requestContext.getUriInfo() == null) {
            return "";
        }
        String path = requestContext.getUriInfo().getPath();
        if (StringUtils.isNotBlank(path)) {
            return path;
        }
        if (requestContext.getUriInfo().getRequestUri() != null) {
            return requestContext.getUriInfo().getRequestUri().getPath();
        }
        return "";
    }

    public String extractFhirSubPath(String path) {
        if (path == null) {
            return "";
        }
        String clean = path.startsWith("/") ? path.substring(1) : path;
        if (clean.startsWith("api/" + FHIR_PATH_PREFIX)) {
            clean = clean.substring(("api/" + FHIR_PATH_PREFIX).length());
        } else if (clean.startsWith(FHIR_PATH_PREFIX)) {
            clean = clean.substring(FHIR_PATH_PREFIX.length());
        }
        return clean.startsWith("/") ? clean.substring(1) : clean;
    }

    public String[] parsePathSegments(String subPath) {
        if (StringUtils.isBlank(subPath)) {
            return new String[0];
        }
        return Arrays.stream(subPath.split("/"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
    }

    public ThemisPrincipal extractPrincipal(SecurityContext securityContext) {
        if (securityContext != null && securityContext.getUserPrincipal() != null) {
            String name = securityContext.getUserPrincipal().getName();
            if (StringUtils.isNotBlank(name) && !"anonymous".equalsIgnoreCase(name) && !"system:anonymous".equalsIgnoreCase(name)) {
                return new ThemisPrincipal(name.trim(), PrincipalType.HUMAN, "harmonia-clinical", Map.of());
            }
        }
        return null;
    }

    public Set<ThemisAuthority> extractAuthorities(SecurityContext securityContext, ThemisPrincipal principal) {
        if (securityContext == null || principal == null) {
            return Collections.emptySet();
        }
        Set<ThemisAuthority> authorities = new HashSet<>();
        for (HarmoniaRoleEnum role : HarmoniaRoleEnum.values()) {
            if (securityContext.isUserInRole(role.getRoleCode()) || securityContext.isUserInRole("ROLE_" + role.getRoleCode())) {
                authorities.addAll(role.getThemisAuthorities());
                authorities.add(ThemisAuthority.of("role_" + role.getRoleCode().toLowerCase(Locale.ROOT)));
            }
        }
        return Collections.unmodifiableSet(authorities);
    }

    private Map<String, String> normalizeHeaders(MultivaluedMap<String, String> headers) {
        if (headers == null || headers.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> normalized = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                normalized.put(entry.getKey(), entry.getValue().get(0));
            }
        }
        return normalized;
    }

    private void abortWithOutcome(ContainerRequestContext requestContext, Response.Status status,
                                  ThemisDecisionReason reason, String diagnostics) {
        OperationOutcome outcome = new OperationOutcome();
        OperationOutcome.OperationOutcomeIssueComponent issue = outcome.addIssue();
        issue.setSeverity(OperationOutcome.IssueSeverity.ERROR);

        if (status == Response.Status.UNAUTHORIZED) {
            issue.setCode(OperationOutcome.IssueType.SECURITY);
        } else {
            issue.setCode(OperationOutcome.IssueType.FORBIDDEN);
        }

        // Strictly avoid PHI, sensitive tokens or credentials in diagnostics
        issue.setDiagnostics(diagnostics != null ? diagnostics : "Access Denied (" + reason + ")");

        String json = FHIR_CONTEXT.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);

        requestContext.abortWith(
                Response.status(status)
                        .type(FHIR_JSON_MEDIA_TYPE)
                        .entity(json)
                        .build()
        );
    }
}
