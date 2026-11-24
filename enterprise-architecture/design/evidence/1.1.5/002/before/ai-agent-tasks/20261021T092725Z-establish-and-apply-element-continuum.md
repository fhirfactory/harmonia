<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Establish and apply the RAD-1.1.5 element continuum

Task-Issued-At: 2026-10-21T09:27:25.083Z

Event meaning: the operative Solution Architect instruction entering this agent interaction.
Attributable source: user-role response_item in Codex thread `01a15dc5-c274-76c1-aacb-35ce618c1b05`, timestamp `2026-10-21T09:27:25.083Z`. The issued wording below is retained directly from that record; the local interaction log is not a continuing dependency.
Issuer: Mark Hunter, Solution Architect. Agent: Codex.
HCRL: [1.1.5-002](../../../../../review/1.1.5/hcrl-1.1.5-002.pending.md).
Entry HEAD: `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; branch `ArchitecturalGovernance`; root release `1.1.4`, predecessor `1.1.3`.
Entry working tree: existing untracked `design/review/1.1.5/` and `design/evidence/1.1.5/001/` are user-supplied input, not changes created by this task.

## Operative issued instruction — verbatim

Goal
Establish and apply the Harmonia Enterprise Architecture Continuum representation strategy for RAD 1.1.5.
The current hcrl-1.1.5-001.pending.md is rejected in its entirety by the Solution Architect.
The rejection is not because its investigation was defective. It is because its migration strategy is based on the wrong architectural premise: that existing RAD documents should generally remain architectural carriers and be relocated or classified within the new Enterprise Architecture framework.
That is not the target architecture.
The Harmonia Enterprise Architecture continuum is to consist of individually represented architectural elements, classified according to the governed ArchiMate metamodel and Harmonia specialisations of ArchiMate concepts.
RAD 1.1.5 is to establish the reference continuum structure, canonical element Markdown representations, Praxis and supporting governance/machinery required for that model, and then apply that model to migrate the existing RAD architectural content into the continuum.
The resulting migrated Enterprise Architecture will then be reviewed holistically by the Solution Architect and ARB.
Expected Outcome
Establish a deterministic Enterprise Architecture continuum beneath:
enterprise-architecture/continuum/

The continuum SHALL provide well-defined locations for the ArchiMate element types used by Harmonia and for governed Harmonia specialisations of those element types.
Every architectural element within the continuum SHALL have an individually addressable canonical Markdown representation.
For example, given:
Principle ABC

the governing machinery and Praxis should make it deterministic that:
1. Principle ABC is classified as an ArchiMate Principle;
2. the governed Principle representation/template is used;
3. the required architectural metadata and content are populated;
4. its governed unique identity is allocated or preserved;
5. its provenance is recorded;
6. the element is placed in the appropriate location within the continuum; and
7. the resulting representation participates correctly in Harmonia governance and ArchiMate projection/round-trip mechanisms.
Conceptually this may result in structures such as:
enterprise-architecture/
└── continuum/
    ├── motivation/
    │   ├── stakeholder/
    │   ├── driver/
    │   ├── assessment/
    │   ├── goal/
    │   ├── outcome/
    │   ├── principle/
    │   ├── requirement/
    │   └── constraint/
    ├── strategy/
    │   ├── resource/
    │   ├── capability/
    │   ├── value-stream/
    │   └── course-of-action/
    ├── business/
    │   └── ...
    ├── application/
    │   └── ...
    └── technology/
        └── ...

This structure is illustrative, not prescribed. Derive the actual reference structure from the governed ArchiMate/Harmonia metamodel.
Once that structure and its governing mechanism are established, use them to ingest the architectural content of the existing RAD into the Enterprise Architecture continuum.
The migration is therefore an architectural transformation:
RAD source material
    ↓
identify architectural content
    ↓
classify against governed ArchiMate/Harmonia metamodel
    ↓
identify or allocate canonical identity
    ↓
materialise individual canonical architectural elements
    ↓
establish provenance
    ↓
place in governed continuum
    ↓
validate

It is not:
RAD document
    ↓
choose a better directory
    ↓
move RAD document

Context
Harmonia already has governed architecture covering:
- the ArchiMate-based architecture metamodel;
- Harmonia specialisations of ArchiMate concepts;
- canonical identity and naming;
- architectural standing;
- provenance;
- Architecture Review and HCRL governance;
- ArchiMate projection and round-trip mechanisms; and
- existing repository governance.
Use those authorities.
Do not establish another architecture taxonomy merely to organise Markdown.
The ArchiMate metamodel provides the primary classification framework. Harmonia specialisations extend those concepts where already governed.
The architectural unit represented within the continuum is an architectural element, not an arbitrary historical document containing multiple architectural assertions.
For example:
enterprise-architecture/continuum/motivation/requirement/<one file per Requirement>

represents Requirements individually.
The same principle applies across the continuum.
A Capability is individually represented.
A Principle is individually represented.
A Constraint is individually represented.
A Business Object is individually represented.
A Harmonia Feature is individually represented using its governed specialisation of ArchiMate Capability.
A Harmonia Topic is individually represented using its governed specialisation of its applicable ArchiMate concept.
Existing RAD Markdown documents are therefore source material for ingestion, not necessarily future architectural carriers.
Where a RAD source contains multiple architectural elements, extract those elements and represent them individually.
Where several RAD sources contribute to the same governed architectural element, reconcile those contributions against the existing identity and authority rules rather than creating duplicate elements merely because multiple source documents exist.
Preserve provenance sufficient to trace the resulting architecture back to its source material and relevant architectural standing.
Once architectural content has been successfully incorporated and its provenance established, the historical RAD carrier should be retired from the active architecture rather than retained merely because it once carried architectural information.
Constraints
Architecture drives the machinery. Machinery SHALL NOT constrain the architecture.
Use existing governed Harmonia architecture, ArchiMate semantics, identity rules, naming rules, specialisations, standing and provenance mechanisms wherever applicable.
Do not invent architectural element types to accommodate existing documentation.
Do not infer architectural classification from the current RAD directory structure.
Do not create miscellaneous, composite or multi-concept continuum document categories simply because an existing Markdown document contains multiple concepts.
Do not preserve a large Markdown document as an active architectural carrier merely because decomposition is difficult.
Do not infer identity from names, filenames, directory position, lexical similarity or document co-location.
Do not create duplicate architectural elements merely because the same architectural concept appears in multiple source documents.
Do not silently promote Proposed, Candidate, historical, superseded or otherwise qualified architectural assertions to Established architecture through migration.
Do not silently discard those assertions either.
Preserve their governed standing and provenance.
Do not treat successful parsing, generation, validation or ArchiMate projection as architectural approval.
Do not rewrite historical evidence to make it appear that this representation strategy existed when earlier releases were created.
Where the existing machinery cannot support the required element-oriented continuum, establish the necessary bounded extension.
Where a genuine architectural ambiguity cannot be resolved from existing governed architecture and source evidence, do not invent the missing architectural decision.
Record the unresolved matter for subsequent Solution Architect/ARB review and continue migrating material that can be safely resolved.
Do not allow isolated uncertainty to halt the entire migration unnecessarily.
Authorised Activity / Scope
First, establish the canonical continuum representation.
Determine the actual continuum directory structure required by the ArchiMate element types used by Harmonia and their governed Harmonia specialisations.
Establish the canonical Markdown representation/template set for those architectural elements.
Determine whether a common base representation with type-specific constraints is appropriate, whether distinct element templates are required, or whether a combination provides the cleanest governed mechanism.
The result must be deterministic and understandable to both humans and authorised AI agents.
Establish how Harmonia specialisations extend or constrain their underlying ArchiMate element representation without creating a parallel metamodel.
Establish the Praxis by which an architectural element is:
identify
→ classify
→ select representation/template
→ allocate or preserve identity
→ populate architectural content
→ establish provenance and standing
→ place
→ validate

Configure or extend the governing machinery as necessary so that this behaviour is repeatable.
A future instruction such as:
Create Principle ABC ...

should be sufficient for an authorised agent, given the architectural content, to know which governed representation to use, what identity/provenance rules apply, and where the resulting element belongs.
Likewise:
Create Feature XYZ ...

must deterministically result in a Harmonia Feature represented as the governed specialisation of ArchiMate Capability rather than an unrelated document model.
Establish appropriate validation so that malformed, incorrectly located, incorrectly typed or inadequately identified continuum elements can be detected.
Then apply the resulting strategy to the existing RAD architectural corpus.
Extract the architectural content represented by the RAD.
Classify that content against the governed ArchiMate/Harmonia metamodel.
Materialise the resulting architectural elements individually within the continuum.
Preserve existing canonical identity where established.
Allocate identity through the governed mechanism where an element legitimately requires identity and none has previously been established.
Preserve architectural standing.
Establish provenance from resulting elements to their source material.
Reconcile duplicate or overlapping source assertions where existing governed evidence is sufficient to establish that they concern the same architectural element.
Preserve unresolved semantic conflicts explicitly rather than selecting whichever source happens to be newest or most conveniently located.
Retire successfully ingested RAD documents from the active architectural corpus according to the appropriate governed historical/provenance mechanism.
Do not retain source documents inside the continuum merely as an easier substitute for proper ingestion.
Where architectural content cannot safely be classified, reconciled or assigned identity without additional architectural authority, preserve its source and provenance, record the unresolved issue, and continue with the remainder of the migration.
Apply corresponding changes to governance, Praxis, validation and supporting machinery required to make the resulting architecture stable, reproducible and shareable.
Ensure the resulting representation remains compatible with the role of ArchiMate as Harmonia's architectural projection/workbench and with the established round-trip strategy.
Treat the associated ArchiMate workspace and LaTeX publication environment according to their actual roles in the resulting architecture. Do not ingest duplicated publication text as additional architectural elements merely because it exists in LaTeX, and do not treat ArchiMate interchange/workspace artefacts as equivalent to RAD source documents.
Treatment of hcrl-1.1.5-001
Record the Solution Architect disposition that hcrl-1.1.5-001.pending.md is rejected as a whole.
Do not individually adjudicate DR-1 through DR-13 as though their proposed document-carrier migration model remains operative.
Preserve the rejected review and its supporting evidence according to the established Architecture Review Praxis.
Its investigation remains potentially useful evidence.
Its proposed document-carrier migration strategy is not accepted architecture.
Proceed through the next governed 1.1.5 iteration for this revised architecture and migration strategy.
Evaluation Principle
Do not attempt to prove the complete strategy through increasingly detailed hypothetical analysis before applying it.
The migrated architecture itself is the principal validation of the strategy.
Use the real RAD corpus to exercise the continuum representation, templates, classification rules, provenance model, identity machinery and validation.
Where application exposes a defect in the strategy or machinery, correct it coherently within the authorised scope where the architectural answer is already established.
Where it exposes a genuinely unresolved architectural decision, preserve the issue for Solution Architect/ARB adjudication rather than inventing an answer.
The objective is to emerge with a real migrated Enterprise Architecture that can be evaluated as a whole, not another proposed migration design awaiting another round of theoretical analysis.
Report
Produce the formal governed Task/Report evidence required by the current Architecture Review Praxis.
Report:
- the resulting continuum directory structure;
- the canonical element Markdown representation/template strategy;
- supported ArchiMate element types;
- Harmonia specialisation treatment;
- identity and naming treatment;
- provenance and standing treatment;
- incorporation/ingestion Praxis;
- governance and machinery changed or established;
- validation established;
- RAD source corpus processed;
- resulting architectural element counts by type and standing;
- identity preservation/allocation results;
- provenance coverage;
- duplicate/overlapping source reconciliation;
- source carriers successfully retired;
- source material retained because migration could not safely be completed;
- unresolved architectural questions;
- ArchiMate integration/round-trip implications and validation results;
- LaTeX/publication implications;
- reproducibility results; and
- any exceptions or limitations preventing the resulting continuum from being considered stable, reproducible and shareable.
Do not stop merely because migration exposes further design questions.
Continue where the answer can be derived from existing governed architecture and evidence.
Stop or defer an affected item only where proceeding would require inventing architectural authority or risk corrupting governed architectural meaning.
[@hcrl-1.1.5-001.pending.md](file:///home/hunterm/Development/Code/harmonia/enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-001.pending.md)
