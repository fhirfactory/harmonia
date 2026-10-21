<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | AI-agent task and report locations within transition evidence | Accept with Clarification | Accepted with clarification. before/ai-agent-tasks/ and after/ai-agent-reports/ are established as the governed locations for AI-agent Task Details and corresponding Task Reports for work performed as part of a governed architecture transition. Such Task Details and Task Reports SHALL be retained as execution provenance; retention of the formal Task/Report does not require retention of every prompt, conversation, tool invocation, working log or other agent interaction. Their architectural significance and treatment remain subject to the existing materiality, evidence and Minutiae rules. The before and after locations represent task-input and execution-result roles respectively and do not assert that the contained artefact existed in the predecessor or successor Git state. |
| [DR-2](#dr-2) | Task/report content, correspondence and architectural standing | Accept with Clarification | Accepted with clarification. For AI-agent work performed as part of a governed architecture transition, the formal Task Detail and corresponding Task Report are required execution provenance in accordance with DR-1 rather than optionally selected evidence. The Task SHALL preserve the operative issued instruction and sufficient attributable context to establish what work was authorised or requested. The Report SHALL preserve sufficient factual execution, outcome, verification, limitation and correspondence information to establish what occurred. Task-to-report correspondence SHALL be adequate to reconstruct the governed transition but need not impose one-to-one file pairing. Task/Report evidence does not constitute an alternative architectural decision record: material architectural content SHALL be surfaced in the applicable HCRL Decision Review, and neither an agent report nor its location confers architectural authority. |
| [DR-3](#dr-3) | Architecture Review Praxis and use of task/report evidence | Accept with Clarification | Accepted with clarification. Establish enterprise-architecture/governance/architecture-review-praxis.md as the common operational Architecture Review Praxis. The Praxis applies to every Harmonia change. The extent of review activity and evidence SHALL be proportionate to the scope, complexity, uncertainty and architectural materiality identified through review; classification as implementation, bug, patch, backlog item or similar does not itself reduce or bypass the Praxis. For AI-agent work performed as part of the governed transition, formal Task Details and corresponding Task Reports SHALL be retained as execution provenance in accordance with DR-1 and DR-2 rather than optionally selected on the basis of architectural materiality. The proposed workflow—establish task/state, prepare HCRL, adjudicate, apply, report/verify, confirm/complete—is accepted. Existing specialised review mechanisms retain their specialised semantics and feed the common HCRL/Praxis rather than being reinterpreted or replaced. |
| [DR-4](#dr-4) | Bounded application, prospective adoption and verification | Accept with Clarification | Accepted with clarification. Apply only the bounded source-to-result mapping identified in DR-4, incorporating the accepted clarifications from DR-1, DR-2 and DR-3. For this transition, the formal AI-agent Task Detail(s) and corresponding Task Report(s) SHALL be retained in the established before/ai-agent-tasks/ and after/ai-agent-reports/ locations respectively, making RAD-1.1.3 the first prospective use of the convention. The application SHALL NOT expand into repository migration, retrospective reconstruction, historical cleanup, erratum machinery, production code, ArchiMate model, publication or other machinery changes. Newly discovered material scope returns for Architecture Review. Verification establishes factual conformance of the incorporated result; final architectural confirmation remains with the Solution Architect. |
| [DR-5](#dr-5) | Bounded Minutiae for candidate preparation | Accept | Accepted as correctly classified bounded preparation and review provenance for the RAD-1.1.3 candidate. No additional material architectural or governance matter is identified within the disclosed Minutiae group. |
| [DR-6](#dr-6) | Architecture Review provenance and authorised-change execution provenance | Accept with Clarification | Accepted with clarification. AI-agent execution provenance applies to a formal Task that directs execution of an identifiable adjudicated/authorised change and to the corresponding Report of that execution. AI-agent activity undertaken to investigate, analyse, prepare or revise material for Architecture Review, including preparation or revision of a pending HCRL, is Architecture Review provenance and does not become execution provenance merely because it is performed by an AI agent or contributes to the same release transition. The existing directive-application Task/Report pair remains the genuine execution-provenance pair for this transition. The incorrectly created candidate-preparation Task/Report files SHALL be removed from execution evidence and SHALL NOT be relocated as a new formal review Task/Report convention. Their historical creation and subsequent reclassification are sufficiently preserved by the pending HCRL chain. Update the common governance, provenance, Praxis and affected guidance consistently with this distinction without rewriting the factual application record in iteration 002. |
| [DR-7](#dr-7) | Durable temporal provenance for execution Tasks and Reports | Accept with Clarification | Accepted with clarification. Every formal AI-agent execution Task and Report SHALL contain durable temporal provenance identifying respectively when the operative Task instruction was issued and when the Report was produced. The authoritative event timestamp SHALL be recorded within the artefact using an unambiguous ISO 8601 timestamp including UTC Z or explicit offset, together with its event meaning and attributable source where required. The filename SHALL also begin with a filesystem-safe sortable UTC timestamp derived from the same authoritative event time, followed by a descriptive name. Filename ordering is navigational convenience and does not replace the authoritative timestamp within the record. Task issuance SHALL remain distinct from agent receipt/capture; Report production SHALL remain distinct from execution start/end, validation, later formalisation, review or acceptance. Existing records SHALL use established source evidence to recover event times where possible; timestamps SHALL NOT be guessed from filesystem metadata, commit time, current time or inferred sequence. Where the original event time cannot be established, preserve the known precision and explicitly record the unresolved temporal provenance for Solution Architect treatment before closure. |
| [DR-8](#dr-8) | Bounded Minutiae for iteration 003 preparation | Accept | Accepted as correctly classified bounded preparation and review provenance for iteration 003. The material review findings and corrective decisions remain governed by DR-6 and DR-7 and are not included within this Minutiae acceptance. |
| [DR-9](#dr-9) | Bounded supporting provenance for corrective application | Reject with Clarification | Yes — I think Reject with Clarification is the right disposition for the current DR-7 application/result, because we are rejecting its conclusion that the three event times must remain unresolved, not rejecting the underlying temporal-provenance requirement.
I'd enter:
DR-7 — REJECT WITH CLARIFICATION
Comment: Rejected with clarification. The incorporated temporal-provenance requirement is retained, but the conclusion that the three identified historical execution events remain unresolved is rejected. For Harmonia AI-agent execution provenance, the attributable system timestamp recording the operative Task instruction entering the agent interaction SHALL constitute the governed Task-Issued-At event. The attributable system timestamp recording emission of the completed Task Report SHALL constitute the governed Report-Produced-At event. These are the authoritative observable provenance events for this purpose and do not assert an earlier human composition time, internal model-generation time, or other unobservable event. Apply the established timestamps accordingly to the affected records and required UTC filename prefixes. A subsequent amendment to an already-produced Report SHALL be preserved as subsequent provenance and SHALL NOT replace the Report's original production timestamp. |

# HCRL — Application of execution-provenance corrections

**Standing and authority.** Pending iteration **004** records Codex's application
of the Solution Architect dispositions in [reviewed iteration 003][reviewed]
and the subsequent user instruction:

> hcrl-1.1.3-003.pending.md updated. Apply!

The opening table preserves all eight issued dispositions and comments verbatim;
DR-9 discloses only the current bounded supporting group, with no agent-issued
acceptance. The reviewed table and Apply instruction govern over iteration 003's
earlier proposal narrative, including its rejected relocation proposal and
optional filename wording. DR-6/DR-7 are material corrective decisions; the
accepted DR-5/DR-8 Minutiae groups retain their original scopes.

**Result and limits.** The review/execution boundary and mandatory event-time /
UTC filename requirements are incorporated in the maintained contracts, Praxis
and affected guidance. Both misclassified preparation records are removed without
relocation. Genuine execution records remain, with explicit unresolved original
times and filename prefixes. The new corrective execution Report has a
contemporaneous production timestamp and corresponding UTC filename prefix.
**Full temporal conformance remains UNRESOLVED** for the three original events
identified in DR-7. Solution Architect treatment of those limitations and final
confirmation of the incorporated result remain pending. No complete/register
record or agent-issued architectural closure is created.

**State and preservation.** This remains **RAD-1.1.2 → RAD-1.1.3**, HEAD
`94223edf4e39f7ce50f4414cb6d82b89597339db`, with uncommitted initial application
present at entry (nine tracked modified and eight untracked files). Root
[release authority][release-json] remains `version=1.1.3`,
`previous_version=1.1.2`, unchanged during this corrective application. No second
increment is allocated. Iterations 001/002/003 remain byte-for-byte unchanged.
The exact updated iteration-003 input has SHA-256
`81e4c8a07777d7d3b652297a1e246ca1fa3bc6ecb93f4c441d2914b41de1384b`.
It is a retained untracked input, not content claimed to exist in HEAD.
[Iteration 002][initial-application] and the genuine original execution Report
preserve the initial nine-modified/six-added application, one release increment,
90-test / 172-link validation and actual creation of the erroneous preparation
pair. These remain historical facts, qualified by DR-6/DR-7 rather than rewritten.
The [completed predecessor][predecessor] retains its accepted standing.

**Axiom consistency.** [Architecture Governance][governance],
[Architecture Change Provenance][provenance] and the [Review Praxis][praxis]
apply under the [release policy][release-policy]. AX-04 uses existing repository,
Git and HCRL mechanisms. AX-08/AX-09 preserve meaningful provenance without
promoting all review activity or working interactions into execution evidence.
AX-14 preserves review/execution, issuance/capture and production/validation/
acceptance distinctions. AX-15/AX-17 preserve unavailable times and final human
authority explicitly. No architectural identity, runtime responsibility, model
boundary, new recorder or publication mechanism is introduced.

<a id="dr-1"></a>
## DR-1 — AI-agent task and report locations within transition evidence

**Preserved decision and corrected application.** The issued DR-1 is retained in
the opening table, read with the subsequent DR-6 clarification. Governed execution
Tasks/Reports use `before/ai-agent-tasks/` and `after/ai-agent-reports/` under
`design/evidence/<resulting-version>/`. They direct/report execution of an
identifiable adjudicated/authorised change. These are task-input/result roles;
they do not assert predecessor/successor Git presence or architectural acceptance.
Retention remains required irrespective of materiality and does not require every
agent interaction. RAD-1.1.3 remains the first prospective use. Review preparation
is accounted for through HCRL, as applied in DR-6.

<a id="dr-2"></a>
## DR-2 — Task/report content, correspondence and architectural standing

**Preserved decision and factual records.** Tasks preserve operative issued wording,
source authority, scope and attributable context. Reports preserve actual outcomes,
verification and limits, with adequate correspondence rather than mandatory
one-to-one file pairing. The [original execution Task][original-task] and
[Report][original-report] remain the genuine initial execution pair. The
[corrective execution Task][corrective-task] and [Report][corrective-report]
record this authorised application. Temporal limitations are explicit under DR-7.

Neither an agent record nor its location supplies architectural acceptance.
Material classification, corrections, actual application and validation limits
remain in these DRs. The original report's historical body is preserved unchanged
beneath a new corrective annotation; it does not assert that its former
candidate-preparation classification remains current authority.

<a id="dr-3"></a>
## DR-3 — Architecture Review Praxis and use of task/report evidence

**Preserved decision and corrected workflow.** The common [Review Praxis][praxis]
continues to apply to every Harmonia change, with review activity/evidence
proportionate to scope, complexity, uncertainty and materiality identified through
review. Implementation, bug, patch or backlog labels do not bypass or reduce it.
The six stages remain: establish task/state, prepare HCRL, adjudicate, apply,
report/verify, confirm/complete.

Stage 1 now distinguishes review activity from Tasks directing adjudicated/
authorised execution. Stage 2 preserves review instructions, findings and
preparation facts through HCRL and removes the former requirement for formal
preparation/investigation execution Reports. Stages 4/5 require execution-event
timestamps and UTC filename prefixes, with explicit treatment of unavailable
original times before closure. Specialised review purposes, exact-input
checkpoints, native modelling/identity semantics, View preservation and
validation/publication gates remain unchanged.

<a id="dr-4"></a>
## DR-4 — Bounded application, prospective adoption and verification

**Exact corrective boundary.** Relative to this Apply entry, nine maintained
files were amended at the same paths: root/docs `AGENTS.md`; common governance,
provenance, Praxis, index and HCRL template; the docs governance index and current
RAD-1.1.3 release-policy entry. Two genuine original execution records received
corrective annotations/correspondence updates, two candidate-preparation files
were removed, and three files were added: the corrective execution Task, its
Report and this pending HCRL. No path was renamed. The [corrective Report][corrective-report]
provides the exact inventory. Root release metadata is unchanged during this
application; its existing modification belongs to the initial accepted change set.

Tracked original pre-images remain recoverable from HEAD at their recorded paths;
prior pending content and original application facts remain in iterations 001–003.
Current results are retained working files, with no immutable after-commit claim.
No blanket migration, retrospective reconstruction, historical cleanup, erratum
machinery, production, model, publication or other machinery change occurred.
The historical pilot and unrelated processing material were not investigated or
disposed of. Temporary recovery/check material is engineering working material.

**Actual verification.** Required architecture suite **PASS**: 90 tests in 11 classes, zero failures/errors/skips; Maven BUILD SUCCESS in 18.271 seconds under a 240-second timeout with five-second termination grace. Release validation **PASS**: 1.1.3, previous 1.1.2. Documentation/preservation checks **PASS**: 241 live local links/anchors across 17 Markdown documents; parsing/rendering; exact copied eight issued decision rows; nine matching DR sections/table rows/anchors; two historically scoped accepted Minutiae controls and one unanswered current group; six Praxis stages; exact 11-modified/two-removed/three-added corrective inventory; unchanged iterations 001/002/003 and release authority; unchanged original Report body and operative original Task context; current Task/Report correspondence; matching new Report event/UTC filename; preserved earlier release-policy content; clean added whitespace; no register completion. Six former-path link occurrences in frozen iterations 002/003 are explicitly accounted for by the two authorised removals, rather than claimed as live links. Three original event times and their filename prefixes remain **UNRESOLVED** for Solution Architect treatment before closure.

**Limits.** Architecture tests establish their implementation-conformance result;
structural checks establish the specified document, correspondence and preservation
facts. They do not prove the missing event times, approve the current supporting
group or confirm architectural closure. Historical references to the two removed
preparation paths remain intentionally frozen in earlier HCRLs; DR-6 records their
removal and current classification instead of rewriting those sources. Live links
are checked separately from those historical path references.

<a id="dr-5"></a>
## DR-5 — Bounded Minutiae for candidate preparation

**Preserved acceptance.** The Solution Architect's DR-5 accepts only the
candidate-preparation/review group originally disclosed in iteration 001:

```text
Minutiae-Accepted: Yes
Comment: Solution Architect's reviewed iteration 001, DR-5: "Accepted as correctly classified bounded preparation and review provenance for the RAD-1.1.3 candidate. No additional material architectural or governance matter is identified within the disclosed Minutiae group." Scope: the candidate-preparation group disclosed in iteration 001 only.
```

This does not accept the erroneous execution classification, the later material
findings or the current corrective application. Their actual meaning and
application remain in their applicable DRs. DR-6 removes the misclassified files;
the HCRL history preserves their creation and reclassification.

<a id="dr-6"></a>
## DR-6 — Architecture Review provenance and authorised-change execution provenance

**Applied issued clarification.** Formal execution provenance covers Tasks
directing an identifiable adjudicated/authorised change and corresponding
execution Reports. Investigation, analysis, candidate preparation and pending-HCRL
preparation/revision remain Architecture Review provenance. The maintained
contracts, Praxis, template and affected instructions/indices/release-policy entry
now express this boundary consistently. Review activity is preserved through HCRL
and its applicable materiality/Minutiae treatment, without a new formal review
Task/Report convention.

Removed the following files under `enterprise-architecture/design/`, with no
relocation, replacement review records or stubs:

| Removed original path | Preserved historical meaning / current treatment |
| --- | --- |
| `evidence/1.1.3/before/ai-agent-tasks/candidate-preparation.md` | Creation as a Task was recorded in iteration 002; iteration 003 identifies the review-only instruction and accepts reclassification/removal through its issued DR-6. Review provenance remains in the pending HCRL chain. |
| `evidence/1.1.3/after/ai-agent-reports/candidate-preparation.md` | Creation and preparation/verification facts remain in iterations 001–003; the formal execution Report classification is removed. No new formal review Report is created. |

The proposed `review/1.1.3/candidate-preparation-task.md` and
`candidate-preparation-report.md` destinations in iteration 003 were rejected by
the issued clarification and have not been created. Former-path references in
frozen HCRLs describe earlier application/review states; they no longer resolve
to live files. This explicit correspondence replaces any inference that those
files never existed or that a missing path is an empty artefact. The original
execution Task's current preparation reference now points to the HCRL history.
The original execution Report retains its factual body beneath the corrective
annotation. Iteration 002 itself is unchanged.

<a id="dr-7"></a>
## DR-7 — Durable temporal provenance for execution Tasks and Reports

**Applied issued clarification.** Every formal execution Task/Report requires an
authoritative event timestamp for instruction issuance / Report production,
respectively, expressed in ISO 8601 with `Z` or an explicit offset, event meaning
and attributable source where required. Filenames must begin with a
filesystem-safe sortable UTC timestamp derived from that same event and a
descriptive name. The contract gives `YYYYMMDDTHHMMSSZ-<descriptive-name>.md` as a
suitable form while preserving finer known precision in the record. This is
navigation, not canonical identity or a replacement for the event record.
Issuance remains distinct from receipt/capture; production remains distinct from
execution, validation, later formalisation, review and acceptance.

**Recovery actually performed.** Inspected the relevant 2026-10-21 local Codex
session for the two exact Apply messages and original report-text emissions.
The source is
`/home/hunterm/.codex/sessions/2026/10/21/rollout-2026-10-21T15-50-06-01a15ccb-8c74-77c0-b6fa-d35406198d34.jsonl`.
Each affected record preserves the bounded observations, event meaning and source
line locators itself; no hidden log is substituted for the required formal
record. These source timestamps establish capture/emission observations, without
established semantics for the original issuer/producer event. Their presence is
not sufficient authority to backfill different events.

| Execution record / required event | Established source observation | Result and filename treatment |
| --- | --- | --- |
| Original `directive-application.md` Task / instruction issuance | Exact operative user message captured at `2026-10-21T04:58:06.105Z`, line 133; prior record gives date `2026-10-21`, Australia/Sydney. | `Task-Issued-At: UNRESOLVED`; original descriptive filename retained, UTC prefix unresolved. |
| Original `directive-application.md` Report / production | Report text in emitted call captured at `2026-10-21T05:02:13.128Z`, line 230, with verification-text amendment captured at `2026-10-21T05:03:15.711Z`, line 259; prior record gives work date only. | `Report-Produced-At: UNRESOLVED`; original descriptive filename retained, UTC prefix unresolved. |
| Corrective `corrective-directive-application.md` Task / instruction issuance | Exact operative user message captured at `2026-10-21T05:21:50.381Z`, line 381; known context `2026-10-21`, Australia/Sydney. | `Task-Issued-At: UNRESOLVED`; descriptive filename retained, UTC prefix unresolved. |
| Corrective execution Report / production | Codex's contemporaneous production declaration: `2026-10-21T05:26:42.905597Z` (UTC, microsecond precision), preserved in the Report. | Explicit `Report-Produced-At` and corresponding UTC filename prefix; no historical event is backdated. |

No original timestamp is guessed from filesystem metadata, commits, the current
clock, elapsed tests, capture times or inferred ordering. No midnight, invented
sequence or placeholder UTC timestamp is substituted for missing precision.
Causal Task/Report/review correspondence remains available; strict original
chronological ordering cannot be claimed. The earlier preparation-clock
observation in iteration 003 is not reused as any execution-event time.

**Outstanding treatment before closure.** Three authoritative original event times
and their UTC filename prefixes remain **UNRESOLVED** as listed above. The
Solution Architect must treat this residual temporal provenance before closure;
this iteration does not infer a waiver, full conformance or architectural
confirmation. Established source evidence can support future recovery and
corresponding renames/reference mappings without rewriting the historical HCRLs.
The new Report's known production event does not resolve the other records.

<a id="dr-8"></a>
## DR-8 — Bounded Minutiae for iteration 003 preparation

**Preserved acceptance.** The issued DR-8 accepts only the disclosed iteration-003
preparation/review group:

```text
Minutiae-Accepted: Yes
Comment: Solution Architect's reviewed iteration 003, DR-8: "Accepted as correctly classified bounded preparation and review provenance for iteration 003. The material review findings and corrective decisions remain governed by DR-6 and DR-7 and are not included within this Minutiae acceptance." Scope: the iteration-003 preparation/review group only.
```

Iteration 003's earlier unanswered preparation placeholder remains unchanged as
historical input. This control reflects its subsequently issued table decision,
without accepting the current application group or promoting review preparation
to execution provenance.

<a id="dr-9"></a>
## DR-9 — Bounded supporting provenance for corrective application

**Current disclosed group.** Limited to the corrective execution Task/Report's
supporting record presentation, scoped references, this iteration's formatting
and factual preservation/check account. Material scope and temporal decisions,
application outcomes, limitations and required treatment remain in DR-4/DR-6/DR-7;
they are not demoted into this group. Prior DR-5/DR-8 acceptance is not enlarged.

```text
Minutiae-Accepted: [awaiting Solution Architect entry: Yes | No]
Comment: [awaiting Solution Architect / ARB comment]
```

No new acceptance or final architectural confirmation is inferred from the Apply
instruction or verification results.

[reviewed]: hcrl-1.1.3-003.pending.md
[initial-application]: hcrl-1.1.3-002.pending.md
[predecessor]: ../../register/1.1.2/hcrl-1.1.2-003.complete.md
[governance]: ../../../governance/architecture-governance.md
[provenance]: ../../../governance/architecture-change-provenance.md#ai-agent-execution-provenance
[praxis]: ../../../governance/architecture-review-praxis.md
[release-policy]: ../../../../docs/markdown/governance/architecture-release-policy.md
[release-json]: ../../../../architecture-release.json
[original-task]: ../../evidence/1.1.3/before/ai-agent-tasks/directive-application.md
[original-report]: ../../evidence/1.1.3/after/ai-agent-reports/directive-application.md
[corrective-task]: ../../evidence/1.1.3/before/ai-agent-tasks/corrective-directive-application.md
[corrective-report]: ../../evidence/1.1.3/after/ai-agent-reports/20261021T052642Z-corrective-directive-application.md
