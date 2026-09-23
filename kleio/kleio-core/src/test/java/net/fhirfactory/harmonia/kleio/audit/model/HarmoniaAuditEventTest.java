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

package net.fhirfactory.harmonia.kleio.audit.model;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("HarmoniaAuditEvent and Value Object Model Tests")
class HarmoniaAuditEventTest {

    private static final String PHI_MARKER = "PATIENT-PHI-MARKER-92831";
    private static final String SECRET_TOKEN = "TOKEN-SECRET-MARKER-81742";
    private static final String SECRET_AUTHORITY = "AUTH-SECRET-TOKEN-81742";

    @Test
    @DisplayName("Enforces mandatory fields in compact constructor")
    void testMandatoryFieldsEnforcement() {
        Instant now = Instant.now();

        // Null eventId
        assertThatThrownBy(() -> new HarmoniaAuditEvent(
                null, now, AuditClassification.SECURITY, AuditAction.READ,
                AuditOutcome.SUCCESS, null, null, null, null, null,
                null, null, null, null, null, null, null
        )).isInstanceOf(NullPointerException.class)
                .hasMessageContaining("eventId");

        // Blank eventId
        assertThatThrownBy(() -> new HarmoniaAuditEvent(
                "   ", now, AuditClassification.SECURITY, AuditAction.READ,
                AuditOutcome.SUCCESS, null, null, null, null, null,
                null, null, null, null, null, null, null
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("eventId");

        // Null recordedAt
        assertThatThrownBy(() -> new HarmoniaAuditEvent(
                "evt-1", null, AuditClassification.SECURITY, AuditAction.READ,
                AuditOutcome.SUCCESS, null, null, null, null, null,
                null, null, null, null, null, null, null
        )).isInstanceOf(NullPointerException.class)
                .hasMessageContaining("recordedAt");

        // Null classification
        assertThatThrownBy(() -> new HarmoniaAuditEvent(
                "evt-1", now, null, AuditAction.READ,
                AuditOutcome.SUCCESS, null, null, null, null, null,
                null, null, null, null, null, null, null
        )).isInstanceOf(NullPointerException.class)
                .hasMessageContaining("classification");

        // Null action
        assertThatThrownBy(() -> new HarmoniaAuditEvent(
                "evt-1", now, AuditClassification.SECURITY, null,
                AuditOutcome.SUCCESS, null, null, null, null, null,
                null, null, null, null, null, null, null
        )).isInstanceOf(NullPointerException.class)
                .hasMessageContaining("action");

        // Null outcome
        assertThatThrownBy(() -> new HarmoniaAuditEvent(
                "evt-1", now, AuditClassification.SECURITY, AuditAction.READ,
                null, null, null, null, null, null,
                null, null, null, null, null, null, null
        )).isInstanceOf(NullPointerException.class)
                .hasMessageContaining("outcome");
    }

    @Test
    @DisplayName("Guarantees immutability and defensive copying for collections and attributes")
    void testImmutabilityAndDefensiveCopies() {
        Set<ThemisAuthority> mutableAuthorities = new HashSet<>();
        mutableAuthorities.add(ThemisAuthority.of("provider.read"));

        Set<ThemisSecurityLabel> mutableLabels = new HashSet<>();
        mutableLabels.add(ThemisSecurityLabel.of("http://example.org/sec", "INTERNAL"));

        Map<String, String> mutableAttributes = new HashMap<>();
        mutableAttributes.put("metaKey", "metaValue");

        HarmoniaAuditEvent event = HarmoniaAuditEvent.builder()
                .eventId("evt-immutable-1")
                .recordedAt(Instant.now())
                .classification(AuditClassification.SECURITY)
                .action(AuditAction.READ)
                .outcome(AuditOutcome.SUCCESS)
                .authorities(mutableAuthorities)
                .securityLabels(mutableLabels)
                .attributes(mutableAttributes)
                .build();

        // Mutating external source collections must not affect the event
        mutableAuthorities.add(ThemisAuthority.of("tamper.authority"));
        mutableLabels.add(ThemisSecurityLabel.of("http://example.org/sec", "TAMPER"));
        mutableAttributes.put("tamperKey", "tamperValue");

        assertThat(event.authorities()).hasSize(1);
        assertThat(event.authorities()).extracting(ThemisAuthority::authorityCode)
                .containsExactly("provider.read");

        assertThat(event.securityLabels()).hasSize(1);
        assertThat(event.attributes()).hasSize(1);
        assertThat(event.attributes()).doesNotContainKey("tamperKey");

        // Returned sets/maps must be unmodifiable
        assertThatThrownBy(() -> event.authorities().add(ThemisAuthority.of("illegal")))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> event.securityLabels().add(ThemisSecurityLabel.of("http://bad", "VAL")))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> event.attributes().put("illegal", "val"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("Distinguishes dual principals correctly")
    void testDualPrincipalDistinction() {
        ThemisPrincipal initiator = ThemisPrincipal.of("user:dr-smith", PrincipalType.HUMAN, "hospital-west");
        ThemisPrincipal executor = ThemisPrincipal.of("service:ponos-worker-01", PrincipalType.SERVICE, "WORKFLOW");

        HarmoniaAuditEvent event = HarmoniaAuditEvent.builder()
                .eventId("evt-dual-1")
                .recordedAt(Instant.now())
                .classification(AuditClassification.CLINICAL)
                .action(AuditAction.EXECUTE)
                .outcome(AuditOutcome.SUCCESS)
                .initiatingPrincipal(initiator)
                .executingPrincipal(executor)
                .build();

        assertThat(event.initiatingPrincipal()).isEqualTo(AuditPrincipal.from(initiator));
        assertThat(event.originatingPrincipal()).isEqualTo(AuditPrincipal.from(initiator));
        assertThat(event.initiatingThemisPrincipal()).isEqualTo(initiator);
        assertThat(event.executingPrincipal()).isEqualTo(AuditPrincipal.from(executor));
        assertThat(event.initiatingPrincipal().principalId()).isEqualTo("user:dr-smith");
        assertThat(event.executingPrincipal().principalId()).isEqualTo("service:ponos-worker-01");
        assertThat(event.initiatingPrincipal().principalType()).isEqualTo(PrincipalType.HUMAN);
        assertThat(event.executingPrincipal().principalType()).isEqualTo(PrincipalType.SERVICE);
        assertThat(event.initiatingPrincipal().sourceDomain()).isEqualTo("hospital-west");
        assertThat(event.executingPrincipal().sourceDomain()).isEqualTo("WORKFLOW");
    }

    @Test
    @DisplayName("toString() suppresses PHI markers, secrets, and raw authority/attribute contents")
    void testSafeStringSuppression() {
        ThemisPrincipal initiator = new ThemisPrincipal(
                "user:dr-jones",
                PrincipalType.HUMAN,
                "CLINICAL",
                Map.of("sessionSecret", SECRET_TOKEN)
        );
        ThemisPrincipal executor = new ThemisPrincipal(
                "service:praxis-engine",
                PrincipalType.SERVICE,
                "WORKFLOW",
                Map.of("serviceSecret", SECRET_TOKEN)
        );

        AuditTarget target = AuditTarget.of("Patient", "PAT-999", "CLINICAL");
        AuditSource source = AuditSource.of("THEMIS", "AUTHORIZATION_ENGINE");
        AuditAuthorizationEvidence evidence = AuditAuthorizationEvidence.of(
                "dec-xyz-123",
                ThemisDecision.ALLOW,
                ThemisDecisionReason.ALLOWED_BY_POLICY,
                "policy-patient-read",
                "Internal debug details with " + PHI_MARKER + " and " + SECRET_TOKEN
        );

        HarmoniaAuditEvent event = HarmoniaAuditEvent.builder()
                .eventId("evt-safe-100")
                .recordedAt(Instant.parse("2026-09-23T10:15:30Z"))
                .classification(AuditClassification.SECURITY)
                .action(AuditAction.READ)
                .outcome(AuditOutcome.SUCCESS)
                .initiatingPrincipal(initiator)
                .executingPrincipal(executor)
                .securityDomain("CLINICAL")
                .target(target)
                .authorizationEvidence(evidence)
                .addAuthority(ThemisAuthority.of(SECRET_AUTHORITY))
                .addSecurityLabel(ThemisSecurityLabel.of("http://example.org/sec", "CLINICAL"))
                .correlationId("corr-safe-001")
                .causationId("cause-safe-002")
                .operationId("op-safe-003")
                .source(source)
                .addAttribute("patientSummary", PHI_MARKER)
                .addAttribute("bearerAuth", SECRET_TOKEN)
                .build();

        String str = event.toString();

        // Structural and safe operational metadata must be present
        assertThat(str).contains("HarmoniaAuditEvent[");
        assertThat(str).contains("eventId=evt-safe-100");
        assertThat(str).contains("recordedAt=2026-09-23T10:15:30Z");
        assertThat(str).contains("classification=SECURITY");
        assertThat(str).contains("action=READ");
        assertThat(str).contains("outcome=SUCCESS");
        assertThat(str).contains("securityDomain=CLINICAL");
        assertThat(str).contains("correlationId=corr-safe-001");
        assertThat(str).contains("causationId=cause-safe-002");
        assertThat(str).contains("operationId=op-safe-003");
        assertThat(str).contains("authoritiesCount=1");
        assertThat(str).contains("securityLabelsCount=1");
        assertThat(str).contains("attributeCount=2");

        // Nested safe representations: bounded AuditPrincipal without attribute count
        assertThat(str).contains("AuditPrincipal[principalId=user:dr-jones, principalType=HUMAN, sourceDomain=CLINICAL]");
        assertThat(str).contains("AuditPrincipal[principalId=service:praxis-engine, principalType=SERVICE, sourceDomain=WORKFLOW]");
        assertThat(str).contains("AuditTarget[resourceType=Patient, resourceId=PAT-999, securityDomain=CLINICAL]");
        assertThat(str).contains("AuditSource[subsystem=THEMIS, component=AUTHORIZATION_ENGINE]");
        assertThat(str).contains("AuditAuthorizationEvidence[decisionId=dec-xyz-123, decision=ALLOW, reason=ALLOWED_BY_POLICY, policyId=policy-patient-read, hasMessage=true]");

        // Verify principal attributes never enter the audit event
        assertThat(event.initiatingPrincipal()).isEqualTo(AuditPrincipal.of("user:dr-jones", PrincipalType.HUMAN, "CLINICAL"));
        assertThat(event.executingPrincipal()).isEqualTo(AuditPrincipal.of("service:praxis-engine", PrincipalType.SERVICE, "WORKFLOW"));

        // Absolute suppression of sensitive markers, authority codes, and attribute keys/values
        assertThat(str).doesNotContain(PHI_MARKER);
        assertThat(str).doesNotContain(SECRET_TOKEN);
        assertThat(str).doesNotContain(SECRET_AUTHORITY);
        assertThat(str).doesNotContain("patientSummary");
        assertThat(str).doesNotContain("bearerAuth");
        assertThat(str).doesNotContain("sessionSecret");
        assertThat(str).doesNotContain("serviceSecret");
        assertThat(str).doesNotContain("Internal debug details");

        // Check standalone nested value objects toString() as well
        assertThat(evidence.toString()).doesNotContain(PHI_MARKER);
        assertThat(evidence.toString()).doesNotContain(SECRET_TOKEN);
        assertThat(target.toString()).contains("Patient", "PAT-999", "CLINICAL");
        assertThat(source.toString()).contains("THEMIS", "AUTHORIZATION_ENGINE");
    }

    @Test
    @DisplayName("fromDecision preserves trusted context without fabricating identities or identifiers")
    void testFromDecisionPreservesContextWithoutFabrication() {
        ThemisPrincipal principal = ThemisPrincipal.of("user:dr-alice", PrincipalType.HUMAN, "hospital-east");
        ThemisResource target = ThemisResource.of("Observation", "OBS-456", "CLINICAL",
                Set.of(ThemisSecurityLabel.of("http://example.org/sec", "RESTRICTED")));

        ThemisSecurityContext context = ThemisSecurityContext.builder()
                .requestingPrincipal(principal)
                .securityDomain("CLINICAL")
                .correlationId("corr-fact-001")
                .addAuthority("clinical.read")
                .build();

        ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                .principal(principal)
                .action(ThemisAction.READ)
                .target(target)
                .context(context)
                .authorities(Set.of(ThemisAuthority.of("provider.read")))
                .build();

        Instant decisionTime = Instant.parse("2026-09-23T11:00:00Z");
        ThemisAuthorizationDecision decision = new ThemisAuthorizationDecision(
                "dec-987",
                ThemisDecision.ALLOW,
                ThemisDecisionReason.ALLOWED_BY_POLICY,
                "clinical-observation-read-policy",
                decisionTime,
                "corr-fact-001",
                "Access granted"
        );

        HarmoniaAuditEvent event = HarmoniaAuditEvent.fromDecision("evt-dec-1", request, decision);

        assertThat(event.eventId()).isEqualTo("evt-dec-1");
        assertThat(event.recordedAt()).isEqualTo(decisionTime);
        assertThat(event.classification()).isEqualTo(AuditClassification.SECURITY);
        assertThat(event.action()).isEqualTo(AuditAction.READ);
        assertThat(event.outcome()).isEqualTo(AuditOutcome.SUCCESS);
        assertThat(event.initiatingPrincipal()).isEqualTo(AuditPrincipal.from(principal));
        assertThat(event.initiatingThemisPrincipal()).isEqualTo(principal);
        assertThat(event.securityDomain()).isEqualTo("CLINICAL");
        assertThat(event.correlationId()).isEqualTo("corr-fact-001");

        // Target mapped
        assertThat(event.target()).isNotNull();
        assertThat(event.target().resourceType()).isEqualTo("Observation");
        assertThat(event.target().resourceId()).isEqualTo("OBS-456");
        assertThat(event.target().securityDomain()).isEqualTo("CLINICAL");

        // Authorization evidence mapped
        assertThat(event.authorizationEvidence()).isNotNull();
        assertThat(event.authorizationEvidence().decisionId()).isEqualTo("dec-987");
        assertThat(event.authorizationEvidence().decision()).isEqualTo(ThemisDecision.ALLOW);
        assertThat(event.authorizationEvidence().reason()).isEqualTo(ThemisDecisionReason.ALLOWED_BY_POLICY);
        assertThat(event.authorizationEvidence().policyId()).isEqualTo("clinical-observation-read-policy");
        assertThat(event.authorizationEvidence().message()).isEqualTo("Access granted");

        // Authorities snapshot combined from request and context
        assertThat(event.authorities()).extracting(ThemisAuthority::authorityCode)
                .containsExactlyInAnyOrder("clinical.read", "provider.read");

        // Security labels preserved
        assertThat(event.securityLabels()).hasSize(1);

        // Crucial invariant: No fabricated executing principal, causationId, or operationId
        assertThat(event.executingPrincipal()).isNull();
        assertThat(event.causationId()).isNull();
        assertThat(event.operationId()).isNull();
    }

    @Test
    @DisplayName("fromDecision maps executingPrincipal and causationId when explicitly present in context")
    void testFromDecisionPreservesContextProvenanceWhenPresent() {
        ThemisPrincipal initiator = ThemisPrincipal.of("user:dr-bob", PrincipalType.HUMAN, "hospital-north");
        ThemisPrincipal delegator = ThemisPrincipal.of("service:worker", PrincipalType.SERVICE, "WORKFLOW");

        ThemisSecurityContext context = ThemisSecurityContext.builder()
                .requestingPrincipal(initiator)
                .executingPrincipal(delegator)
                .correlationId("corr-prov-100")
                .causationId("cause-prov-200")
                .securityDomain("OPERATIONS")
                .build();

        ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                .principal(initiator)
                .action(ThemisAction.PROCESS)
                .target(ThemisResource.of("Task", "TSK-001", "OPERATIONS", Set.of()))
                .context(context)
                .build();

        ThemisAuthorizationDecision decision = ThemisAuthorizationDecision.allow("task-policy", "corr-prov-100");

        HarmoniaAuditEvent event = HarmoniaAuditEvent.fromDecision(request, decision);

        assertThat(event.eventId()).isNotBlank();
        assertThat(event.initiatingPrincipal()).isEqualTo(AuditPrincipal.from(initiator));
        assertThat(event.initiatingThemisPrincipal()).isEqualTo(initiator);
        assertThat(event.executingPrincipal()).isEqualTo(AuditPrincipal.from(delegator));
        assertThat(event.correlationId()).isEqualTo("corr-prov-100");
        assertThat(event.causationId()).isEqualTo("cause-prov-200");
        assertThat(event.operationId()).isNull(); // Still not fabricated
    }

    @Test
    @DisplayName("fromDecision maps DENIED decision and reason correctly")
    void testFromDecisionMapsDeniedOutcome() {
        ThemisPrincipal initiator = ThemisPrincipal.of("user:attacker", PrincipalType.HUMAN);
        ThemisSecurityContext context = ThemisSecurityContext.fromPrincipal(initiator, "corr-deny-999");

        ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                .principal(initiator)
                .action(ThemisAction.DELETE)
                .target(ThemisResource.of("DiagnosticReport", "DR-123"))
                .context(context)
                .build();

        ThemisAuthorizationDecision decision = ThemisAuthorizationDecision.deny(
                ThemisDecisionReason.AUTHORITY_MISSING,
                "report-delete-policy",
                "corr-deny-999",
                "Missing required delete authority"
        );

        HarmoniaAuditEvent event = HarmoniaAuditEvent.fromDecision(request, decision);

        assertThat(event.action()).isEqualTo(AuditAction.DELETE);
        assertThat(event.outcome()).isEqualTo(AuditOutcome.DENIED);
        assertThat(event.authorizationEvidence().decision()).isEqualTo(ThemisDecision.DENY);
        assertThat(event.authorizationEvidence().reason()).isEqualTo(ThemisDecisionReason.AUTHORITY_MISSING);
        assertThat(event.authorizationEvidence().policyId()).isEqualTo("report-delete-policy");
        assertThat(event.decision()).isEqualTo(ThemisDecision.DENY);
        assertThat(event.reason()).isEqualTo(ThemisDecisionReason.AUTHORITY_MISSING);
    }

    @Test
    @DisplayName("AuditAction mapping correctly translates all ThemisAction values")
    void testAuditActionMapping() {
        assertThat(AuditAction.fromThemisAction(ThemisAction.READ)).isEqualTo(AuditAction.READ);
        assertThat(AuditAction.fromThemisAction(ThemisAction.SEARCH)).isEqualTo(AuditAction.SEARCH);
        assertThat(AuditAction.fromThemisAction(ThemisAction.CREATE)).isEqualTo(AuditAction.CREATE);
        assertThat(AuditAction.fromThemisAction(ThemisAction.SUBMIT_CREATE)).isEqualTo(AuditAction.CREATE);
        assertThat(AuditAction.fromThemisAction(ThemisAction.UPDATE)).isEqualTo(AuditAction.UPDATE);
        assertThat(AuditAction.fromThemisAction(ThemisAction.SUBMIT_UPDATE)).isEqualTo(AuditAction.UPDATE);
        assertThat(AuditAction.fromThemisAction(ThemisAction.DELETE)).isEqualTo(AuditAction.DELETE);
        assertThat(AuditAction.fromThemisAction(ThemisAction.EXECUTE)).isEqualTo(AuditAction.EXECUTE);
        assertThat(AuditAction.fromThemisAction(ThemisAction.PROCESS)).isEqualTo(AuditAction.EXECUTE);
        assertThat(AuditAction.fromThemisAction(ThemisAction.APPROVE)).isEqualTo(AuditAction.EXECUTE);
        assertThat(AuditAction.fromThemisAction(ThemisAction.REJECT)).isEqualTo(AuditAction.EXECUTE);
        assertThat(AuditAction.fromThemisAction(ThemisAction.REPLAY)).isEqualTo(AuditAction.EXECUTE);
        assertThat(AuditAction.fromThemisAction(ThemisAction.ADMINISTER)).isEqualTo(AuditAction.EXECUTE);
        assertThat(AuditAction.fromThemisAction(null)).isNull();
    }

    @Test
    @DisplayName("AuditOutcome mapping correctly translates ThemisDecision values")
    void testAuditOutcomeMapping() {
        assertThat(AuditOutcome.fromDecision(ThemisDecision.ALLOW)).isEqualTo(AuditOutcome.SUCCESS);
        assertThat(AuditOutcome.fromDecision(ThemisDecision.DENY)).isEqualTo(AuditOutcome.DENIED);
        assertThat(AuditOutcome.fromDecision(null)).isNull();
    }

    @Test
    @DisplayName("HarmoniaAuditEvent supports Java serialization and deserialization")
    void testSerializationPreserved() throws IOException, ClassNotFoundException {
        HarmoniaAuditEvent event = HarmoniaAuditEvent.builder()
                .eventId("evt-ser-1")
                .recordedAt(Instant.parse("2026-09-23T12:00:00Z"))
                .classification(AuditClassification.SECURITY)
                .action(AuditAction.READ)
                .outcome(AuditOutcome.SUCCESS)
                .initiatingPrincipal(ThemisPrincipal.service("svc:test"))
                .addAuthority(ThemisAuthority.of("auth.test"))
                .addAttribute("auditTag", "verified")
                .build();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(event);
        }

        HarmoniaAuditEvent deserialized;
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            deserialized = (HarmoniaAuditEvent) ois.readObject();
        }

        assertThat(deserialized).isEqualTo(event);
        assertThat(deserialized.hashCode()).isEqualTo(event.hashCode());
        assertThat(deserialized.toString()).isEqualTo(event.toString());
        assertThat(deserialized.eventId()).isEqualTo("evt-ser-1");
        assertThat(deserialized.classification()).isEqualTo(AuditClassification.SECURITY);
        assertThat(deserialized.authorities()).hasSize(1);
        assertThat(deserialized.attributes()).containsEntry("auditTag", "verified");
    }

    @Test
    @DisplayName("Fluent builder and toBuilder work as expected with backward compatibility accessors")
    void testFluentBuilderAndAccessors() {
        Instant timestamp = Instant.parse("2026-09-23T13:00:00Z");

        HarmoniaAuditEvent event = HarmoniaAuditEvent.builder()
                .eventId("evt-builder-1")
                .timestamp(timestamp)
                .classification("SECURITY")
                .action(ThemisAction.READ) // ThemisAction conversion
                .decisionId("dec-legacy-1")
                .decision(ThemisDecision.ALLOW)
                .reason(ThemisDecisionReason.ALLOWED_BY_POLICY)
                .policyId("policy-legacy")
                .principalId("user:legacy")
                .principalType(PrincipalType.HUMAN)
                .sourceDomain("LEGACY_DOMAIN")
                .resourceType("Patient")
                .resourceId("P-123")
                .securityDomain("CLINICAL")
                .correlationId("corr-legacy")
                .causationId("cause-legacy")
                .operationId("op-legacy")
                .build();

        // Verify backward compatibility accessors
        assertThat(event.eventId()).isEqualTo("evt-builder-1");
        assertThat(event.timestamp()).isEqualTo(timestamp);
        assertThat(event.recordedAt()).isEqualTo(timestamp);
        assertThat(event.classification()).isEqualTo(AuditClassification.SECURITY);
        assertThat(event.action()).isEqualTo(AuditAction.READ);
        assertThat(event.outcome()).isEqualTo(AuditOutcome.SUCCESS);
        assertThat(event.decisionId()).isEqualTo("dec-legacy-1");
        assertThat(event.decision()).isEqualTo(ThemisDecision.ALLOW);
        assertThat(event.reason()).isEqualTo(ThemisDecisionReason.ALLOWED_BY_POLICY);
        assertThat(event.policyId()).isEqualTo("policy-legacy");
        assertThat(event.principalId()).isEqualTo("user:legacy");
        assertThat(event.principalType()).isEqualTo(PrincipalType.HUMAN);
        assertThat(event.sourceDomain()).isEqualTo("LEGACY_DOMAIN");
        assertThat(event.resourceType()).isEqualTo("Patient");
        assertThat(event.resourceId()).isEqualTo("P-123");
        assertThat(event.securityDomain()).isEqualTo("CLINICAL");
        assertThat(event.correlationId()).isEqualTo("corr-legacy");
        assertThat(event.causationId()).isEqualTo("cause-legacy");
        assertThat(event.operationId()).isEqualTo("op-legacy");

        // Verify toBuilder roundtrip
        HarmoniaAuditEvent evolved = event.toBuilder()
                .eventId("evt-builder-2")
                .outcome(AuditOutcome.DENIED)
                .build();

        assertThat(evolved.eventId()).isEqualTo("evt-builder-2");
        assertThat(evolved.outcome()).isEqualTo(AuditOutcome.DENIED);
        assertThat(evolved.principalId()).isEqualTo("user:legacy");
        assertThat(evolved.resourceType()).isEqualTo("Patient");
    }

    @Test
    @DisplayName("Builder string classification strictly validates valid enum names and rejects invalid names including legacy AUDIT")
    void testClassificationParsingStrictValidation() {
        // Valid classifications across different casing
        assertThat(HarmoniaAuditEvent.builder().classification("SECURITY").build().classification())
                .isEqualTo(AuditClassification.SECURITY);
        assertThat(HarmoniaAuditEvent.builder().classification("clinical").build().classification())
                .isEqualTo(AuditClassification.CLINICAL);
        assertThat(HarmoniaAuditEvent.builder().classification("  WORKFLOW  ").build().classification())
                .isEqualTo(AuditClassification.WORKFLOW);
        assertThat(HarmoniaAuditEvent.builder().classification("Integration").build().classification())
                .isEqualTo(AuditClassification.INTEGRATION);
        assertThat(HarmoniaAuditEvent.builder().classification("ADMINISTRATION").build().classification())
                .isEqualTo(AuditClassification.ADMINISTRATION);
        assertThat(HarmoniaAuditEvent.builder().classification("system").build().classification())
                .isEqualTo(AuditClassification.SYSTEM);

        // Null string sets classification to null in builder, default SECURITY applied at build()
        assertThat(HarmoniaAuditEvent.builder().classification((String) null).build().classification())
                .isEqualTo(AuditClassification.SECURITY);

        // Legacy "AUDIT" string must be strictly rejected (not silently defaulted to SECURITY)
        assertThatThrownBy(() -> HarmoniaAuditEvent.builder().classification("AUDIT"))
                .isInstanceOf(IllegalArgumentException.class);

        // Arbitrary unknown string must be rejected
        assertThatThrownBy(() -> HarmoniaAuditEvent.builder().classification("UNKNOWN"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> HarmoniaAuditEvent.builder().classification("INVALID"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> HarmoniaAuditEvent.builder().classification(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Validates eventId boundary conditions against FHIR logical-ID pattern")
    void testEventIdBoundaryValidation() {
        Instant now = Instant.now();

        // Valid boundaries: 1 character, 64 characters, UUID, mixed alphanumeric with hyphens and dots
        String singleChar = "a";
        String maxChars = "a".repeat(64);
        String uuidStr = UUID.randomUUID().toString();
        String mixedChars = "Event-123.456-ABC";
        String numericOnly = "1234567890";
        String dotHyphen = "a.b-c.1-2";

        for (String validId : List.of(singleChar, maxChars, uuidStr, mixedChars, numericOnly, dotHyphen)) {
            HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                    validId, now, AuditClassification.SECURITY, AuditAction.READ,
                    AuditOutcome.SUCCESS, null, null, null, null, null,
                    null, null, null, null, null, null, null
            );
            assertThat(event.eventId()).isEqualTo(validId);

            HarmoniaAuditEvent fromBuilder = HarmoniaAuditEvent.builder()
                    .eventId(validId)
                    .recordedAt(now)
                    .build();
            assertThat(fromBuilder.eventId()).isEqualTo(validId);
        }

        // Invalid eventId: empty, blank, illegal characters (colons, underscores, slashes, etc.), length > 64
        List<String> invalidIds = List.of(
                "",
                "   ",
                "evt:1",
                "evt_1",
                "evt/1",
                "evt#1",
                "evt@1",
                "evt 1",
                "evt$1",
                "evt!1",
                "evt?1",
                "evt%1",
                "a".repeat(65)
        );

        for (String invalidId : invalidIds) {
            assertThatThrownBy(() -> new HarmoniaAuditEvent(
                    invalidId, now, AuditClassification.SECURITY, AuditAction.READ,
                    AuditOutcome.SUCCESS, null, null, null, null, null,
                    null, null, null, null, null, null, null
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("eventId");

            assertThatThrownBy(() -> HarmoniaAuditEvent.builder()
                    .eventId(invalidId)
                    .build()
            ).isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("eventId");
        }

        // fromDecision rejects invalid eventId
        ThemisPrincipal principal = ThemisPrincipal.of("user:dr-alice", PrincipalType.HUMAN);
        ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
                .principal(principal)
                .action(ThemisAction.READ)
                .target(ThemisResource.of("Observation", "OBS-1"))
                .build();
        ThemisAuthorizationDecision decision = ThemisAuthorizationDecision.allow("policy-1", "corr-1");

        assertThatThrownBy(() -> HarmoniaAuditEvent.fromDecision("invalid_id", request, decision))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("eventId");

        assertThatThrownBy(() -> HarmoniaAuditEvent.fromDecision("invalid:id", request, decision))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("eventId");

        assertThatThrownBy(() -> HarmoniaAuditEvent.fromDecision("a".repeat(65), request, decision))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("eventId");
    }

    @Test
    @DisplayName("AuditPrincipal bounds principal representation and excludes arbitrary attributes from equality")
    void testBoundedPrincipalInvariants() {
        ThemisPrincipal themisWithAttrs1 = new ThemisPrincipal(
                "user:dr-alice",
                PrincipalType.HUMAN,
                "CLINICAL",
                Map.of("sessionToken", "TOKEN-SECRET-MARKER-81742", "ip", "10.0.0.1")
        );
        ThemisPrincipal themisWithAttrs2 = new ThemisPrincipal(
                "user:dr-alice",
                PrincipalType.HUMAN,
                "CLINICAL",
                Map.of("sessionToken", "DIFFERENT-TOKEN", "customRole", "SPECIAL")
        );
        ThemisPrincipal themisWithoutAttrs = ThemisPrincipal.of("user:dr-alice", PrincipalType.HUMAN, "CLINICAL");

        AuditPrincipal ap1 = AuditPrincipal.from(themisWithAttrs1);
        AuditPrincipal ap2 = AuditPrincipal.from(themisWithAttrs2);
        AuditPrincipal ap3 = AuditPrincipal.from(themisWithoutAttrs);
        AuditPrincipal apDirect = AuditPrincipal.of("user:dr-alice", PrincipalType.HUMAN, "CLINICAL");

        // AuditPrincipal value equality ignores the source attributes
        assertThat(ap1).isEqualTo(ap2);
        assertThat(ap1).isEqualTo(ap3);
        assertThat(ap1).isEqualTo(apDirect);
        assertThat(ap1.hashCode()).isEqualTo(ap2.hashCode());
        assertThat(ap1.toThemisPrincipal()).isEqualTo(themisWithoutAttrs);
        assertThat(ap1.principalId()).isEqualTo("user:dr-alice");
        assertThat(ap1.principalType()).isEqualTo(PrincipalType.HUMAN);
        assertThat(ap1.sourceDomain()).isEqualTo("CLINICAL");

        // Factory without domain and null handling
        assertThat(AuditPrincipal.from(null)).isNull();
        AuditPrincipal apNoDomain = AuditPrincipal.of("sys:1", PrincipalType.SYSTEM);
        assertThat(apNoDomain.sourceDomain()).isNull();
        assertThat(apNoDomain.principalId()).isEqualTo("sys:1");
        assertThat(apNoDomain.principalType()).isEqualTo(PrincipalType.SYSTEM);

        // toString format check: does not contain attributeCount
        String apString = ap1.toString();
        assertThat(apString).isEqualTo("AuditPrincipal[principalId=user:dr-alice, principalType=HUMAN, sourceDomain=CLINICAL]");
        assertThat(apString).doesNotContain("attributeCount");
        assertThat(apString).doesNotContain("TOKEN-SECRET-MARKER-81742");

        // Event equality ignores principal attributes
        HarmoniaAuditEvent event1 = HarmoniaAuditEvent.builder()
                .eventId("evt-prin-eq")
                .recordedAt(Instant.parse("2026-09-23T12:00:00Z"))
                .classification(AuditClassification.SECURITY)
                .action(AuditAction.READ)
                .outcome(AuditOutcome.SUCCESS)
                .initiatingPrincipal(themisWithAttrs1)
                .build();

        HarmoniaAuditEvent event2 = HarmoniaAuditEvent.builder()
                .eventId("evt-prin-eq")
                .recordedAt(Instant.parse("2026-09-23T12:00:00Z"))
                .classification(AuditClassification.SECURITY)
                .action(AuditAction.READ)
                .outcome(AuditOutcome.SUCCESS)
                .initiatingPrincipal(themisWithAttrs2)
                .build();

        HarmoniaAuditEvent event3 = HarmoniaAuditEvent.builder()
                .eventId("evt-prin-eq")
                .recordedAt(Instant.parse("2026-09-23T12:00:00Z"))
                .classification(AuditClassification.SECURITY)
                .action(AuditAction.READ)
                .outcome(AuditOutcome.SUCCESS)
                .initiatingPrincipal(apDirect)
                .build();

        // Exact equality across all events despite different input ThemisPrincipal attributes
        assertThat(event1).isEqualTo(event2);
        assertThat(event1).isEqualTo(event3);
        assertThat(event1.hashCode()).isEqualTo(event2.hashCode());
        assertThat(event1.initiatingPrincipal()).isEqualTo(apDirect);
        assertThat(event1.originatingPrincipal()).isEqualTo(apDirect);
        assertThat(event1.initiatingThemisPrincipal()).isEqualTo(themisWithoutAttrs);

        // Builder accepting AuditPrincipal directly
        HarmoniaAuditEvent directEvent = HarmoniaAuditEvent.builder()
                .eventId("evt-audit-prin-1")
                .initiatingPrincipal(AuditPrincipal.of("user:admin", PrincipalType.HUMAN, "security"))
                .executingPrincipal(AuditPrincipal.of("worker:1", PrincipalType.PROCESS, "workflow"))
                .build();
        assertThat(directEvent.initiatingPrincipal().principalId()).isEqualTo("user:admin");
        assertThat(directEvent.executingPrincipal().principalId()).isEqualTo("worker:1");
    }
}
