<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Design Review Proposal — Harmonia Undertaking and Execution Framework

| Review field | Value |
| --- | --- |
| Revision | Draft.000.001 |
| Standing | Pending Solution Architect review |
| Prepared by | Codex |
| Solution Architect | Mark Hunter |
| Architecture baseline | 1.1.26 |
| Software development identity | `0.1.0-SNAPSHOT` |
| Source | `feature/BL-0006-Calliope-Pragma` at `590d268af553dac40455aa63815d141a4352dcf2` |

## 1. Feature realisation

The framework gives general, Digital Twin and assurance processing common semantics for originating work, realising a particular undertaking, obtaining governed context and recording attributable results. It reuses the accepted **EligibleTaskPublication**, **SubscriptionMatchCorrect**, **RecoveryPreservesTaskLineage**, **SingleSchedulingAuthority** and **Assurance Evidence Correlation** Features. Those Features support the work across publication, routing, execution, recovery and independent assurance. Placement of a shared execution-coordination Feature, if needed after mapping existing Features, is a review matter. [Publication][publication-feature] · [Subscription matching][matching-feature] · [Recovery lineage][lineage] · [Scheduling][scheduler] · [Assurance evidence][assurance-feature]

CORE-004 and PROV-001 support correlation and causation through origin, undertaking, execution and handoff. REQ-FND-005 governs independent assurance. This draft proposes three additional conformance requirements for review: stable linkage from a realisation to its origin, atomic acceptance of a policy-selected execution result, and independent recovery of routing obligations that share an undertaking. Section 8 gives observable checks. [CORE-004][core004] · [PROV-001][prov001] · [REQ-FND-005][assurance-req]

| Applicable axioms | Consequence for this proposal |
| --- | --- |
| AX-04, AX-18 | Praxis provides the policy basis. Ponos governs execution, Petasos owns matching and routing, and Mneme governs the work information. Reuse these capabilities and the existing persistence and checkpoint boundaries. |
| AX-05, AX-06, AX-09 | Mneme provides active access and coordination. Mnemosyne establishes durable authority. Keep transient working detail purpose-bounded and persist the facts needed for recovery, information authority and evidence. |
| AX-07, AX-08 | Apply Themis to protected access and effects. Preserve attributable provenance and applicable Kleio capture for meaningful facts, with the existing Dokimasia boundary. |
| AX-10, AX-15, AX-16 | Competing attempts, lost replies and recovery preserve known effects and uncertainty. Confirm accepted progression against the applicable authoritative record. |
| AX-14, AX-17 | Keep work, execution and outcome meanings distinct. Submit the existing authority conflicts and unresolved mappings for explicit disposition. |

The canonical Principles define each axiom. [Architectural Axioms][axioms]

## 2. Proposal and standing

| Standing in this draft | Content |
| --- | --- |
| Solution Architect decision supplied in the assignment | **At most one Pragmatosis per originating Pragma per subscribing Ergon, irrespective of the number of matching subscription definitions.** Petasos owns matching. Mneme constructs and governs the undertaking. Section 7 identifies the accepted conflict and the pending companion provisions. |
| Assignment design direction, pending agreement on this DRP | The six undertaking classes, the shared Tasks Topic scope, the result-bearing realised role, the JobCard and its attempts, atomic general-result acceptance, ThoughtProcess and Skepsis, and the Keleusma/Dokime assurance work roles. |
| Provisional detail proposed here | The semantic contract for `whereFrom()`, the association of framework execution details with their attempt and the observable boundary for atomic result acceptance. |
| Open | The assurance coordinator and activity names, policy and evidence details, representation correspondence and the further decisions in §9. |

## 3. Design context

The G03 Pragma-family drafts distinguish originating work, undertaking and working information. They leave canonical Task-family correspondence open and use `TaskOutcome` for an actual Ergon execution. The Processing Framework and Checkpoints, accepted with qualifications, places execution, output validation, outcome decision and handoff at distinct stages. This proposal gives the realised undertaking a governed result and preserves attribution of every actual attempt. Section 7 identifies both the pending G03 terminology and the adjudicated Task Topics that require reconciliation. [Mneme Pragma model][pragma-model] · [Pragmaton model][pragmaton-model] · [Processing Framework §§4.2–4.6][processing] · [G03 decisions][g03]

## 4. Architectural definition

### 4.1 Undertaking hierarchy and information contract

```text
Pragma
├── Pragmatosis
├── Stimulus
│   └── Noesis
└── Keleusma
    └── Dokime
```

The tree expresses conceptual inheritance. The originating forms are `Pragma`, `Stimulus` and `Keleusma`. Their corresponding realised undertakings are `Pragmatosis`, `Noesis` and `Dokime`. A realised type inherits the common work semantics and the relevant domain-specific input semantics. The realised types share the same result contract across the three branches. This draft leaves Java inheritance, interfaces and storage representation to detailed design.

| Information | Originating form | Realised undertaking |
| --- | --- | --- |
| Identity | Identifies one occurrence of work. The reusable definition and its applicable revision remain separately referenceable. | Has its own stable identity, lifecycle and lineage. Retry, redelivery and a new attempt preserve that identity. |
| `input()` | Exactly one governed InformationUnit reference under the current G03 direction. Preserve its applicable revision correspondence. | Inherits the same governed input reference and revision basis. Additional governed context, including multiple assurance evidence items, is acquired separately. |
| Provenance and `whereFrom()` | Provenance identifies the issuing source or producing activity and the basis for creating the work. The draft proposes `whereFrom()` express that immediate origin where established. | The draft proposes `whereFrom()` resolve the reference to the immediate originating Pragma, Stimulus or Keleusma. Provenance separately identifies how and under whose authority the realisation was established. |
| Subscriber Ergon identity | The origin can be offered to several interested Ergons. | Identify the logical subscribing Ergon on the general subscription path. A particular Ergon execution instance, subscription definition and queue each have separate identity. Specialised paths retain equivalent accountable Behaviour or assurance Ergon attribution under their reviewed admission contracts. |
| `output()` and `outcome()` | Neither operation is present. | `output()` contains zero or more governed InformationUnits attributable to the undertaking. `outcome()` exposes the common `TaskOutcome` for its governed result when established. |

The result contract exists throughout the realised undertaking's lifecycle. Before a governed result is established, an observer must be able to tell that no such result is available. The detailed presence representation and terminal rules remain open. Output attribution preserves the contributing attempt or activity and the applicable IU identity and revision. Governed references and content-free, non-authoritative summaries support access through Mneme under the applicable Themis controls. [Pragma model §§4.1–4.2, 4.6–4.8][pragma-model] · [Governed information and authority][iu-design]

Keep **execution state** and **TaskOutcome** separate. Execution state records progress and coordination, including whether an attempt is waiting, active, interrupted or settled. An actual attempt has an attributable execution result under Praxis rules, which may contribute to the realised undertaking's TaskOutcome and accepted output. Assurance findings, originating-work disposition, routing or persistence acknowledgements and IU lifecycle state keep their own meanings. An origin with zero subscribers has a selection disposition and no resulting undertaking or Ergon TaskOutcome. [Existing TaskOutcome Topic][task-outcome] · [G03 PF-03][g03] · [Processing Framework §4.5][processing-status]

### 4.2 Concept Topics

```text
Domain=ICT
.Owner=FHIRFactory
.Standard=Harmonia
.Concept=DataObject
.Concern=Workflow
.Scope:<version>=Tasks
.Entity=<entity>
```

The lines form one ordered Topic path. `Entity` takes exactly one of `Pragma`, `Pragmatosis`, `Stimulus`, `Noesis`, `Keleusma` or `Dokime`. All six Topics are siblings beneath `Tasks`. The conceptual inheritance in §4.1 does not alter Topic ancestry. Version governance applies at the `Tasks` Scope entry and covers the whole family. The Entity entries carry no independent version. `<version>` reserves the position for a reviewed version and allocates no value. Concept classification, the identity of an individual work occurrence, and the revision of its governed information each have their own semantics. Version-sensitive checks use the applicable Topic entry version under the Topic contract. [Topic and TopicMask Final.003 §§1–3][topic-design] · [Architecture-target Topic review][topic-review]

### 4.3 General processing

- **JobCard** is a Harmonia-governed operational InformationUnit accessed through Mneme. Each JobCard belongs to exactly one realised undertaking. In the general path it coordinates potentially concurrent execution attempts of a Pragmatosis within Ponos's execution responsibility. The exact lifecycle and whether an undertaking can be associated with successive JobCards remain open.
- **ExecutionAttempt** identifies one particular execution, with its own identity, Ergon instance, relevant policy basis, progress and attributable result. Concurrent or recovered execution preserves attempt-level attribution. Admission and retry follow Ponos and the applicable Praxis, including any `DoNotBefore` constraint. Delivery of a parcel alone provides no execution authority.
- **JobCard.WorkspaceMinutiae** is the proposed JobCard facet for framework-directed execution details attributable to the relevant attempt. The framework can use these details to make the applicable preparation, validation, recovery or close-out decisions. Exact correspondence to `Workspace.Processor.Status` and its audit lifecycle requires review. Pragmaton remains the candidate Mneme-managed working-information environment accessed through `Ergon.WorkSpace`. [Pragmaton §§3.7–3.12][pragmaton-workspace] · [Processing Framework §§4.4–4.6][processing-workspace]

An attempt's outputs and execution result are provisional until accepted under the applicable Praxis-defined policy. The policy governs admissibility, the participating attempt or attempts and the conditions for resolution. Acceptance shall be atomic at the realised undertaking boundary. An observer sees either no acceptance or the accepted decision with its corresponding outcome, output set and attribution. Concurrent or retried acceptance cannot establish incompatible results. If an acceptance reply is lost or the effect is indeterminate, recover the authoritative acceptance decision before retrying or exposing its outputs as accepted. The mechanism, permitted policy modes and treatment of provisional information already established through governed access remain open. The draft requires no cross-system transaction. [Task Topic policy controls][task-topic] · [Accepted Mneme–Mnemosyne interaction][persist] · [AX-15][ax15]

### 4.4 Digital Twin processing

`Stimulus` is originating Twin work. `Noesis` is its realised undertaking and carries the Twin-relative input lineage. A **ThoughtProcess** coordinates **Skepsis** considerations contributing to a Noesis. Their results are attributable to the considerations and evaluated under the applicable Praxis and Behaviour rules. A policy may require several contributions, an ordering or a combined result. This proposal supplies no first-finish-wins rule and makes no general JobCard attempt policy apply to Skepsis.

The adjudicated Domain05 Step-B constraints govern this proposal. Each Twin has one input queue for Stimulus and Effector and processes at most one input at a time. Interested Behaviours run sequentially. On the normal path, each loads fresh Twin-relative context after the applicable predecessor's governed save. Preserve the one-Ergo Praxis, same-Twin output queuing and the existing failure and authorised resumption semantics. Whether and how Skepsis may overlap within an admitted Behaviour remains open within these constraints. Adjudicate Twin fan-out, Noesis correspondence to `BehaviourStimulus`, Behaviour interest and input identity, and the separate treatment of `Effector` and `BehaviourEffector`. [DigitalTwin][twin] · [Behaviour][behaviour] · [A4.7 source and deferred details][twin-note] · [G03 PF-01][g03]

### 4.5 Assurance processing

`Keleusma` is the originating assurance request. `Dokime` is its realised undertaking. Dokimasia loads authorised evidence through the governed access boundary, evaluates the applicable criteria and generates attributable outcomes. Criteria may be composite. The applicable basis must identify the subject and relevant criteria and revisions, the evidence used, and how the resulting findings relate to that basis. The coordinator and individual activity names remain undecided. The assurance Ergon chooses its internal evaluation algorithm subject to the approved criteria, evidence and reporting contract.

The Dokime TaskOutcome reports the result of performing the assurance undertaking. A governed assurance finding produced by that work retains its own meaning and can be carried as an output InformationUnit under its owning contract. A successfully performed evaluation can yield an insufficient-evidence finding. The subject activity owns its own work and outcome and cannot determine its independent assurance. Preserve the finite assurance boundary and the existing self-audit rule for a Dokimasia execution, together with applicable security and evidence obligations. [Independent Assurance acceptance][assurance-acceptance] · [Dokimasia][dokimasia] · [Finite boundary][finite] · [Self-audit boundary][self-audit]

### 4.6 Execution and persistence boundaries

| Concern | Architectural responsibility |
| --- | --- |
| Governed undertaking information | Mneme constructs and manages the six work forms and their version-aware information references. Mnemosyne establishes required durable state via the governed interaction. |
| Operational coordination and execution state | Ponos and the specialised execution responsibilities apply the relevant policy. Coordinators and attempt/activity records describe how the undertaking progresses. Mneme provides governed operational information access, including JobCard access. |
| InformationUnit outputs | Ergon processing may produce candidate IUs. Governed validation and establishment, applicable release rules and accepted-result attribution determine what can appear in a realised undertaking's output. Returning changes to existing governed information follows the applicable Mneme save contract. |
| Framework-directed execution details | The processing boundary conveys attributable execution details alongside the produced IUs. Context unloading, outcome decision, checkpointing, recovery and applicable audit use these details under their own contracts. |
| Praxis-defined policies | Energeia manages the Praxis definition and applicable criteria. Ponos governs interpretation for execution and progression under the existing responsibilities. Specialised policies retain their own Twin and independent assurance constraints. |
| Ergon-specific processing | `Ergon.process()` and its specialised equivalents determine internal business or evaluation logic. The framework governs the exchanged outputs, relevant details and their attribution. |

Persistence is the default architectural preference for governed undertaking and coordination records where the authoritative facts are needed for progression, recovery or durability. Determine the persistence and retention profile from performance, recovery, durability, security and evidence requirements. Local workspace values and incidental diagnostics follow AX-09's ephemeral default. Promote meaningful decision and evidence facts into the applicable governed representations. Governed operational information may have a bounded lifetime. Access and save preserve Mneme/Mnemosyne authority, revision and acknowledgment semantics. [AX-09][ax09] · [Two-cache decision][cache] · [Lifecycle and operational state][lifecycle] · [Accepted IU development correspondence][bl5]

### 4.7 Subscription realisation and routing reconciliation

The Solution Architect's cardinality decision applies to the pair **(originating Pragma identity, logical subscribing Ergon identity)**. The pair specifies the uniqueness scope and leaves identifier encoding and the authoritative establishment mechanism for review. Petasos determines the matches and owns the routing decision. Mneme enforces the cardinality as part of establishing or recovering the governed undertaking, including concurrent requests and lost construction replies. All obligations for that pair can reference the same established Pragmatosis. Different subscribing Ergons can have distinct undertakings for the same Pragma.

Keep the matching subscription definitions and their revision basis, each existing per-subscription routing obligation, intended destination, work reference and delivery/recovery evidence independently attributable. Recover the complete durable decision and obligation set, including a proved empty set, before fulfilment. If a subscription's delivery result is indeterminate, retain that uncertainty with the original obligation. Resolving the shared undertaking proves its identity and does not prove queue acceptance or execution for any obligation. Final.001's H10 originating-work establishment and H11 durable complete-list acceptance remain distinct from subscription matching, routing obligations and work execution. The direct Praxis-Router referent and repeat-selection contract remain under their own review. [Routing Final.001 §§1–4][routing] · [Failure and evidence §§5–6][routing-recovery]

**Example for review.** An originating Pragma matches subscriptions A and B for Ergon X and subscription C for Ergon Y. The decision records three obligations and at most two Pragmatoses. A and B point to X's undertaking once established. If A's injection is confirmed and B's is indeterminate, recovery preserves both facts and reconciles B at the owning boundary. Whether one physical delivery can satisfy several separately recorded obligations needs explicit adjudication, including when their destinations or policies differ. Ponos retains admission authority for any delivery.

## 5. Detailed design delegated to the implementing team

Detailed design selects model interfaces, persistence schema and coordination mechanism within the reviewed contracts. It also specifies provisional output staging, contention handling, attempt observation, composite criteria evaluation and internal processing algorithms. The architectural review establishes the observable semantics and owning boundaries first.

## 6. Evidence and rationale

Stable work identity lets independent routing obligations and execution attempts recover without duplicating the undertaking. Separate output and processing-detail contracts preserve attributable results and support framework-directed evidence. Reuse of Mneme/Mnemosyne, Ponos/Praxis and Petasos keeps each decision and effect at its owning boundary. The existing Task controls already separate execution concurrency, participation in resolving work, and output composition. Whether those controls map to a distinct originating-work resolution contract or also inform the new undertaking-level acceptance policy requires explicit review. [Task Topic][task-topic] · [Routing reconciliation][routing-recovery] · [Processing Framework][processing]

The matrix below distinguishes recorded Solution Architect dispositions and canonical element standing from candidate correspondence and historical discovery. The repository's source succession governs use of wrapped legacy sections. [Architecture Governance][governance] · [A4.7 source succession][twin-note]

## 7. Reconciliation matrix

| Source and exact provision | Recorded standing | Reconciliation for Solution Architect review |
| --- | --- | --- |
| **Petasos Routing and Reconciliation Final.001** §1 opening and construction row, §3 SubscriberSet loop and creation/injection evidence, §§5–6 recovery and obligation/work correspondence, §8 same-subscriber conformance. [§1][routing] · [§3][routing-subscription] · [§5][routing-recovery] · [§6][routing-evidence] · [§8][routing-tests] | Offline design accepted with qualifications under G03 / DR-12. [Disposition][routing-disposition] | **Direct conflict.** Replace one undertaking per applicable subscription with one per origin and subscribing Ergon, subject to adjudication. Retain every applicable obligation, its evidence, H10/H11, and PF-08 durable decision and recovery. Decide physical delivery equivalence separately. |
| **Pragma Publication and Subscription Draft.000.003** §2, PS-09–PS-12, §§5–7. [PS-09–PS-12][publication-selection] · [§5][publication-reliability] | Pending G03 candidate carrying issued allocations. [DR-12][g03-review] | Replace per-match realisation and distinct same-Ergon undertakings. Clarify independent progress of routing obligations, shared undertaking lineage, and the distinction between each attempt's result and the undertaking's outcome. Preserve producer eligibility, zero matches and separate confirmation boundaries. |
| **Interaction Petasos–Mneme Draft.000.004** §§1.2, 4.1–4.4 and 8, especially step 2 in §4.2 and the multiple matching subscriptions row in §4.4. [§1.2][petasos-mneme-req] · [§4.2][petasos-mneme-identity] · [§4.4][petasos-mneme-retry] · [§8][petasos-mneme-tests] | Pending G03 companion. | Align construction/recovery and tests with one authoritative undertaking per pair and retained per-obligation evidence. Map governed lookup, uniqueness, acknowledgement and lost-response recovery. |
| **Mneme Pragma Model Draft.000.004** §§1.2, 4.1–4.2, 4.4–4.5, 4.8 and 9. [§4.1][pragma-model] · [§4.4][pragma-progression] · [§4.8][pragma-outcomes] | Pending G03 candidate. | Move `output()` and `outcome()` to the realised role. Align the per-match row and clarify Petasos's request versus Mneme's construction and information authority. Reconcile the attempt result and realised TaskOutcome, subscriber identity, origins, Topic scope and specialised names. |
| **Mneme Pragmaton Model Draft.000.004** §§3.1, 3.7 and 3.9–3.13, and **Ergon–Mneme Pragmaton Interaction Draft.000.004** §§4.1–4.7. [Pragmaton][pragmaton-model] · [Ergon–Mneme][ergon-mneme] | Pending G03 candidates. The underlying two-cache allocation is accepted with qualifications. [Cache decision][cache] | Map Pragmaton/WorkSpace, attempt-specific context and governed IU references across all three realised forms. Reconcile `WorkspaceMinutiae` and save/unload with the accepted processor-status and generated-output boundaries. Determine the formal association, lifetime and recovery needs. |
| **Harmonia Processing Framework and Checkpoints Final.001** §§4.2–4.6 and 5. [Stages and checkpoints][processing] · [WorkSpace transfer and status][processing-workspace] · [Checkpoint contract][checkpoints] | Offline design accepted with qualifications. [Disposition][processing-disposition] | Reconcile the whole, output-bearing Egress Pragma transferred by value at H05 with the realised undertaking contract. Preserve the transfer meaning, H10's distinct originating work, the status domains and Kleio capture. Decide how a realised TaskOutcome is established alongside the actual Ergon-instance outcome. |
| **TaskDefinition, Task, TaskFulfillment and TaskOutcome** Topics. [TaskDefinition][task-definition] · [Task][task-topic] · [TaskFulfillment][task-fulfillment] · [TaskOutcome][task-outcome] | Bounded adjudicated conceptual meanings, with runtime and mapping reservations. | Existing TaskOutcome resolves originating `ActionableTask` and belongs to it. G03 uses the term for actual Ergon-instance processing. The draft puts a common TaskOutcome on the realised undertaking. Adjudicate all three meanings, origin disposition, the concurrency/participation/composition controls and any mapping from `ActionableTask`/`FulfillmentTask` before incorporation. |
| **Pragma Management and Topic, Petasos and PetasosParcel**, plus the conceptual **PetasosParcel/Mask Final.001**. [Management][pragma-management] · [Pragma][canonical-pragma] · [Petasos][canonical-petasos] · [Parcel Topic][canonical-parcel] · [Parcel design][parcel-final] | Qualified Domain05 canonical descriptions and accepted bounded offline Parcel/Mask design. | Reconcile earlier Mneme selection, originating-work parcel referent and receiving-side establishment with the Petasos matching and Mneme establishment direction. Preserve by-value conveyance, other parcel purposes, and PF-06 / DR-10 selector qualifications. Also reconcile Pragma-stage filtering with the G03 producer IU gate and the qualified Ponos/Praxis continuation description. |
| **DigitalTwin, Behaviour** and **Digital Twin Behaviour and Execution Semantics** §§2, 4–11 and 13. [DigitalTwin][twin] · [Behaviour][behaviour] · [A4.7 DesignNote][twin-note] | Adjudicated Domain05 Step-B candidate with final architecture unapproved. Incorporated excerpts use the canonical elements and remaining legacy material retains its recorded standing. | Map Stimulus/Noesis and ThoughtProcess/Skepsis to governed interest, Behaviour-specific work, Twin-relative identity and fresh context. Carry Effector and failure/recovery qualifications. Reconcile the ordinary same-Ergon FulfillmentTask concurrency description with attempts on a shared undertaking where applicable. |
| **Dokimasia**, **Provenance and Assurance** DR-18/19, and **Energeia, DigitalTwin and Dokimasia Specialisation** §§8–10. [Component][dokimasia] · [Accepted DR-18/19][assurance-acceptance] · [Candidate specialisation note][dokimasia-note] | Accepted independent assurance and provenance responsibilities with detailed obligations. The Domain05 component and historical specialisation descriptions retain their qualifications. | Map Keleusma and Dokime to the existing candidate request/realisation roles. Decide contract for composite criteria, `Dokimasia.Praxis`, finding/output linkage and coordinator/activity names while preserving evidence, independence and finite self-audit boundaries. |
| **Mneme–Mnemosyne Interaction and two-cache acceptance**, **InformationUnit acceptance**, **Calliope Lifecycle, Revision and Operational State Final.001.000** §§1, 3–4, and the **BL-0005** accepted increment. [Interaction][persist] · [IU revisions][iu-design] · [Lifecycle][lifecycle] · [BL-0005][bl5] | Accepted Architecture contracts and qualified offline lifecycle design. BL-0005 is an accepted development increment with specified IU design supersessions and separate incorporation obligations. | Bind the work and JobCard policies to governed authority, revision, recovery, retention and applicable acknowledgment. Determine durable versus transient execution facts and operational projections. Keep IU changes, TaskOutcome and attempt/transport state semantically distinct. |
| **Topic and TopicMask Final.003** §§1–3 and pending Architecture-target correspondence. [Topic design][topic-design] · [Architecture review][topic-review] | Accepted offline Topic design with separately governed incorporation. | Map and version the Tasks concept scope in one decision. Determine coexistence or migration for affected selectors and references to the existing Task-family concepts. |

## 8. Conformance expectations

- Verify the three origin/realisation pairs preserve independent work identities and input identity and revision. Verify realised outputs and outcome access, and the absence of result operations on each originating form. Exercise zero outputs and work with no resulting execution.
- Verify the six ordered Topic paths share the `Tasks` prefix with version only on Scope once reviewed. Check version-aware round trips and selector behaviour using established Topic semantics.
- Exercise concurrent attempts, accepted single or combined results per authorised policy, an interrupted acceptance or lost reply, and recovery from authoritative state. Verify provisional results carry their own attribution and cannot appear as accepted output before the governing decision.
- Exercise same-Ergon multi-subscription matches, different Ergons, concurrent establishment and repeat delivery after lost construction, routing or acceptance replies. Verify one work identity per pair, distinct obligation histories, preserved uncertainty and separate proof of execution eligibility.
- Exercise Twin sequencing and context refresh with several Behaviours and considerations. Demonstrate that early completion of one Skepsis provides no automatic acceptance. For assurance, exercise composite criteria, insufficient evidence, independent outcomes and the finite evidence boundary.
- Verify authorised governed access and audit/evidence for meaningful effects. Verify that cache loss cannot supply authoritative version or accepted-result proof and that the history required for recovery remains available under the applicable durability contract.

## 9. Open questions for Solution Architect review

1. **Routing conflict and identity scope.** Which accepted Routing Final.001 provisions should be amended to apply the issued cardinality decision? Confirm how logical Ergon identity behaves under registration changes, version changes, aliases, republishing and explicitly authorised replay. When can one physical delivery satisfy several obligations, and what proves that result for each obligation?
2. **Work and outcomes.** How do `TaskDefinition`, `Task`, `TaskFulfillment` and the existing originating `TaskOutcome` map to the six classes? Determine the representation of attempt results, the realised TaskOutcome and the originating-work disposition, including applicability of existing participation and output-composition rules and what callers observe before resolution.
3. **Lineage and classification.** Confirm `whereFrom()` and fallback for an unavailable or externally supplied origin, the minimum input revision and provenance requirements, and the Tasks version and compatibility rules for existing references and selectors.
4. **General coordination and atomic acceptance.** Establish JobCard/undertaking multiplicity and lifetime, the owning scope of attempts and `WorkspaceMinutiae`, the acceptance policy inputs and allowed modes, and the minimum authoritative proof and recovery rules. Decide visibility and disposition of losing, late and partially established outputs and controlled side effects.
5. **Framework handovers.** Reconcile H05's whole Egress Pragma and H07's actual execution outcome with the realised work model. Determine working-value save and output establishment order, `Workspace.Processor.Status` correspondence and closure capture, and how accepted results drive publication, Pathway continuation and later H10/H11. Keep the PF-07/PF-08 and DR-7/DR-10 evidence and security obligations in scope.
6. **Twin processing.** Decide Noesis/BehaviourStimulus correspondence, Effector treatment, the ThoughtProcess and Skepsis identities and lifecycles, allowable overlap and contribution policy, and recovery after uncertain Context Save inside the existing Twin serialisation rules.
7. **Assurance processing.** Name the coordinator and individual activity. Determine `Dokimasia.Praxis` derivation and revision, composite criteria semantics, evidence access and missing-evidence treatment, and how findings and TaskOutcome are governed together within the finite boundary.
8. **Durability and evidence.** Set required persistence and retention by record and meaningful fact, including attempt and consideration histories, approved recovery boundaries and required Kleio/Provenance evidence. Determine performance criteria and how to verify loss and failover under the existing Mneme/Mnemosyne contract.
9. **Governed review placement.** Establish the HCRL target and iteration for adjudicating this proposal and recording the source decision and resulting correspondence. Existing G03 / DR-12 provides related review history. [Review Praxis][review-praxis] · [Change Provenance][change-provenance]

## 10. Proposed implementation backlog decomposition

Each row is a proposed work package with an independent verification boundary. Later item IDs and revisions belong to separately prepared Backlog Items. The dependencies below refer to reviewed contracts or prior deliverables. Implementation requires a separate assignment. [Software Development Praxis][software-praxis] · [Backlog Item template][backlog-template]

| Proposed item | Bounded outcome and dependency | Acceptance evidence |
| --- | --- | --- |
| **BL-0006 Calliope Pragma** | Establish the pure Calliope originating/realised information contract, starting with Pragma and Pragmatosis, and the shared result and Tasks classification rules. Depend on review of the core identity, `whereFrom()`, outcome and Topic version contracts and reuse BL-0005's IU model. | Model-level fixtures verify independent identity, lineage, reference/version fidelity, origin/result separation, zero or multiple outputs, and Topic scope semantics. |
| Mneme undertaking establishment and access | Depend on BL-0006 and the reviewed identity/persistence contract. Establish or recover one authoritative general undertaking per eligible origin/Ergon pair and provide governed access to it. | Concurrent callers and a lost creation reply resolve the same authorised identity. Cache loss and denial exercise authoritative recovery and access boundaries. |
| Petasos subscription reconciliation | Depend on Mneme establishment and the adjudicated routing amendment. Use the shared undertaking across applicable obligations with durable decision capture, distinct evidence and bounded retry. | A multi-match scenario retains every obligation and survives interrupted creation and partial delivery. Empty matching, changed definitions and indeterminate acceptance preserve their own evidence. |
| General execution coordination and acceptance | Depend on governed undertaking access and the reviewed JobCard/attempt policy. Establish JobCard access and atomic acceptance under Ponos/Praxis. Verify against controlled routing inputs without requiring the production Petasos increment. | Competing attempts, aggregate policy where authorised, stale/retried acceptance and a lost reply expose a coherent accepted result and attributable attempt history through Mneme. |
| Ergon processing and result handover | Depend on the reviewed processing correspondence and acceptance contract. Establish the processing-detail and output boundary through Context Unloader, validation, outcome decision and egress. | Exercise working-value save, provisional and accepted generated outputs, details and audit capture, and preservation of H05/H07 and H10/H11 evidence boundaries. |
| Digital Twin undertaking and consideration coordination | Depend on the common model and reviewed Twin contracts. Realise Stimulus/Noesis and ThoughtProcess/Skepsis within established Behaviour execution semantics. | Verify Twin-relative lineage, stable sequencing, fresh context, same-Twin queuing, governed consideration contribution and authorised recovery. |
| Dokimasia undertaking and evidence evaluation | Depend on the common model and reviewed assurance contracts. Realise Keleusma/Dokime and the adjudicated coordination roles with governed evidence and composite criteria. | Verify attribution to criteria/evidence revisions, independent finding and execution outcomes, insufficient evidence and the finite assurance boundary. |

## 11. Review decision

**Pending.** The Solution Architect is asked to adjudicate the routing conflict and open questions against the cited sources. This draft is the review artefact for the initial design assignment. [Architecture Review Praxis][review-praxis]

## 12. Post-decision actions

After disposition, preserve the exact reviewed draft and decision provenance in the applicable HCRL and reconcile only the authorised provisions. Establish canonical identity, meaning and relationship correspondence before any Continuum update. Governed application and implementation follow their respective assignments. [Change Provenance][change-provenance] · [Software Development Praxis][software-praxis]

## Appendix A — Design provenance

The operative input is the **Codex Assignment — Harmonia Undertaking and Execution Framework** for `feature/BL-0006-Calliope-Pragma`. Section 6 of that assignment supplies the cardinality decision recorded in §§2 and 4.7. The review used Architecture 1.1.26 and the development line following the accepted BL-0005 increment. The cumulative G03 review records the standing of the candidate family and the acceptance with qualifications of the offline Routing and Processing DRPs. [Architecture baseline][architecture-baseline] · [BL-0005 acceptance][bl5] · [G03 review][g03-review]

[architecture-baseline]: ../register/1.1.26/hcrl-1.1.26-001.complete.md
[axioms]: ../../governance/architectural-axioms.md
[ax09]: ../../continuum/motivation/principle/9a5cb9e8876844e58e660f909f7df9fc.md#architectural-content
[ax15]: ../../continuum/motivation/principle/ef8abbaec37e4890ad78b9f263e67906.md#architectural-content
[governance]: ../../governance/architecture-governance.md#governance-enforcement-and-fail-closed-authority
[review-praxis]: ../../governance/architecture-review-praxis.md
[change-provenance]: ../../governance/architecture-change-provenance.md#ai-agent-activity-provenance
[software-praxis]: ../../governance/software-development-praxis.md#3-backlog-items-assignments-and-traceability
[backlog-template]: ../../../docs/backlog/backlog-item-template.md
[bl5]: ../register/software-0.1.0/hcrl-software-0.1.0-011.complete.md
[publication-feature]: ../../continuum/strategy/capability/feature/c637e91e3d5e402386ff352d0326c1c5.md
[matching-feature]: ../../continuum/strategy/capability/feature/8b9531db49334913a1d8f0a460227fc7.md
[lineage]: ../../continuum/strategy/capability/feature/157b20d5c544429a8ada854c45ec244f.md
[scheduler]: ../../continuum/strategy/capability/feature/48977be5d3b24f82ba676a0b8a26e0d6.md
[assurance-feature]: ../../continuum/strategy/capability/feature/aa69f4ff110044da918213b0c49008e5.md
[assurance-req]: ../../continuum/motivation/requirement/bbb7438e78604308abba93afc14f4e2e.md
[core004]: ../../continuum/motivation/requirement/assurance-requirement/f2e504e7d6374e41a81303265adda6de.md
[prov001]: ../../continuum/motivation/requirement/assurance-requirement/3e45e60affb44bf384d54de26d3add6a.md
[g03]: ../evidence/software-0.1.0/005/after/G03-Pragma-Family-Adjudications-005.md#pf-01pf-12-disposition
[g03-review]: ../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
[routing-disposition]: ../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#g03-petasos-routing-final-001
[processing-disposition]: ../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#g03-processing-framework-final-001
[routing]: DRP-Petasos-Routing-and-Reconciliation.Final.001.md#1-common-strategy
[routing-subscription]: DRP-Petasos-Routing-and-Reconciliation.Final.001.md#3-petasossubscription-router
[routing-recovery]: DRP-Petasos-Routing-and-Reconciliation.Final.001.md#5-failure-and-recovery-analysis
[routing-evidence]: DRP-Petasos-Routing-and-Reconciliation.Final.001.md#6-minimum-identity-and-evidence-for-reconciliation
[routing-tests]: DRP-Petasos-Routing-and-Reconciliation.Final.001.md#8-conformance-expectations
[publication-selection]: DRP-Pragma-Publication-and-Subscription.draft.000.003.md#42-selection-realisation-and-handoff
[publication-reliability]: DRP-Pragma-Publication-and-Subscription.draft.000.003.md#5-reliability-and-consistency-requirements
[petasos-mneme-req]: DRP-Interaction-Petasos-to-Mneme.draft.000.004.md#12-associated-requirements
[petasos-mneme-identity]: DRP-Interaction-Petasos-to-Mneme.draft.000.004.md#42-work-identity-and-reference-handoff
[petasos-mneme-retry]: DRP-Interaction-Petasos-to-Mneme.draft.000.004.md#44-duplicate-delivery-retry-and-recovery
[petasos-mneme-tests]: DRP-Interaction-Petasos-to-Mneme.draft.000.004.md#8-conformance-expectations
[pragma-model]: DRP-Mneme-Pragma-Model.draft.000.004.md#41-work-identity-and-realisation
[pragma-progression]: DRP-Mneme-Pragma-Model.draft.000.004.md#44-progression-and-scheduling
[pragma-outcomes]: DRP-Mneme-Pragma-Model.draft.000.004.md#48-execution-outcomes-and-work-disposition
[pragmaton-model]: DRP-Mneme-Pragmaton-Model.draft.000.004.md#31-identity
[pragmaton-workspace]: DRP-Mneme-Pragmaton-Model.draft.000.004.md#37-pragmaton-and-ergonworkspace
[ergon-mneme]: DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.000.004.md#4-architectural-definition
[processing]: DRP-Harmonia-Processing-Framework-and-Checkpoints.Final.001.md#42-stage-and-checkpoint-correspondence
[processing-workspace]: DRP-Harmonia-Processing-Framework-and-Checkpoints.Final.001.md#44-workspace-information-and-context-unloader
[processing-status]: DRP-Harmonia-Processing-Framework-and-Checkpoints.Final.001.md#45-status-domains-and-processing-instance-closure
[checkpoints]: DRP-Harmonia-Processing-Framework-and-Checkpoints.Final.001.md#5-checkpoint-architecture
[task-definition]: ../../continuum/application/data-object/topic/1d6d4c3a2df440be94262855fa6564eb.md#architectural-content
[task-topic]: ../../continuum/application/data-object/topic/c43139289cb649b4836521a96a51604c.md#architectural-content
[task-fulfillment]: ../../continuum/application/data-object/topic/30f4deffcd694f07bf54b2b192695d4f.md#architectural-content
[task-outcome]: ../../continuum/application/data-object/topic/08d8ee19a30f418f9d5bc497ced0d1ef.md#architectural-content
[pragma-management]: ../../continuum/application/application-function/ab0cdaa7ed4149ee856216efa5a265c4.md#architectural-content
[canonical-pragma]: ../../continuum/application/data-object/27ad143e7c784e378556af8a57c438f7.md#architectural-content
[canonical-petasos]: ../../continuum/application/application-component/195e5f10db8e419391354197b5d7a8cc.md#architectural-content
[canonical-parcel]: ../../continuum/application/data-object/f0a88f05a0cb438cb42572ec07fb8513.md#architectural-content
[parcel-final]: DRP-PetasosParcel-and-PetasosParcelMask.Final.001.md
[twin]: ../../continuum/application/application-component/3f64d339648246e99e8866c8089f8960.md#architectural-content
[behaviour]: ../../continuum/application/application-component/a26f06d66bec4981b266e202c46a9afb.md#architectural-content
[twin-note]: ../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Digital-Twin-Behaviour-and-Execution-Semantics.md#status
[dokimasia]: ../../continuum/application/application-component/d739c75dffe2453081735710b285f03b.md#architectural-content
[assurance-acceptance]: ../register/1.1.20/hcrl-1.1.20-001.complete.md#dr-18
[dokimasia-note]: ../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Energeia-DigitalTwin-Dokimasia-Specialisation.md#8-dokimasia-specialisation
[finite]: ../../continuum/motivation/constraint/3ba73277abf146728ca8c1f5846227d7.md#architectural-content
[self-audit]: ../../continuum/motivation/constraint/e1ce978457e543d5894de24e9be9ac2c.md#architectural-content
[persist]: ../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-1
[cache]: ../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-3
[iu-design]: ../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-4
[lifecycle]: DRP-Calliope-Lifecycle-Revision-and-Operational-State.Final.001.000.md
[topic-design]: DRP-Topic-and-TopicMask.Final.003.md#1-topic-identity-and-structure
[topic-review]: ../review/1.1.28/hcrl-1.1.28-001.pending.md#dr-8
