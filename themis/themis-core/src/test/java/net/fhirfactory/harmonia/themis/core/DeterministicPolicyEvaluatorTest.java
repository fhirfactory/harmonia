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

package net.fhirfactory.harmonia.themis.core;

import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecisionReason;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisResource;
import net.fhirfactory.harmonia.themis.api.model.ThemisRole;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityLabel;
import net.fhirfactory.harmonia.themis.api.policy.ThemisPolicy;
import net.fhirfactory.harmonia.themis.core.constants.HarmoniaSecurityConstants;
import net.fhirfactory.harmonia.themis.core.evaluator.DeterministicPolicyEvaluator;
import net.fhirfactory.harmonia.themis.core.policy.AuditImmutabilityDenyPolicy;
import net.fhirfactory.harmonia.themis.core.policy.AuditReadPolicy;
import net.fhirfactory.harmonia.themis.core.policy.ClinicalAuthorizationPolicy;
import net.fhirfactory.harmonia.themis.core.policy.OperationsAuthorizationPolicy;
import net.fhirfactory.harmonia.themis.core.policy.SystemAdminPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DeterministicPolicyEvaluator Tests")
class DeterministicPolicyEvaluatorTest {

    private DeterministicPolicyEvaluator evaluator;
    private ThemisPrincipal testHuman;
    private ThemisPrincipal testService;
    private ThemisResource providerResource;
    private ThemisResource auditResource;
    private ThemisResource clinicalResource;
    private ThemisResource operationsResource;

    @BeforeEach
    void setUp() {
        evaluator = DeterministicPolicyEvaluator.withDefaultPolicies();
        testHuman = ThemisPrincipal.human("user:dr-alice");
        testService = ThemisPrincipal.service("service:pylai");
        providerResource = ThemisResource.of("Practitioner", "practitioner-123", "PROVIDER_REGISTRY", Set.of(ThemisSecurityLabel.of("PROVIDER_REGISTRY")));
        auditResource = ThemisResource.of("AuditEvent", "audit-456", "AUDIT", Set.of(ThemisSecurityLabel.of("AUDIT")));
        clinicalResource = ThemisResource.of("Patient", "patient-789", "CLINICAL", Set.of(ThemisSecurityLabel.of("CLINICAL")));
        operationsResource = ThemisResource.of("OperationsResource", "/api/operations/summary", "OPERATIONS", Set.of(ThemisSecurityLabel.of("OPERATIONS")));
    }

    @Nested
    @DisplayName("Fail-Closed and Input Validation")
    class FailClosedTests {

        @Test
        @DisplayName("Null request fails closed with CONTEXT_MALFORMED")
        void testNullRequest() {
            ThemisAuthorizationDecision decision = evaluator.authorize(null);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.CONTEXT_MALFORMED);
        }

        @Test
        @DisplayName("Missing principal fails closed with PRINCIPAL_MISSING")
        void testMissingPrincipal() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .action(ThemisAction.READ)
                    .target(providerResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.PRINCIPAL_MISSING);
        }

        @Test
        @DisplayName("Blank principal ID fails closed with PRINCIPAL_MISSING")
        void testBlankPrincipalId() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(ThemisPrincipal.human("   "))
                    .action(ThemisAction.READ)
                    .target(providerResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.PRINCIPAL_MISSING);
        }

        @Test
        @DisplayName("Missing target resource fails closed with CONTEXT_MALFORMED")
        void testMissingTarget() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .action(ThemisAction.READ)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.CONTEXT_MALFORMED);
        }

        @Test
        @DisplayName("Missing action fails closed with ACTION_NOT_PERMITTED")
        void testMissingAction() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .target(providerResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.ACTION_NOT_PERMITTED);
        }
    }

    @Nested
    @DisplayName("Provider Registry Policy Evaluation")
    class ProviderRegistryPolicyTests {

        @Test
        @DisplayName("READ allowed with provider.read authority")
        void testReadAllowed() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_PROVIDER_READ)
                    .action(ThemisAction.READ)
                    .target(providerResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isAllowed()).isTrue();
            assertThat(decision.policyId()).isEqualTo("provider-registry-read-policy");
        }

        @Test
        @DisplayName("READ denied when authority is missing")
        void testReadDeniedWithoutAuthority() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .action(ThemisAction.READ)
                    .target(providerResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.AUTHORITY_MISSING);
        }

        @Test
        @DisplayName("SUBMIT_UPDATE allowed with provider.change.submit")
        void testSubmitAllowed() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_PROVIDER_CHANGE_SUBMIT)
                    .action(ThemisAction.SUBMIT_UPDATE)
                    .target(providerResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isAllowed()).isTrue();
            assertThat(decision.policyId()).isEqualTo("provider-registry-submit-policy");
        }

        @Test
        @DisplayName("SUBMIT_UPDATE denied when submit authority is missing (e.g. reader only)")
        void testSubmitDeniedForReader() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_PROVIDER_READ)
                    .action(ThemisAction.SUBMIT_UPDATE)
                    .target(providerResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.AUTHORITY_MISSING);
        }

        @Test
        @DisplayName("PROCESS allowed with provider.change.process")
        void testProcessAllowed() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testService)
                    .authority(HarmoniaSecurityConstants.AUTH_PROVIDER_CHANGE_PROCESS)
                    .action(ThemisAction.PROCESS)
                    .target(providerResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isAllowed()).isTrue();
            assertThat(decision.policyId()).isEqualTo("provider-registry-process-policy");
        }

        @Test
        @DisplayName("Persistence UPDATE allowed with provider.resource.update")
        void testPersistUpdateAllowed() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testService)
                    .authority(HarmoniaSecurityConstants.AUTH_PROVIDER_RESOURCE_UPDATE)
                    .action(ThemisAction.UPDATE)
                    .target(providerResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isAllowed()).isTrue();
            assertThat(decision.policyId()).isEqualTo("provider-registry-persist-policy");
        }

        @Test
        @DisplayName("Persistence UPDATE denied when persistence authority is missing")
        void testPersistUpdateDeniedWithoutAuthority() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testService)
                    .authority(HarmoniaSecurityConstants.AUTH_PROVIDER_CHANGE_PROCESS) // has process, not persist
                    .action(ThemisAction.UPDATE)
                    .target(providerResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.PERSISTENCE_AUTHORITY_MISSING);
        }
    }

    @Nested
    @DisplayName("Clinical Policy Evaluation")
    class ClinicalPolicyTests {

        @Test
        @DisplayName("READ allowed with clinical.read")
        void testClinicalReadAllowed() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_CLINICAL_READ)
                    .action(ThemisAction.READ)
                    .target(clinicalResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isAllowed()).isTrue();
            assertThat(decision.policyId()).isEqualTo("clinical-authorization-policy");
        }

        @Test
        @DisplayName("SEARCH allowed with clinical.search")
        void testClinicalSearchAllowed() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_CLINICAL_SEARCH)
                    .action(ThemisAction.SEARCH)
                    .target(clinicalResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isAllowed()).isTrue();
            assertThat(decision.policyId()).isEqualTo("clinical-authorization-policy");
        }

        @Test
        @DisplayName("CREATE allowed with clinical.create")
        void testClinicalCreateAllowed() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_CLINICAL_CREATE)
                    .action(ThemisAction.CREATE)
                    .target(clinicalResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isAllowed()).isTrue();
            assertThat(decision.policyId()).isEqualTo("clinical-authorization-policy");
        }

        @Test
        @DisplayName("UPDATE allowed with clinical.update")
        void testClinicalUpdateAllowed() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_CLINICAL_UPDATE)
                    .action(ThemisAction.UPDATE)
                    .target(clinicalResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isAllowed()).isTrue();
            assertThat(decision.policyId()).isEqualTo("clinical-authorization-policy");
        }

        @Test
        @DisplayName("ADMINISTER allowed with clinical.admin")
        void testClinicalAdminAllowed() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_CLINICAL_ADMIN)
                    .action(ThemisAction.ADMINISTER)
                    .target(clinicalResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isAllowed()).isTrue();
            assertThat(decision.policyId()).isEqualTo("clinical-authorization-policy");
        }

        @Test
        @DisplayName("READ denied when authority is missing")
        void testClinicalReadDeniedWithoutAuthority() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .action(ThemisAction.READ)
                    .target(clinicalResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.AUTHORITY_MISSING);
        }

        @Test
        @DisplayName("DELETE denied even with clinical.admin")
        void testClinicalDeleteDenied() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_CLINICAL_ADMIN)
                    .action(ThemisAction.DELETE)
                    .target(clinicalResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.ACTION_NOT_PERMITTED);
        }

        @Test
        @DisplayName("Provider authority does not allow clinical READ")
        void testProviderAuthorityDoesNotAllowClinicalRead() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_PROVIDER_READ)
                    .action(ThemisAction.READ)
                    .target(clinicalResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.AUTHORITY_MISSING);
        }
    }

    @Nested
    @DisplayName("Explicit Deny Precedence")
    class ExplicitDenyTests {

        @Test
        @DisplayName("Explicit Deny policy overrides all matching Allow policies")
        void testExplicitDenyOverridesAllow() {
            ThemisPolicy explicitDenyPolicy = new ThemisPolicy() {
                @Override
                public String getPolicyId() {
                    return "explicit-deny-test";
                }

                @Override
                public String getDescription() {
                    return "Explicitly denies all actions for testing";
                }

                @Override
                public boolean appliesTo(ThemisAuthorizationRequest request) {
                    return true;
                }

                @Override
                public ThemisAuthorizationDecision evaluate(ThemisAuthorizationRequest request) {
                    return ThemisAuthorizationDecision.deny(ThemisDecisionReason.EXPLICIT_DENY, "explicit-deny-test", null, "Explicitly denied");
                }

                @Override
                public boolean isExplicitDeny() {
                    return true;
                }
            };

            evaluator.registerPolicy(explicitDenyPolicy);

            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_SYSTEM_ADMIN)
                    .action(ThemisAction.READ)
                    .target(providerResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.EXPLICIT_DENY);
            assertThat(decision.policyId()).isEqualTo("explicit-deny-test");
        }
    }

    @Nested
    @DisplayName("Dual-Authority Asynchronous Evaluation")
    class AsyncDualAuthorityTests {

        @Test
        @DisplayName("Async execution allowed when originating caller has submit AND executor has process")
        void testAsyncAllowed() {
            ThemisPrincipal submitter = ThemisPrincipal.human("user:bob");
            Set<ThemisAuthority> subAuthorities = Set.of(ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_PROVIDER_CHANGE_SUBMIT));

            ThemisPrincipal executor = ThemisPrincipal.process("ergon:practitioner-change");
            Set<ThemisAuthority> execAuthorities = Set.of(ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_PROVIDER_CHANGE_PROCESS));

            ThemisAuthorizationDecision decision = evaluator.authorizeAsyncExecution(
                    submitter,
                    subAuthorities,
                    executor,
                    execAuthorities,
                    ThemisAction.PROCESS,
                    providerResource,
                    ThemisSecurityContext.anonymous()
            );

            assertThat(decision.isAllowed()).isTrue();
        }

        @Test
        @DisplayName("Async execution denied when originating submitter authority is missing (privilege escalation prevention)")
        void testAsyncDeniedWhenOriginatingMissing() {
            ThemisPrincipal submitter = ThemisPrincipal.human("user:reader-only");
            Set<ThemisAuthority> subAuthorities = Set.of(ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_PROVIDER_READ));

            ThemisPrincipal executor = ThemisPrincipal.process("ergon:practitioner-change");
            Set<ThemisAuthority> execAuthorities = Set.of(ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_PROVIDER_CHANGE_PROCESS));

            ThemisAuthorizationDecision decision = evaluator.authorizeAsyncExecution(
                    submitter,
                    subAuthorities,
                    executor,
                    execAuthorities,
                    ThemisAction.PROCESS,
                    providerResource,
                    ThemisSecurityContext.anonymous()
            );

            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.AUTHORITY_MISSING);
        }

        @Test
        @DisplayName("Async execution denied when executor execution authority is missing (defence in depth)")
        void testAsyncDeniedWhenExecutorMissing() {
            ThemisPrincipal submitter = ThemisPrincipal.human("user:bob");
            Set<ThemisAuthority> subAuthorities = Set.of(ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_PROVIDER_CHANGE_SUBMIT));

            ThemisPrincipal executor = ThemisPrincipal.process("ergon:unauthorized-processor");
            Set<ThemisAuthority> execAuthorities = Set.of(ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_PROVIDER_READ));

            ThemisAuthorizationDecision decision = evaluator.authorizeAsyncExecution(
                    submitter,
                    subAuthorities,
                    executor,
                    execAuthorities,
                    ThemisAction.PROCESS,
                    providerResource,
                    ThemisSecurityContext.anonymous()
            );

            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.EXECUTION_AUTHORITY_MISSING);
        }
    }

    @Nested
    @DisplayName("Operations Authorization Policy Evaluation")
    class OperationsPolicyTests {

        @Test
        @DisplayName("operations.read grants READ on Operations resource via operations-authorization-policy")
        void testOperationsReadAllowed() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_OPERATIONS_READ)
                    .action(ThemisAction.READ)
                    .target(operationsResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isAllowed()).isTrue();
            assertThat(decision.policyId()).isEqualTo(OperationsAuthorizationPolicy.POLICY_ID);
        }

        @Test
        @DisplayName("operations.admin grants EXECUTE on Operations resource via operations-authorization-policy")
        void testOperationsAdminExecuteAllowed() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_OPERATIONS_ADMIN)
                    .action(ThemisAction.EXECUTE)
                    .target(operationsResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isAllowed()).isTrue();
            assertThat(decision.policyId()).isEqualTo(OperationsAuthorizationPolicy.POLICY_ID);
        }

        @Test
        @DisplayName("operations.read is denied on EXECUTE")
        void testOperationsReadExecuteDenied() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_OPERATIONS_READ)
                    .action(ThemisAction.EXECUTE)
                    .target(operationsResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.AUTHORITY_MISSING);
        }

        @Test
        @DisplayName("operations.read on Clinical resource is denied by DEFAULT_DENY")
        void testOperationsAuthorityCannotAccessClinical() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_OPERATIONS_READ)
                    .action(ThemisAction.READ)
                    .target(clinicalResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.AUTHORITY_MISSING);
            assertThat(decision.policyId()).isEqualTo(ClinicalAuthorizationPolicy.POLICY_ID);
        }

        @Test
        @DisplayName("system.admin on Operations resource is allowed via system-admin-policy")
        void testSystemAdminAllowsOperationsResource() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_SYSTEM_ADMIN)
                    .action(ThemisAction.READ)
                    .target(operationsResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isAllowed()).isTrue();
            assertThat(decision.policyId()).isEqualTo(SystemAdminPolicy.POLICY_ID);
        }

        @Test
        @DisplayName("system.integration on Operations resource is allowed for READ via operations-authorization-policy")
        void testSystemIntegrationAllowsOperationsResource() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testService)
                    .authority(HarmoniaSecurityConstants.AUTH_SYSTEM_INTEGRATION)
                    .action(ThemisAction.READ)
                    .target(operationsResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isAllowed()).isTrue();
            assertThat(decision.policyId()).isEqualTo(OperationsAuthorizationPolicy.POLICY_ID);
        }
    }

    @Nested
    @DisplayName("Audit Policy Evaluation & Explicit-Deny Immutability")
    class AuditPolicyTests {

        @Test
        @DisplayName("audit.read allows READ on AUDIT resource")
        void testAuditReadAllowed() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_AUDIT_READ)
                    .action(ThemisAction.READ)
                    .target(auditResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isAllowed()).isTrue();
            assertThat(decision.policyId()).isEqualTo(AuditReadPolicy.POLICY_ID);
        }

        @Test
        @DisplayName("audit.read allows SEARCH on AUDIT resource")
        void testAuditSearchAllowed() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_AUDIT_READ)
                    .action(ThemisAction.SEARCH)
                    .target(auditResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isAllowed()).isTrue();
            assertThat(decision.policyId()).isEqualTo(AuditReadPolicy.POLICY_ID);
        }

        @Test
        @DisplayName("system.admin allows READ on AUDIT resource")
        void testSystemAdminAuditReadAllowed() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_SYSTEM_ADMIN)
                    .action(ThemisAction.READ)
                    .target(auditResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isAllowed()).isTrue();
        }

        @Test
        @DisplayName("system.admin allows SEARCH on AUDIT resource")
        void testSystemAdminAuditSearchAllowed() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_SYSTEM_ADMIN)
                    .action(ThemisAction.SEARCH)
                    .target(auditResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isAllowed()).isTrue();
        }

        @Test
        @DisplayName("Explicit Deny: system.admin is DENIED CREATE on AUDIT resource")
        void testSystemAdminAuditCreateDeniedByExplicitDeny() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_SYSTEM_ADMIN)
                    .action(ThemisAction.CREATE)
                    .target(auditResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.ACTION_NOT_PERMITTED);
            assertThat(decision.policyId()).isEqualTo(AuditImmutabilityDenyPolicy.POLICY_ID);
        }

        @Test
        @DisplayName("Explicit Deny: system.admin is DENIED UPDATE on AUDIT resource")
        void testSystemAdminAuditUpdateDeniedByExplicitDeny() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_SYSTEM_ADMIN)
                    .action(ThemisAction.UPDATE)
                    .target(auditResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.ACTION_NOT_PERMITTED);
            assertThat(decision.policyId()).isEqualTo(AuditImmutabilityDenyPolicy.POLICY_ID);
        }

        @Test
        @DisplayName("Explicit Deny: system.admin is DENIED DELETE on AUDIT resource")
        void testSystemAdminAuditDeleteDeniedByExplicitDeny() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_SYSTEM_ADMIN)
                    .action(ThemisAction.DELETE)
                    .target(auditResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.ACTION_NOT_PERMITTED);
            assertThat(decision.policyId()).isEqualTo(AuditImmutabilityDenyPolicy.POLICY_ID);
        }

        @Test
        @DisplayName("Explicit Deny: audit.read is DENIED CREATE, UPDATE, DELETE on AUDIT resource")
        void testAuditReaderMutationDenied() {
            for (ThemisAction action : Set.of(ThemisAction.CREATE, ThemisAction.UPDATE, ThemisAction.DELETE)) {
                ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                        .principal(testHuman)
                        .authority(HarmoniaSecurityConstants.AUTH_AUDIT_READ)
                        .action(action)
                        .target(auditResource)
                        .build();

                ThemisAuthorizationDecision decision = evaluator.authorize(request);
                assertThat(decision.isDenied()).isTrue();
                assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.ACTION_NOT_PERMITTED);
                assertThat(decision.policyId()).isEqualTo(AuditImmutabilityDenyPolicy.POLICY_ID);
            }
        }

        @Test
        @DisplayName("Clinical and Operations roles are denied AUDIT READ and SEARCH")
        void testOtherRolesCannotReadAudit() {
            for (ThemisRole role : Set.of(
                    HarmoniaSecurityConstants.CLINICAL_READ,
                    HarmoniaSecurityConstants.CLINICAL_WRITE,
                    HarmoniaSecurityConstants.CLINICAL_ADMIN,
                    HarmoniaSecurityConstants.OPS_VIEWER,
                    HarmoniaSecurityConstants.OPS_ADM,
                    HarmoniaSecurityConstants.PRV_RDR
            )) {
                ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                        .principal(testHuman)
                        .authorities(role.authorities())
                        .action(ThemisAction.READ)
                        .target(auditResource)
                        .build();

                ThemisAuthorizationDecision decision = evaluator.authorize(request);
                assertThat(decision.isDenied()).isTrue();
                assertThat(decision.policyId()).isEqualTo(AuditReadPolicy.POLICY_ID);
            }
        }
    }

    @Nested
    @DisplayName("Role Catalogue and Default Deny")
    class DefaultDenyAndRolesTests {

        @Test
        @DisplayName("Role PRV_RDR resolves to provider.read and provider.search")
        void testRoleMapping() {
            var roleOpt = HarmoniaSecurityConstants.getRole("PRV_RDR");
            assertThat(roleOpt).isPresent();
            assertThat(roleOpt.get().authorities())
                    .extracting(ThemisAuthority::authorityCode)
                    .containsExactlyInAnyOrder(HarmoniaSecurityConstants.AUTH_PROVIDER_READ, HarmoniaSecurityConstants.AUTH_PROVIDER_SEARCH);

            var opsViewerOpt = HarmoniaSecurityConstants.getRole("OPS_VIEWER");
            assertThat(opsViewerOpt).isPresent();
            assertThat(opsViewerOpt.get().authorities())
                    .extracting(ThemisAuthority::authorityCode)
                    .containsExactlyInAnyOrder(HarmoniaSecurityConstants.AUTH_OPERATIONS_READ);

            var opsAdmOpt = HarmoniaSecurityConstants.getRole("OPS_ADM");
            assertThat(opsAdmOpt).isPresent();
            assertThat(opsAdmOpt.get().authorities())
                    .extracting(ThemisAuthority::authorityCode)
                    .containsExactlyInAnyOrder(HarmoniaSecurityConstants.AUTH_OPERATIONS_READ, HarmoniaSecurityConstants.AUTH_OPERATIONS_ADMIN);
        }

        @Test
        @DisplayName("Unknown resource and action falls back to DEFAULT_DENY")
        void testDefaultDenyUnknownDomain() {
            ThemisResource unknownResource = ThemisResource.of("UnknownResource", "unknown-1", "CUSTOM_DOMAIN", Set.of());
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority("some.custom.authority")
                    .action(ThemisAction.EXECUTE)
                    .target(unknownResource)
                    .build();

            ThemisAuthorizationDecision decision = evaluator.authorize(request);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.DEFAULT_DENY);
        }
    }
}
