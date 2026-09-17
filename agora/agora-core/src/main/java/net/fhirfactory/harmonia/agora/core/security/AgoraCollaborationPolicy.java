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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.agora.core.security;

import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecisionReason;
import net.fhirfactory.harmonia.themis.api.policy.ThemisPolicy;

/**
 * Themis security policy governing Agora collaboration space, room, and membership operations.
 * Enforces DEFAULT-DENY semantics: only requests carrying explicit Agora or administrative
 * authorities are granted authorization.
 */
public class AgoraCollaborationPolicy implements ThemisPolicy {

    public static final String POLICY_ID = "agora-collaboration-policy";
    public static final String DOMAIN = "AGORA";

    // Standard Agora Authorities
    public static final String AUTH_AGORA_COLLABORATION = "agora.collaboration";
    public static final String AUTH_AGORA_ADMIN = "agora.admin";
    public static final String AUTH_AGORA_MEMBER = "agora.member";
    public static final String AUTH_AGORA_WILDCARD = "agora/*.*";

    // Harmonia Platform Administrative & Integration Authorities
    public static final String AUTH_SYSTEM_INTEGRATION = "system.integration";
    public static final String AUTH_SYSTEM_ADMIN = "system.admin";
    public static final String AUTH_GLOBAL_WILDCARD = "*";

    @Override
    public String getPolicyId() {
        return POLICY_ID;
    }

    @Override
    public String getDescription() {
        return "Authorizes Agora collaboration spaces, child rooms, identity provisioning, and membership reconciliation.";
    }

    @Override
    public int getOrder() {
        return 50; // Priority after system admin (10) and before default deny fallback
    }

    @Override
    public boolean appliesTo(ThemisAuthorizationRequest request) {
        if (request == null || request.target() == null) {
            return false;
        }

        boolean matchesDomain = DOMAIN.equalsIgnoreCase(request.target().securityDomain())
                || request.target().hasSecurityLabel(DOMAIN)
                || "MatrixRoom".equalsIgnoreCase(request.target().resourceType())
                || "MatrixSpace".equalsIgnoreCase(request.target().resourceType())
                || "AgoraCollaboration".equalsIgnoreCase(request.target().resourceType())
                || DOMAIN.equalsIgnoreCase(request.target().resourceType());

        boolean hasAgoraAuthority = request.hasAnyAuthority(
                AUTH_AGORA_COLLABORATION,
                AUTH_AGORA_ADMIN,
                AUTH_AGORA_MEMBER,
                AUTH_AGORA_WILDCARD
        );

        return matchesDomain || hasAgoraAuthority;
    }

    @Override
    public ThemisAuthorizationDecision evaluate(ThemisAuthorizationRequest request) {
        String corrId = (request != null && request.context() != null) ? request.context().correlationId() : null;

        if (request == null) {
            return ThemisAuthorizationDecision.deny(
                    ThemisDecisionReason.DEFAULT_DENY,
                    POLICY_ID,
                    corrId,
                    "Default deny: request is null"
            );
        }

        // 1. Super-admin & Wildcard authorities grant full access
        if (request.hasAuthority(AUTH_GLOBAL_WILDCARD)
                || request.hasAuthority(AUTH_SYSTEM_ADMIN)
                || request.hasAuthority(AUTH_AGORA_ADMIN)
                || request.hasAuthority(AUTH_AGORA_WILDCARD)) {
            return ThemisAuthorizationDecision.allow(
                    POLICY_ID,
                    corrId,
                    "Authorized by Agora administrative authority"
            );
        }

        // 2. Integration service authority grants processing & collaboration access
        if (request.hasAuthority(AUTH_SYSTEM_INTEGRATION)) {
            return ThemisAuthorizationDecision.allow(
                    POLICY_ID,
                    corrId,
                    "Authorized by system integration authority"
            );
        }

        // 3. Collaboration authority authorizes space/room lifecycle and member interactions
        if (request.hasAuthority(AUTH_AGORA_COLLABORATION)) {
            return ThemisAuthorizationDecision.allow(
                    POLICY_ID,
                    corrId,
                    "Authorized by Agora collaboration authority"
            );
        }

        // 4. Member authority authorizes room read/participation and membership
        if (request.hasAuthority(AUTH_AGORA_MEMBER)) {
            if (request.action() == null
                    || request.action() == net.fhirfactory.harmonia.themis.api.model.ThemisAction.READ
                    || request.action() == net.fhirfactory.harmonia.themis.api.model.ThemisAction.SEARCH
                    || request.action() == net.fhirfactory.harmonia.themis.api.model.ThemisAction.PROCESS
                    || request.action() == net.fhirfactory.harmonia.themis.api.model.ThemisAction.EXECUTE
                    || "MatrixRoom".equalsIgnoreCase(request.target().resourceType())) {
                return ThemisAuthorizationDecision.allow(
                        POLICY_ID,
                        corrId,
                        "Authorized by Agora member authority"
                );
            }
        }

        return ThemisAuthorizationDecision.deny(
                ThemisDecisionReason.AUTHORITY_MISSING,
                POLICY_ID,
                corrId,
                "Request lacks required Agora authority for action [" + request.action() + "]"
        );
    }
}
