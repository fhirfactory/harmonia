Task-Issued-At: UNRESOLVED

Event source: The Codex interaction exposes the operative instruction and its attribution without a system-recorded instruction-entry time. Solution Architect treatment is required before closure.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — G03 Mneme Pragma model reconciliation

The Solution Architect requested the next Pragma Model draft and a bounded reconciliation under Iteration 005. The originating worktree was on `feature/BL-CALLIOPE-OO-01` at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`), with Architecture 1.1.26. The task follows the publication/subscription and Pragmaton candidate review already carried in [DR-12][hcrl]. [Pragma Model Draft.002][previous] · [Publication/subscription Draft.000.002][publication] · [Pragmaton Draft.000.003][pragmaton]

## Operative instruction

~~~
# Harmonia — Iteration 005 / G03
## Mneme Pragma Model Reconciliation

### Objective

Create:

`DRP-Mneme-Pragma-Model.draft.000.003.md`

Use `DRP-Mneme-Pragma-Model.draft.002.md` as the starting point.

Incorporate the architectural decisions established during the Pragma publication/subscription and Pragmaton reviews.

This is a documentation and architectural reconciliation activity only.

### 1. Review basis

Consult:

- `DRP-Mneme-Pragma-Model.draft.002.md`
- `DRP-Pragma-Publication-and-Subscription.draft.000.002.md`
- `DRP-Mneme-Pragmaton-Model.draft.000.003.md`
- Relevant existing Pragma Design Notes
- Applicable accepted Continuum definitions

Use bounded excerpts and preserve existing architectural decisions.

### 2. Required architectural semantics

**Pragma and Pragmatosis**

Distinguish the reusable definition of work, an originating archetypal Pragma available for subscription, and a subscriber-specific realisation.

- `Pragma` identifies the archetypal work construct.
- `Pragmatosis` identifies a particular subscriber-specific undertaking.
- Each Pragmatosis has independent identity, lifecycle, outcome and lineage.
- `Stimulus → Noesis` represents the Digital Twin specialisation.
- Dokimasia-specific names remain unresolved.

Reconcile these concepts against historical `ActionableTaskArchetype`, `ActionableTask`, `FulfillmentTask` and `TaskOutcome` terminology without inventing canonical equivalences.

**InformationUnit Input and Output**

- `Pragma.input()` represents exactly one InformationUnit.
- `Pragma.output()` represents zero or more generated InformationUnits.
- Embedded InformationUnit summaries are immutable, content-free and non-authoritative.
- Summaries retain source InformationUnit identity and relevant version correspondence.
- Summaries never establish independent authoritative InformationUnit versions.
- Output generation remains distinct from modification of existing working information.

**Pragmaton and WorkSpace**

- Pragmaton is the Mneme-managed execution-information environment associated with a realised undertaking.
- `Ergon.WorkSpace` provides the Ergon-facing access abstraction for that environment.
- Pragmaton is not synonymous with Pragma or Pragmatosis.
- The detailed JavaSpaces-inspired information-space semantics remain governed by the Pragmaton DRP.

**Lifecycle and scheduling**

Distinguish publication, subscription realisation, durable handoff, execution, completion and retry.

Preserve `DoNotBefore` scheduling semantics.

Do not introduce an implementation-specific lifecycle state machine.

**Ownership**

Mneme owns Pragma and Pragmatosis information management.

Petasos owns durable transport and handoff responsibilities.

Ponos and specialised Ergons perform execution.

Mnemosyne provides authoritative persistence through the governed Mneme interaction.

### 3. Document reconciliation

Preserve the existing DRP's architectural content.

Correct its subsection numbering where necessary.

Update its requirements, invariants and open questions to reflect decisions now established.

Reference the publication/subscription and Pragmaton DRPs rather than duplicating their detailed responsibilities.

Distinguish unresolved architectural questions from implementation choices.

### 4. Evidence and HCRL

Produce bounded reconciliation evidence identifying:

1. Preserved architectural semantics.
2. Newly incorporated decisions.
3. Historical discrepancies.
4. Remaining Solution Architect decisions.
5. Any required follow-on corrections to related DRPs.

Update **DR-12** in:

`hcrl-software-0.1.0-004.pending.md`

Preserve DR-1–DR-11 and all existing DR-12 obligations.

The DRP and HCRL remain pending Solution Architect review.

### 5. Constraints

- No implementation changes.
- No accepted Continuum modifications.
- No changes to other DRPs.
- No new architectural abstractions without explicit review.
- No commit, tag or push.
- Preserve previous draft revisions.
- Maintain the existing Iteration 005 evidence conventions.

### Expected result

A revised Pragma Model DRP that is semantically consistent with the publication/subscription and Pragmaton models, suitable for review alongside the remaining Pragma-family interaction DRPs.

Do not claim architectural acceptance or HCRL closure.
~~~

[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
[previous]: ../../../../../offline-design/DRP-Mneme-Pragma-Model.draft.002.md
[publication]: ../../../../../offline-design/DRP-Pragma-Publication-and-Subscription.draft.000.002.md
[pragmaton]: ../../../../../offline-design/DRP-Mneme-Pragmaton-Model.draft.000.003.md
