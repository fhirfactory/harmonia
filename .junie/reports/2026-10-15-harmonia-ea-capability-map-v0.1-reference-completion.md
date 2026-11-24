<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia EA Capability Map v0.1 — reference catalogue completion

The existing publication is complete for architectural-description review as
Version **0.1**, on `Harmonia-GovernedAccess`. The finished
[711-page PDF](../../docs/latex-rad/Harmonia-EA-Capability-Map/Harmonia-EA-Capability-Map.pdf)
combines authored architectural explanation with the preserved governed RAD
definitions and semantic detail. This is publication completion, not a declaration
that unresolved architecture has been adjudicated or that a domain/programme has
been newly completed or frozen. Version 0.2 has not commenced.

## Authority and scope

The eighteen original fingerprinted RAD documents remain unchanged, as do their
1,674 source-coverage blocks and generated semantic-detail sections. The
Architectural Axioms, metamodel §8, capability-tier model, BC/BEC/EC catalogues,
established Domain03 behaviour and Processes, assurance derivation, B2.1 support,
cross-capability dependencies and supplied Domain03-to-Domain04 traceability
govern the work. The Architecture Completion Plan's fresh-context and authority
guardrails apply; no Domain05 or runtime programme step is advanced.

AX-01/04 retain healthcare meaning rather than deriving capability semantics from
machinery. AX-14 preserves Capability, Feature, Function, Service and Process
distinctions. AX-17 preserves actual ancestry, unresolved relationships and
candidate standing. AX-18 prevents information use and coordination from
conferring domain authority. The explanations consistently apply these limits,
including clinical work, workforce, capacity, collaboration and assurance.

No previous candidate LaTeX publication was read or imported. No capability,
Feature, Function, Service, Process, parent, support relationship or relevance
classification was introduced. The task's explicit identifier-allocation
authorisation supplies the allocation decision; it does not supply missing BEC
ancestry or enlarge the architecture.

## Delivered catalogue

- All **283 Capability records**, including fourteen Enterprise Capabilities and
  three retained Candidates, have an authored **Capability Overview**.
- All **137 Features** have specific authored overviews. Intent, boundaries,
  Harmonia relevance and notes explain established responsibilities where useful.
- All **19 Processes** and thirteen significant Functions/Services have authored
  overviews. Every Function/Service now displays its actual source description
  and applicable local qualifications, rather than relying on a heading and
  distant source link. Complete governed Process descriptions remain in place.
- Headings visibly identify CT1/CT2/CT3, BEC, Feature, Function, Service, Process
  or Enterprise Capability. Unresolved BEC tier allocation remains Unresolved.
- Metadata shows structural ID, stable Reference Id, status, applicable tier,
  complete established BC ancestry or partial owning context, relevance, sources
  and actual support/behavioural relationships. Scope and containment tables
  navigate established children and owned behaviour in both directions.
- Six reviewable authored Markdown files under `catalogue/` are separately
  fingerprinted in the manifest. They are explanation, not additional RAD
  authority. Definitions and exact RAD semantic detail remain distinguishable.

## Identifiers and preserved uncertainty

The publication allocates **190 new BC Structural Canonical IDs**. Approved local
codes use the initial frozen BC Reference Id semantic token uppercased, with
explicit CT typing and the complete established ancestor path. No display ordinal
is promoted to a code. Local codes survive ordinary renaming; an authorised
responsibility relocation requires a changed structural path and a retained,
direct, non-reusable ID alias. Reference IDs and LaTeX labels remain independent.

The fourteen existing governed EC identifiers populate their Canonical ID field
without an invented CT path. All **204 current IDs** are unique. Every BC ancestor
prefix resolves to its actual ancestor. The renderer validates these frozen
allocations and does not reallocate them during regeneration.

**515 IDs remain explicitly unallocated**: 76 BECs, 137 Features, 147 Functions,
133 Services and 19 Processes require unresolved BEC tier ancestry; three
Candidates lack approved placement. The complete element-by-element list is in
[review-exceptions.json](../../docs/latex-rad/Harmonia-EA-Capability-Map/review-exceptions.json).

No Capability Overview is absent. **Health Information Sharing Programme
Management** has an explicitly constrained account because only its working
name/spelling is established; a full scope would require invented architecture.
The exception register additionally identifies **84 records** with material
description constraints, including unestablished Feature associations, the
Exchange naming conflict, clinical-handover authority conflict, maintenance and
readiness placement, and unresolved Process transitions. The report separates
these from the common structural-ID constraint. Deliberate exclusions remain
exclusions rather than gaps to repair.

## Verification

Final `make` in the publication directory: **PASS**. Its bounded XeLaTeX/latexmk
build, index passes and extended validators established:

- unchanged 18 / 39 / 133 BC tiers, 76 untiered BEC contexts, 137 Features,
  147 Functions, 133 Services, 19 Processes, 14 ECs and three separate Candidates;
- all **719** original Reference IDs and all **2,418** original LaTeX labels
  preserved; one new exception-register label, **2,419** total;
- unchanged architecture relationships, source citations and source fingerprints;
- substantive Capability/Feature overviews, visible typed headings, valid complete
  allocated paths and exact structural-ID exception coverage;
- **719 accepted index entries**, zero rejected entries, zero undefined references,
  duplicate labels, duplicate Reference IDs, duplicate structural IDs, overfull
  boxes, missing glyphs or uncovered nonblank governed source lines;
- compiled output copied to the published PDF with SHA-256
  `bddb2c9b9b47d2c20a65d7a1e172e6f3cb09e1950786d30a13c2dc14f49cfb91`.

Final physical PDF pages **118, 119, 195, 351, 448, 466, 527 and 711** were
rendered and visually inspected: BC explanation and containment, split-table
continuation, BEC identity, mobile-work boundary, assurance Function, EC-14,
exceptions and index. Content is readable; no serious layout regression was
observed. Source transcriptions retain their existing presentation machinery.

Repository-required verification:

```bash
timeout --signal=TERM --kill-after=15s 480s mvn test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

**PASS: 90 tests, 0 failures, 0 errors, 0 skipped; BUILD SUCCESS**, reported
19.182 seconds. No timeout or stall occurred. `git diff --check` and Python
compilation checks also passed. The machine-readable publication results are in
[validation.json](../../docs/latex-rad/Harmonia-EA-Capability-Map/validation.json).

The remaining work is selective review and architectural adjudication of the
explicitly retained issues. Those issues have not been silently repaired by prose,
identifier allocation, implementation inference or a new decomposition.
