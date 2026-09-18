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

package net.fhirfactory.harmonia.hapifhir.provider;

import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import net.fhirfactory.harmonia.hapifhir.service.FhirStorageService;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Endpoint;
import org.hl7.fhir.r5.model.IdType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HAPI FHIR R5 Resource Provider for {@link Endpoint} resources.
 */
@Component
public class EndpointResourceProvider implements IResourceProvider {

    private final FhirStorageService storageService;

    public EndpointResourceProvider(FhirStorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Endpoint.class;
    }

    @Create
    public MethodOutcome create(@ResourceParam Endpoint resource) {
        Endpoint created = storageService.createResource(resource);
        MethodOutcome outcome = new MethodOutcome();
        outcome.setCreated(true);
        outcome.setResource(created);
        outcome.setId(created.getIdElement());
        return outcome;
    }

    @Read
    public Endpoint read(@IdParam IdType theId) {
        return storageService.getResource("Endpoint", theId.getIdPart());
    }

    @Update
    public MethodOutcome update(@IdParam IdType theId, @ResourceParam Endpoint resource) {
        Endpoint updated = storageService.updateResource(theId.getIdPart(), resource);
        MethodOutcome outcome = new MethodOutcome();
        outcome.setResource(updated);
        outcome.setId(updated.getIdElement());
        return outcome;
    }

    @Delete
    public MethodOutcome delete(@IdParam IdType theId) {
        storageService.deleteResource("Endpoint", theId.getIdPart());
        MethodOutcome outcome = new MethodOutcome();
        outcome.setId(theId);
        return outcome;
    }

    @Search
    public List<Endpoint> search(
            @Description(shortDefinition = "The ID of the resource")
            @OptionalParam(name = "_id") StringParam theId,
            @Description(shortDefinition = "A name of the Endpoint")
            @OptionalParam(name = "name") StringParam theName,
            @Description(shortDefinition = "An endpoint identifier")
            @OptionalParam(name = "identifier") StringParam theIdentifier,
            @Description(shortDefinition = "The organization managing the endpoint")
            @OptionalParam(name = "organization") StringParam theOrganization,
            @Description(shortDefinition = "The status of the endpoint")
            @OptionalParam(name = "status") StringParam theStatus,
            @Description(shortDefinition = "Protocol/technical standard of the endpoint")
            @OptionalParam(name = "connection-type") StringParam theConnectionType
    ) {
        Map<String, String> params = new HashMap<>();
        if (theId != null) params.put("_id", theId.getValue());
        if (theName != null) params.put("name", theName.getValue());
        if (theIdentifier != null) params.put("identifier", theIdentifier.getValue());
        if (theOrganization != null) params.put("organization", theOrganization.getValue());
        if (theStatus != null) params.put("status", theStatus.getValue());
        if (theConnectionType != null) params.put("connection-type", theConnectionType.getValue());

        return storageService.searchResources("Endpoint", params);
    }
}
