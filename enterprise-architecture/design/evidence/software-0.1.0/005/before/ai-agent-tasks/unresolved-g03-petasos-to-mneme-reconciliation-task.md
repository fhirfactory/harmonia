Task-Issued-At: UNRESOLVED

Event source: The Codex interaction exposes the operative instruction and its attribution without a system-recorded instruction-entry time. Solution Architect treatment is required before closure.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — G03 Petasos-to-Mneme interaction reconciliation

The Solution Architect requested the next Petasos–Mneme interaction draft and bounded reconciliation under Iteration 005. The starting worktree was on `feature/BL-CALLIOPE-OO-01` at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`), with Architecture 1.1.26. The [source draft][previous] and the earlier G03 candidate reviews in [DR-12][review] supply the lineage.

## Operative instruction

The following preserves the issued wording inside the enclosing message fence.

~~~
# Harmonia — Iteration 005 / G03
## Petasos-to-Mneme Interaction Reconciliation

### Objective

Create:

`DRP-Interaction-Petasos-to-Mneme.draft.000.003.md`

Use `DRP-Interaction-Petasos-to-Mneme.draft.002.md` as the starting point.

Reconcile the existing durable work-handoff interaction with the agreed Pragma, Pragmatosis, publication/subscription and Pragmaton architectures.

This is an architectural documentation and reconciliation activity.

### 1. Review basis

Review the following DRPs:

- `DRP-Interaction-Petasos-to-Mneme.draft.002.md`
- `DRP-Mneme-Pragma-Model.draft.000.003.md`
- `DRP-Mneme-Pragmaton-Model.draft.000.003.md`
- `DRP-Pragma-Publication-and-Subscription.draft.000.002.md`
- `DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.000.003.md`

Review the relevant Iteration 005 G03 reconciliation evidence.

Consult the accepted PetasosParcel, Mneme–Mnemosyne persistence, information-management, security and recovery contracts where necessary.

Use bounded excerpts. Do not load large RAD documents unnecessarily.

### 2. Work identity and handoff

Document the agreed sequence:

1. Mneme identifies subscriptions matching an eligible originating Pragma.
2. Mneme's Router/DecisionEngine causes PragmaFactory to establish one subscriber-specific realisation per matching subscription.
3. Each Pragmatosis, or specialised Noesis, has its own identity, lifecycle, outcome and lineage.
4. Mneme establishes the authoritative, recoverable state of that realisation through the governed Mnemosyne interaction.
5. A PetasosParcel carries the reference to the established realisation and sufficient governed handoff metadata.
6. Petasos performs durable transport and delivery.
7. On receipt, the consumer resolves the existing realisation through Mneme.
8. Pragmaton is subsequently established or reconstructed when governed execution requires its working information.

**The receiving consumer does not create a new Pragmatosis merely because a parcel has been delivered.**

Preserve the distinction between work identity, transport representation and working information.

### 3. Durability and acknowledgement

Preserve the independent semantics of:

- Authoritative persistence of the realised undertaking.
- Petasos durable acceptance.
- Petasos delivery and acknowledgement.
- Ergon execution.
- Mneme information save.
- Authoritative InformationUnit persistence.

An acknowledged durable handoff requires an authoritative, resolvable undertaking and its necessary lineage.

However, authoritative persistence alone does not establish successful Petasos delivery.

Identify the minimum architectural acknowledgement and evidence requirements.

Do not invent a transaction coordinator, persistence schema or specific messaging protocol.

### 4. Duplicate delivery and recovery

Document the required behaviour for:

- Multiple subscriptions matching the same originating Pragma.
- Multiple subscriptions associated with the same subscriber.
- Partial fan-out completion.
- Indeterminate persistence or transport outcomes.
- Repeated handoff attempts.
- Duplicate delivery.
- Redelivery following consumer failure.
- Ergon execution failure.
- Retry after an applicable `DoNotBefore` constraint.

Preserve the identity of an existing undertaking when recovering or retrying the same work obligation.

Distinguish duplicate delivery recognition from execution idempotence.

An indeterminate handoff must not automatically create another subscriber-specific undertaking.

Recovery must not resume inside a failed Ergon.

### 5. Pragmaton relationship

Clarify that Petasos does not transport Pragmaton or WorkSpace contents.

Petasos transports the reference to the durable undertaking and necessary handoff information.

When execution starts, Ergon.ContextLoader collaborates with Mneme to establish the undertaking's Pragmaton.

`Ergon.WorkSpace` exposes that managed working information to the ProcessingEngine.

The Workflow Cache supports transient materialisation.

Working information can be reconstructed from governed sources and appropriate durable execution state.

Do not duplicate the detailed Pragmaton or Ergon–Mneme interaction specifications.

### 6. Security, provenance and Assurance

Preserve:

- Themis-governed authority at applicable handoff, resolution and execution boundaries.
- Trusted identity and security-context continuity.
- Pragma and Pragmatosis lineage.
- Source InformationUnit identity and revision correspondence.
- Subscriber and subscription association.
- Petasos delivery and acknowledgement evidence.
- Independent execution and persistence outcomes.
- Appropriate Kleio audit, provenance and Dokimasia Assurance correlation.

Transport metadata must not independently establish authority to access or execute governed information.

### 7. Historical reconciliation

Explicitly reconcile the existing Draft.002 wording concerning:

- Petasos identifying or reconstructing Pragma.
- Mneme establishing Pragma on receipt.
- Recovery of working information.
- Durable work acceptance.
- Duplicate delivery and execution identity.

Preserve historical meaning where compatible.

Where the new model changes previously qualified architectural semantics, record the discrepancy and required Solution Architect disposition.

Do not silently rewrite accepted Continuum authority.

### 8. Evidence and HCRL

Produce bounded reconciliation evidence identifying:

1. Preserved architectural semantics.
2. Incorporated decisions.
3. Superseded historical behaviour.
4. Remaining architectural questions.
5. Required follow-on DRP or Continuum reconciliation.

Update DR-12 in:

`hcrl-software-0.1.0-004.pending.md`

Preserve all existing HCRL issues and obligations.

### 9. Constraints

- Documentation and reconciliation only.
- No application-code changes.
- No accepted Continuum modifications.
- No changes to other DRPs.
- No implementation-specific schemas, algorithms or infrastructure.
- No commit, tag or push.
- Preserve previous revisions.
- Maintain Iteration 005 evidence conventions.
- Do not declare DR-12 accepted or closed.

### Expected result

A Petasos–Mneme interaction DRP that consistently describes durable handoff of an existing subscriber-specific undertaking, with reliable identity, lineage, acknowledgement, recovery and execution separation.

The result remains pending Solution Architect review.
~~~

[previous]: ../../../../../offline-design/DRP-Interaction-Petasos-to-Mneme.draft.002.md
[review]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
