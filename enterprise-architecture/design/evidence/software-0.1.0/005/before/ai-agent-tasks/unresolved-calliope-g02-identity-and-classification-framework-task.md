Task-Issued-At: UNRESOLVED

Event source: Codex interaction. The attributable original instruction-entry timestamp was not exposed. The Task event and filename prefix require Solution Architect treatment before closure.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Calliope iteration 005 G02 identity and classification framework

The instruction entered the Codex interaction as the operative G02 assignment and supplies the Solution Architect's adjudications for A03–A05. It authorizes the offline design draft and scoped review evidence. The originating worktree was based on Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`). HCRL.004 separately identifies the reviewed candidate `2fc15febe2a91c811fd48e83f3e7544dc37f8124` and the historical [BL-CALLIOPE-OO-01 revision 1][backlog]. The draft Design Note and Iteration 005 evidence were already present in the working tree. [HCRL.004][hcrl] · [Issue groups][groups] · [G02 reconciliation][g02] · [ConceptFDN/InstanceFDN note][fdn]

## Operative instruction

~~~markdown
# Harmonia — Calliope Review Iteration 005
## G02 — Topic Identity and Classification Framework

### Working context

**Repository:** `fhirfactory/harmonia`  
**Branch:** `feature/BL-CALLIOPE-OO-01`  
**Software baseline:** `Software-Version-0.1.0-Snapshot-004`  
**Architecture Continuum:** 1.1.26

**Cumulative review:** `hcrl-software-0.1.0-004.pending.md`

**Review evidence:**
- `Calliope-Architectural-Issue-Groups-005.md`
- `Calliope-G02-Architectural-Reconciliation-005.md`
- `DesignNote-ConceptFDN-and-InstanceFDN.md`
- `DRP-Topic-and-TopicMask.draft.001.003.md`

### Objective

Document the Solution Architect's G02 adjudications covering A03, A04 and A05.

Create:

`DRP-Calliope-Identity-and-Classification-Framework.draft.000.001.md`

Place the DRP in the established offline-design directory.

This assignment concerns architectural decision documentation, not implementation.

## 1. A03 — Clean-sheet implementation and identity preservation

Record the following decisions:

1. Calliope 0.1.0 is being developed against the accepted architecture, not the existing implementation.
2. Existing implementation code is replaceable.
3. Existing Java constructors, interfaces, factory behaviours and internal serialization formats do not establish architectural authority.
4. Backward compatibility is not required by default.
5. Compatibility obligations must be supported by independently established architectural, contractual or retained-information requirements.
6. Existing Architecture Continuum identities and governed relationships must be preserved.
7. Governed information references must retain their identity and meaning.
8. Provisional runtime UUIDs and disposable implementation representations need not be preserved unless an independent obligation is established.

Do not reopen F-01.1–F-01.81.

## 2. A04 — InformationUnit identity and ConceptFDN

Record the agreed IU model:

- `InformationUnit.id` is an independently assigned, immutable UUID.
- The IU has no InstanceFDN.
- `InformationUnit.informationProfile` is an immutable ConceptFDN.
- Its value is:

  `solution=harmonia.domain=internal.concept=InformationUnit`

- InformationProfile describes the IU's conceptual classification, not its content.
- Its fixed value intentionally preserves architectural provenance independently of Java classes, packages, modules and implementation languages.
- Constructor-default initialization is permissible but not mandated.
- InformationRepresentation, ContainerMetadata, ContentMetadata and Content retain their existing responsibilities.
- Changes to represented information do not change InformationProfile.
- The established IU lifecycle and transformation/provenance rules remain applicable.

### Derived Topic projection

Where a Topic representation is required, a deterministic projection may be derived from:

`InformationUnit.informationProfile + InformationUnit.id`

The projection does not create another authoritative IU identity or introduce an InstanceFDN.

Identify the correspondence required with the existing Continuum IU Topic contract, including the historical `harmonia.mneme.iu.<compressed-uuid>` designation and independently assigned Topic UUID.

Do not silently change the accepted Continuum.

## 3. A05 — Deterministic classification framework

Record the agreed Calliope design philosophy.

**Calliope shall provide a deterministic, strongly defined conceptual modelling framework comprising explicit elements, value sets, value-set generators, validators and documented extension mechanisms.**

The framework shall:

1. Prefer strict, reproducible behaviour over implicit semantic interpretation.
2. Provide predefined representations for common and release-defined concepts.
3. Support explicitly defined domain-specific concepts.
4. Use documented construction and validation rules.
5. Provide deterministic conversion between supported representations.
6. Reject invalid or ambiguous inputs rather than guessing their meaning.
7. Keep domain-specific semantics with their appropriate architectural owners.
8. Preserve externally governed terminology and native code-system semantics.
9. Distinguish extensibility from permissive interpretation.
10. Avoid introducing general-purpose runtime interpretation engines without an independently established requirement.

### Framework responsibilities

Distinguish:

- **Calliope-Models:** Common structures, types, semantic definitions and applicable release-defined value sets.
- **Calliope-DataSets:** Governed runtime dataset content where applicable.
- **Individual components:** Ownership of their domain-specific classifications and operational decisions.
- **External standards:** Authority over their native terminology and semantics.

Use existing architectural ownership definitions where available. Report genuine discrepancies rather than inventing new responsibilities.

## 4. Relationship to Topic/TopicMask

Preserve all 81 settled Topic/TopicMask decisions.

The new DRP must complement, not replace:

`DRP-Topic-and-TopicMask.draft.001.003.md`

Do not introduce competing Topic identity, ordering, versioning, matching or equality semantics.

Keep ConceptFDN, InstanceFDN, UUID identity and Topic projections conceptually distinct.

## 5. Architectural reconciliation

Identify any differences between the agreed G02 decisions and Architecture Continuum 1.1.26.

Separate:

- Genuine architectural conflicts requiring Solution Architect adjudication.
- Existing authority requiring governed incorporation or amendment.
- Documentation corrections.
- Detailed design obligations.
- Implementation and verification obligations.

Do not infer compatibility requirements from existing implementation code alone.

## 6. Review evidence

Update the Iteration 005 G02 review evidence to record:

- A03 — Architectural direction agreed; independently governed reference obligations retained.
- A04 — Conceptual design agreed; Continuum correspondence outstanding.
- A05 — Deterministic classification framework agreed; detailed value-set definitions and ownership mapping remain subject to design.
- G02 — Architectural adjudications documented; DRP acceptance pending.

Preserve the remaining G03–G07 findings and all existing cumulative review obligations.

## 7. Governance constraints

- Do not modify implementation code.
- Do not modify the accepted Architecture Continuum.
- Do not modify accepted DRPs.
- Do not modify HCRL.004.
- Do not create HCRL.005 yet.
- Do not modify the software release identity.
- Do not commit or push.
- Do not introduce additional architectural decisions.
- Follow the established DRP draft naming convention.

Use targeted repository inspection and bounded excerpts. Avoid unnecessary re-analysis of disposable implementation code.

## Deliverables

1. `DRP-Calliope-Identity-and-Classification-Framework.draft.000.001.md`
2. Updated Iteration 005 G02 reconciliation evidence.
3. Concise report identifying remaining architectural discrepancies and design obligations.

**Stop for Solution Architect review.**
~~~

[backlog]: ../../../../../../../docs/backlog/calliope-oo-01-topic-models.md
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-1-whole-of-calliope-scope-and-candidate
[groups]: ../../after/Calliope-Architectural-Issue-Groups-005.md
[g02]: ../../after/Calliope-G02-Architectural-Reconciliation-005.md
[fdn]: ../../../../../../../docs/design/DesignNote-ConceptFDN-and-InstanceFDN.md#72-informationunit
