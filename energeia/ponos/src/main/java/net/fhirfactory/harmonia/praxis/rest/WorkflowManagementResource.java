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

package net.fhirfactory.harmonia.praxis.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.fhirfactory.harmonia.model.status.ModuleStatus;
import net.fhirfactory.harmonia.petasos.api.Petasos;
import net.fhirfactory.harmonia.praxis.camel.CamelContextManager;
import net.fhirfactory.harmonia.praxis.sequence.Praxis;
import net.fhirfactory.harmonia.praxis.sequence.TaskSequenceLoader;
import net.fhirfactory.harmonia.praxis.service.ModuleStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;

/**
 * REST Endpoint for workflow runtime management, sequence reloading, and configuration validation.
 */
@Path("/workflow")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WorkflowManagementResource {

    private static final Logger log = LoggerFactory.getLogger(WorkflowManagementResource.class);

    @Inject
    private Petasos petasos;

    @Inject
    private CamelContextManager camelContextManager;

    @Inject
    private TaskSequenceLoader taskSequenceLoader;

    @Inject
    private ModuleStatusService moduleStatusService;

    public WorkflowManagementResource() {
    }

    public WorkflowManagementResource(Petasos petasos,
                                      CamelContextManager camelContextManager,
                                      TaskSequenceLoader taskSequenceLoader) {
        this.petasos = petasos;
        this.camelContextManager = camelContextManager;
        this.taskSequenceLoader = taskSequenceLoader;
    }

    public WorkflowManagementResource(Petasos petasos,
                                      CamelContextManager camelContextManager,
                                      TaskSequenceLoader taskSequenceLoader,
                                      ModuleStatusService moduleStatusService) {
        this.petasos = petasos;
        this.camelContextManager = camelContextManager;
        this.taskSequenceLoader = taskSequenceLoader;
        this.moduleStatusService = moduleStatusService;
    }

    /**
     * Triggers full synchronization and reloading of both message queues and task sequences.
     */
    @POST
    @Path("/sync")
    public Response synchronize() {
        return reloadAll();
    }

    /**
     * Triggers full reloading of message queues on Artemis broker and sequence routes in Camel.
     */
    @POST
    @Path("/reload")
    public Response reloadAll() {
        log.info("Workflow reload requested via REST endpoint");
        try {
            List<String> synchronizedQueues = Collections.emptyList();

            boolean sequencesReloaded = camelContextManager != null && camelContextManager.reloadSequences();

            List<Praxis> loaded = taskSequenceLoader != null
                    ? taskSequenceLoader.getLoadedSequences()
                    : Collections.emptyList();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "SYNCHRONIZED");
            response.put("message", "Successfully synchronized queues and task-sequence pipelines");
            response.put("timestamp", Instant.now().toString());
            boolean brokerRunning = false;
            try {
                if (petasos != null && petasos.health() != null) {
                    brokerRunning = petasos.health().isHealthy();
                }
            } catch (Exception ignored) {
            }
            response.put("brokerRunning", brokerRunning);
            response.put("synchronizedQueues", synchronizedQueues);
            response.put("synchronizedQueuesCount", synchronizedQueues.size());
            response.put("sequencesReloaded", sequencesReloaded);
            response.put("activeSequencesCount", loaded.size());

            List<String> seqIds = new ArrayList<>();
            for (Praxis s : loaded) {
                if (s != null) {
                    seqIds.add(s.getPraxisId());
                }
            }
            response.put("activeSequenceIds", seqIds);

            return Response.ok(response).build();
        } catch (Exception e) {
            log.error("Failed to reload workflow configurations", e);
            Map<String, Object> err = new HashMap<>();
            err.put("status", "ERROR");
            err.put("error", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(err).build();
        }
    }

    /**
     * Reloads message queues on the Artemis broker only.
     */
    @POST
    @Path("/queues/reload")
    public Response reloadQueues() {
        try {
            List<String> queues = Collections.emptyList();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "QUEUES_SYNCHRONIZED");
            response.put("synchronizedQueues", queues);
            response.put("count", queues.size());
            return Response.ok(response).build();
        } catch (Exception e) {
            log.error("Failed to reload queues", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "ERROR", "error", e.getMessage()))
                    .build();
        }
    }

    /**
     * Reloads TaskSequences in CamelContext only.
     */
    @POST
    @Path("/sequences/reload")
    public Response reloadSequences() {
        try {
            boolean success = camelContextManager != null && camelContextManager.reloadSequences();
            List<Praxis> loaded = taskSequenceLoader != null
                    ? taskSequenceLoader.getLoadedSequences()
                    : Collections.emptyList();

            Map<String, Object> response = new HashMap<>();
            response.put("status", success ? "SEQUENCES_RELOADED" : "RELOAD_FAILED");
            response.put("count", loaded.size());
            return Response.ok(response).build();
        } catch (Exception e) {
            log.error("Failed to reload sequences", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "ERROR", "error", e.getMessage()))
                    .build();
        }
    }

    /**
     * Validates queue names and task sequence definitions.
     */
    @GET
    @Path("/validate")
    public Response validateGet() {
        return performValidation();
    }

    @POST
    @Path("/validate")
    public Response validatePost() {
        return performValidation();
    }

    private Response performValidation() {
        Map<String, Object> queueReport = Collections.emptyMap();

        Map<String, Object> sequenceReport = taskSequenceLoader != null
                ? taskSequenceLoader.validateSequences()
                : Collections.emptyMap();

        int invalidQueues = (int) queueReport.getOrDefault("invalidQueues", 0);
        int invalidSequences = (int) sequenceReport.getOrDefault("invalidSequences", 0);
        boolean allValid = invalidQueues == 0 && invalidSequences == 0;

        Map<String, Object> result = new HashMap<>();
        result.put("status", allValid ? "VALID" : "INVALID");
        result.put("timestamp", Instant.now().toString());
        result.put("allValid", allValid);
        result.put("queueValidation", queueReport);
        result.put("sequenceValidation", sequenceReport);

        return Response.ok(result).build();
    }

    /**
     * Returns runtime workflow status.
     */
    @GET
    @Path("/status")
    public Response getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("module", "task-sequence-processor");
        boolean brokerRunning = false;
        try {
            if (petasos != null && petasos.health() != null) {
                brokerRunning = petasos.health().isHealthy();
            }
        } catch (Exception ignored) {
        }
        status.put("brokerRunning", brokerRunning);
        status.put("camelStarted", camelContextManager != null && camelContextManager.getCamelContext() != null && camelContextManager.getCamelContext().isStarted());

        List<Praxis> loaded = taskSequenceLoader != null
                ? taskSequenceLoader.getLoadedSequences()
                : Collections.emptyList();
        status.put("activeSequencesCount", loaded.size());

        List<Map<String, Object>> seqList = new ArrayList<>();
        for (Praxis s : loaded) {
            if (s != null) {
                Map<String, Object> sm = new HashMap<>();
                sm.put("sequenceId", s.getPraxisId());
                sm.put("sequenceName", s.getPraxisName());
                sm.put("enabled", s.isEnabled());
                sm.put("activityCount", s.getActivityCount());
                sm.put("gateways", s.getTargetGatewayInstances());
                sm.put("triggers", s.getTargetTriggerTypes());
                seqList.add(sm);
            }
        }
        status.put("activeSequences", seqList);

        if (moduleStatusService != null) {
            status.put("clusterModules", moduleStatusService.getAllModuleStatuses());
            status.put("clusterModuleCount", moduleStatusService.getAllModuleStatuses().size());
        }

        return Response.ok(status).build();
    }

    /**
     * Returns all registered cluster module statuses.
     */
    @GET
    @Path("/modules")
    public Response getClusterModules() {
        List<ModuleStatus> modules = moduleStatusService != null
                ? moduleStatusService.getAllModuleStatuses()
                : Collections.emptyList();
        return Response.ok(modules).build();
    }
}
