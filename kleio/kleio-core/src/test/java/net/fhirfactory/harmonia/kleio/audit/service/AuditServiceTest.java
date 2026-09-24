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

package net.fhirfactory.harmonia.kleio.audit.service;

import net.fhirfactory.harmonia.kleio.audit.model.AuditAction;
import net.fhirfactory.harmonia.kleio.audit.model.AuditClassification;
import net.fhirfactory.harmonia.kleio.audit.model.AuditOutcome;
import net.fhirfactory.harmonia.kleio.audit.model.AuditQuery;
import net.fhirfactory.harmonia.kleio.audit.model.AuditTarget;
import net.fhirfactory.harmonia.kleio.audit.model.HarmoniaAuditEvent;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Kleio Audit Service Tests")
class AuditServiceTest {

    private InMemoryAuditService auditService;

    @BeforeEach
    void setUp() {
        auditService = new InMemoryAuditService(100);
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

        HarmoniaAuditEvent event = auditService.recordDecision(request, decision);

        assertThat(event).isNotNull();
        assertThat(event.decision()).isEqualTo(ThemisDecision.ALLOW);
        assertThat(event.outcome()).isEqualTo(AuditOutcome.SUCCESS);
        assertThat(event.principalId()).isEqualTo("user:dr-smith");
        assertThat(event.principalType()).isEqualTo(PrincipalType.HUMAN);
        assertThat(event.action()).isEqualTo(AuditAction.READ);
        assertThat(event.resourceType()).isEqualTo("Practitioner");
        assertThat(event.resourceId()).isEqualTo("PR-123");
        assertThat(event.securityDomain()).isEqualTo("PROVIDER_REGISTRY");
        assertThat(event.correlationId()).isEqualTo("corr-audit-001");
        assertThat(event.classification()).isEqualTo(AuditClassification.SECURITY);

        List<HarmoniaAuditEvent> correlated = auditService.findByCorrelationId("corr-audit-001");
        assertThat(correlated).hasSize(1);
        assertThat(correlated.get(0)).isEqualTo(event);

        // Also verify backward-compatible alias
        assertThat(auditService.getEventsByCorrelationId("corr-audit-001")).hasSize(1);

        Optional<HarmoniaAuditEvent> found = auditService.findById(event.eventId());
        assertThat(found).isPresent().contains(event);
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

        HarmoniaAuditEvent event = auditService.recordDecision(request, decision);

        assertThat(event).isNotNull();
        assertThat(event.decision()).isEqualTo(ThemisDecision.DENY);
        assertThat(event.outcome()).isEqualTo(AuditOutcome.DENIED);
        assertThat(event.reason()).isEqualTo(ThemisDecisionReason.AUTHORITY_MISSING);
        assertThat(event.policyId()).isEqualTo("provider-registry-submit-policy");
        assertThat(event.correlationId()).isEqualTo("corr-audit-002");

        List<HarmoniaAuditEvent> byPrincipal = auditService.findByPrincipal("user:unauthorized-actor");
        assertThat(byPrincipal).hasSize(1);
        assertThat(byPrincipal.get(0)).isEqualTo(event);

        // Also verify backward-compatible alias
        assertThat(auditService.getEventsByPrincipal("user:unauthorized-actor")).hasSize(1);
    }

    @Test
    @DisplayName("Appends custom audit event directly")
    void testAppendNewEvent() {
        HarmoniaAuditEvent event = HarmoniaAuditEvent.builder()
                .eventId("custom-evt-001")
                .recordedAt(Instant.now())
                .classification(AuditClassification.CLINICAL)
                .action(AuditAction.CREATE)
                .outcome(AuditOutcome.SUCCESS)
                .initiatingPrincipal(ThemisPrincipal.human("user:nurse-jackie"))
                .securityDomain("CLINICAL")
                .target(AuditTarget.of("Encounter", "ENC-999", "CLINICAL"))
                .correlationId("corr-custom-001")
                .build();

        HarmoniaAuditEvent appended = auditService.append(event);

        assertThat(appended).isEqualTo(event);
        assertThat(auditService.findById("custom-evt-001")).isPresent().contains(event);
        assertThat(auditService.getRecentEvents(10)).containsExactly(event);
    }

    @Test
    @DisplayName("Idempotent replay on identical event without duplicate log growth")
    void testAppendIdempotentReplay() {
        HarmoniaAuditEvent event = HarmoniaAuditEvent.builder()
                .eventId("idemp-evt-001")
                .recordedAt(Instant.parse("2026-09-23T10:15:30Z"))
                .classification(AuditClassification.SECURITY)
                .action(AuditAction.READ)
                .outcome(AuditOutcome.SUCCESS)
                .initiatingPrincipal(ThemisPrincipal.service("svc:auth"))
                .securityDomain("OPERATIONS")
                .correlationId("corr-idemp-001")
                .build();

        HarmoniaAuditEvent first = auditService.append(event);
        HarmoniaAuditEvent second = auditService.append(event);
        HarmoniaAuditEvent third = auditService.append(event);

        assertThat(first).isEqualTo(event);
        assertThat(second).isEqualTo(event);
        assertThat(third).isEqualTo(event);

        List<HarmoniaAuditEvent> recent = auditService.getRecentEvents(10);
        assertThat(recent).hasSize(1);
        assertThat(recent.get(0)).isEqualTo(event);
    }

    @Test
    @DisplayName("Throws AuditIntegrityException on conflicting eventId with divergent content")
    void testAppendIntegrityViolationThrowsException() {
        Instant now = Instant.now();
        HarmoniaAuditEvent original = HarmoniaAuditEvent.builder()
                .eventId("conflict-evt-001")
                .recordedAt(now)
                .classification(AuditClassification.SECURITY)
                .action(AuditAction.READ)
                .outcome(AuditOutcome.SUCCESS)
                .initiatingPrincipal(ThemisPrincipal.service("svc:auth"))
                .securityDomain("OPERATIONS")
                .correlationId("corr-orig-001")
                .build();

        auditService.append(original);

        HarmoniaAuditEvent divergent = HarmoniaAuditEvent.builder()
                .eventId("conflict-evt-001")
                .recordedAt(now)
                .classification(AuditClassification.SECURITY)
                .action(AuditAction.DELETE) // Divergent action!
                .outcome(AuditOutcome.SUCCESS)
                .initiatingPrincipal(ThemisPrincipal.service("svc:auth"))
                .securityDomain("OPERATIONS")
                .correlationId("corr-orig-001")
                .build();

        assertThatThrownBy(() -> auditService.append(divergent))
                .isInstanceOf(AuditIntegrityException.class)
                .hasMessageContaining("conflict-evt-001")
                .satisfies(ex -> {
                    AuditIntegrityException aie = (AuditIntegrityException) ex;
                    assertThat(aie.getEventId()).isEqualTo("conflict-evt-001");
                    assertThat(aie.eventId()).isEqualTo("conflict-evt-001");
                });

        // Ensure original record remained uncorrupted
        Optional<HarmoniaAuditEvent> stored = auditService.findById("conflict-evt-001");
        assertThat(stored).isPresent();
        assertThat(stored.get().action()).isEqualTo(AuditAction.READ);
    }

    @Test
    @DisplayName("Queries by dual-principal (initiating and executing) and correlation ID")
    void testQueryMethodsAndDualPrincipal() {
        ThemisPrincipal initiator = ThemisPrincipal.human("user:doctor-alice");
        ThemisPrincipal executor = ThemisPrincipal.service("svc:workflow-runner");

        HarmoniaAuditEvent event = HarmoniaAuditEvent.builder()
                .eventId("dual-princ-001")
                .recordedAt(Instant.now())
                .classification(AuditClassification.WORKFLOW)
                .action(AuditAction.EXECUTE)
                .outcome(AuditOutcome.SUCCESS)
                .initiatingPrincipal(initiator)
                .executingPrincipal(executor)
                .securityDomain("CLINICAL")
                .correlationId("corr-dual-001")
                .build();

        auditService.append(event);

        // Find by initiating principal
        List<HarmoniaAuditEvent> byInitiator = auditService.findByPrincipal("user:doctor-alice");
        assertThat(byInitiator).containsExactly(event);

        // Find by executing principal
        List<HarmoniaAuditEvent> byExecutor = auditService.findByPrincipal("svc:workflow-runner");
        assertThat(byExecutor).containsExactly(event);

        // Non-existent principal or null/blank
        assertThat(auditService.findByPrincipal("user:non-existent")).isEmpty();
        assertThat(auditService.findByPrincipal(null)).isEmpty();
        assertThat(auditService.findByPrincipal("   ")).isEmpty();

        // Correlation queries
        assertThat(auditService.findByCorrelationId("corr-dual-001")).containsExactly(event);
        assertThat(auditService.findByCorrelationId("unknown-corr")).isEmpty();
        assertThat(auditService.findByCorrelationId(null)).isEmpty();
        assertThat(auditService.findByCorrelationId("")).isEmpty();

        // ID queries
        assertThat(auditService.findById("dual-princ-001")).contains(event);
        assertThat(auditService.findById("unknown-id")).isEmpty();
        assertThat(auditService.findById(null)).isEmpty();
        assertThat(auditService.findById(" ")).isEmpty();
    }

    @Test
    @DisplayName("Respects maximum ring buffer capacity on new appends while ignoring idempotent replays")
    void testBufferCapacityLimit() {
        InMemoryAuditService bounded = new InMemoryAuditService(3);

        ThemisPrincipal principal = ThemisPrincipal.of("system:agent", PrincipalType.SYSTEM);
        ThemisResource target = ThemisResource.of("Task", "1", "PROVIDER_REGISTRY", Set.of());

        // Fill buffer to capacity (3 events)
        for (int i = 1; i <= 3; i++) {
            ThemisAuthorizationRequest req = ThemisAuthorizationRequest.builder()
                    .principal(principal)
                    .action(ThemisAction.PROCESS)
                    .target(target)
                    .context(ThemisSecurityContext.fromPrincipal(principal, "corr-" + i))
                    .build();
            ThemisAuthorizationDecision dec = ThemisAuthorizationDecision.allow("test-policy", "corr-" + i);
            bounded.recordDecision(req, dec);
        }

        List<HarmoniaAuditEvent> recent = bounded.getRecentEvents(10);
        assertThat(recent).hasSize(3);
        assertThat(recent.get(0).correlationId()).isEqualTo("corr-3");
        assertThat(recent.get(1).correlationId()).isEqualTo("corr-2");
        assertThat(recent.get(2).correlationId()).isEqualTo("corr-1");

        HarmoniaAuditEvent event3 = recent.get(0);
        assertThat(event3.correlationId()).isEqualTo("corr-3");

        // Idempotent replay of existing event3 should NOT evict event1
        bounded.append(event3);
        recent = bounded.getRecentEvents(10);
        assertThat(recent).hasSize(3);
        assertThat(recent.get(2).correlationId()).isEqualTo("corr-1");

        // Now append two genuinely new events: event 4 and event 5
        for (int i = 4; i <= 5; i++) {
            ThemisAuthorizationRequest req = ThemisAuthorizationRequest.builder()
                    .principal(principal)
                    .action(ThemisAction.PROCESS)
                    .target(target)
                    .context(ThemisSecurityContext.fromPrincipal(principal, "corr-" + i))
                    .build();
            ThemisAuthorizationDecision dec = ThemisAuthorizationDecision.allow("test-policy", "corr-" + i);
            bounded.recordDecision(req, dec);
        }

        recent = bounded.getRecentEvents(10);
        assertThat(recent).hasSize(3);
        assertThat(recent.get(0).correlationId()).isEqualTo("corr-5");
        assertThat(recent.get(1).correlationId()).isEqualTo("corr-4");
        assertThat(recent.get(2).correlationId()).isEqualTo("corr-3");

        // Events 1 and 2 should have been evicted
        assertThat(bounded.findByCorrelationId("corr-1")).isEmpty();
        assertThat(bounded.findByCorrelationId("corr-2")).isEmpty();

        // Edge case: limit <= 0 returns empty list
        assertThat(bounded.getRecentEvents(0)).isEmpty();
        assertThat(bounded.getRecentEvents(-5)).isEmpty();
    }

    @Test
    @DisplayName("Clear resets in-memory repository")
    void testClearResetsStorage() {
        HarmoniaAuditEvent event = HarmoniaAuditEvent.builder()
                .eventId("clear-evt-001")
                .recordedAt(Instant.now())
                .classification(AuditClassification.SYSTEM)
                .action(AuditAction.EXECUTE)
                .outcome(AuditOutcome.SUCCESS)
                .initiatingPrincipal(ThemisPrincipal.system("sys:daemon"))
                .securityDomain("OPERATIONS")
                .build();

        auditService.append(event);
        assertThat(auditService.getRecentEvents(10)).hasSize(1);

        auditService.clear();

        assertThat(auditService.getRecentEvents(10)).isEmpty();
        assertThat(auditService.findById("clear-evt-001")).isEmpty();
    }

    @Test
    @DisplayName("Append null event throws NullPointerException")
    void testAppendNullThrowsException() {
        assertThatThrownBy(() -> auditService.append(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("event must not be null");
    }

    @Test
    @DisplayName("recordDecision returns null if request or decision is null")
    void testRecordDecisionNullHandling() {
        ThemisAuthorizationRequest req = ThemisAuthorizationRequest.builder()
                .principal(ThemisPrincipal.human("user:test"))
                .action(ThemisAction.READ)
                .target(ThemisResource.of("Patient", "1", "CLINICAL", Set.of()))
                .build();
        ThemisAuthorizationDecision dec = ThemisAuthorizationDecision.allow("policy-1", "corr-1");

        assertThat(auditService.recordDecision(null, dec)).isNull();
        assertThat(auditService.recordDecision(req, null)).isNull();
    }

    @Test
    @DisplayName("Finds audit events with AuditQuery by eventId, classification, action, outcome")
    void testFindAuditQueryByPredicates() {
        Instant baseTime = Instant.parse("2026-09-24T10:00:00Z");

        HarmoniaAuditEvent event1 = HarmoniaAuditEvent.builder()
                .eventId("query-evt-001")
                .recordedAt(baseTime)
                .classification(AuditClassification.SECURITY)
                .action(AuditAction.AUTHORIZE)
                .outcome(AuditOutcome.SUCCESS)
                .initiatingPrincipal(ThemisPrincipal.human("user:alice"))
                .securityDomain("CLINICAL")
                .correlationId("corr-101")
                .operationId("op-101")
                .build();

        HarmoniaAuditEvent event2 = HarmoniaAuditEvent.builder()
                .eventId("query-evt-002")
                .recordedAt(baseTime.plusSeconds(10))
                .classification(AuditClassification.CLINICAL)
                .action(AuditAction.READ)
                .outcome(AuditOutcome.DENIED)
                .initiatingPrincipal(ThemisPrincipal.system("sys:bot"))
                .target(AuditTarget.of("Patient", "PT-999", "CLINICAL"))
                .securityDomain("CLINICAL")
                .correlationId("corr-102")
                .operationId("op-102")
                .build();

        auditService.append(event1);
        auditService.append(event2);

        // Match by eventId
        List<HarmoniaAuditEvent> byId = auditService.find(AuditQuery.builder().eventId("query-evt-001").build());
        assertThat(byId).containsExactly(event1);

        // Match by classification
        List<HarmoniaAuditEvent> byClass = auditService.find(AuditQuery.builder().classification(AuditClassification.CLINICAL).build());
        assertThat(byClass).containsExactly(event2);

        // Match by action
        List<HarmoniaAuditEvent> byAction = auditService.find(AuditQuery.builder().action(AuditAction.READ).build());
        assertThat(byAction).containsExactly(event2);

        // Match by outcome
        List<HarmoniaAuditEvent> byOutcome = auditService.find(AuditQuery.builder().outcome(AuditOutcome.SUCCESS).build());
        assertThat(byOutcome).containsExactly(event1);

        // Match by principal
        List<HarmoniaAuditEvent> byPrincipal = auditService.find(AuditQuery.builder().principalId("user:alice").build());
        assertThat(byPrincipal).containsExactly(event1);

        // Match by target
        List<HarmoniaAuditEvent> byTarget = auditService.find(AuditQuery.builder().targetId("PT-999").build());
        assertThat(byTarget).containsExactly(event2);

        // Match by correlation and operation
        List<HarmoniaAuditEvent> byCorrOp = auditService.find(AuditQuery.builder()
                .correlationId("corr-101")
                .operationId("op-101")
                .build());
        assertThat(byCorrOp).containsExactly(event1);

        // Point read get alias
        assertThat(auditService.get("query-evt-001")).contains(event1);
        assertThat(auditService.get("missing")).isEmpty();
    }

    @Test
    @DisplayName("Finds audit events with AuditQuery filtering by canonical recordedAt occurrence time")
    void testFindAuditQueryByTimeRange() {
        Instant t1 = Instant.parse("2026-09-24T08:00:00Z");
        Instant t2 = Instant.parse("2026-09-24T09:00:00Z");
        Instant t3 = Instant.parse("2026-09-24T10:00:00Z");

        HarmoniaAuditEvent e1 = HarmoniaAuditEvent.builder()
                .eventId("time-evt-001")
                .recordedAt(t1)
                .classification(AuditClassification.SECURITY)
                .action(AuditAction.EXECUTE)
                .outcome(AuditOutcome.SUCCESS)
                .initiatingPrincipal(ThemisPrincipal.system("sys:daemon"))
                .securityDomain("OPS")
                .build();

        HarmoniaAuditEvent e2 = HarmoniaAuditEvent.builder()
                .eventId("time-evt-002")
                .recordedAt(t2)
                .classification(AuditClassification.SECURITY)
                .action(AuditAction.EXECUTE)
                .outcome(AuditOutcome.SUCCESS)
                .initiatingPrincipal(ThemisPrincipal.system("sys:daemon"))
                .securityDomain("OPS")
                .build();

        HarmoniaAuditEvent e3 = HarmoniaAuditEvent.builder()
                .eventId("time-evt-003")
                .recordedAt(t3)
                .classification(AuditClassification.SECURITY)
                .action(AuditAction.EXECUTE)
                .outcome(AuditOutcome.SUCCESS)
                .initiatingPrincipal(ThemisPrincipal.system("sys:daemon"))
                .securityDomain("OPS")
                .build();

        // Append out of chronological order
        auditService.append(e2);
        auditService.append(e1);
        auditService.append(e3);

        // Filter window [08:30, 09:30]
        List<HarmoniaAuditEvent> window = auditService.find(AuditQuery.builder()
                .startTime(Instant.parse("2026-09-24T08:30:00Z"))
                .endTime(Instant.parse("2026-09-24T09:30:00Z"))
                .build());
        assertThat(window).containsExactly(e2);

        // Open-ended start time [09:00, inf)
        List<HarmoniaAuditEvent> fromT2 = auditService.find(AuditQuery.builder()
                .startTime(t2)
                .build());
        assertThat(fromT2).containsExactly(e3, e2); // recordedAt DESC
    }

    @Test
    @DisplayName("AuditQuery enforces canonical ordering: recordedAt DESC, eventId DESC")
    void testCanonicalOrderingWithEqualTimestamps() {
        Instant commonTimestamp = Instant.parse("2026-09-24T12:00:00Z");

        HarmoniaAuditEvent evtA = HarmoniaAuditEvent.builder()
                .eventId("order-evt-A")
                .recordedAt(commonTimestamp)
                .classification(AuditClassification.SECURITY)
                .action(AuditAction.EXECUTE)
                .outcome(AuditOutcome.SUCCESS)
                .initiatingPrincipal(ThemisPrincipal.system("sys:daemon"))
                .securityDomain("OPS")
                .build();

        HarmoniaAuditEvent evtC = HarmoniaAuditEvent.builder()
                .eventId("order-evt-C")
                .recordedAt(commonTimestamp)
                .classification(AuditClassification.SECURITY)
                .action(AuditAction.EXECUTE)
                .outcome(AuditOutcome.SUCCESS)
                .initiatingPrincipal(ThemisPrincipal.system("sys:daemon"))
                .securityDomain("OPS")
                .build();

        HarmoniaAuditEvent evtB = HarmoniaAuditEvent.builder()
                .eventId("order-evt-B")
                .recordedAt(commonTimestamp)
                .classification(AuditClassification.SECURITY)
                .action(AuditAction.EXECUTE)
                .outcome(AuditOutcome.SUCCESS)
                .initiatingPrincipal(ThemisPrincipal.system("sys:daemon"))
                .securityDomain("OPS")
                .build();

        auditService.append(evtA);
        auditService.append(evtC);
        auditService.append(evtB);

        List<HarmoniaAuditEvent> results = auditService.find(AuditQuery.builder()
                .startTime(commonTimestamp)
                .endTime(commonTimestamp)
                .build());

        // Secondary tie breaker: eventId DESC -> C, B, A
        assertThat(results).containsExactly(evtC, evtB, evtA);
    }

    @Test
    @DisplayName("AuditQuery limit bounding and clamping")
    void testAuditQueryLimitBounding() {
        AuditQuery defaultQuery = AuditQuery.builder().build();
        assertThat(defaultQuery.limit()).isEqualTo(AuditQuery.DEFAULT_LIMIT);

        AuditQuery negativeQuery = AuditQuery.builder().limit(-10).build();
        assertThat(negativeQuery.limit()).isEqualTo(AuditQuery.DEFAULT_LIMIT);

        AuditQuery zeroQuery = AuditQuery.builder().limit(0).build();
        assertThat(zeroQuery.limit()).isEqualTo(AuditQuery.DEFAULT_LIMIT);

        AuditQuery excessiveQuery = AuditQuery.builder().limit(5000).build();
        assertThat(excessiveQuery.limit()).isEqualTo(AuditQuery.MAX_LIMIT);

        AuditQuery customQuery = AuditQuery.builder().limit(15).build();
        assertThat(customQuery.limit()).isEqualTo(15);
    }
}
