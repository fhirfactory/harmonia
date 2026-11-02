Task-Issued-At: UNRESOLVED

Event source: Instruction-entry timestamp unavailable; SA treatment required

# Harmonia — SolutionFacet Architectural Refactoring

**Release:** 1.1.19  
**HCRL:** 1.1.19-001  
**Operation:** Governed UPDATE  
**Authority:** Solution Architect  
**Scope:** Subject → SolutionFacet architectural refactoring

## 1. Goal

Reconcile the Solution Architect's updated ArchiMate model into the canonical Harmonia Architecture Continuum.

Replace the existing architectural classification concept `Subject` with `SolutionFacet`, represented as a specialisation of the native ArchiMate `Grouping` element.

This is a terminology, classification and model-consistency refactoring.

It is **not** an architectural redesign, a new taxonomy, or an ADD operation.

## 2. Expected Outcome

The Architecture Continuum consistently recognises `SolutionFacet` as the architectural specialisation previously represented by `Subject`.

The resulting architecture must:

1. Preserve existing canonical identities.
2. Preserve the established G01–G18 hierarchy.
3. Preserve existing Composition and Home relationships.
4. Recognise `SolutionFacet` as a specialisation of `Grouping`.
5. Reconcile architectural names, documentation, metadata and navigation.
6. Preserve historical provenance and accepted architectural decisions.
7. Maintain the existing distinction between the Solution-Facet taxonomy and the Capability Map.
8. Produce a validated ArchiMate projection for Solution Architect review.

The operation must not introduce unrelated architectural changes.

## 3. Context

### 3.1 Architectural authority

The canonical architecture resides under:

`enterprise-architecture/continuum/`

The governing architectural representations are:

- **Markdown Continuum:** Authoritative architectural definitions.
- **ArchiMate model:** Governed architectural workbench and authored change input.
- **Open Exchange XML:** Interchange representation.
- **LaTeX:** Publication representation.

The Solution Architect has modified the ArchiMate model to establish `SolutionFacet` as a specialisation of `Grouping`.

The updated model is the authored candidate for reconciliation.

Locate the supplied export through the established ArchiMate ingest/evidence workflow. If its location or identity is ambiguous, request clarification before proceeding.

Do not use an earlier published projection as a substitute for the newly authored model.

### 3.2 Architectural definition

**SolutionFacet**

A named aspect of a solution, expressed in accessible terminology and organised within a hierarchical taxonomy to support the classification, contextualisation and navigation of solution-related information.

A SolutionFacet:

- Provides a plain-English organisation of solution information.
- Supports hierarchical classification and navigation.
- Does not represent an architectural capability.
- Does not imply implementation responsibility or ownership.
- Does not inherently establish an architectural concern, requirement or constraint.
- May provide context for architectural elements of different types.

The Solution-Facet taxonomy and Capability Map are complementary but distinct.

**Solution-Facet:** What aspects of the solution are we describing?

**Capability:** What must the solution be capable of doing?

The Capability Map may become progressively more technical as the architecture develops. The Solution-Facet taxonomy should remain accessible to a broad audience.

### 3.3 Existing model

The current Continuum contains the accepted G01–G18 classification hierarchy.

The Solution Architect has updated the corresponding ArchiMate Groupings to use the `SolutionFacet` specialisation.

For example:

```text
ArchiMate Type: Grouping
Specialisation: SolutionFacet

friendlyName:
ArchitecturalGovernance.ArchitectureConformance

displayName:
Architecture Conformance
```

The existing canonical identity, architectural meaning and relationships must be preserved.

The authored model may still contain generated metadata or documentation referring to `Subject`. These are reconciliation candidates, not evidence that the new specialisation should be rejected.

## 4. Constraints

### 4.1 Identity preservation

**Do not replace existing architectural elements with new elements merely to change their specialisation or terminology.**

Preserve:

- canonicalId
- Existing architectural identities
- Established hierarchy
- Existing relationships
- Historical provenance
- Accepted architectural decisions

Do not generate replacement UUIDs for existing Solution-Facet elements.

### 4.2 Relationship semantics

Preserve the existing architectural relationship semantics.

**Composition**

Represents explicit containment within the classification hierarchy.

**Home Association**

Represents the separately established authoritative Home relationship.

Composition does not automatically imply Home.

Do not infer additional Home relationships or alter existing relationship directions as part of this refactoring.

### 4.3 Terminology reconciliation

Replace references to the former architectural `Subject` classification where they clearly refer to the renamed concept.

Do not blindly replace every occurrence of the English word *subject*.

Distinguish between:

- References to the former architectural classification.
- Ordinary English usage.
- Historical evidence and provenance.
- Unrelated architectural terminology.

Preserve historical records in their original context.

### 4.4 Naming

Preserve established naming and identity conventions.

Do not introduce additional naming rules or restructure the hierarchy.

Where the authored model changes a specialisation or architectural name, reconcile it against the canonical definition while preserving identity.

Do not assume that `SolutionFacet` must be inserted into the canonical or friendly name of every element.

### 4.5 Architectural scope

Do not:

- Introduce new Solution-Facets.
- Redesign G01–G18.
- Reclassify unrelated architectural elements.
- Introduce new capabilities or features.
- Resolve outstanding Decision Records unrelated to this refactoring.
- Incorporate the 784 outstanding OpenSpec candidates.
- Modify protected historical releases.
- Change architectural relationship semantics.

Report any unexpected architectural changes found in the authored model rather than incorporating them automatically.

### 4.6 Git restrictions — mandatory

**DO NOT COMMIT OR PUSH TO GIT.**

This prohibition applies to the entire activity.

Do not:

- Execute `git commit` or `git push`.
- Amend or rewrite Git history.
- Create or move Git tags.
- Automatically commit as part of publication or governance procedures.
- Stage unrelated changes.

Leave all authorised modifications in the working tree for Solution Architect review.

If a governance procedure requires a commit or push, stop at that boundary and report the outstanding step.

**No Git commit or push is authorised by this instruction.**

## 5. Authorised Activity / Scope

### 5.1 Inspect the authored model

Identify the Solution Architect's updated ArchiMate export.

Compare its SolutionFacet definitions and related changes against the current canonical Continuum.

Establish the precise scope of the refactoring.

Identify unexpected differences separately.

Do not treat unrelated differences as authorised changes.

### 5.2 Reconcile the metamodel

Establish `SolutionFacet` as the recognised specialisation of the native ArchiMate `Grouping` type.

Update the relevant canonical definitions and specialisation metadata.

Retain the established architectural identity and hierarchy.

Ensure the metamodel distinguishes Solution-Facets from Capabilities and other architectural constructs.

### 5.3 Reconcile canonical elements

For each affected existing Grouping:

- Preserve its canonical identity.
- Reconcile its specialisation.
- Preserve its architectural meaning.
- Reconcile its documentation where appropriate.
- Preserve its established relationships.
- Retain its provenance.

Do not create replacement elements.

### 5.4 Reconcile navigation and presentation

Update relevant generated or maintained navigation structures to reflect the Solution-Facet terminology.

This may include:

- Architecture Continuum navigation.
- ArchiMate View and folder terminology.
- Classification metadata.
- Reader-facing documentation.
- Generated architectural catalogues.
- Projection templates.

Preserve View identities, layout, geometry, styles, references and established presentation structure.

Do not remove Views or reorganise the taxonomy beyond the authored changes.

### 5.5 Reconcile tooling

Inspect the architecture tooling for assumptions that depend on the former `Subject` terminology.

Update the relevant logic where required to support `SolutionFacet`.

Preserve existing round-trip, identity, publication and validation behaviour.

Avoid unnecessary tooling refactoring.

Where an internal identifier must remain unchanged for compatibility, document the distinction between that identifier and the new architectural terminology.

### 5.6 Generate verification projection

Use the established governed projection machinery to generate a candidate ArchiMate Open Exchange XML.

The candidate must be suitable for import into Archi for Solution Architect inspection.

Preserve the complete accepted presentation, including all 1,285 Views, subject to verification against the actual authored input.

Do not replace the protected published architecture during this activity.

### 5.7 Validate

Execute the relevant architecture and tooling validation procedures.

Verify:

1. Canonical identity preservation.
2. Element and relationship preservation.
3. G01–G18 hierarchy preservation.
4. Composition/Home semantics.
5. SolutionFacet specialisation consistency.
6. View and presentation preservation.
7. Navigation and documentation consistency.
8. Deterministic projection generation.
9. XML schema validity.
10. Absence of unauthorised architectural additions or deletions.

Record all qualifications and unresolved differences.

## 6. Report

Use the established governed evidence conventions.

Retain the Task and authored input under:

`enterprise-architecture/design/evidence/1.1.19/001/before/`

Retain the execution Report and verification evidence under:

`enterprise-architecture/design/evidence/1.1.19/001/after/`

The Report must include:

### 6.1 Architectural reconciliation

- Number of affected elements.
- Number of specialisations updated.
- Canonical identities preserved.
- Relationship preservation results.
- Unexpected authored differences.
- Outstanding reconciliation questions.

### 6.2 Documentation and navigation

- Terminology changes.
- Navigation changes.
- View/folder changes.
- Historical references deliberately preserved.
- Remaining legacy terminology requiring review.

### 6.3 Tooling

- Tooling changes performed.
- Compatibility considerations.
- Validation results.
- Any unresolved round-trip issues.

### 6.4 Verification projection

- Candidate XML location.
- Element count.
- Relationship count.
- View count.
- Schema validation results.
- Identity and reference validation results.
- SHA-256 checksum.

### 6.5 Governance and Git

- HCRL status.
- Modified files.
- Outstanding review obligations.
- Confirmation that no commit or push occurred.

## 7. Completion Boundary

The authorised outcome is a **reconciled, validated, uncommitted architectural candidate** ready for Solution Architect inspection.

Do not:

- Declare the refactoring accepted.
- Close HCRL 1.1.19-001.
- Transition to another release.
- Publish a new protected baseline.
- Incorporate backlog candidates.
- Commit or push to Git.

Await explicit Solution Architect review and acceptance before proceeding beyond this boundary.
