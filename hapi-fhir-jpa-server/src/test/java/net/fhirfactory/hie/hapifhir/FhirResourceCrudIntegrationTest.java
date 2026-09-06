package net.fhirfactory.hie.hapifhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FhirResourceCrudIntegrationTest {

    @LocalServerPort
    private int port;

    private IGenericClient client;
    private final FhirContext fhirContext = FhirContext.forR5();

    @BeforeEach
    void setUp() {
        String serverBase = "http://localhost:" + port + "/fhir";
        client = fhirContext.newRestfulGenericClient(serverBase);
    }

    @Test
    @DisplayName("1. Person Lifecycle: Create, Read, Update, Search, Delete")
    void testPersonLifecycle() {
        // Create
        Person person = new Person();
        person.addName(new HumanName().setFamily("Smith").addGiven("Alice"));
        person.setGender(Enumerations.AdministrativeGender.FEMALE);
        person.addIdentifier(new Identifier().setSystem("urn:mrn").setValue("MRN-1001"));

        MethodOutcome outcome = client.create().resource(person).execute();
        assertThat(outcome.getCreated()).isTrue();
        String personId = outcome.getId().getIdPart();
        assertThat(personId).isNotBlank();

        // Read
        Person fetched = client.read().resource(Person.class).withId(personId).execute();
        assertThat(fetched.getNameFirstRep().getFamily()).isEqualTo("Smith");
        assertThat(fetched.getGender()).isEqualTo(Enumerations.AdministrativeGender.FEMALE);

        // Update
        fetched.getNameFirstRep().setFamily("Johnson");
        MethodOutcome updateOutcome = client.update().resource(fetched).execute();
        assertThat(updateOutcome.getId().getVersionIdPart()).isEqualTo("2");

        Person updated = client.read().resource(Person.class).withId(personId).execute();
        assertThat(updated.getNameFirstRep().getFamily()).isEqualTo("Johnson");

        // Search by name
        Bundle bundle = client.search().forResource(Person.class)
                .where(Person.NAME.matches().value("Johnson"))
                .returnBundle(Bundle.class)
                .execute();
        assertThat(bundle.getEntry()).isNotEmpty();

        // Delete
        client.delete().resourceById(new IdType("Person", personId)).execute();

        // Verify read fails with gone
        assertThatThrownBy(() -> client.read().resource(Person.class).withId(personId).execute())
                .isInstanceOf(ResourceGoneException.class);
    }

    @Test
    @DisplayName("2. RelatedPerson Lifecycle: Create, Read, Update, Search, Delete")
    void testRelatedPersonLifecycle() {
        RelatedPerson relatedPerson = new RelatedPerson();
        relatedPerson.addName(new HumanName().setFamily("Brown").addGiven("Charlie"));
        relatedPerson.setRelationship(Collections.singletonList(
                new CodeableConcept().setText("Brother")
        ));
        relatedPerson.addIdentifier(new Identifier().setSystem("urn:id").setValue("RP-2001"));

        MethodOutcome outcome = client.create().resource(relatedPerson).execute();
        assertThat(outcome.getCreated()).isTrue();
        String id = outcome.getId().getIdPart();

        RelatedPerson fetched = client.read().resource(RelatedPerson.class).withId(id).execute();
        assertThat(fetched.getNameFirstRep().getFamily()).isEqualTo("Brown");

        fetched.getNameFirstRep().setFamily("Brown-Jones");
        client.update().resource(fetched).execute();

        RelatedPerson updated = client.read().resource(RelatedPerson.class).withId(id).execute();
        assertThat(updated.getNameFirstRep().getFamily()).isEqualTo("Brown-Jones");

        client.delete().resourceById(new IdType("RelatedPerson", id)).execute();
        assertThatThrownBy(() -> client.read().resource(RelatedPerson.class).withId(id).execute())
                .isInstanceOf(ResourceGoneException.class);
    }

    @Test
    @DisplayName("3. Practitioner Lifecycle: Create, Read, Update, Search, Delete")
    void testPractitionerLifecycle() {
        Practitioner practitioner = new Practitioner();
        practitioner.addName(new HumanName().setFamily("Watson").addGiven("John").setPrefix(Collections.singletonList(new StringType("Dr."))));
        practitioner.setGender(Enumerations.AdministrativeGender.MALE);
        practitioner.addIdentifier(new Identifier().setSystem("http://hl7.org/fhir/sid/us-npi").setValue("NPI-998877"));

        MethodOutcome outcome = client.create().resource(practitioner).execute();
        String id = outcome.getId().getIdPart();

        Practitioner fetched = client.read().resource(Practitioner.class).withId(id).execute();
        assertThat(fetched.getNameFirstRep().getFamily()).isEqualTo("Watson");

        fetched.addQualification(new Practitioner.PractitionerQualificationComponent()
                .setCode(new CodeableConcept().setText("MD - Cardiology")));
        client.update().resource(fetched).execute();

        Bundle bundle = client.search().forResource(Practitioner.class)
                .where(Practitioner.NAME.matches().value("Watson"))
                .returnBundle(Bundle.class)
                .execute();
        assertThat(bundle.getEntry()).isNotEmpty();

        client.delete().resourceById(new IdType("Practitioner", id)).execute();
        assertThatThrownBy(() -> client.read().resource(Practitioner.class).withId(id).execute())
                .isInstanceOf(ResourceGoneException.class);
    }

    @Test
    @DisplayName("4. PractitionerRole Lifecycle: Create, Read, Update, Search, Delete")
    void testPractitionerRoleLifecycle() {
        PractitionerRole role = new PractitionerRole();
        role.setActive(true);
        role.addIdentifier(new Identifier().setSystem("urn:roles").setValue("ROLE-123"));

        MethodOutcome outcome = client.create().resource(role).execute();
        String id = outcome.getId().getIdPart();

        PractitionerRole fetched = client.read().resource(PractitionerRole.class).withId(id).execute();
        assertThat(fetched.getActive()).isTrue();

        fetched.setActive(false);
        client.update().resource(fetched).execute();

        PractitionerRole updated = client.read().resource(PractitionerRole.class).withId(id).execute();
        assertThat(updated.getActive()).isFalse();

        client.delete().resourceById(new IdType("PractitionerRole", id)).execute();
        assertThatThrownBy(() -> client.read().resource(PractitionerRole.class).withId(id).execute())
                .isInstanceOf(ResourceGoneException.class);
    }

    @Test
    @DisplayName("5. Organization Lifecycle: Create, Read, Update, Search, Delete")
    void testOrganizationLifecycle() {
        Organization org = new Organization();
        org.setName("Metropolitan Health Authority");
        org.setActive(true);
        org.addIdentifier(new Identifier().setSystem("urn:org").setValue("ORG-500"));

        MethodOutcome outcome = client.create().resource(org).execute();
        String id = outcome.getId().getIdPart();

        Organization fetched = client.read().resource(Organization.class).withId(id).execute();
        assertThat(fetched.getName()).isEqualTo("Metropolitan Health Authority");

        fetched.setName("Metropolitan Health System");
        client.update().resource(fetched).execute();

        Bundle bundle = client.search().forResource(Organization.class)
                .where(Organization.NAME.matches().value("System"))
                .returnBundle(Bundle.class)
                .execute();
        assertThat(bundle.getEntry()).isNotEmpty();

        client.delete().resourceById(new IdType("Organization", id)).execute();
        assertThatThrownBy(() -> client.read().resource(Organization.class).withId(id).execute())
                .isInstanceOf(ResourceGoneException.class);
    }

    @Test
    @DisplayName("6. Location Lifecycle: Create, Read, Update, Search, Delete")
    void testLocationLifecycle() {
        Location location = new Location();
        location.setName("Building A - Emergency Ward");
        location.setStatus(Location.LocationStatus.ACTIVE);
        location.setMode(Location.LocationMode.INSTANCE);

        MethodOutcome outcome = client.create().resource(location).execute();
        String id = outcome.getId().getIdPart();

        Location fetched = client.read().resource(Location.class).withId(id).execute();
        assertThat(fetched.getName()).isEqualTo("Building A - Emergency Ward");

        fetched.setDescription("Trauma Care & ICU");
        client.update().resource(fetched).execute();

        Bundle bundle = client.search().forResource(Location.class)
                .where(Location.NAME.matches().value("Emergency"))
                .returnBundle(Bundle.class)
                .execute();
        assertThat(bundle.getEntry()).isNotEmpty();

        client.delete().resourceById(new IdType("Location", id)).execute();
        assertThatThrownBy(() -> client.read().resource(Location.class).withId(id).execute())
                .isInstanceOf(ResourceGoneException.class);
    }

    @Test
    @DisplayName("7. HealthcareService Lifecycle: Create, Read, Update, Search, Delete")
    void testHealthcareServiceLifecycle() {
        HealthcareService service = new HealthcareService();
        service.setName("Cardiovascular Diagnostic Service");
        service.setActive(true);

        MethodOutcome outcome = client.create().resource(service).execute();
        String id = outcome.getId().getIdPart();

        HealthcareService fetched = client.read().resource(HealthcareService.class).withId(id).execute();
        assertThat(fetched.getName()).isEqualTo("Cardiovascular Diagnostic Service");

        fetched.setComment("24/7 On-call cardiologist");
        client.update().resource(fetched).execute();

        Bundle bundle = client.search().forResource(HealthcareService.class)
                .where(HealthcareService.NAME.matches().value("Cardiovascular"))
                .returnBundle(Bundle.class)
                .execute();
        assertThat(bundle.getEntry()).isNotEmpty();

        client.delete().resourceById(new IdType("HealthcareService", id)).execute();
        assertThatThrownBy(() -> client.read().resource(HealthcareService.class).withId(id).execute())
                .isInstanceOf(ResourceGoneException.class);
    }

    @Test
    @DisplayName("8. Group Lifecycle: Create, Read, Update, Search, Delete")
    void testGroupLifecycle() {
        Group group = new Group();
        group.setType(Group.GroupType.PERSON);
        group.setMembership(Group.GroupMembershipBasis.DEFINITIONAL);
        group.setName("Diabetes Management Cohort 2026");

        MethodOutcome outcome = client.create().resource(group).execute();
        String id = outcome.getId().getIdPart();

        Group fetched = client.read().resource(Group.class).withId(id).execute();
        assertThat(fetched.getName()).isEqualTo("Diabetes Management Cohort 2026");

        fetched.setQuantity(25);
        client.update().resource(fetched).execute();

        Bundle bundle = client.search().forResource(Group.class)
                .where(Group.NAME.matches().value("Diabetes"))
                .returnBundle(Bundle.class)
                .execute();
        assertThat(bundle.getEntry()).isNotEmpty();

        client.delete().resourceById(new IdType("Group", id)).execute();
        assertThatThrownBy(() -> client.read().resource(Group.class).withId(id).execute())
                .isInstanceOf(ResourceGoneException.class);
    }
}
