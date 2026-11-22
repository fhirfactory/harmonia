<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Pragma Publication and Subscription

**Design Review Proposal** · Draft.000.001 · Pending Solution Architect adjudication  
**Solution Architect** · Mark Hunter  
**Basis** · Architecture 1.1.26 · Software 0.1.0 review activity in Iteration 005, recorded in cumulative HCRL.004. [Architecture release][release] · [DR-12][hcrl] · [Issued review decisions][task]

## 1. Feature realisation

The design realises **EligibleTaskPublication** and **RecoveryPreservesTaskLineage** under Digital Work, Events & Temporal Coordination / Task Publication, Subscription & Assignment, and **SubscriptionMatchCorrect** under Information Access, Discovery & Distribution / Information Distribution & Notification. **Duplicate Delivery Recognition** and **Assurance Evidence Correlation** support recovery and review. These are existing Features. The proposal uses the existing work-progression, information-distribution and assurance responsibilities and leaves their Business and Business-Enabling allocation to the governed mappings. [Publication][publication-feature] · [Lineage][lineage] · [Matching][matching-feature] · [Duplicate delivery][duplicates] · [Assurance correlation][assurance-feature]

Existing requirements include CORE-004 for correlation and causation, PROV-001 for end-to-end provenance, and REQ-FND-005 for independent assurance. Section 5 states the publication-specific reliability requirements exposed by this review. [CORE-004][core004] · [PROV-001][prov001] · [REQ-FND-005][assurance-requirement]

The proposal applies AX-04 to keep eligibility, selection and execution semantics under Harmonia control. AX-05, AX-06 and AX-09 preserve the boundaries between durable information, transient processing state and snapshots. AX-07 governs selection-related access, forwarding and execution. AX-08 and AX-16 require evidence of decisions and coordinated progression. AX-10, AX-11 and AX-15 require recoverable handoff, bounded information access and explicit uncertain outcomes. AX-02 and AX-13 govern external interoperability. AX-14 distinguishes work, information, transport and processing states. AX-17 keeps unincorporated design differences open for adjudication, and AX-18 preserves the authority of the information and domain owners. [Canonical Principles][axioms]

## 2. Proposal and standing

Apply one Pragma-based publication and subscription framework. Evaluate producer eligibility on generated information before creating downstream work. Mneme selects subscriptions using the eligible work and its input attributes, establishes subscriber-specific undertakings and hands references to Petasos. Ergon executes each undertaking in a governed processing environment. Section 4 transcribes the Solution Architect's design decisions for this draft. Adoption into the accepted architecture requires adjudication of this exact proposal and its reconciliation obligations. [Issued review decisions][task] · [Reconciliation evidence][reconciliation]

## 3. Design context

- **Accepted authority.** The Continuum Principles, existing Features, and accepted cache, persistence and IU revision contracts govern this draft. Mneme owns active information access and coordination, Mnemosyne establishes durable state, and the existing two-cache design separates lightweight summary access from transient materialised working data. The accepted Ergon authorisation DRP records the G01 design disposition, with corresponding application and verification obligations. [Two-cache acceptance][cache-register] · [Persistence interaction][persistence] · [IU framework acceptance][iu-register] · [G01 authorisation][ergon-auth]
- **Qualified Continuum baseline.** The Mneme Pragma Management and PragmaFactory elements record adjudicated candidate allocations. The PetasosParcel Topic includes bounded adjudicated handoff semantics under the Domain05 Step-B baseline. That baseline gives Mneme semantic selection, Petasos durable transport and Ponos execution. Its existing handoff sequence and work vocabulary need the explicit reconciliation in §6. [Pragma Management][pragma-management] · [PragmaFactory][factory] · [PetasosParcel Topic][parcel]
- **Review inputs.** The Pragma/Pragmaton and interaction drafts, Topic/TopicMask Draft.001.003, and the working publication/subscription DesignNote retain their recorded standing. The Parcel/ParcelMask Final records agreed conceptual direction and expressly leaves canonical identities, matching details and delivery ownership open to reconciliation. [Pragma draft][pragma-model] · [Pragmaton draft][pragmaton-model] · [Ergon–Mneme interaction draft][ergon-mneme] · [Petasos–Mneme interaction draft][petasos-mneme] · [Topic/TopicMask draft][topic-mask] · [Publication/subscription DesignNote][legacy] · [Parcel/ParcelMask Final][parcel-final]

## 4. Architectural definition

### 4.1 Output and publication

- **PS-01 — Common framework.** Pragma and its subclasses constitute Harmonia's exclusive publication/subscription work framework. Petasos continues to serve other established communication and notification purposes. [Pragma Management][pragma-management] · [PetasosParcel Topic][parcel]
- **PS-02 — Output information.** An Ergon processes a realised Pragma and generates zero or more output InformationUnits. New output InformationUnits and changes to existing governed working values have distinct information-management consequences. [Pragma model][pragma-model] · [Ergon–Mneme interaction][ergon-mneme]
- **PS-03 — PublishingMask.** Evaluate each generated InformationUnit against the applicable PublishingMask before creating its downstream Pragma. Producer-side eligibility is determined through Mneme's publication responsibility.
- **PS-04 — Filtered information.** An InformationUnit that fails the mask remains governed information. It yields no downstream Pragma and is excluded from ordinary subscription matching. `FILTERED_OUT` is an illustrative publication disposition. Its representation and ownership within the existing operational-state model require adjudication. [Lifecycle and operational-state design][lifecycle]
- **PS-05 — Normal disposition.** Filtering is a normal publication outcome. Preserve the distinction between a known mask rejection, a failed or indeterminate evaluation, an authorisation denial, and an Ergon processing failure.
- **PS-06 — Evidence.** Record the publication decision as appropriate to the evidence purpose. OA&M captures operational counts and outcomes. Kleio captures policy-governed audit events. Provenance records the causal relationship between activity, information and decision. Dokimasia uses relevant evidence for independent assessment. [Audit capture authority][audit] · [Provenance and Assurance disposition][assurance]
- **PS-07 — PragmaFactory.** For each eligible output InformationUnit, PragmaFactory creates a new archetypal Pragma with exactly one input InformationUnit. Here *archetypal Pragma* means the originating work instance selected for subscription. The reusable work definition and that individual instance remain distinct. [PragmaFactory][factory] · [Work definition][work-definition] · [Task Topic][task-topic]

### 4.2 Selection, realisation and handoff

- **PS-08 — Subscription matching.** Select on governed attributes of the Pragma and attributes of the InformationUnit represented by `Pragma.input()`. Matching can use eligible summary metadata and does not read or materialise InformationUnit Content. A TopicMask can select Topic-bearing attributes as a neutral criterion. Mneme establishes selection, and Themis governs authority to access, disclose, forward and execute. [Topic/TopicMask consumer boundaries][topic-mask] · [G01 authorisation][ergon-auth]
- **PS-09 — Subscriber-specific realisation.** For each matching subscription, Mneme's Router/DecisionEngine invokes the appropriate factory mechanism to create a distinct realisation for that subscription, subject to applicable authorised operation boundaries. Mneme records the association and determines the semantic recipient. Petasos manages the concrete destination and durable handoff. Zero matches yield zero subscriber realisations and remain distinguishable from filtering at publication.
- **PS-10 — Terminology.** A **Pragma** realises as a **Pragmatosis**. A Digital Twin **Stimulus** realises as a **Noesis**. The two left-hand terms identify the archetypal role and the two right-hand terms the undertaking role. Dokimasia-specific Pragma and realisation names remain unresolved. `ActionableTask` and `FulfillmentTask` are source terminology requiring explicit reconciliation with these roles. [Terminology reconciliation][reconciliation]
- **PS-11 — Independent identity.** Each realised undertaking has its own identity, lifecycle, outcome, subscriber association and processing context. It retains a governed relationship to the originating archetypal Pragma and uses the governed input InformationUnit reference. Multiple subscriptions can therefore progress independently while retaining common lineage. [Recovery lineage Feature][lineage]
- **PS-12 — Petasos handoff.** A by-value PetasosParcel carries the reference to the specific realisation and the metadata needed for handoff, correlation and recovery. The full InformationUnit Content stays within governed information access. Ponos governs execution and the receiver resolves work through Mneme. [PetasosParcel topic and baseline difference][parcel]
- **PS-13 — Information management.** Mneme owns Pragma management and uses Mnemosyne for authoritative persistence. Mneme's SummaryCache supports efficient governed access to Pragma and InformationUnit summary information. Its correspondence to the accepted Mneme-Standard-Cache and the allocation of Pragma summaries require review before a cache contract is changed. [Cache acceptance][cache-register] · [Persistence interaction][persistence]
- **PS-14 — InformationUnit snapshots.** Summaries carried by a Pragma or its realisation are non-authoritative snapshots. They retain the source InformationUnit identity and applicable revision correspondence. They never become independent authoritative InformationUnit versions. Authoritative status and resolution remain with the governed Mneme/Mnemosyne contracts. [IU framework acceptance][iu-register] · [Mneme Standard Cache][standard-cache]
- **PS-15 — Asymmetric governance.** Publishing governs producer-side eligibility. Subscription separately governs consumer-side selection of eligible work. Those decisions, Themis authorisation, durable handoff and processing completion each retain their own outcomes.
- **PS-16 — Assurance.** Each defined Praxis has a corresponding generated or derived `Dokimasia.Praxis` instance. Dokimasia evaluates expected and actual publication, filtering, subscription matching, handoff and processing progression. The assurance basis, observed evidence such as applicable Journey observations, and findings remain distinct. The finite Assurance boundary and the existing constraint on separate self-execution AuditEvents apply. The derivation, version correspondence and treatment of Dokimasia's own Praxis require reconciliation with those constraints. [Assurance disposition][assurance] · [Finite boundary][finite] · [Self-audit constraint][self-audit]

### 4.3 Ergon processing environment

1. The archetypal Pragma identifies publishable work and its input. Its realisation identifies the undertaking to execute. `Ergon.ContextLoader` establishes the undertaking's processing environment against its input, Praxis requirements and authorised context. It uses Mneme to acquire or resolve governed information, including any required Content, with the existing materialisation exceptions. [Ergon–Mneme draft][ergon-mneme] · [Workflow Cache][workflow-cache]
2. `Ergon.WorkSpace` is the managed processing information environment for that execution. It exposes the contextualised input and other permitted working information to `Ergon.ProcessingEngine`. The Mneme Workflow Cache supports the WorkSpace with transient local materialisations. The Pragma and realisation carry work identity and progression. The InformationUnit carries its own governed identity and revision. Mnemosyne establishes authoritative durable state. [Workflow Cache][workflow-cache] · [Two-cache acceptance][cache-register]
3. `Ergon-InformationAdapter` is the review's name for the governed interaction through which the Ergon framework acquires additional information and returns working changes or output to Mneme. Mneme mediates access, contextualisation and any required persistence through Mnemosyne. The ProcessingEngine runs the bounded Ergon behaviour. Themis governs information access, admission before processing and release or handoff after processing through the applicable enforcement points. Summary attributes, subscription interest and handoff metadata convey no authority. The adapter's exact correspondence to existing interactions remains a review matter. [Ergon–Mneme draft][ergon-mneme] · [Ad hoc information access draft][adhoc] · [G01 authorisation][ergon-auth]

**DigitalTwin-Behaviour.** Mneme loads fresh, entity-relative context for each Behaviour from its own Praxis and saves governed consequences before the next applicable Behaviour loads context. The common realisation pattern must preserve Twin-relative identity and reference resolution, serialisation, and normal queuing of same-Twin outputs. Map Noesis to the existing BehaviourStimulus/Stimulus roles explicitly before changing those contracts. [DigitalTwin][twin] · [Fresh Behaviour context][twin-context]

**Pylai ingress and egress.** An ingress loader establishes initial context from the Communication InformationUnit, with the existing authoritative handover conditions before an acknowledgement that claims durable acceptance. Egress establishes context for the applicable external representation and enforces standards-compliant, authorised, non-destructive, fail-closed projection. Internal PublishingMask eligibility and subscription selection confer no external release authority. [Pylai][pylai]

**Dokimasia.** The specialised Ergon uses common execution and governed context facilities to evaluate evidence independently. Its assurance result does not substitute for underlying observations or the assured activity's outcome. [Dokimasia][dokimasia] · [Assurance disposition][assurance]

## 5. Reliability and consistency requirements

| Requirement | Required behaviour and evidence |
| --- | --- |
| Reliable creation and duplicate prevention | Every matching subscription within the authorised operation must have a determinable creation outcome. Replaying or concurrently retrying the same publication-to-subscription obligation must establish or recover the same realisation without creating a second one. Distinct subscriptions retain distinct undertakings, including when they identify the same subscriber. Evidence must distinguish a recovered existing undertaking from a new one. |
| Persistence before acknowledged handoff | Establish the authoritative realisation and durable, resolvable lineage through Mneme/Mnemosyne before the associated handoff is acknowledged as accepted. Cache presence, a send request, a transport receipt and a confirmed durable work handoff describe different events. Preserve uncertainty after an ambiguous result and reconcile against authoritative state and governed handoff evidence before asserting success or safely retrying. |
| Independent processing and recovery | Attribute processing, failure, retry and terminal outcome to the particular undertaking. Recover through its governed execution boundary with the necessary authoritative information and durable work state. A sibling's result does not determine its outcome. Redelivery, execution retry, parent-work resolution and external acknowledgement retain separate outcomes. |
| Correlation and Assurance | Trace the producing execution, originated InformationUnit and applicable revision, publishing disposition, originating Pragma where one exists, subscription and subscriber, realisation, parcel handoff, and subsequent processing attempts/outcomes. Keep expected obligations and observed results comparable. Evidence distinguishes filtered output, eligible output with zero matches, denied or uncertain operations, and a successful handoff or processing result. |

These requirements specify observable outcomes. Identity keys, persistence schema, coordination and transaction mechanisms, cache layouts, parcel encoding and retry algorithms remain detailed design decisions. [Correlation requirement][core004] · [Duplicate delivery Feature][duplicates] · [Petasos–Mneme draft][petasos-mneme]

## 6. Reconciliation and open decisions

| Matter | Required Solution Architect treatment |
| --- | --- |
| **Publication subject and mask** | Resolve the existing available/egress-Pragma Pattern description against PS-03's output InformationUnit gate before Pragma creation. Determine PublishingMask correspondence to publication and filter statements, policy applicability and the placement of the filtered disposition. Preserve IU governance independently of eligibility. [Pragma Management][pragma-management] · [Working publishing description][legacy-publishing] |
| **Historical work and handoff** | Make explicit the correspondence of `ActionableTaskArchetype`, `ActionableTask`, `FulfillmentTask` and `TaskOutcome` with the single Pragma framework, including identities, concurrency, parent resolution, output aggregation and existing representations. The PetasosParcel Topic records a parcel pointing to the work instance, followed by a consumer establishing the undertaking. PS-09/PS-12 move establishment to Mneme before handoff and change the parcel reference. Preserve that semantic difference for adjudication. [Task Topic][task-topic] · [TaskFulfillment Topic][fulfilment] · [TaskOutcome Topic][task-outcome] · [PetasosParcel Topic][parcel] |
| **Selection attributes** | Reconcile PS-08's Pragma/input attribute boundary with the Parcel/ParcelMask Final's same-context information/routing/optional-operational criteria, including which attributes exist before parcel construction and where other routing criteria apply. Preserve the F-01 Topic/TopicMask compatibility and IU qualifier obligations in DR-10. [Parcel/ParcelMask Final][parcel-final] · [Topic/TopicMask draft][topic-mask] |
| **Pragmaton, WorkSpace and caches** | `Pragmaton` denotes managed contextualised working information (the draft's Data Execution Envelope). The accepted Pragmaton Context Integrity capability protects bounded working information. `Pragmatosis` denotes the independently governed undertaking. Determine how the undertaking relates to the WorkSpace and the existing Pragmaton representation, including containment or association, lifetime and save/unload semantics. Reconcile SummaryCache naming and the Pragma summary role against the existing Standard Cache. [Pragmaton model][pragmaton-model] · [Pragmaton Context Integrity][context-integrity] · [Two-cache acceptance][cache-register] |
| **Specialisations and Assurance** | Establish the Twin subscription boundary, Noesis correspondence to existing Twin-specific inputs and the treatment of Effector. Set the Dokimasia-specific Pragma and realisation names and the derivation and versioning of `Dokimasia.Praxis`. Preserve Pylai's internal/external acceptance boundaries and verify trusted handoff subject to the continuing DR-7 question. [DigitalTwin][twin] · [Pylai][pylai] · [Dokimasia design inputs][dokimasia-note] · [DR-7][security-review] |
| **Retries, acknowledgement and abstractions** | Adjudicate the retry-equivalence boundary across repeat publication or subscription change, the required durable state and acknowledgement owner on each path, and the minimum handoff and Assurance evidence. Confirm whether Router/DecisionEngine, WorkSpace, Ergon-InformationAdapter and derived Dokimasia.Praxis correspond to existing responsibilities or require additional architectural representation. This draft allocates logical responsibilities for review. [Reconciliation evidence][reconciliation] |

## 7. Conformance and candidate Continuum correspondence

Review the producer gate, zero-match path, multiple subscriptions to one subscriber, unauthorised access/release, ambiguous persistence and handoff, replay after partial fan-out, independent processing failures and recoverability. Verify metadata-only matching with unavailable Content, authoritative IU revision preservation, recoverable lineage, specialisation boundary behaviour and sufficient evidence for independent Assurance. Existing source-level checks and implementation review apply to their original candidate scope. [Reconciliation test cases][reconciliation]

If adjudicated, refine the existing Features and their requirement traceability, reconcile the Business work and outcome meanings, and update the matching Application information, function and interaction contracts for Pragma Management, Ergon, Petasos and Dokimasia. Reuse canonical identities where correspondence is established. Allocation of new elements, structural relationships or deployment boundaries requires an explicit architectural decision. [Pragma Management][pragma-management] · [Reconciliation evidence][reconciliation] · [Architecture Review Praxis][review-praxis]

## 8. Review decision

**Pending Solution Architect adjudication of Draft.000.001.** Review the PS-01–PS-16 decision transcription, compatibility and authority differences, and unresolved contract decisions under DR-12 before any dependent architecture application or implementation. [DR-12][hcrl]

[release]: ../../../architecture-release.json
[hcrl]: ../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
[security-review]: ../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-7-pragma-fhir-security-context-authority
[task]: ../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-pragma-publication-and-subscription-task.md
[reconciliation]: ../evidence/software-0.1.0/005/after/Pragma-Publication-and-Subscription-Reconciliation-005.md
[review-praxis]: ../../governance/architecture-review-praxis.md
[axioms]: ../../governance/architectural-axioms.md
[publication-feature]: ../../continuum/strategy/capability/feature/c637e91e3d5e402386ff352d0326c1c5.md
[lineage]: ../../continuum/strategy/capability/feature/157b20d5c544429a8ada854c45ec244f.md
[matching-feature]: ../../continuum/strategy/capability/feature/8b9531db49334913a1d8f0a460227fc7.md
[duplicates]: ../../continuum/strategy/capability/feature/c4ba2198e8a2460d802b0845c938b608.md
[assurance-feature]: ../../continuum/strategy/capability/feature/aa69f4ff110044da918213b0c49008e5.md
[context-integrity]: ../../continuum/strategy/capability/3c87e2b0806647dfa0daa16d523a3915.md
[core004]: ../../continuum/motivation/requirement/assurance-requirement/f2e504e7d6374e41a81303265adda6de.md
[prov001]: ../../continuum/motivation/requirement/assurance-requirement/3e45e60affb44bf384d54de26d3add6a.md
[assurance-requirement]: ../../continuum/motivation/requirement/bbb7438e78604308abba93afc14f4e2e.md
[finite]: ../../continuum/motivation/constraint/3ba73277abf146728ca8c1f5846227d7.md
[self-audit]: ../../continuum/motivation/constraint/e1ce978457e543d5894de24e9be9ac2c.md
[audit]: ../../governance/architectural-axioms.md#kleio-mandatory-capture-authority
[assurance]: ../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-19
[cache-register]: ../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-3
[iu-register]: ../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-4
[persistence]: ../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-1
[ergon-auth]: DRP-Ergon-Authorisation-and-Identity-Delegation.Final.001.md
[lifecycle]: DRP-Calliope-Lifecycle-Revision-and-Operational-State.Final.001.000.md
[pragma-management]: ../../continuum/application/application-function/ab0cdaa7ed4149ee856216efa5a265c4.md
[factory]: ../../continuum/application/application-component/75e77d8dfd1d4870aa6151485d28d1dd.md
[parcel]: ../../continuum/application/data-object/f0a88f05a0cb438cb42572ec07fb8513.md
[work-definition]: ../../continuum/application/data-object/topic/1d6d4c3a2df440be94262855fa6564eb.md
[task-topic]: ../../continuum/application/data-object/topic/c43139289cb649b4836521a96a51604c.md
[fulfilment]: ../../continuum/application/data-object/topic/30f4deffcd694f07bf54b2b192695d4f.md
[task-outcome]: ../../continuum/application/data-object/topic/08d8ee19a30f418f9d5bc497ced0d1ef.md
[standard-cache]: ../../continuum/application/application-function/cfe4e0dbe6894a14841452b731ab9cb8.md
[workflow-cache]: ../../continuum/application/application-function/0bddade1309e426aa84d0cd97c35742a.md
[pylai]: ../../continuum/application/application-component/e3e4147fea3640198dc251ed41ee854d.md
[twin]: ../../continuum/application/application-component/3f64d339648246e99e8866c8089f8960.md
[dokimasia]: ../../continuum/application/application-component/d739c75dffe2453081735710b285f03b.md
[pragma-model]: DRP-Mneme-Pragma-Model.draft.002.md#4-architectural-definition
[pragmaton-model]: DRP-Mneme-Pragmaton-Model.draft.002.md#4-architectural-definition
[ergon-mneme]: DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.002.md#4-architectural-definition
[adhoc]: DRP-Interaction-Ergon-to-Mneme-Adhoc-Information-Access.draft.001.002.md
[petasos-mneme]: DRP-Interaction-Petasos-to-Mneme.draft.002.md#4-architectural-definition
[topic-mask]: DRP-Topic-and-TopicMask.draft.001.003.md#7-consumer-boundaries
[parcel-final]: DRP-PetasosParcel-and-PetasosParcelMask.Final.001.md#4-subscription-and-publication-relationship
[legacy]: ../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Mneme-Publication-Subscription-and-Petasos-Handoff-Architecture.md
[legacy-publishing]: ../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Mneme-Publication-Subscription-and-Petasos-Handoff-Architecture.md#6-publishing
[twin-context]: ../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Digital-Twin-Behaviour-and-Execution-Semantics.md#7-fresh-context-for-each-behaviour
[dokimasia-note]: ../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Energeia-DigitalTwin-Dokimasia-Specialisation.md#10-dokimasia-pragma-specialisation
