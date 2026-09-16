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

package net.fhirfactory.harmonia.paradeigma.test.security;

import net.fhirfactory.harmonia.model.security.HarmoniaAuthorityEnum;
import net.fhirfactory.harmonia.model.security.HarmoniaRoleEnum;
import net.fhirfactory.harmonia.paradeigma.common.security.ParadeigmaSecurityActors;
import net.fhirfactory.harmonia.paradeigma.common.security.SecurityScenarioContext;
import net.fhirfactory.harmonia.themis.api.model.*;
import net.fhirfactory.harmonia.themis.core.constants.HarmoniaSecurityConstants;
import net.fhirfactory.harmonia.themis.core.evaluator.DeterministicPolicyEvaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests validating Paradeigma security actor fixtures, context factories, and authority evaluation
 * against Themis policy engine contracts.
 */
public class ParadeigmaSecurityActorsTest {

    private DeterministicPolicyEvaluator evaluator;
    private ThemisResource practitionerResource;

    @BeforeEach
    void setUp() {
        evaluator = DeterministicPolicyEvaluator.withDefaultPolicies();
        practitionerResource = ThemisResource.of(
                "Practitioner",
                "pract-dr-bowman-01",
                "PROVIDER_REGISTRY",
                Set.of(ThemisSecurityLabel.of("PROVIDER_REGISTRY"))
        );
    }

    @Test
    @DisplayName("Provider Steward context has full submission, read, and approval privileges")
    void providerStewardPrivileges() {
        SecurityScenarioContext steward = SecurityScenarioContext.providerSteward();

        assertThat(steward.principal()).isEqualTo(ParadeigmaSecurityActors.PROVIDER_STEWARD_PRINCIPAL);
        assertThat(steward.hasRole(HarmoniaRoleEnum.PRV_ADM)).isTrue();
        assertThat(steward.hasAuthority(HarmoniaAuthorityEnum.PROVIDER_CHANGE_SUBMIT)).isTrue();
        assertThat(steward.hasAuthority(HarmoniaAuthorityEnum.PROVIDER_CHANGE_APPROVE)).isTrue();
        assertThat(steward.hasAuthority(HarmoniaAuthorityEnum.PROVIDER_READ)).isTrue();
        assertThat(steward.hasAuthority(HarmoniaAuthorityEnum.PROVIDER_SEARCH)).isTrue();

        ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                .principal(steward.principal())
                .authorities(steward.grantedAuthorities())
                .action(ThemisAction.CREATE)
                .target(practitionerResource)
                .context(steward.securityContext())
                .build();

        ThemisAuthorizationDecision decision = evaluator.authorize(request);
        assertThat(decision.isAllowed()).isTrue();
    }

    @Test
    @DisplayName("Clinician context has read and search privileges, but cannot submit change requests")
    void clinicianPrivileges() {
        SecurityScenarioContext clinician = SecurityScenarioContext.clinician();

        assertThat(clinician.principal()).isEqualTo(ParadeigmaSecurityActors.CLINICIAN_PRINCIPAL);
        assertThat(clinician.hasRole(HarmoniaRoleEnum.PRV_RDR)).isTrue();
        assertThat(clinician.hasAuthority(HarmoniaAuthorityEnum.PROVIDER_READ)).isTrue();
        assertThat(clinician.hasAuthority(HarmoniaAuthorityEnum.PROVIDER_SEARCH)).isTrue();
        assertThat(clinician.hasAuthority(HarmoniaAuthorityEnum.PROVIDER_CHANGE_SUBMIT)).isFalse();
        assertThat(clinician.hasAuthority(HarmoniaAuthorityEnum.PROVIDER_CHANGE_APPROVE)).isFalse();

        ThemisAuthorizationRequest readRequest = ThemisAuthorizationRequest.builder()
                .principal(clinician.principal())
                .authorities(clinician.grantedAuthorities())
                .action(ThemisAction.READ)
                .target(practitionerResource)
                .context(clinician.securityContext())
                .build();

        ThemisAuthorizationDecision readDecision = evaluator.authorize(readRequest);
        assertThat(readDecision.isAllowed()).isTrue();

        ThemisAuthorizationRequest createRequest = ThemisAuthorizationRequest.builder()
                .principal(clinician.principal())
                .authorities(clinician.grantedAuthorities())
                .action(ThemisAction.CREATE)
                .target(practitionerResource)
                .context(clinician.securityContext())
                .build();

        ThemisAuthorizationDecision createDecision = evaluator.authorize(createRequest);
        assertThat(createDecision.isDenied()).isTrue();
        assertThat(createDecision.reason()).isEqualTo(ThemisDecisionReason.PERSISTENCE_AUTHORITY_MISSING);
    }

    @Test
    @DisplayName("Unauthorized User is denied on all actions")
    void unauthorizedUserDenied() {
        SecurityScenarioContext unauthorized = SecurityScenarioContext.unauthorized();

        assertThat(unauthorized.grantedAuthorities()).isEmpty();
        assertThat(unauthorized.hasAuthority(HarmoniaAuthorityEnum.PROVIDER_READ)).isFalse();

        ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                .principal(unauthorized.principal())
                .authorities(unauthorized.grantedAuthorities())
                .action(ThemisAction.READ)
                .target(practitionerResource)
                .context(unauthorized.securityContext())
                .build();

        ThemisAuthorizationDecision decision = evaluator.authorize(request);
        assertThat(decision.isDenied()).isTrue();
    }

    @Test
    @DisplayName("Unauthenticated context has null principal and is denied")
    void unauthenticatedContextDenied() {
        SecurityScenarioContext unauth = SecurityScenarioContext.unauthenticated();

        assertThat(unauth.principal()).isNull();

        ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                .principal(null)
                .authorities(Set.of())
                .action(ThemisAction.READ)
                .target(practitionerResource)
                .context(unauth.securityContext())
                .build();

        ThemisAuthorizationDecision decision = evaluator.authorize(request);
        assertThat(decision.isDenied()).isTrue();
    }

    @Test
    @DisplayName("Simulation test seams: Revoked authority filters out targeted authority")
    void testSeamRevokedAuthority() {
        ThemisSecurityContext revokedContext = ParadeigmaSecurityActors.revokedAuthorityContext(
                ParadeigmaSecurityActors.PROVIDER_STEWARD_PRINCIPAL,
                HarmoniaAuthorityEnum.PROVIDER_CHANGE_SUBMIT
        );

        String authoritiesAttr = revokedContext.attributes().get("authorities");
        assertThat(authoritiesAttr).isNotNull();
        assertThat(authoritiesAttr).doesNotContain(HarmoniaAuthorityEnum.PROVIDER_CHANGE_SUBMIT.getCode());
        assertThat(authoritiesAttr).contains(HarmoniaAuthorityEnum.PROVIDER_READ.getCode());
    }

    @Test
    @DisplayName("Integration Service has execution and processing authorities")
    void integrationServicePrivileges() {
        SecurityScenarioContext service = SecurityScenarioContext.integrationService();

        assertThat(service.hasAuthority(HarmoniaAuthorityEnum.SYSTEM_INTEGRATION)).isTrue();
        assertThat(service.hasAuthority(HarmoniaAuthorityEnum.PROVIDER_CHANGE_PROCESS)).isTrue();
    }
}
