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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import net.fhirfactory.harmonia.befe.model.operations.*;
import net.fhirfactory.harmonia.befe.security.ThemisOperationsAuthorizer;
import net.fhirfactory.harmonia.befe.service.OperationsAggregatorService;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * JAX-RS REST endpoint exposing the complete Harmonia Operations API suite (/api/operations/*).
 * Enforces default-deny RBAC security evaluation via Themis.
 */
@ApplicationScoped
@Path("/operations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OperationsResource {

    private static final Logger log = LoggerFactory.getLogger(OperationsResource.class);

    @Inject
    private OperationsAggregatorService aggregatorService;

    @Inject
    private ThemisOperationsAuthorizer themisAuthorizer;

    public OperationsResource() {
    }

    public OperationsResource(OperationsAggregatorService aggregatorService, ThemisOperationsAuthorizer themisAuthorizer) {
        this.aggregatorService = aggregatorService;
        this.themisAuthorizer = themisAuthorizer;
    }

    // =========================================================================
    // 1. Summary
    // =========================================================================

    @GET
    @Path("/summary")
    public Response getSummary(@Context HttpHeaders headers) {
        Response authCheck = checkAuthorization(headers, "/api/operations/summary", "GET");
        if (authCheck != null) {
            return authCheck;
        }
        OperationalSummary summary = getAggregator().getOperationsSummary();
        return Response.ok(summary).build();
    }

    // =========================================================================
    // 2. Subsystems
    // =========================================================================

    @GET
    @Path("/subsystems")
    public Response getSubsystems(@Context HttpHeaders headers) {
        Response authCheck = checkAuthorization(headers, "/api/operations/subsystems", "GET");
        if (authCheck != null) {
            return authCheck;
        }
        List<OperationalSubsystem> subsystems = getAggregator().getSubsystems();
        return Response.ok(subsystems).build();
    }

    @GET
    @Path("/subsystems/{id}")
    public Response getSubsystem(@PathParam("id") String id, @Context HttpHeaders headers) {
        Response authCheck = checkAuthorization(headers, "/api/operations/subsystems/" + id, "GET");
        if (authCheck != null) {
            return authCheck;
        }
        return getAggregator().getSubsystem(id)
                .map(sub -> Response.ok(sub).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Subsystem not found", "subsystemId", id))
                        .build());
    }

    @GET
    @Path("/subsystems/{id}/instances")
    public Response getSubsystemInstances(@PathParam("id") String id, @Context HttpHeaders headers) {
        Response authCheck = checkAuthorization(headers, "/api/operations/subsystems/" + id + "/instances", "GET");
        if (authCheck != null) {
            return authCheck;
        }
        List<OperationalInstance> instances = getAggregator().getSubsystemInstances(id);
        return Response.ok(instances).build();
    }

    @GET
    @Path("/subsystems/{id}/health")
    public Response getSubsystemHealth(@PathParam("id") String id, @Context HttpHeaders headers) {
        Response authCheck = checkAuthorization(headers, "/api/operations/subsystems/" + id + "/health", "GET");
        if (authCheck != null) {
            return authCheck;
        }
        OperationalHealth health = getAggregator().getSubsystemHealth(id);
        return Response.ok(health).build();
    }

    @GET
    @Path("/subsystems/{id}/statistics")
    public Response getSubsystemStatistics(@PathParam("id") String id,
                                           @QueryParam("window") @DefaultValue("15m") String window,
                                           @Context HttpHeaders headers) {
        Response authCheck = checkAuthorization(headers, "/api/operations/subsystems/" + id + "/statistics", "GET");
        if (authCheck != null) {
            return authCheck;
        }
        Map<String, TimeSeries> stats = getAggregator().getSubsystemStatistics(id, window);
        return Response.ok(stats).build();
    }

    // =========================================================================
    // 3. Queues
    // =========================================================================

    @GET
    @Path("/queues")
    public Response getQueues(@QueryParam("status") String status,
                              @QueryParam("search") String search,
                              @Context HttpHeaders headers) {
        Response authCheck = checkAuthorization(headers, "/api/operations/queues", "GET");
        if (authCheck != null) {
            return authCheck;
        }
        List<QueueSummary> queues = getAggregator().getQueues(status, search);
        return Response.ok(queues).build();
    }

    @GET
    @Path("/queues/{id}")
    public Response getQueue(@PathParam("id") String id, @Context HttpHeaders headers) {
        Response authCheck = checkAuthorization(headers, "/api/operations/queues/" + id, "GET");
        if (authCheck != null) {
            return authCheck;
        }
        return getAggregator().getQueue(id)
                .map(q -> Response.ok(q).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Queue not found", "queueId", id))
                        .build());
    }

    // =========================================================================
    // 4. Workflows & Pragmas
    // =========================================================================

    @GET
    @Path("/workflows")
    public Response getWorkflows(@QueryParam("search") String search, @Context HttpHeaders headers) {
        Response authCheck = checkAuthorization(headers, "/api/operations/workflows", "GET");
        if (authCheck != null) {
            return authCheck;
        }
        List<WorkflowSummary> workflows = getAggregator().getWorkflows(search);
        return Response.ok(workflows).build();
    }

    @GET
    @Path("/workflows/{id}")
    public Response getWorkflow(@PathParam("id") String id, @Context HttpHeaders headers) {
        Response authCheck = checkAuthorization(headers, "/api/operations/workflows/" + id, "GET");
        if (authCheck != null) {
            return authCheck;
        }
        return getAggregator().getWorkflow(id)
                .map(w -> Response.ok(w).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Workflow not found", "workflowId", id))
                        .build());
    }

    @GET
    @Path("/workflows/{id}/pragmas")
    public Response getWorkflowPragmas(@PathParam("id") String id, @Context HttpHeaders headers) {
        Response authCheck = checkAuthorization(headers, "/api/operations/workflows/" + id + "/pragmas", "GET");
        if (authCheck != null) {
            return authCheck;
        }
        List<PragmaSummary> pragmas = getAggregator().getPragmasForWorkflow(id);
        return Response.ok(pragmas).build();
    }

    @GET
    @Path("/pragmas/{id}")
    public Response getPragma(@PathParam("id") String id, @Context HttpHeaders headers) {
        Response authCheck = checkAuthorization(headers, "/api/operations/pragmas/" + id, "GET");
        if (authCheck != null) {
            return authCheck;
        }
        return getAggregator().getPragma(id)
                .map(p -> Response.ok(p).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Pragma not found", "pragmaId", id))
                        .build());
    }

    // =========================================================================
    // 5. Events
    // =========================================================================

    @GET
    @Path("/events")
    public Response getEvents(@QueryParam("correlationId") String correlationId,
                              @QueryParam("causationId") String causationId,
                              @QueryParam("messageId") String messageId,
                              @QueryParam("pragmaId") String pragmaId,
                              @QueryParam("subsystem") String subsystem,
                              @QueryParam("eventType") String eventType,
                              @QueryParam("status") String status,
                              @QueryParam("from") Long from,
                              @QueryParam("to") Long to,
                              @QueryParam("page") @DefaultValue("0") int page,
                              @QueryParam("pageSize") @DefaultValue("50") int pageSize,
                              @Context HttpHeaders headers) {
        Response authCheck = checkAuthorization(headers, "/api/operations/events", "GET");
        if (authCheck != null) {
            return authCheck;
        }
        List<OperationalEvent> events = getAggregator().getEvents(
                correlationId, causationId, messageId, pragmaId, subsystem, eventType, status, from, to, page, pageSize
        );
        return Response.ok(events).build();
    }

    @GET
    @Path("/events/{id}")
    public Response getEvent(@PathParam("id") String id, @Context HttpHeaders headers) {
        Response authCheck = checkAuthorization(headers, "/api/operations/events/" + id, "GET");
        if (authCheck != null) {
            return authCheck;
        }
        return getAggregator().getEvent(id)
                .map(e -> Response.ok(e).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Event not found", "eventId", id))
                        .build());
    }

    // =========================================================================
    // 6. Alerts
    // =========================================================================

    @GET
    @Path("/alerts")
    public Response getAlerts(@QueryParam("severity") String severity,
                              @QueryParam("status") String status,
                              @QueryParam("subsystem") String subsystem,
                              @Context HttpHeaders headers) {
        Response authCheck = checkAuthorization(headers, "/api/operations/alerts", "GET");
        if (authCheck != null) {
            return authCheck;
        }
        List<OperationalAlert> alerts = getAggregator().getAlerts(severity, status, subsystem);
        return Response.ok(alerts).build();
    }

    @POST
    @Path("/alerts/{id}/acknowledge")
    public Response acknowledgeAlert(@PathParam("id") String alertId,
                                     Map<String, String> body,
                                     @Context HttpHeaders headers) {
        Response authCheck = checkAuthorization(headers, "/api/operations/alerts/" + alertId + "/acknowledge", "POST");
        if (authCheck != null) {
            return authCheck;
        }
        String operator = (body != null && body.containsKey("operator")) ? body.get("operator") : "operator";
        boolean success = getAggregator().acknowledgeAlert(alertId, operator);
        if (success) {
            return Response.ok(Map.of("alertId", alertId, "status", "ACKNOWLEDGED", "acknowledgedBy", operator)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "Alert not found", "alertId", alertId)).build();
        }
    }

    // =========================================================================
    // Authorization Helper
    // =========================================================================

    private Response checkAuthorization(HttpHeaders httpHeaders, String path, String method) {
        ThemisOperationsAuthorizer authorizer = getAuthorizer();
        if (authorizer == null) {
            return null;
        }
        Map<String, String> headersMap = extractHeadersMap(httpHeaders);
        ThemisAuthorizationDecision decision = authorizer.authorizeRequest(headersMap, path, method);
        if (decision.isDenied()) {
            log.warn("Access DENIED for Operations endpoint [{}]: {}", path, decision.message());
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(Map.of(
                            "error", "Forbidden",
                            "reason", decision.reason().name(),
                            "message", decision.message()
                    ))
                    .build();
        }
        return null;
    }

    private Map<String, String> extractHeadersMap(HttpHeaders httpHeaders) {
        Map<String, String> map = new HashMap<>();
        if (httpHeaders != null && httpHeaders.getRequestHeaders() != null) {
            for (Map.Entry<String, List<String>> entry : httpHeaders.getRequestHeaders().entrySet()) {
                if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                    map.put(entry.getKey(), entry.getValue().get(0));
                }
            }
        }
        return map;
    }

    private OperationsAggregatorService getAggregator() {
        if (aggregatorService == null) {
            aggregatorService = new OperationsAggregatorService();
            aggregatorService.init();
        }
        return aggregatorService;
    }

    private ThemisOperationsAuthorizer getAuthorizer() {
        if (themisAuthorizer == null) {
            themisAuthorizer = new ThemisOperationsAuthorizer();
        }
        return themisAuthorizer;
    }

    public void setAggregatorService(OperationsAggregatorService aggregatorService) {
        this.aggregatorService = aggregatorService;
    }

    public void setThemisAuthorizer(ThemisOperationsAuthorizer themisAuthorizer) {
        this.themisAuthorizer = themisAuthorizer;
    }
}
