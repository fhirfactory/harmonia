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
| [DR-9](#dr-9) | Bounded supporting provenance for corrective application | Reject with Clarification | Yes — I think Reject with Clarification is the right disposition for the current DR-7 application/result, because we are rejecting its conclusion that the three event times must remain unresolved, not rejecting the underlying temporal-provenance requirement.<br>I'd enter:<br>DR-7 — REJECT WITH CLARIFICATION<br>Comment: Rejected with clarification. The incorporated temporal-provenance requirement is retained, but the conclusion that the three identified historical execution events remain unresolved is rejected. For Harmonia AI-agent execution provenance, the attributable system timestamp recording the operative Task instruction entering the agent interaction SHALL constitute the governed Task-Issued-At event. The attributable system timestamp recording emission of the completed Task Report SHALL constitute the governed Report-Produced-At event. These are the authoritative observable provenance events for this purpose and do not assert an earlier human composition time, internal model-generation time, or other unobservable event. Apply the established timestamps accordingly to the affected records and required UTC filename prefixes. A subsequent amendment to an already-produced Report SHALL be preserved as subsequent provenance and SHALL NOT replace the Report's original production timestamp. |
| [DR-10](#dr-10) | Bounded supporting provenance for observable-event application | Accept |Accepted as correctly classified bounded supporting provenance for the observable-event application. Material temporal-provenance decisions, source evidence, execution-record renames, application results and limitations remain governed by DR-4, DR-7 and DR-9 and are not included within this Minutiae acceptance.|

# HCRL — Application of observable execution-event provenance

**Standing and authority.** Pending iteration **005** records application of the
Solution Architect's updated [iteration 004][reviewed] and the user instruction:

> hcrl-113-004.pending.md updated. Apply!

The attached reference identifies `hcrl-1.1.3-004.pending.md`. Its opening table
contains eight previously issued dispositions and a new **Reject with
Clarification** in DR-9. The latter comment expressly identifies DR-7 and rejects
its conclusion that three historical events must remain unresolved. This
unambiguous clarification governs over that earlier conclusion and the DR-9
supporting-group subject. All nine issued dispositions/comments are preserved
above; embedded newlines in the DR-9 comment use `<br>` for a valid table cell.
The exact reviewed input remains unchanged. No acceptance of the former DR-9
supporting group or final confirmation is inferred. DR-10 records the subsequent Solution Architect acceptance of the new bounded
supporting group, entered during final validation. Its comment and scope are
preserved; no disposition is agent-issued.

**Result.** The maintained contracts, Praxis and affected guidance now identify
attributable system instruction-entry and completed-Report-emission timestamps as
the governed observable events. The three historical events are established,
their UTC filename prefixes applied, and their prior findings retained as
historical provenance. Subsequent Report amendments remain separate from original
production. Final Solution Architect confirmation of the incorporated result is
pending; no completed/register record is created.

**State and release.** This remains **RAD-1.1.2 → RAD-1.1.3**. HEAD at entry is
`94223edf4e39f7ce50f4414cb6d82b89597339db`; the earlier applications remain
uncommitted, with nine tracked modified and nine untracked files. Root
[release authority][release-json] remains `version=1.1.3`,
`previous_version=1.1.2`, unchanged in this application. No additional increment
is allocated. Iterations 001–004 are unchanged. Reviewed iteration 004 has SHA-256
`1de0929fa26265f0f3b980c5ea3204b91df78ecbb52bcea7068ac5dc4492bc66`.
It is a retained untracked input, not content claimed to exist in HEAD. The
[completed predecessor][predecessor] retains its accepted standing.

**Architectural consistency.** The [governance][governance],
[provenance contract][provenance], [Review Praxis][praxis], [release policy][release-policy]
and [Architectural Axioms][axioms] govern. AX-04 uses existing attributable system
observations without a new recorder. AX-08/AX-09 retain meaningful execution facts
without copying interaction logs into evidence. AX-14 preserves observable event,
original production, subsequent amendment, validation and acceptance distinctions.
AX-15 permits the prior uncertainty to be resolved through the Solution Architect's
established event meaning and checked source evidence. AX-17 is preserved by
applying that explicit decision, retaining the contrary historical finding and
leaving final confirmation with the Solution Architect. No axiom conflict, new
runtime responsibility, architectural identity, model or machinery is introduced.

<a id="dr-1"></a>
## DR-1 — AI-agent task and report locations within transition evidence

**Preserved decision.** Formal Tasks executing identifiable adjudicated/authorised
change and corresponding execution Reports remain at
`design/evidence/<resulting-version>/before/ai-agent-tasks/` and
`after/ai-agent-reports/`. Retention remains required irrespective of materiality;
input/result roles assert neither Git presence nor architectural acceptance.
RAD-1.1.3 remains the first prospective use. This application's
[Task][temporal-task] and [Report][temporal-report] follow that convention.

<a id="dr-2"></a>
## DR-2 — Task/report content, correspondence and architectural standing

**Actual correspondence.** The [original Task][original-task] and
[original Report][original-report], [corrective Task][corrective-task] and
[corrective Report][corrective-report], and current Task/Report preserve the three
application instructions and results. New annotations establish current temporal
standing and navigation while retaining each earlier record's body unchanged.
The exact historical source events and their meanings are retained in the records;
understanding them does not require a continuing local-log dependency. The path
mapping in DR-7 accounts for former references in frozen HCRLs and historical
bodies. No report or evidence location supplies architectural authority.

<a id="dr-3"></a>
## DR-3 — Architecture Review Praxis and use of task/report evidence

**Bounded alignment.** Every-change review, proportional review activity/evidence,
the six Praxis stages and specialised review semantics remain as issued. Stages
1/4 now identify the system instruction-entry event as governed issuance; stage 5
identifies completed-Report emission as governed production and preserves
subsequent amendments without replacing original production time. Separate
receipt/capture, later formalisation, validation, review and acceptance retain
their actual meanings. Generic unavailable-time treatment remains for genuinely
unestablished events; the three identified events are no longer in that category.

<a id="dr-4"></a>
## DR-4 — Bounded application, prospective adoption and verification

**Actual source/result boundary.** Relative to this Apply entry, ten files are
modified at the same paths: nine maintained governance/guidance files and the
existing corrective Report. Three genuine execution records are renamed and
receive current temporal annotations; three files are added: the temporal
application Task, Report and this pending HCRL. No other path is removed.
The [Report][temporal-report] gives the exact inventory; DR-7 gives all three
old/new path pairs. Root release and iterations 001–004 remain unchanged.
Tracked pre-images remain recoverable from HEAD; the entry states of retained
untracked records are preserved beneath their new annotations and through prior
HCRLs. Current results are working files, with no immutable after-commit claim.

**Validation.** Required architecture suite **PASS**: 90 tests in 11 classes,
zero failures/errors/skips; Maven BUILD SUCCESS in 18.562 seconds, exit 0 under a
240-second timeout with five-second termination grace. Release validation **PASS**:
1.1.3, previous 1.1.2. Documentation/preservation checks **PASS**: 273 live local links/anchors across
20 Markdown documents, with 25 historical former-path link occurrences accounted
for by the three explicit rename mappings and two previously authorised removals.
Parsing/rendering, the valid four-column opening table, all nine copied issued
dispositions/comments, ten matching DR table rows/sections/anchors, two historical
accepted Minutiae scopes and one separately accepted current bounded group, six Praxis stages,
exact ten-modified/three-renamed/three-added inventory, unchanged iterations
001–004 and release authority, all four unchanged historical execution-record
bodies, original production/update preservation, exact source-event observations,
six matching event/UTC filename pairs, current Task/Report correspondence,
preserved earlier release-policy content, clean added whitespace and no register
completion are established. The three previously unresolved events/prefixes are
established and applied; final architectural confirmation remains pending.

**Limits.** Checks establish factual implementation/document conformance and the
specified source/event correspondence, not architectural acceptance. Earlier
validation counts remain historical facts. No production, model, publication,
other machinery, general migration, retrospective reconstruction, historical-pilot
investigation, processing cleanup or erratum work occurred. The former
candidate-preparation pair remains removed without relocation. No command timed
out or stalled. No completed/register record or additional increment is created.

<a id="dr-5"></a>
## DR-5 — Bounded Minutiae for candidate preparation

**Preserved historical acceptance.**

```text
Minutiae-Accepted: Yes
Comment: Solution Architect's reviewed iteration 001, DR-5: "Accepted as correctly classified bounded preparation and review provenance for the RAD-1.1.3 candidate. No additional material architectural or governance matter is identified within the disclosed Minutiae group." Scope: candidate-preparation group disclosed in iteration 001 only.
```

This supplies no acceptance of a later supporting group.

<a id="dr-6"></a>
## DR-6 — Architecture Review provenance and authorised-change execution provenance

**Preserved correction.** Investigation, analysis and candidate/pending-HCRL
preparation/revision remain Architecture Review provenance through HCRL; agent
participation does not convert them into execution Tasks/Reports. The removed
preparation pair is not restored or relocated. Iteration 002 and the original
Report retain the factual creation/application history; iteration 003 and the
subsequent application preserve reclassification. The current Task/Report records
execution of the identifiable adjudicated temporal clarification. No formal review
Task/Report convention is created.

<a id="dr-7"></a>
## DR-7 — Durable temporal provenance for execution Tasks and Reports

**Applied later clarification.** Updated iteration 004's table DR-9 expressly
clarifies DR-7: attributable system instruction-entry timestamps constitute
Task-Issued-At; completed-Report-emission timestamps constitute Report-Produced-At.
These are authoritative observable events without asserting earlier human
composition, internal model-generation time or another unobservable event.
The timestamp remains in the record at known precision; its filename uses the
same event in UTC to seconds. Later amendments do not replace original production.

**Established evidence and required renames.** Rechecked the four source entries
already identified in iteration 004, DR-7, in
`/home/hunterm/.codex/sessions/2026/10/21/rollout-2026-10-21T15-50-06-01a15ccb-8c74-77c0-b6fa-d35406198d34.jsonl`.
The two user-message entries contain the exact operative Apply wording; line 230
emits the original Report text in `exec` / `apply_patch`; line 259 emits its
verification-text amendment. These bounded source observations and the issued
semantic clarification establish:

| Record / governed event | Authoritative time and source | Former → current filename |
| --- | --- | --- |
| Original Task / instruction entry | `2026-10-21T04:58:06.105Z`, user-message `response_item`, line 133 | `before/ai-agent-tasks/directive-application.md` → `before/ai-agent-tasks/20261021T045806Z-directive-application.md` |
| Original Report / completed Report emission | `2026-10-21T05:02:13.128Z`, `response_item` / `custom_tool_call`, line 230 | `after/ai-agent-reports/directive-application.md` → `after/ai-agent-reports/20261021T050213Z-directive-application.md` |
| Corrective Task / instruction entry | `2026-10-21T05:21:50.381Z`, user-message `response_item`, line 381 | `before/ai-agent-tasks/corrective-directive-application.md` → `before/ai-agent-tasks/20261021T052150Z-corrective-directive-application.md` |

Paths are relative to `enterprise-architecture/design/evidence/1.1.3/`.
These are renames with preserved content and additive annotations, not new
architectural identities. The earlier unresolved fields remain explicitly
historical beneath those annotations. No stubs or duplicate record copies remain
at the former paths. Frozen HCRLs and preserved historical bodies retain their
former-path links; this table supplies retrievable current correspondence.

**Original production and amendments.** The original Report's separate amendment
at `2026-10-21T05:03:15.711Z`, source line 259, is recorded as subsequent provenance;
its original production remains `2026-10-21T05:02:13.128Z`. The prior corrective
Report's already-established `2026-10-21T05:26:42.905597Z` production, UTC filename
and `2026-10-21T05:27:48.132210Z` update remain unchanged. Its new annotation
qualifies the former unresolved conclusion while retaining its full prior body.
The current Report records this application as subsequent provenance, not a new
production timestamp for either historical Report.

**Current execution events.** The current Task's governed instruction-entry time
is `2026-10-21T05:30:07.344Z`, line 9 of
`/home/hunterm/.codex/sessions/2026/10/21/rollout-2026-10-21T16-29-53-01a15cef-fad4-7dd1-8411-48cba5cb4209.jsonl`;
its exact issued wording, source and UTC prefix are retained in the Task.
The current Report's system-attributable emission is `2026-10-21T05:33:44.933Z`,
`response_item` / `custom_tool_call`, line 140 of the same session. Its production
event, source and matching `20261021T053344Z` UTC filename prefix are retained in the
Report; subsequent verification amendments do not replace that event.

**Conformance and limits.** The three previously unresolved historical event times
and required filename prefixes are established and applied. No event is guessed
from file metadata, commit time, the current clock or inferred sequence. Known
source precision is preserved without a claim of strict ordering beyond that
precision. No unresolved temporal treatment remains for these three events.
Final architectural confirmation remains a distinct pending human action.

<a id="dr-8"></a>
## DR-8 — Bounded Minutiae for iteration 003 preparation

**Preserved historical acceptance.**

```text
Minutiae-Accepted: Yes
Comment: Solution Architect's reviewed iteration 003, DR-8: "Accepted as correctly classified bounded preparation and review provenance for iteration 003. The material review findings and corrective decisions remain governed by DR-6 and DR-7 and are not included within this Minutiae acceptance." Scope: iteration-003 preparation/review group only.
```

This supplies no acceptance of the former DR-9 group or the current DR-10 group.

<a id="dr-9"></a>
## DR-9 — Bounded supporting provenance for corrective application

**Issued disposition and actual meaning.** The reviewed table records Reject with
Clarification at DR-9; its comment expressly says “DR-7 — REJECT WITH
CLARIFICATION” and explains that the rejected part is the conclusion that the
three historical execution events must remain unresolved. The underlying temporal
requirement is retained. This iteration preserves the exact issued comment and
applies that specific material clarification in DR-7 and the maintained sources.
It does not silently move the decision to another source row, reinterpret the
rejection as acceptance, or infer acceptance of the earlier supporting group.

Iteration 004's result/limitation narrative and unanswered supporting control
remain unchanged as historical input. The new facts, resolved event meanings,
renames, verification and limits are in DR-4/DR-7. No current supporting work is
included in the prior DR-5/DR-8 acceptances.

<a id="dr-10"></a>
## DR-10 — Bounded supporting provenance for observable-event application

**Current disclosed group.** Limited to the current formal Task/Report's supporting
presentation and correspondence, current reference/formatting maintenance, and
factual preservation/check account. Material event meanings, the rejected earlier
conclusion, source evidence, application/rename facts and validation limits remain
in DR-4/DR-7/DR-9; they are not demoted into this group. The earlier rejected
DR-9 group is not silently approved or enlarged.

```text
Minutiae-Accepted: Yes
Comment: Solution Architect's iteration 005 table, DR-10: "Accepted as correctly classified bounded supporting provenance for the observable-event application. Material temporal-provenance decisions, source evidence, execution-record renames, application results and limitations remain governed by DR-4, DR-7 and DR-9 and are not included within this Minutiae acceptance." Scope: the disclosed observable-event application supporting group only.
```

The Yes control reflects only the explicit Solution Architect table DR-10
acceptance entered during final validation. The earlier working input before
this reflection had SHA-256
`86f144e3c80124320357e62fae21d14afdd860bf4e0a942b161e26cbb0595000`; the issued table row is unchanged.
This acceptance does not enlarge the group or approve the earlier DR-9 group.
Final architectural confirmation is not inferred from this bounded acceptance,
Apply or checks.

[reviewed]: hcrl-1.1.3-004.pending.md
[predecessor]: ../../register/1.1.2/hcrl-1.1.2-003.complete.md
[governance]: ../../../governance/architecture-governance.md
[provenance]: ../../../governance/architecture-change-provenance.md#ai-agent-execution-provenance
[praxis]: ../../../governance/architecture-review-praxis.md
[release-policy]: ../../../../docs/markdown/governance/architecture-release-policy.md
[release-json]: ../../../../architecture-release.json
[axioms]: ../../../../docs/markdown/governance/architectural-axioms.md
[original-task]: ../../evidence/1.1.3/before/ai-agent-tasks/20261021T045806Z-directive-application.md
[original-report]: ../../evidence/1.1.3/after/ai-agent-reports/20261021T050213Z-directive-application.md
[corrective-task]: ../../evidence/1.1.3/before/ai-agent-tasks/20261021T052150Z-corrective-directive-application.md
[corrective-report]: ../../evidence/1.1.3/after/ai-agent-reports/20261021T052642Z-corrective-directive-application.md
[temporal-task]: ../../evidence/1.1.3/before/ai-agent-tasks/20261021T053007Z-temporal-directive-application.md
[temporal-report]: ../../evidence/1.1.3/after/ai-agent-reports/20261021T053344Z-temporal-directive-application.md
