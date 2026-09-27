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

package net.fhirfactory.harmonia.pylai.fhir.controller;

import ca.uhn.fhir.context.FhirContext;
import net.fhirfactory.harmonia.hapifhir.service.FhirStorageService;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaCheckpoint;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.model.pragma.ProviderRegistryChangePragma;
import net.fhirfactory.harmonia.model.registry.ProviderRegistryConstants;
import net.fhirfactory.harmonia.praxis.cache.PragmaCacheService;
import net.fhirfactory.harmonia.pylai.fhir.provider.CapabilityStatementProvider;
import net.fhirfactory.harmonia.pylai.fhir.publication.FhirPublicationPolicy;
import net.fhirfactory.harmonia.pylai.fhir.publication.PylaiFhirPublicationProjector;
import net.fhirfactory.harmonia.pylai.fhir.security.FhirSecurityInterceptor;
import net.fhirfactory.harmonia.pylai.fhir.service.ChangeRequestSubmissionService;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Extension;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Practitioner;
import org.hl7.fhir.r5.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Pylai FHIR REST Gateway Controller Tests")
class FhirRestGatewayControllerTest {

    private MockMvc mockMvc;
    private FhirStorageService storageService;
    private ChangeRequestSubmissionService submissionService;
    private CapabilityStatementProvider capabilityStatementProvider;
    private FhirSecurityInterceptor securityInterceptor;
    private PragmaCacheService pragmaCacheService;
    private final FhirContext fhirContext = FhirContext.forR5();

    private static RequestPostProcessor auth(String principalName, String... roles) {
        return request -> {
            request.setUserPrincipal(() -> principalName);
            for (String role : roles) {
                request.addUserRole(role);
            }
            return request;
        };
    }

    @BeforeEach
    void setUp() {
        storageService = Mockito.mock(FhirStorageService.class);
        submissionService = Mockito.mock(ChangeRequestSubmissionService.class);
        capabilityStatementProvider = new CapabilityStatementProvider();
        securityInterceptor = new FhirSecurityInterceptor();
        pragmaCacheService = Mockito.mock(PragmaCacheService.class);

        FhirRestGatewayController controller = new FhirRestGatewayController(
                storageService,
                submissionService,
                capabilityStatementProvider,
                securityInterceptor,
                pragmaCacheService
        );

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new FhirGatewayExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /metadata returns 200 OK with FHIR CapabilityStatement without authentication")
    void testGetMetadata() throws Exception {
        mockMvc.perform(get("/metadata"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/fhir+json"))
                .andExpect(jsonPath("$.resourceType").value("CapabilityStatement"))
                .andExpect(jsonPath("$.status").value("active"))
                .andExpect(jsonPath("$.fhirVersion").value("5.0.0"));
    }

    @Test
    @DisplayName("GET /fhir/metadata returns 200 OK with FHIR CapabilityStatement without authentication")
    void testGetFhirMetadata() throws Exception {
        mockMvc.perform(get("/fhir/metadata"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/fhir+json"))
                .andExpect(jsonPath("$.resourceType").value("CapabilityStatement"))
                .andExpect(jsonPath("$.status").value("active"))
                .andExpect(jsonPath("$.fhirVersion").value("5.0.0"));
    }

    @Test
    @DisplayName("GET /Practitioner/{id} returns 200 OK with ETag when container-authenticated with PRV_RDR")
    void testReadPractitionerSuccess() throws Exception {
        Practitioner pr = new Practitioner();
        pr.setId("Practitioner/PR-100");
        pr.getMeta().setVersionId("1");
        pr.addName(new HumanName().setFamily("Smith").addGiven("John"));

        when(storageService.getResource("Practitioner", "PR-100")).thenReturn(pr);

        mockMvc.perform(get("/Practitioner/PR-100")
                        .with(auth("user:dr-smith", "PRV_RDR")))
                .andExpect(status().isOk())
                .andExpect(header().string("ETag", "W/\"1\""))
                .andExpect(jsonPath("$.resourceType").value("Practitioner"))
                .andExpect(jsonPath("$.name[0].family").value("Smith"));
    }

    @Test
    @DisplayName("GET /Practitioner performs multi-parameter search and returns searchset Bundle when container-authenticated with PRV_RDR")
    void testSearchPractitioners() throws Exception {
        Practitioner pr = new Practitioner();
        pr.setId("Practitioner/PR-101");
        pr.addName(new HumanName().setFamily("Smith").addGiven("Jane"));

        when(storageService.searchResources(eq("Practitioner"), any())).thenReturn(List.of(pr));

        mockMvc.perform(get("/Practitioner?name=Smith")
                        .with(auth("user:dr-smith", "PRV_RDR")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceType").value("Bundle"))
                .andExpect(jsonPath("$.type").value("searchset"))
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.entry[0].resource.resourceType").value("Practitioner"));
    }

    @Test
    @DisplayName("POST /Practitioner returns 202 Accepted with Task Location and Correlation ID when container-authenticated with PRV_SUB")
    void testCreatePractitionerAccepted() throws Exception {
        Practitioner pr = new Practitioner();
        pr.addName(new HumanName().setFamily("Doe").addGiven("John"));
        String json = fhirContext.newJsonParser().encodeResourceToString(pr);

        Pragma pragma = ProviderRegistryChangePragma.buildChangeRequestPragma(
                ProviderRegistryConstants.OPERATION_CREATE,
                pr,
                "admin",
                "test-system",
                "corr-post-1",
                null
        );

        ChangeRequestSubmissionService.SubmissionResult subResult =
                new ChangeRequestSubmissionService.SubmissionResult(
                        pragma.getPragmaId(),
                        "corr-post-1",
                        "/Task/" + pragma.getPragmaId(),
                        pragma
                );

        when(submissionService.submitChangeRequest(eq("CREATE"), eq("Practitioner"), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(subResult);

        mockMvc.perform(post("/Practitioner")
                        .contentType("application/fhir+json")
                        .header("X-Correlation-Id", "corr-post-1")
                        .with(auth("user:submitter", "PRV_SUB"))
                        .content(json))
                .andExpect(status().isAccepted())
                .andExpect(header().string("Location", "/Task/" + pragma.getPragmaId()))
                .andExpect(header().string("X-Correlation-Id", "corr-post-1"))
                .andExpect(header().string("Retry-After", "1"))
                .andExpect(jsonPath("$.resourceType").value("Task"));
    }

    @Test
    @DisplayName("GET /Task/{id} returns Task representing Pragma status when container-authenticated with PRV_RDR")
    void testGetTaskStatus() throws Exception {
        Practitioner pr = new Practitioner();
        pr.setId("PR-200");
        Pragma pragma = ProviderRegistryChangePragma.buildChangeRequestPragma(
                ProviderRegistryConstants.OPERATION_CREATE,
                pr,
                "admin",
                "test-system",
                "corr-task-1",
                null
        );
        pragma.setStatus(PragmaStatus.COMPLETED);

        when(pragmaCacheService.getPragma(pragma.getPragmaId())).thenReturn(Optional.of(pragma));

        mockMvc.perform(get("/Task/" + pragma.getPragmaId())
                        .with(auth("user:dr-smith", "PRV_RDR")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceType").value("Task"))
                .andExpect(jsonPath("$.status").value("completed"));
    }

    @Test
    @DisplayName("GET /Practitioner/{id} denied with 401 Unauthorized when request is unauthenticated (Fail-Closed)")
    void testReadDeniedUnauthenticated() throws Exception {
        mockMvc.perform(get("/Practitioner/PR-100"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.resourceType").value("OperationOutcome"))
                .andExpect(jsonPath("$.issue[0].severity").value("error"))
                .andExpect(jsonPath("$.issue[0].code").value("security"));
    }

    @Test
    @DisplayName("POST /Practitioner denied with 401 Unauthorized when request is unauthenticated")
    void testCreateDeniedUnauthenticated() throws Exception {
        Practitioner pr = new Practitioner();
        pr.addName(new HumanName().setFamily("Doe").addGiven("John"));
        String json = fhirContext.newJsonParser().encodeResourceToString(pr);

        mockMvc.perform(post("/Practitioner")
                        .contentType("application/fhir+json")
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.resourceType").value("OperationOutcome"))
                .andExpect(jsonPath("$.issue[0].severity").value("error"))
                .andExpect(jsonPath("$.issue[0].code").value("security"));
    }

    @Test
    @DisplayName("GET /Practitioner/{id} denied with 401 when caller attempts header spoofing without container principal")
    void testHeaderSpoofingRejectedWithoutContainerPrincipal() throws Exception {
        mockMvc.perform(get("/Practitioner/PR-100")
                        .header("X-Principal-Id", "admin")
                        .header("X-Requester", "superuser")
                        .header("X-User-Roles", "PRV_ADM, ROLE_ADMIN, *")
                        .header("X-Security-Scopes", "system/*.*"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.resourceType").value("OperationOutcome"));
    }

    @Test
    @DisplayName("POST /Practitioner denied with 403 Forbidden when caller only has PRV_RDR role")
    void testCreateDeniedForReader() throws Exception {
        Practitioner pr = new Practitioner();
        pr.addName(new HumanName().setFamily("Doe").addGiven("John"));
        String json = fhirContext.newJsonParser().encodeResourceToString(pr);

        mockMvc.perform(post("/Practitioner")
                        .contentType("application/fhir+json")
                        .with(auth("user:reader-only", "PRV_RDR"))
                        .content(json))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.resourceType").value("OperationOutcome"))
                .andExpect(jsonPath("$.issue[0].severity").value("error"))
                .andExpect(jsonPath("$.issue[0].code").value("forbidden"));
    }

    @Test
    @DisplayName("POST /Practitioner denied with 403 when caller with PRV_RDR role attempts role header injection")
    void testRoleHeaderInjectionIgnoredDeniedForbidden() throws Exception {
        Practitioner pr = new Practitioner();
        pr.addName(new HumanName().setFamily("Doe").addGiven("John"));
        String json = fhirContext.newJsonParser().encodeResourceToString(pr);

        mockMvc.perform(post("/Practitioner")
                        .contentType("application/fhir+json")
                        .header("X-User-Roles", "PRV_SUB, PRV_ADM, SYS_ADM, *")
                        .header("X-Security-Scopes", "system/*.*")
                        .with(auth("user:dr-smith", "PRV_RDR"))
                        .content(json))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.resourceType").value("OperationOutcome"))
                .andExpect(jsonPath("$.issue[0].code").value("forbidden"));
    }

    @Test
    @DisplayName("GET /Practitioner/{id} projects publication boundary: strips operational metadata, preserves clinical confidentiality, ETag, and Last-Modified (AX-05, AX-13)")
    void testReadPractitionerPublicationBoundaryProjection() throws Exception {
        Date updatedDate = new Date();
        Practitioner pr = new Practitioner();
        pr.setId("Practitioner/PR-999");
        pr.getMeta().setVersionId("42");
        pr.getMeta().setLastUpdated(updatedDate);

        // Clinical confidentiality security label (must be preserved)
        pr.getMeta().addSecurity(new Coding(
                FhirPublicationPolicy.HL7_CONFIDENTIALITY_SYSTEM,
                "R",
                "Restricted"
        ));
        // Harmonia operational security label (must be stripped)
        pr.getMeta().addSecurity(new Coding(
                "http://harmonia.fhirfactory.net/security/labels",
                "INTERNAL",
                "Harmonia Internal Governance"
        ));

        // Harmonia internal meta extension (must be stripped)
        pr.getMeta().addExtension(new Extension(
                "http://harmonia.fhirfactory.net/structure/authoritative-version",
                new StringType("v1.0.0-snapshot")
        ));

        // Permitted AU jurisdictional extension (must be preserved)
        pr.addExtension(new Extension(
                "http://hl7.org.au/fhir/StructureDefinition/au-practitioner-role-code",
                new StringType("253111")
        ));
        // Harmonia operational task/security extension (must be stripped)
        pr.addExtension(new Extension(
                "http://fhirfactory.net/harmonia/task/security/context",
                new StringType("op-sec-token-123")
        ));

        pr.addName(new HumanName().setFamily("Smith").addGiven("John"));

        when(storageService.getResource("Practitioner", "PR-999")).thenReturn(pr);

        mockMvc.perform(get("/Practitioner/PR-999")
                        .with(auth("user:dr-smith", "PRV_RDR")))
                .andExpect(status().isOk())
                .andExpect(header().string("ETag", "W/\"42\""))
                .andExpect(header().exists("Last-Modified"))
                .andExpect(jsonPath("$.resourceType").value("Practitioner"))
                .andExpect(jsonPath("$.meta.versionId").value("42"))
                // Clinical security label retained
                .andExpect(jsonPath("$.meta.security", hasSize(1)))
                .andExpect(jsonPath("$.meta.security[0].system").value(FhirPublicationPolicy.HL7_CONFIDENTIALITY_SYSTEM))
                .andExpect(jsonPath("$.meta.security[0].code").value("R"))
                // Meta extension stripped
                .andExpect(jsonPath("$.meta.extension").doesNotExist())
                // Permitted AU extension retained, operational extension stripped
                .andExpect(jsonPath("$.extension", hasSize(1)))
                .andExpect(jsonPath("$.extension[0].url").value("http://hl7.org.au/fhir/StructureDefinition/au-practitioner-role-code"))
                .andExpect(jsonPath("$.name[0].family").value("Smith"));

        // Assert non-destructive deep copy: source instance in storage memory must remain untouched
        assertEquals(2, pr.getMeta().getSecurity().size(), "Source instance security labels must not be mutated");
        assertEquals(1, pr.getMeta().getExtension().size(), "Source instance meta extensions must not be mutated");
        assertEquals(2, pr.getExtension().size(), "Source instance root extensions must not be mutated");
    }

    @Test
    @DisplayName("GET /Practitioner searchset bundle projects publication boundary across all constituent entries")
    void testSearchPractitionersPublicationBoundaryProjection() throws Exception {
        Practitioner pr = new Practitioner();
        pr.setId("Practitioner/PR-SEARCH-1");
        pr.getMeta().setVersionId("1");
        pr.getMeta().addSecurity(new Coding(
                FhirPublicationPolicy.HL7_CONFIDENTIALITY_SYSTEM,
                "N",
                "Normal"
        ));
        pr.getMeta().addSecurity(new Coding(
                "http://harmonia.fhirfactory.net/security/labels",
                "INTERNAL",
                "Harmonia Internal"
        ));
        pr.addExtension(new Extension(
                "http://fhirfactory.net/harmonia/task/praxis-id",
                new StringType("praxis-exec-001")
        ));
        pr.addName(new HumanName().setFamily("Jones").addGiven("Alice"));

        when(storageService.searchResources(eq("Practitioner"), any())).thenReturn(List.of(pr));

        mockMvc.perform(get("/Practitioner?name=Jones")
                        .with(auth("user:dr-smith", "PRV_RDR")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceType").value("Bundle"))
                .andExpect(jsonPath("$.type").value("searchset"))
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.entry[0].resource.resourceType").value("Practitioner"))
                .andExpect(jsonPath("$.entry[0].resource.meta.security", hasSize(1)))
                .andExpect(jsonPath("$.entry[0].resource.meta.security[0].system").value(FhirPublicationPolicy.HL7_CONFIDENTIALITY_SYSTEM))
                .andExpect(jsonPath("$.entry[0].resource.extension").doesNotExist())
                .andExpect(jsonPath("$.entry[0].resource.name[0].family").value("Jones"));

        // Source instance remains non-destructively intact
        assertEquals(2, pr.getMeta().getSecurity().size());
        assertEquals(1, pr.getExtension().size());
    }

    @Test
    @DisplayName("POST /Practitioner change request Task response is projected through publication boundary")
    void testCreatePractitionerChangeRequestTaskPublicationBoundary() throws Exception {
        Practitioner pr = new Practitioner();
        pr.addName(new HumanName().setFamily("Doe").addGiven("Jane"));
        String json = fhirContext.newJsonParser().encodeResourceToString(pr);

        Pragma pragma = ProviderRegistryChangePragma.buildChangeRequestPragma(
                ProviderRegistryConstants.OPERATION_CREATE,
                pr,
                "admin",
                "test-system",
                "corr-post-publish",
                null
        );

        // Simulate internal execution annotations on the Pragma
        pragma.addCheckpoint(new PragmaCheckpoint(pragma.getPragmaId(), "ergon-001", "DISPATCH_QUEUED", PragmaStatus.IN_PROGRESS, 1));

        ChangeRequestSubmissionService.SubmissionResult subResult =
                new ChangeRequestSubmissionService.SubmissionResult(
                        pragma.getPragmaId(),
                        "corr-post-publish",
                        "/Task/" + pragma.getPragmaId(),
                        pragma
                );

        when(submissionService.submitChangeRequest(eq("CREATE"), eq("Practitioner"), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(subResult);

        mockMvc.perform(post("/Practitioner")
                        .contentType("application/fhir+json")
                        .header("X-Correlation-Id", "corr-post-publish")
                        .with(auth("user:submitter", "PRV_SUB"))
                        .content(json))
                .andExpect(status().isAccepted())
                .andExpect(header().string("Location", "/Task/" + pragma.getPragmaId()))
                .andExpect(header().string("X-Correlation-Id", "corr-post-publish"))
                .andExpect(jsonPath("$.resourceType").value("Task"))
                .andExpect(jsonPath("$.status").exists())
                // Ensure no internal operational checkpoint or praxis extensions leaked
                .andExpect(jsonPath("$.extension").doesNotExist());
    }

    @Test
    @DisplayName("PUT /Practitioner/{id} change request Task response is projected through publication boundary")
    void testUpdatePractitionerChangeRequestTaskPublicationBoundary() throws Exception {
        Practitioner pr = new Practitioner();
        pr.setId("PR-UPDATE-1");
        pr.addName(new HumanName().setFamily("Doe").addGiven("Jane"));
        String json = fhirContext.newJsonParser().encodeResourceToString(pr);

        Pragma pragma = ProviderRegistryChangePragma.buildChangeRequestPragma(
                ProviderRegistryConstants.OPERATION_UPDATE,
                pr,
                "admin",
                "test-system",
                "corr-put-publish",
                "W/\"1\""
        );

        ChangeRequestSubmissionService.SubmissionResult subResult =
                new ChangeRequestSubmissionService.SubmissionResult(
                        pragma.getPragmaId(),
                        "corr-put-publish",
                        "/Task/" + pragma.getPragmaId(),
                        pragma
                );

        when(submissionService.submitChangeRequest(eq("UPDATE"), eq("Practitioner"), eq("PR-UPDATE-1"), any(), any(), any(), any(), any(), any(), eq("W/\"1\"")))
                .thenReturn(subResult);

        mockMvc.perform(put("/Practitioner/PR-UPDATE-1")
                        .contentType("application/fhir+json")
                        .header("If-Match", "W/\"1\"")
                        .header("X-Correlation-Id", "corr-put-publish")
                        .with(auth("user:submitter", "PRV_SUB"))
                        .content(json))
                .andExpect(status().isAccepted())
                .andExpect(header().string("Location", "/Task/" + pragma.getPragmaId()))
                .andExpect(header().string("X-Correlation-Id", "corr-put-publish"))
                .andExpect(jsonPath("$.resourceType").value("Task"))
                .andExpect(jsonPath("$.status").exists());
    }

    @Test
    @DisplayName("GET /Task/{id} polling status response projects Task through publication boundary")
    void testGetTaskStatusPollingPublicationBoundary() throws Exception {
        Practitioner pr = new Practitioner();
        pr.setId("PR-TASK-POLL");
        Pragma pragma = ProviderRegistryChangePragma.buildChangeRequestPragma(
                ProviderRegistryConstants.OPERATION_CREATE,
                pr,
                "admin",
                "test-system",
                "corr-task-poll",
                null
        );
        pragma.setStatus(PragmaStatus.COMPLETED);

        when(pragmaCacheService.getPragma(pragma.getPragmaId())).thenReturn(Optional.of(pragma));

        mockMvc.perform(get("/Task/" + pragma.getPragmaId())
                        .with(auth("user:dr-smith", "PRV_RDR")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceType").value("Task"))
                .andExpect(jsonPath("$.status").value("completed"))
                .andExpect(jsonPath("$.extension").doesNotExist());
    }
}
