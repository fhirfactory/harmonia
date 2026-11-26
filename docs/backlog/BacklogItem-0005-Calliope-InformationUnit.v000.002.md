<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Backlog Item: 0005 — Calliope InformationUnit

**Backlog Item ID:** 0005

**Backlog Item Revision:** 000.002

**Standing:** Proposed work specification aligned with the accepted BL-0005 implementation-review corrections. It preserves revision `000.001`; implementation remains subject to its separately authorised assignment and HCRL review.

## Overview

### High-Level Synopsis of Effort

Define and verify the shared Calliope model for a single governed InformationUnit (IU).

### High-Level Synopsis of Activities

Realise the reconciled model in Draft 002.005 §1 using the existing Naming API. Implement model-local construction, validation and lifecycle semantics without taking over persistence, security adjudication or content processing responsibilities.

### High-Level Synopsis of Outcome

Calliope provides a documented IU model with stable identity, initialised metadata, immutable InformationQuality and one governed representation per IU. Local revisions preserve the originating Managed Revision; security proposals remain values for Themis-Krites adjudication; container and content media types remain distinct from concepts and schemas.

### High-Level Synopsis of Acceptance Criteria

Focused tests demonstrate the exact name projection, immutability, revision expectations, authority revisions under the same UUID, Themis-Krites security boundaries, exact vocabulary paths, separate media types, new IU identity for transformation and subsequent validation, portable Content and safe handling of invalid input. Existing Naming behaviour passes regression testing.

## Context

Use the target in [Draft 002.005 §1][draft]. [Architecture 1.1.26][release], [IU Final.001 (IU-01–IU-13)][iu-001], [IU Final.002][iu-002], the [completed 1.1.25 reconciliation][rad25] and the accepted BL-0005 review dispositions establish the governing baseline. Apply the Summary and full-read/write correspondence in [Draft §2][reconciliation]. Draft 002.005 records the reviewed differences; its existence alone does not establish Continuum incorporation or software acceptance.

The branch starts from the accepted standalone Calliope Naming implementation. `Topic` is an immutable value with a qualified, case-sensitive, version-independent identity. `Topic.getName()` joins raw values with periods, omitting qualifiers and entry versions. Reuse the supported Topic builders and accessors. The current IU designation is a string projection and the IU UUID is the sole instance identifier. The assignment requires no legacy compatibility. [Naming API][topic] · [Naming design and designation][naming] · [Software HCRL 010 DR-4][naming-acceptance]

Scope covers the Calliope IU model, supporting values, release-defined builders for the agreed vocabulary, documentation and isolated tests. Consumer work is outside scope, including Store selection, persistence and cache operations, policy enforcement, reference resolution, Summary and search projections, routing, masks, transformations, grading and assurance processes, migration, and provider or wire codecs. The model preserves applicable behavioural obligations, including the Summary reference and authoritative revision semantics. [Calliope-Models][models] · [IU Final.002 §§4.4–6][iu-002] · [Summary contract][summary]

### Dependencies

Use the approved qualified `RAW` Topic and applicable release-defined Topic vocabulary. The Integration Architect defines the release's grading criteria. Consumer contracts use the existing authority, matching and persistence responsibilities. [Draft §1][draft] · [Draft §2][reconciliation]

## Associated Requirements

| Authority | Applicable obligation |
| --- | --- |
| [Conceptual IU][business-iu] · [Logical IU][iu-class] · [Calliope-Models][models] | Preserve one atomic governed IU with authoritative UUID identity, one representation and a standalone shared model boundary. |
| [IU-01–IU-13][iu-001] · [1.1.25 reconciliation][rad25] · [IU Metadata][metadata] · [InformationRepresentation][representation] | Preserve native meaning and identity, logical coordinates independent of security, supplied internal and external relationships, and distinct quality, authority, lifecycle, revision and representation concerns. |
| [Accepted G01][security] · [G02][identity] · [G06][lifecycle] · [Final.003][naming] · [MI-05][store] | Preserve security-criteria fidelity and Themis-Krites adjudication authority, qualified Topic value semantics, required capability coverage, and distinct active and durable responsibilities. |
| [Architectural Axioms][axioms] | AX-03/04 preserve native information and Harmonia's model semantics. AX-05/06 distinguish active state, durable state and information authority. AX-07 requires governed security. AX-14/17 preserve semantic distinctions and open architecture decisions. |

## Expected / Associated Activity

1. **Identity and construction.** Build the IU with immutable `id : UUID` as sole instance identifier, immutable `informationConcept : Topic`, and direct `description : String`. `informationConcept` replaces `ConceptFDN` and `informationProfile`. Implement `getName()` as `informationConcept.getName() + id.toString()` with no inserted separator. Expose the qualified `informationConcept` as the MI-05 match value. Initialise every defined metadata container with applicable defaults.
2. **InformationMetadata.** Use `PersistenceLifecycleMetadata` (Mnemosyne-established authoritative Managed Revision, disposition and legal hold) and `CacheLifecycleMetadata` (Mneme cache state and Active Generation) as the direct lifecycle allocations. Preserve the distinct revision axes and persistence handshake responsibilities. A local modification retains its originating Managed Revision as the expected revision for a subsequent governed write; Mnemosyne establishes the successor after successful persistence. In `OriginMetadata`, permit initially unknown source system Topic, first Harmonia entry component Topic and entry `Instant`, including for internally generated information. Permit population of unknown values and protect established values against change. Keep `PersistenceGateway`, `GatewayEndpoint`, `InformationLocation` and `InformationOwner` immutable per governed revision. An authorised relocation, ownership change or information-authority change establishes a new revision under the same UUID while preserving the previous snapshot and expected Managed Revision. `informationAuthority : Topic` is required on `InformationMetadata`. `SecurityMetadata` is immutable; Themis-Krites adjudicates strengthening, weakening and replacement. Calliope does not decide security policy, and this work does not implement a Krites service. Routing and itinerary belong to Pragma/Pragmatosis.
3. **Relationships.** Use `InformationMetadata.Relationships.relationships : List<InformationUnitRelationship>` for internal and external semantic links. Each element has `type : RelationshipType`, `role : RelationshipRole` and `target : URI` using the values in Draft §1.4. Support URN-based persistent identities and their string representation. An `INTERNAL_IU` target identifies the IU by UUID. Preserve applicable local and remote validation responsibilities. Keep these semantic links separate from the locator for externally persisted Content. [Draft §1][draft]
4. **InformationQuality.** Add the distinct IU constituent containing `owner : String`, `grade : {Anecdotal, Informational, Canonical}` and `certifications : Map<String, String>` (certifier to certification). The owner identifies the party responsible for the quality assessment, distinct from revisionable `InformationOwner`. Preserve grades assigned according to origin and assurance under the Integration Architect's release criteria. Protect all three properties for the IU's lifetime. Subsequent validation yields a new IU with provenance through the owning process. `Canonical` is a quality grade independent of authoritative standing under [AX-06][ax06].
5. **InformationRepresentation.** Compose one governed representation per IU. A transformation producing a new representation requires a new IU with provenance, including for equivalent representations. Governed revisions preserve representation. Include `ContainerMetadata` with `containerConcept`, `containerSchema` and required `containerMediaType`, and `ContentMetadata` with `contentConcept`, `contentSchema`, required `contentMediaType` and an initialised `ContentDetail`. Media types are IANA identifiers stored separately from concepts and schemas; do not make media types content-concept values. `contentConcept` describes what the Content represents. `contentSchema` identifies its structure, schema or profile and carries the applicable Topic entry versions. Keep format and encoding distinct from the schema and preserve the native payload instance version independently. The `RAW` container path is `Domain=ICT.Owner=fhirfactory.Standard=harmonia.Concept=InformationUnit.InformationContainer=RAW` and means Calliope imposes no additional container structure. FHIR R4 resource schemas use `Domain=Health.Owner=HealthLevelSevenInternational.Standard=FHIR.Release:4.0.1=R4.Resource=<resource-name>`; `R4` is the release designation and `4.0.1` its specific version, with no independent resource-definition versions. Preserve the common `Domain → Owner → Standard` hierarchy and use standard-defined subordinate containment rather than a separate relationship model.
6. **Content and integrity.** Model `Content.reference` as the locator for externally persisted Content and `Content.data` as inline portable Content. Require exactly one. An inline IU can be constructed before a persistence reference exists. Mneme, Mnemosyne and the PersistenceService own provider-reference generation and association management under the governed interaction contracts. Later persistence can establish a locator while preserving the IU's governed information representation. Summary projects the existing locator from the single authoritative association and leaves it absent when none exists. `ContentDetail` carries provider-supplied byte `size`, `checksum` and `checksumAlgorithm`, plus applicable `language`, `height`, `width`, `frames`, `duration` and `pages`. `SHA-256` remains the default for digest generation under the provider contract. The PersistenceService determines how and when to verify integrity. Preserve the existing durability guarantees and reference-only Summary registration boundary.
7. **Keep the Java model cohesive and format-neutral.** Use `net.fhirfactory.harmonia.model.core.information.entity` as the package reference. Treat owned metadata and representation structures as dependent values. Leave unspecified Java types, builder signatures, class decomposition and applicable media detail validation to detailed design within the agreed contracts. Document invariants, mutation boundaries and failure behaviour. Protect mutable buffers and collections and keep sensitive values out of diagnostics. [Initial Model Architecture][package] · [Object and Class Granularity][granularity] · [Immutability Guidelines][immutability] · [Serialization and Format Selection][serialization] · [Implementation Guide][guide]

## Expected Outcomes

- A documented standalone Calliope IU model and its supporting values, with the applicable Solution Architect dispositions traceable from its public contracts.
- Repeatable unit tests with synthetic data for valid and invalid model states, permitted and rejected changes, Content fidelity and Naming regressions.

## Acceptance Criteria

| Criterion | Observable result | Verification / expected evidence |
| --- | --- | --- |
| AC-1 — Identity and name | The UUID is the sole instance identifier. `getName()` concatenates the raw Topic name and UUID with no separator. The qualified `informationConcept` remains available for MI-05. | Test exact output using an explicit UUID and Topics with qualifiers and optional entry versions. Verify stable identity across revisions and that supported TopicMasks evaluate the qualified concept through the existing Naming API. |
| AC-2 — Metadata, relationships and security | Containers are initialised. Local updates preserve the origin Managed Revision as the expected write revision; Mnemosyne establishes its successor after successful persistence. Required information authority changes through a new same-UUID snapshot. Themis-Krites adjudicates security proposals; Calliope does not decide policy. | Test initial defaults, origin establishment and rejected replacement of known values, separate Managed Revision and Active Generation, coordinate/authority snapshot preservation and expected-revision retention. Exercise every relationship type and role, URI string round trips including a URN, and UUID-based `INTERNAL_IU` targets. Test security strengthening, weakening and replacement as immutable proposals and confirm no Calliope policy decision. |
| AC-3 — Governed revisions and transformations | Coordinate or authority changes create a new governed revision under the same UUID, preserve prior snapshots and retain the originating Managed Revision as the expected write revision. A transformed representation belongs to a new IU with provenance, including when semantically equivalent. | Verify coordinate and authority revisions preserve prior snapshots, identity, representation and expected revision. Verify Mnemosyne can establish the successor after successful persistence. Verify transformed-result fixtures have a distinct UUID and source provenance, and review the model API for representation replacement through a same-UUID revision. |
| AC-4 — Representation, media types and references | `contentSchema` identifies structure and applicable Topic versions independently of IANA media types, encoding and native payload instance version. Required container/content media types remain separate from concepts and schemas. Exactly one Content carrier is present. `RAW` and FHIR R4 Topics use the specified containment paths and versioning levels. | Test media type normalization and invalid cases, exact `RAW` and all 146 FHIR R4 resource Topic paths, inline text and binary construction without a locator, supplied references, both/neither carrier rejection, byte fidelity and defensive copying. Check provider integrity metadata against fixture values. Review model conformance to Draft §2, including the persistence and Summary boundaries. |
| AC-5 — Isolation and evidence | Calliope remains a standalone model library. Invalid operations reject safely and preserve valid state. | Run `mvn -B -ntp -pl calliope -am test` for IU unit tests and Naming regression. Review dependencies, public APIs and documentation for boundary conformance. For a later software candidate, record the applicable solution-wide regression, integration and conformance results under the [Software Development Praxis][verification]. |
| AC-6 — InformationQuality | `InformationQuality` is a distinct lifetime-immutable IU constituent. Its assessment owner is separate from coordinate ownership, and `Canonical` is independent of authoritative standing. A subsequent validation result has a distinct IU UUID and source provenance. | Exercise all three grades and attempts to mutate the certification map at input or through a returned value. Verify quality stability across a coordinate-owner revision, keep grade independent of authority in the fixtures, and verify distinct identity and source provenance for subsequent validation. |

**Verification dependency.** The Paradeigma ArchUnit suite named in AGENTS.md is absent on this branch. Its proposed retirement is pending. Obtain an applicable disposition for the unavailable check before relying on software candidate acceptance. [ArchUnit review][archunit]

[draft]: ../../enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.Draft.002.005.md#1-draft-002005--informationunit-model-for-calliope
[reconciliation]: ../../enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.Draft.002.005.md#2-reconciliation-with-accepted-contracts
[release]: ../../architecture-release.json
[iu-001]: ../../enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.Final.001.md
[iu-002]: ../../enterprise-architecture/design/evidence/1.1.25/001/before/accepted-input/DRP-Information-Unit-Framework.Final.002.md
[rad25]: ../../enterprise-architecture/design/register/1.1.25/hcrl-1.1.25-001.complete.md#dr-2
[topic]: ../../calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/Topic.java
[naming]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.003.md#information-unit-topic-designation
[naming-acceptance]: ../../enterprise-architecture/design/register/software-0.1.0/hcrl-software-0.1.0-010.complete.md#dr-4
[models]: ../../enterprise-architecture/continuum/application/application-component/307b3440a507403f8bc214d290bffb6a.md#architectural-content
[business-iu]: ../../enterprise-architecture/continuum/business/business-object/10284034c8534b639284386b064f2c54.md#architectural-content
[iu-class]: ../../enterprise-architecture/continuum/application/data-object/class/74ec609de1ae498fbf9d13e4ef8c6cbe.md#architectural-content
[metadata]: ../../enterprise-architecture/continuum/application/data-object/c05b7e37278141488ecc90b9a9ef96eb.md#architectural-content
[representation]: ../../enterprise-architecture/continuum/application/data-object/b9891b88150840ada3daefdec6f26c35.md#architectural-content
[summary]: ../../enterprise-architecture/continuum/application/application-service/2988247b75664896a7deb97b12b20c0a.md#architectural-content
[store]: ../../enterprise-architecture/continuum/application/application-function/9c680d56a6b943da8ff22f3264e19bc7.md#architectural-content
[security]: ../../enterprise-architecture/design/offline-design/DRP-Ergon-Authorisation-and-Identity-Delegation.Final.001.md#9-outstanding-qualifications
[identity]: ../../enterprise-architecture/design/offline-design/DRP-Calliope-Identity-and-Classification-Framework.Final.001.000.md#2-a04--informationunit-identity-and-conceptfdn
[lifecycle]: ../../enterprise-architecture/design/offline-design/DRP-Calliope-Lifecycle-Revision-and-Operational-State.Final.001.000.md
[axioms]: ../../enterprise-architecture/governance/architectural-axioms.md
[ax06]: ../../enterprise-architecture/continuum/motivation/principle/952c3b6148ce4fce94b69f99a0c2fec7.md#architectural-content
[package]: ../design/DesignNote-Calliope-Initial-Model-Architecture.md#4-target-information-model-package-hierarchy
[granularity]: ../design/DesignNote-Object-and-Class-Granularity.md
[immutability]: ../design/DesignNote-Object-Immutability-and-Modification-Guidelines.md
[serialization]: ../design/DesignNote-Serialization-and-Format-Selection.md
[guide]: ../implementation/Harmonia-Implementation-Guide-Philosophy.md
[verification]: ../../enterprise-architecture/governance/software-development-praxis.md#6-solution-wide-candidate-submission-and-evidence
[archunit]: ../../enterprise-architecture/design/review/1.1.27/hcrl-1.1.27-002.pending.md
