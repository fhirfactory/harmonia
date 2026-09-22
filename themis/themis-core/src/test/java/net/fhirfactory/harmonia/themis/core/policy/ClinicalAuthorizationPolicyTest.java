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
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecisionReason;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisResource;
import net.fhirfactory.harmonia.themis.api.model.ThemisRole;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityLabel;
import net.fhirfactory.harmonia.themis.core.constants.HarmoniaSecurityConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ClinicalAuthorizationPolicy Tests")
class ClinicalAuthorizationPolicyTest {

    private ClinicalAuthorizationPolicy policy;
    private ThemisPrincipal testHuman;
    private ThemisResource clinicalResource;
    private ThemisResource providerResource;

    @BeforeEach
    void setUp() {
        policy = new ClinicalAuthorizationPolicy();
        testHuman = ThemisPrincipal.human("user:dr-alice");
        clinicalResource = ThemisResource.of(
                "Patient",
                "patient-123",
                HarmoniaSecurityConstants.LABEL_CLINICAL,
                Set.of(ThemisSecurityLabel.of(HarmoniaSecurityConstants.LABEL_CLINICAL))
        );
        providerResource = ThemisResource.of(
                "Practitioner",
                "practitioner-456",
                HarmoniaSecurityConstants.LABEL_PROVIDER_REGISTRY,
                Set.of(ThemisSecurityLabel.of(HarmoniaSecurityConstants.LABEL_PROVIDER_REGISTRY))
        );
    }

    @Nested
    @DisplayName("Policy Metadata and Applicability")
    class MetadataAndApplicabilityTests {

        @Test
        @DisplayName("Policy ID, description, and default order are properly configured")
        void testPolicyMetadata() {
            assertThat(policy.getPolicyId()).isEqualTo("clinical-authorization-policy");
            assertThat(policy.getDescription()).isNotBlank();
            assertThat(policy.getOrder()).isEqualTo(100);
            assertThat(policy.isExplicitDeny()).isFalse();
        }

        @Test
        @DisplayName("Applies to clinical resources by domain or security label")
        void testAppliesToClinical() {
            ThemisAuthorizationRequest domainRequest = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .action(ThemisAction.READ)
                    .target(ThemisResource.of("Observation", "obs-1", "CLINICAL", Set.of()))
                    .build();
            assertThat(policy.appliesTo(domainRequest)).isTrue();

            ThemisAuthorizationRequest labelRequest = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .action(ThemisAction.READ)
                    .target(ThemisResource.of("Observation", "obs-1", null, Set.of(ThemisSecurityLabel.of("CLINICAL"))))
                    .build();
            assertThat(policy.appliesTo(labelRequest)).isTrue();
        }

        @Test
        @DisplayName("Does not apply to null requests or non-clinical domains")
        void testDoesNotApplyToNonClinical() {
            assertThat(policy.appliesTo(null)).isFalse();

            ThemisAuthorizationRequest noTarget = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .action(ThemisAction.READ)
                    .build();
            assertThat(policy.appliesTo(noTarget)).isFalse();

            ThemisAuthorizationRequest nonClinical = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .action(ThemisAction.READ)
                    .target(providerResource)
                    .build();
            assertThat(policy.appliesTo(nonClinical)).isFalse();
        }
    }

    @Nested
    @DisplayName("Explicit Allow Decisions")
    class ExplicitAllowTests {

        @Test
        @DisplayName("1. clinical.read + READ -> ALLOW")
        void testClinicalReadAllowsRead() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_CLINICAL_READ)
                    .action(ThemisAction.READ)
                    .target(clinicalResource)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);
            assertThat(decision.isAllowed()).isTrue();
            assertThat(decision.policyId()).isEqualTo(ClinicalAuthorizationPolicy.POLICY_ID);
        }

        @Test
        @DisplayName("2. clinical.search + SEARCH -> ALLOW")
        void testClinicalSearchAllowsSearch() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_CLINICAL_SEARCH)
                    .action(ThemisAction.SEARCH)
                    .target(clinicalResource)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);
            assertThat(decision.isAllowed()).isTrue();
            assertThat(decision.policyId()).isEqualTo(ClinicalAuthorizationPolicy.POLICY_ID);
        }

        @Test
        @DisplayName("3. clinical.create + CREATE -> ALLOW")
        void testClinicalCreateAllowsCreate() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_CLINICAL_CREATE)
                    .action(ThemisAction.CREATE)
                    .target(clinicalResource)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);
            assertThat(decision.isAllowed()).isTrue();
            assertThat(decision.policyId()).isEqualTo(ClinicalAuthorizationPolicy.POLICY_ID);
        }

        @Test
        @DisplayName("4. clinical.update + UPDATE -> ALLOW")
        void testClinicalUpdateAllowsUpdate() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_CLINICAL_UPDATE)
                    .action(ThemisAction.UPDATE)
                    .target(clinicalResource)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);
            assertThat(decision.isAllowed()).isTrue();
            assertThat(decision.policyId()).isEqualTo(ClinicalAuthorizationPolicy.POLICY_ID);
        }

        @Test
        @DisplayName("5. clinical.admin + ADMINISTER -> ALLOW")
        void testClinicalAdminAllowsAdminister() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_CLINICAL_ADMIN)
                    .action(ThemisAction.ADMINISTER)
                    .target(clinicalResource)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);
            assertThat(decision.isAllowed()).isTrue();
            assertThat(decision.policyId()).isEqualTo(ClinicalAuthorizationPolicy.POLICY_ID);
        }
    }

    @Nested
    @DisplayName("Default-Deny and Negative Test Cases")
    class DefaultDenyTests {

        @Test
        @DisplayName("6. No authorities + READ -> DENY (AUTHORITY_MISSING)")
        void testNoAuthoritiesDeniesRead() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .action(ThemisAction.READ)
                    .target(clinicalResource)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.AUTHORITY_MISSING);
        }

        @Test
        @DisplayName("7. clinical.read + CREATE -> DENY (AUTHORITY_MISSING)")
        void testClinicalReadDeniesCreate() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_CLINICAL_READ)
                    .action(ThemisAction.CREATE)
                    .target(clinicalResource)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.AUTHORITY_MISSING);
        }

        @Test
        @DisplayName("8. clinical.create + READ -> DENY (AUTHORITY_MISSING)")
        void testClinicalCreateDeniesRead() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_CLINICAL_CREATE)
                    .action(ThemisAction.READ)
                    .target(clinicalResource)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.AUTHORITY_MISSING);
        }

        @Test
        @DisplayName("9. clinical.update + DELETE -> DENY (ACTION_NOT_PERMITTED)")
        void testClinicalUpdateDeniesDelete() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_CLINICAL_UPDATE)
                    .action(ThemisAction.DELETE)
                    .target(clinicalResource)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.ACTION_NOT_PERMITTED);
        }

        @Test
        @DisplayName("10. clinical.admin + DELETE -> DENY (ACTION_NOT_PERMITTED)")
        void testClinicalAdminDeniesDelete() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_CLINICAL_ADMIN)
                    .action(ThemisAction.DELETE)
                    .target(clinicalResource)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.ACTION_NOT_PERMITTED);
        }

        @Test
        @DisplayName("11. Unknown / unmapped actions -> DENY (ACTION_NOT_PERMITTED)")
        void testUnmappedActionDenies() {
            for (ThemisAction unmapped : Set.of(ThemisAction.EXECUTE, ThemisAction.REPLAY, ThemisAction.PROCESS, ThemisAction.APPROVE, ThemisAction.REJECT)) {
                ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                        .principal(testHuman)
                        .authority(HarmoniaSecurityConstants.AUTH_CLINICAL_ADMIN)
                        .action(unmapped)
                        .target(clinicalResource)
                        .build();

                ThemisAuthorizationDecision decision = policy.evaluate(request);
                assertThat(decision.isDenied())
                        .as("Action %s must be denied on clinical resources", unmapped)
                        .isTrue();
                assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.ACTION_NOT_PERMITTED);
            }
        }

        @Test
        @DisplayName("12. Non-Clinical resource -> DENY (SECURITY_LABEL_NOT_PERMITTED)")
        void testNonClinicalResourceDenies() {
            ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .authority(HarmoniaSecurityConstants.AUTH_CLINICAL_READ)
                    .action(ThemisAction.READ)
                    .target(providerResource)
                    .build();

            ThemisAuthorizationDecision decision = policy.evaluate(request);
            assertThat(decision.isDenied()).isTrue();
            assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.SECURITY_LABEL_NOT_PERMITTED);
        }

        @Test
        @DisplayName("13. Missing principal / request / resource information -> fail-closed DENY")
        void testMissingRequestInformation() {
            ThemisAuthorizationDecision nullReqDecision = policy.evaluate(null);
            assertThat(nullReqDecision.isDenied()).isTrue();
            assertThat(nullReqDecision.reason()).isEqualTo(ThemisDecisionReason.CONTEXT_MALFORMED);

            ThemisAuthorizationRequest nullPrincipal = ThemisAuthorizationRequest.builder()
                    .action(ThemisAction.READ)
                    .target(clinicalResource)
                    .build();
            assertThat(policy.evaluate(nullPrincipal).reason()).isEqualTo(ThemisDecisionReason.PRINCIPAL_MISSING);

            ThemisAuthorizationRequest blankPrincipal = ThemisAuthorizationRequest.builder()
                    .principal(ThemisPrincipal.human("   "))
                    .action(ThemisAction.READ)
                    .target(clinicalResource)
                    .build();
            assertThat(policy.evaluate(blankPrincipal).reason()).isEqualTo(ThemisDecisionReason.PRINCIPAL_MISSING);

            ThemisAuthorizationRequest nullTarget = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .action(ThemisAction.READ)
                    .build();
            assertThat(policy.evaluate(nullTarget).reason()).isEqualTo(ThemisDecisionReason.CONTEXT_MALFORMED);

            ThemisAuthorizationRequest nullAction = ThemisAuthorizationRequest.builder()
                    .principal(testHuman)
                    .target(clinicalResource)
                    .build();
            assertThat(policy.evaluate(nullAction).reason()).isEqualTo(ThemisDecisionReason.ACTION_NOT_PERMITTED);
        }

        @Test
        @DisplayName("14. Provider, Audit, and System authorities must NOT grant Clinical access")
        void testOtherAuthoritiesDoNotGrantClinicalAccess() {
            Set<String> nonClinicalAuthorities = Set.of(
                    HarmoniaSecurityConstants.AUTH_PROVIDER_READ,
                    HarmoniaSecurityConstants.AUTH_PROVIDER_SEARCH,
                    HarmoniaSecurityConstants.AUTH_PROVIDER_ADMIN,
                    HarmoniaSecurityConstants.AUTH_PROVIDER_RESOURCE_CREATE,
                    HarmoniaSecurityConstants.AUTH_PROVIDER_RESOURCE_UPDATE,
                    HarmoniaSecurityConstants.AUTH_PROVIDER_RESOURCE_DELETE,
                    HarmoniaSecurityConstants.AUTH_AUDIT_READ,
                    HarmoniaSecurityConstants.AUTH_SYSTEM_INTEGRATION
            );

            for (String auth : nonClinicalAuthorities) {
                ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                        .principal(testHuman)
                        .authority(auth)
                        .action(ThemisAction.READ)
                        .target(clinicalResource)
                        .build();

                ThemisAuthorizationDecision decision = policy.evaluate(request);
                assertThat(decision.isDenied())
                        .as("Authority %s must not grant READ on clinical resources", auth)
                        .isTrue();
                assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.AUTHORITY_MISSING);
            }
        }
    }

    @Nested
    @DisplayName("Role Vocabulary Outcomes")
    class RoleVocabularyTests {

        @Test
        @DisplayName("CLINICAL_READ: READ, SEARCH -> ALLOW; CREATE, UPDATE, DELETE -> DENY")
        void testClinicalReadRoleOutcomes() {
            ThemisRole role = HarmoniaSecurityConstants.CLINICAL_READ;
            Set<ThemisAuthority> authorities = role.authorities();

            ThemisAuthorizationRequest readReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).authorities(authorities).action(ThemisAction.READ).target(clinicalResource).build();
            assertThat(policy.evaluate(readReq).isAllowed()).isTrue();

            ThemisAuthorizationRequest searchReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).authorities(authorities).action(ThemisAction.SEARCH).target(clinicalResource).build();
            assertThat(policy.evaluate(searchReq).isAllowed()).isTrue();

            ThemisAuthorizationRequest createReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).authorities(authorities).action(ThemisAction.CREATE).target(clinicalResource).build();
            assertThat(policy.evaluate(createReq).isDenied()).isTrue();

            ThemisAuthorizationRequest updateReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).authorities(authorities).action(ThemisAction.UPDATE).target(clinicalResource).build();
            assertThat(policy.evaluate(updateReq).isDenied()).isTrue();

            ThemisAuthorizationRequest deleteReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).authorities(authorities).action(ThemisAction.DELETE).target(clinicalResource).build();
            assertThat(policy.evaluate(deleteReq).isDenied()).isTrue();
        }

        @Test
        @DisplayName("CLINICAL_WRITE: READ, SEARCH, CREATE, UPDATE -> ALLOW; DELETE -> DENY")
        void testClinicalWriteRoleOutcomes() {
            ThemisRole role = HarmoniaSecurityConstants.CLINICAL_WRITE;
            Set<ThemisAuthority> authorities = role.authorities();

            ThemisAuthorizationRequest readReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).authorities(authorities).action(ThemisAction.READ).target(clinicalResource).build();
            assertThat(policy.evaluate(readReq).isAllowed()).isTrue();

            ThemisAuthorizationRequest searchReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).authorities(authorities).action(ThemisAction.SEARCH).target(clinicalResource).build();
            assertThat(policy.evaluate(searchReq).isAllowed()).isTrue();

            ThemisAuthorizationRequest createReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).authorities(authorities).action(ThemisAction.CREATE).target(clinicalResource).build();
            assertThat(policy.evaluate(createReq).isAllowed()).isTrue();

            ThemisAuthorizationRequest updateReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).authorities(authorities).action(ThemisAction.UPDATE).target(clinicalResource).build();
            assertThat(policy.evaluate(updateReq).isAllowed()).isTrue();

            ThemisAuthorizationRequest deleteReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).authorities(authorities).action(ThemisAction.DELETE).target(clinicalResource).build();
            assertThat(policy.evaluate(deleteReq).isDenied()).isTrue();
        }

        @Test
        @DisplayName("CLINICAL_ADMIN: READ, SEARCH, CREATE, UPDATE, ADMINISTER -> ALLOW; DELETE -> DENY")
        void testClinicalAdminRoleOutcomes() {
            ThemisRole role = HarmoniaSecurityConstants.CLINICAL_ADMIN;
            Set<ThemisAuthority> authorities = role.authorities();

            ThemisAuthorizationRequest readReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).authorities(authorities).action(ThemisAction.READ).target(clinicalResource).build();
            assertThat(policy.evaluate(readReq).isAllowed()).isTrue();

            ThemisAuthorizationRequest searchReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).authorities(authorities).action(ThemisAction.SEARCH).target(clinicalResource).build();
            assertThat(policy.evaluate(searchReq).isAllowed()).isTrue();

            ThemisAuthorizationRequest createReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).authorities(authorities).action(ThemisAction.CREATE).target(clinicalResource).build();
            assertThat(policy.evaluate(createReq).isAllowed()).isTrue();

            ThemisAuthorizationRequest updateReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).authorities(authorities).action(ThemisAction.UPDATE).target(clinicalResource).build();
            assertThat(policy.evaluate(updateReq).isAllowed()).isTrue();

            ThemisAuthorizationRequest adminReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).authorities(authorities).action(ThemisAction.ADMINISTER).target(clinicalResource).build();
            assertThat(policy.evaluate(adminReq).isAllowed()).isTrue();

            ThemisAuthorizationRequest deleteReq = ThemisAuthorizationRequest.builder()
                    .principal(testHuman).authorities(authorities).action(ThemisAction.DELETE).target(clinicalResource).build();
            assertThat(policy.evaluate(deleteReq).isDenied()).isTrue();
        }
    }
}
