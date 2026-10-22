<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Architecture Continuum RAD

The human-readable narrative publication of the canonical Architecture Continuum.
Read the **1.1.8 [PDF](published/1.1.8/continuum-rad.pdf)** or
[LaTeX source](published/1.1.8/continuum-rad.tex).
The [correspondence record](published/1.1.8/structure.json) accounts for every
element, its source hash, preserved standing and publication placement.
Domain Part source files are generated under `published/<release>/parts/`.

Parts follow populated native model domains: Motivation, Strategy, Business and
Application. Chapters and nested headings reuse existing ArchiMate organisations,
including Business Capabilities, Business Enabling Capabilities, Enterprise
Capabilities and InformationMap. The hierarchy comes from the complete governed
Exchange projection, including its existing `Continuum additions` organisation.
That existing branch is filtered into the applicable native domain Parts without
inventing chapter/section labels or rearranging its internal order or nesting.
Every architectural heading identifies its exact model node in `structure.json`.
No fallback chapters are created. Empty Technology/Implementation folders supply
no architecture to describe and acquire no invented content. Historical/superseded
records remain qualified in their existing model branches. Eight section levels
preserve the deepest model paths; the PDF outline exposes the full hierarchy.

The publication adapts the existing LaTeX RAD v0.2 typesetting template: KOMA
report layout, fonts, colour palette, tables, nested headings, PDF bookmarks and
architectural-name index. The old title, taxonomy, authority claims and semantic
extracts are replaced. The original LaTeX workspace is preserved; generation
reads no old RAD/model copy or historical processing workspace.

The [reader notes](reader-notes.json) contain AI-assisted explanatory prose with
explicit canonical source identities. They guide reading without establishing
architecture. Definitions and boundaries are rendered from canonical Markdown;
duplicate source-qualified contributions remain accessible in the canonical
record and Manifest. Every current/pending entry links to that record. Both
human-readable standing and exact machine-readable qualifications remain visible.

Small TikZ diagrams select existing active native Composition assertions using
the existing relationship-state selector. Arrow direction, relationship
currentness, source standing and complete edge records remain traceable. Selection
and layout are presentation decisions; no edge is inferred, allocated or saved
into the model. No manually authored Archi View is required. Generation uses no
live AI service; explanatory text and presentation code are retained inputs.

```bash
make -C enterprise-architecture/tooling continuum-rad
make -C enterprise-architecture/tooling rad-pdf
make -C enterprise-architecture/tooling check
```

Use the declared Python dependencies, XeLaTeX, latexmk, makeindex, DejaVu fonts
and the standard LaTeX packages in the retained template (including TikZ).
`rad-pdf` uses fixed `SOURCE_DATE_EPOCH=1792627200`; override explicitly if needed.
Compilation intermediates go to the ignored release-local `build/` directory.
PDF compilation/inspection remains distinct from deterministic source validation
and Solution Architect confirmation.
