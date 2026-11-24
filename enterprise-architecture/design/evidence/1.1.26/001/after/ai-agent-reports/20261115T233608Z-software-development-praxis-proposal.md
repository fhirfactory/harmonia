Report-Produced-At: 2026-11-15T23:36:08.383737Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Formal Report — Software Development Praxis proposal

**Outcome: proposal prepared; stopped at Solution Architect review boundary.**
Activity: current-governance investigation, proposal and pending-HCRL preparation.
Task correspondence: [operative issued assignment](../../before/ai-agent-tasks/unresolved-software-development-praxis-proposal.md).
Review/provenance boundary: provisional Architecture **1.1.25 → 1.1.26 / 001**;
no release allocated or reserved. The emitter supplies Report-Produced-At and
the corresponding UTC filename from actual completed Report emission.

## 1. Verified originating state

Repository branch is `Mneme-and-Mnemosyne-Build`, HEAD
`441ec989a3853db4ea4dad253e14b8464181f293`; entry worktree was clean.
Root Architecture release is **1.1.25**, previous **1.1.24**. The completed 1.1.25
HCRL establishes accepted architecture with recorded qualifications. Its original
Git-handoff receipt remains historical; later local commits do not retrospectively
prove that handoff's authority or establish software acceptance. Root Maven version
is `1.0.0-SNAPSHOT`, with no local tags. No accepted software-release identity was
established by the examined sources. See [entry evidence](../../before/repository-state.json).
No fetch or remote-state claim was made.

## 2. Mechanisms examined and applicability

Loaded root AGENTS.md, architectural-axiom navigation and materially relevant
canonical Principles AX-04/08/09/14/15/17, current Architecture Governance, Review
Praxis, Change Provenance and Release Policy, the Implementation Guide, scoped
docs instructions, common HCRL template and completed 1.1.25 acceptance record.
Inspected the current evidence/release helpers, evidence tests, tooling README,
Makefile and local Git/POM release context.

Existing common governance supplies proportionate review, Solution Architect
authority, HCRL semantic decisions, corrective iterations, formal Tasks/Reports,
purpose-selected evidence, qualified deferrals and immutable accepted records.
The Implementation Guide already supplies defensive/unhappy-path, educational,
documentation and object-design standards; this proposal adds no competing standard.
The [source assessment](../governance-assessment.md) distinguishes established
mechanisms from gaps and demonstrates axiom consistency.

## 3. Proposed Praxis

The [reviewable Software Development Praxis](../../../../../review/1.1.26/software-development-praxis.proposed.md)
defines accepted baseline → bounded assignment → autonomous implementation/testing
→ exact candidate/HCRL → Solution Architect review/correction → authorised release
closure and next baseline. Ordinary engineering, tests, documentation and in-scope
review remediation are delegated; genuine architectural decisions, scope changes
and missing authority escalate. No mandatory additional reviewer agent, PR gate,
per-test approval or repeated ordinary-engineering approval is proposed.

It covers acceptance, explicit qualified acceptance/obligations, rejection,
same-increment remediation, abandonment, source rollback after acceptance and
authorised operational rollback. Failed/stalled/uncertain results retain their
meaning; source reversion does not imply durable-state rollback. Agents cannot
self-accept or infer deployment authority from source acceptance.

## 4. Genuine conflicts/gaps and already-covered matters

The principal explicit conflict is the current universal Architecture patch rule
in Release Policy, Review Praxis and AGENTS.md. It cannot provide independent
software releases without a reviewed amendment. HCRL/evidence paths currently bind
to numeric RAD targets; lifecycle finalisation assumes architecture publication.
The evidence helper currently rejects qualified software targets. These require
bounded extensions before the proposed process is operational.

Existing formal provenance, human confirmation, meaningful evidence selection,
materiality and fail-closed rules are sufficient and retained. Compilation/coverage
alone is already insufficient under the Implementation Guide. No replacement
provenance regime, software acceptance board or architectural knowledge taxonomy
is needed. No legacy implementation was used to derive architectural authority.

## 5. Exact proposed repository changes

The [prospective diff](../governance-amendments.proposed.patch) adds
`enterprise-architecture/governance/software-development-praxis.md` and amends:

- `enterprise-architecture/governance/architecture-governance.md`
- `enterprise-architecture/governance/architecture-review-praxis.md`
- `enterprise-architecture/governance/architecture-change-provenance.md`
- `enterprise-architecture/governance/architecture-release-policy.md`
- `AGENTS.md`
- `docs/AGENTS.md`
- `enterprise-architecture/governance/templates/hcrl-template.pending.md`

[Supporting amendments](../supporting-amendments.proposed.md) specify the smallest
additional tooling work in `architecture_review_evidence.py`, its existing
`tests/test_architecture_review_evidence.py` suite and `tooling/README.md` under
`enterprise-architecture/`. Extend strict target validation/emission/checks; keep
ArchiMate scaffolding Architecture-only. No tooling implementation was performed.
The Architecture allocator, schema, Continuum and software source stay unchanged.

## 6. Release and HCRL recommendations

Use independent repository software `X.Y.Z`, `software-X.Y.Z` HCRL/evidence target
keys and immutable `software/vX.Y.Z` source tags. Record accepted architectural
authority/source, exact implementation, tested scope, Maven/artefact correspondence
and qualifications in the completed software HCRL; no duplicate software JSON
release register. Prepare release-version source edits before candidate validation.

Software-only acceptance does not increment Architecture; accepted architectural
or normative governance change retains its existing allocation. Mixed transitions
are explicitly linked. Pending/corrective iterations allocate nothing. Reuse
existing HCRL Updated/Finalised with target dispatch and all common authority,
preservation and exact-confirmation controls. This governance proposal's own
adoption would require one Architecture patch under current policy, rechecking
the actual predecessor rather than treating 1.1.26 as reserved. No software release
is created by adopting the process.

## 7. Review decisions and unresolved matters

The [pending HCRL](../../../../../review/1.1.26/hcrl-1.1.26-001.pending.md) requests:

| DR | Solution Architect review point |
| --- | --- |
| DR-1 | Lifecycle, engineering delegation and independent exact-result acceptance. |
| DR-2 | Explicit release-policy amendment, software identity and target/tag conventions. |
| DR-3 | Shared evidence, correction/rejection/qualification/rollback treatment and HCRL command dispatch. |
| DR-4 | Exact bounded document amendments, helper requirements and future adoption/verification sequence. |
| DR-5 | Establish or defer the first software baseline/version without retrospective acceptance; preserve inherited standing. |
| DR-6 | Bounded supporting Minutiae and explicit treatment of unavailable Task event/prefix. |

No Solution Architect disposition is recorded. Initial software baseline/version
selection may be part of the first bounded software assignment; it need not block
review of this governance proposal. The Implementation Guide retains Draft/Pending
wording while AGENTS.md and this Task require compliance. That standing mismatch
is disclosed without rewriting the guide or inventing a separate acceptance event.

**Temporal provenance: UNRESOLVED.** The interface exposes the operative Task text
but no attributable instruction-entry system timestamp. Its event and required
UTC filename prefix are explicitly unresolved; later clock/commit/file times were
not substituted. Existing governance requires Solution Architect treatment before
closure. The Report's production event is captured by the maintained emitter.

## 8. Performed verification, limitations and resulting Git state

[Verification evidence](../verification.json) retains purpose-selected results:

- PASS — bounded offline repository architecture suite: **90 tests**, zero
  failures/errors/skips; Maven completed in 18.632 seconds.
- PASS — bounded read-only Continuum validation: **6,563 elements**, **9,427
  relationships**, navigation/identity/provenance checks passed.
- PASS — root Architecture release validation at 1.1.25 / previous 1.1.24.
- PASS — prospective diff applicability checked without applying it; proposed
  canonical local links resolved against in-memory candidates/current sources.
- PASS — proposal local links and six consecutive HCRL table/section references
  checked before Report emission; final mechanical checks complete the same boundary.
- UNRESOLVED — Task issuance event/prefix. This is not a test failure or full
  temporal compliance. The existing iteration checker is used without weakening it.

These are baseline/document mechanics, not proof that the proposed helper extension
works or acceptance of any existing source. No software-target implementation/tests,
publication, deployment or external operation was performed. No new tests were
added for this documentation proposal. Raw successful build logs remain transient;
retained summaries supply the relevant review evidence.

Resulting HEAD/branch remain the entry values; tracked diff is empty. The only
repository additions are untracked proposal/review/evidence under
`enterprise-architecture/design/review/1.1.26/` and
`enterprise-architecture/design/evidence/1.1.26/`. They are present in the worktree,
not recoverable from the unchanged HEAD. No existing source, normative document,
accepted HCRL/publication, release authority or Implementation Guide was edited.
No Calliope refactoring or unrelated implementation occurred. No release allocation,
adoption, HCRL completion/advancement, commit, tag, merge or push occurred.

The authorised proposal-preparation activity is complete. Further substantive
application awaits Solution Architect review and applicable authorisation.

## Review-driven amendment — current 1.1.26/001 proposal

Amendment-Produced-At: 2026-11-16T00:55:13.390868Z

Event source: Python system UTC clock immediately after the completed initial
Report amendment append closed; field synchronisation is provenance maintenance.
This event is not Task issuance or replacement of original Report production.

**Current outcome: corrected candidate resubmitted for Solution Architect
acceptance; proposal stage only.** This amendment continues the original assignment,
Task, Report and HCRL iteration. Initial submission statements above remain history;
the recorded review dispositions and corrected candidate now supply current standing.
The original Report-Produced-At and filename are unchanged. The amendment's
completed-emission event is recorded separately below by mechanical synchronisation.

### Review authority and preserved history

The [existing Task](../../before/ai-agent-tasks/unresolved-software-development-praxis-proposal.md)
now retains the exact Solution Architect directions and two clarifications: the
Task 9 sample is not future Backlog structure/content authority, and Backlog Items
must have the specified minimum sections. DR-1 is Changes Requested; DR-2/DR-3
Accepted with refinement; DR-4 Revise and resubmit; DR-5 Accepted; DR-6 Accepted with
qualification. Those dispositions remain in the same HCRL, rather than being
replaced with agent completion statuses. No HCRL advance was requested or performed.

Seven [reviewed inputs](../../before/solution-architect-review/source-correspondence.json)
are retained byte-for-byte as compressed snapshots, including the original HCRL,
Praxis, assessment, diff, supporting amendments and Task/Report states. The original
Task/Report are unchanged prefixes of their amended records. Initial verification
evidence remains unchanged and is not relabelled as current execution.

### Proposal changes and Backlog structure

The [revised Praxis](../../../../../review/1.1.26/software-development-praxis.proposed.md)
makes `docs/backlog/` the single human/agent Backlog location. Items are work-management
artefacts, not Continuum/Motivation elements. They may realise accepted architecture,
capture justified technical work or propose architectural changes under existing
governance, ADD and HCRL. Their existence supplies no implementation authority.

Inspection found only the six-line Task 9 sample, with no README or template. It
remains unchanged historical work material. The proposed
[Backlog Item template](../../../../../review/1.1.26/backlog-item-template.proposed.md)
uses the Solution Architect's minimum: Overview with synopses of Effort, Activities,
Outcome and Acceptance Criteria; Context; Associated Requirements; Expected /
Associated Activity; Expected Outcomes; Acceptance Criteria. “Critera” in the issued
list is normalised to “Criteria” without changing meaning. Scope, constraints,
source/revision and traceability references sit in Context. The Overview summarises
the detailed specification rather than duplicating it.

Task Details reference originating items/revisions and retain execution-specific
instructions. Implementation Reports and HCRLs identify those items and link
acceptance-criteria, conformance and test results. Many-to-many Continuum ↔ Backlog
→ Task → Implementation/Report → HCRL → Accepted Software Release correspondence
uses existing references, without a new catalogue, identity scheme or artificial
cardinality. Ordinary authorised corrections retain engineering autonomy.

### Solution-wide verification and release semantics

Independent software `X.Y.Z` remains solution-wide. Component Maven/build versions
do not establish accepted Harmonia baselines. Every software PR/HCRL submission
represents the integrated solution, even if its assignment changes few modules.
Before submission the agent must perform applicable component/unit, solution-wide
regression, solution-wide integration and relevant architectural/implementation-
conformance verification against the exact candidate. Record actual commands,
source, scope, environment, outcomes, material failures and unavailable/unexecuted
checks. Incomplete required testing needs an exception and explicit Solution
Architect disposition; an exception request is not a successful readiness claim.

No change to every module is required. No Maven phase name or module test pass is
treated as proof of integration coverage. Immutable release identification, exact
Git-state correspondence, qualified/rejected/abandoned/rollback treatment and
same-increment remediation/retesting are preserved. No new approval is required
for ordinary in-scope review corrections.

### Updated amendment/tooling scope

The [revised exact unapplied diff](../governance-amendments.proposed.patch) covers
ten documents: the same seven current governance/instruction/template amendments,
the Praxis addition, and the proposed `docs/backlog/README.md` and
`docs/backlog/backlog-item-template.md` additions. The
[supporting scope](../supporting-amendments.proposed.md) identifies every path.
The same bounded future evidence-helper, existing helper-test and tooling-README
work remains necessary for software-qualified targets. No helper implementation,
new approval/test platform, backlog service, lifecycle CLI or separate evidence
model was introduced. No existing backlog, normative or implementation source changed.

### Validation and remaining decisions

[Correction verification](../review-correction/verification.json) records:

- PASS — mandatory architecture suite: **90 tests**, zero failures/errors/skips,
  completed in **19.194 seconds**.
- PASS — template minimum hierarchy, current/prospective local links, exact diff
  applicability/whitespace, six DR/disposition correspondence, reviewed-input
  integrity, original Task/Report prefix and historical-sample preservation.
- PASS — unchanged tracked sources, HEAD, root 1.1.25 / previous 1.1.24 and absence
  of installed normative additions. Proposed helper support is not claimed operational.
- UNRESOLVED — factual original Task time/prefix, now explicitly accepted with
  qualification in DR-6. No historical time was manufactured. Review/follow-up
  instruction-entry times are separately unavailable and retained as such; they
  are not inferred from the original exception or amendment-production event.

This governance correction is not a software acceptance candidate. Solution-wide
software regression/integration readiness was not executed or claimed here. The
software-candidate requirement is explicit in the revised text. The initial
Continuum validation PASS remains prior evidence; unchanged canonical sources
provided no reason to repeat it.

No new material authority conflict was found. The revised exact candidate awaits
Solution Architect acceptance under DR-1/DR-4. DR-5's first-baseline/version deferral
and DR-6's original temporal treatment are recorded decisions, not repeat requests.
The universal Architecture-patch rule remains current until the reviewed separation
is actually adopted/applied. Existing architectural qualifications keep their scope.

Final branch/HEAD remain `Mneme-and-Mnemosyne-Build` /
`441ec989a3853db4ea4dad253e14b8464181f293`; only the existing 1.1.26 proposal/evidence
worktree additions are present and uncommitted. No normative adoption, software
implementation, Calliope refactoring, HCRL completion/advancement, release allocation,
commit, tag, merge, push, publication or deployment occurred. Mechanical final
verification completes this same amendment without another record or review cycle.

## Final Backlog refinements — corrected candidate for final review

Amendment-Produced-At: 2026-11-16T01:02:52.058076Z

Event source: Python system UTC clock immediately after the completed initial
Report amendment append closed; field synchronization is provenance maintenance.
This preserves original Report production and is not an instruction-entry time.

The Solution Architect's latest directions, retained in the same Task, confirm
substantive acceptance in principle and authorise only minor identity/revision/link
corrections. This continues HCRL 1.1.26/001 without a new assignment, Report identity,
iteration or review framework. All original Report event fields and prior amendments
remain unchanged; the completed emission of this amendment is recorded separately.

**Corrections incorporated:** the item title identifies the item, with explicit
**Backlog Item ID** and **Backlog Item Revision** header metadata. The existing
identifier is retained without an allocator or prescribed revision format. The
Praxis requires Task Details, Reports and HCRLs to reference both values and exact
source content rather than duplicate specifications. All six minimum sections
and four Overview synopses remain unchanged.

The maintained README text links to `backlog-item-template.md` and
`../../enterprise-architecture/governance/software-development-praxis.md`, resolving
from `docs/backlog/README.md` to the required incorporation targets. The existing
rebase already supplied those valid destinations; verification confirmed no
proposal-only link remains in maintained README text. Review-copy links remain
usable while the maintained targets are still unincorporated.

**Affected current proposal files:**

- `enterprise-architecture/design/review/1.1.26/backlog-item-template.proposed.md`
- `enterprise-architecture/design/review/1.1.26/backlog-readme.proposed.md`
- `enterprise-architecture/design/review/1.1.26/software-development-praxis.proposed.md`
- `enterprise-architecture/design/review/1.1.26/hcrl-1.1.26-001.pending.md`

Within this iteration, `after/governance-amendments.proposed.patch` and
`after/supporting-amendments.proposed.md` were reconciled; the existing formal
Task/Report were appended. Eight pre-correction reviewed inputs are retained with
[source correspondence](../../before/final-backlog-refinements/source-correspondence.json).
The prospective target set remains ten documents plus the previously identified
future helper/test/README tooling scope; no new target or machinery was added.

**Validation:** [final-refinement evidence](../final-backlog-refinements/verification.json)
records PASS for the accepted section hierarchy, metadata and title, maintained
link resolution against the prospective target set, review/patch text correspondence,
patch applicability/whitespace, identical prior disposition table, preserved reviewed
inputs and original formal-record prefixes. The mandatory architecture suite passed
**90 tests**, zero failures/errors/skips, in **18.673 seconds**. Root Architecture
release remains 1.1.25 / previous 1.1.24; tracked sources and HEAD are unchanged.
Static checks supply no human acceptance or software-readiness claim.

The evidence checker remains factually UNRESOLVED for the original Task event/prefix,
with its prior accepted qualification preserved. The latest instruction-entry time
is separately unavailable and not inferred. No new material acceptance concern was
found. The corrected candidate is presented for final Solution Architect review;
accepted-in-principle semantics and all six earlier dispositions are not reopened.
No normative amendment was applied, HCRL finalised, release allocated, commit/push
performed or software implementation commenced. Final mechanical checks complete
this same amendment without another provenance or acceptance cycle.
