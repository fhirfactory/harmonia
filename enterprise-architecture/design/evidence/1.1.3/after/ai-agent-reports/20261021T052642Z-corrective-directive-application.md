<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task Report — Apply RAD-1.1.3 corrective dispositions

**Subsequent temporal-provenance amendment.** The Solution Architect's updated
[iteration 004](../../../../review/1.1.3/hcrl-1.1.3-004.pending.md), table DR-9 expressly
clarifying DR-7, resolves the three historical events previously reported below
as unresolved. Original Task issuance is `2026-10-21T04:58:06.105Z`; original Report
production is `2026-10-21T05:02:13.128Z`; corrective Task issuance is
`2026-10-21T05:21:50.381Z`. These are the attributable system instruction-entry /
Report-emission events at source lines 133 / 230 / 381 retained in those records.
Current [corrective Task](../../before/ai-agent-tasks/20261021T052150Z-corrective-directive-application.md)
and [iteration 005](../../../../review/1.1.3/hcrl-1.1.3-005.pending.md) provide the rename correspondence and application facts.

This is a subsequent amendment under the
[temporal application Task](../../before/ai-agent-tasks/20261021T053007Z-temporal-directive-application.md).
The already-established `Report-Produced-At: 2026-10-21T05:26:42.905597Z`, its
`20261021T052642Z` filename prefix and the separately recorded original
`Report-Updated-At: 2026-10-21T05:27:48.132210Z` remain unchanged. This amendment
does not re-date that Report. The full prior account below, including actual
verification and the then-unresolved conclusions, is preserved unchanged as
historical provenance and qualified by this amendment.

## Preserved Report before this amendment

**Report-Produced-At: 2026-10-21T05:26:42.905597Z.** Event: Codex produced this formal
corrective execution Report. Source: Codex's contemporaneous production declaration,
using the timezone-aware UTC system clock while assembling this record for
emission; precision is microseconds. This is a newly recorded production event,
not recovery of a historical event from the current clock or file metadata.
The filename's `20261021T052642Z` prefix is derived from this same event in UTC to
seconds; the record preserves finer precision. Production is distinct from
execution, test completion, later revision, review and acceptance. This Report's
known production time does not resolve any original Task/report time.

**Task, authority and result.** Applied the
[corrective execution Task](../../before/ai-agent-tasks/corrective-directive-application.md)
under the Solution Architect's [reviewed iteration 003](../../../../review/1.1.3/hcrl-1.1.3-003.pending.md),
DR-6/DR-7, and its subsequent Apply instruction. All eight issued table rows are
retained verbatim in [application iteration 004](../../../../review/1.1.3/hcrl-1.1.3-004.pending.md).
The issued clarification removes the candidate-preparation files and requires
timestamped execution filenames; the earlier relocation/optional-name proposal
was not applied. DR-8 acceptance is limited to iteration-003 preparation.

The contracts, Praxis, template and affected guidance now distinguish review
preparation from execution of identifiable adjudicated/authorised change, require
authoritative issuance/production event times and mandatory sortable UTC filename
prefixes, and preserve unavailable original times for Solution Architect treatment
before closure. The misclassified pair is removed without relocation or a new
formal review Task/Report convention. Genuine original execution records remain,
with bounded corrective annotations and preserved historical application facts.

**Origin and release.** HEAD is `94223edf4e39f7ce50f4414cb6d82b89597339db`.
At corrective entry the initial application was already uncommitted: nine tracked
modified and eight untracked files. The reviewed iteration-003 input's SHA-256 was
`81e4c8a07777d7d3b652297a1e246ca1fa3bc6ecb93f4c441d2914b41de1384b`.
Iterations 001/002/003 are unchanged. Root release remains 1.1.3, previous 1.1.2;
this corrective iteration allocates no increment. Tracked original pre-images
remain at HEAD; current results are working files, not an immutable after-commit
state. Prior application facts remain in iteration 002 and the original Report.

## Actual corrective inventory

Paths below are relative to the repository root. Compared with this Apply entry,
11 files are modified, two removed and three added. There are no renames.

| Operation | Path | Actual treatment |
| --- | --- | --- |
| Modify | `AGENTS.md` | Correct execution scope, event timestamps, mandatory UTC filename prefixes and unresolved-time treatment. |
| Modify | `docs/AGENTS.md` | Documentation entry follows the corrected common contract and review/execution distinction. |
| Modify | `docs/markdown/governance/README.md` | Correct common Praxis/provenance guidance and current application standing. |
| Modify | `docs/markdown/governance/architecture-release-policy.md` | Correct this transition's convention/standing in its existing RAD-1.1.3 entry; no new release. |
| Modify | `enterprise-architecture/governance/README.md` | Correct convention summary and link current application/temporal limits. |
| Modify | `enterprise-architecture/governance/architecture-governance.md` | Align common scope and provenance entry with reviewed DR-6/DR-7. |
| Modify | `enterprise-architecture/governance/architecture-change-provenance.md` | Install the accepted execution boundary, event timestamps, mandatory UTC filenames and unavailable-original-time treatment. |
| Modify | `enterprise-architecture/governance/architecture-review-praxis.md` | Preserve six stages; handle preparation as review provenance and require execution event-time/filename treatment. |
| Modify | `enterprise-architecture/governance/templates/hcrl-template.pending.md` | Correct review/execution and temporal guidance without a new record taxonomy. |
| Modify | `enterprise-architecture/design/evidence/1.1.3/before/ai-agent-tasks/directive-application.md` | Retain genuine Task; annotate unresolved issuance/prefix and update preparation correspondence to HCRL history. |
| Modify | `enterprise-architecture/design/evidence/1.1.3/after/ai-agent-reports/directive-application.md` | Retain genuine Report with its historical body unchanged; add classification/temporal correction annotation. |
| Remove | `enterprise-architecture/design/evidence/1.1.3/before/ai-agent-tasks/candidate-preparation.md` | Remove misclassified preparation Task; no relocation, stub or replacement formal review record. |
| Remove | `enterprise-architecture/design/evidence/1.1.3/after/ai-agent-reports/candidate-preparation.md` | Remove misclassified preparation Report; HCRL history preserves creation and reclassification. |
| Add | `enterprise-architecture/design/evidence/1.1.3/before/ai-agent-tasks/corrective-directive-application.md` | Exact corrective Apply instruction, reviewed scope and honest unresolved issuance provenance. |
| Add | `enterprise-architecture/design/evidence/1.1.3/after/ai-agent-reports/20261021T052642Z-corrective-directive-application.md` | This corrective execution Report with contemporaneous production event and matching UTC filename prefix. |
| Add | `enterprise-architecture/design/review/1.1.3/hcrl-1.1.3-004.pending.md` | Issued decisions, actual correction/removal/temporal observations, verification and pending human treatment/confirmation. |

The root `architecture-release.json` is unchanged relative to this Apply entry;
its existing 1.1.3 increment belongs to the initial change set. The nine maintained
source edits listed above include the pre-existing untracked common Praxis;
these are not nine additional tracked modifications. Earlier HCRLs retain former
preparation-path references as historical facts; iteration 004 records the removed
paths and current classification. Live records refer to HCRL history instead.

## Temporal evidence and remaining uncertainty

Inspected only the relevant local 2026-10-21 session for exact Apply messages and
original report-text emissions. The original Task, original Report and corrective
Task each preserve source observations/line locators and known date-only context.
The source is
`/home/hunterm/.codex/sessions/2026/10/21/rollout-2026-10-21T15-50-06-01a15ccb-8c74-77c0-b6fa-d35406198d34.jsonl`:
original instruction capture `2026-10-21T04:58:06.105Z` (line 133); original
report-text capture `2026-10-21T05:02:13.128Z` (line 230), with amendment capture
`2026-10-21T05:03:15.711Z` (line 259); corrective instruction capture
`2026-10-21T05:21:50.381Z` (line 381). These observations do not independently
establish the original issuance/production instants. No filesystem/commit/current
time or inferred sequence was substituted for them.

**UNRESOLVED:** original Task issuance, original Report production and corrective
Task issuance, with their three required UTC filename prefixes. Those records
retain descriptive filenames and explicit unresolved fields pending Solution
Architect treatment before closure. Full temporal conformance and strict original
chronological order are not claimed. The present Report's new production event
and filename conform without backdating the original execution or this Task.
Iteration 004, DR-7, holds the material limitation and required treatment.

## Verification and limits

**Report-Updated-At: 2026-10-21T05:27:48.132210Z.** Event: Codex amended this already
produced Report to record the completed documentation/preservation checks. Source:
contemporaneous producer declaration using the UTC system clock. This revision
does not replace the original production event or change its filename prefix.

Required architecture suite **PASS**: 90 tests in 11 classes, zero failures,
errors or skips; Maven BUILD SUCCESS in 18.271 seconds. Command:

```bash
timeout --signal=TERM --kill-after=5s 240s mvn test \
  -pl paradeigma/paradeigma-test -am \
  '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

It exited 0 within the bound. `python3 scripts/architecture_release.py check`
**PASS**: 1.1.3, previous 1.1.2. No command timed out or stalled. Temporary
recovery material and test output at `/tmp/harmonia-rad-1.1.3-correction-004/`
are working material, not new permanent evidence obligations.

Documentation/preservation checks **PASS**: 241 live local links/anchors across 17 Markdown documents; parsing/rendering; exact copied eight issued decision rows; nine matching DR sections/table rows/anchors; two historically scoped accepted Minutiae controls and one unanswered current group; six Praxis stages; exact 11-modified/two-removed/three-added corrective inventory; unchanged iterations 001/002/003 and release authority; unchanged original Report body and operative original Task context; current Task/Report correspondence; matching new Report event/UTC filename; preserved earlier release-policy content; clean added whitespace; no register completion. Six former-path link occurrences in frozen iterations 002/003 are explicitly accounted for by the two authorised removals, rather than claimed as live links. Three original event times and their filename prefixes remain **UNRESOLVED** for Solution Architect treatment before closure.

No production, model, publication, specialised machinery, broad migration,
historical-pilot investigation, cleanup or erratum work occurred. The architecture
suite and document checks establish their stated factual scope; they do not
establish the missing event times or architectural acceptance. DR-5/DR-8 retain
their historical acceptance scopes; the current bounded supporting group in DR-9
awaits human disposition. Solution Architect treatment of unresolved temporal
provenance and final confirmation remain pending. No complete/register record
or agent-issued closure is created.
