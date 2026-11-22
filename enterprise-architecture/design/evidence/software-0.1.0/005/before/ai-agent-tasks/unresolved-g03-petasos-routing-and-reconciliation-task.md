Task-Issued-At: UNRESOLVED

Event source: Instruction-entry timestamp unavailable; SA treatment required

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Petasos Routing and Reconciliation · G03 / Iteration 005

The operative Solution Architect instruction follows. The starting worktree was on `feature/BL-CALLIOPE-OO-01` at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`) with prior G03 evidence and pending cumulative HCRL.004. [DR-12][hcrl] · [Prior adjudications][prior]

## Operative instruction

# Harmonia — Petasos Routing and Reconciliation

**Architecture baseline:** 1.1.26  
**Software:** 0.1.0  
**Review:** G03 / Iteration 005 / DR-12

## Objective

Create:

`DRP-Petasos-Routing-and-Reconciliation.draft.000.001.md`

Capture the Solution Architect's routing and recovery strategy for:

- `Petasos.Subscription-Router`
- `Petasos.Praxis-Router`

Both routers use a common **durable decision and reconciliation strategy**, while retaining their distinct functional responsibilities.

Reuse existing architectural concepts and contracts. Do not introduce an additional workflow, routing or HA/DR framework.

This is an architectural design activity, not software implementation authorisation.

## 1. Common routing and reconciliation strategy

Each router shall:

1. Determine the applicable routing obligations through its owning decision mechanism.
2. Establish a durable record of the decision and its complete obligation set through Mneme/Mnemosyne.
3. Request construction of the applicable Pragma-family objects through Mneme's PragmaFactory.
4. Inject the resulting work into the appropriate Petasos queues.
5. Record successful construction and queue-injection outcomes against the corresponding obligations.
6. Reconcile incomplete or indeterminate obligations following failure or interruption.

The durable decision record SHALL be established before attempting to fulfil its individual routing obligations.

Recovery SHALL operate against the recorded decision and obligations rather than silently re-evaluating potentially changed subscription or Praxis definitions.

Preserve stable work identities, partial completion, explicit uncertainty and evidence of acknowledged operations.

The common strategy does not mandate a generic routing-event superclass or a new workflow framework.

## 2. Petasos.Subscription-Router

### 2.1 Responsibility

Petasos owns subscription evaluation, matching, routing orchestration and delivery.

Mneme provides governed information access, construction and persistence.

### 2.2 Proposed operation

Capture the Solution Architect's intended sequence:

```java
// Petasos.Subscription-Router

SubscriptionCache subscriptionCache =
    parseSubscriptionCache();

SubscriberSet subscriberSet =
    determineSubscribers(subscriptionCache, publishedIU, pragma);

// Durable creation through Mneme.
SubscriptionIdEvent event =
    mneme.createSubscriptionIdEvent(subscriberSet);

// Initially, success of individual obligations is unknown.
for (SubscriberEntry entry : subscriberSet) {

    Pragmatosis undertaking =
        mneme.createNewPragmatosis(entry);

    queueNewPragmatosis(undertaking);

    mneme.updateSubscriptionIdEvent(
        event,
        entry,
        undertaking
    );
}
```

*Illustrative pseudocode; names and signatures are not canonical implementation contracts.*

### 2.3 SubscriptionIdEvent

`SubscriptionIdEvent` is the durable reconciliation record for the publication's subscription fan-out.

It captures:

- The originating publication and applicable identity.
- The resolved SubscriberSet.
- The applicable subscriber and subscription associations.
- The creation and queue-injection outcomes for each obligation.
- References to the resulting Pragmatosis instances.
- Sufficient correlation and recovery information.

The initial `Successful-Pragmatosis-Creation+RouteInjection` value is `NULL`.

`NULL` means successful completion has not been established. It does not prove that no operation occurred.

After an interruption, Petasos uses the existing SubscriptionIdEvent and its recorded obligations rather than repeating subscription evaluation against the current SubscriptionCache.

The exact representation of intermediate and indeterminate states remains subject to reconciliation.

## 3. Petasos.Praxis-Router

### 3.1 Explicit architectural decision

**Ponos owns the Praxis evaluation method/API service.**

**Petasos.Praxis-Router invokes that Ponos service.**

Ponos interprets the applicable Praxis and evaluates workflow progression.

Petasos does not independently implement or reinterpret the Praxis progression rules.

Petasos owns routing orchestration, construction requests, queue injection and reconciliation of the resulting routing obligations.

Mneme provides the governed information construction and persistence operations.

### 3.2 Responsibility allocation

| Responsibility | Owner | Worker | Involved |
|---|---|---|---|
| Praxis evaluation | Ponos | Ponos | Petasos |
| Praxis routing orchestration | Petasos | Petasos | Ponos |
| Pragma-family construction | Requesting component | Mneme | — |
| Queue injection | Petasos | Petasos | — |
| Routing reconciliation | Petasos | Petasos | Mneme |

Preserve the existing Themis PEP/PDP responsibilities at applicable protected boundaries.

### 3.3 Proposed operation

Capture the equivalent routing sequence:

```java
// Petasos.Praxis-Router

// Praxis interpretation belongs to Ponos.
PraxisRoutingResult result =
    ponos.evaluatePraxis(praxisContext);

// Durable creation through Mneme.
PraxisRoutingEvent event =
    mneme.createPraxisRoutingEvent(result);

for (PraxisRoutingEntry entry : result.entries()) {

    Pragma work =
        mneme.createPragma(entry);

    queueNewWork(work);

    mneme.updatePraxisRoutingEvent(
        event,
        entry,
        work
    );
}
```

*Illustrative pseudocode; names and signatures are not canonical implementation contracts.*

Do not assume every successor obligation necessarily requires a base Pragma rather than an appropriate specialised Pragma-family object. Preserve the existing work-model semantics.

### 3.4 Praxis routing decision record

The Praxis-Router requires an equivalent durable reconciliation record.

The proposed `PraxisRoutingEvent` name is provisional.

The record identifies:

- The triggering processing outcome or progression event.
- The relevant Praxis identity and revision.
- The progression decision returned by Ponos.
- The resulting successor-work obligations.
- The created Pragma-family object identities.
- The queue-injection outcomes.
- The applicable correlation and recovery information.

Ponos owns the interpretation and evaluation of Praxis.

Petasos owns the durable orchestration and reconciliation of the resulting obligations.

Mneme manages the information representation without interpreting the Praxis.

Once the Ponos evaluation result has been durably captured, recovery SHALL use that result rather than automatically re-invoking the Ponos evaluation API.

Any need to re-evaluate must be an explicitly governed new decision, not an implicit consequence of recovery.

## 4. Common failure and recovery analysis

Analyse both routers against the following cases:

| Failure point | Required consideration |
|---|---|
| Before durable routing-record creation | No durable routing decision has yet been established; determine how the original trigger is recovered. |
| After durable record creation | Recover the existing obligation set. |
| During PragmaFactory construction | Preserve indeterminate outcomes and reconcile against governed information identity. |
| After successful construction, before queue injection | Recover the existing work identity and outstanding injection obligation. |
| During Artemis queue injection | Distinguish confirmed failure, durable success and indeterminate commit. |
| After successful injection, before record update | Reconcile the queue obligation without assuming the recorded `NULL` means failure. |
| During routing-record update | Preserve uncertainty and reconcile against authoritative evidence. |
| Partial fan-out | Preserve successful sibling obligations while recovering incomplete obligations. |
| Definition changes | Do not silently reinterpret an existing decision using a newer subscription or Praxis definition. |
| Duplicate delivery | Preserve the distinction between repeated transport delivery and repeated execution. |
| Deliberate replay | Establish its separate governed identity and intended relationship to the original routing decision. |

### 4.1 Identity and reconciliation

Investigate the minimum identity and evidence requirements necessary to recover an existing Pragmatosis or successor work item.

Do not assume:

- That an indeterminate creation operation failed.
- That a missing routing-record update proves queue injection failed.
- That an Artemis queue lookup proves a message was never injected.
- That Artemis duplicate-detection caches provide permanent exactly-once guarantees.
- That retrying the same delivery authorises a second Ergon execution.

Prefer reuse of existing identities and contracts.

Do not invent a new distributed transaction framework.

### 4.2 Authoritative persistence and queue durability

Routing decision records and created Pragma-family objects are managed through Mneme/Mnemosyne.

Queue injection and delivery remain Petasos responsibilities.

Preserve the distinction between:

- Durable routing-decision persistence.
- Pragma-family object construction and authoritative persistence.
- Artemis queue acceptance.
- Delivery acknowledgement.
- Ergon execution.
- Subsequent information save.
- Authoritative information mutation.

The proposed `Mnemosyne.DurableInformation` / `Mnemosyne.DurableData` decomposition remains a separate pending architectural candidate.

Do not silently incorporate that proposal or treat it as accepted Continuum authority.

## 5. Component boundaries

Preserve these architectural responsibilities.

**Ponos**
- Owns Praxis evaluation semantics and the corresponding method/API service.
- Evaluates the applicable Praxis when invoked by Petasos.
- Retains its existing execution and workflow responsibilities.

**Petasos**
- Owns subscription matching.
- Invokes Ponos for Praxis evaluation.
- Owns routing orchestration.
- Requests creation of the required work objects.
- Manages queue injection and delivery.
- Reconciles incomplete routing obligations.

**Mneme**
- Provides governed information access.
- Owns PragmaFactory construction operations.
- Persists routing records and Pragma-family information through Mnemosyne.
- Does not interpret subscriptions, Praxis progression or queue-routing decisions.

**Mnemosyne**
- Establishes authoritative persistence under the applicable governed contract.

**Themis**
- Provides the applicable authorisation policy and decisions.

**Kleio**
- Captures required auditable occurrences associated with the operations of their respective owning components.

Do not transfer application semantics between these boundaries.

## 6. Existing architectural correspondence

Reconcile this proposal against the existing:

- Petasos architectural model.
- Ponos and Praxis responsibilities.
- Pragma and Pragmatosis models.
- PragmaFactory.
- Publication/subscription DRP.
- Petasos–Mneme interaction.
- PetasosParcel and ParcelMask definitions.
- Mneme/Mnemosyne contracts.
- TaskOutcome and workflow-progression semantics.
- Themis authorisation contracts.
- Kleio audit requirements.
- G03 PF-07 and PF-08 findings.

Use the existing accepted Continuum as the authority.

Where the proposed design differs from accepted architecture, identify the exact discrepancy and required architectural adjudication.

Do not silently modify accepted responsibilities.

## 7. Required outputs

### 7.1 Candidate DRP

Create:

`DRP-Petasos-Routing-and-Reconciliation.draft.000.001.md`

Include the two router designs, common reconciliation invariants, failure analysis, responsibility allocation and unresolved questions.

### 7.2 Reconciliation evidence

Create a bounded reconciliation report under the established Iteration 005 evidence structure.

Identify:

- Existing architectural correspondence.
- Proposed changes.
- Genuine inconsistencies.
- Resolved and unresolved PF-07/PF-08 questions.
- Requirements for subsequent design or implementation verification.

### 7.3 Cumulative HCRL

Update:

`hcrl-software-0.1.0-004.pending.md`

Add the candidate and its reconciliation evidence to DR-12.

Preserve all previous review dispositions and evidence references.

Do not claim acceptance.

## 8. Constraints

- No application-code changes.
- No accepted Continuum changes.
- No architecture publication.
- No new generic workflow framework.
- No additional HA/DR framework.
- No speculative changes to other DRPs.
- No commit, tag or push.
- Preserve existing document revisions.
- DR-12 remains pending Solution Architect review.

## 9. Completion criteria

Report:

1. The candidate DRP filename.
2. The reconciliation evidence filename.
3. The updated HCRL reference.
4. The remaining architectural questions.
5. Any discrepancies between the proposal and accepted Continuum responsibilities.

**The objective is to capture the agreed routing strategy, not to reopen its established functional ownership decisions.**

[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
[prior]: ../../after/G03-Pragma-Family-Adjudications-005.md
