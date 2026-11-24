<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task Detail — DR-18/DR-19 architectural acceptance and closure preparation

Task-Issued-At: UNRESOLVED

Event source: the operative user instruction entered this Codex interaction without
an exposed attributable system instruction-entry timestamp. The original event and
filename prefix remain unresolved for Solution Architect treatment before closure.
Current/capture/Report time, filesystem metadata and prior execution events are not
substitutes. The formal acceptance is explicit in the instruction below; its event
time is not stated and is not inferred. This temporal qualification does not replace
or negate the attributable architectural acceptance.

HCRL boundary: 1.1.19/001, DR-18 and DR-19. This instruction follows the
[amended-candidate Task](unresolved-mneme-post-create-availability-amendment.md) and
[Report](../../after/ai-agent-reports/20261103T011842Z-mneme-post-create-availability-amendment.md).
Earlier evidence/production events are preserved. No architecture, version,
publication or Git mutation is authorised by this record/assessment activity.

## Operative issued instruction (verbatim, outer code fence omitted)

# Harmonia — HCRL 1.1.19-001
## DR-18 / DR-19 — Architectural Acceptance and Closure Preparation

### 1. Goal

Record the Solution Architect's formal acceptance of the consolidated DR-18/DR-19 architectural content and prepare HCRL 1.1.19-001 for governed closure.

The architectural review is complete.

### 2. Expected Outcome

The accepted architectural content is recorded in the existing HCRL, together with the verification evidence, preserved unresolved design matters and outstanding governance qualifications.

Codex shall determine whether the remaining closure requirements have been satisfied or require further Solution Architect disposition.

**Do not close the HCRL until all mandatory closure requirements are satisfied or explicitly dispositioned under the governing Praxis.**

### 3. Context — Solution Architect Acceptance

The Solution Architect formally accepts the architectural content of the following six definitions:

| Definition | Acceptance |
|---|---|
| Provenance — DataObject | Accepted |
| Provenance Client Library — ApplicationComponent | Accepted |
| Dokimasia — ApplicationComponent | Accepted |
| Kleio — ApplicationComponent | Accepted |
| Mneme — ApplicationComponent | Accepted, including the post-creation cache-availability amendment |
| Mnemosyne — ApplicationComponent | Accepted |

The Solution Architect also accepts the associated DR-18/DR-19 architectural relationships and responsibility allocations as represented in the reviewed candidate.

#### Mneme cache-availability policy

The following property and its three values are accepted:

`Cache{InformationUnit}.postCreateAvailability`

- `Immediate-And-Distributed`
- `Immediate`
- `OnRequest`

The semantics, invariants and unresolved design details are those recorded in the amended Mneme canonical definition.

No further architectural amendment is requested.

### 4. Constraints

Preserve all existing architecture governance requirements.

In particular:

- Canonical Markdown remains authoritative.
- ArchiMate remains the governed projection.
- Existing canonical identities, relationships and Views must be preserved.
- The six accepted definitions must not be substantively changed.
- All previously recorded unresolved design matters remain unresolved.
- No new architectural decisions, implementation mechanisms or runtime contracts are authorised.
- Existing DR-1–DR-17 dispositions remain unchanged.
- Do not weaken validation or publication controls.

Architectural acceptance does not constitute acceptance of unrelated verification defects or governance deviations.

### 5. Authorised Activity / Scope

#### 5.1 Record architectural acceptance

Update DR-18 and DR-19 within HCRL 1.1.19-001 to record the Solution Architect's explicit architectural acceptance.

Preserve the distinction between:

- Accepted architectural content.
- Outstanding detailed-design questions.
- Verification qualifications.
- Governance and closure obligations.

#### 5.2 Reconcile verification evidence

Retain the completed verification evidence, including:

- 87 passing architecture tooling tests.
- Four passing final focused tests.
- 90 passing Java architecture tests.
- Successful Archi 5.10 import/save/export.
- The inherited strict transport-verification failure.

The strict transport verifier's `xml:lang` failure remains a recorded qualification.

Its reported scope is unchanged: 575 relationship identities and 1,741 attribute differences.

Do not represent this check as passing.

Determine whether the existing governance process permits closure with this qualification and identify any required Solution Architect disposition.

#### 5.3 Review remaining governance obligations

Identify and report the status of:

1. The inherited strict Archi transport-verification qualification.
2. The unresolved original Task event timestamp and filename-prefix provenance.
3. Any outstanding reader-facing structural or visual review required by the governing Praxis.
4. Any other mandatory HCRL closure requirements.

Do not invent historical timestamps or retrospectively manufacture evidence.

Where an obligation remains unsatisfied, identify the exact governing requirement and the disposition required.

#### 5.4 Preserve unresolved design matters

Retain all six original DR-18/DR-19 unresolved questions.

Also retain the unresolved determination of `postCreateAvailability`, including per-creation and governing-metadata possibilities.

Do not convert these into accepted design decisions or make their resolution an implicit prerequisite for accepting the architectural content.

#### 5.5 Prepare closure

If all mandatory closure conditions are satisfied, report that HCRL 1.1.19-001 is ready for closure.

If any mandatory condition remains unresolved, leave the HCRL open and identify the required action or Solution Architect decision.

Do not independently authorise a waiver or deferral.

### 6. Report

Produce a governed closure-readiness report using the established evidence conventions.

The report shall include:

1. Recorded Solution Architect architectural acceptance.
2. DR-18 and DR-19 dispositions.
3. Confirmation that accepted architectural content remains unchanged.
4. Verification results and qualifications.
5. Outstanding governance obligations and their governing references.
6. Required Solution Architect decisions, if any.
7. Closure-readiness determination.

Record the working report under `.junie/reports` and preserve governed evidence in the established HCRL evidence structure.

### 7. Authority and Execution Boundary

**Authorised:**
- Record architectural acceptance.
- Reconcile the existing evidence.
- Prepare the HCRL for closure.
- Identify remaining governance decisions.

**Not authorised:**
- Unilateral waiver of verification failures.
- Fabrication or retrospective reconstruction of missing evidence.
- Substantive architecture changes.
- HCRL closure while mandatory conditions remain unresolved.
- Accepted-baseline publication, version advancement, commit or push before closure authorisation.

Return the closure-readiness assessment to the Solution Architect for final disposition.
