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
import org.hl7.fhir.r5.model.InstantType;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.UriType;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Bi-directional, deterministic, stateless mapper between canonical {@link HarmoniaAuditEvent}
 * and HAPI FHIR R5 {@link AuditEvent}.
 * <p>
 * Ensures lossless round-tripping across all 17 canonical fields and 5 frozen extensions while
 * strictly isolating persistence metadata (meta.versionId, meta.lastUpdated, meta.security) and
 * enforcing fail-fast validation on malformed representations.
 */
public class HarmoniaAuditEventMapper {

    public HarmoniaAuditEventMapper() {
        // Stateless mapper instance
    }

    /**
     * Maps a canonical {@link HarmoniaAuditEvent} into a FHIR R5 {@link AuditEvent}.
     *
     * @param event canonical audit event
     * @return FHIR AuditEvent resource
     * @throws HarmoniaAuditMappingException if event is null or contains invalid state
     */
    public AuditEvent toFhir(HarmoniaAuditEvent event) {
        if (event == null) {
            throw new HarmoniaAuditMappingException("HarmoniaAuditEvent must not be null");
        }

        AuditEvent auditEvent = new AuditEvent();

        // 1. eventId -> AuditEvent.id
        auditEvent.setId(event.eventId());

        // 2. recordedAt -> AuditEvent.recorded (string-preserving InstantType)
        auditEvent.setRecordedElement(new InstantType(event.recordedAt().toString()));

        // 3. classification -> AuditEvent.category
        CodeableConcept category = new CodeableConcept();
        category.addCoding(new Coding()
                .setSystem(HarmoniaAuditFhirConstants.SYSTEM_AUDIT_CLASSIFICATION)
                .setCode(event.classification().name()));
        auditEvent.addCategory(category);

        // 4. action -> AuditEvent.action & AuditEvent.code
        AuditEvent.AuditEventAction fhirAction = switch (event.action()) {
            case CREATE -> AuditEvent.AuditEventAction.C;
            case READ, SEARCH -> AuditEvent.AuditEventAction.R;
            case UPDATE -> AuditEvent.AuditEventAction.U;
            case DELETE -> AuditEvent.AuditEventAction.D;
            case EXECUTE, AUTHORIZE -> AuditEvent.AuditEventAction.E;
        };
        auditEvent.setAction(fhirAction);

        CodeableConcept codeConcept = new CodeableConcept();
        codeConcept.addCoding(new Coding()
                .setSystem(HarmoniaAuditFhirConstants.SYSTEM_AUDIT_ACTION)
                .setCode(event.action().name()));
        auditEvent.setCode(codeConcept);

        // 5. outcome -> AuditEvent.outcome.code
        AuditEvent.AuditEventOutcomeComponent outcome = new AuditEvent.AuditEventOutcomeComponent();
        outcome.setCode(new Coding()
                .setSystem(HarmoniaAuditFhirConstants.SYSTEM_AUDIT_OUTCOME)
                .setCode(event.outcome().name()));
        auditEvent.setOutcome(outcome);

        // 6 & 7 & 10 & 11: Dual Agents (initiatingPrincipal, executingPrincipal, authorities, policyId)
        AuditEvent.AuditEventAgentComponent initiatorAgent = null;
        AuditEvent.AuditEventAgentComponent executorAgent = null;

        if (event.initiatingPrincipal() != null) {
            initiatorAgent = new AuditEvent.AuditEventAgentComponent();
            initiatorAgent.setRequestor(true);

            CodeableConcept roleConcept = new CodeableConcept();
            roleConcept.addCoding(new Coding()
                    .setSystem(HarmoniaAuditFhirConstants.SYSTEM_AGENT_ROLE)
                    .setCode(HarmoniaAuditFhirConstants.CODE_AGENT_ROLE_INITIATOR));
            initiatorAgent.addRole(roleConcept);

            AuditPrincipal p = event.initiatingPrincipal();
            if (p.principalId() != null || p.sourceDomain() != null) {
                Reference who = new Reference();
                Identifier identifier = new Identifier();
                if (p.principalId() != null) {
                    identifier.setValue(p.principalId());
                }
                if (p.sourceDomain() != null) {
                    identifier.setSystem(p.sourceDomain());
                }
                who.setIdentifier(identifier);
                initiatorAgent.setWho(who);
            }

            if (p.principalType() != null) {
                CodeableConcept typeConcept = new CodeableConcept();
                typeConcept.addCoding(new Coding()
                        .setSystem(HarmoniaAuditFhirConstants.SYSTEM_PRINCIPAL_TYPE)
                        .setCode(p.principalType().name()));
                initiatorAgent.setType(typeConcept);
            }

            auditEvent.addAgent(initiatorAgent);
        }

        if (event.executingPrincipal() != null) {
            executorAgent = new AuditEvent.AuditEventAgentComponent();
            executorAgent.setRequestor(false);

            CodeableConcept roleConcept = new CodeableConcept();
            roleConcept.addCoding(new Coding()
                    .setSystem(HarmoniaAuditFhirConstants.SYSTEM_AGENT_ROLE)
                    .setCode(HarmoniaAuditFhirConstants.CODE_AGENT_ROLE_EXECUTOR));
            executorAgent.addRole(roleConcept);

            AuditPrincipal p = event.executingPrincipal();
            if (p.principalId() != null || p.sourceDomain() != null) {
                Reference who = new Reference();
                Identifier identifier = new Identifier();
                if (p.principalId() != null) {
                    identifier.setValue(p.principalId());
                }
                if (p.sourceDomain() != null) {
                    identifier.setSystem(p.sourceDomain());
                }
                who.setIdentifier(identifier);
                executorAgent.setWho(who);
            }

            if (p.principalType() != null) {
                CodeableConcept typeConcept = new CodeableConcept();
                typeConcept.addCoding(new Coding()
                        .setSystem(HarmoniaAuditFhirConstants.SYSTEM_PRINCIPAL_TYPE)
                        .setCode(p.principalType().name()));
                executorAgent.setType(typeConcept);
            }

            auditEvent.addAgent(executorAgent);
        }

        AuditEvent.AuditEventAgentComponent agentForAuth = initiatorAgent != null ? initiatorAgent : executorAgent;

        if (event.authorizationEvidence() != null) {
            AuditAuthorizationEvidence evidence = event.authorizationEvidence();
            if (evidence.policyId() != null) {
                if (agentForAuth != null) {
                    agentForAuth.addPolicy(evidence.policyId());
                } else {
                    CodeableConcept policyConcept = new CodeableConcept();
                    policyConcept.addCoding(new Coding()
                            .setSystem(HarmoniaAuditFhirConstants.SYSTEM_THEMIS_POLICY)
                            .setCode(evidence.policyId()));
                    outcome.addDetail(policyConcept);
                }
            }

            if (evidence.decision() != null || evidence.decisionId() != null) {
                CodeableConcept decisionConcept = new CodeableConcept();
                Coding coding = new Coding().setSystem(HarmoniaAuditFhirConstants.SYSTEM_THEMIS_DECISION);
                if (evidence.decision() != null) {
                    coding.setCode(evidence.decision().name());
                }
                decisionConcept.addCoding(coding);
                if (evidence.decisionId() != null) {
                    decisionConcept.setText(evidence.decisionId());
                }
                outcome.addDetail(decisionConcept);
            }

            if (evidence.reason() != null || evidence.message() != null) {
                CodeableConcept reasonConcept = new CodeableConcept();
                Coding coding = new Coding().setSystem(HarmoniaAuditFhirConstants.SYSTEM_THEMIS_DECISION_REASON);
                if (evidence.reason() != null) {
                    coding.setCode(evidence.reason().name());
                }
                reasonConcept.addCoding(coding);
                if (evidence.message() != null) {
                    reasonConcept.setText(evidence.message());
                }
                outcome.addDetail(reasonConcept);
            }
        }

        if (event.authorities() != null && !event.authorities().isEmpty()) {
            for (ThemisAuthority auth : event.authorities()) {
                CodeableConcept authConcept = new CodeableConcept();
                authConcept.addCoding(new Coding()
                        .setSystem(HarmoniaAuditFhirConstants.SYSTEM_AUTHORITY_CODE)
                        .setCode(auth.authorityCode()));
                if (agentForAuth != null) {
                    agentForAuth.addAuthorization(authConcept);
                } else {
                    auditEvent.addAuthorization(authConcept);
                }
            }
        }

        // 8. securityDomain -> extension security-domain
        if (event.securityDomain() != null) {
            auditEvent.addExtension(new Extension(
                    HarmoniaAuditFhirConstants.EXTENSION_SECURITY_DOMAIN,
                    new StringType(event.securityDomain())
            ));
        }

        // 9 & 12: target -> AuditEvent.entity & securityLabels
        if (event.target() != null || (event.securityLabels() != null && !event.securityLabels().isEmpty())) {
            AuditEvent.AuditEventEntityComponent targetEntity = new AuditEvent.AuditEventEntityComponent();

            CodeableConcept roleConcept = new CodeableConcept();
            roleConcept.addCoding(new Coding()
                    .setSystem(HarmoniaAuditFhirConstants.SYSTEM_ENTITY_ROLE)
                    .setCode(HarmoniaAuditFhirConstants.CODE_ENTITY_ROLE_TARGET));
            targetEntity.setRole(roleConcept);

            if (event.target() != null) {
                AuditTarget target = event.target();
                if (target.resourceType() != null || target.resourceId() != null) {
                    Reference what = new Reference();
                    if (target.resourceType() != null && target.resourceId() != null) {
                        what.setReference(target.resourceType() + "/" + target.resourceId());
                    } else if (target.resourceId() != null) {
                        what.setReference("/" + target.resourceId());
                    } else {
                        what.setReference(target.resourceType() + "/");
                    }
                    targetEntity.setWhat(what);
                }

                if (target.securityDomain() != null) {
                    AuditEvent.AuditEventEntityDetailComponent detail = new AuditEvent.AuditEventEntityDetailComponent();
                    CodeableConcept typeConcept = new CodeableConcept();
                    typeConcept.addCoding(new Coding()
                            .setSystem(HarmoniaAuditFhirConstants.SYSTEM_ENTITY_DETAIL)
                            .setCode(HarmoniaAuditFhirConstants.CODE_DETAIL_SECURITY_DOMAIN));
                    detail.setType(typeConcept);
                    detail.setValue(new StringType(target.securityDomain()));
                    targetEntity.addDetail(detail);
                }
            }

            if (event.securityLabels() != null && !event.securityLabels().isEmpty()) {
                for (ThemisSecurityLabel label : event.securityLabels()) {
                    CodeableConcept labelConcept = new CodeableConcept();
                    labelConcept.addCoding(new Coding()
                            .setSystem(label.system() != null ? label.system() : HarmoniaAuditFhirConstants.SYSTEM_SECURITY_LABEL)
                            .setCode(label.code()));
                    targetEntity.addSecurityLabel(labelConcept);
                }
            }

            auditEvent.addEntity(targetEntity);
        }

        // 13. correlationId -> extension correlation-id
        if (event.correlationId() != null) {
            auditEvent.addExtension(new Extension(
                    HarmoniaAuditFhirConstants.EXTENSION_CORRELATION_ID,
                    new StringType(event.correlationId())
            ));
        }

        // 14. causationId -> extension causation-id
        if (event.causationId() != null) {
            auditEvent.addExtension(new Extension(
                    HarmoniaAuditFhirConstants.EXTENSION_CAUSATION_ID,
                    new StringType(event.causationId())
            ));
        }

        // 15. operationId -> extension operation-id
        if (event.operationId() != null) {
            auditEvent.addExtension(new Extension(
                    HarmoniaAuditFhirConstants.EXTENSION_OPERATION_ID,
                    new StringType(event.operationId())
            ));
        }

        // 16. source -> AuditEvent.source
        if (event.source() != null) {
            AuditEvent.AuditEventSourceComponent source = new AuditEvent.AuditEventSourceComponent();
            if (event.source().component() != null) {
                Reference observer = new Reference();
                Identifier id = new Identifier();
                id.setValue(event.source().component());
                observer.setIdentifier(id);
                source.setObserver(observer);
            }
            if (event.source().subsystem() != null) {
                CodeableConcept typeConcept = new CodeableConcept();
                typeConcept.addCoding(new Coding()
                        .setSystem(HarmoniaAuditFhirConstants.SYSTEM_SOURCE_SUBSYSTEM)
                        .setCode(event.source().subsystem()));
                source.addType(typeConcept);
            }
            auditEvent.setSource(source);
        }

        // 17. attributes -> extension audit-attribute
        if (event.attributes() != null && !event.attributes().isEmpty()) {
            for (Map.Entry<String, String> entry : event.attributes().entrySet()) {
                Extension attrExt = new Extension(HarmoniaAuditFhirConstants.EXTENSION_AUDIT_ATTRIBUTE);
                attrExt.addExtension(new Extension(
                        HarmoniaAuditFhirConstants.SUB_EXTENSION_KEY,
                        new StringType(entry.getKey())
                ));
                attrExt.addExtension(new Extension(
                        HarmoniaAuditFhirConstants.SUB_EXTENSION_VALUE,
                        new StringType(entry.getValue())
                ));
                auditEvent.addExtension(attrExt);
            }
        }

        return auditEvent;
    }

    /**
     * Reconstructs a canonical {@link HarmoniaAuditEvent} from a FHIR R5 {@link AuditEvent}.
     *
     * @param fhir FHIR AuditEvent resource
     * @return canonical audit event
     * @throws HarmoniaAuditMappingException on missing required elements or malformed input
     */
    public HarmoniaAuditEvent fromFhir(AuditEvent fhir) {
        if (fhir == null) {
            throw new HarmoniaAuditMappingException("AuditEvent must not be null");
        }

        // 1. eventId from AuditEvent.id
        String eventId = fhir.getIdPart();
        if (eventId == null || eventId.isBlank()) {
            throw new HarmoniaAuditMappingException("AuditEvent.id must not be null or blank");
        }

        // 2. recordedAt from AuditEvent.recorded (string-preserving)
        if (!fhir.hasRecordedElement() || fhir.getRecordedElement().getValueAsString() == null || fhir.getRecordedElement().getValueAsString().isBlank()) {
            throw new HarmoniaAuditMappingException("AuditEvent.recorded must not be null or blank");
        }
        Instant recordedAt;
        try {
            recordedAt = Instant.parse(fhir.getRecordedElement().getValueAsString());
        } catch (Exception e) {
            throw new HarmoniaAuditMappingException("Invalid recordedAt timestamp format: " + fhir.getRecordedElement().getValueAsString(), e);
        }

        // 3. classification from AuditEvent.category
        AuditClassification classification = null;
        if (fhir.hasCategory()) {
            for (CodeableConcept catConcept : fhir.getCategory()) {
                for (Coding coding : catConcept.getCoding()) {
                    if (HarmoniaAuditFhirConstants.SYSTEM_AUDIT_CLASSIFICATION.equals(coding.getSystem())) {
                        if (classification != null) {
                            throw new HarmoniaAuditMappingException("Duplicate audit classification coding found");
                        }
                        if (coding.getCode() == null || coding.getCode().isBlank()) {
                            throw new HarmoniaAuditMappingException("Missing classification code");
                        }
                        try {
                            classification = AuditClassification.valueOf(coding.getCode().trim().toUpperCase());
                        } catch (IllegalArgumentException e) {
                            throw new HarmoniaAuditMappingException("Unrecognized audit classification: " + coding.getCode(), e);
                        }
                    }
                }
            }
        }
        if (classification == null) {
            throw new HarmoniaAuditMappingException("Missing required audit classification with system: " + HarmoniaAuditFhirConstants.SYSTEM_AUDIT_CLASSIFICATION);
        }

        // 4. action from AuditEvent.code
        AuditAction action = null;
        if (fhir.getCode() != null && fhir.getCode().hasCoding()) {
            for (Coding coding : fhir.getCode().getCoding()) {
                if (HarmoniaAuditFhirConstants.SYSTEM_AUDIT_ACTION.equals(coding.getSystem())) {
                    if (action != null) {
                        throw new HarmoniaAuditMappingException("Duplicate audit action coding found");
                    }
                    if (coding.getCode() == null || coding.getCode().isBlank()) {
                        throw new HarmoniaAuditMappingException("Missing action code");
                    }
                    try {
                        action = AuditAction.valueOf(coding.getCode().trim().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new HarmoniaAuditMappingException("Unrecognized audit action: " + coding.getCode(), e);
                    }
                }
            }
        }
        if (action == null) {
            throw new HarmoniaAuditMappingException("Missing required audit action with system: " + HarmoniaAuditFhirConstants.SYSTEM_AUDIT_ACTION);
        }

        // 5. outcome from AuditEvent.outcome.code
        if (!fhir.hasOutcome() || !fhir.getOutcome().hasCode()) {
            throw new HarmoniaAuditMappingException("Missing required outcome in AuditEvent");
        }
        Coding outcomeCoding = fhir.getOutcome().getCode();
        if (!HarmoniaAuditFhirConstants.SYSTEM_AUDIT_OUTCOME.equals(outcomeCoding.getSystem())) {
            throw new HarmoniaAuditMappingException("Unrecognized outcome system: " + outcomeCoding.getSystem() + ", expected: " + HarmoniaAuditFhirConstants.SYSTEM_AUDIT_OUTCOME);
        }
        if (outcomeCoding.getCode() == null || outcomeCoding.getCode().isBlank()) {
            throw new HarmoniaAuditMappingException("Missing outcome code");
        }
        AuditOutcome outcome;
        try {
            outcome = AuditOutcome.valueOf(outcomeCoding.getCode().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new HarmoniaAuditMappingException("Unrecognized audit outcome: " + outcomeCoding.getCode(), e);
        }

        // 6 & 7 & 10 & 11: Agents, authorities, policyId
        AuditPrincipal initiatingPrincipal = null;
        AuditPrincipal executingPrincipal = null;
        boolean hasInitiatorAgent = false;
        boolean hasExecutorAgent = false;
        Set<ThemisAuthority> authorities = new LinkedHashSet<>();
        String policyId = null;

        if (fhir.hasAgent()) {
            for (AuditEvent.AuditEventAgentComponent agent : fhir.getAgent()) {
                boolean isInitiator = false;
                boolean isExecutor = false;

                if (agent.hasRole()) {
                    for (CodeableConcept roleConcept : agent.getRole()) {
                        for (Coding coding : roleConcept.getCoding()) {
                            if (HarmoniaAuditFhirConstants.SYSTEM_AGENT_ROLE.equals(coding.getSystem())) {
                                if (HarmoniaAuditFhirConstants.CODE_AGENT_ROLE_INITIATOR.equals(coding.getCode())) {
                                    isInitiator = true;
                                } else if (HarmoniaAuditFhirConstants.CODE_AGENT_ROLE_EXECUTOR.equals(coding.getCode())) {
                                    isExecutor = true;
                                }
                            }
                        }
                    }
                }

                if (agent.hasRequestor() && agent.getRequestor()) {
                    isInitiator = true;
                }

                if (isInitiator && isExecutor) {
                    throw new HarmoniaAuditMappingException("Agent cannot have both INITIATOR and EXECUTOR roles");
                }

                if (isInitiator) {
                    if (hasInitiatorAgent) {
                        throw new HarmoniaAuditMappingException("Duplicate initiator agent found");
                    }
                    hasInitiatorAgent = true;
                    initiatingPrincipal = extractPrincipal(agent);
                } else if (isExecutor) {
                    if (hasExecutorAgent) {
                        throw new HarmoniaAuditMappingException("Duplicate executor agent found");
                    }
                    hasExecutorAgent = true;
                    executingPrincipal = extractPrincipal(agent);
                }

                if (agent.hasAuthorization()) {
                    for (CodeableConcept authConcept : agent.getAuthorization()) {
                        for (Coding coding : authConcept.getCoding()) {
                            if (HarmoniaAuditFhirConstants.SYSTEM_AUTHORITY_CODE.equals(coding.getSystem())) {
                                if (coding.getCode() == null || coding.getCode().isBlank()) {
                                    throw new HarmoniaAuditMappingException("Authority coding missing code");
                                }
                                authorities.add(ThemisAuthority.of(coding.getCode()));
                            }
                        }
                    }
                }

                if (agent.hasPolicy()) {
                    for (UriType p : agent.getPolicy()) {
                        if (p.hasValue() && !p.getValue().isBlank()) {
                            if (policyId != null && !policyId.equals(p.getValue())) {
                                throw new HarmoniaAuditMappingException("Conflicting policy IDs found in agents: " + policyId + " vs " + p.getValue());
                            }
                            policyId = p.getValue();
                        }
                    }
                }
            }
        }

        // Also collect any top-level audit event authorization codings
        if (fhir.hasAuthorization()) {
            for (CodeableConcept authConcept : fhir.getAuthorization()) {
                for (Coding coding : authConcept.getCoding()) {
                    if (HarmoniaAuditFhirConstants.SYSTEM_AUTHORITY_CODE.equals(coding.getSystem())) {
                        if (coding.getCode() == null || coding.getCode().isBlank()) {
                            throw new HarmoniaAuditMappingException("Authority coding missing code");
                        }
                        authorities.add(ThemisAuthority.of(coding.getCode()));
                    }
                }
            }
        }

        // 10. AuthorizationEvidence from outcome.detail and policyId
        String decisionId = null;
        ThemisDecision decision = null;
        ThemisDecisionReason reason = null;
        String message = null;

        if (fhir.hasOutcome() && fhir.getOutcome().hasDetail()) {
            for (CodeableConcept detailConcept : fhir.getOutcome().getDetail()) {
                for (Coding coding : detailConcept.getCoding()) {
                    if (HarmoniaAuditFhirConstants.SYSTEM_THEMIS_DECISION.equals(coding.getSystem())) {
                        if (decision != null) {
                            throw new HarmoniaAuditMappingException("Duplicate decision in outcome.detail");
                        }
                        if (coding.getCode() != null && !coding.getCode().isBlank()) {
                            try {
                                decision = ThemisDecision.valueOf(coding.getCode().trim().toUpperCase());
                            } catch (IllegalArgumentException e) {
                                throw new HarmoniaAuditMappingException("Unrecognized Themis decision: " + coding.getCode(), e);
                            }
                        }
                        if (detailConcept.hasText() && !detailConcept.getText().isBlank()) {
                            decisionId = detailConcept.getText();
                        }
                    } else if (HarmoniaAuditFhirConstants.SYSTEM_THEMIS_DECISION_REASON.equals(coding.getSystem())) {
                        if (reason != null) {
                            throw new HarmoniaAuditMappingException("Duplicate decision reason in outcome.detail");
                        }
                        if (coding.getCode() != null && !coding.getCode().isBlank()) {
                            try {
                                reason = ThemisDecisionReason.valueOf(coding.getCode().trim().toUpperCase());
                            } catch (IllegalArgumentException e) {
                                throw new HarmoniaAuditMappingException("Unrecognized Themis decision reason: " + coding.getCode(), e);
                            }
                        }
                        if (detailConcept.hasText() && !detailConcept.getText().isBlank()) {
                            message = detailConcept.getText();
                        }
                    } else if (HarmoniaAuditFhirConstants.SYSTEM_THEMIS_POLICY.equals(coding.getSystem())) {
                        if (coding.getCode() != null && !coding.getCode().isBlank()) {
                            if (policyId != null && !policyId.equals(coding.getCode())) {
                                throw new HarmoniaAuditMappingException("Conflicting policy ID in outcome.detail: " + policyId + " vs " + coding.getCode());
                            }
                            policyId = coding.getCode();
                        }
                    }
                }
            }
        }

        AuditAuthorizationEvidence authorizationEvidence = null;
        if (decisionId != null || decision != null || reason != null || policyId != null || message != null) {
            authorizationEvidence = AuditAuthorizationEvidence.of(decisionId, decision, reason, policyId, message);
        }

        // 9 & 12: Target and Security Labels from TARGET entity
        AuditTarget target = null;
        Set<ThemisSecurityLabel> securityLabels = new LinkedHashSet<>();
        AuditEvent.AuditEventEntityComponent targetEntity = null;

        if (fhir.hasEntity()) {
            for (AuditEvent.AuditEventEntityComponent entity : fhir.getEntity()) {
                boolean isTarget = false;
                if (entity.hasRole()) {
                    for (Coding coding : entity.getRole().getCoding()) {
                        if (HarmoniaAuditFhirConstants.SYSTEM_ENTITY_ROLE.equals(coding.getSystem())
                                && HarmoniaAuditFhirConstants.CODE_ENTITY_ROLE_TARGET.equals(coding.getCode())) {
                            isTarget = true;
                            break;
                        }
                    }
                }
                if (isTarget) {
                    if (targetEntity != null) {
                        throw new HarmoniaAuditMappingException("Duplicate target entity found with role TARGET");
                    }
                    targetEntity = entity;
                }
            }
        }

        if (targetEntity != null) {
            String resourceType = null;
            String resourceId = null;

            if (targetEntity.hasWhat() && targetEntity.getWhat().hasReference()) {
                String ref = targetEntity.getWhat().getReference();
                if (ref != null && !ref.isBlank()) {
                    int slashIdx = ref.indexOf('/');
                    if (slashIdx >= 0) {
                        String rt = ref.substring(0, slashIdx);
                        String ri = ref.substring(slashIdx + 1);
                        resourceType = rt.isEmpty() ? null : rt;
                        resourceId = ri.isEmpty() ? null : ri;
                    } else {
                        resourceType = ref;
                    }
                }
            }

            String targetSecurityDomain = null;
            if (targetEntity.hasDetail()) {
                for (AuditEvent.AuditEventEntityDetailComponent detail : targetEntity.getDetail()) {
                    if (detail.hasType()) {
                        for (Coding coding : detail.getType().getCoding()) {
                            if (HarmoniaAuditFhirConstants.SYSTEM_ENTITY_DETAIL.equals(coding.getSystem())
                                    && HarmoniaAuditFhirConstants.CODE_DETAIL_SECURITY_DOMAIN.equals(coding.getCode())) {
                                if (targetSecurityDomain != null) {
                                    throw new HarmoniaAuditMappingException("Duplicate target security-domain detail found");
                                }
                                if (detail.hasValue()) {
                                    if (detail.hasValueStringType()) {
                                        targetSecurityDomain = detail.getValueStringType().getValue();
                                    } else {
                                        throw new HarmoniaAuditMappingException("Target security-domain detail value must be StringType, got: " + detail.getValue().getClass().getSimpleName());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (targetEntity.hasSecurityLabel()) {
                for (CodeableConcept labelConcept : targetEntity.getSecurityLabel()) {
                    for (Coding coding : labelConcept.getCoding()) {
                        if (coding.getCode() == null || coding.getCode().isBlank()) {
                            throw new HarmoniaAuditMappingException("Security label coding missing code");
                        }
                        String system = coding.getSystem() != null ? coding.getSystem() : HarmoniaAuditFhirConstants.SYSTEM_SECURITY_LABEL;
                        securityLabels.add(ThemisSecurityLabel.of(system, coding.getCode()));
                    }
                }
            }

            if (resourceType != null || resourceId != null || targetSecurityDomain != null) {
                target = AuditTarget.of(resourceType, resourceId, targetSecurityDomain);
            }
        }

        // Top-level Extensions (security-domain, correlation-id, causation-id, operation-id, audit-attribute)
        String securityDomain = null;
        String correlationId = null;
        String causationId = null;
        String operationId = null;
        Map<String, String> attributes = new LinkedHashMap<>();

        if (fhir.hasExtension()) {
            for (Extension ext : fhir.getExtension()) {
                String url = ext.getUrl();
                if (HarmoniaAuditFhirConstants.EXTENSION_SECURITY_DOMAIN.equals(url)) {
                    if (securityDomain != null) {
                        throw new HarmoniaAuditMappingException("Duplicate security-domain extension found");
                    }
                    if (ext.hasValue()) {
                        if (ext.getValue() instanceof StringType) {
                            securityDomain = ((StringType) ext.getValue()).getValue();
                        } else {
                            throw new HarmoniaAuditMappingException("security-domain extension value must be StringType, got: " + ext.getValue().getClass().getSimpleName());
                        }
                    }
                } else if (HarmoniaAuditFhirConstants.EXTENSION_CORRELATION_ID.equals(url)) {
                    if (correlationId != null) {
                        throw new HarmoniaAuditMappingException("Duplicate correlation-id extension found");
                    }
                    if (ext.hasValue()) {
                        if (ext.getValue() instanceof StringType) {
                            correlationId = ((StringType) ext.getValue()).getValue();
                        } else {
                            throw new HarmoniaAuditMappingException("correlation-id extension value must be StringType, got: " + ext.getValue().getClass().getSimpleName());
                        }
                    }
                } else if (HarmoniaAuditFhirConstants.EXTENSION_CAUSATION_ID.equals(url)) {
                    if (causationId != null) {
                        throw new HarmoniaAuditMappingException("Duplicate causation-id extension found");
                    }
                    if (ext.hasValue()) {
                        if (ext.getValue() instanceof StringType) {
                            causationId = ((StringType) ext.getValue()).getValue();
                        } else {
                            throw new HarmoniaAuditMappingException("causation-id extension value must be StringType, got: " + ext.getValue().getClass().getSimpleName());
                        }
                    }
                } else if (HarmoniaAuditFhirConstants.EXTENSION_OPERATION_ID.equals(url)) {
                    if (operationId != null) {
                        throw new HarmoniaAuditMappingException("Duplicate operation-id extension found");
                    }
                    if (ext.hasValue()) {
                        if (ext.getValue() instanceof StringType) {
                            operationId = ((StringType) ext.getValue()).getValue();
                        } else {
                            throw new HarmoniaAuditMappingException("operation-id extension value must be StringType, got: " + ext.getValue().getClass().getSimpleName());
                        }
                    }
                } else if (HarmoniaAuditFhirConstants.EXTENSION_AUDIT_ATTRIBUTE.equals(url)) {
                    String key = null;
                    String val = null;
                    for (Extension sub : ext.getExtension()) {
                        if (HarmoniaAuditFhirConstants.SUB_EXTENSION_KEY.equals(sub.getUrl())) {
                            if (key != null) {
                                throw new HarmoniaAuditMappingException("Duplicate key sub-extension in audit-attribute");
                            }
                            if (sub.hasValue()) {
                                if (sub.getValue() instanceof StringType) {
                                    key = ((StringType) sub.getValue()).getValue();
                                } else {
                                    throw new HarmoniaAuditMappingException("audit-attribute key sub-extension value must be StringType, got: " + sub.getValue().getClass().getSimpleName());
                                }
                            }
                        } else if (HarmoniaAuditFhirConstants.SUB_EXTENSION_VALUE.equals(sub.getUrl())) {
                            if (val != null) {
                                throw new HarmoniaAuditMappingException("Duplicate value sub-extension in audit-attribute");
                            }
                            if (!sub.hasValue()) {
                                throw new HarmoniaAuditMappingException("Missing value in audit-attribute extension");
                            }
                            if (sub.getValue() instanceof StringType) {
                                val = ((StringType) sub.getValue()).getValue();
                            } else {
                                throw new HarmoniaAuditMappingException("audit-attribute value sub-extension value must be StringType, got: " + sub.getValue().getClass().getSimpleName());
                            }
                        }
                    }
                    if (key == null || key.isBlank()) {
                        throw new HarmoniaAuditMappingException("Missing or blank key in audit-attribute extension");
                    }
                    if (val == null) {
                        throw new HarmoniaAuditMappingException("Missing value in audit-attribute extension for key: " + key);
                    }
                    if (attributes.containsKey(key)) {
                        if (!Objects.equals(attributes.get(key), val)) {
                            throw new HarmoniaAuditMappingException("Conflicting attribute key: " + key + " (existing: " + attributes.get(key) + ", new: " + val + ")");
                        }
                    }
                    attributes.put(key, val);
                }
            }
        }

        // 16. source from AuditEvent.source
        AuditSource source = null;
        if (fhir.hasSource()) {
            String component = null;
            String subsystem = null;

            if (fhir.getSource().hasObserver() && fhir.getSource().getObserver().hasIdentifier()) {
                component = fhir.getSource().getObserver().getIdentifier().getValue();
            }

            if (fhir.getSource().hasType()) {
                for (CodeableConcept typeConcept : fhir.getSource().getType()) {
                    for (Coding coding : typeConcept.getCoding()) {
                        if (HarmoniaAuditFhirConstants.SYSTEM_SOURCE_SUBSYSTEM.equals(coding.getSystem())) {
                            if (subsystem != null) {
                                throw new HarmoniaAuditMappingException("Duplicate subsystem coding in source.type");
                            }
                            subsystem = coding.getCode();
                        }
                    }
                }
            }

            if (component != null || subsystem != null) {
                source = AuditSource.of(subsystem, component);
            }
        }

        // Note: meta.versionId, meta.lastUpdated, and meta.security are strictly ignored here.

        try {
            return new HarmoniaAuditEvent(
                    eventId,
                    recordedAt,
                    classification,
                    action,
                    outcome,
                    initiatingPrincipal,
                    executingPrincipal,
                    securityDomain,
                    target,
                    authorizationEvidence,
                    authorities,
                    securityLabels,
                    correlationId,
                    causationId,
                    operationId,
                    source,
                    attributes
            );
        } catch (IllegalArgumentException e) {
            throw new HarmoniaAuditMappingException("Failed to construct HarmoniaAuditEvent: " + e.getMessage(), e);
        }
    }

    private AuditPrincipal extractPrincipal(AuditEvent.AuditEventAgentComponent agent) {
        String principalId = null;
        String sourceDomain = null;
        PrincipalType principalType = null;

        if (agent.hasWho() && agent.getWho().hasIdentifier()) {
            Identifier id = agent.getWho().getIdentifier();
            principalId = id.getValue();
            sourceDomain = id.getSystem();
        }

        if (agent.hasType()) {
            for (Coding coding : agent.getType().getCoding()) {
                if (HarmoniaAuditFhirConstants.SYSTEM_PRINCIPAL_TYPE.equals(coding.getSystem())) {
                    if (coding.getCode() != null && !coding.getCode().isBlank()) {
                        try {
                            principalType = PrincipalType.valueOf(coding.getCode().trim().toUpperCase());
                        } catch (IllegalArgumentException e) {
                            throw new HarmoniaAuditMappingException("Unrecognized principal type: " + coding.getCode(), e);
                        }
                    }
                }
            }
        }

        return AuditPrincipal.of(principalId, principalType, sourceDomain);
    }
}
