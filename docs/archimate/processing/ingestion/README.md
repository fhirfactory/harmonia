<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Ingestion processing

This boundary is reserved for working state and evidence produced while analysing
incoming Archi exports. The incoming files themselves belong in
[exchange/ingest](../../exchange/ingest/README.md). No ingestion reconciliation
workflow is implemented by this workspace restructure.

An export and its processing results do not become authoritative architecture.
The Solution Architect must accept reconciled changes into the governed Harmonia
RAD. **Archi export is architecture change input, not replacement architecture repository.**

The governed [Ingestion Change Review contract](../../../markdown/governance/archimate-ingestion-change-review.md)
now defines the review/adjudication and executable-instruction boundary; the
reconciliation engine remains unimplemented. Use the
[governed template](../../../markdown/governance/templates/archimate-ingestion-change-review-template.md)
when preparing a review. A generated review defaults every CR to On-Hold.

<a id="run-layout"></a>

## Run layout

For new ingestion runs use:

```text
docs/archimate/processing/ingestion/<baseline-release>/run-<nnn>/
├── change-review.md
├── detection/
├── reconciliation/
└── validation/
```

`<baseline-release>` is the exact governed publication release used for comparison,
not an asserted release from the incoming export or a presumed future acceptance
release. Under that baseline, serially allocate the next unused positive run
number, starting `run-001`, zero-padded to at least three digits. Keep it fixed;
never reuse it or rename it after adjudication or a release bump. The run locator
and CR IDs are process identities, never architectural identities. Allocate no
run merely to install this convention.

Record the exact incoming path/hash and baseline path/hash in `change-review.md`,
along with source revision, detection methods, decomposition and evidence limits.
Retain a byte-identical incoming snapshot under `detection/` if its landing path
may be overwritten; otherwise reference an immutable, hash-pinned artefact. Do
not move, repair or rewrite the original. A distinct incoming revision requires
a new run with a link to its predecessor, rather than replacing observed evidence.
Review/adjudication revisions for the same input stay in the same run, with prior
decisions and instruction revisions recoverable through retained version history
or explicit immutable revision copies. Do not overwrite unaudited history.

- `detection/`: raw observations, comparisons, source/target evidence, transport
  controls and preserved inputs/provenance where needed.
- `reconciliation/`: authorised instruction revision references, execution or
  manual-action evidence, actual changes and before/after artefacts/hashes.
- `validation/`: checks, commands, results and evidence of the resulting state,
  including unresolved outcomes and publication references when applicable.

Link evidence by relative paths from the review and cross-reference the stable
CR IDs. Create evidence directories when they contain evidence; do not invent
successful reconciliation or validation records to fill the layout. Publication
packages and publishing working state retain their existing locations.

The pre-existing `1.0.8/blind-observation/` directory and the three blind-analysis
report bundles retain their original names and bytes. They are historical
observation evidence, not retroactively generated Change Reviews; no migration,
adjudication or execution of their proposals is authorised by this convention.
