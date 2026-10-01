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
import net.fhirfactory.harmonia.themis.core.identities.HarmoniaServiceIdentities;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AuthoritativeFhirResourceIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private final FhirContext fhirContext = FhirContext.forR5();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Full Authoritative Point Lifecycle: CREATE, READ, UPDATE, Stale UPDATE, Duplicate CREATE")
    void testAuthoritativeLifecycleIntegration() throws Exception {
        String patientId = "pat-" + UUID.randomUUID();
        Principal mnemePrincipal = () -> HarmoniaServiceIdentities.ID_MNEME;

        // 1. Initial State: READ should return 404 Not Found
        mockMvc.perform(get("/api/authoritative/fhir/Patient/" + patientId)
                        .principal(mnemePrincipal)
                        .accept("application/fhir+json"))
                .andExpect(status().isNotFound());

        // 2. CREATE on absent resource
        Patient patient = new Patient();
        patient.setId(patientId);
        patient.setActive(true);
        patient.addName(new HumanName().setFamily("Smith").addGiven("Alice"));
        String payloadV1 = fhirContext.newJsonParser().encodeResourceToString(patient);

        mockMvc.perform(put("/api/authoritative/fhir/Patient/" + patientId)
                        .principal(mnemePrincipal)
                        .contentType("application/fhir+json")
                        .header(AuthoritativeVersionHelper.HEADER_IF_NONE_MATCH, "*")
                        .content(payloadV1))
                .andExpect(status().isCreated())
                .andExpect(header().string("ETag", "W/\"1\""))
                .andExpect(header().string("X-Harmonia-Authoritative-Version", "1"))
                .andExpect(jsonPath("$.id").value(patientId))
                .andExpect(jsonPath("$.name[0].family").value("Smith"));

        // 3. READ after create returns version 1
        mockMvc.perform(get("/api/authoritative/fhir/Patient/" + patientId)
                        .principal(mnemePrincipal)
                        .accept("application/fhir+json"))
                .andExpect(status().isOk())
                .andExpect(header().string("ETag", "W/\"1\""))
                .andExpect(header().string("X-Harmonia-Authoritative-Version", "1"))
                .andExpect(jsonPath("$.id").value(patientId))
                .andExpect(jsonPath("$.name[0].family").value("Smith"));

        // 4. Duplicate CREATE returns 412 Precondition Failed with current version 1
        mockMvc.perform(put("/api/authoritative/fhir/Patient/" + patientId)
                        .principal(mnemePrincipal)
                        .contentType("application/fhir+json")
                        .header(AuthoritativeVersionHelper.HEADER_IF_NONE_MATCH, "*")
                        .content(payloadV1))
                .andExpect(status().isPreconditionFailed())
                .andExpect(header().string("ETag", "W/\"1\""))
                .andExpect(header().string("X-Harmonia-Authoritative-Version", "1"));

        // 5. UPDATE with expected version 1 succeeds and produces version 2
        Patient patientV2 = new Patient();
        patientV2.setId(patientId);
        patientV2.setActive(true);
        patientV2.addName(new HumanName().setFamily("Smith-Jones").addGiven("Alice"));
        String payloadV2 = fhirContext.newJsonParser().encodeResourceToString(patientV2);

        mockMvc.perform(put("/api/authoritative/fhir/Patient/" + patientId)
                        .principal(mnemePrincipal)
                        .contentType("application/fhir+json")
                        .header(AuthoritativeVersionHelper.HEADER_IF_MATCH, "W/\"1\"")
                        .content(payloadV2))
                .andExpect(status().isOk())
                .andExpect(header().string("ETag", "W/\"2\""))
                .andExpect(header().string("X-Harmonia-Authoritative-Version", "2"))
                .andExpect(jsonPath("$.id").value(patientId))
                .andExpect(jsonPath("$.name[0].family").value("Smith-Jones"));

        // 6. READ after update returns version 2
        mockMvc.perform(get("/api/authoritative/fhir/Patient/" + patientId)
                        .principal(mnemePrincipal)
                        .accept("application/fhir+json"))
                .andExpect(status().isOk())
                .andExpect(header().string("ETag", "W/\"2\""))
                .andExpect(header().string("X-Harmonia-Authoritative-Version", "2"))
                .andExpect(jsonPath("$.name[0].family").value("Smith-Jones"));

        // 7. Stale UPDATE with expected version 1 returns 412 Precondition Failed with current version 2
        mockMvc.perform(put("/api/authoritative/fhir/Patient/" + patientId)
                        .principal(mnemePrincipal)
                        .contentType("application/fhir+json")
                        .header(AuthoritativeVersionHelper.HEADER_IF_MATCH, "W/\"1\"")
                        .content(payloadV2))
                .andExpect(status().isPreconditionFailed())
                .andExpect(header().string("ETag", "W/\"2\""))
                .andExpect(header().string("X-Harmonia-Authoritative-Version", "2"));

        // 8. Unauthenticated invocation is rejected with 401
        mockMvc.perform(get("/api/authoritative/fhir/Patient/" + patientId)
                        .accept("application/fhir+json"))
                .andExpect(status().isUnauthorized());

        // 9. Unauthorized invocation is rejected with 403
        Principal unprivileged = () -> "service:petasos";
        mockMvc.perform(get("/api/authoritative/fhir/Patient/" + patientId)
                        .principal(unprivileged)
                        .accept("application/fhir+json"))
                .andExpect(status().isForbidden());
    }
}
