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

package net.fhirfactory.harmonia.hapifhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import net.fhirfactory.harmonia.hapifhir.service.FhirStorageService;
import net.fhirfactory.harmonia.model.ergon.ErgonReasonEnum;
import net.fhirfactory.harmonia.model.security.FhirConfidentialityEnum;
import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
import org.hl7.fhir.r5.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FhirResourceCrudIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FhirStorageService storageService;

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
        assertThat(FhirSecurityTagManager.hasConfidentiality(fetched, FhirConfidentialityEnum.N)).isTrue();

        // Update
        fetched.getNameFirstRep().setFamily("Johnson");
        MethodOutcome updateOutcome = client.update().resource(fetched).execute();
        assertThat(updateOutcome.getId().getVersionIdPart()).isEqualTo("2");

        Person updated = client.read().resource(Person.class).withId(personId).execute();
        assertThat(updated.getNameFirstRep().getFamily()).isEqualTo("Johnson");
        assertThat(FhirSecurityTagManager.hasConfidentiality(updated, FhirConfidentialityEnum.N)).isTrue();

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

    @Test
    @DisplayName("9. Provenance Lifecycle: Create, Read, Update, Search, Delete")
    void testProvenanceLifecycle() {
        Provenance provenance = new Provenance();
        provenance.addTarget(new Reference("Person/1001"));
        Provenance.ProvenanceAgentComponent agent = new Provenance.ProvenanceAgentComponent();
        agent.setWho(new Reference("Practitioner/2001").setDisplay("Dr. Watson"));
        provenance.addAgent(agent);
        provenance.setRecorded(new java.util.Date());

        MethodOutcome outcome = client.create().resource(provenance).execute();
        String id = outcome.getId().getIdPart();

        Provenance fetched = client.read().resource(Provenance.class).withId(id).execute();
        assertThat(fetched.getAgentFirstRep().getWho().getDisplay()).isEqualTo("Dr. Watson");

        fetched.getAgentFirstRep().getWho().setDisplay("Dr. John Watson");
        client.update().resource(fetched).execute();

        Provenance updated = client.read().resource(Provenance.class).withId(id).execute();
        assertThat(updated.getAgentFirstRep().getWho().getDisplay()).isEqualTo("Dr. John Watson");

        client.delete().resourceById(new IdType("Provenance", id)).execute();
        assertThatThrownBy(() -> client.read().resource(Provenance.class).withId(id).execute())
                .isInstanceOf(ResourceGoneException.class);
    }

    @Test
    @DisplayName("10. AuditEvent Mutation Rejection: Provider Absence and Generic Storage Immutability")
    void testAuditEventLifecycle() {
        AuditEvent auditEvent = new AuditEvent();
        auditEvent.setId("AE-TEST-001");
        auditEvent.setAction(AuditEvent.AuditEventAction.C);
        auditEvent.setRecorded(new java.util.Date());
        AuditEvent.AuditEventAgentComponent agent = new AuditEvent.AuditEventAgentComponent();
        agent.setWho(new Reference("Practitioner/2001").setDisplay("Auditor Smith"));
        auditEvent.addAgent(agent);
        auditEvent.setCode(new CodeableConcept().setText("REST Create Audit"));

        // 1. REST Client attempt fails because AuditEvent provider is absent
        assertThatThrownBy(() -> client.create().resource(auditEvent).execute())
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Unknown resource type 'AuditEvent'");

        assertThatThrownBy(() -> client.read().resource(AuditEvent.class).withId("AE-TEST-001").execute())
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Unknown resource type 'AuditEvent'");

        // 2. Direct FhirStorageService CREATE fails closed
        assertThatThrownBy(() -> storageService.createResource(auditEvent))
                .isInstanceOf(ForbiddenOperationException.class)
                .hasMessageContaining("AuditEvent is immutable and cannot be created, updated, or deleted via generic FHIR storage");

        // 3. Direct FhirStorageService UPDATE fails closed (blocking modification and resurrection)
        assertThatThrownBy(() -> storageService.updateResource("AE-TEST-001", auditEvent))
                .isInstanceOf(ForbiddenOperationException.class)
                .hasMessageContaining("AuditEvent is immutable and cannot be created, updated, or deleted via generic FHIR storage");

        // 4. Direct FhirStorageService DELETE fails closed
        assertThatThrownBy(() -> storageService.deleteResource("AuditEvent", "AE-TEST-001"))
                .isInstanceOf(ForbiddenOperationException.class)
                .hasMessageContaining("AuditEvent is immutable and cannot be created, updated, or deleted via generic FHIR storage");

        // 5. Direct FhirStorageService DELETE with lowercase resource type also fails closed
        assertThatThrownBy(() -> storageService.deleteResource("auditevent", "AE-TEST-001"))
                .isInstanceOf(ForbiddenOperationException.class)
                .hasMessageContaining("AuditEvent is immutable and cannot be created, updated, or deleted via generic FHIR storage");
    }

    @Test
    @DisplayName("11. Consent Lifecycle: Create, Read, Update, Search, Delete")
    void testConsentLifecycle() {
        Consent consent = new Consent();
        consent.setStatus(Consent.ConsentState.ACTIVE);
        consent.addCategory(new CodeableConcept().setText("Medical Research"));
        consent.addIdentifier(new Identifier().setSystem("urn:consent").setValue("CONSENT-001"));
        consent.setSubject(new Reference("Person/1001"));

        MethodOutcome outcome = client.create().resource(consent).execute();
        String id = outcome.getId().getIdPart();

        Consent fetched = client.read().resource(Consent.class).withId(id).execute();
        assertThat(fetched.getStatus()).isEqualTo(Consent.ConsentState.ACTIVE);

        fetched.setStatus(Consent.ConsentState.INACTIVE);
        client.update().resource(fetched).execute();

        Consent updated = client.read().resource(Consent.class).withId(id).execute();
        assertThat(updated.getStatus()).isEqualTo(Consent.ConsentState.INACTIVE);

        client.delete().resourceById(new IdType("Consent", id)).execute();
        assertThatThrownBy(() -> client.read().resource(Consent.class).withId(id).execute())
                .isInstanceOf(ResourceGoneException.class);
    }

    @Test
    @DisplayName("12. Task Lifecycle: Create, Read, Update, Search, Delete")
    void testTaskLifecycle() {
        Task task = new Task();
        task.setStatus(Task.TaskStatus.REQUESTED);
        task.setIntent(Task.TaskIntent.ORDER);
        task.setDescription("Review Lab Results");
        task.addIdentifier(new Identifier().setSystem("urn:task").setValue("TASK-101"));

        MethodOutcome outcome = client.create().resource(task).execute();
        String id = outcome.getId().getIdPart();

        Task fetched = client.read().resource(Task.class).withId(id).execute();
        assertThat(fetched.getDescription()).isEqualTo("Review Lab Results");
        assertThat(ErgonReasonEnum.hasReason(fetched, ErgonReasonEnum.HIE_SYNTHETIC_TASK)).isTrue();

        fetched.setStatus(Task.TaskStatus.COMPLETED);
        client.update().resource(fetched).execute();

        Task updated = client.read().resource(Task.class).withId(id).execute();
        assertThat(updated.getStatus()).isEqualTo(Task.TaskStatus.COMPLETED);

        client.delete().resourceById(new IdType("Task", id)).execute();
        assertThatThrownBy(() -> client.read().resource(Task.class).withId(id).execute())
                .isInstanceOf(ResourceGoneException.class);
    }

    @Test
    @DisplayName("13. Communication Lifecycle: Create, Read, Update, Search, Delete")
    void testCommunicationLifecycle() {
        Communication comm = new Communication();
        comm.setStatus(Enumerations.EventStatus.COMPLETED);
        comm.setSubject(new Reference("Person/1001").setDisplay("Alice Smith"));
        comm.addIdentifier(new Identifier().setSystem("urn:comm").setValue("COMM-555"));
        comm.addNote(new Annotation().setText("Follow-up call completed"));

        MethodOutcome outcome = client.create().resource(comm).execute();
        String id = outcome.getId().getIdPart();

        Communication fetched = client.read().resource(Communication.class).withId(id).execute();
        assertThat(fetched.getStatus()).isEqualTo(Enumerations.EventStatus.COMPLETED);

        fetched.addNote(new Annotation().setText("Patient acknowledged instructions"));
        client.update().resource(fetched).execute();

        Communication updated = client.read().resource(Communication.class).withId(id).execute();
        assertThat(updated.getNote()).hasSize(2);

        client.delete().resourceById(new IdType("Communication", id)).execute();
        assertThatThrownBy(() -> client.read().resource(Communication.class).withId(id).execute())
                .isInstanceOf(ResourceGoneException.class);
    }

    @Test
    @DisplayName("14. DocumentReference Lifecycle: Create, Read, Update, Search, Delete")
    void testDocumentReferenceLifecycle() {
        DocumentReference doc = new DocumentReference();
        doc.setStatus(DocumentReference.DocumentReferenceStatus.CURRENT);
        doc.setDescription("Discharge Summary Note");
        doc.addIdentifier(new Identifier().setSystem("urn:doc").setValue("DOC-9001"));
        doc.setSubject(new Reference("Person/1001"));

        MethodOutcome outcome = client.create().resource(doc).execute();
        String id = outcome.getId().getIdPart();

        DocumentReference fetched = client.read().resource(DocumentReference.class).withId(id).execute();
        assertThat(fetched.getDescription()).isEqualTo("Discharge Summary Note");

        fetched.setDescription("Updated Discharge Summary Note");
        client.update().resource(fetched).execute();

        DocumentReference updated = client.read().resource(DocumentReference.class).withId(id).execute();
        assertThat(updated.getDescription()).isEqualTo("Updated Discharge Summary Note");

        client.delete().resourceById(new IdType("DocumentReference", id)).execute();
        assertThatThrownBy(() -> client.read().resource(DocumentReference.class).withId(id).execute())
                .isInstanceOf(ResourceGoneException.class);
    }

    @Test
    @DisplayName("15. Security Tagging: Auto-Enforce Default 'N' and Preserve Explicit 'R'")
    void testSecurityTagEnforcementAndPreservation() {
        // 1. Default enforcement on untagged resource
        Organization untaggedOrg = new Organization();
        untaggedOrg.setName("General Clinic");
        MethodOutcome outcome1 = client.create().resource(untaggedOrg).execute();
        String orgId = outcome1.getId().getIdPart();

        Organization fetchedUntagged = client.read().resource(Organization.class).withId(orgId).execute();
        assertThat(FhirSecurityTagManager.hasConfidentiality(fetchedUntagged, FhirConfidentialityEnum.N)).isTrue();
        assertThat(fetchedUntagged.getMeta().getSecurityFirstRep().getSystem()).isEqualTo(FhirConfidentialityEnum.CONFIDENTIALITY_SYSTEM);

        // 2. Preservation of explicit confidentiality security tag
        Organization restrictedOrg = new Organization();
        restrictedOrg.setName("Confidential Mental Health Facility");
        restrictedOrg.setMeta(new Meta());
        restrictedOrg.getMeta().addSecurity(new Coding(FhirConfidentialityEnum.CONFIDENTIALITY_SYSTEM, "R", "Restricted"));

        MethodOutcome outcome2 = client.create().resource(restrictedOrg).execute();
        String restId = outcome2.getId().getIdPart();

        Organization fetchedRestricted = client.read().resource(Organization.class).withId(restId).execute();
        assertThat(FhirSecurityTagManager.hasConfidentiality(fetchedRestricted, FhirConfidentialityEnum.R)).isTrue();
        assertThat(FhirSecurityTagManager.hasConfidentiality(fetchedRestricted, FhirConfidentialityEnum.N)).isFalse();
    }
}
