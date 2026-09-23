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

package net.fhirfactory.harmonia.kleio.fhir.mapper;

import net.fhirfactory.harmonia.kleio.audit.model.AuditAction;
import net.fhirfactory.harmonia.kleio.audit.model.AuditAuthorizationEvidence;
import net.fhirfactory.harmonia.kleio.audit.model.AuditClassification;
import net.fhirfactory.harmonia.kleio.audit.model.AuditOutcome;
import net.fhirfactory.harmonia.kleio.audit.model.AuditPrincipal;
import net.fhirfactory.harmonia.kleio.audit.model.AuditSource;
import net.fhirfactory.harmonia.kleio.audit.model.AuditTarget;
import net.fhirfactory.harmonia.kleio.audit.model.HarmoniaAuditEvent;
import net.fhirfactory.harmonia.kleio.fhir.constants.HarmoniaAuditFhirConstants;
import net.fhirfactory.harmonia.kleio.fhir.exception.HarmoniaAuditMappingException;
import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecisionReason;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityLabel;
import org.hl7.fhir.r5.model.AuditEvent;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Extension;
import org.hl7.fhir.r5.model.Identifier;
import org.hl7.fhir.r5.model.IntegerType;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.UriType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("HarmoniaAuditEventMapper Bidirectional Round-Trip Tests")
class HarmoniaAuditEventMapperTest {

    private HarmoniaAuditEventMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new HarmoniaAuditEventMapper();
    }

    @Test
    @DisplayName("Scenario 1: Minimal Event Round-Trip with only mandatory fields")
    void testScenario01_MinimalEventRoundTrip() {
        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-min-1",
                Instant.parse("2026-09-24T07:44:00Z"),
                AuditClassification.SECURITY,
                AuditAction.READ,
                AuditOutcome.SUCCESS,
                null, null, null, null, null,
                null, null, null, null, null, null, null
        );

        AuditEvent fhir = mapper.toFhir(event);
        assertThat(fhir).isNotNull();
        assertThat(fhir.getIdPart()).isEqualTo("evt-min-1");

        HarmoniaAuditEvent reconstructed = mapper.fromFhir(fhir);
        assertThat(reconstructed).isEqualTo(event);
        assertThat(reconstructed.hashCode()).isEqualTo(event.hashCode());
    }

    @Test
    @DisplayName("Scenario 2: Fully Populated Event Round-Trip with all 17 canonical fields")
    void testScenario02_FullyPopulatedEventRoundTrip() {
        Instant recordedAt = Instant.parse("2026-09-24T07:44:00.123456789Z");
        AuditPrincipal initiator = AuditPrincipal.of("user:dr-alice", PrincipalType.HUMAN, "hospital-east");
        AuditPrincipal executor = AuditPrincipal.of("service:pipeline", PrincipalType.SERVICE, "integration-hub");
        AuditTarget target = AuditTarget.of("Patient", "PAT-100", "CLINICAL");
        AuditAuthorizationEvidence evidence = AuditAuthorizationEvidence.of(
                "dec-001",
                ThemisDecision.ALLOW,
                ThemisDecisionReason.ALLOWED_BY_POLICY,
                "patient-create-policy",
                "Granted by policy"
        );
        Set<ThemisAuthority> authorities = Set.of(
                ThemisAuthority.of("patient.create"),
                ThemisAuthority.of("clinical.write")
        );
        Set<ThemisSecurityLabel> securityLabels = Set.of(
                ThemisSecurityLabel.of("http://harmonia.fhirfactory.net/security/labels", "RESTRICTED"),
                ThemisSecurityLabel.of("http://example.org/sensitivity", "VERY_HIGH")
        );
        AuditSource source = AuditSource.of("PYLAI", "MLLP_INGRESS");
        Map<String, String> attributes = Map.of(
                "tenantId", "T-01",
                "channel", "mllp",
                "retryCount", "0"
        );

        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-full-123",
                recordedAt,
                AuditClassification.CLINICAL,
                AuditAction.CREATE,
                AuditOutcome.SUCCESS,
                initiator,
                executor,
                "CLINICAL",
                target,
                evidence,
                authorities,
                securityLabels,
                "corr-999",
                "cause-888",
                "op-777",
                source,
                attributes
        );

        AuditEvent fhir = mapper.toFhir(event);
        HarmoniaAuditEvent reconstructed = mapper.fromFhir(fhir);

        assertThat(reconstructed).isEqualTo(event);
        assertThat(reconstructed.hashCode()).isEqualTo(event.hashCode());
        assertThat(reconstructed.recordedAt()).isEqualTo(recordedAt);
        assertThat(reconstructed.initiatingPrincipal()).isEqualTo(initiator);
        assertThat(reconstructed.executingPrincipal()).isEqualTo(executor);
        assertThat(reconstructed.target()).isEqualTo(target);
        assertThat(reconstructed.authorizationEvidence()).isEqualTo(evidence);
        assertThat(reconstructed.authorities()).containsExactlyInAnyOrderElementsOf(authorities);
        assertThat(reconstructed.securityLabels()).containsExactlyInAnyOrderElementsOf(securityLabels);
        assertThat(reconstructed.attributes()).isEqualTo(attributes);
    }

    @Test
    @DisplayName("Scenario 3: Agent Distinction & Permutations (initiator-only, executor-only, reversed list)")
    void testScenario03_AgentDistinctionAndPermutations() {
        AuditPrincipal initiator = AuditPrincipal.of("user:dr-alice", PrincipalType.HUMAN, "hospital-east");
        AuditPrincipal executor = AuditPrincipal.of("service:worker", PrincipalType.SERVICE, "backend");

        // Initiator only
        HarmoniaAuditEvent initiatorOnly = new HarmoniaAuditEvent(
                "evt-init-only",
                Instant.now(),
                AuditClassification.SECURITY,
                AuditAction.SEARCH,
                AuditOutcome.SUCCESS,
                initiator,
                null,
                null, null, null, null, null, null, null, null, null, null
        );
        HarmoniaAuditEvent reconInit = mapper.fromFhir(mapper.toFhir(initiatorOnly));
        assertThat(reconInit.initiatingPrincipal()).isEqualTo(initiator);
        assertThat(reconInit.executingPrincipal()).isNull();
        assertThat(reconInit).isEqualTo(initiatorOnly);

        // Executor only
        HarmoniaAuditEvent executorOnly = new HarmoniaAuditEvent(
                "evt-exec-only",
                Instant.now(),
                AuditClassification.SECURITY,
                AuditAction.EXECUTE,
                AuditOutcome.SUCCESS,
                null,
                executor,
                null, null, null, null, null, null, null, null, null, null
        );
        HarmoniaAuditEvent reconExec = mapper.fromFhir(mapper.toFhir(executorOnly));
        assertThat(reconExec.initiatingPrincipal()).isNull();
        assertThat(reconExec.executingPrincipal()).isEqualTo(executor);
        assertThat(reconExec).isEqualTo(executorOnly);

        // Dual agents with swapped list order
        HarmoniaAuditEvent dualEvent = new HarmoniaAuditEvent(
                "evt-dual-agents",
                Instant.now(),
                AuditClassification.SECURITY,
                AuditAction.UPDATE,
                AuditOutcome.SUCCESS,
                initiator,
                executor,
                null, null, null, null, null, null, null, null, null, null
        );
        AuditEvent fhir = mapper.toFhir(dualEvent);
        assertThat(fhir.getAgent()).hasSize(2);

        // Swap agent list positions: [executor, initiator]
        List<AuditEvent.AuditEventAgentComponent> reversedAgents = new ArrayList<>(fhir.getAgent());
        Collections.reverse(reversedAgents);
        fhir.setAgent(reversedAgents);

        HarmoniaAuditEvent reconSwapped = mapper.fromFhir(fhir);
        assertThat(reconSwapped).isEqualTo(dualEvent);
        assertThat(reconSwapped.initiatingPrincipal()).isEqualTo(initiator);
        assertThat(reconSwapped.executingPrincipal()).isEqualTo(executor);
    }

    @Test
    @DisplayName("Scenario 4: Target Labels & Persistence Metadata Immunity")
    void testScenario04_TargetLabelsAndPersistenceMetadataImmunity() {
        AuditTarget target = AuditTarget.of("DiagnosticReport", "DR-500", "CLINICAL");
        Set<ThemisSecurityLabel> targetLabels = Set.of(
                ThemisSecurityLabel.of("http://harmonia.fhirfactory.net/security/labels", "RESTRICTED"),
                ThemisSecurityLabel.of("http://example.org/sec", "CONFIDENTIAL")
        );

        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-meta-immunity",
                Instant.now(),
                AuditClassification.CLINICAL,
                AuditAction.READ,
                AuditOutcome.SUCCESS,
                null, null, null,
                target,
                null, null,
                targetLabels,
                null, null, null, null, null
        );

        AuditEvent fhir = mapper.toFhir(event);

        // Inject repository persistence metadata into AuditEvent
        fhir.getMeta().setVersionId("42");
        fhir.getMeta().setLastUpdated(new Date());
        fhir.getMeta().addSecurity(new Coding()
                .setSystem("http://example.org/repo-tag")
                .setCode("AUDIT_LOG_RECORD"));
        fhir.getMeta().addSecurity(new Coding()
                .setSystem(HarmoniaAuditFhirConstants.SYSTEM_SECURITY_LABEL)
                .setCode("SHOULD_NOT_LEAK"));

        HarmoniaAuditEvent reconstructed = mapper.fromFhir(fhir);

        // Ensure reconstructed event is identical and meta.security never pollutes securityLabels
        assertThat(reconstructed).isEqualTo(event);
        assertThat(reconstructed.securityLabels()).containsExactlyInAnyOrderElementsOf(targetLabels);
        assertThat(reconstructed.securityLabels())
                .extracting(ThemisSecurityLabel::code)
                .doesNotContain("AUDIT_LOG_RECORD", "SHOULD_NOT_LEAK");
    }

    @Test
    @DisplayName("Scenario 5: Set and Coding Order Independence")
    void testScenario05_SetAndCodingOrderIndependence() {
        AuditPrincipal initiator = AuditPrincipal.of("user:dr-alice", PrincipalType.HUMAN, "hospital-east");
        AuditTarget target = AuditTarget.of("Observation", "OBS-1", "CLINICAL");
        AuditAuthorizationEvidence evidence = AuditAuthorizationEvidence.of(
                "dec-999", ThemisDecision.ALLOW, ThemisDecisionReason.ALLOWED_BY_POLICY, "policy-1", "Reason details"
        );
        Set<ThemisAuthority> authorities = Set.of(
                ThemisAuthority.of("auth.alpha"),
                ThemisAuthority.of("auth.beta"),
                ThemisAuthority.of("auth.gamma")
        );
        Set<ThemisSecurityLabel> labels = Set.of(
                ThemisSecurityLabel.of("http://example.org/l1", "L1"),
                ThemisSecurityLabel.of("http://example.org/l2", "L2"),
                ThemisSecurityLabel.of("http://example.org/l3", "L3")
        );

        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-order-indep",
                Instant.now(),
                AuditClassification.SECURITY,
                AuditAction.READ,
                AuditOutcome.SUCCESS,
                initiator, null, null,
                target, evidence,
                authorities, labels,
                null, null, null, null, null
        );

        AuditEvent fhir = mapper.toFhir(event);

        // Shuffle authorities on agent
        AuditEvent.AuditEventAgentComponent agent = fhir.getAgentFirstRep();
        List<CodeableConcept> auths = new ArrayList<>(agent.getAuthorization());
        Collections.reverse(auths);
        agent.setAuthorization(auths);

        // Shuffle labels on target entity
        AuditEvent.AuditEventEntityComponent entity = fhir.getEntityFirstRep();
        List<CodeableConcept> secLabels = new ArrayList<>(entity.getSecurityLabel());
        Collections.reverse(secLabels);
        entity.setSecurityLabel(secLabels);

        // Shuffle detail on outcome
        List<CodeableConcept> details = new ArrayList<>(fhir.getOutcome().getDetail());
        Collections.reverse(details);
        fhir.getOutcome().setDetail(details);

        HarmoniaAuditEvent reconstructed = mapper.fromFhir(fhir);
        assertThat(reconstructed).isEqualTo(event);
        assertThat(reconstructed.authorities()).containsExactlyInAnyOrderElementsOf(authorities);
        assertThat(reconstructed.securityLabels()).containsExactlyInAnyOrderElementsOf(labels);
    }

    @Test
    @DisplayName("Scenario 6: High-precision Instant nanosecond round-trip")
    void testScenario06_NanosecondInstantPrecision() {
        // High-precision instant with 9-digit fractional seconds
        Instant nanoInstant = Instant.parse("2026-09-24T07:44:00.987654321Z");

        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-nano-precision",
                nanoInstant,
                AuditClassification.SYSTEM,
                AuditAction.EXECUTE,
                AuditOutcome.SUCCESS,
                null, null, null, null, null,
                null, null, null, null, null, null, null
        );

        AuditEvent fhir = mapper.toFhir(event);
        assertThat(fhir.getRecordedElement().getValueAsString()).isEqualTo("2026-09-24T07:44:00.987654321Z");

        HarmoniaAuditEvent reconstructed = mapper.fromFhir(fhir);
        assertThat(reconstructed.recordedAt()).isEqualTo(nanoInstant);
        assertThat(reconstructed.recordedAt().getNano()).isEqualTo(987654321);
        assertThat(reconstructed).isEqualTo(event);
    }

    @Test
    @DisplayName("Scenario 7: Omission of Deprecated Extensions (audit-event-id, agent-attribute)")
    void testScenario07_AbsenceOfDeprecatedExtensions() {
        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-no-deprecated",
                Instant.now(),
                AuditClassification.SECURITY,
                AuditAction.READ,
                AuditOutcome.SUCCESS,
                AuditPrincipal.of("user:bob", PrincipalType.HUMAN),
                null, "SEC_DOMAIN",
                AuditTarget.of("Patient", "P1"),
                null, null, null,
                "corr-1", "cause-1", "op-1", null,
                Map.of("key1", "val1")
        );

        AuditEvent fhir = mapper.toFhir(event);

        // Verify top-level extensions
        for (Extension ext : fhir.getExtension()) {
            assertThat(ext.getUrl()).doesNotContain("audit-event-id");
            assertThat(ext.getUrl()).doesNotContain("agent-attribute");
        }

        // Verify agent extensions
        for (AuditEvent.AuditEventAgentComponent agent : fhir.getAgent()) {
            for (Extension ext : agent.getExtension()) {
                assertThat(ext.getUrl()).doesNotContain("agent-attribute");
                assertThat(ext.getUrl()).doesNotContain("audit-event-id");
            }
        }

        // Verify entity extensions
        for (AuditEvent.AuditEventEntityComponent entity : fhir.getEntity()) {
            for (Extension ext : entity.getExtension()) {
                assertThat(ext.getUrl()).doesNotContain("agent-attribute");
                assertThat(ext.getUrl()).doesNotContain("audit-event-id");
            }
        }
    }

    @Test
    @DisplayName("Scenario 8: Attribute Order Independence")
    void testScenario08_AttributeOrderIndependence() {
        Map<String, String> attributes = new LinkedHashMap<>();
        attributes.put("tagA", "alpha");
        attributes.put("tagB", "beta");
        attributes.put("tagC", "gamma");
        attributes.put("tagD", "delta");

        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-attr-order",
                Instant.now(),
                AuditClassification.WORKFLOW,
                AuditAction.UPDATE,
                AuditOutcome.SUCCESS,
                null, null, null, null, null,
                null, null, null, null, null, null,
                attributes
        );

        AuditEvent fhir = mapper.toFhir(event);

        // Invert order of attribute extensions
        List<Extension> extensions = new ArrayList<>(fhir.getExtension());
        Collections.reverse(extensions);
        fhir.setExtension(extensions);

        HarmoniaAuditEvent reconstructed = mapper.fromFhir(fhir);
        assertThat(reconstructed.attributes()).isEqualTo(attributes);
        assertThat(reconstructed).isEqualTo(event);
    }

    @Test
    @DisplayName("Scenario 9: Duplicate Singleton Extension Failures")
    void testScenario09_DuplicateSingletonExtensionFailures() {
        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-dup-ext",
                Instant.now(),
                AuditClassification.SECURITY,
                AuditAction.READ,
                AuditOutcome.SUCCESS,
                null, null, null, null, null,
                null, null, null, null, null, null, null
        );

        // Duplicate security-domain
        AuditEvent fhirSecDom = mapper.toFhir(event);
        fhirSecDom.addExtension(new Extension(HarmoniaAuditFhirConstants.EXTENSION_SECURITY_DOMAIN, new StringType("d1")));
        fhirSecDom.addExtension(new Extension(HarmoniaAuditFhirConstants.EXTENSION_SECURITY_DOMAIN, new StringType("d2")));
        assertThatThrownBy(() -> mapper.fromFhir(fhirSecDom))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("Duplicate security-domain extension");

        // Duplicate correlation-id
        AuditEvent fhirCorr = mapper.toFhir(event);
        fhirCorr.addExtension(new Extension(HarmoniaAuditFhirConstants.EXTENSION_CORRELATION_ID, new StringType("c1")));
        fhirCorr.addExtension(new Extension(HarmoniaAuditFhirConstants.EXTENSION_CORRELATION_ID, new StringType("c2")));
        assertThatThrownBy(() -> mapper.fromFhir(fhirCorr))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("Duplicate correlation-id extension");

        // Duplicate causation-id
        AuditEvent fhirCause = mapper.toFhir(event);
        fhirCause.addExtension(new Extension(HarmoniaAuditFhirConstants.EXTENSION_CAUSATION_ID, new StringType("cause1")));
        fhirCause.addExtension(new Extension(HarmoniaAuditFhirConstants.EXTENSION_CAUSATION_ID, new StringType("cause2")));
        assertThatThrownBy(() -> mapper.fromFhir(fhirCause))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("Duplicate causation-id extension");

        // Duplicate operation-id
        AuditEvent fhirOp = mapper.toFhir(event);
        fhirOp.addExtension(new Extension(HarmoniaAuditFhirConstants.EXTENSION_OPERATION_ID, new StringType("op1")));
        fhirOp.addExtension(new Extension(HarmoniaAuditFhirConstants.EXTENSION_OPERATION_ID, new StringType("op2")));
        assertThatThrownBy(() -> mapper.fromFhir(fhirOp))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("Duplicate operation-id extension");
    }

    @Test
    @DisplayName("Scenario 10: Duplicate Agent Role and Conflicting Role Failures")
    void testScenario10_DuplicateAgentRoleFailures() {
        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-dup-agents",
                Instant.now(),
                AuditClassification.SECURITY,
                AuditAction.READ,
                AuditOutcome.SUCCESS,
                null, null, null, null, null,
                null, null, null, null, null, null, null
        );

        // Duplicate initiator agents
        AuditEvent fhirInit = mapper.toFhir(event);
        AuditEvent.AuditEventAgentComponent a1 = new AuditEvent.AuditEventAgentComponent();
        a1.setRequestor(true);
        AuditEvent.AuditEventAgentComponent a2 = new AuditEvent.AuditEventAgentComponent();
        a2.setRequestor(true);
        fhirInit.addAgent(a1);
        fhirInit.addAgent(a2);
        assertThatThrownBy(() -> mapper.fromFhir(fhirInit))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("Duplicate initiator agent");

        // Duplicate executor agents
        AuditEvent fhirExec = mapper.toFhir(event);
        AuditEvent.AuditEventAgentComponent e1 = new AuditEvent.AuditEventAgentComponent();
        e1.addRole(new CodeableConcept().addCoding(new Coding()
                .setSystem(HarmoniaAuditFhirConstants.SYSTEM_AGENT_ROLE)
                .setCode(HarmoniaAuditFhirConstants.CODE_AGENT_ROLE_EXECUTOR)));
        AuditEvent.AuditEventAgentComponent e2 = new AuditEvent.AuditEventAgentComponent();
        e2.addRole(new CodeableConcept().addCoding(new Coding()
                .setSystem(HarmoniaAuditFhirConstants.SYSTEM_AGENT_ROLE)
                .setCode(HarmoniaAuditFhirConstants.CODE_AGENT_ROLE_EXECUTOR)));
        fhirExec.addAgent(e1);
        fhirExec.addAgent(e2);
        assertThatThrownBy(() -> mapper.fromFhir(fhirExec))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("Duplicate executor agent");

        // Agent with both initiator and executor roles
        AuditEvent fhirBoth = mapper.toFhir(event);
        AuditEvent.AuditEventAgentComponent b1 = new AuditEvent.AuditEventAgentComponent();
        b1.setRequestor(true);
        b1.addRole(new CodeableConcept().addCoding(new Coding()
                .setSystem(HarmoniaAuditFhirConstants.SYSTEM_AGENT_ROLE)
                .setCode(HarmoniaAuditFhirConstants.CODE_AGENT_ROLE_EXECUTOR)));
        fhirBoth.addAgent(b1);
        assertThatThrownBy(() -> mapper.fromFhir(fhirBoth))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("Agent cannot have both INITIATOR and EXECUTOR");
    }

    @Test
    @DisplayName("Scenario 11: Event ID Boundary Preservation and Validation")
    void testScenario11_EventIdBoundaryPreservation() {
        Instant now = Instant.now();

        // 1-character ID
        HarmoniaAuditEvent evt1 = new HarmoniaAuditEvent("x", now, AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS, null, null, null, null, null, null, null, null, null, null, null, null);
        assertThat(mapper.fromFhir(mapper.toFhir(evt1)).eventId()).isEqualTo("x");

        // 64-character ID
        String id64 = "a".repeat(64);
        HarmoniaAuditEvent evt64 = new HarmoniaAuditEvent(id64, now, AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS, null, null, null, null, null, null, null, null, null, null, null, null);
        assertThat(mapper.fromFhir(mapper.toFhir(evt64)).eventId()).isEqualTo(id64);

        // UUID
        String uuid = UUID.randomUUID().toString();
        HarmoniaAuditEvent evtUuid = new HarmoniaAuditEvent(uuid, now, AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS, null, null, null, null, null, null, null, null, null, null, null, null);
        assertThat(mapper.fromFhir(mapper.toFhir(evtUuid)).eventId()).isEqualTo(uuid);

        // Dots and hyphens
        String dotHyphen = "audit.event-123.test-id";
        HarmoniaAuditEvent evtDot = new HarmoniaAuditEvent(dotHyphen, now, AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS, null, null, null, null, null, null, null, null, null, null, null, null);
        assertThat(mapper.fromFhir(mapper.toFhir(evtDot)).eventId()).isEqualTo(dotHyphen);

        // Missing ID in FHIR
        AuditEvent fhirNoId = mapper.toFhir(evt1);
        fhirNoId.setId((String) null);
        assertThatThrownBy(() -> mapper.fromFhir(fhirNoId))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("AuditEvent.id must not be null or blank");

        // Invalid ID format in FHIR
        AuditEvent fhirBadId = mapper.toFhir(evt1);
        fhirBadId.setId("invalid:id:with:colons");
        assertThatThrownBy(() -> mapper.fromFhir(fhirBadId))
                .isInstanceOf(HarmoniaAuditMappingException.class);
    }

    @Test
    @DisplayName("Scenario 12: Conflicting Duplicate Attribute Keys Failure")
    void testScenario12_ConflictingDuplicateAttributeKeys() {
        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-conflicting-keys",
                Instant.now(),
                AuditClassification.SECURITY,
                AuditAction.READ,
                AuditOutcome.SUCCESS,
                null, null, null, null, null,
                null, null, null, null, null, null, null
        );

        AuditEvent fhir = mapper.toFhir(event);

        // First audit-attribute
        Extension attr1 = new Extension(HarmoniaAuditFhirConstants.EXTENSION_AUDIT_ATTRIBUTE);
        attr1.addExtension(new Extension(HarmoniaAuditFhirConstants.SUB_EXTENSION_KEY, new StringType("tag")));
        attr1.addExtension(new Extension(HarmoniaAuditFhirConstants.SUB_EXTENSION_VALUE, new StringType("value1")));
        fhir.addExtension(attr1);

        // Second audit-attribute with same key but different value
        Extension attr2 = new Extension(HarmoniaAuditFhirConstants.EXTENSION_AUDIT_ATTRIBUTE);
        attr2.addExtension(new Extension(HarmoniaAuditFhirConstants.SUB_EXTENSION_KEY, new StringType("tag")));
        attr2.addExtension(new Extension(HarmoniaAuditFhirConstants.SUB_EXTENSION_VALUE, new StringType("value2")));
        fhir.addExtension(attr2);

        assertThatThrownBy(() -> mapper.fromFhir(fhir))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("Conflicting attribute key: tag");
    }

    @Test
    @DisplayName("Scenario 13: Missing Required Elements Fail-Fast (timestamp, classification, action, outcome)")
    void testScenario13_MissingRequiredElementsFailFast() {
        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-missing-req",
                Instant.now(),
                AuditClassification.SECURITY,
                AuditAction.READ,
                AuditOutcome.SUCCESS,
                null, null, null, null, null,
                null, null, null, null, null, null, null
        );

        // Missing recorded timestamp
        AuditEvent fhirNoTime = mapper.toFhir(event);
        fhirNoTime.setRecordedElement(null);
        assertThatThrownBy(() -> mapper.fromFhir(fhirNoTime))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("AuditEvent.recorded must not be null or blank");

        // Missing classification
        AuditEvent fhirNoCat = mapper.toFhir(event);
        fhirNoCat.getCategory().clear();
        assertThatThrownBy(() -> mapper.fromFhir(fhirNoCat))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("Missing required audit classification");

        // Missing action
        AuditEvent fhirNoAction = mapper.toFhir(event);
        fhirNoAction.setCode(null);
        assertThatThrownBy(() -> mapper.fromFhir(fhirNoAction))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("Missing required audit action");

        // Missing outcome
        AuditEvent fhirNoOutcome = mapper.toFhir(event);
        fhirNoOutcome.setOutcome(null);
        assertThatThrownBy(() -> mapper.fromFhir(fhirNoOutcome))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("Missing required outcome");
    }

    @Test
    @DisplayName("Scenario 14: Unrecognized or Corrupt Codings Fail-Fast")
    void testScenario14_UnrecognizedCodingsFailFast() {
        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-bad-codings",
                Instant.now(),
                AuditClassification.SECURITY,
                AuditAction.READ,
                AuditOutcome.SUCCESS,
                null, null, null, null, null,
                null, null, null, null, null, null, null
        );

        // Unknown classification
        AuditEvent fhirBadCat = mapper.toFhir(event);
        fhirBadCat.getCategoryFirstRep().getCodingFirstRep().setCode("NON_EXISTENT_CLASSIFICATION");
        assertThatThrownBy(() -> mapper.fromFhir(fhirBadCat))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("Unrecognized audit classification");

        // Unknown action
        AuditEvent fhirBadAction = mapper.toFhir(event);
        fhirBadAction.getCode().getCodingFirstRep().setCode("NON_EXISTENT_ACTION");
        assertThatThrownBy(() -> mapper.fromFhir(fhirBadAction))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("Unrecognized audit action");

        // Unknown outcome
        AuditEvent fhirBadOutcome = mapper.toFhir(event);
        fhirBadOutcome.getOutcome().getCode().setCode("NON_EXISTENT_OUTCOME");
        assertThatThrownBy(() -> mapper.fromFhir(fhirBadOutcome))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("Unrecognized audit outcome");

        // Unknown principal type
        AuditEvent fhirBadPrincipal = mapper.toFhir(event);
        AuditEvent.AuditEventAgentComponent agent = new AuditEvent.AuditEventAgentComponent();
        agent.setRequestor(true);
        agent.setType(new CodeableConcept().addCoding(new Coding()
                .setSystem(HarmoniaAuditFhirConstants.SYSTEM_PRINCIPAL_TYPE)
                .setCode("ALIEN_PRINCIPAL")));
        fhirBadPrincipal.addAgent(agent);
        assertThatThrownBy(() -> mapper.fromFhir(fhirBadPrincipal))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("Unrecognized principal type");
    }

    @Test
    @DisplayName("Scenario 15: Foreign Entities and Agents Isolation (selectively binds TARGET role)")
    void testScenario15_ForeignEntitiesAndAgentsIsolation() {
        AuditPrincipal initiator = AuditPrincipal.of("user:dr-alice", PrincipalType.HUMAN, "hospital-east");
        AuditTarget target = AuditTarget.of("Patient", "PAT-100", "CLINICAL");

        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-foreign-isolation",
                Instant.now(),
                AuditClassification.CLINICAL,
                AuditAction.READ,
                AuditOutcome.SUCCESS,
                initiator, null, null,
                target,
                null, null, null, null, null, null, null, null
        );

        AuditEvent fhir = mapper.toFhir(event);

        // Add a foreign entity (not role TARGET)
        AuditEvent.AuditEventEntityComponent foreignEntity = new AuditEvent.AuditEventEntityComponent();
        foreignEntity.setRole(new CodeableConcept().addCoding(new Coding()
                .setSystem("http://example.org/roles")
                .setCode("PROVENANCE_CONTEXT")));
        Reference foreignWhat = new Reference();
        foreignWhat.setReference("Provenance/prov-999");
        foreignEntity.setWhat(foreignWhat);
        // Add foreign entity at the beginning of the list
        fhir.getEntity().add(0, foreignEntity);

        // Add a foreign agent (neither initiator nor executor)
        AuditEvent.AuditEventAgentComponent foreignAgent = new AuditEvent.AuditEventAgentComponent();
        foreignAgent.setRole(List.of(new CodeableConcept().addCoding(new Coding()
                .setSystem("http://example.org/roles")
                .setCode("THIRD_PARTY_OBSERVER"))));
        foreignAgent.setRequestor(false);
        fhir.getAgent().add(0, foreignAgent);

        HarmoniaAuditEvent reconstructed = mapper.fromFhir(fhir);
        assertThat(reconstructed).isEqualTo(event);
        assertThat(reconstructed.target()).isEqualTo(target);
        assertThat(reconstructed.initiatingPrincipal()).isEqualTo(initiator);
        assertThat(reconstructed.executingPrincipal()).isNull();
    }

    @Test
    @DisplayName("Target permutations round-trip (type-only, id-only, domain-only)")
    void testTargetPermutationsRoundTrip() {
        // Type only
        HarmoniaAuditEvent eventTypeOnly = new HarmoniaAuditEvent(
                "evt-type-only", Instant.now(), AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS,
                null, null, null, AuditTarget.of("Patient", null, null), null, null, null, null, null, null, null, null
        );
        HarmoniaAuditEvent reconType = mapper.fromFhir(mapper.toFhir(eventTypeOnly));
        assertThat(reconType.target()).isEqualTo(AuditTarget.of("Patient", null, null));
        assertThat(reconType).isEqualTo(eventTypeOnly);

        // ID only
        HarmoniaAuditEvent eventIdOnly = new HarmoniaAuditEvent(
                "evt-id-only", Instant.now(), AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS,
                null, null, null, AuditTarget.of(null, "12345", null), null, null, null, null, null, null, null, null
        );
        HarmoniaAuditEvent reconId = mapper.fromFhir(mapper.toFhir(eventIdOnly));
        assertThat(reconId.target()).isEqualTo(AuditTarget.of(null, "12345", null));
        assertThat(reconId).isEqualTo(eventIdOnly);

        // Domain only
        HarmoniaAuditEvent eventDomainOnly = new HarmoniaAuditEvent(
                "evt-dom-only", Instant.now(), AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS,
                null, null, null, AuditTarget.of(null, null, "CUSTOM_DOMAIN"), null, null, null, null, null, null, null, null
        );
        HarmoniaAuditEvent reconDom = mapper.fromFhir(mapper.toFhir(eventDomainOnly));
        assertThat(reconDom.target()).isEqualTo(AuditTarget.of(null, null, "CUSTOM_DOMAIN"));
        assertThat(reconDom).isEqualTo(eventDomainOnly);
    }

    @Test
    @DisplayName("Source permutations round-trip (subsystem-only, component-only)")
    void testSourcePermutationsRoundTrip() {
        // Subsystem only
        HarmoniaAuditEvent eventSubOnly = new HarmoniaAuditEvent(
                "evt-sub-only", Instant.now(), AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS,
                null, null, null, null, null, null, null, null, null, null, AuditSource.of("THEMIS", null), null
        );
        HarmoniaAuditEvent reconSub = mapper.fromFhir(mapper.toFhir(eventSubOnly));
        assertThat(reconSub.source()).isEqualTo(AuditSource.of("THEMIS", null));
        assertThat(reconSub).isEqualTo(eventSubOnly);

        // Component only
        HarmoniaAuditEvent eventCompOnly = new HarmoniaAuditEvent(
                "evt-comp-only", Instant.now(), AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS,
                null, null, null, null, null, null, null, null, null, null, AuditSource.of(null, "AUTH_ENGINE"), null
        );
        HarmoniaAuditEvent reconComp = mapper.fromFhir(mapper.toFhir(eventCompOnly));
        assertThat(reconComp.source()).isEqualTo(AuditSource.of(null, "AUTH_ENGINE"));
        assertThat(reconComp).isEqualTo(eventCompOnly);
    }

    @Test
    @DisplayName("Authorization evidence permutations round-trip")
    void testEvidencePermutationsRoundTrip() {
        AuditPrincipal initiator = AuditPrincipal.of("user:dr-alice", PrincipalType.HUMAN);

        // Only policyId
        HarmoniaAuditEvent eventPolicyOnly = new HarmoniaAuditEvent(
                "evt-policy-only", Instant.now(), AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS,
                initiator, null, null, null,
                AuditAuthorizationEvidence.of(null, null, null, "policy-alpha", null),
                null, null, null, null, null, null, null
        );
        HarmoniaAuditEvent reconPolicy = mapper.fromFhir(mapper.toFhir(eventPolicyOnly));
        assertThat(reconPolicy.authorizationEvidence()).isEqualTo(AuditAuthorizationEvidence.of(null, null, null, "policy-alpha", null));
        assertThat(reconPolicy).isEqualTo(eventPolicyOnly);

        // Only decision & reason
        HarmoniaAuditEvent eventDecReason = new HarmoniaAuditEvent(
                "evt-dec-reason", Instant.now(), AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS,
                null, null, null, null,
                AuditAuthorizationEvidence.of(null, ThemisDecision.DENY, ThemisDecisionReason.AUTHORITY_MISSING, null, null),
                null, null, null, null, null, null, null
        );
        HarmoniaAuditEvent reconDecReason = mapper.fromFhir(mapper.toFhir(eventDecReason));
        assertThat(reconDecReason.authorizationEvidence()).isEqualTo(AuditAuthorizationEvidence.of(null, ThemisDecision.DENY, ThemisDecisionReason.AUTHORITY_MISSING, null, null));
        assertThat(reconDecReason).isEqualTo(eventDecReason);
    }

    @Test
    @DisplayName("Duplicate TARGET entity fails fast")
    void testDuplicateTargetEntityFailFast() {
        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-dup-target", Instant.now(), AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS,
                null, null, null, AuditTarget.of("Patient", "1"), null, null, null, null, null, null, null, null
        );
        AuditEvent fhir = mapper.toFhir(event);

        AuditEvent.AuditEventEntityComponent dupEntity = new AuditEvent.AuditEventEntityComponent();
        dupEntity.setRole(new CodeableConcept().addCoding(new Coding()
                .setSystem(HarmoniaAuditFhirConstants.SYSTEM_ENTITY_ROLE)
                .setCode(HarmoniaAuditFhirConstants.CODE_ENTITY_ROLE_TARGET)));
        fhir.addEntity(dupEntity);

        assertThatThrownBy(() -> mapper.fromFhir(fhir))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("Duplicate target entity found with role TARGET");
    }

    @Test
    @DisplayName("Duplicate security-domain detail on target entity fails fast")
    void testDuplicateTargetSecurityDomainDetailFailFast() {
        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-dup-target-sec", Instant.now(), AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS,
                null, null, null, AuditTarget.of("Patient", "1", "DOM1"), null, null, null, null, null, null, null, null
        );
        AuditEvent fhir = mapper.toFhir(event);
        AuditEvent.AuditEventEntityComponent targetEntity = fhir.getEntityFirstRep();

        AuditEvent.AuditEventEntityDetailComponent dupDetail = new AuditEvent.AuditEventEntityDetailComponent();
        dupDetail.setType(new CodeableConcept().addCoding(new Coding()
                .setSystem(HarmoniaAuditFhirConstants.SYSTEM_ENTITY_DETAIL)
                .setCode(HarmoniaAuditFhirConstants.CODE_DETAIL_SECURITY_DOMAIN)));
        dupDetail.setValue(new StringType("DOM2"));
        targetEntity.addDetail(dupDetail);

        assertThatThrownBy(() -> mapper.fromFhir(fhir))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("Duplicate target security-domain detail found");
    }

    @Test
    @DisplayName("Duplicate subsystem in source.type fails fast")
    void testDuplicateSourceSubsystemFailFast() {
        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-dup-source", Instant.now(), AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS,
                null, null, null, null, null, null, null, null, null, null, AuditSource.of("THEMIS", "ENGINE"), null
        );
        AuditEvent fhir = mapper.toFhir(event);
        fhir.getSource().addType(new CodeableConcept().addCoding(new Coding()
                .setSystem(HarmoniaAuditFhirConstants.SYSTEM_SOURCE_SUBSYSTEM)
                .setCode("OTHER_SUBSYSTEM")));

        assertThatThrownBy(() -> mapper.fromFhir(fhir))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("Duplicate subsystem coding in source.type");
    }

    @Test
    @DisplayName("Duplicate decision and decision reason in outcome.detail fail fast")
    void testDuplicateOutcomeDetailDecisionsFailFast() {
        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-dup-decision", Instant.now(), AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS,
                null, null, null, null,
                AuditAuthorizationEvidence.of("d1", ThemisDecision.ALLOW, ThemisDecisionReason.ALLOWED_BY_POLICY, null, "msg"),
                null, null, null, null, null, null, null
        );

        // Duplicate decision
        AuditEvent fhirDupDec = mapper.toFhir(event);
        CodeableConcept dupDec = new CodeableConcept().addCoding(new Coding()
                .setSystem(HarmoniaAuditFhirConstants.SYSTEM_THEMIS_DECISION)
                .setCode(ThemisDecision.DENY.name()));
        fhirDupDec.getOutcome().addDetail(dupDec);
        assertThatThrownBy(() -> mapper.fromFhir(fhirDupDec))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("Duplicate decision in outcome.detail");

        // Duplicate decision reason
        AuditEvent fhirDupReason = mapper.toFhir(event);
        CodeableConcept dupReason = new CodeableConcept().addCoding(new Coding()
                .setSystem(HarmoniaAuditFhirConstants.SYSTEM_THEMIS_DECISION_REASON)
                .setCode(ThemisDecisionReason.ACTION_NOT_PERMITTED.name()));
        fhirDupReason.getOutcome().addDetail(dupReason);
        assertThatThrownBy(() -> mapper.fromFhir(fhirDupReason))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("Duplicate decision reason in outcome.detail");
    }

    @Test
    @DisplayName("Identical duplicate attribute keys succeed while conflicting keys fail fast")
    void testIdenticalDuplicateAttributeKeys() {
        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-ident-keys", Instant.now(), AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS,
                null, null, null, null, null, null, null, null, null, null, null,
                Map.of("key1", "val1")
        );
        AuditEvent fhir = mapper.toFhir(event);

        // Add identical duplicate key
        Extension attrDup = new Extension(HarmoniaAuditFhirConstants.EXTENSION_AUDIT_ATTRIBUTE);
        attrDup.addExtension(new Extension(HarmoniaAuditFhirConstants.SUB_EXTENSION_KEY, new StringType("key1")));
        attrDup.addExtension(new Extension(HarmoniaAuditFhirConstants.SUB_EXTENSION_VALUE, new StringType("val1")));
        fhir.addExtension(attrDup);

        HarmoniaAuditEvent reconstructed = mapper.fromFhir(fhir);
        assertThat(reconstructed.attributes()).containsEntry("key1", "val1");
        assertThat(reconstructed).isEqualTo(event);
    }

    @Test
    @DisplayName("Null handling on mapper methods fails fast")
    void testNullInputsFailFast() {
        assertThatThrownBy(() -> mapper.toFhir(null))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("HarmoniaAuditEvent must not be null");

        assertThatThrownBy(() -> mapper.fromFhir(null))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("AuditEvent must not be null");
    }

    @Test
    @DisplayName("Malformed security-domain extension (non-StringType value) fails fast")
    void testMalformedSecurityDomainExtensionFailFast() {
        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-malformed-sec-dom", Instant.now(), AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS,
                null, null, null, null, null, null, null, null, null, null, null, null
        );
        AuditEvent fhir = mapper.toFhir(event);

        // Add malformed security-domain extension with non-StringType value
        Extension malformed = new Extension(HarmoniaAuditFhirConstants.EXTENSION_SECURITY_DOMAIN);
        malformed.setValue(new Coding().setSystem("test").setCode("test")); // Wrong type
        fhir.addExtension(malformed);

        assertThatThrownBy(() -> mapper.fromFhir(fhir))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("security-domain extension value must be StringType");
    }

    @Test
    @DisplayName("Malformed correlation-id extension (non-StringType value) fails fast")
    void testMalformedCorrelationIdExtensionFailFast() {
        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-malformed-corr", Instant.now(), AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS,
                null, null, null, null, null, null, null, null, null, null, null, null
        );
        AuditEvent fhir = mapper.toFhir(event);

        // Add malformed correlation-id extension with non-StringType value
        Extension malformed = new Extension(HarmoniaAuditFhirConstants.EXTENSION_CORRELATION_ID);
        malformed.setValue(new CodeableConcept().addCoding(new Coding().setCode("test"))); // Wrong type
        fhir.addExtension(malformed);

        assertThatThrownBy(() -> mapper.fromFhir(fhir))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("correlation-id extension value must be StringType");
    }

    @Test
    @DisplayName("Malformed causation-id extension (non-StringType value) fails fast")
    void testMalformedCausationIdExtensionFailFast() {
        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-malformed-caus", Instant.now(), AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS,
                null, null, null, null, null, null, null, null, null, null, null, null
        );
        AuditEvent fhir = mapper.toFhir(event);

        // Add malformed causation-id extension with non-StringType value
        Extension malformed = new Extension(HarmoniaAuditFhirConstants.EXTENSION_CAUSATION_ID);
        malformed.setValue(new UriType("http://example.com")); // Wrong type
        fhir.addExtension(malformed);

        assertThatThrownBy(() -> mapper.fromFhir(fhir))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("causation-id extension value must be StringType");
    }

    @Test
    @DisplayName("Malformed operation-id extension (non-StringType value) fails fast")
    void testMalformedOperationIdExtensionFailFast() {
        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-malformed-op", Instant.now(), AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS,
                null, null, null, null, null, null, null, null, null, null, null, null
        );
        AuditEvent fhir = mapper.toFhir(event);

        // Add malformed operation-id extension with non-StringType value
        Extension malformed = new Extension(HarmoniaAuditFhirConstants.EXTENSION_OPERATION_ID);
        malformed.setValue(new Identifier().setSystem("test").setValue("test")); // Wrong type
        fhir.addExtension(malformed);

        assertThatThrownBy(() -> mapper.fromFhir(fhir))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("operation-id extension value must be StringType");
    }

    @Test
    @DisplayName("Malformed audit-attribute key sub-extension (non-StringType value) fails fast")
    void testMalformedAuditAttributeKeySubExtensionFailFast() {
        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-malformed-attr-key", Instant.now(), AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS,
                null, null, null, null, null, null, null, null, null, null, null, null
        );
        AuditEvent fhir = mapper.toFhir(event);

        // Add malformed audit-attribute with non-StringType key
        Extension attr = new Extension(HarmoniaAuditFhirConstants.EXTENSION_AUDIT_ATTRIBUTE);
        attr.addExtension(new Extension(HarmoniaAuditFhirConstants.SUB_EXTENSION_KEY, new Coding().setCode("test"))); // Wrong type
        attr.addExtension(new Extension(HarmoniaAuditFhirConstants.SUB_EXTENSION_VALUE, new StringType("val")));
        fhir.addExtension(attr);

        assertThatThrownBy(() -> mapper.fromFhir(fhir))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("audit-attribute key sub-extension value must be StringType");
    }

    @Test
    @DisplayName("Malformed audit-attribute value sub-extension (non-StringType value) fails fast")
    void testMalformedAuditAttributeValueSubExtensionFailFast() {
        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-malformed-attr-val", Instant.now(), AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS,
                null, null, null, null, null, null, null, null, null, null, null, null
        );
        AuditEvent fhir = mapper.toFhir(event);

        // Add malformed audit-attribute with non-StringType value
        Extension attr = new Extension(HarmoniaAuditFhirConstants.EXTENSION_AUDIT_ATTRIBUTE);
        attr.addExtension(new Extension(HarmoniaAuditFhirConstants.SUB_EXTENSION_KEY, new StringType("key")));
        attr.addExtension(new Extension(HarmoniaAuditFhirConstants.SUB_EXTENSION_VALUE, new Identifier().setSystem("test").setValue("test"))); // Wrong type
        fhir.addExtension(attr);

        assertThatThrownBy(() -> mapper.fromFhir(fhir))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("audit-attribute value sub-extension value must be StringType");
    }

    @Test
    @DisplayName("Malformed target security-domain detail (non-StringType value) fails fast")
    void testMalformedTargetSecurityDomainDetailFailFast() {
        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-malformed-target-detail", Instant.now(), AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS,
                null, null, null, AuditTarget.of("Patient", "1"), null, null, null, null, null, null, null, null
        );
        AuditEvent fhir = mapper.toFhir(event);
        AuditEvent.AuditEventEntityComponent targetEntity = fhir.getEntityFirstRep();

        // Add malformed security-domain detail with non-StringType value
        AuditEvent.AuditEventEntityDetailComponent malformedDetail = new AuditEvent.AuditEventEntityDetailComponent();
        malformedDetail.setType(new CodeableConcept().addCoding(new Coding()
                .setSystem(HarmoniaAuditFhirConstants.SYSTEM_ENTITY_DETAIL)
                .setCode(HarmoniaAuditFhirConstants.CODE_DETAIL_SECURITY_DOMAIN)));
        malformedDetail.setValue(new IntegerType(42)); // Wrong type
        targetEntity.addDetail(malformedDetail);

        assertThatThrownBy(() -> mapper.fromFhir(fhir))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("Target security-domain detail value must be StringType");
    }

    @Test
    @DisplayName("Missing audit-attribute key sub-extension fails fast")
    void testMissingAuditAttributeKeySubExtensionFailFast() {
        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-missing-attr-key", Instant.now(), AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS,
                null, null, null, null, null, null, null, null, null, null, null, null
        );
        AuditEvent fhir = mapper.toFhir(event);

        // Add audit-attribute with missing key sub-extension
        Extension attr = new Extension(HarmoniaAuditFhirConstants.EXTENSION_AUDIT_ATTRIBUTE);
        attr.addExtension(new Extension(HarmoniaAuditFhirConstants.SUB_EXTENSION_VALUE, new StringType("val")));
        fhir.addExtension(attr);

        assertThatThrownBy(() -> mapper.fromFhir(fhir))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("Missing or blank key in audit-attribute extension");
    }

    @Test
    @DisplayName("Missing audit-attribute value sub-extension fails fast")
    void testMissingAuditAttributeValueSubExtensionFailFast() {
        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-missing-attr-value", Instant.now(), AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS,
                null, null, null, null, null, null, null, null, null, null, null, null
        );
        AuditEvent fhir = mapper.toFhir(event);

        // Add audit-attribute with a key but no value sub-extension.
        Extension attr = new Extension(HarmoniaAuditFhirConstants.EXTENSION_AUDIT_ATTRIBUTE);
        attr.addExtension(new Extension(HarmoniaAuditFhirConstants.SUB_EXTENSION_KEY, new StringType("key")));
        fhir.addExtension(attr);

        assertThatThrownBy(() -> mapper.fromFhir(fhir))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("Missing value in audit-attribute extension for key: key");
    }

    @Test
    @DisplayName("Blank audit-attribute key sub-extension fails fast")
    void testBlankAuditAttributeKeySubExtensionFailFast() {
        HarmoniaAuditEvent event = new HarmoniaAuditEvent(
                "evt-blank-attr-key", Instant.now(), AuditClassification.SECURITY, AuditAction.READ, AuditOutcome.SUCCESS,
                null, null, null, null, null, null, null, null, null, null, null, null
        );
        AuditEvent fhir = mapper.toFhir(event);

        // Add audit-attribute with blank key sub-extension
        Extension attr = new Extension(HarmoniaAuditFhirConstants.EXTENSION_AUDIT_ATTRIBUTE);
        attr.addExtension(new Extension(HarmoniaAuditFhirConstants.SUB_EXTENSION_KEY, new StringType("   ")));
        attr.addExtension(new Extension(HarmoniaAuditFhirConstants.SUB_EXTENSION_VALUE, new StringType("val")));
        fhir.addExtension(attr);

        assertThatThrownBy(() -> mapper.fromFhir(fhir))
                .isInstanceOf(HarmoniaAuditMappingException.class)
                .hasMessageContaining("Missing or blank key in audit-attribute extension");
    }
}
