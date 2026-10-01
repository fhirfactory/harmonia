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

package net.fhirfactory.harmonia.hapifhir.controller;

import ca.uhn.fhir.context.FhirContext;
import net.fhirfactory.harmonia.hapifhir.controller.dto.AuthoritativeVersionHelper;
import net.fhirfactory.harmonia.hapifhir.controller.security.AuthoritativeSecurityInterceptor;
import net.fhirfactory.harmonia.hapifhir.persistence.AuthoritativePersistencePort;
import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import net.fhirfactory.harmonia.themis.api.ThemisAuthorizer;
import net.fhirfactory.harmonia.themis.core.evaluator.DeterministicPolicyEvaluator;
import net.fhirfactory.harmonia.themis.core.identities.HarmoniaServiceIdentities;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthoritativeFhirResourceControllerSecurityTest {

    @Mock
    private AuthoritativePersistencePort<IBaseResource> persistencePort;

    private FhirContext fhirContext;
    private ThemisAuthorizer themisAuthorizer;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        fhirContext = FhirContext.forR5();
        themisAuthorizer = DeterministicPolicyEvaluator.withDefaultPolicies();
        AuthoritativeSecurityInterceptor interceptor = new AuthoritativeSecurityInterceptor(themisAuthorizer);
        AuthoritativeFhirResourceController controller = new AuthoritativeFhirResourceController(persistencePort, fhirContext);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(interceptor)
                .build();
    }

    @Nested
    @DisplayName("Fail-Closed Unauthenticated Invocations (401 Unauthorized)")
    class UnauthenticatedTests {

        @Test
        @DisplayName("Invocation without principal returns 401 Unauthorized")
        void testMissingPrincipalRejected() throws Exception {
            mockMvc.perform(get("/api/authoritative/fhir/Patient/pat-1"))
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(persistencePort);
        }

        @Test
        @DisplayName("Invocation with anonymous principal returns 401 Unauthorized")
        void testAnonymousPrincipalRejected() throws Exception {
            Principal anonymousPrincipal = () -> "anonymous";

            mockMvc.perform(get("/api/authoritative/fhir/Patient/pat-1")
                            .principal(anonymousPrincipal))
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(persistencePort);
        }

        @Test
        @DisplayName("Caller-controlled identity header without container principal returns 401 Unauthorized")
        void testCallerControlledHeaderIgnoredAndRejected() throws Exception {
            mockMvc.perform(get("/api/authoritative/fhir/Patient/pat-1")
                            .header("X-Harmonia-Service-Identity", "service:mneme")
                            .header("X-Themis-Principal", "service:mneme"))
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(persistencePort);
        }
    }

    @Nested
    @DisplayName("Fail-Closed Unauthorized Invocations (403 Forbidden)")
    class UnauthorizedTests {

        @Test
        @DisplayName("Authenticated principal lacking clinical authorities returns 403 Forbidden")
        void testUnauthorizedPrincipalRejected() throws Exception {
            Principal unprivileged = () -> "service:petasos"; // petasos only has system.integration, no clinical.*

            mockMvc.perform(get("/api/authoritative/fhir/Patient/pat-1")
                            .principal(unprivileged))
                    .andExpect(status().isForbidden());

            verifyNoInteractions(persistencePort);
        }

        @Test
        @DisplayName("Unknown authenticated service principal returns 403 Forbidden")
        void testUnknownServicePrincipalRejected() throws Exception {
            Principal unknownService = () -> "service:unknown-intruder";

            mockMvc.perform(get("/api/authoritative/fhir/Patient/pat-1")
                            .principal(unknownService))
                    .andExpect(status().isForbidden());

            verifyNoInteractions(persistencePort);
        }
    }

    @Nested
    @DisplayName("Authorized Invocations (Themis PERMIT)")
    class AuthorizedTests {

        @Test
        @DisplayName("Authenticated service:mneme principal is authorized for READ")
        void testMnemeAuthorizedForRead() throws Exception {
            Principal mnemePrincipal = () -> HarmoniaServiceIdentities.ID_MNEME;
            ResourceKey key = ResourceKey.of("Patient", "pat-1");
            Patient patient = new Patient();
            patient.setId("pat-1");

            when(persistencePort.read(key))
                    .thenReturn(new AuthoritativePersistenceResult.Committed<>(patient, AuthoritativeVersion.of(1L)));

            mockMvc.perform(get("/api/authoritative/fhir/Patient/pat-1")
                            .principal(mnemePrincipal))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Authenticated service:mneme principal is authorized for CREATE")
        void testMnemeAuthorizedForCreate() throws Exception {
            Principal mnemePrincipal = () -> HarmoniaServiceIdentities.ID_MNEME;
            ResourceKey key = ResourceKey.of("Patient", "pat-1");
            Patient patient = new Patient();
            patient.setId("pat-1");
            patient.addName(new HumanName().setFamily("Smith"));
            String jsonPayload = fhirContext.newJsonParser().encodeResourceToString(patient);

            when(persistencePort.create(eq(key), any(IBaseResource.class)))
                    .thenReturn(new AuthoritativePersistenceResult.Committed<>(patient, AuthoritativeVersion.of(1L)));

            mockMvc.perform(put("/api/authoritative/fhir/Patient/pat-1")
                            .principal(mnemePrincipal)
                            .contentType("application/fhir+json")
                            .header(AuthoritativeVersionHelper.HEADER_IF_NONE_MATCH, "*")
                            .content(jsonPayload))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Authenticated service:mneme principal is authorized for UPDATE")
        void testMnemeAuthorizedForUpdate() throws Exception {
            Principal mnemePrincipal = () -> HarmoniaServiceIdentities.ID_MNEME;
            ResourceKey key = ResourceKey.of("Patient", "pat-1");
            Patient patient = new Patient();
            patient.setId("pat-1");
            patient.addName(new HumanName().setFamily("Jones"));
            String jsonPayload = fhirContext.newJsonParser().encodeResourceToString(patient);

            when(persistencePort.update(eq(key), any(IBaseResource.class), eq(ExpectedAuthoritativeVersion.of("1"))))
                    .thenReturn(new AuthoritativePersistenceResult.Committed<>(patient, AuthoritativeVersion.of(2L)));

            mockMvc.perform(put("/api/authoritative/fhir/Patient/pat-1")
                            .principal(mnemePrincipal)
                            .contentType("application/fhir+json")
                            .header(AuthoritativeVersionHelper.HEADER_IF_MATCH, "W/\"1\"")
                            .content(jsonPayload))
                    .andExpect(status().isOk());
        }
    }
}
