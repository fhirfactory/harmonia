<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Pragma Publication and Subscription

**Design Review Proposal** · Draft.000.003 · Pending Solution Architect adjudication  
**Solution Architect** · Mark Hunter  
**Basis** · Architecture 1.1.26 · Software 0.1.0 review activity in Iteration 005, recorded in cumulative HCRL.004. [Architecture release][release] · [DR-12][hcrl] · [Issued review decisions][task]  
**Lineage** · Draft.000.003 applies the G03 adjudications to reviewed Draft.000.002. [G03 adjudications][g03-adjudications] · [Reviewed predecessor][g03-predecessor] · [Original revision][previous-draft] · [Pragmaton candidate][pragmaton-model]

## 1. Feature realisation

The design realises **EligibleTaskPublication** and **RecoveryPreservesTaskLineage** under Digital Work, Events & Temporal Coordination / Task Publication, Subscription & Assignment, and **SubscriptionMatchCorrect** under Information Access, Discovery & Distribution / Information Distribution & Notification. **Duplicate Delivery Recognition** and **Assurance Evidence Correlation** support recovery and review. These are existing Features. The proposal uses the existing work-progression, information-distribution and assurance responsibilities and leaves their Business and Business-Enabling allocation to the governed mappings. [Publication][publication-feature] · [Lineage][lineage] · [Matching][matching-feature] · [Duplicate delivery][duplicates] · [Assurance correlation][assurance-feature]

Existing requirements include CORE-004 for correlation and causation, PROV-001 for end-to-end provenance, and REQ-FND-005 for independent assurance. Section 5 states the publication-specific reliability requirements exposed by this review. [CORE-004][core004] · [PROV-001][prov001] · [REQ-FND-005][assurance-requirement]

The proposal applies AX-04 to keep eligibility, selection and execution semantics under Harmonia control. AX-05, AX-06 and AX-09 preserve the boundaries between durable information, transient processing state and snapshots. AX-07 governs selection-related access, forwarding and execution. AX-08 and AX-16 require evidence of decisions and coordinated progression. AX-10, AX-11 and AX-15 require recoverable handoff, bounded information access and explicit uncertain outcomes. AX-02 and AX-13 govern external interoperability. AX-14 distinguishes work, information, transport and processing states. AX-17 keeps unincorporated design differences open for adjudication, and AX-18 preserves the authority of the information and domain owners. [Canonical Principles][axioms]

## 2. Proposal and standing

Apply one Pragma-based publication and subscription framework. Ponos owns producer-side eligibility and Mneme evaluates the producing Ergon's PublishingMask for each newly created InformationUnit. Petasos owns and performs subscription matching, requests one Pragmatosis per applicable match and manages its routing and durable delivery. Mneme provides governed information and constructs the requested Pragma-family objects. The receiving Ergon resolves its undertaking through Mneme and accesses its Pragmaton through WorkSpace. Canonical correspondence and incorporation remain pending. [G03 adjudications][g03-adjudications] · [Pragmaton candidate][pragmaton-model]

## 3. Design context

- **Accepted authority.** The Continuum Principles, existing Features, and accepted cache, persistence and IU revision contracts govern this draft. Mneme owns active information access and coordination, Mnemosyne establishes durable state, and the existing two-cache design separates lightweight summary access from transient materialised working data. The accepted Ergon authorisation DRP records the G01 design disposition, with corresponding application and verification obligations. [Two-cache acceptance][cache-register] · [Persistence interaction][persistence] · [IU framework acceptance][iu-register] · [G01 authorisation][ergon-auth]
- **Qualified Continuum baseline.** The Mneme Pragma Management and PragmaFactory elements record adjudicated candidate allocations. The PetasosParcel Topic includes bounded adjudicated handoff semantics under the Domain05 Step-B baseline. That baseline gives Mneme semantic selection, Petasos durable transport and Ponos execution. Its existing handoff sequence and work vocabulary need the explicit reconciliation in §6. [Pragma Management][pragma-management] · [PragmaFactory][factory] · [PetasosParcel Topic][parcel]
- **Review inputs.** The current Pragma-family candidates, Topic/TopicMask Draft.001.003 and the historical publication/subscription DesignNote retain their recorded standing. The qualified allocation of semantic selection to Mneme differs from G03's Petasos allocation. The Parcel/ParcelMask Final records agreed conceptual criteria and leaves canonical and contract correspondence for review. [Pragma draft][pragma-model] · [Pragmaton candidate][pragmaton-model] · [Ergon–Mneme interaction draft][ergon-mneme] · [Petasos–Mneme interaction draft][petasos-mneme] · [Topic/TopicMask draft][topic-mask] · [Publication/subscription DesignNote][legacy] · [Parcel/ParcelMask Final][parcel-final]

## 4. Architectural definition

### 4.1 Output and publication

- **PS-01 — Common framework.** Pragma and its subclasses constitute Harmonia's exclusive publication/subscription work framework. Petasos continues to serve other established communication and notification purposes. [Pragma Management][pragma-management] · [PetasosParcel Topic][parcel]
- **PS-02 — Output information.** An Ergon processes an undertaking and can generate zero or more output InformationUnits. Every newly created InformationUnit enters the producing Ergon's PublishingMask evaluation. Changes to existing Pragmaton working information return through governed Mneme save, with authoritative changes established through Mnemosyne where required. The mask determines eligibility for downstream publication. It supplies no decision about IU governance, output mediation or the separate Praxis-defined Pathway continuation. [Pragmaton output distinction][pragmaton-output] · [Ergon–Mneme interaction][ergon-mneme]
- **PS-03 — PublishingMask.** Ponos owns publication eligibility. Mneme evaluates the producing Ergon's PublishingMask before downstream Pragma creation. The default is `ALLOW_ALL`. Themis governs protected access, disclosure and other applicable operations independently of the mask.
- **PS-04 — Filtered information.** An InformationUnit that fails the mask remains governed information. It yields no downstream Pragma and is excluded from ordinary subscription matching. `FILTERED_OUT` is an illustrative publication disposition. Its representation and ownership within the existing operational-state model require adjudication. [Lifecycle and operational-state design][lifecycle]
- **PS-05 — Normal disposition.** Filtering is a normal publication outcome. Preserve the distinction between a known mask rejection, a failed or indeterminate evaluation, an authorisation denial, and an Ergon processing failure.
- **PS-06 — Evidence.** Record the publication decision as appropriate to the evidence purpose. OA&M captures operational counts and outcomes. Kleio captures policy-governed audit events. Provenance records the causal relationship between activity, information and decision. Dokimasia uses relevant evidence for independent assessment. [Audit capture authority][audit] · [Provenance and Assurance disposition][assurance]
- **PS-07 — PragmaFactory.** For each eligible output InformationUnit, the publication flow establishes an originating Pragma with exactly one input IU. The reusable work definition and the instantiated Pragma remain distinct. The Mneme-engine PragmaFactory accepts construction requests from Ponos, DigitalTwinEngine and Petasos and applies the relevant information-model invariants. Workflow progression, subscription interpretation and queue routing remain with their functional owners. [PragmaFactory][factory] · [Work definition][work-definition] · [Task Topic][task-topic]

### 4.2 Selection, realisation and handoff

- **PS-08 — Subscription matching.** Petasos owns and performs SubscriptionMask evaluation. Mneme supplies governed Pragma and input IU summary/metadata for R1/R2. R3+ may also evaluate authorised IU payload content. Mneme provides access through governed information services and leaves interpretation to Petasos. TopicMask retains its approved neutral criterion semantics. Themis PEPs enforce PDP decisions at the applicable access, disclosure, forwarding and execution boundaries. An unavailable or unauthorised input supplies no basis to widen matching. [Topic/TopicMask consumer boundaries][topic-mask] · [G01 authorisation][ergon-auth]
- **PS-09 — Subscriber-specific realisation.** Petasos owns Pragmatosis generation and requests construction through Mneme's PragmaFactory. Each applicable match establishes its corresponding undertaking under the applicable identity, retry and authorisation rules. The subscription already determines the intended Ergon queue. Petasos owns and performs queue routing and management. Zero matches yield zero undertakings and no Ergon TaskOutcome. Preserve the distinction between zero matches and a PublishingMask non-match.
- **PS-10 — Terminology.** A **Pragma** realises as a **Pragmatosis**. A Digital Twin **Stimulus** realises as a **Noesis**. The two left-hand terms identify the archetypal role and the two right-hand terms the undertaking role. Dokimasia-specific Pragma and realisation names remain unresolved. `ActionableTask` and `FulfillmentTask` are source terminology requiring explicit reconciliation with these roles. [Terminology reconciliation][reconciliation]
- **PS-11 — Independent identity.** Each realised undertaking has its own identity, lifecycle, outcome, subscriber association and processing context. It retains a governed relationship to the originating archetypal Pragma and uses the governed input InformationUnit reference. Multiple subscriptions can therefore progress independently while retaining common lineage. [Recovery lineage Feature][lineage]
- **PS-12 — Petasos handoff.** A by-value PetasosParcel carries the established undertaking's Pragmatosis identity and governed handoff metadata. Keep the IU reference and payload outside the parcel within governed information access. Petasos manages durable delivery. The receiving Ergon resolves the undertaking through Mneme, while Ponos governs execution. [Petasos–Mneme interaction][petasos-mneme] · [PetasosParcel topic and baseline difference][parcel]
- **PS-13 — Information management.** Mneme manages Pragma-family information and coordinates authoritative persistence through Mnemosyne. Standard Cache provides distributed IU summary/reference access. The allocation of Pragma summaries and the correspondence of the earlier `SummaryCache` label remain open. [Cache acceptance][cache-register] · [Persistence interaction][persistence]
- **PS-14 — InformationUnit snapshots.** Summaries carried by a Pragma or its realisation are immutable, content-free, non-authoritative snapshots. They retain the source InformationUnit identity and applicable revision correspondence. Contextualised copies remain active working state and retain the link to their source's authority and revision. Only the authoritative Mneme/Mnemosyne path establishes a new authoritative version. [Pragmaton output and IU distinctions][pragmaton-output] · [IU framework acceptance][iu-register] · [Mneme Standard Cache][standard-cache]
- **PS-15 — Asymmetric governance.** Publishing governs producer-side eligibility. Subscription separately governs consumer-side selection of eligible work. Those decisions, Themis authorisation, durable handoff and processing completion each retain their own outcomes.
- **PS-16 — Assurance.** Dokimasia independently evaluates the relevant expected and observed publication, filtering, subscription, handoff and processing progression against the applicable Praxis basis. Keep that basis, Journey observations where applicable, and Assurance findings distinct from execution outcomes, audit and provenance. The applicability, derivation and version correspondence of any `Dokimasia.Praxis`, including treatment of Dokimasia's own Praxis, remain pending within the finite Assurance and self-audit constraints. [Assurance disposition][assurance] · [Finite boundary][finite] · [Self-audit constraint][self-audit]

### 4.3 Ergon processing environment

**Pragmaton** is the Mneme-managed, bounded execution-information environment for a particular realised undertaking. **`Ergon.WorkSpace`** is the Ergon-facing access abstraction for that environment. Through `workspace()` the Ergon accesses the operational existence pocket of that execution. `Ergon.ContextLoader` establishes the initial working context with Mneme. `Ergon.ProcessingEngine` uses WorkSpace for bounded processing. [Pragmaton and WorkSpace][pragmaton-workspace]

Pragmaton supports identity-based and associative access to working information. JavaSpaces supplies the associative-access analogy. Lazy resolution and additional acquisition during execution use the `Ergon-InformationAdapter` to mediate governed access through Mneme. Themis PEP/PDP controls apply to initial and later access. Existing full-read materialisation requirements and exceptions apply when execution requires Content. Petasos subscription matching follows the release-specific information scope in PS-08. [Associative information-space model][pragmaton-space] · [On-demand acquisition][pragmaton-access] · [G01 authorisation][ergon-auth]

Mneme's Workflow Cache supports Pragmaton using transient, execution-local materialised working information in the applicable JVM. Standard Cache provides distributed, lightweight IU summary and reference access. Mnemosyne establishes authoritative durable state. The accepted two-cache allocation and its qualifications apply. The Context Unloader and Mneme save interaction handle changes to existing working values separately from new Pragma Output. [Pragmaton cache relationship][pragmaton-cache] · [Workflow Cache][workflow-cache] · [Two-cache acceptance][cache-register] · [Ergon–Mneme interaction][ergon-mneme]

**DigitalTwin-Behaviour.** Each Behaviour establishes fresh, entity-relative context from its own Praxis with Mneme, which saves governed consequences before the next applicable Behaviour loads context. The common realisation pattern must preserve Twin-relative identity and reference resolution, serialisation, and normal queuing of same-Twin outputs. Map Noesis to the existing BehaviourStimulus/Stimulus roles explicitly before changing those contracts. [DigitalTwin][twin] · [Fresh Behaviour context][twin-context]

**Pylai ingress and egress.** An ingress loader establishes initial context from the Communication InformationUnit, with the existing authoritative handover conditions before an acknowledgement that claims durable acceptance. Egress establishes context for the applicable external representation and enforces standards-compliant, authorised, non-destructive, fail-closed projection. Internal PublishingMask eligibility and subscription selection confer no external release authority. [Pylai][pylai]

**Dokimasia.** The specialised Ergon uses common execution and governed context facilities to evaluate evidence independently. Its assurance result does not substitute for underlying observations or the assured activity's outcome. [Dokimasia][dokimasia] · [Assurance disposition][assurance]

## 5. Reliability and consistency requirements

| Requirement | Required behaviour and evidence |
| --- | --- |
| Reliable creation and duplicate prevention | Every matching subscription within the authorised operation must have a determinable creation outcome. Replaying or concurrently retrying the same publication-to-subscription obligation must establish or recover the same realisation without creating a second one. Distinct subscriptions retain distinct undertakings, including when they identify the same subscriber. Evidence must distinguish a recovered existing undertaking from a new one. |
| Persistence before acknowledged handoff | Establish the authoritative realisation and durable, resolvable lineage through Mneme/Mnemosyne before the associated handoff is acknowledged as accepted. Cache presence, a send request, a transport receipt and a confirmed durable work handoff describe different events. Preserve uncertainty after an ambiguous result and reconcile against authoritative state and governed handoff evidence before asserting success or safely retrying. |
| Independent processing and recovery | Record TaskOutcome for actual Ergon-instance execution under Praxis-defined rules. Preserve attempt attribution for concurrent equivalent processing and recovery. Undertaking disposition, output mediation, Assurance, originating Pragma disposition, redelivery and external acknowledgement retain separate meanings. Zero subscribers yield no Ergon TaskOutcome. [Pragma outcome model][pragma-outcomes] |
| Correlation and Assurance | Trace the producing execution, originated InformationUnit and applicable revision, publishing disposition, originating Pragma where one exists, subscription and subscriber, realisation, parcel handoff, and subsequent processing attempts/outcomes. Keep expected obligations and observed results comparable. Evidence distinguishes filtered output, eligible output with zero matches, denied or uncertain operations, and a successful handoff or processing result. |

These requirements specify observable outcomes. Identity keys, persistence schema, coordination and transaction mechanisms, cache layouts, parcel encoding and retry algorithms remain detailed design decisions. [Correlation requirement][core004] · [Duplicate delivery Feature][duplicates] · [Petasos–Mneme draft][petasos-mneme]

## 6. Reconciliation and open decisions

| Matter | Required Solution Architect treatment |
| --- | --- |
| **Publication subject and mask** | Resolve the existing available/egress-Pragma Pattern description against PS-03's output InformationUnit gate before Pragma creation. Determine PublishingMask correspondence to publication and filter statements, policy applicability and the placement of the filtered disposition. Preserve IU governance independently of eligibility. [Pragma Management][pragma-management] · [Working publishing description][legacy-publishing] |
| **Historical work and handoff** | Establish the correspondence of `ActionableTaskArchetype`, `ActionableTask`, `FulfillmentTask` and the existing `TaskOutcome` Topic with the candidate framework. Preserve concurrency, output mediation and originating-work resolution. The PetasosParcel Topic describes an originating work reference and consumer-side undertaking creation. PS-09/PS-12 define Petasos-requested construction through Mneme before handoff and carry the undertaking identity. Reconcile both differences before incorporation. [Task Topic][task-topic] · [TaskFulfillment Topic][fulfilment] · [TaskOutcome Topic][task-outcome] · [PetasosParcel Topic][parcel] |
| **Selection attributes** | Reconcile PS-08's release-specific information scope with the Parcel/ParcelMask Final's same-context information/routing/optional-operational criteria. Establish what is available before parcel construction, how unavailable or inapplicable criteria affect selection, where later routing checks apply, and the R3+ authorised content contract. Preserve DR-10 and G03 A06/A07. [Parcel/ParcelMask Final][parcel-final] · [Topic/TopicMask draft][topic-mask] |
| **Pragmaton incorporation and cache correspondence** | Review formal representation and lifecycle of the Pragmaton association to a realised undertaking, any required durable linkage to the originating Pragma, and save/unload outcomes. Determine where Pragma summaries belong and reconcile the earlier SummaryCache label with the two accepted caches. [Pragmaton review matters][pragmaton-questions] · [Pragmaton Context Integrity][context-integrity] · [Two-cache acceptance][cache-register] |
| **Specialisations and Assurance** | Establish the Twin subscription boundary, Noesis correspondence to existing Twin-specific inputs and the treatment of Effector. Set the Dokimasia-specific Pragma and realisation names and the derivation and versioning of `Dokimasia.Praxis`. Preserve Pylai's internal/external acceptance boundaries and verify trusted handoff subject to the continuing DR-7 question. [DigitalTwin][twin] · [Pylai][pylai] · [Dokimasia design inputs][dokimasia-note] · [DR-7][security-review] |
| **Retries, acknowledgement and contract correspondence** | Decide the retry-equivalence boundary across repeat publication or subscription change, the required durable state and acknowledgement owner on each path, and the minimum handoff and Assurance evidence. Map the revised Petasos matching/routing and Mneme construction roles, WorkSpace and adapter roles, and any `Dokimasia.Praxis` derivation to existing architecture before allocating new elements. Confirm on-demand access and save outcomes in the interaction candidates. [G03 adjudications][g03-adjudications] |

## 7. Conformance and candidate Continuum correspondence

Review the producer gate, zero-match path, multiple subscriptions to one subscriber, unauthorised access/release, ambiguous persistence and handoff, replay after partial fan-out, independent processing failures and recoverability. Verify R1/R2 matching without Content, the separate R3+ authorised content boundary, policy-governed lazy resolution, IU revision preservation, recoverable lineage, specialisation boundary behaviour and evidence for independent Assurance. Existing source-level checks and implementation review apply to their original candidate scope. [Original reconciliation test cases][reconciliation] · [G03 adjudications][g03-adjudications]

If adjudicated, refine the existing Features and their requirement traceability, reconcile the Business work and outcome meanings, and update the matching Application information, function and interaction contracts for Pragma Management, Ergon, Petasos and Dokimasia. Reuse canonical identities where correspondence is established. Allocation of new elements, structural relationships or deployment boundaries requires an explicit architectural decision. [Pragma Management][pragma-management] · [Reconciliation evidence][reconciliation] · [Architecture Review Praxis][review-praxis]

## 8. Review decision

**Draft.000.003 remains pending Solution Architect review.** PS-01–PS-16 reflect the issued G03 adjudications and the remaining decision and verification obligations under DR-12. [G03 adjudications][g03-adjudications] · [DR-12][hcrl]

[release]: ../../../architecture-release.json
[hcrl]: ../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
[security-review]: ../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-7-pragma-fhir-security-context-authority
[task]: ../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-pragma-publication-and-subscription-task.md
[follow-up]: ../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-pragmaton-publication-subscription-reconciliation-task.md
[previous-draft]: DRP-Pragma-Publication-and-Subscription.draft.000.001.md
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
[pragma-model]: DRP-Mneme-Pragma-Model.draft.000.004.md#4-architectural-definition
[pragmaton-model]: DRP-Mneme-Pragmaton-Model.draft.000.004.md
[pragmaton-workspace]: DRP-Mneme-Pragmaton-Model.draft.000.004.md#37-pragmaton-and-ergonworkspace
[pragmaton-space]: DRP-Mneme-Pragmaton-Model.draft.000.004.md#38-associative-managed-information-space-semantics-javaspaces-inspiration
[pragmaton-cache]: DRP-Mneme-Pragmaton-Model.draft.000.004.md#39-mneme-workflow-cache-relationship
[pragmaton-access]: DRP-Mneme-Pragmaton-Model.draft.000.004.md#311-on-demand-acquisition-and-authorisation
[pragmaton-output]: DRP-Mneme-Pragmaton-Model.draft.000.004.md#312-pragma-pragmatosis-informationunit-and-output-separation
[pragmaton-questions]: DRP-Mneme-Pragmaton-Model.draft.000.004.md#9-open-questions--matters-for-review
[ergon-mneme]: DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.000.004.md#4-architectural-definition
[petasos-mneme]: DRP-Interaction-Petasos-to-Mneme.draft.000.004.md#4-architectural-definition
[topic-mask]: DRP-Topic-and-TopicMask.draft.001.003.md#7-consumer-boundaries
[parcel-final]: DRP-PetasosParcel-and-PetasosParcelMask.Final.001.md#4-subscription-and-publication-relationship
[legacy]: ../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Mneme-Publication-Subscription-and-Petasos-Handoff-Architecture.md
[legacy-publishing]: ../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Mneme-Publication-Subscription-and-Petasos-Handoff-Architecture.md#6-publishing
[twin-context]: ../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Digital-Twin-Behaviour-and-Execution-Semantics.md#7-fresh-context-for-each-behaviour
[dokimasia-note]: ../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Energeia-DigitalTwin-Dokimasia-Specialisation.md#10-dokimasia-pragma-specialisation

[g03-predecessor]: DRP-Pragma-Publication-and-Subscription.draft.000.002.md
[g03-adjudications]: ../evidence/software-0.1.0/005/after/G03-Pragma-Family-Adjudications-005.md
[pragma-outcomes]: DRP-Mneme-Pragma-Model.draft.000.004.md#48-execution-outcomes-and-work-disposition
