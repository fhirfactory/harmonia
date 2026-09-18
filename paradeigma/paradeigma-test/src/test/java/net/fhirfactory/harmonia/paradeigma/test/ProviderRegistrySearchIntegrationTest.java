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

package net.fhirfactory.harmonia.paradeigma.test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import net.fhirfactory.harmonia.hapifhir.model.FhirResourceEntity;
import net.fhirfactory.harmonia.hapifhir.repository.FhirResourceRepository;
import net.fhirfactory.harmonia.hapifhir.service.FhirStorageService;
import net.fhirfactory.harmonia.paradeigma.common.generator.SyntheticProviderRegistryGenerator;
import net.fhirfactory.harmonia.paradeigma.common.generator.SyntheticProviderRegistryGenerator.ProviderRegistryGraph;
import net.fhirfactory.harmonia.pylai.fhir.controller.FhirRestGatewayController;
import net.fhirfactory.harmonia.pylai.fhir.provider.CapabilityStatementProvider;
import net.fhirfactory.harmonia.pylai.fhir.security.FhirSecurityInterceptor;
import net.fhirfactory.harmonia.pylai.fhir.service.ChangeRequestSubmissionService;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.PractitionerRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProviderRegistrySearchIntegrationTest {

    private final FhirContext fhirContext = FhirContext.forR5();
    private FhirStorageService storageService;
    private FhirRestGatewayController gatewayController;
    private ProviderRegistryGraph graph;
    private Map<String, FhirResourceEntity> store;

    @BeforeEach
    void setUp() {
        store = new ConcurrentHashMap<>();
        FhirResourceRepository mockRepository = Mockito.mock(FhirResourceRepository.class);

        when(mockRepository.findByResourceTypeAndFhirId(any(), any())).thenAnswer(invocation -> {
            String type = invocation.getArgument(0);
            String id = invocation.getArgument(1);
            FhirResourceEntity entity = store.get(type + "/" + id);
            return entity != null ? Optional.of(entity) : Optional.empty();
        });

        when(mockRepository.findByResourceTypeAndDeletedFalse(any())).thenAnswer(invocation -> {
            String type = invocation.getArgument(0);
            List<FhirResourceEntity> list = new ArrayList<>();
            for (FhirResourceEntity e : store.values()) {
                if (e.getResourceType().equalsIgnoreCase(type) && !e.isDeleted()) {
                    list.add(e);
                }
            }
            return list;
        });

        when(mockRepository.save(any(FhirResourceEntity.class))).thenAnswer(invocation -> {
            FhirResourceEntity e = invocation.getArgument(0);
            store.put(e.getResourceType() + "/" + e.getFhirId(), e);
            return e;
        });

        storageService = new FhirStorageService(mockRepository);
        gatewayController = new FhirRestGatewayController(
                storageService,
                Mockito.mock(ChangeRequestSubmissionService.class),
                new CapabilityStatementProvider(),
                new FhirSecurityInterceptor(),
                null
        );

        // Seed synthetic graph
        graph = SyntheticProviderRegistryGenerator.generateConnectedProviderHierarchy("50");
        storageService.createResource(graph.getOrganization());
        storageService.createResource(graph.getEndpoint());
        storageService.createResource(graph.getLocation());
        storageService.createResource(graph.getHealthcareService());
        storageService.createResource(graph.getPractitioner());
        storageService.createResource(graph.getPractitionerRole());
        storageService.createResource(graph.getGroup());
    }

    @Test
    @DisplayName("Search across all seven Provider Registry resources via Pylai REST Gateway")
    void testSearchAcrossAllResources() {
        IParser parser = fhirContext.newJsonParser();

        // 1. Practitioner by name and identifier
        ResponseEntity<String> prResp = gatewayController.searchResources(
                "Practitioner",
                Map.of("name", "Bowman", "identifier", "8003610000000050"),
                null
        );
        assertThat(prResp.getStatusCode().value()).isEqualTo(200);
        Bundle prBundle = parser.parseResource(Bundle.class, prResp.getBody());
        assertThat(prBundle.getTotal()).isEqualTo(1);

        // 2. Organization by name and active
        ResponseEntity<String> orgResp = gatewayController.searchResources(
                "Organization",
                Map.of("name", "Vincent", "active", "true"),
                null
        );
        assertThat(orgResp.getStatusCode().value()).isEqualTo(200);
        Bundle orgBundle = parser.parseResource(Bundle.class, orgResp.getBody());
        assertThat(orgBundle.getTotal()).isEqualTo(1);

        // 3. Location by status and organization
        ResponseEntity<String> locResp = gatewayController.searchResources(
                "Location",
                Map.of("status", "active", "organization", graph.getOrganization().getIdPart()),
                null
        );
        assertThat(locResp.getStatusCode().value()).isEqualTo(200);
        Bundle locBundle = parser.parseResource(Bundle.class, locResp.getBody());
        assertThat(locBundle.getTotal()).isEqualTo(1);

        // 4. HealthcareService by location and active
        ResponseEntity<String> svcResp = gatewayController.searchResources(
                "HealthcareService",
                Map.of("location", graph.getLocation().getIdPart(), "active", "true"),
                null
        );
        assertThat(svcResp.getStatusCode().value()).isEqualTo(200);
        Bundle svcBundle = parser.parseResource(Bundle.class, svcResp.getBody());
        assertThat(svcBundle.getTotal()).isEqualTo(1);

        // 5. Endpoint by connection-type and status
        ResponseEntity<String> epResp = gatewayController.searchResources(
                "Endpoint",
                Map.of("connection-type", "hl7-fhir-rest", "status", "active"),
                null
        );
        assertThat(epResp.getStatusCode().value()).isEqualTo(200);
        Bundle epBundle = parser.parseResource(Bundle.class, epResp.getBody());
        assertThat(epBundle.getTotal()).isEqualTo(1);

        // 6. PractitionerRole by practitioner, organization, location, and service
        ResponseEntity<String> roleResp = gatewayController.searchResources(
                "PractitionerRole",
                Map.of(
                        "practitioner", graph.getPractitioner().getIdPart(),
                        "organization", graph.getOrganization().getIdPart(),
                        "location", graph.getLocation().getIdPart(),
                        "service", graph.getHealthcareService().getIdPart()
                ),
                null
        );
        assertThat(roleResp.getStatusCode().value()).isEqualTo(200);
        Bundle roleBundle = parser.parseResource(Bundle.class, roleResp.getBody());
        assertThat(roleBundle.getTotal()).isEqualTo(1);

        // 7. Group by name and type
        ResponseEntity<String> grpResp = gatewayController.searchResources(
                "Group",
                Map.of("name", "Cardiovascular", "type", "practitioner"),
                null
        );
        assertThat(grpResp.getStatusCode().value()).isEqualTo(200);
        Bundle grpBundle = parser.parseResource(Bundle.class, grpResp.getBody());
        assertThat(grpBundle.getTotal()).isEqualTo(1);
    }
}
