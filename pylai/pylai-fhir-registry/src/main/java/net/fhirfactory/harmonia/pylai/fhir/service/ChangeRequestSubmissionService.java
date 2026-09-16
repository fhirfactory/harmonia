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

package net.fhirfactory.harmonia.pylai.fhir.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.ProviderRegistryChangePragma;
import net.fhirfactory.harmonia.model.registry.ProviderRegistryConstants;
import net.fhirfactory.harmonia.petasos.api.Petasos;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import net.fhirfactory.harmonia.praxis.cache.PragmaCacheService;
import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Service orchestrating ingestion, structural validation, Pragma creation,
 * and asynchronous Petasos message publication for Provider Registry change requests.
 */
@Service
public class ChangeRequestSubmissionService {

    private static final Logger log = LoggerFactory.getLogger(ChangeRequestSubmissionService.class);

    private final FhirContext fhirContext;
    private final PragmaCacheService pragmaCacheService;
    private final Petasos petasos;

    public ChangeRequestSubmissionService(
            @Autowired(required = false) PragmaCacheService pragmaCacheService,
            @Autowired(required = false) Petasos petasos) {
        this.fhirContext = FhirContext.forR5();
        this.pragmaCacheService = pragmaCacheService;
        this.petasos = petasos;
    }

    public static class SubmissionResult {
        private final String pragmaId;
        private final String correlationId;
        private final String statusLocation;
        private final Pragma pragma;

        public SubmissionResult(String pragmaId, String correlationId, String statusLocation, Pragma pragma) {
            this.pragmaId = pragmaId;
            this.correlationId = correlationId;
            this.statusLocation = statusLocation;
            this.pragma = pragma;
        }

        public String getPragmaId() {
            return pragmaId;
        }

        public String getCorrelationId() {
            return correlationId;
        }

        public String getStatusLocation() {
            return statusLocation;
        }

        public Pragma getPragma() {
            return pragma;
        }
    }

    /**
     * Ingests, parses, and queues a FHIR Provider Registry change request with caller's security context.
     */
    public SubmissionResult submitChangeRequest(
            String operation,
            String resourceType,
            String fhirId,
            String payloadJson,
            ThemisPrincipal principal,
            Set<ThemisAuthority> authorities,
            ThemisSecurityContext securityContext,
            String sourceSystem,
            String correlationId,
            String ifMatch) {

        if (StringUtils.isBlank(payloadJson)) {
            throw new InvalidRequestException("HTTP request body cannot be empty");
        }

        if (!ProviderRegistryConstants.isSupportedResourceType(resourceType)) {
            throw new UnprocessableEntityException("Resource type [" + resourceType + "] is not supported by Provider Registry");
        }

        // 1. Structural FHIR Parsing & Validation
        Resource resource;
        try {
            IParser parser = fhirContext.newJsonParser();
            IBaseResource parsed = parser.parseResource(payloadJson);
            if (!(parsed instanceof Resource r)) {
                throw new UnprocessableEntityException("Parsed object is not a valid FHIR R5 Resource");
            }
            resource = r;
        } catch (Exception ex) {
            log.warn("Structural FHIR parsing failed for {} change request: {}", resourceType, ex.getMessage());
            throw new UnprocessableEntityException("Malformed FHIR R5 payload: " + ex.getMessage());
        }

        if (!resource.fhirType().equalsIgnoreCase(resourceType)) {
            throw new UnprocessableEntityException("Payload resourceType [" + resource.fhirType() + "] does not match URL path [" + resourceType + "]");
        }

        if (StringUtils.isNotBlank(fhirId)) {
            resource.setId(new IdType(resourceType, fhirId));
        }

        String finalCorrId = StringUtils.isNotBlank(correlationId) ? correlationId : UUID.randomUUID().toString();
        String finalRequester = principal != null && StringUtils.isNotBlank(principal.principalId())
                ? principal.principalId()
                : "api-client";
        String finalSource = StringUtils.isNotBlank(sourceSystem) ? sourceSystem : "pylai-fhir-registry";

        // 2. Build Canonical Pragma Change Instance
        Pragma pragma = ProviderRegistryChangePragma.buildChangeRequestPragma(
                operation,
                resource,
                finalRequester,
                finalSource,
                finalCorrId,
                ifMatch
        );

        // Attach Themis Originating Security Context
        ThemisPrincipal finalPrincipal = principal != null
                ? principal
                : ThemisPrincipal.of(finalRequester, PrincipalType.HUMAN, finalSource);
        pragma.setOriginatingPrincipal(finalPrincipal);

        if (authorities != null) {
            for (ThemisAuthority auth : authorities) {
                pragma.addOriginatingAuthority(auth);
            }
        }

        ThemisSecurityContext finalContext = securityContext != null
                ? securityContext
                : ThemisSecurityContext.builder().principal(finalPrincipal).correlationId(finalCorrId).build();
        pragma.setOriginatingSecurityContext(finalContext);
        pragma.setPolicyVersion("1.0.0");

        // 3. Persist Pragma to Cache for status tracking
        if (pragmaCacheService != null) {
            pragmaCacheService.savePragma(pragma);
        }

        // 4. Publish Petasos Message to Artemis change queue
        if (petasos != null) {
            try {
                String pragmaJson = fhirContext.newJsonParser().encodeResourceToString(
                        net.fhirfactory.harmonia.model.pragma.PragmaFhirConverter.toFhirTask(pragma)
                );
                PetasosMessage msg = PetasosMessage.builder()
                        .messageId(pragma.getPragmaId())
                        .correlationId(finalCorrId)
                        .source(finalSource)
                        .destination(PetasosDestination.queue(ProviderRegistryConstants.QUEUE_PROVIDER_REGISTRY_CHANGE_REQUEST))
                        .payload(pragmaJson)
                        .build();
                petasos.send(msg);
                log.info("Published Petasos change message for Pragma/{} to queue [{}]",
                        pragma.getPragmaId(), ProviderRegistryConstants.QUEUE_PROVIDER_REGISTRY_CHANGE_REQUEST);
            } catch (Exception ex) {
                log.error("Failed to publish change request for Pragma/{}: {}", pragma.getPragmaId(), ex.getMessage(), ex);
            }
        }

        String statusLocation = "/Task/" + pragma.getPragmaId();
        return new SubmissionResult(pragma.getPragmaId(), finalCorrId, statusLocation, pragma);
    }

    /**
     * Ingests, parses, and queues a FHIR Provider Registry change request (legacy signature).
     */
    public SubmissionResult submitChangeRequest(
            String operation,
            String resourceType,
            String fhirId,
            String payloadJson,
            String requester,
            String sourceSystem,
            String correlationId,
            String ifMatch) {

        ThemisPrincipal principal = StringUtils.isNotBlank(requester)
                ? ThemisPrincipal.of(requester, PrincipalType.HUMAN, sourceSystem != null ? sourceSystem : "pylai")
                : null;

        return submitChangeRequest(
                operation,
                resourceType,
                fhirId,
                payloadJson,
                principal,
                null,
                null,
                sourceSystem,
                correlationId,
                ifMatch
        );
    }
}
