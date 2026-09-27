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
import jakarta.servlet.http.HttpServletRequest;
import net.fhirfactory.harmonia.model.security.HarmoniaAuthorityEnum;
import net.fhirfactory.harmonia.model.security.HarmoniaRoleEnum;
import net.fhirfactory.harmonia.model.security.HarmoniaSecurityLabelEnum;
import net.fhirfactory.harmonia.themis.api.ThemisService;
import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisResource;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import net.fhirfactory.harmonia.themis.core.evaluator.DeterministicPolicyEvaluator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Security interceptor and RBAC/ABAC evaluator for FHIR Provider Registry endpoints.
 * Translates incoming credentials, roles, and headers into Themis domain models
 * and enforces defence-in-depth authorization decisions via {@link ThemisService}.
 */
@Component
public class FhirSecurityInterceptor {

    private static final Logger log = LoggerFactory.getLogger(FhirSecurityInterceptor.class);

    public static final String HEADER_AUTH_TOKEN = "Authorization";
    public static final String HEADER_USER_ROLES = "X-User-Roles";
    public static final String HEADER_SECURITY_SCOPES = "X-Security-Scopes";
    public static final String HEADER_REQUESTER = "X-Requester";
    public static final String HEADER_PRINCIPAL_ID = "X-Principal-Id";
    public static final String HEADER_PRINCIPAL_TYPE = "X-Principal-Type";
    public static final String HEADER_SOURCE_DOMAIN = "X-Source-Domain";
    public static final String HEADER_SOURCE_SYSTEM = "X-Source-System";
    public static final String HEADER_CORRELATION_ID = "X-Correlation-Id";

    public static final String ATTR_THEMIS_PRINCIPAL = "THEMIS_PRINCIPAL";
    public static final String ATTR_THEMIS_AUTHORITIES = "THEMIS_AUTHORITIES";
    public static final String ATTR_THEMIS_CONTEXT = "THEMIS_CONTEXT";
    public static final String ATTR_THEMIS_DECISION = "THEMIS_DECISION";

    private final ThemisService themisService;

    public FhirSecurityInterceptor() {
        this(DeterministicPolicyEvaluator.withDefaultPolicies());
    }

    public FhirSecurityInterceptor(@Autowired(required = false) ThemisService themisService) {
        this.themisService = themisService != null ? themisService : DeterministicPolicyEvaluator.withDefaultPolicies();
    }

    public ThemisService getThemisService() {
        return themisService;
    }

    /**
     * Evaluates whether the request is authorized to execute the specified interaction on the given resource.
     *
     * @param resourceType FHIR resource type (e.g. "Practitioner", "Endpoint")
     * @param interaction  interaction type (e.g. "read", "search", "create.request", "update.request")
     * @param request      HTTP request containing authentication headers
     * @throws ForbiddenOperationException if authorization is denied
     * @throws AuthenticationException     if authentication is missing/invalid
     */
    public void authorize(String resourceType, String interaction, HttpServletRequest request) {
        // Allow capability metadata without restriction per FHIR R5 specification
        if ("metadata".equalsIgnoreCase(resourceType) || "CapabilityStatement".equalsIgnoreCase(resourceType)) {
            return;
        }

        ThemisPrincipal principal = extractPrincipal(request);
        if (principal == null) {
            throw new AuthenticationException("Trusted caller identity not established");
        }

        Set<ThemisAuthority> authorities = extractAuthorities(request);
        ThemisAction action = mapToAction(interaction);

        String correlationId = request != null ? request.getHeader(HEADER_CORRELATION_ID) : null;
        if (StringUtils.isBlank(correlationId)) {
            correlationId = UUID.randomUUID().toString();
        }

        ThemisSecurityContext context = ThemisSecurityContext.builder()
                .principal(principal)
                .correlationId(correlationId)
                .build();

        ThemisResource target = ThemisResource.builder()
                .resourceType(resourceType)
                .securityDomain(HarmoniaSecurityLabelEnum.PROVIDER_REGISTRY.getCode())
                .securityLabels(Set.of(
                        HarmoniaSecurityLabelEnum.PROVIDER_REGISTRY.toThemisLabel(),
                        HarmoniaSecurityLabelEnum.INTERNAL.toThemisLabel()
                ))
                .build();

        ThemisAuthorizationRequest authReq = ThemisAuthorizationRequest.builder()
                .principal(principal)
                .authorities(authorities)
                .action(action)
                .target(target)
                .context(context)
                .build();

        ThemisAuthorizationDecision decision = themisService.authorize(authReq);

        // Store resolved security attributes on HttpServletRequest for downstream use
        if (request != null) {
            request.setAttribute(ATTR_THEMIS_PRINCIPAL, principal);
            request.setAttribute(ATTR_THEMIS_AUTHORITIES, authorities);
            request.setAttribute(ATTR_THEMIS_CONTEXT, context);
            request.setAttribute(ATTR_THEMIS_DECISION, decision);
        }

        if (decision.decision() == ThemisDecision.DENY) {
            log.warn("Themis authorization DENIED for principal [{}] performing [{}] on [{}] (policy={}, reason={})",
                    principal.principalId(), action, resourceType, decision.policyId(), decision.reason());
            throw new ForbiddenOperationException("Access denied: missing required permission [" + resourceType + "." + interaction +
                    "] - Themis decision: " + decision.decision() + " (" + decision.reason() + ")");
        }

        log.debug("Themis authorization GRANTED for principal [{}] performing [{}] on [{}] (policy={})",
                principal.principalId(), action, resourceType, decision.policyId());
    }

    public ThemisPrincipal extractPrincipal(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        Principal userPrincipal = request.getUserPrincipal();
        if (userPrincipal == null || StringUtils.isBlank(userPrincipal.getName()) || "system:anonymous".equalsIgnoreCase(userPrincipal.getName().trim())) {
            return null;
        }

        String principalId = userPrincipal.getName().trim();
        PrincipalType principalType = resolvePrincipalType(principalId);

        String sourceDomain = request.getHeader(HEADER_SOURCE_DOMAIN);
        if (StringUtils.isBlank(sourceDomain)) {
            sourceDomain = request.getHeader(HEADER_SOURCE_SYSTEM);
        }
        if (StringUtils.isBlank(sourceDomain)) {
            sourceDomain = "pylai";
        }

        return ThemisPrincipal.of(principalId, principalType, sourceDomain);
    }

    private PrincipalType resolvePrincipalType(String principalId) {
        if (principalId.startsWith("service:")) {
            return PrincipalType.SERVICE;
        } else if (principalId.startsWith("system:")) {
            return PrincipalType.SYSTEM;
        } else if (principalId.startsWith("process:")) {
            return PrincipalType.PROCESS;
        } else {
            return PrincipalType.HUMAN;
        }
    }

    public Set<ThemisAuthority> extractAuthorities(HttpServletRequest request) {
        Set<ThemisAuthority> authorities = new HashSet<>();
        if (request == null || request.getUserPrincipal() == null) {
            return Collections.emptySet();
        }

        for (HarmoniaRoleEnum role : HarmoniaRoleEnum.values()) {
            if (request.isUserInRole(role.getRoleCode()) || request.isUserInRole("ROLE_" + role.getRoleCode())) {
                authorities.addAll(role.getThemisAuthorities());
            }
        }

        return Collections.unmodifiableSet(authorities);
    }

    public ThemisAction mapToAction(String interaction) {
        if (interaction == null) {
            return ThemisAction.READ;
        }
        return switch (interaction.toLowerCase().trim()) {
            case "read", "get" -> ThemisAction.READ;
            case "search", "find" -> ThemisAction.SEARCH;
            case "create.request", "create", "submit_create", "post" -> ThemisAction.SUBMIT_CREATE;
            case "update.request", "update", "submit_update", "put" -> ThemisAction.SUBMIT_UPDATE;
            case "process" -> ThemisAction.PROCESS;
            case "approve" -> ThemisAction.APPROVE;
            case "reject" -> ThemisAction.REJECT;
            case "delete" -> ThemisAction.DELETE;
            default -> ThemisAction.READ;
        };
    }
}
