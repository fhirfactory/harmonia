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

package net.fhirfactory.harmonia.themis.core.evaluator;

import net.fhirfactory.harmonia.themis.api.ThemisService;
import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecisionReason;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisResource;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import net.fhirfactory.harmonia.themis.api.policy.ThemisPolicy;
import net.fhirfactory.harmonia.themis.core.constants.HarmoniaSecurityConstants;
import net.fhirfactory.harmonia.themis.core.policy.AuditImmutabilityDenyPolicy;
import net.fhirfactory.harmonia.themis.core.policy.AuditReadPolicy;
import net.fhirfactory.harmonia.themis.core.policy.ClinicalAuthorizationPolicy;
import net.fhirfactory.harmonia.themis.core.policy.OperationsAuthorizationPolicy;
import net.fhirfactory.harmonia.themis.core.policy.ProviderRegistryPersistPolicy;
import net.fhirfactory.harmonia.themis.core.policy.ProviderRegistryProcessPolicy;
import net.fhirfactory.harmonia.themis.core.policy.ProviderRegistryReadPolicy;
import net.fhirfactory.harmonia.themis.core.policy.ProviderRegistrySubmitPolicy;
import net.fhirfactory.harmonia.themis.core.policy.SystemAdminPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Deterministic policy evaluation engine enforcing DEFAULT DENY.
 *
 * Evaluation Pipeline:
 * 1. Request Integrity Validation (fail-closed if malformed, null, or principal missing)
 * 2. Explicit Deny Policies (explicit denial takes precedence over allow)
 * 3. Priority-ordered Rule Matching (first ALLOW match grants authorization)
 * 4. Default Deny Fallback (rejection if no policy explicitly grants permission)
 */
public class DeterministicPolicyEvaluator implements ThemisService {

    private static final Logger LOG = LoggerFactory.getLogger(DeterministicPolicyEvaluator.class);

    private final List<ThemisPolicy> policies = new CopyOnWriteArrayList<>();

    public DeterministicPolicyEvaluator() {}

    public DeterministicPolicyEvaluator(List<ThemisPolicy> initialPolicies) {
        if (initialPolicies != null) {
            this.policies.addAll(initialPolicies);
            sortPolicies();
        }
    }

    public static DeterministicPolicyEvaluator create() {
        return new DeterministicPolicyEvaluator();
    }

    public static DeterministicPolicyEvaluator withDefaultPolicies() {
        DeterministicPolicyEvaluator evaluator = new DeterministicPolicyEvaluator();
        evaluator.registerPolicy(new AuditImmutabilityDenyPolicy());
        evaluator.registerPolicy(new SystemAdminPolicy());
        evaluator.registerPolicy(new ProviderRegistryReadPolicy());
        evaluator.registerPolicy(new ProviderRegistrySubmitPolicy());
        evaluator.registerPolicy(new ProviderRegistryProcessPolicy());
        evaluator.registerPolicy(new ProviderRegistryPersistPolicy());
        evaluator.registerPolicy(new AuditReadPolicy());
        evaluator.registerPolicy(new ClinicalAuthorizationPolicy());
        evaluator.registerPolicy(new OperationsAuthorizationPolicy());
        return evaluator;
    }

    @Override
    public void registerPolicy(ThemisPolicy policy) {
        Objects.requireNonNull(policy, "policy must not be null");
        unregisterPolicy(policy.getPolicyId());
        policies.add(policy);
        sortPolicies();
        LOG.debug("Registered Themis policy: {} (order: {})", policy.getPolicyId(), policy.getOrder());
    }

    @Override
    public void unregisterPolicy(String policyId) {
        if (policyId != null) {
            policies.removeIf(p -> p.getPolicyId().equalsIgnoreCase(policyId));
        }
    }

    @Override
    public List<ThemisPolicy> getRegisteredPolicies() {
        return Collections.unmodifiableList(new ArrayList<>(policies));
    }

    private void sortPolicies() {
        policies.sort(Comparator.comparingInt(ThemisPolicy::getOrder));
    }

    @Override
    public ThemisAuthorizationDecision authorize(ThemisAuthorizationRequest request) {
        String corrId = (request != null && request.context() != null) ? request.context().correlationId() : null;

        // Step 1: Fail-closed validation
        if (request == null) {
            LOG.warn("Themis authorization rejected: request is null [FAIL CLOSED]");
            return ThemisAuthorizationDecision.deny(ThemisDecisionReason.CONTEXT_MALFORMED, "evaluator", corrId, "Authorization request is null");
        }

        if (request.principal() == null || request.principal().principalId() == null || request.principal().principalId().isBlank()) {
            LOG.warn("Themis authorization rejected: principal or principalId is missing [FAIL CLOSED]");
            return ThemisAuthorizationDecision.deny(ThemisDecisionReason.PRINCIPAL_MISSING, "evaluator", corrId, "Principal identity is missing or blank");
        }

        if (request.action() == null) {
            LOG.warn("Themis authorization rejected for principal [{}]: action is null [FAIL CLOSED]", request.principal().principalId());
            return ThemisAuthorizationDecision.deny(ThemisDecisionReason.ACTION_NOT_PERMITTED, "evaluator", corrId, "Requested action is null");
        }

        if (request.target() == null || request.target().resourceType() == null || request.target().resourceType().isBlank()) {
            LOG.warn("Themis authorization rejected for principal [{}]: target resource is missing [FAIL CLOSED]", request.principal().principalId());
            return ThemisAuthorizationDecision.deny(ThemisDecisionReason.CONTEXT_MALFORMED, "evaluator", corrId, "Target resource or resource type is missing");
        }

        // Step 2: Evaluate Explicit Deny Policies
        for (ThemisPolicy policy : policies) {
            if (policy.isExplicitDeny() && policy.appliesTo(request)) {
                ThemisAuthorizationDecision decision = policy.evaluate(request);
                if (decision != null && decision.isDenied()) {
                    LOG.info("Themis authorization denied by explicit deny policy [{}] for principal [{}]", policy.getPolicyId(), request.principal().principalId());
                    return decision;
                }
            }
        }

        // Step 3: Evaluate Ordered Matching Policies
        ThemisAuthorizationDecision mostSpecificDenial = null;
        for (ThemisPolicy policy : policies) {
            if (!policy.isExplicitDeny() && policy.appliesTo(request)) {
                ThemisAuthorizationDecision decision = policy.evaluate(request);
                if (decision != null) {
                    if (decision.isAllowed()) {
                        LOG.debug("Themis authorization granted by policy [{}] for principal [{}]", policy.getPolicyId(), request.principal().principalId());
                        return decision;
                    } else {
                        if (mostSpecificDenial == null || decision.reason() != ThemisDecisionReason.DEFAULT_DENY) {
                            mostSpecificDenial = decision;
                        }
                    }
                }
            }
        }

        // Step 4: Fallback to Most Specific Denial or Default Deny
        if (mostSpecificDenial != null) {
            LOG.info("Themis authorization denied by policy [{}] with reason [{}] for principal [{}]",
                    mostSpecificDenial.policyId(), mostSpecificDenial.reason(), request.principal().principalId());
            return mostSpecificDenial;
        }

        LOG.info("Themis authorization denied: no matching policy allowed action [{}] on target [{}] for principal [{}] [DEFAULT DENY]",
                request.action(), request.target().resourceType(), request.principal().principalId());
        return ThemisAuthorizationDecision.defaultDeny(corrId,
                String.format("Action [%s] on resource [%s] denied by default-deny rule", request.action(), request.target().resourceType()));
    }

    @Override
    public ThemisAuthorizationDecision authorizeAsyncExecution(
            ThemisPrincipal originatingPrincipal,
            Set<ThemisAuthority> originatingAuthorities,
            ThemisPrincipal executionPrincipal,
            Set<ThemisAuthority> executionAuthorities,
            ThemisAction action,
            ThemisResource target,
            ThemisSecurityContext context
    ) {
        String corrId = context != null ? context.correlationId() : null;

        // Fail-closed checks on principals
        if (originatingPrincipal == null || originatingPrincipal.principalId() == null || originatingPrincipal.principalId().isBlank()) {
            return ThemisAuthorizationDecision.deny(
                    ThemisDecisionReason.PRINCIPAL_MISSING,
                    "async-evaluator",
                    corrId,
                    "Originating principal is missing or invalid"
            );
        }

        if (executionPrincipal == null || executionPrincipal.principalId() == null || executionPrincipal.principalId().isBlank()) {
            return ThemisAuthorizationDecision.deny(
                    ThemisDecisionReason.PRINCIPAL_MISSING,
                    "async-evaluator",
                    corrId,
                    "Execution principal is missing or invalid"
            );
        }

        // Check 1: Originating caller authority verification (Prevent Privilege Amplification)
        boolean hasAdmin = (originatingAuthorities != null && originatingAuthorities.stream().anyMatch(a ->
                a.authorityCode().equalsIgnoreCase(HarmoniaSecurityConstants.AUTH_PROVIDER_ADMIN)
                        || a.authorityCode().equalsIgnoreCase(HarmoniaSecurityConstants.AUTH_SYSTEM_ADMIN)
                        || a.authorityCode().equals("*")
        ));

        boolean hasSubmit = (originatingAuthorities != null && originatingAuthorities.stream().anyMatch(a ->
                a.authorityCode().equalsIgnoreCase(HarmoniaSecurityConstants.AUTH_PROVIDER_CHANGE_SUBMIT)
        ));

        if (!hasAdmin && !hasSubmit) {
            return ThemisAuthorizationDecision.deny(
                    ThemisDecisionReason.AUTHORITY_MISSING,
                    "async-evaluator",
                    corrId,
                    "Originating caller lacks required submission authority [provider.change.submit]"
            );
        }

        // Check 2: Ergon execution authority verification
        boolean execHasAdmin = (executionAuthorities != null && executionAuthorities.stream().anyMatch(a ->
                a.authorityCode().equalsIgnoreCase(HarmoniaSecurityConstants.AUTH_PROVIDER_ADMIN)
                        || a.authorityCode().equalsIgnoreCase(HarmoniaSecurityConstants.AUTH_SYSTEM_ADMIN)
                        || a.authorityCode().equals("*")
        ));

        boolean execHasProcess = (executionAuthorities != null && executionAuthorities.stream().anyMatch(a ->
                a.authorityCode().equalsIgnoreCase(HarmoniaSecurityConstants.AUTH_PROVIDER_CHANGE_PROCESS)
        ));

        if (!execHasAdmin && !execHasProcess) {
            return ThemisAuthorizationDecision.deny(
                    ThemisDecisionReason.EXECUTION_AUTHORITY_MISSING,
                    "async-evaluator",
                    corrId,
                    "Execution actor lacks required execution authority [provider.change.process]"
            );
        }

        // Check 3: Evaluate target policy against execution request
        ThemisAuthorizationRequest execRequest = new ThemisAuthorizationRequest(
                executionPrincipal,
                executionAuthorities,
                action,
                target,
                context
        );

        return authorize(execRequest);
    }
}
