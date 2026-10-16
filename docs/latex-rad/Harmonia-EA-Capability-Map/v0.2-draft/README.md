<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia EA Capability Map — v0.2-draft

This publication incorporates the **16 October 2026 human-reviewed candidate BEC
hierarchy**. Start with the [PDF](Harmonia-EA-Capability-Map.pdf) and the
[complete reconciliation report](../../../../.junie/reports/2026-10-16-bec-human-adjudicated-reconciliation.md).
The reviewed candidate and its structural relationships remain Proposed; this
publication does not amend RADS or declare a domain complete.

## Reconciliation result

- The current BEC tree contains **7 CT1, 76 CT2 and 95 CT3** responsibilities.
  The exact reviewed input has 162 nodes; six retained administrative descriptions
  and ten relocated service-context responsibilities supply the sixteen additional
  meaningful CT3 frames. No CT3 is manufactured for dependency service contexts,
  undecomposed informatics purposes or deliberately empty scoped branches.
- All **862 pre-review identities** and **719 v0.1 elements** survive, with stable
  Reference IDs and LaTeX labels. Canonical definitions, sources and semantic
  standing remain preserved. The 416 structural-ID changes retain previous IDs as
  direct aliases; there are 432 unique historical aliases in the reserved namespace.
- The model contains 946 records: 845 current elements, 32 superseded records and
  69 records explicitly pending human adjudication. History and pending semantics
  are published outside the current BEC tree. Every current Feature has a meaningful
  CT3 ancestor; 21 original Features retain unresolved frames rather than acquiring
  a false semantic parent.
- All eleven explicit **Not Addressed in R1.x / R2.x** annotations are visible and
  inherited through their stated branches. They are release scope, independent of
  Canonical / Proposed / Candidate status.
- Client Privacy Guardrail Management retains Canonical consent responsibility
  and FEAT-EM-09. Health Information Control references/respects that responsibility
  without duplicating consent ownership.
- Genuine conflicts are marked **HUMAN ADJUDICATION REQUIRED**. These include
  Canonical subject assurance versus assurance-service review; physical badges
  versus clinical service credentials; technical boundary communication versus
  patient communication; internal execution versus the external work bridge;
  unallocated compatible Stage-2 responsibilities; general security Feature
  framing; and mixed specimen/materiel Canonical wording. The existing unique
  BEC display name **Health Information Exchange Enablement** is retained while
  recording the reviewed wording and the unresolved CM-R05 collision with the BC.
- All 228 BC/EC complete element records are unchanged. Their architecture was not
  independently redesigned. Original relationship assertions and statuses survive;
  currentness and explicit Proposed reference repairs are recorded separately.

## Publication and verification

The **1,531-page PDF** has 1,007 outline entries, 946 indexed records and 4,101
numbered technical catalogue fields. All 2,681 previous labels survive; the source
contains 2,768 unique labels. Validation found zero undefined references, duplicate
labels, missing glyphs or overfull boxes. The required 90 ArchUnit tests, 40 existing
RADS validator tests, 17 reviewed-decision tests and 13 invalid-model probes pass.
Passing validation retains the explicitly unresolved architectural semantics.

The generated master and thirty included TeX files are first-class editable source.
Business Capability, Business Enabling Capability and Enterprise Capability remain
separate Parts; CT1 / CT2 / CT3 use chapter / section / subsection. Features use
subsubsection; existing technical numbering remains enabled. Publication numbering
is navigation, never architectural identity.

### Compile the existing editable LaTeX

Open **Harmonia-EA-Capability-Map.tex** as the master in Kile or another conventional
LaTeX editor, or run from this directory:

```bash
latexmk -pdf -interaction=nonstopmode -halt-on-error Harmonia-EA-Capability-Map.tex
```

Normal PDFLaTeX also works; latexmk runs makeindex and the necessary reference and
contents passes. Requirements are a conventional TeX installation with pdfLaTeX,
latexmk, makeindex, KOMA-Script, the DejaVu TeX fonts and the packages named in the
master. TeX Live recommended/extra/extra-fonts collections supply these dependencies.

**No Python, model JSON, Markdown, Maven, Makefile, generator or repository parent
is required to compile the generated source.** A clean external copy containing
only the 32 source/config files compiled and produced identical rendered text.
`.latexmkrc` has no generation hook. Subsidiary files retain conventional editor
root hints and the Kile project is preserved. The Kile GUI was not driven.

Direct edits to `sections/*.tex` compile normally. Optional later regeneration
overwrites generated TeX. XeLaTeX support is retained in the template; it additionally
requires installed DejaVu Sans / DejaVu Sans Mono OpenType fonts and was not rerun
for this reconciliation.

```bash
latexmk -c Harmonia-EA-Capability-Map.tex
```

This removes auxiliary files while retaining the PDF and sources. Use `-C` to remove
the PDF too. Clean before switching engines.

### Optional repository regeneration and validation

```bash
make compose render
make model-check semantic-tests
make pdf check
```

Generation requires Python 3, `markdown-it-py`, the governed evidence, the frozen
v0.1 transcriber and the explicit reviewed inputs. PDF validation additionally
requires Poppler and MuPDF `mutool`. `make pdf` uses the conventional source project
with outputs under `build/`; it has a 300-second bound. The full optional `make all`
performs composition, rendering, compilation and validation. After verified results
are available, `python3 tools/report.py` regenerates the required task report.

Do not reallocate identities by ordinary composition. The explicit migration switch
is guarded to retain existing keys, Reference IDs, labels, local tokens and aliases;
it is reserved for reviewed identity maintenance.

## Model, history and evidence

| Artefact | Role |
| --- | --- |
| `reviewed-bec-hierarchy.json` | Exact bounded human-reviewed hierarchy and explicit release-scope input. |
| `tools/reviewed_bec.py` | Explicit identity-preserving semantic migration over reproducible original Stage-2 composition. |
| `model.json` | Current and preserved records, typed relationships, separate assertion statuses and relationship currentness. |
| `bec-reconciliation-ledger.json` | All 137 predecessor dispositions, new reviewed allocations, predecessor distributions, relocations, Feature changes, structural-ID changes and conflicts. |
| `identity-lock.json`, `pre-review-identity-lock.json`, `reviewed-identity-history.json` | Current allocations and reserved pre-review / reconciliation history. |
| `pre-review-publication-labels.json` | All 2,681 original publication labels protected by validation. |
| `semantic-baseline.json`, `v0.1-fingerprints.json`, `source-coverage.json` | Unchanged Canonical semantic and source evidence. |
| `v0.3-reconciliation.json`, `pre-review-v0.3-reconciliation.json`, `derivation-provenance.json` | Current decisions, exact previous decisions and reverse provenance; candidate working IDs are not aliases. |
| `pre-review-publication-evidence.json` | Verbatim original verification reports and README, retained as historical evidence. |
| `catalogue/proposals.md` | Current/protected Proposed definitions with explicit reconciliation standing. |
| `catalogue/review-focus.md` | Original Stage-2 review focus, retained as history; it does not override the human-reviewed input. |
| `validation.json`, `model-validation.json`, `reconciliation-tests.json`, `validator-probes.json`, `architecture-tests.json` | Current model, semantic, rejection-probe and architecture results. |
| `standalone-validation.json`, `publication-preservation.json` | Source-only portability, identity preservation and BC/EC preservation evidence. |

See the [standalone report](standalone-publication-report.md),
[navigation report](publication-navigation-report.md), and the complete
[human reconciliation report](../../../../.junie/reports/2026-10-16-bec-human-adjudicated-reconciliation.md).
All genuine remaining conflicts require human judgement; current structural
assertions do not silently promote their semantic standing.
