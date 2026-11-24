<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Pragma publication and subscription reconciliation · Iteration 005

**Origin** · Architecture 1.1.26 · `feature/BL-CALLIOPE-OO-01` at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`).  
**Review correspondence** · The [operative Task][task] supplied PS-01–PS-16. [Draft.000.001][draft] presents these decisions. The [cumulative pending HCRL.004 / DR-12][review] records the substantive review and refers to its originating Iteration 005 evidence. The reserved 005 HCRL has yet to be created.

## Source correspondence and reconciliation obligations

| Review decision or boundary | Bounded source and standing | Reconciliation required |
| --- | --- | --- |
| **PS-01, PS-07, PS-10, PS-11 · One framework and work identity** | The qualified Pragma and PragmaFactory elements distinguish reusable `ActionableTaskArchetype`, identifiable `ActionableTask`, `FulfillmentTask` and parent resolution `TaskOutcome`. The Mneme Pragma draft identifies the managed envelope and preserves its distinction from operational working information. [Pragma Topic][pragma] · [Work definition][definition] · [Task][work] · [TaskFulfillment][undertaking] · [TaskOutcome][outcome] · [Factory][factory] · [Pragma draft][pragma-draft] | The originating archetypal Pragma occupies the work-instance role for this flow. Pragmatosis occupies the subscriber undertaking role, with independent progression and input lineage. Neither role supplies a canonical identity/synonym mapping. Preserve and adjudicate existing work definition, claim/assignment, concurrency, outcome participation and output composition obligations during correspondence review. |
| **PS-02–PS-05, PS-15 · Producer eligibility** | The working October note describes Publishing via a Pattern on egress Pragma. The qualified Pragma Management function keeps Publishing provider-side and Subscription consumer-side. The continuation design distinguishes separate downstream work from the Praxis-defined role that progresses a current Pathway. [Working publishing description][legacy-publish] · [Pragma Management][management] · [Output and continuation source][continuation] | Move the producer gate to generated IU eligibility before creating a publication Pragma within the proposed flow. Resolve PublishingMask composition and represent a normal filtered outcome separately from failed evaluation, processing failure, or IU lifecycle/withdrawal. Keep the relationship to Pathway continuation explicit for existing handoff paths. |
| **PS-08, PS-15 · Consumer selection** | Topic/TopicMask Draft.001.003 §7 describes neutral Topic criteria, Themis authorisation and feature-owned semantics. The Parcel/ParcelMask Final §4 records agreed conceptual direction for information, routing and optional operational masks in the same context, with conjunction where both apply. The Topic draft's implementation and compatibility obligations remain in HCRL.004 / DR-10. [Topic/TopicMask draft][topic] · [Parcel/ParcelMask Final][parcel-final] · [DR-10][topic-review] | Determine how existing information/parcel/operational criteria correspond to Pragma and input attributes before construction of the parcel. The name `contentMask` alone establishes no requirement to load Content. If a criterion requires an already formed parcel or unavailable metadata, establish its governed evaluation point and outcome before applying the design. |
| **PS-09, PS-11, PS-12 · Realisation and parcel handoff** | The qualified, bounded PetasosParcel element describes by-value conveyance with a reference to the work instance, followed by consumer-side creation of a distinct undertaking. The historical DesignNote repeats that sequence. The Petasos–Mneme draft allows reconstruction and keeps durable work acceptance separate from authority over persisted information. [PetasosParcel Topic][parcel] · [Working handoff description][legacy-handoff] · [Petasos–Mneme draft][petasos-mneme] | PS-09/PS-12 move realisation establishment to Mneme and put the reference to that realisation in the parcel. Reconcile the existing handoff sequence, recipient/claim semantics, required durable state, and supported representations. Preserve separate Petasos queue status, IU authoritative state and Ergon processing outcome. |
| **PS-13, PS-14 · Summaries and durable authority** | RAD 1.1.24 DR-1/DR-3 and RAD 1.1.25 DR-4 establish the Mneme–Mnemosyne interaction, two-cache allocation and IU revision/Content authority. The Mneme–Mnemosyne Final file retains earlier preparation standing; the completed HCRLs record the resulting interpretations and qualifications. [Mneme–Mnemosyne source][interaction] · [Five-contract acceptance][interaction-register] · [Cache acceptance][caches] · [IU revisions][iu] | Establish whether SummaryCache is the existing Standard Cache responsibility or a bounded extension for Pragma summaries. Preserve the difference between a governed Summary obtained via the authoritative contract and a snapshot carried by work. No snapshot creates an IU identity or independent version. |
| **Execution context · Pragmaton and WorkSpace** | Pragmaton .002 tentatively names a Mneme-managed Data Execution Envelope. The accepted Pragmaton Context Integrity capability protects bounded working information. The Ergon–Mneme draft describes contextualisation and return. The accepted Workflow Cache responsibility supports transient local materialisation and context reconstruction. [Pragmaton draft][pragmaton] · [Context integrity][context] · [Ergon–Mneme interaction draft][ergon] · [Cache acceptance][caches] | Pragmatosis identifies the subscriber's realised work; Pragmaton denotes working information. Establish the relationship of existing Pragmaton and WorkSpace, including whether they correspond to the same processing environment at different abstraction levels. Review ContextLoader, ProcessingEngine and Ergon-InformationAdapter functional correspondence before allocating any new architecture. |
| **Specialisations · DigitalTwin and Pylai** | DigitalTwin and Behaviour retain qualified Domain05 definitions, with per-Behaviour fresh context and a serialised Twin input queue for Stimulus/Effector. Pylai's governed interface separates ingress context establishment and durable acceptance from protocol acknowledgements, and preserves AX-02/AX-13 on egress. [DigitalTwin][twin] · [Fresh context source][fresh] · [Pylai][pylai] | Determine the Twin subscription/interest boundary and map Noesis to Stimulus and BehaviourStimulus roles while preserving Effector as a separate review matter. For Pylai, confirm placement relative to authoritative ingress handover and keep internal eligibility and routing separate from authorised external publication. |
| **Security and evidence** | AX-07 and the architecturally accepted G01 design require Themis at governed boundaries. Existing DR-7 leaves the trust conditions of FHIR Task-extension context unresolved. AX-08, RAD 1.1.19 DR-18/19 and the Finite Assurance boundary distinguish evidence meaning, independent evaluation and Kleio audit. [G01][g01] · [DR-7][dr7] · [Provenance][provenance] · [Assurance][assurance] · [Finite Assurance constraint][finite] | Preserve trusted-context requirements on each inspection, execution and release path. Show which OA&M, Kleio, provenance and Assurance evidence records each meaningful disposition. Protect Content and sensitive data through existing controls. |
| **PS-06, PS-16 · Dokimasia Praxis** | Existing adjudication identifies Praxis as the assurance basis and Journey as an observed-evidence subject. The Dokimasia specialisation note leaves the assurance Pragma names and detailed information semantics open. [Assurance disposition][assurance] · [Dokimasia specialisation source][dokimasia] | Define derivation, applicability and version correspondence of `Dokimasia.Praxis`, including how the finite boundary treats Dokimasia's own Praxis. Confirm evidence sufficiency and the unresolved specialised archetype and realisation names. Preserve the boundaries on self-audit, self-assessment and findings modifying the assured activity. |

## Conformance cases for the adjudicated design

These are architectural review and future verification cases. The required outcomes express the design without selecting a schema, algorithm or transaction mechanism.

| Case | Evidence to establish |
| --- | --- |
| Ergon produces zero outputs or a mix of eligible and filtered outputs | Zero outputs need no downstream creation. Each filtered IU retains governed identity and a known normal disposition with no Pragma or subscription match. Eligible outputs have their own originating Pragma and one input. |
| Eligible Pragma yields zero matches or multiple matches | Zero matches remain observable apart from filtering. Multiple matches create independent undertakings, including two matches for distinct subscriptions of one subscriber. |
| Content cannot be materialised and governed summaries are available | Selection can use only permitted Pragma and input attributes. The subsequent Ergon read independently reports its governed unavailable outcome if Content is required. |
| Themis denies or cannot establish permission | Selection does not authorise disclosure, execution or release. A denied or unavailable authority decision has its own evidence and outcome. |
| Partial fan-out or ambiguous persistence/handoff is retried | Confirmed undertakings retain identity across replays, including concurrent retries. Already-established work is not recreated. Persisted, handed-off and indeterminate outcomes are independently accounted for, with authoritative persistence preceding acknowledged durable handoff. |
| One realisation completes and another fails or is redelivered | Each subscriber-specific outcome and retry remains independently traceable to the originating Pragma, source IU revision, subscription and handoff. WorkSpace loss reconstructs required information from governed sources and durable work. |
| Specialised execution and independent assurance | Verify fresh Twin context and serial execution, Pylai ingress and egress boundaries, and Dokimasia's comparison of expected and observed progression with independent access to sufficient evidence. |

## Review boundary

Draft correspondence is established for the current design review. Canonical identities, archived or live representations, accepted-projection edits and implementation details require subsequent scoped review and authority. The inherited DR-1–DR-11 obligations and pre-existing Iteration 005 review evidence retain their recorded status. [Cumulative HCRL][hcrl]

[draft]: ../../../../offline-design/DRP-Pragma-Publication-and-Subscription.draft.000.001.md
[task]: ../before/ai-agent-tasks/unresolved-pragma-publication-and-subscription-task.md
[review]: ../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
[hcrl]: ../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md
[topic-review]: ../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-10
[dr7]: ../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-7-pragma-fhir-security-context-authority
[pragma]: ../../../../../continuum/application/data-object/27ad143e7c784e378556af8a57c438f7.md
[definition]: ../../../../../continuum/application/data-object/topic/1d6d4c3a2df440be94262855fa6564eb.md
[work]: ../../../../../continuum/application/data-object/topic/c43139289cb649b4836521a96a51604c.md
[undertaking]: ../../../../../continuum/application/data-object/topic/30f4deffcd694f07bf54b2b192695d4f.md
[outcome]: ../../../../../continuum/application/data-object/topic/08d8ee19a30f418f9d5bc497ced0d1ef.md
[factory]: ../../../../../continuum/application/application-component/75e77d8dfd1d4870aa6151485d28d1dd.md
[pragma-draft]: ../../../../offline-design/DRP-Mneme-Pragma-Model.draft.002.md#4-architectural-definition
[management]: ../../../../../continuum/application/application-function/ab0cdaa7ed4149ee856216efa5a265c4.md
[legacy-publish]: ../../../../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Mneme-Publication-Subscription-and-Petasos-Handoff-Architecture.md#6-publishing
[legacy-handoff]: ../../../../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Mneme-Publication-Subscription-and-Petasos-Handoff-Architecture.md#12-actionabletask-and-fulfillmenttask-handoff-semantics
[continuation]: ../../../../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Pragma-Outputs-and-Pathway-Continuation.md#3-outputs-and-continuation
[topic]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.003.md#7-consumer-boundaries
[parcel-final]: ../../../../offline-design/DRP-PetasosParcel-and-PetasosParcelMask.Final.001.md#4-subscription-and-publication-relationship
[parcel]: ../../../../../continuum/application/data-object/f0a88f05a0cb438cb42572ec07fb8513.md
[petasos-mneme]: ../../../../offline-design/DRP-Interaction-Petasos-to-Mneme.draft.002.md#4-architectural-definition
[interaction]: ../../../../offline-design/DRP-Interaction-Mneme-to-Mnemosyne.Final.001.md
[interaction-register]: ../../../../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-1
[caches]: ../../../../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-3
[iu]: ../../../../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-4
[pragmaton]: ../../../../offline-design/DRP-Mneme-Pragmaton-Model.draft.002.md#4-architectural-definition
[context]: ../../../../../continuum/strategy/capability/3c87e2b0806647dfa0daa16d523a3915.md
[ergon]: ../../../../offline-design/DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.002.md#4-architectural-definition
[twin]: ../../../../../continuum/application/application-component/3f64d339648246e99e8866c8089f8960.md
[fresh]: ../../../../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Digital-Twin-Behaviour-and-Execution-Semantics.md#7-fresh-context-for-each-behaviour
[pylai]: ../../../../../continuum/application/application-component/e3e4147fea3640198dc251ed41ee854d.md
[g01]: ../../../../offline-design/DRP-Ergon-Authorisation-and-Identity-Delegation.Final.001.md
[provenance]: ../../../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-18
[assurance]: ../../../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-19
[finite]: ../../../../../continuum/motivation/constraint/3ba73277abf146728ca8c1f5846227d7.md
[dokimasia]: ../../../../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Energeia-DigitalTwin-Dokimasia-Specialisation.md#10-dokimasia-pragma-specialisation
