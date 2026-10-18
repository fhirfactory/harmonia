<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| ID | Brief Description | Disposition | Outcome |
| --- | --- | --- | --- |
| [CR-001](#cr-001--example-proposal-awaiting-adjudication) | Example proposal awaiting adjudication | On-Hold | Unresolved |
| [CR-002](#cr-002--example-approved-bounded-change) | Example approved bounded change | Accept | Unresolved |
| [CR-003](#cr-003--example-declined-proposal) | Example declined proposal | Reject | Resolved |
| [CR-004](#cr-004--example-change-requiring-human-resolution) | Example change requiring human resolution | Manual Intervention | Unresolved |
| [CR-005](#cr-005--example-finding-resolved-by-governing-decision) | Example finding resolved by governing decision | On-Hold | Resolved |

# ArchiMate Ingestion Change Review — template and worked form

**EXAMPLE ONLY.** These generic entries demonstrate all four Dispositions and independent Outcomes; they
establish no Harmonia architecture or executable authority. When instantiating,
replace examples/placeholders with actual evidence, remove unused examples, and
initialise **every newly generated CR to Disposition On-Hold and Outcome
Unresolved**, with a descriptive/evidence-referencing **Outcome Reason**,
**Not authorised** and **Not yet processed**. Only an explicit recorded Solution
Architect instruction may revise a previously issued Disposition; retain its audit
history. A later Outcome does not rewrite the call to action. No placeholder-bearing instruction is executable.

Follow the [governed contract](../archimate-ingestion-change-review.md).
Keep the linked summary as the first visible content, with one phrase of at most
80 characters per description. Allocate stable review-local `CR-nnn` IDs; keep
anchors stable and summary/detail Disposition and Outcome values synchronised. These IDs are not
Canonical IDs. Detailed CRs may be arbitrarily long. Omit meaningless empty
subsections while keeping evidence, proposal, questions, Disposition, Solution
Architect Decision/rationale/authority/adjudicator/date, Outcome, Outcome Reason,
Authorised Reconciliation Instruction and processing record distinguishable.
Disposition is a call to action; Outcome records what happened to the CR. Use only
Unresolved/Resolved for Outcome; no Disposition determines it automatically.

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

**Disposition:** On-Hold

**Outcome:** Unresolved

**Outcome Reason:** The bounded architectural question below awaits Solution Architect authority; no evidence yet establishes resolution.

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

**Solution Architect Decision:** Initial/default On-Hold; no acceptance, rejection
or manual-resolution adjudication recorded. Adjudicator/date and rationale for
further adjudication: not yet recorded. Applicable authority: `<reference or identified gap>`.

### Related Change Requests

`<Informational links, required prerequisite CR and exact required state, or none>`.
Illustrative relationship only: this CR may require CR-002's resulting state;
record the actual dependency rather than assuming acceptance establishes that state.

### Authorised Reconciliation Instruction

Not authorised while Disposition is On-Hold, including after resolution by a governing decision.

### Reconciliation / Processing Record

Not yet processed.

<a id="cr-002--example-approved-bounded-change"></a>

## CR-002 — Example approved bounded change

**Disposition:** Accept

**Outcome:** Unresolved

**Outcome Reason:** The recorded Accept instruction authorises only the bounded operation below; the required resulting state and validation are not yet evidenced.

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

**Disposition:** Reject

**Outcome:** Resolved

**Outcome Reason:** In this worked case the recorded rejection decision below resolves the bounded proposal, with no further required handling outstanding. Reference its rationale/authority and closure evidence; Reject alone does not manufacture this Outcome.

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

**Disposition:** Manual Intervention

**Outcome:** Unresolved

**Outcome Reason:** The instructed human-controlled resolution and its completion/validation evidence are outstanding.

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


<a id="cr-005--example-finding-resolved-by-governing-decision"></a>

## CR-005 — Example finding resolved by governing decision

**Disposition:** On-Hold

**Outcome:** Resolved

**Outcome Reason:** Resolved by Governing Decision `<exact RADS authority/anchor,
release/date and applicability>`; the general rule answers the original bounded
interpretation question without acceptance or reconciliation of a related instance.

### Proposed Architectural Change

**Candidate Architectural Interpretation:** `<original proposed interpretation>`.
This was a valid finding exposing missing architecture, retained without rewriting
it to resemble a later decision.

### Detected Evidence

**Observed Evidence:** `<original immutable observation, input hash and locator>`.

### Architectural Questions / Adjudication

Original question: `<meaning unresolved when detected>`.

**Solution Architect Decision:** The original On-Hold call to action remains
recorded with `<original adjudicator/date/rationale, or explicit default basis>`.
Subsequent governing decision: `<exact authority, adjudicator/date and rationale>`.
Record the Outcome update actor/date in the review revision evidence. The common
question is answered by that rule; no separate instance interpretation decision
is needed. Do not infer acceptance of any related proposal.

### Related Change Requests

`<related instance CR and required governed state if later reconciliation is
independently authorised>`. This is not a prerequisite for resolution of the
interpretation question. A Resolved Outcome does not supply a governed endpoint
or relationship for another CR.

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed. The finding is resolved by architecture authority without
reconciliation; no execution is claimed. Retain the prior unresolved Outcome,
issued Disposition and evidence in review history.
