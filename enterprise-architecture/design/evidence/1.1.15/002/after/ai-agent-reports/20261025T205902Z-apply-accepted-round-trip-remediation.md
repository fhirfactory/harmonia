Report-Produced-At: 2026-10-25T20:59:02.250620Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
# HCRL 1.1.15-002 — application and verification Report

Producer: Codex, co-executor. Boundary: RAD 1.1.15, iteration 002.
Corresponding [application Task](../../before/ai-agent-tasks/unresolved-apply-accepted-round-trip-remediation.md).
Authoritative input: [exact reviewed HCRL](../../before/hcrl-1.1.15-002.reviewed.md),
with source/hash correspondence in [application inputs](../../before/application-inputs.json).
Current decision/result record: [HCRL 002](../../../../../review/1.1.15/hcrl-1.1.15-002.pending.md).

## Incorporated result

The accepted opt-in round-trip machinery and operational instructions are now
operative as reviewed. The normal single patch increment was prepared through
`architecture_release.py bump --expect 1.1.14`: root authority now records
**1.1.15**, predecessor **1.1.14**. This is the accepted machinery change's release;
it incorporates none of the separate iteration 001 architecture proposals.

Application and automated verification succeeded. The next mandatory checkpoint
is **Solution Architect confirmation of the incorporated result**, under Review
Praxis §6. HCRL remains pending in `design/review`; no completion/register record
or Git mutation has been performed.

| Accepted matter | Actual application |
| --- | --- |
| DR-1 | Adopted `review --normalised` as the accepted opt-in path; removed obsolete machinery-proposal status from tool help/delta standing while retaining UNREVIEWED architecture, exact-baseline validation and all fail-closed guards. The existing default review remains unchanged as reviewed. |
| DR-2 | Adopted the bounded Praxis/tooling/workbench instructions and evidence preparation behavior. Preserved old link anchors, independent CR interpretation, HCRL/SA checkpoints, event uncertainty, no-overwrite behavior and the review-only schema boundary. |
| DR-3 | Adopted the reviewed optional `preserve_view_hierarchy=True` behavior and retained its preservation/regeneration tests. Preserved the explicitly reviewed publication default; no material View/navigation publication change was inferred. |
| DR-4 | Preserved the reviewed decision input, operative application Task, input/state pins, validation evidence and this Report. Synchronized Minutiae-Accepted to the explicit Yes in the Solution Architect's application instruction, with attribution. Unavailable event times remain factual uncertainty. |

Affected maintained sources are `continuum_workbench.py`, `exchange_compare.py`,
`architecture_review_evidence.py`, Round-Trip Praxis, EA tooling README and the
Archi workbench README. Their post-adjudication changes adopt accepted status and
operating instructions; they do not add another framework or alter the reviewed
comparison algorithm, default CLI behavior, application adapter, identity policy
or publication default. The earlier implementation/tests remain the accepted
engineering content of this change set.

Normal generation produced the 1.1.15 Manifest/browser, narrative RAD and durable
[workbench projection](../../../../../archimate/published/1.1.15/Harmonia-Architecture.xml)
under their maintained release destinations. The generated Continuum Manifest
changed only its release heading. Existing releases were preserved.

## Verification and regression

The [application validation summary](../application/validation-summary.json)
records the actual checks, limits and protected-state results.

- `make -C enterprise-architecture/tooling all` passed generation, **81 EA tests
  in 293.150 seconds**, and release/Manifest/Exchange/browser/narrative/link
  consistency checks. Expected malformed-input diagnostics are test fixtures,
  not implementation failures.
- The required architecture suite passed with cached offline Maven dependencies:
  **90 tests, zero failures/errors/skips**, total **25.814 seconds**.
- The current-release browser smoke passed identity search, selection, Feature
  filtering and combined-currentness filtering over 1,602 elements.
- The current 1.1.15 projection passed identical-input normalized review in
  **11.285388 seconds**, returning REVIEW_READY with no exceptions or delta.
  This demonstrates intake readiness, not an actual Archi no-edit transport.
- `git diff --check` and active-document/evidence link checks passed.

The [001 regression](../application/001-regression-summary.json) used the exact
original baseline/candidate hashes and an isolated matching 1.1.14 maintained
source fixture. Live release authority was not rewound and no historical
publication was relabelled. Comparison took **11.554961 seconds**; fixture and
comparison together took **11.885649 seconds**. The **68,799-byte** delta matches
the retained engineering benchmark apart from machinery standing and measured
elapsed time. It retains 12 additional elements, 9 relationships, 7 Views and
17 existing View navigation changes, with no representation exception. No AI
review, DRP corpus retrieval or CR application was invoked.

The [publication comparison](../application/publication-validation.json) proves
that the only 1.1.14 → 1.1.15 XML difference is the model's
`Baseline / Architecture Release` value. All **1,602 elements, 1,651 relationships,
1,209 Views and the full organizations XML** are unchanged. The new projection's
SHA-256 is `61e7ebb958b321404c1c4694ae952119480f1fabd04dc2d319f35def84313dec`.
The accepted baseline remains
`ee1d7ef8d5491a4a656d8c2f07c38999b6276a130064d797a08f19501aa47161`.
The raw comparator's single release-property observation is accounted for as the
explicit normal patch consequence; no new general normalization rule hides it.

The optional View-preservation regression passed within the full EA suite.
Because this application changes no actual Archi organization, it does not
trigger the material-organization structural-inspection safeguard. Actual Archi
import/export/visual inspection was not performed or claimed. Any future
material use of the option retains those normal proof and human checkpoints.

## Preservation, provenance and closure boundary

All **1,727 protected files** retained their pre-application hashes: iteration 001
evidence and review/HCRL, canonical element/navigation sources, original template,
historical publications, original 002 benchmark evidence and available Git
HEAD/index/packed-refs. The derived root Manifest release heading and release
authority were intentionally outside that unchanged-source boundary. No canonical
architectural element/relationship, candidate or authored View was changed.

**CR-001 through CR-009 were not applied, disposed, reclassified, duplicated or
otherwise modified.** Iteration 001 remains separate. Its original baseline and
review standing were not rewritten because the machinery's release advanced.
No new architectural CR or automatic schema-2 application mechanism was invented.

The HCRL received all four Accept dispositions as authoritative input. Its
Minutiae field still contained a placeholder at receipt; the operative Task's
explicit Yes supplies attributable authority for the mechanical synchronization.
The exact original was retained before editing, with its embedded relative links
interpreted from the recorded original source location.

Previously unavailable Task times remain preserved under the accepted DR-4
treatment. This application instruction's entry timestamp is also unavailable in
the exposed interaction, so its Task event and filename prefix remain explicitly
UNRESOLVED for result confirmation; none was guessed. This Report's completed
emission event is captured by the accepted evidence emitter. These factual limits
do not reopen the accepted architectural decisions or claim temporal conformance
that cannot be established.

No new architectural contradiction or exceptional representation condition was
exposed. No command timed out or stalled. No blocking defect requiring further
tooling development was identified. Continue Harmonia architecture/solution work
after normal result confirmation; defer unrelated tooling extensions.

**No Git mutation was performed:** no staging, commit, push, merge, rebase,
amend, tag, branch creation or remote/history mutation. The resulting working
tree contains the accepted implementation, release preparation, generated
artifacts and iteration-scoped evidence for Solution Architect inspection.
Commit/history ownership remains with the Solution Architect. Stop at the
incorporated-result confirmation checkpoint; do not self-complete the HCRL.
