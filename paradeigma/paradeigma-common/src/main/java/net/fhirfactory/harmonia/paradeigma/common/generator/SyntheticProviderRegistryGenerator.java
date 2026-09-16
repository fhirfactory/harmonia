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

package net.fhirfactory.harmonia.paradeigma.common.generator;

import org.hl7.fhir.r5.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates synthetic, realistic HL7 FHIR Release 5 Provider Registry resources
 * including Australian Healthcare Identifiers (HPI-I, HPI-O), Locations, Endpoints,
 * HealthcareServices, PractitionerRoles, and Groups.
 */
public class SyntheticProviderRegistryGenerator {

    public static final String SYSTEM_HPI_I = "http://ns.electronichealth.net.au/id/hi/hpii/1.0";
    public static final String SYSTEM_HPI_O = "http://ns.electronichealth.net.au/id/hi/hpio/1.0";
    public static final String SYSTEM_ENDPOINT_CONN_TYPE = "http://terminology.hl7.org/CodeSystem/endpoint-connection-type";
    public static final String SYSTEM_PRACTITIONER_ROLE = "http://terminology.hl7.org/CodeSystem/practitioner-role";

    public static class ProviderRegistryGraph {
        private final Organization organization;
        private final Location location;
        private final HealthcareService healthcareService;
        private final Endpoint endpoint;
        private final Practitioner practitioner;
        private final PractitionerRole practitionerRole;
        private final Group group;

        public ProviderRegistryGraph(
                Organization organization,
                Location location,
                HealthcareService healthcareService,
                Endpoint endpoint,
                Practitioner practitioner,
                PractitionerRole practitionerRole,
                Group group) {
            this.organization = organization;
            this.location = location;
            this.healthcareService = healthcareService;
            this.endpoint = endpoint;
            this.practitioner = practitioner;
            this.practitionerRole = practitionerRole;
            this.group = group;
        }

        public Organization getOrganization() {
            return organization;
        }

        public Location getLocation() {
            return location;
        }

        public HealthcareService getHealthcareService() {
            return healthcareService;
        }

        public Endpoint getEndpoint() {
            return endpoint;
        }

        public Practitioner getPractitioner() {
            return practitioner;
        }

        public PractitionerRole getPractitionerRole() {
            return practitionerRole;
        }

        public Group getGroup() {
            return group;
        }
    }

    /**
     * Generates a fully interconnected provider directory hierarchy exemplar.
     */
    public static ProviderRegistryGraph generateConnectedProviderHierarchy(String suffix) {
        String s = suffix != null ? suffix : "01";
        String orgId = "org-stvincents-" + s;
        String epId = "ep-fhir-stvincents-" + s;
        String locId = "loc-cardiology-wing-" + s;
        String svcId = "svc-cardiology-consult-" + s;
        String practId = "pract-dr-bowman-" + s;
        String roleId = "role-cardiologist-" + s;
        String grpId = "grp-cardiology-panel-" + s;

        // 1. Organization
        Organization org = new Organization();
        org.setId(new IdType("Organization", orgId));
        org.setName("St Vincent's Health Network " + s);
        org.setActive(true);
        org.addIdentifier(new Identifier()
                .setSystem(SYSTEM_HPI_O)
                .setValue("80036200000000" + s));
        org.addType(new CodeableConcept().addCoding(
                new Coding("http://terminology.hl7.org/CodeSystem/organization-type", "prov", "Healthcare Provider")
        ));

        // 2. Endpoint
        Endpoint endpoint = new Endpoint();
        endpoint.setId(new IdType("Endpoint", epId));
        endpoint.setName("St Vincent's FHIR R5 Secure Gateway " + s);
        endpoint.setStatus(Endpoint.EndpointStatus.ACTIVE);
        endpoint.addConnectionType(new CodeableConcept().addCoding(
                new Coding(SYSTEM_ENDPOINT_CONN_TYPE, "hl7-fhir-rest", "HL7 FHIR REST")
        ));
        endpoint.setAddress("https://fhir.stvincents.org.au/r5/" + s);
        endpoint.setManagingOrganization(new Reference("Organization/" + orgId));
        endpoint.addIdentifier(new Identifier().setSystem("urn:endpoint:identifier").setValue("EP-STV-" + s));

        // 3. Location
        Location location = new Location();
        location.setId(new IdType("Location", locId));
        location.setName("Cardiology Department Suite " + s);
        location.setStatus(Location.LocationStatus.ACTIVE);
        location.setManagingOrganization(new Reference("Organization/" + orgId));
        location.addEndpoint(new Reference("Endpoint/" + epId));
        location.addIdentifier(new Identifier().setSystem("urn:location:identifier").setValue("LOC-CARD-" + s));

        // 4. HealthcareService
        HealthcareService service = new HealthcareService();
        service.setId(new IdType("HealthcareService", svcId));
        service.setName("Comprehensive Cardiology Consultation " + s);
        service.setActive(true);
        service.setProvidedBy(new Reference("Organization/" + orgId));
        service.addLocation(new Reference("Location/" + locId));
        service.addEndpoint(new Reference("Endpoint/" + epId));
        service.addCategory(new CodeableConcept().setText("Cardiology"));
        service.addIdentifier(new Identifier().setSystem("urn:service:identifier").setValue("SVC-CARD-" + s));

        // 5. Practitioner
        Practitioner practitioner = new Practitioner();
        practitioner.setId(new IdType("Practitioner", practId));
        practitioner.setActive(true);
        practitioner.addName(new HumanName()
                .setFamily("Bowman")
                .addGiven("Frank")
                .addPrefix("Dr."));
        practitioner.addIdentifier(new Identifier()
                .setSystem(SYSTEM_HPI_I)
                .setValue("80036100000000" + s));

        // 6. PractitionerRole
        PractitionerRole role = new PractitionerRole();
        role.setId(new IdType("PractitionerRole", roleId));
        role.setActive(true);
        role.setPractitioner(new Reference("Practitioner/" + practId));
        role.setOrganization(new Reference("Organization/" + orgId));
        role.addLocation(new Reference("Location/" + locId));
        role.addHealthcareService(new Reference("HealthcareService/" + svcId));
        role.addEndpoint(new Reference("Endpoint/" + epId));
        role.addCode(new CodeableConcept().addCoding(
                new Coding(SYSTEM_PRACTITIONER_ROLE, "doctor", "Doctor")
        ).setText("Lead Consultant Cardiologist"));
        role.addIdentifier(new Identifier().setSystem("urn:role:identifier").setValue("ROLE-CARD-" + s));

        // 7. Group
        Group group = new Group();
        group.setId(new IdType("Group", grpId));
        group.setName("Cardiovascular Clinical Specialist Panel " + s);
        group.setType(Group.GroupType.PRACTITIONER);
        group.setMembership(Group.GroupMembershipBasis.ENUMERATED);
        group.setManagingEntity(new Reference("Organization/" + orgId));
        group.addMember(new Group.GroupMemberComponent().setEntity(new Reference("Practitioner/" + practId)));
        group.addIdentifier(new Identifier().setSystem("urn:group:identifier").setValue("GRP-CARD-" + s));

        return new ProviderRegistryGraph(org, location, service, endpoint, practitioner, role, group);
    }
}
