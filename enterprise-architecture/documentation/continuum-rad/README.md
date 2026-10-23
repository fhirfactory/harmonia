<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Architecture Continuum RAD

The human-readable narrative publication of the canonical Architecture Continuum.
Read the **1.1.10 [PDF](published/1.1.10/continuum-rad.pdf)** or
[LaTeX source](published/1.1.10/continuum-rad.tex).
The [correspondence record](published/1.1.10/structure.json) accounts for every
source, its hash, full navigation coordinate and preserved standing.
The [completed 1.1.10 HCRL](../../design/register/1.1.10/hcrl-1.1.10-001.complete.md)
records the Solution Architect’s acceptance, preserved deferrals and documented limits.

Front matter leads to Architecture Governance, Parts I–VI (Motivation, Strategy,
Business, Application, Technology, Implementation & Migration), and the
Architectural Name Index. The ordered canonical
[navigation tree](../../continuum/navigation.json) supplies every architectural
heading and coordinate. Native-type file directories and workbench/history
folders supply no reading taxonomy. Governance uses G; architectural Parts use
I–VI. Numbering continues to every published descendant, independently of identity.
The complete PDF outline exposes all levels; printed contents select higher
levels to keep the document readable. Name-index entries include their coordinates.

Strategy preserves the governed capability decomposition. Business behaviour uses
references to populated capability contexts. Stakeholders are cross-cutting
Motivation profiles, without a peer Stakeholders chapter. Application separates
logical Topics from scoped information structures and follows existing application
responsibilities. Technology and Implementation & Migration have no individually
represented elements: their Part roots state absence without speculative children.
Unclassified branches retain the specific contextual uncertainties described in
the HCRL and execution evidence. Neither navigation nor publication changes
architectural standing or establishes containment.

Canonical definitions and boundaries are rendered from Markdown. Historical
entries remain qualified in their architectural contexts, with direct canonical
links. Repeated source contributions and full metadata remain in the canonical
record and Manifest. Existing [reader notes](reader-notes.json) are source-cited
explanatory material. Small diagrams select existing native Composition assertions
with direction, standing and complete edge correspondence. No diagram creates a
semantic edge. Publication uses retained deterministic presentation inputs and
no live AI service.

```bash
make -C enterprise-architecture/tooling continuum-rad
make -C enterprise-architecture/tooling rad-pdf
make -C enterprise-architecture/tooling check
```

Use the declared Python dependencies, XeLaTeX, latexmk, makeindex, DejaVu fonts
and standard LaTeX packages including TikZ. `rad-pdf` uses fixed
`SOURCE_DATE_EPOCH=1792627200`; override explicitly if needed. Intermediates remain
in ignored release-local `build/`. Generation reads no Legacy-RAD, old LaTeX model,
historical processor or interaction log. Historical publications remain unchanged.
PDF compilation/inspection, reproducibility and human acceptance are distinct.
