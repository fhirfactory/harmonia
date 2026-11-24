<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# First governed ArchiMate ingestion Change Review — preparation only

**2026-10-18.** The [Change Review](../../docs/archimate/processing/ingestion/1.0.8/run-001/change-review.md)
is ready for Solution Architect adjudication. It contains **36 CRs, all On-Hold**.
No architectural decision, reconciliation instruction, execution, identity allocation
or publication has occurred.

## Exact Submission and originating Published Baseline

| Evidence | Exact artefact | Bytes | SHA-256 |
| --- | --- | ---: | --- |
| Submission | [HarmoniaArchitecture-1.0.8.xml](../../docs/archimate/exchange/ingest/1.0.8/HarmoniaArchitecture-1.0.8.xml) | 21,854,756 | `6b973db242a4b26bc6a93e27b482100b6f96655cb40deb4ceb35a93c134c7d3e` |
| Published Baseline 1.0.8 | [Harmonia-Architecture-1.0.8.xml](../../docs/archimate/exchange/publish/1.0.8/Harmonia-Architecture-1.0.8.xml) | 20,800,334 | `504703cd1c4a91efe0f598db6a974ffea1a08541ebe81372130f1d647d71d69a` |
| Retained incoming snapshot | [detection/submission.xml](../../docs/archimate/processing/ingestion/1.0.8/run-001/detection/submission.xml) | 21,854,756 | `6b973db242a4b26bc6a93e27b482100b6f96655cb40deb4ceb35a93c134c7d3e` |

The [published package index](../../docs/archimate/exchange/publish/README.md),
[publication report](../../docs/archimate/processing/publishing/1.0.8/publication-1.0.8.md),
[inspection guide](../../docs/archimate/processing/publishing/1.0.8/archi-inspection-1.0.8.md)
and [publication comparison](../../docs/archimate/processing/publishing/1.0.8/publication-comparison.json)
identify this exact 1.0.8 package. In-memory restoration of its one release value
reproduces the entire published 1.0.7 byte stream. The submission retains the
published model identity, 1.0.8 release Property, all 1,299 original canonical
owners and established element/relationship bindings. This establishes the
applicable originating published representation without inventing native GUI
edit history or a further human acceptance event.

The repository governance release is **1.0.11**, previous 1.0.10. That value was
not used as the semantic comparison baseline. The deliberate Submission closes
this one Authoring Iteration; earlier evolving exports are historical working
state, not additional Submissions.

## Run, authority and method

The next unused run was **1.0.8/run-001**, allocated under the existing
[ingestion run convention](../../docs/archimate/processing/ingestion/README.md#run-layout).
The run contains the review, immutable input snapshot, baseline/provenance
references, fresh detection, decomposition and validation evidence. The reserved
`reconciliation/` location has no evidence and is not materialised, following the
convention's instruction to create evidence directories when they contain evidence.

Read the root/docs AGENTS instructions, current release authority, Architectural
Axioms, Increment 2A/2B/2C contracts, identity/naming, navigation/View, mapping and
release contracts, all four requested ArchiMate entry/procedure documents and the
governed Change Review template. Exact source revision and working-tree authority
hashes are retained in [provenance.json](../../docs/archimate/processing/ingestion/1.0.8/run-001/detection/provenance.json).
Relevant axioms are AX-04, AX-14, AX-15, AX-17 and AX-18: tools do not establish
meaning, semantic roles remain separate, uncertainty remains explicit, and no
missing architectural or domain authority is inferred. No authority conflict
requiring affected processing to stop was identified; explicitly deferred metamodel
questions remain questions.

Fresh detection ran the unchanged read-only Increment 1 comparator against the
Published Baseline and the justified preserved real no-edit 1.0.7 Archi transport
control, plus its identity/source-context audit. A separate run-scoped XML query
compared exact records, resolved Property keys/claims, canonical endpoint identities,
organisation path/reference multisets and visual membership/geometry/connections.
No source was matched by Name or inferred from folders or geometry. Commands,
tool versions, raw compressed diagnostics and the retained queries are linked
from [detection observations](../../docs/archimate/processing/ingestion/1.0.8/run-001/detection/observations.md).
This is a bounded operational procedure, not a reusable reconciliation engine.

## Detection and decomposition

| Finding | Result |
| --- | --- |
| Added elements | 9: one Capability recognised as proposed Feature, four Products, three BusinessServices, one BusinessProcess |
| Added semantic relationships | 13: six Composition, four Realization, two Flow, one Serving |
| Retained semantic architecture | No element/relationship removal, native Name/type mutation, established identity/naming mutation or retained semantic edit beyond characterised transport |
| Views | Two new, two modified; eight visual nodes, twenty connections and six bendpoints added; six retained nodes edited |
| Navigation | Three folders and twenty-four references added; no existing path/reference membership lost or moved |
| Existing metadata | All 1,299 canonical owners and 14 EC aliases retained; nine additions have no machine metadata |

The [decomposition record](../../docs/archimate/processing/ingestion/1.0.8/run-001/detection/decomposition.md)
maps all observations and resulting-state dependencies to stable CR-001–CR-036:

| CR category | Count | Review-local IDs |
| --- | ---: | --- |
| Element instances, including deterministically recognised proposed classification | 9 | CR-001–009 |
| Native Composition additions | 6 | CR-010–015 |
| Instance-specific CT3-framing/native-encoding questions | 6 | CR-016–021 |
| Native Realizations and separate logical Feature-context questions | 8 | CR-022–029 |
| Flow/Serving additions | 3 | CR-030–032 |
| Authored View proposals | 4 | CR-033–036 |

Each semantic edge can receive a disposition independently of its endpoints and
additional Harmonia interpretation. Five existing target Features already retain
source-qualified Person Identity Administration context with **Proposed placement**;
the six Composition interpretation CRs do not recreate or silently promote that
context. The Lineage Feature retains **Proposed architectural standing** and its
existing domain-authority boundary. The four native Realizations remain distinct
from logical Feature-context questions. No universal Composition or Realization
rule is submitted or established.

Dependencies name evidenced governed resulting states: reconciled endpoints,
reconciled exact relationships, and valid references/standing for presentations.
The graph is acyclic; no dependency logic was executed. Stronger View context
claims are explicit questions requiring a recorded scope/dependency revision
before authorisation, rather than assumptions about presentation geometry.

## Relationship reuse, Views and remaining questions

Five added Compositions have fully resolved canonical endpoints and no baseline
duplicate. No raw Exchange-triple or resolved-canonical-key duplicate is observed.
The other eight new edges have at least one newly authored endpoint without a
governed identity: final semantic duplicate status remains **UNRESOLVED**, rather
than being declared unique from differing Exchange identifiers. No attempted
duplicate against fully resolved governed endpoints was positively established.
Existing assertion discriminators/bindings remain preserved in the evidence.

V1 modifies Person Identity Administration's Feature Landscape, adding Identifier
Capture and six new-edge appearances with authored geometry/styles. V2 modifies
Entity Information Enablement's CT2 Landscape solely to expose **seven existing
governed Compositions**. CR-034 is presentation-only, with no duplicate semantic
creation CR. V3 adds Identifier Capture's realisation View; V4 adds Lineage's
realisation View. Exact node/connection IDs, native semantic and View-local
endpoints, relationship origins, geometry/styles, bendpoints and homes are
retained in [View evidence](../../docs/archimate/processing/ingestion/1.0.8/run-001/detection/views.json).

Questions include each new instance's bounded responsibility, standing and intended
identity binding; native relationship meaning/legality; the six instance CT3
interpretations; four logical Feature-context interpretations; Product scope;
actual Flow transfer/Serving consumption; and authored View purpose, standing
filter, layout/readability and home. No answers or likely dispositions were supplied.
The new Views have no Documentation; V1's unchanged governed-child wording needs
explicit scope/standing treatment. No GUI acceptance or re-layout was claimed.

## Transport, metadata and historical evidence

Known transport findings remain evidence-only: 61 Property-definition rekeys and
language losses, 1,669 evidence-Property whitespace changes, loss of 7,096 original
organisation item IDs, 3,910 materialised alpha defaults, 34 final-LF losses and
connection ordering in 15 Views. Raw comparator events remain separate from
architectural CR counts. The two positional organisation loss events do not
establish deletion; independent membership comparison demonstrates retention.
No whole-model lossless fidelity or paint-order equivalence is asserted.

All thirteen incoming relationship references are at `Relations` root. Later
authorised publication uses the governed source home; arbitrary folder replay
receives no semantic CR. Missing generated metadata receives no defect/population
CR. The **841 existing Name/displayName discrepancies** remain unchanged migration
debt, with no migration CR or repair in this Submission.

The three prior blind reports remain historical evidence only. Their hashes,
recorded working-state input hashes and differences from current 2B recognition
are retained in [historical-evidence.md](../../docs/archimate/processing/ingestion/1.0.8/run-001/detection/historical-evidence.md).
Fresh detection agrees with the third report's final empirical counts/hash, while
current classification, metadata, relationship-navigation and duplicate treatment
apply the subsequent governance. The exact second intermediate XML remains
unavailable; it was not reconstructed and is unnecessary for baseline-to-final
detection. No historical report was changed.

## Validation and preservation

[Review validation](../../docs/archimate/processing/ingestion/1.0.8/run-001/validation/review-validation.json)
checks template distinctions, first-visible summary, unique/stable IDs, the
80-character description limit, matching summary/details, On-Hold status, absent
adjudication, exact unauthorised/unprocessed records, valid required-state
dependencies, evidence links/anchors, exact XML locators, hashes, identity
continuity, relationship reuse and the write boundary. Offline schema/reference
checks pass; readiness NONCONFORMANT in the legacy diagnostic is explained by
the nine expected metadata-incomplete authoring records, not architectural rejection.

The required architecture suite **PASSed: 90 tests, zero failures/errors/skips**,
offline with JDK 21, bounded to 180 seconds; Maven elapsed 29.319 seconds. Release
validation PASSed at 1.0.11/1.0.10. Detection commands were bounded to 60 seconds;
review preparation to 30 seconds. There were no timeouts or stalls. Two initial
run-query harness errors were corrected without changing architecture or existing
tooling; causes and corrected outcomes are retained in
[command results](../../docs/archimate/processing/ingestion/1.0.8/run-001/validation/command-results.json).

The [preservation audit](../../docs/archimate/processing/ingestion/1.0.8/run-001/validation/preservation.json)
verifies **all 2,994 pre-existing protected files byte-identical**, including the
incoming XML, comparison baseline, all publication packages, RADS/source architecture,
2A/2B/2C governance, prior blind reports/bundles, migration findings and the
pre-existing working-tree edits/untracked files. Only this run and this report
were added to repository evidence. Maven refreshed ordinary ignored `target/`
build/test outputs; those transient outputs are outside the protected source and
evidence inventory. No unavoidable governed-artefact difference occurred.

Root release authority remains unchanged: preparation/generation is not acceptance
of an architectural change set. Any later accepted change set remains subject to
the governed patch increment. No Canonical ID, final governed name, semantic edge,
navigation repair, generated View, migration or Published Baseline was created.

**Stop boundary reached: detection and Change Review preparation only.** Every CR
has `On-Hold`, `Not authorised` and `Not yet processed`. Return the linked Change
Review to the Solution Architect; no reconciliation follows this report.
