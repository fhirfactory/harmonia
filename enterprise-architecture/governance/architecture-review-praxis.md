<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia Architecture Review Praxis

## Authority and scope

This is the common operational Architecture Review Praxis, established by the
Solution Architect's [reviewed HCRL 1.1.3 iteration 001][reviewed], DR-1–DR-4,
and subsequent Apply instruction. [Application iteration 002][application]
preserves initial incorporation and verification facts. The Solution Architect's
[reviewed iteration 003][corrective-review], DR-6/DR-7, and subsequent Apply
instruction clarify execution scope and temporal provenance.
[Application iteration 004][corrective-application] records the corrections and
then-unresolved event times. Its updated Solution Architect table DR-9 expressly
clarifies DR-7, establishing observable instruction-entry and Report-emission event
times. [Application iteration 005][temporal-application] records recovery of the
three times and required filename prefixes. Mark Hunter, Solution Architect,
confirmed the incorporated result on 2026-10-21: “result confirmed. Complete!”
The [completed HCRL iteration 005][completed] is the authoritative RAD-1.1.2 →
RAD-1.1.3 transition record; completion consumes no additional release.

[Architecture Governance][governance] controls review authority, HCRL structure,
materiality, bounded Minutiae and completion. [Architecture Change Provenance][provenance]
is the single representation-neutral transition-evidence contract. The
[Architectural Axioms][axioms] remain highest authority; the [release policy][release-policy]
and root [architecture-release.json][release-json] control release allocation.
This Praxis applies those contracts without establishing an alternative decision
record, approval service, architectural taxonomy or identity regime.

The Praxis SHALL apply to every Harmonia change. The extent of review activity
and evidence SHALL be proportionate to scope, complexity, uncertainty and
architectural materiality identified through review. Classification as
implementation, bug, patch, backlog item or similar does not itself reduce or
bypass the Praxis. Review may establish that a change has no material
architectural effect; such a transition still receives appropriate HCRL review.

For formal AI-agent Tasks directing execution of an identifiable
adjudicated/authorised change, Task Details and corresponding execution Reports
SHALL be retained as execution provenance, irrespective of architectural
materiality, under the provenance contract:

```text
enterprise-architecture/design/evidence/<resulting-version>/
├── before/
│   └── ai-agent-tasks/
└── after/
    └── ai-agent-reports/
```

These are task-input and execution-result roles, not assertions that a record
existed in the predecessor or successor Git state. Retaining formal Tasks/Reports
does not require every prompt, conversation, tool invocation, working log or
other agent interaction. Additional evidence remains purpose-selected. Materiality
determines review treatment, not whether formal AI-agent Task/Report provenance
is retained. RAD-1.1.3 is the first prospective use of this convention.

Investigation, analysis, candidate preparation and preparation/revision of a
pending HCRL are Architecture Review provenance. Preserve their relevant
instructions, findings and review history through the HCRL lifecycle and bounded
Minutiae where appropriate. Agent participation and contribution to the same
release do not make that activity execution provenance. No new formal review
Task/Report convention is established.

## 1. Establish the task and originating state

Identify the operative instruction, actual immediate governed release, source
state and applicable authority. Load the minimum sufficient repository-held
authority, expanding through dependencies and traceability as needed; do not
substitute prior-session interpretations or existing implementation for authority.
Identify relevant axioms, constraints, conditions and exclusions. Preserve
unestablished authority and unresolved effects explicitly.

Classify the instruction against its actual purpose and authority. For a Task
directing execution of an identifiable adjudicated/authorised change, retain the
formal Task Detail at the governed task location. Preserve operative issued
wording, attributable context, material corrections/follow-ups and adequate
source/review correspondence. Record the authoritative issuance timestamp in
ISO 8601 with `Z` or an explicit offset and its source; begin the filename with
a filesystem-safe sortable UTC timestamp derived from that event, followed by a
descriptive name. The attributable system timestamp of the operative instruction
entering the agent interaction is the governed Task-Issued-At event; it asserts
no earlier human composition time. Separate receipt/capture or later recording
does not replace that event. Follow the provenance contract's treatment of
unavailable original times; record unresolved
times and filename prefixes for Solution Architect treatment before closure.
An instruction to investigate or prepare a candidate remains review provenance
and does not itself approve the proposed architecture.

## 2. Prepare the candidate HCRL

Use `design/review/<resulting-version>/hcrl-<version>-<iteration>.pending.md`
and the [common HCRL template][template]. Keep one opening Decision Review table,
consecutive top-level DRs and at most one subordinate level. Describe material
proposals, relevant authority, uncertainties, evidence, consequences and
dependencies within the applicable DR. Disclose supporting/minor provenance in
one bounded Minutiae group without demoting substantive architectural content.

Preserve the review-preparation instruction, relevant findings, source references
and preparation/verification facts within the pending HCRL and its applicable DRs.
Reference genuine execution records where relevant to the matter under review.
Do not create formal execution Tasks/Reports for preparation or investigation,
or imply that a Report for future application already exists. Pending iterations
consume no releases and supply no acceptance.

## 3. Obtain and preserve adjudication

Mark Hunter, Solution Architect, holds final architectural decision and acceptance
authority. ARB is the collaborative review/adjudication forum; peer/agent roles
retain the common governance limits. Record attributable Solution Architect
dispositions and comments against the reviewed content, preserving clarifications,
conditions, dependencies, exclusions, deferrals and material revisions/reasons.

Use the bounded Minutiae control for its disclosed group. Unanswered fields,
agent classification/recommendations, elapsed time and successful tooling confer
no approval. An issued clarification governs over earlier proposal wording; retain
the earlier reviewed content and decision history rather than silently rewriting
the input. Resolve ambiguous authority before dependent application.

## 4. Apply the authorised scope

Read the exact reviewed HCRL and applicable application instruction. Recheck the
originating source/baseline and immediate predecessor before mutation. Apply only
unambiguous authorised directives and deterministic derivations, with their actual
conditions/exclusions. Retain formal application Task Details and material
follow-up instructions at the task location, preserving their correspondence,
issuance event times and required timestamped filenames.

Prepare the one accepted-change patch increment under the existing release policy
and expected-current check; identify the release used for resulting-state
verification. An increment alone does not establish acceptance or publication.
Do not allocate another increment for a further iteration within that change set.

Implementation discoveries that materially affect architectural meaning SHALL
return to the applicable HCRL DR for review/adjudication before dependent work.
Continue independent authorised work where possible. Preserve uncertain effects
under AX-15 and missing architectural authority under AX-17.

## 5. Report and verify the incorporated result

Retain formal Task Reports at `after/ai-agent-reports/`. Preserve sufficient
factual execution, outcome, verification, limitation and correspondence information
to establish what occurred. Identify the corresponding tasks, actual affected
paths/source-state locators, departures and unresolved effects. Correspondence
SHALL support reconstruction of the governed transition without imposing
one-to-one Task/Report file pairing.

Record when the Report is produced as an authoritative ISO 8601 timestamp with
`Z` or an explicit offset, its event meaning and attributable source where needed.
The attributable system timestamp recording emission of the completed Task
Report is the governed Report-Produced-At event; it asserts no internal
model-generation time or other unobservable event. Begin its filename with the
filesystem-safe sortable UTC timestamp derived from that same production time
and a descriptive name. A Report's production is
distinct from execution, validation, file metadata, later formalisation, review
and acceptance. Preserve subsequent amendments as subsequent provenance without
replacing the original Report production timestamp or its filename prefix.
Recover existing original times only from established source evidence; preserve
known precision and unresolved temporal provenance for
Solution Architect treatment before closure. Never infer missing times or order
from filesystem metadata, commit time, the current clock or sequence. Filename
ordering aids navigation and does not replace the authoritative event record.

Record actual application, source/result correspondence, material validation
conclusions and limits in the applicable HCRL DR, with useful report references.
A report does not replace the semantic record. Distinguish proposed, performed
and incorporated work; PASS, FAIL, TIMEOUT, STALLED and UNRESOLVED; automated
verification, human review, publication and actual inspection/transport outcomes.
Use bounded execution and required repository checks. Do not claim uncommitted
results are recoverable from an earlier Git commit.

An attributable Solution Architect decision in a task retains its actual authority
and conditions. Neither an agent report nor its evidence location confers
architectural authority. New material content requires adjudication in the
applicable DR; normally minor report detail remains supporting provenance.

## 6. Confirm and complete the transition

Present the authorised meaning and actual incorporated result for Solution
Architect confirmation. Complete the HCRL at
`design/register/<resulting-version>/hcrl-<version>-<iteration>.complete.md`
only after that confirmation. Agents report facts and cannot self-confirm
architectural closure. Preserve explicit deferrals/exclusions, attributable
decisions, required retrievable Task/Report evidence and relevant correspondence.

Completed HCRLs are immutable under common governance. Pending iterations remain
working provenance while needed for active review/traceability; detailed erratum
mechanics remain deferred until an actual need. Completion within the accepted
change set consumes no additional release. Required formal Task/Report retention
does not authorise historical reconstruction, migration, processing cleanup or
loss of needed recovery material.

## Specialised mechanisms and architectural consistency

The [Authoring Praxis][authoring], [Round-Trip Praxis][roundtrip] and
[ArchiMate Change Review contract][specialised-review] retain their specialised
discovery, analysis, validation, presentation and reconciliation purposes. They
feed the common HCRL/Praxis. Preserve their triggers, exact-input checkpoints,
dispositions/outcomes, native modelling and identity semantics, View preservation
and applicable validation/publication gates. Task/Report provenance complements
those mechanisms without replacing them or creating competing acceptance records.

AX-04 is preserved through existing repository/Git/HCRL mechanisms. AX-08/AX-09
support meaningful formal provenance and purpose-bounded processing retention;
formal Task/Report retention does not turn every agent interaction into evidence.
AX-14 preserves decision, execution, validation and acceptance distinctions;
AX-15 preserves unresolved effects; AX-17 preserves missing authority and the
standing of proposals. This is governance knowledge requiring no continuum entry
or ArchiMate element, and changes no runtime responsibility or module boundary.

[reviewed]: ../design/review/1.1.3/hcrl-1.1.3-001.pending.md
[application]: ../design/review/1.1.3/hcrl-1.1.3-002.pending.md
[corrective-review]: ../design/review/1.1.3/hcrl-1.1.3-003.pending.md
[corrective-application]: ../design/review/1.1.3/hcrl-1.1.3-004.pending.md
[temporal-application]: ../design/review/1.1.3/hcrl-1.1.3-005.pending.md
[completed]: ../design/register/1.1.3/hcrl-1.1.3-005.complete.md
[governance]: architecture-governance.md
[provenance]: architecture-change-provenance.md#ai-agent-execution-provenance
[template]: templates/hcrl-template.pending.md
[axioms]: ../../docs/markdown/governance/architectural-axioms.md
[release-policy]: ../../docs/markdown/governance/architecture-release-policy.md
[release-json]: ../../architecture-release.json
[authoring]: ../../docs/markdown/governance/architecture-authoring-praxis.md
[roundtrip]: ../../docs/markdown/governance/archimate-round-trip-engineering-praxis.md
[specialised-review]: ../../docs/markdown/governance/archimate-ingestion-change-review.md
