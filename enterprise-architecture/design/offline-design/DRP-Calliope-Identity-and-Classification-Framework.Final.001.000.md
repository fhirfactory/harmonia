<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Design Review Package — Calliope Identity and Classification Framework

- **Standing** · Final.001.000, architecturally accepted for G02 by the Solution Architect
- **Design lineage** · [Draft.000.001][prior-draft] → [Draft.000.002][reviewed-draft] → `Final.001.000`
- **Evidence boundary** · software-0.1.0, iteration 005, G02 (A03–A05)
- **Review basis** · Architecture Continuum 1.1.26, `Software-Version-0.1.0-Snapshot-004` at `6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d` on `feature/BL-CALLIOPE-OO-01`. [Architecture release][release] · [Cumulative HCRL.004][hcrl]

This revision carries forward A03–A05 from [Draft.000.001][prior-draft] and records the Solution Architect's clarification of Topic value semantics and historical correspondence. It complements the Topic/TopicMask draft, which carries all 81 settled F-01 decisions and their explicit successions. The incorporated Topic, TopicMask and IU contracts in 1.1.26 govern existing obligations pending governed amendment. [Original adjudications][task] · [Solution Architect clarification][refinement] · [G02 evidence][g02] · [Topic/TopicMask Draft.001.003][topic-draft] · [RAD 1.1.25 DR-2–DR-4][rad]

## 1. A03 — Clean-sheet implementation and identity preservation

Calliope 0.1.0 is developed against the accepted architecture. Existing code is replaceable. Java constructors, interfaces, factory behaviours and internal serialization formats provide implementation evidence. Architectural authority comes from the accepted architecture and independently governed contracts. [AX-04 and AX-17][axioms]

Backward compatibility is required only where an independently established architectural, contractual or retained-information obligation applies. Existing Architecture Continuum identities, including governed `canonicalId` values, and governed relationships must be preserved. Governed information references must retain their referent, identity and historical meaning. Provisional runtime UUIDs and disposable implementation representations may be replaced where independent obligations do not require their preservation. This boundary preserves meaningful evidence and the limited lifetime of transient state under AX-08 and AX-09. [Axioms][axioms] · [Architectural identity][naming] · [F-01.76 and F-01.79][f004] · [A03 evidence][g02-a03]

Where an established obligation applies, document the correspondence between the governed source reference and the redesigned representation. The retained source, applicable registry or contract and governing owner establish what the reference means. Handle ambiguous references through explicit disposition before migration. [F-01.76–F-01.79][f004]

### Topic value semantics

Topic is a reusable value datatype. It has no independently assigned ID or UUID attribute. Its non-versioned qualified canonical path supplies value identity, equality and hashing under the settled F-01 semantics. Equivalent Topics can be constructed independently and have the same value identity. The object using a Topic retains its own identity and lifecycle. TopicMask retains its settled structural and deterministic semantics, including matching. Topic ordering and versioning follow the existing Topic/TopicMask design. [Solution Architect clarification][refinement] · [F-01.76–F-01.79][f004] · [Topic/TopicMask Draft.001.003][topic-draft]

The common Topic model definition and existing governed Continuum elements retain their architectural identifiers, including `canonicalId`, and their governed relationships. Where an element describes a runtime Topic value, F-01.79 requires explicit correspondence. [AX-14 and AX-17][axioms] · [Architectural identity][naming] · [Continuum Topic][topic] · [F-01.79][f004]

## 2. A04 — InformationUnit identity and ConceptFDN

| Concern | Agreed design |
| --- | --- |
| `InformationUnit.id` | An independently assigned, immutable UUID and the authoritative identity of the IU instance. The IU has no InstanceFDN. |
| `InformationUnit.informationProfile` | An immutable ConceptFDN with the fixed value `solution=harmonia.domain=internal.concept=InformationUnit`. It describes the IU's conceptual classification. The shared value preserves explicit architectural provenance independently of Java classes, packages, modules, implementation languages and refactoring. Constructor-default initialization is permitted. The initialization mechanism is detailed design. |
| Represented information | Each IU has one governed `InformationRepresentation`. `ContainerMetadata` describes its envelope, `ContentMetadata` describes its content structure and technical characteristics, and `Content` carries the represented information. |

Changes to represented information leave the profile fixed and follow the existing IU revision and lifecycle rules. A transformation that produces a differently structured or formatted governed representation creates a new IU with provenance under the existing transformation contract. [Design Note §7.2][fdn] · [Continuum IU Class][iu] · [IU lifecycle][iu-lifecycle] · [IU representation][iu-representation]

### Derived Topic projection

Where a Topic representation is required, a deterministic projection may use `InformationUnit.informationProfile + InformationUnit.id` as its inputs. The expression identifies those inputs. The IU UUID remains authoritative and the projection adds neither a second IU identity nor an InstanceFDN. The ConceptFDN classifies the IU concept. An InstanceFDN, where applicable in other designs, designates an instance in its own context. The IU Topic projection is a Topic representation governed by its applicable contract. Generic FDN design questions remain in the Design Note. [Design Note §§3–5, 7.2 and 10][fdn] · [Topic/TopicMask Draft.001.003 §7][topic-draft-iu]

Continuum 1.1.26 assigns an independent UUID to the historical IU Topic and specifies `harmonia.mneme.iu.<hex-string-of-compressed-uuid>` as its canonical designation, with deterministic reversible compression. F-01.78 carries that designation forward as the unqualified `Topic.getName()` projection and calls for a separately governed qualified path. The derived Topic is a value. Where a historical reference has an established preservation obligation, its correspondence to the IU and the projected Topic value must be demonstrated from the governing source. The two projection inputs alone cannot establish the meaning of a previously assigned Topic UUID. [Continuum Topic][topic] · [Continuum IU Class][iu] · [F-01.78][f004] · [Solution Architect clarification][refinement]

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

### Historical references with established preservation obligations

Preserve each independently governed historical reference and its original referent and meaning. Establish the source authority, original reference and applicable registry, retention or replay scope before assigning correspondence. Carry UUID-backed Topic and TopicMask references through the required legacy binding or an explicitly governed migration outside the redesigned value datatypes. Preserve legacy registry uniqueness and explicit sequence rules at bindings where those contracts apply. Resolve alias, collision and generated-qualifier ambiguities before changing a reference or selector. Equal Topic values alone cannot establish that two historical references identified the same referent. [Continuum Topic][topic] · [Continuum TopicMask][mask] · [F-01.76–F-01.79][f004] · [A03 evidence][g02-a03]

### Architecture definitions requiring governed amendment

The independent Topic UUID in 1.1.26 is part of the accepted historical architectural contract. Its correspondence and these amendments require governed reconciliation through ADD/HCRL before dependent application. Preserve the existing Continuum element IDs and governed relationships. [RAD 1.1.25 DR-2–DR-4][rad] · [F-01.79][f004]

- **Common Topic definition** · Document the reusable, immutable datatype and its structural value identity in place of the independent Topic UUID and replacement-by-UUID clauses. Reconcile ordered qualifier/value entries, optional entry versions, registry-scoped name rules and explicit sequence requirements with the approved redesign and applicable legacy bindings. F-01.78 keeps entry versions independent of IU revisions, Mneme operational versions, Content Structure Version and native Content versions. [Continuum Topic][topic] · [F-01.76 and F-01.78][f004] · [Topic/TopicMask draft][topic-draft]
- **IU Class and designation** · Record the sole IU UUID identity and the immutable `informationProfile` ConceptFDN in the IU Class. Describe the Topic as a derived value where required and reconcile the historical distinct Topic UUID clause and canonical designation with F-01.78 and the established reference obligations. [Continuum IU Class][iu] · [Continuum Topic][topic] · [F-01.78][f004]
- **Common TopicMask definition** · Reconcile its historical UUID, two alternative bases and lack of a version axis with the previously adjudicated structural criteria, explicit universality and optional entry-version criteria. Adoption requires governed alignment and no-widening proof under G03. Review dependent reference and selector contracts where the mapping changes their meaning. [Continuum TopicMask][mask] · [F-01.77][f004] · [Issue groups][groups]

A05 fits the existing Models/DataSets division. Its framework-wide construction, validation, generator and extension rules are agreed design direction. Assess their incorporation under ADD/HCRL where normative architecture is affected. Refer any conflicting ownership assignment to the Solution Architect, including where a proposed domain vocabulary allocation would alter Calliope's accepted model or nomenclature ownership. [Models][models] · [DataSets][datasets] · [Outcome][outcome]

### Disposable implementation representations

Provisional factory UUIDs and internal encodings are replaceable where evidence establishes no independent preservation obligation. A UUID or format observed in code or a fixture alone establishes no compatibility requirement. [A03 evidence][g02-a03] · [Solution Architect clarification][refinement]

### Documentation corrections

The Design Note §7.2 now distinguishes the accepted canonical IU Topic designation from the redesign's unqualified projection, and the ConceptFDN from represented content. Its historical `<hex-uuid>` notation is shorthand for the compressed UUID designation, with exact encoding still a design obligation. The existing retained-input filename discrepancy for Topic/TopicMask remains a provenance correction awaiting governed treatment. [Design Note §7.2][fdn] · [Evidence limits][g02-limits]

### Detailed design obligations

- Establish the exact IU qualifier sequence, compressed UUID encoding and binding-specific readers and writers. Identify which source references and replay obligations apply to each corpus. Specify correspondence for historical UUID-backed references with established preservation obligations, including registry scopes, aliases and collisions. G03 and G04 consume the selector and reference mappings. [A03–A04 evidence][g02] · [Issue groups][groups]
- For each classification family, document the owner, base values, membership/change rules, extension admission, generator inputs, validation and supported conversions. Apply the distinct lifecycle and outcome semantics from G06 when designing the relevant mappings. [A05 evidence][g02] · [Issue groups][groups]

### Implementation and verification obligations

Once authorized, verify preservation of governed referents and equal selector scope, deterministic round trips and repeatable generation, rejection of invalid or ambiguous input, and fidelity of native code-system semantics. Test applicable retained bindings and replay, IU designation and UUID correspondence, and the G03/G04/G06 consumer obligations. Retain the existing F-01 behavioural acceptance evidence requirements. [G02 evidence][g02] · [Topic/TopicMask Draft.001.003 §10][topic-tests]

## 5. Review decision

**Architecturally accepted for G02.** Mark Hunter, Solution Architect, accepted the exact [reviewed Draft.000.002][reviewed-draft] for A03–A05. Section 4 carries forward governed Continuum reconciliation, historical reference correspondence, IU Topic qualifier, encoding and boundary mappings, detailed value-set definitions and ownership, implementation conformance and verification, and the dependencies on G03, G04 and G06. The cumulative HCRL.004 obligations retain their review standing. [Solution Architect disposition][acceptance] · [G02 reconciliation][g02] · [HCRL.004][hcrl]

Subsequent proposed updates to `Final.001.000` begin at `draft.001.001`.

[release]: ../../../architecture-release.json
[hcrl]: ../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md
[prior-draft]: DRP-Calliope-Identity-and-Classification-Framework.draft.000.001.md
[reviewed-draft]: DRP-Calliope-Identity-and-Classification-Framework.draft.000.002.md
[acceptance]: ../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-calliope-g02-identity-and-classification-framework-acceptance-task.md
[refinement]: ../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-calliope-g02-identity-and-classification-framework-refinement-task.md
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
