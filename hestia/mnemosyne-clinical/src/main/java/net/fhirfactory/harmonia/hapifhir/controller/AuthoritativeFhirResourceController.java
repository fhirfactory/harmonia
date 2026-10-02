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
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.core.JsonParseException;
import net.fhirfactory.harmonia.hapifhir.controller.dto.AuthoritativeVersionHelper;
import net.fhirfactory.harmonia.hapifhir.persistence.AuthoritativePersistencePort;
import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativePreconditionConflict;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.PreconditionFailureReason;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;

/**
 * Dedicated internal authoritative REST controller for Mnemosyne persistence.
 * <p>
 * Exposes point {@code READ}, {@code CREATE-if-absent}, and {@code UPDATE-if-expected-predecessor} operations
 * to {@code MnemeAuthoritativeHttpClient} at {@code /api/authoritative/fhir/{resourceType}/{id}}.
 * <p>
 * Enforces normative HTTP wire contract with explicit 4-way version domain separation.
 */
@RestController
@RequestMapping("/api/authoritative/fhir")
public class AuthoritativeFhirResourceController {

    private static final Logger log = LoggerFactory.getLogger(AuthoritativeFhirResourceController.class);

    private static final MediaType FHIR_JSON_MEDIA_TYPE = MediaType.parseMediaType("application/fhir+json;charset=UTF-8");

    private final AuthoritativePersistencePort<IBaseResource> persistencePort;
    private final FhirContext fhirContext;

    @Autowired
    public AuthoritativeFhirResourceController(
            AuthoritativePersistencePort<IBaseResource> persistencePort,
            FhirContext fhirContext) {
        this.persistencePort = Objects.requireNonNull(persistencePort, "persistencePort must not be null");
        this.fhirContext = Objects.requireNonNull(fhirContext, "fhirContext must not be null");
    }

    /**
     * Point READ operation: retrieves a single authoritative FHIR resource by key.
     *
     * @param resourceType the FHIR resource type
     * @param id the resource logical ID
     * @return HTTP response containing resource or error status
     */
    @GetMapping(value = "/{resourceType}/{id}", produces = {"application/fhir+json", "application/json"})
    public ResponseEntity<String> read(
            @PathVariable("resourceType") String resourceType,
            @PathVariable("id") String id) {

        if (resourceType == null || resourceType.isBlank() || id == null || id.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid resourceType or id in URI path");
        }

        ResourceKey key = new ResourceKey(resourceType.trim(), id.trim());
        log.debug("Authoritative READ request for {}", key);

        AuthoritativePersistenceResult<IBaseResource> result;
        try {
            result = persistencePort.read(key);
        } catch (Exception e) {
            log.error("Unexpected error executing authoritative READ for {}: {}", key, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal persistence error: " + e.getMessage());
        }

        if (result instanceof AuthoritativePersistenceResult.Committed<IBaseResource> committed) {
            AuthoritativeVersion version = committed.authoritativeVersion();
            String etag = AuthoritativeVersionHelper.formatEtag(version);
            String payload = fhirContext.newJsonParser().setPrettyPrint(false).encodeResourceToString(committed.persistedResource());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(FHIR_JSON_MEDIA_TYPE);
            if (etag != null) {
                headers.set(AuthoritativeVersionHelper.HEADER_ETAG, etag);
            }
            if (version != null && version.value() != null) {
                headers.set(AuthoritativeVersionHelper.HEADER_X_AUTHORITATIVE_VERSION, version.value());
            }

            return new ResponseEntity<>(payload, headers, HttpStatus.OK);
        }

        if (result instanceof AuthoritativePersistenceResult.Absent<IBaseResource> absent) {
            log.debug("Authoritative READ absent for {}: {}", key, absent.message());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Resource not found: " + key);
        }

        if (result instanceof AuthoritativePersistenceResult.NotCommitted<IBaseResource> notCommitted) {
            log.warn("Authoritative READ not committed for {}: {}", key, notCommitted.failureMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Read not committed: " + notCommitted.failureMessage());
        }

        if (result instanceof AuthoritativePersistenceResult.OutcomeUnknown<IBaseResource> outcomeUnknown) {
            log.error("Authoritative READ outcome unknown for {}: {}", key, outcomeUnknown.message());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Persistence outcome unknown: " + outcomeUnknown.message());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unexpected result type from persistence adapter for READ " + key);
    }

    /**
     * Point CREATE-if-absent ({@code If-None-Match: *}) or UPDATE-if-expected-predecessor
     * ({@code If-Match: W/"{version}"}) operation.
     *
     * @param resourceType the FHIR resource type from URI
     * @param id the resource logical ID from URI
     * @param ifNoneMatch optional {@code If-None-Match} header for conditional CREATE
     * @param ifMatch optional {@code If-Match} header for conditional UPDATE
     * @param requestBody the serialized FHIR resource JSON
     * @return HTTP response with status, version headers, and persisted resource
     */
    @PutMapping(value = "/{resourceType}/{id}", consumes = {"application/fhir+json", "application/json", "*/*"}, produces = {"application/fhir+json", "application/json"})
    public ResponseEntity<String> createOrUpdate(
            @PathVariable("resourceType") String resourceType,
            @PathVariable("id") String id,
            @RequestHeader(value = AuthoritativeVersionHelper.HEADER_IF_NONE_MATCH, required = false) String ifNoneMatch,
            @RequestHeader(value = AuthoritativeVersionHelper.HEADER_IF_MATCH, required = false) String ifMatch,
            @RequestBody(required = false) String requestBody) {

        if (resourceType == null || resourceType.isBlank() || id == null || id.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid resourceType or id in URI path");
        }

        boolean hasIfNoneMatch = ifNoneMatch != null && !ifNoneMatch.isBlank();
        boolean hasIfMatch = ifMatch != null && !ifMatch.isBlank();

        // FR-5: Deterministic Precondition Discrimination & Header Validation
        if (!hasIfNoneMatch && !hasIfMatch) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED)
                    .body("Precondition required: must supply either If-None-Match: * or If-Match: W/\"{expectedVersion}\"");
        }

        if (hasIfNoneMatch && hasIfMatch) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Conflicting precondition headers: cannot specify both If-None-Match and If-Match");
        }

        if (requestBody == null || requestBody.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Request body must not be null or empty");
        }

        ResourceKey key = new ResourceKey(resourceType.trim(), id.trim());

        // Validate and parse payload
        IBaseResource parsedResource;
        try {
            IParser parser = fhirContext.newJsonParser();
            parsedResource = parser.parseResource(requestBody);
        } catch (DataFormatException dfe) {
            if (dfe.getCause() instanceof JsonParseException || (dfe.getMessage() != null && dfe.getMessage().toLowerCase().contains("syntax"))) {
                log.warn("Malformed JSON syntax in request body for {}: {}", key, dfe.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Malformed JSON payload syntax: " + dfe.getMessage());
            }
            log.warn("Invalid FHIR schema/structure in request body for {}: {}", key, dfe.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("Unprocessable FHIR entity schema: " + dfe.getMessage());
        } catch (Exception e) {
            log.warn("Failed to parse request payload for {}: {}", key, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to parse resource payload: " + e.getMessage());
        }

        if (parsedResource == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Unable to parse valid FHIR resource from body");
        }

        // Validate identity match between URI path and payload body
        String parsedType = parsedResource.fhirType();
        if (parsedType == null || !parsedType.equalsIgnoreCase(key.resourceType())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Resource type mismatch: URI is " + key.resourceType() + " but payload is " + parsedType);
        }

        String parsedId = parsedResource.getIdElement() != null ? parsedResource.getIdElement().getIdPart() : null;
        if (parsedId != null && !parsedId.isBlank() && !parsedId.equals(key.id())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Resource ID mismatch: URI is " + key.id() + " but payload is " + parsedId);
        }

        if (hasIfNoneMatch) {
            return handleCreate(key, ifNoneMatch, parsedResource);
        } else {
            return handleUpdate(key, ifMatch, parsedResource);
        }
    }

    private ResponseEntity<String> handleCreate(ResourceKey key, String ifNoneMatch, IBaseResource proposedResource) {
        if (!"*".equals(ifNoneMatch.trim())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid If-None-Match value: point CREATE only supports If-None-Match: *");
        }

        log.debug("Authoritative CREATE request for {}", key);
        AuthoritativePersistenceResult<IBaseResource> result;
        try {
            result = persistencePort.create(key, proposedResource);
        } catch (Exception e) {
            log.error("Unexpected error executing authoritative CREATE for {}: {}", key, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal persistence error: " + e.getMessage());
        }

        if (result instanceof AuthoritativePersistenceResult.Committed<IBaseResource> committed) {
            AuthoritativeVersion version = committed.authoritativeVersion();
            String etag = AuthoritativeVersionHelper.formatEtag(version);
            String payload = fhirContext.newJsonParser().setPrettyPrint(false).encodeResourceToString(committed.persistedResource());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(FHIR_JSON_MEDIA_TYPE);
            if (etag != null) {
                headers.set(AuthoritativeVersionHelper.HEADER_ETAG, etag);
            }
            if (version != null && version.value() != null) {
                headers.set(AuthoritativeVersionHelper.HEADER_X_AUTHORITATIVE_VERSION, version.value());
            }

            return new ResponseEntity<>(payload, headers, HttpStatus.CREATED);
        }

        if (result instanceof AuthoritativePersistenceResult.Conflict<IBaseResource> conflictResult) {
            AuthoritativePreconditionConflict conflict = conflictResult.conflict();
            log.warn("Authoritative CREATE conflict for {}: {}", key, conflict.message());

            HttpHeaders headers = new HttpHeaders();
            conflict.currentVersionOptional().ifPresent(v -> {
                headers.set(AuthoritativeVersionHelper.HEADER_ETAG, AuthoritativeVersionHelper.formatEtag(v));
                headers.set(AuthoritativeVersionHelper.HEADER_X_AUTHORITATIVE_VERSION, v.value());
            });

            return new ResponseEntity<>(conflict.message(), headers, HttpStatus.PRECONDITION_FAILED);
        }

        if (result instanceof AuthoritativePersistenceResult.NotCommitted<IBaseResource> notCommitted) {
            log.warn("Authoritative CREATE not committed for {}: {}", key, notCommitted.failureMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Create not committed: " + notCommitted.failureMessage());
        }

        if (result instanceof AuthoritativePersistenceResult.OutcomeUnknown<IBaseResource> outcomeUnknown) {
            log.error("Authoritative CREATE outcome unknown for {}: {}", key, outcomeUnknown.message());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Create outcome unknown: " + outcomeUnknown.message());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unexpected result type from persistence adapter for CREATE " + key);
    }

    private ResponseEntity<String> handleUpdate(ResourceKey key, String ifMatch, IBaseResource proposedResource) {
        Optional<ExpectedAuthoritativeVersion> expectedVersionOpt = AuthoritativeVersionHelper.parseIfMatch(ifMatch);
        if (expectedVersionOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Malformed or invalid If-Match header format: " + ifMatch);
        }

        ExpectedAuthoritativeVersion expectedVersion = expectedVersionOpt.get();
        log.debug("Authoritative UPDATE request for {} with expected version {}", key, expectedVersion);

        AuthoritativePersistenceResult<IBaseResource> result;
        try {
            result = persistencePort.update(key, proposedResource, expectedVersion);
        } catch (Exception e) {
            log.error("Unexpected error executing authoritative UPDATE for {}: {}", key, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal persistence error: " + e.getMessage());
        }

        if (result instanceof AuthoritativePersistenceResult.Committed<IBaseResource> committed) {
            AuthoritativeVersion version = committed.authoritativeVersion();
            String etag = AuthoritativeVersionHelper.formatEtag(version);
            String payload = fhirContext.newJsonParser().setPrettyPrint(false).encodeResourceToString(committed.persistedResource());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(FHIR_JSON_MEDIA_TYPE);
            if (etag != null) {
                headers.set(AuthoritativeVersionHelper.HEADER_ETAG, etag);
            }
            if (version != null && version.value() != null) {
                headers.set(AuthoritativeVersionHelper.HEADER_X_AUTHORITATIVE_VERSION, version.value());
            }

            return new ResponseEntity<>(payload, headers, HttpStatus.OK);
        }

        if (result instanceof AuthoritativePersistenceResult.Conflict<IBaseResource> conflictResult) {
            AuthoritativePreconditionConflict conflict = conflictResult.conflict();
            log.warn("Authoritative UPDATE conflict for {}: reason={}, msg={}", key, conflict.reason(), conflict.message());

            // Target resource absent for UPDATE returns 404 Not Found
            if (conflict.currentVersionOptional().isEmpty() && conflict.message() != null && conflict.message().toLowerCase().contains("does not exist")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Target resource does not exist for UPDATE: " + key);
            }

            HttpHeaders headers = new HttpHeaders();
            conflict.currentVersionOptional().ifPresent(v -> {
                headers.set(AuthoritativeVersionHelper.HEADER_ETAG, AuthoritativeVersionHelper.formatEtag(v));
                headers.set(AuthoritativeVersionHelper.HEADER_X_AUTHORITATIVE_VERSION, v.value());
            });

            return new ResponseEntity<>(conflict.message(), headers, HttpStatus.PRECONDITION_FAILED);
        }

        if (result instanceof AuthoritativePersistenceResult.NotCommitted<IBaseResource> notCommitted) {
            log.warn("Authoritative UPDATE not committed for {}: {}", key, notCommitted.failureMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Update not committed: " + notCommitted.failureMessage());
        }

        if (result instanceof AuthoritativePersistenceResult.OutcomeUnknown<IBaseResource> outcomeUnknown) {
            log.error("Authoritative UPDATE outcome unknown for {}: {}", key, outcomeUnknown.message());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Update outcome unknown: " + outcomeUnknown.message());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unexpected result type from persistence adapter for UPDATE " + key);
    }
}
