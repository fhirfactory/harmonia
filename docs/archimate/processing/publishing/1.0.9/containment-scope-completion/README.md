<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# RAD-1.0.9 Containment selection and projection correction

The unsigned [RAD-1.0.9 candidate](../../../../exchange/publish/1.0.9/Harmonia-Architecture-1.0.9.xml)
contains the corrected generated Containment projection. The release remains
1.0.9, predecessor 1.0.8. Final Solution Architect rendering inspection and sign-off
remain pending. Information Architecture work has not commenced.

This separate completion record covers the Solution Architect's accepted scope
investigation and subsequent correction. The earlier
[presentation completion](../view-presentation-completion/README.md) and
[Home completion](../home-context-completion/README.md), their results, snapshots
and actual Archi exports remain unchanged at their original boundaries.

## Authority and implementation

The Solution Architect's task decision and the
[Containment contract](../../../../../markdown/governance/archimate-ingestion-publication-semantics.md#deterministic-containment-view)
govern this work. AX-04 keeps semantics authoritative over diagram machinery;
AX-14 distinguishes Composition, Home, responsibility, navigation and presentation;
AX-17 preserves historical assertions, architectural standing and unresolved debt.
The corrected approach preserves all three axioms. The previous requirement for
visual connections and a `context:` role conflicted with the accepted selection
rule and has been removed.

`view_presentation.py` reads each generated landscape's declared immediate-child
purpose and source Capability. It derives the complete child and relationship sets
from current governed native Composition records and explicitly accepted ingestion.
Native Capability endpoints include Harmonia Features. Relationship currentness is
checked independently: a preserved historical Composition between otherwise current
elements does not enter the current hierarchy. Proposed standing is not promoted.
Legacy roles, declared legacy membership, folder placement, ownership and existing
visual objects are not eligibility inputs.

Projection creates missing nodes and View-local references to the existing native
Composition relationships before applying the established placement and routing
algorithm. Stable visual identities use the existing publication identity convention.
There is no Feature-specific layout and no new semantic element or relationship.
Accepted authored presentation takes precedence. Existing unexpected visual content
causes a conformance error; it cannot supply semantic architecture or silently narrow
the governed child population.

The RADS contract now records the accepted rule. Its current-authority hash in
`baseline/correspondence.json` is updated; the prior document and correspondence
are recoverable in this task's snapshots. No baseline semantic capture, accepted
ingestion capture, identity binding or release authority is rewritten. This is the
canonical-documentation assessment for this bounded work: one applicable RADS
presentation contract was clarified, with no architecture-domain expansion or
outstanding migration required by this correction.

## Census before mutation and resulting population

[The semantic census captured before mutation](semantic-census-before.json)
verified exactly 231 eligible generated Views, with no loss or membership change
among the previously selected 119. The additional population is exactly the
investigated 112. The publisher re-derives the semantic sets and checks both this
census and the [pinned selection](selection.json); population drift stops generation.

| Population | Eligible / validated | Changed from starting candidate |
| --- | ---: | ---: |
| Previously authorised generated Containment Views | 119 | 0 |
| Additional Business Capability hierarchy Views | 55 | 55 |
| Additional Business Enabling Capability hierarchy Views | 56 | 56 |
| Strategy Person Identity Feature landscape | 1 | 1 |
| Total generated Containment population | 231 | 112 |
| Business Behaviour Views | 61 | 0 |
| Protected authored presentations | 4 | 0 |
| Recorded historical candidates | 62 | 0 |
| More-than-20-child View identities | 3 | 0 |

All 111 additional Business hierarchy Views now have the same governed Containment
presentation. Projection adds 331 visual connection objects referencing existing
Composition relationships, and one visual node. Existing visual identities, sizes,
styles, names and View metadata remain unchanged. There are 225 one-column and six
two-column Containment Views; the two-column counts are 13, 14 and 16, each appearing
twice. The governed boundary supports 20 children and excludes more than 20. The task's
regression bullet saying “20-child” was interpreted consistently with its explicit
1–20-child scope and >20-child exclusion; tests establish both 20 and 21 boundaries.

The Strategy Person Identity Administration — Feature Landscape
`id-c796d1c0d4c158d08f8bd86308f2f0a0` now contains its source and all six accepted
direct Feature children, including Identifier Capture
`id-727a264026584f80bc3304ff8383dc92`, through existing Composition
`id-bff76286f8994d20aa1b8aef166acebd`. Its five pre-existing Feature nodes are retained.

All four protected authored Views retain exact XML:

- Business Person Identity Administration — Feature Landscape:
  `id-e2a326c7d36751af9076a32dabe0e9cd`, with all six Feature children.
- Entity Information Enablement — CT2 Landscape:
  `id-c8ec850977565a17869af357ee28d917`.
- Identifier Capture — Feature realisation:
  `id-216c4ab0757b45b3894e7e0c2594b9f9`.
- Identity Source & Correction Lineage — Feature realisation:
  `id-8fb2f8b1776f4ebf969e9b5b638e4ed3`.

All three 21-child Views retain exact XML:

- `id-2bf749a8feb75a2e9777e7bcb89ea548` — retained Health Service Operations
  Enablement Overview.
- `id-d4e0a5eea7ef588e8b0ad4dbee80ec88` — Strategy Health Service Operations
  Enablement landscape.
- `id-2fe4e484c5a85b59ae5ded504b4a2fee` — Business Health Service Operations
  Enablement landscape.

More-than-20-child presentation remains unresolved architectural presentation debt.
The uncertain diagnostic `id-ef8af9b152505720b37914080c957a1c` and all other
unselected View XML remain byte-identical. Nothing is split, omitted or laid out
with a new archetype to evade cardinality limits.

## Behaviour routing and preservation

All 61 generated Business Behaviour Views and all 136 of their connections are
processed from final node rectangles. The 29 known stale connections remain
separately identified. Full regeneration corrects those 29 routes across 19 Views;
107 unchanged connection records and 42 unchanged Views retain exact XML. Compared
with the already corrected starting candidate, recalculation produces no further
Business route changes. Reapplying the final presentation produces no XML changes.

[The change ledger](population-and-changes.json) distinguishes full regeneration
from the incremental correction. Full regeneration migrates 231 Containment Views
and reroutes the 29 Business connections. This task changes exactly 112 Views and
adds 331 visual references. Independently removing Views from the before/after model
establishes exact preservation of all 1,308 semantic elements, 1,077 native
relationships, identities, naming, Properties, 212 explicit Homes, responsibility
assertions and model-tree navigation. The View population remains 1,198.

[before.json](before.json) pins 647 pre-existing files. Eight changed files have
byte-recoverable gzip snapshots; 639 other protected files remain at their original
hashes. The starting candidate SHA-256 is
`b9c63aef051f5424b937c7b01951cabd38389c4352610a8331a0d40e449dbf24`.
The corrected candidate SHA-256 is
`0f53e81f90a1d62974747e262218612dd70956c3eb65ebf3b2eb2b19af6ec529`.

## Validation and transport

[verification.json](verification.json) records actual bounded commands, outcomes,
elapsed times and log hashes. The focused suite has 16 tests, including eligibility
without visual objects or `context:`, CT3/Feature participation, complete membership,
accepted ingestion without legacy currentness fields, semantic relationship reuse,
protected precedence, historical-edge exclusion, 20/21 cardinality and rejection of
architecture manufactured from Home, Realization, responsibility or presentation.
The executed suites all passed:

| Suite | Tests passed |
| --- | ---: |
| Full ArchiMate/framework suite | 206 |
| Focused presentation suite | 16 |
| Governance suite | 72 |
| Java architecture suite, JDK 21/offline Maven | 90 |

Publication, identity, RADS, unchanged release authority, semantic/visual
preservation, historical replay and serialization checks also passed.

The [actual Archi 5.10 transport proof](roundtrip/archi-transport-verification.json)
checks all 3,924 nodes and 1,804 visual connections, all semantic/native identities,
naming Properties, 212 Homes, accepted authored presentations and navigation paths.
Every corrected presentation survives the real no-edit import/export. The isolated
CLI used English `en`, folder export and enabled export validation. The exact
5.10.0.202608252016 toolchain manifest, native model, raw export, comparator and
executed CLI log are retained under `roundtrip/` and `validation/`.

The raw whole-model comparator remains **DIFFERENCES**, including the same 1,669
historical semantic-text findings as the established transport baseline. Other raw
transport findings remain visible; no comparator or equivalence rule was changed.
Whole-model lossless fidelity and human rendering acceptance are not asserted.
The serialization-only comparison is EQUIVALENT. Historical governance, Home and
all three earlier actual transport probes are replayed against their pinned
candidates without rewriting their evidence.

Two unsuccessful checks are retained separately: the initial focused run detected
the governance document's stale current-authority hash, corrected in correspondence;
the initial sandboxed Archi startup could not open the local display. The successful
local CLI retry used isolated temporary state. Neither required a semantic change.

[Independent reproduction](reproduction.json) verifies all 15 generated publication
outputs in separate processes, including the current report and inspection guide.
Generation does not increment the release or allocate semantic identities. Human
rendering inspection and final Solution Architect sign-off remain outstanding.
The task stops with the updated unsigned RAD-1.0.9 candidate.

Reproduce from the repository root with bounded commands:

```bash
timeout --signal=TERM --kill-after=5s 120s python3 -B docs/archimate/framework/tools/synchronise_baseline.py --check
timeout --signal=TERM --kill-after=5s 120s python3 -B docs/archimate/processing/publishing/1.0.9/containment-scope-completion/verify.py preservation
timeout --signal=TERM --kill-after=5s 90s python3 -B docs/archimate/processing/publishing/1.0.9/containment-scope-completion/verify.py transport
timeout --signal=TERM --kill-after=5s 120s python3 -B docs/archimate/processing/publishing/1.0.9/containment-scope-completion/verify.py history
timeout --signal=TERM --kill-after=5s 120s python3 -B docs/archimate/framework/tools/verify_baseline_outputs.py
```
