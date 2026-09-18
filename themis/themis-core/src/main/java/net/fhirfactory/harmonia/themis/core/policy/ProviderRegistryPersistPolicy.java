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
 * Policy governing persistence-level operations (CREATE, UPDATE, DELETE) on Provider Registry entities.
 */
public class ProviderRegistryPersistPolicy implements ThemisPolicy {

    public static final String POLICY_ID = "provider-registry-persist-policy";
    public static final String DOMAIN = "PROVIDER_REGISTRY";
    public static final String AUTH_PROVIDER_RESOURCE_CREATE = "provider.resource.create";
    public static final String AUTH_PROVIDER_RESOURCE_UPDATE = "provider.resource.update";
    public static final String AUTH_PROVIDER_RESOURCE_DELETE = "provider.resource.delete";
    public static final String AUTH_PROVIDER_ADMIN = "provider.admin";

    @Override
    public String getPolicyId() {
        return POLICY_ID;
    }

    @Override
    public String getDescription() {
        return "Authorizes storage boundary state changes (CREATE, UPDATE, DELETE) on Provider Registry resources.";
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
        boolean matchesAction = request.action() == ThemisAction.CREATE
                || request.action() == ThemisAction.UPDATE
                || request.action() == ThemisAction.DELETE;
        return matchesDomain && matchesAction;
    }

    @Override
    public ThemisAuthorizationDecision evaluate(ThemisAuthorizationRequest request) {
        String corrId = request.context() != null ? request.context().correlationId() : null;
        if (request.hasAuthority(AUTH_PROVIDER_ADMIN)) {
            return ThemisAuthorizationDecision.allow(POLICY_ID, corrId, "Authorized by provider.admin");
        }

        if (request.action() == ThemisAction.CREATE) {
            if (request.hasAuthority(AUTH_PROVIDER_RESOURCE_CREATE)) {
                return ThemisAuthorizationDecision.allow(POLICY_ID, corrId, "Authorized by " + AUTH_PROVIDER_RESOURCE_CREATE);
            }
            return ThemisAuthorizationDecision.deny(ThemisDecisionReason.PERSISTENCE_AUTHORITY_MISSING, POLICY_ID, corrId, "Missing required persistence authority: " + AUTH_PROVIDER_RESOURCE_CREATE);
        }

        if (request.action() == ThemisAction.UPDATE) {
            if (request.hasAuthority(AUTH_PROVIDER_RESOURCE_UPDATE) || request.hasAuthority(AUTH_PROVIDER_RESOURCE_CREATE)) {
                return ThemisAuthorizationDecision.allow(POLICY_ID, corrId, "Authorized by " + AUTH_PROVIDER_RESOURCE_UPDATE);
            }
            return ThemisAuthorizationDecision.deny(ThemisDecisionReason.PERSISTENCE_AUTHORITY_MISSING, POLICY_ID, corrId, "Missing required persistence authority: " + AUTH_PROVIDER_RESOURCE_UPDATE);
        }

        if (request.action() == ThemisAction.DELETE) {
            if (request.hasAuthority(AUTH_PROVIDER_RESOURCE_DELETE)) {
                return ThemisAuthorizationDecision.allow(POLICY_ID, corrId, "Authorized by " + AUTH_PROVIDER_RESOURCE_DELETE);
            }
            return ThemisAuthorizationDecision.deny(ThemisDecisionReason.PERSISTENCE_AUTHORITY_MISSING, POLICY_ID, corrId, "Missing required persistence authority: " + AUTH_PROVIDER_RESOURCE_DELETE);
        }

        return ThemisAuthorizationDecision.deny(ThemisDecisionReason.ACTION_NOT_PERMITTED, POLICY_ID, corrId, "Action not permitted by persist policy");
    }
}
