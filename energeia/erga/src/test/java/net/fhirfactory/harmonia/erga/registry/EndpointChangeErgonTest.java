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

package net.fhirfactory.harmonia.erga.registry;

import net.fhirfactory.harmonia.hapifhir.service.FhirStorageService;
import net.fhirfactory.harmonia.hapifhir.service.ProviderRegistryReferenceValidator;
import net.fhirfactory.harmonia.hapifhir.service.ProviderRegistryReferenceValidator.ValidationResult;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.model.pragma.ProviderRegistryChangePragma;
import net.fhirfactory.harmonia.model.registry.ProviderRegistryConstants;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Endpoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class EndpointChangeErgonTest {

    private EndpointChangeErgon ergon;
    private FhirStorageService mockStorageService;
    private ProviderRegistryReferenceValidator mockReferenceValidator;
    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        ergon = new EndpointChangeErgon();
        mockStorageService = Mockito.mock(FhirStorageService.class);
        mockReferenceValidator = Mockito.mock(ProviderRegistryReferenceValidator.class);
        ergon.setStorageService(mockStorageService);
        ergon.setReferenceValidator(mockReferenceValidator);
    }

    @Test
    @DisplayName("Valid Endpoint is approved and committed")
    void testValidEndpoint() throws Exception {
        Endpoint endpoint = new Endpoint();
        endpoint.setName("CareConnect FHIR Service");
        endpoint.setStatus(Endpoint.EndpointStatus.ACTIVE);
        endpoint.setAddress("https://fhir.careconnect.org/r5");
        endpoint.addConnectionType(new CodeableConcept().addCoding(
                new Coding("http://terminology.hl7.org/CodeSystem/endpoint-connection-type", "hl7-fhir-rest", "HL7 FHIR REST")
        ));

        Endpoint savedEp = endpoint.copy();
        savedEp.setId("Endpoint/EP-001");
        savedEp.getMeta().setVersionId("1");

        when(mockReferenceValidator.validateReferences(any())).thenReturn(ValidationResult.success());
        when(mockStorageService.createResource(any())).thenReturn(savedEp);

        Pragma pragma = ProviderRegistryChangePragma.buildChangeRequestPragma(
                ProviderRegistryConstants.OPERATION_CREATE,
                endpoint,
                "admin-user",
                "pylai-gateway",
                "corr-ep-1",
                null
        );

        Exchange exchange = camelContext.getEndpoint("direct:test").createExchange();
        ergon.processErgon(pragma, exchange);

        assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.COMPLETED);
        assertThat(pragma.getOutput()).isNotEmpty();
    }

    @Test
    @DisplayName("Endpoint missing connection type or address is rejected")
    void testInvalidEndpoint() throws Exception {
        Endpoint invalid = new Endpoint();
        invalid.setName("Broken Endpoint");

        when(mockReferenceValidator.validateReferences(any())).thenReturn(ValidationResult.success());

        Pragma pragma = ProviderRegistryChangePragma.buildChangeRequestPragma(
                ProviderRegistryConstants.OPERATION_CREATE,
                invalid,
                "admin-user",
                "pylai-gateway",
                "corr-ep-2",
                null
        );

        Exchange exchange = camelContext.getEndpoint("direct:test").createExchange();
        ergon.processErgon(pragma, exchange);

        assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.REJECTED);
    }
}
