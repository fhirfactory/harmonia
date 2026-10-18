<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| ID | Brief Description | Status |
| --- | --- | --- |
| [CR-001](#cr-001--example-proposal-awaiting-adjudication) | Example proposal awaiting adjudication | On-Hold |
| [CR-002](#cr-002--example-approved-bounded-change) | Example approved bounded change | Accept |
| [CR-003](#cr-003--example-declined-proposal) | Example declined proposal | Reject |
| [CR-004](#cr-004--example-change-requiring-human-resolution) | Example change requiring human resolution | Manual Intervention |

# ArchiMate Ingestion Change Review — template and worked form

**EXAMPLE ONLY.** These generic entries demonstrate all four dispositions and
establish no Harmonia architecture or executable authority. When instantiating,
replace examples/placeholders with actual evidence, remove unused examples, and
initialise **every newly generated CR to On-Hold**, with **Not authorised** and
**Not yet processed**. Only recorded Solution Architect adjudication may change
disposition. No placeholder-bearing instruction is executable.

Follow the [governed contract](../archimate-ingestion-change-review.md).
Keep the linked summary as the first visible content, with one phrase of at most
80 characters per description. Allocate stable review-local `CR-nnn` IDs; keep
anchors stable and summary/detail Status values synchronised. These IDs are not
Canonical IDs. Detailed CRs may be arbitrarily long. Omit meaningless empty
subsections while keeping evidence, proposal, questions, decision, instruction
and outcome distinguishable.

## Review basis

- Review/run: `<baseline-release>/run-<nnn>`; review revision: `<revision>`.
- Prepared by / date: `<actor / date>`.
- Incoming export: `<immutable path or retained snapshot>`; SHA-256: `<hash>`.
- Governed comparison baseline / release: `<path / release>`; SHA-256: `<hash>`.
- Target RADS/source revision: `<revision and applicable source references>`.
- Transport control, if used: `<path / hash / why applicable>`.
- Detection: `<methods, tool versions, commands and detection evidence links>`.
- Evidence limits: `<missing snapshots, unknown provenance or explicit none>`.
- Run evidence: `detection/`, `reconciliation/`, `validation/` (paths are relative
  to the instantiated run, not this governed template).

### Decomposition record

Explain why each proposal can be independently adjudicated. Map observations to
CRs; identify shared evidence and observations retained only as transport findings
or unresolved evidence. Do not count XML records or GUI actions as CRs. Record
any subsequent split/correction with the original IDs and history retained.

| Observation/evidence locator | CR(s) supported | Decomposition reason / evidence-only treatment |
| --- | --- | --- |
| `<exact record(s), fields, references, View objects or evidence file>` | `<CR-nnn or none>` | `<why one decision, separate decisions, or evidence only>` |

<a id="cr-001--example-proposal-awaiting-adjudication"></a>

## CR-001 — Example proposal awaiting adjudication

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** `<bounded proposed architectural
change, distinct from the observed record(s)>`.

### Detected Evidence

**Observed Evidence:** `<exact incoming record/field and before-state where
available; native type, source/target or presentation reference as relevant>`.
Reference the pinned input and `detection/` evidence; distinguish native Names,
Properties, semantic relationships, organisations and visual objects. State
unavailable evidence explicitly. Do not rewrite this observation after a decision.

### Architectural Questions / Adjudication

Question: `<what existing authority does not establish>`.

**Solution Architect Decision:** No final disposition. Adjudicator/date and
rationale: not yet recorded. Applicable authority: `<reference or identified gap>`.

### Related Change Requests

`<Informational links, required prerequisite CR and exact required state, or none>`.
Illustrative relationship only: this CR may require CR-002's resulting state;
record the actual dependency rather than assuming acceptance establishes that state.

### Authorised Reconciliation Instruction

Not authorised while status is On-Hold.

### Reconciliation / Processing Record

Not yet processed.

<a id="cr-002--example-approved-bounded-change"></a>

## CR-002 — Example approved bounded change

**Status:** Accept

**Example of the completed form after adjudication; not an actual decision.**

### Proposed Architectural Change

**Candidate Architectural Interpretation:** `<one bounded architectural change>`.

### Detected Evidence

**Observed Evidence:** `<pinned incoming evidence and exact source/target records;
governed before-state and target source locators; no name-based identity match>`.

### Architectural Questions / Adjudication

Question/authority: `<question resolved, or existing rule and applicability>`.

**Solution Architect Decision:** Accept only `<precise instance and meaning>`.
Rationale / governing references: `<decision and authority>`.
Solution Architect / date: `<name / date>`.
Resulting instruction revision / decision reference: `<revision or retained
digest / recorded adjudication>`.
This instance decision establishes no universal modelling/inference rule.

### Related Change Requests

`<Required CR IDs and resulting governed state/evidence, or explicit none>`.
Related information is not automatically an execution prerequisite. Do not execute
until each actual prerequisite has produced the required evidenced state.

### Authorised Reconciliation Instruction

**Example instruction pattern; complete from the recorded decision before execution:**

Apply only `<approved operation and bounded meaning>` to `<exact governed target
source(s)/element(s)>`, using `<pinned evidence locators>`. The resulting state
SHALL be `<precise approved result>`.

For a semantic relationship, specify `<relationship identity, approved native
type, exact source and target, direction and interpretation>`. For an authorised
View-only change, reference `<existing relationship, View and visual endpoints>`
and state the authorised presentation treatment without creating a semantic edge.
Remove these conditional prompts when irrelevant.

Apply `<independently governed identity/naming treatment and authority>`;
organise `<approved homes/navigation>` and change `<authorised View treatment,
if any>`. Preserve `<explicit unaffected scope/constraints>`. Do not infer extra
context, Composition or universal rules. Verify `<expected state, applicable
reference/identity/schema/semantic checks and evidence required>`.

Stop affected execution and return ambiguities or baseline drift for Solution
Architect adjudication; record any partial effects. The execution agent cannot
broaden or improve this decision.

### Reconciliation / Processing Record

Not yet processed. Architectural disposition remains Accept independently of
execution and validation outcomes.

When processed, retain the actual entry and evidence for each attempt:

- Instruction revision used; executing actor/date; automatic or manual handling.
- Prerequisite checks and observed governed target state.
- Before/after paths, revisions/hashes and actual changes; commands/results.
- Validation criteria and results with evidence links under `validation/`.
- Unresolved/partial effects, deviations and returned adjudication questions.
- Reconciliation evidence under `reconciliation/`; publication reference if applicable.

<a id="cr-003--example-declined-proposal"></a>

## CR-003 — Example declined proposal

**Status:** Reject

### Proposed Architectural Change

**Candidate Architectural Interpretation:** `<independent proposed change>`.

### Detected Evidence

**Observed Evidence:** `<unchanged submission and pinned detection evidence>`.

### Architectural Questions / Adjudication

Question/authority: `<required adjudication or existing rule>`.

**Solution Architect Decision:** Reject `<scope>`. Rationale/authority:
`<reason and reference>`. Solution Architect/date: `<name / date>`.
Retain the original proposal and evidence; rejection does not erase submission.

Related CRs/dependencies: `<links and impact on dependent CRs, or none>`.

### Authorised Reconciliation Instruction

Not authorised. Do not reconcile this rejected proposal.

### Reconciliation / Processing Record

Not yet processed. Retain subsequent handling evidence; do not record rejection
as successful reconciliation or silently execute dependent changes.

<a id="cr-004--example-change-requiring-human-resolution"></a>

## CR-004 — Example change requiring human resolution

**Status:** Manual Intervention

### Proposed Architectural Change

**Candidate Architectural Interpretation:** `<bounded proposed change>`.

### Detected Evidence

**Observed Evidence:** `<pinned observations and relevant current governed state>`.

### Architectural Questions / Adjudication

Question/authority: `<what required the decision>`.

**Solution Architect Decision:** Automated reconciliation SHALL NOT execute this
CR. Reason: `<why automation is unauthorised or insufficient>`.
Solution Architect/date; rationale/authority: `<name / date / references>`.
This is a decision requiring deliberate human-controlled action, not On-Hold.

### Related Change Requests

`<Dependencies, required governed state and affected dependent CRs, or none>`.
Unresolved manual intervention blocks dependent execution that needs its result.

### Authorised Reconciliation Instruction

No automatically executable reconciliation instruction is required or authorised.

Human-controlled resolution required: `<known action and responsible human role;
unknown details remain explicit>`.
Completion evidence and validation: `<how the actual result will be established>`.
An agent SHALL NOT convert this into an automatic operation.

### Reconciliation / Processing Record

Not yet processed. After manual action, record who acted and when, what was
actually done, the instruction/decision revision, before/after evidence, validation
and remaining uncertainty. Link retained `reconciliation/` and `validation/`
evidence. Evidence of the required governed state may unblock dependent CRs;
the Manual Intervention disposition does not itself authorise automation.
