<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia Architecture Change Provenance

## Authority and scope

This is the single maintained, representation-neutral contract for architecture
transition evidence, installed under the Solution Architect's
[HCRL 1.1.2 iteration 002](../design/review/1.1.2/hcrl-1.1.2-002.pending.md)
dispositions and application instruction. The adoption transition and the
Solution Architect's result confirmation are recorded in the
[completed HCRL 1.1.2 iteration 003](../design/register/1.1.2/hcrl-1.1.2-003.complete.md).
The [common governance contract](architecture-governance.md)
defines review authority, HCRL structure, lifecycle and prospective adoption.
The [Architectural Axioms](architectural-axioms.md)
remain controlling; AX-04, AX-08, AX-09, AX-14, AX-15 and AX-17 apply.

The Solution Architect's [reviewed HCRL 1.1.3 iteration 001](../design/review/1.1.3/hcrl-1.1.3-001.pending.md)
and subsequent Apply instruction establish the required formal AI-agent
execution-provenance convention then in force, first used for RAD-1.1.3. The common
[Architecture Review Praxis](architecture-review-praxis.md) applies this contract.
[Pending iteration 002](../design/review/1.1.3/hcrl-1.1.3-002.pending.md) preserves
the initial application facts. The Solution Architect's
[reviewed iteration 003](../design/review/1.1.3/hcrl-1.1.3-003.pending.md), DR-6/DR-7,
clarifies execution scope and requires event timestamps and timestamped filenames.
[Application iteration 004](../design/review/1.1.3/hcrl-1.1.3-004.pending.md)
records the corrections and then-unresolved temporal provenance. The Solution
Architect's updated iteration 004, table DR-9 expressly clarifying DR-7, and its
Apply instruction establish the observable event meanings below. [Application
iteration 005](../design/review/1.1.3/hcrl-1.1.3-005.pending.md) records the
three recovered times and filename mappings. Mark Hunter, Solution Architect,
confirmed the incorporated result on 2026-10-21: “result confirmed. Complete!”
The [completed HCRL iteration 005](../design/register/1.1.3/hcrl-1.1.3-005.complete.md)
is the authoritative RAD-1.1.2 → RAD-1.1.3 transition record; completion allocates
no additional release.

The operative 004 Task, retained with [HCRL 1.1.4 iteration 004](../design/review/1.1.4/hcrl-1.1.4-004.pending.md),
authorises prospective implementation of the iteration-scoped model demonstrated
by [iteration 003](../design/review/1.1.4/hcrl-1.1.4-003.pending.md), DR-13/DR-14.
From 004, the rules below explicitly supersede the RAD-1.1.3 execution-only
Task/Report scope and release-level evidence placement. They preserve its
attributable event-time requirements and the Solution Architect's authority.
003 demonstrated/refactored the structure; it did not install this Praxis.
Historical records retain their actual rules and standing; no retrospective
compliance or migration approval is asserted.
Mark Hunter, Solution Architect, subsequently confirmed the bounded 004 result
on 2026-10-21: “result confirmed, complete!” The [completed HCRL 004](../design/register/1.1.4/hcrl-1.1.4-004.complete.md)
records that confirmation with its attributable event and explicit migration
deferrals. Completion consumes no additional release; the substantive migration
remains reserved for 005.

This contract governs authorised transitions through Markdown, ArchiMate and
other representations. It does not redefine runtime provenance, Kleio audit,
diagnostic retention, software test reporting or release allocation.
The [pre-adoption contract](../../docs/markdown/governance/architecture-change-provenance.md)
is preserved in place as superseded historical text; its old package obligations
are not the current evidence contract.

## Governed transition and semantic record

Architectural evidence SHALL support understanding, verification and preservation
of the authorised incremental transition between identifiable governed states.
It SHALL NOT accumulate execution history merely because machinery participated.

The governing principle of Harmonia architecture change provenance is:

> **Provenance records describe the governed activity, not the mechanical production history of the provenance records themselves.**

Provenance frameworks capture meaningful, attributable governance boundaries rather
than recursively documenting their own documentation. Detailed mechanical history of
edits to governance artefacts is evidenced through Git history, execution sessions,
and tooling outputs, not through recursive provenance records.

The completed HCRL is the single authoritative semantic change/review record for
the immediate governed predecessor → resulting governed RAD release. It records
the authorised meaning, actual incorporated result, attributable decisions and
material revisions, scope, conditions, dependencies, exclusions, deferrals,
relevant correspondence and validation conclusions/limits within the applicable
Decision Reviews. Specialised reviews contribute their relevant material without
becoming competing final acceptance records.

Before applying a change, identify the actual originating state, applicable
authority, permitted scope and original content. Before closure, identify the
actual resulting state and account for differences in that scope. A commit,
version label, successful command or publication alone does not establish
architectural acceptance. Unknown effects remain explicit until resolved or
given an explicit governed treatment.

## Working material, evidence and HCRL

**Working material** is transient material used while performing an activity.
It is discardable when its purpose has ended and SHALL NOT become governed
evidence merely because it existed or an agent produced it. Preserve material
still needed for active recovery, validation or unresolved dependencies under
the processing rules below; this distinction supplies no unrelated cleanup authority.

**Evidence** is durable inputs, outputs or supporting material retained because
they materially support, explain, reproduce or verify the governed activity
associated with an HCRL iteration. Investigation and review can produce evidence:
purpose-selected mappings, reference inventories, checks and similar substantive
outputs SHALL be retained where they materially support that activity. Production
by an agent or machinery alone does not require retention of an intermediate artefact.

**Substantive governed activity** is activity that changes, analyses, reconciles,
proposes or validates architectural or solution content in a manner requiring
governed evidence. Substantive activity initiates or advances an authorized
governance lifecycle and produces formal evidence.

**Provenance maintenance** is the mechanical creation, completion, correction,
formatting, cross-referencing, timestamp synchronization or verification of the
Task, Report, HCRL and related governance artefacts required to document an
already-authorised substantive activity within an existing provenance boundary.
Provenance maintenance performed within an authorized provenance boundary SHALL NOT
recursively create another Task, Report or HCRL iteration. A new provenance boundary
is required only when new substantive governed activity is initiated.

**Substantive discovery escalation (Fail-closed):** The provenance maintenance
exemption is strictly bounded to mechanical operations on governance records. If an
agent or practitioner discovers a new material architectural issue, invariant
tension, or unintentional code change while completing provenance, that finding
SHALL NOT be resolved or dismissed as provenance maintenance; it SHALL be surfaced
through HCRL for Solution Architect adjudication under AX-17.

**HCRL** is the authoritative semantic change/decision record for the governed
RAD transition. Pending iterations preserve its review/adjudication lifecycle;
the completed HCRL records the accepted transition. HCRL SHALL NOT serve as the
Architecture Review working document, an inventory of every evidence file or a
substitute for detailed supporting evidence. Include material meaning, decisions,
application/validation conclusions and sufficient evidence references for
traceability in the applicable DR; retain supporting detail in evidence.

## Selecting required evidence

Select before/after evidence where it materially contributes to understanding,
verifying or preserving the governed activity and architectural transition.
Evidence is not accumulated for historical completeness. Material adequately preserved by source
control need not be duplicated. A processor reading a source does not make a
copy of it architectural evidence.

For substantive AI-agent activity associated with an HCRL iteration, formal Tasks
and corresponding Reports are required evidence under the rule below, irrespective
of architectural materiality. This includes investigation, analysis, review,
candidate/pending-HCRL preparation or revision, corrective work, application and
verification. These purposes do not establish different provenance models. The
selection rules govern additional evidence; they do not make these formal
Task/Report records optional or require retention of every agent interaction.

Required before/after evidence belongs at
`enterprise-architecture/design/evidence/<RAD-version>/<HCRL-iteration>/before/`
and `after/`. The HCRL iteration is the provenance boundary. Use the resulting
governed RAD version and corresponding HCRL iteration (for example `1.1.4/004`),
independently of agent, session, software version or processing-run identity.
Retain evidence with its originating activity; later iterations reference it
without moving it or duplicating it merely to populate a new package. Preserve
source/result correspondence for any purpose-required later representation.
The old mandatory `before.tar.gz`, `after.tar.gz`, `change-review.md`
package contract is superseded. There is no universal archive, hashing or
duplicate exact-byte snapshot requirement. These directories do not require
unnecessary copies or empty package artefacts when source control and retained
governed content already supply the required evidence.

Git remains authoritative for source-code history. Do not reproduce implementation
source merely to make architectural evidence historically complete. A bounded
code exemplar may be retained only where it materially assists understanding of
a decision, principle, pattern, constraint or design. Record that bounded purpose.

<a id="ai-agent-execution-provenance"></a>
## AI-agent activity provenance

For substantive AI-agent activity associated with an HCRL iteration, the formal
Task Detail(s) and corresponding Report(s) SHALL be retained at these governed
locations, under the same model for all activity purposes:

```text
enterprise-architecture/design/evidence/<RAD-version>/<HCRL-iteration>/
├── before/
│   └── ai-agent-tasks/
└── after/
    └── ai-agent-reports/
```

The legacy execution-provenance anchor above remains a navigation alias only;
it establishes no narrower rule. Investigation/review and authorised application
have different authority and outcome semantics, but share this provenance model.
An instruction to investigate, review or prepare does not approve a proposed change.

`before/` and `after/` represent activity-input and activity-output roles; they do
not assert that a contained artefact existed in the predecessor or successor
Git state. Follow-up tasks remain task inputs. Interim, unsuccessful or uncertain
results retain their actual standing; location supplies neither success nor
architectural acceptance.

The Task SHALL preserve the operative issued instruction and sufficient
attributable context to establish what work was authorised or requested,
including relevant scope, constraints and source/review references. Preserve
material corrections or follow-up instructions and their correspondence to the
earlier instruction. An agent summary does not replace available issued wording;
unknown attribution or missing source content remains explicit.

The Report SHALL preserve sufficient factual activity, outcome, verification,
limitation and correspondence information to establish what occurred. Identify
the corresponding task(s), affected source/result paths or adequate source-control
locators, departures from requested scope and unresolved effects. Distinguish
proposed, performed and incorporated work and PASS, FAIL, TIMEOUT, STALLED and
UNRESOLVED outcomes. Task-to-report correspondence SHALL be adequate to reconstruct
the governed transition; one-to-one file pairing is not required.

Every required formal Task and Report SHALL contain an authoritative event
timestamp: when the operative Task instruction was issued and when the Report
was produced, respectively. Record an unambiguous ISO 8601 timestamp with UTC
`Z` or an explicit offset, its event meaning and attributable source where
required. `Task-Issued-At` and `Report-Produced-At` are suitable labels. Preserve
known precision; UTC to at least seconds is recommended for newly captured events.

For Harmonia AI-agent activity provenance, the attributable system timestamp
recording the operative Task instruction entering the agent interaction SHALL
constitute the governed `Task-Issued-At` event. The attributable system timestamp
recording emission of the completed Task Report SHALL constitute the governed
`Report-Produced-At` event. These are the authoritative observable provenance
events; they do not assert an earlier human composition time, internal
model-generation time or another unobservable event. Preserve the event timestamp
and attributable source in the formal record so that a local interaction log is
not a continuing dependency for understanding the retained evidence.

The filename SHALL begin with a filesystem-safe sortable UTC timestamp derived
from the same authoritative event time, followed by a descriptive name; for
example, `YYYYMMDDTHHMMSSZ-<descriptive-name>.md`. Preserve any finer precision
in the record. Filename ordering is navigational convenience, not the authoritative
event record or proof of strict order where precision, ties or clock uncertainty
prevent it. A separate receipt/capture or later recording time SHALL NOT replace
the governed instruction-entry event. Report production is distinct from
execution start/end, validation, file creation or modification, later
formalisation, review and acceptance. The Report is a finite record of the
substantive activity performed, outcome, validation, affected artefacts,
unresolved matters and resulting Git state.

Mechanical synchronisation, formatting, cross-reference alignment, and post-emission
verification checks executed within the authorized boundary complete that same Report
and SHALL NOT instantiate recursive amendment-logging cycles or require the Report
to document its own post-write validation. Where a subsequent, separately issued
substantive instruction directs amendments to an already-produced Report across
sessions or after formal closure, those amendments preserve material follow-up
correspondence and SHALL NOT replace the original production timestamp or the
filename prefix derived from it.

For existing records, recover event times from established source evidence where
possible and identify that source. Do not guess event times from filesystem
metadata, commit time, the current clock or inferred sequence. Where the original
event time cannot be established, preserve known precision and explicitly record
unresolved temporal provenance for Solution Architect treatment before closure.
A missing authoritative time also leaves the required filename prefix unresolved;
do not manufacture a prefix or claim full temporal conformance. A later capture
or formalisation time may be recorded with its actual meaning, separately.

Maintain readable formal records and references adequate to identify their tasks,
reports and applicable HCRL DRs. Markdown is suitable. These event fields and
filename prefixes establish no canonical architectural identity or agent-specific
identity regime; no additional universal record schema is required.
Preserve source/result correspondence when content is copied or represented in a
formal record. Git may preserve repository-held versions without duplicate
archives; a reference to missing or unrecoverable content cannot replace the
required record.

Formal Task/Report retention is required irrespective of materiality; their
architectural significance and review treatment remain subject to the common
materiality, evidence and bounded Minutiae rules. Material architectural content,
decisions, scope changes, application facts and validation conclusions SHALL be
surfaced in the applicable HCRL DR. A Task/Report is not an alternative
architectural decision record. An attributable Solution Architect decision
contained in a task retains its actual authority and conditions; an agent's
report, interpretation or directory placement confers no architectural authority.

Retaining formal Tasks/Reports does not require retaining every prompt,
conversation, tool invocation, working log or other agent interaction. Processing
material retains the purpose-bounded treatment below. RAD-1.1.3 first required
execution-only records; 1.1.4-003 demonstrated and refactored the iteration layout
and recovered actual earlier records. This common iteration-scoped convention
applies prospectively from 1.1.4-004; it supplies no further retrospective
reconstruction, repository migration or disposal authority.

## Representation and recovery

Evidence SHALL preserve architecturally relevant before/after state faithfully.
Repository-controlled material may be identified and recovered through Git;
duplicate exact-byte archival and mandatory hashing are not required.
Use source/state locators and correspondence adequate for the evidence's purpose.
Do not claim an uncommitted file is recoverable from a Git commit that does not
contain it.

Where required evidence cannot reliably be recovered from source control, the
retained evidence SHALL itself preserve the necessary state and correspondence.
References and hashes identify content; they do not replace missing required
content. Preserve unique material while it remains needed. No regeneration may
invent missing originals, attribution or architectural authority.

Required evidence and governed state/release identities SHALL remain retrievable
under the approved repository/recovery arrangements. This is faithful transition
preservation, not a full-backup or historical-machinery-reproduction obligation.

Account for additions, modifications, removals, relocation and representation
succession where relevant. Distinguish absence from an empty file. Record both
source/result paths and meaningful correspondence; relocation does not by itself
establish semantic replacement or architectural identity. Evidence is bounded by
the authorised transition, not an entire workspace, dependency tree or Git tree.
Do not recursively capture earlier evidence packages or processing histories.

## Identity and correspondence

Preserve canonical identity of governed architectural things where it exists.
Governance/prose statements and HCRL DR references do not require canonical
architectural identities. Use scoped source/reference mechanisms where canonical
element identity does not apply. Record explicit correspondence where needed for
continuity across releases. It SHALL reflect established architectural continuity
and SHALL NOT be inferred from naming, location or tooling convenience. Existing
canonical versus representation identity and specialised modelling rules remain
binding; no new identity regime is established.

## Application, validation and closure

Authorisation binds the reviewed meaning, permitted changes and any deterministic
derivations, subject to the decision's actual conditions/exclusions. It is not an
unrestricted instruction to generate architecture. Preserve issued dispositions
and material decision history separately from actual processing/results. A
changed result or new material scope requires renewed Solution Architect review.

Record factual application and validation conclusions within the applicable DR,
including material limits and distinct human acceptance, automated verification,
publication and actual inspection/transport results where applicable. PASS,
FAIL, TIMEOUT, STALLED and UNRESOLVED remain distinct. Detailed commands/logs
may serve engineering recovery without becoming permanent architectural evidence.

The Solution Architect confirms that the HCRL accurately represents both the
authorised change and the result actually incorporated before it becomes complete.
An agent may report the facts, not self-issue that confirmation. Record explicit
exclusion/deferral of residual matters rather than imply their resolution.
Immutability, deferred non-semantic erratum mechanics and pragmatic
pending-iteration retention follow the common governance contract.

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

Where mechanical Git state is established or checked for an activity terminating at Report, that verification remains provenance maintenance and does not extend the lifecycle. A commit is not required where the governed activity legitimately produces no repository change beyond its Report/evidence. The governance lifecycle concludes cleanly at Git commit and the resulting repository state. Mechanical actions required to reach this state (including tooling executions, linter or check invocations, and git status inspections) terminate at that state and SHALL NOT instantiate recursive provenance loops.

## Processing material and historical boundaries

Retain processing material while needed for active recovery, validation,
unresolved decisions, unique architectural content or live migration dependencies.
Its operational importance does not automatically confer architectural-evidence
standing. The required end state remains that current authoring, validation and
publication SHALL operate from maintained governed source, maintained
framework/configuration and declared fixtures or governed releases as applicable.
They SHALL NOT depend on historical processing workspaces, run-local executables,
hidden local logs or frozen framework copies. Existing dependencies need
separately authorised work; this adoption does not prove that they have been
removed or authorise cleanup.

HCRL governance is prospective. No blanket historical reconstruction or
reconciliation is required. The historical 1.0.10 → 1.1.0 pilot is excluded:
do not investigate, complete, cancel or otherwise reconcile it unless a future
specific architectural need requires it. Preserve frozen records and unknown
historical authority/correspondence; do not fabricate retrospective compliance.
This contract supplies no processing disposal, historical rewrite or repository
migration authority.
