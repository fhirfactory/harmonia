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

package net.fhirfactory.harmonia.themis.core.identities;

import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Harmonia Service Identities Tests")
class HarmoniaServiceIdentitiesTest {

    @Test
    @DisplayName("SERVICE:mneme is registered with least-privilege clinical authorities")
    void mnemeServiceIdentityAndAuthoritiesAreRegistered() {
        assertThat(HarmoniaServiceIdentities.ID_MNEME).isEqualTo("service:mneme");
        assertThat(HarmoniaServiceIdentities.PRINCIPAL_MNEME).isNotNull();
        assertThat(HarmoniaServiceIdentities.PRINCIPAL_MNEME.principalId()).isEqualTo("service:mneme");
        assertThat(HarmoniaServiceIdentities.PRINCIPAL_MNEME.principalType()).isEqualTo(PrincipalType.SERVICE);
        assertThat(HarmoniaServiceIdentities.PRINCIPAL_MNEME.sourceDomain()).isEqualTo("mneme");

        Set<ThemisAuthority> mnemeAuthorities = HarmoniaServiceIdentities.getAuthorities(HarmoniaServiceIdentities.ID_MNEME);
        assertThat(mnemeAuthorities)
                .extracting(ThemisAuthority::authorityCode)
                .containsExactlyInAnyOrder(
                        "clinical.read",
                        "clinical.create",
                        "clinical.update",
                        "clinical.delete",
                        "system.integration"
                );

        // Verify principal overload returns identical authorities
        assertThat(HarmoniaServiceIdentities.getAuthorities(HarmoniaServiceIdentities.PRINCIPAL_MNEME))
                .isEqualTo(mnemeAuthorities);

        // Verify authorities set is unmodifiable
        assertThatThrownBy(() -> mnemeAuthorities.add(ThemisAuthority.of("admin")))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("Canonical service principals and authorities are unmodifiable and non-empty")
    void canonicalServiceAuthoritiesRegistry() {
        assertThat(HarmoniaServiceIdentities.getAllServiceAuthorities()).containsKey(HarmoniaServiceIdentities.ID_MNEME);
        assertThat(HarmoniaServiceIdentities.getAllServiceAuthorities()).containsKey(HarmoniaServiceIdentities.ID_IRIS_BEFE);
        assertThat(HarmoniaServiceIdentities.getAllServiceAuthorities()).containsKey(HarmoniaServiceIdentities.ID_MNEMOSYNE);
        assertThat(HarmoniaServiceIdentities.getAllServiceAuthorities()).containsKey(HarmoniaServiceIdentities.ID_PONOS);
        assertThat(HarmoniaServiceIdentities.getAllServiceAuthorities()).containsKey(HarmoniaServiceIdentities.ID_PONOS_PROCESS);

        // Unknown service returns empty set
        assertThat(HarmoniaServiceIdentities.getAuthorities("unknown:service")).isEmpty();
        assertThat(HarmoniaServiceIdentities.getAuthorities((String) null)).isEmpty();
    }
}
