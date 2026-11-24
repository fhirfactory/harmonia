<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | AI-agent task and report locations within transition evidence | Accept with Clarification | Accepted with clarification. before/ai-agent-tasks/ and after/ai-agent-reports/ are established as the governed locations for AI-agent Task Details and corresponding Task Reports for work performed as part of a governed architecture transition. Such Task Details and Task Reports SHALL be retained as execution provenance; retention of the formal Task/Report does not require retention of every prompt, conversation, tool invocation, working log or other agent interaction. Their architectural significance and treatment remain subject to the existing materiality, evidence and Minutiae rules. The before and after locations represent task-input and execution-result roles respectively and do not assert that the contained artefact existed in the predecessor or successor Git state. |
| [DR-2](#dr-2) | Task/report content, correspondence and architectural standing | Accept with Clarification | Accepted with clarification. For AI-agent work performed as part of a governed architecture transition, the formal Task Detail and corresponding Task Report are required execution provenance in accordance with DR-1 rather than optionally selected evidence. The Task SHALL preserve the operative issued instruction and sufficient attributable context to establish what work was authorised or requested. The Report SHALL preserve sufficient factual execution, outcome, verification, limitation and correspondence information to establish what occurred. Task-to-report correspondence SHALL be adequate to reconstruct the governed transition but need not impose one-to-one file pairing. Task/Report evidence does not constitute an alternative architectural decision record: material architectural content SHALL be surfaced in the applicable HCRL Decision Review, and neither an agent report nor its location confers architectural authority. |
| [DR-3](#dr-3) | Architecture Review Praxis and use of task/report evidence | Accept with Clarification | Accepted with clarification. Establish enterprise-architecture/governance/architecture-review-praxis.md as the common operational Architecture Review Praxis. The Praxis applies to every Harmonia change. The extent of review activity and evidence SHALL be proportionate to the scope, complexity, uncertainty and architectural materiality identified through review; classification as implementation, bug, patch, backlog item or similar does not itself reduce or bypass the Praxis. For AI-agent work performed as part of the governed transition, formal Task Details and corresponding Task Reports SHALL be retained as execution provenance in accordance with DR-1 and DR-2 rather than optionally selected on the basis of architectural materiality. The proposed workflow—establish task/state, prepare HCRL, adjudicate, apply, report/verify, confirm/complete—is accepted. Existing specialised review mechanisms retain their specialised semantics and feed the common HCRL/Praxis rather than being reinterpreted or replaced. |
| [DR-4](#dr-4) | Bounded application, prospective adoption and verification | Accept with Clarification | Accepted with clarification. Apply only the bounded source-to-result mapping identified in DR-4, incorporating the accepted clarifications from DR-1, DR-2 and DR-3. For this transition, the formal AI-agent Task Detail(s) and corresponding Task Report(s) SHALL be retained in the established before/ai-agent-tasks/ and after/ai-agent-reports/ locations respectively, making RAD-1.1.3 the first prospective use of the convention. The application SHALL NOT expand into repository migration, retrospective reconstruction, historical cleanup, erratum machinery, production code, ArchiMate model, publication or other machinery changes. Newly discovered material scope returns for Architecture Review. Verification establishes factual conformance of the incorporated result; final architectural confirmation remains with the Solution Architect. |
| [DR-5](#dr-5) | Bounded Minutiae for candidate preparation | Accept | Accepted as correctly classified bounded preparation and review provenance for the RAD-1.1.3 candidate. No additional material architectural or governance matter is identified within the disclosed Minutiae group. |

# HCRL — AI-agent task/report evidence and Architecture Review Praxis

**Standing and review basis.** Candidate iteration **001**, prepared by Codex on
2026-10-21 (Australia/Sydney), for **RAD-1.1.2 → RAD-1.1.3**. This is a proposal
for Solution Architect adjudication. All dispositions are unrecorded. The user's
instruction authorises candidate preparation; it supplies neither acceptance of
the proposed rules nor an instruction to apply them. No new governance contract,
evidence subtree, release increment or completed HCRL is installed by this draft.

**Originating state and exact inputs.** The repository was clean at
`6684a60eb62472769fc9436bf3acb9e32552948b` before preparation. Root
[architecture-release.json][release-json] records `version=1.1.2` and
`previous_version=1.1.1`. During preparation, HEAD advanced to
`94223edf4e39f7ce50f4414cb6d82b89597339db`. Git comparison established identical
tracked trees at these commits; the reviewed sources and governed predecessor
remain unchanged. The user supplied
[RAD-1.1.2 pending iteration 003][supplied-pending] and requested:

> Prepare the RAD-1.1.3 candidate HCRL to establish before/ai-agent-tasks/ and after/ai-agent-reports/ within transition evidence and incorporate their use into the Architecture Review Praxis.

The predecessor's [completed iteration 003][predecessor] records Mark Hunter's
2026-10-21 confirmation, “result confirmed - complete”. It establishes the
accepted predecessor standing; the supplied pending record's earlier statements
about pending confirmation and uncommitted application describe its preparation
state. Neither predecessor record is amended. Their DR references and accepted
dispositions belong to that transition and do not decide this candidate's DRs.

This candidate applies the maintained [Architecture Governance][governance],
[Architecture Change Provenance][provenance], [HCRL template][template],
[release policy][release-policy] and root [agent instructions][agents]. The
resulting candidate is retained at this working path; it is not claimed to exist
in the originating commit. Recheck the actual immediate governed predecessor
before acceptance or application; baseline drift requires review of applicability.

**Axiom consistency and existing mechanisms.** [AX-04][ax-04] is preserved by
using existing repository files, Git recovery and HCRL rather than introducing a
new recorder or approval service. [AX-08][ax-08] and [AX-09][ax-09] support
purpose-selected task/report evidence and bounded retention of processing
material. [AX-14][ax-14] keeps task authority, agent interpretation, decisions,
execution results and architectural acceptance distinct. [AX-15][ax-15] preserves
uncertain execution effects; [AX-17][ax-17] preserves proposed rules and missing
authority until adjudicated. No conflict with these axioms or the current common
contracts was identified in this bounded documentation review. Current contracts
recognise Task Details/Reports but do not specify these subdirectories or a
standalone common Architecture Review Praxis. This is a proposed governance
extension, not a finding about production-code conformance. Paths, links and
review structure can be checked mechanically; evidence relevance, materiality and
architectural acceptance remain human judgements. No runtime responsibility,
module boundary, modelling semantics or architectural identity changes.

<a id="dr-1"></a>
## DR-1 — AI-agent task and report locations within transition evidence

**Established basis.** The common governance contract's **Review coverage and
candidate judgement** recognises Task Details and Task Reports for significant
collaborative work as useful transition provenance, normally minutiae unless
materially architectural. The provenance contract's **Selecting required evidence**
places required evidence beneath the resulting release's `before/` and `after/`
directories, selected for understanding, verification or preservation. It rejects
historical completeness, duplicate Git history and compulsory archive packages.
The proposed locations refine that established arrangement.

**Proposed incorporation.** Establish the following locations in the common
governance layout and provenance contract:

```text
enterprise-architecture/design/evidence/<resulting-version>/
├── before/
│   └── ai-agent-tasks/
└── after/
    └── ai-agent-reports/
```

`before/ai-agent-tasks/` holds selected Task Details issued to AI agents for work
contributing to the governed transition. `after/ai-agent-reports/` holds selected
Task Reports describing that work's actual results and verification. The version
is the transition's resulting RAD release, independent of agent, session,
pending-HCRL iteration, software version or processing-run identity.

These are input/output evidence roles. A follow-up task issued during review or
application still belongs with task inputs, and an interim or unsuccessful report
may be useful result evidence. Placement under `before/` does not claim that a
task existed in the predecessor Git tree; placement under `after/` does not
establish successful execution or acceptance of a report's contents.

Apply the existing evidence-selection and recovery rules. Significant AI-agent
work SHALL be assessed for useful task/report provenance. Retain the selected
content or identify an adequately recoverable repository-held version without
unnecessary duplication. Where required evidence is not reliably recoverable,
retain the necessary content itself in the applicable location. No directory
placeholder, empty task/report pair, universal archive, hash or transcript is
required. Other required before/after evidence remains permitted in its
appropriate location; these subdirectories do not exhaust transition evidence.

**Decision sought and consequences.** Accept or clarify these locations and their
selection semantics. This gives selected task/report evidence a common home
across AI agents without making every prompt, tool invocation, session or log a
permanent architectural record. DR-2 governs content and standing; DR-3 governs
use; DR-4 bounds prospective application. No existing evidence is moved now.

<a id="dr-2"></a>
## DR-2 — Task/report content, correspondence and architectural standing

**Proposed incorporation.** Selected Task Details SHALL preserve the operative
issued instruction, attributable issuer/date where established, scope,
constraints and relevant source/review references sufficiently to understand the
work requested. Preserve material corrections or follow-up instructions and
their relationship to the earlier instruction. An agent's reconstructed summary
does not replace available issued wording. Unknown attribution or missing source
content remains explicit; no reconstruction may manufacture it.

Selected Task Reports SHALL identify the corresponding task or tasks and state
what was actually done, affected source/result paths or adequate Git locators,
verification conclusions and material limits, unresolved effects and relevant
departures from the requested scope. Distinguish proposed, performed and
incorporated work, and distinguish PASS, FAIL, TIMEOUT, STALLED and UNRESOLVED.
A report may cover several related tasks or several reports may contribute to
one task; preserve correspondence adequate for the transition rather than impose
a one-to-one file pairing.

Use readable repository-held records with descriptive local filenames and
references adequate to identify the task/report and relevant HCRL DR. Markdown
is suitable; this proposal adds no universal content schema, canonical ID,
timestamp-derived identity or fixed agent-specific filename convention. Preserve
source identity when referencing an existing record. If a selected source is
copied, record its source/result correspondence and purpose rather than imply
that the copy is the original or a new architectural decision.

Task/report evidence normally supports the bounded Minutiae group. Material
architectural content SHALL be surfaced in the applicable substantive DR for
adjudication, retaining relevant task/report references. Material decisions,
scope changes, application facts and validation conclusions SHALL survive in the
applicable HCRL DR; a linked report does not replace that semantic record.
An attributable Solution Architect decision contained in a task retains its
actual authority and conditions. An agent's report, interpretation, filename or
directory placement supplies no independent architectural authority. Do not
classify substantive decision content as accepted minutiae merely because it
appears in a task or report.

The provenance contract continues to govern retention and recovery: preserve
selected evidence and correspondence needed for the transition; retain working
material while required for active review, recovery, validation or unresolved
dependencies. Routine execution logs, full conversations, frozen machinery and
implementation-source copies are not accumulated merely because an agent used
them. Pending iterations retain their existing treatment; task/report placement
creates no new disposal authority.

**Decision sought and consequences.** Accept or clarify this minimum faithful
content and correspondence treatment. It makes the new locations useful without
turning task/report evidence into a competing decision log, an automatic
acceptance mechanism or a duplicate source-history archive. Any new material
meaning discovered during implementation returns through HCRL before dependent
application proceeds.

<a id="dr-3"></a>
## DR-3 — Architecture Review Praxis and use of task/report evidence

**Observed gap and proposed location.** The common governance contract already
defines Architecture Review, but no standalone **Architecture Review Praxis** was
found in the maintained common governance sources or existing specialised Praxis
index. The [Authoring Praxis][authoring], [Round-Trip Praxis][roundtrip] and
[ArchiMate Change Review contract][specialised-review] retain their specialised
purposes. Do not silently reinterpret one of them as the common Praxis.

Propose a new maintained companion at
`enterprise-architecture/governance/architecture-review-praxis.md`, linked from
the common governance entry and contract. It operationalises the existing common
rules and DR-1/DR-2 evidence treatment. The governance and provenance contracts
remain controlling in their respective roles. The new Praxis is governance
knowledge, requiring no continuum entry, ArchiMate element or new review service.

**Proposed Praxis workflow.** Incorporate the following sequence, applicable to
every Harmonia change with effort proportionate to its architectural effect:

1. **Establish the task and originating state.** Identify the actual immediate
   governed release, source state, operative instruction and applicable authority.
   Load the minimum sufficient repository-held authority, expanding through
   dependencies as needed. Identify relevant axioms and constraints. Select
   useful AI-agent Task Details and retain or locate them under DR-1/DR-2. A task
   to investigate or prepare a candidate does not itself approve a change.
2. **Prepare the candidate HCRL.** Use the resulting release's review directory,
   the common opening Decision Review table and corresponding DR sections.
   Describe material proposals, evidence, uncertainties, consequences and
   dependencies in the applicable DR; disclose supporting/minor task/report
   provenance as a bounded Minutiae group. Reference selected task evidence and
   available investigation reports. An initial candidate need not have a report
   for application that has not occurred. Pending iterations consume no release.
3. **Obtain and preserve adjudication.** Mark Hunter, Solution Architect, decides
   the reviewed matters; ARB/peer participation retains its existing role. Record
   attributable dispositions/comments, clarifications, conditions, exclusions,
   deferrals and material revision history. Use the bounded Minutiae control.
   Unanswered fields, agent recommendations and tool success supply no approval.
4. **Apply the authorised scope.** Read the exact reviewed HCRL and applicable
   application instruction. Recheck source/baseline applicability and apply only
   unambiguous authorised directives. Select material follow-up task instructions
   as task evidence. Surface new architectural discoveries or conflicting
   instructions in the applicable DR for adjudication before dependent work;
   continue independent authorised work where possible.
5. **Report and verify the incorporated result.** Record selected Task Reports
   under `after/ai-agent-reports/`, or adequate recovery references as allowed by
   the provenance contract. Relate them to their tasks and DRs. Record actual
   application, source/result correspondence, validation conclusions, limits and
   unresolved effects in the applicable DR. Keep automated verification, human
   review, publication and actual inspection/transport outcomes distinct.
6. **Confirm and complete the transition.** Present the authorised change and
   actual result for Solution Architect confirmation. Complete the HCRL in the
   resulting release's register only after that confirmation, preserving explicit
   deferrals/exclusions and completed-record immutability. Verify that required
   task/report evidence remains retrievable. Apply the single accepted-change
   patch increment under the existing release policy; further pending iterations
   and completion within that change set do not allocate further releases.

The Praxis SHALL preserve specialised review inputs, triggers, dispositions,
outcome semantics, exact-input checkpoints, identity/native-type rules, View
preservation and applicable validation/publication gates. Specialised review
continues to contribute relevant material into HCRL. Task/report evidence neither
replaces those checkpoints nor creates a competing final acceptance record.

**Decision sought and dependency.** Accept or clarify the common Praxis location
and this workflow, subject to accepted DR-1/DR-2 wording. The new document has not
been installed. If another intended common Praxis source is identified during
review, resolve its correspondence and scope here before application rather
than infer a relocation or replacement from the shared word “Praxis”.

<a id="dr-4"></a>
## DR-4 — Bounded application, prospective adoption and verification

**Proposed application boundary.** Subject to the actual Solution Architect
dispositions and application instruction, apply only this bounded mapping:

| Source / result path | Proposed operation and permitted purpose |
| --- | --- |
| `enterprise-architecture/governance/architecture-governance.md` | Amend in place: add the two subdirectories to the target layout, connect task/report standing to DR-1/DR-2 and link the common Praxis. Preserve decision authority and lifecycle rules. |
| `enterprise-architecture/governance/architecture-change-provenance.md` | Amend in place: specify selected task/report locations, faithful content/correspondence and recovery treatment without reinstating mandatory archives or duplicate Git history. |
| `enterprise-architecture/governance/architecture-review-praxis.md` | Add the common operational Praxis from accepted DR-3, referencing the controlling common contracts. |
| `enterprise-architecture/governance/README.md` | Amend in place: add Praxis navigation and the accepted transition reference. |
| `enterprise-architecture/governance/templates/hcrl-template.pending.md` | Amend in place: guide task/report evidence references in the applicable DR and bounded Minutiae disclosure; preserve the single decision structure and unaccepted placeholders. |
| `AGENTS.md` | Amend in place: connect architecture-task entry to the common Praxis and selected task/report locations without replacing authority/provenance rules or `.junie` historical treatment. |
| `docs/AGENTS.md` | Amend in place: add the same common Praxis/evidence entry to documentation work; preserve RADS, legacy preservation and specialised review requirements. |
| `docs/markdown/governance/README.md` | Amend in place: add a navigation link to the common Praxis; leave the specialised contracts at their current paths. |
| `docs/markdown/governance/architecture-release-policy.md` | Add the accepted RAD-1.1.2 → RAD-1.1.3 governance transition and its authority/confirmation standing; preserve the patch mechanism and historical decisions. |
| `architecture-release.json` | Use the existing expected-current patch mechanism once for this accepted change set, after verifying applicability: resulting `1.1.3`, predecessor `1.1.2`. |
| `enterprise-architecture/design/evidence/1.1.3/before/ai-agent-tasks/` and `after/ai-agent-reports/` | Add selected records for this transition when authorised, with actual input/result correspondence. Do not create empty placeholders or backfill historical transitions. |
| `enterprise-architecture/design/review/1.1.3/` and `register/1.1.3/` | Preserve reviewed decisions in subsequent pending application facts; add the completed HCRL only after Solution Architect confirmation. |

All existing maintained sources above remain at the same paths; the common Praxis
and selected evidence are additions. This mapping is prospective for this
transition and subsequent authorised work. Existing `.junie` plans/reports,
predecessor HCRLs/evidence and historical processing remain at their current
paths. Useful existing records may be referenced with adequate recovery locators;
this proposal authorises no blanket migration, retrospective reconstruction,
cleanup, deferred erratum machinery or investigation of the excluded historical
1.0.10 → 1.1.0 pilot. No production code, ArchiMate model, publication or machinery
change is proposed. New material scope or another affected consumer returns for
review rather than silently expanding this mapping.

**Proposed verification and closure.** At application, verify the exact reviewed
directives against the resulting path/content inventory; check source/result
correspondence, local links/anchors, HCRL structure, task/report retrieval and
classification, preservation of predecessor decisions and specialised semantics,
release metadata and required architecture-suite results. Confirm that selected
records are present or reliably recoverable and that no uncommitted result is
claimed to exist in an earlier commit. Record actual conclusions/limits within
the applicable DRs. Automated checks establish their bounded results; the
Solution Architect separately confirms authorised meaning and incorporated state.

**Decision sought and current application state.** Accept or amend this mapping
and prospective boundary. The current task prepares only this pending candidate.
Root release remains 1.1.2; no policy edits or evidence population are presented
as applied. RAD-1.1.3 remains the proposed target. Pending preparation consumes no
release under the existing policy. Acceptance of this draft, application of its
directives and confirmation of the resulting state remain distinct.

<a id="dr-5"></a>
## DR-5 — Bounded Minutiae for candidate preparation

**Disclosed group.** The supporting/minor group submitted for review is limited
to this preparation's provenance and mechanical presentation:

- The Task Details quoted in the review basis, including the supplied pending
  HCRL reference. DR-1–DR-4 contain the proposed material meaning.
- This candidate's local references, formatting, originating-state observation
  and factual account of preparation/verification below.
- Candidate preparation only: read the linked pending HCRL and completed
  predecessor; load the applicable common contracts, template, axioms and release
  policy; investigate the common Praxis source; write this pending HCRL. No
  additional application work or future report is accepted through this group.

```text
Minutiae-Accepted: [awaiting Solution Architect entry: Yes | No]
Comment: [awaiting Solution Architect / ARB comment]
```

This control and the opening table concern the same disclosed group. Prior
RAD-1.1.2 Minutiae acceptance does not extend to it. Material proposals require
their own dispositions; agent classification, silence and verification supply
no acceptance.

**Preparation verification.** Required architecture suite **PASS**: 90 tests in
11 classes, zero failures/errors/skips; Maven BUILD SUCCESS in 18.168 seconds,
bounded by a 240-second timeout with five-second termination grace:

```bash
timeout --signal=TERM --kill-after=5s 240s mvn test \
  -pl paradeigma/paradeigma-test -am \
  '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

Release validation **PASS**: 1.1.2, previous 1.1.1. Documentation/scope checks
**PASS**: Markdown parsing/rendering, local links and explicit anchors, five
consecutive DRs matching the opening table, one pending Minutiae control, clean
whitespace, unchanged tracked sources/predecessor records/release authority and
exactly this one new pending candidate. The initial source-locator check detected
the HEAD update recorded above; subsequent tree comparison established unchanged
tracked content. No evidence subtree, common Praxis or register record was added.
These are preparation checks, not evidence that the proposed governance changes
have been applied or architecturally accepted.

[supplied-pending]: ../1.1.2/hcrl-1.1.2-003.pending.md
[predecessor]: ../../register/1.1.2/hcrl-1.1.2-003.complete.md
[governance]: ../../../governance/architecture-governance.md
[provenance]: ../../../governance/architecture-change-provenance.md
[template]: ../../../governance/templates/hcrl-template.pending.md
[release-json]: ../../../../architecture-release.json
[release-policy]: ../../../../docs/markdown/governance/architecture-release-policy.md
[agents]: ../../../../AGENTS.md
[authoring]: ../../../../docs/markdown/governance/architecture-authoring-praxis.md
[roundtrip]: ../../../../docs/markdown/governance/archimate-round-trip-engineering-praxis.md
[specialised-review]: ../../../../docs/markdown/governance/archimate-ingestion-change-review.md
[ax-04]: ../../../../docs/markdown/governance/architectural-axioms.md#ax-04
[ax-08]: ../../../../docs/markdown/governance/architectural-axioms.md#ax-08
[ax-09]: ../../../../docs/markdown/governance/architectural-axioms.md#ax-09
[ax-14]: ../../../../docs/markdown/governance/architectural-axioms.md#ax-14
[ax-15]: ../../../../docs/markdown/governance/architectural-axioms.md#ax-15
[ax-17]: ../../../../docs/markdown/governance/architectural-axioms.md#ax-17
