Task-Issued-At: UNRESOLVED

Event source: Instruction-entry timestamp unavailable; SA treatment required

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Harmonia Processing Framework and Checkpoints · G03 / Iteration 005

The operative Solution Architect instruction follows. Entry state was `feature/BL-CALLIOPE-OO-01` at `3588f5f8df1fbe65d64e8635a3a6a378bf19360c` (Snapshot-004 (II)), with the prior terminology Task and Report untracked and the tracked worktree and index clean. The [terminology Report][terminology] supplies the assigned starting point. [HCRL.004 / DR-12][hcrl]

## Operative instruction

# Codex Assignment — Harmonia Processing Framework and Checkpoints

## 1. Objective

Create:

`enterprise-architecture/design/offline-design/DRP-Harmonia-Processing-Framework-and-Checkpoints.draft.000.001.md`

This document will describe the **candidate Harmonia processing and orchestration architecture**, incorporating the Solution Architect's recent decisions and observations.

The architectural approach recognises three distinct tiers:

1. **Intra-Module Orchestration**
2. **Inter-Module Orchestration**
3. **OA&M Oversight**

The primary design subject is **Intra-Module Orchestration**, using Ponos and the common Ergon processing framework as the reference architecture.

The resulting DRP must distinguish:

- Accepted architectural authority.
- Existing qualified designs.
- Solution Architect directions established during this review.
- Candidate mechanisms requiring further adjudication.
- Detailed implementation matters.

**Do not treat the complete processing framework as accepted architectural authority.**

## 2. Repository and review context

The accepted Architecture baseline is **1.1.26**.

The current software review is **Software 0.1.0 / G03 / Iteration 005**, with cumulative review activity recorded in:

`enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-004.pending.md`

The recently completed terminology reconciliation is:

`enterprise-architecture/design/evidence/software-0.1.0/005/after/ai-agent-reports/20261122T111009Z-g03-ergon-route-checkpoint-reconciliation-report.md`

Use that report as the starting point for existing names, responsibilities, correspondence and unresolved terminology.

Consult relevant governed sources only where needed to resolve specific questions.

**Do not repeat the previous broad repository investigation or load large RAD documents unnecessarily.**

Confirm current branch, HEAD and worktree state. Preserve existing work.

## 3. Three-tier orchestration architecture

Introduce the following conceptual architecture.

### Tier 1 — Intra-Module Orchestration

**Purpose:** Orchestrate processing within an individual Harmonia module.

Primary candidate implementation mechanisms:

- Apache Camel routes.
- Camel `Processor` functions.
- Jakarta EE/CDI-managed functional objects, with `@Dependent` as a candidate lifecycle mechanism.
- Lightweight, synchronous, named Checkpoints.
- Mneme-managed operational and housekeeping state.
- Configurable logging, auditing and checkpoint persistence.
- Independent Assurance evidence.

Primary candidate applications:

- Ponos / common Ergon framework.
- DigitalTwin-Behaviour, as a specialised Ergon processing application.

Related adaptations:

- Pylai-Message-In.
- Pylai-Message-Out.
- Pylai-API-Server.
- Pylai-API-Client.

The Pylai variants must accommodate their respective external integration, protocol, security, request/response and delivery responsibilities.

**Do not prescribe identical processing routes for every module.**

### Tier 2 — Inter-Module Orchestration

**Purpose:** Coordinate progression and work distribution between processing modules.

Recognise two principal progression mechanisms.

**Praxis-based progression**

- Uses defined processing paths and progression rules.
- Ponos owns interpretation and evaluation of applicable Praxis progression.
- Petasos fulfils the resulting routing obligations.

**Publication/Subscription-based progression**

- Supports dynamically determined processing paths.
- Petasos evaluates applicable subscriptions and performs routing.
- Subscriber-specific undertakings are created through Mneme under the existing G03 candidate design.

Petasos comprises **Apache ActiveMQ Artemis and Harmonia-specific support functionality**.

It provides the messaging/routing capabilities needed for inter-module work transfer.

Preserve the separate responsibilities for work identity, authoritative persistence, security, execution admission and recovery.

Do not redesign the existing Petasos routing DRP.

### Tier 3 — OA&M Oversight

**Purpose:** Provide system-wide operational observation, evidence, assurance and oversight.

This architectural tier remains **less mature and requires further exploration**.

Recognise the existing component responsibilities:

| Component | Responsibility |
|---|---|
| Iris | Operational observations, status, metrics and presentation |
| Kleio | Authoritative audit capture |
| Dokimasia | Independent Assurance |
| Ponos / Petasos | Operational events and execution/routing evidence |
| Mneme | Governed operational and information-state access |

Possible future capabilities include operational correlation, investigation, exception detection, recovery oversight and authorised intervention.

These are investigation subjects, not approved new capabilities or component allocations.

Do not create another orchestration engine or merge Iris, Kleio and Dokimasia responsibilities.

The third tier is provisionally called **OA&M Oversight**, not necessarily another orchestration engine.

## 4. Intra-Module Processing Framework

Describe the candidate framework using the following architectural principles.

### 4.1 Apache Camel routes

Apache Camel provides the primary candidate mechanism for orchestrating the internal processing sequence.

A route comprises functional processing stages separated by named handover Checkpoints.

Camel endpoints/adapters may provide appropriate integration mechanisms.

Potential transports include `direct:`, `seda:` and Artemis-backed messaging, but their selection must follow the specific interaction requirements.

**The use of Apache Camel as a conceptual route model does not automatically prescribe the exact Camel implementation of every interaction.**

### 4.2 Functional processing stages

Functional stages are generally implemented through Camel-compatible `Processor` behaviour:

```java
void process(Exchange exchange);
```

The common Ergon framework supplies the surrounding processing scaffolding.

The domain-specific `Ergon.Process()` should remain focused on its bounded processing responsibility.

Supporting concerns such as contextualisation, security, integration, logging, audit, recovery and Assurance should be handled through appropriate framework responsibilities rather than repeatedly implemented by individual Ergons.

### 4.3 CDI lifecycle and concurrency

Record CDI `@Dependent` as a candidate mechanism for managing processing-function instances.

Do not claim that CDI scope automatically guarantees one instance per Camel Exchange.

The architecture requires correct execution isolation.

Processor instance lifecycle, concurrency configuration, pooling and throughput optimisation are detailed implementation concerns.

The engineering approach is:

**Establish correct execution and isolation first; optimise throughput using measured evidence.**

Do not unnecessarily tie this engineering sequence to formal Harmonia release numbers.

## 5. Reference Ponos / Ergon processing route

Include the Solution Architect's original stylised route, preserving its conceptual structure:

```java
from(ergon.ingress.queueId)
    .checkpoint()
    .process(ergon.ingress.pep)
    .checkpoint()
    .process(ergon.context-load)
    .checkpoint()
    .process(ergon.processor)
    .checkpoint()
    .process(ergon.egress.output-extract)
    .checkpoint()
    .process(ergon.egress.filter)
    .checkpoint()
    .process(ergon.egress.pep)
    .checkpoint()
    .process(petasos.route-in)
    .to(checkpoint());
```

Explicitly state that this is **architectural pseudocode**, not valid or prescribed Camel DSL.

Using the previous terminology reconciliation, identify the correspondence with established names such as:

- `IngressEndpoint`
- `Ergon.PEP.In`
- Ergo Context Loader / `Ergon.ContextLoader`
- `Ergon.Process`
- Context Unloader
- Pragma Output
- PublishingMask
- Dead-task blocking
- `Ergon.PEP.Out`
- Petasos Router / RoutingEngine

Preserve distinctions among context save, output extraction, publishing eligibility, dead-task blocking, authorisation and originating Pragma creation.

Do not silently merge these responsibilities.

### Required diagram

Provide **one single-row, left-to-right logical processing diagram**.

The principal row must represent the conceptual Camel processing route.

Place the Checkpoints between the processing functions.

Use provisional handover identifiers where helpful.

**Do not place Mneme/Mnemosyne interactions into the principal processing row.**

Document supporting interactions separately.

Do not invent a central Egress Coordinator.

## 6. Checkpoint architecture

This is a central subject of the DRP.

### 6.1 Checkpoint purpose

A Checkpoint is a lightweight, synchronous, governed handover mechanism between functional processing stages.

Every functional handover has a Checkpoint.

**Not every Checkpoint requires durable persistence.**

Checkpoints supply standardised processing and integration scaffolding.

They may invoke APIs supplied by other Harmonia components as required by the particular interaction.

Mneme generally provides high-speed access to relevant persisted operational status and housekeeping information.

### 6.2 Candidate Checkpoint processing pattern

Capture the Solution Architect's illustrative sequence:

```java
from(checkpointIn)

    .process(checkPersistedPragmaOrIU)

    .choice()
        .when(checkFailed)
            .process(logIfRequired)
            .process(auditIfRequired)
            .stop()
    .end()

    .process(logIfRequired)
    .process(auditIfRequired)

    .to(checkpointOut)

    .process(checkSuccessfulForward)

    .choice()
        .when(forwardFailed)
            .process(logIfRequired)
            .process(auditIfRequired)
            .stop()
    .end();
```

Treat this as conceptual pseudocode.

It illustrates:

1. Incoming state validation.
2. Conditional logging and audit.
3. Synchronous forwarding.
4. Forwarding-outcome verification.
5. Governed handling of unsuccessful outcomes.

**A Checkpoint does not introduce an asynchronous endpoint into its internal processing sequence.**

The Checkpoint's operation is synchronous even where the broader system employs asynchronous messaging.

### 6.3 Outcome semantics

The Solution Architect has described internal handovers conceptually as **Pass/Fail**.

This is not a requirement for a Boolean result, a two-value enum or a universal outcome type.

The actual outcome must reflect the interaction contract, including indeterminate, denied, deferred, filtered or other meaningful results where applicable.

Do not invent a `PassFail` type or equivalent abstraction.

### 6.4 Configurable granularity

Each named Checkpoint supports independently configurable levels for:

- Logging.
- Audit.
- Checkpoint persistence.

The proposed levels are:

`COARSE | MEDIUM | FINE | EXTREME`

These describe **granularity**, not universally fixed implementation behaviours.

Exact semantics of each level remain to be defined.

Configuration must not weaken mandatory security, authoritative durability, audit capture, information governance or recovery requirements.

Logging and observation are not substitutes for authoritative audit evidence.

A lightweight Checkpoint should avoid unnecessary persistence, serialization, network calls or other overhead when these are not required by its contract.

### 6.5 Mneme-managed housekeeping

The framework may use lightweight operational/housekeeping objects supplied and managed through Mneme.

These support high-speed access to relevant processing, handover, checkpoint and recovery state.

Do not invent canonical object names or assume every object requires separate durable persistence.

Preserve the distinction between:

- Ephemeral operational information.
- Cached representations of governed information.
- Authoritatively persisted information.
- Evidence sufficient to establish a particular outcome.

Reuse existing Mneme state-management capabilities where appropriate.

### 6.6 Independent Assurance

Checkpoints and processing functions provide independently attributable handover evidence.

An independent Assurance capability can evaluate this evidence against applicable expectations.

Preserve Dokimasia's existing Assurance responsibility.

Do not equate an operational observation with an authoritative AuditEvent or an Assurance finding.

Do not require the processing function to determine its own independent Assurance result.

## 7. Relationship to existing DRPs

Reconcile this candidate against the established and pending designs for:

- Ergon–Mneme interaction.
- Pragmaton and WorkSpace.
- Mneme Pragma Model.
- Pragma publication and subscription.
- Petasos routing and reconciliation.
- Themis ingress/egress enforcement.
- Kleio audit capture.
- Ponos and Praxis execution.
- Existing checkpoint and recovery features.
- Iris operational observability.
- Dokimasia Assurance.

Use the existing terminology reconciliation report to avoid repeating research.

Where discrepancies exist, identify their source and the required Solution Architect decision.

Do not silently amend accepted semantics.

## 8. Architectural status and open decisions

Include a compact decision table distinguishing:

**Candidate architectural direction**

- Three orchestration tiers.
- Camel-based intra-module processing.
- Reusable functional scaffolding.
- Synchronous Checkpoints at functional handovers.
- Independent checkpoint configuration.
- Mneme-managed housekeeping.
- Independent Assurance.
- Candidate reuse across Ponos, DigitalTwin-Behaviour and Pylai.

**Outstanding architectural questions**

- Final processing-stage terminology.
- Exact checkpoint identity and lifecycle.
- Relationship between handover points and durable recovery checkpoints.
- Definition of the four granularity levels.
- Minimum mandatory checkpoint evidence.
- Correspondence with existing Ergon context load/save and output processing.
- External integration adaptations for Pylai.
- Further exploration of OA&M Oversight.

**Detailed implementation questions**

- Camel endpoint choices.
- CDI instance lifecycle.
- Processor pooling and concurrency.
- Specific Java interfaces and classes.
- Performance tuning.
- Checkpoint implementation mechanics.

Do not turn implementation questions into unnecessary architectural blockers.

## 9. Evidence and HCRL

Follow the established Task/Report evidence conventions under:

`enterprise-architecture/design/evidence/software-0.1.0/005/`

Create the new DRP as Draft.000.001.

Make a **bounded additive update** to the existing cumulative HCRL:

`enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-004.pending.md`

Record the new candidate under **DR-12**, identifying its relationship to PF-07/PF-08 and the remaining G03 review.

Preserve all previous review activities, dispositions and outstanding obligations.

The DRP and HCRL remain pending Solution Architect review.

Do not modify existing DRPs solely to align their terminology.

## 10. Verification and constraints

Verify:

- All relevant architectural references resolve.
- Existing accepted responsibilities are preserved.
- The three tiers remain distinct.
- The Ponos route is represented as one horizontal processing sequence.
- Checkpoints remain synchronous.
- Configurable granularity does not weaken mandatory governance.
- Mneme housekeeping is not confused with authoritative persistence.
- Independent Assurance remains independent.
- No unnecessary new framework, processing engine or coordination component has been introduced.

Run appropriate bounded documentation and architecture checks.

Report any existing validation failures without introducing unrelated corrective changes.

**Do not:**

- Implement software.
- Modify the Architecture Continuum.
- Change accepted architectural baselines.
- Create additional DRPs.
- Invent canonical APIs or data types.
- Commit, tag or push.
- Mark DR-12 or the new DRP accepted.

## 11. Deliverables

Return:

1. `DRP-Harmonia-Processing-Framework-and-Checkpoints.draft.000.001.md`
2. A concise reconciliation/execution report.
3. The additive HCRL DR-12 amendment.

The document should be sufficiently precise for architectural review without becoming an implementation specification.

**Primary objective:** Capture the candidate three-tier orchestration model and reusable checkpoint-based processing architecture, preserving existing Harmonia responsibilities and leaving detailed implementation choices to subsequent design.

[terminology]: ../../after/ai-agent-reports/20261122T111009Z-g03-ergon-route-checkpoint-reconciliation-report.md
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
