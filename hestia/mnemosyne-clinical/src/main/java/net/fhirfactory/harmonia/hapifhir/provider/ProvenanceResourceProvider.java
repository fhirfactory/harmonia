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
import org.hl7.fhir.r5.model.Provenance;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProvenanceResourceProvider implements IResourceProvider {

    private final FhirStorageService storageService;

    public ProvenanceResourceProvider(FhirStorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Provenance.class;
    }

    @Create
    public MethodOutcome create(@ResourceParam Provenance resource) {
        Provenance created = storageService.createResource(resource);
        MethodOutcome outcome = new MethodOutcome();
        outcome.setCreated(true);
        outcome.setResource(created);
        outcome.setId(created.getIdElement());
        return outcome;
    }

    @Read
    public Provenance read(@IdParam IdType theId) {
        return storageService.getResource("Provenance", theId.getIdPart());
    }

    @Update
    public MethodOutcome update(@IdParam IdType theId, @ResourceParam Provenance resource) {
        Provenance updated = storageService.updateResource(theId.getIdPart(), resource);
        MethodOutcome outcome = new MethodOutcome();
        outcome.setResource(updated);
        outcome.setId(updated.getIdElement());
        return outcome;
    }

    @Delete
    public MethodOutcome delete(@IdParam IdType theId) {
        storageService.deleteResource("Provenance", theId.getIdPart());
        MethodOutcome outcome = new MethodOutcome();
        outcome.setId(theId);
        return outcome;
    }

    @Search
    public List<Provenance> search(
            @Description(shortDefinition = "The ID of the resource")
            @OptionalParam(name = "_id") StringParam theId,
            @Description(shortDefinition = "A descriptive name or target")
            @OptionalParam(name = "name") StringParam theName,
            @Description(shortDefinition = "A provenance identifier")
            @OptionalParam(name = "identifier") StringParam theIdentifier
    ) {
        String idStr = theId != null ? theId.getValue() : null;
        String nameStr = theName != null ? theName.getValue() : null;
        String identStr = theIdentifier != null ? theIdentifier.getValue() : null;
        return storageService.searchResources("Provenance", idStr, nameStr, identStr);
    }
}
