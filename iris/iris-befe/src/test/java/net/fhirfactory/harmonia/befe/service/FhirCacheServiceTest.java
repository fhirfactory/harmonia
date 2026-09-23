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

package net.fhirfactory.harmonia.befe.service;

import net.fhirfactory.harmonia.befe.security.ThemisSecurityContextProvider;
import net.fhirfactory.harmonia.model.security.FhirConfidentialityEnum;
import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
import net.fhirfactory.harmonia.model.security.HarmoniaSecurityLabelEnum;
import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import net.fhirfactory.harmonia.themis.core.identities.HarmoniaServiceIdentities;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Meta;
import org.hl7.fhir.r5.model.Organization;
import org.hl7.fhir.r5.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FhirCacheServiceTest {

    private FhirCacheService cacheService;
    private ThemisSecurityContextProvider securityContextProvider;

    @BeforeEach
    void setUp() {
        cacheService = new FhirCacheService();
        cacheService.init();
        securityContextProvider = new ThemisSecurityContextProvider();
        cacheService.setSecurityContextProvider(securityContextProvider);
    }

    @Test
    @DisplayName("Verify saveResource applies default security tag when missing")
    void testSaveResourceWithDefaultSecurityTag() {
        Person person = new Person();
        person.getNameFirstRep().setFamily("Taylor").addGiven("Alice");

        Person saved = cacheService.saveResource(person);
        assertThat(saved).isNotNull();
        assertThat(FhirSecurityTagManager.hasConfidentiality(saved, FhirConfidentialityEnum.N)).isTrue();
        assertThat(saved.getMeta().getSecurity()).hasSize(1);
        assertThat(saved.getMeta().getSecurityFirstRep().getCode()).isEqualTo("N");
        assertThat(saved.getMeta().getSecurityFirstRep().getSystem()).isEqualTo(FhirConfidentialityEnum.CONFIDENTIALITY_SYSTEM);

        // Fetch back and check
        Person fetched = cacheService.getResource("Person", saved.getIdPart(), Person.class);
        assertThat(fetched).isNotNull();
        assertThat(FhirSecurityTagManager.hasConfidentiality(fetched, FhirConfidentialityEnum.N)).isTrue();
    }

    @Test
    @DisplayName("Verify saveResource preserves explicit security tags")
    void testSaveResourcePreservesExplicitSecurityTag() {
        Organization org = new Organization();
        org.setName("Restricted Clinic");
        org.setMeta(new Meta());
        org.getMeta().addSecurity(new Coding(FhirConfidentialityEnum.CONFIDENTIALITY_SYSTEM, "R", "Restricted"));

        Organization saved = cacheService.saveResource(org);
        assertThat(saved).isNotNull();
        assertThat(FhirSecurityTagManager.hasConfidentiality(saved, FhirConfidentialityEnum.R)).isTrue();
        assertThat(FhirSecurityTagManager.hasConfidentiality(saved, FhirConfidentialityEnum.N)).isFalse();
        assertThat(saved.getMeta().getSecurity()).hasSize(1);

        Organization fetched = cacheService.getResource("Organization", saved.getIdPart(), Organization.class);
        assertThat(fetched).isNotNull();
        assertThat(FhirSecurityTagManager.hasConfidentiality(fetched, FhirConfidentialityEnum.R)).isTrue();
    }

    @Test
    @DisplayName("Verify active security context is accessible in FhirCacheService when bound")
    void testActiveSecurityContextAccessibleWhenBound() {
        ThemisPrincipal human = ThemisPrincipal.of("dr.watson", PrincipalType.HUMAN, "harmonia-clinical");
        ThemisSecurityContext context = ThemisSecurityContext.builder()
                .requestingPrincipal(human)
                .executingPrincipal(HarmoniaServiceIdentities.PRINCIPAL_IRIS_BEFE)
                .securityDomain(HarmoniaSecurityLabelEnum.CLINICAL.getCode())
                .authorities(Set.of(ThemisAuthority.of("clinical.create"), ThemisAuthority.of("clinical.read")))
                .correlationId(UUID.randomUUID().toString())
                .requestedAt(Instant.now())
                .build();

        securityContextProvider.setSecurityContext(context);

        assertThat(cacheService.getActiveSecurityContext()).contains(context);
        assertThat(cacheService.getActiveSecurityContext().get().requestingPrincipal().principalId()).isEqualTo("dr.watson");
        assertThat(cacheService.getActiveSecurityContext().get().executingPrincipal()).isEqualTo(HarmoniaServiceIdentities.PRINCIPAL_IRIS_BEFE);
    }

    @Test
    @DisplayName("Verify active security context returns empty when unbound without error")
    void testActiveSecurityContextEmptyWhenUnbound() {
        assertThat(cacheService.getActiveSecurityContext()).isEmpty();

        Person person = new Person();
        person.getNameFirstRep().setFamily("Doe").addGiven("John");

        Person saved = cacheService.saveResource(person);
        assertThat(saved).isNotNull();
    }
}
