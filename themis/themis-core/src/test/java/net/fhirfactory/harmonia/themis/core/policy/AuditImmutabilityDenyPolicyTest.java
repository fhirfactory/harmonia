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
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisResource;
import net.fhirfactory.harmonia.themis.api.model.ThemisRole;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityLabel;
import net.fhirfactory.harmonia.themis.core.constants.HarmoniaSecurityConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AuditImmutabilityDenyPolicy Tests")
class AuditImmutabilityDenyPolicyTest {

    private AuditImmutabilityDenyPolicy policy;
    private ThemisPrincipal testHuman;
    private ThemisResource auditResource;
    private ThemisResource auditLabelResource;
    private ThemisResource clinicalResource;
    private ThemisResource operationsResource;
    private ThemisResource providerResource;

    @BeforeEach
    void setUp() {
        policy = new AuditImmutabilityDenyPolicy();
        testHuman = ThemisPrincipal.human("user:auditor");
        auditResource = ThemisResource.of("AuditEvent", "audit-123", "AUDIT", Set.of());
        auditLabelResource = ThemisResource.of("AuditEvent", "audit-456", "SOME_DOMAIN", Set.of(ThemisSecurityLabel.of("AUDIT")));
        clinicalResource = ThemisResource.of("Patient", "patient-123", "CLINICAL", Set.of(ThemisSecurityLabel.of("CLINICAL")));
        operationsResource = ThemisResource.of("OperationsResource", "/ops/1", "OPERATIONS", Set.of(ThemisSecurityLabel.of("OPERATIONS")));
        providerResource = ThemisResource.of("Practitioner", "prv-1", "PROVIDER_REGISTRY", Set.of(ThemisSecurityLabel.of("PROVIDER_REGISTRY")));
    }

    @Nested
    @DisplayName("Policy Metadata and Contract")
    class MetadataTests {

        @Test
        @DisplayName("Policy ID is audit-immutability-deny-policy")
        void testPolicyId() {
            assertThat(policy.getPolicyId()).isEqualTo("audit-immutability-deny-policy");
        }

        @Test
        @DisplayName("Order is 10")
        void testOrder() {
            assertThat(policy.getOrder()).isEqualTo(10);
        }

        @Test
        @DisplayName("isExplicitDeny is true")
        void testIsExplicitDeny() {
            assertThat(policy.isExplicitDeny()).isTrue();
        }

        @Test
        @DisplayName("Description is defined")
        void testDescription() {
            assertThat(policy.getDescription()).isNotBlank();
        }
    }

    @Nested
    @DisplayName("appliesTo Target and Action Matching")
    class AppliesToTests {

        @Test
        @DisplayName("appliesTo is true for AUDIT domain on CREATE, UPDATE, DELETE")
        void testAppliesToMutatingActionsOnAuditDomain() {
            ThemisAuthorizationRequest createReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).action(ThemisAction.CREATE).target(auditResource).build();
            ThemisAuthorizationRequest updateReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).action(ThemisAction.UPDATE).target(auditResource).build();
            ThemisAuthorizationRequest deleteReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).action(ThemisAction.DELETE).target(auditResource).build();

            assertThat(policy.appliesTo(createReq)).isTrue();
            assertThat(policy.appliesTo(updateReq)).isTrue();
            assertThat(policy.appliesTo(deleteReq)).isTrue();
        }

        @Test
        @DisplayName("appliesTo is true for AUDIT label on CREATE, UPDATE, DELETE")
        void testAppliesToMutatingActionsOnAuditLabel() {
            ThemisAuthorizationRequest createReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).action(ThemisAction.CREATE).target(auditLabelResource).build();
            ThemisAuthorizationRequest updateReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).action(ThemisAction.UPDATE).target(auditLabelResource).build();
            ThemisAuthorizationRequest deleteReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).action(ThemisAction.DELETE).target(auditLabelResource).build();

            assertThat(policy.appliesTo(createReq)).isTrue();
            assertThat(policy.appliesTo(updateReq)).isTrue();
            assertThat(policy.appliesTo(deleteReq)).isTrue();
        }

        @Test
        @DisplayName("appliesTo is false for non-mutating actions on AUDIT resource (READ, SEARCH)")
        void testAppliesToNonMutatingActionsOnAudit() {
            ThemisAuthorizationRequest readReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).action(ThemisAction.READ).target(auditResource).build();
            ThemisAuthorizationRequest searchReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).action(ThemisAction.SEARCH).target(auditResource).build();
            ThemisAuthorizationRequest execReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).action(ThemisAction.EXECUTE).target(auditResource).build();

            assertThat(policy.appliesTo(readReq)).isFalse();
            assertThat(policy.appliesTo(searchReq)).isFalse();
            assertThat(policy.appliesTo(execReq)).isFalse();
        }

        @Test
        @DisplayName("appliesTo is false for other domains (CLINICAL, OPERATIONS, PROVIDER_REGISTRY)")
        void testAppliesToOtherDomains() {
            ThemisAuthorizationRequest clinicalCreate = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).action(ThemisAction.CREATE).target(clinicalResource).build();
            ThemisAuthorizationRequest opsUpdate = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).action(ThemisAction.UPDATE).target(operationsResource).build();
            ThemisAuthorizationRequest prvDelete = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).action(ThemisAction.DELETE).target(providerResource).build();

            assertThat(policy.appliesTo(clinicalCreate)).isFalse();
            assertThat(policy.appliesTo(opsUpdate)).isFalse();
            assertThat(policy.appliesTo(prvDelete)).isFalse();
        }

        @Test
        @DisplayName("appliesTo handles null request, null target, and null action safely")
        void testAppliesToNullHandling() {
            assertThat(policy.appliesTo(null)).isFalse();

            ThemisAuthorizationRequest nullTarget = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).action(ThemisAction.CREATE).build();
            assertThat(policy.appliesTo(nullTarget)).isFalse();

            ThemisAuthorizationRequest nullAction = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).target(auditResource).build();
            assertThat(policy.appliesTo(nullAction)).isFalse();
        }
    }

    @Nested
    @DisplayName("evaluate Immutability Denial across Caller Roles")
    class EvaluateTests {

        @Test
        @DisplayName("evaluate returns ACTION_NOT_PERMITTED denial with correlation ID propagated")
        void testEvaluateDecisionDetails() {
            ThemisSecurityContext context = ThemisSecurityContext.builder()
                    .correlationId("corr-audit-001")
                    .securityDomain("AUDIT")
                    .build();
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .action(ThemisAction.CREATE)
                    .target(auditResource)
                    .context(context)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);

            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.policyId()).isEqualTo(AuditImmutabilityDenyPolicy.POLICY_ID);
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.ACTION_NOT_PERMITTED);
            assertThat(decision.correlationId()).isEqualTo("corr-audit-001");
            assertThat(decision.message()).contains("External mutation of AUDIT records is strictly prohibited");
        }

        @Test
        @DisplayName("SYS_ADM (system.admin) is denied when evaluated against immutability policy")
        void testSystemAdminDenied() {
            ThemisRole role = HarmoniaSecurityConstants.SYS_ADM;
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authorities(role.authorities())
                    .action(ThemisAction.DELETE)
                    .target(auditResource)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.ACTION_NOT_PERMITTED);
        }

        @Test
        @DisplayName("AUD_RDR (audit.read) is denied mutation when evaluated against immutability policy")
        void testAuditReaderDeniedMutation() {
            ThemisRole role = HarmoniaSecurityConstants.AUD_RDR;
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authorities(role.authorities())
                    .action(ThemisAction.CREATE)
                    .target(auditResource)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.ACTION_NOT_PERMITTED);
        }

        @Test
        @DisplayName("Clinical and Operations roles are denied mutation when evaluated against immutability policy")
        void testOtherRolesDeniedMutation() {
            for (ThemisRole role : Set.of(
                    HarmoniaSecurityConstants.CLINICAL_WRITE,
                    HarmoniaSecurityConstants.CLINICAL_ADMIN,
                    HarmoniaSecurityConstants.OPS_ADM,
                    HarmoniaSecurityConstants.PRV_ADM
            )) {
                ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                        .principal(testHuman)
                        .authorities(role.authorities())
                        .action(ThemisAction.UPDATE)
                        .target(auditResource)
                        .build();

                ThemisAuthorizationDecision decision = policy.evaluate(request);
                assertThat(decision.isDenied()).isTrue();
                assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.ACTION_NOT_PERMITTED);
            }
        }
    }
}
