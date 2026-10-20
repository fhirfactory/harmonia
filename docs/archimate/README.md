<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Working on the Harmonia architecture

Start with a published Harmonia model, author architecture in Archi, and submit
the completed iteration once for governed review. Continue from the next
publication after adjudication, reconciliation and validation.

## Round-trip engineering requests

Use the [governed Round-Trip Engineering Praxis](../markdown/governance/archimate-round-trip-engineering-praxis.md)
with Codex or Junie:

> Model Updated. Please review and generate change-review.md.

After adjudicating the resulting review:

> change-review.md reviewed and updated. Apply directives.

Identify the export or review only when provenance is ambiguous. The reviewed
document is the architectural checkpoint; generation does not accept proposals.
The framework supports governed agent execution with explicit capability limits,
not an unattended reconciliation service.

## Quick-start

1. Obtain the current applicable model from the [published package index](exchange/publish/README.md)
   and import its Open Exchange XML into Archi.
2. Confirm and retain the originating publication/release and exact package
   provenance. This is the one baseline for your **Authoring Iteration**.
3. Author architecture normally: choose appropriate native types, meaningful
   Names and authorised Harmonia `[xxx]` qualifiers where applicable. Save and
   experiment as often as useful; saves are working state.
4. Express intended semantic relationships explicitly. Folders and View geometry
   cannot substitute for Composition, Realization, Serving, Flow or other valid
   relationships. Preserve unknown meaning for review.
5. Create useful Views and layouts. Routine authoring need not manually maintain
   Harmonia-generated IDs, derived names, metadata, navigation or publication Views.
6. When ready for governance, deliberately export and [submit once](#submission-and-review)
   through the repository ingestion procedure. A backup export is not submission.
7. Treat that iteration as **closed**. Retain it for evidence or inspection;
   do not edit it into another governed submission.
8. Review detected changes and answer architectural questions. The Solution
   Architect adjudicates the generated Change Review under Increment 2A.
9. Have authorised changes reconciled and validated under 2A/2B, then complete
   the applicable release and publication gates.
10. Begin further governed architectural editing from the **next applicable
    Published Baseline**, incorporating the actual adjudicated outcome.

> Architect authors architecture; Harmonia machinery authors governance machinery.

You may inspect machine-governed metadata. Routine architecture work does not
require hand-building it. New semantic relationships must exist in the semantic
model; adding a visual connection to an existing relationship creates another
appearance, not another relationship. Accepted authored Views and deterministic
generated Views have distinct preservation/publication obligations under 2B.

## One governed architecture, several representations

The **Harmonia repository is the governed architecture system of record**.
[Markdown RADS](../markdown/) are the governed textual architecture source;
Archi is the interactive workbench; Open Exchange XML is the interchange mechanism.
LaTeX RAD and publication output are publication representations. These and
other governed projections support consumption and review without becoming
competing architecture repositories.

The current release identifier comes from the root [release authority](../../architecture-release.json).
It does not by itself identify a completed publication. Confirm your model using
the published package index and its publication evidence, rather than assuming
an XML exists for every release or opening an arbitrary older Archi file.

## Submission and review

Submission is the deliberate handover of your completed iteration to governed
ingestion: export the model and place/provide it through the
[incoming-exchange procedure](exchange/ingest/README.md), retaining the
[2A baseline, input, run and hash evidence](processing/ingestion/README.md#run-layout).
Saving, autosaving, retaining a backup export or making an unrelated Git commit
does not submit anything. The reconciliation engine remains future work; use
the governed procedure without assuming an automatic ingestion service exists.

```text
save != submission
submission != acceptance
acceptance != reconciliation
reconciliation != publication
authoring iteration != architecture release
```

Submission proposes architectural change. Detection and Change Review follow;
only authorised changes may enter the governed architecture. During that interval,
review evidence, answer adjudication questions and discuss future ideas outside
the governed authoring stream. A closed iteration cannot produce another governed
submission. Wait for the next applicable publication to begin further governed
authoring; current Praxis uses a serial publication lineage without branching
or merge semantics.

The [Increment 2C Praxis in RADS](../markdown/governance/architecture-authoring-praxis.md)
is the normative lifecycle contract behind this entry point. It provides the
[distinct lifecycle terms](../markdown/governance/architecture-authoring-praxis.md#terms-that-must-remain-distinct),
[responsibility split](../markdown/governance/architecture-authoring-praxis.md#human-and-processing-responsibilities),
[recovery guidance](../markdown/governance/architecture-authoring-praxis.md#recovery-and-interrupted-work)
and a [worked fictional lifecycle](../markdown/governance/architecture-authoring-praxis.md#worked-example--illustrative-not-architecture-authority).
Increment [2A](../markdown/governance/archimate-ingestion-change-review.md) governs
review and authorisation; [2B](../markdown/governance/archimate-ingestion-publication-semantics.md)
governs authorised interpretation, representation, organisation and publication.

## Example: one iteration, one submission

From Published Baseline N, an architect explores a fictional “Workshop Booking”
Business Process and Business Service, authors their intended valid semantic
relationship, and adds a review View. Several saves later, they export and submit
once; that iteration closes. Detection produces a Change Review, the Solution
Architect adjudicates its CRs, and authorised changes are reconciled and validated.
After the applicable gates and publication complete, further authoring begins
from Published Baseline N+1. This example accepts no architecture and concerns
none of the current blind-test proposals; N/N+1 denotes successive publications,
not release increment rules.

## Workspace navigation

```text
docs/archimate/
├── framework/                       reusable machinery
├── governance-tooling/              non-RAD internal evidence and migration audits
├── processing/
│   ├── publishing/<release>/        publication working state and evidence
│   └── ingestion/                   incoming-export working state and evidence
└── exchange/
    ├── publish/<release>/           finished governed exchange packages
    └── ingest/                      incoming exchange files
```

[Framework](framework/README.md) contains reusable tools, schemas, tests,
projection support and framework baselines. [Processing](processing/README.md)
keeps working material separate from exchanged files and separates publishing
from ingestion. [Publish](exchange/publish/README.md) contains versioned finished
packages: currently one Open Exchange XML per release. [Ingest](exchange/ingest/README.md)
is the landing area for incoming Archi exports.

[Governance/tooling evidence](governance-tooling/README.md) is structurally separate
from architecture publications. The internal identifier 1.0.14 has no RAD release
classification; its original artefacts and provenance are preserved there.

### Superseded architectural notes — preserved history

------- Legacy Content - Superseded ------- Start ------

Superseded by:
[Increment 2C working model](../markdown/governance/architecture-authoring-praxis.md#working-model-and-representations),
[baseline selection](../markdown/governance/architecture-authoring-praxis.md#begin-from-a-published-baseline),
[submission lifecycle](../markdown/governance/architecture-authoring-praxis.md#submit-once-and-close-the-iteration)
and [review through publication](../markdown/governance/architecture-authoring-praxis.md#review-reconcile-validate-and-publish).
The following original architectural notes are unchanged historical context;
use the current Praxis and published package index above for authoring.

---------------------------------------------------------

Publication: **Harmonia RAD → framework → processing/publishing → exchange/publish → Archi**.
The future return flow is **Archi → exchange/ingest → processing/ingestion →
comparison/reconciliation → Harmonia RAD**. Increment 2 is not implemented here.

The [governed Ingestion Change Review](../markdown/governance/archimate-ingestion-change-review.md)
formalises the return flow's review/adjudication and executable reconciliation
contract. Its [run layout](processing/ingestion/README.md#run-layout) retains the
review and detection, reconciliation and validation evidence. Increment 2
execution machinery remains future work; this contract approves no blind-test change.

The governed [Markdown RAD](../markdown/) owns the architecture. Canonical identity
and governed names belong to governed architectural resources and elements.
The framework, processing files and exchange packages establish no competing
architectural authority. **Archi export is architecture change input, not
replacement architecture repository.** Incoming files may contain semantic changes,
presentation changes and known transport transformations. Comparison/reconciliation
and Solution Architect acceptance must precede changes to the Harmonia RAD.

Release 1.0.7 remains the frozen accepted identity-bearing baseline and real
Archi 5.10 identity/naming proof. Release 1.0.8 is the policy-defined structure and
tooling release with unchanged architecture and presentation apart from release
metadata. The root [release authority](../../architecture-release.json) determines
the current release; generation never increments it.

------- Legacy Content - Superseded ------- Finish ----

### Historical navigation

Historical reports, manifests, logs and source archives retain their original
bytes and path statements. Use the [location map](framework/historical-locations.json)
for current navigation to a recorded historical locator. Relative links inside
frozen reports may still describe their original location. The
[migration report](../../.junie/reports/2026-10-18-archimate-workspace-restructure.md)
records preservation and validation. The [former README](framework/historical-workspace-readme.md)
is retained as history; use the framework README for current commands.
