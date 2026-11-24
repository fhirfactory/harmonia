<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Four-View Exchange publication report — 16 October 2026

Produced [Harmonia-Capability-Architecture.xml](Harmonia-Capability-Architecture.xml)
from the explicitly confirmed Archi 5.9 import baseline. All **228 Capability
elements and 189 Composition relationships** retain their original IDs, labels,
endpoints, hierarchy, properties and architectural-status text. No semantic
elements or relationships were added. Existing model metadata, semantic
organisations and the original diagnostic View/navigation are preserved.

AX-17 and the Domain03 traceability rule require preservation of established
relationships and unestablished ancestry. This tool copies the authorised input
and projects only existing Composition edges. It makes no architecture decision,
changes no RADS content and does not promote the baseline's standing beyond this
publication task.

## Files added

All additions are under `docs/archimate/capability-architecture/`. No pre-existing
repository files were changed by this task.

| Files | Purpose |
| --- | --- |
| `tools/generate_views.py` | Configurable XML-in/XML-out generator and preservation/reference/schema/layout checks. |
| `tests/test_views.py` | 28 publication regression and rejection tests. |
| `baseline/Harmonia-Capability-Architecture-v2.1a.xml`, `baseline/semantic-lock.json` | Exact confirmed input fixture, source checksum, protected-content and per-record identity/content hashes; never rewritten by publication. |
| `schemas/archimate3_Model.xsd`, `schemas/archimate3_View.xsd`, `schemas/archimate3_Diagram.xsd`, `schemas/xml.xsd`, `schemas/dc.xsd` | Unmodified XSDs from Archi 5.9's Exchange plugin. |
| `schemas/LICENSE.txt`, `schemas/provenance.json` | Original plugin licence and source/schema hashes. |
| `Harmonia-Capability-Architecture.xml`, `validation.json` | Generated publication and deterministic validation evidence. |
| `Makefile`, `requirements.txt`, `.gitignore`, `README.md`, `view-generation-report.md` | Bounded generation/check/test entry points, dependency, Python cache exclusion, usage and this report. |

The repository JSON/LaTeX publication and its existing identity-lock mechanism
remain untouched. The baseline fixture was copied byte for byte from the confirmed
external file; generation and tests subsequently ran using repository inputs only.

## Views, IDs and layout

| Generated View | Nodes | Connections | Canvas extent |
| --- | ---: | ---: | --- |
| Harmonia Capability Landscape | 39 | 0 | 1520 × 1396 |
| Business Capability Model | 52 | 34 | 1140 × 5120 |
| Business Enabling Capability Model | 162 | 155 | 1140 × 12540 |
| Enterprise Capability Model | 14 | 0 | 1520 × 500 |

The landscape contains the 18 Business, 7 Business Enabling and 14 Enterprise
roots in separate wrapped bands. The detailed Views include all capabilities in
their respective baseline layers, with no Features. Enterprise has no baseline
Composition hierarchy, so its fourteen capabilities remain an unconnected grid.
The four new Views live in nested `Views / Capability Architecture / Overview`
and `Capability Models` folders. The original five-node/four-connection import
probe remains at its original location: five total Views, four generated Views.

Visual identifiers are `id-` plus UUIDv5 hex. A fixed versioned publication
namespace and model identifier scope each key. View keys are stable; node keys
include View ID and semantic element ID; connection keys include View ID,
semantic relationship ID and visual source/target IDs. Folder and reference-item
IDs use separate keys. Labels, coordinates, traversal order and timestamps are
excluded. Repeated publication replaces only these generated visual branches.

Layout is algorithmic: four-column grids for the landscape/flat Enterprise model;
one column per existing hierarchy depth elsewhere, with contiguous vertical
sibling/subtree blocks and centred parents. Alphabetical label/ID ordering makes
layout independent of source record order. Nodes retain 300-unit width, minimum
84-unit height and 12-point Sans; height can grow for longer labels. Connections
use orthogonal bends in the 80-unit column gaps. Nothing shrinks to fit a canvas.

## Validation results

**PASS** — `make -C docs/archimate/capability-architecture all`: 28 tests, no
failures. Checks cover unchanged semantic IDs/content, global identifier
uniqueness, every `elementRef`/`relationshipRef`, View-local source/target nodes,
semantic endpoint agreement, complete View coverage, correct nested Views
organisation, original View/navigation preservation, node dimensions/overlap,
XML/schema validity, byte-identical repeated generation and idempotent republication.
Negative probes reject invalid references, endpoints, navigation, geometry and
semantic mutations. An isolated copy publishes/checks without Downloads or the
repository JSON. Tests also establish byte preservation of the complete elements,
relationships and property-definitions XML blocks.

Both input and serialized/reparsed output validate offline against Archi 5.9's
bundled Exchange XSDs. The committed [validation.json](validation.json) contains
the output SHA-256, protected-content hash and individual check results.

**PASS** — the required architecture command completed in 17.6 seconds:

```bash
timeout --signal=TERM --kill-after=10s 300s mvn test \
  -pl paradeigma/paradeigma-test -am \
  '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

Result: **90 tests, zero failures/errors/skips**. Run log:
`/tmp/harmonia-exchange-architecture-tests.log`.

## Unresolved issues and review boundary

- **Publication/source governance:** the proven Exchange baseline has 228
  capabilities/189 Composition edges and differs from the repository JSON
  publication. This discrepancy is preserved, not reconciled. The canonical
  semantic-generation pipeline requires a separate authorised review.
- **Natural-view membership:** the baseline supplies no membership for Entity
  Management, Service Administration, Service Delivery, Health Service Operations
  or Intrinsic / Shared Enablement. They remain presentation concepts; no
  capabilities, tiers, memberships or connections were inferred for them.
- **Presentation:** full hierarchical Views are deliberately tall, especially
  BEC (12,540 units), with long shared connector trunks and vertical scrolling.
  Landscape band headings are in View documentation rather than unreferenced
  diagram nodes. Label fit/font rendering and navigation need visual inspection
  in Archi 5.9; this run validates the Exchange representation and geometry, not
  the GUI rendering. No new GUI import or visual inspection is claimed.

Work stops at these four Views. CT1 drill-downs, Features, Functions, Application
Architecture and other visualisations remain outside this task.
