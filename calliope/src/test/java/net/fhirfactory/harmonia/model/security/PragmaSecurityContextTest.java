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

import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaFhirConverter;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.hl7.fhir.r5.model.Extension;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Pragma Security Context & Ergon Security Tests")
class PragmaSecurityContextTest {

    @Test
    @DisplayName("Pragma builds and retains originating security context")
    void testPragmaSecurityContextRetention() {
        ThemisPrincipal principal = ThemisPrincipal.of("user:dr-smith", PrincipalType.HUMAN, "hospital-east");
        ThemisAuthority authority = ThemisAuthority.of("provider.change.submit");
        ThemisSecurityContext context = ThemisSecurityContext.fromPrincipal(principal, "corr-123");

        Pragma pragma = Pragma.builder()
                .pragmaId("pragma-sec-01")
                .correlationId("corr-123")
                .status(PragmaStatus.ACCEPTED)
                .originatingPrincipal(principal)
                .addOriginatingAuthority(authority)
                .originatingSecurityContext(context)
                .policyVersion("1.0.0")
                .build();

        assertThat(pragma.getOriginatingPrincipal()).isNotNull();
        assertThat(pragma.getOriginatingPrincipal().principalId()).isEqualTo("user:dr-smith");
        assertThat(pragma.getOriginatingPrincipal().sourceDomain()).isEqualTo("hospital-east");
        assertThat(pragma.getOriginatingAuthorities()).containsExactly(authority);
        assertThat(pragma.getPolicyVersion()).isEqualTo("1.0.0");
    }

    @Test
    @DisplayName("Originating authorities set is unmodifiable")
    void testOriginatingAuthoritiesImmutability() {
        Pragma pragma = new Pragma();
        pragma.addOriginatingAuthority("provider.read");

        Set<ThemisAuthority> authorities = pragma.getOriginatingAuthorities();
        assertThatThrownBy(() -> authorities.add(ThemisAuthority.of("provider.admin")))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("PragmaFhirConverter bi-directionally converts security claims to FHIR Task extensions")
    void testPragmaFhirConverterSecurityExtensions() {
        ThemisPrincipal principal = ThemisPrincipal.of("service:pylai-gateway", PrincipalType.SERVICE, "pylai");
        ThemisAuthority auth1 = ThemisAuthority.of("provider.change.submit");
        ThemisAuthority auth2 = ThemisAuthority.of("provider.read");

        Pragma original = Pragma.builder()
                .pragmaId("task-sec-456")
                .correlationId("corr-456")
                .originatingPrincipal(principal)
                .addOriginatingAuthority(auth1)
                .addOriginatingAuthority(auth2)
                .policyVersion("2.1.0")
                .build();

        Task fhirTask = PragmaFhirConverter.toFhirTask(original);
        assertThat(fhirTask).isNotNull();

        // Verify task extensions
        assertThat(fhirTask.getExtensionByUrl(PragmaFhirConverter.EXTENSION_SECURITY_PRINCIPAL_ID).getValue())
                .isInstanceOf(StringType.class)
                .extracting(v -> ((StringType) v).getValue())
                .isEqualTo("service:pylai-gateway");

        assertThat(fhirTask.getExtensionByUrl(PragmaFhirConverter.EXTENSION_SECURITY_PRINCIPAL_TYPE).getValue())
                .extracting(v -> ((StringType) v).getValue())
                .isEqualTo("SERVICE");

        // Convert back to Pragma
        Pragma reconstructed = PragmaFhirConverter.fromFhirTask(fhirTask);
        assertThat(reconstructed).isNotNull();
        assertThat(reconstructed.getOriginatingPrincipal()).isNotNull();
        assertThat(reconstructed.getOriginatingPrincipal().principalId()).isEqualTo("service:pylai-gateway");
        assertThat(reconstructed.getOriginatingPrincipal().principalType()).isEqualTo(PrincipalType.SERVICE);
        assertThat(reconstructed.getOriginatingAuthorities())
                .extracting(ThemisAuthority::authorityCode)
                .containsExactlyInAnyOrder("provider.change.submit", "provider.read");
        assertThat(reconstructed.getPolicyVersion()).isEqualTo("2.1.0");
    }

    @Test
    @DisplayName("ErgonSecurityDefinition validates execution authorities, actions and domains")
    void testErgonSecurityDefinition() {
        ErgonSecurityDefinition def = ErgonSecurityDefinition.forProviderRegistryChange("ergon:practitioner-change", "Practitioner");

        assertThat(def.ergonId()).isEqualTo("ergon:practitioner-change");
        assertThat(def.permitsAction(ThemisAction.PROCESS)).isTrue();
        assertThat(def.permitsAction(ThemisAction.UPDATE)).isTrue();
        assertThat(def.permitsAction(ThemisAction.DELETE)).isFalse();
        assertThat(def.permitsResourceType("Practitioner")).isTrue();
        assertThat(def.permitsResourceType("Patient")).isFalse();
        assertThat(def.permitsSecurityDomain("PROVIDER_REGISTRY")).isTrue();
        assertThat(def.permitsSecurityDomain("AUDIT")).isFalse();

        assertThat(def.requiredExecutionAuthorities())
                .extracting(ThemisAuthority::authorityCode)
                .contains("provider.change.process", "provider.resource.create", "provider.resource.update");
    }
}
