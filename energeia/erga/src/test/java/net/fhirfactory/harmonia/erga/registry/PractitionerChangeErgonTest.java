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
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.model.pragma.ProviderRegistryChangePragma;
import net.fhirfactory.harmonia.model.registry.ProviderRegistryConstants;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Identifier;
import org.hl7.fhir.r5.model.Practitioner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class PractitionerChangeErgonTest {

    private PractitionerChangeErgon ergon;
    private FhirStorageService mockStorageService;
    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        ergon = new PractitionerChangeErgon();
        mockStorageService = Mockito.mock(FhirStorageService.class);
        ergon.setStorageService(mockStorageService);
    }

    @Test
    @DisplayName("Valid Practitioner creation is approved and committed")
    void testValidPractitionerCreate() throws Exception {
        Practitioner practitioner = new Practitioner();
        practitioner.addName(new HumanName().setFamily("Smith").addGiven("John"));
        practitioner.addIdentifier(new Identifier().setSystem("http://ns.electronichealth.net.au/id/hi/hpii/1.0").setValue("8003610000000001"));

        Practitioner savedPractitioner = practitioner.copy();
        savedPractitioner.setId("Practitioner/PR-001");
        savedPractitioner.getMeta().setVersionId("1");

        when(mockStorageService.searchResources(eq(ProviderRegistryConstants.RESOURCE_PRACTITIONER), any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(mockStorageService.createResource(any(Practitioner.class)))
                .thenReturn(savedPractitioner);

        Pragma pragma = ProviderRegistryChangePragma.buildChangeRequestPragma(
                ProviderRegistryConstants.OPERATION_CREATE,
                practitioner,
                "admin-user",
                "pylai-gateway",
                "corr-1",
                null
        );

        Exchange exchange = camelContext.getEndpoint("direct:test").createExchange();
        ergon.processErgon(pragma, exchange);

        assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.COMPLETED);
        assertThat(pragma.getMetadata().get(ProviderRegistryConstants.METADATA_RESULTING_VERSION)).isEqualTo("1");
        assertThat(pragma.getOutput()).isNotEmpty();
    }

    @Test
    @DisplayName("Practitioner without name or identifier is rejected")
    void testInvalidPractitionerMissingFields() throws Exception {
        Practitioner invalidPractitioner = new Practitioner();

        Pragma pragma = ProviderRegistryChangePragma.buildChangeRequestPragma(
                ProviderRegistryConstants.OPERATION_CREATE,
                invalidPractitioner,
                "admin-user",
                "pylai-gateway",
                "corr-2",
                null
        );

        Exchange exchange = camelContext.getEndpoint("direct:test").createExchange();
        ergon.processErgon(pragma, exchange);

        assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.REJECTED);
        assertThat(pragma.getOutput()).isNotEmpty();
    }

    @Test
    @DisplayName("Duplicate identifier rejects Practitioner creation")
    void testDuplicateIdentifierRejection() throws Exception {
        Practitioner practitioner = new Practitioner();
        practitioner.addName(new HumanName().setFamily("Doe").addGiven("Jane"));
        practitioner.addIdentifier(new Identifier().setSystem("http://ns.electronichealth.net.au/id/hi/hpii/1.0").setValue("8003610000000001"));

        Practitioner existing = new Practitioner();
        existing.setId("Practitioner/EXISTING-PR-001");
        existing.addIdentifier(new Identifier().setSystem("http://ns.electronichealth.net.au/id/hi/hpii/1.0").setValue("8003610000000001"));

        when(mockStorageService.searchResources(eq(ProviderRegistryConstants.RESOURCE_PRACTITIONER), any(), any(), eq("8003610000000001")))
                .thenReturn(List.of(existing));

        Pragma pragma = ProviderRegistryChangePragma.buildChangeRequestPragma(
                ProviderRegistryConstants.OPERATION_CREATE,
                practitioner,
                "admin-user",
                "pylai-gateway",
                "corr-3",
                null
        );

        Exchange exchange = camelContext.getEndpoint("direct:test").createExchange();
        ergon.processErgon(pragma, exchange);

        assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.REJECTED);
    }
}
