<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# G03 Pragma-family adjudications · Iteration 005

**Basis** · Architecture 1.1.26 · Software 0.1.0 · Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`) plus the five worktree drafts pinned by the cross-document review. The Solution Architect's G03 instruction establishes the candidate-design decisions below. DR-12 and the revised candidates remain pending. [Operative Task][task] · [Original PF-01–PF-12 register][basis] · [HCRL.004 / DR-12][hcrl]

## Candidate responsibility allocation

| Responsibility | Owner | Worker | Involved |
| --- | --- | --- | --- |
| Publication eligibility | Ponos | Mneme | — |
| Subscription matching | Petasos | Petasos | Mneme |
| Pragmatosis generation | Petasos | Mneme | — |
| Queue routing | Petasos | Petasos | — |
| Queue management | Petasos | Petasos | — |

Mneme provides governed information and construction services. Its PragmaFactory serves Ponos, DigitalTwinEngine and Petasos and applies the requested objects' model invariants. Ownership of functional decisions remains with the designated boundaries. Mneme does not interpret workflow progression, Praxis outcome rules, SubscriptionMask, queue routing/management or Ergon execution decisions. Themis owns policy evaluation through PDPs. The applicable PEPs enforce those decisions at protected access, disclosure, forwarding, resolution and execution boundaries. Functional allocations supply no authorisation. [Pragma model §§4.4–4.6][pragma] · [Petasos–Mneme §§4.1–4.2, 4.6][petasos] · [G01 design][g01] · [AX-07][ax07] · [AX-18][ax18]

## PF-01–PF-12 disposition

**Partially resolved** applies where G03 settles candidate intent or corrects a conflict and a material part of the original exception remains open. **Unresolved** applies where the governing decision is still required. **Editorial only** covers bounded wording and reference alignment. These dispositions apply to the review exceptions. DR-12 governs any future incorporation and acceptance.

| Exception | Disposition | Adjudicated candidate treatment | Remaining authority or contract question |
| --- | --- | --- | --- |
| PF-01 · Twin execution identity | Partially resolved | DigitalTwin Behaviour executes the applicable realised undertaking, named Noesis in the candidate. The originating Stimulus supplies Twin-relative input for fresh context. Existing serialisation and same-Twin queuing apply. [Pragmaton §3.10][pragmaton] · [Ergon–Mneme §4.1][ergon] · [Publication §4.3][publication] | Establish Noesis/Stimulus correspondence with the existing BehaviourStimulus and interest model, queue/subscription identity, and the independent Effector treatment. |
| PF-02 · PublishingMask scope and placement | Partially resolved | Evaluate every newly created IU against the producing Ergon's PublishingMask before downstream publication. Default `ALLOW_ALL`. Ponos owns eligibility and Mneme evaluates. A known non-match is an ordinary publication disposition, leaves the IU governed and leaves working save, output mediation and Pathway continuation under their own contracts. [Publication PS-02–PS-07][publication] · [Pragma §4.2][pragma] · [Pragmaton §3.12][pragmaton] · [Ergon–Mneme §4.6][ergon] | Reconcile the earlier qualified egress-Pragma gate and the new IU-stage gate, including mask/statement and filtered-disposition correspondence and the deferred representation of continuation. |
| PF-03 · Canonical work, output and cardinality | Partially resolved | Retain one input and zero or more generated outputs with source/production attribution. Keep reusable definition, originating Pragma and each undertaking distinct. TaskOutcome concerns actual Ergon-instance processing under Praxis-defined rules. Undertaking disposition, output mediation, Assurance and originating Pragma disposition remain independently meaningful. Preserve attribution during concurrent equivalent processing and recovery. Zero subscribers yield no Ergon TaskOutcome. [Pragma §§4.1–4.2, 4.8][pragma] · [Ergon–Mneme §4.6][ergon] | Adjudicate historical Task-family correspondence, the existing TaskOutcome Topic's originating-work meaning versus the G03 use, legacy input/work representations, concurrency and outcome participation, output composition/version attribution, and canonical outcome/disposition representation. |
| PF-04 · Parcel reference and undertaking establishment | Partially resolved | Petasos requests subscriber-specific construction through Mneme's PragmaFactory. Establish authoritative undertaking and lineage before handoff. The parcel carries the undertaking identity and permitted handoff metadata. The subscription already determines the intended Ergon queue. Petasos manages routing and delivery. The Ergon resolves through Mneme. [Publication PS-09–PS-12][publication] · [Petasos–Mneme §§4.1–4.3, 7][petasos] | Map the new referent, construction allocation and recoverable lineage to the governed elements. The qualified prior PetasosParcel account refers to originating work and consumer-side establishment. Preserve other parcel purposes and ADR-018. |
| PF-05 · Cache qualification and Pragma summaries | Partially resolved | Reaffirm distributed, reference-oriented IU access in the Standard Cache and embedded LOCAL Workflow Cache per applicable execution JVM, with MC-01–MC-07. WorkSpace is the Ergon-facing operational access to Pragmaton. Mnemosyne establishes authoritative versions. [Pragmaton §§3.7, 3.9][pragmaton] · [Publication PS-13–PS-14][publication] · [Accepted cache allocation][caches] | Determine Pragma summary scope and allocation and the correspondence of the earlier SummaryCache label within the accepted two-cache design. Preserve applicability and deployment qualifications. |
| PF-06 · Pre-parcel selector scope | Partially resolved | Petasos owns and performs SubscriptionMask matching with governed information supplied through Mneme. R1/R2 use Pragma and IU summary/metadata. R3+ may additionally evaluate authorised IU payload. Themis governs access and disclosure, while the subscription already specifies its Ergon queue. [Publication PS-08–PS-09, §6][publication] · [Petasos–Mneme §§4.1–4.2][petasos] | Reconcile the qualified prior allocation to Mneme, same-context pre-parcel inputs, unavailable/inapplicable criteria, later routing checks, R3+ content access and legacy selector projections. Preserve DR-10 and G03 A06/A07. |
| PF-07 · Authority, acceptance and acknowledgement evidence | Partially resolved | Separate authoritative work establishment, Petasos durable acceptance and delivery, actual Ergon execution, Mneme save, authoritative IU persistence and required Kleio capture. Preserve indeterminate effects and separate fan-out results. The Artemis candidate provides a conditional transaction scope for one handoff pair. [Petasos–Mneme §§4.3–4.4, 4.7][petasos] · [Audit candidate][audit] | Confirm each acknowledgement's issuer, sufficient recoverable revision/lineage evidence, external boundary where applicable, authoritative reconciliation after partial/unknown outcomes, and the new audit capture/evidence question. |
| PF-08 · Retry equivalence and execution eligibility | Unresolved | Same-obligation retries preserve identity and lineage and the applicable `DoNotBefore`. Duplicate delivery recognition and execution idempotence remain distinct. Preserve evidence of actual execution and uncertainty. [Pragma §§4.4, 4.8][pragma] · [Petasos–Mneme §4.4][petasos] | Establish same-obligation equivalence across renewed publication, changed subscription and deliberate replay, ownership of timing/recovery decisions, and acceptable evidence after an indeterminate outcome. |
| PF-09 · Pragmaton access, lifecycle and save | Partially resolved | The Ergon's `workspace()` is the operational existence pocket of the execution, exposing its particular undertaking's Mneme-managed Pragmaton. Load, governed additional access, working return and any authoritative save effects stay distinct. [Pragmaton §§3.6–3.11][pragmaton] · [Ergon–Mneme §§4.1–4.7][ergon] | Establish formal association/lifetime, access and contextualisation scope, source/version and checkpoint evidence, intake versus bounded return, policy change/non-disclosure, unload/save outcomes, and canonical adapter and Load/Save correspondence. |
| PF-10 · Trusted handoff and resolution context | Unresolved | Apply G01 and existing independent Themis enforcement at protected operations. Held references and correlation metadata supply no authority. [Petasos–Mneme §4.6][petasos] · [Ergon–Mneme §4.5][ergon] · [G01 design][g01] | Resolve trusted transport and operation binding, including DR-7 and evidence for denied, unavailable and indeterminate operations. Audit transaction participation likewise requires a separate authority contract. |
| PF-11 · Derived Dokimasia.Praxis | Unresolved | Preserve independent Assurance against the applicable Praxis basis. Candidate wording keeps derivation conditional and respects the finite Assurance and self-audit constraints. [Publication PS-16][publication] · [Pragmaton §3.13][pragmaton] | Decide applicability, derivation/version correspondence, sufficient expected-versus-observed evidence, treatment of Dokimasia's own Praxis and the outstanding specialised names. |
| PF-12 · Standing, residual language and references | Editorial only | Update operative references to the exact companion drafts. Use the agreed names and realised-undertaking association. Clarify that a governed save may yield a separate durable effect while Pragmaton remains working state. Preserve predecessor links. [Pragmaton §§2, 3.1, 3.6][pragmaton] · [Publication §§3–5][publication] | Formal lifecycle, canonical correspondence and incorporation remain governed by their respective exceptions. |

The prior Pragma Management, Petasos component, Parcel and Task-family accounts retain their recorded standing. The differences in PF-02–PF-04 and PF-06 require explicit correspondence before any Continuum delta. AX-04 and AX-18 support separate functional decisions and information services. AX-05/06/09 preserve the distinction between authority, caches and working access. AX-07/08 preserve authorisation and meaningful evidence. AX-10/14/15/16 govern bounded recovery, separate outcomes and traceable progression. AX-17 preserves unanswered questions and candidate standing. [Canonical principles][axioms] · [Original authority and incorporation surfaces][basis]

## Audit investigation

Kleio owns Audit Capture and authoritative FHIR AuditEvent construction. Petasos contributes policy-relevant occurrences for its operations. Mneme contributes evidence from its information operations. The Log-Satellite receipt boundary supplies no audit durability. The proposed local Kleio integration corresponds functionally to the existing client contract. Its exact Audit.Agent/Audit.Satellite identity and per-engine allocation remain a modelling gap. [Audit candidate §§1–2][audit]

Apache's Artemis 2.33.0 documentation supports atomic parcel/occurrence sends in a local transaction when both persistent sends share a transactional session and broker resource and both durable routes are established. Scope ends at the broker and covers neither Mneme/Mnemosyne state nor the later AuditEvent write. The candidate holds staging until downstream durability and acknowledgement are established, requires audit durability parity with Petasos work, and preserves attempted, confirmed committed, confirmed failed and indeterminate outcomes. [Audit candidate §§3–4][audit]

The Solution Architect still needs to decide whether eligible Kleio-governed staging can satisfy the authoritative capture gate, its evidence/custody and outage conditions, the local identity and transaction authority, the mandatory occurrence/acknowledgement/replay contract, and required durability and recovery evidence. Until adjudicated, apply the existing Kleio capture and fail-closed requirements. [Audit candidate §5][audit] · [Canonical AX-07][ax07] · [HCRL.004 / DR-12][hcrl]

## Exact candidate revisions

- [DRP-Mneme-Pragma-Model.draft.000.004.md][pragma-file]
- [DRP-Mneme-Pragmaton-Model.draft.000.004.md][pragmaton-file]
- [DRP-Pragma-Publication-and-Subscription.draft.000.003.md][publication-file]
- [DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.000.004.md][ergon-file]
- [DRP-Interaction-Petasos-to-Mneme.draft.000.004.md][petasos-file]
- [DRP-Interaction-Petasos-to-Kleio-Audit-Capture.draft.000.001.md][audit]

[basis]: G03-Pragma-Family-Cross-Document-Reconciliation-005.md
[task]: ../before/ai-agent-tasks/unresolved-g03-pragma-family-adjudication-reconciliation.md
[hcrl]: ../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
[pragma-file]: ../../../../offline-design/DRP-Mneme-Pragma-Model.draft.000.004.md
[pragmaton-file]: ../../../../offline-design/DRP-Mneme-Pragmaton-Model.draft.000.004.md
[publication-file]: ../../../../offline-design/DRP-Pragma-Publication-and-Subscription.draft.000.003.md
[ergon-file]: ../../../../offline-design/DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.000.004.md
[petasos-file]: ../../../../offline-design/DRP-Interaction-Petasos-to-Mneme.draft.000.004.md
[pragma]: ../../../../offline-design/DRP-Mneme-Pragma-Model.draft.000.004.md#4-architectural-definition
[pragmaton]: ../../../../offline-design/DRP-Mneme-Pragmaton-Model.draft.000.004.md#4-architectural-definition
[publication]: ../../../../offline-design/DRP-Pragma-Publication-and-Subscription.draft.000.003.md#4-architectural-definition
[ergon]: ../../../../offline-design/DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.000.004.md#4-architectural-definition
[petasos]: ../../../../offline-design/DRP-Interaction-Petasos-to-Mneme.draft.000.004.md#4-architectural-definition
[audit]: ../../../../offline-design/DRP-Interaction-Petasos-to-Kleio-Audit-Capture.draft.000.001.md
[g01]: ../../../../offline-design/DRP-Ergon-Authorisation-and-Identity-Delegation.Final.001.md#4-architectural-definition
[caches]: ../../../../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-3
[axioms]: ../../../../../governance/architectural-axioms.md
[ax07]: ../../../../../continuum/motivation/principle/architectural-axiom/5de2ae2c3b794c2d9f3a14efd51d564f.md#kleio-mandatory-capture-authority
[ax18]: ../../../../../continuum/motivation/principle/architectural-axiom/b8cafe3cf5904d2bb9a9767260fddf78.md
