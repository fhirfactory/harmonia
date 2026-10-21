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
