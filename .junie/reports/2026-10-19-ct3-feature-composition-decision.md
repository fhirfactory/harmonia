<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# CT3 → Feature Composition: Solution Architect decision

Recorded by Codex, **2026-10-19 (Australia/Sydney)**. The explicit Solution
Architect task decision resolves a shared metamodel question exposed during
review of **1.0.8/run-001**. The review was prepared on 2026-10-18; no earlier
adjudication date is present. No proposal adjudication or execution is implied.

The [revised Change Review](../../docs/archimate/processing/ingestion/1.0.8/run-001/change-review.md#shared-metamodel-decision)
is **revision 2** in the same run. Its summary table remains first visible content.
The [revision-1 copy](../../docs/archimate/processing/ingestion/1.0.8/run-001/history/revision-001/change-review.md)
is byte-identical; its manifest records the original path for relative links.

## Governed decision and release

Within the Business Enabling Capability hierarchy, accepted native ArchiMate
Composition from CT3 Capability to Feature Capability canonically establishes
Feature framing and semantic containment. The hierarchy is
`CT1 → Composition → CT2 → Composition → CT3 → Composition → Feature`.
Feature remains native `Capability`, Harmonia classification `Feature`.
Folder nesting, View membership/geometry, naming proximity and navigation cannot
independently supply containment. This defines accepted edge meaning; it approves
no submitted edge, endpoint, placement standing or missing ancestry.

The durable rule is in the [Business metamodel §1.1](../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md#bec-composition-hierarchy).
Consistent references were amended in the existing [mapping profile §5](../../docs/markdown/governance/rad-archimate-model-mapping.md#5-relationship-profile-and-direction),
[2B semantics](../../docs/markdown/governance/archimate-ingestion-publication-semantics.md),
[mapping catalogue](../../docs/markdown/governance/rad-archimate-mapping-catalogue.md),
[coverage assessment](../../docs/markdown/governance/rad-archimate-coverage-assessment.md)
and [governance index](../../docs/markdown/governance/README.md).
Current-authority hashes were refreshed in existing
[correspondence](../../docs/markdown/governance/baseline/correspondence.json).

Under the [release policy](../../docs/markdown/governance/architecture-release-policy.md),
the accepted metamodel change increments **1.0.11 → 1.0.12**;
`previous_version=1.0.11`. No publication generator was run. The incoming
Authoring Iteration remains closed and its Published Baseline remains **1.0.8**.
AX-04 preserves Harmonia meaning; AX-14 separates rule, evidence and instance
disposition; AX-15/17 retain unresolved identity, standing and record handling.

## Review treatment

**CR-016–CR-021 remain On-Hold.** Their six valid findings exposed one missing
rule, now resolved by the governing decision. Each preserves its exact Detected
Evidence, original candidate/questions, R1–R6 relationship trace and paired
CR-010–CR-015 required-state dependency. No second interpretation decision is
needed if the corresponding Composition is later accepted. The remaining gap is
how to close/supersede those retained records: 2A has no explicit disposition for
this case, and Accept/Reject/Manual Intervention would add a final individual
CR decision not supplied by the Solution Architect. Record handling remains
pending Solution Architect confirmation; no new status was invented.

**CR-010–CR-015 remain On-Hold and independently adjudicable.** All other CR
decision fields are unchanged. CR-001's question and CR-033's View question were
clarified to reference the general rule; the obsolete conditional requirement for
six separate interpretation approvals was removed from CR-033. Its existing
required reconciled element/edge states remain. No dependency was executed.
Realization/logical Feature context questions remain unresolved.

The [revision overlay](../../docs/archimate/processing/ingestion/1.0.8/run-001/review-revision-002.json)
records current decision/decomposition treatment. Detection and revision-1
validation files remain unchanged historical evidence. All 36 CRs retain
`Not authorised` instructions and `Not yet processed` processing records.

## Validation and preservation

The bounded architecture suite passed **90 tests**. Release, identity, capture
and source-history regression suites passed **72 tests**. The release check
passes; candidate-capture validation passes with all **2,467** unresolved
dimensions retained. Commands and initial/final results are recorded in
[revision-2 command evidence](../../docs/archimate/processing/ingestion/1.0.8/run-001/validation/revision-002/command-results.json).

Initial capture validation failed because it compared frozen source-era hashes
and locators against the amended live metamodel; this also caused one regression
failure and two errors. The frozen capture was not rewritten. The exact original
metamodel (SHA-256 `321cdfa5aee6b178403dc5508c2e6e7ae7c40138aecf18a6d6b791063af0343f`)
is now retained as compressed historical evidence. Existing correspondence and
[baseline guidance](../../docs/markdown/governance/baseline/README.md) identify
its historical role. The bounded change to
[source validation](../../scripts/rads_business_architecture.py) proves the live
current-authority pin, compressed archive hash and exact original hash before
using it solely for frozen-capture checks. Added regression cases reject source
or archive drift, incorrect original bytes and paths outside governed history.
This preserves semantic amendments rather than treating them as removable
presentation notes. The existing presentation overlay remains byte-identical.

[Independent revision validation](../../docs/archimate/processing/ingestion/1.0.8/run-001/validation/revision-002/validation-results.json)
checks all 36 IDs, table/detail parity, statuses, decisions/instructions/processing,
exact observed evidence, links, dependencies, hashes, authority pins and the write
boundary against the start snapshot. Prior review, detection, blind reports,
source instance architecture, migration findings and publication packages are
preserved. The archive and revision evidence are the only generated additions;
no architecture/publication projections were regenerated.

Submission:
`docs/archimate/exchange/ingest/1.0.8/HarmoniaArchitecture-1.0.8.xml`
SHA-256: `6b973db242a4b26bc6a93e27b482100b6f96655cb40deb4ceb35a93c134c7d3e`.

Published Baseline:
`docs/archimate/exchange/publish/1.0.8/Harmonia-Architecture-1.0.8.xml`
SHA-256: `504703cd1c4a91efe0f598db6a974ffea1a08541ebe81372130f1d647d71d69a`.

Both inputs and retained Submission are byte-identical. No new Submission,
Authoring Iteration, run, proposed Canonical ID, reconciled proposal or Published
Baseline was created. No CR was accepted, rejected or sent for manual execution.
The only architectural adjudication recorded is the explicit general metamodel
decision. Work stops at this revised review for the Solution Architect.
