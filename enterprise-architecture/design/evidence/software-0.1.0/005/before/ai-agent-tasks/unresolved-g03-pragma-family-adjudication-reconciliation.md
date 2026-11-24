Task-Issued-At: UNRESOLVED

Event source: Instruction-entry timestamp unavailable; SA treatment required

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — G03 Architectural Reconciliation of the Pragma family · Iteration 005

The operative instruction below supplies the Solution Architect's candidate-design adjudications. The starting state is branch `feature/BL-CALLIOPE-OO-01` at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`), with pre-existing worktree edits and the five candidates pinned in the [review basis][review-basis]. Architecture 1.1.26 is the accepted baseline. [Cumulative HCRL.004 / DR-12][hcrl]

## Operative instruction

# Harmonia — G03 Architectural Reconciliation

**Software release:** 0.1.0  
**Review iteration:** 005  
**Architecture baseline:** 1.1.26  
**HCRL:** `hcrl-software-0.1.0-004.pending.md`

## 1. Objective

Reconcile the five Pragma-family DRPs against the Solution Architect's adjudications and the existing governed Audit and Logging architecture.

Use `G03-Pragma-Family-Cross-Document-Reconciliation-005.md` and its PF-01–PF-12 exception register as the review basis.

Produce revised candidate documents and a clear exception-disposition report.

**Do not modify application code or accepted architecture.**

## 2. Scope

Review and reconcile:

1. `DRP-Mneme-Pragma-Model.draft.000.003.md`
2. `DRP-Mneme-Pragmaton-Model.draft.000.003.md`
3. `DRP-Pragma-Publication-and-Subscription.draft.000.002.md`
4. `DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.000.003.md`
5. `DRP-Interaction-Petasos-to-Mneme.draft.000.003.md`

Locate and consult, without modifying, the existing governed Kleio Audit, Audit Framework, Mneme Audit and Harmonia Logging design material.

Prefer bounded document inspection and relevant sections rather than loading unrelated architectural documents.

## 3. Architectural adjudications

### 3.1 Component responsibilities

| Responsibility | Owner | Worker | Involved |
|---|---|---|---|
| Publication eligibility | Ponos | Mneme | — |
| Subscription matching | Petasos | Petasos | Mneme |
| Pragmatosis generation | Petasos | Mneme | — |
| Queue routing | Petasos | Petasos | — |
| Queue management | Petasos | Petasos | — |

Maintain separate Themis PEP/PDP responsibilities at the appropriate protected boundaries.

Functional ownership does not establish authorisation.

### 3.2 Mneme

Mneme provides governed information services.

Mneme SHALL NOT own or interpret:

- Workflow progression.
- Praxis outcome rules.
- Subscription matching.
- Queue routing.
- Queue management.
- Ergon execution decisions.

Mneme may execute an information-management operation requested by another component without assuming ownership of that component's functional decisions.

### 3.3 PragmaFactory

PragmaFactory belongs to the Mneme engine.

It responds to construction requests from:

- Ponos.
- DigitalTwinEngine.
- Petasos.

It constructs the requested Pragma-family information objects and applies their governed information-model invariants.

It is not a workflow engine, subscription engine or router.

### 3.4 Publication

Every newly created InformationUnit is subject to the producing Ergon's PublishingMask.

The default PublishingMask is `ALLOW_ALL`.

Ponos owns publication eligibility; Mneme performs the evaluation.

A non-match is a normal publication disposition, not an information-management or processing failure.

The InformationUnit remains governed.

### 3.5 Subscription matching

Petasos owns and performs subscription matching.

Mneme provides governed information required for evaluation.

R1/R2 matching uses Pragma and InformationUnit summary/metadata information.

R3+ may additionally evaluate authorised InformationUnit payload content.

Themis governs the applicable information access and disclosure.

Mneme does not interpret SubscriptionMask semantics.

### 3.6 Pragmatosis construction and delivery

Petasos requests construction of subscriber-specific Pragmatosis objects through Mneme's PragmaFactory.

Each matching subscription establishes its corresponding undertaking, subject to applicable identity and retry rules.

PetasosParcel carries the Pragmatosis identity, not an InformationUnit reference or payload.

The intended Ergon queue is already determined by the subscription.

Petasos manages routing and durable delivery.

The receiving Ergon resolves the undertaking through Mneme.

### 3.7 Cache and workspace

Preserve:

- Standard Cache: distributed, reference-oriented IU access.
- Workflow Cache: execution-local working information.
- Authoritative persistence: Mnemosyne, not either cache.

`workspace()` represents the operational existence pocket of an Ergon execution.

Distinguish:

- Pragmatosis: governed undertaking.
- Pragmaton: Mneme-managed execution-information environment.
- WorkSpace: Ergon-facing operational access to that environment.

Do not equate WorkSpace with a cache or create a third cache.

### 3.8 TaskOutcome

TaskOutcome concerns Ergon-instance processing.

Praxis defines the applicable outcome rules.

Preserve the distinctions between:

- Individual execution outcome.
- Undertaking disposition.
- Output mediation.
- Assurance.
- Originating Pragma disposition.

Support concurrent equivalent processing, recovery and no-subscriber circumstances.

Do not fabricate an Ergon TaskOutcome when no Ergon execution occurred.

Do not invent a final canonical representation where one remains unresolved.

## 4. Petasos–Kleio audit integration

### 4.1 Existing architectural authority

Consult the existing governed audit and logging design material.

Preserve:

- Kleio ownership of authoritative Audit Information Management.
- Kleio ownership of Audit Capture.
- Client submission of semantically meaningful auditable occurrences.
- Kleio responsibility for constructing FHIR AuditEvent.
- Separation of AuditEvent, LogEvent, Provenance and Assurance.
- Mandatory authoritative durable audit capture.
- Fail-closed behaviour when mandatory capture cannot be established.

Do not apply Log-Satellite delivery semantics to authoritative audit evidence.

### 4.2 Local audit integration

The Solution Architect has identified a Kleio Audit.Agent/Audit.Satellite associated with each Petasos-Engine instance as the intended local audit integration.

Reconcile this proposal with existing governed Kleio architecture.

Do not invent duplicate audit components where equivalent constructs already exist.

If the exact Audit.Agent/Audit.Satellite identity is not established in the governed architecture, record the modelling gap rather than inventing an accepted component.

Petasos contributes auditable occurrences associated with its own operations.

Mneme contributes separate audit evidence for its governed information operations.

Do not require Petasos operational events to be reconstructed from Mneme information mutations.

### 4.3 Artemis-based asynchronous audit capture

Investigate and document the following candidate architecture:

1. The local Kleio Audit Capture integration submits auditable occurrences to a durable Apache ActiveMQ Artemis audit queue.
2. Artemis provides durable capture and acknowledgement.
3. Kleio consumes accepted occurrences asynchronously.
4. Kleio constructs and manages the authoritative FHIR AuditEvent representation.
5. An occurrence is removed from durable staging only after the applicable downstream durability and acknowledgement requirements are satisfied.

**Architectural design constraint:**

Where Artemis provides durable storage for Petasos work and Kleio audit capture, the durability assurance for mandatory audit evidence SHALL be no weaker than that required for durable Petasos work.

This includes applicable transaction acknowledgement, persistence, recovery, replication and failure-handling guarantees.

Equivalent durability does not imply equivalent retention or lifecycle semantics.

### 4.4 Transactional integration

Investigate whether Artemis can atomically commit:

- A PetasosParcel to its destination queue.
- The corresponding auditable occurrence to the Kleio audit queue.

Where technically applicable, describe the transaction boundary, prerequisites and limitations.

Do not assume an Artemis transaction also commits Mneme/Mnemosyne state.

Distinguish attempted, committed, failed and indeterminate operations.

### 4.5 Audit capture authority

The proposed refinement is to recognise a Kleio-governed durable staging mechanism as the authoritative capture boundary, with subsequent AuditEvent processing performed asynchronously.

**This refinement remains subject to explicit architectural review.**

Do not silently rewrite the existing mandatory Audit Capture invariant.

Identify any changes required to establish the proposed capture boundary without weakening the existing durability, authority and evidence-preservation requirements.

### 4.6 Artemis implementation investigation

Using authoritative Artemis documentation, investigate:

- Durable queues and journal configuration.
- Transactional sends and commits.
- Commit acknowledgement guarantees.
- Asynchronous acknowledgement facilities.
- Duplicate detection and replay behaviour.
- Broker failover and recovery.
- Broker notifications and plugins where relevant.
- Relevant limitations and configuration-dependent guarantees.

Distinguish verified Artemis capabilities from proposed Harmonia implementation choices.

Do not implement the integration.

## 5. Reconciliation and outputs

### 5.1 Adjudication register

Create:

`G03-Pragma-Family-Adjudications-005.md`

Map the architectural decisions to PF-01–PF-12.

Identify each exception as:

- Resolved.
- Partially resolved.
- Unresolved.
- Editorial only.

Retain outstanding matters explicitly.

### 5.2 Revised DRPs

Produce sequentially versioned revisions of the affected DRPs.

Preserve earlier versions unchanged.

Update cross-references consistently.

Do not modify unrelated DRPs.

For the Petasos–Kleio investigation, produce a separate bounded design note or candidate DRP if the findings materially exceed the existing interaction document's scope.

Do not automatically incorporate that candidate into the Continuum.

### 5.3 HCRL

Update:

`hcrl-software-0.1.0-004.pending.md`

Record the G03 reconciliation activity, resulting candidate revisions, remaining exceptions and evidence references.

Preserve earlier review history and qualifications.

DR-12 remains pending Solution Architect review.

## 6. Constraints

- No source-code modifications.
- No accepted Continuum modifications.
- No architecture publication.
- No commit, tag or push.
- No unapproved component invention.
- No assumed resolution of outstanding architectural questions.
- Preserve the existing evidence conventions.

## 7. Completion criteria

Return:

1. The revised DRP candidate filenames.
2. The adjudication register.
3. An updated PF-01–PF-12 exception disposition.
4. The Petasos–Kleio/Artemis investigation findings.
5. Any architectural questions requiring Solution Architect decision.
6. Confirmation that the HCRL remains pending.

**Do not request architectural acceptance on the basis of internally generated assumptions.**

[review-basis]: ../../after/G03-Pragma-Family-Cross-Document-Reconciliation-005.md
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
