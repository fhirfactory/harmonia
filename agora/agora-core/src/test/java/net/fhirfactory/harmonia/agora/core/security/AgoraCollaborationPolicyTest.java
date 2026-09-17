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

import net.fhirfactory.harmonia.themis.api.model.*;
import net.fhirfactory.harmonia.themis.core.evaluator.DeterministicPolicyEvaluator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AgoraCollaborationPolicy Themis Security Tests")
@Timeout(10)
class AgoraCollaborationPolicyTest {

    private final AgoraCollaborationPolicy policy = new AgoraCollaborationPolicy();

    @Test
    @DisplayName("Should allow request with agora.collaboration authority")
    void testAllowWithAgoraCollaborationAuthority() {
        ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                .principal(ThemisPrincipal.human("dr-alice"))
                .authority(ThemisAuthority.of(AgoraCollaborationPolicy.AUTH_AGORA_COLLABORATION))
                .action(ThemisAction.CREATE)
                .target(ThemisResource.of("PATIENT", "pat-123"))
                .build();

        assertThat(policy.appliesTo(request)).isTrue();
        ThemisAuthorizationDecision decision = policy.evaluate(request);
        assertThat(decision.isAllowed()).isTrue();
        assertThat(decision.policyId()).isEqualTo(AgoraCollaborationPolicy.POLICY_ID);
    }

    @Test
    @DisplayName("Should allow request with agora.admin authority")
    void testAllowWithAgoraAdminAuthority() {
        ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                .principal(ThemisPrincipal.service("service:agora"))
                .authority(ThemisAuthority.of(AgoraCollaborationPolicy.AUTH_AGORA_ADMIN))
                .action(ThemisAction.UPDATE)
                .target(ThemisResource.of("MatrixRoom", "!room:synapse"))
                .build();

        assertThat(policy.appliesTo(request)).isTrue();
        ThemisAuthorizationDecision decision = policy.evaluate(request);
        assertThat(decision.isAllowed()).isTrue();
    }

    @Test
    @DisplayName("Should allow request with agora.member authority")
    void testAllowWithAgoraMemberAuthority() {
        ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                .principal(ThemisPrincipal.human("nurse-bob"))
                .authority(ThemisAuthority.of(AgoraCollaborationPolicy.AUTH_AGORA_MEMBER))
                .action(ThemisAction.READ)
                .target(ThemisResource.of("MatrixRoom", "!room:synapse"))
                .build();

        assertThat(policy.appliesTo(request)).isTrue();
        ThemisAuthorizationDecision decision = policy.evaluate(request);
        assertThat(decision.isAllowed()).isTrue();
    }

    @Test
    @DisplayName("Should deny request without Agora authorities under default-deny")
    void testDenyWithoutAuthorities() {
        ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                .principal(ThemisPrincipal.human("unauthorized-user"))
                .action(ThemisAction.READ)
                .target(ThemisResource.of("MatrixRoom", "!room:synapse"))
                .build();

        assertThat(policy.appliesTo(request)).isTrue();
        ThemisAuthorizationDecision decision = policy.evaluate(request);
        assertThat(decision.isDenied()).isTrue();
        assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.AUTHORITY_MISSING);

        // When evaluated through DeterministicPolicyEvaluator with AgoraCollaborationPolicy registered
        DeterministicPolicyEvaluator evaluator = DeterministicPolicyEvaluator.withDefaultPolicies();
        evaluator.registerPolicy(new AgoraCollaborationPolicy());
        ThemisAuthorizationDecision evalDecision = evaluator.authorize(request);
        assertThat(evalDecision.isDenied()).isTrue();
    }

    @Test
    @DisplayName("Should integrate with DeterministicPolicyEvaluator default policies")
    void testIntegrationWithDeterministicPolicyEvaluator() {
        DeterministicPolicyEvaluator evaluator = DeterministicPolicyEvaluator.withDefaultPolicies();

        ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                .principal(ThemisPrincipal.human("dr-alice"))
                .authority(ThemisAuthority.of(AgoraCollaborationPolicy.AUTH_AGORA_COLLABORATION))
                .action(ThemisAction.CREATE)
                .target(ThemisResource.of("PATIENT", "pat-123"))
                .build();

        // Without AgoraCollaborationPolicy, default evaluator denies
        ThemisAuthorizationDecision initialDecision = evaluator.authorize(request);
        assertThat(initialDecision.isDenied()).isTrue();

        // Once AgoraCollaborationPolicy is registered, evaluator allows
        evaluator.registerPolicy(new AgoraCollaborationPolicy());
        ThemisAuthorizationDecision updatedDecision = evaluator.authorize(request);
        assertThat(updatedDecision.isAllowed()).isTrue();
    }
}
