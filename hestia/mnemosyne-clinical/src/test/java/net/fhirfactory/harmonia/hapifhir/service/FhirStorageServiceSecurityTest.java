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

package net.fhirfactory.harmonia.hapifhir.service;

import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import net.fhirfactory.harmonia.hapifhir.repository.FhirResourceRepository;
import net.fhirfactory.harmonia.model.security.HarmoniaAuthorityEnum;
import net.fhirfactory.harmonia.themis.api.ThemisService;
import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.core.evaluator.DeterministicPolicyEvaluator;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Practitioner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@DisplayName("Mnemosyne FhirStorageService Themis Security Gate Tests")
class FhirStorageServiceSecurityTest {

    private FhirResourceRepository repositoryMock;
    private ThemisService themisService;
    private FhirStorageService storageService;

    @BeforeEach
    void setUp() {
        repositoryMock = Mockito.mock(FhirResourceRepository.class);
        themisService = DeterministicPolicyEvaluator.withDefaultPolicies();
        storageService = new FhirStorageService(repositoryMock, themisService);
    }

    @Test
    @DisplayName("Allows create when caller has provider.resource.create authority")
    void testCreateAuthorized() {
        Practitioner practitioner = new Practitioner();
        practitioner.setId("PR-ST-1");
        practitioner.addName(new HumanName().setFamily("Smith").addGiven("John"));

        ThemisPrincipal principal = ThemisPrincipal.of("service:provider-registry", PrincipalType.SERVICE, "hestia");

        assertThatCode(() -> storageService.createResource(
                practitioner,
                principal,
                Set.of(HarmoniaAuthorityEnum.PROVIDER_RESOURCE_CREATE.toThemisAuthority()),
                "corr-store-01"
        )).doesNotThrowAnyException();

        verify(repositoryMock).save(any());
    }

    @Test
    @DisplayName("Denies create when caller lacks provider.resource.create authority (Default Deny)")
    void testCreateDeniedMissingAuthority() {
        Practitioner practitioner = new Practitioner();
        practitioner.setId("PR-ST-2");
        practitioner.addName(new HumanName().setFamily("Doe").addGiven("Jane"));

        ThemisPrincipal principal = ThemisPrincipal.of("user:reader-only", PrincipalType.HUMAN, "hospital-east");

        assertThatThrownBy(() -> storageService.createResource(
                practitioner,
                principal,
                Set.of(HarmoniaAuthorityEnum.PROVIDER_READ.toThemisAuthority()), // Only read authority!
                "corr-store-02"
        )).isInstanceOf(ForbiddenOperationException.class)
          .hasMessageContaining("Persistence authorization denied by Themis");
    }
}
