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

import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
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
import net.fhirfactory.harmonia.themis.core.constants.HarmoniaSecurityConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OperationsAuthorizationPolicy Tests")
class OperationsAuthorizationPolicyTest {

    private OperationsAuthorizationPolicy policy;
    private ThemisPrincipal testHuman;
    private ThemisPrincipal testService;
    private ThemisResource operationsResource;
    private ThemisResource legacyOperationsResource;
    private ThemisResource clinicalResource;
    private ThemisResource providerResource;
    private ThemisResource auditResource;

    @BeforeEach
    void setUp() {
        policy = new OperationsAuthorizationPolicy();
        testHuman = ThemisPrincipal.human("operator-123");
        testService = ThemisPrincipal.service("service:pylai");

        operationsResource = ThemisResource.builder()
                .resourceType("OperationsResource")
                .resourceId("/api/operations/summary")
                .securityDomain(HarmoniaSecurityConstants.LABEL_OPERATIONS)
                .securityLabels(Set.of(ThemisSecurityLabel.of(HarmoniaSecurityConstants.LABEL_OPERATIONS)))
                .build();

        legacyOperationsResource = ThemisResource.of("OperationsResource", "/api/operations/subsystems");

        clinicalResource = ThemisResource.builder()
                .resourceType("Patient")
                .resourceId("patient-456")
                .securityDomain(HarmoniaSecurityConstants.LABEL_CLINICAL)
                .securityLabels(Set.of(ThemisSecurityLabel.of(HarmoniaSecurityConstants.LABEL_CLINICAL)))
                .build();

        providerResource = ThemisResource.builder()
                .resourceType("Practitioner")
                .resourceId("practitioner-789")
                .securityDomain(HarmoniaSecurityConstants.LABEL_PROVIDER_REGISTRY)
                .securityLabels(Set.of(ThemisSecurityLabel.of(HarmoniaSecurityConstants.LABEL_PROVIDER_REGISTRY)))
                .build();

        auditResource = ThemisResource.builder()
                .resourceType("AuditEvent")
                .resourceId("audit-101")
                .securityDomain(HarmoniaSecurityConstants.LABEL_AUDIT)
                .securityLabels(Set.of(ThemisSecurityLabel.of(HarmoniaSecurityConstants.LABEL_AUDIT)))
                .build();
    }

    @Nested
    @DisplayName("Policy Metadata and Contract")
    class MetadataTests {

        @Test
        @DisplayName("Policy ID is operations-authorization-policy")
        void policyIdIsCorrect() {
            assertThat(policy.getPolicyId()).isEqualTo("operations-authorization-policy");
        }

        @Test
        @DisplayName("Description is present and not blank")
        void descriptionIsPresent() {
            assertThat(policy.getDescription()).isNotBlank();
        }

        @Test
        @DisplayName("Order is 100")
        void orderIs100() {
            assertThat(policy.getOrder()).isEqualTo(100);
        }

        @Test
        @DisplayName("Policy is not explicit deny")
        void isNotExplicitDeny() {
            assertThat(policy.isExplicitDeny()).isFalse();
        }
    }

    @Nested
    @DisplayName("Target Applicability")
    class ApplicabilityTests {

        @Test
        @DisplayName("Applies to resource with OPERATIONS domain")
        void appliesToOperationsDomain() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .action(ThemisAction.READ)
                    .target(operationsResource)
                    .build();
            assertThat(policy.appliesTo(request)).isTrue();
        }

        @Test
        @DisplayName("Applies to legacy unlabelled OperationsResource")
        void appliesToLegacyOperationsResource() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .action(ThemisAction.READ)
                    .target(legacyOperationsResource)
                    .build();
            assertThat(policy.appliesTo(request)).isTrue();
        }

        @Test
        @DisplayName("Does not apply to null request or target")
        void doesNotApplyToNull() {
            assertThat(policy.appliesTo(null)).isFalse();
            ThemisAuthorizationRequest nullTarget = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .action(ThemisAction.READ)
                    .build();
            assertThat(policy.appliesTo(nullTarget)).isFalse();
        }

        @Test
        @DisplayName("Does not apply to non-Operations domain resources")
        void doesNotApplyToNonOperationsDomains() {
            ThemisAuthorizationRequest clinicalReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .action(ThemisAction.READ)
                    .target(clinicalResource)
                    .build();
            assertThat(policy.appliesTo(clinicalReq)).isFalse();

            ThemisAuthorizationRequest providerReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .action(ThemisAction.READ)
                    .target(providerResource)
                    .build();
            assertThat(policy.appliesTo(providerReq)).isFalse();

            ThemisAuthorizationRequest auditReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .action(ThemisAction.READ)
                    .target(auditResource)
                    .build();
            assertThat(policy.appliesTo(auditReq)).isFalse();
        }

        @Test
        @DisplayName("Does not apply to unknown resource without OPERATIONS domain or label")
        void doesNotApplyToUnknownResource() {
            ThemisResource unknown = ThemisResource.of("UnknownResource", "123");
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .action(ThemisAction.READ)
                    .target(unknown)
                    .build();
            assertThat(policy.appliesTo(request)).isFalse();
        }

        @Test
        @DisplayName("Does not apply to OperationsResource if explicitly scoped to CLINICAL domain")
        void doesNotApplyIfExplicitlyScopedToOtherDomain() {
            ThemisResource mislabeled = ThemisResource.builder()
                    .resourceType("OperationsResource")
                    .resourceId("/api/operations/custom")
                    .securityDomain(HarmoniaSecurityConstants.LABEL_CLINICAL)
                    .build();
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .action(ThemisAction.READ)
                    .target(mislabeled)
                    .build();
            assertThat(policy.appliesTo(request)).isFalse();
        }
    }

    @Nested
    @DisplayName("Explicit Allow Decisions")
    class AllowTests {

        @Test
        @DisplayName("operations.read authority allows READ action")
        void operationsReadAllowsRead() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authorities(Set.of(ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_OPERATIONS_READ)))
                    .action(ThemisAction.READ)
                    .target(operationsResource)
                    .context(ThemisSecurityContext.builder().correlationId("corr-read-1").build())
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);

            assertThat(decision.isAllowed()).isTrue();
            assertThat(decision.policyId()).isEqualTo("operations-authorization-policy");
            assertThat(decision.correlationId()).isEqualTo("corr-read-1");
        }

        @Test
        @DisplayName("operations.read authority allows SEARCH action")
        void operationsReadAllowsSearch() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authorities(Set.of(ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_OPERATIONS_READ)))
                    .action(ThemisAction.SEARCH)
                    .target(operationsResource)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);

            assertThat(decision.isAllowed()).isTrue();
            assertThat(decision.policyId()).isEqualTo("operations-authorization-policy");
        }

        @Test
        @DisplayName("operations.admin authority allows READ and SEARCH actions")
        void operationsAdminAllowsReadAndSearch() {
            ThemisAuthorizationRequest readReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authorities(Set.of(ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_OPERATIONS_ADMIN)))
                    .action(ThemisAction.READ)
                    .target(operationsResource)
                    .build();
            assertThat(policy.evaluate(readReq).isAllowed()).isTrue();

            ThemisAuthorizationRequest searchReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authorities(Set.of(ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_OPERATIONS_ADMIN)))
                    .action(ThemisAction.SEARCH)
                    .target(operationsResource)
                    .build();
            assertThat(policy.evaluate(searchReq).isAllowed()).isTrue();
        }

        @Test
        @DisplayName("operations.admin authority allows EXECUTE action")
        void operationsAdminAllowsExecute() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authorities(Set.of(ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_OPERATIONS_ADMIN)))
                    .action(ThemisAction.EXECUTE)
                    .target(operationsResource)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);

            assertThat(decision.isAllowed()).isTrue();
            assertThat(decision.policyId()).isEqualTo("operations-authorization-policy");
        }

        @Test
        @DisplayName("operations.admin authority allows ADMINISTER action")
        void operationsAdminAllowsAdminister() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authorities(Set.of(ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_OPERATIONS_ADMIN)))
                    .action(ThemisAction.ADMINISTER)
                    .target(operationsResource)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);

            assertThat(decision.isAllowed()).isTrue();
            assertThat(decision.policyId()).isEqualTo("operations-authorization-policy");
        }

        @Test
        @DisplayName("system.integration authority allows READ and SEARCH actions")
        void systemIntegrationAllowsReadAndSearch() {
            ThemisAuthorizationRequest readReq = ThemisAuthorizationRequest.builder()
                    .principal(testService)
                    .authorities(Set.of(ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_SYSTEM_INTEGRATION)))
                    .action(ThemisAction.READ)
                    .target(operationsResource)
                    .build();
            assertThat(policy.evaluate(readReq).isAllowed()).isTrue();

            ThemisAuthorizationRequest searchReq = ThemisAuthorizationRequest.builder()
                    .principal(testService)
                    .authorities(Set.of(ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_SYSTEM_INTEGRATION)))
                    .action(ThemisAction.SEARCH)
                    .target(operationsResource)
                    .build();
            assertThat(policy.evaluate(searchReq).isAllowed()).isTrue();
        }
    }

    @Nested
    @DisplayName("Fail-Closed and Default-Deny Decisions")
    class DenyTests {

        @Test
        @DisplayName("Caller with no authorities is denied on READ with AUTHORITY_MISSING")
        void noAuthoritiesDenied() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authorities(Set.of())
                    .action(ThemisAction.READ)
                    .target(operationsResource)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);

            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.AUTHORITY_MISSING);
        }

        @Test
        @DisplayName("operations.read does not permit EXECUTE or ADMINISTER")
        void operationsReadCannotExecuteOrAdminister() {
            ThemisAuthorizationRequest execReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authorities(Set.of(ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_OPERATIONS_READ)))
                    .action(ThemisAction.EXECUTE)
                    .target(operationsResource)
                    .build();
            ThemisAuthorizationDecision execDec = policy.evaluate(execReq);
            assertThat(execDec.isDenied()).isTrue();
            assertThat(execDec.reason()).isEqualTo(ThemisDecisionReason.AUTHORITY_MISSING);

            ThemisAuthorizationRequest adminReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authorities(Set.of(ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_OPERATIONS_READ)))
                    .action(ThemisAction.ADMINISTER)
                    .target(operationsResource)
                    .build();
            ThemisAuthorizationDecision adminDec = policy.evaluate(adminReq);
            assertThat(adminDec.isDenied()).isTrue();
            assertThat(adminDec.reason()).isEqualTo(ThemisDecisionReason.AUTHORITY_MISSING);
        }

        @Test
        @DisplayName("operations.read does not permit CREATE, UPDATE, or DELETE")
        void operationsReadCannotMutateOrDelete() {
            for (ThemisAction action : Set.of(ThemisAction.CREATE, ThemisAction.UPDATE, ThemisAction.DELETE)) {
                ThemisAuthorizationRequest req = ThemisAuthorizationRequest.builder()
                        .principal(testHuman)
                        .authorities(Set.of(ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_OPERATIONS_READ)))
                        .action(action)
                        .target(operationsResource)
                        .build();
                ThemisAuthorizationDecision dec = policy.evaluate(req);
                assertThat(dec.isDenied()).isTrue();
                assertThat(dec.reason()).isEqualTo(ThemisDecisionReason.ACTION_NOT_PERMITTED);
            }
        }

        @Test
        @DisplayName("operations.admin does not permit DELETE")
        void operationsAdminCannotDelete() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authorities(Set.of(ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_OPERATIONS_ADMIN)))
                    .action(ThemisAction.DELETE)
                    .target(operationsResource)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);

            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.ACTION_NOT_PERMITTED);
        }

        @Test
        @DisplayName("system.integration cannot EXECUTE, ADMINISTER, CREATE, UPDATE, or DELETE")
        void systemIntegrationCannotExecuteOrMutate() {
            for (ThemisAction action : Set.of(ThemisAction.EXECUTE, ThemisAction.ADMINISTER)) {
                ThemisAuthorizationRequest req = ThemisAuthorizationRequest.builder()
                        .principal(testService)
                        .authorities(Set.of(ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_SYSTEM_INTEGRATION)))
                        .action(action)
                        .target(operationsResource)
                        .build();
                ThemisAuthorizationDecision dec = policy.evaluate(req);
                assertThat(dec.isDenied()).isTrue();
                assertThat(dec.reason()).isEqualTo(ThemisDecisionReason.AUTHORITY_MISSING);
            }

            for (ThemisAction action : Set.of(ThemisAction.CREATE, ThemisAction.UPDATE, ThemisAction.DELETE)) {
                ThemisAuthorizationRequest req = ThemisAuthorizationRequest.builder()
                        .principal(testService)
                        .authorities(Set.of(ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_SYSTEM_INTEGRATION)))
                        .action(action)
                        .target(operationsResource)
                        .build();
                ThemisAuthorizationDecision dec = policy.evaluate(req);
                assertThat(dec.isDenied()).isTrue();
                assertThat(dec.reason()).isEqualTo(ThemisDecisionReason.ACTION_NOT_PERMITTED);
            }
        }

        @ParameterizedTest
        @EnumSource(value = ThemisAction.class, names = {"PROCESS", "APPROVE", "REJECT", "SUBMIT_CREATE", "SUBMIT_UPDATE", "REPLAY"})
        @DisplayName("Unsupported workflow actions are denied with ACTION_NOT_PERMITTED")
        void unsupportedActionsDenied(ThemisAction action) {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authorities(Set.of(
                            ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_OPERATIONS_READ),
                            ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_OPERATIONS_ADMIN)
                    ))
                    .action(action)
                    .target(operationsResource)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);

            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.ACTION_NOT_PERMITTED);
        }

        @Test
        @DisplayName("Unknown authority is denied with AUTHORITY_MISSING")
        void unknownAuthorityDenied() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authorities(Set.of(ThemisAuthority.of("custom.unknown.authority")))
                    .action(ThemisAction.READ)
                    .target(operationsResource)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);

            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.AUTHORITY_MISSING);
        }

        @Test
        @DisplayName("Non-operations resource evaluated against policy returns SECURITY_LABEL_NOT_PERMITTED")
        void nonOperationsResourceDenied() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authorities(Set.of(ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_OPERATIONS_READ)))
                    .action(ThemisAction.READ)
                    .target(clinicalResource)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);

            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.SECURITY_LABEL_NOT_PERMITTED);
        }

        @Test
        @DisplayName("Missing principal is denied with PRINCIPAL_MISSING")
        void missingPrincipalDenied() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .authorities(Set.of(ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_OPERATIONS_READ)))
                    .action(ThemisAction.READ)
                    .target(operationsResource)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);

            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.PRINCIPAL_MISSING);
        }

        @Test
        @DisplayName("Blank principalId is denied with PRINCIPAL_MISSING")
        void blankPrincipalDenied() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(ThemisPrincipal.human("   "))
                    .authorities(Set.of(ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_OPERATIONS_READ)))
                    .action(ThemisAction.READ)
                    .target(operationsResource)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);

            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.PRINCIPAL_MISSING);
        }

        @Test
        @DisplayName("Null request is denied with CONTEXT_MALFORMED")
        void nullRequestDenied() {
            ThemisAuthorizationDecision decision = policy.evaluate(null);

            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.CONTEXT_MALFORMED);
        }

        @Test
        @DisplayName("Missing target resource is denied with CONTEXT_MALFORMED")
        void missingTargetDenied() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authorities(Set.of(ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_OPERATIONS_READ)))
                    .action(ThemisAction.READ)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);

            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.CONTEXT_MALFORMED);
        }

        @Test
        @DisplayName("Null action is denied with ACTION_NOT_PERMITTED")
        void nullActionDenied() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authorities(Set.of(ThemisAuthority.of(HarmoniaSecurityConstants.AUTH_OPERATIONS_READ)))
                    .target(operationsResource)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);

            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.ACTION_NOT_PERMITTED);
        }

        @Test
        @DisplayName("Principal attributes alone (e.g. role=OPS_VIEWER or OPS_ADM) do not grant access at policy level")
        void principalRoleAttributesAloneDoNotGrant() {
            ThemisPrincipal principalWithRole = new ThemisPrincipal("operator", PrincipalType.HUMAN, "test", Map.of("role", "OPS_VIEWER"));
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(principalWithRole)
                    .authorities(Set.of())
                    .action(ThemisAction.READ)
                    .target(operationsResource)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);

            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.AUTHORITY_MISSING);
        }
    }

    @Nested
    @DisplayName("Cross-Domain Isolation")
    class CrossDomainIsolationTests {

        @Test
        @DisplayName("Operations authorities cannot grant Clinical access")
        void operationsAuthoritiesCannotGrantClinicalAccess() {
            for (String auth : Set.of(HarmoniaSecurityConstants.AUTH_OPERATIONS_READ, HarmoniaSecurityConstants.AUTH_OPERATIONS_ADMIN, HarmoniaSecurityConstants.AUTH_SYSTEM_INTEGRATION)) {
                for (ThemisAction action : Set.of(ThemisAction.READ, ThemisAction.SEARCH, ThemisAction.CREATE, ThemisAction.UPDATE, ThemisAction.DELETE)) {
                    ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                            .principal(testHuman)
                            .authorities(Set.of(ThemisAuthority.of(auth)))
                            .action(action)
                            .target(clinicalResource)
                            .build();

                    assertThat(policy.appliesTo(request)).isFalse();
                    assertThat(policy.evaluate(request).isDenied()).isTrue();
                }
            }
        }

        @Test
        @DisplayName("Operations authorities cannot grant Provider Registry access")
        void operationsAuthoritiesCannotGrantProviderRegistryAccess() {
            for (String auth : Set.of(HarmoniaSecurityConstants.AUTH_OPERATIONS_READ, HarmoniaSecurityConstants.AUTH_OPERATIONS_ADMIN, HarmoniaSecurityConstants.AUTH_SYSTEM_INTEGRATION)) {
                ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                        .principal(testHuman)
                        .authorities(Set.of(ThemisAuthority.of(auth)))
                        .action(ThemisAction.READ)
                        .target(providerResource)
                        .build();

                assertThat(policy.appliesTo(request)).isFalse();
                assertThat(policy.evaluate(request).isDenied()).isTrue();
            }
        }

        @Test
        @DisplayName("Operations authorities cannot grant Audit access")
        void operationsAuthoritiesCannotGrantAuditAccess() {
            for (String auth : Set.of(HarmoniaSecurityConstants.AUTH_OPERATIONS_READ, HarmoniaSecurityConstants.AUTH_OPERATIONS_ADMIN, HarmoniaSecurityConstants.AUTH_SYSTEM_INTEGRATION)) {
                ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                        .principal(testHuman)
                        .authorities(Set.of(ThemisAuthority.of(auth)))
                        .action(ThemisAction.READ)
                        .target(auditResource)
                        .build();

                assertThat(policy.appliesTo(request)).isFalse();
                assertThat(policy.evaluate(request).isDenied()).isTrue();
            }
        }

        @Test
        @DisplayName("Provider, Clinical, and Audit authorities cannot grant Operations access")
        void otherDomainAuthoritiesCannotGrantOperationsAccess() {
            Set<String> otherAuths = Set.of(
                    HarmoniaSecurityConstants.AUTH_CLINICAL_READ,
                    HarmoniaSecurityConstants.AUTH_CLINICAL_SEARCH,
                    HarmoniaSecurityConstants.AUTH_CLINICAL_CREATE,
                    HarmoniaSecurityConstants.AUTH_CLINICAL_UPDATE,
                    HarmoniaSecurityConstants.AUTH_CLINICAL_ADMIN,
                    HarmoniaSecurityConstants.AUTH_PROVIDER_READ,
                    HarmoniaSecurityConstants.AUTH_PROVIDER_ADMIN,
                    HarmoniaSecurityConstants.AUTH_AUDIT_READ
            );

            for (String auth : otherAuths) {
                ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                        .principal(testHuman)
                        .authorities(Set.of(ThemisAuthority.of(auth)))
                        .action(ThemisAction.READ)
                        .target(operationsResource)
                        .build();

                ThemisAuthorizationDecision decision = policy.evaluate(request);
                assertThat(decision.isDenied()).isTrue();
                assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.AUTHORITY_MISSING);
            }
        }
    }

    @Nested
    @DisplayName("Role Vocabulary Integration")
    class RoleVocabularyTests {

        @Test
        @DisplayName("OPS_VIEWER role yields READ, SEARCH allow; others deny")
        void opsViewerRoleOutcomes() {
            ThemisRole opsViewer = HarmoniaSecurityConstants.OPS_VIEWER;
            Set<ThemisAuthority> authorities = opsViewer.authorities();

            ThemisAuthorizationRequest readReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).authorities(authorities).action(ThemisAction.READ).target(operationsResource).build();
            assertThat(policy.evaluate(readReq).isAllowed()).isTrue();

            ThemisAuthorizationRequest searchReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).authorities(authorities).action(ThemisAction.SEARCH).target(operationsResource).build();
            assertThat(policy.evaluate(searchReq).isAllowed()).isTrue();

            for (ThemisAction action : Set.of(ThemisAction.EXECUTE, ThemisAction.ADMINISTER, ThemisAction.CREATE, ThemisAction.UPDATE, ThemisAction.DELETE)) {
                ThemisAuthorizationRequest req = ThemisAuthorizationRequest.builder()
                        .principal(testHuman).authorities(authorities).action(action).target(operationsResource).build();
                assertThat(policy.evaluate(req).isDenied()).isTrue();
            }
        }

        @Test
        @DisplayName("OPS_ADM role yields READ, SEARCH, EXECUTE, ADMINISTER allow; CREATE, UPDATE, DELETE deny")
        void opsAdmRoleOutcomes() {
            ThemisRole opsAdm = HarmoniaSecurityConstants.OPS_ADM;
            Set<ThemisAuthority> authorities = opsAdm.authorities();

            for (ThemisAction action : Set.of(ThemisAction.READ, ThemisAction.SEARCH, ThemisAction.EXECUTE, ThemisAction.ADMINISTER)) {
                ThemisAuthorizationRequest req = ThemisAuthorizationRequest.builder()
                        .principal(testHuman).authorities(authorities).action(action).target(operationsResource).build();
                assertThat(policy.evaluate(req).isAllowed()).isTrue();
            }

            for (ThemisAction action : Set.of(ThemisAction.CREATE, ThemisAction.UPDATE, ThemisAction.DELETE)) {
                ThemisAuthorizationRequest req = ThemisAuthorizationRequest.builder()
                        .principal(testHuman).authorities(authorities).action(action).target(operationsResource).build();
                assertThat(policy.evaluate(req).isDenied()).isTrue();
            }
        }
    }
}
