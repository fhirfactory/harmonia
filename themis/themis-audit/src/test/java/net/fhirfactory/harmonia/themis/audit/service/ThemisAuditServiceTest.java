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

package net.fhirfactory.harmonia.themis.audit.service;

import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecisionReason;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisResource;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityLabel;
import net.fhirfactory.harmonia.themis.audit.model.ThemisAuditEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Themis Audit Service Tests")
class ThemisAuditServiceTest {

    private InMemoryThemisAuditService auditService;

    @BeforeEach
    void setUp() {
        auditService = new InMemoryThemisAuditService(100);
    }

    @Test
    @DisplayName("Records and correlates ALLOW security decision")
    void testRecordAllowDecision() {
        ThemisPrincipal principal = ThemisPrincipal.of("user:dr-smith", PrincipalType.HUMAN, "hospital-west");
        ThemisResource target = ThemisResource.of("Practitioner", "PR-123", "PROVIDER_REGISTRY",
                Set.of(ThemisSecurityLabel.of("http://harmonia.net/security-label", "PROVIDER_REGISTRY")));
        ThemisSecurityContext context = ThemisSecurityContext.fromPrincipal(principal, "corr-audit-001");

        ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                .principal(principal)
                .authorities(Set.of(ThemisAuthority.of("provider.read")))
                .action(ThemisAction.READ)
                .target(target)
                .context(context)
                .build();

        ThemisAuthorizationDecision decision = ThemisAuthorizationDecision.allow("provider-registry-read-policy", "corr-audit-001");

        ThemisAuditEvent event = auditService.recordDecision(request, decision);

        assertThat(event).isNotNull();
        assertThat(event.decision()).isEqualTo(ThemisDecision.ALLOW);
        assertThat(event.principalId()).isEqualTo("user:dr-smith");
        assertThat(event.principalType()).isEqualTo(PrincipalType.HUMAN);
        assertThat(event.action()).isEqualTo(ThemisAction.READ);
        assertThat(event.resourceType()).isEqualTo("Practitioner");
        assertThat(event.resourceId()).isEqualTo("PR-123");
        assertThat(event.securityDomain()).isEqualTo("PROVIDER_REGISTRY");
        assertThat(event.correlationId()).isEqualTo("corr-audit-001");
        assertThat(event.classification()).isEqualTo("AUDIT");

        List<ThemisAuditEvent> correlated = auditService.getEventsByCorrelationId("corr-audit-001");
        assertThat(correlated).hasSize(1);
    }

    @Test
    @DisplayName("Records and correlates DENY security decision without PHI")
    void testRecordDenyDecision() {
        ThemisPrincipal principal = ThemisPrincipal.of("user:unauthorized-actor", PrincipalType.HUMAN, "external");
        ThemisResource target = ThemisResource.of("Practitioner", "PR-456", "PROVIDER_REGISTRY", Set.of());
        ThemisSecurityContext context = ThemisSecurityContext.fromPrincipal(principal, "corr-audit-002");

        ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                .principal(principal)
                .action(ThemisAction.SUBMIT_UPDATE)
                .target(target)
                .context(context)
                .build();

        ThemisAuthorizationDecision decision = ThemisAuthorizationDecision.deny(
                ThemisDecisionReason.AUTHORITY_MISSING,
                "provider-registry-submit-policy",
                "corr-audit-002",
                "Missing required authority: provider.change.submit"
        );

        ThemisAuditEvent event = auditService.recordDecision(request, decision);

        assertThat(event).isNotNull();
        assertThat(event.decision()).isEqualTo(ThemisDecision.DENY);
        assertThat(event.reason()).isEqualTo(ThemisDecisionReason.AUTHORITY_MISSING);
        assertThat(event.policyId()).isEqualTo("provider-registry-submit-policy");
        assertThat(event.correlationId()).isEqualTo("corr-audit-002");

        List<ThemisAuditEvent> byPrincipal = auditService.getEventsByPrincipal("user:unauthorized-actor");
        assertThat(byPrincipal).hasSize(1);
    }

    @Test
    @DisplayName("Respects maximum ring buffer capacity")
    void testBufferCapacityLimit() {
        InMemoryThemisAuditService bounded = new InMemoryThemisAuditService(3);

        ThemisPrincipal principal = ThemisPrincipal.of("system:agent", PrincipalType.SYSTEM);
        ThemisResource target = ThemisResource.of("Task", "1", "PROVIDER_REGISTRY", Set.of());

        for (int i = 1; i <= 5; i++) {
            ThemisAuthorizationRequest req = ThemisAuthorizationRequest.builder()
                    .principal(principal)
                    .action(ThemisAction.PROCESS)
                    .target(target)
                    .context(ThemisSecurityContext.fromPrincipal(principal, "corr-" + i))
                    .build();
            ThemisAuthorizationDecision dec = ThemisAuthorizationDecision.allow("test-policy", "corr-" + i);
            bounded.recordDecision(req, dec);
        }

        List<ThemisAuditEvent> recent = bounded.getRecentEvents(10);
        assertThat(recent).hasSize(3);
        assertThat(recent.get(0).correlationId()).isEqualTo("corr-5");
        assertThat(recent.get(1).correlationId()).isEqualTo("corr-4");
        assertThat(recent.get(2).correlationId()).isEqualTo("corr-3");
    }
}
