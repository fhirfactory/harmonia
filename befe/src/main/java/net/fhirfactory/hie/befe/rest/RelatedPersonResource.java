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
import org.hl7.fhir.r5.model.RelatedPerson;

import java.net.URI;

@Path("/fhir/RelatedPerson")
@RequestScoped
@Produces({"application/fhir+json", MediaType.APPLICATION_JSON})
@Consumes({"application/fhir+json", MediaType.APPLICATION_JSON})
public class RelatedPersonResource {

    @Inject
    private FhirCacheService cacheService;

    @GET
    public Response search(
            @QueryParam("_id") String id,
            @QueryParam("name") String name,
            @QueryParam("identifier") String identifier) {
        Bundle bundle = cacheService.searchAsBundle("RelatedPerson", id, name, identifier);
        String json = cacheService.getJsonParser().encodeResourceToString(bundle);
        return Response.ok(json).build();
    }

    @GET
    @Path("/{id}")
    public Response read(@PathParam("id") String id) {
        String json = cacheService.getResourceJson("RelatedPerson", id);
        if (json == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"resourceType\":\"OperationOutcome\",\"issue\":[{\"severity\":\"error\",\"code\":\"not-found\",\"diagnostics\":\"RelatedPerson/" + id + " not found\"}]}")
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
        RelatedPerson resource = parser.parseResource(RelatedPerson.class, payload);
        RelatedPerson saved = cacheService.saveResource(resource);
        String json = parser.encodeResourceToString(saved);
        return Response.created(URI.create("/api/fhir/RelatedPerson/" + saved.getIdElement().getIdPart()))
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
        RelatedPerson resource = parser.parseResource(RelatedPerson.class, payload);
        resource.setId("RelatedPerson/" + id);
        RelatedPerson saved = cacheService.saveResource(resource);
        String json = parser.encodeResourceToString(saved);
        return Response.ok(json).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        boolean deleted = cacheService.deleteResource("RelatedPerson", id);
        if (deleted) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
