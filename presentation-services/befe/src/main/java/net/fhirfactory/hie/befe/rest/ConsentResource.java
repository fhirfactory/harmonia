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

import ca.uhn.fhir.parser.IParser;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import net.fhirfactory.hie.befe.service.FhirCacheService;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Consent;

import java.net.URI;

@Path("/fhir/Consent")
@RequestScoped
@Produces({"application/fhir+json", MediaType.APPLICATION_JSON})
@Consumes({"application/fhir+json", MediaType.APPLICATION_JSON})
public class ConsentResource {

    @Inject
    private FhirCacheService cacheService;

    @GET
    public Response search(
            @QueryParam("_id") String id,
            @QueryParam("name") String name,
            @QueryParam("identifier") String identifier) {
        Bundle bundle = cacheService.searchAsBundle("Consent", id, name, identifier);
        String json = cacheService.getJsonParser().encodeResourceToString(bundle);
        return Response.ok(json).build();
    }

    @GET
    @Path("/{id}")
    public Response read(@PathParam("id") String id) {
        String json = cacheService.getResourceJson("Consent", id);
        if (json == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"resourceType\":\"OperationOutcome\",\"issue\":[{\"severity\":\"error\",\"code\":\"not-found\",\"diagnostics\":\"Consent/" + id + " not found\"}]}")
                    .build();
        }
        return Response.ok(json).build();
    }

    @POST
    public Response create(String payload) {
        if (StringUtils.isBlank(payload)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        IParser parser = cacheService.getJsonParser();
        Consent resource = parser.parseResource(Consent.class, payload);
        Consent saved = cacheService.saveResource(resource);
        String json = parser.encodeResourceToString(saved);
        return Response.created(URI.create("/api/fhir/Consent/" + saved.getIdElement().getIdPart()))
                .entity(json)
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, String payload) {
        if (StringUtils.isBlank(payload)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        IParser parser = cacheService.getJsonParser();
        Consent resource = parser.parseResource(Consent.class, payload);
        resource.setId("Consent/" + id);
        Consent saved = cacheService.saveResource(resource);
        String json = parser.encodeResourceToString(saved);
        return Response.ok(json).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        boolean deleted = cacheService.deleteResource("Consent", id);
        if (deleted) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
