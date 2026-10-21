<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia Architecture Governance

## Authority and adoption

This is the maintained common Architecture Review and HCRL governance contract.
It implements the Solution Architect dispositions in
[HCRL 1.1.2 iteration 002](../design/review/1.1.2/hcrl-1.1.2-002.pending.md),
authorised for application on 2026-10-21. Its adoption transition is
**RAD-1.1.1 → RAD-1.1.2**. Incorporation facts and the Solution Architect's
2026-10-21 confirmation, “result confirmed - complete”, are recorded in the
[completed HCRL](../design/register/1.1.2/hcrl-1.1.2-003.complete.md).
Architectural acceptance of the decisions, application and confirmation of the
actual result remain distinct.

The Solution Architect's [reviewed HCRL 1.1.3 iteration 001](../design/review/1.1.3/hcrl-1.1.3-001.pending.md)
and subsequent Apply instruction extend this contract with required formal
AI-agent Task/Report execution provenance and the common
[Architecture Review Praxis](architecture-review-praxis.md). Application facts
for RAD-1.1.2 → RAD-1.1.3 are recorded in
[pending iteration 002](../design/review/1.1.3/hcrl-1.1.3-002.pending.md).
The Solution Architect's [reviewed iteration 003](../design/review/1.1.3/hcrl-1.1.3-003.pending.md),
DR-6/DR-7, clarifies the review/execution boundary and requires durable event
timestamps and timestamped filenames. [Application iteration 004](../design/review/1.1.3/hcrl-1.1.3-004.pending.md)
records the corrections and then-unresolved temporal provenance. Its updated
Solution Architect table DR-9 expressly clarifies DR-7: attributable system
instruction-entry and completed-Report-emission timestamps are the governed
issuance/production events. Subsequent Report amendments preserve original
production time. [Application iteration 005](../design/review/1.1.3/hcrl-1.1.3-005.pending.md)
records recovery of the three times and filename mappings; final Solution
Architect confirmation of that incorporated result was supplied on 2026-10-21:
“result confirmed. Complete!” The [completed HCRL iteration 005](../design/register/1.1.3/hcrl-1.1.3-005.complete.md)
is the authoritative semantic record for RAD-1.1.2 → RAD-1.1.3. Completion belongs
to the same accepted change set and consumes no additional release.

The [Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md)
remain the highest architectural authority. This contract applies AX-04, AX-08,
AX-09, AX-14, AX-15 and AX-17: machinery supplies evidence, meaningful decisions
are preserved, working material serves its purpose, significant distinctions and
uncertainty remain explicit, and implementation cannot invent authority.
No axiom, runtime responsibility or native modelling semantics is changed.

[Architecture Change Provenance](architecture-change-provenance.md) is the single
representation-neutral transition-evidence contract. The
[release policy](../../docs/markdown/governance/architecture-release-policy.md)
and root [release authority](../../architecture-release.json) continue to control
allocation. Specialised contracts retain their specialised purpose, subject to
these common review and provenance rules.

## Architectural decision authority

The Architecture Review Board (ARB) is the collaborative architectural review and
adjudication forum. Mark Hunter, as Solution Architect, holds final architectural
decision and acceptance authority. Use **Solution Architect Disposition** for the
operative architectural decision; ARB participation does not imply an independent
Board approval mechanism.

ChatGPT acts as architecture peer/reviewer and may challenge, recommend, concur
or dissent, but cannot independently approve. Codex, Junie and other agents may
submit, analyse and implement authorised work, but cannot approve architecture or
self-declare architectural closure. Material peer disagreement may be retained
where useful. Consensus is desirable, not required. There is no voting, quorum
or majority mechanism. Delegation of Solution Architect authority requires a
separate explicit governance decision.

## Review coverage and candidate judgement

Every Harmonia change SHALL receive Architecture Review, including architecture,
features, backlog, implementation, bugs, patches and refactoring. A change's label
does not exempt it or itself reduce the Praxis. The
[Architecture Review Praxis](architecture-review-praxis.md) applies to every
change. The extent of review activity and evidence SHALL be proportionate to the
scope, complexity, uncertainty and architectural materiality identified through
review. Review does not imply material architectural effect.
Submitters use architectural judgement to distinguish material matters from
minutiae/non-material implementation change; ARB/SA adjudicates that assessment.

Submit useful candidates and state uncertainty rather than withhold a potentially
material matter because classification or interpretation is incomplete. Explain
the proposed meaning, relevant authority, evidence and consequences sufficiently
for adjudication. ARB corrections become precedent for better judgement; learning
never delegates architectural authority. No additional compulsory knowledge
schema is established by this contract.

Anyone authorised to implement Harmonia is expected to recognise when the work
changes, contradicts, extends, clarifies or otherwise materially affects
architectural intent. Such discoveries SHALL be surfaced through HCRL for ARB
adjudication. Implementation is a legitimate discovery mechanism, not independent
architectural authorisation. Ordinary detail without architectural effect needs
normal HCRL/minutiae review, without separate material treatment.

For formal AI-agent Tasks directing execution of an identifiable
adjudicated/authorised change, Task Details and corresponding execution Reports
SHALL be retained as execution provenance at the governed task/report locations
below, with authoritative issuance/production timestamps and filenames beginning
with sortable UTC timestamps derived from those events, in accordance with
[Architecture Change Provenance](architecture-change-provenance.md#ai-agent-execution-provenance).
Retention is required irrespective of architectural materiality. Their review
treatment is normally minutiae unless materially architectural; material content
belongs in the applicable substantive DR. Retaining the formal Task/Report does
not require retaining every prompt, conversation, tool invocation, working log
or other agent interaction. Investigation, analysis, candidate preparation and
pending-HCRL preparation/revision are Architecture Review provenance, preserved
through the HCRL lifecycle and applicable materiality/Minutiae treatment. They do
not become execution provenance through agent participation or contribution to
the same transition. No separate formal review Task/Report convention is created.

Git is authoritative for source-code history. Do not reproduce implementation
source as architectural evidence merely for historical completeness. A bounded
code exemplar may be retained where it materially explains a decision, principle,
pattern, constraint or design.

## HCRL as the common Decision Review Log

HCRL SHALL be the single authoritative semantic change/review record for each
governed RAD release transition. A completed HCRL records the authorised semantic
transition from the immediate governed predecessor to the resulting release.
It governs that transition, not every intermediate edit or processing event.
A transition without material architectural effect still receives appropriate
review, including the bounded Minutiae mechanism where applicable.

Specialised review mechanisms remain authoritative for their discovery, analysis,
validation and presentation purposes. They produce relevant review material into
HCRL, without competing final acceptance records. Material findings requiring
adjudication SHALL be represented in the applicable Decision Review. Minor detail
may be represented as minutiae/supporting evidence. Preserve useful specialised
review/CR references, instructions and validation distinctions.

The beginning of each HCRL SHALL contain one Decision Review table with columns:

```text
Decision Review | Subject | Solution Architect Disposition | Comment
```

Number top-level matters `DR-1` through `DR-X`, each with a corresponding
`## DR-1 — <Subject>` section. Describe the matter with sufficient context and
detail for ARB adjudication. Independently adjudicable subordinate matters may use
one level, such as `DR-4.01`, with corresponding subordinate sections. No further
Decision Review nesting is permitted; a matter needing deeper decomposition
becomes another top-level DR.

Do not create parallel matter, knowledge, action, uncertainty or minutiae decision
taxonomies. Relevant proposals, uncertainties, consequences, evidence,
implementation implications, dependencies and provenance belong in their
applicable DR. Source adjudication labels may identify inputs; they do not become
another operating decision structure. DR references are scoped review references,
not canonical architectural identities.

Record the attributable Solution Architect disposition and comment against the
reviewed matter/content. Preserve material revisions and their reasons. Pending
fields, submitter classification, elapsed time, successful tooling and processing
outcomes supply no acceptance. Do not replace a recorded human disposition with
an agent-generated lifecycle status. Application remains bounded by the actual
decision, clarification, conditions, exclusions and dependencies.

The [HCRL template](templates/hcrl-template.pending.md) implements this structure;
its placeholders establish no decision or executable authority.

## Bounded Minutiae

Supporting/minor matters SHALL be disclosed as a bounded Minutiae group within
the Decision Review structure. Use one control for that group:

```text
Minutiae-Accepted: Yes | No
Comment: <Solution Architect / ARB comment>
```

Yes accepts the disclosed bounded group. No identifies required correction,
escalation or reclassification. Silence never means acceptance. Agent
classification does not supply architectural authority. An unambiguous recorded
Solution Architect acceptance of the identified group may be reflected in this
control with its source attribution; an agent must not enlarge the accepted scope.
Subsequent HCRL iterations incorporate adjudication and improve classification.
Do not use `ACCEPTED_WITHOUT_COMMENT` as a per-item disposition. The table entry
and group control describe the same decision, not separate approval mechanisms.

## Repository structure and migration boundary

The accepted target structure is:

```text
enterprise-architecture/
├── continuum/
│   ├── <Enterprise/Solution Architecture content>
│   └── documentation/
│       └── published/
│           └── <LaTeX publication>
├── design/
│   ├── archimate/
│   ├── review/<version>/
│   ├── register/<version>/
│   └── evidence/<version>/
│       ├── before/
│       │   └── ai-agent-tasks/
│       └── after/
│           └── ai-agent-reports/
└── governance/
```

The version is the resulting architecture release. `continuum/` contains the
governed Enterprise/Solution Architecture; Markdown is its representation format,
not its architectural taxonomy. The publication location holds LaTeX publication
material. `design/` holds architecture design/change lifecycle material. The
existing ArchiMate strategy remains authoritative beneath `design/archimate/`;
restructuring does not authorise redesigning it.

Material not fully approved, including partially approved artefacts requiring
another iteration, belongs in `design/review/<version>/`. Approved architectural
artefacts/design records belong in `design/register/<version>/`. Partially
approved artefacts do not enter the register. Required evidence belongs in the
before/after locations under the provenance contract.

`before/ai-agent-tasks/` and `after/ai-agent-reports/` are the governed locations
for formal AI-agent Tasks directing execution of an identifiable
adjudicated/authorised change and corresponding execution Reports in that
transition. They represent task-input and execution-result roles, respectively;
they do not assert that a contained artefact existed in the predecessor or
successor Git state, or that a result is accepted. RAD-1.1.3 is the first
prospective use of this convention. Other required transition evidence retains
its appropriate before/after placement.

Use `hcrl-<target-version>-<iteration>.pending.md` and
`hcrl-<target-version>-<iteration>.complete.md`. The completed HCRL belongs in the
register. Iterations are not releases or processing-run identities.

Adoption of this structure does not itself authorise repository migration.
Migration requires a separately established bounded source-to-destination mapping
and application authority preserving standing, identity, correspondence and
specialised semantics. Existing architecture, specialised contracts, machinery
and publications remain at their existing paths pending that work. This
installation creates the new common governance sources and reconciles current
references in place; it does not move the existing corpus or certify it as a
completed continuum.

## Governance and architectural knowledge

Governance machinery, rules and context belong in
`enterprise-architecture/governance/`. Durable governance knowledge SHALL NOT be
manufactured into ArchiMate architectural knowledge merely because it is durable.
There is no general requirement for governance material to have a corresponding
continuum entry or ArchiMate element.

For knowledge genuinely part of Enterprise/Solution Architecture, ArchiMate
together with governed Harmonia specialisations provides the taxonomy. The
Enterprise Architecture Continuum SHALL align with the ArchiMate metamodel.
Specialisations reside within their governing native concept's structure.
Submitters propose applicable native-concept or governed-specialisation tags;
ARB may accept, reclassify or reject them. Accepted classification determines
continuum placement. Agents SHALL NOT invent architectural types or
specialisations to accommodate content; a new type/specialisation itself needs
adjudication. The continuum and Archi model should provide corresponding
navigation over that governed architecture.

No independent documentation taxonomy or separate architectural-knowledge master
index is established. Domain13 does not become the EA master catalogue.
Architectural content ultimately follows what architectural thing it is, rather
than historical documentation convenience. Governance follows its governance
location. Existing specialised native-type and modelling semantics remain binding;
folder placement does not establish architectural relationships or identity.

## Completion, correction and pending provenance

HCRL becomes complete only when the Solution Architect confirms that it accurately
represents both the authorised change and the result actually incorporated.
Explicitly deferred/excluded matters do not prevent completion when their treatment
is recorded. Agents may report implementation and validation facts but cannot
confirm architectural closure.

A completed HCRL is immutable. A genuinely non-semantic correction may use an
attributable erratum; a material correction/change requires a subsequent governed
HCRL transition. Detailed erratum mechanics SHALL be established when an actual
requirement arises; no speculative carrier, procedure or machinery is installed.

Pending iterations are working review provenance and SHALL be retained while
needed for active review and traceability. The completed HCRL is the durable
authoritative transition record. There is no requirement to retain every
superseded pending iteration indefinitely. Retention/disposal may be determined
pragmatically after completion provided required evidence and attributable
decision history are not lost. This does not authorise loss of needed recovery
material or unrelated processing cleanup.

## Prospective operation and release policy

HCRL governance applies prospectively from this adoption. No retrospective HCRL
reconstruction or historical reconciliation is required. The historical
1.0.10 → 1.1.0 pilot is outside this transition and SHALL NOT be investigated,
completed, cancelled or otherwise reconciled unless a future specific
architectural need requires it. No old evidence, frozen claim or unknown
correspondence is rewritten to imply retrospective compliance.

Every accepted repository change set continues to receive the patch increment
under the existing release policy. Pending iterations do not consume releases;
generation does not allocate one. Major/minor changes require an explicit
governance decision. Recheck the immediate accepted predecessor before acceptance;
do not treat renaming an HCRL after baseline drift as renewed approval.
