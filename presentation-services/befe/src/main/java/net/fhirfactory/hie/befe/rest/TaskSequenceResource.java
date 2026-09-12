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

package net.fhirfactory.hie.befe.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.fhirfactory.hie.befe.service.TaskSequenceCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Path("/operations/sequences")
public class TaskSequenceResource {

    private static final Logger log = LoggerFactory.getLogger(TaskSequenceResource.class);

    @Inject
    private TaskSequenceCacheService sequenceCacheService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSequences() {
        try {
            List<String> sequences = sequenceCacheService.getAllSequenceJsons();
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < sequences.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(sequences.get(i));
            }
            sb.append("]");
            return Response.ok(sb.toString()).build();
        } catch (Exception e) {
            log.error("Error retrieving task sequences: {}", e.getMessage(), e);
            return Response.serverError().entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSequenceById(@PathParam("id") String id) {
        try {
            Optional<String> seq = sequenceCacheService.getSequenceJson(id);
            if (seq.isPresent()) {
                return Response.ok(seq.get()).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"TaskSequence not found\"}").build();
        } catch (Exception e) {
            log.error("Error retrieving task sequence [{}]: {}", id, e.getMessage(), e);
            return Response.serverError().entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSequence(String payload) {
        try {
            String saved = sequenceCacheService.saveSequence(null, payload);
            return Response.status(Response.Status.CREATED).entity(saved).build();
        } catch (Exception e) {
            log.error("Error creating task sequence: {}", e.getMessage(), e);
            return Response.serverError().entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSequence(@PathParam("id") String id, String payload) {
        try {
            String saved = sequenceCacheService.saveSequence(id, payload);
            return Response.ok(saved).build();
        } catch (Exception e) {
            log.error("Error updating task sequence [{}]: {}", id, e.getMessage(), e);
            return Response.serverError().entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSequence(@PathParam("id") String id) {
        try {
            boolean deleted = sequenceCacheService.deleteSequence(id);
            if (deleted) {
                return Response.noContent().build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"TaskSequence not found\"}").build();
        } catch (Exception e) {
            log.error("Error deleting task sequence [{}]: {}", id, e.getMessage(), e);
            return Response.serverError().entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
}
