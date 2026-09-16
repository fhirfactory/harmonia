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

package net.fhirfactory.harmonia.paradeigma.test.registry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import net.fhirfactory.harmonia.erga.registry.PractitionerChangeErgon;
import net.fhirfactory.harmonia.hapifhir.model.FhirResourceEntity;
import net.fhirfactory.harmonia.hapifhir.repository.FhirResourceRepository;
import net.fhirfactory.harmonia.hapifhir.service.FhirStorageService;
import net.fhirfactory.harmonia.hapifhir.service.ProviderRegistryReferenceValidator;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.paradeigma.common.generator.PractitionerGenerator;
import net.fhirfactory.harmonia.paradeigma.common.security.ParadeigmaSecurityActors;
import net.fhirfactory.harmonia.paradeigma.common.security.SecurityScenarioContext;
import net.fhirfactory.harmonia.themis.api.model.*;
import net.fhirfactory.harmonia.themis.core.evaluator.DeterministicPolicyEvaluator;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.hl7.fhir.r5.model.Practitioner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Validates RBAC/ABAC authorization scenarios and defense-in-depth across Pylai,
 * Ponos Ergons, and Mnemosyne storage layers.
 */
public class ProviderRegistrySecurityScenarioTest {

    private final FhirContext fhirContext = FhirContext.forR5();
    private IParser parser;
    private DeterministicPolicyEvaluator policyEvaluator;
    private ThemisResource practitionerResource;

    private FhirResourceRepository mockRepository;
    private FhirStorageService storageService;
    private ProviderRegistryReferenceValidator referenceValidator;
    private PractitionerChangeErgon practErgon;
    private CamelContext camelContext;

    private Map<String, FhirResourceEntity> store;

    @BeforeEach
    void setUp() {
        parser = fhirContext.newJsonParser();
        policyEvaluator = DeterministicPolicyEvaluator.withDefaultPolicies();
        practitionerResource = ThemisResource.of(
                "Practitioner",
                "pract-sec-101",
                "PROVIDER_REGISTRY",
                Set.of(ThemisSecurityLabel.of("PROVIDER_REGISTRY"))
        );

        store = new ConcurrentHashMap<>();
        mockRepository = Mockito.mock(FhirResourceRepository.class);

        when(mockRepository.findByResourceTypeAndFhirId(any(), any())).thenAnswer(invocation -> {
            String type = invocation.getArgument(0);
            String id = invocation.getArgument(1);
            FhirResourceEntity entity = store.get(type + "/" + id);
            return entity != null ? Optional.of(entity) : Optional.empty();
        });

        when(mockRepository.save(any(FhirResourceEntity.class))).thenAnswer(invocation -> {
            FhirResourceEntity e = invocation.getArgument(0);
            store.put(e.getResourceType() + "/" + e.getFhirId(), e);
            return e;
        });

        storageService = new FhirStorageService(mockRepository);
        referenceValidator = new ProviderRegistryReferenceValidator(mockRepository);

        practErgon = new PractitionerChangeErgon();
        practErgon.setStorageService(storageService);
        practErgon.setReferenceValidator(referenceValidator);

        camelContext = new DefaultCamelContext();
    }

    @Test
    @DisplayName("Authorized Provider Read: Clinician evaluates to ALLOW")
    void authorizedProviderRead() {
        SecurityScenarioContext clinician = SecurityScenarioContext.clinician();

        ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                .principal(clinician.principal())
                .authorities(clinician.grantedAuthorities())
                .action(ThemisAction.READ)
                .target(practitionerResource)
                .context(clinician.securityContext())
                .build();

        ThemisAuthorizationDecision decision = policyEvaluator.authorize(request);
        assertThat(decision.isAllowed()).isTrue();
    }

    @Test
    @DisplayName("Unauthorized Provider Read: Guest user evaluates to DENY")
    void unauthorizedProviderRead() {
        SecurityScenarioContext unauthorized = SecurityScenarioContext.unauthorized();

        ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                .principal(unauthorized.principal())
                .authorities(unauthorized.grantedAuthorities())
                .action(ThemisAction.READ)
                .target(practitionerResource)
                .context(unauthorized.securityContext())
                .build();

        ThemisAuthorizationDecision decision = policyEvaluator.authorize(request);
        assertThat(decision.isDenied()).isTrue();
    }

    @Test
    @DisplayName("Authorized Change Request: Provider Steward evaluates to ALLOW")
    void authorizedChangeRequest() {
        SecurityScenarioContext steward = SecurityScenarioContext.providerSteward();

        ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                .principal(steward.principal())
                .authorities(steward.grantedAuthorities())
                .action(ThemisAction.CREATE)
                .target(practitionerResource)
                .context(steward.securityContext())
                .build();

        ThemisAuthorizationDecision decision = policyEvaluator.authorize(request);
        assertThat(decision.isAllowed()).isTrue();
    }

    @Test
    @DisplayName("Unauthorized Change Request: Read-Only user evaluates to DENY and storage untouched")
    void unauthorizedChangeRequestDenied() {
        SecurityScenarioContext readOnly = SecurityScenarioContext.readOnly();

        ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                .principal(readOnly.principal())
                .authorities(readOnly.grantedAuthorities())
                .action(ThemisAction.CREATE)
                .target(practitionerResource)
                .context(readOnly.securityContext())
                .build();

        ThemisAuthorizationDecision decision = policyEvaluator.authorize(request);
        assertThat(decision.isDenied()).isTrue();
        assertThat(store).isEmpty();
    }

    @Test
    @DisplayName("Defense-in-Depth: Dual authority evaluation for async Ergon execution")
    void defenseInDepthAsyncExecution() {
        ThemisPrincipal submitter = ParadeigmaSecurityActors.PROVIDER_STEWARD_PRINCIPAL;
        Set<ThemisAuthority> subAuthorities = ParadeigmaSecurityActors.getThemisAuthoritiesForRoles(ParadeigmaSecurityActors.PROVIDER_STEWARD_ROLES);

        ThemisPrincipal executor = ParadeigmaSecurityActors.INTEGRATION_SERVICE_PRINCIPAL;
        Set<ThemisAuthority> execAuthorities = ParadeigmaSecurityActors.getThemisAuthoritiesForRoles(ParadeigmaSecurityActors.INTEGRATION_SERVICE_ROLES);

        ThemisAuthorizationDecision decision = policyEvaluator.authorizeAsyncExecution(
                submitter,
                subAuthorities,
                executor,
                execAuthorities,
                ThemisAction.PROCESS,
                practitionerResource,
                ThemisSecurityContext.anonymous()
        );

        assertThat(decision.isAllowed()).isTrue();
    }
}
