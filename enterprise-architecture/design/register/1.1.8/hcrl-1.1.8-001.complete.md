<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | First-class documentation and Continuum Manifest relocation | Accept | The first-class Enterprise Architecture documentation structure and Continuum Manifest relocation are accepted as implemented.|
| [DR-2](#dr-2) | Narrative Continuum RAD grounded in the current model | Accept | The Continuum RAD is accepted as the narrative publication of the Architecture Continuum. Its model-derived hierarchy, source-grounded explanatory material and model-derived diagrams satisfy the authorised documentation intent without changing or extending the canonical architecture.|
| [DR-3](#dr-3) | Bounded supporting Minutiae and formal provenance | Accept | The bounded supporting Minutiae and formal provenance are accepted. The Task instruction-entry timestamp was unavailable to the executing agent and was correctly left unresolved rather than inferred or replaced with another timestamp. The retained Task content, interaction date, originating Git state, governed review record, Report production event and implementation/verification evidence provide sufficient provenance for this transition. No further remediation is required. |

# HCRL — Architecture Continuum documentation publications

**Standing:** completed iteration **001**, the authoritative semantic record for
**1.1.7 → 1.1.8**. Mark Hunter, Solution Architect, accepted DR-1–DR-3 in the
[reviewed pending input](../../review/1.1.8/hcrl-1.1.8-001.pending.md) and instructed:

> hcrl-1.1.8-001.pending.md updated. complete

Source: the corresponding user message in this Codex interaction, explicitly
linked to the reviewed HCRL and retained with the operative Task. Its attributable
instruction-entry timestamp is unavailable and is not reconstructed. The
Solution Architect's Accept DR-3 expressly accepts the disclosed provenance as
sufficient for this transition, with no further remediation required. Reviewed
input SHA-256: `9bd627dd8bba4a4f87347ab9cdce338ebf12020b8b89e94b8b1b74f78890bd76`.
The pending input remains byte-for-byte unchanged. Opening-table dispositions
and comments are carried verbatim. This completed record records human acceptance
and confirmation; Codex supplies no independent architectural decision.

Completion belongs to the same accepted change set and consumes no further
release: root `architecture-release.json` remains **1.1.8**, previous **1.1.7**.
The documentation change is complete. Canonical architectural content and its
existing element/assertion standing remain unchanged; publication acceptance
resolves none of the baseline's architectural residuals. This completed register
record is immutable under common governance.

**Review basis:** immediate governed predecessor **1.1.7**, resulting release
**1.1.8**, iteration **001**. Originating HEAD
`ca7c471d6a2cc65b5bb2c52a031d9fde27b7f474`, clean working tree; root release
`1.1.7`, previous `1.1.6`. The
[completed 1.1.7 HCRL](../../register/1.1.7/hcrl-1.1.7-001.complete.md)
establishes the accepted baseline. The attached
[1.1.7 pending record](../../review/1.1.7/hcrl-1.1.7-001.pending.md) and all earlier
review/register/evidence remain unchanged.

Mark Hunter, Solution Architect, directly authorised this bounded implementation
in the [operative Task](../../evidence/1.1.8/001/before/ai-agent-tasks/unresolved-documentation-publications.md)
on 2026-10-22 (interaction date). The opening table now carries the Solution
Architect's subsequent Accept dispositions exactly. The explicit completion
instruction confirms the incorporated result under the existing Review Praxis.
No new architectural decision is needed for this presentation change. Actual
application and verification facts, exclusions and source qualifications below
remain those of the reviewed documentation transition.

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

The formal Task's attributable instruction-entry timestamps and required sortable
filename prefix remain explicitly **UNRESOLVED**, without substitution from the
current clock, file metadata or prior release decisions. The Solution Architect's
Accept DR-3 explicitly treats the disclosed limit: retained Task content,
interaction date, originating Git state, governed review record, Report production
event and implementation/verification evidence provide sufficient provenance
for this transition; no further remediation is required. This records the actual
1.1.8 decision and does not extend 1.1.7's treatment by inference. Report production
retains its separately observable emission event. No full temporal conformance
or mechanical-checker waiver is asserted.

The required offline Maven architecture suite passed **90 tests in 11 classes**
under JDK 21, with zero failures, errors or skips. The earlier Java 25 invocation
produced unsupported class-import warnings; the supported-JDK repeat established
the reported result without production changes. Final working-tree whitespace
validation passed.

The formal
[Report](../../evidence/1.1.8/001/after/ai-agent-reports/20261022T094844Z-continuum-documentation-publications.md)
retains correspondence, actual checks and limits. The
[iteration evidence check](../../evidence/1.1.8/001/after/iteration-evidence-check.json)
remains **UNRESOLVED**, with one Task, one Report and **no structural errors**:
only the original Task event time/prefix limit remains, alongside the human
Accept DR-3 treatment. Successful publication is not substituted for that decision
or for the explicit result confirmation. The required Report production event and
filename prefix remain separate from unavailable Task timing. The hierarchy,
publication-path and completion follow-ups also have no exposed instruction-entry
timestamps; their verbatim wording and unresolved events remain with the same
Task. Report amendments preserve their actual later provenance without replacing
original production. The [closure checks](../../evidence/1.1.8/001/after/closure-checks.json)
retain reviewed-input preservation, completed-record correspondence, current
navigation/release checks and the accepted temporal limit.

```text
Minutiae-Accepted: Yes
Comment: Recorded from the Solution Architect's Accept DR-3 and its opening-table comment. The bounded supporting group and sufficient formal provenance are accepted; no further remediation is required.
```

The Solution Architect's Accept DR-1–DR-3 dispositions and subsequent explicit
completion instruction confirm this bounded transition. Current navigation now
points to this completed record. The publication remains at the accepted
`parts/` paths with unchanged PDF bytes. Earlier architectural residuals and
individual standing remain outside this documentation acceptance. Completion
changes no canonical architecture, publication content or release metadata.
