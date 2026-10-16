<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Publication navigation and numbering

The generator now gives Business Capability Architecture, Business Enabling
Capability Architecture and Enterprise Capability Architecture distinct LaTeX
Parts. The guide, registers/provenance and governed source detail occupy separate
supporting Parts. CT1/CT2/CT3 map to chapter/section/subsection; Features and
behaviours follow their existing ownership, retaining typed headings.

All 862 architectural entries and 3,277 catalogue headings are numbered, with
paragraph/subparagraph numbering enabled. For example, Referral Administration
is `21.8.8`, its Capability Overview is `21.8.8.1`, and Referral Document Ingestion's
Feature Overview is `21.8.8.7.1`. These are publication locations; architectural
identifiers and labels remain unchanged. The contents and PDF outline expose all
862 elements under their actual ancestry. Numbered catalogue fields stay outside
the outline, which contains 916 entries.

Validation: **PASS**. Complete regeneration produces the
[1,224-page PDF](Harmonia-EA-Capability-Map.pdf). The model, identities, statuses,
all 948 relationships, definitions, explanatory prose and provenance are unchanged;
all 2,681 LaTeX labels are preserved. Before/after fingerprints and normalized
publication-text comparisons are recorded in
[publication-preservation.json](publication-preservation.json).

Semantic-baseline, source-fingerprint, regeneration and navigation checks pass;
all 90 repository architecture tests pass. The final LaTeX build has no warnings,
duplicate labels, undefined references, missing glyphs or overfull boxes.
Representative contents, Part, capability, Feature and behaviour pages were
visually inspected with no material layout regressions. Detailed results are in
[validation.json](validation.json) and [architecture-tests.json](architecture-tests.json).

Changes reside in the renderer and publication template. Version-path adapters
restore regeneration after the earlier v0.1 directory relocation while preserving
every frozen v0.1 file and fingerprint. No architectural remediation was performed.
