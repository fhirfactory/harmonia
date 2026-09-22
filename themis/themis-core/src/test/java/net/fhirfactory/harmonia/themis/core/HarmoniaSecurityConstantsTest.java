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

package net.fhirfactory.harmonia.themis.core;

import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.core.constants.HarmoniaSecurityConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Harmonia Security Constants Tests")
class HarmoniaSecurityConstantsTest {

    @Test
    @DisplayName("Clinical roles are registered with their approved authority mappings")
    void clinicalRolesAreRegistered() {
        assertThat(HarmoniaSecurityConstants.getRole(HarmoniaSecurityConstants.ROLE_CLINICAL_READ))
                .isPresent()
                .get()
                .satisfies(role -> assertThat(role.authorities())
                        .extracting(ThemisAuthority::authorityCode)
                        .containsExactlyInAnyOrder(
                                HarmoniaSecurityConstants.AUTH_CLINICAL_READ,
                                HarmoniaSecurityConstants.AUTH_CLINICAL_SEARCH));

        assertThat(HarmoniaSecurityConstants.getRole(HarmoniaSecurityConstants.ROLE_CLINICAL_WRITE))
                .isPresent()
                .get()
                .satisfies(role -> assertThat(role.authorities())
                        .extracting(ThemisAuthority::authorityCode)
                        .containsExactlyInAnyOrder(
                                HarmoniaSecurityConstants.AUTH_CLINICAL_READ,
                                HarmoniaSecurityConstants.AUTH_CLINICAL_SEARCH,
                                HarmoniaSecurityConstants.AUTH_CLINICAL_CREATE,
                                HarmoniaSecurityConstants.AUTH_CLINICAL_UPDATE));

        assertThat(HarmoniaSecurityConstants.getRole(HarmoniaSecurityConstants.ROLE_CLINICAL_ADMIN))
                .isPresent()
                .get()
                .satisfies(role -> assertThat(role.authorities())
                        .extracting(ThemisAuthority::authorityCode)
                        .containsExactlyInAnyOrder(
                                HarmoniaSecurityConstants.AUTH_CLINICAL_READ,
                                HarmoniaSecurityConstants.AUTH_CLINICAL_SEARCH,
                                HarmoniaSecurityConstants.AUTH_CLINICAL_CREATE,
                                HarmoniaSecurityConstants.AUTH_CLINICAL_UPDATE,
                                HarmoniaSecurityConstants.AUTH_CLINICAL_ADMIN)
                        .doesNotContain("clinical.delete"));
    }

    @Test
    @DisplayName("Operations roles are registered with their approved authority mappings")
    void operationsRolesAreRegistered() {
        assertThat(HarmoniaSecurityConstants.getRole(HarmoniaSecurityConstants.ROLE_OPS_VIEWER))
                .isPresent()
                .get()
                .satisfies(role -> assertThat(role.authorities())
                        .extracting(ThemisAuthority::authorityCode)
                        .containsExactlyInAnyOrder(HarmoniaSecurityConstants.AUTH_OPERATIONS_READ));

        assertThat(HarmoniaSecurityConstants.getRole(HarmoniaSecurityConstants.ROLE_OPS_ADM))
                .isPresent()
                .get()
                .satisfies(role -> assertThat(role.authorities())
                        .extracting(ThemisAuthority::authorityCode)
                        .containsExactlyInAnyOrder(
                                HarmoniaSecurityConstants.AUTH_OPERATIONS_READ,
                                HarmoniaSecurityConstants.AUTH_OPERATIONS_ADMIN));
    }

    @Test
    @DisplayName("Existing role registry entries remain available")
    void existingRolesRemainRegistered() {
        assertThat(HarmoniaSecurityConstants.getRole(HarmoniaSecurityConstants.ROLE_PRV_RDR)).contains(HarmoniaSecurityConstants.PRV_RDR);
        assertThat(HarmoniaSecurityConstants.getRole(HarmoniaSecurityConstants.ROLE_AUD_RDR)).contains(HarmoniaSecurityConstants.AUD_RDR);
        assertThat(HarmoniaSecurityConstants.getRole(HarmoniaSecurityConstants.ROLE_SYS_ADM)).contains(HarmoniaSecurityConstants.SYS_ADM);
        assertThat(HarmoniaSecurityConstants.getRole(HarmoniaSecurityConstants.ROLE_OPS_VIEWER)).contains(HarmoniaSecurityConstants.OPS_VIEWER);
        assertThat(HarmoniaSecurityConstants.getRole(HarmoniaSecurityConstants.ROLE_OPS_ADM)).contains(HarmoniaSecurityConstants.OPS_ADM);
    }
}