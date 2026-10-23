<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Canonical architectural organisation and navigation remap | Accept | The 1.1.10 canonical architectural organisation and navigation remap is accepted as implemented. The Architecture Continuum navigation hierarchy is accepted as the current canonical architectural organisation. Navigation coordinates identify architectural location and do not replace Canonical IDs as architectural identity.  The implemented Strategy structure, including the populated Resources subject warranted by existing architecture, is accepted. Existing architectural identity, semantics, relationships and standing remain authoritative and are not altered by navigation organisation.|
| [DR-2](#dr-2) | Preserved classification uncertainty and absent architecture | Accept | The explicit deferral of the 76 contextual-classification questions and the disclosed absence of individually represented Technology and Implementation & Migration architecture is accepted. The 7 non-axiom Principles under Unclassified Motivation and 69 BusinessInteractions under Unclassified Business retain their existing meaning, type and standing. Their placement records unresolved architectural classification and does not constitute semantic reclassification or permanent disposition. No architectural content is to be invented solely to eliminate an Unclassified branch or populate an otherwise absent architectural subject. |
| [DR-3](#dr-3) | Shared navigation projections and narrative publication | Accept | The shared navigation projection and Continuum RAD publication result are accepted within the documented projection and validation limits. The Continuum Manifest and RAD expose the canonical hierarchy and navigation coordinates as intended. The documented Archi behaviour — including retention of native layer roots, splitting of mixed-layer organisation and omission of source-only, cross-reference or empty non-element navigation nodes — is accepted as a projection limitation. Preservation of navigation coordinates on the projected architectural elements provides the required cross-projection navigation reference. No model elements or semantic relationships are to be invented merely to force Archi to reproduce unsupported canonical navigation nodes. The documented limitation that full page-by-page editorial review and interactive Archi visual inspection have not been asserted does not prevent acceptance of this bounded transition.|
| [DR-4](#dr-4) | Bounded supporting Minutiae and provenance | Accept | The bounded supporting evidence and explicit treatment of the unavailable Task event/prefix are accepted. The unresolved original Task timestamp/prefix is retained as an acknowledged provenance limitation and is not a blocker to closure. |

# HCRL — Continuum architectural taxonomy and navigation

**Standing:** completed iteration **001**, the authoritative semantic record for
**1.1.9 → 1.1.10**. Mark Hunter, Solution Architect, accepted DR-1–DR-4 in the
[reviewed pending input](../../review/1.1.10/hcrl-1.1.10-001.pending.md) and instructed:

> hcrl-1.1.10-001.pending.md updated. Complete!

Source: the corresponding user message in this Codex interaction, explicitly
linked to the updated HCRL and retained with the
[operative Task](../../evidence/1.1.10/001/before/ai-agent-tasks/unresolved-continuum-taxonomy-remap.md).
Its attributable instruction-entry timestamp is unavailable and is not inferred.
DR-4 accepts the unavailable original Task event/prefix as an acknowledged
provenance limitation that does not prevent closure. The completion instruction
confirms the accepted incorporated result; Codex records that human confirmation
without supplying an independent architectural decision.

Reviewed pending input SHA-256: `2eadf2bd67d2225317dee62646c96a63ba04c7c49f3b62779384418d6ec152c4`.
The reviewed input remains byte-for-byte unchanged. Its opening-table dispositions
and comments are carried verbatim. Explicit classification deferrals, the absence
of represented Technology/Implementation & Migration architecture, existing element
standing and the documented Archi/inspection limits remain as accepted.

The [formal execution Report](../../evidence/1.1.10/001/after/ai-agent-reports/20261022T233519Z-continuum-taxonomy-remap.md)
retains its original production event **2026-10-22T23:35:19Z**. Its subsequent
completion amendment and the [completion check](../../evidence/1.1.10/001/after/completion-check.json)
record closure and the lifecycle-only navigation/publication refresh. The reviewed
navigation and PDF are retained under
[completion inputs](../../evidence/1.1.10/001/before/completion-inputs/state.json).

Completion belongs to this same accepted change set and consumes no additional
release. Root `architecture-release.json` remains **1.1.10**, previous **1.1.9**.
The 1,602 canonical element files, their IDs, Names, native types, semantics,
1,651 relationships and standing remain unchanged. The 1,941-node hierarchy,
its coordinates and 1,209 authored Views are preserved. This completed register
record is immutable under common governance.

**Review/application basis:** the following account retains the originating
state, bounded remap and verification conclusions. Reviewed publication metrics
identify the accepted pre-completion snapshot; final completion-publication
metrics and hashes are recorded separately without overwriting that evidence.

Originating HEAD: `b8c14272f0ab0a8a452d904a8c4fa608f65b620b`, clean working tree.
The single expected-current patch operation prepared root release 1.1.10,
previous 1.1.9. Further generation and completion consume no additional increment.

The common [Review Praxis](../../../governance/architecture-review-praxis.md),
[Provenance contract](../../../governance/architecture-change-provenance.md) and
[release policy](../../../governance/architecture-release-policy.md) apply.
Relevant canonical axioms are AX-03 (preserve native semantics), AX-04
(architecture governs machinery), AX-08 (meaningful evidence), AX-14
(identity/location/standing and information-model distinctions), AX-15 and AX-17
(preserve uncertainty and the limits of established authority). Their definitions
remain solely in the linked [Continuum Principles](../../../governance/architectural-axioms.md).
No runtime responsibility, module/dependency boundary or new architectural type
is introduced.

<a id="dr-1"></a>
## DR-1 — Canonical architectural organisation and navigation remap

The Task explicitly replaces incidental native-type/history/model folder
organisation as the reader's taxonomy. The Continuum now owns one explicit
ordered [navigation tree](../../../continuum/navigation.json): Architecture
Governance `G`, Motivation `I`, Strategy `II`, Business `III`, Application `IV`,
Technology `V`, and Implementation & Migration `VI`. This is canonical
organisation metadata, not another element registry or a publication-owned
architecture. UUID-named native-type files remain stable storage locators; their
directories continue to validate native types/specialisations without prescribing
chapters. The generated root Manifest makes the numbered canonical hierarchy
directly navigable.

All **1,602 existing elements** are placed once. The existing element files remain
byte-identical, preserving canonical IDs, names, definitions, relationships,
source contributions, currentness, standing and residual qualifications. Native
relationships remain semantic assertions. Organisation creates no Composition,
Aggregation, Home, ownership or other ArchiMate edge. No identity is allocated.
The [before population](../../evidence/1.1.10/001/before/population.json) retains
source hashes and original organisation paths against originating HEAD.

Strategy exposes the existing Enterprise / Business / Business Enabling scopes
and their decompositions. Explicit recorded parent/owner correspondence supplies
navigation, retaining proposed ancestry and placement as proposed. Historical
capabilities retain their individual IDs and known original contexts within the
same scopes; no historical/superseded/pending architecture branch remains.
An exact source-to-instance binding or preserved original organisation is used;
neither label resemblance nor navigation supplies identity or semantic closure.
Courses of Action remain strategic approaches, with the one existing
ImplementationGuideline under Implementation Guardrails. The three substantive
existing strategic Resource definitions warrant a populated Resources subject.
No Architecture Patterns bucket is invented for approaches not so classified.

Business separates information, products/value streams, roles/organisational
collaborations and behaviour. Behaviour uses populated owner-capability contexts
with references back to Strategy, without repeating its full hierarchy or
asserting new Feature associations. Motivation retains its canonical axioms and
native classifications. The ten Stakeholders are numbered cross-cutting profiles,
with existing associations and no peer Stakeholders chapter or invented
associations for the six without represented edges.

Application separates the 70 explicitly specialised logical Topics from other
information structures and organises existing software by application context.
Recorded Topic decomposition is retained. Component/scoped DataObjects are not
promoted to Topics or Classes. Existing responsibilities place Petasos's shared
messaging architecture under Application/System-of-Systems/Middleware; this
classification creates no deployment boundary or Technology-level middleware.
Ponos and Energeia remain peers. Services and Interfaces remain peer subjects
within their relevant application context. Existing system-map cross-references
and the Provider Registry solution collaboration create no enclosing component.
Application/Component navigation describes scope without creating semantic
containment or converting specialisation/reuse into Composition.

Every exposed architectural navigation node receives a coordinate through the
same depth-first sibling-index traversal. The namespace prefix is fixed by the
authorised domain; each descendant appends its one-based sibling position. All
numbered nodes, including populated elements, continue to the actual published
depth. Numbers are derived locations, never canonical IDs or naming tokens.
Relocation tests demonstrate changed coordinates with unchanged element bytes.

Reviewed proposal, accepted by Solution Architect DR-1: confirm the bounded remap as implemented, preserving
individual standing and DR-2's residual classifications. The [all-element remap](../../evidence/1.1.10/001/after/element-remap.json),
[before/after outline](../../evidence/1.1.10/001/after/organisation-remap.md) and
[preservation check](../../evidence/1.1.10/001/after/preservation-check.json)
account for all 1,602 byte-identical element files, unchanged 1,651 semantic
relationships and stable storage locators. The hierarchy has 1,941 numbered
nodes, reaching six descendant levels. Automated checks supply no acceptance.

<a id="dr-2"></a>
## DR-2 — Preserved classification uncertainty and absent architecture

**Unclassified Motivation: 7 native Principles.** They are not the 18 canonical
AX axioms and the agreed taxonomy supplies no established location for these
broader/historical Principles. The remap preserves their meanings and standing;
it does not elevate them into axioms or redefine them as Requirements/Constraints.

**Unclassified Business: 69 native BusinessInteractions.** Their definitions and
Business domain are determinate, but the current canonical records provide no
governed capability allocation. No lexical capability match, ApplicationInteraction,
integration event, endpoint, flow or other application architecture is inferred.
Review may later establish contextual placement; this remap leaves that question
explicit. The navigation source retains an issue explanation against each of the
76 elements; the [complete Unclassified list](../../evidence/1.1.10/001/after/unclassified.json)
retains every ID, name, coordinate and classification issue.

Strategy, Application, Technology and Implementation & Migration have **zero**
elements requiring Unclassified branches in this remap. Empty Unclassified
headings are omitted. Existing proposed ancestry, canonical-name vocabulary gaps,
candidate architecture, unresolved Feature relationships and other semantic
residuals remain as recorded; being classifiable for navigation does not resolve
them. An automated confidence score supplies no architectural decision.

Technology and Implementation & Migration have no individually represented
elements in the current population. Their authorised Part roots state that
absence, with no speculative subsections. Native BusinessInteractions remain in
Business; component-scoped conceptual information is not invented into Classes;
no empty Integration Architecture, Release Strategy or R1/R2 family is fabricated.
Governance evidence/review/register/Praxes remain in their existing repository,
not Implementation & Migration architecture. No Legacy-RAD population is imported
to fill these gaps.

Reviewed proposal, accepted by Solution Architect DR-2: accept explicit deferral of the 76 contextual-classification
questions and the disclosed absent architecture, without resolving their meaning,
authorising new content or changing any existing element/assertion standing.

<a id="dr-3"></a>
## DR-3 — Shared navigation projections and narrative publication

Exchange, Manifest and RAD consume the same canonical organisation; none
classifies architecture during generation. The Exchange projection replaces
obsolete architecture folders and exposes each coordinate in organisation labels
and element location properties. Native element Name remains displayName;
canonical/representation IDs and all semantic edges are preserved. The 1,209
authored Views and their View-folder navigation remain unchanged. Actual Archi
transport and visual inspection remain distinct from XML/schema checks.

The structural Manifest, browser, index and LaTeX catalogue expose the same full
numbered hierarchy. The browser opens with architecture navigation and provides
name/coordinate/identity/content search and native-type/currentness filters.
The RAD follows front matter, Architecture Governance, Parts I–VI, and the
Architectural Name Index. Canonical coordinates replace LaTeX's automatic
section-number scheme. The PDF outline reaches every architectural node; printed
contents select higher levels for readability. Index entries include their
navigation coordinates. Model/history folders do not become narrative chapters.

Canonical prose, source-qualified standing and linked detailed contributions
remain source-defined. Existing source-cited reader notes and diagrams selecting
explicit existing relationships remain explanatory publication material. No new
architecture, Class/Topic conversion, native type or relationship is introduced
to improve the document. Current generation reads no Legacy-RAD, historical
processing configuration, old LaTeX model or interaction logs. Repeated and
isolated generation must be byte-identical, with PDF checks reported separately.

The Solution Architect accepts the generated publication and navigation result
within the disclosed validation/transport and inspection limits (Accept DR-3).
The reviewed pre-completion PDF is **522 pages, 1,699,289 bytes**, with **1,941 distinct
architectural bookmark destinations**. Its [publication check](../../evidence/1.1.10/001/after/rad-publication-check.json)
records representative rendered-page inspection, no overfull/undefined-reference
warnings and byte-identical clean compilation using the maintained relative
build path, declared fixed epoch and same installed TeX/font toolchain. An initial
nested-bookmark destination defect was corrected before the final compiled check.
Full page-by-page editorial acceptance is not asserted.

[Actual Archi 5.10 import/save/export](../../evidence/1.1.10/001/after/archimate-validation.json)
passed for all canonical fields/name-role property values, 1,651 relationships
and 1,209 Views, with zero canonical workbench changes. The
[native-navigation check](../../evidence/1.1.10/001/after/archi-navigation-check.json)
confirms all 1,602 coordinate properties. Archi retains mandatory native layer
roots, splits mixed-layer organisation, and omits 31 source-only/cross-reference/empty non-element
nodes (Governance, cross-reference System Map and unpopulated Part roots). Exact organisation remains in the
Exchange hierarchy and other projections. Native leaf Names remain unchanged.
No model elements were invented to force unsupported folders. Interactive Archi
visual inspection is not asserted. The raw whole-model comparator remains
DIFFERENCES with its classifications/counts preserved; canonical preservation
does not claim whole-model byte fidelity. The no-edit return does not replace
the maintained template.

[Isolated reproducibility](../../evidence/1.1.10/001/after/reproducibility-check.json)
records stable-input source reconstruction, current EA tests and byte-identical
derived publications without Legacy-RAD, scripts, Git or historical processing
configuration. PDF byte reproduction is separately bounded as above.

<a id="dr-4"></a>
## DR-4 — Bounded supporting Minutiae and provenance

The bounded group comprises the root patch increment, focused taxonomy and
publication tests, necessary maintained operational/representation instructions,
formal Task/Report evidence, and purpose-selected before/after mapping,
preservation and verification results. No production Java/POM change, general
cleanup, historical evidence rewrite, new Praxis or architecture-factory concept
is included. The required offline JDK 21 architecture suite passed **90 tests in 11
classes**, with no failures/errors/skips. Current EA identity/reference, semantic
containment, information naming, offline XSD, release/link/staleness and browser
DOM-contract gates passed. The final isolated EA suite passed **57 tests** and reproduced **18 byte-identical
derived artifacts**. Detailed outcomes are
recorded with [verification evidence](../../evidence/1.1.10/001/after/validation-results.json).
The maintained local-link check also covers the new governed-source navigation.
Creation placement explicitly resolves canonical element/context Names without
copying them into organisation metadata; its focused regression test is included.

Task-Issued-At and its required filename prefix remain **UNRESOLVED**: the
interaction exposes the operative wording and date context, not its attributable
instruction-entry time. The current clock is not substituted. The predecessor's
accepted treatment does not automatically extend to this Task. Accept DR-4
now explicitly supplies treatment for this transition: the unavailable event/prefix
is retained as an acknowledged provenance limitation and does not prevent closure. Report emission records its own
observable production event and matching UTC filename prefix. The final
[layout/event-prefix check](../../evidence/1.1.10/001/after/provenance-check.json)
recognises the one Task and one Report with no format errors; its outcome is
UNRESOLVED solely for the original Task event/prefix; its factual observation is
preserved alongside Accept DR-4 without inventing recovered timing or a machine PASS.
This checker establishes
neither event authenticity nor acceptance. An initially bold unresolved marker
was made plain text for the checker's conventional field parser; the unknown
event and verbatim operative wording were not altered.

```text
Minutiae-Accepted: Yes
Comment: The bounded supporting evidence and explicit treatment of the unavailable Task event/prefix are accepted. The unresolved original Task timestamp/prefix is retained as an acknowledged provenance limitation and is not a blocker to closure.
Source: Solution Architect Accept DR-4 in the reviewed pending input; no scope enlargement.
```

Mark Hunter, Solution Architect, supplied result confirmation in the completion
instruction quoted above. This register preserves the accepted bounded result
and its explicit deferrals/limits. The transition is complete on that human
acceptance and confirmation, without another release increment.

Completion refreshes only G.3.2's source link and lifecycle description: it now
points to the existing review repository and preserves the accepted classification
deferrals instead of saying 1.1.10 still awaits confirmation. All node Names,
order, references to canonical elements and navigation coordinates are unchanged.
Derived Manifest/Exchange/RAD/PDF outputs are regenerated consistently. The final
PDF remains **522 pages**, now **1,699,400 bytes**. The completion check records
source/PDF reproducibility, complete distinct bookmark destinations, exact semantic
projection equality outside organisation, reviewed-input preservation and local
links. Original transport/publication evidence retains its actual measured input;
no new actual Archi transport or interactive inspection is claimed for the
lifecycle-only organisation-text refresh.
