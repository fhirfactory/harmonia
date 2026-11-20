Task-Issued-At: UNRESOLVED

Event source: The interaction did not expose the attributable instruction-entry timestamp. Solution Architect treatment required before closure.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Topic/TopicMask design consolidation and HCRL preparation

- **Assigned agent** · Codex
- **Issuing authority** · Solution Architect, as identified in the operative instruction in this interaction
- **Governed boundary** · `software-0.1.0/004`, design consolidation and review preparation
- **Relevant backlog provenance** · [BL-CALLIOPE-OO-01, revision 1](../../../../../../../docs/backlog/calliope-oo-01-topic-models.md). The earlier [assignment-specific Backlog Item 00001, revision 001](../../../../../../../docs/backlog/BacklogItem-0001-Calliope-Model-Restructure.001.md) is carried with its original scope treatment in [HCRL 001](../../../../../review/software-0.1.0/hcrl-software-0.1.0-001.pending.md).
- **Repository correspondence** · The supplied iteration 004 adjudications are physically present as [F-01-Topic-TopicMask-Adjudications-Continuation-004.md](../F-01-Topic-TopicMask-Adjudications-Continuation-004.md). The issued locator below is preserved verbatim.

## Operative instruction

````markdown
# Harmonia — Topic/TopicMask Design Consolidation and HCRL Preparation

**Repository:** `fhirfactory/harmonia`  
**Branch:** `feature/BL-CALLIOPE-OO-01`  
**Software release:** `software-0.1.0`  
**Iteration:** `004`  
**Authority:** Solution Architect  
**Status:** Authorised for documentation consolidation and review preparation.

## 1. Objective

Complete the incorporation of the Solution Architect's accepted F-01 adjudications through F-01.81.

Produce the following two principal deliverables:

1. `DRP-Topic-and-TopicMask.draft.001.003.md`
2. `hcrl-software-0.1.0-004.pending.md`

The new HCRL must preserve outstanding review matters from the preceding software HCRL and introduce a specific review activity for the revised Topic/TopicMask DRP.

This activity prepares the design for formal Solution Architect review and acceptance.

It does not authorise implementation or architectural publication.

## 2. Authoritative Inputs

### 2.1 Existing design

Read:

`enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.draft.001.002.md`

Also consult the accepted predecessor and earlier draft where necessary to verify design lineage.

Do not modify historical versions.

### 2.2 Iteration 004 adjudications

Read:

`enterprise-architecture/design/evidence/software-0.1.0/004/before/F-01-Topic-TopicMask-Adjudications-Continuation.md`

This contains the Solution Architect's accepted decisions F-01.75–F-01.81.

These decisions govern the reconciliation of the outstanding matters from iteration 003.

### 2.3 Previous HCRL

Locate and read the current preceding software review record, including:

`enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-001.pending.md`

Also inspect any later software HCRL, if present.

Establish the actual preceding review state before constructing the new HCRL.

**Every unresolved matter in the preceding HCRL must be carried forward unless an explicit, attributable Solution Architect disposition authorises its closure.**

Do not assume that an issue has been resolved merely because it does not relate directly to F-01.

## 3. Design Consolidation

Create:

`enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.draft.001.003.md`

Use Draft.001.002 as the immediate predecessor.

Integrate F-01.75–F-01.81 into the relevant sections of the design.

Do not simply append the new adjudications.

### 3.1 API semantics — F-01.75

Incorporate the accepted rules concerning:

- Strict canonical version parsing.
- Topic naming projections.
- Hierarchy-navigation argument handling.
- Universal TopicMask accessor semantics.

Ensure the proposed Java surface agrees with these decisions.

### 3.2 Runtime identity — F-01.76

Confirm:

- Topic identity derives from its non-versioned canonical name.
- Architectural identities are separate.
- IU UUID identity remains independent.
- Unqualified Topic names need not be globally unique.
- Legacy UUID references require explicit compatibility or migration treatment.
- Sequence indices are construction mechanisms, not independent identity components.

### 3.3 TopicMask compatibility — F-01.77

Incorporate the accepted migration principles:

- Structural mask identity.
- Explicit universal matching.
- Exact/Inclusive correspondence where equivalent.
- Explicit treatment of legacy name-based selectors.
- RE2/J compatibility assessment.
- Preservation of selection scope.
- Fail-closed behaviour for invalid or ambiguous migration inputs.

### 3.4 IU Topic designation — F-01.78

Preserve:

- The governed IU Topic designation.
- Its correspondence to the Topic's unqualified `getName()` representation.
- A distinct qualified canonical representation.
- Independent IU UUID identity.
- Separation of TopicEntry versions from IU and content versions.

The exact qualifier vocabulary remains subject to separate definition.

Do not invent or silently authorise that vocabulary.

### 3.5 Architectural identity — F-01.79

Preserve existing architectural element identifiers and governed canonical identities.

Distinguish these from runtime Topic identities and naming projections.

Do not introduce architectural identifiers into the runtime Topic model.

### 3.6 Dependency allocation — F-01.80

Incorporate:

- RE2/J dependency allocation to the implementing Maven module.
- Topic/TopicMask independence from Themis.
- Use of Harmonia's application-logging abstraction.
- No unnecessary RE2/J wrapper.
- Independent treatment of existing `themis-api` dependency concerns.

### 3.7 Logging under backpressure — F-01.81

Incorporate:

- Synchronous Boolean matching.
- Fail-closed error behaviour.
- No synchronous remote-delivery requirement for routine TRACE diagnostics.
- Existing Log-Satellite delivery responsibilities.
- No TopicMask-specific logging infrastructure.
- Bounded and measured backpressure.
- Reliable ERROR diagnostics.
- Mandatory failure, saturation and recovery verification.

### 3.8 Preserve implementation qualifications

The architectural dispositions resolve R1–R6 at the decision level.

They do not establish that migration, compatibility, dependency allocation, RE2/J behaviour or logging reliability has been implemented and verified.

Retain these as explicit implementation and acceptance obligations.

## 4. HCRL Preparation

Create:

`enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-004.pending.md`

Follow the repository's established HCRL structure, references and review conventions.

### 4.1 Carry-forward requirements

Inspect the preceding software HCRL and reproduce its outstanding matters in the new review record.

For each carried matter:

- Preserve its original identification and provenance.
- Preserve its accepted or pending disposition.
- Record any relevant intervening evidence.
- Identify whether it remains open, has an authorised disposition or requires further review.
- Do not fabricate a resolution.
- Do not remove matters merely because they fall outside the immediate Topic/TopicMask design.

Maintain traceability to the preceding HCRL.

### 4.2 New DRP review activity

Add a distinct review item for:

`DRP-Topic-and-TopicMask.draft.001.003.md`

The review item must identify:

**Review objective:** Determine whether the revised Topic/TopicMask design correctly incorporates all accepted adjudications and is suitable for Solution Architect acceptance.

**Review basis:**

- Accepted Final.001.000 predecessor.
- Draft.001.001 and Draft.001.002 lineage.
- F-01.1–F-01.81 adjudications.
- Iterations 002, 003 and 004 evidence.
- Applicable Architecture Continuum contracts.
- Relevant implementation-gap and architectural-reconciliation findings.

**Acceptance considerations:**

1. Completeness and consistency of the revised design.
2. Correct incorporation of F-01.75–F-01.81.
3. Preservation of historical decisions and explicit supersessions.
4. Reconciliation of R1–R6.
5. Identification of remaining compatibility and migration obligations.
6. Completeness of mandatory implementation acceptance criteria.
7. Absence of unauthorised implementation or architectural changes.

**Initial disposition:** Pending Solution Architect review.

Do not mark the DRP accepted.

### 4.3 HCRL completeness

The HCRL must represent the broader outstanding software review state, not merely the new F-01 review activity.

If a preceding matter cannot be reconciled, retain it explicitly as unresolved and report the problem.

Do not silently drop review obligations.

## 5. Iteration 004 Evidence

Use:

`enterprise-architecture/design/evidence/software-0.1.0/004/`

Preserve the supplied adjudications under `before/`.

Produce execution and reconciliation evidence under `after/`, following repository governance.

At minimum, include:

- Adjudication incorporation and decision-delta evidence.
- Updated architectural reconciliation.
- Updated implementation qualifications and acceptance obligations.
- HCRL carry-forward reconciliation.
- Execution report.

Prefer exception-based reporting.

Avoid duplicating unchanged iteration 003 analysis unnecessarily.

## 6. Validation

Verify that:

1. Every F-01.75–F-01.81 decision is incorporated.
2. Draft.001.003 is internally consistent.
3. Historical DRPs remain unchanged.
4. Historical evidence remains unchanged.
5. The new HCRL carries all outstanding predecessor issues.
6. The Topic/TopicMask review activity is explicitly pending.
7. R1–R6 architectural decisions are distinguished from implementation verification.
8. All references and document links resolve.
9. The repository's applicable evidence and review validation tools have been executed.

Report any checker failures, including missing prerequisites, without modifying governance rules to conceal them.

## 7. Governance Constraints

Do not:

- Modify Java source code.
- Modify tests or Maven configuration.
- Modify accepted Architecture Continuum content.
- Modify historical DRPs or historical iteration evidence.
- Change previously accepted adjudications.
- Initiate architectural publication.
- Accept or complete the HCRL.
- Commit or push without explicit authorisation.

The new DRP and HCRL remain pending Solution Architect review.

## 8. Completion Report

Return an exception-based report covering:

1. Draft.001.003 creation and adjudication coverage.
2. HCRL.004 creation.
3. Complete predecessor HCRL carry-forward reconciliation.
4. New Topic/TopicMask review activity.
5. Outstanding review and implementation matters.
6. Validation results.
7. Any governance exceptions.
8. Exact files created or modified.

**Stop at the Solution Architect review boundary.**
````
