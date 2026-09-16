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
import org.hl7.fhir.r5.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProviderRegistrySearchTest {

    @LocalServerPort
    private int port;

    private IGenericClient client;
    private final FhirContext fhirContext = FhirContext.forR5();

    @BeforeEach
    void setUp() {
        String serverBase = "http://localhost:" + port + "/fhir";
        this.client = fhirContext.newRestfulGenericClient(serverBase);
    }

    @Test
    @DisplayName("Search Practitioner with multi-parameter filter")
    void testPractitionerSearch() {
        Practitioner pr = new Practitioner();
        pr.setActive(true);
        pr.addName(new HumanName().setFamily("Bowman").addGiven("Frank"));
        pr.addIdentifier(new Identifier().setSystem("http://ns.electronichealth.net.au/id/hi/hpii/1.0").setValue("8003610000000001"));
        MethodOutcome outcome = client.create().resource(pr).execute();
        String id = outcome.getId().getIdPart();

        Bundle result = client.search().forResource(Practitioner.class)
                .where(Practitioner.NAME.matches().value("Bowman"))
                .and(Practitioner.IDENTIFIER.exactly().systemAndCode("http://ns.electronichealth.net.au/id/hi/hpii/1.0", "8003610000000001"))
                .returnBundle(Bundle.class)
                .execute();

        assertThat(result.getEntry()).isNotEmpty();
        Practitioner found = (Practitioner) result.getEntryFirstRep().getResource();
        assertThat(found.getIdElement().getIdPart()).isEqualTo(id);
        assertThat(found.getNameFirstRep().getFamily()).isEqualTo("Bowman");
    }

    @Test
    @DisplayName("Search Endpoint with connection-type, organization, and status")
    void testEndpointSearch() {
        Organization org = new Organization();
        org.setName("St Vincent Hospital");
        MethodOutcome orgOutcome = client.create().resource(org).execute();
        String orgId = orgOutcome.getId().getIdPart();

        Endpoint ep = new Endpoint();
        ep.setName("St Vincent FHIR REST Endpoint");
        ep.setStatus(Endpoint.EndpointStatus.ACTIVE);
        ep.setManagingOrganization(new Reference("Organization/" + orgId));
        ep.addConnectionType(new CodeableConcept().addCoding(new Coding("http://terminology.hl7.org/CodeSystem/endpoint-connection-type", "hl7-fhir-rest", "HL7 FHIR REST")));
        ep.setAddress("https://fhir.stvincent.org.au/r5");
        ep.addIdentifier(new Identifier().setSystem("urn:endpoint:id").setValue("EP-STV-01"));

        MethodOutcome epOutcome = client.create().resource(ep).execute();
        String epId = epOutcome.getId().getIdPart();

        Bundle result = client.search().forResource(Endpoint.class)
                .where(Endpoint.NAME.matches().value("St Vincent"))
                .and(Endpoint.ORGANIZATION.hasId("Organization/" + orgId))
                .returnBundle(Bundle.class)
                .execute();

        assertThat(result.getEntry()).isNotEmpty();
        Endpoint found = (Endpoint) result.getEntryFirstRep().getResource();
        assertThat(found.getIdElement().getIdPart()).isEqualTo(epId);
        assertThat(found.getAddress()).isEqualTo("https://fhir.stvincent.org.au/r5");
    }

    @Test
    @DisplayName("Search PractitionerRole with practitioner and organization references")
    void testPractitionerRoleSearch() {
        Practitioner pr = new Practitioner();
        pr.addName(new HumanName().setFamily("Taylor").addGiven("Alice"));
        MethodOutcome prOutcome = client.create().resource(pr).execute();
        String prId = prOutcome.getId().getIdPart();

        Organization org = new Organization();
        org.setName("Austin Health");
        MethodOutcome orgOutcome = client.create().resource(org).execute();
        String orgId = orgOutcome.getId().getIdPart();

        PractitionerRole role = new PractitionerRole();
        role.setActive(true);
        role.setPractitioner(new Reference("Practitioner/" + prId));
        role.setOrganization(new Reference("Organization/" + orgId));
        role.addCode(new CodeableConcept().setText("Cardiologist"));
        MethodOutcome roleOutcome = client.create().resource(role).execute();
        String roleId = roleOutcome.getId().getIdPart();

        Bundle result = client.search().forResource(PractitionerRole.class)
                .where(PractitionerRole.PRACTITIONER.hasId("Practitioner/" + prId))
                .and(PractitionerRole.ORGANIZATION.hasId("Organization/" + orgId))
                .returnBundle(Bundle.class)
                .execute();

        assertThat(result.getEntry()).isNotEmpty();
        PractitionerRole found = (PractitionerRole) result.getEntryFirstRep().getResource();
        assertThat(found.getIdElement().getIdPart()).isEqualTo(roleId);
        assertThat(found.getCodeFirstRep().getText()).isEqualTo("Cardiologist");
    }

    @Test
    @DisplayName("Search Group by name and type")
    void testGroupSearch() {
        Group group = new Group();
        group.setName("Cardiology Specialist Panel");
        group.setType(Group.GroupType.PRACTITIONER);
        group.setMembership(Group.GroupMembershipBasis.ENUMERATED);
        group.addIdentifier(new Identifier().setSystem("urn:group:id").setValue("GRP-CARD-01"));

        MethodOutcome outcome = client.create().resource(group).execute();
        String groupId = outcome.getId().getIdPart();

        Bundle result = client.search().forResource(Group.class)
                .where(Group.NAME.matches().value("Cardiology"))
                .and(Group.TYPE.exactly().code("practitioner"))
                .returnBundle(Bundle.class)
                .execute();

        assertThat(result.getEntry()).isNotEmpty();
        Group found = (Group) result.getEntryFirstRep().getResource();
        assertThat(found.getIdElement().getIdPart()).isEqualTo(groupId);
        assertThat(found.getName()).isEqualTo("Cardiology Specialist Panel");
    }
}
