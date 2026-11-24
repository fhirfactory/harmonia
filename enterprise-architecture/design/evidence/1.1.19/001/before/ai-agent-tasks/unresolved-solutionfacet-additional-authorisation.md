Task-Issued-At: UNRESOLVED

Event source: Instruction-entry timestamp unavailable; SA treatment required

# Harmonia — SolutionFacet Refactoring: Additional Authorisation

**HCRL:** 1.1.19-001  
**Decision Record:** DR-17  
**Operation:** Governed UPDATE  
**Authority:** Solution Architect

## Goal

Complete the outstanding architectural classification and naming corrections identified during the SolutionFacet reconciliation.

## Expected Outcome

The reconciled candidate includes the Solution Architect's authorised ArchitecturalAxiom specialisations and corrected naming inconsistencies, with all existing identities and architectural semantics preserved.

Produce an updated, validated ArchiMate candidate for Solution Architect inspection.

## Context

The Solution Architect has reviewed the SolutionFacet reconciliation Report and explicitly resolved two outstanding matters.

### Decision 1 — ArchitecturalAxiom specialisations

**APPROVED.**

The authored changes to the following existing Principles are intentional:

- AX-07
- AX-17
- AX-18

These elements are ArchitecturalAxioms and should carry the corresponding `ArchitecturalAxiom` specialisation.

The omission of these specialisations from the existing canonical model was an inconsistency.

Incorporate the authored specialisations and associated native naming changes.

Preserve the existing canonical identities, native ArchiMate Principle types, relationships, definitions, provenance and architectural semantics.

Do not create replacement elements or reinterpret the axioms.

### Decision 2 — Naming corrections

**APPROVED WITH BOUNDARIES.**

Codex is authorised to correct obvious spelling, punctuation and typographical inconsistencies identified during the reconciliation.

Examples include:

- `PerformancAndCapacity` → `PerformanceAndCapacity`
- `ReslienceAndRecovery` → `ResilienceAndRecovery`
- `ArchitectureGovernance` → `ArchitecturalGovernance`, where the established canonical context confirms the intended name.

Apply corrections consistently across affected current architectural naming fields, metadata and maintained presentation representations.

Preserve canonical identities and relationship semantics.

Do not alter historical evidence merely to modernise its terminology or spelling.

Where a discrepancy could indicate a genuine difference in architectural meaning, hierarchy or naming convention, retain it as an unresolved qualification for Solution Architect review.

Do not invent new naming conventions.

## Constraints

- Preserve all 1,994 existing canonical element identities.
- Preserve the 2,236 reconciled relationships.
- Preserve all 1,288 authored Views.
- Preserve the G01–G18 SolutionFacet hierarchy.
- Preserve existing Composition and Home semantics.
- Do not incorporate the 784 OpenSpec backlog candidates.
- Do not resolve unrelated Decision Records.
- Do not change protected architectural publications.

### Mandatory Git restriction

**DO NOT COMMIT OR PUSH TO GIT.**

Do not stage files, create tags, amend commits, rewrite history or perform equivalent Git operations.

Leave all changes in the working tree.

## Authorised Activity / Scope

1. Record both Solution Architect dispositions in DR-17.
2. Incorporate the three ArchitecturalAxiom specialisations.
3. Correct unambiguous naming inconsistencies.
4. Reconcile the affected canonical definitions and maintained ArchiMate presentation.
5. Preserve the original authored export and prior verification evidence.
6. Regenerate the candidate XML.
7. Execute the relevant validation and regression tests.
8. Report any remaining naming ambiguities separately.

Do not weaken validation or transport checks to obtain a passing result.

## Report

Retain the updated Report and verification evidence under:

`enterprise-architecture/design/evidence/1.1.19/001/after/`

Include:

- ArchitecturalAxiom corrections applied.
- Naming corrections applied.
- Any unresolved naming discrepancies.
- Canonical identity and relationship preservation results.
- View preservation results.
- Updated candidate XML path and SHA-256.
- Validation and test results.
- Confirmation that no Git commit or push occurred.

## Completion Boundary

Produce a reconciled, validated, uncommitted candidate ready for Solution Architect inspection.

**Do not close HCRL 1.1.19-001, publish a protected release, commit or push.**
