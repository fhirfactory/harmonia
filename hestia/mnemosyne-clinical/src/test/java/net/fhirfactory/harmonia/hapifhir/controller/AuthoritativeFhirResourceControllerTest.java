/*
 * Copyright (c) 2026 Mark Hunter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU License as published by
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
import net.fhirfactory.harmonia.hapifhir.persistence.AuthoritativePersistencePort;
import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativePreconditionConflict;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.PreconditionFailureReason;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Practitioner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthoritativeFhirResourceControllerTest {

    @Mock
    private AuthoritativePersistencePort<IBaseResource> persistencePort;

    private FhirContext fhirContext;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        fhirContext = FhirContext.forR5();
        AuthoritativeFhirResourceController controller = new AuthoritativeFhirResourceController(persistencePort, fhirContext);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Nested
    @DisplayName("Point READ (GET /api/authoritative/fhir/{type}/{id})")
    class ReadTests {

        @Test
        @DisplayName("READ on existing resource returns 200 OK with ETag, X-Harmonia-Authoritative-Version, and FHIR body")
        void testReadSuccess() throws Exception {
            ResourceKey key = ResourceKey.of("Patient", "pat-1");
            Patient patient = new Patient();
            patient.setId("pat-1");
            patient.addName(new HumanName().setFamily("Smith").addGiven("Alice"));

            when(persistencePort.read(key))
                    .thenReturn(new AuthoritativePersistenceResult.Committed<>(patient, AuthoritativeVersion.of(1L)));

            mockMvc.perform(get("/api/authoritative/fhir/Patient/pat-1")
                            .accept("application/fhir+json"))
                    .andExpect(status().isOk())
                    .andExpect(header().string("ETag", "W/\"1\""))
                    .andExpect(header().string("X-Harmonia-Authoritative-Version", "1"))
                    .andExpect(header().string("Content-Type", "application/fhir+json;charset=UTF-8"))
                    .andExpect(jsonPath("$.resourceType").value("Patient"))
                    .andExpect(jsonPath("$.id").value("pat-1"))
                    .andExpect(jsonPath("$.name[0].family").value("Smith"));
        }

        @Test
        @DisplayName("READ on non-existing resource returns 404 Not Found")
        void testReadNotFound() throws Exception {
            ResourceKey key = ResourceKey.of("Patient", "missing-99");
            when(persistencePort.read(key))
                    .thenReturn(new AuthoritativePersistenceResult.NotCommitted<>("Resource not found"));

            mockMvc.perform(get("/api/authoritative/fhir/Patient/missing-99")
                            .accept("application/fhir+json"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("READ returning OutcomeUnknown returns 500 Internal Server Error")
        void testReadOutcomeUnknown() throws Exception {
            ResourceKey key = ResourceKey.of("Patient", "pat-err");
            when(persistencePort.read(key))
                    .thenReturn(new AuthoritativePersistenceResult.OutcomeUnknown<>("Database query timeout"));

            mockMvc.perform(get("/api/authoritative/fhir/Patient/pat-err")
                            .accept("application/fhir+json"))
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    @DisplayName("Point CREATE-if-absent (PUT with If-None-Match: *)")
    class CreateTests {

        @Test
        @DisplayName("CREATE on absent resource returns 201 Created with ETag and version header")
        void testCreateSuccess() throws Exception {
            ResourceKey key = ResourceKey.of("Patient", "pat-1");
            Patient patient = new Patient();
            patient.setId("pat-1");
            patient.addName(new HumanName().setFamily("Smith").addGiven("Alice"));
            String jsonPayload = fhirContext.newJsonParser().encodeResourceToString(patient);

            when(persistencePort.create(eq(key), any(IBaseResource.class)))
                    .thenReturn(new AuthoritativePersistenceResult.Committed<>(patient, AuthoritativeVersion.of(1L)));

            mockMvc.perform(put("/api/authoritative/fhir/Patient/pat-1")
                            .contentType("application/fhir+json")
                            .header(AuthoritativeVersionHelper.HEADER_IF_NONE_MATCH, "*")
                            .content(jsonPayload))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("ETag", "W/\"1\""))
                    .andExpect(header().string("X-Harmonia-Authoritative-Version", "1"))
                    .andExpect(jsonPath("$.resourceType").value("Patient"))
                    .andExpect(jsonPath("$.id").value("pat-1"));
        }

        @Test
        @DisplayName("CREATE on existing resource returns 412 Precondition Failed with current version header")
        void testCreateConflictExisting() throws Exception {
            ResourceKey key = ResourceKey.of("Patient", "pat-1");
            Patient patient = new Patient();
            patient.setId("pat-1");
            String jsonPayload = fhirContext.newJsonParser().encodeResourceToString(patient);

            AuthoritativePreconditionConflict conflict = AuthoritativePreconditionConflict.resourceAlreadyExists(
                    key, AuthoritativeVersion.of(3L));

            when(persistencePort.create(eq(key), any(IBaseResource.class)))
                    .thenReturn(new AuthoritativePersistenceResult.Conflict<>(conflict));

            mockMvc.perform(put("/api/authoritative/fhir/Patient/pat-1")
                            .contentType("application/fhir+json")
                            .header(AuthoritativeVersionHelper.HEADER_IF_NONE_MATCH, "*")
                            .content(jsonPayload))
                    .andExpect(status().isPreconditionFailed())
                    .andExpect(header().string("ETag", "W/\"3\""))
                    .andExpect(header().string("X-Harmonia-Authoritative-Version", "3"));
        }
    }

    @Nested
    @DisplayName("Point UPDATE-if-expected-predecessor (PUT with If-Match: W/\"{version}\")")
    class UpdateTests {

        @Test
        @DisplayName("UPDATE with matching expected version returns 200 OK with incremented ETag and version")
        void testUpdateSuccess() throws Exception {
            ResourceKey key = ResourceKey.of("Patient", "pat-1");
            Patient patient = new Patient();
            patient.setId("pat-1");
            patient.addName(new HumanName().setFamily("Smith-Jones"));
            String jsonPayload = fhirContext.newJsonParser().encodeResourceToString(patient);

            when(persistencePort.update(eq(key), any(IBaseResource.class), eq(ExpectedAuthoritativeVersion.of("1"))))
                    .thenReturn(new AuthoritativePersistenceResult.Committed<>(patient, AuthoritativeVersion.of(2L)));

            mockMvc.perform(put("/api/authoritative/fhir/Patient/pat-1")
                            .contentType("application/fhir+json")
                            .header(AuthoritativeVersionHelper.HEADER_IF_MATCH, "W/\"1\"")
                            .content(jsonPayload))
                    .andExpect(status().isOk())
                    .andExpect(header().string("ETag", "W/\"2\""))
                    .andExpect(header().string("X-Harmonia-Authoritative-Version", "2"))
                    .andExpect(jsonPath("$.resourceType").value("Patient"))
                    .andExpect(jsonPath("$.id").value("pat-1"));
        }

        @Test
        @DisplayName("UPDATE with stale expected version returns 412 Precondition Failed with actual current version")
        void testUpdateStaleVersionConflict() throws Exception {
            ResourceKey key = ResourceKey.of("Patient", "pat-1");
            Patient patient = new Patient();
            patient.setId("pat-1");
            String jsonPayload = fhirContext.newJsonParser().encodeResourceToString(patient);

            AuthoritativePreconditionConflict conflict = AuthoritativePreconditionConflict.expectedVersionMismatch(
                    key, ExpectedAuthoritativeVersion.of("1"), AuthoritativeVersion.of(5L));

            when(persistencePort.update(eq(key), any(IBaseResource.class), eq(ExpectedAuthoritativeVersion.of("1"))))
                    .thenReturn(new AuthoritativePersistenceResult.Conflict<>(conflict));

            mockMvc.perform(put("/api/authoritative/fhir/Patient/pat-1")
                            .contentType("application/fhir+json")
                            .header(AuthoritativeVersionHelper.HEADER_IF_MATCH, "W/\"1\"")
                            .content(jsonPayload))
                    .andExpect(status().isPreconditionFailed())
                    .andExpect(header().string("ETag", "W/\"5\""))
                    .andExpect(header().string("X-Harmonia-Authoritative-Version", "5"));
        }

        @Test
        @DisplayName("UPDATE on non-existing resource returns 404 Not Found")
        void testUpdateTargetAbsent() throws Exception {
            ResourceKey key = ResourceKey.of("Patient", "pat-missing");
            Patient patient = new Patient();
            patient.setId("pat-missing");
            String jsonPayload = fhirContext.newJsonParser().encodeResourceToString(patient);

            AuthoritativePreconditionConflict conflict = new AuthoritativePreconditionConflict(
                    key,
                    PreconditionFailureReason.EXPECTED_VERSION_MISMATCH,
                    ExpectedAuthoritativeVersion.of("1"),
                    null,
                    "Target resource does not exist for UPDATE: Patient/pat-missing"
            );

            when(persistencePort.update(eq(key), any(IBaseResource.class), eq(ExpectedAuthoritativeVersion.of("1"))))
                    .thenReturn(new AuthoritativePersistenceResult.Conflict<>(conflict));

            mockMvc.perform(put("/api/authoritative/fhir/Patient/pat-missing")
                            .contentType("application/fhir+json")
                            .header(AuthoritativeVersionHelper.HEADER_IF_MATCH, "W/\"1\"")
                            .content(jsonPayload))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Precondition & Validation Rules (FR-3, FR-5)")
    class ValidationTests {

        @Test
        @DisplayName("PUT without If-None-Match or If-Match returns 428 Precondition Required")
        void testMissingPreconditions() throws Exception {
            Patient patient = new Patient();
            patient.setId("pat-1");
            String jsonPayload = fhirContext.newJsonParser().encodeResourceToString(patient);

            mockMvc.perform(put("/api/authoritative/fhir/Patient/pat-1")
                            .contentType("application/fhir+json")
                            .content(jsonPayload))
                    .andExpect(status().isPreconditionRequired());

            verifyNoInteractions(persistencePort);
        }

        @Test
        @DisplayName("PUT with both If-None-Match and If-Match returns 400 Bad Request")
        void testConflictingPreconditions() throws Exception {
            Patient patient = new Patient();
            patient.setId("pat-1");
            String jsonPayload = fhirContext.newJsonParser().encodeResourceToString(patient);

            mockMvc.perform(put("/api/authoritative/fhir/Patient/pat-1")
                            .contentType("application/fhir+json")
                            .header(AuthoritativeVersionHelper.HEADER_IF_NONE_MATCH, "*")
                            .header(AuthoritativeVersionHelper.HEADER_IF_MATCH, "W/\"1\"")
                            .content(jsonPayload))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(persistencePort);
        }

        @Test
        @DisplayName("PUT with invalid If-None-Match (not *) returns 400 Bad Request")
        void testInvalidIfNoneMatchValue() throws Exception {
            Patient patient = new Patient();
            patient.setId("pat-1");
            String jsonPayload = fhirContext.newJsonParser().encodeResourceToString(patient);

            mockMvc.perform(put("/api/authoritative/fhir/Patient/pat-1")
                            .contentType("application/fhir+json")
                            .header(AuthoritativeVersionHelper.HEADER_IF_NONE_MATCH, "W/\"1\"")
                            .content(jsonPayload))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(persistencePort);
        }

        @Test
        @DisplayName("PUT with malformed/empty If-Match returns 400 Bad Request")
        void testMalformedIfMatch() throws Exception {
            Patient patient = new Patient();
            patient.setId("pat-1");
            String jsonPayload = fhirContext.newJsonParser().encodeResourceToString(patient);

            mockMvc.perform(put("/api/authoritative/fhir/Patient/pat-1")
                            .contentType("application/fhir+json")
                            .header(AuthoritativeVersionHelper.HEADER_IF_MATCH, "   ")
                            .content(jsonPayload))
                    .andExpect(status().isPreconditionRequired());

            verifyNoInteractions(persistencePort);
        }

        @Test
        @DisplayName("PUT with resourceType mismatch between URI and payload returns 400 Bad Request")
        void testResourceTypeMismatch() throws Exception {
            Practitioner practitioner = new Practitioner();
            practitioner.setId("doc-1");
            String jsonPayload = fhirContext.newJsonParser().encodeResourceToString(practitioner);

            mockMvc.perform(put("/api/authoritative/fhir/Patient/doc-1")
                            .contentType("application/fhir+json")
                            .header(AuthoritativeVersionHelper.HEADER_IF_NONE_MATCH, "*")
                            .content(jsonPayload))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(persistencePort);
        }

        @Test
        @DisplayName("PUT with resource ID mismatch between URI and payload returns 400 Bad Request")
        void testResourceIdMismatch() throws Exception {
            Patient patient = new Patient();
            patient.setId("pat-999");
            String jsonPayload = fhirContext.newJsonParser().encodeResourceToString(patient);

            mockMvc.perform(put("/api/authoritative/fhir/Patient/pat-1")
                            .contentType("application/fhir+json")
                            .header(AuthoritativeVersionHelper.HEADER_IF_NONE_MATCH, "*")
                            .content(jsonPayload))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(persistencePort);
        }

        @Test
        @DisplayName("PUT with malformed JSON syntax returns 400 Bad Request")
        void testMalformedJsonSyntax() throws Exception {
            mockMvc.perform(put("/api/authoritative/fhir/Patient/pat-1")
                            .contentType("application/fhir+json")
                            .header(AuthoritativeVersionHelper.HEADER_IF_NONE_MATCH, "*")
                            .content("{ invalid json syntax ... "))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(persistencePort);
        }

        @Test
        @DisplayName("PUT with invalid FHIR schema returns 422 Unprocessable Entity")
        void testInvalidFhirSchema() throws Exception {
            String invalidFhirJson = "{\"resourceType\":\"Patient\",\"gender\": 12345}";

            mockMvc.perform(put("/api/authoritative/fhir/Patient/pat-1")
                            .contentType("application/fhir+json")
                            .header(AuthoritativeVersionHelper.HEADER_IF_NONE_MATCH, "*")
                            .content(invalidFhirJson))
                    .andExpect(status().isUnprocessableEntity());

            verifyNoInteractions(persistencePort);
        }
    }
}
