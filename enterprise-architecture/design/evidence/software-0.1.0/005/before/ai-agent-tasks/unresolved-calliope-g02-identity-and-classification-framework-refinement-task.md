Task-Issued-At: UNRESOLVED

Event source: Codex interaction. The attributable original instruction-entry timestamp was not exposed. The Task event and filename prefix require Solution Architect treatment before closure.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Calliope iteration 005 G02 identity and classification framework refinement

The Solution Architect issued the following clarification in the Codex interaction after reviewing Draft.000.001. The instruction is scoped to Draft.000.002 and its evidence. The originating worktree was on `feature/BL-CALLIOPE-OO-01` at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`). HCRL.004 separately records the reviewed candidate and the historical [BL-CALLIOPE-OO-01 revision 1][backlog]. [Reviewed Draft.000.001][prior-draft] · [Original G02 Task][prior-task] · [Original G02 Report][prior-report] · [HCRL.004][hcrl]

## Operative instruction

~~~markdown
# Harmonia — Calliope Review Iteration 005
## G02 — Identity and Classification Framework Refinement

**Repository:** `fhirfactory/harmonia`  
**Branch:** `feature/BL-CALLIOPE-OO-01`  
**Architecture baseline:** 1.1.26  
**Software baseline:** `Software-Version-0.1.0-Snapshot-004`

### Objective

Revise:

`DRP-Calliope-Identity-and-Classification-Framework.draft.000.001.md`

Create:

`DRP-Calliope-Identity-and-Classification-Framework.draft.000.002.md`

Preserve all previously agreed A03–A05 decisions.

This is a targeted clarification following Solution Architect review.

### 1. Topic identity — definitive clarification

The Solution Architect confirms:

**Topic is a reusable value datatype, not an independently identifiable architectural entity.**

Consequently:

- Topic has no independently assigned ID or UUID attribute.
- Topic equality and identity semantics follow the previously accepted Topic value semantics.
- Equivalent Topics can be constructed independently without acquiring separate identities.
- Topic does not own the identity or lifecycle of the object using it.
- TopicMask retains its previously adjudicated structural and deterministic semantics.

Do not reopen the 81 accepted Topic/TopicMask adjudications.

### 2. InformationUnit

Confirm that:

- `InformationUnit.id` is its independently assigned, immutable UUID.
- `InformationUnit.informationProfile` is its immutable ConceptFDN.
- InformationProfile identifies the IU concept and preserves architectural provenance independently of implementation structures.
- An IU has no InstanceFDN.
- A Topic representation may be deterministically derived from InformationProfile and IU ID when required.
- The derived Topic introduces no second IU identity.

Preserve the existing InformationRepresentation, ContainerMetadata, ContentMetadata and Content responsibilities.

### 3. Historical architectural correspondence

Architecture Continuum 1.1.26 includes an earlier Topic contract with an independently assigned Topic UUID.

This is an **accepted historical architectural contract requiring governed reconciliation**, not a requirement to perpetuate independent Topic identity in the redesigned runtime model.

Distinguish:

1. Historical references with independently established preservation obligations.
2. Existing architecture definitions requiring amendment.
3. Disposable implementation representations with no demonstrated preservation obligation.

Do not discard genuinely governed historical references.

Do not reinstate Topic UUIDs in the redesigned datatype merely to reproduce the historical implementation or architecture.

Record the required Continuum amendments and correspondence obligations.

### 4. A05

Preserve the agreed deterministic framework philosophy without modification:

- Well-defined model elements.
- Governed value sets.
- Value-set generators.
- Explicit validation.
- Deterministic conversion.
- Documented extension mechanisms.
- No speculative interpretation engine.

### 5. Evidence and governance

Update the G02 reconciliation and Iteration 005 issue-group evidence as necessary.

Preserve G01 and G03–G07.

Do not modify:

- Implementation code.
- Accepted Continuum content.
- Accepted DRPs.
- HCRL.004.
- Topic/TopicMask Draft.001.003.

Do not create HCRL.005.

Do not commit or push.

### Deliverables

1. `DRP-Calliope-Identity-and-Classification-Framework.draft.000.002.md`
2. Updated G02 evidence where required.
3. Concise report of changes and outstanding obligations.

**Stop for Solution Architect review.**

Do not independently promote the DRP to an acceptance candidate or Final.
~~~

[backlog]: ../../../../../../../docs/backlog/calliope-oo-01-topic-models.md
[prior-draft]: ../../../../../offline-design/DRP-Calliope-Identity-and-Classification-Framework.draft.000.001.md
[prior-task]: unresolved-calliope-g02-identity-and-classification-framework-task.md
[prior-report]: ../../after/ai-agent-reports/20261120T042553Z-calliope-g02-identity-and-classification-framework-report.md
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-1-whole-of-calliope-scope-and-candidate
