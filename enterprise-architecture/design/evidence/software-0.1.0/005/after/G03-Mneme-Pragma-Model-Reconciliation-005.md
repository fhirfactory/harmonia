<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# G03 Mneme Pragma Model reconciliation · Iteration 005

**Origin** · Architecture 1.1.26 · `feature/BL-CALLIOPE-OO-01` at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`).  
**Review correspondence** · [G03 Task][task] · [Starting Draft.002][previous] · [Mneme Pragma Model Draft.000.003][candidate] · [Cumulative HCRL.004 / DR-12][hcrl]

## Preserved architectural semantics

| Draft.002 concern | Retained architecture and candidate correspondence |
| --- | --- |
| Governed work information and ownership | Keep the Mneme-managed information envelope and the separate Ponos/Ergon execution, Petasos handoff and Mnemosyne durable-authority responsibilities. The qualified Pragma Topic retains its Metabolē realisation and lifecycle separation from Pylai.Diadromí. FHIR Task, message and runtime representations remain distinct from the architectural work envelope. [Pragma Topic][pragma] · [Pragma Management][management] · [Management responsibility DesignNote][management-note] |
| New output and changed working values | Preserve the distinction between generated Pragma Output and modification of working context. Continue to apply the governed Mneme save and persistence boundary, and preserve the Praxis-defined Pathway-continuation role without assigning an identity or subtype to it. [Mneme Context DesignNote][context-note] · [Output and continuation DesignNote][output-note] · [Pragma Topic][pragma] · [Persistence interaction][persistence] |
| Progression and evidence | Preserve governed identity, correlation, causation, security, audit and provenance. Information revision, work resolution, undertaking outcome and durable handoff retain distinct semantics. Preserve applicability of Themis, Kleio and independent Dokimasia Assurance. [IU authority][iu] · [Authorisation][security] · [Publication/subscription candidate][publication] |

## Decisions incorporated in the candidate

- The reusable definition, an originating archetypal Pragma and a subscriber-specific Pragmatosis are distinct. Each realised undertaking carries independent identity, lifecycle, outcome and lineage. Stimulus → Noesis preserves the reviewed Digital Twin terminology. [Publication/subscription candidate][publication]
- `Pragma.input()` represents one governed InformationUnit. `Pragma.output()` represents zero or more generated InformationUnits. Embedded summaries are immutable, content-free and non-authoritative, and preserve source identity and version correspondence. Output attribution identifies the undertaking that produced the output. Originating-work resolution remains governed separately. [IU authority][iu] · [Pragmaton candidate][pragmaton]
- The realised undertaking has a Mneme-managed Pragmaton for execution. WorkSpace is the Ergon-facing access abstraction. The Pragmaton candidate supplies the detailed information-space design. [Pragmaton candidate][pragmaton]
- Publication, subscription realisation, durable handoff, execution, completion and retry have separate effects and evidence. The Task supplies the `DoNotBefore` requirement. The draft records its minimum earliest-execution meaning and carries the applicable constraint across handoff and retry using existing scheduling responsibility. The bounded source search found no named `DoNotBefore` field contract. The accepted **SingleSchedulingAuthority** Feature supplies the ownership constraint, and NFR-097 supplies source-qualified scheduling context. [G03 Task][task] · [SingleSchedulingAuthority][scheduler-feature] · [NFR-097][scheduler-principle]

## Historical discrepancies and required disposition

| Discrepancy | Review obligation |
| --- | --- |
| Historical Task terminology and parent resolution | Establish correspondence between reusable `ActionableTaskArchetype`, identifiable `ActionableTask`, `FulfillmentTask` undertaking and its contribution to `TaskOutcome`. Preserve the historical model's independent controls for execution concurrency, outcome participation and output composition. A Pragmatosis outcome is its own result. Canonical identities and supported Work Order, To Do, synthetic Task and FHIR representations need explicit disposition. [TaskDefinition][definition] · [Task][work] · [TaskFulfillment][undertaking] · [TaskOutcome][outcome] |
| Older input cardinality and output notation | The Mneme Context DesignNote illustrates `Input(s)`, and the qualified Pragma Topic permits generic payload descriptions. Record the exact single-IU input semantics for this candidate. Determine correspondence for legacy multi-input representations, parent output aggregation and Pathway continuation before any migration or incorporation. [Mneme Context DesignNote][context-note] · [Pragma Topic][pragma] · [Output and continuation DesignNote][output-note] |
| Handoff and lifecycle wording | The PetasosParcel Topic carries a work reference and describes recipient-side undertaking creation. PS-09/PS-12 create the subscriber realisation in Mneme before reference handoff and preserve authoritative state before acknowledged durable handoff. Determine correspondence, acknowledgement owner and minimum recoverable state and lineage. Preserve the existing retry and evidence obligations. [PetasosParcel Topic][parcel] · [Publication/subscription reconciliation][publication-evidence] |
| Scheduling details and authority | Confirm canonical placement of `DoNotBefore`, which governed boundary evaluates and evidences it, how it applies following retry or recovery, and how uncertain schedule eligibility remains visible. The design asserts an execution-start constraint. Publication/selection timing effects and pre-start handoff policy have no separate disposition in this task. [G03 Task][task] · [SingleSchedulingAuthority][scheduler-feature] |
| Working context and specialised roles | The revised Pragmaton candidate locates the environment at execution of a realised undertaking. Formal association and lifecycle, any durable relation to the archetypal Pragma and governed save/unload still require disposition. Confirm Twin correspondence including Effector and retain unresolved Dokimasia type names. [Pragmaton candidate][pragmaton] · [Publication/subscription reconciliation][publication-evidence] |

State-machine encoding, timer mechanics, identifier formats, retry algorithms and storage or transport schema remain software design under the agreed semantic contracts. The Pragma model fixes neither the G03 selector migration choices nor the routing projection. A06/A07 and the DR-4/DR-10 compatibility and selector obligations carry forward. [G03 issue group][group] · [DR-10][topic-review]

## Proposed follow-on corrections

- **Petasos–Mneme Draft.002 §§3.2–3.4.** Align work acceptance, reference and reconstruction wording with the pre-established subscriber realisation. Distinguish recovering that undertaking and its lineage from reconstructing transient working context. Address the retained scheduling constraint after the authoritative contract is adjudicated. [Petasos–Mneme interaction][petasos-mneme]
- **Ergon–Mneme Draft.002 §§3.1–3.3 and §9.** Carry forward the existing proposed WorkSpace and collaborative contextualisation amendments. Identify the realised undertaking and the authoritative source/version basis in context establishment. Preserve return of changed values and separate attribution of new output. [Ergon–Mneme interaction][ergon-mneme] · [Prior proposed amendments][amendments]
- **Pragmaton Draft.000.003 §§2 and 9.** Align the earlier “within/associated with a Pragma” wording with §§3.7 and 3.12. Carry forward the formal association, lifetime and possible durable linkage rather than reopening the agreed WorkSpace relationship. [Pragmaton candidate][pragmaton]
- **Publication/subscription Draft.000.002 §§4.1–4.2 and 5.** Cross-reference the Pragma model for exact input/output cardinality and Mneme management of both originating and realised work. Carry the scheduling constraint into handoff and retry requirements after its allocation is adjudicated. [Publication/subscription candidate][publication]

## Review checks

Compare different subscriptions and replay of the same obligation. Verify independent outcomes and stable origin, subscriber and IU lineage. Check that a reliably delivered undertaking remains deferred before an applicable `DoNotBefore`, including through recovery, and that reaching the time supplies only timing eligibility. Confirm that work output, governed save, transport receipt and durable authority can be attributed and evidenced independently. These are candidate review checks and future conformance cases. [Candidate][candidate] · [DR-12][hcrl]

[task]: ../before/ai-agent-tasks/unresolved-g03-mneme-pragma-model-reconciliation-task.md
[previous]: ../../../../offline-design/DRP-Mneme-Pragma-Model.draft.002.md
[candidate]: ../../../../offline-design/DRP-Mneme-Pragma-Model.draft.000.003.md
[publication]: ../../../../offline-design/DRP-Pragma-Publication-and-Subscription.draft.000.002.md
[publication-evidence]: Pragma-Publication-and-Subscription-Reconciliation-005.md
[amendments]: Pragma-Publication-and-Subscription-Reconciliation-005.md#proposed-ergonmneme-interaction-amendments
[pragmaton]: ../../../../offline-design/DRP-Mneme-Pragmaton-Model.draft.000.003.md
[petasos-mneme]: ../../../../offline-design/DRP-Interaction-Petasos-to-Mneme.draft.002.md#4-architectural-definition
[ergon-mneme]: ../../../../offline-design/DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.002.md#4-architectural-definition
[security]: ../../../../offline-design/DRP-Ergon-Authorisation-and-Identity-Delegation.Final.001.md
[hcrl]: ../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
[topic-review]: ../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-10
[group]: Calliope-Architectural-Issue-Groups-005.md#g03--selector-composition-compatibility-and-routing-eligibility
[pragma]: ../../../../../continuum/application/data-object/27ad143e7c784e378556af8a57c438f7.md
[management]: ../../../../../continuum/application/application-function/ab0cdaa7ed4149ee856216efa5a265c4.md
[parcel]: ../../../../../continuum/application/data-object/f0a88f05a0cb438cb42572ec07fb8513.md
[definition]: ../../../../../continuum/application/data-object/topic/1d6d4c3a2df440be94262855fa6564eb.md
[work]: ../../../../../continuum/application/data-object/topic/c43139289cb649b4836521a96a51604c.md
[undertaking]: ../../../../../continuum/application/data-object/topic/30f4deffcd694f07bf54b2b192695d4f.md
[outcome]: ../../../../../continuum/application/data-object/topic/08d8ee19a30f418f9d5bc497ced0d1ef.md
[scheduler-feature]: ../../../../../continuum/strategy/capability/feature/48977be5d3b24f82ba676a0b8a26e0d6.md
[scheduler-principle]: ../../../../../continuum/motivation/principle/architectural-axiom/99adfc9b5af049748df91755d395c247.md
[persistence]: ../../../../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-1
[iu]: ../../../../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-4
[management-note]: ../../../../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Pragma-Management-Responsibility-and-Energeia-Boundary.md#responsibility-separation
[context-note]: ../../../../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Mneme-Context-and-Pragma-Management.md#10-pragma-management
[output-note]: ../../../../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Pragma-Outputs-and-Pathway-Continuation.md#3-outputs-and-continuation
