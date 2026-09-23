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

package net.fhirfactory.harmonia.model.security;

import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HarmoniaAuthorityEnumTest {

    @Test
    void testClinicalDeleteAuthority() {
        HarmoniaAuthorityEnum auth = HarmoniaAuthorityEnum.CLINICAL_DELETE;
        assertThat(auth.getCode()).isEqualTo("clinical.delete");
        assertThat(auth.getDescription()).isEqualTo("Delete Clinical resources");

        ThemisAuthority themisAuth = auth.toThemisAuthority();
        assertThat(themisAuth).isNotNull();
        assertThat(themisAuth.authorityCode()).isEqualTo("clinical.delete");

        Optional<HarmoniaAuthorityEnum> resolved = HarmoniaAuthorityEnum.fromCode("clinical.delete");
        assertThat(resolved).isPresent().contains(HarmoniaAuthorityEnum.CLINICAL_DELETE);

        Optional<HarmoniaAuthorityEnum> resolvedCaseInsensitive = HarmoniaAuthorityEnum.fromCode("CLINICAL.DELETE");
        assertThat(resolvedCaseInsensitive).isPresent().contains(HarmoniaAuthorityEnum.CLINICAL_DELETE);

        Optional<HarmoniaAuthorityEnum> resolvedWithWhitespace = HarmoniaAuthorityEnum.fromCode(" clinical.delete ");
        assertThat(resolvedWithWhitespace).isPresent().contains(HarmoniaAuthorityEnum.CLINICAL_DELETE);
    }

    @Test
    void testFromCodeWithUnknownOrNull() {
        assertThat(HarmoniaAuthorityEnum.fromCode(null)).isEmpty();
        assertThat(HarmoniaAuthorityEnum.fromCode("")).isEmpty();
        assertThat(HarmoniaAuthorityEnum.fromCode("non.existent")).isEmpty();
    }
}
