<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Design Review Package — Calliope Identity and Classification Framework

- **Standing** · Draft.000.001, pending Solution Architect review
- **Evidence boundary** · software-0.1.0, iteration 005, G02 (A03–A05)
- **Review basis** · Architecture Continuum 1.1.26, `Software-Version-0.1.0-Snapshot-004` at `6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d` on `feature/BL-CALLIOPE-OO-01`. [Architecture release][release] · [Cumulative HCRL.004][hcrl]

This draft records the Solution Architect's G02 adjudications. It complements the Topic/TopicMask draft, which carries all 81 settled F-01 decisions and their explicit successions. Topic identity, ordering, versioning, matching and equality follow that draft within its redesign scope. The incorporated Topic, TopicMask and IU contracts in 1.1.26 govern existing architectural obligations pending governed alignment. [Issued adjudications][task] · [G02 evidence][g02] · [Topic/TopicMask Draft.001.003][topic-draft] · [RAD 1.1.25 DR-2–DR-4][rad]

## 1. A03 — Clean-sheet implementation and identity preservation

Calliope 0.1.0 is developed against the accepted architecture. Existing code is replaceable. Java constructors, interfaces, factory behaviours and internal serialization formats provide implementation evidence. Architectural authority comes from the accepted architecture and independently governed contracts. [AX-04 and AX-17][axioms]

Backward compatibility is required only where an independently established architectural, contractual or retained-information obligation applies. Existing Architecture Continuum identities, including governed `canonicalId` values, and governed relationships must be preserved. Governed information references must retain their referent, identity and historical meaning. Provisional runtime UUIDs and disposable implementation representations may be replaced where independent obligations do not require their preservation. This boundary preserves meaningful evidence and the limited lifetime of transient state under AX-08 and AX-09. [Axioms][axioms] · [Architectural identity][naming] · [F-01.76 and F-01.79][f004] · [A03 evidence][g02-a03]

Where an established obligation applies, document the correspondence between the governed source reference and the redesigned representation. The retained source, applicable registry or contract and governing owner establish what the reference means. Handle ambiguous references through explicit disposition before migration. [F-01.76–F-01.79][f004]

## 2. A04 — InformationUnit identity and ConceptFDN

| Concern | Agreed design |
| --- | --- |
| `InformationUnit.id` | An independently assigned, immutable UUID and the authoritative identity of the IU instance. The IU has no InstanceFDN. |
| `InformationUnit.informationProfile` | An immutable ConceptFDN with the fixed value `solution=harmonia.domain=internal.concept=InformationUnit`. It describes the IU's conceptual classification. The shared value preserves explicit architectural provenance independently of Java classes, packages, modules, implementation languages and refactoring. Constructor-default initialization is permitted. The initialization mechanism is detailed design. |
| Represented information | Each IU has one governed `InformationRepresentation`. `ContainerMetadata` describes its envelope, `ContentMetadata` describes its content structure and technical characteristics, and `Content` carries the represented information. |

Changes to represented information leave the profile fixed and follow the existing IU revision and lifecycle rules. A transformation that produces a differently structured or formatted governed representation creates a new IU with provenance under the existing transformation contract. [Design Note §7.2][fdn] · [Continuum IU Class][iu] · [IU lifecycle][iu-lifecycle] · [IU representation][iu-representation]

### Derived Topic projection

Where a Topic representation is required, a deterministic projection may use `InformationUnit.informationProfile + InformationUnit.id` as its inputs. The expression identifies those inputs. The IU UUID remains authoritative and the projection adds neither a second IU identity nor an InstanceFDN. The ConceptFDN classifies the IU concept. An InstanceFDN, where applicable in other designs, designates an instance in its own context. The IU Topic projection is a Topic representation governed by its applicable contract. Generic FDN design questions remain in the Design Note. [Design Note §§3–5, 7.2 and 10][fdn] · [Topic/TopicMask Draft.001.003 §7][topic-draft-iu]

Continuum 1.1.26 gives the IU Topic an independently assigned Topic UUID distinct from the IU UUID. It assigns the historical designation `harmonia.mneme.iu.<hex-string-of-compressed-uuid>` as the canonical IU Topic name, with deterministic reversible compression. F-01.78 preserves that designation as the unqualified `Topic.getName()` projection in the redesign and calls for a separately governed qualified path. The two projection inputs do not establish correspondence to a previously assigned Topic UUID. The design must account for both the per-IU designation and the distinct Topic UUID, including governed legacy references and their meaning. [Continuum Topic][topic] · [Continuum IU Class][iu] · [F-01.78][f004]

## 3. A05 — Deterministic classification framework

Calliope shall provide a deterministic, strongly defined conceptual modelling framework comprising explicit elements, value sets, value-set generators, validators and documented extension mechanisms.

- Define strict, reproducible construction and validation rules. Provide predefined representations for common and release-defined concepts and support explicitly defined domain-specific concepts through documented extension rules.
- Provide deterministic conversion between supported representations. Reject invalid or ambiguous inputs. Extensions must be explicitly defined and validated. A general-purpose runtime interpretation engine requires an independently established need.
- Preserve domain ownership and native terminology, including the source code system, its semantics and the applicable version where the native contract makes that version significant. [AX-02, AX-03, AX-04, AX-14 and AX-18][axioms]

| Authority | Responsibility |
| --- | --- |
| Calliope-Models | Supplies the common structures, types, semantic definitions and applicable release-defined value sets or their build-time representations. Generated representations describe the governed definitions and known values. [Continuum Calliope-Models][models] |
| Calliope-DataSets | Manages applicable governed runtime dataset content and membership through existing Mneme access and Mnemosyne authoritative persistence under AX-05. Apply any release-specific membership and change rules. [Continuum Calliope-DataSets][datasets] · [U12 and U13 allocations][calliope] |
| Individual components | Own the semantics of their assigned domain-specific classifications and their operational decisions within the applicable contracts. Shared model definition, vocabulary membership and operational use are distinct responsibilities. For example, Calliope governs U12 authorisation nomenclature and Themis owns policy association, resolution and enforcement. The common Outcome model and its governed closed Reason extensions retain Calliope's established model ownership. [U12 allocation][calliope] · [Continuum Outcome][outcome] |
| External standards | Govern their native terminology and code-system semantics. Harmonia governs any internal mapping and the use of external terminology within its own operations under the applicable boundary contract. [AX-02, AX-03 and AX-18][axioms] |

## 4. Reconciliation with Architecture Continuum 1.1.26

### Architectural conflicts requiring Solution Architect disposition

- The incorporated Topic and TopicMask contracts retain UUID identity. The redesign uses the qualified Topic path and structural mask identity, with legacy reference handling outside those runtime models. The incorporated Topic also requires sequence numbers in storage and exchange and unqualified-name uniqueness within its applicable registry. Bindings that would drop indices or admit colliding names to that registry require explicit treatment. The redesign adds optional entry versions to the incorporated qualifier/name shape. F-01.78 keeps those versions independent of IU revisions, Mneme operational versions, Content Structure Version and native Content versions. [Continuum Topic][topic] · [F-01.76 and F-01.78][f004] · [Topic/TopicMask draft][topic-draft]
- The incorporated mask contract has two alternative bases and no version axis. The redesign uses structural criteria, explicit universality and optional entry-version criteria. Adoption requires governed alignment and no-widening proof under G03. [Continuum TopicMask][mask] · [F-01.77][f004] · [Issue groups][groups]
- For the IU, substituting the shared ConceptFDN for the per-IU designation or dropping a governed Topic UUID or its reference obligations changes accepted meaning and requires disposition. Preserving the designation and Topic-reference correspondence allows a derived representation to coexist with the accepted contract. [Continuum IU Class][iu] · [Continuum Topic][topic] · [F-01.78][f004]

### Governed incorporation or amendment

The IU Class has no explicit `informationProfile` allocation in 1.1.26. Review incorporation of the immutable ConceptFDN and its relationship to existing IU and Topic contracts. A05 fits the existing Models/DataSets division. Its framework-wide construction, validation, generator and extension rules are agreed design direction. Assess their incorporation under ADD/HCRL where normative architecture is affected. Refer any conflicting ownership assignment to the Solution Architect, including where a proposed domain vocabulary allocation would alter Calliope's accepted model or nomenclature ownership. [Continuum IU Class][iu] · [Models][models] · [DataSets][datasets] · [Outcome][outcome]

### Documentation corrections

The Design Note §7.2 now distinguishes the accepted canonical IU Topic designation from the redesign's unqualified projection, and the ConceptFDN from represented content. Its historical `<hex-uuid>` notation is shorthand for the compressed UUID designation, with exact encoding still a design obligation. The existing retained-input filename discrepancy for Topic/TopicMask remains a provenance correction awaiting governed treatment. [Design Note §7.2][fdn] · [Evidence limits][g02-limits]

### Detailed design obligations

- Establish the exact IU qualifier sequence, compressed UUID encoding and binding-specific readers and writers. Identify which source references and replay obligations apply to each corpus. Specify correspondence for governed Topic UUIDs, registry scopes, aliases and collisions. G03 and G04 consume the selector and reference mappings. [A03–A04 evidence][g02] · [Issue groups][groups]
- For each classification family, document the owner, base values, membership/change rules, extension admission, generator inputs, validation and supported conversions. Apply the distinct lifecycle and outcome semantics from G06 when designing the relevant mappings. [A05 evidence][g02] · [Issue groups][groups]

### Implementation and verification obligations

Once authorized, verify preservation of governed referents and equal selector scope, deterministic round trips and repeatable generation, rejection of invalid or ambiguous input, and fidelity of native code-system semantics. Test applicable retained bindings and replay, IU designation and UUID correspondence, and the G03/G04/G06 consumer obligations. Retain the existing F-01 behavioural acceptance evidence requirements. [G02 evidence][g02] · [Topic/TopicMask Draft.001.003 §10][topic-tests]

## 5. Review decision

**Pending Solution Architect acceptance of this exact draft.** A03 direction, A04 conceptual design and A05 framework direction are agreed. Continuum correspondence, detailed design and the existing cumulative review obligations remain open with their recorded standing. [G02 disposition][g02] · [HCRL.004][hcrl]

[release]: ../../../architecture-release.json
[hcrl]: ../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md
[task]: ../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-calliope-g02-identity-and-classification-framework-task.md
[g02]: ../evidence/software-0.1.0/005/after/Calliope-G02-Architectural-Reconciliation-005.md
[g02-a03]: ../evidence/software-0.1.0/005/after/Calliope-G02-Architectural-Reconciliation-005.md#a03--topic-identity-and-reference-migration
[g02-limits]: ../evidence/software-0.1.0/005/after/Calliope-G02-Architectural-Reconciliation-005.md#evidence-limits
[groups]: ../evidence/software-0.1.0/005/after/Calliope-Architectural-Issue-Groups-005.md
[topic-draft]: DRP-Topic-and-TopicMask.draft.001.003.md
[topic-draft-iu]: DRP-Topic-and-TopicMask.draft.001.003.md#information-unit-topic-designation
[topic-tests]: DRP-Topic-and-TopicMask.draft.001.003.md#10-mandatory-behavioural-acceptance
[rad]: ../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-2
[f004]: ../evidence/software-0.1.0/004/before/F-01-Topic-TopicMask-Adjudications-Continuation-004.md
[axioms]: ../../governance/architectural-axioms.md
[naming]: ../../governance/canonical-identity-and-naming.md#architectural-identity
[topic]: ../../continuum/application/data-object/9e2ffa9b4a1d4422a33c2a4bbcfc86ef.md#architectural-content
[mask]: ../../continuum/application/data-object/bb489157a6bf48be926c5a601bb0a6c3.md#architectural-content
[iu]: ../../continuum/application/data-object/class/74ec609de1ae498fbf9d13e4ef8c6cbe.md#architectural-content
[iu-lifecycle]: DRP-Information-Unit-Framework.Final.002.md#43-information-lifecycle-is-not-an-operational-handshake
[iu-representation]: DRP-Information-Unit-Framework.Final.002.md#5-informationrepresentation-containermetadata-contentmetadata-and-content
[fdn]: ../../../docs/design/DesignNote-ConceptFDN-and-InstanceFDN.md#72-informationunit
[models]: ../../continuum/application/application-component/307b3440a507403f8bc214d290bffb6a.md#architectural-content
[datasets]: ../../continuum/application/application-component/53bf6815c7a54ed0abde9b1aad311843.md#architectural-content
[calliope]: ../../continuum/application/application-component/466ad8f75a954c0581b5bba090443849.md#architectural-content
[outcome]: ../../continuum/application/data-object/6d43c35381a9438f9101cf1d8b530b19.md#architectural-content
