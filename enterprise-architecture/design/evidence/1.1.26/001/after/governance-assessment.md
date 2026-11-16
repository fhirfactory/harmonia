<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Governance assessment and source correspondence

**History note:** the initial assessment below is retained. The final review-correction
section records subsequent Solution Architect decisions and supersedes its original
“Outstanding review choices” standing; it does not alter current normative authority.

**Activity:** proposal preparation under current governance, not adoption.
Baseline/source facts are retained in [entry evidence](../before/repository-state.json).
Review target: proposed Architecture 1.1.25 → 1.1.26 / 001, not a reservation.
The [Praxis](../../../../review/1.1.26/software-development-praxis.proposed.md)
and [amendments](supporting-amendments.proposed.md) contain the recommended result.

## Current mechanisms and actual gaps

| Examined source / mechanism | Established applicability | Gap or required treatment |
| --- | --- | --- |
| Root [AGENTS.md](../../../../../../AGENTS.md), §§1.1–1.5, lifecycle commands, §§4–6 and implementation standards | Fresh authority loading, proportional review, fail-closed escalation, bounded commands, mandatory architecture checks, formal evidence and Implementation Guide compliance apply. | §1.3 mandates Architecture increments for every accepted change. Lifecycle command finalisation explicitly publishes architecture. Those live requirements need amendment before software-only release operation. |
| [Architecture Governance](../../../../../governance/architecture-governance.md), decision authority, review coverage, HCRL, completion and release sections | Every change receives review; engineering detail is distinguished from material discoveries. Solution Architect confirmation, one DR table, bounded Minutiae, immutable completion and qualified deferrals already exist. | HCRL target and resulting-state semantics are RAD-only. Extend their scope and target naming; do not invent a software approval board or second final acceptance record. |
| [Review Praxis](../../../../../governance/architecture-review-praxis.md), §§1–6 | Establish authority, prepare, adjudicate, apply, report/verify and confirm/complete already provide the common lifecycle. An assignment can delegate work, and routine evidence maintenance creates no recursive activity. | An operational software specialisation must state milestones, ordinary engineering delegation, exact candidate acceptance and software closure. Current §4 unconditionally prepares the Architecture patch. |
| [Change Provenance](../../../../../governance/architecture-change-provenance.md), evidence selection, AI-agent activity and closure | Same iteration-scoped Task/Report model already covers investigation, corrective work, application and validation irrespective of materiality. Timestamp, source-recovery and working-material rules are sufficient. | Current paths explicitly use resulting RAD version independently of software version. Amend target selection prospectively; no extra per-test record, universal archive or retrospective migration is needed. |
| [Release Policy](../../../../../governance/architecture-release-policy.md), authority/scope and mechanism | Root release file, strict expected-current patch allocation, independent Maven versions, no allocation for pending iterations or maintenance already exist. | Explicit conflict with the assignment's prospective objective: every accepted repository change currently increments Architecture. Only an adopted amendment can separate software allocation. No exception is applied now. |
| [HCRL template](../../../../../governance/templates/hcrl-template.pending.md) and current completed [1.1.25 HCRL](../../../../register/1.1.25/hcrl-1.1.25-001.complete.md) | A single semantic record can retain exact-result acceptance with explicit FAIL/TIMEOUT/UNRESOLVED qualifications and exclusions. Existing human dispositions remain binding. | Completed Architecture acceptance does not accept the subsequent implementation snapshots or waive future software tests. The older closure receipt's pending Git handoff is not rewritten from later commit presence. |
| [Implementation Guide](../../../../../../docs/implementation/Harmonia-Implementation-Guide-Philosophy.md), §§2–5, 7–12 | Functional and defensive correctness, 95/5 scenarios, education, documentation, object/package design and the 500-line exception already apply through current AGENTS.md and this Task. | No new engineering standard or numeric coverage target is needed. Draft/Pending wording conflicts with its presentation as applicable in AGENTS.md; preserve the wording and explicit current compliance obligation, without inferring separate formal adoption. |
| [EA tooling README](../../../../../tooling/README.md), `architecture_review_evidence.py`, `architecture_release.py`, Makefile and existing evidence tests | Existing evidence emitter captures actual Report emission, preserves missing Task events, validates numeric target layout and events; Architecture allocator stays separate from generation. | Evidence helper rejects software-qualified keys. Small path-validation/test/documentation extension is needed; its ArchiMate scaffold must remain Architecture-only. No new release engine is necessary. |
| [docs instructions](../../../../../../docs/AGENTS.md), root POM and local Git history/tags | Scoped docs inherit common rules; POM is `1.0.0-SNAPSHOT`; branch/commits identify sources. | Align inherited evidence path instructions when adopting. No accepted software version/baseline is established by these examined sources; no local tags exist. A first-baseline decision belongs to a future bounded assignment. |

Current governance already covers attributable decisions, explicit deferrals,
material discovery escalation, proportional review and meaningful evidence. The
new work is the software lifecycle specialisation, release-policy separation and
small target-name extension. Backlog, PR, CI, packaging and deployment mechanisms
can support that lifecycle without becoming independent acceptance authorities.
The finite Report-only path does not exempt repository changes from mandatory review.

## Materially relevant axioms and consistency

The canonical definitions below were read; the common governance contracts already
apply these axioms to governance. This proposal changes no runtime allocation.

| Canonical Principle | Application to proposal |
| --- | --- |
| [AX-04](../../../../../continuum/motivation/principle/54508cda80484a6caf95b901780eaf7d.md) | Reuse Git, current HCRL and evidence helpers; no separate governance platform. Machinery never supplies acceptance. |
| [AX-08](../../../../../continuum/motivation/principle/21d6864657964ba5b03e3f6b5eb29cd5.md), [AX-09](../../../../../continuum/motivation/principle/9a5cb9e8876844e58e660f909f7df9fc.md) | Retain meaningful decisions, formal activity evidence and useful test/conformance results; keep transient debug/build work purpose-bounded under existing provenance rules. |
| [AX-14](../../../../../continuum/motivation/principle/d3fc6025dc7646d1b2020b51f7a44bfa.md) | Separate architecture/software identities, authorisation/acceptance, testing/deployment and accepted/qualified/rejected/abandoned outcomes. |
| [AX-15](../../../../../continuum/motivation/principle/ef8abbaec37e4890ad78b9f263e67906.md) | Preserve TIMEOUT/STALLED/UNRESOLVED and uncertain rollback or durable effects. |
| [AX-17](../../../../../continuum/motivation/principle/architectural-axiom/8882573df7ce43c6bc0f66da0a676139.md) | Proposal stays non-authoritative; current conflicting release rule stays operative until adjudicated. Do not infer accepted software baseline, missing timestamps or architectural authority. |

No implementation architecture conflict was adjudicated or repaired. Existing
source was inspected for repository/release context only, not audited for software
acceptance. Future architecture/code disagreements return through HCRL; no Continuum
element is required merely to represent governance knowledge.

Mechanically enforceable parts include strict target namespaces, iteration layout,
timestamp-prefix correspondence, immutable source/tag correspondence and bounded
test execution. Human judgement remains necessary for scope, documentation quality,
conformance, residual-risk treatment and exact-result acceptance.

## Outstanding review choices

- Adopt/revise the autonomous lifecycle, release-policy separation and proposed
  software target/tag conventions through this HCRL; all remain unapproved.
- Authorise/revise the bounded amendments and helper work if adoption is desired.
  Explicit application and final confirmation remain future lifecycle stages.
- Establish the first accepted software predecessor/source and initial software
  version before using the software process, or defer that decision to the first
  assignment with explicit no-predecessor treatment. Do not select one now from
  snapshots or Maven numbering.
- Treat this Task's unavailable instruction-entry timestamp/prefix explicitly
  before HCRL closure. Proposal preparation does not claim temporal completeness.

The Implementation Guide header mismatch is disclosed; there is no uncertainty
about the current instruction to comply with its engineering standards. No new
permission gate is proposed for that obligation.

## Review correction — current disposition and source correspondence

This continues 1.1.26/001. The [existing Task](../before/ai-agent-tasks/unresolved-software-development-praxis-proposal.md)
retains the Solution Architect review and both subsequent Backlog clarifications.
[Reviewed-input correspondence](../before/solution-architect-review/source-correspondence.json)
preserves the original candidate/analysis and Task/Report bytes. No new assignment,
iteration, release or independent review framework has been created.

Inspection found only the six-line Task 9 Backlog sample, with a Mneme group,
BL-09-01 heading and short objective text; no README or template. The Solution
Architect explicitly disclaims it as future structure/content authority. It is
preserved unchanged. The requested minimum comes from the subsequent issued list,
not that sample: Overview with four high-level synopses (Effort, Activities, Outcome,
Acceptance Criteria), Context, Associated Requirements, Expected / Associated
Activity, Expected Outcomes and Acceptance Criteria. The
[proposed template](../../../../review/1.1.26/backlog-item-template.proposed.md)
uses that hierarchy, with source/scope/constraints/traceability in Context.

| DR | Recorded treatment and revised proposal correspondence |
| --- | --- |
| DR-1 — Changes Requested | Praxis §§3–5 and proposed Backlog template/README: single docs/backlog location; work-management semantics; governed architecture proposals; referenced specifications and many-to-many traceability. Items do not authorise execution or become Motivation elements. |
| DR-2 — Accepted with refinement | Praxis §§2, 4, 6: independent solution-wide X.Y.Z and integrated candidate; component/unit plus solution-wide regression/integration and relevant conformance before acceptance submission; explicit incomplete-verification exception/disposition. |
| DR-3 — Accepted with refinement | Praxis §§3, 6–8 and common provenance/template amendments: originating item/revision in Reports/HCRLs, criteria/conformance/solution-verification evidence, same-increment corrections and appropriate retesting without routine reapproval. |
| DR-4 — Revise and resubmit | Exact diff now includes ten document targets (seven existing plus three additions); same helper/test/README tooling scope. No normative application or new machinery. |
| DR-5 — Accepted | First software baseline/version deferred to the first appropriately authorised assignment, without retrospective source acceptance. No repeat decision requested. |
| DR-6 — Accepted with qualification | Original Task issuance/prefix exception preserved, evidence retained; no fabricated time. Follow-up instruction-entry times are separately unavailable and retained as such. |

The [current HCRL](../../../../review/1.1.26/hcrl-1.1.26-001.pending.md) is the semantic
record. Revised text awaits exact-candidate acceptance; accepted decisions above
are not relabelled unanswered. Initial first-baseline and original-time treatment
questions have received the recorded dispositions. No new material authority
conflict was found. The universal Architecture-patch rule remains current until
the already-reviewed separation is actually adopted/applied. No software-readiness
claim is made by this governance correction; future integrated regression/integration
commands, topology and environment remain actual assignment/verification obligations.

The same axiom assessment applies: use existing machinery (AX-04), purposeful
evidence (AX-08/09), separate work/authority/acceptance and component/solution facts
(AX-14), preserve verification uncertainty (AX-15) and explicit architectural
authority (AX-17). No Backlog/Continuum identity equivalence or mandatory cardinality
is introduced. Architecture Authoring Praxis and established ADD/HCRL controls
continue to govern resulting architecture changes; no new architecture process
is inferred from the backlog location.
