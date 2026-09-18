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
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.model.pragma.ProviderRegistryChangePragma;
import net.fhirfactory.harmonia.model.registry.ProviderRegistryConstants;
import net.fhirfactory.harmonia.praxis.cache.PragmaCacheService;
import net.fhirfactory.harmonia.pylai.fhir.provider.CapabilityStatementProvider;
import net.fhirfactory.harmonia.pylai.fhir.security.FhirSecurityInterceptor;
import net.fhirfactory.harmonia.pylai.fhir.service.ChangeRequestSubmissionService;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Practitioner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FhirRestGatewayControllerTest {

    private MockMvc mockMvc;
    private FhirStorageService storageService;
    private ChangeRequestSubmissionService submissionService;
    private CapabilityStatementProvider capabilityStatementProvider;
    private FhirSecurityInterceptor securityInterceptor;
    private PragmaCacheService pragmaCacheService;
    private final FhirContext fhirContext = FhirContext.forR5();

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
    @DisplayName("GET /metadata returns 200 OK with FHIR CapabilityStatement")
    void testGetMetadata() throws Exception {
        mockMvc.perform(get("/metadata"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/fhir+json"))
                .andExpect(jsonPath("$.resourceType").value("CapabilityStatement"))
                .andExpect(jsonPath("$.status").value("active"))
                .andExpect(jsonPath("$.fhirVersion").value("5.0.0"));
    }

    @Test
    @DisplayName("GET /Practitioner/{id} returns 200 OK with ETag")
    void testReadPractitionerSuccess() throws Exception {
        Practitioner pr = new Practitioner();
        pr.setId("Practitioner/PR-100");
        pr.getMeta().setVersionId("1");
        pr.addName(new HumanName().setFamily("Smith").addGiven("John"));

        when(storageService.getResource("Practitioner", "PR-100")).thenReturn(pr);

        mockMvc.perform(get("/Practitioner/PR-100")
                        .header("X-User-Roles", "PRV_RDR")
                        .header("X-Requester", "user:dr-smith"))
                .andExpect(status().isOk())
                .andExpect(header().string("ETag", "W/\"1\""))
                .andExpect(jsonPath("$.resourceType").value("Practitioner"))
                .andExpect(jsonPath("$.name[0].family").value("Smith"));
    }

    @Test
    @DisplayName("GET /Practitioner performs multi-parameter search and returns searchset Bundle")
    void testSearchPractitioners() throws Exception {
        Practitioner pr = new Practitioner();
        pr.setId("Practitioner/PR-101");
        pr.addName(new HumanName().setFamily("Smith").addGiven("Jane"));

        when(storageService.searchResources(eq("Practitioner"), any())).thenReturn(List.of(pr));

        mockMvc.perform(get("/Practitioner?name=Smith")
                        .header("X-User-Roles", "PRV_RDR")
                        .header("X-Requester", "user:dr-smith"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceType").value("Bundle"))
                .andExpect(jsonPath("$.type").value("searchset"))
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.entry[0].resource.resourceType").value("Practitioner"));
    }

    @Test
    @DisplayName("POST /Practitioner returns 202 Accepted with Task Location and Correlation ID")
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
                        .header("X-User-Roles", "PRV_SUB")
                        .header("X-Requester", "user:submitter")
                        .header("X-Correlation-Id", "corr-post-1")
                        .content(json))
                .andExpect(status().isAccepted())
                .andExpect(header().string("Location", "/Task/" + pragma.getPragmaId()))
                .andExpect(header().string("X-Correlation-Id", "corr-post-1"))
                .andExpect(header().string("Retry-After", "1"))
                .andExpect(jsonPath("$.resourceType").value("Task"));
    }

    @Test
    @DisplayName("GET /Task/{id} returns Task representing Pragma status")
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
                        .header("X-User-Roles", "PRV_RDR")
                        .header("X-Requester", "user:dr-smith"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceType").value("Task"))
                .andExpect(jsonPath("$.status").value("completed"));
    }

    @Test
    @DisplayName("GET /Practitioner/{id} denied with 403 when no user roles provided (Default Deny)")
    void testReadDeniedDefaultDeny() throws Exception {
        mockMvc.perform(get("/Practitioner/PR-100"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /Practitioner denied with 403 when caller only has PRV_RDR role")
    void testCreateDeniedForReader() throws Exception {
        Practitioner pr = new Practitioner();
        pr.addName(new HumanName().setFamily("Doe").addGiven("John"));
        String json = fhirContext.newJsonParser().encodeResourceToString(pr);

        mockMvc.perform(post("/Practitioner")
                        .contentType("application/fhir+json")
                        .header("X-User-Roles", "PRV_RDR")
                        .header("X-Requester", "user:reader-only")
                        .content(json))
                .andExpect(status().isForbidden());
    }
}
