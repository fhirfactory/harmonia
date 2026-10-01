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

package net.fhirfactory.harmonia.hapifhir.controller.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.fhirfactory.harmonia.hapifhir.controller.dto.AuthoritativeVersionHelper;
import net.fhirfactory.harmonia.model.security.HarmoniaSecurityLabelEnum;
import net.fhirfactory.harmonia.themis.api.ThemisAuthorizer;
import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisResource;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import net.fhirfactory.harmonia.themis.core.evaluator.DeterministicPolicyEvaluator;
import net.fhirfactory.harmonia.themis.core.identities.HarmoniaServiceIdentities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.security.Principal;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Spring MVC security interceptor enforcing Themis default-deny authorization on
 * the dedicated authoritative route {@code /api/authoritative/fhir/**}.
 * <p>
 * Consumes trusted container/transport authentication ({@link HttpServletRequest#getUserPrincipal()})
 * and constructs canonical {@link ThemisSecurityContext} and {@link ThemisAuthorizationRequest}.
 * <p>
 * Fail-closed behavior:
 * <ul>
 *   <li>Missing/null/anonymous principal -> HTTP 401 Unauthorized</li>
 *   <li>Themis policy evaluation != ALLOW -> HTTP 403 Forbidden</li>
 * </ul>
 */
@Component
public class AuthoritativeSecurityInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuthoritativeSecurityInterceptor.class);

    private static final String API_PREFIX = "/api/authoritative/fhir/";

    private final ThemisAuthorizer themisAuthorizer;

    public AuthoritativeSecurityInterceptor() {
        this(DeterministicPolicyEvaluator.withDefaultPolicies());
    }

    @Autowired
    public AuthoritativeSecurityInterceptor(@Autowired(required = false) ThemisAuthorizer themisAuthorizer) {
        this.themisAuthorizer = themisAuthorizer != null
                ? themisAuthorizer
                : DeterministicPolicyEvaluator.withDefaultPolicies();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Principal userPrincipal = request.getUserPrincipal();

        // 1. Authenticated principal validation (Fail closed to 401 if missing or anonymous)
        if (userPrincipal == null || userPrincipal.getName() == null || userPrincipal.getName().isBlank()
                || "anonymous".equalsIgnoreCase(userPrincipal.getName().trim())
                || "anonymousUser".equalsIgnoreCase(userPrincipal.getName().trim())) {
            log.warn("Authoritative request rejected: unauthenticated caller (missing/anonymous principal) on {}", request.getRequestURI());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or unauthenticated principal");
            return false;
        }

        String principalName = userPrincipal.getName().trim();

        // 2. Resolve ThemisPrincipal and default authorities
        ThemisPrincipal requestingPrincipal;
        if (HarmoniaServiceIdentities.ID_MNEME.equalsIgnoreCase(principalName)) {
            requestingPrincipal = HarmoniaServiceIdentities.PRINCIPAL_MNEME;
        } else if (HarmoniaServiceIdentities.ID_MNEMOSYNE.equalsIgnoreCase(principalName)) {
            requestingPrincipal = HarmoniaServiceIdentities.PRINCIPAL_MNEMOSYNE;
        } else {
            requestingPrincipal = ThemisPrincipal.of(principalName, PrincipalType.SERVICE, principalName);
        }

        Set<ThemisAuthority> authorities = HarmoniaServiceIdentities.getAuthorities(principalName);

        // 3. Extract target resource information from URI path
        String path = request.getRequestURI();
        if (request.getContextPath() != null && !request.getContextPath().isBlank() && path.startsWith(request.getContextPath())) {
            path = path.substring(request.getContextPath().length());
        }

        String resourceType = "Unknown";
        String resourceId = "Unknown";
        int prefixIndex = path.indexOf(API_PREFIX);
        if (prefixIndex >= 0) {
            String subPath = path.substring(prefixIndex + API_PREFIX.length());
            String[] segments = subPath.split("/");
            if (segments.length >= 1 && !segments[0].isBlank()) {
                resourceType = segments[0].trim();
            }
            if (segments.length >= 2 && !segments[1].isBlank()) {
                resourceId = segments[1].trim();
            }
        }

        ThemisResource target = ThemisResource.of(
                resourceType,
                resourceId,
                HarmoniaSecurityLabelEnum.CLINICAL.getCode(),
                Set.of(HarmoniaSecurityLabelEnum.CLINICAL.toThemisSecurityLabel())
        );

        // 4. Map HTTP method & preconditions to ThemisAction
        String httpMethod = request.getMethod();
        ThemisAction action;
        if ("GET".equalsIgnoreCase(httpMethod) || "HEAD".equalsIgnoreCase(httpMethod)) {
            action = ThemisAction.READ;
        } else if ("PUT".equalsIgnoreCase(httpMethod)) {
            String ifNoneMatch = request.getHeader(AuthoritativeVersionHelper.HEADER_IF_NONE_MATCH);
            if (ifNoneMatch != null && !ifNoneMatch.isBlank()) {
                action = ThemisAction.CREATE;
            } else {
                action = ThemisAction.UPDATE;
            }
        } else {
            action = ThemisAction.EXECUTE;
        }

        // 5. Build ThemisSecurityContext
        ThemisSecurityContext context = new ThemisSecurityContext(
                requestingPrincipal,
                HarmoniaServiceIdentities.PRINCIPAL_MNEMOSYNE,
                HarmoniaSecurityLabelEnum.CLINICAL.getCode(),
                authorities,
                request.getHeader("X-Correlation-ID"),
                request.getHeader("X-Causation-ID"),
                null,
                request.getRemoteAddr(),
                Instant.now(),
                Map.of()
        );

        ThemisAuthorizationRequest authRequest = new ThemisAuthorizationRequest(
                requestingPrincipal,
                authorities,
                action,
                target,
                context
        );

        // 6. Evaluate policy
        ThemisAuthorizationDecision decision = themisAuthorizer.authorize(authRequest);

        if (!decision.isAllowed()) {
            log.warn("Authoritative request denied by Themis: principal={}, action={}, target={}, reason={}, message={}",
                    principalName, action, target, decision.reason(), decision.message());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied by Themis security policy: " + decision.reason());
            return false;
        }

        log.debug("Authoritative request authorized by Themis: principal={}, action={}, target={}",
                principalName, action, target);
        return true;
    }
}
