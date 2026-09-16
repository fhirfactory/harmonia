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
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import net.fhirfactory.harmonia.hapifhir.service.FhirStorageService;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaFhirConverter;
import net.fhirfactory.harmonia.model.pragma.ProviderRegistryChangePragma;
import net.fhirfactory.harmonia.model.registry.ProviderRegistryConstants;
import net.fhirfactory.harmonia.praxis.cache.PragmaCacheService;
import net.fhirfactory.harmonia.pylai.fhir.provider.CapabilityStatementProvider;
import net.fhirfactory.harmonia.pylai.fhir.security.FhirSecurityInterceptor;
import net.fhirfactory.harmonia.pylai.fhir.service.ChangeRequestSubmissionService;
import net.fhirfactory.harmonia.pylai.fhir.service.ChangeRequestSubmissionService.SubmissionResult;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.CapabilityStatement;
import org.hl7.fhir.r5.model.OperationOutcome;
import org.hl7.fhir.r5.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * REST Controller exposing the FHIR R5 Provider Registry API boundary.
 * <p>
 * Provides synchronous READ and SEARCH paths directly backed by {@link FhirStorageService},
 * and asynchronous governed CREATE / UPDATE change submissions returning {@code HTTP 202 Accepted}
 * with status tracking via standard {@code /Task/{id}}.
 */
@RestController
@RequestMapping(produces = {"application/fhir+json", "application/json"})
public class FhirRestGatewayController {

    private static final Logger log = LoggerFactory.getLogger(FhirRestGatewayController.class);
    private static final String FHIR_JSON = "application/fhir+json;charset=utf-8";

    private final FhirStorageService storageService;
    private final ChangeRequestSubmissionService submissionService;
    private final CapabilityStatementProvider capabilityStatementProvider;
    private final FhirSecurityInterceptor securityInterceptor;
    private final PragmaCacheService pragmaCacheService;
    private final FhirContext fhirContext;

    public FhirRestGatewayController(
            @Autowired(required = false) FhirStorageService storageService,
            ChangeRequestSubmissionService submissionService,
            CapabilityStatementProvider capabilityStatementProvider,
            FhirSecurityInterceptor securityInterceptor,
            @Autowired(required = false) PragmaCacheService pragmaCacheService) {
        this.storageService = storageService;
        this.submissionService = submissionService;
        this.capabilityStatementProvider = capabilityStatementProvider;
        this.securityInterceptor = securityInterceptor;
        this.pragmaCacheService = pragmaCacheService;
        this.fhirContext = FhirContext.forR5();
    }

    private IParser getJsonParser() {
        return fhirContext.newJsonParser().setPrettyPrint(true);
    }

    /**
     * FHIR CapabilityStatement discovery endpoint.
     */
    @GetMapping(value = {"/metadata", "/fhir/metadata"})
    public ResponseEntity<String> getMetadata(HttpServletRequest request) {
        securityInterceptor.authorize("CapabilityStatement", "read", request);
        CapabilityStatement cs = capabilityStatementProvider.buildCapabilityStatement();
        String json = getJsonParser().encodeResourceToString(cs);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(FHIR_JSON)).body(json);
    }

    /**
     * FHIR Task status tracking endpoint reflecting asynchronous change lifecycle.
     */
    @GetMapping(value = {"/Task/{id}", "/fhir/Task/{id}"})
    public ResponseEntity<String> getTaskStatus(
            @PathVariable("id") String id,
            HttpServletRequest request) {

        securityInterceptor.authorize("Task", "read", request);

        // 1. Try to load Pragma from Cache Service
        if (pragmaCacheService != null) {
            Optional<Pragma> pragmaOpt = pragmaCacheService.getPragma(id);
            if (pragmaOpt.isPresent()) {
                Task fhirTask = PragmaFhirConverter.toFhirTask(pragmaOpt.get());
                String json = getJsonParser().encodeResourceToString(fhirTask);
                return ResponseEntity.ok().contentType(MediaType.parseMediaType(FHIR_JSON)).body(json);
            }
        }

        // 2. Try to load Task from Mnemosyne Storage Service
        if (storageService != null) {
            try {
                Task task = storageService.getResource("Task", id);
                String json = getJsonParser().encodeResourceToString(task);
                return ResponseEntity.ok().contentType(MediaType.parseMediaType(FHIR_JSON)).body(json);
            } catch (ResourceNotFoundException ignored) {
            }
        }

        OperationOutcome outcome = new OperationOutcome();
        outcome.addIssue()
                .setSeverity(OperationOutcome.IssueSeverity.ERROR)
                .setCode(OperationOutcome.IssueType.NOTFOUND)
                .setDiagnostics("Task/" + id + " not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.parseMediaType(FHIR_JSON))
                .body(getJsonParser().encodeResourceToString(outcome));
    }

    /**
     * Synchronous FHIR Read Path by ID.
     */
    @GetMapping(value = {"/{resourceType}/{id}", "/fhir/{resourceType}/{id}"})
    public ResponseEntity<String> readResource(
            @PathVariable("resourceType") String resourceType,
            @PathVariable("id") String id,
            HttpServletRequest request) {

        if ("Task".equalsIgnoreCase(resourceType)) {
            return getTaskStatus(id, request);
        }

        securityInterceptor.authorize(resourceType, "read", request);

        if (storageService == null) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("{\"error\":\"StorageService unavailable\"}");
        }

        try {
            IBaseResource resource = storageService.getResource(resourceType, id);
            String json = getJsonParser().encodeResourceToString(resource);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(FHIR_JSON));
            if (resource instanceof org.hl7.fhir.r5.model.Resource r && r.getMeta() != null && r.getMeta().getVersionId() != null) {
                headers.setETag("W/\"" + r.getMeta().getVersionId() + "\"");
            }

            return new ResponseEntity<>(json, headers, HttpStatus.OK);

        } catch (ResourceNotFoundException ex) {
            OperationOutcome outcome = new OperationOutcome();
            outcome.addIssue()
                    .setSeverity(OperationOutcome.IssueSeverity.ERROR)
                    .setCode(OperationOutcome.IssueType.NOTFOUND)
                    .setDiagnostics("Resource " + resourceType + "/" + id + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.parseMediaType(FHIR_JSON))
                    .body(getJsonParser().encodeResourceToString(outcome));

        } catch (ResourceGoneException ex) {
            OperationOutcome outcome = new OperationOutcome();
            outcome.addIssue()
                    .setSeverity(OperationOutcome.IssueSeverity.ERROR)
                    .setCode(OperationOutcome.IssueType.DELETED)
                    .setDiagnostics("Resource " + resourceType + "/" + id + " has been deleted");
            return ResponseEntity.status(HttpStatus.GONE)
                    .contentType(MediaType.parseMediaType(FHIR_JSON))
                    .body(getJsonParser().encodeResourceToString(outcome));
        }
    }

    /**
     * Synchronous FHIR Multi-Parameter Search Path.
     */
    @GetMapping(value = {"/{resourceType}", "/fhir/{resourceType}"})
    public ResponseEntity<String> searchResources(
            @PathVariable("resourceType") String resourceType,
            @RequestParam Map<String, String> allParams,
            HttpServletRequest request) {

        securityInterceptor.authorize(resourceType, "search", request);

        if (storageService == null) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("{\"error\":\"StorageService unavailable\"}");
        }

        List<IBaseResource> matches = storageService.searchResources(resourceType, allParams);

        Bundle searchBundle = new Bundle();
        searchBundle.setType(Bundle.BundleType.SEARCHSET);
        searchBundle.setTotal(matches.size());

        for (IBaseResource res : matches) {
            if (res instanceof org.hl7.fhir.r5.model.Resource r) {
                Bundle.BundleEntryComponent entry = searchBundle.addEntry();
                entry.setFullUrl("http://localhost:8080/fhir/" + resourceType + "/" + r.getIdElement().getIdPart());
                entry.setResource(r);
                entry.getSearch().setMode(Bundle.SearchEntryMode.MATCH);
            }
        }

        String json = getJsonParser().encodeResourceToString(searchBundle);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(FHIR_JSON)).body(json);
    }

    /**
     * Asynchronous Governed Create Request Path (HTTP POST).
     */
    @PostMapping(value = {"/{resourceType}", "/fhir/{resourceType}"}, consumes = {"application/fhir+json", "application/json", "application/xml", "text/xml"})
    public ResponseEntity<String> createResource(
            @PathVariable("resourceType") String resourceType,
            @RequestBody String payload,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId,
            @RequestHeader(value = "X-Source-System", required = false) String sourceSystem,
            @RequestHeader(value = "X-Requester", required = false) String requester,
            HttpServletRequest request) {

        securityInterceptor.authorize(resourceType, "create.request", request);

        ThemisPrincipal principal = request != null ? (ThemisPrincipal) request.getAttribute(FhirSecurityInterceptor.ATTR_THEMIS_PRINCIPAL) : null;
        @SuppressWarnings("unchecked")
        Set<ThemisAuthority> authorities = request != null ? (Set<ThemisAuthority>) request.getAttribute(FhirSecurityInterceptor.ATTR_THEMIS_AUTHORITIES) : null;
        ThemisSecurityContext context = request != null ? (ThemisSecurityContext) request.getAttribute(FhirSecurityInterceptor.ATTR_THEMIS_CONTEXT) : null;

        SubmissionResult result = submissionService.submitChangeRequest(
                ProviderRegistryConstants.OPERATION_CREATE,
                resourceType,
                null,
                payload,
                principal,
                authorities,
                context,
                sourceSystem,
                correlationId,
                null
        );

        Task fhirTask = PragmaFhirConverter.toFhirTask(result.getPragma());
        String taskJson = getJsonParser().encodeResourceToString(fhirTask);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(FHIR_JSON));
        headers.setLocation(URI.create(result.getStatusLocation()));
        headers.set("X-Correlation-Id", result.getCorrelationId());
        headers.set("Retry-After", "1");

        return new ResponseEntity<>(taskJson, headers, HttpStatus.ACCEPTED);
    }

    /**
     * Asynchronous Governed Update Request Path (HTTP PUT).
     */
    @PutMapping(value = {"/{resourceType}/{id}", "/fhir/{resourceType}/{id}"}, consumes = {"application/fhir+json", "application/json", "application/xml", "text/xml"})
    public ResponseEntity<String> updateResource(
            @PathVariable("resourceType") String resourceType,
            @PathVariable("id") String id,
            @RequestBody String payload,
            @RequestHeader(value = "If-Match", required = false) String ifMatch,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId,
            @RequestHeader(value = "X-Source-System", required = false) String sourceSystem,
            @RequestHeader(value = "X-Requester", required = false) String requester,
            HttpServletRequest request) {

        securityInterceptor.authorize(resourceType, "update.request", request);

        ThemisPrincipal principal = request != null ? (ThemisPrincipal) request.getAttribute(FhirSecurityInterceptor.ATTR_THEMIS_PRINCIPAL) : null;
        @SuppressWarnings("unchecked")
        Set<ThemisAuthority> authorities = request != null ? (Set<ThemisAuthority>) request.getAttribute(FhirSecurityInterceptor.ATTR_THEMIS_AUTHORITIES) : null;
        ThemisSecurityContext context = request != null ? (ThemisSecurityContext) request.getAttribute(FhirSecurityInterceptor.ATTR_THEMIS_CONTEXT) : null;

        SubmissionResult result = submissionService.submitChangeRequest(
                ProviderRegistryConstants.OPERATION_UPDATE,
                resourceType,
                id,
                payload,
                principal,
                authorities,
                context,
                sourceSystem,
                correlationId,
                ifMatch
        );

        Task fhirTask = PragmaFhirConverter.toFhirTask(result.getPragma());
        String taskJson = getJsonParser().encodeResourceToString(fhirTask);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(FHIR_JSON));
        headers.setLocation(URI.create(result.getStatusLocation()));
        headers.set("X-Correlation-Id", result.getCorrelationId());
        headers.set("Retry-After", "1");

        return new ResponseEntity<>(taskJson, headers, HttpStatus.ACCEPTED);
    }
}
