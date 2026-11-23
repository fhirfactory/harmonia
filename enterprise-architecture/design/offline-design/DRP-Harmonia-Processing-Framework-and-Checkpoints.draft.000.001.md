<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Design Review Proposal — Harmonia Processing Framework and Checkpoints

**Status** · Draft.000.001 · Pending Solution Architect review  
**Basis** · Architecture 1.1.26 · Software 0.1.0 · G03 / Iteration 005 / DR-12  
**Solution Architect** · Mark Hunter. [Issued design direction][task] · [Terminology reconciliation][terminology] · [HCRL.004 / DR-12][hcrl]

## 1. Scope and standing

This proposal describes three processing concerns. Its primary design subject is **Intra-Module Orchestration**, with Ponos and the common Ergon framework as the reference. Inter-module progression and OA&M Oversight define how that local processing relates to existing owners. The tiers are logical boundaries of responsibility. They imply no deployment layout, additional execution engine, coordination platform or common route for every module.

| Standing | Treatment in this draft |
| --- | --- |
| Accepted architectural authority | Architecture 1.1.26 and canonical Principles govern the design. The accepted two-cache allocation, common Ergon checkpoint/recovery responsibility, authoritative persistence and independent Assurance remain controlling. Use accepted checkpoint Features for boundaries, required durability, recovery integrity and completeness. [Release authority][release] · [Principles][axioms] · [1.1.24 DR-1][persistence] · [1.1.24 DR-3][cache] · [Assurance][assurance] |
| Existing qualified designs | Ponos, Ergon, Praxis, Pathway endpoints, Petasos and Pylai contributions retain their recorded standing. G01 Final.001 records the accepted Ergon security design and preserves its transport and criteria-binding qualifications. Existing G03 Pragma, Pragmaton, publication and routing DRPs retain candidate standing. [Terminology authority matrix][terminology] · [G01 review decision][g01-decision] · [G03 review][hcrl] |
| Issued review direction | Use the three-tier model and a lightweight synchronous Checkpoint at each functional handover in the candidate Tier 1 framework. Preserve configurable logging, audit and checkpoint persistence, governed housekeeping and independently attributable evidence. [Issued design direction][task] |
| Candidate mechanisms and detailed design | Camel routes and processors, CDI lifecycle, named handover points, configuration levels and adaptations are assessed within the boundaries below. The complete framework remains a review candidate. |

AX-04 supports reuse of Camel and Artemis while keeping semantics with Harmonia. AX-05/06/09 distinguish active state, authoritative persistence and the meaning of evidence. AX-07/08 preserve authorisation and meaningful audit. AX-10/11 protect distributed recovery and responsiveness, while AX-14/15/16 require distinct, observable outcomes and explicit uncertainty. AX-17 preserves the standing of candidate decisions and any unestablished correspondence. [Canonical Principles][axioms]

## 2. Three-tier architecture

| Tier | Purpose and boundaries |
| --- | --- |
| **1 · Intra-Module Orchestration** | Sequence processing inside a module. Candidate Camel routes combine functional stages and lightweight synchronous named Checkpoints. The common framework supplies reusable contextualisation, enforcement, integration and recovery scaffolding, governed Mneme housekeeping and attributable operational, audit, persistence and Assurance evidence as applicable. Ponos/common Ergon is the reference. DigitalTwin-Behaviour is a specialised application. |
| **2 · Inter-Module Orchestration** | Coordinate progression and durable work transfer between modules. For defined Praxis progression, Ponos owns interpretation and evaluation, and Petasos fulfils the resulting routing obligations in the G03 design. For dynamic publication/subscription, Petasos matches subscriptions and routes subscriber-specific undertakings constructed through Mneme. Petasos comprises Apache ActiveMQ Artemis and Harmonia-specific support functionality for routing, queues, delivery and reconciliation. Work identity, authoritative persistence, Themis enforcement, execution admission and execution recovery keep their existing boundaries. [Praxis][praxis] · [Petasos][petasos] · [G03 publication][publication] · [Routing and reconciliation][routing] |
| **3 · OA&M Oversight** | Observe operations across modules and present governed evidence for operational review and independent Assurance. The tier is less mature. Correlation, investigation, exception detection, recovery oversight and authorised intervention are subjects for exploration. It does not add a progression decision-maker or transfer operational control. [Operational State Reporting][operational] · [Iris][iris] · [Assurance][assurance] |

OA&M reuses the contributing boundaries below. Operational evidence and presentation provide context for investigation. Any eventual intervention requires its own authorisation and owning operation.

| Component | Responsibility in OA&M Oversight |
| --- | --- |
| Iris | Present governed operational observations, status and metrics and convey authorised operator intent through existing interfaces. |
| Kleio | Capture required auditable occurrences and construct and preserve authoritative AuditEvents. |
| Dokimasia | Independently evaluate relevant evidence against applicable Assurance expectations. |
| Ponos / Petasos | Contribute attributable execution/progression or routing/delivery events and observations for their respective operations. |
| Mneme | Supply governed access to relevant operational and information state. |

### 2.1 Tier 1 applications and adaptations

| Application | Scope of reuse |
| --- | --- |
| Ponos / common Ergon | Reference route in §4. Ponos retains execution and recovery coordination and Praxis interpretation. Common Ergon supplies the execution-side context, checkpoint and recovery scaffolding. |
| DigitalTwin-Behaviour | Reuse appropriate common Ergon processing with fresh, entity-relative context for each Behaviour. Preserve within-Twin serialisation, work scope and the governing stop and failure semantics. Reuse of processors supplies no concurrency entitlement inside a Twin. |
| Pylai-Message-In | Adapt receipt, protocol parsing and authorisation, mapping and durable handover to the applicable inbound acknowledgement contract. Distinguish external receipt, queue acceptance and required authoritative work establishment. |
| Pylai-Message-Out | Adapt governed output projection, transmission, delivery outcomes and remote acknowledgements to the external protocol. Preserve external release restrictions and delivery/retry ownership. |
| Pylai-API-Server | Adapt the route to governed authentication/authorisation, request/response and applicable internal changes or read/search handling. Preserve the qualified distinction between Pathway-backed changes and Diadromí-Controller reads/searches. |
| Pylai-API-Client | Adapt outbound invocation and response, timeout and failure handling to the relevant protocol, security, egress and governing processing contract. |

The Pylai names identify candidate adaptation contexts. Each retains its interoperability and protocol obligations, separates its own acknowledgements from internal work acceptance, and selects stages for its actual interaction. [Common Ergon recovery][cache] · [DigitalTwin][twin] · [Pylai interoperability boundary][pylai] · [Repository ingress and egress guardrails][agents]

## 3. Intra-module processing framework

Apache Camel is the primary candidate mechanism for sequencing the processing functions and handovers. A processing route in this proposal is a conceptual Camel sequence. The qualified Pathway vocabulary also uses **PathwayTraversal** for internal passage and **Route** for ordered inter-component segments. Final correspondence of terms remains for review. A route's processing stages and named Checkpoints express semantics before selecting endpoints or adapters. [Terminology reconciliation][terminology] · [Qualified Pathway vocabulary][pathway-model]

`direct:` is a candidate for synchronous invocation within a Camel context. `seda:` provides asynchronous in-process queuing and supplies no durable recovery after JVM loss. It can be assessed for interactions that allow a separate asynchronous boundary. Artemis-backed messaging supports appropriate asynchronous or durable handoffs under their owning contracts. The approved mandate routes every PetasosParcel through Artemis, including co-located participants. Transport selection must preserve the interaction's security, concurrency, acknowledgement and recovery requirements. [Camel Direct][camel-direct] · [Camel SEDA][camel-seda] · [Exclusive PetasosParcel Routing][artemis]

Functional stages may use Camel-compatible `Processor` behaviour, illustrated as:

```java
void process(Exchange exchange);
```

The common Ergon framework supplies the surrounding scaffolding. Domain-specific `Ergon.Process` remains bounded business logic using the applicable working context. Framework and collaborating component responsibilities supply contextualisation, security, integration, logging, audit and recovery. Independent Assurance remains with Dokimasia. The illustration supplies no new Harmonia interface. [Camel Processor][camel-processor] · [Common Ergon][ergon] · [Ergon–Mneme interaction][context] · [Assurance][assurance]

Jakarta EE/CDI-managed functional objects are a candidate realisation, with `@Dependent` a lifecycle option. A dependent bean injected into a route owner is tied to that owner or lookup lifecycle. Its scope alone does not create one instance per Camel Exchange. Protect each execution's authorised working state and context under concurrency, failure and retry. Exact instance ownership and destruction, sharing, pooling, threading and throughput are implementation matters. Establish correct execution and isolation first, then optimise throughput with measured evidence. [CDI 4.0 §2.5.4][cdi] · [Ergon Execution Isolation][isolation]

## 4. Reference Ponos / Ergon processing route

The Solution Architect's stylised route is **architectural pseudocode**. The `.checkpoint()` and terminal `.to(checkpoint())` forms express handover positions. They supply neither valid nor prescribed Camel DSL. [Issued design direction][task] · [Terminology reconciliation][terminology]

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

### 4.1 Logical processing row

```text
IngressEndpoint → [H01] → Ergon.PEP.In → [H02] → Context Loader → [H03] → Ergon.Process → [H04] → Output extraction → [H05] → Filter (meaning open) → [H06] → Ergon.PEP.Out → [H07] → Petasos intake → [H08]
```

H01–H08 are provisional named handovers, not canonical identities or persistent recovery points. The row follows the supplied processing sequence and shows the successful path at its intended abstraction level. H08 is a terminal outcome observation whose exact scope and evidence issuer remain open. Supporting interactions and gates in §4.3 govern the steps even where the supplied row abstracts them. [Terminology reconciliation][terminology]

### 4.2 Stage and Checkpoint correspondence

| Processing slot | Governed or candidate correspondence | Handover fact and evidence boundary |
| --- | --- | --- |
| `from(ergon.ingress.queueId)` | `IngressEndpoint` (qualified C29) or `Ergon.In` (G01). The queue identifier is an adapter binding. | **H01** · Receipt at the Ergon boundary, independently of execution admission. |
| `ergon.ingress.pep` | `Ergon.PEP.In` invokes applicable Themis enforcement before processing. | **H02** · Admission, denial or unavailable determination under the applicable contract. |
| `ergon.context-load` | **Ergo Context Loader** (qualified) and `Ergon.ContextLoader` (G03) refer to the execution-side role. | **H03** · Initial permitted working context available, or the owning load outcome. |
| `ergon.processor` | `Ergon.Process` is the G01 processing boundary. **Ergon Business Logic** is the bounded function it executes. `Ergon.ProcessingEngine` remains a qualified G03 naming question. | **H04** · Attributable Ergon-instance processing result, separate from undertaking/parent disposition and release. |
| `ergon.egress.output-extract` | **Output extraction** is descriptive. **Context Unloader** returns working changes and **Pragma Output**, whose governed handling is separate. The exact scope of this slot remains open. | **H05** · Evidence of the applicable extraction or return. Save has its own outcome. |
| `ergon.egress.filter` | **PublishingMask**, **dead-task blocking** and Praxis output selection are distinct decisions. Adjudicate the filter slot's subject and owner. | **H06** · Disposition issued by the owning check, with the reason and applicable next action preserved. |
| `ergon.egress.pep` | `Ergon.PEP.Out` enforces release under applicable Themis policy. | **H07** · Release decision. It supplies no evidence of originating Pragma construction or handover acceptance. |
| `petasos.route-in` | Qualified `EgressEndpoint`, G01 `Ergon.Out` and **Petasos Router / RoutingEngine** correspondence as a logical intake and routing boundary. Exact intake name and activation remain open. | **H08** · The confirmed Petasos fact under the eventual intake contract. Receipt, durable decision, queue acceptance and delivery remain distinct. |

The named facts identify evidence sources for review. They do not prescribe logging, audit capture or durable checkpoint selection at each handover. [Pathway endpoints][endpoint] · [G01 §4][g01] · [Ergon–Mneme interaction][context] · [Terminology matrix][terminology]

### 4.3 Supporting interactions and egress obligations

- **Working context and save.** The common Loader collaborates with Mneme Context Load to contextualise governed information. G03 exposes the Mneme-managed Pragmaton through `Ergon.WorkSpace` and mediates additional access through the candidate Ergon-InformationAdapter under applicable Themis controls. Context Unloader returns working changes for Mneme Context Save and identifies new Pragma Output. Mnemosyne establishes authoritative durable effects where applicable. Keep output extraction, return, save and actual information mutation independently attributable. [Ergon–Mneme §§4.1–4.7][context] · [Completion and return][unload] · [Accepted persistence contracts][persistence]
- **Eligibility, blocking and release.** The qualified baseline applies Publishing Pattern to egress Pragma. The G03 candidate makes Ponos responsible for publication eligibility and Mneme the evaluator of the producing Ergon's PublishingMask on the newly created IU. A non-match leaves the IU governed. Dead-task blocking precedes the outbound PEP. Neither mask evaluation nor blocking substitutes for Themis or the separate Ponos evaluation of Praxis progression. Determine which decision the stylised filter slot illustrates and how the other decisions and governed save are represented. [Pragma Management][publish-base] · [Publication and subscription][publication] · [Ergon Egress][routing-egress] · [G01 §4][g01]
- **Work creation and onward routing.** Under the existing G03 design, after blocking and the outbound PEP, Ergon Egress requests originating Pragma construction through Mneme's PragmaFactory. Governed persistence establishes the list before handover to Petasos. Subscription-Router receives originating work and requests subscriber-specific Pragmatosis after durable decision capture. Praxis-Router fulfils the recorded Ponos decision on existing originating Pragmas. Applicable authoritative Provenance can reconstruct the original complete list for recovery. Those responsibilities remain with their owners. Petasos applies its own protected FORWARD boundary. [Pragma Model][pragma] · [Routing and reconciliation][routing] · [Egress recovery][routing-recovery] · [Petasos][petasos]

## 5. Checkpoint architecture

### 5.1 Purpose and processing contract

Every functional handover in the proposed Tier 1 framework has a lightweight, synchronous, named Checkpoint. It validates the incoming basis where the handover requires it, applies conditional logging and audit, forwards synchronously, verifies the relevant forwarding outcome and responds under the governing contract. It may call APIs supplied by existing components as needed for that interaction. A handover Checkpoint requires durable persistence only where its recovery or evidence contract calls for it. The accepted Features retain their requirements for defined recovery boundaries, durable required task checkpoints, valid identifiable recovery state and evidence of completeness. [Checkpoint Boundary Definition][checkpoint-boundary] · [Durable Task Checkpoint][checkpoint-durable] · [Recoverable Checkpoint Integrity][checkpoint-integrity] · [Checkpoint Completeness Evidence][checkpoint-completeness]

The following **conceptual pseudocode** expresses those steps. `checkPersistedPragmaOrIU` applies where persisted state is a precondition. The stopping branches withhold dependent activity and use the outcome semantics in §5.2.

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

The Checkpoint's internal sequence is synchronous, including its applicable forwarding verification. It introduces no asynchronous endpoint inside that sequence. A synchronous handover at the edge of a wider asynchronous flow can establish only the fact confirmed by its owning contract, such as durable acceptance where proved. It does not establish delivery or downstream execution. The governing operation still determines how to handle timeout, failure and uncertain effects. [Issued design direction][task] · [Explicit Indeterminate Outcome][indeterminate] · [Petasos routing and reconciliation][routing]

### 5.2 Outcomes and recovery

**Pass/Fail** describes the conceptual control decision to proceed or withhold a dependent step. Preserve the actual owning outcome and its evidence. An indeterminate write, denied access, unavailable authorisation, deferred execution or non-match for publication retains its distinct meaning. A known filter non-match is an ordinary disposition. A timeout is insufficient to decide occurrence or non-occurrence. Withhold claims of success lacking required proof and reconcile uncertain effects before any unsafe retry. The model prescribes no Boolean, two-value enum or universal outcome type. [AX-14/15][axioms] · [Explicit Indeterminate Outcome][indeterminate] · [Accepted persistence][persistence]

Common Ergon owns execution checkpoints and contextual recovery. Ponos coordinates governed execution. Petasos retains its durable handoff and redelivery/reconciliation responsibilities. Recover at the owning boundary using durable work, authoritative information and sufficient evidence of valid recovery state. A checkpoint observation does not authorise re-execution, change work identity or resume an Ergon internally after failure. [Common Ergon recovery][cache] · [Routing and reconciliation][routing] · [Checkpoint integrity][checkpoint-integrity]

### 5.3 Configuration and housekeeping

Each named Checkpoint supports independent **logging**, **audit** and **checkpoint persistence** settings. The proposed granularity levels are `COARSE | MEDIUM | FINE | EXTREME`. The levels describe extent of capture, with exact semantics, defaults and configuration policy left for design. Mandatory security, information governance, authoritative durability, audit capture and recovery obligations establish minimum behaviour at every level. An optional logging or observation path cannot supply missing mandatory audit evidence. Clients submit meaningful auditable occurrences. Kleio constructs and preserves authoritative AuditEvents and the affected operation fails closed if required capture cannot be established. Use existing diagnostic controls, including protection of PHI. [Canonical AX-07/08/09][axioms] · [Kleio capture][kleio] · [Pending audit-staging design][audit-draft]

Use existing Mneme capabilities for governed, high-speed access to relevant persisted facts and lightweight operational housekeeping. Keep ephemeral state, cached representations of governed information, authoritative persistence and evidence of a particular outcome distinguishable. Mneme manages active information and obtains authoritative facts through the applicable contracts. Mnemosyne establishes authoritative durable state. Cache residence or a local success flag is insufficient to prove durable acceptance. Preserve the two-cache and governing access allocations. Keep a Checkpoint lightweight by avoiding persistence, serialisation, network calls and other work where its contract does not require them. [Mneme][mneme] · [Cache governance][cache] · [Accepted persistence][persistence]

### 5.4 Independent Assurance

Checkpoints and functions contribute attributable observations and evidence of their own handovers. Correlate material evidence to the relevant work, execution and attempt, stage and known outcome under the owning contracts. Evidence completeness and retention minima remain for adjudication. Dokimasia independently evaluates evidence against applicable Praxis and other governing expectations. Journey records applicable observations, Provenance describes lineage and activity, and Kleio retains AuditEvents. LogEvents describe diagnostic or operational matters. These representations do not supply the assessment result. Processing functions provide evidence without deciding their own Assurance. Preserve the finite Assurance boundary and the existing self-audit rule. [Accepted Assurance and Provenance][assurance] · [Journey][journey] · [Dokimasia][dokimasia] · [Finite boundary][finite] · [Self-audit constraint][self-audit]

## 6. Reconciliation and G03 dependencies

| Surface | Correspondence and decision needed |
| --- | --- |
| Processing names, context and Pragma output | Preserve the C29 and G01 names with their standing and the G03 Pragmaton/WorkSpace allocation. Confirm the Loader spelling, ProcessingEngine scope, output extraction versus whole Unloader and governed save boundary. The qualified Pathway terms and the Camel route label require distinct scope. [Terminology matrix][terminology] · [Ergon–Mneme interaction][context] |
| Egress and publication | Resolve the supplied filter slot, PublishingMask/qualified Publishing Pattern correspondence and relative placement of context return, save, publication eligibility and release. The candidate preserves dead-task blocking before `Ergon.PEP.Out` and governed originating Pragma creation/persistence before Petasos handover. Keep publication and Praxis continuation separate. [Pragma Management][publish-base] · [G03 publication review][publication-open] · [Routing Draft.000.003][routing] |
| Work, transport and execution | The earlier qualified model gives Mneme semantic selection, parcels originating work with receiver-side undertaking creation and describes Ponos/Ergo continuation routing. G03 gives Petasos matching and routing fulfilment, passes the established Pragmatosis identity for subscription work and uses Ponos evaluation for direct Praxis routing of selected existing Pragmas. The direct Praxis parcel/receiving role and candidate per-execution TaskOutcome versus the canonical parent-resolution Topic still need correspondence. This DRP applies the candidate context without revising the routing DRP. [PetasosParcel][parcel] · [TaskOutcome Topic][task-outcome] · [G03 Pragma Model][pragma] · [Routing open decisions][routing-open] |
| Security and evidence | Preserve the Ergon-hosted ingress and egress PEPs and Petasos FORWARD enforcement. G01 retains the calling ID rule and Themis authority resolution. Reconcile trusted binding and qualified pass-off terminology without widening authority. Select the minimum independently attributable evidence by handover and owning contract. The pending audit-staging proposal does not alter the Kleio capture gate. [G01 qualifications][g01-open] · [Kleio][kleio] · [Terminology discrepancies][terminology] |
| PF-07 and PF-08 | **PF-07 remains partially resolved.** H01–H08 need owning acknowledgement and minimum evidence where applicable, especially H08 and the distinction between receipt, authoritative work, durable routing and accepted queue effects. Egress completeness evidence and recovery remain open. **PF-08 — Architectural strategy resolved; detailed transport, concurrency and recovery contracts outstanding.** Reuse the qualified routing strategy. A Checkpoint neither replaces durable routing records nor supplies a retry, reinjection, fencing or execution-admission proof. All remaining G03 qualifications retain their standing. [G03 exception register][pf] · [PF-08 disposition][routing-pf08] · [DR-12][hcrl] |

## 7. Decision and verification register

| Status | Matter |
| --- | --- |
| **Candidate architectural direction** | Three distinct tiers. Camel-based intra-module sequencing and reusable scaffolding with a synchronous Checkpoint at each named functional handover. Independent configuration, Mneme-managed housekeeping and independently attributable Assurance evidence. Candidate reuse by Ponos/common Ergon and DigitalTwin-Behaviour with purpose-specific Pylai adaptations. |
| **Architectural adjudication outstanding** | Final processing-stage names and filter subject, Checkpoint identity and lifecycle, H08 and other acknowledgement boundaries, which handovers are durable recovery checkpoints, exact granularity definitions and mandatory evidence, and correspondence of context load/save, output extraction, release and originating work. Establish Pylai adaptation contracts and explore OA&M correlation and intervention under existing authority. |
| **Detailed implementation** | Camel endpoint/adapter choice, CDI instance ownership and lifecycle, processor sharing/pooling and concurrency configuration, interfaces and classes, checkpoint implementation and measured performance tuning. Verify correct isolation and effects before optimising. These choices stay within the agreed architectural contracts. |
| **Later conformance evidence** | Verify stage and recovery ownership, valid synchronous outcomes under success and failure, no internal asynchronous step, preserved Ack/indeterminate distinctions, sufficient evidence under every configuration, and fail-closed security and mandatory Kleio capture. Exercise isolation under concurrency and retry, durable recovery evidence versus transient observation, and independent Assurance. Qualify each integration against its actual protocol and transport. |

**Review disposition** · Draft.000.001 remains pending Solution Architect review in DR-12. [HCRL.004 / DR-12][hcrl]

[task]: ../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-g03-processing-framework-and-checkpoints-task.md
[terminology]: ../evidence/software-0.1.0/005/after/ai-agent-reports/20261122T111009Z-g03-ergon-route-checkpoint-reconciliation-report.md
[hcrl]: ../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
[release]: ../../../architecture-release.json
[agents]: ../../../AGENTS.md
[axioms]: ../../governance/architectural-axioms.md
[persistence]: ../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-1
[cache]: ../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-3
[assurance]: ../register/1.1.20/hcrl-1.1.20-001.complete.md#dr-19
[g01-decision]: DRP-Ergon-Authorisation-and-Identity-Delegation.Final.001.md#11-review-decision
[g01]: DRP-Ergon-Authorisation-and-Identity-Delegation.Final.001.md#4-architectural-definition
[g01-open]: DRP-Ergon-Authorisation-and-Identity-Delegation.Final.001.md#9-outstanding-qualifications
[endpoint]: ../../continuum/application/application-interface/800789f54ba24f53a354600d5cd43f6f.md#architectural-content
[pathway-model]: ../../continuum/application/application-component/fb3f37a6ac614415ac486a6de9d35994.md#designnote-pathways-journeys-and-assurance-representation--original-lines-124-124
[ergon]: ../../continuum/application/application-component/49c9eb2bef0c47b8b5328621d5fc5479.md#architectural-content
[praxis]: ../../continuum/application/data-object/0f961462340348b290fd844357d54d67.md#architectural-content
[petasos]: ../../continuum/application/application-component/195e5f10db8e419391354197b5d7a8cc.md#architectural-content
[parcel]: ../../continuum/application/data-object/f0a88f05a0cb438cb42572ec07fb8513.md#architectural-content
[artemis]: ../../continuum/motivation/constraint/mandate/e024703b32c5437481ce290e8e50b079.md#approved-definition
[operational]: ../../continuum/motivation/principle/architectural-axiom/d7e3f0388d0043259585d1e4c5c948d6.md#architectural-content
[iris]: ../../continuum/application/application-component/e927c1af96c64fcd979be659bee2cbe3.md#architectural-content
[twin]: ../../continuum/application/application-component/3f64d339648246e99e8866c8089f8960.md#architectural-content
[pylai]: ../../continuum/application/application-component/e3e4147fea3640198dc251ed41ee854d.md#architectural-content
[mneme]: ../../continuum/application/application-component/1af8a56358f7429fabfb57be66542a99.md#architectural-content
[kleio]: ../../continuum/application/application-component/fefda110e2274884ad58906733606efd.md#auditability-and-availability
[journey]: ../../continuum/application/data-object/359b901df27f4b43b5a2ff14a8484e34.md#architectural-content
[dokimasia]: ../../continuum/application/application-component/d739c75dffe2453081735710b285f03b.md#architectural-content
[finite]: ../../continuum/motivation/constraint/3ba73277abf146728ca8c1f5846227d7.md#architectural-content
[self-audit]: ../../continuum/motivation/constraint/e1ce978457e543d5894de24e9be9ac2c.md#architectural-content
[isolation]: ../../continuum/strategy/course-of-action/implementation-guideline/4332542fe3c54af9ba07220d255de9c2.md#architectural-content
[indeterminate]: ../../continuum/motivation/requirement/fc732b64fc564127b73174e28d9ccce8.md#architectural-content
[checkpoint-boundary]: ../../continuum/strategy/capability/feature/bb57137dc8844c96ba2f0c3ccc88a2f9.md#architectural-content
[checkpoint-durable]: ../../continuum/strategy/capability/feature/4f7a02255cb9400ab944c2f3827463af.md#architectural-content
[checkpoint-integrity]: ../../continuum/strategy/capability/feature/5cce4e7eabbb4b63919ae43d591f1279.md#architectural-content
[checkpoint-completeness]: ../../continuum/strategy/capability/feature/06c40d4b74764978a2b0e16a3e5baa2f.md#architectural-content
[task-outcome]: ../../continuum/application/data-object/topic/08d8ee19a30f418f9d5bc497ced0d1ef.md#architectural-content
[publish-base]: ../../continuum/application/application-function/ab0cdaa7ed4149ee856216efa5a265c4.md#source-qualified-contribution
[context]: DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.000.004.md#4-architectural-definition
[unload]: DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.000.004.md#46-completion-and-return
[pragma]: DRP-Mneme-Pragma-Model.draft.000.004.md#48-execution-outcomes-and-work-disposition
[publication]: DRP-Pragma-Publication-and-Subscription.draft.000.003.md#41-output-and-publication
[publication-open]: DRP-Pragma-Publication-and-Subscription.draft.000.003.md#6-reconciliation-and-open-decisions
[routing]: DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md
[routing-egress]: DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md#21-ergon-egress-creates-and-hands-over-originating-pragmas
[routing-recovery]: DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md#51-ergon-egress-handover-recovery-from-authoritative-provenance
[routing-open]: DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md#7-architecture-correspondence-and-open-decisions
[routing-pf08]: DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md#71-pf-08-qualified-disposition
[audit-draft]: DRP-Interaction-Petasos-to-Kleio-Audit-Capture.draft.000.001.md#3-candidate-staging-and-transaction-boundary
[pf]: ../evidence/software-0.1.0/005/after/G03-Pragma-Family-Adjudications-005.md#pf-01pf-12-disposition
[camel-direct]: https://github.com/apache/camel/blob/camel-4.4.2/components/camel-direct/src/main/docs/direct-component.adoc
[camel-seda]: https://github.com/apache/camel/blob/camel-4.4.2/components/camel-seda/src/main/docs/seda-component.adoc
[camel-processor]: https://github.com/apache/camel/blob/camel-4.4.2/core/camel-api/src/main/java/org/apache/camel/Processor.java
[cdi]: https://jakarta.ee/specifications/cdi/4.0/jakarta-cdi-spec-4.0.html#dependent-context
