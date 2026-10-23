<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Durable release projection and bounded publication integration | Not recorded | Issued task authorises implementation; result confirmation remains pending. |
| [DR-2](#dr-2) | Bounded supporting Minutiae and temporal provenance | Not recorded | Unavailable Task instruction-entry event/prefix requires treatment before closure. |

# HCRL — 1.1.11 → 1.1.12: published ArchiMate release projection

**Review basis.** Mark Hunter, Solution Architect, issued the bounded publication
task retained [verbatim in the formal Task][task]. Task entry was a clean worktree
at accepted commit `ed8121a`, with root release 1.1.11 / predecessor 1.1.10.
The user-linked pending 1.1.11-002 HCRL is preserved review history; its
[completed counterpart](../../register/1.1.11/hcrl-1.1.11-002.complete.md)
establishes accepted 1.1.11 state. This task supplies authority to implement the
publication omission and prepare this pending HCRL, not authority to accept or
close it. The release-policy patch preparation belongs to this one change set;
it does not reopen or alter accepted 1.1.11.

The Architecture Continuum remains canonical authority. AX-04 supports reuse of
the maintained machinery; AX-08 supports meaningful retained provenance;
AX-14 preserves canonical architecture, derived publication, workbench proposal
and evidence as distinct concepts; AX-15/17 preserve unavailable event times and
reserve architectural acceptance to the Solution Architect. The common Governance,
Review Praxis and Change Provenance contracts apply. No canonical architecture,
navigation, identity, names, standing, semantic relationship or authored View is
changed. No round-trip process redesign or automatic ingest is authorised.

<a id="dr-1"></a>
## DR-1 — Durable release projection and bounded publication integration

The implemented publication is
`enterprise-architecture/design/archimate/published/1.1.11/Harmonia-Architecture.xml`.
It is the ArchiMate Open Exchange projection of the **accepted Harmonia
Architecture Continuum release 1.1.11**, retained as a release artefact and the
normal starting file for importing that release into Archi. The XML filename is
the maintained generator's existing `Harmonia-Architecture.xml` convention.

The file was generated directly by the maintained projector while the source
tree still held the accepted 1.1.11 release authority and unchanged canonical
inputs. It was not copied from evidence, an intermediate model or an Archi return.
The accepted original projector was independently executed twice against an
isolated Git snapshot of `ed8121a`; both outputs and the current projector output
match the new file byte for byte. It also matches the unchanged 1.1.11 Manifest
Exchange and the actual Archi-tested baseline hash in accepted evidence:
`e33ae52b3934cf4d9b9b0102ce65e09461fa554022f43d56f2637685d8c39caa`.
The accepted Archi-returned control has different bytes and is not the publication.

Bounded verification establishes 1,602 canonical elements, 1,651 semantic
relationships and 1,209 authored Views; canonical identity/bindings, names, types,
content and standing match accepted canonical inputs. The complete View subtree,
including geometry, is unchanged. All 1,602 elements retain the accepted navigation
coordinates, paths and typed reference metadata. Offline Open Exchange XSD and
reference checks pass. [Source/publication verification][verification] records
the exact accepted-source correspondence and preservation of all 1,688 protected
files, including accepted navigation, publications, evidence and both HCRLs.

Actual **Archi 5.10.0** import, native save and no-edit export of the new published
path succeeded. The maintained [transport verification][transport] establishes
zero canonical element, relationship or View changes. Archi's raw XML changes
language attributes and organisation representation; complete raw byte fidelity
and new interactive human visual inspection are not asserted. The presentation
template and published projection are not replaced by the return. The existing
1.1.11 navigation representation limits retain their accepted standing.

The existing `architecture_continuum.py project` command now accepts `--publish`,
deriving `design/archimate/published/<release>/Harmonia-Architecture.xml` from the
source tree's root release authority. Used with `--output`, it writes both existing
Manifest and durable ArchiMate destinations from a **single validated projection**.
`make continuum` uses this mode; `make check` checks both. Repeated identical
publication is safe. A differing existing durable projection is rejected before
either destination is written. `--check` is read-only. There is no second semantic
generator, release allocator, historical version relabelling or general release
automation. Independent-generation verification now includes this release artefact.

Current workbench, tooling and round-trip documentation identify the publication
path and distinguish canonical authority, released projection, architect export
and evidence. An architect-modified export remains proposed input to a subsequent
round-trip, retained as appropriate `before` evidence of that activity. Merely
existing does not modify or supersede the Continuum. No current governed permanent
ingest-directory requirement was found; generic incoming-landing references do
not mandate one. Historical intake locations remain historical. No ingest directory
or input-handling mechanism is added. Existing review/application checkpoints are
unchanged.

This is a publication/tooling change set preparing 1.1.12, not an architectural
republication of the Continuum as a newly accepted 1.1.12 model. Its initial
release artefact intentionally represents accepted 1.1.11. Unrelated reader-facing
publications and the current root Continuum Manifest are not regenerated.
Future accepted Continuum publication uses the existing command and acceptance
gates. No historical backfill beyond the explicitly requested 1.1.11 is implied.

**For Solution Architect review:** confirm that the publication location, its
release-artefact semantics and this bounded integration satisfy the issued task.
Successful verification does not constitute acceptance; this HCRL remains pending.

<a id="dr-2"></a>
## DR-2 — Bounded supporting Minutiae and temporal provenance

The disclosed supporting group comprises the formal Task and execution Report,
purpose-selected source/generation/preservation checks, the two bounded publication
regressions, maintained-suite results, exact no-edit Archi transport control/log
and the single release-policy patch preparation. The provenance boundary is
1.1.12/001. No historical evidence, HCRL, publication, cleanup or migration is
rewritten or authorised. No runtime or Java source changes are made.

The Task instruction-entry system timestamp was not exposed. `Task-Issued-At`
and its required sortable UTC filename prefix remain explicitly **UNRESOLVED**;
current clock, recording time and Git/file timestamps are not substituted.
Solution Architect treatment is required before closure. Report production records
its observable emission event separately. This limitation does not block the
independent publication and validation activity expressly authorised by the task.

Validation conclusions and detailed Task/Report correspondence are retained with
the iteration evidence. They establish bounded execution results and do not supply
architectural acceptance or closure. The release authority is prepared once for
1.1.11 → 1.1.12; generation never increments it. Checks of accepted-source release
publications use their actual 1.1.11 inputs, not a relabelled current authority.

All **62 maintained EA tests** pass in the isolated current-input tree, with zero
failures/errors; the focused source/publication suite passes 22 tests. All **90
Java architecture tests** pass on Java 21 with zero failures/errors/skips.
Independent generation reproduces 20 derived artefacts exactly, including the
new durable XML, without Legacy-RAD, `docs/`, `scripts/`, Git metadata or historical
configuration. Accepted-source staleness/link checks and browser DOM smoke checks
pass. [Test results](../../evidence/1.1.12/001/after/test-results.json) and
[reproducibility](../../evidence/1.1.12/001/after/reproducibility.json) preserve
the bounded outcomes. The initial Archi sandbox launch failed before import at
GTK initialization and succeeded with display access; an initial supporting
check's scalar/list assertion was corrected and the full publication verification
passed. Neither temporary check/environment failure altered the XML or accepted
1.1.11 material. No timeout or stalled outcome occurred.

The [completed execution Report][report] records production at
`2026-10-23T03:15:05.082845Z`, source/result correspondence and the verification
limits. [Evidence-layout verification](../../evidence/1.1.12/001/after/evidence-layout.json)
finds one formal Task and one Report, zero layout/event-format errors and the
explicitly unresolved Task event/prefix. Report completion records performed
activity; it does not accept or close this HCRL.

```text
Minutiae-Accepted: [awaiting Solution Architect entry: Yes | No]
Comment: [awaiting Solution Architect treatment of the unavailable Task event/prefix and result confirmation]
```

[task]: ../../evidence/1.1.12/001/before/ai-agent-tasks/unresolved-published-archimate-projection.md
[verification]: ../../evidence/1.1.12/001/after/publication-verification.json
[transport]: ../../evidence/1.1.12/001/after/archi-transport.json
[report]: ../../evidence/1.1.12/001/after/ai-agent-reports/20261023T031505Z-published-archimate-projection.md
