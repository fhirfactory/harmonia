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
import net.fhirfactory.harmonia.erga.registry.*;
import net.fhirfactory.harmonia.hapifhir.model.FhirResourceEntity;
import net.fhirfactory.harmonia.hapifhir.repository.FhirResourceRepository;
import net.fhirfactory.harmonia.hapifhir.service.FhirStorageService;
import net.fhirfactory.harmonia.hapifhir.service.ProviderRegistryReferenceValidator;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaFhirConverter;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.model.registry.ProviderRegistryConstants;
import net.fhirfactory.harmonia.paradeigma.common.generator.SyntheticProviderRegistryGenerator;
import net.fhirfactory.harmonia.paradeigma.common.generator.SyntheticProviderRegistryGenerator.ProviderRegistryGraph;
import net.fhirfactory.harmonia.praxis.cache.PragmaCacheService;
import net.fhirfactory.harmonia.pylai.fhir.controller.FhirRestGatewayController;
import net.fhirfactory.harmonia.pylai.fhir.provider.CapabilityStatementProvider;
import net.fhirfactory.harmonia.pylai.fhir.security.FhirSecurityInterceptor;
import net.fhirfactory.harmonia.pylai.fhir.service.ChangeRequestSubmissionService;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ProviderRegistryEndToEndWriteTest {

    private final FhirContext fhirContext = FhirContext.forR5();
    private FhirResourceRepository mockRepository;
    private FhirStorageService storageService;
    private ProviderRegistryReferenceValidator referenceValidator;
    private PragmaCacheService pragmaCacheService;
    private ChangeRequestSubmissionService submissionService;
    private FhirRestGatewayController gatewayController;

    private OrganizationChangeErgon orgErgon;
    private EndpointChangeErgon epErgon;
    private LocationChangeErgon locErgon;
    private HealthcareServiceChangeErgon svcErgon;
    private PractitionerChangeErgon practErgon;
    private PractitionerRoleChangeErgon roleErgon;
    private GroupChangeErgon groupErgon;

    private CamelContext camelContext;
    private Map<String, FhirResourceEntity> store;
    private Map<String, Pragma> cache;

    @BeforeEach
    void setUp() {
        store = new ConcurrentHashMap<>();
        cache = new ConcurrentHashMap<>();
        mockRepository = Mockito.mock(FhirResourceRepository.class);

        when(mockRepository.findByResourceTypeAndFhirId(any(), any())).thenAnswer(invocation -> {
            String type = invocation.getArgument(0);
            String id = invocation.getArgument(1);
            if (id != null && id.contains("/")) {
                id = id.substring(id.lastIndexOf('/') + 1);
            }
            FhirResourceEntity entity = store.get(type + "/" + id);
            if (entity == null) {
                for (FhirResourceEntity e : store.values()) {
                    if (e.getResourceType().equalsIgnoreCase(type) && e.getFhirId().equalsIgnoreCase(id)) {
                        return Optional.of(e);
                    }
                }
            }
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
        referenceValidator = new ProviderRegistryReferenceValidator(mockRepository);

        pragmaCacheService = Mockito.mock(PragmaCacheService.class);
        when(pragmaCacheService.savePragma(any(Pragma.class))).thenAnswer(invocation -> {
            Pragma p = invocation.getArgument(0);
            cache.put(p.getPragmaId(), p);
            return p;
        });
        when(pragmaCacheService.getPragma(any(String.class))).thenAnswer(invocation -> {
            String id = invocation.getArgument(0);
            Pragma p = cache.get(id);
            return p != null ? Optional.of(p) : Optional.empty();
        });

        submissionService = new ChangeRequestSubmissionService(pragmaCacheService, null);
        gatewayController = new FhirRestGatewayController(
                storageService,
                submissionService,
                new CapabilityStatementProvider(),
                new FhirSecurityInterceptor(),
                pragmaCacheService
        );

        camelContext = new DefaultCamelContext();

        orgErgon = new OrganizationChangeErgon();
        orgErgon.setStorageService(storageService);
        orgErgon.setReferenceValidator(referenceValidator);

        epErgon = new EndpointChangeErgon();
        epErgon.setStorageService(storageService);
        epErgon.setReferenceValidator(referenceValidator);

        locErgon = new LocationChangeErgon();
        locErgon.setStorageService(storageService);
        locErgon.setReferenceValidator(referenceValidator);

        svcErgon = new HealthcareServiceChangeErgon();
        svcErgon.setStorageService(storageService);
        svcErgon.setReferenceValidator(referenceValidator);

        practErgon = new PractitionerChangeErgon();
        practErgon.setStorageService(storageService);
        practErgon.setReferenceValidator(referenceValidator);

        roleErgon = new PractitionerRoleChangeErgon();
        roleErgon.setStorageService(storageService);
        roleErgon.setReferenceValidator(referenceValidator);

        groupErgon = new GroupChangeErgon();
        groupErgon.setStorageService(storageService);
        groupErgon.setReferenceValidator(referenceValidator);
    }

    @Test
    @DisplayName("End-to-End Governance Write Lifecycle: Full Provider Hierarchy")
    void testEndToEndProviderHierarchyCreation() throws Exception {
        IParser parser = fhirContext.newJsonParser();
        ProviderRegistryGraph graph = SyntheticProviderRegistryGenerator.generateConnectedProviderHierarchy("99");

        // Step 1: POST /Organization -> 202 Accepted -> Ponos commits Organization
        String orgJson = parser.encodeResourceToString(graph.getOrganization());
        ResponseEntity<String> orgResp = gatewayController.createResource("Organization", orgJson, "corr-1", "pas", "admin", null);
        assertThat(orgResp.getStatusCode().value()).isEqualTo(202);
        String orgPragmaId = extractPragmaIdFromLocation(orgResp.getHeaders().getLocation().toString());

        Pragma orgPragma = cache.get(orgPragmaId);
        Exchange ex1 = camelContext.getEndpoint("direct:test").createExchange();
        orgErgon.processErgon(orgPragma, ex1);
        assertThat(orgPragma.getStatus()).isEqualTo(PragmaStatus.COMPLETED);
        assertThat(store).containsKey("Organization/" + graph.getOrganization().getIdPart());

        // Step 2: POST /Endpoint -> 202 Accepted -> Ponos commits Endpoint
        String epJson = parser.encodeResourceToString(graph.getEndpoint());
        ResponseEntity<String> epResp = gatewayController.createResource("Endpoint", epJson, "corr-2", "pas", "admin", null);
        assertThat(epResp.getStatusCode().value()).isEqualTo(202);
        String epPragmaId = extractPragmaIdFromLocation(epResp.getHeaders().getLocation().toString());

        Pragma epPragma = cache.get(epPragmaId);
        Exchange ex2 = camelContext.getEndpoint("direct:test").createExchange();
        epErgon.processErgon(epPragma, ex2);
        assertThat(epPragma.getStatus()).isEqualTo(PragmaStatus.COMPLETED);

        // Step 3: POST /Location -> 202 Accepted -> Ponos validates Organization & Endpoint and commits Location
        String locJson = parser.encodeResourceToString(graph.getLocation());
        ResponseEntity<String> locResp = gatewayController.createResource("Location", locJson, "corr-3", "pas", "admin", null);
        assertThat(locResp.getStatusCode().value()).isEqualTo(202);
        String locPragmaId = extractPragmaIdFromLocation(locResp.getHeaders().getLocation().toString());

        Pragma locPragma = cache.get(locPragmaId);
        Exchange ex3 = camelContext.getEndpoint("direct:test").createExchange();
        locErgon.processErgon(locPragma, ex3);
        assertThat(locPragma.getStatus()).isEqualTo(PragmaStatus.COMPLETED);

        // Step 4: POST /HealthcareService -> 202 Accepted -> Ponos validates references and commits HealthcareService
        String svcJson = parser.encodeResourceToString(graph.getHealthcareService());
        ResponseEntity<String> svcResp = gatewayController.createResource("HealthcareService", svcJson, "corr-4", "pas", "admin", null);
        assertThat(svcResp.getStatusCode().value()).isEqualTo(202);
        String svcPragmaId = extractPragmaIdFromLocation(svcResp.getHeaders().getLocation().toString());

        Pragma svcPragma = cache.get(svcPragmaId);
        Exchange ex4 = camelContext.getEndpoint("direct:test").createExchange();
        svcErgon.processErgon(svcPragma, ex4);
        assertThat(svcPragma.getStatus()).isEqualTo(PragmaStatus.COMPLETED);

        // Step 5: POST /Practitioner -> 202 Accepted -> Ponos commits Practitioner
        String practJson = parser.encodeResourceToString(graph.getPractitioner());
        ResponseEntity<String> practResp = gatewayController.createResource("Practitioner", practJson, "corr-5", "pas", "admin", null);
        assertThat(practResp.getStatusCode().value()).isEqualTo(202);
        String practPragmaId = extractPragmaIdFromLocation(practResp.getHeaders().getLocation().toString());

        Pragma practPragma = cache.get(practPragmaId);
        Exchange ex5 = camelContext.getEndpoint("direct:test").createExchange();
        practErgon.processErgon(practPragma, ex5);
        assertThat(practPragma.getStatus()).isEqualTo(PragmaStatus.COMPLETED);

        // Step 6: POST /PractitionerRole -> 202 Accepted -> Ponos validates all 5 references and commits PractitionerRole
        String roleJson = parser.encodeResourceToString(graph.getPractitionerRole());
        ResponseEntity<String> roleResp = gatewayController.createResource("PractitionerRole", roleJson, "corr-6", "pas", "admin", null);
        assertThat(roleResp.getStatusCode().value()).isEqualTo(202);
        String rolePragmaId = extractPragmaIdFromLocation(roleResp.getHeaders().getLocation().toString());

        Pragma rolePragma = cache.get(rolePragmaId);
        Exchange ex6 = camelContext.getEndpoint("direct:test").createExchange();
        roleErgon.processErgon(rolePragma, ex6);
        assertThat(rolePragma.getStatus()).isEqualTo(PragmaStatus.COMPLETED);

        // Step 7: POST /Group -> 202 Accepted -> Ponos validates member references and commits Group
        String groupJson = parser.encodeResourceToString(graph.getGroup());
        ResponseEntity<String> groupResp = gatewayController.createResource("Group", groupJson, "corr-7", "pas", "admin", null);
        assertThat(groupResp.getStatusCode().value()).isEqualTo(202);
        String groupPragmaId = extractPragmaIdFromLocation(groupResp.getHeaders().getLocation().toString());

        Pragma groupPragma = cache.get(groupPragmaId);
        Exchange ex7 = camelContext.getEndpoint("direct:test").createExchange();
        groupErgon.processErgon(groupPragma, ex7);
        assertThat(groupPragma.getStatus()).isEqualTo(PragmaStatus.COMPLETED);

        // Step 8: Verify synchronous search retrieves newly committed PractitionerRole and related graph
        ResponseEntity<String> searchResp = gatewayController.searchResources(
                "PractitionerRole",
                Map.of("practitioner", graph.getPractitioner().getIdPart()),
                null
        );

        assertThat(searchResp.getStatusCode().value()).isEqualTo(200);
        Bundle searchBundle = parser.parseResource(Bundle.class, searchResp.getBody());
        assertThat(searchBundle.getTotal()).isEqualTo(1);
        PractitionerRole foundRole = (PractitionerRole) searchBundle.getEntryFirstRep().getResource();
        assertThat(foundRole.getIdElement().getIdPart()).isEqualTo(graph.getPractitionerRole().getIdPart());
        assertThat(foundRole.getOrganization().getReference()).contains(graph.getOrganization().getIdPart());
    }

    private String extractPragmaIdFromLocation(String location) {
        return location.substring(location.lastIndexOf('/') + 1);
    }
}
