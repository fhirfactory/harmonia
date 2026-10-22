<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia Enterprise Architecture

Current operations: [EA tooling](tooling/README.md), [governance](governance/README.md), and [completed RAD-1.1.6 operational succession](design/register/1.1.6/hcrl-1.1.6-001.complete.md).

The Solution Architect's [prospective RAD-1.1.7 acceptance](design/register/1.1.7/hcrl-1.1.7-001.complete.md)
accepts the reconciled Architecture Continuum as the current canonical
architectural baseline and the completed RAD → Continuum transition. Existing
unresolved, pending, historical and superseded material retains its standing;
individual elements/assertions gain no different semantic approval.

Current release outputs: [architecture browser](continuum/documentation/published/1.1.7/continuum.html),
[element index](continuum/documentation/published/1.1.7/README.md),
[ArchiMate Exchange](continuum/documentation/published/1.1.7/Harmonia-Architecture.xml)
and [review/navigation manifest](continuum/MANIFEST.md).

## Historical RAD-1.1.5 review-package account

The following RAD-1.1.5 review-package account and links preserve their originating standing. Current publication is generated for the root `architecture-release.json` version under `continuum/documentation/published/<version>/`.

The migrated architecture is ready for **holistic Solution Architect / ARB review**.
Its architectural unit is one individually identified ArchiMate element. Existing
standing remains qualified; this submission does not assert architectural closure.

- [Browse the full architecture](continuum/documentation/published/1.1.5/continuum.html).
- [Continuum review manifest — by native layer/aspect and type](continuum/MANIFEST.md).
- [Canonical element index](continuum/documentation/published/1.1.5/README.md).
- [ArchiMate Exchange model](continuum/documentation/published/1.1.5/Harmonia-Architecture.xml).
- [PDF identity and standing catalogue](continuum/documentation/published/1.1.5/continuum-catalogue.pdf).
- [HCRL 1.1.5-002: incorporated result and review questions](design/review/1.1.5/hcrl-1.1.5-002.pending.md).
- [Representation contract](governance/continuum-representation.md), [incorporation Praxis](governance/continuum-incorporation-praxis.md), and [Archi workbench](design/archimate/README.md).
- [Formal Task Report](design/evidence/1.1.5/002/after/ai-agent-reports/20261021T100913Z-element-continuum-applied-migration.md).
- [Manifest follow-up Report](design/evidence/1.1.5/002/after/ai-agent-reports/20261021T102230Z-continuum-review-manifest.md).
- [Original corpus accounting](design/evidence/1.1.5/002/after/corpus-accounting.json) and [source succession](design/evidence/1.1.5/002/after/source-succession.json).

There are **1,559 canonical Markdown elements**: 1,394 preserved identities and
165 newly allocated identities. The model carries 1,137 native relationships and
462 separately qualified source predicates. All elements have source provenance.
The type catalogue defines paths for all 60 native types, including the 26 used
in this population. Feature specialises Capability; Topic specialises DataObject.

Read the [Architectural Axioms](governance/architectural-axioms.md) and governing
contracts before editing. Canonical files live under `continuum/<native path>/`
with UUID32 filenames. `continuum/MANIFEST.md` and
`continuum/documentation/published/` are generated review/navigation output.
The generated browser is self-contained and requires no network connection.
Its source links require the shared repository checkout. Mermaid source blocks
remain readable source; this publication does not execute external diagram code.

From the repository root:

```bash
make -C enterprise-architecture/tooling all
python3 enterprise-architecture/tooling/verify_continuum_reproducibility.py --report /tmp/reproducibility.json
```

Python dependencies are `lxml`, `jsonschema` and `markdown`. The optional PDF uses
XeLaTeX, DejaVu Sans, hyperref, xurl and seqsplit. It is a catalogue, not a duplicate
narrative architecture source. Existing LaTeX workspaces and past Archi releases
retain their historical roles. Unretired governance and explicitly unresolved
source material remain available; retired passages preserve their original text.
