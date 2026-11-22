Task-Issued-At: UNRESOLVED

Event source: Codex interaction. The attributable original instruction-entry timestamp is unavailable. The Task event and filename prefix require Solution Architect treatment before closure.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Calliope iteration 005 A04 InformationProfile semantics and IU identity

The user issued the Solution Architect's follow-up clarification in the Codex interaction. The task applies to the working-tree draft created by the preceding A04 update and to the existing Iteration 005 G02 evidence. The preceding Task and Report retain their original content and standing. [Prior report][prior] · [Design Note][note] · [G02 evidence][g02]

The originating branch is `feature/BL-CALLIOPE-OO-01`, HEAD `6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d` (`Software-Version-0.1.0-Snapshot-004`), with Architecture 1.1.26. HCRL.004 DR-1 locates historical BL-CALLIOPE-OO-01 revision 1 and the reviewed implementation. The working-tree Design Note contains the previously revised §7.2 and §11, which differ from HEAD. [Release][release] · [HCRL.004][hcrl]

## Operative instruction

~~~markdown
# Harmonia — Calliope Review Iteration 005
## A04 — InformationProfile Semantics and IU Identity

**Repository:** `fhirfactory/harmonia`  
**Branch:** `feature/BL-CALLIOPE-OO-01`  
**Architecture baseline:** 1.1.26  
**Software snapshot:** Software-Version-0.1.0-Snapshot-004

### Objective

Revise the **current working-tree version** of:

`docs/design/DesignNote-ConceptFDN-and-InstanceFDN.md`

Codex has already completed the preceding A04 update. Preserve that work and incorporate the following additional Solution Architect clarifications.

Do not revert to the previous checked-in version.

### 1. InformationUnit identity

An InformationUnit has an independently assigned, immutable UUID.

The UUID is its authoritative instance identity.

An InformationUnit does not have or require an InstanceFDN.

### 2. InformationProfile

`InformationUnit.informationProfile` is a `ConceptFDN` identifying the InformationUnit's conceptual classification.

The agreed ConceptFDN is:

`solution=harmonia.domain=internal.concept=InformationUnit`

**InformationProfile is immutable for the lifetime of an InformationUnit.**

Its value is intrinsic to the InformationUnit concept, rather than dependent on the information contained within the IU.

It is sufficiently fixed that its initialisation could reasonably occur through a constructor default.

This is an observation about its semantics, not an instruction to implement a particular Java constructor.

### 3. Architectural provenance

InformationProfile is intentionally retained even though its value is effectively fixed.

Its purpose includes preserving explicit conceptual identity and provenance independently of:

- Java class names.
- Package hierarchies.
- Maven modules.
- Implementation language.
- Software refactoring and restructuring.

Do not infer that InformationProfile is redundant because it has a fixed value.

### 4. Information representation

Changes to the information represented by an IU do not alter its InformationProfile.

The applicable representation characteristics belong to:

- InformationRepresentation.
- ContainerMetadata.
- ContentMetadata.
- Content.

Preserve the established single-representation IU contract and its governed lifecycle semantics.

Do not introduce payload classification responsibilities into InformationProfile.

### 5. Derived Topic representation

Where an existing architectural contract requires a Topic representation of an IU, a deterministic projection may be derived from:

`InformationUnit.informationProfile + InformationUnit.id`

This projection:

- Does not introduce an InstanceFDN.
- Does not allocate another independent IU identity.
- Does not replace the authoritative IU UUID.
- Remains stable because the InformationProfile and IU ID are immutable.
- Must preserve the distinction between conceptual classification and instance identity.

Do not invent additional qualifiers, naming rules or serialization formats beyond the agreed semantics.

### 6. Architectural reconciliation

Review the revised §7.2 and related sections for consistency.

Identify any outstanding conflict with Architecture Continuum 1.1.26, particularly historical IU Topic identity and naming requirements.

Do not independently modify accepted architectural authority.

Record remaining correspondence obligations in the Iteration 005 G02 evidence.

### Constraints

- Preserve the immediately preceding Codex update.
- Do not modify implementation code.
- Do not modify the accepted Architecture Continuum.
- Do not modify accepted DRPs.
- Do not reopen the 81 settled Topic/TopicMask decisions.
- Do not modify HCRL.004 or create HCRL.005.
- Do not commit or push.
- Do not introduce additional architectural decisions.

### Deliverable

Return the revised Design Note and a concise report identifying:

1. Changes made.
2. Confirmation that the preceding A04 update was preserved.
3. Any remaining architectural conflicts or correspondence obligations.

Stop for Solution Architect review.
~~~

[prior]: ../../after/ai-agent-reports/20261120T040336Z-calliope-a04-informationunit-conceptfdn-clarification-report.md
[note]: ../../../../../../../docs/design/DesignNote-ConceptFDN-and-InstanceFDN.md#72-informationunit
[g02]: ../../after/Calliope-G02-Architectural-Reconciliation-005.md#a04--iu-topic-vocabulary-and-boundary-mappings
[release]: ../../../../../../../architecture-release.json
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-1-whole-of-calliope-scope-and-candidate

## Working-tree Design Note before this clarification

This bounded excerpt preserves the preceding uncommitted A04 wording for review. Whole-file SHA-256 was `b5d925affd8a9d308525fddc8249323f6f9c1009cca170cb4c2ee1fa3964420b`.

~~~markdown
### 7.2 InformationUnit

An InformationUnit has an independently assigned UUID that establishes its durable
identity. The UUID is independent of its classification, information representation,
ownership, location, lifecycle and version. An InformationUnit requires no
`InstanceFDN`.

The shared ConceptFDN `solution=harmonia.domain=internal.concept=InformationUnit`
identifies the InformationUnit concept and describes what an InformationUnit is.

`InformationRepresentation`, including its `ContainerMetadata` and
`ContentMetadata`, retains responsibility for describing the payload and its
representation.

The historical `harmonia.mneme.iu.<hex-uuid>` convention concerns the IU Topic
designation. The accepted Architecture Continuum 1.1.26 specifies that
designation as `harmonia.mneme.iu.<hex-string-of-compressed-uuid>` and gives the
IU Topic a distinct Topic UUID. F-01.78 preserves the designation as the
unqualified projection in the Topic redesign. The IU Topic contract and its
boundary mappings remain subject to the outstanding G02 reconciliation.
[Architecture Topic](../../enterprise-architecture/continuum/application/data-object/9e2ffa9b4a1d4422a33c2a4bbcfc86ef.md#architectural-content) ·
[InformationUnit Class](../../enterprise-architecture/continuum/application/data-object/class/74ec609de1ae498fbf9d13e4ef8c6cbe.md#architectural-content) ·
[F-01.78](../../enterprise-architecture/design/evidence/software-0.1.0/004/before/F-01-Topic-TopicMask-Adjudications-Continuation-004.md#f-0178--r3-information-unit-topic-designation--accepted)
~~~

The corresponding §11 bullet was:

~~~markdown
- Preserve the distinction between InformationUnit concept classification and the
  payload described by `InformationRepresentation`.
~~~
