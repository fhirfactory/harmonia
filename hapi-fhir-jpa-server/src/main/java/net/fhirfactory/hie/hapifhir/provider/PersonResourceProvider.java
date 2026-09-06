package net.fhirfactory.hie.hapifhir.provider;

import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import net.fhirfactory.hie.hapifhir.service.FhirStorageService;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Person;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonResourceProvider implements IResourceProvider {

    private final FhirStorageService storageService;

    public PersonResourceProvider(FhirStorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Person.class;
    }

    @Create
    public MethodOutcome create(@ResourceParam Person person) {
        Person created = storageService.createResource(person);
        MethodOutcome outcome = new MethodOutcome();
        outcome.setCreated(true);
        outcome.setResource(created);
        outcome.setId(created.getIdElement());
        return outcome;
    }

    @Read
    public Person read(@IdParam IdType theId) {
        return storageService.getResource("Person", theId.getIdPart());
    }

    @Update
    public MethodOutcome update(@IdParam IdType theId, @ResourceParam Person person) {
        Person updated = storageService.updateResource(theId.getIdPart(), person);
        MethodOutcome outcome = new MethodOutcome();
        outcome.setResource(updated);
        outcome.setId(updated.getIdElement());
        return outcome;
    }

    @Delete
    public MethodOutcome delete(@IdParam IdType theId) {
        storageService.deleteResource("Person", theId.getIdPart());
        MethodOutcome outcome = new MethodOutcome();
        outcome.setId(theId);
        return outcome;
    }

    @Search
    public List<Person> search(
            @Description(shortDefinition = "The ID of the resource")
            @OptionalParam(name = "_id") StringParam theId,
            @Description(shortDefinition = "A server defined search that may match any of the string fields in the Person.name")
            @OptionalParam(name = "name") StringParam theName,
            @Description(shortDefinition = "A person Identifier")
            @OptionalParam(name = "identifier") StringParam theIdentifier
    ) {
        String idStr = theId != null ? theId.getValue() : null;
        String nameStr = theName != null ? theName.getValue() : null;
        String identStr = theIdentifier != null ? theIdentifier.getValue() : null;
        return storageService.searchResources("Person", idStr, nameStr, identStr);
    }
}
