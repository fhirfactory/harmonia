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
import net.fhirfactory.harmonia.hapifhir.model.FhirResourceEntity;
import net.fhirfactory.harmonia.hapifhir.repository.FhirResourceRepository;
import net.fhirfactory.harmonia.hapifhir.service.FhirStorageService;
import net.fhirfactory.harmonia.hapifhir.service.ProviderRegistryReferenceValidator;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.paradeigma.common.generator.PractitionerGenerator;
import net.fhirfactory.harmonia.praxis.cache.PragmaCacheService;
import net.fhirfactory.harmonia.pylai.fhir.controller.FhirRestGatewayController;
import net.fhirfactory.harmonia.pylai.fhir.provider.CapabilityStatementProvider;
import net.fhirfactory.harmonia.pylai.fhir.security.FhirSecurityInterceptor;
import net.fhirfactory.harmonia.pylai.fhir.service.ChangeRequestSubmissionService;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.hl7.fhir.r5.model.Practitioner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Validates failure injection scenarios using explicit architectural test seams (mocking repository failure),
 * confirming that failures transition Pragma to FAILED and leave persistent storage clean without crashes.
 */
public class ProviderRegistryFailureSeamTest {

    private final FhirContext fhirContext = FhirContext.forR5();
    private IParser parser;

    private FhirResourceRepository failingRepository;
    private FhirStorageService storageService;
    private ProviderRegistryReferenceValidator referenceValidator;
    private PragmaCacheService pragmaCacheService;
    private ChangeRequestSubmissionService submissionService;
    private FhirRestGatewayController gatewayController;
    private PractitionerChangeErgon practErgon;
    private CamelContext camelContext;

    private Map<String, Pragma> cache;

    @BeforeEach
    void setUp() {
        parser = fhirContext.newJsonParser();
        cache = new ConcurrentHashMap<>();

        // Test seam: Simulate persistent storage failure
        failingRepository = Mockito.mock(FhirResourceRepository.class);
        when(failingRepository.findByResourceTypeAndFhirId(any(), any())).thenReturn(Optional.empty());
        when(failingRepository.save(any(FhirResourceEntity.class)))
                .thenThrow(new DataAccessResourceFailureException("Database connection timed out during commit"));

        storageService = new FhirStorageService(failingRepository);
        referenceValidator = new ProviderRegistryReferenceValidator(failingRepository);

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

        camelContext = new DefaultCamelContext();
    }

    @Test
    @DisplayName("Persistence Failure Seam: DB failure transitions Pragma to FAILED and handles gracefully")
    void testPersistenceFailureSeam() throws Exception {
        Practitioner practitioner = new PractitionerGenerator(999L).generateValid("pract-fail-101");
        String json = parser.encodeResourceToString(practitioner);

        ResponseEntity<String> postResp = gatewayController.createResource(
                "Practitioner",
                json,
                "corr-fail-101",
                "pas",
                "steward",
                null
        );

        assertThat(postResp.getStatusCode().value()).isEqualTo(202);
        String location = postResp.getHeaders().getLocation().toString();
        String pragmaId = location.substring(location.lastIndexOf('/') + 1);

        Pragma pragma = cache.get(pragmaId);
        assertThat(pragma).isNotNull();

        Exchange exchange = camelContext.getEndpoint("direct:test").createExchange();
        practErgon.processErgon(pragma, exchange);

        assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.FAILED);
        assertThat(pragma.getOutput()).isNotEmpty();
    }
}
