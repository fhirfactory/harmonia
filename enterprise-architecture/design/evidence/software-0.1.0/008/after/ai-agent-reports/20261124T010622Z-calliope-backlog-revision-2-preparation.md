Report-Produced-At: 2026-11-24T01:06:22.653455Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Backlog preparation report — BL-CALLIOPE-OO-01, revision 2

**Outcome · Submitted for Solution Architect review.** Prepared a proposed work
specification for software 0.1.0 / Iteration 008 against Architecture 1.1.26.
The file retains the original item ID and basename and uses the numbered
`.002.md` suffix following the backlog's `.001.md` examples. The revision
header uses `2`, consistent with the numbering of the existing item.
[Current work specification][rev2] · [Historical predecessor, revision 1][rev1] · [Preparation Task][task]

The proposal applies the qualified Final.001.001 design acceptance and the
existing independent review. The original 008 Implementation Task and Report
continue to refer to revision 1. Adoption for a future corrective assignment
requires an explicit Solution Architect decision. This submission leaves
HCRL.008 and its dispositions as recorded. [Final.001.001][design] ·
[Design acceptance][design-acceptance] · [Independent review][review] · [Pending HCRL.008][hcrl]

## Revision 1 → revision 2 reconciliation

| Criterion | Treatment in the proposal |
| --- | --- |
| AC-1 | Retained cohesive responsibilities, justified collaborators, complete 500-physical-line accounting and explicit per-file exceptions. Scope is the affected Topic/TopicMask files and their necessary collaborators. |
| AC-2 | Replaces blanket legacy API and serialization preservation with Final.001.001's Java API, qualified-path Topic identity, canonical string names and structural mask semantics. Retains applicable IDs and references, registry uniqueness/aliases, public APIs, persisted selectors, JSON/XML/Java serialization and consumer scope as historical obligations. Requires evidence of preservation or explicit authority for migration or qualification. |
| AC-3 | Replaces duplicate-index rejection with successful indexed replacement at unchanged depth. Retains exclusive modes, validation before mutation, nonnegative indices, contiguous final assembly, defaults, naming, recovery and immutable snapshots. The ordered entries replace the historical public map. The new core has immutable fields and no Date member. Defensive-copy obligations still apply to any supported legacy boundary that actually exposes mutable values. |
| AC-4 | Replaces UUID identity, the legacy name/canonical basis and implicit unrestricted defaults with explicit mask kinds, ordered qualifier/value/version criteria, exact or trailing-containment scope and RE2/J full-field matching. Adds explicit version-presence and canonical reconstruction checks. Retains positive, negative, null, repeatability and consumer tests and requires equivalent scope for established legacy selectors. |
| AC-5 | Retains educational documentation for classes, public/protected operations and non-obvious private behaviour. Makes the A-06 documentation check explicit. |
| AC-6 | Retains the exact integrated candidate and all applicable component, solution-wide regression, integration and conformance checks. Carries forward Final.001.001 §10 and C-02–C-05, including release bounds, logging/delivery evidence, skipped live gates and targeted cases. Required missing or unsuccessful outcomes require explicit treatment. |
| AC-7 | Retains the formal Task, Report, HCRL and exact source correspondence and attributable acceptance. Preserves actual revision 1 references in the earlier evidence and carries B-01, B-02 and C-06 into the applicable review route. |

Removed the revision 1 synopsis, context, activities and outcomes that assumed
duplicate-index rejection or blanket preservation of obsolete names and API
shapes. Updated the context to recognise the existing implementation candidate.
Retained the original OO and documentation objectives and the boundary around
unrelated Calliope, V001 and G03 work. [Revision 1 criteria][rev1-criteria] ·
[Current criteria][rev2-criteria] · [Accepted construction and API][design]

The proposal maps A-01/A-05 to AC-4, A-02–A-04 to AC-2 and A-06 to AC-5.
C-01/C-07 attach to actual historical and format boundaries, C-02–C-05 to
correction verification and C-06 to candidate attribution and closure.
D-01 remains optional. The XML preference applies where practical at an
applicable boundary. Selection of a codec depends on the actual boundary
contract. [Independent review findings][findings] · [NFR-099][nfr]

## Matters for Solution Architect review

1. **DR-3 / B-01.** Decide whether to adopt this exact revision as the work
   specification for the bounded correction assignment. Approval of the
   work specification and authority for Junie's correction are distinct
   dispositions. Any subsequent HCRL amendment requires its own approval.
2. **DR-3/DR-4/DR-7 / B-02, C-01, C-07.** Preserve the known Continuum and
   IU identity differences pending separate alignment or explicit treatment.
   Determine the disposition for affected historical boundaries where
   compatibility cannot be shown. The exact qualified IU vocabulary, approved
   mappings and any required adapter encoding retain their existing decision
   route. The proposal does not assume a deployed registry, a specific legacy
   corpus or a new XML binding.
3. **DR-5/DR-6/DR-8 / C-02–C-06.** Keep the candidate's release bounds, shared
   logging and delivery, integrated test gates and provenance qualifications
   pending actual evidence or explicit disposition. These remain candidate
   acceptance matters. [Pending HCRL.008][hcrl] · [F-01 reconciliation][arch-reconciliation]

## Verification and resulting state

- **PASS** · Compared the complete work specifications and traced all seven
  acceptance criteria to the proposed disposition above. Verified
  the accepted model and obligations against Final.001.001 §§1–10 and the
  independent review. Confirmed the mandatory template sections, local links
  and anchors, stable ID and separate revision path. Verification covered the
  documents and evidence correspondence. Product verification remains governed
  by AC-6.
- **PASS** · Verified the change against the entry Git state on
  `feature/BL-CALLIOPE-OO-01` at `8af315134a1f2e03e75512f66513ef2f7eeb80b8`.
  Revision 1 is byte-for-byte unchanged (SHA-256
  `56ce6e2acaa10e74c5673ca7a3898db9009811c0dbcc574266285741428c1ed7`).
  The exact proposed revision has SHA-256
  `9246fe5b8050e1a1a5e945cb0d25c30abb5e2835f15580bb4326d3b131da13a1`.
  The repository delta comprises the proposed backlog file and its new
  Iteration 008 Task and Report. The prior modified/untracked files, index and
  branch match entry.
- **UNRESOLVED** · The original issue time for this preparation Task is
  unavailable. Its event field and filename prefix record that limitation for
  Solution Architect treatment. Existing provenance qualifications retain their
  recorded treatment. [Preparation Task][task] · [Provenance contract][provenance]

**Post-submission locator update.** The links below resolve to the accepted
work specification, [Backlog Item 0003, revision 000.002][rev2], after the
Solution Architect's identity correction. The outcome and SHA-256 above
describe the reviewed proposal at preparation time. The application Reports
preserve both transitions and the exact correspondence. [Acceptance application][acceptance-application] ·
[Identity correction application][identity-correction]

[rev1]: ../../../../../../../docs/backlog/calliope-oo-01-topic-models.md
[rev2]: ../../../../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.002.md
[rev1-criteria]: ../../../../../../../docs/backlog/calliope-oo-01-topic-models.md#acceptance-criteria
[rev2-criteria]: ../../../../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.002.md#acceptance-criteria
[task]: ../../before/ai-agent-tasks/unresolved-calliope-backlog-revision-2-preparation.md
[design]: ../../../../../offline-design/DRP-Topic-and-TopicMask.Final.001.001.md
[design-acceptance]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-10
[review]: 20261124T005629Z-calliope-topic-topicmask-independent-review.md
[findings]: 20261124T005629Z-calliope-topic-topicmask-independent-review.md#prioritised-exception-register
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-008.pending.md
[nfr]: ../../../../../../continuum/motivation/principle/6973441f2d62448188af9bfce5ad9d9f.md
[arch-reconciliation]: ../../../004/after/F-01-Architecture-Reconciliation.md
[provenance]: ../../../../../../governance/architecture-change-provenance.md
[acceptance-application]: 20261124T011356Z-calliope-backlog-revision-2-acceptance.md
[identity-correction]: 20261124T011844Z-calliope-backlog-identity-correction.md
