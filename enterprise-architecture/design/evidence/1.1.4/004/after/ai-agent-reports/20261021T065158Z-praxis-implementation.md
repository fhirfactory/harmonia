<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

**Report-Produced-At: 2026-10-21T06:51:58.138Z.** Event: Codex emitted the completed Report body
in system-recorded `response_item` / `custom_tool_call` to `functions.exec`,
line 295 of `/home/hunterm/.codex/sessions/2026/10/21/rollout-2026-10-21T17-43-20-01a15d33-374c-7952-84af-4ea7d32c2480.jsonl`; call id `call_kG0SCJHj6EmwoFygInvxk5Kr`.
The source call contains the exact completed body and emission command; the
command's heredoc was decoded and compared byte-for-byte before retention.
This observable event is distinct from later installation, validation, review,
acceptance and filesystem metadata. It asserts no internal model-generation time.
The established millisecond precision is retained; the UTC filename prefix derives
from the same event to seconds. The source interaction log is not a continuing
dependency for understanding the formal Report.

The emitted body below is retained unchanged. Its SHA-256 (UTF-8, including its
final newline) is `9d6bb64e86164279a26511a9a0c757852cf2080eb44478d8acaf24977ba48222`.
This subsequent event-registration/navigation annotation does not replace its
original production timestamp. Final registration verification is recorded below
as subsequent provenance, separately from the emitted body.

<!-- EMITTED REPORT BODY START -->
# Task Report — HCRL 1.1.4-004 Praxis implementation

## Result and authority

Created and applied [pending HCRL 1.1.4-004](../../../../../review/1.1.4/hcrl-1.1.4-004.pending.md)
to implement the common iteration-scoped Architecture Review, HCRL and evidence
Praxis demonstrated by [003](../../../../../review/1.1.4/hcrl-1.1.4-003.pending.md).
The complete [004 Task](../../before/ai-agent-tasks/20261021T064322Z-praxis-implementation.md)
is retained with Task-Issued-At **2026-10-21T06:43:22.583Z**, its attributable
system instruction-entry source and matching UTC filename prefix. This Report's
observable emission event is registered in its formal record after emission;
that registration does not invent an earlier production time.

These are working-tree application and verification facts. Mark Hunter, Solution
Architect, retains final decision/acceptance authority; final confirmation and the
bounded Minutiae disposition remain unrecorded. No completed HCRL or register
entry was created. Branch is ArchitecturalGovernance, HEAD is
41047e1dc5c7d1ca4769dc7479bb50cae60989a7. The 001/002/003 inputs were untracked
at entry and are not claimed recoverable from that commit.

## Governance and guidance changed

Paths below are relative to the repository root:

| Changed document | Result |
| --- | --- |
| enterprise-architecture/governance/architecture-review-praxis.md | Explicit activity/working-material/evidence/HCRL model; iteration-scoped workflow for substantive agent activity throughout review and application; preserved adjudication and completion checkpoints |
| enterprise-architecture/governance/architecture-change-provenance.md | Normative definitions, purpose-selected durable evidence, iteration paths, substantive Task/Report retention and preserved event-time/UTC-prefix requirements |
| enterprise-architecture/governance/architecture-governance.md | Consistent review coverage, repository structure, evidence boundary and pending semantic-record standing; unchanged Solution Architect/ARB authority |
| enterprise-architecture/governance/templates/hcrl-template.pending.md | Iteration paths, common substantive-activity records, evidence selection and sufficient DR traceability without detailed working material |
| enterprise-architecture/governance/README.md | Current navigation, historical/current rule distinction and read-only checker/test commands |
| docs/markdown/governance/README.md | Existing navigation explicitly identifies prospective 004 supersession while retaining 1.1.3 history |
| docs/markdown/governance/architecture-release-policy.md | Explicit prospective supersession and single applied-change-set patch increment; original 1.1.3 decision retained as history |
| AGENTS.md; docs/AGENTS.md | Agent instructions now use the common iteration model and preserve attributable temporal provenance and evidence selection |

The root architecture-release.json was advanced through the existing
bump --expect 1.1.3 mechanism to version **1.1.4**, previous_version **1.1.3**,
once for this applied change set. Pending preparation itself allocates no release;
further iterations of this transition consume no additional increment. This
supplies neither acceptance nor publication. No frozen model/publication was
regenerated, and no Maven software version changed.

## Superseded rules and resulting semantics

From 004, the common contracts explicitly supersede the RAD-1.1.3 execution-only
Task/Report scope, release-level before/after placement and exclusion of
substantive review/preparation from formal records. The old rule remains a true
account of what 1.1.3 governed. 003 demonstrated/refactored the corrected layout
and recovered actual earlier provenance; 004 installs its governed process
prospectively. Historical HCRLs, records, event times and frozen pins remain
unchanged. The old execution-provenance anchor remains a navigation alias only.

The resulting normative distinctions are:

- Working material is transient material used while performing an activity. It
  is discardable when its purpose ends and is not governed evidence merely
  because it existed. Preserve active recovery, validation and unresolved
  dependencies; this does not authorise unrelated cleanup.
- Evidence is durable inputs, outputs or supporting material retained because
  they materially support, explain, reproduce or verify the governed activity
  associated with an HCRL iteration. Material mappings, reference inventories,
  checks and similar review outputs are evidence where they serve that purpose.
- HCRL is the authoritative semantic change/decision record for the governed RAD
  transition. It is not the Architecture Review working document, a manifest of
  every evidence file or a substitute for detailed supporting evidence. Its DRs
  retain material meaning, decisions and application/validation conclusions with
  sufficient traceability references.

Durable evidence uses:

    enterprise-architecture/design/evidence/<RAD-version>/<HCRL-iteration>/
    ├── before/
    │   └── ai-agent-tasks/<timestamp>-<task>.md
    └── after/
        └── ai-agent-reports/<timestamp>-<report>.md

The HCRL iteration is the provenance boundary. Before/after are activity-input
and activity-output roles, without claims about predecessor/successor Git state
or acceptance. Earlier evidence stays with its originating iteration and is
referenced by later iterations without mandatory copying. No distinct review
versus execution evidence taxonomy is introduced.

## Substantive AI-agent Tasks and Reports

Investigation, analysis, review, candidate/pending-HCRL preparation or revision,
corrective work, application and verification use this same model. Their formal
Tasks and corresponding Reports are required irrespective of architectural
materiality. Their differing authority and outcomes remain explicit; preparing
a candidate does not authorise its application. Correspondence must be adequate
without requiring one-to-one file pairing. There is no requirement to retain
every intermediate artefact, conversation, prompt, tool invocation or working log.

The existing timestamp requirements remain: attributable system instruction-entry
is Task-Issued-At; completed Report emission is Report-Produced-At. Retain ISO 8601
with Z or explicit offset, event meaning/source and established precision, and
derive sortable UTC filename prefixes from those same events. No earlier human
composition or internal generation time is asserted. Later capture, validation,
file metadata, commits and current time cannot substitute for the original event.
Subsequent Report amendments preserve original production time/prefix with
subsequent provenance. Missing originals and filename prefixes remain explicitly
unresolved for Solution Architect treatment before closure. No historical time
or provenance was invented.

## Machinery, evidence and validation

Added scripts/architecture_review_evidence.py and
scripts/tests/test_architecture_review_evidence.py. The read-only command checks
one explicitly selected HCRL iteration's layout and conventional Markdown
Task/Report event-time/UTC-prefix correspondence. Its 12 tests cover offset and
fractional times, multiple Tasks/Reports, wrong placement, invalid or mismatching
timestamps, unavailable originals, incomplete sides, purpose-selected supporting
evidence and absence of unnecessary empty packages. Other iterations are not
implicitly audited or rewritten. Other record representations require separate
inspection. Parsing cannot prove semantic completeness, substantive activity
coverage, actual attribution, correspondence or architectural acceptance.

The [validation record](../validation.json) retains exact commands, counts,
error identities, bounded baseline-comparison context and integrity facts.
The full failed framework-suite output and focused entry-baseline comparison
are retained in compressed form because they materially support the reported
verification limitation. Passing-script/Maven conclusions are retained without
requiring their raw temporary logs. Construction helpers and intermediate logs
remain transient; current governed operation does not depend on them.

| Executed check | Result |
| --- | --- |
| Complete scripts/tests discovery, 420-second command bound | PASS: 84 tests, including the 12 new evidence checks; 24.479 seconds |
| Complete docs/archimate/framework/tests discovery, 420-second command bound | FAIL: 251 executed tests, ten error events, zero assertion failures; 91.182 seconds. Class setup/import errors prevent dependent tests from executing |
| Required Maven *ArchitectureTest suite, 240-second command bound | PASS: 90 tests in 11 classes, zero failures/errors/skips; BUILD SUCCESS, 19.192 seconds |
| Focused comparison against an isolated git archive of unchanged entry HEAD, original release 1.1.3 | Comparison PASS: the same ten error identities and causes recur; the baseline tests themselves remain FAIL (four executed cases plus class-setup errors, ten error events, 0.740 seconds) |
| Entry-byte preservation, carried body and HCRL structure | PASS: all 15 existing 001/002/003 files unchanged; DR-1–DR-11 carried verbatim; 18 corresponding consecutive DRs and one Minutiae control |
| Current navigation before Report registration | PASS: 201 current targets/anchors; the one Report link is deliberately deferred until its actual emission event is registered |
| Release helper and tracked whitespace checks | PASS: 1.1.4/previous 1.1.3 and git diff --check |

The ten framework errors comprise eight AGENTS current-authority drift errors,
one IA capture carry-forward guard and one missing root-release publication.
An unchanged entry-commit archive reproduces all ten. The missing-publication
path uses 1.1.3 at entry and 1.1.4 after the authorised patch; no publication was
fabricated to satisfy that dependency. The first direct-name baseline diagnostic
aborted during the IA module import; discovery-based selection then captured all
ten errors. The comparison reran only those failing classes/tests, not another
full suite. No timeout or stall occurred. No complete framework-suite PASS,
maintained-metadata validity or clean-clone operational-baseline success is claimed.

The final Report-event registration, selected-iteration check and complete current
link checks are recorded as subsequent registration verification in the formal
record and validation evidence. They preserve this emitted body and original
production event. Test-generated tracked Python bytecode was restored and only
new untracked test bytecode was removed; historical evidence was not cleaned up.

## Carry-forward, unresolved matters and stop

All applicable 003 decisions and standing material were carried forward. The
substantive DR-1–DR-11 migration body is verbatim; all inherited unrecorded
dispositions retain that standing. DR-12 preserves historical supporting groups;
DR-13/DR-14 preserve the directed iteration boundary, actual refactoring, recovered
provenance, integrity limits and outstanding questions with explicit historical
context. No original 001/002/003 file or evidence byte changed, including the
mapping, compressed reference inventory, check record and recovered Tasks/Reports.

No governance conflict beyond the expressly authorised prospective supersession
required an invented architectural rule. The inherited migration decisions,
exact-path discrepancy, authority-pin reconciliation and operational-baseline
limits retain their existing standing for Solution Architect treatment in 005.
The additional observed suite errors are verification facts; they neither amend
those migration recommendations nor adjudicate their resolution. Final result
confirmation and Minutiae acceptance remain Solution Architect decisions.

**Substantive RAD-1.1.4 repository migration/adjudication was not resumed.** No
mapped corpus relocation, fixture copying, migration mapping/classification/finding
or recommendation change, maintained-validator redesign, authority-pin refresh,
model/publication regeneration, processing disposal or historical-pilot
reconciliation occurred. No 005 work, completed 1.1.4 HCRL or register entry was
created. Work stops after the 004 Praxis, this formal Report, supporting validation
and pending HCRL update.
<!-- EMITTED REPORT BODY FINISH -->

## Subsequent registration verification

**Registration-Verified-At: 2026-10-21T06:52:51.427Z.** Instrumented UTC time after the
post-emission verification commands completed in the registration tool action;
this is subsequent verification/annotation time, not Report-Produced-At.

PASS: selected 1.1.4/004 layout and both formal Markdown records' recorded
event/UTC-prefix correspondence; all 206 current link targets/anchors; root release
1.1.4/previous 1.1.3; tracked whitespace; unchanged emitted-body SHA-256 and all
15 historical input files. The detailed results are retained in validation.json.
This annotation leaves the emitted body, original production event and filename
prefix unchanged. The full framework suite remains FAIL with the ten reproduced
baseline errors described above. No final architectural confirmation is asserted.
