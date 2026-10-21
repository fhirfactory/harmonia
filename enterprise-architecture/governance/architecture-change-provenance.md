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
The [Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md)
remain controlling; AX-04, AX-08, AX-09, AX-14, AX-15 and AX-17 apply.

The Solution Architect's [reviewed HCRL 1.1.3 iteration 001](../design/review/1.1.3/hcrl-1.1.3-001.pending.md)
and subsequent Apply instruction establish the required formal AI-agent
execution-provenance convention below, first used for RAD-1.1.3. The common
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

## Selecting required evidence

Select before/after evidence where it materially contributes to understanding,
verifying or preserving the authorised architectural transition. Evidence is not
accumulated for historical completeness. Material adequately preserved by source
control need not be duplicated. A processor reading a source does not make a
copy of it architectural evidence.

Formal AI-agent Tasks directing execution of an identifiable adjudicated/authorised
change and their corresponding execution Reports are required provenance under
the rule below, irrespective of architectural materiality. Investigation, analysis
and preparation/revision of Architecture Review material remain review provenance.
The selection rules in this section govern additional evidence; they do not make
formal execution Task/Report retention optional.

Required before/after evidence belongs at
`enterprise-architecture/design/evidence/<resulting-version>/before/` and
`after/`. The old mandatory `before.tar.gz`, `after.tar.gz`, `change-review.md`
package contract is superseded. There is no universal archive, hashing or
duplicate exact-byte snapshot requirement. These directories do not require
unnecessary copies or empty package artefacts when source control and retained
governed content already supply the required evidence.

Git remains authoritative for source-code history. Do not reproduce implementation
source merely to make architectural evidence historically complete. A bounded
code exemplar may be retained only where it materially assists understanding of
a decision, principle, pattern, constraint or design. Record that bounded purpose.

## AI-agent execution provenance

For a formal AI-agent Task directing execution of an identifiable
adjudicated/authorised change, the Task Detail(s) and corresponding execution
Report(s) SHALL be retained as execution provenance at these governed locations:

```text
enterprise-architecture/design/evidence/<resulting-version>/
├── before/
│   └── ai-agent-tasks/
└── after/
    └── ai-agent-reports/
```

AI-agent investigation, analysis, candidate preparation and preparation/revision
of a pending HCRL are Architecture Review provenance. They do not become execution
provenance merely through agent participation or contribution to the same release
transition. Preserve their relevant instructions, findings and review history
through the HCRL lifecycle and applicable materiality/Minutiae treatment; this
contract establishes no separate formal review Task/Report convention.

The version is the resulting governed RAD release, independent of agent,
session, pending-HCRL iteration, software version or processing-run identity.
`before/` and `after/` represent task-input and execution-result roles; they do
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

The Report SHALL preserve sufficient factual execution, outcome, verification,
limitation and correspondence information to establish what occurred. Identify
the corresponding task(s), affected source/result paths or adequate source-control
locators, departures from requested scope and unresolved effects. Distinguish
proposed, performed and incorporated work and PASS, FAIL, TIMEOUT, STALLED and
UNRESOLVED outcomes. Task-to-report correspondence SHALL be adequate to reconstruct
the governed transition; one-to-one file pairing is not required.

Every formal execution Task and Report SHALL contain an authoritative event
timestamp: when the operative Task instruction was issued and when the Report
was produced, respectively. Record an unambiguous ISO 8601 timestamp with UTC
`Z` or an explicit offset, its event meaning and attributable source where
required. `Task-Issued-At` and `Report-Produced-At` are suitable labels. Preserve
known precision; UTC to at least seconds is recommended for newly captured events.

For Harmonia AI-agent execution provenance, the attributable system timestamp
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
execution start/end, validation, file creation or
modification, later formalisation, review and acceptance. Preserve material
follow-up/revision correspondence and established event times; later recording
does not replace the original event. A subsequent amendment to an already-produced
Report SHALL be preserved as subsequent provenance and SHALL NOT replace its
original production timestamp or the filename prefix derived from it.

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
material retains the purpose-bounded treatment below. This convention first
applies to RAD-1.1.3 and subsequent governed transitions; it supplies no
retrospective reconstruction, repository migration or disposal authority.

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
