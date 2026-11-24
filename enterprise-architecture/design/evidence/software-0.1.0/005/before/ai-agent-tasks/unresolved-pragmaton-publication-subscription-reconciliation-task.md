Task-Issued-At: UNRESOLVED

Event source: The Codex interaction exposes the operative instruction and its attribution without a system-recorded instruction-entry time. Solution Architect treatment is required before closure.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Pragmaton and publication/subscription DRP reconciliation

The Solution Architect supplied the [Pragmaton Draft.000.003 candidate][pragmaton] and requested reconciliation with the [publication/subscription Draft.000.001][publication], the Ergon–Mneme interaction, and cumulative HCRL.004 / DR-12. The originating worktree was based on Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`), with Architecture 1.1.26. This is a follow-up to the [original publication/subscription review][previous] in the same Iteration 005 evidence boundary. [DR-12][hcrl]

## Operative instruction

The following preserves the issued wording inside the message fence.

~~~
# Harmonia — Iteration 005
## Pragmaton and Publication/Subscription DRP Reconciliation

### Objective

Reconcile the recently revised Pragmaton architecture with the publication/subscription design and its associated review evidence.

The Solution Architect has prepared:

`DRP-Mneme-Pragmaton-Model.draft.000.003.md`

This revision incorporates the previously discussed JavaSpaces-inspired information-space model and the relationship between Pragmaton, `Ergon.WorkSpace`, Mneme's Workflow Cache and governed information access.

The revision is a **candidate DRP**, not an accepted architectural baseline.

### 1. Review scope

Review these documents:

1. `DRP-Mneme-Pragmaton-Model.draft.000.003.md`
2. `DRP-Pragma-Publication-and-Subscription.draft.000.001.md`
3. `DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.002.md`
4. `Pragma-Publication-and-Subscription-Reconciliation-005.md`
5. `hcrl-software-0.1.0-004.pending.md`

Consult additional existing architecture only where necessary to establish a specific correspondence or discrepancy.

Prefer bounded excerpts. Do not load large RAD documents unnecessarily.

### 2. Architectural decisions to preserve

**Pragma and Pragmatosis**

- `Pragma` represents archetypal work available for subscription.
- `Pragmatosis` represents a particular subscriber-specific realisation.
- `Stimulus → Noesis` is the Digital Twin specialisation.
- Each realisation has its own identity, lifecycle, outcome and lineage.

**Pragmaton**

- `Pragmaton` represents the Mneme-managed execution-information environment.
- It is distinct from Pragma and Pragmatosis.
- `Ergon.WorkSpace` is the Ergon-facing representation or access abstraction for Pragmaton.
- WorkSpace must not become another independently managed information construct.

**JavaSpaces-inspired semantics**

Pragmaton follows an associative information-space paradigm.

This includes contextualised information, identity-based and associative access, lazy resolution and governed acquisition of additional information during processing.

The analogy does not prescribe JavaSpaces technology, distributed tuple-space coordination or a specific implementation structure.

**Workflow Cache**

Pragmaton integrates with Mneme's Workflow Cache for transient, workflow-local working information.

It is not an authoritative persistence repository.

Preserve the existing distinction between the Standard Cache and Workflow Cache.

**Execution responsibilities**

- `Ergon.ContextLoader` establishes initial working information.
- `Ergon.ProcessingEngine` operates through `Ergon.WorkSpace`.
- `Ergon-InformationAdapter` mediates additional governed information access through Mneme.
- Themis continues to govern access through the applicable PEP/PDP mechanisms.
- Digital Twin and Pylai specialisations may establish their initial contexts differently.

**InformationUnit authority**

InformationUnit summaries carried within Pragmas and realisations are immutable, non-authoritative snapshots.

Working information within Pragmaton must not become an independent authoritative InformationUnit version.

### 3. Required reconciliation

#### A. Publication/subscription DRP

Revise `DRP-Pragma-Publication-and-Subscription.draft.000.001.md` to incorporate the clarified Pragmaton/WorkSpace relationship.

Update its references to the revised Pragmaton DRP.

Avoid duplicating the detailed working-information model. The Pragmaton DRP remains the conceptual home for that information-space architecture.

Create the next draft revision rather than overwriting the previous one.

#### B. Ergon-to-Mneme interaction

Review `DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.002.md`.

Determine whether its current interaction descriptions remain consistent with:

- Pragmaton contextualisation.
- WorkSpace access.
- ContextLoader responsibilities.
- Lazy information resolution.
- Ergon-InformationAdapter.
- Mneme Workflow Cache.
- Themis authorisation.
- Working-state output and authoritative persistence boundaries.

Identify any required corrections.

Do not silently change interaction contracts or invent new architectural interfaces.

#### C. Existing reconciliation evidence

Update `Pragma-Publication-and-Subscription-Reconciliation-005.md` to reflect the revised Pragmaton DRP.

Distinguish:

1. Architectural decisions now explicitly documented.
2. Historical discrepancies still requiring reconciliation.
3. Software-level design choices remaining open.
4. Genuine unresolved architectural questions.

Do not continue reporting an already-resolved conceptual relationship as an unresolved question merely because the accepted Continuum has not yet incorporated it.

Instead, distinguish **design agreement** from **architectural incorporation**.

### 4. HCRL

Update the cumulative:

`hcrl-software-0.1.0-004.pending.md`

Extend DR-12 to include review of the revised Pragmaton model and its incorporation into the publication/subscription design.

Preserve DR-1 through DR-11 and all existing DR-12 review obligations.

Do not mark DR-12 accepted or closed.

### 5. Deliverables

Produce:

1. Revised publication/subscription DRP.
2. Updated Iteration 005 reconciliation evidence.
3. Updated cumulative HCRL.004.
4. A bounded execution report identifying changes, remaining discrepancies and required Solution Architect decisions.

If changes to the Ergon-to-Mneme interaction DRP are necessary, propose the required amendments or create a clearly identified candidate revision for review.

### 6. Constraints

- Documentation and review preparation only.
- No application-code changes.
- No accepted Continuum modifications.
- No implementation of new interfaces.
- No automatic acceptance of draft DRPs.
- No commit, tag or push.
- Preserve previous document revisions and historical evidence.
- Do not reopen agreed terminology or architectural responsibilities without identifying a specific conflict with accepted authority.

**The purpose is reconciliation, not architectural reinvention.**
~~~

[pragmaton]: ../../../../../offline-design/DRP-Mneme-Pragmaton-Model.draft.000.003.md
[publication]: ../../../../../offline-design/DRP-Pragma-Publication-and-Subscription.draft.000.001.md
[previous]: unresolved-pragma-publication-and-subscription-task.md
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
