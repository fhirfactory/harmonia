Report-Produced-At: 2026-11-24T01:18:44.406193Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Identity correction report — Calliope Backlog Item 0003, revision 000.002

Applied the Solution Architect's exact identity and path for the accepted
corrective work specification in software 0.1.0 / Iteration 008.
[Backlog Item 0003, revision 000.002][corrective] occupies
`docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.002.md`.
The [historical predecessor][predecessor] is BL-CALLIOPE-OO-01, revision 1.
The [DR-3 record][dr3] identifies the corrected work specification and links
the initial disposition and the correction. Acceptance remains qualified and
the implementation candidate remains pending. All seven acceptance criteria,
including the AC-6 distinction and platform qualifications, match the
previously accepted content byte for byte.
[Operative correction][task] · [Previously accepted application][application]

The prior acceptance used the filename
`BacklogItem-BL-CALLIOPE-OO-01-Calliope-Topic-and-TopicMask.002.md`.
Current references in the preparation and acceptance evidence now resolve to
the corrected path. Original instructions, event fields and content hashes in
those records retain their original meaning. The original implementation Task
and Report continue to identify revision 1.

## Verification

- **PASS** · Checked the new path, heading, item ID and revision, and verified
  the corrected HCRL and evidence links. Verified the historical predecessor,
  original implementation evidence and out-of-scope paths against the entry
  state at `8af315134a1f2e03e75512f66513ef2f7eeb80b8`.
- **PASS** · Reversing the correction patch below reproduces the originally
  accepted backlog file with SHA-256
  `258decaad784b56368e6ce6ccb53f9494fca7d24e79e58eb72b8ef888af29d0f`.
  Reversing the prior application patch then recovers the proposal, SHA-256
  `9246fe5b8050e1a1a5e945cb0d25c30abb5e2835f15580bb4326d3b131da13a1`.
  The corrected file has SHA-256
  `ec7d82a98eb040afe9998bacb711354b82dc7cacf78438c2f6996ada2cff4499`.
- **UNRESOLVED** · The original issue time of the operative correction was
  unavailable. The Task records that limitation for Solution Architect
  treatment. [Task][task] · [Provenance contract][provenance]

## Identity correction patch

Reverse this patch against the corrected file in a temporary destination to
reconstruct the before-correction bytes. The previous application Report
contains the separate proposal-to-acceptance patch.

```diff
--- originally-accepted-backlog.md
+++ corrected-backlog.md
@@ -3 +3 @@
-# Backlog Item: BL-CALLIOPE-OO-01 — Cohesive Topic and TopicMask implementation
+# Backlog Item: 0003 — Cohesive Topic and TopicMask implementation
@@ -5 +5 @@
-**Backlog Item ID:** BL-CALLIOPE-OO-01
+**Backlog Item ID:** 0003
@@ -7 +7 @@
-**Backlog Item Revision:** 2
+**Backlog Item Revision:** 000.002
@@ -42,6 +42,7 @@
-HCRL.004 DR-10. Revision 1 records the original accepted work specification and
-continues to identify the source used by the Iteration 008 implementation
-assignment. Revision 2 is the accepted work specification for the subsequent
-bounded corrective implementation assignment. The assignment supplies execution
-authority. Acceptance of the Iteration 008 implementation candidate and
-integrated software release remains pending. [Revision 1][revision-1] · [Design acceptance][design-acceptance] ·
+HCRL.004 DR-10. The historical predecessor, [BL-CALLIOPE-OO-01, revision 1][historical-predecessor],
+records the original accepted work specification used by the Iteration 008
+implementation assignment. Backlog Item `0003`, revision `000.002`, is the
+accepted work specification for the subsequent bounded corrective implementation
+assignment. The assignment supplies execution authority. Acceptance of the
+Iteration 008 implementation candidate and integrated software release remains
+pending. [Design acceptance][design-acceptance] ·
@@ -49 +50 @@
-[Independent review and candidate locator][review] · [Revision 2 preparation Task][preparation-task]
+[Independent review and candidate locator][review] · [Preparation Task][preparation-task]
@@ -61 +62 @@
-accepted software baseline remains a separate decision. [Revision 1][revision-1] ·
+accepted software baseline remains a separate decision. [Historical predecessor][historical-predecessor] ·
@@ -140 +141 @@
-[revision-1]: calliope-oo-01-topic-models.md
+[historical-predecessor]: calliope-oo-01-topic-models.md
```

[corrective]: ../../../../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.002.md
[predecessor]: ../../../../../../../docs/backlog/calliope-oo-01-topic-models.md
[dr3]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-008.pending.md#dr-3-backlog-compatibility-and-historical-migration
[task]: ../../before/ai-agent-tasks/unresolved-calliope-backlog-identity-correction.md
[application]: 20261124T011356Z-calliope-backlog-revision-2-acceptance.md
[provenance]: ../../../../../../governance/architecture-change-provenance.md
