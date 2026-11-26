<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# AI-Agent Task Details — BL-0005 Calliope InformationUnit Implementation

**Task-Issued-At:** UNRESOLVED — the attributable system event time for the operative instruction entering this interaction was not supplied at sufficient precision; the task-context clock supplies only local minute precision. No UTC event or filename prefix is inferred.

**Agent:** Junie  
**Repository:** `fhirfactory/harmonia`  
**Branch:** `feature/BL-0005-Calliope-InformationUnit`  
**Source at task start:** commit `a2e68b9f871102c435dca4cc2583ea69d881dfe1`; the working tree already contained a modified Information Unit Draft and an untracked BL-0005 Backlog Item.  
**Software target:** `software-0.1.0` development candidate; HCRL iteration `011`  
**Architecture baseline:** `1.1.26`  
**Backlog Item:** [0005, revision 000.001](../../../../../../../docs/backlog/BacklogItem-0005-Calliope-InformationUnit.v000.001.md)  
**Implementation specification:** [Information Unit Framework Draft 002.004](../../../../../offline-design/DRP-Information-Unit-Framework.Draft.002.004.md)  
**Related accepted decision:** [HCRL software-0.1.0 / 010](../../../../../register/software-0.1.0/hcrl-software-0.1.0-010.complete.md)

## Operative assignment (verbatim)

> Junie — BL-0005 Calliope InformationUnit Implementation
>
> **Goal**  
> Implement the accepted Calliope InformationUnit model for Harmonia software 0.1.0.
>
> Deliver a cohesive, well-documented, defensively implemented and thoroughly tested Java model consistent with the accepted BL-0005 specification.
>
> **Expected Outcome**  
> A standalone Calliope InformationUnit implementation providing:
>
> - Governed IU identity and classification.
> - InformationMetadata and its constituent models.
> - Immutable InformationQuality.
> - InformationRepresentation, ContainerMetadata, ContentMetadata, ContentDetail and Content.
> - Typed InformationUnitRelationship support.
> - Construction, validation and controlled modification consistent with the agreed lifecycle and immutability rules.
> - Comprehensive unit tests, including unhappy paths.
> - Educational documentation explaining model semantics, invariants and responsibilities.
>
> Existing Calliope Naming behaviour must remain intact.
>
> **Context**  
> Repository: fhirfactory/harmonia  
> Branch: feature/BL-0005-Calliope-InformationUnit  
> Software release: 0.1.0  
> Accepted architecture baseline: 1.1.26
>
> Accepted implementation specifications:
>
> - docs/backlog/BacklogItem-0005-Calliope-InformationUnit.v000.001.md
> - enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.Draft.002.004.md
>
> These documents contain the accepted architectural and implementation requirements. Their current repository contents are authoritative for this assignment.
>
> Supporting design guidance:
>
> - docs/design/DesignNote-Calliope-Initial-Model-Architecture.md
> - docs/design/DesignNote-Object-and-Class-Granularity.md
> - docs/design/DesignNote-Object-Immutability-and-Modification-Guidelines.md
> - docs/design/DesignNote-Serialization-and-Format-Selection.md
>
> Mandatory implementation standard: docs/implementation/Harmonia-Implementation-Guide-Philosophy.md
>
> Apply its defensive-programming, educational-documentation and 95/5 testing philosophy. The 95/5 principle is not a numeric code-coverage requirement.
>
> Follow the repository's existing Software Development Praxis and agent instructions.
>
> **Authorised Activity**  
> Implement the complete BL-0005 scope, including the supporting model classes, builders, validation, documentation and tests.
>
> Exercise normal engineering autonomy for Java-specific implementation choices not prescribed by the accepted specification.
>
> Where an actual architectural contradiction prevents implementation, report it explicitly rather than silently redefining the contract.
>
> Test valid construction, invalid input, collection and buffer immutability, controlled updates, relationship semantics, content fidelity, Topic identity/version semantics and Naming regression.
>
> Run at least:
>
> `mvn -B -ntp -pl calliope -am test`
>
> Undertake additional relevant validation required by the existing repository development process.
>
> Do not commit, push, merge or declare a software release accepted without explicit authorisation.
>
> **Report**  
> Produce the normal governed implementation evidence required by Harmonia's Software Development Praxis.
>
> Summarise:
>
> - Implemented classes and public contracts.
> - Significant engineering decisions.
> - Test results and commands executed.
> - Any unresolved issues or qualifications.
> - Deviations from the accepted specification, if any.
>
> **Implementation Guide Conformance**  
> Provide concrete evidence addressing:
>
> - Functional correctness and relevant tests.
> - Unhappy paths, defensive behaviour, negative tests and applicable failure/recovery scenarios.
> - Class and method documentation, including educational clarity for developers unfamiliar with Harmonia.
> - Architectural and security-boundary conformance.
> - Known limitations, untested scenarios and exceptions.
>
> Identify actual test classes and relevant methods. Distinguish verified results from assumptions.
>
> Submit the implementation and evidence for Solution Architect review under the existing Software Development Praxis.
>
> Do not treat implementation completion as architectural or release acceptance.

## Execution boundaries and authority

- The completed HCRL 010 accepts the standalone Naming development candidate but does not establish an accepted software release or authorize a Git transition. This assignment produces a new review candidate only; it cannot accept itself.
- Preserve the pre-existing working-tree changes to the Information Unit Draft and BL-0005 Backlog Item. Do not rewrite, stage, or silently absorb those edits into implementation-only evidence.
- Apply the current repository Architectural Axioms, Architecture Review Praxis, Architecture Change Provenance and Software Development Praxis. Candidate code and evidence do not establish Architecture or software acceptance.
- Architecture questions discovered during implementation, including missing model authority representation, remain explicit review matters. Do not add a field, security policy engine, persistence/cache service, wire codec, or other boundary not authorised by the assignment.
- Do not commit, push, merge, publish, tag, deploy, allocate a release, or declare acceptance.
