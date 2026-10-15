<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia EA Capability Map — Version 0.2-draft

**Proposed Complete Architecture — Step 2.** The [PDF](Harmonia-EA-Capability-Map.pdf)
is the review publication. Start with the
[prioritised architectural decision report](../../../../.junie/reports/2026-10-15-harmonia-ea-capability-map-v0.2-draft-review.md).

This version preserves the accepted [v0.1](../README.md) semantic substrate and
constructs an explicit proposed architecture over it. It is not final v0.2, does
not adjudicate the governed RADS, and does not declare an architecture domain
complete. No governed Markdown or frozen v0.1 file is modified.

## Architectural result

- Seven proposed BEC CT1s, 28 CT2s and 102 CT3s provide complete placement of all
  existing responsibilities and 178 Features. The five historical views remain
  contextual navigation. Most non-entity responsibilities themselves become CT3s;
  additional tiers are not manufactured from their old contextual descriptions.
- Three proposed Enterprise CT1s contain the fourteen Canonical EC responsibilities
  and the new Proposed Information Authority Qualification CT2. Ten selected CT3s
  distinguish materially different reusable abilities beneath EC-04, EC-06, EC-07
  and EC-10. Other EC branches intentionally stop at CT2.
- All 719 v0.1 identities survive: 716 Canonical and three Candidate. There are
  143 Proposed additions: 68 BC/BEC Capabilities, fourteen Enterprise Capabilities,
  41 Features, thirteen Functions, six Services and one Process.
- All 862 elements have stable Reference IDs, LaTeX labels and structural IDs.
  The 190 established BC structural paths are retained. EC-01–EC-14 remain stable
  Reference IDs and become direct historical structural-ID aliases. Newly
  completed or changed structural assertions are explicitly Proposed.
- The 462 baseline relationships are retained unchanged, including 22 unresolved
  associations. There are 448 new Proposed relationships and 38 exactly captured
  established assurance contributions. Four explicit No Material Role constraints
  remain constraints. Enablement does not imply decomposition, implementation
  equivalence, runtime dependency, inherited edges or transfer of domain authority.
- All 942 v0.3 primary concepts and 277 captured behavioural occurrences have
  individual reconciliation decisions. Useful deltas are retained/refined; generic
  pattern replication, non-element placeholders, software-shaped BC groupings and
  the parallel Enterprise coexistence branch are not mechanically copied.

## Identity, semantic preservation and assertion status

`model.json` is the current machine-readable proposal for downstream derivation.
`architectural_status`, `placement_status`, `structural_id_status` and `name_status`
are separate. A Canonical element is never demoted because its ancestry is Proposed.
The three existing Candidate concerns remain Candidate despite receiving Proposed
placement and explicitly Proposed semantic elaboration.

For every baseline element, `canonical_name`, `sources`,
`governed_definition_latex`, `baseline_description`, `baseline_unresolved` and
`baseline_placement` preserve the semantic anchor. Broader established ownership
is recorded separately from a proposed narrower Feature frame. Functions,
Services and Processes remain directly Capability-owned where established;
realisation/exposure/progression form a typed graph.

The proposed BEC display rename **Health Information Exchange Enablement** makes
current capability names unique without rewriting the established name or meaning
of either the BC or BEC. Its name status remains Proposed, and the old name is
searchable. The RAD naming conflict is not claimed resolved by adjudication.

New local codes use stable category-qualified semantic tokens. The typed structural
grammar follows metamodel §8. Its application to EC hierarchy is an explicitly
Proposed extension. Allocation is frozen in `identity-lock.json`; composition
fails on changes to an existing allocation. A future relocation must retain every
old structural ID as a direct alias to the same stable identity. No used ID may
be reassigned. v0.3 working IDs are provenance locators, never identity aliases.

## Files

| File | Purpose |
| --- | --- |
| `Harmonia-EA-Capability-Map.tex`, `sections/` | Standalone generated LaTeX source; sans-serif book, metadata, definitions, overviews, scope tables, typed links and name index. |
| `model.json` | All current responsibilities, explicit placements, definitions, provenance and typed assertion-status graph. |
| `identity-lock.json` | Frozen current allocation and direct alias namespace. |
| `semantic-baseline.json`, `v0.1-fingerprints.json` | Immutable semantic snapshot and checksums protecting the independent parent publication. |
| `change-ledger.json` | Per-element additions, renames, reframing, ID allocations and new relationships. |
| `v0.3-reconciliation.json` | Every prior concept and behavioural occurrence with locator, disposition, target and semantic reason. |
| `derivation-provenance.json` | Reverse lookup from current Reference ID to v0.3 derivation decisions. |
| `corpus-register.json` | Fingerprinted current architectural corpus, with source standing retained. |
| `source-coverage.json` | The complete, unchanged v0.1 source-transcription coverage. |
| `catalogue/proposals.md` | Reviewable definitions, overviews and boundaries for every new proposal. |
| `catalogue/review-focus.md` | Prioritised decisions and preserved tensions, also printed in the book. |
| `validation.json` | Full model, source, identity, reference, index and PDF verification. |
| `architecture-tests.json` | Repository architecture-suite result and test counts. |
| `validator-probes.json` | Thirteen in-memory invalid-model probes, all rejected by the structural/status/identity guards. |

## Reproduction and checking

Requirements are the same as v0.1: Python 3 with `markdown-it-py`, XeLaTeX,
`latexmk`, `makeindex`, DejaVu Sans fonts and Poppler tools. The immutable parent
provides the proven Markdown transcriber and validator; no baseline regeneration
is performed. Generated `.tex` is committed and can be compiled directly.

```bash
cd docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft
make all
```

`make model-check` checks the model and generated sources without compiling a PDF.
`make pdf check` compiles and verifies the committed publication. PDF compilation
has a 300-second command limit; subsidiary validators have bounded execution.
The checker verifies preserved identities/definitions/relationships and source
fingerprints, complete typed ancestry, Feature framing, alias non-reuse, unique
current names, proposal provenance/status, cross references, index coverage and
clean LaTeX output. Source uncertainty and source-status contradictions are
represented; they are not suppressed to obtain PASS.

## Downstream use

Treat the current architecture as the working hypothesis for the Application
Component Map while retaining its Proposed assertions. Derive cohesive logical
responsibility centres from the model. Existing components provide evidence, not
a required target layout. Reuse is not centralisation; participation does not
distribute responsibility; information support grants no business authority.

The proposed publication remains outside RADS by task design. Any later acceptance
and incorporation into Domains02–05 requires explicit architectural reconciliation;
neither this book nor its generated model silently changes governed Markdown.
