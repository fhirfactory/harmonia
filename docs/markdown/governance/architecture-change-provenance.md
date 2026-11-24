# Architecture Change Provenance — current authority

The single maintained normative contract is now
[Architecture Change Provenance](../../../enterprise-architecture/governance/architecture-change-provenance.md),
under [Architecture Governance](../../../enterprise-architecture/governance/architecture-governance.md).
The Solution Architect's RAD-1.1.2 HCRL dispositions and application instruction
supersede this predecessor contract as the current rule. Required evidence no
longer follows a mandatory three-file archive package or universal exact-byte
duplication/hashing requirement. HCRL is the single final semantic review record.

The complete predecessor contract is preserved below unchanged for existing
references and historical meaning. Its original adoption/pilot statements are
not instructions to investigate or reconcile history in this transition.
No existing processing carrier becomes disposable and no repository migration
is authorised by this in-place supersession.

------- Legacy Content - Superseded ------- Start ------

Superseded by:
[current provenance contract](../../../enterprise-architecture/governance/architecture-change-provenance.md)
and [common review, completion and adoption rules](../../../enterprise-architecture/governance/architecture-governance.md).

---------------------------------------------------------

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia Architecture Change Provenance

## Status, authority and scope

**Governed — accepted by the Solution Architect for installation, 2026-10-20.**
This contract governs future architecture activity. The installation decision
accepts the reviewed provenance design with the amendments recorded below;
the design report remains a historical proposal, not an alternative authority.

This document is the single maintained definition of Harmonia
architecture-change provenance. Agent instructions, Praxes, templates and tools
must reference it rather than establish competing provenance rules. It is
subordinate to the [Architectural Axioms](architectural-axioms.md) and applies
AX-04, AX-08, AX-09, AX-14, AX-15 and AX-17. The
[architecture release policy](architecture-release-policy.md) continues to
govern release allocation; provenance machinery never allocates a release.

The contract applies to authorised architecture work through Markdown/RADS,
ArchiMate and future representations and tools, whether performed by the
Solution Architect, Codex, Junie or another authorised contributor. It does
not confer acceptance on a representation, processing run or generated result.
It defines repository architecture-change evidence, not all runtime provenance,
diagnostic retention or software test reporting.

## Governing rule

Architectural evidence SHALL record the authorised incremental change between
two governed architecture states. It SHALL NOT record the execution history of
the machinery used to produce or validate that change.

For a governed transition `RAD-x → RAD-y`, durable architectural evidence
consists of exactly:

```text
before.tar.gz
after.tar.gz
change-review.md
```

Processing artefacts, intermediate candidates, machinery snapshots, test outputs,
logs, temporary states and copies of governance/framework source SHALL NOT become
architectural evidence merely because they participated in producing a change.

## Definitions

| Term | Meaning |
| --- | --- |
| Governed state | An explicitly identifiable, authorised state of Harmonia architecture held in the repository, with its applicable release, source boundaries, identities, decisions and standing. A state may preserve explicitly unresolved or candidate assertions without accepting them. A commit, version label, publication or PASS result alone does not establish architectural acceptance. |
| Architectural change | An authorised difference in governed architectural content or its governed representation between two governed states, including approved architectural governance, identity, semantic, documentation or presentation changes. A processing correction or machinery run is not inherently an architectural change. |
| Changed content | The bounded repository content whose state actually differs as part of the authorised architectural transition. It includes approved additions, modifications, removals and path changes and the necessary governed representation changes. It excludes unchanged dependencies and incidental processing output. |
| Before state | The originating governed state, restricted in the before archive to the original versions of the changed content that existed there. It is not an incoming proposal, an immediately preceding run or an arbitrary pre-correction workspace. |
| After state | The resulting governed state, restricted in the after archive to the resulting versions of that same changed content that exist there. It is not an unaccepted candidate or an alternative regenerated result. |
| Architectural evidence | The exact three-file record of a governed transition: the changed content before and after, and its reviewed change/authorisation record. |
| Processing artefact | Material used to discover, prepare, execute, recover, test or publish a change without itself being accepted changed architectural content. Its operational importance and possible temporary retention do not confer architectural-evidence standing. |
| Authorisation record | The attributable Solution Architect decision recorded in change-review.md, binding the exact proposed change and directives to their originating and resulting governed states, scope, conditions and exclusions. Generated dispositions or successful tests are not authorisation. |
| Three-file evidence package | The logical evidence unit for one governed transition, containing only before.tar.gz, after.tar.gz and change-review.md. This term prescribes no final repository directory layout. |

## Identifying the transition and changed content

Before applying a change, the authorised contributor SHALL establish the exact
originating governed state, applicable authority, intended scope and original
content. Before finalising evidence, the contributor SHALL establish the actual
resulting governed state and account for every difference in the approved scope.

The changed-content inventory SHALL be recorded in change-review.md. Each entry
SHALL identify its repository-relative path or corresponding paths, operation,
before/after existence and relevant Change Request or
directive. Renames and release-representation succession SHALL record both paths
and their correspondence; architectural identity continuity SHALL remain separate
from file-path continuity.

Whole-file preservation is authoritative. When architectural content within a
repository file changes, each applicable archive SHALL preserve the complete
file's exact bytes, including for Markdown, JSON, XML and future representations.
Extracted records, XML fragments, Markdown sections or other partial
representations SHALL NOT substitute for that file. change-review.md identifies
the narrower architectural change within it: **Archive = exact changed-file
bytes. Review = architectural meaning and authorisation.** Preserving a changed
file does not authorise capturing its directory, dependency tree or repository.

The changed-content inventory is mandatory; it is not a machine manifest.
Per-file/member byte hashes MAY be recorded where they materially assist
identity, provenance, ambiguity resolution or verification. They are not
mandatory for every member. The SHA-256 hashes of before.tar.gz and after.tar.gz
SHOULD be recorded in the final review.

Supporting source, schemas, code, tests or governance SHALL NOT be copied into
the archives merely because a processor read them. A governance source belongs
in the archives only where that source itself is changed by the authorised
architectural transition. A machinery-only code change uses Git version history;
its participation in an architectural change set does not make it architectural
content. An ambiguous classification SHALL be raised rather than inferred.

Release-specific publication paths may differ. Where a changed logical
architecture representation has an original version at one release path and its
result at another, the archives SHALL retain those respective versions at their
actual repository-relative paths and the review SHALL map their succession.
The predecessor publication may remain an immutable governed release in the
repository; its inclusion as original changed architectural content does not
claim that its stored file was modified or deleted. This is not a filesystem
rename or two unrelated additions. Unchanged baseline/dependency files SHALL
NOT be included merely because they help interpret the transition. The inventory
must distinguish logical architectural change from repository storage operations.

## Archive contents and path preservation

before.tar.gz SHALL contain the original versions of the content actually
changed by the transition. after.tar.gz SHALL contain the resulting versions
of that same changed content. Both SHALL preserve the repository-relative paths
of the respective state, without an enclosing workspace/run prefix or rewritten
basename. Files are retained as actual content, not individually recompressed
snapshot copies with artificial .gz suffixes.

For a modification at one path, that path appears in both archives. For an
addition, no before member exists; for a removal, no after member exists.
change-review.md SHALL state ABSENT explicitly for the applicable side. An empty
file SHALL NOT stand for absence. A pure-addition or pure-removal transition may
therefore have one valid empty tar archive; both required archive files still
exist. A rename maps the old and new paths without fabricating a file in either
state. Release-representation succession likewise maps the original and resulting
versions at their different paths without claiming removal of the predecessor.
The logical changed-content set is the same on both sides even where the member
sets differ for these reasons.

Archive interpretation SHALL be deterministic. Member names SHALL be unique,
normalised repository-relative paths. Absolute paths, parent traversal and
members escaping that boundary SHALL be rejected. Any required links or special
file types need an explicitly approved preservation rule; tools SHALL NOT
silently dereference external content. Verification SHALL check member lists,
declared absences, exact file-byte correspondence to the identified states and
any recorded hashes against the review. Verification does not require a hash
entry for every member in change-review.md.

The archives SHALL contain no permanent auxiliary manifest, log, plan, test-result,
decision or machinery files unless the particular file is itself changed governed
architectural source. It is forbidden to place processing material inside an
archive to evade the three-file limit. Required interpretation metadata belongs
in change-review.md; existing governed metadata already intrinsic to an archived
source file remains part of that exact file.

## change-review.md and authorisation

The reviewed change record SHALL describe the architecture change, its
dispositions/directives and Solution Architect authorisation. It SHALL contain:

- originating and resulting governed-state/release identities, immutable
  repository locators where available and the applicable authority;
- the bounded architectural purpose and differences, stable CR/directive
  identities where applicable, dependencies, exclusions and unresolved matters;
- the reviewed dispositions, instructions, reasons and material decision
  revisions, with attributable authorisation actor/date and conditions;
- the exact changed-content inventory, path/rename correspondence and existence
  states; per-file/member hashes are optional as specified above;
- the checks needed to interpret and verify the transition, including relevant
  validation conclusions, limitations and distinct
  human acceptance, publication and inspection outcomes;
- for reconstructed historical evidence, the reconstruction date, provenance
  sources, original authorisation facts and explicit limits of what was recovered.

The final review SHOULD record both archive SHA-256 hashes. Neither an exhaustive
member-hash list nor an auxiliary manifest is required.

Only the Solution Architect supplies architectural acceptance or changes an
issued disposition. Processing status, Outcome and Outcome Reason remain distinct
from Disposition. A review may record held or rejected proposals without accepting
them. It SHALL preserve the decision facts needed to understand the authorised
transition without requiring a processing workspace to interpret them.

Material decision history may be quoted or incorporated into this one reviewed
record. It SHALL NOT require permanent companion decision files or a recursive
collection of older reviews. No review self-hash is required; the review is kept
outside the archives and its final version is bound by repository history and
Solution Architect approval. Archive hashes can therefore be recorded without a
circular hash dependency.

Authorisation binds the proposed content and permitted deterministic derivations,
not an unrestricted instruction to generate architecture. Where final archive
hashes or completion facts are recorded after the application authorisation,
they SHALL be distinguishable from the original decision. Final closure must
confirm that they describe its authorised result. A changed result or material
new scope requires renewed Solution Architect review. Tool success supplies no
missing approval and SHALL NOT overwrite the reviewed decision.

## Git and architectural evidence

Git is the normal version-history mechanism for maintained governance,
executable machinery, tests and configuration. Extra source snapshots require a
specific architectural/evidentiary reason; participation in a run is insufficient.

Git records file history. The three-file package records which incremental
architectural change was authorised and the actual changed content before and
after. Neither substitutes for the other's role. A Git commit can include
unrelated machinery and processing files; copying its entire diff or tree is not
an architectural-evidence selection rule. A file that was never committed at its
source path is not assumed recoverable there. Where needed changed architectural
bytes survive only in an older snapshot, they SHALL be recovered and verified
before that carrier becomes disposable.

Evidence and governed release identities SHALL remain durably retrievable under
the approved repository/recovery arrangements. References and hashes identify
content but do not replace missing content. The package is an incremental
record, not a full backup or a promise to rebuild an entire state without its
governed baseline.

## No recursive evidence capture

Evidence SHALL NOT recursively capture previous evidence. Archives SHALL NOT
contain previous packages, before/after archive trees, processing histories or
copies of their own evidence directory. Reference earlier governed states or
transitions by identity and locator in the review; do not embed their packages.

Content selection SHALL start from the authorised changed-content inventory,
not a recursive workspace walk. Genuine historical architecture text intrinsic
to a changed governed source remains byte-preserved in that source; this does
not justify copying the separate evidence files to which it refers.

## Transition verification versus historical machinery reproduction

Proving a transition means establishing its authorised scope, original and
resulting changed content, identity continuity, exclusions and actual governed
outcome. It does not require preserving every command, temporary candidate,
test result, tool version or environment that produced the outcome.

Reproducing a historical machinery execution is a separate diagnostic or
engineering activity. It may use Git-pinned code and purpose-specific regression
fixtures in an isolated workspace. It SHALL NOT define architectural acceptance,
enlarge the three-file package or become a dependency of current operation.
Any separately required diagnostic retention must have an explicit purpose and
must not be represented as a fourth architecture-evidence component.

The required end state is that current authoring, validation and publication
SHALL operate from maintained governed source, maintained framework/configuration
and declared fixtures or
governed releases as applicable. They SHALL NOT depend on historical processing
workspaces, run-local executables, hidden local logs or frozen framework copies.
Reusable mechanics SHALL be maintained as framework code before the processing
carrier is discarded. Fixtures retain testing purpose and no architectural
authority merely through their use by tests.

Existing operational dependencies on historical processing SHALL be identified
and removed through separately authorised migration/framework work. Retain the
affected material until independence and preservation of unique architectural
content are verified. Adoption establishes this obligation; it does not claim
that current machinery already meets it or authorise refactoring in this increment.

## Lifecycle, closure and migration

```text
governed state
      ↓
processing workspace
      ↓
change-review.md
      ↓
Solution Architect authorisation
      ↓
updated governed state
      ↓
before.tar.gz
after.tar.gz
change-review.md
      ↓
processing workspace eligible for disposal
```

Original before bytes SHALL be secured before mutation even though final package
closure follows the updated state. While work is open, processing material may
be needed for recovery, pending decisions or validation. Completion requires
verified evidence, an authorised result, preserved unique architectural content,
resolved current dependencies and applicable retention/disposal authority.
Disposable means eligible for an explicitly authorised disposal operation; it is
not an instruction to delete automatically.

A failed, diagnostic, superseded or no-change run with no governed architectural
transition has no architectural evidence package merely by having existed.
Uncertain effects must first be resolved or held explicitly under AX-15/AX-17.
An issued held/rejected decision needed for future architecture work must remain
available in the applicable reviewed record or maintained governed unresolved
matter; processing material SHALL NOT become a hidden architecture authority.

Historical conversion SHALL identify actual governed state boundaries and
authorisation before selecting content. It SHALL NOT relabel run-to-run changes
as governed transitions, invent retrospective authorisation, reconstruct missing
originals by regeneration, or rewrite frozen historical records. A retrospective
review may consolidate established decisions only with explicit Solution
Architect approval and transparent reconstruction provenance.

### Adoption and reconciliation — Solution Architect decision, 2026-10-20

This contract explicitly supersedes the permanent machinery-run, snapshot and
complete processing-history retention interpretation of the maintained release
policy, Praxes and Change Review requirements. Those requirements now serve
active recovery, validation, unresolved decisions or migration dependencies;
they do not enlarge durable architectural evidence. Frozen historical records
and their original claims SHALL NOT be rewritten to assert retrospective
conformance. Other applicable retention obligations remain in force.

Historical reconstruction is demand-driven. Adoption creates no obligation to
construct evidence packages for every historical RAD release.
`RAD-1.0.10 → RAD-1.1.0` is the intended first migration/pilot transition, subject to separate
authorisation. Earlier transitions SHALL be reconstructed only where required to:

- retire an operational dependency on historical processing;
- preserve architectural evidence that would otherwise be lost;
- resolve a provenance requirement; or
- satisfy explicit Solution Architect direction.

A machinery run is not an architectural transition merely because it existed.
Installing this contract authorises no historical reconstruction or package
creation, including for the installation increment itself, and no processing
movement, deletion, cleanup or machinery refactoring. The normal release policy
still applies to this accepted governance change set.

No existing processing material has become disposable merely as a consequence
of installing the governance contract. No file is disposable solely because
of its filename, extension, age, duplication, compression or generated status.
Unknown architectural value or a live dependency blocks disposal of that content.
Physical layout, migration selection and disposal execution require separately
authorised work; this contract creates no blanket cleanup authority.

------- Legacy Content - Superseded ------- Finish ----
