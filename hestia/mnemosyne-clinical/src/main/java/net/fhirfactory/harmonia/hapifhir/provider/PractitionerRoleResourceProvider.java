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
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.PractitionerRole;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PractitionerRoleResourceProvider implements IResourceProvider {

    private final FhirStorageService storageService;

    public PractitionerRoleResourceProvider(FhirStorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return PractitionerRole.class;
    }

    @Create
    public MethodOutcome create(@ResourceParam PractitionerRole resource) {
        PractitionerRole created = storageService.createResource(resource);
        MethodOutcome outcome = new MethodOutcome();
        outcome.setCreated(true);
        outcome.setResource(created);
        outcome.setId(created.getIdElement());
        return outcome;
    }

    @Read
    public PractitionerRole read(@IdParam IdType theId) {
        return storageService.getResource("PractitionerRole", theId.getIdPart());
    }

    @Update
    public MethodOutcome update(@IdParam IdType theId, @ResourceParam PractitionerRole resource) {
        PractitionerRole updated = storageService.updateResource(theId.getIdPart(), resource);
        MethodOutcome outcome = new MethodOutcome();
        outcome.setResource(updated);
        outcome.setId(updated.getIdElement());
        return outcome;
    }

    @Delete
    public MethodOutcome delete(@IdParam IdType theId) {
        storageService.deleteResource("PractitionerRole", theId.getIdPart());
        MethodOutcome outcome = new MethodOutcome();
        outcome.setId(theId);
        return outcome;
    }

    @Search
    public List<PractitionerRole> search(
            @Description(shortDefinition = "The ID of the resource")
            @OptionalParam(name = "_id") StringParam theId,
            @Description(shortDefinition = "A practitioner role Identifier")
            @OptionalParam(name = "identifier") StringParam theIdentifier,
            @Description(shortDefinition = "Practitioner that is able to provide the defined services for the organization")
            @OptionalParam(name = "practitioner") StringParam thePractitioner,
            @Description(shortDefinition = "The organization where the Practitioner performs the role")
            @OptionalParam(name = "organization") StringParam theOrganization,
            @Description(shortDefinition = "The location where the Practitioner provides care")
            @OptionalParam(name = "location") StringParam theLocation,
            @Description(shortDefinition = "The list of healthcare services that this worker provides for this role's Organization/Location(s)")
            @OptionalParam(name = "service") StringParam theService,
            @Description(shortDefinition = "Whether this practitioner role record is in active use")
            @OptionalParam(name = "active") StringParam theActive
    ) {
        java.util.Map<String, String> params = new java.util.HashMap<>();
        if (theId != null) params.put("_id", theId.getValue());
        if (theIdentifier != null) params.put("identifier", theIdentifier.getValue());
        if (thePractitioner != null) params.put("practitioner", thePractitioner.getValue());
        if (theOrganization != null) params.put("organization", theOrganization.getValue());
        if (theLocation != null) params.put("location", theLocation.getValue());
        if (theService != null) params.put("service", theService.getValue());
        if (theActive != null) params.put("active", theActive.getValue());
        return storageService.searchResources("PractitionerRole", params);
    }
}
