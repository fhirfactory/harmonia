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
import org.hl7.fhir.r5.model.PractitionerRole;
import org.hl7.fhir.r5.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PractitionerRoleChangeErgonTest {

    private PractitionerRoleChangeErgon ergon;
    private FhirStorageService mockStorageService;
    private ProviderRegistryReferenceValidator mockReferenceValidator;
    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        ergon = new PractitionerRoleChangeErgon();
        mockStorageService = Mockito.mock(FhirStorageService.class);
        mockReferenceValidator = Mockito.mock(ProviderRegistryReferenceValidator.class);
        ergon.setStorageService(mockStorageService);
        ergon.setReferenceValidator(mockReferenceValidator);
    }

    @Test
    @DisplayName("Valid PractitionerRole passes referential integrity and commits")
    void testValidPractitionerRole() throws Exception {
        PractitionerRole role = new PractitionerRole();
        role.setPractitioner(new Reference("Practitioner/PR-100"));
        role.setOrganization(new Reference("Organization/ORG-100"));

        PractitionerRole savedRole = role.copy();
        savedRole.setId("PractitionerRole/PRR-100");
        savedRole.getMeta().setVersionId("1");

        when(mockReferenceValidator.validateReferences(any())).thenReturn(ValidationResult.success());
        when(mockStorageService.createResource(any())).thenReturn(savedRole);

        Pragma pragma = ProviderRegistryChangePragma.buildChangeRequestPragma(
                ProviderRegistryConstants.OPERATION_CREATE,
                role,
                "admin-user",
                "pylai-gateway",
                "corr-role-1",
                null
        );

        Exchange exchange = camelContext.getEndpoint("direct:test").createExchange();
        ergon.processErgon(pragma, exchange);

        assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.COMPLETED);
        assertThat(pragma.getOutput()).isNotEmpty();
    }

    @Test
    @DisplayName("PractitionerRole referencing non-existent Practitioner is rejected")
    void testMissingPractitionerReferenceRejection() throws Exception {
        PractitionerRole role = new PractitionerRole();
        role.setPractitioner(new Reference("Practitioner/missing-999"));
        role.setOrganization(new Reference("Organization/ORG-100"));

        when(mockReferenceValidator.validateReferences(any())).thenReturn(
                ValidationResult.failure(ProviderRegistryConstants.VAL_CODE_REFERENCE_NOT_FOUND, "Practitioner missing-999 not found")
        );

        Pragma pragma = ProviderRegistryChangePragma.buildChangeRequestPragma(
                ProviderRegistryConstants.OPERATION_CREATE,
                role,
                "admin-user",
                "pylai-gateway",
                "corr-role-2",
                null
        );

        Exchange exchange = camelContext.getEndpoint("direct:test").createExchange();
        ergon.processErgon(pragma, exchange);

        assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.REJECTED);
        assertThat(pragma.getOutput()).isNotEmpty();
    }
}
