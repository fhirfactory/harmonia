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
import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.model.pragma.ProviderRegistryChangePragma;
import net.fhirfactory.harmonia.model.registry.ProviderRegistryConstants;
import net.fhirfactory.harmonia.model.security.ErgonSecurityDefinition;
import net.fhirfactory.harmonia.model.security.HarmoniaAuthorityEnum;
import net.fhirfactory.harmonia.themis.api.ThemisService;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.core.evaluator.DeterministicPolicyEvaluator;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Practitioner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Provider Registry Change Ergon Persistence Security Tests")
class AbstractProviderRegistryChangeErgonPersistenceSecurityTest {

    private CamelContext camelContext;
    private FhirStorageService storageServiceMock;
    private ProviderRegistryReferenceValidator referenceValidatorMock;
    private ThemisService themisService;

    static class TestPractitionerChangeErgon extends AbstractProviderRegistryChangeErgon<Practitioner> {
        public TestPractitionerChangeErgon() {
            super("ergon:practitioner-change", "Practitioner Change Ergon", Practitioner.class, "Practitioner");
        }

        @Override
        protected void validateResource(Practitioner resource, Pragma pragma, List<String> errorMessages, List<String> errorCodes) {
            // Valid for test
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        camelContext = new DefaultCamelContext();
        storageServiceMock = Mockito.mock(FhirStorageService.class);
        referenceValidatorMock = Mockito.mock(ProviderRegistryReferenceValidator.class);
        themisService = DeterministicPolicyEvaluator.withDefaultPolicies();

        when(referenceValidatorMock.validateReferences(any())).thenReturn(ProviderRegistryReferenceValidator.ValidationResult.valid());
    }

    @AfterEach
    void tearDown() throws Exception {
        if (camelContext != null) {
            camelContext.stop();
        }
    }

    @Test
    @DisplayName("Allows persistence when Ergon has PRV_PROC and provider.resource.update authority")
    void testAuthorizedPersistenceAllowed() throws Exception {
        TestPractitionerChangeErgon ergon = new TestPractitionerChangeErgon();
        ergon.setStorageService(storageServiceMock);
        ergon.setReferenceValidator(referenceValidatorMock);
        ergon.setThemisService(themisService);

        Practitioner practitioner = new Practitioner();
        practitioner.setId("PR-901");
        practitioner.addName(new HumanName().setFamily("Smith").addGiven("John"));

        when(storageServiceMock.updateResource(eq("PR-901"), any(), any())).thenReturn(practitioner);

        Pragma pragma = ProviderRegistryChangePragma.buildChangeRequestPragma(
                ProviderRegistryConstants.OPERATION_UPDATE,
                practitioner,
                "user:submitter",
                "test-source",
                "corr-persist-01",
                "W/\"1\""
        );

        ergon.processErgon(pragma, null);

        assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.COMPLETED);
        verify(storageServiceMock).updateResource(eq("PR-901"), any(), any());
    }

    @Test
    @DisplayName("Denies persistence and fails closed when Ergon lacks provider.resource.update authority (Persistence Gate)")
    void testDeniedPersistencePrivilegeRevoked() throws Exception {
        TestPractitionerChangeErgon ergon = new TestPractitionerChangeErgon();
        ergon.setStorageService(storageServiceMock);
        ergon.setReferenceValidator(referenceValidatorMock);
        ergon.setThemisService(themisService);

        // Revoke persistence authority, keeping only process authority
        ergon.setSecurityDefinition(ErgonSecurityDefinition.builder()
                .ergonId("ergon:practitioner-change")
                .requiredExecutionAuthorities(Set.of(HarmoniaAuthorityEnum.PROVIDER_CHANGE_PROCESS.toThemisAuthority())) // Missing resource.update!
                .permittedResourceType("Practitioner")
                .permittedSecurityDomain("PROVIDER_REGISTRY")
                .build());

        Practitioner practitioner = new Practitioner();
        practitioner.setId("PR-902");
        practitioner.addName(new HumanName().setFamily("Doe").addGiven("Jane"));

        Pragma pragma = ProviderRegistryChangePragma.buildChangeRequestPragma(
                ProviderRegistryConstants.OPERATION_UPDATE,
                practitioner,
                "user:submitter",
                "test-source",
                "corr-persist-02",
                "W/\"1\""
        );

        ergon.processErgon(pragma, null);

        assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.FAILED);
        assertThat(pragma.getCheckpoints())
                .anyMatch(cp -> cp.getStageName().equals("COMMIT_DENIED_BY_THEMIS")
                        && cp.getStatusMessage().contains("AUTHORITY_MISSING"));

        // Verify storage service was NEVER called
        verify(storageServiceMock, never()).updateResource(any(), any(), any());
        verify(storageServiceMock, never()).createResource(any());
    }
}
