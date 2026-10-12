# DesignNote — Kleio Audit Information Management and Resilient Audit Capture

**Status:** Architectural Design Note  
**Scope:** Harmonia Information Systems Layer Architecture / Domain05 Candidate Architecture Step A  
**Subject:** Kleio responsibility, authoritative audit capture, FHIR AuditEvent representation, immutability, longitudinal evidence, resilient persistence, and R1.x/R2.x scope

## 1. Purpose

This DesignNote records the current architectural position for **Kleio**, Harmonia's authoritative Audit Information Management component.

It establishes the responsibility boundary and architectural invariants needed for Candidate Architecture Step A without prematurely defining Kleio's detailed client API, internal processing model, persistence topology, reconciliation mechanisms, indexing strategy, or future historical-context processing.

## 2. Architectural Responsibility

**Kleio is Harmonia's authoritative Audit Information Management component.**

Kleio is responsible for:

- authoritative Audit Capture;
- creation and management of authoritative audit information;
- canonical representation of audit events as FHIR `AuditEvent`;
- preservation and retrieval of audit history;
- management of semantically significant user and system audit activity;
- provision and preparation of historical audit context for subsequent operational and assurance use.

Kleio is distinct from:

- **Governance**, which establishes rules, constraints, permissions and expectations;
- **Dokimasia / Assurance**, which assesses observed activity against applicable expectations and produces assurance findings/results;
- **UI/security assurance**, which governs and assesses security of interfaces, sessions, devices and related mechanisms;
- **Mneme**, which provides governed information-management capability;
- **Mnemosyne**, which provides durable persistence responsibility.

**Audit is not Governance. Audit is not Assurance.**

## 3. Audit Capture

Kleio owns **Audit Capture** as an application responsibility.

A Harmonia client identifies that a semantically significant auditable occurrence has taken place and reports the occurrence and necessary context through a Kleio-provided contract, library and/or API.

The client reports **what occurred**. It does not own Harmonia's authoritative audit representation.

```text
Client Component
      |
      | reports auditable occurrence
      v
Kleio Audit Capture Contract
      |
      v
Kleio
      |
      | constructs and manages
      v
Authoritative Audit Information
```

The detailed form of the client contract is deliberately unresolved.

## 4. Canonical FHIR AuditEvent Representation

Kleio will ultimately represent authoritative audit events as **FHIR AuditEvent** resources.

> **Clients SHALL NOT be required to construct FHIR AuditEvent resources. Clients submit auditable occurrences through the Kleio Audit Capture contract. Kleio is responsible for constructing and managing the authoritative FHIR AuditEvent representation.**

This centralises FHIR AuditEvent knowledge and audit consistency within the component that owns Audit Information Management.

FHIR `AuditEvent` is the authoritative audit-event representation. It does **not** necessarily define the entirety of Kleio's internal audit-information structures. Kleio may require indexes, correlation structures, prepared historical context or other internal/supporting representations without making those structures new authoritative domain facts.

## 5. Audit Immutability and Subsequent Correction

Authoritative audit information managed by Kleio is immutable.

> **An existing authoritative AuditEvent SHALL NOT be corrected, amended, replaced or overwritten.**

Where an earlier AuditEvent is subsequently determined to be incorrect, incomplete, misleading, or otherwise requires qualification, Kleio SHALL create a **new AuditEvent** that refers back to the earlier AuditEvent and records the subsequent assertion.

```text
AuditEvent A
  original authoritative assertion
        |
        | remains immutable
        v
AuditEvent B
  subsequent correction /
  qualification / superseding assertion
        |
        +---- references AuditEvent A
```

The existence of a later correction does not erase the fact that the earlier assertion was made. The original event remains part of Harmonia's audit history.

This is consistent with Harmonia's broader historical-truth position: subsequent information may alter what Harmonia now understands, but it does not rewrite the information Harmonia previously held or asserted.

## 6. Semantically Significant User Activity

Kleio captures and manages user activity that is significant to accountability, access, disclosure, information change, operational action, decision-making or subsequent assurance.

Examples may include:

- access to or viewing of protected information;
- governed searches and relevant result access;
- creation, modification, deletion or submission of information;
- approvals, rejections and other attributable decisions;
- initiation or completion of significant operational work;
- significant disclosure or export activity;
- denied or prohibited actions where audit is required.

Kleio is **not** a general UI telemetry, clickstream or user-experience analytics service.

Security assurance/governance of the UI itself is outside Kleio's responsibility.

## 7. Journey and AuditEvent Are Distinct

Journey and AuditEvent are not equivalent constructs.

```text
Journey
    governed representation of observed
    Harmonia activity/progression

AuditEvent
    authoritative audit representation
    of a significant auditable occurrence
```

A Journey may be associated with many AuditEvents. AuditEvents may refer to activity represented by a Journey. Some auditable occurrences, particularly user-interface access or denied activity, may occur without meaningful Journey participation.

> **Journey participation SHALL NOT be a prerequisite for Audit Capture.**

Journey is an important source of audit-relevant information; it is not the audit boundary.

## 8. Relationship to Dokimasia

Kleio may consume Journey information and Dokimasia outputs as sources of audit information and historical evidence.

Kleio may also provide historical audit context to Dokimasia for subsequent assurance activity.

```text
Dokimasia -------> Kleio
 assurance         audit information
 outputs            management
                       |
                       | historical context
                       v
                    Dokimasia
```

This feedback relationship does not make Kleio an assurance engine.

Kleio may eventually perform substantial correlation, selection, contextualisation or other processing required to prepare historical audit information. The detailed processing boundary is deliberately unresolved.

One invariant applies:

> **Authoritative audit evidence SHALL remain distinguishable from any derived, correlated, contextualised or otherwise prepared historical view produced from that evidence.**

## 9. Audit Information versus Authoritative Source Information

Kleio SHALL NOT duplicate authoritative information from other sources merely because that information is relevant to an audit event.

The preferred model is to reference authoritative information with sufficient identity, version, provenance and temporal detail to establish the information applicable at the audited point in time.

> **Reference by default; replicate only where necessary to preserve longitudinal audit evidence.**

Where authoritative information can be reliably resolved longitudinally, Kleio SHALL reference that information rather than clone it into the audit record.

```text
AuditEvent
    |
    | reference + sufficient identity /
    | version / provenance / temporal detail
    v
Authoritative Information
    |
    +-- historical state remains reliably resolvable
```

Version identity alone SHALL NOT be assumed to provide sufficient temporal location where the authoritative information model requires additional temporal metadata.

## 10. Longitudinal Evidence Exception

Where the authoritative source cannot guarantee longitudinal retrieval of the information as it existed at the relevant audited point in time, Kleio SHALL preserve the information necessary to maintain the integrity of the audit evidence.

In that circumstance, and **only** in that circumstance, Kleio may replicate or clone the required source information within, or in association with, the authoritative AuditEvent.

```text
Authoritative Source
    |
    X historical state cannot be
    | reliably retrieved later
    v

AuditEvent
    |
    +-- preserved snapshot / clone
        required for longitudinal
        audit evidence
```

Replication is an evidentiary-preservation exception, not a convenience mechanism and not a general audit-data duplication strategy.

The replicated content SHOULD be limited to the information necessary to preserve the required audit evidence.

The detailed FHIR mechanism used to carry, attach, reference or associate such preserved information is deliberately unresolved.

## 11. Authoritative Auditability Is a Precondition of Harmonia Operation

Authoritative audit capture is an essential Harmonia service, not an ancillary or best-effort capability.

> **Harmonia SHALL NOT perform an auditable activity unless the required audit information can be authoritatively and durably captured.**

> **If Kleio cannot authoritatively capture required audit information, Harmonia is DOWN.**

The inability to establish required audit evidence is therefore a platform-level loss of essential Harmonia service and SHALL result in fail-closed behaviour.

Audit information SHALL NOT be treated as something that may simply be reconstructed later from operational logs, Journey information, database state or other incidental evidence.

Detailed transactional, queuing, retry, health/readiness and recovery mechanisms used to satisfy this invariant remain downstream design concerns.

## 12. Resilient Audit Persistence

Kleio may rely upon multiple independent authoritative audit persistence targets provided through Mneme/Mnemosyne.

The loss of an individual persistence target does not necessarily make Harmonia unavailable.

For R1.x/R2.x:

> **The availability of at least one configured authoritative audit persistence target capable of durably establishing the required audit information is sufficient for Harmonia to continue auditable processing.**

```text
                         Kleio
                           |
                  Authoritative AuditEvent
                           |
              +------------+------------+
              |            |            |
              v            v            v
         Audit Store A  Audit Store B  Audit Store C
              |            |            |
            working       failed       failed
              |
              v
       audit established
       Harmonia continues
```

Where no configured authoritative audit persistence target can durably establish the required audit information:

```text
Audit Store A   Audit Store B   Audit Store C
    failed          failed          failed
       \              |              /
        +-------------+-------------+
                      |
                      v
                 Kleio cannot
              establish audit
                      |
                      v
                HARMONIA DOWN
```

These targets SHALL NOT be described as replicas unless and until convergence/replication semantics are actually established. For R1.x/R2.x they are **independent authoritative audit persistence targets**.

## 13. R1.x/R2.x Cross-Store Harmonisation Is SEP

Multiple writable/authoritative audit persistence targets do not imply that Harmonia R1.x/R2.x must reconcile, replicate or converge their contents.

> **For R1.x/R2.x, audit data harmonisation between independent Mnemosyne audit persistence targets is explicitly outside Harmonia scope — Somebody Else's Problem (SEP).**

R1.x/R2.x does not guarantee that independent audit persistence targets:

- contain identical audit histories;
- contain complete copies of all AuditEvents;
- converge automatically;
- reconcile after disconnection;
- provide a unified cross-target longitudinal audit history.

An AuditEvent remains an authoritative immutable audit assertion even where the individual persistence target containing that event does not possess the complete Harmonia audit corpus.

An administrator querying different independent persistence targets may therefore observe different audit histories in R1.x/R2.x. This is an accepted consequence of the release-scope decision, not by itself an architectural defect.

Future releases may introduce harmonisation, reconciliation, convergence, replication or unified longitudinal audit-history capabilities. No such mechanism is selected or implied by this DesignNote.

## 14. Persistence Responsibility

Kleio retains application responsibility for Audit Information Management.

Governed information management and durable persistence may be delegated:

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
            |
            +-- Independent Audit Target A
            +-- Independent Audit Target B
            `-- Independent Audit Target C
```

The exact number of persistence targets is not prescribed by this DesignNote.

Audit information aligns with the **Harmonia Operations & Assurance (O&A)** persistence responsibility.

Persistence responsibility does not confer audit semantic/application ownership upon Mnemosyne.

## 15. Architectural Guardrails

1. **Audit is not Governance.**
2. **Audit is not Assurance.**
3. **Kleio owns authoritative Audit Information Management.**
4. **Audit Capture is a Kleio application responsibility exposed through an authoritative client contract/library/API.**
5. **Clients SHALL NOT be required to construct FHIR AuditEvent resources.**
6. **Kleio constructs and manages the authoritative FHIR AuditEvent representation.**
7. **Authoritative AuditEvents are immutable.**
8. **Corrections/qualifications SHALL be represented by new AuditEvents referring to earlier AuditEvents; existing events SHALL NOT be rewritten.**
9. **Kleio captures semantically significant auditable activity, not general UI telemetry.**
10. **Journey and AuditEvent are distinct; Journey participation is not required for Audit Capture.**
11. **Authoritative audit evidence SHALL remain distinguishable from derived/prepared historical context.**
12. **Kleio SHALL reference authoritative source information rather than duplicate it where longitudinal resolution can be guaranteed.**
13. **Source information SHALL be cloned only where necessary because the authoritative source cannot guarantee longitudinal retrieval of the relevant historical state.**
14. **Authoritative auditability is a precondition of Harmonia operation.**
15. **If required audit information cannot be authoritatively and durably captured, Harmonia SHALL fail closed.**
16. **For R1.x/R2.x, one available configured authoritative audit persistence target is sufficient for continued operation.**
17. **For R1.x/R2.x, cross-target audit harmonisation/reconciliation/convergence is explicitly SEP.**
18. **Independent authoritative audit persistence targets SHALL NOT be assumed to be replicas or complete copies of the audit corpus.**
19. **Persistence may be delegated through Mneme/Mnemosyne without transferring Kleio's application responsibility.**
20. **Detailed persistence topology, target-selection logic, retry/failover mechanics, cross-target consistency and future harmonisation remain downstream concerns.**

## 16. Deliberately Unresolved

This DesignNote does not determine:

- the detailed Kleio Audit Capture API;
- client-side library/framework implementation;
- detailed mapping from client audit submissions to FHIR AuditEvent;
- the FHIR mechanism used to preserve cloned evidence where required;
- detailed retention, archival, disposal or legal-hold policy;
- audit indexing/query architecture;
- detailed Journey/Praxis/task/user/resource correlation schema;
- detailed historical-context preparation algorithms;
- the eventual extent of Kleio's internal audit-information processing;
- transaction coordination between originating activity and Audit Capture;
- persistence-target selection strategy;
- whether a healthy event is written to one, some or all available targets;
- retry/failover mechanics;
- health/readiness determination;
- physical database/schema/deployment design;
- cross-target harmonisation, replication, reconciliation or convergence;
- future unified longitudinal audit-history architecture.

These matters may be resolved later without weakening the architectural invariants established here.

## 17. Summary

Kleio is Harmonia's authoritative Audit Information Management component.

Clients report semantically significant auditable occurrences through Kleio's Audit Capture contract. Kleio constructs and manages the authoritative FHIR AuditEvent representation. AuditEvents are immutable: subsequent correction or qualification is represented by a new AuditEvent referring to the earlier event.

Kleio does not become a duplicate store of authoritative source information. It references authoritative information with sufficient temporal detail wherever longitudinal retrieval can be guaranteed and clones information only where required to preserve longitudinal audit evidence.

Authoritative auditability is a precondition of Harmonia operation. R1.x/R2.x may use multiple independent authoritative audit persistence targets, and the continued availability of at least one target is sufficient for operation. If no target can durably establish required audit evidence, Harmonia is DOWN.

Cross-target audit harmonisation is explicitly **SEP for R1.x/R2.x**.

**Reference by default. Preserve history rather than rewrite it. Audit must be durably capturable for Harmonia to operate.**
