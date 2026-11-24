Task-Issued-At: UNRESOLVED

Event source: The Codex interaction exposes the operative instruction and its attribution without a system-recorded instruction-entry time. Solution Architect treatment is required before closure.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — G03 Ergon-to-Mneme Pragmaton interaction reconciliation

The Solution Architect requested the next Ergon–Mneme interaction draft and bounded reconciliation under Iteration 005. The starting worktree was on `feature/BL-CALLIOPE-OO-01` at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`), with Architecture 1.1.26. The [source Draft.002][previous] and prior [DR-12][review] supply the design and review lineage.

## Operative instruction

The following preserves the issued wording inside the enclosing message fence.

~~~
# Harmonia — Iteration 005 / G03
## Ergon-to-Mneme Pragmaton Interaction Reconciliation

### Objective

Create:

`DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.000.003.md`

Use `DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.002.md` as the source.

Reconcile its interaction contracts with the agreed Pragma, Pragmatosis and Pragmaton models.

### 1. Review basis

Review the following candidate documents:

- `DRP-Mneme-Pragma-Model.draft.000.003.md`
- `DRP-Mneme-Pragmaton-Model.draft.000.003.md`
- `DRP-Pragma-Publication-and-Subscription.draft.000.002.md`
- `DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.002.md`

Also consult:

- `G03-Mneme-Pragma-Model-Reconciliation-005.md`
- `Pragma-Publication-and-Subscription-Reconciliation-005.md`
- Relevant accepted Mneme cache, information-access and persistence contracts.
- Applicable Ergon authorisation decisions.

Use bounded source excerpts.

### 2. Architectural responsibilities

Preserve the following boundaries.

**Mneme**

- Owns Pragmaton contextualisation and governed information management.
- Provides governed information access and authoritative persistence coordination.
- Manages the Workflow Cache according to its accepted responsibility.
- Retains ownership of Pragma and Pragmatosis information.

**Ergon**

- Executes a particular subscriber-specific undertaking.
- Uses its ContextLoader to establish the initial processing context with Mneme.
- Uses its ProcessingEngine to perform bounded processing.
- Accesses working information through `Ergon.WorkSpace`.

**Pragmaton**

- Represents the Mneme-managed execution-information environment.
- Is associated with a particular realised undertaking.
- Supports contextualised, associative information access.
- Is not authoritative persistence.

**Ergon.WorkSpace**

- Is the Ergon-facing access abstraction for Pragmaton.
- Does not introduce another independently managed information construct.
- Supports access to initially loaded and subsequently acquired working information.

### 3. Interaction semantics

#### A. Establishment

Describe the interaction by which ContextLoader establishes an appropriate WorkSpace for the realised undertaking.

The initial information is contextualised through Mneme.

Preserve the applicable InformationUnit identity and version correspondence.

Digital Twin and Pylai specialisations may use different context-establishment mechanisms.

#### B. Processing and associative access

Document the JavaSpaces-inspired associative information-space semantics.

The WorkSpace is not restricted to a fixed collection of initially loaded values.

The ProcessingEngine may require additional information during execution.

Distinguish:

1. Access to information already present in Pragmaton.
2. Resolution of an existing InformationUnit reference.
3. Additional governed information acquisition.

Do not introduce JavaSpaces technology, distributed tuple-space coordination or new cache responsibilities.

#### C. Ergon-InformationAdapter

Document its logical responsibility for mediating additional governed access through Mneme.

The adapter must not bypass Mneme, Themis or authoritative information-management contracts.

Its correspondence to existing application interfaces remains subject to review.

Do not assign it independent persistence or save authority.

#### D. Workflow Cache

Document the relationship between Pragmaton and Mneme's Workflow Cache.

Preserve:

- Workflow-local information materialisation.
- Existing locality and isolation constraints.
- Authoritative version correspondence.
- Reconstruction from governed information and durable execution state.
- Existing materialisation and pass-by-reference requirements.

The Workflow Cache must not become authoritative persistence.

#### E. Security

Themis governs initial and subsequent information access through the applicable PEP/PDP mechanisms.

Possession of a reference, cached value or previous access decision must not independently establish authority for another operation.

Preserve appropriate denied, unavailable and indeterminate outcomes.

#### F. Completion and return

Distinguish:

- Changed existing working information.
- Newly generated InformationUnits.
- Pragma/Pragmatosis execution outcomes.

Context Unloader returns changed working information through the governed Mneme save interaction.

Mneme determines the associated information-management consequences.

New InformationUnits become Pragma Outputs and follow the separate PublishingMask process.

Local working-state modification or successful unload does not independently establish authoritative persistence success.

#### G. Failure and recovery

Preserve the existing rule that an Ergon does not resume internally after failure.

Recovery must use the applicable durable work identity, authoritative information and governed recovery/checkpoint arrangements.

Distinguish:

- Work execution outcomes.
- Information-access outcomes.
- Mneme save outcomes.
- Authoritative persistence outcomes.
- Petasos delivery outcomes.

### 4. Existing architectural correspondence

Review historical `Context`, `Load` and `Save` terminology.

Determine how the established Ergon/Ergo contextualisation responsibilities correspond to the revised interaction.

Do not automatically remove historical elements where they have accepted architectural meaning.

Replace the previous unresolved question concerning whether Ergon receives the whole Pragmaton or a view with the agreed WorkSpace abstraction.

Retain any genuinely unresolved access-contract or representation questions.

### 5. Evidence and HCRL

Produce bounded reconciliation evidence covering:

1. Preserved interaction semantics.
2. Incorporated decisions.
3. Historical discrepancies.
4. Remaining architectural questions.
5. Required downstream corrections, particularly Petasos–Mneme.

Update DR-12 in:

`hcrl-software-0.1.0-004.pending.md`

Preserve all existing review obligations.

### 6. Constraints

- Documentation only.
- No application-code changes.
- No accepted Continuum modifications.
- No changes to other DRPs.
- No implementation-specific interfaces or algorithms.
- No commit, tag or push.
- Preserve previous revisions.
- Maintain Iteration 005 evidence conventions.

The revised DRP remains pending Solution Architect review.

### Expected result

An Ergon–Mneme interaction DRP that consistently describes establishment, use, additional acquisition, completion, save and recovery of the Mneme-managed Pragmaton through `Ergon.WorkSpace`.
~~~

[previous]: ../../../../../offline-design/DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.002.md
[review]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
