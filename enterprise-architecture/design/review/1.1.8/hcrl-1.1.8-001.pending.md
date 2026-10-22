<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | First-class documentation and Continuum Manifest relocation | Accept | The first-class Enterprise Architecture documentation structure and Continuum Manifest relocation are accepted as implemented.|
| [DR-2](#dr-2) | Narrative Continuum RAD grounded in the current model | Accept | The Continuum RAD is accepted as the narrative publication of the Architecture Continuum. Its model-derived hierarchy, source-grounded explanatory material and model-derived diagrams satisfy the authorised documentation intent without changing or extending the canonical architecture.|
| [DR-3](#dr-3) | Bounded supporting Minutiae and formal provenance | Accept | The bounded supporting Minutiae and formal provenance are accepted. The Task instruction-entry timestamp was unavailable to the executing agent and was correctly left unresolved rather than inferred or replaced with another timestamp. The retained Task content, interaction date, originating Git state, governed review record, Report production event and implementation/verification evidence provide sufficient provenance for this transition. No further remediation is required. |

# HCRL — Architecture Continuum documentation publications

**Review basis:** immediate governed predecessor **1.1.7**, resulting release
**1.1.8**, iteration **001**. Originating HEAD
`ca7c471d6a2cc65b5bb2c52a031d9fde27b7f474`, clean working tree; root release
`1.1.7`, previous `1.1.6`. The
[completed 1.1.7 HCRL](../../register/1.1.7/hcrl-1.1.7-001.complete.md)
establishes the accepted baseline. The attached
[1.1.7 pending record](../1.1.7/hcrl-1.1.7-001.pending.md) and all earlier
review/register/evidence remain unchanged.

Mark Hunter, Solution Architect, directly authorised this bounded implementation
in the [operative Task](../../evidence/1.1.8/001/before/ai-agent-tasks/unresolved-documentation-publications.md)
on 2026-10-22 (interaction date). The opening table records that instruction's
authority, not an invented disposition or final confirmation. No new architectural
decision is needed to implement the requested presentation. This pending record
retains actual application and verification facts; completion requires Solution
Architect confirmation under the existing Review Praxis.

<a id="dr-1"></a>
## DR-1 — First-class documentation and Continuum Manifest relocation

The authorised mapping is exactly
`enterprise-architecture/continuum/documentation/` →
`enterprise-architecture/documentation/continuum-manifest/`.
Documentation now sits beside Continuum, governance, design and tooling. The
existing structural browser, index, Exchange and LaTeX catalogue retain their
purpose and generating machinery; `continuum/MANIFEST.md` remains generated
navigation. Canonical Markdown ingestion no longer exempts a documentation
subtree: only that exact root navigation manifest is excluded.

Retained 1.1.5–1.1.7 publications move with the Manifest. Relative source links
are rebased and their publication hashes refreshed; historical XML/PDF bytes
remain unchanged. Historical PDF printed paths retain original context.
[Relocation correspondence](../../evidence/1.1.8/001/after/manifest-relocation.json)
records all before/after paths and hashes. Current source-succession navigation
links are updated without changing enclosed original legacy content. Earlier
evidence/accepted records keep their original locators and standing.

Current Makefile paths, reproducibility operation, maintained navigation and the
two governance passages describing publication placement/ingestion are updated.
These narrowly apply the issued relocation authority; no general governance or
documentation framework is redesigned. Root release is advanced once using the
expected-current `1.1.7` check to `1.1.8`, previous `1.1.7`.

Validation confirms all 16 relocation records and historical XML/PDF byte
preservation, 57 navigation-only legacy changes, 49 current navigation links and
6,408 Manifest browser source links. The new browser's complete architectural
payload is identical to 1.1.7; Exchange differs only in its model release value.
The [preservation check](../../evidence/1.1.8/001/after/publication-preservation-check.json)
retains the exact bounded conclusions.

Relevant axioms: AX-04 uses the existing machinery; AX-14 keeps canonical state,
derived documentation and acceptance distinct; AX-17 prohibits architecture
inference from paths. No runtime/module boundary changes.

<a id="dr-2"></a>
## DR-2 — Narrative Continuum RAD grounded in the current model

`enterprise-architecture/documentation/continuum-rad/` contains the adapted LaTeX
template and source-cited reader notes. Release publications hold the master,
domain Part files under `published/<release>/parts/`, PDF, full source/model correspondence and
publication hashes. The v0.2 LaTeX RAD supplies KOMA layout, fonts, colour palette,
tables, heading/bookmark facilities and architectural-name index. Its original
workspace remains unchanged. Its architecture, model copies, taxonomy and
authority claims are not publication inputs.

The Parts are **Motivation, Strategy, Business and Application**, in governed
native domain order. Chapters and subordinate sections derive from the complete
existing Exchange model organisation, including the established projector's
`Continuum additions` branches. Existing Business Capabilities, Business Enabling
Capabilities, Enterprise Capabilities, InformationMap and historical branches
retain their labels, internal order and nesting. Native classification selects
the domain Part for each existing additions branch; it creates no chapter or
section labels. Every heading has an exact XPath into the current governed
Exchange projection. All eight required section levels are preserved, including
the deepest model paths, without silent flattening. Empty native domains acquire
no invented content. Navigation folders confer no architectural containment.

The user's operative hierarchy clarification, retained verbatim with the Task,
governs this implementation: publication-only structure is exceptional rather
than the organising hierarchy. The initial draft's fallback type grouping and
historical appendix were removed before result confirmation. Historical and
superseded elements remain qualified in their existing model branches; they are
not reorganised by publication currentness.

The deterministic renderer accounts for **all 1,602 elements**: **1,270** current
or pending descriptions and **332** historical/superseded references. Its **2,970**
architectural headings derive from existing model organisations (1,368) and
element-reference nodes (1,602). Front matter and the name index are the only
publication-only framing. This directly achieves the requested model
correspondence without an independent RAD taxonomy. Exact heading origins,
element placement/source hashes and complete source standing/unresolved metadata
are retained in the generated `structure.json`.

AI-assisted reader notes explain existing responsibility boundaries with explicit
canonical source identities. Source definitions/boundaries are rendered directly
from canonical Markdown; repeated source-qualified contributions remain available
through links to the full canonical record and Manifest. No generation-time AI
service, source classifier, ingestion adapter, new architectural fact or identity
allocation is introduced. TikZ diagrams select small explicit native Composition
neighbourhoods using the existing relationship-state selector, retaining exact
edges, direction, currentness and standing in correspondence. No new Archi View
or model mutation is required.

AX-04 preserves existing machinery; AX-14 preserves independent candidate,
proposed, historical and current standing; AX-17 preserves absent/unresolved
architecture. Existing baseline qualifications remain visible, including source
wording about unapproved detailed Solution Architecture. Publication supplies no
new adjudication or acceptance. No architectural publication blocker or new
architectural decision was encountered; existing baseline residuals remain
outside this documentation task.

Before the path correction below, validation passed all **48 current EA tests**, including five targeted
publication-boundary tests, release/staleness/link checks and browser DOM-contract
smoke checks. The pre-correction isolated
[reproducibility check](../../evidence/1.1.8/001/after/reproducibility-check.json)
passed for **14 derived files**, with no legacy RAD, scripts, Git metadata or
historical configuration present. Canonical source bytes remained unchanged.
Identity/reference, naming, containment and offline XSD gates passed; publication
allocated zero identities. Existing source qualifications remain unresolved
where already recorded.

XeLaTeX/latexmk/index compilation produced the **532-page PDF**. All **2,970
architectural PDF bookmarks** match the model's title, order and depth. The final
TeX log has no overfull boxes, missing characters, undefined/duplicate references
or pending rerun. A bounded visual sample inspected the source-cited Strategy
diagram, historical model chapter and Petasos/Mneme descriptions. No full-document
visual review or actual Archi transport is asserted.

Preservation against originating HEAD confirms **all 1,602 canonical element
files and the projection template are unchanged**. No architecture, relationship,
standing or residual question was altered to obtain these publication results.

The Solution Architect's final pre-acceptance follow-up, retained verbatim in the
operative Task, corrects the generated directory name from `chapters/` to
`parts/`: these four files represent EA-domain/LaTeX Parts. Generator paths,
the four master includes, publication hash keys and focused test expectations
now use `parts/`. The existing reproducibility verifier derives its expected
paths from the corrected generator. No document hierarchy or content changes.

The [targeted correction check](../../evidence/1.1.8/001/after/parts-directory-correction-check.json)
records four byte-identical relocated files, unchanged `structure.json` and
unchanged metadata apart from the required paths/master hash. All **five focused
RAD tests**, source staleness validation, isolated reproduction of **eight RAD
files** and PDF compilation passed. The rebuilt **532-page PDF** has identical
page text and the same outline titles, order, depths and destinations as before
the correction; its complete PDF bytes and SHA-256 are also unchanged.
Canonical sources, projection template, Continuum Manifest and release metadata
remain unchanged. Earlier full-suite results are retained as pre-correction
evidence; unrelated checks were not repeated.

<a id="dr-3"></a>
## DR-3 — Bounded supporting Minutiae and formal provenance

This bounded group comprises current navigation/path corrections, the ignored
LaTeX compilation directory, release metadata mechanics, targeted publication
tests and deterministic verification, the formal Task/Report and purpose-selected
relocation/preservation/verification evidence. The operative Task expressly
authorises these supporting activities. They establish no architectural content.

The formal Task's attributable instruction-entry timestamp is unavailable in this
interaction. Its event and required sortable filename prefix remain explicitly
**UNRESOLVED**, without substitution from the current clock, file metadata or
prior release decisions. The accepted temporal treatment for 1.1.7 is not extended
to this new iteration. Report production retains its own observable emission
event. Solution Architect treatment is required before closure.

The required offline Maven architecture suite passed **90 tests in 11 classes**
under JDK 21, with zero failures, errors or skips. The earlier Java 25 invocation
produced unsupported class-import warnings; the supported-JDK repeat established
the reported result without production changes. Final working-tree whitespace
validation passed.

The formal
[Report](../../evidence/1.1.8/001/after/ai-agent-reports/20261022T094844Z-continuum-documentation-publications.md)
retains correspondence, actual checks and limits. The
[iteration evidence check](../../evidence/1.1.8/001/after/iteration-evidence-check.json)
is **UNRESOLVED**, with one Task, one Report and **no structural errors**:
only the unavailable Task event time/prefix requires Solution Architect
treatment. Successful publication does not waive that limit or supply result
confirmation. The required Report production event and filename prefix are
recorded separately from the unresolved original Task timing.
The publication-path follow-up also has no exposed instruction-entry timestamp;
its verbatim instruction and unresolved event are retained with the same Task.
The Report's later amendment records this bounded correction and verification
without replacing its original production event.

```text
Minutiae-Accepted: [awaiting Solution Architect entry: Yes | No]
Comment: [awaiting Solution Architect treatment of this bounded group and the disclosed Task time/prefix limit]
```

No architectural closure or complete register record is asserted by Codex.
