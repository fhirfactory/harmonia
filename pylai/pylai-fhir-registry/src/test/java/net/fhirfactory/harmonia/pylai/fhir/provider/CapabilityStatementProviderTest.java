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

package net.fhirfactory.harmonia.pylai.fhir.provider;

import org.hl7.fhir.r5.model.CapabilityStatement;
import org.hl7.fhir.r5.model.Enumerations;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class CapabilityStatementProviderTest {

    private final CapabilityStatementProvider provider = new CapabilityStatementProvider();

    @Test
    @DisplayName("CapabilityStatement generates accurate FHIR R5 Provider Registry metadata")
    void testCapabilityStatementGeneration() {
        CapabilityStatement cs = provider.buildCapabilityStatement();

        assertThat(cs).isNotNull();
        assertThat(cs.getStatus()).isEqualTo(Enumerations.PublicationStatus.ACTIVE);
        assertThat(cs.getFhirVersion()).isEqualTo(Enumerations.FHIRVersion._5_0_0);
        assertThat(cs.getFormat()).extracting(f -> f.getValue()).contains("json", "xml");

        CapabilityStatement.CapabilityStatementRestComponent rest = cs.getRestFirstRep();
        assertThat(rest.getMode()).isEqualTo(CapabilityStatement.RestfulCapabilityMode.SERVER);
        assertThat(rest.getSecurity()).isNotNull();

        List<String> resourceTypes = rest.getResource().stream()
                .map(CapabilityStatement.CapabilityStatementRestResourceComponent::getType)
                .collect(Collectors.toList());

        assertThat(resourceTypes).containsExactlyInAnyOrder(
                "Practitioner",
                "PractitionerRole",
                "Organization",
                "Location",
                "HealthcareService",
                "Endpoint",
                "Group",
                "Task"
        );

        // Verify Practitioner search parameters
        CapabilityStatement.CapabilityStatementRestResourceComponent prComp = rest.getResource().stream()
                .filter(r -> "Practitioner".equals(r.getType()))
                .findFirst().orElseThrow();

        List<String> prParams = prComp.getSearchParam().stream()
                .map(CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent::getName)
                .collect(Collectors.toList());
        assertThat(prParams).contains("_id", "name", "identifier", "active");

        // Verify Endpoint search parameters
        CapabilityStatement.CapabilityStatementRestResourceComponent epComp = rest.getResource().stream()
                .filter(r -> "Endpoint".equals(r.getType()))
                .findFirst().orElseThrow();

        List<String> epParams = epComp.getSearchParam().stream()
                .map(CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent::getName)
                .collect(Collectors.toList());
        assertThat(epParams).contains("_id", "name", "identifier", "organization", "status", "connection-type");
    }
}
