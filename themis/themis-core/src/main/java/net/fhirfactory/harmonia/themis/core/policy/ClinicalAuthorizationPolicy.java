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
import net.fhirfactory.harmonia.themis.core.constants.HarmoniaSecurityConstants;

/**
 * Policy governing access to Clinical FHIR resources.
 * Authorizes Clinical domain actions using granular clinical.* authorities.
 * Strictly adheres to default-deny and fail-closed security invariants.
 */
public class ClinicalAuthorizationPolicy implements ThemisPolicy {

    public static final String POLICY_ID = "clinical-authorization-policy";
    public static final String DOMAIN = HarmoniaSecurityConstants.LABEL_CLINICAL;

    public static final String AUTH_CLINICAL_READ = HarmoniaSecurityConstants.AUTH_CLINICAL_READ;
    public static final String AUTH_CLINICAL_SEARCH = HarmoniaSecurityConstants.AUTH_CLINICAL_SEARCH;
    public static final String AUTH_CLINICAL_CREATE = HarmoniaSecurityConstants.AUTH_CLINICAL_CREATE;
    public static final String AUTH_CLINICAL_UPDATE = HarmoniaSecurityConstants.AUTH_CLINICAL_UPDATE;
    public static final String AUTH_CLINICAL_ADMIN = HarmoniaSecurityConstants.AUTH_CLINICAL_ADMIN;

    @Override
    public String getPolicyId() {
        return POLICY_ID;
    }

    @Override
    public String getDescription() {
        return "Authorizes READ, SEARCH, CREATE, UPDATE, and administrative operations on Clinical resources.";
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
        return DOMAIN.equalsIgnoreCase(request.target().securityDomain())
                || request.target().hasSecurityLabel(DOMAIN);
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

        boolean matchesDomain = DOMAIN.equalsIgnoreCase(request.target().securityDomain())
                || request.target().hasSecurityLabel(DOMAIN);
        if (!matchesDomain) {
            return ThemisAuthorizationDecision.deny(
                    ThemisDecisionReason.SECURITY_LABEL_NOT_PERMITTED,
                    POLICY_ID,
                    corrId,
                    "Target resource is not in the CLINICAL security domain"
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
            case READ -> {
                if (request.hasAuthority(AUTH_CLINICAL_READ)) {
                    yield ThemisAuthorizationDecision.allow(POLICY_ID, corrId, "Authorized by " + AUTH_CLINICAL_READ);
                }
                yield ThemisAuthorizationDecision.deny(
                        ThemisDecisionReason.AUTHORITY_MISSING,
                        POLICY_ID,
                        corrId,
                        "Missing required authority: " + AUTH_CLINICAL_READ
                );
            }
            case SEARCH -> {
                if (request.hasAuthority(AUTH_CLINICAL_SEARCH)) {
                    yield ThemisAuthorizationDecision.allow(POLICY_ID, corrId, "Authorized by " + AUTH_CLINICAL_SEARCH);
                }
                yield ThemisAuthorizationDecision.deny(
                        ThemisDecisionReason.AUTHORITY_MISSING,
                        POLICY_ID,
                        corrId,
                        "Missing required authority: " + AUTH_CLINICAL_SEARCH
                );
            }
            case CREATE -> {
                if (request.hasAuthority(AUTH_CLINICAL_CREATE)) {
                    yield ThemisAuthorizationDecision.allow(POLICY_ID, corrId, "Authorized by " + AUTH_CLINICAL_CREATE);
                }
                yield ThemisAuthorizationDecision.deny(
                        ThemisDecisionReason.AUTHORITY_MISSING,
                        POLICY_ID,
                        corrId,
                        "Missing required authority: " + AUTH_CLINICAL_CREATE
                );
            }
            case UPDATE -> {
                if (request.hasAuthority(AUTH_CLINICAL_UPDATE)) {
                    yield ThemisAuthorizationDecision.allow(POLICY_ID, corrId, "Authorized by " + AUTH_CLINICAL_UPDATE);
                }
                yield ThemisAuthorizationDecision.deny(
                        ThemisDecisionReason.AUTHORITY_MISSING,
                        POLICY_ID,
                        corrId,
                        "Missing required authority: " + AUTH_CLINICAL_UPDATE
                );
            }
            case ADMINISTER -> {
                if (request.hasAuthority(AUTH_CLINICAL_ADMIN)) {
                    yield ThemisAuthorizationDecision.allow(POLICY_ID, corrId, "Authorized by " + AUTH_CLINICAL_ADMIN);
                }
                yield ThemisAuthorizationDecision.deny(
                        ThemisDecisionReason.AUTHORITY_MISSING,
                        POLICY_ID,
                        corrId,
                        "Missing required authority: " + AUTH_CLINICAL_ADMIN
                );
            }
            case DELETE -> ThemisAuthorizationDecision.deny(
                    ThemisDecisionReason.ACTION_NOT_PERMITTED,
                    POLICY_ID,
                    corrId,
                    "DELETE action is not permitted on Clinical resources"
            );
            default -> ThemisAuthorizationDecision.deny(
                    ThemisDecisionReason.ACTION_NOT_PERMITTED,
                    POLICY_ID,
                    corrId,
                    "Action [" + request.action() + "] not permitted on Clinical resources"
            );
        };
    }
}
