<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Report — Architecture Continuum documentation publications

Report-Produced-At: 2026-10-22T09:48:44Z

Production event: completed Report emitted to the governed repository by Codex.
The timestamp is the system UTC clock reading used immediately for this emission,
not an inferred Task, execution, file-metadata or acceptance time.

Agent: Codex. Release: **1.1.8**; immediate predecessor: **1.1.7**;
HCRL iteration: **001**.
Corresponding [operative Task and hierarchy clarification](../../before/ai-agent-tasks/unresolved-documentation-publications.md).
Originating HEAD: `ca7c471d6a2cc65b5bb2c52a031d9fde27b7f474`, clean entry
worktree; entry release `1.1.7`, previous `1.1.6`.
Result: implemented in the working tree; no commit or architectural closure is
asserted. The [pending HCRL](../../../../../review/1.1.8/hcrl-1.1.8-001.pending.md)
records the semantic change and confirmation standing.

## Implemented publication relocation

The complete `enterprise-architecture/continuum/documentation/` publication tree
now resides at `enterprise-architecture/documentation/continuum-manifest/`.
Its existing catalogue/browser, index, Exchange projection and LaTeX catalogue
retain their purpose and renderer. All 16 retained publication files from
1.1.5–1.1.7 have relocation correspondence in
[manifest-relocation.json](../manifest-relocation.json). Relative source links
were rebased and affected publication hashes refreshed. Historical XML and PDF
bytes remain unchanged; printed historical PDF paths retain their original
publication context.

The root `continuum/MANIFEST.md` remains the generated navigation manifest.
Canonical ingestion now excludes only that exact navigation file; it no longer
exempts a documentation subtree. The existing publisher generated a new 1.1.8
Manifest at its relocated path.

## Continuum RAD and model correspondence

The [compiled Continuum RAD](../../../../../../documentation/continuum-rad/published/1.1.8/continuum-rad.pdf)
and its LaTeX, chapters, correspondence and publication metadata are retained under
`enterprise-architecture/documentation/continuum-rad/published/1.1.8/`.
The document has **532 pages**, navigable PDF bookmarks, linked architectural
names, canonical source links and an architectural-name index.

The hierarchy is derived from the complete current governed Exchange projection,
using the existing `architecture_continuum.project` operation and native type
catalogue. Parts are **Motivation, Strategy, Business and Application**. Within
each Part, existing model organisation nodes supply Chapters and subordinate
sections in their existing order and nesting. Existing labels such as Business
Capabilities, Business Enabling Capabilities, Enterprise Capabilities and
InformationMap are preserved. The model's existing cross-domain `Continuum
additions` branch is filtered into the applicable native domain Parts, preserving
its labels, internal order and depth. Empty domains supply no invented content.

Every architectural heading records its exact model XPath in
[structure.json](../../../../../../documentation/continuum-rad/published/1.1.8/structure.json).
All **2,970 architectural headings** correspond to existing model nodes:
**1,368 organisation nodes** and **1,602 element-reference nodes**. Eight LaTeX
section levels preserve the deepest model organisation without flattening.
All **1,602 canonical elements** occur exactly once: **1,270 current/pending
descriptions** and **332 historical/superseded references**. Historical entries
retain their qualifications and original model branches. No independent RAD
taxonomy, fallback chapter or historical appendix is introduced. Only front
matter, a reading guide and the name index provide publication framing.

Canonical definitions and boundaries render the main architectural-content
section of each existing Markdown record. Repeated source-qualified
contributions and detailed provenance remain available through the linked
canonical record and structural Manifest. Existing standing and unresolved
properties remain traceable; publication does not promote Candidate or Proposed
architecture or adjudicate existing residual questions.

## Existing LaTeX reused and explanatory mechanisms

The starting presentation material was
`docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/Harmonia-EA-Capability-Map.tex`.
The new retained template adapts its KOMA report layout, typography, colours,
tables, heading/bookmark facilities and index. The original LaTeX workspace is
unchanged. Its title, independently organised taxonomy, authority claims, model
copies and semantic extracts are not current publication inputs.

Retained AI-assisted `reader-notes.json` paragraphs explain how to read the
existing domains and responsibility boundaries. Each paragraph cites explicit
canonical UUIDs. The deterministic renderer checks those source identities and
renders canonical definitions without semantic rewriting. Generation calls no
live AI service and performs no architecture discovery, enrichment or ingestion.

Two TikZ diagrams select six existing Composition assertions each, in Strategy
and Application. Selection uses the existing relationship-state selector, then a
deterministic bounded layout. Source/target identities, exact relationship records,
direction, assertion standing and currentness are preserved in `structure.json`;
visible labels retain Proposed/current qualifications where applicable. Layout
and omission assert no further relationships. No new model edge, identity or
manually authored Archi View was required.

## Tooling, navigation and release

The bounded `publish_continuum_rad.py` renderer reuses the current loader,
projector, native classification, source succession, TeX escaping and relationship
selector. Existing Makefile `all` and `check` operations now cover both
publications; `continuum-rad` generates narrative sources and `rad-pdf` compiles
them. The existing independence verifier now compares both products while
retaining only current presentation inputs. No general-purpose documentation
framework was introduced.

Current EA/root navigation, tooling paths, publication placement/ingestion
passages in governance and 57 legacy source-succession navigation links were
updated. The latter changes are path substitutions only, leaving original
legacy architectural text intact. Earlier HCRLs, completed registers and evidence
keep their originating paths and standing. The new documentation entry points
make the two products explicit. Compilation intermediates have a narrow ignored
`build/` path.

The existing expected-current bump advanced the sole root release authority once
from 1.1.7 to **1.1.8**, recording **previous_version=1.1.7**. Regeneration does not
increment the release. No Maven software version was changed.

## Validation and publication results

All commands used bounded execution. Python operations used the declared
dependencies already available via `PYTHONPATH=/tmp/harmonia-ea-deps`; no
repository dependency change was needed.

| Check | Actual result |
| --- | --- |
| Current complete EA test suite in final isolated verification | **PASS: 48 tests**, including five targeted publication-boundary tests. |
| Final Makefile `check browser-test` | **PASS**: release, navigation manifest, Exchange and both publication staleness, links and browser DOM-contract smoke checks. |
| Current projection validation | **PASS**: 1,602 elements, 1,651 relationships, identity/reference, naming, containment and offline XSD gates; zero allocated identities. Existing unresolved source matters remain qualified. |
| Final isolated reproducibility | **PASS**: 14 derived files matched byte for byte; no legacy RAD, scripts, Git metadata or historical configuration present. Canonical bytes remained unchanged. |
| Required Maven architecture suite, offline with JDK 21 | **PASS: 90 tests in 11 classes**, zero failures, errors or skips. |
| XeLaTeX/latexmk/index publication | **PASS**: 532-page PDF, fixed `SOURCE_DATE_EPOCH=1792627200`; final log has no overfull boxes, missing characters, undefined/duplicate references or pending rerun. |
| Exact model heading and PDF outline correspondence | **PASS**: all 2,970 architectural headings/bookmarks match model title, order and depth. |
| Relocation, preservation and source/navigation links | **PASS**: 16 relocation records, 57 navigation-only legacy changes, 49 current navigation links and 6,408 Manifest browser source links. |
| Working-tree whitespace validation | **PASS**: `git diff --check`. |

The final [reproducibility result](../reproducibility-check.json) retains commands
and artifact hashes. The [preservation check](../publication-preservation-check.json)
retains source preservation, hierarchy/PDF and link conclusions. Visual inspection
covered the source-cited Strategy explanation/diagram, a historical model chapter
(PDF page 291) and Petasos/Mneme descriptions (page 490). This is a bounded visual
sample; no full-document visual review or actual Archi transport is claimed.

The initial default Java 25 invocation passed its tests but produced unsupported
class-import warnings. Repeating with the supported JDK 21 established the
reported clean architecture-suite result without production changes. Earlier
intermediate publication comparisons were superseded by the final complete
isolated PASS for the settled hierarchy and diagram sources.

PDF SHA-256:
`3dd21d32b3fcf6dac10bfcbab59e6a785c70bd7d0fde212bb9b74a4711ee5470`.

## Canonical preservation and Solution Architect matters

All **1,602 canonical element files are byte-identical to originating HEAD**,
including their names, identities, definitions, relationships, standing and
unresolved material. The canonical projection template is unchanged. The
1.1.8 Exchange differs from 1.1.7 only in its model release value, and the complete
Manifest architectural browser payload is identical. Documentation generation
did not alter canonical architectural content or reopen migration, architecture
reconciliation or governance design.

No architectural decision or architectural publication blocker was encountered.
The only outstanding provenance matter is the unavailable attributable
instruction-entry time for the original Task and its hierarchy clarification.
The Task event and filename prefix remain **UNRESOLVED**; current clock,
filesystem/Git times and 1.1.7's accepted treatment are not substituted.

The iteration evidence checker runs after this Report's emission; its retained
result and any subsequent verification amendment preserve that order.
Solution Architect treatment of the disclosed Task timing and confirmation of the
incorporated result remain necessary under the existing Review Praxis before
the HCRL becomes complete. This Report records implementation and verification,
not human architectural acceptance.

## Subsequent verification amendment

Report-Amended-At: 2026-10-22T09:49:17Z

After original Report emission, the read-only iteration evidence checker returned
**UNRESOLVED** (exit 2): one Task, one Report, no structural errors. Only the
original Task event time and required filename prefix need Solution Architect
treatment. The [retained result](../iteration-evidence-check.json) records the
check's layout/event-prefix limits. This amendment preserves the original
Report-Produced-At and filename; it supplies no acceptance or temporal waiver.

## Publication-path correction amendment

Report-Amended-At: 2026-10-22T10:17:01Z

The user's final pre-acceptance correction is retained verbatim in the
corresponding Task, with its unavailable instruction-entry time explicitly
unresolved. This amendment belongs to the existing 1.1.8/001 change; release
remains 1.1.8, previous 1.1.7. Original Report production and earlier verification
events are preserved.

The generated directory is now `published/<release>/parts/`, containing
`motivation.tex`, `strategy.tex`, `business.tex` and `application.tex`.
These are domain Part files. All four files are byte-identical to the earlier
`chapters/` files. The generator, four master LaTeX includes, publication hash
keys and focused test expectations use `parts/`; the product README describes
the corrected location. The existing reproducibility verifier obtains its
expected artifact paths directly from the corrected generator.

The master changes only those four input paths. `structure.json`, all
architectural headings, narrative, diagrams, standing, front matter and index
remain unchanged. Other publication metadata remains unchanged except the
relocated hash keys and required master-source hash. The old generated directory
is absent.

Correction validation passed **five focused RAD tests**, the **eight-file source
staleness check**, isolated reproduction of **all eight RAD source/metadata
files**, and the existing bounded `rad-pdf` build. The rebuilt **532-page PDF is
byte-for-byte identical** to the pre-correction PDF, including the SHA-256 already
recorded above. Page text and outline titles, order, depth and destinations were
also compared explicitly. The final TeX log has no overfull boxes, missing
characters, undefined/duplicate references or pending rerun.

All 1,602 canonical element files, the projection template, Continuum Manifest
and release metadata remain unchanged. No unrelated full EA, Maven, browser or
Manifest checks were repeated for this bounded path correction. The original
full-suite/reproducibility evidence describes the state before the correction;
the [correction check](../parts-directory-correction-check.json) retains current
`parts/` correspondence, artifact hashes and actual focused results.

The iteration evidence checker remains **UNRESOLVED** solely for the unavailable
Task instruction-entry times/prefix, with one Task, one Report and no structural
errors. `git diff --check` passed. The pending HCRL DR-2/DR-3 records the
correction, verification and unchanged confirmation standing.

## Completion amendment

Report-Amended-At: 2026-10-22T10:23:36Z

Mark Hunter, Solution Architect, supplied **Accept** dispositions for DR-1–DR-3
in the reviewed HCRL and instructed:

> hcrl-1.1.8-001.pending.md updated. complete

The instruction is retained verbatim with the operative Task. Its attributable
instruction-entry time is unavailable and is not inferred. The reviewed pending
input SHA-256 is
`9bd627dd8bba4a4f87347ab9cdce338ebf12020b8b89e94b8b1b74f78890bd76`.
That input remains byte-for-byte unchanged.

The [completed register](../../../../../register/1.1.8/hcrl-1.1.8-001.complete.md)
now records the **1.1.7 → 1.1.8** transition, carrying the opening dispositions
and comments verbatim, the explicit confirmation and all existing architectural
qualifications. The bounded Minutiae control records the supplied Accept DR-3.
Current EA/documentation navigation and the governance publication reference
point to the completed record. Root release remains **1.1.8**, previous **1.1.7**;
completion consumes no further increment.

The Solution Architect's Accept DR-3 expressly accepts the disclosed temporal
provenance as sufficient for this transition, with no further remediation
required. The evidence checker still returns **UNRESOLVED**, one Task and one
Report, no structural errors; unavailable Task event times/prefix are not
manufactured or declared mechanically compliant. This records the human
treatment alongside the actual checker result.

[Closure checks](../closure-checks.json) passed reviewed-input/disposition
correspondence, completed-record/current-navigation checks (**72 links**), release
validation and `git diff --check`. All **1,602 canonical elements**, the projection
template, published artifact hashes and the corrected `parts/` PDF bytes remain
unchanged. No architecture or publication inputs changed during completion, so
the previously completed full and targeted tests were not repeated. No commit,
additional architectural acceptance beyond the reviewed scope or residual
architecture resolution is asserted.

Original Report production and prior amendments remain unchanged. This amendment
records the Solution Architect's actual confirmation and completion, superseding
the earlier pending-confirmation statements for this documentation transition.
