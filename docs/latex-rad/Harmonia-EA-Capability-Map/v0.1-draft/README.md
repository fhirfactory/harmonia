<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia-EA-Capability-Map — Version 0.1

This is the definitive LaTeX representation of the **current governed RAD
capability architecture**, at the source baseline recorded in `manifest.json`.
It explains what the RAD contains, including its uncertainty, through an authored
reference catalogue and exact governed semantic detail. It establishes no new
decomposition, capability, Feature, behavioural relationship or adjudication.
This authorised v0.1 completion allocates structural identifiers where sufficient
context is established. The Markdown RAD remains architectural authority.

## Authority and scope

The primary sources are:

- `docs/markdown/02-strategy/capabilities/business-capabilities.md`
- `docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md`
- `docs/markdown/02-strategy/capabilities/enterprise-capabilities.md`

The Business Architecture metamodel, capability-tier model, six Domain03
behaviour catalogues, principal Process catalogue, approved assurance Strategy
derivation, B2.1 support trace, dependency matrix and the supplied Domain03→04
traceability document supply applicable semantics and relationships. The K9
section of the current G1 review and selected relevant Architectural Axioms
preserve governance constraints. `manifest.json` enumerates **18 Markdown source
documents**, exact source ranges and SHA-256 fingerprints.

AX-01/04 preserve health/business meaning; AX-14 preserves semantic distinctions;
AX-17 prevents inferred architecture; AX-18 preserves domain authority. The full
metamodel and selected governance text are reproduced in the publication.

The existing RAD `reference-model/business-architecture.json` and its README
were inspected **as navigation aids**, retaining their explicitly candidate
capture standing. They located source assertions and existing internal record
keys. They confer no architectural authority: Markdown inventories, explicit
parent statements and source citations were checked independently. Candidate
summaries were not imported. Rendering depends only on the frozen publication
manifest and fingerprinted Markdown, not that reference-candidate JSON.

No legacy document was read to supply architecture. No earlier candidate LaTeX
document was inspected or imported, including v0.1–v0.4 in the excluded scratchpad.
Links already present in reproduced governed prose remain links; they do not make
their targets publication authority. Governed Markdown was not modified.

## Document structure

- `Harmonia-EA-Capability-Map.tex` — complete master, sans-serif typography,
  TOC, hyperlinks, page references, headers/footers and name index.
- `sections/reading-guide.tex` — authority, semantics, stable identity policy,
  reconciled counts and preserved issues.
- `sections/business-capabilities.tex` — actual BC CT1→CT2→CT3 hierarchy and
  separately identified candidate concerns.
- `sections/business-enabling-capabilities.tex` — documented BEC contextual
  views, established owners and their Features/Functions/Services/Processes.
  Heading depth here is navigation, **not a CT allocation**.
- `sections/enterprise-capabilities.tex` — the fourteen established EC entries,
  with complete governed entry prose and no invented decomposition.
- `sections/relationships.tex` — source-evidenced typed relationships, with
  unestablished associations visibly unresolved.
- `sections/catalogue-contexts.tex` — the 73 named enabling descriptions,
  distinguishing four established identities from 69 unresolved identities.
- `sections/identifiers.tex` — sorted Reference Id register linked to elements.
- `sections/rad-*.tex` — complete transcriptions of selected Markdown scope,
  preserving detailed definitions, boundaries, exclusions, notes and uncertainty.
- `manifest.json` — frozen element Reference IDs/labels, ownership/parent
  references, statuses, aliases, evidence and source fingerprints. This is a
  publication/identity manifest, **not a replacement architecture model**.
- `source-coverage.json` — line ranges and hashes of every transcribed source
  block; selected scope has no unrepresented nonblank source lines.
- `validation.json` — machine-readable checks and architecture counts.
- `catalogue/*.md` — reviewable authored overviews, intent and boundary prose,
  distinct from governed definitions and source transcription. These files are
  fingerprinted separately in the manifest; they are not new RAD authority.
- `identity-baseline.json` — frozen pre-enrichment identities, structural
  dimensions, relationships/source fingerprints and all 2,418 existing labels.
- `review-exceptions.json`, `sections/review-exceptions.tex` — complete
  element lists for structural-ID exceptions and material description limits.
- `tools/render.py`, `tools/check.py`, `Makefile` — bounded regeneration,
  compilation and verification. Generated `.tex` files are committed so the
  publication also compiles without Python.

Each of the 283 Capability records (including three Candidates) has a substantive
**Capability Overview**, separate from its governed definition. Intent, boundary
and relevance prose explain the actual responsibility where evidence supports it;
contained/owned-element tables provide navigable scope. All 137 Features have
specific authored overviews. Functions and Services retain their actual semantic
descriptions and source qualifications; significant behaviours and all nineteen
Processes also have authored overviews. Complete local Process descriptions are
available in their catalogue entries, rather than only in the source appendix.

Canonical is the status of an element, not a claim that every explanatory
sentence was already present in the RAD. Authored prose synthesises definitions,
actual ancestry/ownership, evidenced behaviour and metamodel boundaries for
review. It does not establish new relationships, clinical authority, classifications
or implementation allocation. The earlier candidate LaTeX remains excluded.

Rich source prose is linked directly from element metadata and preserved alongside
the explanation. Source equations wrap as equivalent textual
operator sequences. Source diagram notation remains textual; box/tree glyphs
are removed from source listings. No graphical containment pictographs are
introduced. Source presentation never establishes hierarchy.

## Stable identifier and label policy

There are **719 element records**: 716 Canonical identities/responsibilities and
three Candidate BC concerns. Preserve every existing `reference_id` and
`latex_label` value verbatim in later versions, including on rename or relocation.
Never regenerate identities by sorting, heading position, current parent or build.

Existing authoritative local Feature tokens (`FEAT-EM-01`, etc.) and EC identifiers
(`EC-01`–`EC-14`) are used unchanged as Reference IDs. The source explicitly says
BC catalogue ordinals are not allocated codes; BC-18-style shorthand is retained
in source prose without converting it into a structural Canonical ID or ID alias.

For the other 568 records, the initial publication allocation is deterministic:

```text
HCM-<kind>-<semantic-name-token>
kind = BC | BEC | FN | SV | PR
semantic-name-token = initial name lowercased, each non-[a-z0-9] run → "-",
                      leading/trailing "-" removed
```

These tokens depend on neither tier nor hierarchy. Initial allocation found no
collisions. Future additions require an explicit reviewed manifest entry; if a
token collides, choose and record a meaningful disambiguator, never an ordinal
or random suffix. The renderer **does not allocate or update identifiers**; it
validates the frozen allocations against their policy and actual governed ancestry.

Label prefixes are `cap:bc-`, `cap:bec-`, `cap:ec-`, `feature:`, `function:`,
`service:` and `process:`. Feature labels retain their lowercased full local token;
EC labels retain their lowercased identifier. Other initial labels use the same
semantic name token. No authoritative pre-existing LaTeX label was found in the
governed source scope. Exact manifest values take precedence over these initial
allocation descriptions. Internal record keys are navigation keys, not new
architectural Canonical IDs or aliases.

**Reference Id and structural Canonical ID are different fields.** The metamodel
requires a contextual structural ID to change when responsibility context changes,
retaining the old ID as an alias. A publication Reference Id and LaTeX label remain
stable.

The authorised v0.1 allocation establishes **190 BC structural IDs** under
metamodel §8. Initial approved local codes are the frozen `HCM-BC-` semantic
tokens uppercased, excluding that publication prefix. Components carry their
actual `CT1-`, `CT2-` or `CT3-` type and are joined with dots in the established
responsibility path. For example:

```text
CT1-HEALTH-RESEARCH-INNOVATION.CT2-HEALTH-INNOVATION.CT3-HEALTH-INNOVATION-EVALUATION
```

The publication Reference Id is an allocation seed only: it was not previously
a Canonical ID. Neither source display ordinals nor inferred ancestors are used.
Local codes are frozen on allocation and survive ordinary name changes; structural
relocation requires a new path and a retained direct ID alias under §8.4. Canonical
IDs and aliases occupy one unique, non-reusable namespace. The existing governed
**EC-01–EC-14** identifiers also populate the separate Canonical ID field; no CT
path applies to them. The 204 current IDs are unique and every BC ancestor prefix
resolves to its actual ancestor. Allocations are recorded in manifest metadata
and displayed in the identifier register.

**515 structural IDs remain `null`**: all 512 canonical BEC/Feature/behaviour
records require unresolved BEC tier ancestry, and three Candidate concerns lack
approved placement. All are named in `review-exceptions.json`. None is given an
incomplete path or a fabricated owner to obtain an ID. This preserves §8 and K9.

## Status semantics and counts

`Canonical` identifies a RAD-established identity/responsibility. It does not
assert that all its classifications, ancestry or relationships are resolved.
`Candidate` retains non-approved placement/standing. `Unresolved` describes an
unestablished dimension or relationship; it is not a fifth relevance category.
Harmonia relevance is separately populated only where evidenced. No unestablished
relationship is emitted as an approved edge.

| Category | Governed / represented |
| --- | ---: |
| Business CT1 | 18 |
| Business CT2 | 39 |
| Business CT3 | 133 |
| Business Enabling CT1 / CT2 / CT3 allocations | 0 / 0 / 0 — unresolved |
| Established BEC responsibility contexts | 76 |
| Features | 137 |
| Functions | 147 |
| Services | 133 |
| Processes | 19 |
| Enterprise Capabilities | 14 |
| Named Candidate BC concerns, separate from canonical counts | 3 |
| Enabling descriptions with unresolved independent element identity | 69 |

Zero BEC tier allocations does **not** mean zero BECs. No current BEC owner is
silently classified CT3 to frame the 137 Features. The required fixed model and
the current reconciliation gap are both preserved. Functions/Services/Processes
form a graph in their evidenced owning contexts, not a mandatory decomposition
chain. The 14 Enterprise entries' responsibility bullets are not new named
Business Functions or Services.

## Build and verification

Prerequisites: XeLaTeX/TeX Live (including KOMA-Script, fontspec, longtable,
seqsplit, fvextra, enumitem and bookmark), DejaVu fonts, latexmk, makeindex,
Python 3 with `markdown_it`, and Poppler `pdfinfo`.

From this directory:

```bash
make
```

This renders, compiles with sufficient latexmk/makeindex passes, validates the
result and copies the finished PDF alongside the master. The compile is bounded
at 180 seconds; `build/latexmk.txt` retains its output. A timeout is TIMEOUT,
not a semantic architecture/test failure. On this environment the build
converges well within the bound. Auxiliary files stay in ignored `build/`.

To compile committed LaTeX without regeneration:

```bash
make pdf
```

To verify sources and the existing compiled publication:

```bash
python3 tools/render.py --check
python3 tools/check.py
```

Checks include exact source fingerprints/citations, independent Markdown element
inventories and all 172 explicit BC parents, uniqueness of IDs/labels, complete
known BC tier paths, ownership references, unresolved BEC tier preservation,
deterministic generated output, source-block coverage, resolved compiled labels,
index completeness, PDF destinations, and zero undefined references, overfull
boxes or missing glyphs. Representative PDF pages were visually inspected.

Completion checks also require substantive Capability/Feature overviews, typed
headings, exact preservation of Reference IDs and existing labels, structural-ID
uniqueness and complete typed paths, explicit null IDs where ancestry is missing,
and a complete exception list. The sharing-programme Candidate has an overview
explaining its limitation, but cannot receive a full semantic account because
only its working name/spelling is established. That limitation is reported
separately from an absent overview. Existing semantic conflicts remain visible.

Source drift causes a hard stop: review the changed architectural assertions
before updating the manifest/evidence. Do not refresh fingerprints mechanically.
Reuse v0.1 identities; a later architectural reconciliation needs its own authority.

Repository-required architecture verification from the repository root:

```bash
timeout --signal=TERM --kill-after=15s 480s mvn test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

The initial baseline report is
`.junie/reports/2026-10-15-harmonia-ea-capability-map-v0.1.md`.
The reference-catalogue completion report is
`.junie/reports/2026-10-15-harmonia-ea-capability-map-v0.1-reference-completion.md`.
Version 0.2 work has not commenced.
