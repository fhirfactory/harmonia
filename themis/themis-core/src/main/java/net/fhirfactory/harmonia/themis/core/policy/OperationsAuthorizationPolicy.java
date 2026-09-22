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
import net.fhirfactory.harmonia.themis.api.model.ThemisResource;
import net.fhirfactory.harmonia.themis.api.policy.ThemisPolicy;
import net.fhirfactory.harmonia.themis.core.constants.HarmoniaSecurityConstants;

/**
 * Policy governing access to Operations resources and administrative endpoints.
 * Authorizes Operations domain actions using granular operations.* and system.integration authorities.
 * Strictly adheres to default-deny and fail-closed security invariants.
 */
public class OperationsAuthorizationPolicy implements ThemisPolicy {

    public static final String POLICY_ID = "operations-authorization-policy";
    public static final String DOMAIN = HarmoniaSecurityConstants.LABEL_OPERATIONS;
    public static final String TARGET_OPERATIONS_RESOURCE = "OperationsResource";

    public static final String AUTH_OPERATIONS_READ = HarmoniaSecurityConstants.AUTH_OPERATIONS_READ;
    public static final String AUTH_OPERATIONS_ADMIN = HarmoniaSecurityConstants.AUTH_OPERATIONS_ADMIN;
    public static final String AUTH_SYSTEM_INTEGRATION = HarmoniaSecurityConstants.AUTH_SYSTEM_INTEGRATION;

    @Override
    public String getPolicyId() {
        return POLICY_ID;
    }

    @Override
    public String getDescription() {
        return "Authorizes READ, SEARCH, and approved operational administration actions on Operations resources.";
    }

    @Override
    public int getOrder() {
        return 100;
    }

    @Override
    public boolean appliesTo(ThemisAuthorizationRequest request) {
        if (request == null || request.target() == null) {
            return false;
        }
        ThemisResource target = request.target();
        if (target.resourceType() == null || target.resourceType().isBlank()) {
            return false;
        }
        // If explicitly scoped to a different security domain, do not apply
        if (target.securityDomain() != null && !target.securityDomain().isBlank()
                && !DOMAIN.equalsIgnoreCase(target.securityDomain())) {
            return false;
        }
        return DOMAIN.equalsIgnoreCase(target.securityDomain())
                || target.hasSecurityLabel(DOMAIN)
                || TARGET_OPERATIONS_RESOURCE.equalsIgnoreCase(target.resourceType());
    }

    @Override
    public ThemisAuthorizationDecision evaluate(ThemisAuthorizationRequest request) {
        if (request == null) {
            return ThemisAuthorizationDecision.deny(
                    ThemisDecisionReason.CONTEXT_MALFORMED,
                    POLICY_ID,
                    null,
                    "Authorization request is null"
            );
        }

        String corrId = request.context() != null ? request.context().correlationId() : null;

        if (request.principal() == null || request.principal().principalId() == null || request.principal().principalId().isBlank()) {
            return ThemisAuthorizationDecision.deny(
                    ThemisDecisionReason.PRINCIPAL_MISSING,
                    POLICY_ID,
                    corrId,
                    "Principal identity is missing or blank"
            );
        }

        if (request.target() == null || request.target().resourceType() == null || request.target().resourceType().isBlank()) {
            return ThemisAuthorizationDecision.deny(
                    ThemisDecisionReason.CONTEXT_MALFORMED,
                    POLICY_ID,
                    corrId,
                    "Target resource is missing"
            );
        }

        if (!appliesTo(request)) {
            return ThemisAuthorizationDecision.deny(
                    ThemisDecisionReason.SECURITY_LABEL_NOT_PERMITTED,
                    POLICY_ID,
                    corrId,
                    "Target resource is not in the OPERATIONS security domain"
            );
        }

        if (request.action() == null) {
            return ThemisAuthorizationDecision.deny(
                    ThemisDecisionReason.ACTION_NOT_PERMITTED,
                    POLICY_ID,
                    corrId,
                    "Requested action is null"
            );
        }

        return switch (request.action()) {
            case READ, SEARCH -> {
                if (request.hasAuthority(AUTH_OPERATIONS_READ)
                        || request.hasAuthority(AUTH_OPERATIONS_ADMIN)
                        || request.hasAuthority(AUTH_SYSTEM_INTEGRATION)) {
                    yield ThemisAuthorizationDecision.allow(
                            POLICY_ID,
                            corrId,
                            "Authorized for Operations read/search"
                    );
                }
                yield ThemisAuthorizationDecision.deny(
                        ThemisDecisionReason.AUTHORITY_MISSING,
                        POLICY_ID,
                        corrId,
                        "Missing required authority for Operations read: " + AUTH_OPERATIONS_READ
                );
            }
            case EXECUTE, ADMINISTER -> {
                if (request.hasAuthority(AUTH_OPERATIONS_ADMIN)) {
                    yield ThemisAuthorizationDecision.allow(
                            POLICY_ID,
                            corrId,
                            "Authorized by " + AUTH_OPERATIONS_ADMIN
                    );
                }
                yield ThemisAuthorizationDecision.deny(
                        ThemisDecisionReason.AUTHORITY_MISSING,
                        POLICY_ID,
                        corrId,
                        "Missing required authority for Operations administration: " + AUTH_OPERATIONS_ADMIN
                );
            }
            case DELETE -> ThemisAuthorizationDecision.deny(
                    ThemisDecisionReason.ACTION_NOT_PERMITTED,
                    POLICY_ID,
                    corrId,
                    "DELETE action is not permitted on Operations resources"
            );
            default -> ThemisAuthorizationDecision.deny(
                    ThemisDecisionReason.ACTION_NOT_PERMITTED,
                    POLICY_ID,
                    corrId,
                    "Action [" + request.action() + "] is not permitted on Operations resources"
            );
        };
    }
}
