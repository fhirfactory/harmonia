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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.paradeigma.common.generator;

import org.hl7.fhir.r5.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generates synthetic, realistic HL7 FHIR Release 5 Provider Registry resources
 * including Australian Healthcare Identifiers (HPI-I, HPI-O), Locations, Endpoints,
 * HealthcareServices, PractitionerRoles, and Groups with deterministic seed support.
 */
public class SyntheticProviderRegistryGenerator {

    public static final String SYSTEM_HPI_I = "http://ns.electronichealth.net.au/id/hi/hpii/1.0";
    public static final String SYSTEM_HPI_O = "http://ns.electronichealth.net.au/id/hi/hpio/1.0";
    public static final String SYSTEM_ENDPOINT_CONN_TYPE = "http://terminology.hl7.org/CodeSystem/endpoint-connection-type";
    public static final String SYSTEM_PRACTITIONER_ROLE = "http://terminology.hl7.org/CodeSystem/practitioner-role";

    private final SeedRandom random;
    private final PractitionerGenerator practitionerGenerator;
    private final PractitionerRoleGenerator practitionerRoleGenerator;
    private final OrganizationGenerator organizationGenerator;
    private final LocationGenerator locationGenerator;
    private final HealthcareServiceGenerator healthcareServiceGenerator;
    private final EndpointGenerator endpointGenerator;
    private final GroupGenerator groupGenerator;

    public SyntheticProviderRegistryGenerator() {
        this(new SeedRandom());
    }

    public SyntheticProviderRegistryGenerator(long seed) {
        this(new SeedRandom(seed));
    }

    public SyntheticProviderRegistryGenerator(SeedRandom random) {
        this.random = random != null ? random : new SeedRandom();
        this.practitionerGenerator = new PractitionerGenerator(this.random);
        this.practitionerRoleGenerator = new PractitionerRoleGenerator(this.random);
        this.organizationGenerator = new OrganizationGenerator(this.random);
        this.locationGenerator = new LocationGenerator(this.random);
        this.healthcareServiceGenerator = new HealthcareServiceGenerator(this.random);
        this.endpointGenerator = new EndpointGenerator(this.random);
        this.groupGenerator = new GroupGenerator(this.random);
    }

    public static class ProviderRegistryGraph {
        private final Organization organization;
        private final Location location;
        private final HealthcareService healthcareService;
        private final Endpoint endpoint;
        private final Practitioner practitioner;
        private final PractitionerRole practitionerRole;
        private final Group group;
        private final List<PractitionerRole> additionalRoles;
        private final List<Location> additionalLocations;

        public ProviderRegistryGraph(
                Organization organization,
                Location location,
                HealthcareService healthcareService,
                Endpoint endpoint,
                Practitioner practitioner,
                PractitionerRole practitionerRole,
                Group group) {
            this(organization, location, healthcareService, endpoint, practitioner, practitionerRole, group, Collections.emptyList(), Collections.emptyList());
        }

        public ProviderRegistryGraph(
                Organization organization,
                Location location,
                HealthcareService healthcareService,
                Endpoint endpoint,
                Practitioner practitioner,
                PractitionerRole practitionerRole,
                Group group,
                List<PractitionerRole> additionalRoles,
                List<Location> additionalLocations) {
            this.organization = organization;
            this.location = location;
            this.healthcareService = healthcareService;
            this.endpoint = endpoint;
            this.practitioner = practitioner;
            this.practitionerRole = practitionerRole;
            this.group = group;
            this.additionalRoles = additionalRoles != null ? List.copyOf(additionalRoles) : Collections.emptyList();
            this.additionalLocations = additionalLocations != null ? List.copyOf(additionalLocations) : Collections.emptyList();
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

        public List<PractitionerRole> getAdditionalRoles() {
            return additionalRoles;
        }

        public List<Location> getAdditionalLocations() {
            return additionalLocations;
        }

        public List<Resource> getAllResources() {
            List<Resource> list = new ArrayList<>();
            if (organization != null) list.add(organization);
            if (endpoint != null) list.add(endpoint);
            if (location != null) list.add(location);
            list.addAll(additionalLocations);
            if (healthcareService != null) list.add(healthcareService);
            if (practitioner != null) list.add(practitioner);
            if (practitionerRole != null) list.add(practitionerRole);
            list.addAll(additionalRoles);
            if (group != null) list.add(group);
            return list;
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

    /**
     * Generates a deterministic Solo Practitioner scenario with linked Organization and Role.
     */
    public ProviderRegistryGraph generateSoloPractitioner(long seed) {
        SyntheticProviderRegistryGenerator gen = new SyntheticProviderRegistryGenerator(seed);
        String s = String.valueOf(Math.abs(seed) % 10000);
        String orgId = "org-solo-" + s;
        String practId = "pract-solo-" + s;
        String roleId = "role-solo-" + s;

        Organization org = gen.organizationGenerator.generateValid(orgId);
        Practitioner pract = gen.practitionerGenerator.generateValid(practId);
        PractitionerRole role = gen.practitionerRoleGenerator.generateValid(roleId, practId, orgId, null, null, null);

        return new ProviderRegistryGraph(org, null, null, null, pract, role, null);
    }

    /**
     * Generates a deterministic Specialist Practitioner with multiple distinct roles across organizations and locations.
     */
    public ProviderRegistryGraph generatePractitionerWithMultipleRoles(long seed) {
        SyntheticProviderRegistryGenerator gen = new SyntheticProviderRegistryGenerator(seed);
        String s = String.valueOf(Math.abs(seed) % 10000);
        String orgId1 = "org-main-" + s;
        String orgId2 = "org-regional-" + s;
        String locId1 = "loc-suite-a-" + s;
        String locId2 = "loc-suite-b-" + s;
        String practId = "pract-spec-" + s;
        String roleId1 = "role-lead-" + s;
        String roleId2 = "role-visiting-" + s;

        Organization org1 = gen.organizationGenerator.generateValid(orgId1);
        Organization org2 = gen.organizationGenerator.generateValid(orgId2);
        Location loc1 = gen.locationGenerator.generateValid(locId1, orgId1, null);
        Location loc2 = gen.locationGenerator.generateValid(locId2, orgId2, null);
        Practitioner pract = gen.practitionerGenerator.generateValid(practId);

        PractitionerRole role1 = gen.practitionerRoleGenerator.generateValid(roleId1, practId, orgId1, locId1, null, null);
        PractitionerRole role2 = gen.practitionerRoleGenerator.generateValid(roleId2, practId, orgId2, locId2, null, null);

        return new ProviderRegistryGraph(org1, loc1, null, null, pract, role1, null, List.of(role2), List.of(loc2));
    }

    /**
     * Generates an Organization with multiple Locations and Endpoints.
     */
    public ProviderRegistryGraph generateOrganizationWithLocations(long seed) {
        SyntheticProviderRegistryGenerator gen = new SyntheticProviderRegistryGenerator(seed);
        String s = String.valueOf(Math.abs(seed) % 10000);
        String orgId = "org-net-" + s;
        String epId = "ep-main-" + s;
        String locId1 = "loc-north-" + s;
        String locId2 = "loc-south-" + s;

        Organization org = gen.organizationGenerator.generateValid(orgId);
        Endpoint ep = gen.endpointGenerator.generateValid(epId, orgId);
        Location loc1 = gen.locationGenerator.generateValid(locId1, orgId, epId);
        Location loc2 = gen.locationGenerator.generateValid(locId2, orgId, epId);

        return new ProviderRegistryGraph(org, loc1, null, ep, null, null, null, Collections.emptyList(), List.of(loc2));
    }

    /**
     * Generates a complete HealthcareService topology.
     */
    public ProviderRegistryGraph generateHealthcareServiceTopology(long seed) {
        SyntheticProviderRegistryGenerator gen = new SyntheticProviderRegistryGenerator(seed);
        String s = String.valueOf(Math.abs(seed) % 10000);
        String orgId = "org-hosp-" + s;
        String epId = "ep-hosp-" + s;
        String locId = "loc-dept-" + s;
        String svcId = "svc-telehealth-" + s;

        Organization org = gen.organizationGenerator.generateValid(orgId);
        Endpoint ep = gen.endpointGenerator.generateValid(epId, orgId);
        Location loc = gen.locationGenerator.generateValid(locId, orgId, epId);
        HealthcareService svc = gen.healthcareServiceGenerator.generateValid(svcId, orgId, locId, epId);

        return new ProviderRegistryGraph(org, loc, svc, ep, null, null, null);
    }

    /**
     * Generates an entire connected Provider Network scenario graph.
     */
    public ProviderRegistryGraph generateProviderNetwork(long seed) {
        SyntheticProviderRegistryGenerator gen = new SyntheticProviderRegistryGenerator(seed);
        String s = String.valueOf(Math.abs(seed) % 10000);
        String orgId = "org-network-" + s;
        String epId = "ep-network-" + s;
        String locId = "loc-network-" + s;
        String svcId = "svc-network-" + s;
        String practId = "pract-network-" + s;
        String roleId = "role-network-" + s;
        String grpId = "grp-network-" + s;

        Organization org = gen.organizationGenerator.generateValid(orgId);
        Endpoint ep = gen.endpointGenerator.generateValid(epId, orgId);
        Location loc = gen.locationGenerator.generateValid(locId, orgId, epId);
        HealthcareService svc = gen.healthcareServiceGenerator.generateValid(svcId, orgId, locId, epId);
        Practitioner pract = gen.practitionerGenerator.generateValid(practId);
        PractitionerRole role = gen.practitionerRoleGenerator.generateValid(roleId, practId, orgId, locId, svcId, epId);
        Group group = gen.groupGenerator.generateValid(grpId, orgId, List.of(practId));

        return new ProviderRegistryGraph(org, loc, svc, ep, pract, role, group);
    }

    /**
     * Generates a graph containing deliberately broken / non-existent references.
     */
    public ProviderRegistryGraph generateInvalidReferenceGraph(long seed) {
        SyntheticProviderRegistryGenerator gen = new SyntheticProviderRegistryGenerator(seed);
        String s = String.valueOf(Math.abs(seed) % 10000);
        String practId = "pract-broken-" + s;
        String roleId = "role-broken-" + s;

        Practitioner pract = gen.practitionerGenerator.generateValid(practId);
        PractitionerRole role = gen.practitionerRoleGenerator.generateBrokenReference(roleId, practId);

        return new ProviderRegistryGraph(null, null, null, null, pract, role, null);
    }

    public PractitionerGenerator getPractitionerGenerator() {
        return practitionerGenerator;
    }

    public PractitionerRoleGenerator getPractitionerRoleGenerator() {
        return practitionerRoleGenerator;
    }

    public OrganizationGenerator getOrganizationGenerator() {
        return organizationGenerator;
    }

    public LocationGenerator getLocationGenerator() {
        return locationGenerator;
    }

    public HealthcareServiceGenerator getHealthcareServiceGenerator() {
        return healthcareServiceGenerator;
    }

    public EndpointGenerator getEndpointGenerator() {
        return endpointGenerator;
    }

    public GroupGenerator getGroupGenerator() {
        return groupGenerator;
    }

    public SeedRandom getRandom() {
        return random;
    }
}
