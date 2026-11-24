# DesignNote — Kleio Audit Information Management

**Status:** Architectural Design Note  
**Scope:** Harmonia Information Systems Layer Architecture / Domain05  
**Subject:** Kleio responsibility, authoritative audit capture, FHIR AuditEvent representation, historical context, and longitudinal evidence preservation

## 1. Purpose

This DesignNote records the current architectural position for **Kleio**, Harmonia's Audit Information Management component.

The intent is to establish the component boundary and the governing information-management principles without prematurely defining Kleio's detailed API, internal processing model, persistence implementation, or eventual audit-analysis capabilities.

## 2. Architectural Responsibility

**Kleio is Harmonia's authoritative Audit Information Management component.**

Kleio is responsible for the authoritative capture, creation, management, preservation, retrieval and preparation of Harmonia audit information.

Kleio is distinct from:

- Governance, which establishes applicable rules, constraints and expectations;
- Security and UI security assurance, which establish and assess the security of interfaces and platform behaviour;
- Dokimasia, which performs governed assurance activity and produces assurance findings/results;
- Mneme, which provides governed information-management capabilities;
- Mnemosyne, which provides durable persistence responsibility.

Audit is not Governance.

Assurance and Audit are related but distinct responsibilities.

## 3. Audit Capture

Kleio owns **Audit Capture** as an application responsibility.

Harmonia components and clients identify that a semantically significant auditable occurrence has taken place and submit the relevant occurrence/context through a Kleio-provided client contract, library and/or API.

The client is not responsible for implementing Harmonia's authoritative audit-information model.

Conceptually:

```text
Client Component
      |
      | reports auditable occurrence
      v
Kleio Audit Capture API / Framework
      |
      v
Kleio
      |
      | constructs and manages
      v
Authoritative Audit Information
```

Kleio may delegate governed information management and durable persistence through Mneme and Mnemosyne, but that delegation does not transfer Kleio's application responsibility for audit information.

## 4. Canonical FHIR AuditEvent Representation

Kleio will ultimately represent authoritative audit information as **FHIR AuditEvent** resources.

A key architectural guardrail is:

> **Clients SHALL NOT be required to construct FHIR AuditEvent resources. Clients submit auditable occurrences through the Kleio Audit Capture contract. Kleio is responsible for constructing and managing the authoritative FHIR AuditEvent representation.**

This prevents FHIR AuditEvent construction and audit-model knowledge from being distributed across Iris, Energeia, Ponos, Petasos, Mneme, security components, and other audit-producing clients.

The detailed form of the Kleio client contract is deliberately unresolved. It may eventually use typed structures, an audit envelope, attachments, builders, contextual metadata, or another mechanism. That is downstream API/framework design.

## 5. Semantically Significant User Activity

Kleio captures and manages user activity that is significant to accountability, access, disclosure, information change, operational action or subsequent assurance.

Examples may include:

- access to or viewing of protected information;
- governed searches and relevant result access;
- creation, modification, deletion or submission of information;
- approvals, rejections or other attributable decisions;
- initiation or completion of significant operational work;
- significant disclosure or export activity;
- denied or prohibited actions where audit is required.

Kleio is **not** a general UI telemetry, clickstream or user-experience analytics service.

Mouse movement, rendering events, control interaction and other low-level UI telemetry are outside Kleio's audit responsibility unless separately determined to constitute a semantically significant auditable occurrence.

Security assurance of the UI itself is outside Kleio's responsibility.

## 6. Relationship to Journey and Dokimasia

Kleio may consume Journey and information produced by Dokimasia as sources of audit information and historical evidence.

Kleio also provides historical audit context back to Dokimasia for subsequent assurance activity.

Conceptually:

```text
Journey --------------------+
                            |
Dokimasia Findings/Results -+--> Kleio --> Historical Audit Context
                            |                    |
Other Audit Sources --------+                    v
                                              Dokimasia
```

This relationship does not make Kleio an assurance engine.

Kleio may perform substantial processing required to manage and prepare historical audit information. The detailed boundary of those future processing capabilities is deliberately left for subsequent design and implementation.

## 7. Audit Information versus Source Information

Kleio SHALL NOT duplicate authoritative information from other sources merely because that information is relevant to an audit event.

The preferred model is to reference authoritative information with sufficient identity, version, provenance and temporal detail to establish the information applicable at the audited point in time.

> **Reference by default; replicate only where necessary to preserve longitudinal audit evidence.**

Where authoritative information can be reliably resolved longitudinally, Kleio SHALL reference that information rather than clone it into the audit record.

Conceptually:

```text
AuditEvent
    |
    | reference + sufficient
    | identity/version/temporal detail
    v
Authoritative Information
    |
    +-- historical state remains reliably resolvable
```

This principle is consistent with the Information Architecture distinction between current information state and the information that was applicable at a historical point in time. Version identity alone must not be assumed to provide sufficient temporal location where the authoritative information model requires additional temporal metadata.

## 8. Longitudinal Evidence Exception

Where the authoritative source **cannot guarantee longitudinal retrieval** of the information as it existed at the relevant audited point in time, Kleio SHALL preserve the information necessary to maintain the integrity of the audit evidence.

In that circumstance, and only in that circumstance, the AuditEvent managed by Kleio may replicate or clone the required source information, for example within or in association with the AuditEvent.

Conceptually:

```text
Authoritative Source
    |
    X  historical state cannot be
    |  reliably retrieved later
    v

AuditEvent
    |
    +-- preserved snapshot / clone
        of the information required
        as longitudinal audit evidence
```

Replication is therefore an **exception required by evidentiary preservation**, not a convenience mechanism and not a general audit-data duplication strategy.

The replicated content SHOULD be limited to the information necessary to preserve the required audit evidence.

The detailed FHIR mechanism by which such information is carried, attached, referenced or associated with AuditEvent is intentionally not defined by this DesignNote.

## 9. Persistence Responsibility

Kleio owns the application responsibility for Audit Information Management.

Governed information management and persistence may be delegated:

```text
Kleio
  Audit Information Management
            |
            v
Mneme
  Governed Information Management
            |
            v
Mnemosyne
  Harmonia Operations & Assurance
  Persistence Responsibility
```

Audit information is operational/transactional historical information and aligns with the **Harmonia Operations & Assurance** persistence responsibility rather than Harmonia Configuration & Governance.

This does not make Mnemosyne responsible for audit semantics.

## 10. Architectural Guardrails

The following guardrails apply:

1. **Audit is not Governance.**
2. **Kleio owns authoritative Audit Information Management.**
3. **Audit Capture is a Kleio responsibility exposed through an authoritative client contract/library/API.**
4. **Clients SHALL NOT be required to construct FHIR AuditEvent resources.**
5. **Kleio constructs and manages the authoritative FHIR AuditEvent representation.**
6. **Kleio captures semantically significant auditable activity, not general UI telemetry.**
7. **Kleio may consume Journey, Dokimasia outputs and other relevant audit sources without assuming ownership of those source constructs.**
8. **Kleio may provide historical audit context to Dokimasia without becoming the assurance decision-maker.**
9. **Kleio SHALL reference authoritative source information rather than duplicate it where longitudinal resolution can be guaranteed.**
10. **Source information SHALL be replicated into/with the audit evidence only where necessary because the authoritative source cannot guarantee longitudinal retrieval of the relevant historical state.**
11. **Persistence may be delegated through Mneme/Mnemosyne without transferring Kleio's application responsibility.**
12. **Detailed client API/framework design, FHIR attachment/reference mechanics, internal content-processing algorithms and physical persistence design remain downstream concerns.**

## 11. Deliberately Unresolved

This DesignNote does not determine:

- the detailed Kleio Audit Capture API;
- client-side library/framework implementation;
- the detailed mapping from client audit submissions to FHIR AuditEvent;
- the FHIR mechanism used to preserve cloned evidence where required;
- detailed audit retention policy;
- detailed correlation/indexing strategy;
- detailed historical-context preparation algorithms;
- the eventual extent of Kleio's internal audit-information processing or analytical functions;
- physical database/schema design;
- O&A shard keys or deployment topology.

These matters may be resolved by subsequent Information Systems design, detailed solution design and implementation without weakening the architectural responsibility and evidence-preservation principles established here.

## 12. Summary

Kleio is Harmonia's authoritative Audit Information Management component.

Clients report semantically significant auditable occurrences through Kleio's Audit Capture contract; Kleio constructs and manages the authoritative FHIR AuditEvent representation. Kleio preserves and retrieves audit history and may prepare that history as context for subsequent Dokimasia assurance.

Kleio does not become a duplicate store of authoritative source information. Where source information remains longitudinally resolvable, the AuditEvent references it with sufficient identity and temporal detail. Only where that historical state cannot be guaranteed does Kleio preserve a required snapshot or clone as part of the audit evidence.

**Reference by default; replicate only where necessary to preserve longitudinal audit evidence.**
