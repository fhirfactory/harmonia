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
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.fhirfactory.harmonia.kleio.audit.model.AuditQuery;
import net.fhirfactory.harmonia.kleio.audit.model.HarmoniaAuditEvent;
import net.fhirfactory.harmonia.kleio.audit.service.AuditService;
import net.fhirfactory.harmonia.kleio.fhir.mapper.HarmoniaAuditEventMapper;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.AuditEvent;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.OperationOutcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Path("/fhir/AuditEvent")
@RequestScoped
@Produces({"application/fhir+json", MediaType.APPLICATION_JSON})
@Consumes({"application/fhir+json", MediaType.APPLICATION_JSON})
public class AuditEventResource {

    private static final Logger log = LoggerFactory.getLogger(AuditEventResource.class);
    private static final FhirContext FHIR_CONTEXT = FhirContext.forR5();

    @Inject
    private AuditService auditService;

    @Inject
    private HarmoniaAuditEventMapper auditMapper;

    public AuditEventResource() {
    }

    public AuditEventResource(AuditService auditService, HarmoniaAuditEventMapper auditMapper) {
        this.auditService = auditService;
        this.auditMapper = auditMapper;
    }

    public void setAuditService(AuditService auditService) {
        this.auditService = auditService;
    }

    public void setAuditMapper(HarmoniaAuditEventMapper auditMapper) {
        this.auditMapper = auditMapper;
    }

    private HarmoniaAuditEventMapper getAuditMapper() {
        if (auditMapper == null) {
            auditMapper = new HarmoniaAuditEventMapper();
        }
        return auditMapper;
    }

    private IParser getJsonParser() {
        return FHIR_CONTEXT.newJsonParser().setPrettyPrint(true);
    }

    private Response buildOperationOutcomeResponse(Response.Status status, OperationOutcome.IssueType issueType, String diagnostics) {
        OperationOutcome outcome = new OperationOutcome();
        outcome.addIssue()
                .setSeverity(OperationOutcome.IssueSeverity.ERROR)
                .setCode(issueType)
                .setDiagnostics(diagnostics);
        String json = getJsonParser().encodeResourceToString(outcome);
        return Response.status(status)
                .type("application/fhir+json")
                .entity(json)
                .build();
    }

    @GET
    public Response search(
            @QueryParam("_id") String id,
            @QueryParam("name") String name,
            @QueryParam("identifier") String identifier) {
        if (StringUtils.isNotBlank(name) || StringUtils.isNotBlank(identifier)) {
            return buildOperationOutcomeResponse(
                    Response.Status.BAD_REQUEST,
                    OperationOutcome.IssueType.NOTSUPPORTED,
                    "Search parameter not supported for AuditEvent"
            );
        }

        try {
            AuditQuery.Builder queryBuilder = AuditQuery.builder();
            if (StringUtils.isNotBlank(id)) {
                queryBuilder.eventId(id.trim());
            }
            AuditQuery query = queryBuilder.build();
            List<HarmoniaAuditEvent> events = auditService.find(query);

            Bundle bundle = new Bundle();
            bundle.setType(Bundle.BundleType.SEARCHSET);
            bundle.setTotal(events != null ? events.size() : 0);

            if (events != null) {
                for (HarmoniaAuditEvent event : events) {
                    AuditEvent fhirEvent = getAuditMapper().toFhir(event);
                    Bundle.BundleEntryComponent entry = bundle.addEntry();
                    String eventId = fhirEvent.getIdElement().getIdPart();
                    if (StringUtils.isNotBlank(eventId)) {
                        entry.setFullUrl("AuditEvent/" + eventId);
                    }
                    entry.setResource(fhirEvent);
                }
            }

            String json = getJsonParser().encodeResourceToString(bundle);
            return Response.ok(json).type("application/fhir+json").build();
        } catch (Exception e) {
            log.error("Internal error searching AuditEvents: {}", e.getClass().getSimpleName());
            return buildOperationOutcomeResponse(
                    Response.Status.INTERNAL_SERVER_ERROR,
                    OperationOutcome.IssueType.EXCEPTION,
                    "Internal error retrieving AuditEvent"
            );
        }
    }

    @GET
    @Path("/{id}")
    public Response read(@PathParam("id") String id) {
        if (StringUtils.isBlank(id)) {
            return buildOperationOutcomeResponse(
                    Response.Status.NOT_FOUND,
                    OperationOutcome.IssueType.NOTFOUND,
                    "AuditEvent/ not found"
            );
        }

        try {
            Optional<HarmoniaAuditEvent> eventOpt = auditService.findById(id.trim());
            if (eventOpt.isEmpty()) {
                return buildOperationOutcomeResponse(
                        Response.Status.NOT_FOUND,
                        OperationOutcome.IssueType.NOTFOUND,
                        "AuditEvent/" + id + " not found"
                );
            }

            AuditEvent fhirEvent = getAuditMapper().toFhir(eventOpt.get());
            String json = getJsonParser().encodeResourceToString(fhirEvent);
            return Response.ok(json).type("application/fhir+json").build();
        } catch (Exception e) {
            log.error("Internal error reading AuditEvent [{}]: {}", id, e.getClass().getSimpleName());
            return buildOperationOutcomeResponse(
                    Response.Status.INTERNAL_SERVER_ERROR,
                    OperationOutcome.IssueType.EXCEPTION,
                    "Internal error retrieving AuditEvent"
            );
        }
    }
}
