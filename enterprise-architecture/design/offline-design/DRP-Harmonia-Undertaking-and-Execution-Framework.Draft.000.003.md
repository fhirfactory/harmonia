<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Design Review Proposal — Harmonia Undertaking and Execution Framework

| Review field | Value |
| --- | --- |
| Revision | Draft.000.003 |
| Standing | Pending Solution Architect review |
| Prepared by | Codex |
| Solution Architect | Mark Hunter |
| Architecture baseline | 1.1.26 |
| Software development identity | `0.1.0-SNAPSHOT` |
| Source | [Draft.000.002][predecessor] on `feature/BL-0006-Calliope-Pragma` at `590d268af553dac40455aa63815d141a4352dcf2` |

## 1. Feature realisation

The framework gives general, Digital Twin and assurance processing common semantics for originating work, realising a particular undertaking, obtaining governed context and recording attributable results. It reuses the accepted **EligibleTaskPublication**, **SubscriptionMatchCorrect**, **RecoveryPreservesTaskLineage**, **SingleSchedulingAuthority** and **Assurance Evidence Correlation** Features. Those Features support the work across publication, routing, execution, recovery and independent assurance. Placement of a shared execution-coordination Feature, if needed after mapping existing Features, is a review matter. [Publication][publication-feature] · [Subscription matching][matching-feature] · [Recovery lineage][lineage] · [Scheduling][scheduler] · [Assurance evidence][assurance-feature]

CORE-004 and PROV-001 support correlation and causation through origin, undertaking, execution and handoff. REQ-FND-005 governs independent assurance. The design preserves immutable input and origin lineage, attributes activity to the applicable operational record, and provides recovery of the single subscription-routing obligation per resulting Pragmatosis. Section 8 gives observable conformance checks. [CORE-004][core004] · [PROV-001][prov001] · [REQ-FND-005][assurance-req]

| Applicable axioms | Consequence for this proposal |
| --- | --- |
| AX-04, AX-18 | The applicable framework or engine coordinates execution under Praxis. Petasos owns matching and routing, and Mneme governs access to the work information. Reuse these capabilities and the existing persistence and checkpoint boundaries. |
| AX-05, AX-06, AX-09 | Mneme provides active access and coordination. Mnemosyne establishes durable authority. Keep transient working detail purpose-bounded and persist the facts needed for recovery, information authority and evidence. |
| AX-07, AX-08 | Apply Themis to protected access and effects. Preserve attributable provenance and applicable Kleio capture for meaningful facts, with the existing Dokimasia boundary. |
| AX-10, AX-15, AX-16 | Competing attempts, lost replies and recovery preserve known effects and uncertainty. Confirm accepted progression against the applicable authoritative record. |
| AX-14, AX-17 | Keep work, execution and outcome meanings distinct. Submit the existing authority conflicts and unresolved mappings for explicit disposition. |

The canonical Principles define each axiom. [Architectural Axioms][axioms]

## 2. Proposal and standing

| Standing in this draft | Content |
| --- | --- |
| Solution Architect decisions for this revision | Apply the three family relationships in §4.1. The applicable framework or engine coordinates processing under Praxis. Each undertaking has one governed operational record and one stable TaskOutcome created atomically on receiving Ergon acceptance from Petasos. Domain-specific activity and concurrency rules retain their own scope. [Decision provenance][decisions] |
| Design direction carried forward | The six undertaking classes, inherited source and immutable input, separate origin provenance, shared Tasks Topic scope, result-bearing realised role, existing Task correspondence and atomic adjudication. The subscription path still uses first successful match per Ergon and one routing obligation per resulting Pragmatosis. [Draft.000.002][predecessor] |
| Open | Detailed contracts, specialised coordination and the remaining reconciliation questions in §9. |

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

The tree expresses conceptual inheritance and the table identifies the architectural relationships across work, operational records and individual activities. A realised undertaking inherits the common work semantics and its domain-specific input semantics. Detailed design owns Java inheritance, interfaces, composition and storage, including any choice to use a shared operational-record superclass. Each operational record has its own domain semantics within the common architectural contract.

| Initiating form | Realised undertaking | Operational record | Individual activity record |
| --- | --- | --- | --- |
| Pragma | Pragmatosis | JobCard | ExecutionAttempt |
| Stimulus | Noesis | ThoughtProcess | Skepsis |
| Keleusma | Dokime | Tekmerion | Dossier |

| Information | Originating form | Realised undertaking |
| --- | --- | --- |
| Identity | Identifies one occurrence of work. The reusable definition and its applicable revision remain separately referenceable. | Has its own stable identity, lifecycle and lineage. Retry, redelivery and a new attempt preserve that identity. |
| `input()` | References one immutable governed InformationUnit, with its identity and revision. | Inherits `input()` unchanged. It refers to the same immutable InformationUnit at the same revision. Additional governed context, including assurance evidence, is acquired separately. |
| `whereFrom()` | Carries the source information of the originating work. | Inherits `whereFrom()` unchanged from the originating form. |
| Provenance | Identifies issuance or production and the basis for creating the work. | A separate provenance relationship identifies the originating `Pragma`, `Stimulus` or `Keleusma`. Realisation provenance also records the basis for establishing the undertaking. |
| Subscriber Ergon identity | The origin can be offered to several interested Ergons. | Identify the logical subscribing Ergon on the general subscription path. A particular Ergon execution instance, subscription definition and queue each have separate identity. Specialised paths retain equivalent accountable Behaviour or assurance Ergon attribution under their reviewed admission contracts. |
| `output()` and `outcome()` | Neither operation is present. | `output()` contains zero or more governed InformationUnits attributable to the undertaking. `outcome()` exposes the separately governed `TaskOutcome` created at receiving Ergon acceptance. Its initial disposition is unresolved. |

An observer must be able to distinguish an unaccepted undertaking, an accepted undertaking with an unresolved TaskOutcome, and an undertaking with an adjudicated TaskOutcome. Output attribution preserves the contributing attempt or activity and the applicable IU identity and revision. Governed references and content-free, non-authoritative summaries support access through Mneme under the applicable Themis controls. [Pragma model §§4.1–4.2, 4.6–4.8][pragma-model] · [Governed information and authority][iu-design]

**Execution state** records operational progress. An **execution result** is attributable to the individual activity and remains provisional until adjudicated. The **TaskOutcome** records the undertaking's unresolved or adjudicated disposition under the applicable Praxis rules. Assurance findings, originating-work disposition, routing or persistence acknowledgements and IU lifecycle state keep their own meanings. An origin with zero subscribers has a selection disposition and no resulting undertaking or TaskOutcome. [Existing TaskOutcome Topic][task-outcome] · [G03 PF-03][g03] · [Processing Framework §4.5][processing-status]

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

### 4.3 Operational records and common TaskOutcome lifecycle

**JobCard**, **ThoughtProcess** and **Tekmerion** are governed operational information objects. Each holds operational information, activity history, framework observations, developer-reported details and a stable `TaskOutcomeReference` to its separately governed outcome. Other governed outcome references follow the applicable domain contract. The processing framework or engine coordinates the work and applies Praxis-defined policies. Each record and its individual activity records retain their domain-specific semantics. [Processing Framework §§4.4–4.6][processing-workspace] · [Accepted Mneme–Mnemosyne interaction][persist]

#### Acceptance, recovery and adjudication

| Event | Governed effect across all three families |
| --- | --- |
| Receiving Ergon accepts the undertaking from Petasos | Atomically establish one operational record, one separately governed `TaskOutcome` in an unresolved state and the record's stable `TaskOutcomeReference`. Successful acceptance exposes the complete pair and reference as one governed effect. The pair belongs to exactly one realised undertaking, which retains that same pair throughout its lifetime once established. |
| Redelivery, retry or concurrent execution | Resolve the existing operational record and TaskOutcome by governed identity. Any newly recorded activity belongs to the existing record under the applicable family rules. The pair and reference keep their identities. |
| Context loading or processing fails | Record the operational failure in the operational record with the relevant activity attribution. The applicable Praxis governs adjudication of the existing TaskOutcome. |
| An activity result becomes available | Retain the attributable provisional result and any provisional outputs in their respective contracts. Praxis governs which results contribute and how they are qualified. |
| Framework adjudicates under Praxis | Establish the adjudicated disposition, accepted outputs and attribution atomically at the undertaking boundary. Exhaustion of permitted execution attempts requires explicit adjudication, including an unsuccessful outcome where applicable. |

The routing path in §4.7 can establish the undertaking's identity before handoff. The exact correspondence of receiving Ergon acceptance to H01 receipt, H02 admission, Petasos acknowledgements and the specialised ingress boundaries requires review. Contextual loading after acceptance operates with the existing TaskOutcome. If context-loading failures exhaust the permitted attempts, the framework explicitly adjudicates that outcome under Praxis. [Processing Framework §§4.1–4.2][processing] · [Routing Final.001][routing-subscription]

Mneme provides governed access and recovery through Mnemosyne, which establishes authoritative durability, including the pair and its reference. Preserve provenance of the acceptance, participating activities and adjudication through the governed information path. When the effect of acceptance or adjudication is indeterminate, reconcile it through authoritative evidence before retrying or treating the effect as confirmed. The acceptance invariant defines the required visible effect. The persistence mechanism and proof of atomicity remain for review under the existing Mneme–Mnemosyne contract. [Accepted Mneme–Mnemosyne interaction][persist] · [AX-15][ax15]

#### General processing

JobCard remains a Harmonia-governed operational InformationUnit accessed through Mneme. Ponos coordinates general execution and applies the relevant Praxis, including admission and any `DoNotBefore` constraint under the applicable Themis controls. The following JobCard details apply to the general family. The Twin and assurance families define their own activity and concurrency contracts. [Processing Framework §§4.4–4.6][processing-workspace]

| JobCard information | Meaning |
| --- | --- |
| `JobCard.ExecutionAttempt` | Identifies one execution and records framework observations such as Ergon instance identity, execution state, metrics and failures, with attributable execution results. A retry or concurrent execution adds an attempt to the same JobCard. Recovering an observation of existing execution preserves its attempt identity. |
| `JobCard.WorkspaceMinutiae` | Records developer-reported framework information from `Ergon.process()`. Each observation retains attribution to its originating attempt. Competing attempts cannot overwrite one another's observations. Detailed correspondence to `Workspace.Processor.Status`, including its audit lifecycle, remains for review. |

Pragmaton remains the candidate Mneme-managed working-information environment accessed through `Ergon.WorkSpace`. The framework supplies the context for preparation, validation, recovery and close-out. [Pragmaton §§3.7–3.12][pragmaton-workspace] · [Processing Framework §§4.4–4.6][processing-workspace]

### 4.4 Digital Twin processing

`Stimulus` is originating Twin work. `Noesis` is its realised undertaking and carries the Twin-relative input lineage. **ThoughtProcess** holds its governed operational history. Each **Skepsis** records an individual consideration. The Digital Twin processing framework coordinates considerations and applies the relevant Praxis within the existing Behaviour boundary. The contribution, adjudication and activity contracts remain open. Completion order supplies no default selection rule.

The adjudicated Domain05 Step-B constraints govern this proposal. Each Twin has one input queue for Stimulus and Effector and processes at most one input at a time. Interested Behaviours run sequentially. On the normal path, each loads fresh Twin-relative context after the applicable predecessor's governed save. Preserve the one-Ergo Praxis, same-Twin output queuing and the existing failure and authorised resumption semantics. Whether and how Skepsis may overlap within an admitted Behaviour remains open within these constraints. Adjudicate Twin fan-out, Noesis correspondence to `BehaviourStimulus`, Behaviour interest and input identity, and the separate treatment of `Effector` and `BehaviourEffector`. [DigitalTwin][twin] · [Behaviour][behaviour] · [A4.7 source and deferred details][twin-note] · [G03 PF-01][g03]

### 4.5 Assurance processing

`Keleusma` is the originating assurance request. `Dokime` is its realised undertaking. **Tekmerion** holds its governed operational history. Each **Dossier** records an individual assurance activity. The Dokimasia processing framework coordinates assurance work under the applicable Praxis. Dokimasia loads authorised evidence through the governed access boundary, evaluates the applicable criteria and generates attributable outcomes. Criteria may be composite. The applicable basis must identify the subject and relevant criteria and revisions, the evidence used, and how the resulting findings relate to that basis. Detailed coordination and activity semantics remain open. The assurance Ergon chooses its internal evaluation algorithm subject to the approved criteria, evidence and reporting contract.

The Dokime TaskOutcome records the adjudicated disposition of the assurance undertaking. A governed assurance finding produced by that work retains its own meaning and can be carried as an output InformationUnit under its owning contract. A successfully performed evaluation can yield an insufficient-evidence finding. The subject activity owns its own work and outcome and cannot determine its independent assurance. Preserve the finite assurance boundary and the existing self-audit rule for a Dokimasia execution, together with applicable security and evidence obligations. [Independent Assurance acceptance][assurance-acceptance] · [Dokimasia][dokimasia] · [Finite boundary][finite] · [Self-audit boundary][self-audit]

### 4.6 Execution and persistence boundaries

| Concern | Architectural responsibility |
| --- | --- |
| Governed undertaking information | Mneme constructs and manages the six work forms and their version-aware information references. Mnemosyne establishes required durable state via the governed interaction. |
| Execution coordination | Ponos and the applicable specialised processing frameworks or engines coordinate execution and apply Praxis-defined progression and adjudication rules. |
| InformationUnit outputs | Ergon processing may produce candidate IUs. Governed validation and establishment, applicable release rules and accepted-result attribution determine what can appear in a realised undertaking's output. Returning changes to existing governed information follows the applicable Mneme save contract. |
| Framework-observed execution information | The applicable framework observes execution and records attributable facts in the operational and individual activity records. The general family uses `JobCard.ExecutionAttempt` for its execution state, metrics and failures. |
| Developer-reported framework details | The processing boundary conveys developer-reported details alongside produced IUs, with attribution to the activity. The general family records these in `JobCard.WorkspaceMinutiae`. Context unloading, checkpointing, recovery and applicable audit use their owning contracts. |
| Adjudicated TaskOutcome | The framework applies Praxis-defined rules to adjudicate the stable outcome. The operational record retains the reference. The persisted decision identifies the accepted result and associated attribution under §4.3. |
| Praxis-defined policies | Energeia manages the Praxis definition and applicable criteria. Ponos governs interpretation for execution and progression under the existing responsibilities. Specialised policies retain their own Twin and independent assurance constraints. |
| Ergon-specific processing | `Ergon.process()` and its specialised equivalents determine internal business or evaluation logic. The framework governs the exchanged outputs, relevant details and their attribution. |

Mneme/Mnemosyne provide governed access, persistence, recovery and provenance for the undertaking, operational record and stable TaskOutcome. Their acceptance-time identities and reference require authoritative establishment under §4.3. Determine persistence and retention for associated detail from performance, recovery, durability, security and evidence requirements. Local workspace values and incidental diagnostics follow AX-09's ephemeral default. Promote meaningful decision and evidence facts into the applicable governed representations. Access and save preserve Mneme/Mnemosyne authority, revision and acknowledgment semantics. [AX-09][ax09] · [Two-cache decision][cache] · [Lifecycle and operational state][lifecycle] · [Accepted IU development correspondence][bl5]

### 4.7 Subscription realisation and routing reconciliation

Petasos evaluates applicable subscriptions for an originating Pragma and returns a **set of distinct subscribing Ergons**. It evaluates subscriptions for each Ergon until the first successful match, then skips all remaining subscriptions for that Ergon. Match success is governed by the applicable selection and Themis contracts. The decision records the applicable matching basis, including the successful definition and revision for each selected Ergon and its intended destination. The ordering, destination equivalence and treatment of denied or indeterminate evaluations remain for review. [Publication and subscription §4.2][publication-selection] · [Routing Final.001 §3][routing-subscription]

One originating Pragma produces at most one `Pragmatosis` for each selected Ergon. Petasos requests establishment and Mneme constructs or recovers the governed undertaking. The uniqueness scope is the pair **(originating Pragma identity, subscribing Ergon identity)**. Petasos establishes **one routing obligation per resulting undertaking**. Before fulfilment, it durably captures the matching decision and the complete set of obligations for the selected Ergons, including an established empty set. The obligation retains correspondence to the selection and acquires the undertaking reference as identity is established or recovered before handoff. A later subscription definition for the same selected Ergon supplies no further routing obligation. [Routing Final.001 common strategy][routing] · [Minimum routing evidence][routing-evidence]

Recovery uses the durable decision, original matching and provenance basis, undertaking identity, intended destination, and separately attributable work-establishment and delivery evidence for each obligation. A lost construction reply is reconciled through Mneme. A lost or indeterminate delivery reply is reconciled with Petasos evidence at the owning boundary. A recorded decision is recovered using its original matching basis. Current subscription definitions do not replace that basis. H10 originating-work establishment, H11 durable complete-list acceptance, matching, handoff and execution admission are distinct. The direct Praxis-Router retains its separate correspondence questions. [Routing Final.001 §§5–6][routing-recovery] · [PF-08 disposition][routing-pf08]

**Example for review.** One originating Pragma has subscriptions A and B for Ergon X and C for Ergon Y. If A is X's first successful match, Petasos skips B. If C also matches, the decision records two Ergons and two obligations, one for each resulting Pragmatosis. If delivery to X is indeterminate and delivery to Y is confirmed, recovery reconciles X's obligation while retaining Y's confirmed progress.

## 5. Detailed design delegated to the implementing team

Detailed design selects model interfaces, persistence schema and coordination mechanism within the reviewed contracts. It also specifies provisional output staging, contention handling, attempt observation, composite criteria evaluation and internal processing algorithms. The architectural review establishes the observable semantics and owning boundaries first.

## 6. Evidence and rationale

The selected Ergon, undertaking and routing obligation retain sufficient correspondence to recover a handoff without duplicating work. The stable operational record and TaskOutcome identities support recovery and let each activity contribute attributable facts under Praxis. The framework retains coordination authority. Each record preserves the sources and meaning of its observations and developer reports, while the separately governed outcome retains the adjudicated result. Reuse of Mneme/Mnemosyne, the applicable execution frameworks and Petasos keeps each effect and its evidence at the owning boundary. [Task Topic][task-topic] · [Routing reconciliation][routing-recovery] · [Processing Framework][processing]

The matrix below distinguishes recorded Solution Architect dispositions and canonical element standing from candidate correspondence and historical discovery. The repository's source succession governs use of wrapped legacy sections. [Architecture Governance][governance] · [A4.7 source succession][twin-note]

## 7. Reconciliation matrix

### 7.1 Existing Task architecture correspondence

The Solution Architect established the following design correspondence in Draft.000.002. [Predecessor decision provenance][predecessor-decisions]

| Existing | New correspondence |
| --- | --- |
| ActionableTask | Pragma |
| FulfillmentTask | Pragmatosis |
| TaskOutcome | Accepted Undertaking Outcome |
| OutcomeConcurrency | Distinct Execution and Outcome Capture Rule |
| OutputRules | Distinct Outcome Adjudication and Qualification Rules |

The existing TaskOutcome Topic describes resolution of the originating `ActionableTask`. The G03 candidates use the term for the attributable result of one Ergon execution. The accepted undertaking outcome in the table is the adjudicated disposition of the separately governed `TaskOutcome` in this draft. Reconciliation must preserve the existing meanings in their own sources, adjudicate the representation of any originating-work disposition, and map the prior participation and output rules without carrying their policy modes over by implication. The new rules keep the capture of actual executions and their results distinct from adjudication and qualification. [TaskDefinition][task-definition] · [Task and its controls][task-topic] · [TaskFulfillment][task-fulfillment] · [TaskOutcome][task-outcome] · [G03 PF-03][g03]

### 7.2 Affected provisions and required follow-up

| Source and exact provision | Recorded standing | Reconciliation for Solution Architect review |
| --- | --- | --- |
| **Undertaking and Execution Framework Draft.000.002** §§4.3, 4.4 and 4.6. [JobCard and lifecycle][predecessor-jobcard] · [ThoughtProcess][predecessor-twin] · [Execution boundary][predecessor-boundary] | Prior proposal preserved as review history. | The prior draft grouped JobCard with execution coordination, explicitly assigned coordination of Skepsis to ThoughtProcess and split JobCard creation at receipt from TaskOutcome creation after context loading. Apply §§4.3–4.6 of this revision. The frameworks coordinate execution. JobCard, ThoughtProcess and Tekmerion retain the operational history and outcome reference, with atomic pair creation on receiving Ergon acceptance for all three families. |
| **Petasos Routing and Reconciliation Final.001** §1 opening, strategy steps 2–6 and construction row. §3 `SubscriberSet` loop, `SubscriptionIdEvent` and the explicit obligation per matching subscription. §§5–6 work establishment, replay, obligation/work correspondence and queue handoff. §7.1 exact identities and §8 same-subscriber conformance. [§1][routing] · [§3][routing-subscription] · [§5][routing-recovery] · [§6][routing-evidence] · [§7.1][routing-pf08] · [§8][routing-tests] | Offline design accepted with qualifications under G03 / DR-12. [Disposition][routing-disposition] | **Direct conflict.** Reconcile the accepted per-subscription construction and obligation provisions with first successful match per Ergon, distinct-Ergon results and one obligation per undertaking. Preserve H10/H11, the PF-08 durable decision and recovery strategy, and attributable matching, work establishment and handoff evidence. |
| **Pragma Publication and Subscription Draft.000.003** §2, PS-08–PS-12, §§5–7. [PS-08–PS-12][publication-selection] · [§5][publication-reliability] | Pending G03 candidate carrying issued allocations. [DR-12][g03-review] | Apply first-match selection and one realisation and obligation per Ergon for a given Pragma. Retain the successful subscription and decision basis as provenance. Preserve producer eligibility, zero matches, and separate handoff and execution confirmation. |
| **Interaction Petasos–Mneme Draft.000.004** §§1.2, 4.1–4.4 and 8, especially step 2 in §4.2 and the multiple matching subscriptions row in §4.4. [§1.2][petasos-mneme-req] · [§4.2][petasos-mneme-identity] · [§4.4][petasos-mneme-retry] · [§8][petasos-mneme-tests] | Pending G03 companion. | Align construction and tests to distinct Ergons and recover one undertaking and one routing obligation per pair. Map governed lookup, uniqueness, acknowledgement and lost-response recovery. |
| **Mneme Pragma Model Draft.000.004** §§1.2, 4.1–4.2, 4.4–4.5, 4.8 and 8–9. [§4.1][pragma-model] · [§4.4][pragma-progression] · [§4.8][pragma-outcomes] | Pending G03 candidate. | Move `output()` and `outcome()` to the realised role. Align per-match construction and conformance with the distinct-Ergon selection. Reconcile inherited input and `whereFrom()`, separate origin provenance, activity results and the realised TaskOutcome lifecycle, Topic scope and specialised names. |
| **Mneme Pragmaton Model Draft.000.004** §§3.1, 3.7 and 3.9–3.13, and **Ergon–Mneme Pragmaton Interaction Draft.000.004** §§4.1–4.7. [Pragmaton][pragmaton-model] · [Ergon–Mneme][ergon-mneme] | Pending G03 candidates. The underlying two-cache allocation is accepted with qualifications. [Cache decision][cache] | Map Pragmaton/WorkSpace, attempt-specific context and governed IU references across all three realised forms. Reconcile `WorkspaceMinutiae` and save/unload with the accepted processor-status and generated-output boundaries. Determine the formal association, lifetime and recovery needs. |
| **Harmonia Processing Framework and Checkpoints Final.001** §§4.2–4.6 and 5, especially H01–H05 and H07. [Stages and checkpoints][processing] · [WorkSpace transfer and status][processing-workspace] · [Checkpoint contract][checkpoints] | Offline design accepted with qualifications. [Disposition][processing-disposition] | Locate receiving Ergon acceptance relative to H01/H02 and define evidence of atomic record and TaskOutcome establishment. H03 retains the context-loading boundary. Reconcile H05's whole Egress Pragma transfer and H07's attributable Ergon-instance result with the realised undertaking contract and separate Praxis adjudication. Preserve the transfer, H10's distinct originating work, the status domains and Kleio capture. |
| **TaskDefinition, Task, TaskFulfillment and TaskOutcome** Topics. [TaskDefinition][task-definition] · [Task][task-topic] · [TaskFulfillment][task-fulfillment] · [TaskOutcome][task-outcome] | Bounded adjudicated conceptual meanings, with runtime and mapping reservations. | Apply §7.1 as the review correspondence. Reconcile existing candidate and canonical meanings of TaskOutcome, originating-work disposition, and existing concurrency, participation and output-composition policy semantics. |
| **Pragma Management and Topic, Petasos and PetasosParcel**, plus **PetasosParcel and PetasosParcelMask Final.001** §§4 and 6, specifically Mneme subscription selection, same-context mask composition and delivery/retry/recovery allocation. [Management][pragma-management] · [Pragma][canonical-pragma] · [Petasos][canonical-petasos] · [Parcel Topic][canonical-parcel] · [Parcel design §4][parcel-final] · [Parcel design §6][parcel-register] | Qualified Domain05 canonical descriptions and agreed bounded offline Parcel/Mask concepts. | Reconcile earlier Mneme selection, originating-work parcel referent and receiving-side establishment with Petasos matching and Mneme establishment. Keep per-definition evaluation criteria, same-context mask composition, by-value conveyance, other parcel purposes and PF-06 / DR-10 selector qualifications. Reconcile Pragma-stage filtering with the G03 producer IU gate and the qualified Ponos/Praxis continuation description. |
| **DigitalTwin, Behaviour** and **Digital Twin Behaviour and Execution Semantics** §§2, 4–11 and 13. [DigitalTwin][twin] · [Behaviour][behaviour] · [A4.7 DesignNote][twin-note] | Adjudicated Domain05 Step-B candidate with final architecture unapproved. Incorporated excerpts use the canonical elements and remaining legacy material retains its recorded standing. | Map Stimulus/Noesis and the ThoughtProcess/Skepsis records to governed interest, Behaviour-specific work, Twin-relative identity and fresh context. Reconcile specialised ingress and the common acceptance invariant with the per-Twin queue and sequential Behaviours. Preserve Effector and failure/recovery qualifications. Determine Twin-specific activity scope and contribution rules under its framework. |
| **Dokimasia**, **Provenance and Assurance** DR-18/19, and **Energeia, DigitalTwin and Dokimasia Specialisation** §§8–10. [Component][dokimasia] · [Accepted DR-18/19][assurance-acceptance] · [Candidate specialisation note][dokimasia-note] | Accepted independent assurance and provenance responsibilities with detailed obligations. The Domain05 component and historical specialisation descriptions retain their qualifications. | Map Keleusma/Dokime and the Tekmerion/Dossier records to the assurance execution and evidence contracts. Decide specialised ingress, `Dokimasia.Praxis`, composite criteria and finding/output linkage while preserving evidence, independence and finite self-audit boundaries. |
| **Mneme–Mnemosyne Interaction and two-cache acceptance**, **InformationUnit acceptance**, **Calliope Lifecycle, Revision and Operational State Final.001.000** §§1, 3–4, and the **BL-0005** accepted increment. [Interaction][persist] · [IU revisions][iu-design] · [Lifecycle][lifecycle] · [BL-0005][bl5] | Accepted Architecture contracts and qualified offline lifecycle design. BL-0005 is an accepted development increment with specified IU design supersessions and separate incorporation obligations. | Establish correspondence for atomic operational-record and TaskOutcome creation, stable reference, authoritative proof and recovery. Bind the three families to applicable authority, revision, provenance, retention and acknowledgment. Determine durable activity facts and transient detail while preserving distinct meanings for IU changes, TaskOutcome and activity and transport state. |
| **Topic and TopicMask Final.003** §§1–3 and pending Architecture-target correspondence. [Topic design][topic-design] · [Architecture review][topic-review] | Accepted offline Topic design with separately governed incorporation. | Map and version the Tasks concept scope in one decision. Determine coexistence or migration for affected selectors and references to the existing Task-family concepts. |

## 8. Conformance expectations

- Verify independent work identities, unchanged inherited `whereFrom()` and immutable input identity and revision, and a separate origin provenance relationship for all three pairs. Verify realised outputs and outcome access, and the absence of result operations on each originating form.
- Verify the six ordered Topic paths share the `Tasks` prefix with version only on Scope once reviewed. Check version-aware round trips and selector behaviour using established Topic semantics.
- For each family, exercise acceptance, interruption during creation and a lost acceptance reply. Verify that confirmed acceptance establishes exactly one operational record with one stable reference to the unresolved TaskOutcome, that partial state cannot appear as accepted, and that retries, redelivery and any permitted concurrent execution recover the same pair.
- Verify context-load and processing failures appear in the operational history and preserve the outcome's identity. Exercise explicit Praxis adjudication on exhaustion, including where every permitted attempt fails in loading, and verify the atomic adjudicated result, accepted output and attribution under recovery.
- Exercise concurrent and retried general executions. Verify separate attempt identities and framework observations, and preserve each developer-reported `WorkspaceMinutiae` observation against competing writes. For each family verify that produced IUs, framework observations, developer reports and adjudicated results are distinguishable.
- Exercise earlier non-matches, the first successful match for an Ergon, skipped later definitions, multiple distinct Ergons and a proved empty result. Verify exactly one routing obligation per resulting undertaking and independent recovery across different Ergons after lost construction or delivery replies. Verify the durable matching basis, lineage and separate proof of execution eligibility.
- Verify Twin sequencing and fresh context. Derive Skepsis and Dossier activity checks from their own reviewed domain contracts. For assurance, verify composite criteria under the reviewed rules, insufficient evidence, independent outcomes and the finite evidence boundary.
- Verify authorised governed access and audit/evidence for meaningful effects. Verify that cache loss cannot supply authoritative version or accepted-result proof and that the history required for recovery remains available under the applicable durability contract.

## 9. Open questions for Solution Architect review

1. **Routing correspondence.** Review the amendments identified in §7.2. Define subscription evaluation order, selection when definitions specify differing destinations or an earlier evaluation is denied or indeterminate, and stable Ergon and obligation identity across registration changes and authorised replay. Preserve PF-07/PF-08 and DR-7/DR-10 evidence and security obligations.
2. **Task architecture and adjudication.** Apply the §7.1 correspondence in governed review. Resolve originating-work disposition, prior Task policy mapping, qualification and exhaustion criteria by domain, and treatment of provisional outputs and effects.
3. **Acceptance and recovery.** Locate receiving Ergon acceptance relative to H01 receipt, H02 admission, transport acknowledgement and the specialised ingress contracts. Define authoritative establishment and recovery of the atomic pair and its stable reference through the existing Mneme–Mnemosyne path, including evidence after an uncertain result and retention of activity histories.
4. **Classification and provenance.** Establish the Tasks version and selector compatibility, and the representation and evidence required for the inherited source and separate origin relationship.
5. **Framework handovers.** Reconcile H05, H07, `Workspace.Processor.Status`, Kleio closure capture and adjudication-driven publication or continuation. Confirm the boundary and evidence for accepted outputs.
6. **Twin processing.** Resolve Noesis/BehaviourStimulus correspondence, Effector treatment, ThoughtProcess/Skepsis record semantics and contribution, coordination and recovery within existing Twin serialisation.
7. **Assurance processing.** Resolve Tekmerion/Dossier record and activity semantics, Dokimasia coordination and Praxis derivation, composite criteria, evidence access and the finding's relationship to Dokime and TaskOutcome.
8. **Governed review placement.** Establish the HCRL target and iteration for the draft and decision correspondence. Existing G03 / DR-12 provides related review history. [Review Praxis][review-praxis] · [Change Provenance][change-provenance]

## 10. Proposed implementation backlog decomposition

Each row is a proposed work package with an independent verification boundary. Later item IDs and revisions belong to separately prepared Backlog Items. The dependencies below refer to reviewed contracts or prior deliverables. Implementation requires a separate assignment. [Software Development Praxis][software-praxis] · [Backlog Item template][backlog-template]

| Proposed item | Bounded outcome and dependency | Acceptance evidence |
| --- | --- | --- |
| **BL-0006 Calliope Pragma** | Establish the pure Calliope originating/realised information contract, starting with Pragma and Pragmatosis, and the shared result and Tasks classification rules. Reuse BL-0005's IU model and apply the reviewed representation and Topic version contracts. | Model-level fixtures verify independent identity, immutable input and unchanged `whereFrom()`, separate origin provenance, origin/result separation, zero or multiple outputs, and Topic scope semantics. |
| Mneme undertaking establishment and access | Depend on BL-0006 and the reviewed identity/persistence contract. Establish or recover one authoritative general undertaking per eligible origin/Ergon pair and provide governed access to it. | Concurrent callers and a lost creation reply resolve the same authorised identity. Cache loss and denial exercise authoritative recovery and access boundaries. |
| Petasos subscription reconciliation | Depend on Mneme establishment and the reviewed routing amendment. Select distinct Ergons via first successful match and establish one obligation per resulting undertaking with durable decision capture and bounded retry. | Later definitions for an already-matched Ergon are skipped. Recovery preserves the original decision and independent progress across distinct Ergons after interrupted construction or delivery. |
| General execution coordination and acceptance | Depend on governed undertaking access and reviewed acceptance, checkpoint and Praxis contracts. Establish JobCard and its unresolved TaskOutcome atomically at receiving Ergon acceptance. Keep general attempt observations and developer reports separately attributable and adjudicate the outcome under Praxis. Verify against controlled routing inputs. | Lost acceptance replies and permitted retries recover the same pair, competing attempts preserve attribution, and exhausted permitted executions result in explicit adjudication. |
| Ergon processing and result handover | Depend on the reviewed processing correspondence and acceptance contract. Establish the processing-detail and output boundary through Context Unloader, validation, outcome decision and egress. | Exercise working-value save, provisional and accepted generated outputs, details and audit capture, and preservation of H05/H07 and H10/H11 evidence boundaries. |
| Digital Twin undertaking and consideration coordination | Depend on the common model and reviewed Twin contracts. Establish ThoughtProcess and TaskOutcome with the common acceptance guarantee and record Skepsis under the Twin processing framework. | Verify Twin-relative lineage, stable sequencing, fresh context, same-Twin queuing, governed consideration contribution and authorised recovery with stable record and outcome identities. |
| Dokimasia undertaking and evidence evaluation | Depend on the common model and reviewed assurance contracts. Establish Tekmerion and TaskOutcome with the common acceptance guarantee and record Dossier activities under the Dokimasia processing framework. | Verify stable record and outcome identities, attribution to criteria and evidence revisions, independent finding and execution outcomes, insufficient evidence and the finite assurance boundary. |

## 11. Review decision

**Pending Solution Architect review.** The directions issued for Draft.000.003 are incorporated here. The affected architectural provisions and the remaining decisions in §9 require governed review. [Architecture Review Praxis][review-praxis]

## 12. Post-decision actions

After disposition, preserve the exact reviewed draft and decision provenance in the applicable HCRL and reconcile only the authorised provisions. Establish canonical identity, meaning and relationship correspondence before any Continuum update. Governed application and implementation follow their respective assignments. [Change Provenance][change-provenance] · [Software Development Praxis][software-praxis]

## Appendix A — Design provenance

The source is [Draft.000.002][predecessor] (SHA-256 `b31614af5ae213cdd651a9ccf448799ad912960304844b312db4ed0ab96cb4cc`). The operative Solution Architect instruction is **Codex — Undertaking and Execution Framework, Draft.000.003**, issued in the review interaction for `feature/BL-0006-Calliope-Pragma`. Its §5 carries forward the Draft.000.002 subscription decisions. The review uses Architecture 1.1.26 and the development line following the accepted BL-0005 increment. The cumulative G03 review records the standing of the candidate family and the qualified acceptance of the offline Routing and Processing DRPs. [Prior decision provenance][predecessor-decisions] · [Architecture baseline][architecture-baseline] · [BL-0005 acceptance][bl5] · [G03 review][g03-review]

<a id="changes-from-draft-000-002"></a>

### Changes from Draft.000.002

| Solution Architect instruction | Incorporated change |
| --- | --- |
| §1 Common undertaking and operational-record model | §4.1 names all three operational and individual activity record pairs, including Tekmerion/Dossier. Java structure remains a detailed-design responsibility. |
| §2 Operational records versus coordination | §§4.3–4.6 give JobCard, ThoughtProcess and Tekmerion the history and reference roles. The applicable framework or engine coordinates work under Praxis. |
| §3 Common TaskOutcome lifecycle | §4.3 replaces split receipt and context-load creation with atomic pair creation at Ergon acceptance. Each family begins with an unresolved TaskOutcome and stable reference. Failures remain attributable and exhausted execution requires explicit adjudication. |
| §4 Processing boundaries | §4.6 distinguishes produced IUs, framework observations, developer reports, adjudicated outcomes and internal Ergon logic. |
| §5 Reconciliation | §§7–9 identify the earlier coordination and creation provisions, acceptance and persistence correspondence, and updated review questions and conformance. Section 4.7 carries forward the first-match and single-obligation subscription decisions. |

[predecessor]: DRP-Harmonia-Undertaking-and-Execution-Framework.Draft.000.002.md
[predecessor-decisions]: DRP-Harmonia-Undertaking-and-Execution-Framework.Draft.000.002.md#changes-from-draft-000-001
[predecessor-jobcard]: DRP-Harmonia-Undertaking-and-Execution-Framework.Draft.000.002.md#43-jobcard-and-execution-coordination
[predecessor-twin]: DRP-Harmonia-Undertaking-and-Execution-Framework.Draft.000.002.md#44-digital-twin-processing
[predecessor-boundary]: DRP-Harmonia-Undertaking-and-Execution-Framework.Draft.000.002.md#46-execution-and-persistence-boundaries
[decisions]: #changes-from-draft-000-002

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
[routing-pf08]: DRP-Petasos-Routing-and-Reconciliation.Final.001.md#71-pf-08-qualified-disposition
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
[parcel-final]: DRP-PetasosParcel-and-PetasosParcelMask.Final.001.md#4-subscription-and-publication-relationship
[parcel-register]: DRP-PetasosParcel-and-PetasosParcelMask.Final.001.md#6-reconciliation-register
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
