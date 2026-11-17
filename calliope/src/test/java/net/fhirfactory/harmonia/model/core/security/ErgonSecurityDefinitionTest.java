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

package net.fhirfactory.harmonia.model.core.security;

import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/** Verifies that an Ergon security definition denies incomplete and unlisted requests. */
class ErgonSecurityDefinitionTest {

    @Test
    void actionChecksDenyNullEmptyAndUnlistedValues() {
        ErgonSecurityDefinition empty = definition(Set.of(), Set.of(), Set.of());
        ErgonSecurityDefinition limited = definition(Set.of(ThemisAction.EXECUTE), Set.of(), Set.of());

        assertThat(empty.permitsAction(null)).isFalse();
        assertThat(empty.permitsAction(ThemisAction.EXECUTE)).isFalse();
        assertThat(limited.permitsAction(null)).isFalse();
        assertThat(limited.permitsAction(ThemisAction.PROCESS)).isFalse();
        assertThat(limited.permitsAction(ThemisAction.EXECUTE)).isTrue();
    }

    @Test
    void resourceTypeChecksDenyNullBlankEmptyAndUnlistedValues() {
        ErgonSecurityDefinition empty = definition(Set.of(), Set.of(), Set.of());
        ErgonSecurityDefinition limited = definition(Set.of(), Set.of("Patient"), Set.of());

        assertThat(empty.permitsResourceType(null)).isFalse();
        assertThat(empty.permitsResourceType("Patient")).isFalse();
        assertThat(limited.permitsResourceType(" ")).isFalse();
        assertThat(limited.permitsResourceType("Observation")).isFalse();
        assertThat(limited.permitsResourceType("patient")).isTrue();
    }

    @Test
    void securityDomainChecksDenyNullBlankEmptyAndUnlistedValues() {
        ErgonSecurityDefinition empty = definition(Set.of(), Set.of(), Set.of());
        ErgonSecurityDefinition limited = definition(Set.of(), Set.of(), Set.of("CLINICAL"));

        assertThat(empty.permitsSecurityDomain(null)).isFalse();
        assertThat(empty.permitsSecurityDomain("CLINICAL")).isFalse();
        assertThat(limited.permitsSecurityDomain(" ")).isFalse();
        assertThat(limited.permitsSecurityDomain("PROVIDER_REGISTRY")).isFalse();
        assertThat(limited.permitsSecurityDomain("clinical")).isTrue();
    }

    private static ErgonSecurityDefinition definition(
            Set<ThemisAction> actions,
            Set<String> resourceTypes,
            Set<String> securityDomains) {
        return new ErgonSecurityDefinition("ergon-test", Set.of(), actions, resourceTypes, securityDomains);
    }
}