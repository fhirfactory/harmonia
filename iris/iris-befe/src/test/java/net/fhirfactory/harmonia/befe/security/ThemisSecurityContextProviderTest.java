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

package net.fhirfactory.harmonia.befe.security;

import net.fhirfactory.harmonia.model.security.HarmoniaSecurityLabelEnum;
import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import net.fhirfactory.harmonia.themis.core.identities.HarmoniaServiceIdentities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link ThemisSecurityContextProvider} verifying request-scoped security context holding,
 * fail-closed governance, tampering prevention, and lifecycle state management.
 */
class ThemisSecurityContextProviderTest {

    private ThemisSecurityContextProvider provider;
    private ThemisSecurityContext validContext;

    @BeforeEach
    void setUp() {
        provider = new ThemisSecurityContextProvider();

        ThemisPrincipal humanPrincipal = ThemisPrincipal.of("dr.smith", PrincipalType.HUMAN, "harmonia-clinical");
        validContext = ThemisSecurityContext.builder()
                .requestingPrincipal(humanPrincipal)
                .executingPrincipal(HarmoniaServiceIdentities.PRINCIPAL_IRIS_BEFE)
                .securityDomain(HarmoniaSecurityLabelEnum.CLINICAL.getCode())
                .authorities(Set.of(ThemisAuthority.of("clinical.read"), ThemisAuthority.of("clinical.create")))
                .correlationId(UUID.randomUUID().toString())
                .requestedAt(Instant.now())
                .build();
    }

    @Test
    @DisplayName("1. Initially unbound provider returns empty Optional and hasSecurityContext=false")
    void testInitiallyUnbound() {
        assertThat(provider.hasSecurityContext()).isFalse();
        assertThat(provider.getSecurityContext()).isEmpty();
        assertThat(provider.get()).isNull();
    }

    @Test
    @DisplayName("2. requireSecurityContext fails closed when unbound")
    void testRequireSecurityContextFailsClosedWhenUnbound() {
        assertThatThrownBy(() -> provider.requireSecurityContext())
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("FAIL CLOSED");
    }

    @Test
    @DisplayName("3. Successfully bind and retrieve canonical security context")
    void testBindAndRetrieveContext() {
        provider.setSecurityContext(validContext);

        assertThat(provider.hasSecurityContext()).isTrue();
        assertThat(provider.getSecurityContext()).contains(validContext);
        assertThat(provider.get()).isSameAs(validContext);
        assertThat(provider.requireSecurityContext()).isSameAs(validContext);

        ThemisSecurityContext ctx = provider.requireSecurityContext();
        assertThat(ctx.requestingPrincipal().principalId()).isEqualTo("dr.smith");
        assertThat(ctx.requestingPrincipal().principalType()).isEqualTo(PrincipalType.HUMAN);
        assertThat(ctx.executingPrincipal()).isEqualTo(HarmoniaServiceIdentities.PRINCIPAL_IRIS_BEFE);
        assertThat(ctx.securityDomain()).isEqualTo("CLINICAL");
        assertThat(ctx.authorities()).contains(ThemisAuthority.of("clinical.read"), ThemisAuthority.of("clinical.create"));
    }

    @Test
    @DisplayName("4. Binding null context throws NullPointerException")
    void testBindNullContextThrows() {
        assertThatThrownBy(() -> provider.setSecurityContext(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("securityContext must not be null");
    }

    @Test
    @DisplayName("5. Setting same/equal context is idempotent")
    void testSettingSameContextIsIdempotent() {
        provider.setSecurityContext(validContext);
        // Setting the exact same instance or equal record must not fail
        provider.setSecurityContext(validContext);

        assertThat(provider.get()).isSameAs(validContext);
    }

    @Test
    @DisplayName("6. Overwriting with a different context is rejected (anti-tampering)")
    void testOverwritingWithDifferentContextThrows() {
        provider.setSecurityContext(validContext);

        ThemisPrincipal otherHuman = ThemisPrincipal.of("attacker", PrincipalType.HUMAN, "untrusted");
        ThemisSecurityContext spoofedContext = ThemisSecurityContext.builder()
                .requestingPrincipal(otherHuman)
                .correlationId(UUID.randomUUID().toString())
                .build();

        assertThatThrownBy(() -> provider.setSecurityContext(spoofedContext))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already bound to current request context and cannot be overridden");
    }

    @Test
    @DisplayName("7. clear() unbinds context allowing clean scope teardown")
    void testClearContext() {
        provider.setSecurityContext(validContext);
        assertThat(provider.hasSecurityContext()).isTrue();

        provider.clear();

        assertThat(provider.hasSecurityContext()).isFalse();
        assertThat(provider.getSecurityContext()).isEmpty();
        assertThat(provider.get()).isNull();
        assertThatThrownBy(() -> provider.requireSecurityContext())
                .isInstanceOf(SecurityException.class);
    }

    @Test
    @DisplayName("8. Separate provider instances remain strictly isolated (request-scope simulation)")
    void testProviderIsolation() {
        ThemisSecurityContextProvider request1Provider = new ThemisSecurityContextProvider();
        ThemisSecurityContextProvider request2Provider = new ThemisSecurityContextProvider();

        request1Provider.setSecurityContext(validContext);

        assertThat(request1Provider.hasSecurityContext()).isTrue();
        assertThat(request2Provider.hasSecurityContext()).isFalse();
        assertThat(request2Provider.getSecurityContext()).isEmpty();

        ThemisPrincipal user2 = ThemisPrincipal.of("nurse.jack", PrincipalType.HUMAN, "harmonia-clinical");
        ThemisSecurityContext context2 = ThemisSecurityContext.builder()
                .requestingPrincipal(user2)
                .executingPrincipal(HarmoniaServiceIdentities.PRINCIPAL_IRIS_BEFE)
                .securityDomain(HarmoniaSecurityLabelEnum.CLINICAL.getCode())
                .correlationId(UUID.randomUUID().toString())
                .build();

        request2Provider.setSecurityContext(context2);

        assertThat(request1Provider.requireSecurityContext().requestingPrincipal().principalId()).isEqualTo("dr.smith");
        assertThat(request2Provider.requireSecurityContext().requestingPrincipal().principalId()).isEqualTo("nurse.jack");
    }
}
