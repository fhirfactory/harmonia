<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Design Review Proposal — Harmonia Undertaking and Execution Framework

| Review field | Value |
| --- | --- |
| Revision | Draft.000.005 |
| Standing | Pending Solution Architect review |
| Prepared by | Codex |
| Solution Architect | Mark Hunter |
| Architecture baseline | 1.1.26 |
| Software development identity | `0.1.0-SNAPSHOT` |
| Source | [Draft.000.004][predecessor] in the `feature/BL-0006-Calliope-Pragma` working tree based on `590d268af553dac40455aa63815d141a4352dcf2` |

## 1. Feature realisation

The framework gives general, Digital Twin and assurance processing common semantics for originating work, realising a particular undertaking, obtaining governed context and recording attributable results. It reuses the accepted **EligibleTaskPublication**, **SubscriptionMatchCorrect**, **RecoveryPreservesTaskLineage**, **SingleSchedulingAuthority** and **Assurance Evidence Correlation** Features. Those Features support the work across publication, routing, execution, recovery and independent assurance. Placement of a shared execution-coordination Feature, if needed after mapping existing Features, is a review matter. [Publication][publication-feature] · [Subscription matching][matching-feature] · [Recovery lineage][lineage] · [Scheduling][scheduler] · [Assurance evidence][assurance-feature]

CORE-004 and PROV-001 support correlation and causation through origin, undertaking, execution and handoff. REQ-FND-005 governs independent assurance. The design preserves immutable input and origin lineage, attributes activity to the applicable operational record, and provides recovery of the single subscription-routing obligation per resulting Pragmatosis. Section 8 gives observable conformance checks. [CORE-004][core004] · [PROV-001][prov001] · [REQ-FND-005][assurance-req]

| Applicable axioms | Consequence for this proposal |
| --- | --- |
| AX-04, AX-18 | The applicable framework or engine coordinates execution under Praxis. Petasos owns matching and routing, and Mneme governs access to work information. The Coordinator applies the Praxis definition to the Praxiosis, which owns the pathway outcome. Reuse existing persistence and checkpoint boundaries. |
| AX-05, AX-06, AX-09 | Mneme provides active access and coordination. Mnemosyne establishes durable authority. Recover the Praxiosis operational IU and outcome through the governed information path. Governed IU revisions preserve outcome determinations. Retain independent activity information for its operational purpose and preserve facts needed for recovery and evidence. |
| AX-07, AX-08 | Apply Themis and mandatory IU governance to either eligibility rule basis. Checkpoints preserve attributable decisions and applicable Kleio capture and durable handoff, with the existing Dokimasia boundary. |
| AX-10, AX-15, AX-16 | Competing attempts, lost replies and recovery preserve known effects and uncertainty. Confirm accepted progression against the applicable authoritative record and retain the basis for intervening pathway decisions. |
| AX-14, AX-17 | Distinguish the Praxis definition, Praxiosis, its operational IU and the Coordinator. Preserve distinct execution results, undertaking and pathway outcomes, progression decisions, subscriber publication and pathway forwarding. Submit authority conflicts and unresolved mappings for explicit disposition. |

The canonical Principles define each axiom. [Architectural Axioms][axioms]

## 2. Proposal and standing

| Standing in this draft | Content |
| --- | --- |
| Solution Architect decisions for this revision | Praxiosis is one realisation of a defined Praxis. Each Praxiosis owns its pathway TaskOutcome and has one governed operational IU. The pair is established atomically and retains stable identities and association. Participant outcomes are linked through governed references. The operational IU's name remains open. [Decision provenance][decisions] |
| Design direction carried forward | The six undertaking classes, inherited source and immutable input, separate origin provenance, shared Tasks Topic scope, acceptance boundary, atomic adjudication of accepted result and attribution, and domain-specific activity and concurrency. Each realised undertaking owns its own TaskOutcome and has a governed operational IU with independent activity objects. Coordinator responsibilities and the separately owned publishing and Praxis forwarding rules carry forward. The subscription path still uses first successful match per Ergon and one routing obligation per resulting Pragmatosis. [Draft.000.004][predecessor] |
| Open | Detailed contracts, specialised coordination and the remaining reconciliation questions in §9. |

## 3. Design context

The G03 Pragma-family drafts distinguish originating work, undertaking and working information. They leave canonical Task-family correspondence open and use `TaskOutcome` for an actual Ergon execution. The Processing Framework and Checkpoints, accepted with qualifications, places execution, output validation, outcome decision and handoff at distinct stages. This proposal gives each realised undertaking a governed outcome, preserves individual activity attribution and relates participating outcomes to the separate outcome owned by the Praxiosis. Section 7 identifies the accepted and pending provisions that require reconciliation. [Mneme Pragma model][pragma-model] · [Pragmaton model][pragmaton-model] · [Processing Framework §§4.2–4.6][processing] · [G03 decisions][g03]

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

| Originating form | Realised undertaking | Governed operational InformationUnit | Independent individual activity |
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
| `output()` and `outcome()` | Neither operation is present. An origin can give rise to several realised undertakings, each with its own TaskOutcome. Any collective evaluation requires an explicit Praxis or workflow definition. | `output()` contains zero or more governed InformationUnits attributable to the undertaking. `outcome()` exposes the one separately governed `TaskOutcome` owned by the undertaking and created at receiving Ergon acceptance. Its initial disposition is unresolved. |

An observer must be able to distinguish an unaccepted undertaking, an accepted undertaking with an unresolved TaskOutcome, and an undertaking with an adjudicated TaskOutcome. Output attribution preserves the contributing attempt or activity and the applicable IU identity and revision. Governed references and content-free, non-authoritative summaries support access through Mneme under the applicable Themis controls. [Pragma model §§4.1–4.2, 4.6–4.8][pragma-model] · [Governed information and authority][iu-design]

**Execution state** records operational progress. An **execution result** is attributable to the individual activity and remains provisional for undertaking adjudication. The undertaking's **TaskOutcome** records its unresolved or adjudicated disposition as defined by the governing Praxis. A pathway progression decision controls what happens next, and the Praxiosis owns the overall pathway TaskOutcome described in §4.8. Originating Pragma, Stimulus and Keleusma have no consolidated TaskOutcome. Assurance findings, originating-work disposition, routing or persistence acknowledgements and IU lifecycle state keep their own meanings. An origin with zero subscribers has a selection disposition and no resulting undertaking or TaskOutcome. [Existing TaskOutcome Topic][task-outcome] · [G03 PF-03][g03] · [Processing Framework §4.5][processing-status]

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

**JobCard**, **ThoughtProcess** and **Tekmerion** are governed operational InformationUnits accessed through Mneme. Each records operational history, including attributable observations, details and associations appropriate to the family, and retains a stable `TaskOutcomeReference` to the separately governed outcome owned by its realised undertaking. Other governed outcome references follow the applicable domain contract. The applicable processing framework or engine coordinates the work under Praxis. [Processing Framework §§4.4–4.6][processing-workspace] · [Accepted Mneme–Mnemosyne interaction][persist]

**ExecutionAttempt**, **Skepsis** and **Dossier** are independent operational objects associated with their respective operational IUs. Their association has no containment semantics. Their operational information may be transient and retains identity and association sufficient for attribution, correlation and recovery. At processing checkpoints, the governing Praxis and execution framework determine retention, persistence, archival or disposal after applicable AuditEvents have been generated and their required durable handoff has been established. Kleio retains its authoritative AuditEvent responsibility. Activity identity and association remain sufficient for the recovery and evidence obligations that survive the checkpoint. [AX-09][ax09] · [Checkpoint contract][checkpoints] · [Kleio capture authority][kleio-capture]

#### Acceptance, recovery and adjudication

| Event | Governed effect across all three families |
| --- | --- |
| Receiving Ergon accepts the undertaking from Petasos | Atomically establish the operational IU, the realised undertaking's one separately governed `TaskOutcome` in an unresolved state and the IU's stable `TaskOutcomeReference`. Successful acceptance exposes the complete pair and reference as one governed effect. The realised undertaking retains the same pair throughout its lifetime once established. |
| Redelivery, retry or concurrent execution | Resolve the existing operational IU and TaskOutcome by governed identity. Associate each newly recorded activity with the same IU under the applicable family rules. The pair and reference keep their identities. |
| Context loading or processing fails | Record the operational failure in the operational record with the relevant activity attribution. The applicable Praxis governs adjudication of the existing TaskOutcome. |
| An activity result becomes available | Retain the attributable provisional result and any provisional outputs in their respective contracts. Praxis governs which results contribute and how they are qualified. |
| Framework adjudicates under Praxis | Establish the adjudicated disposition, accepted outputs and attribution atomically at the undertaking boundary. Record authorised changes to determinations through governed InformationUnit revisions under the same TaskOutcome identity, with previous adjudications traceable. Exhaustion of permitted execution attempts requires explicit adjudication, including an unsuccessful outcome where applicable. |

The routing path in §4.7 can establish the undertaking's identity before handoff. The exact correspondence of receiving Ergon acceptance to H01 receipt, H02 admission, Petasos acknowledgements and the specialised ingress boundaries requires review. Contextual loading after acceptance operates with the existing TaskOutcome. If context-loading failures exhaust the permitted attempts, the framework explicitly adjudicates that outcome under Praxis. [Processing Framework §§4.1–4.2][processing] · [Routing Final.001][routing-subscription]

Mneme provides governed access and recovery through Mnemosyne, which establishes authoritative durability, including the pair and its reference. Preserve provenance of the acceptance, participating activities and adjudication through the governed information path. When the effect of acceptance or adjudication is indeterminate, reconcile it through authoritative evidence before retrying or treating the effect as confirmed. The acceptance invariant defines the required visible effect. The persistence mechanism and proof of atomicity remain for review under the existing Mneme–Mnemosyne contract. [Accepted Mneme–Mnemosyne interaction][persist] · [AX-15][ax15]

#### General processing

JobCard remains a Harmonia-governed operational InformationUnit accessed through Mneme. Ponos coordinates general execution and applies the relevant Praxis, including admission and any `DoNotBefore` constraint under the applicable Themis controls. The following JobCard details apply to the general family. The Twin and assurance families define their own activity and concurrency contracts. [Processing Framework §§4.4–4.6][processing-workspace]

| JobCard information | Meaning |
| --- | --- |
| Association to `ExecutionAttempt` | An independent ExecutionAttempt identifies one execution and records framework observations such as Ergon instance identity, execution state, metrics and failures, with attributable execution results. A retry or concurrent execution associates a distinct attempt with the same JobCard. Recovering an observation of existing execution preserves its attempt identity. |
| `JobCard.WorkspaceMinutiae` | Records developer-reported framework information from `Ergon.process()`. Each observation retains attribution to its originating attempt. Competing attempts cannot overwrite one another's observations. Detailed correspondence to `Workspace.Processor.Status`, including its audit lifecycle, remains for review. |

Pragmaton remains the candidate Mneme-managed working-information environment accessed through `Ergon.WorkSpace`. The framework supplies the context for preparation, validation, recovery and close-out. [Pragmaton §§3.7–3.12][pragmaton-workspace] · [Processing Framework §§4.4–4.6][processing-workspace]

### 4.4 Digital Twin processing

`Stimulus` is originating Twin work. `Noesis` is its realised undertaking and carries the Twin-relative input lineage. **ThoughtProcess** holds its governed operational history. Each independent **Skepsis** records an individual consideration associated with ThoughtProcess. The Digital Twin processing framework coordinates considerations and applies the relevant Praxis within the existing Behaviour boundary. The contribution, adjudication and activity contracts remain open. Completion order supplies no default selection rule.

The adjudicated Domain05 Step-B constraints govern this proposal. Each Twin has one input queue for Stimulus and Effector and processes at most one input at a time. Interested Behaviours run sequentially. On the normal path, each loads fresh Twin-relative context after the applicable predecessor's governed save. Preserve the one-Ergo Praxis, same-Twin output queuing and the existing failure and authorised resumption semantics. Whether and how Skepsis may overlap within an admitted Behaviour remains open within these constraints. Adjudicate Twin fan-out, Noesis correspondence to `BehaviourStimulus`, Behaviour interest and input identity, and the separate treatment of `Effector` and `BehaviourEffector`. [DigitalTwin][twin] · [Behaviour][behaviour] · [A4.7 source and deferred details][twin-note] · [G03 PF-01][g03]

### 4.5 Assurance processing

`Keleusma` is the originating assurance request. `Dokime` is its realised undertaking. **Tekmerion** holds its governed operational history. Each independent **Dossier** records an individual assurance activity associated with Tekmerion. The Dokimasia processing framework coordinates assurance work under the applicable Praxis. Dokimasia loads authorised evidence through the governed access boundary, evaluates the applicable criteria and generates attributable outcomes. Criteria may be composite. The applicable basis must identify the subject and relevant criteria and revisions, the evidence used, and how the resulting findings relate to that basis. Detailed coordination and activity semantics remain open. The assurance Ergon chooses its internal evaluation algorithm subject to the approved criteria, evidence and reporting contract.

The Dokime TaskOutcome records the adjudicated disposition of the assurance undertaking. A governed assurance finding produced by that work retains its own meaning and can be carried as an output InformationUnit under its owning contract. A successfully performed evaluation can yield an insufficient-evidence finding. The subject activity owns its own work and outcome and cannot determine its independent assurance. Preserve the finite assurance boundary and the existing self-audit rule for a Dokimasia execution, together with applicable security and evidence obligations. [Independent Assurance acceptance][assurance-acceptance] · [Dokimasia][dokimasia] · [Finite boundary][finite] · [Self-audit boundary][self-audit]

### 4.6 Execution and persistence boundaries

| Concern | Architectural responsibility |
| --- | --- |
| Governed undertaking information | Mneme constructs and manages the six work forms and their version-aware information references. Mnemosyne establishes required durable state via the governed interaction. |
| Governed Praxiosis information | Mneme provides governed access to the Praxiosis operational IU. Mnemosyne establishes authoritative durable state for the operational IU and its corresponding pathway TaskOutcome under §4.8. |
| Execution coordination | Ponos and the applicable specialised processing frameworks or engines coordinate execution and apply Praxis-defined progression and adjudication rules. |
| InformationUnit outputs | Ergon processing may produce candidate IUs. Governed validation and establishment, applicable release rules and accepted-result attribution determine what can appear in a realised undertaking's output. Returning changes to existing governed information follows the applicable Mneme save contract. |
| Framework-observed execution information | The applicable framework observes execution and records attributable facts in the operational IU and the associated independent activity objects according to their contracts. The general family uses ExecutionAttempt for execution state, metrics and failures. |
| Developer-reported framework details | The processing boundary conveys developer-reported details alongside produced IUs, with attribution to the activity. The general family records these in `JobCard.WorkspaceMinutiae`. Context unloading, checkpointing, recovery and applicable audit use their owning contracts. |
| Adjudicated TaskOutcome | The framework applies Praxis-defined rules to adjudicate the undertaking's stable outcome. The operational IU retains the reference. Governed revisions preserve the accepted result and associated attribution under §4.3. Section 4.8 defines the distinct coordination decisions and pathway outcome. |
| Praxis-defined policies | Energeia manages the Praxis definition and applicable criteria. Ponos governs interpretation for execution and progression under the existing responsibilities. Specialised policies retain their own Twin and independent assurance constraints. |
| Ergon-specific processing | `Ergon.process()` and its specialised equivalents determine internal business or evaluation logic. The framework governs the exchanged outputs, relevant details and their attribution. |

Mneme/Mnemosyne provide governed access, persistence, recovery and provenance for the undertaking, operational IU and stable TaskOutcome. Their acceptance-time identities and reference require authoritative establishment under §4.3. Apply §4.3 to activity information at processing checkpoints. Local workspace values and incidental diagnostics follow AX-09's ephemeral default. Promote meaningful decision and evidence facts into the applicable governed representations. Access and save preserve Mneme/Mnemosyne authority, revision and acknowledgment semantics. [AX-09][ax09] · [Two-cache decision][cache] · [Lifecycle and operational state][lifecycle] · [Accepted IU development correspondence][bl5]

### 4.7 Subscription realisation and routing reconciliation

Petasos evaluates applicable subscriptions for an originating Pragma and returns a **set of distinct subscribing Ergons**. It evaluates subscriptions for each Ergon until the first successful match, then skips all remaining subscriptions for that Ergon. Match success is governed by the applicable selection and Themis contracts. The decision records the applicable matching basis, including the successful definition and revision for each selected Ergon and its intended destination. The ordering, destination equivalence and treatment of denied or indeterminate evaluations remain for review. [Publication and subscription §4.2][publication-selection] · [Routing Final.001 §3][routing-subscription]

One originating Pragma produces at most one `Pragmatosis` for each selected Ergon. Petasos requests establishment and Mneme constructs or recovers the governed undertaking. The uniqueness scope is the pair **(originating Pragma identity, subscribing Ergon identity)**. Petasos establishes **one routing obligation per resulting undertaking**. Before fulfilment, it durably captures the matching decision and the complete set of obligations for the selected Ergons, including an established empty set. The obligation retains correspondence to the selection and acquires the undertaking reference as identity is established or recovered before handoff. A later subscription definition for the same selected Ergon supplies no further routing obligation. [Routing Final.001 common strategy][routing] · [Minimum routing evidence][routing-evidence]

Recovery uses the durable decision, original matching and provenance basis, undertaking identity, intended destination, and separately attributable work-establishment and delivery evidence for each obligation. A lost construction reply is reconciled through Mneme. A lost or indeterminate delivery reply is reconciled with Petasos evidence at the owning boundary. A recorded decision is recovered using its original matching basis. Current subscription definitions do not replace that basis. H10 originating-work establishment, H11 durable complete-list acceptance, matching, handoff and execution admission are distinct. The direct Praxis-Router retains its separate correspondence questions. [Routing Final.001 §§5–6][routing-recovery] · [PF-08 disposition][routing-pf08]

**Example for review.** One originating Pragma has subscriptions A and B for Ergon X and C for Ergon Y. If A is X's first successful match, Petasos skips B. If C also matches, the decision records two Ergons and two obligations, one for each resulting Pragmatosis. If delivery to X is indeterminate and delivery to Y is confirmed, recovery reconciles X's obligation while retaining Y's confirmed progress.

### 4.8 Praxis, Praxiosis and pathway coordination

#### Definition and realisation

**Praxis** is the reusable definition of a pathway. It defines its participating intermediaries, progression rules, forwarding rules and outcome-adjudication semantics. **Praxiosis** is one individual realisation or traversal of a defined Praxis. It has its own stable identity and lifecycle, references its governing Praxis definition and applicable revision, and is associated with its participating realised undertakings and their TaskOutcomes. Each Praxiosis owns one TaskOutcome representing the overall pathway outcome as defined by its governing Praxis.

A Praxis may govern the execution of one Ergon or a pathway involving several participating Ergons. Each realised undertaking has the one TaskOutcome established under §4.3, whether it runs alone or participates in a pathway. A Praxiosis TaskOutcome is distinct from every participant's TaskOutcome. Collective evaluation requires an explicit Praxis or workflow definition. Section 9 retains the correspondence questions for single-Ergon and Pub/Sub use.

#### Governed operational information and recovery

Each Praxiosis has exactly one governed operational InformationUnit accessed through Mneme. It records pathway progression, participating undertakings, coordination decisions and operational history. Its architectural name is deliberately unresolved.

Establish the operational IU and the Praxiosis TaskOutcome atomically, applying the common lifecycle and recovery principles in §4.3. Confirm establishment only when both are authoritatively established with their relationship. After interruption or an uncertain reply, reconcile the effect through authoritative evidence before retrying. Recover an established pair through Mneme and Mnemosyne with the same identities and relationship. These remain stable through retries, recovery and authorised adjudication revisions. Governed IU revisions retain traceability of earlier determinations. The establishment checkpoint for this pair requires disposition under §9. [Accepted Mneme–Mnemosyne interaction][persist] · [Governed IU revisions][iu-design] · [AX-15][ax15]

#### Participation and TaskOutcome relationships

| Source | Relationship | Referent |
| --- | --- | --- |
| Participating undertaking's TaskOutcome | `partOf` | Praxiosis TaskOutcome |
| Praxiosis TaskOutcome | `enabledBy` | Participating undertakings' TaskOutcomes |

Establish these governed references as participating TaskOutcomes are created. They may be added incrementally as the Praxiosis progresses. Authoritative recovery preserves existing outcomes and references and prevents their duplication. The persistence and recovery contract for interrupted reference establishment requires review under §9.

#### Coordination and adjudication

`Praxis.Coordinator` is the execution function that coordinates progression and adjudication for the Praxiosis under its governing Praxis definition and revision. Praxiosis owns the pathway TaskOutcome. The Coordinator evaluates progression at the completion checkpoint of each participating Ergon. Under the governing Praxis it may continue, redirect, suspend or prematurely terminate the traversal. Early termination can satisfy the governing Praxis and constitute a successful Praxiosis outcome. Praxis can permit intermediate adjudications and authorised changes to the Praxiosis determination, with no required single final adjudication event. The individual execution result describes the activity, the progression decision governs the next step, and the Praxiosis TaskOutcome records the overall pathway determination. Each remains independently attributable.

Energeia retains management of the Praxis definition. The Coordinator denotes a responsibility of the execution framework applying that definition. Reconcile its general placement with Ponos's Praxis processing responsibility and the existing Praxis.Agent and Praxis.Server provisions. The exact allocation, including its specialised framework correspondence, requires review. [Praxis][canonical-praxis] · [Ponos][canonical-ponos] · [Processing Framework §4.6][processing-coordination]

### 4.9 Publishing and Praxis forwarding

| Rule set | Owner | Purpose |
| --- | --- | --- |
| `PublishingRules` | Ergon developer | Primary criteria for publishing eligible Ergon outputs |
| `PraxisForwardingRules` | Praxis developer | Criteria for forwarding eligible information within the defined Praxis pathway |

Either independently owned rule set may authorise eligibility for the applicable forwarding action (`Eligible = PublishingRules OR PraxisForwardingRules`). The applicable forwarding effect remains subject to Themis authorisation and mandatory InformationUnit governance. A positive eligibility decision carries its applicable action and rule basis. Subscriber publication still requires its publication and subscription-routing decisions. Forwarding along a Praxis pathway still requires its governed progression and routing decisions.

A Praxis pathway runs between defined points through known intermediaries. `PraxisForwardingRules` apply only to information belonging to that pathway. WorkSpace accessibility alone establishes no pathway membership. While coordinating a Praxiosis, `Praxis.Coordinator` may establish or dynamically adjust its effective `PraxisForwardingRules` under the governing Praxis, including in response to preceding TaskOutcomes. The Ergon developer and Praxis developer retain their respective rule ownership.

For publication to subscribers, the Petasos Subscription-Router matches and routes under §4.7. The existing qualified pathway allocation gives Ponos progression evaluation and the Petasos Praxis-Router fulfilment of the resulting routing obligations. Mneme provides governed information access and establishment for the applicable action. The relationship between the two rule sets and the PublishingMask gate, originating-work creation, list acceptance and direct Praxis selection is an explicit amendment for review in §§7.2 and 9. [Egress stages][processing] · [Subscription-Router][routing-subscription] · [Praxis-Router][routing-praxis] · [Pragma output and continuation][canonical-pragma]

Checkpoint evidence identifies the action, the applicable rule basis and any Coordinator-directed changes. Keep the rule decision, the applicable Themis determination, the IU governance outcome and routing or handoff evidence independently attributable. Preserve the distinction between a known non-match and an unestablished eligibility or forwarding effect under the applicable recovery contract. [Checkpoint evidence][checkpoints] · [AX-15][ax15]

## 5. Detailed design delegated to the implementing team

Detailed design selects Java inheritance, interfaces, composition, persistence implementation and coordination mechanisms within the reviewed contracts. It also specifies provisional output staging, contention handling, attempt observation, composite criteria evaluation and internal processing algorithms. The architectural review establishes the observable semantics and owning boundaries first.

## 6. Evidence and rationale

The selected Ergon, undertaking and routing obligation retain sufficient correspondence to recover a handoff without duplicating work. The Praxiosis identity and governing Praxis revision give progression and adjudication evidence a traversal-specific basis. Stable operational IU, activity and TaskOutcome identities preserve attribution through recovery. Governed revisions preserve previous determinations, and Praxiosis associations and outcome references identify the undertakings that participated. Rule and checkpoint evidence establishes the basis for progression or release without conflating eligibility, Themis authorisation, IU governance and delivery. Mneme/Mnemosyne, the execution frameworks, Kleio and Petasos retain their respective evidence boundaries. [Task Topic][task-topic] · [Routing reconciliation][routing-recovery] · [Processing Framework][processing]

The matrix below distinguishes recorded Solution Architect dispositions and canonical element standing from candidate correspondence and historical discovery. The repository's source succession governs use of wrapped legacy sections. [Architecture Governance][governance] · [A4.7 source succession][twin-note]

## 7. Reconciliation matrix

### 7.1 Existing Task architecture correspondence

The Solution Architect established the left-to-right review correspondence in Draft.000.002. Draft.000.005 identifies Praxiosis as the owner of the distinct pathway TaskOutcome introduced in Draft.000.004. [Earlier decision provenance][prior-task-correspondence] · [Draft.000.004 decisions][predecessor-decisions]

| Existing | Earlier design correspondence | Required amendment or disposition |
| --- | --- | --- |
| ActionableTask | Pragma | Review the originating-work role. Pragma, Stimulus and Keleusma own no consolidated TaskOutcome. Review the representation of any separate originating-work disposition. |
| FulfillmentTask | Pragmatosis | The accepted TaskFulfillment provision denies independent TaskOutcome ownership. Review amendment for the realised role, which owns exactly one outcome in each family. |
| TaskOutcome | Accepted Undertaking Outcome | The accepted Topic assigns the outcome to the ActionableTask and describes references to FulfillmentTasks. G03 also uses the name for an Ergon-instance execution result. Review the Topic amendment for two distinct owners, the realised undertaking and the Praxiosis, with stable outcome identities and governed revisions. Review canonical correspondence for `partOf` and `enabledBy` between their outcomes while preserving the existing exclusion of recursive containment. |
| OutcomeConcurrency | Distinct Execution and Outcome Capture Rule | `FirstToFinish`, `AssignedToFinish` and `Aggregate` govern FulfillmentTask participation in resolving the ActionableTask outcome. Review which existing semantics, if any, carry into explicit Praxis or workflow participation and qualification. Keep execution concurrency, actual activity capture and outcome participation distinct. |
| OutputRules | Distinct Outcome Adjudication and Qualification Rules | `Direct`, `Collection` and `Derived` govern composition of ActionableTask output. Review their applicability to undertaking or explicit collective evaluation. Preserve their orthogonality to participation, and distinguish output composition and qualification from publishing and Praxis forwarding eligibility. |

The accepted Topic meanings and pending G03 usage retain their recorded standing until adjudicated. The existing reservation of TaskOutcome runtime identity and lifecycle leaves the detailed mechanism open and supplies no authority to change the adjudicated ownership or policy modes. [TaskDefinition][task-definition] · [Task and its controls][task-topic] · [TaskFulfillment][task-fulfillment] · [TaskOutcome][task-outcome] · [OutcomeConcurrency Topic][outcome-concurrency] · [OutputRules Topic][output-rules] · [G03 PF-03][g03]

### 7.2 Affected provisions and required follow-up

| Source and exact provision | Recorded standing | Reconciliation for Solution Architect review |
| --- | --- | --- |
| **Undertaking and Execution Framework Draft.000.002** §§4.3, 4.4 and 4.6, **Draft.000.003** §§4.1, 4.3–4.6, and **Draft.000.004** §§4.3, 4.8–4.9. [Earlier JobCard and lifecycle][predecessor-jobcard] · [Earlier ThoughtProcess][predecessor-twin] · [Earlier execution boundary][predecessor-boundary] · [Immediate predecessor][predecessor] | Prior proposals preserved as review history. | Draft.000.003 moved coordination to the frameworks and established atomic pair creation on receiving Ergon acceptance. Draft.000.004 established independent activity objects and distinguished the overall pathway TaskOutcome from participant outcomes. Apply the common atomicity and recovery principles to the Praxiosis pair. Preserve the undertaking acceptance and activity boundaries. |
| **Petasos Routing and Reconciliation Final.001** §1 opening, strategy steps 2–6 and construction row. §3 `SubscriberSet` loop, `SubscriptionIdEvent` and the explicit obligation per matching subscription. §§5–6 work establishment, replay, obligation/work correspondence and queue handoff. §7.1 exact identities and §8 same-subscriber conformance. [§1][routing] · [§3][routing-subscription] · [§5][routing-recovery] · [§6][routing-evidence] · [§7.1][routing-pf08] · [§8][routing-tests] | Offline design accepted with qualifications under G03 / DR-12. [Disposition][routing-disposition] | **Direct conflict.** Reconcile the accepted per-subscription construction and obligation provisions with first successful match per Ergon, distinct-Ergon results and one obligation per undertaking. Preserve H10/H11, the PF-08 durable decision and recovery strategy, and attributable matching, work establishment and handoff evidence. |
| **Pragma Publication and Subscription Draft.000.003** §2, PS-02–PS-07 and PS-08–PS-12, §§5–7. [Output and publication][publication-output] · [PS-08–PS-12][publication-selection] · [§5][publication-reliability] | Pending G03 candidate carrying issued allocations. [DR-12][g03-review] | Reconcile the producing Ergon's PublishingMask, its `ALLOW_ALL` default and Ponos/Mneme evaluation allocation with the independently owned rule sets and action-specific eligibility. Review what a mask rejection means when the Praxis rule qualifies forwarding. Apply first-match selection and one realisation and obligation per Ergon for a given Pragma. Retain the successful subscription and decision basis as provenance, valid zero matches, and separate handoff and execution confirmation. Amend the Ergon-instance TaskOutcome terminology in §5. |
| **Interaction Petasos–Mneme Draft.000.004** §§1.2, 4.1–4.4 and 8, especially step 2 in §4.2 and the multiple matching subscriptions row in §4.4. [§1.2][petasos-mneme-req] · [§4.2][petasos-mneme-identity] · [§4.4][petasos-mneme-retry] · [§8][petasos-mneme-tests] | Pending G03 companion. | Align construction and tests to distinct Ergons and recover one undertaking and one routing obligation per pair. Map governed lookup, uniqueness, acknowledgement and lost-response recovery. |
| **Mneme Pragma Model Draft.000.004** §§1.2, 4.1–4.2, 4.4–4.5, 4.8 and 8–9. [§4.1][pragma-model] · [§4.4][pragma-progression] · [§4.8][pragma-outcomes] | Pending G03 candidate. | Move `output()` and `outcome()` to the realised role. Align per-match construction and conformance with the distinct-Ergon selection. Reconcile inherited input and `whereFrom()`, separate origin provenance, activity results and the realised TaskOutcome lifecycle, Topic scope and specialised names. |
| **Mneme Pragmaton Model Draft.000.004** §§3.1, 3.7 and 3.9–3.13, and **Ergon–Mneme Pragmaton Interaction Draft.000.004** §§4.1–4.7. [Pragmaton][pragmaton-model] · [Ergon–Mneme][ergon-mneme] | Pending G03 candidates. The underlying two-cache allocation is accepted with qualifications. [Cache decision][cache] | Map Pragmaton/WorkSpace, attempt-specific context and governed IU references across all three realised forms. Reconcile `WorkspaceMinutiae` and save/unload with the accepted processor-status and generated-output boundaries. Determine the formal association, lifetime and recovery needs. |
| **Harmonia Processing Framework and Checkpoints Final.001** §§4.2–4.6 and 5, H01–H10. [Stages and checkpoints][processing] · [WorkSpace transfer and status][processing-workspace] · [Checkpoint contract][checkpoints] | Offline design accepted with qualifications. [Disposition][processing-disposition] | Locate receiving Ergon acceptance relative to H01/H02 and define evidence of atomic undertaking pair establishment. Establish the separate correspondence for Praxiosis creation and checkpoint decisions. Preserve H03 context loading and H05 whole Egress Pragma transfer. Amend H07's Ergon-instance `TaskOutcome` label while retaining the attributable result. Amend H08's mask-only eligibility gate for the applicable forwarding action and to capture the rule basis and Coordinator changes. Preserve dead-task blocking, H09 Themis enforcement, H10's distinct originating work, the status domains and Kleio capture. Reconcile activity disposition with the distinct §4.5 `Workspace.Processor.Status` closure AuditEvent. |
| **Praxis** canonical element, including Pathway `[0..1]`, Context, fulfilment and the qualified Journey relationship. [Praxis][canonical-praxis] | Canonical element with source qualifications and retained definition-to-instance reservations. | Record the Praxis → Praxiosis definition-to-realisation relationship for defined traversals. Reconcile this scope with the distinct Pathway construct and the provision for Pub/Sub Praxis without a predefined Pathway. Establish correspondence for a single-Ergon Praxis and retain Energeia's definition management and applicable Context and assurance responsibilities. |
| **Ponos** canonical element and **Processing Framework Final.001** §§4.5–4.6. [Ponos][canonical-ponos] · [Status domains][processing-status] · [Coordination][processing-coordination] | Ponos retains its source qualifications. The offline processing design is accepted with qualifications. [Disposition][processing-disposition] | Map `Praxis.Coordinator` to the existing Ponos-owned Praxis.Agent/Server functions, including functional concurrency, intermediate adjudication and effective forwarding rule changes. Reconcile the Praxiosis operational IU with Ponos-owned `Pathway.Processing.Status` and progression observations contributing to Journey. The current provisions do not establish equivalence or a change in status ownership. |
| **Petasos Routing and Reconciliation Final.001** §§2.1–2.2, 3–4 and 7.2. [H10][routing-egress] · [Subscription-Router][routing-subscription] · [Praxis-Router][routing-praxis] · [PF-07][routing-pf07] | Offline design accepted with qualifications under G03 / DR-12. [Disposition][routing-disposition] | **Forwarding gate conflict.** H10 and H11 precede the two router decisions on the Ergon egress path, and the PublishingMask can exclude a candidate before originating Pragma creation. The Praxis-Router selects only existing originating Pragmas. Amend the gate and work/list correspondence to carry eligible Praxis forwarding on its own rule basis within the separate pathway route. Preserve the complete-list acceptance invariant, the Ponos evaluation and Petasos fulfilment split, durable decision capture and replay correspondence. Establish how §4's triggering occurrence and Praxis revision correspond to the specific Praxiosis during routing and recovery. The exact route-specific list and realised-work contract require disposition. |
| **TaskDefinition, Task, TaskFulfillment, TaskOutcome, OutcomeConcurrency and OutputRules** Topics. [TaskDefinition][task-definition] · [Task][task-topic] · [TaskFulfillment][task-fulfillment] · [TaskOutcome][task-outcome] · [OutcomeConcurrency][outcome-concurrency] · [OutputRules][output-rules] | Bounded adjudicated conceptual meanings, with runtime and mapping reservations. | **Outcome ownership conflict.** Apply §7.1 as review correspondence, amend ownership and pathway relationships by governed decision, and adjudicate the existing participation and output-composition modes separately. |
| **Pragma Management and Topic, Petasos and PetasosParcel**, plus **PetasosParcel and PetasosParcelMask Final.001** §§4 and 6, specifically Mneme subscription selection, same-context mask composition and delivery/retry/recovery allocation. [Management][pragma-management] · [Pragma][canonical-pragma] · [Petasos][canonical-petasos] · [Parcel Topic][canonical-parcel] · [Parcel design §4][parcel-final] · [Parcel design §6][parcel-register] | Qualified Domain05 canonical descriptions and agreed bounded offline Parcel/Mask concepts. | Reconcile earlier Mneme selection, originating-work parcel referent and receiving-side establishment with Petasos matching and Mneme establishment. Keep per-definition evaluation criteria, same-context mask composition, by-value conveyance, other parcel purposes and PF-06 / DR-10 selector qualifications. Reconcile Pragma-stage filtering with the G03 producer IU gate and the qualified Ponos/Praxis continuation description. |
| **DigitalTwin, Behaviour** and **Digital Twin Behaviour and Execution Semantics** §§2, 4–11 and 13. [DigitalTwin][twin] · [Behaviour][behaviour] · [A4.7 DesignNote][twin-note] | Adjudicated Domain05 Step-B candidate with final architecture unapproved. Incorporated excerpts use the canonical elements and remaining legacy material retains its recorded standing. | Map Stimulus/Noesis and the ThoughtProcess/Skepsis records to governed interest, Behaviour-specific work, Twin-relative identity and fresh context. Reconcile specialised ingress and the common acceptance invariant with the per-Twin queue and sequential Behaviours. Preserve Effector and failure/recovery qualifications. Determine Twin-specific activity scope and contribution rules under its framework. |
| **Dokimasia**, **Provenance and Assurance** DR-18/19, and **Energeia, DigitalTwin and Dokimasia Specialisation** §§8–10. [Component][dokimasia] · [Accepted DR-18/19][assurance-acceptance] · [Candidate specialisation note][dokimasia-note] | Accepted independent assurance and provenance responsibilities with detailed obligations. The Domain05 component and historical specialisation descriptions retain their qualifications. | Map Keleusma/Dokime and the Tekmerion/Dossier records to the assurance execution and evidence contracts. Decide specialised ingress, `Dokimasia.Praxis`, composite criteria and finding/output linkage while preserving evidence, independence and finite self-audit boundaries. |
| **Mneme–Mnemosyne Interaction and two-cache acceptance**, **InformationUnit acceptance**, **Calliope Lifecycle, Revision and Operational State Final.001.000** §§1, 3–4, and the **BL-0005** accepted increment. [Interaction][persist] · [IU revisions][iu-design] · [Lifecycle][lifecycle] · [BL-0005][bl5] | Accepted Architecture contracts and qualified offline lifecycle design. BL-0005 is an accepted development increment with specified IU design supersessions and separate incorporation obligations. | Establish correspondence for atomic operational-IU and TaskOutcome creation at the undertaking and Praxiosis boundaries, stable references, governed revisions, historical determinations and authoritative recovery. Resolve incremental Praxiosis participation and outcome-reference establishment and its interruption semantics. Determine durable activity facts, association and checkpoint retention after required AuditEvent handoff while preserving separate IU, activity and transport states. |
| **Topic and TopicMask Final.003** §§1–3 and pending Architecture-target correspondence. [Topic design][topic-design] · [Architecture review][topic-review] | Accepted offline Topic design with separately governed incorporation. | Map and version the Tasks concept scope in one decision. Determine coexistence or migration for affected selectors and references to the existing Task-family concepts. |

## 8. Conformance expectations

- Verify independent work identities, unchanged inherited `whereFrom()` and immutable input identity and revision, and a separate origin provenance relationship for all three pairs. Verify realised outputs and outcome access, and the absence of result operations on each originating form. Exercise several realised undertakings from one originating item and verify independent TaskOutcomes, with collective evaluation only where the Praxis or workflow explicitly defines it.
- Verify the six ordered Topic paths share the `Tasks` prefix with version only on Scope once reviewed. Check version-aware round trips and selector behaviour using established Topic semantics.
- For each family, exercise acceptance, interruption during creation and a lost acceptance reply. Verify that confirmed acceptance establishes exactly one operational record with one stable reference to the unresolved TaskOutcome, that partial state cannot appear as accepted, and that retries, redelivery and any permitted concurrent execution recover the same pair.
- Verify context-load and processing failures appear in the operational history and preserve the outcome's identity. Exercise explicit Praxis adjudication on exhaustion, including where every permitted attempt fails in loading, and verify the atomic adjudicated result, accepted output and attribution under recovery.
- For each family, verify Mneme access to the operational IU and the identity and association of each independent activity object without embedding it in that IU. Exercise concurrent and retried general executions. Preserve distinct attempt identities, failure and framework observations and each developer-reported `WorkspaceMinutiae` observation against competing writes. Verify attribution and correlation after recovery and exercise checkpoint-directed activity retention, persistence, archival and disposal as applicable. Where AuditEvents are required, verify their generation and durable handoff before disposition. Exercise failed and indeterminate handoffs under the mandatory capture contract.
- Exercise two traversals of one reusable Praxis. Verify distinct Praxiosis identities and lifecycles and the reference to the governing definition and applicable revision for each. Verify one operational IU per Praxiosis, with governed Mneme access to pathway progression, participating undertakings, coordination decisions and operational history.
- Interrupt atomic establishment of a Praxiosis operational IU and its TaskOutcome and exercise a lost establishment reply. Verify that confirmed establishment exposes the complete pair and relationship. Recover the same identities and relationship from authoritative state after retry or active-state loss. Verify that authorised redetermination creates a governed IU revision of the same outcome and keeps the pair's association and earlier adjudications traceable.
- Exercise standalone Ergon execution and a multi-Ergon traversal. Verify one TaskOutcome per undertaking and a separate outcome owned by the Praxiosis for the traversal. Verify each `partOf` and corresponding `enabledBy` when participating TaskOutcomes are created. Exercise incremental additions and recovery of interrupted reference establishment without duplicate outcomes or relationships or attribution to the wrong traversal. Exercise a single-Ergon Praxiosis where the reviewed correspondence applies.
- Verify the Coordinator evaluates progression at each participating Ergon's completion, with Praxis-governed continue, redirect, suspend and early termination decisions. Exercise early termination that satisfies the Praxis, intermediate adjudications and an authorised change. Verify governed evidence distinguishes the individual execution result, the Coordinator's progression decision and the overall determination owned by the Praxiosis, without requiring a single final adjudication event.
- For an applicable forwarding action, exercise approval by `PublishingRules` alone, `PraxisForwardingRules` alone, both rule sets and neither. Verify neither rule can override Themis denial or mandatory IU governance. Verify that a WorkSpace IU outside the defined pathway gains no eligibility from `PraxisForwardingRules`. Exercise a Coordinator adjustment based on a preceding TaskOutcome and verify checkpoint evidence of the applicable rule basis and the directed change for the applicable Praxiosis. Verify both routing paths retain independent decisions and obligations, including that Praxis-only eligibility does not confer subscriber publication.
- Exercise earlier non-matches, the first successful match for an Ergon, skipped later definitions, multiple distinct Ergons and a proved empty result. Verify exactly one routing obligation per resulting undertaking and independent recovery across different Ergons after lost construction or delivery replies. Verify the durable matching basis, lineage and separate proof of execution eligibility.
- Verify Twin sequencing and fresh context. Derive Skepsis and Dossier activity checks from their own reviewed domain contracts. For assurance, verify composite criteria under the reviewed rules, insufficient evidence, independent outcomes and the finite evidence boundary.
- Verify authorised governed access and audit/evidence for meaningful effects. Verify that cache loss cannot supply authoritative version or accepted-result proof and that the history required for recovery remains available under the applicable durability contract.

## 9. Open questions for Solution Architect review

1. **Routing correspondence.** Review the amendments identified in §7.2. Define subscription evaluation order, selection when definitions specify differing destinations or an earlier evaluation is denied or indeterminate, and stable Ergon and obligation identity across registration changes and authorised replay. Preserve PF-07/PF-08 and DR-7/DR-10 evidence and security obligations.
2. **Task architecture and adjudication.** Disposition the separate undertaking and Praxiosis TaskOutcome ownership and canonical reference amendments in §7.1. Resolve originating-work disposition, the applicability of prior OutcomeConcurrency and OutputRules modes to explicit collective evaluation, qualification and exhaustion criteria by domain, and treatment of provisional outputs and effects.
3. **Undertaking acceptance and recovery.** Locate receiving Ergon acceptance relative to H01 receipt, H02 admission, transport acknowledgement and the specialised ingress contracts. Define authoritative establishment and recovery of the undertaking's atomic pair and its stable reference through the existing Mneme–Mnemosyne path, including evidence after an uncertain result.
4. **Classification and provenance.** Establish the Tasks version and selector compatibility, and the representation and evidence required for the inherited source and separate origin relationship.
5. **Framework handovers.** Reconcile H05, the H07 Ergon-instance result, `Workspace.Processor.Status` and Kleio closure capture. Confirm the boundary and evidence for accepted outputs. Specify the minimum durable activity identity and association, applicable AuditEvents and authoritative durable-handoff evidence before checkpoint-directed disposition.
6. **Twin processing.** Resolve Noesis/BehaviourStimulus correspondence, Effector treatment, ThoughtProcess/Skepsis record semantics and contribution, coordination and recovery within existing Twin serialisation.
7. **Assurance processing.** Resolve Tekmerion/Dossier record and activity semantics, Dokimasia coordination and Praxis derivation, composite criteria, evidence access and the finding's relationship to Dokime and TaskOutcome.
8. **Governed review placement.** Establish the HCRL target and iteration for the draft and decision correspondence. Existing G03 / DR-12 provides related review history. [Review Praxis][review-praxis] · [Change Provenance][change-provenance]
9. **Praxis scope and coordination.** Reconcile the Praxiosis definition-to-realisation relationship with the distinct Pathway concept, the single-Ergon use and Pub/Sub Praxis without a predefined Pathway. Map `Praxis.Coordinator` to Praxis.Agent, Praxis.Server and the specialised frameworks. Establish the correspondence of the Praxiosis operational IU to `Pathway.Processing.Status` and Journey observations. Establish the required evidence for intermediate determinations and progression, including suspension and early successful termination.
10. **Publication and forwarding.** Disposition how PublishingMask, including the existing `ALLOW_ALL` default and Ponos/Mneme evaluation roles, corresponds to `PublishingRules` and `PraxisForwardingRules`. Define the route-specific eligibility, membership and evidence required at H08–H11 and in the separate routers. Resolve how a Praxis-eligible, publication-ineligible item reaches the existing-work Praxis route, how the complete list and receiving work are represented, and how the applicable Praxiosis, effective Coordinator changes and unsuccessful or uncertain evaluations are established and recovered.
11. **Praxiosis operational IU and establishment.** The IU's name remains unresolved for Solution Architect disposition. Establish the lifecycle boundary at which the Praxiosis pair becomes authoritative relative to the first participating undertaking, the applicable Praxis revision correspondence, and the evidence needed to recover the pair and the incrementally established `partOf` and `enabledBy` references after interrupted creation or adjudication.

## 10. Proposed implementation backlog decomposition

Each row is a proposed work package with an independent verification boundary. Later item IDs and revisions belong to separately prepared Backlog Items. The dependencies below refer to reviewed contracts or prior deliverables. Implementation requires a separate assignment. [Software Development Praxis][software-praxis] · [Backlog Item template][backlog-template]

| Proposed item | Bounded outcome and dependency | Acceptance evidence |
| --- | --- | --- |
| **BL-0006 Calliope Pragma** | Establish the pure Calliope originating/realised information contract, starting with Pragma and Pragmatosis, and the shared result and Tasks classification rules. Reuse BL-0005's IU model and apply the reviewed representation and Topic version contracts. | Model-level fixtures verify independent identity, immutable input and unchanged `whereFrom()`, separate origin provenance, origin/result separation, zero or multiple outputs, and Topic scope semantics. |
| Mneme undertaking establishment and access | Depend on BL-0006 and the reviewed identity/persistence contract. Establish or recover one authoritative general undertaking per eligible origin/Ergon pair and provide governed access to it. | Concurrent callers and a lost creation reply resolve the same authorised identity. Cache loss and denial exercise authoritative recovery and access boundaries. |
| Petasos subscription reconciliation | Depend on Mneme establishment and the reviewed routing amendment. Select distinct Ergons via first successful match and establish one obligation per resulting undertaking with durable decision capture and bounded retry. | Later definitions for an already-matched Ergon are skipped. Recovery preserves the original decision and independent progress across distinct Ergons after interrupted construction or delivery. |
| General execution coordination and acceptance | Depend on governed undertaking access and reviewed acceptance, checkpoint and Praxis contracts. Establish JobCard and its unresolved TaskOutcome atomically at receiving Ergon acceptance. Associate independent ExecutionAttempts, attribute developer reports and adjudicate the outcome under Praxis. | Lost acceptance replies and permitted retries recover the same pair, competing attempts preserve attribution, and exhausted permitted executions result in explicit adjudication. Governed revisions preserve outcome identity and earlier determinations. |
| Ergon processing and result handover | Depend on the reviewed processing correspondence and acceptance contract. Establish the processing-detail and output boundary through Context Unloader, validation, outcome decision and egress. | Exercise working-value save, provisional and accepted generated outputs, activity disposition after required audit handoff, and preservation of H05/H07 and H10/H11 evidence boundaries. |
| Digital Twin undertaking and consideration coordination | Depend on the common model and reviewed Twin contracts. Establish ThoughtProcess and TaskOutcome with the common acceptance guarantee and record Skepsis under the Twin processing framework. | Verify Twin-relative lineage, stable sequencing, fresh context, same-Twin queuing, governed consideration contribution and authorised recovery with stable record and outcome identities. |
| Dokimasia undertaking and evidence evaluation | Depend on the common model and reviewed assurance contracts. Establish Tekmerion and TaskOutcome with the common acceptance guarantee and record Dossier activities under the Dokimasia processing framework. | Verify stable record and outcome identities, attribution to criteria and evidence revisions, independent finding and execution outcomes, insufficient evidence and the finite assurance boundary. |

## 11. Review decision

**Pending Solution Architect review.** The directions issued for Draft.000.005 are incorporated in this proposal. The contract amendments in §7 and open decisions in §9 await Solution Architect disposition. [Architecture Review Praxis][review-praxis]

## 12. Post-decision actions

After disposition, preserve the exact reviewed draft and decision provenance in the applicable HCRL and reconcile only the authorised provisions. Establish canonical identity, meaning and relationship correspondence before any Continuum update. Governed application and implementation follow their respective assignments. [Change Provenance][change-provenance] · [Software Development Praxis][software-praxis]

## Appendix A — Design provenance

The source is [Draft.000.004][predecessor] (SHA-256 `6e04740d8b28e0fb28c6350edb8166716e464b0c1cb967430b6f0bac7aaf8bed`). The operative Solution Architect instruction is **Codex Assignment — Undertaking and Execution Framework, Draft.000.005**, issued in the review interaction for `feature/BL-0006-Calliope-Pragma`. Section 4.7 carries forward the subscription decisions preserved in Draft.000.004. The review uses Architecture 1.1.26 and the development line following the accepted BL-0005 increment. The cumulative G03 review records the standing of the candidate family and the qualified acceptance of the offline Routing and Processing DRPs. Formal Task/Report placement for this assignment awaits identification of the governing HCRL target and iteration. [Prior decision provenance][predecessor-decisions] · [Architecture baseline][architecture-baseline] · [BL-0005 acceptance][bl5] · [G03 review][g03-review]

<a id="changes-from-draft-000-004"></a>

### Changes from Draft.000.004

| Solution Architect instruction | Incorporated change |
| --- | --- |
| §2 Praxis and Praxiosis | §4.8 distinguishes the reusable definition, the individual traversal and the Coordinator. Praxiosis has a stable identity and lifecycle, references the applicable Praxis revision and owns the pathway TaskOutcome. |
| §3 Governed operational InformationUnit | §§4.6 and 4.8 establish one governed operational IU accessed through Mneme and created atomically with the Praxiosis TaskOutcome. Recovery preserves both identities and their relationship. The IU name is unresolved. |
| §4 TaskOutcome relationships | §4.8 preserves the directions of `partOf` and `enabledBy`, establishes them as participant outcomes are created and allows incremental additions with authoritative recovery. |
| §5 Coordination and forwarding | §§4.8–4.9 apply the existing checkpoint adjudication and authorised changes to effective forwarding rules to the Praxiosis. The Ergon and Praxis developers retain their respective rule ownership. |
| §6 Reconciliation | §§7–9 identify affected authority, conformance expectations and Praxiosis decisions still open. |

[predecessor]: DRP-Harmonia-Undertaking-and-Execution-Framework.Draft.000.004.md
[predecessor-decisions]: DRP-Harmonia-Undertaking-and-Execution-Framework.Draft.000.004.md#changes-from-draft-000-003
[prior-task-correspondence]: DRP-Harmonia-Undertaking-and-Execution-Framework.Draft.000.002.md#71-existing-task-architecture-correspondence
[predecessor-jobcard]: DRP-Harmonia-Undertaking-and-Execution-Framework.Draft.000.002.md#43-jobcard-and-execution-coordination
[predecessor-twin]: DRP-Harmonia-Undertaking-and-Execution-Framework.Draft.000.002.md#44-digital-twin-processing
[predecessor-boundary]: DRP-Harmonia-Undertaking-and-Execution-Framework.Draft.000.002.md#46-execution-and-persistence-boundaries
[decisions]: #changes-from-draft-000-004

[architecture-baseline]: ../register/1.1.26/hcrl-1.1.26-001.complete.md
[axioms]: ../../governance/architectural-axioms.md
[ax09]: ../../continuum/motivation/principle/9a5cb9e8876844e58e660f909f7df9fc.md#architectural-content
[ax15]: ../../continuum/motivation/principle/ef8abbaec37e4890ad78b9f263e67906.md#architectural-content
[kleio-capture]: ../../continuum/motivation/principle/architectural-axiom/5de2ae2c3b794c2d9f3a14efd51d564f.md#kleio-mandatory-capture-authority
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
[routing-egress]: DRP-Petasos-Routing-and-Reconciliation.Final.001.md#21-h10--originating-pragma-creation
[routing-subscription]: DRP-Petasos-Routing-and-Reconciliation.Final.001.md#3-petasossubscription-router
[routing-praxis]: DRP-Petasos-Routing-and-Reconciliation.Final.001.md#4-petasospraxis-router
[routing-recovery]: DRP-Petasos-Routing-and-Reconciliation.Final.001.md#5-failure-and-recovery-analysis
[routing-evidence]: DRP-Petasos-Routing-and-Reconciliation.Final.001.md#6-minimum-identity-and-evidence-for-reconciliation
[routing-pf08]: DRP-Petasos-Routing-and-Reconciliation.Final.001.md#71-pf-08-qualified-disposition
[routing-pf07]: DRP-Petasos-Routing-and-Reconciliation.Final.001.md#72-pf-07-disposition-and-companion-follow-up
[routing-tests]: DRP-Petasos-Routing-and-Reconciliation.Final.001.md#8-conformance-expectations
[publication-output]: DRP-Pragma-Publication-and-Subscription.draft.000.003.md#41-output-and-publication
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
[processing-coordination]: DRP-Harmonia-Processing-Framework-and-Checkpoints.Final.001.md#46-ponos-praxis-processing-coordination
[checkpoints]: DRP-Harmonia-Processing-Framework-and-Checkpoints.Final.001.md#5-checkpoint-architecture
[task-definition]: ../../continuum/application/data-object/topic/1d6d4c3a2df440be94262855fa6564eb.md#architectural-content
[task-topic]: ../../continuum/application/data-object/topic/c43139289cb649b4836521a96a51604c.md#architectural-content
[task-fulfillment]: ../../continuum/application/data-object/topic/30f4deffcd694f07bf54b2b192695d4f.md#architectural-content
[task-outcome]: ../../continuum/application/data-object/topic/08d8ee19a30f418f9d5bc497ced0d1ef.md#architectural-content
[outcome-concurrency]: ../../continuum/application/data-object/topic/72c505782347448d8f89d74ef76359af.md#architectural-content
[output-rules]: ../../continuum/application/data-object/topic/a035fe1913db4d2589a2e4edde8db737.md#architectural-content
[canonical-praxis]: ../../continuum/application/data-object/0f961462340348b290fd844357d54d67.md#architectural-content
[canonical-ponos]: ../../continuum/application/application-component/b521762c1ea444b48cd5f66be3752d30.md#architectural-content
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
