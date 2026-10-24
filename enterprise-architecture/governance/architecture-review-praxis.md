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

The operative 004 Task recorded in [HCRL 1.1.4 iteration 004][iteration-004]
authorises the prospective Praxis below, carrying forward the iteration-scoped
structure demonstrated by [iteration 003][iteration-003], DR-13/DR-14. From 004,
this explicitly supersedes the 1.1.3 execution-only Task/Report boundary and
release-level evidence placement. The original event-time requirements and
Solution Architect authority remain in force. 003 demonstrated and refactored
the structure; 004 implements its governed process. Prior practice is preserved.
Mark Hunter, Solution Architect, subsequently confirmed the bounded 004 result
on 2026-10-21: “result confirmed, complete!” The [completed HCRL 004](../design/register/1.1.4/hcrl-1.1.4-004.complete.md)
records that confirmation with its attributable event and explicit migration
deferrals. Completion consumes no additional release; the substantive migration
remains reserved for 005.

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

## Activity, evidence and semantic record

The Praxis SHALL distinguish the following, using the normative definitions in
[Architecture Change Provenance][evidence-semantics]:

- **Working material:** transient material used while performing an activity;
  discardable when its purpose ends, without retention merely because it existed.
- **Evidence:** durable inputs, outputs or supporting material retained because
  they materially support, explain, reproduce or verify the governed activity
  associated with an HCRL iteration.
- **Substantive governed activity:** activity that changes, analyses, reconciles,
  proposes or validates architectural or solution content in a manner requiring
  governed evidence.
- **Provenance maintenance:** mechanical creation, completion, correction,
  formatting, cross-referencing, timestamp synchronization or verification of the
  governance artefacts required to document an already-authorised substantive
  activity within an existing provenance boundary. Provenance maintenance SHALL NOT
  recursively instantiate a new Task, Report or HCRL iteration.
- **HCRL:** the authoritative semantic change/decision record for the governed RAD
  transition. It is not the Architecture Review working document or a substitute
  for detailed supporting evidence.

The governing principle of the Praxis is that provenance records describe the
governed activity, not the mechanical production history of the provenance records
themselves.

The finite lifecycle progression for governed activity is:

```text
Authorised Task
    ↓
Substantive Activity
    ↓
Report
    ↓
Architecture Review required?
    ├── No
    │     ↓
    │  TERMINAL
    │
    └── Yes
          ↓
      Architecture Review
          ↓
      Solution Architect Disposition
          ↓
      HCRL Completion
          ↓
      Resulting Git State
          ↓
      TERMINAL
```

Where mechanical Git state is established or checked for an activity terminating at Report, that verification remains provenance maintenance and does not extend the lifecycle. A commit is not required where the governed activity legitimately produces no repository change beyond its Report/evidence.

```text
Architecture Review activity
├── transient working material
│   └── not retained merely because it existed
├── durable evidence
│   └── enterprise-architecture/design/evidence/<RAD-version>/<HCRL-iteration>/
│       ├── before/
│       │   └── ai-agent-tasks/<timestamp>-<task>.md
│       └── after/
│           └── ai-agent-reports/<timestamp>-<report>.md
└── HCRL
    └── governed semantic change/decision record
```

The HCRL iteration SHALL be the provenance boundary. The version is the resulting
RAD release; the iteration corresponds to its HCRL, such as `1.1.4/004`.
`before/` contains activity inputs/input provenance; `after/` contains outputs/output
provenance. These roles do not assert presence in predecessor/successor Git states
or acceptance of results. Keep earlier evidence with its originating iteration;
later iterations use sufficient references and correspondence rather than recopying
it. An iteration does not become a release, session or processing-run identity.

For substantive AI-agent activity associated with an iteration, formal Tasks and
corresponding Reports SHALL be retained in these locations, irrespective of
architectural materiality. Investigation, analysis, review, candidate/pending-HCRL
preparation or revision, corrective work, application and verification all use
this model. Their authority and outcomes remain distinct; an investigation Task
supplies no authority to apply a candidate. There is no separate review/execution
evidence taxonomy. Materiality determines review treatment, not retention of
these required substantive-activity records.

Select additional evidence by purpose: durable mappings, reference inventories,
checks and similar review outputs belong in the originating iteration's evidence
when they materially support the review. Retaining formal Tasks/Reports does not
require every prompt, conversation, tool invocation, intermediate artefact or
working log. Preserve material still needed for recovery, validation or unresolved
dependencies; the convention authorises no unrelated historical cleanup.
HCRL records material semantics and conclusions with enough references for
traceability, without becoming a manifest of every evidence file.

## 1. Establish the task and originating state

Identify the operative instruction, actual immediate governed release, source
state and applicable authority. Load the minimum sufficient repository-held
authority, expanding through dependencies and traceability as needed; do not
substitute prior-session interpretations or existing implementation for authority.
Identify relevant axioms, constraints, conditions and exclusions. Preserve
unestablished authority and unresolved effects explicitly.

Identify the instruction's actual purpose, authority and associated HCRL iteration.
For substantive AI-agent activity, retain the formal Task Detail at that
iteration's `before/ai-agent-tasks/` location, whether the activity is investigation,
review, corrective work, application or verification. A new Task is required only
when initiating new substantive governed activity; mechanical provenance maintenance
within an existing authorized boundary SHALL NOT instantiate a new Task. Preserve
operative issued wording, attributable context, material corrections/follow-ups and
adequate source/review correspondence. Record the authoritative issuance timestamp in
ISO 8601 with `Z` or an explicit offset and its source; begin the filename with
a filesystem-safe sortable UTC timestamp derived from that event, followed by a
descriptive name. The attributable system timestamp of the operative instruction
entering the agent interaction is the governed Task-Issued-At event; it asserts
no earlier human composition time. Separate receipt/capture or later recording
does not replace that event. Follow the provenance contract's treatment of
unavailable original times; record unresolved
times and filename prefixes for Solution Architect treatment before closure.
An instruction to investigate or prepare a candidate does not itself approve the
proposed architecture; its Task/Report nonetheless follows the same iteration model.

## 2. Prepare the candidate HCRL

Use `design/review/<resulting-version>/hcrl-<version>-<iteration>.pending.md`
and the [common HCRL template][template]. Keep one opening Decision Review table,
consecutive top-level DRs and at most one subordinate level. Describe material
proposals, relevant authority, uncertainties, evidence, consequences and
dependencies within the applicable DR. Disclose supporting/minor provenance in
one bounded Minutiae group without demoting substantive architectural content.

Retain the substantive review-preparation Task in the iteration's `before/` and
its actual Report and durable supporting outputs in `after/`. Preserve findings,
source correspondence and preparation/verification conclusions in the applicable
DR with sufficient evidence references. Keep detailed analysis outside the HCRL;
use transient working material while developing it. A Report for completed review
activity does not imply a future application Report exists. Pending iterations
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
follow-up instructions at the associated iteration's task location, preserving
their correspondence, issuance event times and required timestamped filenames.

Prepare the one accepted-change patch increment under the existing release policy
and expected-current check; identify the release used for resulting-state
verification. An increment alone does not establish acceptance or publication.
Do not allocate another increment for a further iteration within that change set.

Implementation discoveries that materially affect architectural meaning SHALL
return to the applicable HCRL DR for review/adjudication before dependent work.
Continue independent authorised work where possible. Preserve uncertain effects
under AX-15 and missing architectural authority under AX-17.

## 5. Report and verify the incorporated result

Where an Open Exchange publication materially changes Archi organisation, the
generated model SHALL be imported into the supported Archi version and its actual
workbench tree structurally reviewed by the Solution Architect before HCRL
acceptance. Retain representative inspectable evidence for the exact publication
and imported native model. This complements Open Exchange validity, identity,
relationship and View preservation, deterministic generation and no-edit
round-trip verification; automated success and agent inspection do not supply
Solution Architect acceptance. This bounded safeguard applies the
[1.1.13 iteration 002 Task](../design/evidence/1.1.13/002/before/ai-agent-tasks/unresolved-archi-workbench-remediation.md#future-verification-safeguard).

A generated reader-facing architecture/navigation publication SHALL receive
Solution Architect structural review before HCRL acceptance when a change
materially alters architectural navigation or publication structure. Inspect
representative and structurally significant generated portions, including where
applicable Motivation, the Strategy capability hierarchy, Business Behaviour
contextual hierarchy, Application hierarchy, cross-cutting/reference material,
historical/superseded presentation and deep navigation numbering. This is a human
architectural checkpoint; automated checks and agent inspection provide evidence
but do not replace it. Publication size alone does not require exhaustive
page-by-page review. This bounded safeguard applies the Solution Architect's
[1.1.11 disposition 5](../design/evidence/1.1.11/002/before/ai-agent-tasks/unresolved-continuum-navigation-remediation.md#solution-architect-disposition-5--reader-facing-review-safeguard).

Retain formal Task Reports at the associated iteration's `after/ai-agent-reports/`,
including completed investigation/review and corrective/verification activity.
The Report is a finite record: it SHALL preserve sufficient factual activity,
outcome, verification, limitation, affected artefacts, unresolved matters, and
resulting Git state to establish what occurred. Correspondence SHALL support
reconstruction of the governed transition without imposing one-to-one
Task/Report file pairing.

Verification occurring as part of completing the authorised activity does not
require recursive amendment cycles or self-documenting reporting passes. Final
mechanical validation is evidenced by execution output, tooling return codes,
and Git state rather than requiring the Report to describe its own post-write
validation. Post-emission mechanical synchronisations, formatting, cross-reference
alignment, or linking execute within the same boundary and terminate cleanly.

Record when the Report is produced as an authoritative ISO 8601 timestamp with
`Z` or an explicit offset, its event meaning and attributable source where needed.
The attributable system timestamp recording emission of the completed Task
Report is the governed Report-Produced-At event; it asserts no internal
model-generation time or other unobservable event. Begin its filename with the
filesystem-safe sortable UTC timestamp derived from that same production time
and a descriptive name. A Report's production is
distinct from execution, validation, file metadata, later formalisation, review
and acceptance. Where a subsequent, separately issued substantive task directs
amendments to an already-produced Report across sessions or after review closure,
those amendments preserve material follow-up correspondence and SHALL NOT replace
the original production timestamp or the filename prefix derived from it.
Recover existing original times only from established source evidence; preserve
known precision and unresolved temporal provenance for
Solution Architect treatment before closure. Never infer missing times or order
from filesystem metadata, commit time, the current clock or sequence. Filename
ordering aids navigation and does not replace the authoritative event record.

Record actual activity/application, source/result correspondence, material validation
conclusions and limits in the applicable HCRL DR, with useful report references.
A report does not replace the semantic record. Distinguish proposed, performed
and incorporated work; PASS, FAIL, TIMEOUT, STALLED and UNRESOLVED; automated
verification, human review, publication and actual inspection/transport outcomes.
Use bounded execution and required repository checks. The read-only
[iteration evidence check][evidence-check] checks the selected iteration's layout
and conventional Markdown Task/Report event-time/prefix correspondence. It cannot
adjudicate evidence significance, attribution authenticity or architectural acceptance;
record its limits and any unresolved original times. Do not claim uncommitted
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

Mechanical completion, cross-referencing, and registration of the HCRL constitute
provenance maintenance within the existing boundary; completion terminates cleanly
at commit and resulting Git state without creating a new HCRL iteration or Task.
If during completion a new material architectural issue is discovered, execution
halts and the matter is surfaced for Solution Architect adjudication under
fail-closed governance (AX-17).

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
[provenance]: architecture-change-provenance.md#ai-agent-activity-provenance
[evidence-semantics]: architecture-change-provenance.md#working-material-evidence-and-hcrl
[iteration-003]: ../design/review/1.1.4/hcrl-1.1.4-003.pending.md
[iteration-004]: ../design/review/1.1.4/hcrl-1.1.4-004.pending.md
[evidence-check]: ../tooling/architecture_review_evidence.py
[template]: templates/hcrl-template.pending.md
[axioms]: architectural-axioms.md
[release-policy]: architecture-release-policy.md
[release-json]: ../../architecture-release.json
[authoring]: architecture-authoring-praxis.md
[roundtrip]: archimate-round-trip-engineering-praxis.md
[specialised-review]: archimate-ingestion-change-review.md
