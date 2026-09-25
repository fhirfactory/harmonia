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

package net.fhirfactory.harmonia.befe.rest;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.core.Response;
import net.fhirfactory.harmonia.kleio.audit.model.AuditAction;
import net.fhirfactory.harmonia.kleio.audit.model.AuditClassification;
import net.fhirfactory.harmonia.kleio.audit.model.AuditOutcome;
import net.fhirfactory.harmonia.kleio.audit.model.AuditQuery;
import net.fhirfactory.harmonia.kleio.audit.model.HarmoniaAuditEvent;
import net.fhirfactory.harmonia.kleio.audit.service.AuditIntegrityException;
import net.fhirfactory.harmonia.kleio.audit.service.AuditService;
import net.fhirfactory.harmonia.kleio.fhir.exception.HarmoniaAuditMappingException;
import net.fhirfactory.harmonia.kleio.fhir.mapper.HarmoniaAuditEventMapper;
import net.fhirfactory.harmonia.kleio.persistence.exception.AuditPersistenceException;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import org.hl7.fhir.r5.model.AuditEvent;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.OperationOutcome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuditEventResourceTest {

    private static final FhirContext FHIR_CONTEXT = FhirContext.forR5();
    private static final IParser PARSER = FHIR_CONTEXT.newJsonParser();

    private AuditService auditService;
    private HarmoniaAuditEventMapper auditMapper;
    private AuditEventResource resource;

    @BeforeEach
    void setUp() {
        auditService = mock(AuditService.class);
        auditMapper = new HarmoniaAuditEventMapper();
        resource = new AuditEventResource(auditService, auditMapper);
    }

    private HarmoniaAuditEvent createSampleEvent(String eventId, Instant recordedAt) {
        return HarmoniaAuditEvent.builder()
                .eventId(eventId)
                .recordedAt(recordedAt)
                .classification(AuditClassification.SECURITY)
                .action(AuditAction.READ)
                .outcome(AuditOutcome.SUCCESS)
                .initiatingPrincipal(ThemisPrincipal.human("practitioner-101"))
                .securityDomain("CLINICAL")
                .correlationId("corr-" + eventId)
                .operationId("op-" + eventId)
                .build();
    }

    @Test
    @DisplayName("READ: GET /fhir/AuditEvent/{id} returns 200 with mapped AuditEvent")
    void testRead_Found() {
        String eventId = "EVT-001";
        HarmoniaAuditEvent event = createSampleEvent(eventId, Instant.parse("2026-09-24T12:00:00Z"));
        when(auditService.findById(eventId)).thenReturn(Optional.of(event));

        Response response = resource.read(eventId);

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMediaType().toString()).contains("application/fhir+json");

        String json = (String) response.getEntity();
        AuditEvent fhirEvent = PARSER.parseResource(AuditEvent.class, json);
        assertThat(fhirEvent.getIdElement().getIdPart()).isEqualTo(eventId);
        assertThat(fhirEvent.getRecorded().toInstant()).isEqualTo(Instant.parse("2026-09-24T12:00:00Z"));
    }

    @Test
    @DisplayName("READ: GET /fhir/AuditEvent/{id} returns 404 OperationOutcome when not found")
    void testRead_NotFound() {
        when(auditService.findById("EVT-999")).thenReturn(Optional.empty());

        Response response = resource.read("EVT-999");

        assertThat(response.getStatus()).isEqualTo(404);
        String json = (String) response.getEntity();
        OperationOutcome outcome = PARSER.parseResource(OperationOutcome.class, json);
        assertThat(outcome.getIssue()).hasSize(1);
        assertThat(outcome.getIssueFirstRep().getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
        assertThat(outcome.getIssueFirstRep().getCode()).isEqualTo(OperationOutcome.IssueType.NOTFOUND);
        assertThat(outcome.getIssueFirstRep().getDiagnostics()).isEqualTo("AuditEvent/EVT-999 not found");
    }

    @Test
    @DisplayName("READ: Blank ID returns 404 OperationOutcome")
    void testRead_BlankId_NotFound() {
        Response response = resource.read("   ");

        assertThat(response.getStatus()).isEqualTo(404);
        String json = (String) response.getEntity();
        OperationOutcome outcome = PARSER.parseResource(OperationOutcome.class, json);
        assertThat(outcome.getIssueFirstRep().getCode()).isEqualTo(OperationOutcome.IssueType.NOTFOUND);
    }

    @Test
    @DisplayName("READ: AuditIntegrityException returns 500 OperationOutcome with zero PHI/sensitive leakage")
    void testRead_IntegrityException_Returns500() {
        when(auditService.findById("EVT-CORRUPTED"))
                .thenThrow(new AuditIntegrityException("EVT-CORRUPTED", "Secret DB hash mismatch checksum=0xdeadbeef"));

        Response response = resource.read("EVT-CORRUPTED");

        assertThat(response.getStatus()).isEqualTo(500);
        String json = (String) response.getEntity();
        OperationOutcome outcome = PARSER.parseResource(OperationOutcome.class, json);
        assertThat(outcome.getIssueFirstRep().getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
        assertThat(outcome.getIssueFirstRep().getCode()).isEqualTo(OperationOutcome.IssueType.EXCEPTION);
        assertThat(outcome.getIssueFirstRep().getDiagnostics()).isEqualTo("Internal error retrieving AuditEvent");
        assertThat(json).doesNotContain("deadbeef");
        assertThat(json).doesNotContain("checksum");
    }

    @Test
    @DisplayName("READ: AuditPersistenceException returns 500 OperationOutcome with zero PHI/sensitive leakage")
    void testRead_PersistenceException_Returns500() {
        when(auditService.findById("EVT-ERR"))
                .thenThrow(new AuditPersistenceException("PostgreSQL connection refused on 10.0.0.5:5432"));

        Response response = resource.read("EVT-ERR");

        assertThat(response.getStatus()).isEqualTo(500);
        String json = (String) response.getEntity();
        OperationOutcome outcome = PARSER.parseResource(OperationOutcome.class, json);
        assertThat(outcome.getIssueFirstRep().getCode()).isEqualTo(OperationOutcome.IssueType.EXCEPTION);
        assertThat(outcome.getIssueFirstRep().getDiagnostics()).isEqualTo("Internal error retrieving AuditEvent");
        assertThat(json).doesNotContain("PostgreSQL");
        assertThat(json).doesNotContain("10.0.0.5");
    }

    @Test
    @DisplayName("READ: HarmoniaAuditMappingException returns 500 OperationOutcome")
    void testRead_MappingException_Returns500() {
        HarmoniaAuditEventMapper mockMapper = mock(HarmoniaAuditEventMapper.class);
        resource.setAuditMapper(mockMapper);
        HarmoniaAuditEvent event = createSampleEvent("EVT-MAP-ERR", Instant.now());
        when(auditService.findById("EVT-MAP-ERR")).thenReturn(Optional.of(event));
        when(mockMapper.toFhir(event)).thenThrow(new HarmoniaAuditMappingException("Invalid mapping"));

        Response response = resource.read("EVT-MAP-ERR");

        assertThat(response.getStatus()).isEqualTo(500);
        String json = (String) response.getEntity();
        OperationOutcome outcome = PARSER.parseResource(OperationOutcome.class, json);
        assertThat(outcome.getIssueFirstRep().getCode()).isEqualTo(OperationOutcome.IssueType.EXCEPTION);
        assertThat(outcome.getIssueFirstRep().getDiagnostics()).isEqualTo("Internal error retrieving AuditEvent");
    }

    @Test
    @DisplayName("SEARCH: GET /fhir/AuditEvent returns 200 Bundle preserving canonical descending order")
    void testSearch_Unfiltered_PreservesOrdering() {
        HarmoniaAuditEvent evt2 = createSampleEvent("EVT-002", Instant.parse("2026-09-24T12:00:00Z"));
        HarmoniaAuditEvent evt1 = createSampleEvent("EVT-001", Instant.parse("2026-09-24T11:00:00Z"));
        when(auditService.find(any(AuditQuery.class))).thenReturn(List.of(evt2, evt1));

        Response response = resource.search(null, null, null);

        assertThat(response.getStatus()).isEqualTo(200);
        String json = (String) response.getEntity();
        Bundle bundle = PARSER.parseResource(Bundle.class, json);
        assertThat(bundle.getType()).isEqualTo(Bundle.BundleType.SEARCHSET);
        assertThat(bundle.getTotal()).isEqualTo(2);
        assertThat(bundle.getEntry()).hasSize(2);

        assertThat(bundle.getEntry().get(0).getFullUrl()).isEqualTo("AuditEvent/EVT-002");
        assertThat(((AuditEvent) bundle.getEntry().get(0).getResource()).getIdElement().getIdPart()).isEqualTo("EVT-002");

        assertThat(bundle.getEntry().get(1).getFullUrl()).isEqualTo("AuditEvent/EVT-001");
        assertThat(((AuditEvent) bundle.getEntry().get(1).getResource()).getIdElement().getIdPart()).isEqualTo("EVT-001");

        ArgumentCaptor<AuditQuery> captor = ArgumentCaptor.forClass(AuditQuery.class);
        verify(auditService).find(captor.capture());
        assertThat(captor.getValue().eventId()).isNull();
    }

    @Test
    @DisplayName("SEARCH: GET /fhir/AuditEvent?_id=EVT-001 passes eventId to AuditQuery")
    void testSearch_ById_PassesIdToQuery() {
        HarmoniaAuditEvent evt1 = createSampleEvent("EVT-001", Instant.parse("2026-09-24T11:00:00Z"));
        when(auditService.find(any(AuditQuery.class))).thenReturn(List.of(evt1));

        Response response = resource.search("EVT-001", null, null);

        assertThat(response.getStatus()).isEqualTo(200);
        ArgumentCaptor<AuditQuery> captor = ArgumentCaptor.forClass(AuditQuery.class);
        verify(auditService).find(captor.capture());
        assertThat(captor.getValue().eventId()).isEqualTo("EVT-001");
    }

    @Test
    @DisplayName("SEARCH: Unsupported 'name' parameter fails closed with 400 OperationOutcome")
    void testSearch_UnsupportedName_FailsClosed400() {
        Response response = resource.search(null, "legacy-name-filter", null);

        assertThat(response.getStatus()).isEqualTo(400);
        String json = (String) response.getEntity();
        OperationOutcome outcome = PARSER.parseResource(OperationOutcome.class, json);
        assertThat(outcome.getIssueFirstRep().getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
        assertThat(outcome.getIssueFirstRep().getCode()).isEqualTo(OperationOutcome.IssueType.NOTSUPPORTED);
        assertThat(outcome.getIssueFirstRep().getDiagnostics()).isEqualTo("Search parameter not supported for AuditEvent");

        verifyNoInteractions(auditService);
    }

    @Test
    @DisplayName("SEARCH: Unsupported 'identifier' parameter fails closed with 400 OperationOutcome")
    void testSearch_UnsupportedIdentifier_FailsClosed400() {
        Response response = resource.search(null, null, "urn:system|12345");

        assertThat(response.getStatus()).isEqualTo(400);
        String json = (String) response.getEntity();
        OperationOutcome outcome = PARSER.parseResource(OperationOutcome.class, json);
        assertThat(outcome.getIssueFirstRep().getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
        assertThat(outcome.getIssueFirstRep().getCode()).isEqualTo(OperationOutcome.IssueType.NOTSUPPORTED);
        assertThat(outcome.getIssueFirstRep().getDiagnostics()).isEqualTo("Search parameter not supported for AuditEvent");

        verifyNoInteractions(auditService);
    }

    @Test
    @DisplayName("SEARCH: AuditIntegrityException returns 500 OperationOutcome")
    void testSearch_IntegrityException_Returns500() {
        when(auditService.find(any(AuditQuery.class)))
                .thenThrow(new AuditIntegrityException("Hash mismatch during audit scan"));

        Response response = resource.search(null, null, null);

        assertThat(response.getStatus()).isEqualTo(500);
        String json = (String) response.getEntity();
        OperationOutcome outcome = PARSER.parseResource(OperationOutcome.class, json);
        assertThat(outcome.getIssueFirstRep().getCode()).isEqualTo(OperationOutcome.IssueType.EXCEPTION);
        assertThat(outcome.getIssueFirstRep().getDiagnostics()).isEqualTo("Internal error retrieving AuditEvent");
    }

    @Test
    @DisplayName("SEARCH: AuditPersistenceException returns 500 OperationOutcome")
    void testSearch_PersistenceException_Returns500() {
        when(auditService.find(any(AuditQuery.class)))
                .thenThrow(new AuditPersistenceException("Scan ceiling exceeded"));

        Response response = resource.search(null, null, null);

        assertThat(response.getStatus()).isEqualTo(500);
        String json = (String) response.getEntity();
        OperationOutcome outcome = PARSER.parseResource(OperationOutcome.class, json);
        assertThat(outcome.getIssueFirstRep().getCode()).isEqualTo(OperationOutcome.IssueType.EXCEPTION);
        assertThat(outcome.getIssueFirstRep().getDiagnostics()).isEqualTo("Internal error retrieving AuditEvent");
    }

    @Test
    @DisplayName("ARCH/SURFACE: Mutation methods (POST, PUT, DELETE, PATCH) are absent on AuditEventResource")
    void testMutationEndpointsAbsent() {
        Method[] methods = AuditEventResource.class.getDeclaredMethods();
        for (Method method : methods) {
            assertThat(method.isAnnotationPresent(POST.class))
                    .as("Method %s must not have @POST annotation", method.getName())
                    .isFalse();
            assertThat(method.isAnnotationPresent(PUT.class))
                    .as("Method %s must not have @PUT annotation", method.getName())
                    .isFalse();
            assertThat(method.isAnnotationPresent(DELETE.class))
                    .as("Method %s must not have @DELETE annotation", method.getName())
                    .isFalse();
            assertThat(method.isAnnotationPresent(PATCH.class))
                    .as("Method %s must not have @PATCH annotation", method.getName())
                    .isFalse();

            List<String> forbiddenNames = List.of("create", "update", "delete", "patch");
            assertThat(forbiddenNames)
                    .as("Method name %s is forbidden on AuditEventResource", method.getName())
                    .doesNotContain(method.getName().toLowerCase());
        }
    }

    @Test
    @DisplayName("ARCH/SURFACE: FhirCacheService is not declared or injected in AuditEventResource")
    void testNoFhirCacheServiceField() {
        Field[] fields = AuditEventResource.class.getDeclaredFields();
        boolean hasFhirCacheService = Arrays.stream(fields)
                .anyMatch(f -> f.getType().getName().contains("FhirCacheService"));
        assertThat(hasFhirCacheService).isFalse();
    }
}
