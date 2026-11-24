<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia Capability Architecture — Exchange publication

## Current synchronised baseline

Open [synchronised-baseline/Harmonia-Architecture-Baseline.xml](synchronised-baseline/Harmonia-Architecture-Baseline.xml)
in **Archi 5.10.0**, the supported human architecture workbench for Release 1.0.6,
using Open Exchange import. Start with the
[inspection guide](synchronised-baseline/archi-inspection-guide.md) and
[canonical Markdown baseline](../../markdown/governance/baseline/README.md).
`make all` generates and validates this baseline and its textual projection.
The native resource categories, numbered View domains and independent projections follow the
[current convention](../../markdown/governance/archi-navigation-and-view-convention.md#synchronised-baseline-convention--2026-10-17).

Capability contexts provide immediate-child landscape Views. Business resources,
Feature-specific Views and deterministic Business layouts share the same governed
Feature association index. Shared behaviour retains its single Capability resource
home when no preferred Feature home is established; diagrams retain all recorded
associations through separate appearances of the same semantic element. See the
completion report for the explicit shared and unresolved cases.

The complete generated set is **14 files**: 12 core outputs plus the report and
inspection guide. `make baseline-independent` regenerates and compares all 14
byte-for-byte in independent processes. `make all` includes that check. Historical
stage reports and preservation proofs are audit evidence, not generated outputs.

The `recovery-v0.2` model and report are immutable historical recovery evidence.
`make recovery-check` replays the pinned snapshot with its explicit 1.0.5 release
fixture, independently of the current release. Its earlier report's repository-wide
fingerprints describe the historical checkout; rerunning that report against later
live sources is not a current-baseline gate. `legacy-all` remains the earlier
publication workflow and must be replayed with its historical release fixtures.

## Authority and collaboration lifecycle

**Markdown RAD — author and govern.** The canonical [Markdown RAD](../../markdown/)
is the governed Harmonia source repository for architecture, design, motivation,
implementation, decisions, governance, traceability and rationale.
**ArchiMate — visualise, navigate, review and propose change.** The model is a
governed architectural projection and human architecture workbench. Open Exchange
XML is its interchange representation.
**LaTeX RAD — render and publish.** LaTeX is a downstream documentation/report
publication.

The normal ArchiMate collaboration lifecycle is:

1. Generate an ArchiMate Open Exchange representation from the governed Markdown
   RAD and associated governed projection metadata.
2. Import it into Archi for navigation, review and architectural editing.
3. Export the reviewed/edited model from Archi.
4. Analyse that export as architecture change input.
5. Reconcile accepted architectural changes into the appropriate Markdown RAD
   sources and accepted presentation changes into governed projection metadata.
6. Validate the resulting architecture.
7. Increment the architecture release for the accepted change set.
8. Generate the next ArchiMate representation from the updated governed repository.

An Archi export is change input, not a replacement architecture repository.
Archi-specific presentation state, such as accepted View layout, coordinates,
bendpoints or navigation information, may be retained as subordinate governed
projection metadata where it has no appropriate Markdown RAD representation.
Presentation metadata establishes no architectural identity or relationship.

The LaTeX publication direction is strictly:

```text
Markdown RAD -> LaTeX RAD -> publication output
```

LaTeX/PDF output is not an architectural authoring source and has no normal
reverse-reconciliation path. Historical recovery from earlier LaTeX material is a
bounded recovery/migration activity only.

The current corrections complete the already prepared, uncommitted **1.0.6**
candidate within the same accepted change set. They require no further increment.
Generation always reads the prepared release state and never increments it.
Resource navigation uses native category labels without numeric prefixes. View
navigation retains its accepted global numbering. Classified folders expose
`[CT1] -> [CT2] -> [CT3] -> [Feature]` wherever exact governed ancestry exists,
in both Strategy and Business context projections. Semantic Names and existing
View IDs remain unchanged. Final inspection corrections add deterministic
landscapes and improve Feature-centred Business presentation; empty context
folders assert no relationships.

The bundled Archi 5.9 Open Exchange XSDs remain the validation schema for this
format; using Archi 5.10.0 does not change the Exchange namespace or schema.

## Historical recovery workflow

------- Legacy Content - Superseded ------- Start ------
Superseded by: [current baseline workflow](#current-synchronised-baseline), [baseline authority](../../markdown/governance/baseline/README.md).
Only the active workflow/entry point is superseded; the source recovery and its historical results are preserved.
---------------------------------------------------------

**1.0.5A recovery for inspection:** import
[Harmonia-Architecture-1.0.5A.xml](recovery-v0.2/Harmonia-Architecture-1.0.5A.xml).
Read the [recovery report](recovery-v0.2/1.0.5A-recovery-report.md) and
[Archi inspection guide](recovery-v0.2/archi-inspection-guide.md).
From this directory, `PYTHONDONTWRITEBYTECODE=1 make all` now generates, tests
and checks that separate frozen-v0.2 recovery. It does not refresh RADS, recompose
LaTeX, alter identity registers or overwrite the original 1.0.4 XML below.
The original publication commands and reports remain historical; `legacy-all`
names their earlier pipeline and requires its corresponding release fixtures.
The recovery preserves its established navigation conventions while keeping
unbound old objects in explicit reference branches. No 1.0.5B overlay or commit
has been performed.


------- Legacy Content - Superseded ------- Finish ----

## Historical 1.0.4 publication

Release **1.0.4** applies the governed
[Archi Navigation & View Convention](../../markdown/governance/archi-navigation-and-view-convention.md).
Import [Harmonia-Capability-Architecture.xml](Harmonia-Capability-Architecture.xml)
using Archi's **Open Exchange XML** import. It preserves all 228 capabilities,
189 Composition relationships, existing semantic IDs, 14 EC identity bindings
and their Documentation. Only navigation and additional Views change.

Inspect these model-tree paths:

```text
Strategy / Business Capabilities / <CT1> / <CT2> / <CT3>
Strategy / Business Enabling Capabilities / <CT1> / <CT2> / <CT3>
Strategy / Enterprise Capabilities
Relations / Capabilities / <category> / <source capability ancestry>
Views / Capabilities / Harmonia Capability Landscape
Views / Capabilities / Business Capabilities / Business Capability Model
Views / Capabilities / Business Capabilities / <CT1> / <CT2> / <CT3>
Views / Capabilities / Business Enabling Capabilities / <same progressive pattern>
Views / Capabilities / Enterprise Capabilities / Enterprise Capability Model
```

Each BC/BEC folder contains its same-named semantic element and child folders.
Each corresponding View folder contains an Overview of that context and its
immediate children. Leaves have a single-element overview. The 214 new detail
Views contain at most 22 nodes each. All five original diagrams are retained
unchanged, giving **219 total Views**, with no replacements. The BC/BEC category
overviews still show CT1 + CT2 + CT3. EC remains an independent flat catalogue.
Folder ancestry creates no architectural relationship.

The [current release report](navigation-release-report.md) lists exact inspection
examples, validation results and the pending human review. Historical presentation
instructions below are preserved explicitly; the governed convention supersedes
those sections only.

------- Legacy Content - Superseded ------- Start ------
Superseded by: [Harmonia Archi Navigation & View Convention](../../markdown/governance/archi-navigation-and-view-convention.md#canonical-model-tree-homes)
---------------------------------------------------------

Import [Harmonia-Capability-Architecture.xml](Harmonia-Capability-Architecture.xml)
into Archi using **Open Exchange XML** import. The four generated Views are under:

```text
Views
└── Capability Architecture
    ├── Overview
    │   └── Harmonia Capability Landscape
    └── Capability Models
        ├── Business Capability Model
        ├── Business Enabling Capability Model
        └── Enterprise Capability Model
```

The baseline's existing **Archi Import Test - Individual Health Assessment** View
and its original navigation entry are retained separately. There are four new
Views and five total Views; the diagnostic is not a generated deliverable.

This publication reads the user-confirmed Archi 5.9 import baseline: **228
capabilities and 189 Composition relationships**. It preserves those semantics and the four generated Views, and adds governed Documentation only for the 14 exact Enterprise Reference-ID matches.
It does not read, compose, reconcile or modify the repository JSON/LaTeX model,
its identity lock or RADS. The Documentation overlay reads only the explicit governed Enterprise source in its manifest. The Exchange baseline is the explicitly authorised
input for this bounded publication task; it does not supersede RADS.

------- Legacy Content - Superseded ------- Finish ----

## Generate and validate

Requirements: Python 3.9+ and `lxml` (`requirements.txt`). From this directory:

```bash
PYTHONDONTWRITEBYTECODE=1 make all
```

`generate` runs `tools/publish_navigation.py`: replay the unchanged original
publishers and locks, then project checked navigation and detail Views.
`inventory` refreshes assessment evidence. `adoption` replays the historical
1.0.3 proof without rewriting it; `bindings` proves the 14 bindings against the
current semantic model. `test` runs all publication regression/rejection tests.
`check` performs read-only regeneration and identity/source checks. `report`
checks the current report against pinned verification evidence. Commands are
bounded and generation never increments the release.

For configurable publication from any working directory:

```bash
python3 tools/publish_navigation.py \
  --baseline baseline/Harmonia-Capability-Architecture-v2.1a.xml \
  --lock baseline/semantic-lock.json \
  --navigation-lock baseline/navigation-baseline-1.0.3.json \
  --schema schemas/archimate3_Diagram.xsd \
  --documentation-map documentation-map.json \
  --release-file ../../../architecture-release.json \
  --output Harmonia-Capability-Architecture.xml \
  --report validation.json
```

Add `--check` for read-only verification. Make accepts the existing input/output
variables shown below. Refresh the separate
[navigation release verification](navigation-release-verification.json) after
running affected required suites, then render with `tools/report_navigation.py`.
The renderer rejects stale hashes, failed or missing required checks. `make all`
does not run Maven or the root script suite; the release report records those
separate checks. Earlier release reports and adoption evidence remain historical.
The original lower-level publisher is preserved for historical replay; using it
alone does not produce the current navigation publication.

------- Legacy Content - Superseded ------- Start ------
Superseded by: [Harmonia Archi Navigation & View Convention](../../markdown/governance/archi-navigation-and-view-convention.md#determinism-and-verification)
---------------------------------------------------------

Requirements: Python 3.9+ and `lxml` (`requirements.txt`). From this directory:

```bash
make all
```

`generate` validates before writing the XML and `validation.json`. `inventory`
refreshes the source assessment without generating semantics. `test` runs
the regression/rejection tests. `check` validates the published XML, compares it
byte for byte with fresh generation, and checks the saved validation report
without writing files. Commands have bounded execution times.

`adoption` refreshes the bounded Release 1.0.3 identity-chain verification;
`check` also checks that evidence, the identity inventory and the common register.
These release gates need the repository's governed RAD sources and scripts.
They prove the 14 retained EC bindings against frozen Release 1.0.2 evidence,
including unchanged XML bytes, semantic/visual IDs, relationships, Documentation,
source provenance and unresolved findings. They do not allocate identities.

`report` checks the current [identity release report](../../markdown/governance/architecture-identity/release-report.md)
against the root authority and its pinned verification evidence. The earlier
`documentation-publication-report.md` / `release-verification.json` and their
renderer are retained as the historical 1.0.1 change report. They are not regenerated
under a later release number. Refresh current release evidence after affected
checks before claiming a new PASS. `make all` is publication validation; the complete release
also requires the root script tests, RAD validator and AGENTS.md ArchUnit command
recorded in the report. Reports do not substitute for those checks.

The command-line tool works from any working directory and has explicit,
configurable input, lock, schema, output and report paths:

```bash
python3 tools/publish_documentation.py \
  --baseline baseline/Harmonia-Capability-Architecture-v2.1a.xml \
  --lock baseline/semantic-lock.json \
  --schema schemas/archimate3_Diagram.xsd \
  --documentation-map documentation-map.json \
  --release-file ../../../architecture-release.json \
  --output Harmonia-Capability-Architecture.xml \
  --report validation.json
```

Add `--check` for read-only verification. Make also accepts `BASELINE`, `SCHEMA`,
`OUTPUT`, `REPORT`, `DOCUMENTATION_MAP` and `RELEASE_FILE` overrides. Inputs cannot be overwritten by outputs.
Ordinary generation never creates or updates a semantic lock. A changed semantic
baseline requires a separately authorised task; the current lock rejects it.

------- Legacy Content - Superseded ------- Finish ----

## Input and schema provenance

`baseline/Harmonia-Capability-Architecture-v2.1a.xml` is a byte-identical copy of
the confirmed externally produced `Harmonia-Capability-Architecture-v2.1a-archi-import-test.xml`.
Its original SHA-256 and per-record semantic hashes are in `baseline/semantic-lock.json`.
No Downloads path or installed Archi path is required to publish or test.

`schemas/` contains the five unmodified XSDs shipped by Archi **5.9.0.202604140726**,
the plugin licence and a provenance/hash manifest. Archi's representation uses the
Exchange **3.0 namespace and 3.1 diagram XSD**, matching the proven import. The
validator resolves the W3C XML namespace schema locally; network lookup is disabled.

## Presentation rules

Semantic names/properties are inherited through `elementRef`; nodes have no label
overrides. Visual IDs use UUIDv5 in a separate publication namespace, keyed by
model ID, stable View key and referenced semantic IDs. Connections include their
View-local node IDs in the identity key and reference the original relationship.
Neither coordinates nor labels allocate identities.

The retained landscape and overview layouts are unchanged. New detail diagrams
show a highlighted context above a four-column grid of immediate children, with
12-point inherited labels and only existing direct Composition edges. Drill down
through the matching folders under Views. See the governed convention and
[current validation](validation.json); visual acceptance remains pending in Archi.

------- Legacy Content - Superseded ------- Start ------
Superseded by: [Harmonia Archi Navigation & View Convention](../../markdown/governance/archi-navigation-and-view-convention.md#progressive-views)
---------------------------------------------------------

The landscape uses four-column grids in Business, Business Enabling and Enterprise
bands, top to bottom. Detailed hierarchies run left to right, with alphabetically
ordered siblings stacked vertically and each parent centred against its subtree.
Nodes remain 300 units wide, at least 84 high, with 12-point Sans labels; conservative
line estimates can increase height. Orthogonal bends occupy the column gaps.
Views without Composition edges use wrapped grids.

The five natural-view groupings remain presentation concepts. This input has no
membership assignments for them, so the BEC View follows its seven existing CT1
branches without inferring new group membership. The Enterprise input has fourteen
independent capabilities and no Composition edges; no hierarchy is manufactured.

See [the generation report](view-generation-report.md) and
[machine-readable validation](validation.json) for counts, checks and unresolved
presentation/source-governance issues. Visual inspection in Archi is the next
review gate; CT1 drill-downs, Features and other architecture Views are out of scope.

------- Legacy Content - Superseded ------- Finish ----

## Exact-ID Documentation projection

`documentation-map.json` is the explicit 14-entry reviewed crosswalk. It binds
existing XML IDs to existing EC Reference IDs, governed source headings and full
section hashes. The source path is relative to the manifest and configurable;
there is no Downloads dependency. The default source is the current RAD Enterprise
catalogue. A standalone copy needs that source file and an explicit manifest path.

The publisher rejects missing/duplicate Reference IDs, model/layer/label/source
drift and missing descriptions. It adds full verbatim section bodies only to
EC-01–EC-14, retaining responsibility exclusions and qualifications. The other
214 capabilities remain undocumented pending exact identity adjudication. It does
not infer matches from labels or use the Business JSON capture to generate XML.

The original baseline fixture and semantic lock are immutable. The overlay
validates the baseline against that lock, verifies each exact documentation
addition, then removes only those additions in memory and validates the original
lock and all existing View/reference constraints again. No lock file is replaced.
An ephemeral expected-content snapshot verifies the authorised augmented output.
`generate_views.py` remains the lower-level original View-only projector.

Documentation is plain text containing the source Markdown; rendering and relative
source links need visual review in Archi. See the RAD
[mapping specification](../../markdown/governance/rad-archimate-model-mapping.md),
[catalogue](../../markdown/governance/rad-archimate-mapping-catalogue.md),
[coverage assessment](../../markdown/governance/rad-archimate-coverage-assessment.md)
and [incremental plan](../../markdown/governance/rad-archimate-publication-plan.md).

The root [release authority](../../../architecture-release.json) is read by publication,
assessment and report tooling through `scripts/architecture_release.py`. See the
[release policy](../../markdown/governance/architecture-release-policy.md). Generation
never bumps the release or changes Maven. A portable copy needs the shared script
in the same repository layout, an explicit `--release-file`, and its governed source.
The [Documentation release report](documentation-publication-report.md) records
the 1.0.1 enrichment. The owner has confirmed its Archi visual verification.
The [identity model](../../markdown/governance/architecture-identity/model.md) and
[1.0.3 adoption decision](../../markdown/governance/architecture-identity/adoption-1.0.3.md)
continue to govern the same 14 EC bindings. Current
[binding validation](../../markdown/governance/architecture-identity/bindings-validation.json)
proves their continuity independently of presentation changes. No additional
semantic publication or identity allocation is authorised by this release.

------- Legacy Content - Superseded ------- Start ------
Superseded by: [Harmonia Archi Navigation & View Convention](../../markdown/governance/archi-navigation-and-view-convention.md#determinism-and-verification)
---------------------------------------------------------

The [identity model](../../markdown/governance/architecture-identity/model.md) and
[Release 1.0.3 adoption decision](../../markdown/governance/architecture-identity/adoption-1.0.3.md)
govern the first 14 EC register bindings. Their native identities and existing
semantic UUIDs are retained. Publication bytes and properties remain unchanged;
the adoption proof is in the register and verification evidence. Stop for human
review after these adoptions. No further semantic slice is authorised.
------- Legacy Content - Superseded ------- Finish ----
