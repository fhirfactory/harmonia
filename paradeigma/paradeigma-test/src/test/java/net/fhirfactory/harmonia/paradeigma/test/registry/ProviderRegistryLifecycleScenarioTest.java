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

package net.fhirfactory.harmonia.paradeigma.test.registry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import net.fhirfactory.harmonia.erga.registry.PractitionerChangeErgon;
import net.fhirfactory.harmonia.erga.registry.PractitionerRoleChangeErgon;
import net.fhirfactory.harmonia.hapifhir.model.FhirResourceEntity;
import net.fhirfactory.harmonia.hapifhir.repository.FhirResourceRepository;
import net.fhirfactory.harmonia.hapifhir.service.FhirStorageService;
import net.fhirfactory.harmonia.hapifhir.service.ProviderRegistryReferenceValidator;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.paradeigma.common.generator.PractitionerGenerator;
import net.fhirfactory.harmonia.paradeigma.common.generator.SyntheticProviderRegistryGenerator;
import net.fhirfactory.harmonia.paradeigma.common.security.ParadeigmaSecurityActors;
import net.fhirfactory.harmonia.paradeigma.common.security.SecurityScenarioContext;
import net.fhirfactory.harmonia.praxis.cache.PragmaCacheService;
import net.fhirfactory.harmonia.pylai.fhir.controller.FhirRestGatewayController;
import net.fhirfactory.harmonia.pylai.fhir.provider.CapabilityStatementProvider;
import net.fhirfactory.harmonia.pylai.fhir.security.FhirSecurityInterceptor;
import net.fhirfactory.harmonia.pylai.fhir.service.ChangeRequestSubmissionService;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Practitioner;
import org.hl7.fhir.r5.model.PractitionerRole;
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

/**
 * Validates the full governed Provider Registry change lifecycle across creation,
 * updates with ETag, stale ETag conflicts, duplicate identifier detection, and referential validation.
 */
public class ProviderRegistryLifecycleScenarioTest {

    private final FhirContext fhirContext = FhirContext.forR5();
    private IParser parser;

    private FhirResourceRepository mockRepository;
    private FhirStorageService storageService;
    private ProviderRegistryReferenceValidator referenceValidator;
    private PragmaCacheService pragmaCacheService;
    private ChangeRequestSubmissionService submissionService;
    private FhirRestGatewayController gatewayController;

    private PractitionerChangeErgon practErgon;
    private PractitionerRoleChangeErgon roleErgon;
    private CamelContext camelContext;

    private Map<String, FhirResourceEntity> store;
    private Map<String, Pragma> cache;

    @BeforeEach
    void setUp() {
        parser = fhirContext.newJsonParser();
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
            return Optional.ofNullable(cache.get(id));
        });

        submissionService = new ChangeRequestSubmissionService(pragmaCacheService, null);
        gatewayController = new FhirRestGatewayController(
                storageService,
                submissionService,
                new CapabilityStatementProvider(),
                new FhirSecurityInterceptor(),
                pragmaCacheService
        );

        practErgon = new PractitionerChangeErgon();
        practErgon.setStorageService(storageService);
        practErgon.setReferenceValidator(referenceValidator);

        roleErgon = new PractitionerRoleChangeErgon();
        roleErgon.setStorageService(storageService);
        roleErgon.setReferenceValidator(referenceValidator);

        camelContext = new DefaultCamelContext();
    }

    @Test
    @DisplayName("Governed Write: POST Practitioner -> 202 Accepted -> Ponos Ergon -> Mnemosyne -> Synchronous GET")
    void testGovernedCreateLifecycle() throws Exception {
        Practitioner practitioner = new PractitionerGenerator(12345L).generateValid("pract-dr-elena-101");
        String json = parser.encodeResourceToString(practitioner);

        ResponseEntity<String> postResp = gatewayController.createResource(
                "Practitioner",
                json,
                "corr-create-101",
                "pas",
                "steward",
                null
        );

        assertThat(postResp.getStatusCode().value()).isEqualTo(202);
        String pragmaId = extractPragmaId(postResp.getHeaders().getLocation().toString());

        Pragma pragma = cache.get(pragmaId);
        assertThat(pragma).isNotNull();
        assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.ACCEPTED);

        Exchange exchange = camelContext.getEndpoint("direct:test").createExchange();
        practErgon.processErgon(pragma, exchange);

        assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.COMPLETED);
        assertThat(store).isNotEmpty();
        Optional<FhirResourceEntity> savedPract = store.values().stream()
                .filter(e -> "Practitioner".equalsIgnoreCase(e.getResourceType()))
                .findFirst();
        assertThat(savedPract).isPresent();
        String savedId = savedPract.get().getFhirId();

        ResponseEntity<String> getResp = gatewayController.readResource("Practitioner", savedId, null);
        assertThat(getResp.getStatusCode().value()).isEqualTo(200);
        Practitioner fetched = parser.parseResource(Practitioner.class, getResp.getBody());
        assertThat(fetched.getNameFirstRep().getFamily()).isEqualTo(practitioner.getNameFirstRep().getFamily());
    }

    @Test
    @DisplayName("Governed Update: PUT Practitioner with If-Match -> Version incremented to 2")
    void testGovernedUpdateLifecycle() throws Exception {
        // Initial create
        Practitioner initial = new PractitionerGenerator(12345L).generateValid("pract-update-102");
        storageService.createResource(initial);

        // Prepare updated resource
        Practitioner updated = new PractitionerGenerator(12345L).generateUpdate(initial, 2);
        String updatedJson = parser.encodeResourceToString(updated);

        ResponseEntity<String> putResp = gatewayController.updateResource(
                "Practitioner",
                "pract-update-102",
                updatedJson,
                "W/\"1\"",
                "corr-upd-102",
                "pas",
                "steward",
                null
        );

        assertThat(putResp.getStatusCode().value()).isEqualTo(202);
        String pragmaId = extractPragmaId(putResp.getHeaders().getLocation().toString());

        Pragma pragma = cache.get(pragmaId);
        Exchange exchange = camelContext.getEndpoint("direct:test").createExchange();
        practErgon.processErgon(pragma, exchange);

        assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.COMPLETED);

        // Verify version incremented
        ResponseEntity<String> getResp = gatewayController.readResource("Practitioner", "pract-update-102", null);
        assertThat(getResp.getStatusCode().value()).isEqualTo(200);
        assertThat(getResp.getHeaders().getETag()).isEqualTo("W/\"2\"");
    }

    @Test
    @DisplayName("Optimistic Locking: PUT Practitioner with stale ETag -> FAILED Precondition")
    void testStaleETagConflict() throws Exception {
        Practitioner initial = new PractitionerGenerator(12345L).generateValid("pract-stale-103");
        storageService.createResource(initial);

        Practitioner updated = new PractitionerGenerator(12345L).generateUpdate(initial, 2);
        String updatedJson = parser.encodeResourceToString(updated);

        // Submit with stale / mismatched ETag
        ResponseEntity<String> putResp = gatewayController.updateResource(
                "Practitioner",
                "pract-stale-103",
                updatedJson,
                "W/\"99\"",
                "corr-stale-103",
                "pas",
                "steward",
                null
        );

        assertThat(putResp.getStatusCode().value()).isEqualTo(202);
        String pragmaId = extractPragmaId(putResp.getHeaders().getLocation().toString());

        Pragma pragma = cache.get(pragmaId);
        Exchange exchange = camelContext.getEndpoint("direct:test").createExchange();
        practErgon.processErgon(pragma, exchange);

        assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.FAILED);
        assertThat(pragma.getOutput()).isNotEmpty();
    }

    @Test
    @DisplayName("Referential Rejection: Broken Practitioner reference -> REJECTED with OperationOutcome")
    void testReferentialRejection() throws Exception {
        SyntheticProviderRegistryGenerator.ProviderRegistryGraph brokenGraph =
                new SyntheticProviderRegistryGenerator(42L).generateInvalidReferenceGraph(42L);

        String roleJson = parser.encodeResourceToString(brokenGraph.getPractitionerRole());
        ResponseEntity<String> resp = gatewayController.createResource(
                "PractitionerRole",
                roleJson,
                "corr-ref-rej",
                "pas",
                "steward",
                null
        );

        assertThat(resp.getStatusCode().value()).isEqualTo(202);
        String pragmaId = extractPragmaId(resp.getHeaders().getLocation().toString());

        Pragma pragma = cache.get(pragmaId);
        Exchange exchange = camelContext.getEndpoint("direct:test").createExchange();
        roleErgon.processErgon(pragma, exchange);

        assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.REJECTED);
        assertThat(pragma.getOutput()).isNotEmpty();
    }

    private String extractPragmaId(String location) {
        return location.substring(location.lastIndexOf('/') + 1);
    }
}
