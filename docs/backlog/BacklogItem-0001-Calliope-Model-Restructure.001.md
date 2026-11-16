<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Backlog Item: BacklogItem-0001-Calliope-Model-Restructure.OO1

**Backlog Item ID:** 00001
**Backlog Item Revision:** 001  
**Target software release:** 0.1.0  
**Accepted Architecture Continuum:** 1.1.26

Implementation authority, software acceptance and release tagging remain separate under the Software Development Praxis.

## Overview
### Context

The existing Calliope model is poorly structured with many of the classes not conforming to identified best-practices
associated with file length, Object Oriented encapsulation and the use of factories, value-sets and data elements.

This Backlog-Item addresses the identified problems in the Calliope Java model.

### Activity / Work

The effort required to restructure the Calliope model will be significant, as it involves reorganizing classes, separating responsibilities, and improving the overall structure of the model. The following tasks will be involved:

1. Review and analyze the existing Calliope model to identify areas that need restructuring.
2. Identify the appropriate package hierarchy and functional/object-oriented containment model for the Calliope model.
3. Move classes with mixed responsibilities to separate collaborating objects.
4. Refactor source files to reduce their length and improve readability.
5. Organize data elements, value sets, and factories in a consistent manner.
6. Update historical implementation concepts to reflect the accepted Architecture Continuum.

### Outcome 

The restructuring of the Calliope model will result in improved organization, reduced complexity, and enhanced maintainability. The following outcomes are expected:

1. Classes with mixed responsibilities will be separated into separate collaborating objects, leading to improved encapsulation and reduced coupling.
2. Source files will be refactored to reduce their length and improve readability, making the codebase easier to navigate and understand.
3. Data elements, value sets, and factories will be organized in a consistent manner, improving the overall structure and reducing redundancy.

## Context — identified engineering problems

The **Calliope Java model as a whole** is poorly structured. Its package hierarchy is inconsistent with Harmonia's preferred functional/object-oriented containment model, and many classes mix responsibilities that belong in separate collaborating objects. Source files are excessively long, supporting data elements, value sets and factories are not consistently organised, and some historical implementation concepts do not reflect the accepted Architecture Continuum.

The problems have already been identified. Examples include:

- `Topic` and `TopicMask` in `model.topic`: wrong package organisation, mixed responsibilities and oversized source files (historically 736 and 672 physical lines). The associated enums, constituent types, factories and matching behaviour need proper homes.
- `Topic` retains `source`, `target`, `origin`, `destination` and `receivedDate`, even though those are routing/operational concerns rather than properties of Topic identity and naming. `TopicSubscription` and its consumers also warrant examination.
- Other large classes, including `ErgonPayload`, `PraxisDefinition`, `Pragma`, `InformationUnit`, `PersistenceOperationEnvelope`, `OperationalMetadata`, `ContainerMetadata` and `PragmaFhirConverter`, require appropriate OO decomposition and package review.
- Equivalent structural problems may exist elsewhere in Calliope, including files that happen to be shorter than 500 lines.

**The existing code is the problem to be corrected, not a package structure or API to preserve.** The accepted architecture and the Implementation Guide already tell us the expected direction; this assignment does not ask Junie to rediscover our conclusions.

## Required engineering work

**Restructure the entire Calliope model**, rather than merely cleaning up `Topic` and `TopicMask`.

1. **Reorganise packages around cohesive information concepts.** Apply Implementation Guide §7.2 throughout Calliope. As a concrete example, the naming model should be organised under a package such as `net.fhirfactory.harmonia.model.core.naming`, with appropriate `datatypes`, `valuesets`, `factory` and other justified supporting packages. Apply equivalent reasoning to all model areas. The example is an engineering direction, not a requirement to create empty packages or mechanically copy a template.
2. **Perform proper object-oriented decomposition.** Extract data elements, enumerations/value sets, factories, conversions and independently meaningful behaviours from multifunctional classes. Keep model objects focused on their own state, invariants and domain responsibilities. Prefer cohesive collaboration and encapsulation to giant classes, arbitrary static utility collections, hollow compatibility facades or speculative inheritance.
3. **Remove misplaced responsibilities.** In particular, make `Topic` a model of the accepted naming/identity concept, not a routing envelope. Trace legacy contextual properties into their actual callers and move necessary information to its correct owner. `InformationCoordinates`, `InformationItinerary`, message/transport and operational models may be relevant; do not force a field-for-field migration based only on similar names. Apply the same architectural-fidelity test elsewhere in Calliope.
4. **Correct known implementation defects.** In particular, reject duplicate Topic sequence positions without overwriting previous entries or corrupting builder state. Preserve the accepted Topic and TopicMask identity, immutability, sequencing, naming and matching semantics; apply equally appropriate validation and defensive behaviour across the restructured model.
5. **Migrate affected consumers.** Update dependent Calliope code and other Harmonia modules, imports, tests and documentation as required. We have **no accepted software release whose historical Java packages, constructors, nested classes or getters must be preserved**. Do, however, identify and deliberately protect genuine accepted information, persistence, interchange, identity and external integration contracts; do not silently change their semantics.
6. **Apply the Implementation Guide in full.** Use the preferred OO organisation, educational class and public/protected-operation documentation, 500-physical-line production source-file rule and meaningful normal/unhappy-path tests. A file under 500 lines is not automatically a good design; a large class is not fixed by arbitrarily splitting it.

The implementation agent should use sound engineering judgement for the detailed class design, migration sequence and test plan. The Solution Architect's known findings are instructions to act on, **not a research task to repeat**. If a specific Solution Architect-authored exemplar is supplied or identified, use it to understand the desired quality, not as a rigid class template.

## Expected outcome

A cohesive, properly packaged and educational **whole-of-Calliope Java model** consistent with the accepted Architecture Continuum and the Harmonia Implementation Guide, with affected consumers migrated and the resulting integrated Harmonia candidate verified.

The outcome is not achieved by improving only a selected pair of classes. Junie may execute the restructuring in manageable stages, but this Backlog Item remains open until the **whole model** has been reviewed and the necessary restructuring completed.

This is an engineering-model restructuring, **not authority to invent new Harmonia capabilities, change accepted architecture, or redesign unrelated runtime services**. Genuine architecture conflicts or new requirements go to the Solution Architect for disposition; ordinary class, package and consumer changes within this assignment do not need fresh architectural authorisation.

## Acceptance criteria

| ID | Required result | Evidence |
| --- | --- | --- |
| **AC-1 — Whole-model package structure** | All Calliope model areas follow a coherent, concept-oriented package hierarchy with justified `datatypes`, `valuesets`, `factory` and other supporting responsibilities. The old package layout is not retained merely for compatibility. | Resulting package tree and concise responsibility mapping. |
| **AC-2 — Object-oriented quality** | Classes are cohesive, appropriately sized, encapsulated and documented. The known oversized/multifunctional examples and equivalent deficiencies throughout Calliope are resolved, with no artificial fragmentation. | Source/design review, before/after inventory, Implementation Guide conformance and any explicitly accepted exceptions. |
| **AC-3 — Architecture and information contracts** | Model semantics and ownership conform to the accepted Continuum. In particular, Topic no longer carries routing/operational metadata, and accepted identity, naming and matching behaviours remain correct. Genuine persisted/external contracts are deliberately handled. | Contract tests, architectural review and documented migration choices. |
| **AC-4 — Consumer migration and robustness** | Affected Harmonia consumers use the new model correctly. Known defects and meaningful normal, invalid, boundary and defensive scenarios are tested. Obsolete Java compatibility surfaces are not retained without justification. | Consumer diffs, module tests, representative serialisation/migration tests and unhappy-path evidence. |
| **AC-5 — Integrated, reviewable candidate** | The exact candidate has attributable component, architecture, regression and relevant integration verification; incomplete or failed checks are explicitly reported rather than presented as passes. A formal Implementation Report/HCRL supplies the evidence for Solution Architect acceptance. | Actual test commands/results, module and scenario coverage, candidate identity, limitations and Software Development Praxis provenance. |

## References

- [Calliope-Models](../../enterprise-architecture/continuum/application/application-component/307b3440a507403f8bc214d290bffb6a.md)
- [Topic](../../enterprise-architecture/continuum/application/data-object/9e2ffa9b4a1d4422a33c2a4bbcfc86ef.md) and [TopicMask](../../enterprise-architecture/continuum/application/data-object/bb489157a6bf48be926c5a601bb0a6c3.md)
- [Harmonia Implementation Guide](../implementation/Harmonia-Implementation-Guide-Philosophy.md), particularly §§4–5 and 7
- [Software Development Praxis](../../enterprise-architecture/governance/software-development-praxis.md)
- [Earlier engineering assessment](../../enterprise-architecture/design/evidence/1.1.27/001/after/engineering-assessment.md) — evidence of known engineering deficiencies, not authority for the old narrow scope.

**Standing:** Proposed revision 2. The Solution Architect must accept it before a corresponding implementation Task is issued.
