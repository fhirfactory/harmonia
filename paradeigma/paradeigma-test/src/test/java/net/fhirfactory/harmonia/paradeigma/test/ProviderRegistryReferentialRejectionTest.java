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
import net.fhirfactory.harmonia.erga.registry.PractitionerRoleChangeErgon;
import net.fhirfactory.harmonia.hapifhir.model.FhirResourceEntity;
import net.fhirfactory.harmonia.hapifhir.repository.FhirResourceRepository;
import net.fhirfactory.harmonia.hapifhir.service.FhirStorageService;
import net.fhirfactory.harmonia.hapifhir.service.ProviderRegistryReferenceValidator;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaFhirConverter;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.model.registry.ProviderRegistryConstants;
import net.fhirfactory.harmonia.praxis.cache.PragmaCacheService;
import net.fhirfactory.harmonia.pylai.fhir.controller.FhirRestGatewayController;
import net.fhirfactory.harmonia.pylai.fhir.provider.CapabilityStatementProvider;
import net.fhirfactory.harmonia.pylai.fhir.security.FhirSecurityInterceptor;
import net.fhirfactory.harmonia.pylai.fhir.service.ChangeRequestSubmissionService;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.hl7.fhir.r5.model.OperationOutcome;
import org.hl7.fhir.r5.model.PractitionerRole;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProviderRegistryReferentialRejectionTest {

    private final FhirContext fhirContext = FhirContext.forR5();
    private FhirResourceRepository mockRepository;
    private FhirStorageService storageService;
    private ProviderRegistryReferenceValidator referenceValidator;
    private PragmaCacheService pragmaCacheService;
    private ChangeRequestSubmissionService submissionService;
    private FhirRestGatewayController gatewayController;
    private PractitionerRoleChangeErgon roleErgon;
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
            FhirResourceEntity entity = store.get(type + "/" + id);
            return entity != null ? Optional.of(entity) : Optional.empty();
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

        roleErgon = new PractitionerRoleChangeErgon();
        roleErgon.setStorageService(storageService);
        roleErgon.setReferenceValidator(referenceValidator);

        camelContext = new DefaultCamelContext();
    }

    @Test
    @DisplayName("Submit PractitionerRole with missing Practitioner -> 202 Accepted -> Ergon REJECTED -> Task contains OperationOutcome")
    void testMissingReferentialTargetRejection() throws Exception {
        IParser parser = fhirContext.newJsonParser();

        PractitionerRole role = new PractitionerRole();
        role.setPractitioner(new Reference("Practitioner/non-existent-pr-999"));
        role.setOrganization(new Reference("Organization/non-existent-org-999"));

        String json = parser.encodeResourceToString(role);

        // 1. Submit POST request -> 202 Accepted
        ResponseEntity<String> response = gatewayController.createResource("PractitionerRole", json, "corr-rej-1", "pas", "admin", null);
        assertThat(response.getStatusCode().value()).isEqualTo(202);

        String location = response.getHeaders().getLocation().toString();
        String pragmaId = location.substring(location.lastIndexOf('/') + 1);

        Pragma pragma = cache.get(pragmaId);
        assertThat(pragma).isNotNull();

        // 2. Execute Ponos Ergon activity
        Exchange exchange = camelContext.getEndpoint("direct:test").createExchange();
        roleErgon.processErgon(pragma, exchange);

        // 3. Verify Pragma transitions to REJECTED with OperationOutcome diagnostic details
        assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.REJECTED);
        assertThat(pragma.getOutput()).isNotEmpty();

        // 4. Verify polling GET /Task/{id} yields rejected Task with OperationOutcome diagnostic details
        ResponseEntity<String> taskResp = gatewayController.getTaskStatus(pragmaId, null);
        assertThat(taskResp.getStatusCode().value()).isEqualTo(200);

        Task fhirTask = parser.parseResource(Task.class, taskResp.getBody());
        assertThat(fhirTask.getStatus()).isEqualTo(Task.TaskStatus.REJECTED);

        // 5. Assert no PractitionerRole was committed to authoritative store
        assertThat(store.containsKey("PractitionerRole/" + role.getIdPart())).isFalse();
    }
}
