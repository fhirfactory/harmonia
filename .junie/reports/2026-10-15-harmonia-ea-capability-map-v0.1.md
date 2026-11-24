<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia-EA-Capability-Map v0.1 — completion report

**Result: COMPLETE — v0.1 delivered for review.**

This is an execution report, not architectural authority. The publication faithfully
represents current governed Markdown, preserves its uncertainty and establishes
stable publication identities. It introduces no architecture or v0.2 derivation.

## Delivered files

All publication deliverables are under
`docs/latex-rad/Harmonia-EA-Capability-Map/`:

- Complete master: `Harmonia-EA-Capability-Map.tex`, version **0.1**.
- Compiled PDF: `Harmonia-EA-Capability-Map.pdf`, **502 A4 pages**.
- Multi-file architecture, relationship, catalogue-context and identifier sections.
- Full linked RAD prose transcriptions within the selected source scope.
- `manifest.json`: 719 frozen element Reference IDs/labels, status, type, tier,
  parent/owner, aliases, provenance and exact Markdown evidence.
- `source-coverage.json`: 1,674 transcribed source blocks and their line/hash locators.
- `validation.json`: machine-readable successful validation results and counts.
- `README.md`: authority, layout, identifier policy, status semantics and build steps.
- `Makefile`, `tools/render.py`, `tools/check.py`: bounded compilation and validation.

The final published PDF SHA-256 is
`8d2da6e40f7f6f2a5987e30da4e55678f4933b2207d6436b91f7ea3c7616789e`.

Working branch was verified as `Harmonia-GovernedAccess`. The source snapshot is
recorded against commit `98410db60155198a75f74c79d98060070209faa1`, with per-source
SHA-256 fingerprints. No governed Markdown or production source was modified.

## Architectural authority and source files used

The repository and documentation AGENTS instructions apply. AX-01/04 preserve
health/business meaning, AX-14 preserves semantic distinctions, AX-17 prevents
inferred architecture and requires explicit uncertainty, and AX-18 preserves domain
authority. This is representation work, not a material architectural change or
convergence/runtime implementation activity.

The publication uses the following **18 current governed Markdown sources**:

1. `docs/markdown/02-strategy/capabilities/business-capabilities.md`
2. `docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md`
3. `docs/markdown/02-strategy/capabilities/enterprise-capabilities.md`
4. `docs/markdown/02-strategy/capability-maps/capability-tier-model.md`
5. `docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md`
6. `docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md`
7. `docs/markdown/03-business-architecture/behaviours/01-entity-management.md`
8. `docs/markdown/03-business-architecture/behaviours/02-service-administration.md`
9. `docs/markdown/03-business-architecture/behaviours/03-service-delivery.md`
10. `docs/markdown/03-business-architecture/behaviours/04-health-service-operations.md`
11. `docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md`
12. `docs/markdown/03-business-architecture/behaviours/health-service-assurance.md`
13. `docs/markdown/03-business-architecture/processes/business-processes.md`
14. `docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md`
15. `docs/markdown/04-information-architecture/traceability/domain03-traceability.md`
16. `docs/markdown/05-information-systems-architecture/traceability/b2-1-individual-care-delivery-health-information-access.md`
17. `docs/markdown/04-information-architecture/reviews/package2-g1-review.md`, K9 section
    (lines 1392–1498 in this snapshot).
18. `docs/markdown/governance/architectural-axioms.md`, AX-01, AX-04, AX-14, AX-17 and AX-18.

The first sixteen documents are transcribed in full. Governance excerpts use exact
recorded ranges. HTML comments/anchor markup are represented by publication
locators rather than printed; source diagram code remains textual, and box/tree
glyphs are removed from source listings. Long equations wrap as textual operator
sequences. These are presentation changes only. Full definitions, intent, state
changes, boundaries, decomposition assertions, illustrative activities, care
contexts, exclusions, notes, classifications and unresolved material remain
available through direct metadata links.

The RAD Business Architecture reference-candidate JSON and its README were
inspected as **navigation aids**, preserving their candidate capture standing.
They located current Markdown assertions and existing internal record keys;
they supplied no architectural approval. No candidate summaries were imported.
The actual Markdown element inventories, 172 explicit BC parents and all 299
behavioural owner declarations were independently checked. Rendering depends on
the frozen publication manifest and source Markdown, not the reference-candidate
capture. Existing source links to historical material are retained as links only;
their targets were not used to supply architecture.

## Architecture counts and reconciliation

| Category | Governed source finding | Publication |
| --- | ---: | ---: |
| Business CT1 | 18 | 18 |
| Business CT2 | 39 | 39 |
| Business CT3 | 133 | 133 |
| Canonical Business Capabilities | 190 | 190 |
| Business Enabling CT1 allocations | Unresolved; none established | 0 established |
| Business Enabling CT2 allocations | Unresolved; none established | 0 established |
| Business Enabling CT3 allocations | Unresolved; none established | 0 established |
| Established BEC responsibility contexts | 76 | 76 |
| Features | 137 | 137 |
| Functions | 147 | 147 |
| Services | 133 | 133 |
| Processes | 19 | 19 |
| Enterprise Capabilities | 14 | 14 |
| Named candidate BC concerns with unresolved placement | 3 | 3, separately Candidate |
| Named enabling decomposition descriptions | 73 | 73 |
| Description identities independently established as BECs | 4 | 4, not recounted |
| Description standalone element identities unresolved | 69 | 69, not invented Capabilities |

There is **no source/publication inventory discrepancy**. The 137 Feature frames
remain unreconciled; zero established BEC tier allocations does not imply zero BECs.
No missing peer, tier or owner was invented to obtain a conventional hierarchy.
Business Capabilities do not acquire Features. BEC contextual-view membership is
not ancestry. Functions, Services and Processes remain a source-evidenced graph
in their owning contexts rather than a mandatory containment chain. Enterprise
responsibility bullets do not become new named Business Functions or Services.

There are **719 architectural-element publication records**: **716 Canonical**
identity/responsibility records and **3 Candidate** concerns. Candidate/provisional
taxonomies and other unresolved proposals retained in source prose are not
silently converted into independently established elements.

## Identifier allocation

- Preserved all **137 existing Feature identifiers** and **14 EC identifiers**
  exactly, including `FEAT-EM-01` spelling and EC-14.
- Allocated **568 stable publication Reference IDs**, using
  `HCM-BC-`, `HCM-BEC-`, `HCM-FN-`, `HCM-SV-` or `HCM-PR-` plus an initial
  deterministic semantic name token. This includes the three Candidate concerns
  without approving their placement.
- Allocated **719 stable LaTeX element labels**, using typed namespaces. No
  authoritative existing labels were found in the governed source scope.
- Preserved existing navigation keys without promoting them to Canonical IDs or
  aliases. Preserved evidenced name aliases only for the same element.
- Allocated **zero structural Canonical IDs**. All unallocated structural IDs
  remain unresolved, including BCs with established tiers but unallocated codes.

The distinction between stable publication Reference Id and contextual structural
Canonical ID is necessary: the metamodel requires the latter to change on a
responsibility relocation, retaining its prior value as an alias. Publication
Reference IDs/labels remain frozen across relocation and rename. Neither depends
on chapter, ordinal, current parent position or random generation. The renderer
does not allocate identifiers. Future versions must reuse manifest values.

## Validation performed

**PASS — source and publication checks.**

- All 18 source fingerprints and exact evidence lines match current Markdown.
- Independent BC/Feature/Function/Service/Process inventories match the manifest.
- All 172 BC parent assertions agree with explicit Markdown tier/parent and
  decomposition statements. Known BC paths contain the correct CT tiers.
- All 299 Function, Service and Process owners agree with Markdown declarations.
- BEC tiers, required Feature ancestry and explicitly unestablished relationships
  remain unresolved. The known partial responsibility chain is not inflated.
- Each of the 719 elements has one unique Reference Id and one definitive label.
- **2,418 unique LaTeX labels**, no duplicates; all references resolve.
- All selected nonblank source lines occur in covered transcription blocks;
  **zero uncovered lines**. Source hashes also check those coverage blocks.
- Frozen generated output matches current rendering deterministically.
- **719 index entries accepted**, zero rejected, zero makeindex warnings.
- **Zero undefined references, duplicate PDF destinations, overfull boxes,
  missing glyphs or remaining LaTeX warnings.**

**PASS — complete LaTeX compilation.**

The final bounded `latexmk -xelatex` build converged with two XeLaTeX passes,
makeindex and PDF generation. Earlier compilation defects in URL escaping and
TOC widths were corrected; the final build is clean. TOC, name references, page
references and the name index are resolved. Representative title/TOC, BC,
Feature/Function/Service, Enterprise, assurance-source, dependency-table and index
pages were rendered and visually inspected. The published PDF matches the
validated build hash. Build auxiliaries are ignored; the review PDF is delivered.

Reproduce from the publication directory with `make`. Compilation has a
180-second command-level bound. Source checking uses `python3 tools/render.py
--check`; complete publication checking uses `python3 tools/check.py`.

**PASS — repository architecture suite.**

```bash
timeout --signal=TERM --kill-after=15s 480s mvn test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

Result: **90 tests, 0 failures, 0 errors, 0 skipped**, across 11 architecture test
classes; **BUILD SUCCESS**, Maven elapsed **19.178 seconds**. There was no timeout
or stall. No production code was changed in response to verification. The existing
RAD navigation-capture validator also passed, separately reporting its preserved
architectural uncertainties; that result does not reconcile those uncertainties.

## Preserved RAD issues and boundaries

1. **K9/BEC reconciliation:** all 76 BEC contexts retain unresolved CT assignment
   and complete ancestry. All 137 required Feature frames remain unreconciled.
   The 69 named descriptions do not become invented intermediate Capabilities.
2. **BC/BEC name collision:** Health Information Exchange remains two distinct
   responsibilities with an unresolved global naming conflict. Neither is renamed,
   merged or treated as an alias; the name index distinguishes both categories.
3. **BC decomposition uncertainty:** OI-BC-001 compounding/preparation remains
   neither established nor rejected; OI-BC-002 operational-readiness placement
   remains unresolved. The Maintenance membership discrepancy and Sharing
   Programme placement remain unadjudicated. Clinical Intervention's proposed
   three-child taxonomy stays provisional; Clinical Determination and Individual
   Care Planning have no adjudicated CT3 set.
4. **Relevance versus status:** Health Service Governance is Canonical CT1 but has
   unresolved relevance/enablement. Descendant relevance is not inherited.
5. **Credential distinction:** Clinical Qualification Management and operational
   Credential Management remain distinct, including their Feature-association
   boundary; lexical similarity does not merge them.
6. **Care-team Function conflict:** the current `Coordinate Care-Team Collaboration`
   description mentions managing clinical handovers; metamodel §3.7 excludes
   clinical-handover management authority. Both assertions are preserved for
   architectural review under AX-17; no semantic correction is made here.
7. **Behaviour/Process qualifications:** illustration scope, Encounter ON_LEAVE
   relationships and the disconnected Synthetic Task stalled/failed outcome remain
   as stated. Process detail does not establish universal transitions or extra states.
8. **Assurance boundaries:** the Strategy source's opening current reconciliation
   qualifies its older BC-18 tier/relevance statements. Its Strategy stop boundary
   does not erase subsequent approved Domain03 assurance elements. Detailed
   lifecycle/contract/internal mapping questions remain unestablished. Independent
   assurance, operational management and clinical adequacy remain distinct.
9. **Relationship semantics:** BC evidence/context/consumption statements, approved
   EC contribution meanings and withdrawn Service dependencies retain their exact
   source meanings. No stronger runtime, ownership or structural edge substitutes
   for them. Retained B2.1 L1/L2 wording does not allocate current CT tiers or extend
   enablement to new BC descendants.

These are reported source conditions, not defects silently repaired by the
publication. Full qualifications remain in the linked source text.

## Scope confirmation and stop

**No architectural content from any earlier candidate LaTeX document was imported.**
The excluded candidate LaTeX files were not inspected, even for presentation.
No legacy documentation filled a gap. No governed Markdown was changed. No
additional Feature, Enterprise Capability, peer taxonomy, ancestry, component
allocation or v0.2 material was derived. No graphical containment pictographs
were introduced. Work stops at the completed and validated v0.1 baseline for review.
