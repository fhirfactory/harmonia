package net.fhirfactory.hie.hapifhir.provider;

import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import net.fhirfactory.hie.hapifhir.service.FhirStorageService;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.HealthcareService;
import org.hl7.fhir.r5.model.IdType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HealthcareServiceResourceProvider implements IResourceProvider {

    private final FhirStorageService storageService;

    public HealthcareServiceResourceProvider(FhirStorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return HealthcareService.class;
    }

    @Create
    public MethodOutcome create(@ResourceParam HealthcareService resource) {
        HealthcareService created = storageService.createResource(resource);
        MethodOutcome outcome = new MethodOutcome();
        outcome.setCreated(true);
        outcome.setResource(created);
        outcome.setId(created.getIdElement());
        return outcome;
    }

    @Read
    public HealthcareService read(@IdParam IdType theId) {
        return storageService.getResource("HealthcareService", theId.getIdPart());
    }

    @Update
    public MethodOutcome update(@IdParam IdType theId, @ResourceParam HealthcareService resource) {
        HealthcareService updated = storageService.updateResource(theId.getIdPart(), resource);
        MethodOutcome outcome = new MethodOutcome();
        outcome.setResource(updated);
        outcome.setId(updated.getIdElement());
        return outcome;
    }

    @Delete
    public MethodOutcome delete(@IdParam IdType theId) {
        storageService.deleteResource("HealthcareService", theId.getIdPart());
        MethodOutcome outcome = new MethodOutcome();
        outcome.setId(theId);
        return outcome;
    }

    @Search
    public List<HealthcareService> search(
            @Description(shortDefinition = "The ID of the resource")
            @OptionalParam(name = "_id") StringParam theId,
            @Description(shortDefinition = "A portion of the service name")
            @OptionalParam(name = "name") StringParam theName,
            @Description(shortDefinition = "A healthcare service Identifier")
            @OptionalParam(name = "identifier") StringParam theIdentifier
    ) {
        String idStr = theId != null ? theId.getValue() : null;
        String nameStr = theName != null ? theName.getValue() : null;
        String identStr = theIdentifier != null ? theIdentifier.getValue() : null;
        return storageService.searchResources("HealthcareService", idStr, nameStr, identStr);
    }
}
