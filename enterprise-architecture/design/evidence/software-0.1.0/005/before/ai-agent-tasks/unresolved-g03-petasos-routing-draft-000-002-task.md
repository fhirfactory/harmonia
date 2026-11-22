Task-Issued-At: UNRESOLVED

Event source: Instruction-entry timestamp unavailable; SA treatment required

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Petasos Routing and Reconciliation · Revision 002

The operative Solution Architect instruction follows. The starting worktree was on `feature/BL-CALLIOPE-OO-01` at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`). [Draft.000.001][predecessor] SHA-256 `c0930af415c09c63af4fd637e58118a1a2bcdc976fe2dc043b98ded9061a35dc` identifies the exact source revision. [G03 / Iteration 005 / DR-12][hcrl]

## Operative instruction

# Harmonia — Petasos Routing and Reconciliation: Revision 002

**Architecture baseline:** 1.1.26  
**Software:** 0.1.0  
**Review:** G03 / Iteration 005 / DR-12

## Objective

Revise:

`DRP-Petasos-Routing-and-Reconciliation.draft.000.001.md`

Create:

`DRP-Petasos-Routing-and-Reconciliation.draft.000.002.md`

Incorporate the Solution Architect's subsequent decisions concerning:

1. The creation and handover of originating Pragmas during Ergon Egress.
2. The operation of `Petasos.Praxis-Router`.
3. Recovery of the originating Pragma list using authoritative provenance.

Preserve the existing Subscription-Router design, common routing/reconciliation strategy, responsibility allocations and outstanding review qualifications.

This is a bounded architectural reconciliation activity.

## 1. Ergon Egress — originating Pragma creation

The Solution Architect has established the following conceptual sequence:

```java
List<Pragma> createdList =
    Ergon.Egress
        .PostBlockingOfDeadTasks()
        .PostPEP
        .CreatePragmaFromOutputList();

Ergon.Egress.HandoverToPetasos(createdList);

Petasos.doStuff();
```

The pseudocode expresses the architectural sequence, not canonical Java APIs.

### Required interpretation

Ergon Egress is responsible for producing the originating Pragma list from the eligible outputs of its completed processing.

The sequence preserves:

1. Dead-task blocking.
2. The applicable Themis PEP enforcement.
3. Construction of originating Pragmas through Mneme's PragmaFactory.
4. Governed persistence of the resulting Pragmas.
5. Handover of the created Pragma list to Petasos.
6. Subsequent routing by Petasos.

Do not relocate originating Pragma construction into Petasos.

Do not interpret the conceptual call chain as requiring a specific implementation structure.

The existing architectural distinctions between Ergon processing, TaskOutcome, work disposition, output publication and routing must remain intact.

## 2. Petasos.Subscription-Router

Preserve the existing agreed strategy.

Subscription-Router receives already-created originating Pragmas.

Petasos:

1. Evaluates the applicable subscriptions.
2. Establishes the SubscriberSet.
3. Durably creates the SubscriptionIdEvent through Mneme.
4. Requests subscriber-specific Pragmatosis construction through Mneme's PragmaFactory.
5. Injects the resulting undertakings into their designated queues.
6. Updates the durable reconciliation record.
7. Recovers incomplete obligations without repeating the original matching decision.

Preserve the distinction between originating Pragma identity and subscriber-specific Pragmatosis identity.

The previously identified construction, injection, acknowledgement and update uncertainty remains subject to the existing PF-07/PF-08 reconciliation requirements.

Do not otherwise redesign Subscription-Router.

## 3. Petasos.Praxis-Router

### 3.1 Architectural correction

**Praxis-Router routes existing originating Pragmas. It does not create new originating Pragmas.**

The previous Draft.000.001 illustrative call:

```java
Pragma work = mneme.createPragma(entry);
```

is incorrect for the agreed Praxis-Router operation and must be removed.

### 3.2 Praxis evaluation

Ponos owns the Praxis evaluation method/API service.

Petasos.Praxis-Router invokes that service.

A Praxis progression rule can specify that one or more existing output Pragmas from the preceding Ergon must be routed to identified subsequent Ergons.

The Praxis may include selection criteria or filters determining which output Pragma or Pragmas satisfy each progression rule.

The exact filter model and evaluation semantics remain design matters requiring reconciliation.

**Ponos evaluates these rules and selects the applicable existing Pragma identities.**

Petasos does not independently reinterpret the Praxis.

### 3.3 Conceptual operation

```java
// Petasos.Praxis-Router

PraxisRoutingResult result =
    ponos.evaluatePraxis(
        praxisContext,
        createdList
    );

// Durable routing decision and complete obligation set.
PraxisRoutingEvent event =
    mneme.createPraxisRoutingEvent(result);

for (PraxisRoutingEntry entry : result.entries()) {

    // Resolve the existing originating Pragma.
    Pragma existingPragma =
        mneme.getPragma(entry.pragmaId());

    // Route the existing work to the Praxis-selected destination.
    queueExistingPragma(
        existingPragma,
        entry.destinationErgon()
    );

    mneme.updatePraxisRoutingEvent(
        event,
        entry,
        existingPragma
    );
}
```

This pseudocode is illustrative and does not define canonical interfaces.

### 3.4 Identity and routing

Preserve the identity of each existing originating Pragma.

The durable Praxis routing record must capture:

- The triggering Ergon execution or progression occurrence.
- The applicable Praxis identity and revision.
- The Ponos evaluation result.
- The selected originating Pragma identities.
- The corresponding destination Ergon identities.
- The resulting routing obligations.
- Queue-injection outcomes and relevant evidence.
- Sufficient correlation and provenance for recovery.

A single originating Pragma may potentially satisfy multiple Praxis routing rules.

Distinct routing obligations must therefore remain identifiable without creating duplicate originating Pragmas.

The precise PetasosParcel/work-realisation correspondence for direct Praxis routing remains an explicit review question.

Do not invent a new Pragmatosis construction requirement merely to satisfy the existing parcel model.

## 4. Ergon Egress → Petasos handover recovery

### 4.1 Architectural decision

The Solution Architect has established that, if necessary, **authoritative provenance can be used to reconstruct the original `createdList`**.

This avoids requiring a separate durable handover record solely to preserve the list of originating Pragmas.

### 4.2 Recovery operation

Conceptually:

```java
List<Pragma> createdList =
    mneme.findPragmasByProvenance(
        originatingErgonExecutionIdentity,
        egressOccurrenceIdentity
    );

Ergon.Egress.HandoverToPetasos(createdList);
```

The pseudocode is illustrative.

It does not prescribe a new Mneme API or specific provenance-query implementation.

### 4.3 Required semantics

Recovery must:

1. Identify the particular originating Ergon execution and egress occurrence.
2. Retrieve the identities of the previously created originating Pragmas.
3. Reconstruct the original createdList from governed, authoritative provenance.
4. Preserve the original Pragma identities and their lineage.
5. Repeat or reconcile the Petasos handover as required.
6. Avoid generating additional originating Pragmas merely because the original handover outcome was uncertain.
7. Reconcile against existing durable Petasos routing decisions before initiating any new routing obligations.

The provenance correlation must distinguish legitimate repeated executions of the same Ergon or input Pragma.

### 4.4 Recovery qualifications

The strategy depends on authoritative provenance being sufficient to identify the complete output set associated with the relevant egress occurrence.

Identify the minimum required provenance and completion evidence.

In particular, distinguish:

- A complete originating Pragma set.
- A partially established output set.
- An indeterminate output-construction outcome.
- A completed output set whose Petasos handover acknowledgement was lost.
- A handover already processed by one or both Petasos routers.

Do not assume that the mere presence of some matching Pragmas proves that output construction was completed.

Preserve existing governed durability and acknowledgement requirements.

Do not introduce another durable coordination mechanism unless an actual unmet requirement is demonstrated.

## 5. Common routing and reconciliation

Retain the Draft.000.001 common strategy.

Both routers establish their routing decision and complete obligation set durably before fulfilling individual obligations.

Both reconcile incomplete routing against the recorded decision rather than silently re-evaluating potentially changed definitions.

However, explicitly distinguish their work-handling semantics:

| Concern | Subscription-Router | Praxis-Router |
|---|---|---|
| Input | Existing originating Pragmas | Existing originating Pragmas |
| Decision mechanism | Petasos subscription matching | Ponos Praxis evaluation API |
| Selection basis | Subscription definitions and applicable masks | Praxis progression rules and potential output filters |
| New originating Pragma construction | None | None |
| Subscriber-specific Pragmatosis construction | Yes, through Mneme | Not established by this design |
| Routed work | Subscriber-specific Pragmatosis | Selected existing originating Pragma |
| Durable routing record | SubscriptionIdEvent | PraxisRoutingEvent, provisional name |
| Queue injection and reconciliation | Petasos | Petasos |

Maintain distinct evidence for routing-decision persistence, governed work persistence, Artemis acceptance, delivery, execution and subsequent information mutation.

Do not presume atomicity across Mneme and Artemis.

## 6. Architectural reconciliation

Reconcile the revised proposal against the existing:

- Ergon Egress and output handling.
- Ponos progression and Praxis evaluation responsibilities.
- Pragma and Pragmatosis identities and lifecycle.
- Mneme PragmaFactory.
- Pragma provenance and lineage.
- Petasos–Mneme interaction.
- PetasosParcel and ParcelMask.
- Publication and subscription architecture.
- Themis PEP enforcement.
- TaskOutcome and work disposition.
- PF-07/PF-08 outstanding contract requirements.

Identify any accepted Continuum statements that conflict with the agreed responsibilities.

Do not silently change those authoritative elements.

In particular, do not confuse:

**Ergon output Pragma creation → Petasos handover → Subscription/Praxis routing → queue delivery → subsequent Ergon execution.**

These are distinct architectural operations with their own applicable evidence and recovery requirements.

## 7. Required outputs

### 7.1 Revised DRP

Create:

`DRP-Petasos-Routing-and-Reconciliation.draft.000.002.md`

Preserve Draft.000.001 unchanged.

### 7.2 Reconciliation evidence

Create a bounded revision reconciliation under:

`enterprise-architecture/design/evidence/software-0.1.0/005/after/`

Identify:

- The exact revisions and corrections made.
- Existing architectural correspondence.
- Outstanding PetasosParcel/work-realisation questions.
- Provenance completeness requirements.
- Remaining PF-07/PF-08 issues.

### 7.3 HCRL

Update the cumulative:

`hcrl-software-0.1.0-004.pending.md`

Under DR-12:

- Reference Draft.000.002.
- Record the agreed Praxis-Router correction.
- Record the authoritative-provenance recovery decision.
- Preserve the outstanding handoff and reconciliation qualifications.
- Retain all existing dispositions and evidence references.

## 8. Constraints

- No application-code modifications.
- No accepted Continuum modifications.
- No changes to existing accepted DRPs.
- No additional routing/workflow framework.
- No new HA/DR framework.
- No speculative API invention.
- No commit, tag or push.
- Preserve Draft.000.001 and all previous evidence.
- DR-12 remains pending Solution Architect review.

## 9. Completion report

Report:

1. Revised DRP path.
2. Reconciliation evidence path.
3. Updated HCRL reference.
4. Any unresolved conflicts with the accepted Continuum.
5. Remaining PF-07/PF-08 contract questions.

**The objective is to incorporate two agreed architectural decisions, not initiate another general review of Petasos.**

[predecessor]: ../../../../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.001.md
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
