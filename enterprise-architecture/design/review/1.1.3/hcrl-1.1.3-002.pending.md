<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | AI-agent task and report locations within transition evidence | Accept with Clarification | Accepted with clarification. before/ai-agent-tasks/ and after/ai-agent-reports/ are established as the governed locations for AI-agent Task Details and corresponding Task Reports for work performed as part of a governed architecture transition. Such Task Details and Task Reports SHALL be retained as execution provenance; retention of the formal Task/Report does not require retention of every prompt, conversation, tool invocation, working log or other agent interaction. Their architectural significance and treatment remain subject to the existing materiality, evidence and Minutiae rules. The before and after locations represent task-input and execution-result roles respectively and do not assert that the contained artefact existed in the predecessor or successor Git state. |
| [DR-2](#dr-2) | Task/report content, correspondence and architectural standing | Accept with Clarification | Accepted with clarification. For AI-agent work performed as part of a governed architecture transition, the formal Task Detail and corresponding Task Report are required execution provenance in accordance with DR-1 rather than optionally selected evidence. The Task SHALL preserve the operative issued instruction and sufficient attributable context to establish what work was authorised or requested. The Report SHALL preserve sufficient factual execution, outcome, verification, limitation and correspondence information to establish what occurred. Task-to-report correspondence SHALL be adequate to reconstruct the governed transition but need not impose one-to-one file pairing. Task/Report evidence does not constitute an alternative architectural decision record: material architectural content SHALL be surfaced in the applicable HCRL Decision Review, and neither an agent report nor its location confers architectural authority. |
| [DR-3](#dr-3) | Architecture Review Praxis and use of task/report evidence | Accept with Clarification | Accepted with clarification. Establish enterprise-architecture/governance/architecture-review-praxis.md as the common operational Architecture Review Praxis. The Praxis applies to every Harmonia change. The extent of review activity and evidence SHALL be proportionate to the scope, complexity, uncertainty and architectural materiality identified through review; classification as implementation, bug, patch, backlog item or similar does not itself reduce or bypass the Praxis. For AI-agent work performed as part of the governed transition, formal Task Details and corresponding Task Reports SHALL be retained as execution provenance in accordance with DR-1 and DR-2 rather than optionally selected on the basis of architectural materiality. The proposed workflow—establish task/state, prepare HCRL, adjudicate, apply, report/verify, confirm/complete—is accepted. Existing specialised review mechanisms retain their specialised semantics and feed the common HCRL/Praxis rather than being reinterpreted or replaced. |
| [DR-4](#dr-4) | Bounded application, prospective adoption and verification | Accept with Clarification | Accepted with clarification. Apply only the bounded source-to-result mapping identified in DR-4, incorporating the accepted clarifications from DR-1, DR-2 and DR-3. For this transition, the formal AI-agent Task Detail(s) and corresponding Task Report(s) SHALL be retained in the established before/ai-agent-tasks/ and after/ai-agent-reports/ locations respectively, making RAD-1.1.3 the first prospective use of the convention. The application SHALL NOT expand into repository migration, retrospective reconstruction, historical cleanup, erratum machinery, production code, ArchiMate model, publication or other machinery changes. Newly discovered material scope returns for Architecture Review. Verification establishes factual conformance of the incorporated result; final architectural confirmation remains with the Solution Architect. |
| [DR-5](#dr-5) | Bounded Minutiae for candidate preparation | Accept | Accepted as correctly classified bounded preparation and review provenance for the RAD-1.1.3 candidate. No additional material architectural or governance matter is identified within the disclosed Minutiae group. |

# HCRL — AI-agent execution provenance and Review Praxis application record

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
