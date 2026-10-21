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

# HCRL — Review findings on execution-provenance scope and temporal provenance

**Current standing.** Pending iteration **003**, prepared by Codex on 2026-10-21
(Australia/Sydney), submits two material findings from review of
[application iteration 002][prior-application] for adjudication within
**RAD-1.1.2 → RAD-1.1.3**. DR-1–DR-5 retain their issued decisions/comments;
DR-6 and DR-7 contain the new matters. No new corrective disposition is recorded
or inferred from the instruction to prepare this iteration. Final Solution
Architect confirmation remains pending with these material issues unresolved.

**Review instruction and source.** The user reported:

> hcrl-1.1.3-002.pending.md reviewed. Two material issues were identified during verification:
> 1. Candidate-preparation Task/Report records have been treated as AI-agent execution provenance. Candidate preparation is Architecture Review provenance; the execution-provenance structure is intended to capture Tasks directing execution of adjudicated/authorised change and the corresponding execution Reports.
> 2. AI-agent Task/Report records contain no durable temporal provenance. Neither the filenames nor the records establish when a Task was issued or a Report produced, so ordering cannot be reliably reconstructed.
> Prepare the next pending HCRL iteration for adjudication of these issues. Preserve the existing decisions and application facts. Do not apply corrections.

The supplied attachment is the [directive-application Task Detail][application-task].
This instruction authorises review preparation only. This iteration's task and
preparation facts are Architecture Review provenance here; no execution Task or
Report is added for this review-preparation request.

**State and preservation.** HEAD is
`94223edf4e39f7ce50f4414cb6d82b89597339db`. Root release remains
`version=1.1.3`, `previous_version=1.1.2`; the existing uncommitted application
change set remains present. Iterations 001 and 002, the four existing evidence
records and all maintained contracts/entry guidance remain unchanged. The source
iteration 002 SHA-256 at review entry is
`6a35d346a613409dafe13e7e961b1d4bb0e8f91d2871260541f78ae9857b7f11`.
It identifies the retained input, not a universal hash obligation or content
claimed to be in HEAD. This pending iteration is the sole new repository file.
No correction, relocation, reclassification annotation on an existing record,
timestamp backfill, policy edit, release allocation or register entry is applied.
A preparation-clock observation was `2026-10-21T05:15:43Z` (UTC); it describes
this review preparation and supplies no historical Task-issuance or Report-production time.

**Authority and axiom consistency.** Apply [Architecture Governance][governance],
[Architecture Change Provenance][provenance], the [common Review Praxis][praxis]
and [release policy][release-policy], subject to this explicit review-only scope.
[AX-08][ax-08]/[AX-09][ax-09] support meaningful provenance and bounded working
history. [AX-14][ax-14] requires review and change execution, and issuance,
execution, recording and report production, to remain distinct.
[AX-15][ax-15]/[AX-17][ax-17] prevent guessed event times, inferred acceptance
or silent resolution of the scope conflict. Current broad wording and the
record-placement/time deficiencies are raised in DR-6/DR-7 rather than silently
reconciled. Existing files and HCRL references can provide the required review
mechanism; no new recorder, runtime component or architectural identity is
proposed. Future checks can validate fields, references and correspondence;
semantic classification and authoritative historical event times require evidence
and Solution Architect adjudication.

**Reading the carried-forward record.** The following application basis and
DR-1–DR-5 text are reproduced verbatim from iteration 002. Their claims about
incorporation, verification and absence of new material issues describe the
application-stage account, not a disposition of the later findings. DR-6/DR-7 now
qualify the account's provenance-classification and temporal-adequacy conclusions.
The recorded 90-test result, 172-link checks, actual file additions and release
increment remain historical application facts; they do not prove either disputed
semantic requirement. The accepted DR-5 group remains confined to candidate
preparation and does not accept these new issues or this iteration's new group.

## Retained iteration 002 application basis and facts

**Standing.** Iteration **002**, prepared by Codex on 2026-10-21
(Australia/Sydney), records application of the Solution Architect dispositions
in [reviewed iteration 001][reviewed] and the subsequent user instruction,
**“hcrl-1.1.3-001.pending.md updated. Apply!”** The opening table reproduces
those issued dispositions/comments unchanged. It supplies no new disposition on
the application facts. The transition is **RAD-1.1.2 → RAD-1.1.3**; root
[architecture-release.json][release-json] now records `version=1.1.3`,
`previous_version=1.1.2`.

**Final-confirmation standing.** The authorised rules are incorporated in the
working change set. Final Solution Architect confirmation that this HCRL
accurately represents the authorised change and actual incorporated result is
**pending**. No completed HCRL/register entry or architectural closure is issued
by the agent. Acceptance of the reviewed decisions, application, automated
verification and final confirmation remain distinct.

**Exact authority and state.** Application began at
`94223edf4e39f7ce50f4414cb6d82b89597339db`, verified with root release
1.1.2/previous 1.1.1 and no tracked changes. The reviewed, untracked iteration
001 was retained at its actual path; its input SHA-256 is
`01ed25d70c4445f10e1edd0640fd384ccee7397058b98a3d5ef45e230a89adea`.
It is preserved unchanged, not claimed to exist in that commit. The completed
[RAD-1.1.2 predecessor][predecessor] retains its accepted standing and immutable
content.

The source table's subsequent attributable decisions and Apply instruction
govern over iteration 001's earlier preparation-only narrative, unanswered
Minutiae placeholder and proposed optional-selection wording. DR-1–DR-3 expressly
require formal Task/Report retention for AI-agent work within the governed
transition, irrespective of materiality. This iteration records that material
clarification without rewriting the reviewed input or inferring new authority.

Tracked pre-images are recoverable through the originating commit at the same
paths. New/current records and resulting sources are retained as working files;
no immutable after-commit locator is claimed. The [application report][report]
gives the exact nine-modified/six-added application inventory, distinct from the
pre-existing untracked reviewed input. Required formal Tasks/Reports are retained
directly at the new governed locations. No extra source archives are selected
merely for completeness; temporary engineering recovery captures are not a new
permanent evidence package.

**Axiom consistency.** AX-04 is preserved through existing repository, Git and
HCRL mechanisms. AX-08/AX-09 support meaningful formal provenance while keeping
routine interactions and processing retention purpose-bounded. AX-14 distinguishes
required record retention from materiality, task authority from agent reporting,
and decisions from application, validation and acceptance. AX-15 preserves
uncertain effects; AX-17 preserves missing authority and unaccepted proposals.
No axiom, runtime responsibility, module boundary, model identity or native
modelling semantics changes. No new material issue requiring adjudication was
identified within this bounded application.

<a id="dr-1"></a>
## DR-1 — AI-agent task and report locations within transition evidence

**Authorised clarification.** Formal Task Details and corresponding Task Reports
for AI-agent work in a governed transition are required execution provenance at
`before/ai-agent-tasks/` and `after/ai-agent-reports/`, under the resulting
release's evidence directory. Materiality governs architectural significance and
review treatment, rather than whether these records are retained. The locations
represent task-input and execution-result roles, not assertions about presence
in predecessor/successor Git states. Formal retention does not require every
prompt, conversation, tool invocation or working log.

**Application.** Added the two locations to the target layout in
[Architecture Governance][governance] and specified their required retention,
roles and prospective operation in [Architecture Change Provenance][provenance].
RAD-1.1.3 is the first use. Retained the formal records for both actual tasks in
this transition:

- [Candidate-preparation Task Detail][preparation-task] and
  [Task Report][preparation-report].
- [Directive-application Task Detail][application-task] and
  [Task Report][report].

The preparation records preserve this transition's existing instruction and
preparation facts, including their source references. They do not reconstruct an
earlier release. Other required transition evidence retains its existing
purpose-selected treatment. No empty placeholder, transcript archive or duplicate
Git source-history package is introduced.

<a id="dr-2"></a>
## DR-2 — Task/report content, correspondence and architectural standing

**Authorised clarification.** The Task preserves the operative issued instruction
and sufficient attributable context to establish authorised/requested work. The
Report preserves factual execution, outcomes, verification, limits and
correspondence adequate to establish what occurred. Correspondence supports
reconstructing the transition without compulsory one-to-one file pairing.
Material architectural meaning belongs in the applicable HCRL DR; an agent report
or its location confers no architectural authority.

**Application.** The provenance contract now expresses those content and
correspondence requirements, adequate recovery, actual result distinctions and
bounded processing retention. It distinguishes mandatory formal records from
additional evidence selection. No optional-selection exception remains for formal
AI-agent Tasks/Reports. Existing no-universal-schema/hash/archive and Git-history
rules remain in force.

The retained tasks quote their operative issued instructions and identify the
attachments, source state, scope and corresponding reports. The application task
identifies the reviewed input and authoritative clarifications. Reports identify
actual work, resulting paths, validation/limits and pending final confirmation.
Task-to-report and HCRL references preserve correspondence. This HCRL records the
material clarification and incorporated result; the reports support it rather
than become competing decision records. The source decision's authority remains
distinct from agent-reported facts and from evidence-directory placement.

<a id="dr-3"></a>
## DR-3 — Architecture Review Praxis and use of task/report evidence

**Authorised clarification.** Establish the common operational Praxis at
`enterprise-architecture/governance/architecture-review-praxis.md` for every
Harmonia change. Review activity and evidence are proportionate to scope,
complexity, uncertainty and architectural materiality identified through review.
Implementation, bug, patch, backlog or similar classification does not itself
reduce or bypass the Praxis. Formal AI-agent Task/Report retention remains
required irrespective of materiality. The proposed six-stage workflow is accepted;
specialised mechanisms retain their purposes and semantics and feed the common
HCRL/Praxis.

**Application.** Added [Architecture Review Praxis][praxis] with the six stages:
establish task/state, prepare HCRL, preserve adjudication, apply authorised scope,
report/verify and confirm/complete. It applies the issued mandatory-retention
clarifications throughout, preserves the common contracts' authority, and keeps
actual execution/verification separate from final architectural confirmation.

Linked the Praxis and required task/report entry from the common governance
contract and [index][governance-index], [HCRL template][template], root
[agent instructions][agents], [documentation instructions][docs-agents] and
[existing specialised-contract index][docs-index]. These sources express the
accepted review-proportionality criteria without using implementation labels as
an exemption or a reduced-review shortcut.

The Authoring and Round-Trip Praxes, ArchiMate Change Review contract/template,
model and machinery remain unchanged at their existing paths. The new common
Praxis preserves their triggers, exact-input checkpoints, dispositions/outcomes,
identity/native-type rules, View preservation and validation/publication gates.
No continuum entry, ArchiMate element, new service or specialised migration is
created for this governance knowledge.

<a id="dr-4"></a>
## DR-4 — Bounded application, prospective adoption and verification

**Authorised boundary.** Apply only the source-to-result mapping in reviewed
DR-4 with the DR-1–DR-3 clarifications. Retain this transition's formal Tasks and
Reports in the governed locations. Preserve the explicit exclusions for repository
migration, retrospective reconstruction, historical cleanup, erratum machinery,
production code, ArchiMate model, publication and other machinery changes. Newly
discovered material scope returns for review. Final confirmation remains with
the Solution Architect.

**Actual correspondence.** Nine existing maintained files were amended at the
same paths: root/docs instructions, release authority/policy, common governance/
provenance/index, HCRL template and the existing specialised-contract index. Six
application files were added: the common Praxis, two formal Task Details, two
formal Task Reports and this pending application HCRL. The reviewed iteration
001 is a pre-existing retained input. The [application report][report] lists
all exact paths and purposes. No path is renamed or removed; no existing
architecture, legacy document, `.junie` record or historical evidence is migrated.
The completed predecessor and reviewed input remain unchanged.

**Release application.** Verified 1.1.2/previous 1.1.1 before mutation, then ran
`python3 scripts/architecture_release.py bump --expect 1.1.2` once. Root authority
now records 1.1.3/previous 1.1.2. The [release policy][release-policy] records
this accepted bounded governance application and pending final confirmation.
No model/publication regeneration occurs; completion within this same change set
consumes no further release increment.

**Verification.** Required architecture suite **PASS**: 90 tests in 11 classes,
zero failures/errors/skips; Maven BUILD SUCCESS in 18.141 seconds, bounded by a
240-second timeout with five-second termination grace. This establishes the
suite's implementation-conformance result, not architectural closure. Release
validation **PASS**: 1.1.3, previous 1.1.2. Documentation, scope and preservation
checks **PASS**: 172 local links/anchors, Markdown parsing/rendering, unchanged
issued table, five matching DRs, one correctly scoped Minutiae control, six
Praxis stages, operative task wording and task/report correspondence. The exact
nine-modified/six-added application boundary, unchanged reviewed input and
predecessor/specialised sources, preserved release-policy history, clean added
whitespace and absence of a 1.1.3 register entry were verified. Required formal
retention is expressed in the common contracts, Praxis, template and agent
entries; additional evidence selection remains separate. These bounded checks
support factual conformance without supplying final architectural confirmation.

**Final-confirmation state and limits.** Incorporated sources and formal records
are retained in the working change set. No immutable after-commit locator is
claimed. No new material uncertainty was identified; the deliberate closure
boundary is Solution Architect confirmation of authorised meaning and actual
result. No completed HCRL/register entry is created, and no historical pilot or
processing material is investigated, reconciled or disposed of. Specialised
transport, publication and inspection were outside this application and are not
claimed as verified.

<a id="dr-5"></a>
## DR-5 — Bounded Minutiae for candidate preparation

**Recorded source decision.** The Solution Architect's reviewed iteration 001,
DR-5, accepts only its disclosed candidate-preparation group. The following
control reflects that issued decision rather than the earlier unanswered
placeholder in that source:

```text
Minutiae-Accepted: Yes
Comment: Solution Architect's reviewed iteration 001, DR-5: "Accepted as correctly classified bounded preparation and review provenance for the RAD-1.1.3 candidate. No additional material architectural or governance matter is identified within the disclosed Minutiae group." Scope: the candidate-preparation group disclosed in iteration 001 only.
```

That group comprises the preparation task/reference, presentation/local-reference
and originating-state facts, and bounded preparation/verification account in
iteration 001. Its material proposals remain in DR-1–DR-4 and are governed by
their issued clarifications. Formalising the preparation Task/Report preserves
those existing facts without enlarging the accepted group.

**Application provenance.** This Apply task and [application report][report]
provide required execution provenance under DR-1/DR-2/DR-4. Their actual
application and validation facts are submitted in the applicable DRs above for
final confirmation. They supply no independent architectural decision and are
not accepted by expanding DR-5's candidate-preparation disposition.


<a id="dr-6"></a>
## DR-6 — Architecture Review provenance and authorised-change execution provenance

**Reported finding and observed evidence.** The user identifies candidate
preparation as Architecture Review provenance and the execution-provenance
structure as the record of Tasks directing execution of adjudicated/authorised
change and the corresponding execution Reports. The retained
[preparation Task][preparation-task] requests a candidate, explicitly distinguishing
that request from application or acceptance. Its [Report][preparation-report]
describes preparing iteration 001. Nevertheless, iteration 002 DR-1/DR-4 and the
[application Report][report] list both preparation records as execution-provenance
additions. That placement and classification occurred and are preserved as facts.

The maintained provenance rule says “AI-agent work performed as part of a governed
architecture transition”; the common Praxis's candidate-preparation stage also
requires a formal preparation/investigation report. Those statements extend the
execution-record requirement into review work. Iteration 001 DR-5 separately
accepted candidate preparation as bounded preparation and review provenance.
The newly reported distinction therefore requires explicit clarification of the
operative scope and its application, preserving the original decisions rather
than rewriting them to imply that the narrower scope was already implemented.

**Proposed resolution for adjudication; unapplied.** Define execution Task/Report
provenance by the work directed: a Task directs execution of an identifiable
adjudicated/authorised change, references its governing decision/instruction and
bounds the permitted execution; its corresponding Report records the actual
execution, outcome, verification and limits. Retention of those formal execution
records remains required irrespective of materiality. Authorisation to investigate
or prepare a proposal authorises review work; it does not by itself make that
work execution of an adjudicated change.

Candidate preparation, review analysis and generation/revision of pending HCRLs
remain Architecture Review provenance, retained through the review lifecycle and
bounded Minutiae where appropriate. They are not execution provenance solely
because an AI agent performed them or they concern the same release. Distinguish
roles by operative scope and authority, including mixed instructions, rather than
filename or agent identity. Preserve review coverage for every Harmonia change,
review proportionality, specialised mechanisms, and HCRL as the single semantic
record; this clarification creates no review exemption.

**Candidate correction boundary; unapplied.** Clarify the common governance,
provenance and Praxis wording and the corresponding root/docs guidance, template,
indexes and current release-policy entry consistently. In particular, revise the
Praxis's preparation-stage execution-record requirement. Do not rewrite the
retained issued decisions or iteration 002's actual application facts.

The directive-application Task/Report concern executing the reviewed directives
and remain the execution-provenance pair, subject to DR-7. The preparation pair
should receive Architecture Review standing. If relocation is adjudicated, the
following bounded correspondence is proposed for review:

| Existing path under `enterprise-architecture/design/` | Proposed review destination under the same root |
| --- | --- |
| `evidence/1.1.3/before/ai-agent-tasks/candidate-preparation.md` | `review/1.1.3/candidate-preparation-task.md` |
| `evidence/1.1.3/after/ai-agent-reports/candidate-preparation.md` | `review/1.1.3/candidate-preparation-report.md` |

These destinations are proposals, not created files or inferred migration
permission. Any authorised correction must preserve original content, actual
source/result correspondence and attributable classification history, and keep
historical HCRL references resolvable before removing any source path. The
physical treatment and reference-preservation method require an explicit bounded
application mapping; retaining the current originals pending adjudication supplies
no renewed execution classification. No blanket migration or historical cleanup
is proposed.

**Decision sought.** Confirm the review/execution boundary and the retained status
of the genuine execution pair; adjudicate the corrective wording and treatment
of the misclassified preparation records, including whether to use the proposed
review destinations and what correspondence must survive. No correction is
applied by this iteration, and no new disposition is entered by the agent.

<a id="dr-7"></a>
## DR-7 — Durable temporal provenance for execution Tasks and Reports

**Reported finding and observed evidence.** The user reports that ordering cannot
be reliably reconstructed because neither filenames nor records establish Task
issuance and Report production. All four current filenames are descriptive and
contain no timestamp. Their date-only `2026-10-21` / `Australia/Sydney` context
identifies a calendar day; the Reports describe work performed on that day but
provide no explicit production timestamp. There is no precise issuance/production
record adequate to order same-day events. Existing source hashes, Git locators,
Maven elapsed times and cross-references do not supply those missing event times.
The supplied [execution Task][application-task] is within this finding.

**Proposed resolution for adjudication; unapplied.** Require durable event-time
provenance in each formal execution Task and Report. A Task records when its
operative instruction was issued; a Report records when that Report was produced.
Give the event its explicit meaning, time zone/UTC offset, known precision and
attributable source. Proposed record labels are `Task-Issued-At` and
`Report-Produced-At`, using an unambiguous ISO 8601 timestamp with UTC `Z` or an
explicit offset; UTC to at least seconds is recommended for newly captured times.
These are proposed minimum provenance fields, not an installed universal schema.

Report production remains distinct from execution start/end, a test's completion,
file creation/modification, later transcription/capture, review and acceptance.
If a historical Report is formalised later, preserve any established original
production time and identify the later formalisation time separately. For Tasks,
distinguish the issuer's event from an agent's later receipt/capture. Cite the
source of recovered times and preserve material follow-up/revision ordering.

The recommended minimum puts the event time in the record itself. Filenames may
also carry a sortable timestamp derived from that same event for navigation;
filename prefixes alone do not replace the explicit event meaning and source.
This proposal does not require a new canonical identity or make filesystem/Git
time an architectural event by convention. Keep Task/Report and governing-review
correspondence as causal evidence. Tied timestamps, clock uncertainty or partial
precision do not justify inventing a strict order that the evidence cannot establish.

**Existing-record treatment and limits; proposed.** Preserve known date-only
context. Recover original event times only from established source evidence where
available, distinguishing recovered times from a later recording time. Where a
required time cannot be established, state that fact with the known precision and
source; do not substitute the current clock, file metadata, commit time, a guessed
midnight or an invented sequence. Any residual inability to reconstruct ordering
requires explicit Solution Architect treatment before closure. This iteration
performs no timestamp recovery or backfill and makes no claim that exact original
times have been established.

**Candidate correction and validation boundary; unapplied.** Add the accepted
requirement to the single provenance contract and reflect it in the common
Praxis, task/report entry guidance and HCRL template. Apply it to the genuine
execution records identified through DR-6; handle the preparation records in
their proper review role rather than retain an erroneous execution classification
just to add timestamps. Any renames and reference changes require an adjudicated
bounded mapping. Future verification should check event labels, timestamp/offset
syntax, source and precision, correspondence and honestly unresolved ordering.
It must not reduce temporal adequacy to presence of an arbitrary date string.
No machinery or speculative historical archive is required by this proposal.

**Decision sought and dependency.** Adjudicate the temporal minimum, explicit
record fields, optional filename convention and treatment of unavailable
historical times. DR-6 governs which records are execution provenance; this
requirement remains independently material. Acceptance, any corrective
application and final confirmation remain separate. No release is allocated by
this further pending iteration, and neither new matter is classified as minutiae.

<a id="dr-8"></a>
## DR-8 — Bounded Minutiae for iteration 003 preparation

**Disclosed current group.** This group is limited to the review-only instruction
quoted above, its supplied source reference, the creation/formatting and local
references of this pending iteration, its preparation-clock observation and
factual preparation/preservation/verification account below. The two findings,
proposed normative wording and correction boundaries belong in DR-6/DR-7 and
are excluded from this minor group.

```text
Minutiae-Accepted: [awaiting Solution Architect entry: Yes | No]
Comment: [awaiting Solution Architect / ARB comment]
```

This is the current iteration's disclosed group. The carried-forward DR-5 control
records acceptance of its historical candidate-preparation group only; it neither
accepts nor enlarges this group. The new table/control await the same human
disposition. Preparation, elapsed time and successful checks supply no acceptance.

**Actual preparation and verification.** Loaded the reviewed application record,
supplied execution Task, existing four records, common governance/provenance/
Praxis/template, relevant axioms and release rules. Added only this pending HCRL
with preserved prior decisions/application facts and the two new material matters.
No execution-evidence record or corrective policy change is part of this review
preparation.

**Preparation verification.** Required architecture suite **PASS**: 90 tests in
11 classes, zero failures/errors/skips; Maven BUILD SUCCESS in 17.540 seconds,
bounded by a 240-second timeout with five-second termination grace. Markdown
parsing/rendering, 42 local links/anchors, eight matching table/DR/anchor entries,
unchanged five issued decision rows and verbatim carried-forward application
text **PASS**. All 16 existing changed/untracked files match their review-entry
content; iteration 003 is the sole addition. Root release validation **PASS**:
1.1.3, previous 1.1.2. The historical DR-5 control and current unadjudicated DR-8
group remain distinct. No correction, evidence-record mutation, policy edit or
new release is included. These checks establish bounded preparation/preservation
facts; they do not adjudicate the new matters, prove adequate historical event
ordering or confirm architectural closure. Final confirmation remains pending.

[reviewed]: hcrl-1.1.3-001.pending.md
[predecessor]: ../../register/1.1.2/hcrl-1.1.2-003.complete.md
[preparation-task]: ../../evidence/1.1.3/before/ai-agent-tasks/candidate-preparation.md
[application-task]: ../../evidence/1.1.3/before/ai-agent-tasks/directive-application.md
[preparation-report]: ../../evidence/1.1.3/after/ai-agent-reports/candidate-preparation.md
[report]: ../../evidence/1.1.3/after/ai-agent-reports/directive-application.md
[governance]: ../../../governance/architecture-governance.md
[provenance]: ../../../governance/architecture-change-provenance.md#ai-agent-execution-provenance
[praxis]: ../../../governance/architecture-review-praxis.md
[governance-index]: ../../../governance/README.md
[template]: ../../../governance/templates/hcrl-template.pending.md
[agents]: ../../../../AGENTS.md
[docs-agents]: ../../../../docs/AGENTS.md
[docs-index]: ../../../../docs/markdown/governance/README.md
[release-json]: ../../../../architecture-release.json
[release-policy]: ../../../../docs/markdown/governance/architecture-release-policy.md

[prior-application]: hcrl-1.1.3-002.pending.md
[ax-08]: ../../../../docs/markdown/governance/architectural-axioms.md#ax-08
[ax-09]: ../../../../docs/markdown/governance/architectural-axioms.md#ax-09
[ax-14]: ../../../../docs/markdown/governance/architectural-axioms.md#ax-14
[ax-15]: ../../../../docs/markdown/governance/architectural-axioms.md#ax-15
[ax-17]: ../../../../docs/markdown/governance/architectural-axioms.md#ax-17
