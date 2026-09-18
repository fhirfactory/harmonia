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

package net.fhirfactory.harmonia.themis.core.policy;

import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecisionReason;
import net.fhirfactory.harmonia.themis.api.policy.ThemisPolicy;

/**
 * Policy governing read and search access to Provider Registry resources.
 */
public class ProviderRegistryReadPolicy implements ThemisPolicy {

    public static final String POLICY_ID = "provider-registry-read-policy";
    public static final String DOMAIN = "PROVIDER_REGISTRY";
    public static final String AUTH_PROVIDER_READ = "provider.read";
    public static final String AUTH_PROVIDER_SEARCH = "provider.search";
    public static final String AUTH_PROVIDER_ADMIN = "provider.admin";

    @Override
    public String getPolicyId() {
        return POLICY_ID;
    }

    @Override
    public String getDescription() {
        return "Authorizes READ and SEARCH interactions on Provider Registry resources.";
    }

    @Override
    public int getOrder() {
        return 100;
    }

    @Override
    public boolean appliesTo(ThemisAuthorizationRequest request) {
        if (request == null || request.target() == null || request.action() == null) {
            return false;
        }
        boolean matchesDomain = DOMAIN.equalsIgnoreCase(request.target().securityDomain())
                || request.target().hasSecurityLabel(DOMAIN);
        boolean matchesAction = request.action() == ThemisAction.READ || request.action() == ThemisAction.SEARCH;
        return matchesDomain && matchesAction;
    }

    @Override
    public ThemisAuthorizationDecision evaluate(ThemisAuthorizationRequest request) {
        String corrId = request.context() != null ? request.context().correlationId() : null;
        if (request.hasAuthority(AUTH_PROVIDER_ADMIN)) {
            return ThemisAuthorizationDecision.allow(POLICY_ID, corrId, "Authorized by provider.admin");
        }

        if (request.action() == ThemisAction.READ) {
            if (request.hasAuthority(AUTH_PROVIDER_READ)) {
                return ThemisAuthorizationDecision.allow(POLICY_ID, corrId, "Authorized by provider.read");
            } else {
                return ThemisAuthorizationDecision.deny(ThemisDecisionReason.AUTHORITY_MISSING, POLICY_ID, corrId, "Missing required authority: " + AUTH_PROVIDER_READ);
            }
        }

        if (request.action() == ThemisAction.SEARCH) {
            if (request.hasAuthority(AUTH_PROVIDER_SEARCH) || request.hasAuthority(AUTH_PROVIDER_READ)) {
                return ThemisAuthorizationDecision.allow(POLICY_ID, corrId, "Authorized for search");
            } else {
                return ThemisAuthorizationDecision.deny(ThemisDecisionReason.AUTHORITY_MISSING, POLICY_ID, corrId, "Missing required authority: " + AUTH_PROVIDER_SEARCH);
            }
        }

        return ThemisAuthorizationDecision.deny(ThemisDecisionReason.ACTION_NOT_PERMITTED, POLICY_ID, corrId, "Action not permitted by read policy");
    }
}
