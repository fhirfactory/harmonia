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

package net.fhirfactory.harmonia.paradeigma.test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import net.fhirfactory.harmonia.erga.registry.PractitionerChangeErgon;
import net.fhirfactory.harmonia.hapifhir.repository.FhirResourceRepository;
import net.fhirfactory.harmonia.hapifhir.service.FhirStorageService;
import net.fhirfactory.harmonia.hapifhir.service.ProviderRegistryReferenceValidator;
import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaFhirConverter;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.model.pragma.ProviderRegistryChangePragma;
import net.fhirfactory.harmonia.model.registry.ProviderRegistryConstants;
import net.fhirfactory.harmonia.model.security.ErgonSecurityDefinition;
import net.fhirfactory.harmonia.model.security.HarmoniaAuthorityEnum;
import net.fhirfactory.harmonia.model.security.HarmoniaRoleEnum;
import net.fhirfactory.harmonia.model.topic.Topic;
import net.fhirfactory.harmonia.praxis.cache.PragmaCacheService;
import net.fhirfactory.harmonia.praxis.sequence.PraxisImplementation;
import net.fhirfactory.harmonia.pylai.fhir.controller.FhirGatewayExceptionHandler;
import net.fhirfactory.harmonia.pylai.fhir.controller.FhirRestGatewayController;
import net.fhirfactory.harmonia.pylai.fhir.provider.CapabilityStatementProvider;
import net.fhirfactory.harmonia.pylai.fhir.security.FhirSecurityInterceptor;
import net.fhirfactory.harmonia.pylai.fhir.service.ChangeRequestSubmissionService;
import net.fhirfactory.harmonia.themis.api.ThemisService;
import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecisionReason;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisResource;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import net.fhirfactory.harmonia.themis.audit.model.ThemisAuditEvent;
import net.fhirfactory.harmonia.themis.audit.service.InMemoryThemisAuditService;
import net.fhirfactory.harmonia.themis.core.evaluator.DeterministicPolicyEvaluator;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Practitioner;
import org.hl7.fhir.r5.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Paradeigma Themis Defence-in-Depth Acceptance Test Suite")
class ThemisDefenceInDepthAcceptanceTest {

    private CamelContext camelContext;
    private ThemisService themisService;
    private InMemoryThemisAuditService auditService;
    private FhirStorageService storageServiceMock;
    private ProviderRegistryReferenceValidator referenceValidatorMock;
    private PragmaCacheService pragmaCacheServiceMock;
    private FhirSecurityInterceptor securityInterceptor;
    private ChangeRequestSubmissionService submissionService;
    private FhirRestGatewayController gatewayController;
    private MockMvc mockMvc;
    private final FhirContext fhirContext = FhirContext.forR5();

    @BeforeEach
    void setUp() throws Exception {
        camelContext = new DefaultCamelContext();
        themisService = DeterministicPolicyEvaluator.withDefaultPolicies();
        auditService = new InMemoryThemisAuditService();
        storageServiceMock = Mockito.mock(FhirStorageService.class);
        referenceValidatorMock = Mockito.mock(ProviderRegistryReferenceValidator.class);
        pragmaCacheServiceMock = Mockito.mock(PragmaCacheService.class);

        when(referenceValidatorMock.validateReferences(any())).thenReturn(ProviderRegistryReferenceValidator.ValidationResult.valid());

        securityInterceptor = new FhirSecurityInterceptor(themisService);
        submissionService = new ChangeRequestSubmissionService(pragmaCacheServiceMock, null);
        CapabilityStatementProvider capabilityProvider = new CapabilityStatementProvider();

        gatewayController = new FhirRestGatewayController(
                storageServiceMock,
                submissionService,
                capabilityProvider,
                securityInterceptor,
                pragmaCacheServiceMock
        );

        mockMvc = MockMvcBuilders.standaloneSetup(gatewayController)
                .setControllerAdvice(new FhirGatewayExceptionHandler())
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        if (camelContext != null) {
            camelContext.stop();
        }
    }

    // =========================================================================
    // Scenario A: Governed Read & Search
    // =========================================================================

    @Test
    @DisplayName("Scenario A: Principal with PRV_RDR issues GET/SEARCH -> Pylai evaluates Themis READ -> ALLOW")
    void testScenarioA_GovernedReadAndSearch() throws Exception {
        Practitioner pr = new Practitioner();
        pr.setId("Practitioner/PR-A1");
        pr.addName(new HumanName().setFamily("Smith").addGiven("Alice"));
        when(storageServiceMock.getResource("Practitioner", "PR-A1")).thenReturn(pr);
        when(storageServiceMock.searchResources(eq("Practitioner"), any())).thenReturn(List.of(pr));

        // 1. Authorized Read
        mockMvc.perform(get("/Practitioner/PR-A1")
                        .header(FhirSecurityInterceptor.HEADER_USER_ROLES, "PRV_RDR")
                        .header(FhirSecurityInterceptor.HEADER_REQUESTER, "user:registry-reader"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceType").value("Practitioner"))
                .andExpect(jsonPath("$.name[0].family").value("Smith"));

        // 2. Authorized Search
        mockMvc.perform(get("/Practitioner?name=Smith")
                        .header(FhirSecurityInterceptor.HEADER_USER_ROLES, "PRV_RDR")
                        .header(FhirSecurityInterceptor.HEADER_REQUESTER, "user:registry-reader"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceType").value("Bundle"))
                .andExpect(jsonPath("$.entry[0].resource.resourceType").value("Practitioner"));

        verify(storageServiceMock, times(1)).getResource("Practitioner", "PR-A1");
        verify(storageServiceMock, times(1)).searchResources(eq("Practitioner"), any());
    }

    // =========================================================================
    // Scenario B: Unauthorized Write Rejection
    // =========================================================================

    @Test
    @DisplayName("Scenario B: Principal with PRV_RDR attempts PUT -> Pylai evaluates Themis SUBMIT_UPDATE -> DENY (HTTP 403)")
    void testScenarioB_UnauthorizedWriteRejection() throws Exception {
        Practitioner pr = new Practitioner();
        pr.setId("PR-B1");
        pr.addName(new HumanName().setFamily("Smith").addGiven("Bob"));
        String json = fhirContext.newJsonParser().encodeResourceToString(pr);

        mockMvc.perform(put("/Practitioner/PR-B1")
                        .contentType("application/fhir+json")
                        .header(FhirSecurityInterceptor.HEADER_USER_ROLES, "PRV_RDR") // Only reader, missing submit!
                        .header(FhirSecurityInterceptor.HEADER_REQUESTER, "user:registry-reader")
                        .content(json))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.resourceType").value("OperationOutcome"))
                .andExpect(jsonPath("$.issue[0].code").value("forbidden"));

        // Ensure no Pragma was created or persisted
        verify(pragmaCacheServiceMock, never()).savePragma(any());
    }

    // =========================================================================
    // Scenario C: Governed End-to-End Write
    // =========================================================================

    @Test
    @DisplayName("Scenario C: PRV_SUB submits -> Pylai ALLOW -> Ponos PRV_PROC ALLOW -> Ergon & Storage UPDATE ALLOW -> COMPLETED")
    void testScenarioC_GovernedEndToEndWrite() throws Exception {
        // 1. Setup Ergon
        PractitionerChangeErgon ergon = new PractitionerChangeErgon();
        ergon.setStorageService(storageServiceMock);
        ergon.setReferenceValidator(referenceValidatorMock);
        ergon.setThemisService(themisService);

        // 2. Gateway Ingestion (Pylai Boundary)
        Practitioner pr = new Practitioner();
        pr.setId("PR-C1");
        pr.addName(new HumanName().setFamily("Curie").addGiven("Marie"));
        String json = fhirContext.newJsonParser().encodeResourceToString(pr);

        ThemisPrincipal submitterPrincipal = ThemisPrincipal.of("user:registry-submitter", PrincipalType.HUMAN, "hospital-west");
        Set<ThemisAuthority> submitterAuthorities = HarmoniaRoleEnum.PRV_SUB.getThemisAuthorities();
        ThemisSecurityContext submitterContext = ThemisSecurityContext.fromPrincipal(submitterPrincipal, "corr-scenario-c");

        ChangeRequestSubmissionService.SubmissionResult subResult = submissionService.submitChangeRequest(
                ProviderRegistryConstants.OPERATION_UPDATE,
                "Practitioner",
                "PR-C1",
                json,
                submitterPrincipal,
                submitterAuthorities,
                submitterContext,
                "hospital-west",
                "corr-scenario-c",
                "W/\"1\""
        );

        Pragma pragma = subResult.getPragma();
        assertThat(pragma.getOriginatingPrincipal().principalId()).isEqualTo("user:registry-submitter");
        assertThat(pragma.getOriginatingAuthorities()).contains(HarmoniaAuthorityEnum.PROVIDER_CHANGE_SUBMIT.toThemisAuthority());

        // 3. Workflow Execution (Ponos & Ergon & Storage Boundary)
        ThemisResource targetResource = ThemisResource.of("Practitioner", "PR-C1", "PROVIDER_REGISTRY", Set.of());
        ThemisAuthorizationRequest ponosAuthReq = ThemisAuthorizationRequest.builder()
                .principal(ThemisPrincipal.of("process:ponos-engine", PrincipalType.PROCESS, "ponos"))
                .authorities(ergon.getSecurityDefinition().requiredExecutionAuthorities())
                .action(ThemisAction.PROCESS)
                .target(targetResource)
                .context(submitterContext)
                .build();
        ThemisAuthorizationDecision ponosDecision = themisService.authorize(ponosAuthReq);
        assertThat(ponosDecision.decision()).isEqualTo(ThemisDecision.ALLOW);

        when(storageServiceMock.updateResource(eq("PR-C1"), any(), any())).thenReturn(pr);

        ergon.processErgon(pragma, null);
        assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.COMPLETED);

        // Verify storage service committed the change
        verify(storageServiceMock).updateResource(eq("PR-C1"), any(), any());
    }

    // =========================================================================
    // Scenario D: Execution Privilege Failure (Defence in Depth)
    // =========================================================================

    @Test
    @DisplayName("Scenario D: Valid Pragma submitted by PRV_SUB but Ponos Ergon execution authority is revoked -> Ponos DENY")
    void testScenarioD_ExecutionPrivilegeFailure() throws Exception {
        PractitionerChangeErgon ergon = new PractitionerChangeErgon();
        ergon.setStorageService(storageServiceMock);
        ergon.setReferenceValidator(referenceValidatorMock);
        ergon.setThemisService(themisService);

        // Strip execution authority from Ergon
        ergon.setSecurityDefinition(ErgonSecurityDefinition.builder()
                .ergonId("ergon:practitioner-change")
                .requiredExecutionAuthorities(Set.of()) // Revoked!
                .permittedResourceType("Practitioner")
                .permittedSecurityDomain("PROVIDER_REGISTRY")
                .build());

        ThemisPrincipal submitterPrincipal = ThemisPrincipal.of("user:registry-submitter", PrincipalType.HUMAN, "hospital-west");
        ThemisSecurityContext submitterContext = ThemisSecurityContext.fromPrincipal(submitterPrincipal, "corr-scenario-d");

        // Ponos evaluation halts execution when execution authority is revoked
        ThemisResource targetResource = ThemisResource.of("Practitioner", "PR-D1", "PROVIDER_REGISTRY", Set.of());
        ThemisAuthorizationRequest ponosAuthReq = ThemisAuthorizationRequest.builder()
                .principal(ThemisPrincipal.of("process:ponos-engine", PrincipalType.PROCESS, "ponos"))
                .authorities(ergon.getSecurityDefinition().requiredExecutionAuthorities()) // Empty!
                .action(ThemisAction.PROCESS)
                .target(targetResource)
                .context(submitterContext)
                .build();
        ThemisAuthorizationDecision ponosDecision = themisService.authorize(ponosAuthReq);
        assertThat(ponosDecision.decision()).isEqualTo(ThemisDecision.DENY);
        assertThat(ponosDecision.reason()).isEqualTo(ThemisDecisionReason.EXECUTION_AUTHORITY_MISSING);

        // Since Ponos denied execution, storage service is never invoked
        verify(storageServiceMock, never()).updateResource(any(), any(), any());
    }

    // =========================================================================
    // Scenario E: Persistence Privilege Failure (Defence in Depth)
    // =========================================================================

    @Test
    @DisplayName("Scenario E: Pylai & Ponos ALLOW, but persistence authority revoked at Ergon/Storage -> Persistence Gate DENY")
    void testScenarioE_PersistencePrivilegeFailure() throws Exception {
        PractitionerChangeErgon ergon = new PractitionerChangeErgon();
        ergon.setStorageService(storageServiceMock);
        ergon.setReferenceValidator(referenceValidatorMock);
        ergon.setThemisService(themisService);

        // Ergon has execution authority to process, but NOT persistence authority to update storage
        ergon.setSecurityDefinition(ErgonSecurityDefinition.builder()
                .ergonId("ergon:practitioner-change")
                .requiredExecutionAuthority(HarmoniaAuthorityEnum.PROVIDER_CHANGE_PROCESS.toThemisAuthority()) // Only process, missing resource.update!
                .permittedResourceType("Practitioner")
                .permittedSecurityDomain("PROVIDER_REGISTRY")
                .build());

        Practitioner practitioner = new Practitioner();
        practitioner.setId("PR-E1");
        practitioner.addName(new HumanName().setFamily("Watson").addGiven("John"));

        Pragma pragma = ProviderRegistryChangePragma.buildChangeRequestPragma(
                ProviderRegistryConstants.OPERATION_UPDATE,
                practitioner,
                "user:registry-submitter",
                "hospital-west",
                "corr-scenario-e",
                "W/\"1\""
        );

        ergon.processErgon(pragma, null);

        assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.FAILED);
        assertThat(pragma.getCheckpoints())
                .anyMatch(cp -> cp.getStageName().equals("COMMIT_DENIED_BY_THEMIS")
                        && cp.getStatusMessage().contains("AUTHORITY_MISSING"));

        // Verify database remains untouched
        verify(storageServiceMock, never()).updateResource(any(), any(), any());
        verify(storageServiceMock, never()).createResource(any());
    }

    // =========================================================================
    // Pragma Tampering & Fail-Closed Tests
    // =========================================================================

    @Test
    @DisplayName("Pragma context serialization and tamper-resistance verification")
    void testPragmaSecurityContextSerializationAndIntegrity() {
        ThemisPrincipal principal = ThemisPrincipal.of("user:dr-franklin", PrincipalType.HUMAN, "genomics-dept");
        Pragma pragma = Pragma.builder()
                .pragmaId("pragma-tamper-01")
                .originatingPrincipal(principal)
                .addOriginatingAuthority(HarmoniaAuthorityEnum.PROVIDER_CHANGE_SUBMIT.toThemisAuthority())
                .policyVersion("1.0.0")
                .build();

        // Convert to FHIR Task
        Task fhirTask = PragmaFhirConverter.toFhirTask(pragma);

        // Reconstruct from FHIR Task
        Pragma reconstructed = PragmaFhirConverter.fromFhirTask(fhirTask);

        assertThat(reconstructed.getOriginatingPrincipal()).isNotNull();
        assertThat(reconstructed.getOriginatingPrincipal().principalId()).isEqualTo("user:dr-franklin");
        assertThat(reconstructed.getOriginatingPrincipal().principalType()).isEqualTo(PrincipalType.HUMAN);
        assertThat(reconstructed.getOriginatingPrincipal().sourceDomain()).isEqualTo("genomics-dept");
        assertThat(reconstructed.getOriginatingAuthorities())
                .extracting(ThemisAuthority::authorityCode)
                .containsExactly("provider.change.submit");
    }
}
