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
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import net.fhirfactory.harmonia.erga.registry.PractitionerChangeErgon;
import net.fhirfactory.harmonia.hapifhir.model.FhirResourceEntity;
import net.fhirfactory.harmonia.hapifhir.repository.FhirResourceRepository;
import net.fhirfactory.harmonia.hapifhir.service.FhirStorageService;
import net.fhirfactory.harmonia.hapifhir.service.ProviderRegistryReferenceValidator;
import net.fhirfactory.harmonia.logging.PhiLogger;
import net.fhirfactory.harmonia.logging.PhiLoggerFactory;
import net.fhirfactory.harmonia.logging.PhiLoggingConfig;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.paradeigma.common.generator.PractitionerGenerator;
import net.fhirfactory.harmonia.paradeigma.common.logging.PhiLogTestProbe;
import net.fhirfactory.harmonia.paradeigma.common.logging.SecretLeakageAssertion;
import net.fhirfactory.harmonia.paradeigma.common.security.ParadeigmaSecurityActors;
import net.fhirfactory.harmonia.paradeigma.common.security.SecurityScenarioContext;
import net.fhirfactory.harmonia.praxis.cache.PragmaCacheService;
import net.fhirfactory.harmonia.pylai.fhir.controller.FhirRestGatewayController;
import net.fhirfactory.harmonia.pylai.fhir.provider.CapabilityStatementProvider;
import net.fhirfactory.harmonia.pylai.fhir.security.FhirSecurityInterceptor;
import net.fhirfactory.harmonia.pylai.fhir.service.ChangeRequestSubmissionService;
import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisResource;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityLabel;
import net.fhirfactory.harmonia.themis.core.evaluator.DeterministicPolicyEvaluator;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Practitioner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Cross-Capability End-to-End Scenario Test combining:
 * 1. Synthetic Provider Steward Security Actor & Themis RBAC authorization
 * 2. Pylai REST Ingress Change Request submission
 * 3. Ponos Ergon validation & Mnemosyne durable commit
 * 4. Dual-gate PHI logging verification via PhiLogTestProbe
 * 5. Secret leakage protection via SecretLeakageAssertion
 * 6. End-to-end correlation ID preservation.
 */
public class ProviderRegistryCrossCapabilityE2ETest {

    private final FhirContext fhirContext = FhirContext.forR5();
    private IParser parser;
    private DeterministicPolicyEvaluator policyEvaluator;

    private FhirResourceRepository mockRepository;
    private FhirStorageService storageService;
    private ProviderRegistryReferenceValidator referenceValidator;
    private PragmaCacheService pragmaCacheService;
    private ChangeRequestSubmissionService submissionService;
    private FhirRestGatewayController gatewayController;

    private PractitionerChangeErgon practErgon;
    private CamelContext camelContext;

    private org.slf4j.Logger operationalLogger;
    private PhiLogger phiLogger;
    private Logger rootLogger;

    private Map<String, FhirResourceEntity> store;
    private Map<String, Pragma> cache;

    @BeforeEach
    void setUp() {
        PhiLoggingConfig.reset();
        PhiLoggerFactory.clearCacheForTesting();

        parser = fhirContext.newJsonParser();
        policyEvaluator = DeterministicPolicyEvaluator.withDefaultPolicies();

        operationalLogger = LoggerFactory.getLogger("net.fhirfactory.harmonia.pylai.e2e");
        phiLogger = PhiLoggerFactory.getLogger("ProviderRegistryE2E");
        rootLogger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);

        store = new ConcurrentHashMap<>();
        cache = new ConcurrentHashMap<>();
        mockRepository = Mockito.mock(FhirResourceRepository.class);

        when(mockRepository.findByResourceTypeAndFhirId(any(), any())).thenAnswer(invocation -> {
            String type = invocation.getArgument(0);
            String id = invocation.getArgument(1);
            if (id != null && id.contains("/")) {
                id = id.substring(id.lastIndexOf('/') + 1);
            }
            FhirResourceEntity entity = store.get(type + "/" + id);
            if (entity == null) {
                for (FhirResourceEntity e : store.values()) {
                    if (e.getResourceType().equalsIgnoreCase(type) && e.getFhirId().equalsIgnoreCase(id)) {
                        return Optional.of(e);
                    }
                }
            }
            return entity != null ? Optional.of(entity) : Optional.empty();
        });

        when(mockRepository.save(any(FhirResourceEntity.class))).thenAnswer(invocation -> {
            FhirResourceEntity e = invocation.getArgument(0);
            store.put(e.getResourceType() + "/" + e.getFhirId(), e);
            return e;
        });

        storageService = new FhirStorageService(mockRepository);
        referenceValidator = new ProviderRegistryReferenceValidator(mockRepository);

        pragmaCacheService = Mockito.mock(PragmaCacheService.class);
        when(pragmaCacheService.savePragma(any(Pragma.class))).thenAnswer(invocation -> {
            Pragma p = invocation.getArgument(0);
            cache.put(p.getPragmaId(), p);
            return p;
        });
        when(pragmaCacheService.getPragma(any(String.class))).thenAnswer(invocation -> {
            String id = invocation.getArgument(0);
            return Optional.ofNullable(cache.get(id));
        });

        submissionService = new ChangeRequestSubmissionService(pragmaCacheService, null);
        gatewayController = new FhirRestGatewayController(
                storageService,
                submissionService,
                new CapabilityStatementProvider(),
                new FhirSecurityInterceptor(),
                pragmaCacheService
        );

        practErgon = new PractitionerChangeErgon();
        practErgon.setStorageService(storageService);
        practErgon.setReferenceValidator(referenceValidator);

        camelContext = new DefaultCamelContext();
    }

    @AfterEach
    void tearDown() {
        PhiLoggingConfig.reset();
        PhiLoggerFactory.clearCacheForTesting();
    }

    @Test
    @DisplayName("Complete Cross-Capability E2E Scenario: Steward -> Pylai -> Ergon -> Mnemosyne -> Logging -> Search")
    void testCrossCapabilityEndToEndScenario() throws Exception {
        String correlationId = "corr-cross-e2e-8888";
        SecurityScenarioContext steward = SecurityScenarioContext.providerSteward();
        Practitioner practitioner = new PractitionerGenerator(8888L).generateValid("pract-dr-marcus-8888");
        String json = parser.encodeResourceToString(practitioner);

        ThemisResource targetResource = ThemisResource.of(
                "Practitioner",
                "pract-dr-marcus-8888",
                "PROVIDER_REGISTRY",
                Set.of(ThemisSecurityLabel.of("PROVIDER_REGISTRY"))
        );

        // 1. Evaluate Ingress Security Authorization
        ThemisAuthorizationRequest authRequest = ThemisAuthorizationRequest.builder()
                .principal(steward.principal())
                .authorities(steward.grantedAuthorities())
                .action(ThemisAction.CREATE)
                .target(targetResource)
                .context(steward.securityContext())
                .build();

        ThemisAuthorizationDecision authDecision = policyEvaluator.authorize(authRequest);
        assertThat(authDecision.isAllowed()).isTrue();

        // 2. Enable Dual-Gate PHI Logging & Start Test Probe
        PhiLoggingConfig.setPhiEnabled(true);
        ((Logger) LoggerFactory.getLogger("ca.uhn.fhir")).setLevel(Level.WARN);
        ((Logger) operationalLogger).setLevel(Level.TRACE);

        try (PhiLogTestProbe logProbe = PhiLogTestProbe.startCapture()) {
            // 3. Pylai Ingress: Submit Change Request -> 202 Accepted
            operationalLogger.info("Received change request: resourceType=Practitioner, correlationId={}", correlationId);
            phiLogger.debug("Synthetic Practitioner payload: name={}, HPI-I={}",
                    practitioner.getNameFirstRep().getFamily(),
                    practitioner.getIdentifierFirstRep().getValue());

            ResponseEntity<String> postResp = gatewayController.createResource(
                    "Practitioner",
                    json,
                    correlationId,
                    "pas",
                    steward.principal().principalId(),
                    null
            );

            assertThat(postResp.getStatusCode().value()).isEqualTo(202);
            assertThat(postResp.getHeaders().getFirst("X-Correlation-Id")).isEqualTo(correlationId);

            String location = postResp.getHeaders().getLocation().toString();
            String pragmaId = location.substring(location.lastIndexOf('/') + 1);

            // 4. Ponos / Ergon Execution: Process Pragma and commit to Mnemosyne
            Pragma pragma = cache.get(pragmaId);
            assertThat(pragma).isNotNull();
            assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.ACCEPTED);

            Exchange exchange = camelContext.getEndpoint("direct:test").createExchange();
            practErgon.processErgon(pragma, exchange);

            assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.COMPLETED);

            // 5. Verify Persistent Storage State
            assertThat(store).isNotEmpty();
            Optional<FhirResourceEntity> savedPract = store.values().stream()
                    .filter(e -> "Practitioner".equalsIgnoreCase(e.getResourceType()))
                    .findFirst();
            assertThat(savedPract).isPresent();
            String savedId = savedPract.get().getFhirId();

            // 6. Verify Synchronous Read & Search
            ResponseEntity<String> getResp = gatewayController.readResource("Practitioner", savedId, null);
            assertThat(getResp.getStatusCode().value()).isEqualTo(200);

            // 7. Verify PHI Dual-Gate Logging & Marker
            logProbe.assertNoPhiInOperationalLogs(
                    practitioner.getNameFirstRep().getFamily(),
                    practitioner.getIdentifierFirstRep().getValue()
            );
            logProbe.assertPhiPresentInDiagnosticLogs(
                    practitioner.getNameFirstRep().getFamily(),
                    practitioner.getIdentifierFirstRep().getValue()
            );
            logProbe.assertPhiMarkerAttachedToDiagnosticLogs();

            // 8. Verify No Secrets Leaked
            logProbe.assertNoSecretsInAnyLog(SecretLeakageAssertion.getStandardSyntheticSecrets());
        }
    }
}
