Task-Issued-At: UNRESOLVED

Event source: Instruction-entry timestamp unavailable; SA treatment required

# Harmonia — SolutionFacet Refactoring: Final Naming Dispositions

**HCRL:** 1.1.19-001  
**Decision Record:** DR-17  
**Operation:** Governed UPDATE  
**Authority:** Solution Architect

## Goal

Resolve the three outstanding naming qualifications identified in the SolutionFacet additional-corrections Report, applying the Solution Architect's established naming conventions.

## Expected Outcome

The three naming discrepancies are reconciled against their authoritative Composition ancestry.

The resulting canonical definitions and ArchiMate projection consistently distinguish programmatic naming from human-readable presentation.

Produce a validated, uncommitted candidate for Solution Architect inspection.

## Context

The Solution Architect has clarified the existing Harmonia naming conventions.

### 1. canonicalId

The immutable architectural identity of the element.

Neither naming corrections nor changes in containment may cause a new canonicalId to be allocated.

### 2. friendlyName

A dot-separated, containment-qualified name intended to be useful in Java and other programming contexts.

Example:

`ArchitecturalGovernance.ArchitectureConformance.Constraints`

The segments provide a machine-parseable representation of the element's architectural context.

**Composition is authoritative for containment.**

The friendlyName expresses that containment but does not establish or override it.

Do not derive containment from View folders, Groupings, Home relationships or textual naming alone.

### 3. displayName

A locally meaningful, human-readable presentation label.

Normally, this is the element's local name.

However, generic or repeatedly occurring leaf names such as `Constraints`, `Requirements`, `Principles` and `Drivers` may incorporate the immediate parent name to provide useful context.

For example:

`ArchitectureConformance - Constraints`

This avoids presenting numerous indistinguishable elements labelled simply `Constraints`.

The displayName is not required to reproduce the complete friendlyName hierarchy.

### 4. canonicalName

Retain the separately governed semantics of canonicalName.

Do not automatically equate canonicalName with friendlyName or displayName.

## Authorised Naming Dispositions

### A. Information Sovereignty and Residency

**canonicalId:** `f4af2b5206264a6fa4c5040324ad272c`

Current friendlyName:

`InformationSovereigntyAndResidency`

Authored native Name:

`InformationManagement.InformationSovereigntyAndResidency`

Verify the authoritative Composition ancestry.

If it confirms the InformationManagement parent context, update friendlyName to:

`InformationManagement.InformationSovereigntyAndResidency`

Retain the locally meaningful displayName:

`Information Sovereignty and Residency`

If Composition ancestry does not confirm that context, report the discrepancy rather than inventing containment.

### B. User Interface Responsiveness

**canonicalId:** `1f441cdb8fa342cca0afe5b3b65b368d`

Current friendlyName:

`UserInterfaceResponsiveness`

Authored native Name:

`UserExperience.UserInterfaceResponsiveness`

Verify the authoritative Composition ancestry.

If it confirms the UserExperience parent context, update friendlyName to:

`UserExperience.UserInterfaceResponsiveness`

Retain the locally meaningful displayName:

`User Interface Responsiveness`

If Composition ancestry does not confirm that context, report the discrepancy rather than inventing containment.

### C. Architecture Conformance — Constraints

**canonicalId:** `46dd1f4490ae47cc854ce70ff600cb47`

Retain the established friendlyName, subject to Composition verification:

`ArchitecturalGovernance.ArchitectureConformance.Constraints`

Update displayName to:

`ArchitectureConformance - Constraints`

This follows the Solution Architect's convention for generic leaf-node names.

The displayName should identify the immediate parent context without reproducing the complete containment-qualified friendlyName.

Do not change Composition or Home relationships to achieve this naming result.

## Constraints

- Preserve all 1,994 canonical element identities.
- Preserve all 2,236 relationships.
- Preserve all 1,288 authored Views, including layout and presentation.
- Preserve the G01–G18 SolutionFacet hierarchy.
- Preserve the existing native ArchiMate types and specialisations.
- Preserve AX-07, AX-17 and AX-18 as Principle «ArchitecturalAxiom».
- Do not modify historical evidence or previously accepted publications.
- Do not introduce new architectural elements or relationships.
- Do not incorporate OpenSpec backlog candidates.
- Do not resolve unrelated Decision Records.
- Do not infer hierarchy from names where Composition evidence is absent.

### Mandatory Git restriction

**DO NOT COMMIT OR PUSH TO GIT.**

Do not stage changes, create tags, amend commits or rewrite history.

## Authorised Activity / Scope

1. Record these Solution Architect naming dispositions in DR-17.
2. Verify Composition ancestry for the three affected elements.
3. Apply the authorised naming corrections where ancestry confirms the intended context.
4. Preserve historical authored values and provenance.
5. Reconcile affected canonical definitions, metadata, navigation and maintained projections.
6. Regenerate the candidate ArchiMate XML.
7. Execute relevant validation and regression checks.
8. Report any unresolved ancestry or naming discrepancies.

Do not weaken validation or transport checks.

## Report

Write the execution Report and associated evidence under:

`enterprise-architecture/design/evidence/1.1.19/001/after/`

Include:

- Composition ancestry verified for each element.
- Before/after naming values.
- Canonical identity preservation.
- Relationship and View preservation.
- Updated candidate XML path and SHA-256.
- Validation and regression results.
- Any unresolved qualifications.
- Confirmation that no Git operations were performed.

## Completion Boundary

Produce a reconciled, validated, uncommitted candidate for Solution Architect inspection.

Do not close HCRL 1.1.19-001, publish a protected release, allocate a release version, commit or push.

Additional operative instruction: Explicitly preserve the distinction between canonicalName, friendlyName and displayName that we've now clarified.
