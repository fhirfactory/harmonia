<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Change Request Disposition and Outcome governance

Recorded by Codex, **2026-10-19 (Australia/Sydney)**, from the explicit accepted
Solution Architect governance decision. [Change Review revision 3](../../docs/archimate/processing/ingestion/1.0.8/run-001/change-review.md#disposition-outcome-transition)
remains in **1.0.8/run-001**. No new input, run or Authoring Iteration was created.

## Decision and authoritative changes

[Increment 2A](../../docs/markdown/governance/archimate-ingestion-change-review.md#solution-architect-dispositions)
now defines **Disposition** as the Solution Architect's authorised call to action
to the processing agent. Its exact values remain Accept, Reject, On-Hold and
Manual Intervention. The independent **Outcome** records resulting CR state;
**Outcome Reason** separately explains why, citing applicable authority/evidence.
The only Outcome values introduced are **Unresolved** and **Resolved**. Processing
results and records keep their existing meanings. No Disposition determines an
Outcome automatically; resolution supplies no reconciliation authority.

Issued Dispositions are retained audit records. Later events update Outcome,
Outcome Reason and processing evidence rather than silently rewriting the call
to action. Explicit later Solution Architect re-adjudication may revise an
instruction only with its prior history preserved. Existing run revision rules
suffice; no event-sourcing or workflow mechanism was introduced.

The [template](../../docs/markdown/governance/templates/archimate-ingestion-change-review-template.md)
now separates all required fields, adds Outcome to the compact summary and includes
a worked On-Hold/Resolved finding alongside the four Disposition examples. Newly
generated CRs start On-Hold/Unresolved, Not authorised and Not yet processed, with
a reason for their unresolved state. No fictitious adjudicator/date is generated.

Directly dependent [2B semantics](../../docs/markdown/governance/archimate-ingestion-publication-semantics.md),
[2C Praxis](../../docs/markdown/governance/architecture-authoring-praxis.md),
[governance index](../../docs/markdown/governance/README.md) and
[run revision guidance](../../docs/archimate/processing/ingestion/README.md)
use the distinction consistently. Only the two changed existing current-authority
pins were refreshed in [correspondence](../../docs/markdown/governance/baseline/correspondence.json).
Unrelated element, duplicate and command-result uses of status were preserved.

Current release authority was **1.0.12**, previous 1.0.11. Under the
[release policy](../../docs/markdown/governance/architecture-release-policy.md),
this accepted change increments to **1.0.13**, previous **1.0.12**. The
[1.0.12 CT3 → Feature metamodel decision](../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md#bec-composition-hierarchy)
remains byte-identical. No Published Baseline was created.

## Current review treatment

All **36 Dispositions remain On-Hold**. **CR-016–CR-021 have Outcome Resolved**,
with each Outcome Reason citing the exact CT3 → Feature rule established in
release **1.0.12**. These were valid findings: the common meaning question was
unresolved when detected, and On-Hold correctly withheld processing. Subsequent
governance resolved it without six instance interpretation decisions or any
reconciliation. The formerly identified record-handling gap is now resolved.

**CR-001–CR-015 and CR-022–CR-036 have Outcome Unresolved** and remain
architecturally undecided. CR-010–CR-015 are neither accepted nor reconciled by
resolution of the related findings. All Authorised Reconciliation Instructions
remain **Not authorised**; all Processing Records remain **Not yet processed**.

The historical required-state references from CR-016–CR-021 to CR-010–CR-015
are retained as conditional information for any later independently authorised
instance representation, not prerequisites to resolution of the common question.
The other 30 CRs' dependencies are unchanged. No dependency has been executed.
The [revision-3 overlay](../../docs/archimate/processing/ingestion/1.0.8/run-001/review-revision-003.json)
records this distinction and the separate fields.

## Validation and preservation

The bounded architecture suite passed **90 tests**. Governance, release,
identity, source-history and capture suites passed **72 tests**. Release metadata
validation passes; frozen candidate-capture validation passes with all **2,467**
previous unresolved dimensions preserved. [Commands and results](../../docs/archimate/processing/ingestion/1.0.8/run-001/validation/revision-003/command-results.json)
and [independent review checks](../../docs/archimate/processing/ingestion/1.0.8/run-001/validation/revision-003/validation-results.json)
are scoped to revision 3. Previous validators/results remain historical evidence.
The required AX-04/14/15/17 distinctions between authority, instruction, observed
evidence, interpretation, actual processing and resulting state are preserved.

A start-state hash audit verifies the write boundary and all original detected
evidence. Review revision 2 and the prior contract/template were retained
byte-for-byte under [history/revision-002](../../docs/archimate/processing/ingestion/1.0.8/run-001/history/revision-002/manifest.json);
revision 1, revision-2 overlay/validation, detection files, source architecture,
prior reports, source-era archives, migration findings and publication packages
remain unchanged. Archived relative links resolve from the original paths in
the manifests. Historical Status fields denote Disposition, not Outcome; they
were not rewritten to the new representation.

Submission SHA-256:
`6b973db242a4b26bc6a93e27b482100b6f96655cb40deb4ceb35a93c134c7d3e`
for `docs/archimate/exchange/ingest/1.0.8/HarmoniaArchitecture-1.0.8.xml`.
Published Baseline **1.0.8** SHA-256:
`504703cd1c4a91efe0f598db6a974ffea1a08541ebe81372130f1d647d71d69a`
for `docs/archimate/exchange/publish/1.0.8/Harmonia-Architecture-1.0.8.xml`.
Both and the retained Submission remain byte-identical. No submitted element
received a Canonical ID; no submitted relationship or View was changed.

**Remaining governance uncertainty in this increment: none.** Existing undecided
architectural proposals and identity/Realization-context questions remain in the
review. No proposal was adjudicated or reconciled, and no publication occurred.
Work stops at the revised Change Review.
