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

package net.fhirfactory.harmonia.paradeigma.test.generator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import net.fhirfactory.harmonia.paradeigma.common.generator.*;
import org.hl7.fhir.r5.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Validates that all Provider Registry synthetic generators and graph topologies are 100% deterministic
 * when initialized with identical seeds, and produce varied outputs when initialized with different seeds.
 */
public class ProviderRegistryGeneratorDeterminismTest {

    private final FhirContext fhirContext = FhirContext.forR5();
    private IParser jsonParser;

    @BeforeEach
    void setUp() {
        jsonParser = fhirContext.newJsonParser().setPrettyPrint(true);
    }

    private String serialize(Resource r) {
        // Strip non-deterministic lastUpdated timestamp for strict determinism comparison
        if (r != null && r.getMeta() != null) {
            r.getMeta().setLastUpdated(new Date(0));
        }
        return jsonParser.encodeResourceToString(r);
    }

    @Test
    @DisplayName("PractitionerGenerator produces identical output given the same seed")
    void practitionerDeterminism() {
        Practitioner p1 = new PractitionerGenerator(12345L).generateValid("pract-101");
        Practitioner p2 = new PractitionerGenerator(12345L).generateValid("pract-101");
        Practitioner p3 = new PractitionerGenerator(99999L).generateValid("pract-101");

        assertThat(serialize(p1)).isEqualTo(serialize(p2));
        assertThat(serialize(p1)).isNotEqualTo(serialize(p3));
        assertThat(p1.getIdentifier()).isNotEmpty();
        assertThat(p1.getIdentifierFirstRep().getSystem()).isEqualTo(PractitionerGenerator.SYSTEM_HPI_I);
    }

    @Test
    @DisplayName("OrganizationGenerator produces identical output given the same seed")
    void organizationDeterminism() {
        Organization o1 = new OrganizationGenerator(12345L).generateValid("org-101");
        Organization o2 = new OrganizationGenerator(12345L).generateValid("org-101");
        Organization o3 = new OrganizationGenerator(99999L).generateValid("org-101");

        assertThat(serialize(o1)).isEqualTo(serialize(o2));
        assertThat(serialize(o1)).isNotEqualTo(serialize(o3));
        assertThat(o1.getIdentifierFirstRep().getSystem()).isEqualTo(OrganizationGenerator.SYSTEM_HPI_O);
    }

    @Test
    @DisplayName("LocationGenerator produces identical output given the same seed")
    void locationDeterminism() {
        Location l1 = new LocationGenerator(12345L).generateValid("loc-101", "org-101", "ep-101");
        Location l2 = new LocationGenerator(12345L).generateValid("loc-101", "org-101", "ep-101");

        assertThat(serialize(l1)).isEqualTo(serialize(l2));
        assertThat(l1.getManagingOrganization().getReference()).isEqualTo("Organization/org-101");
    }

    @Test
    @DisplayName("HealthcareServiceGenerator produces identical output given the same seed")
    void healthcareServiceDeterminism() {
        HealthcareService s1 = new HealthcareServiceGenerator(12345L).generateValid("svc-101", "org-101", "loc-101", "ep-101");
        HealthcareService s2 = new HealthcareServiceGenerator(12345L).generateValid("svc-101", "org-101", "loc-101", "ep-101");

        assertThat(serialize(s1)).isEqualTo(serialize(s2));
        assertThat(s1.getProvidedBy().getReference()).isEqualTo("Organization/org-101");
    }

    @Test
    @DisplayName("EndpointGenerator produces identical output given the same seed")
    void endpointDeterminism() {
        Endpoint ep1 = new EndpointGenerator(12345L).generateValid("ep-101", "org-101");
        Endpoint ep2 = new EndpointGenerator(12345L).generateValid("ep-101", "org-101");

        assertThat(serialize(ep1)).isEqualTo(serialize(ep2));
        assertThat(ep1.getManagingOrganization().getReference()).isEqualTo("Organization/org-101");
    }

    @Test
    @DisplayName("PractitionerRoleGenerator produces identical output given the same seed")
    void practitionerRoleDeterminism() {
        PractitionerRole r1 = new PractitionerRoleGenerator(12345L).generateValid("role-101", "pract-101", "org-101", "loc-101", "svc-101", "ep-101");
        PractitionerRole r2 = new PractitionerRoleGenerator(12345L).generateValid("role-101", "pract-101", "org-101", "loc-101", "svc-101", "ep-101");

        assertThat(serialize(r1)).isEqualTo(serialize(r2));
        assertThat(r1.getPractitioner().getReference()).isEqualTo("Practitioner/pract-101");
        assertThat(r1.getOrganization().getReference()).isEqualTo("Organization/org-101");
    }

    @Test
    @DisplayName("GroupGenerator produces identical output given the same seed")
    void groupDeterminism() {
        Group g1 = new GroupGenerator(12345L).generateValid("grp-101", "org-101", List.of("pract-101", "pract-102"));
        Group g2 = new GroupGenerator(12345L).generateValid("grp-101", "org-101", List.of("pract-101", "pract-102"));

        assertThat(serialize(g1)).isEqualTo(serialize(g2));
        assertThat(g1.getMember()).hasSize(2);
    }

    @Test
    @DisplayName("Graph Builders: Solo practitioner and network graphs are deterministic")
    void graphBuildersDeterminism() {
        SyntheticProviderRegistryGenerator gen1 = new SyntheticProviderRegistryGenerator(42L);
        SyntheticProviderRegistryGenerator gen2 = new SyntheticProviderRegistryGenerator(42L);

        SyntheticProviderRegistryGenerator.ProviderRegistryGraph solo1 = gen1.generateSoloPractitioner(42L);
        SyntheticProviderRegistryGraphSolo(solo1);

        SyntheticProviderRegistryGenerator.ProviderRegistryGraph net1 = gen1.generateProviderNetwork(42L);
        SyntheticProviderRegistryGenerator.ProviderRegistryGraph net2 = gen2.generateProviderNetwork(42L);

        assertThat(serialize(net1.getOrganization())).isEqualTo(serialize(net2.getOrganization()));
        assertThat(serialize(net1.getPractitioner())).isEqualTo(serialize(net2.getPractitioner()));
        assertThat(serialize(net1.getPractitionerRole())).isEqualTo(serialize(net2.getPractitionerRole()));
        assertThat(serialize(net1.getHealthcareService())).isEqualTo(serialize(net2.getHealthcareService()));
        assertThat(serialize(net1.getLocation())).isEqualTo(serialize(net2.getLocation()));
        assertThat(serialize(net1.getEndpoint())).isEqualTo(serialize(net2.getEndpoint()));
        assertThat(serialize(net1.getGroup())).isEqualTo(serialize(net2.getGroup()));
    }

    private void SyntheticProviderRegistryGraphSolo(SyntheticProviderRegistryGenerator.ProviderRegistryGraph graph) {
        assertThat(graph.getOrganization()).isNotNull();
        assertThat(graph.getPractitioner()).isNotNull();
        assertThat(graph.getPractitionerRole()).isNotNull();
    }

    @Test
    @DisplayName("Negative / Incomplete / Invalid generation helpers work as expected")
    void variationGenerators() {
        PractitionerGenerator practGen = new PractitionerGenerator(123L);
        Practitioner incomplete = practGen.generateIncomplete("pract-inc");
        assertThat(incomplete.getName()).isEmpty();

        Practitioner invalid = practGen.generateInvalid("pract-inv");
        assertThat(invalid.getActive()).isFalse();

        Practitioner duplicate = practGen.generateDuplicateIdentifier("pract-dup", "8003619999999999");
        assertThat(duplicate.getIdentifierFirstRep().getValue()).isEqualTo("8003619999999999");

        SyntheticProviderRegistryGenerator regGen = new SyntheticProviderRegistryGenerator(123L);
        SyntheticProviderRegistryGenerator.ProviderRegistryGraph brokenGraph = regGen.generateInvalidReferenceGraph(123L);
        assertThat(brokenGraph.getPractitionerRole().getOrganization().getReference()).contains("non-existent-org");
    }
}
