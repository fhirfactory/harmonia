<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Bounded InformationUnit Final.002 reconciliation

Standing: applied canonical candidate, not an accepted release. Proposed transition
1.1.24 → 1.1.25 / iteration 001. The root release stays 1.1.24. Source facts are
pinned in [baseline.json](../before/baseline.json); the [operative Task](../before/ai-agent-tasks/unresolved-information-unit-final002-reconciliation.md)
supplies acceptance/application authority within its stated scope. HCRL is the
semantic decision record; this document supplies the detailed correspondence.

## Source inventory and actual standing

All filenames below are under `enterprise-architecture/design/offline-design/`.
Tracked predecessors are recoverable from entry HEAD
`626d72fdb2c26768114a6c7f0a2f5845875e5b2f`; untracked accepted inputs are preserved
byte-for-byte as [IU Final.002](../before/accepted-input/DRP-Information-Unit-Framework.Final.002.md),
[Topic/TopicMask Final.001](../before/accepted-input/DRP-Topic-and-TopicMask.Final.001.md)
and [OperationalMetadata Final.001](../before/accepted-input/DRP-OperationalMetadata-and-OperationalMetadataMask.Final.001.md).

| Source | Observed state and governing authority |
| --- | --- |
| DRP-Information-Unit-Framework.Final.001.md | Present, tracked, unchanged accepted predecessor; completed HCRL 1.1.22/001 establishes whole-result acceptance despite retained pre-approval source text. IU-01–IU-13 remain operative with only the specifically authorised refinements below. |
| DRP-Information-Unit-Framework.Final.002.md | Present at the requested destination on entry, untracked; current Task identifies newly accepted conceptual reconciliation. Header and §10 record conceptual acceptance. Internal proposed-amendment/draft wording remains source history; it does not self-accept this candidate. No move or overwrite was needed. |
| DRP-Topic-and-TopicMask.Final.001.md | Absent from the initial inventory, then observable at the exact location supplied by the user during preflight. Untracked. Current Task expressly identifies Solution Architect approval; the body still says Draft 001/pending complete approval. Original bytes and both facts are preserved. No approval is inferred from Final.001. |
| DRP-OperationalMetadata-and-OperationalMetadataMask.Final.001.md | Present, untracked approved-successor input. Header says approved/not incorporated, but §§8/10 retain approval-requested and PENDING REVIEW — DRAFT 002 wording; its stated draft.001 predecessor is absent under that exact filename. The requested draft.002 filename is absent. Current Task explicitly approves OM-06–OM-10, corroborated by IU Final.002 §§4/10. Use that bounded authority; do not infer acceptance of every unresolved suggestion from its filename. |
| DRP-Interaction-Mneme-to-Mnemosyne.Final.001.md | Present, tracked; accepted complete through HCRL 1.1.24/001. Five logical contracts, AV/FC/MI and SearchResult qualifications retained. |
| DRP-Mneme-Cache-Architecture.Final.001.md | Present, tracked; accepted complete through HCRL 1.1.24/001, including MC-01–MC-07. No topology/concurrency/durability qualification is discharged here. |
| DRP-Mnemosyne-Authoritative-Persistence-Service-Model.Final.001.md | Present, tracked; accepted through HCRL 1.1.23/001; five responsibility tiers, authority and provider asymmetry remain. |
| DRP-Topic-Identity-InformationCoordinates-and-Information-Security.001.md | Present, tracked earlier candidate. Version-bearing and older naming vocabulary are historical input, not a basis to override the newly approved Topic model. No wholesale promotion or source rewrite. |
| DRP-Themis-Policy-Passport-Enforcement.draft.001.md | Present, tracked adjacent candidate; no wholesale acceptance inferred. Current canonical Themis/PEP boundaries and accepted IU security obligations control. |
| DRP-PetasosParcel-and-PetasosParcelMask.Final.001.md | Present, untracked; explicitly **not approved** by the operative Task regardless of filename. No incorporation, copy into accepted-input or framework derivation. |

`Mark-ChatGPT-Context` was not found by filename search of the repository and the
exposed current agent workspace, including hidden files. It was optional; no
substitute context or remembered architecture was used. Historical draft filenames
not supplied here remain absent, rather than reconstructed. The available approved
successors and explicit Task decisions establish this bounded reconciliation.

## Current identity and relationship inventory

The before-state contains **6,563 elements / 9,427 native relationships / 462 qualified
source predicates**. [canonical-inventory.json](canonical-inventory.json) records
the bounded anchors and all incident predecessor/current relationships, with actual
Composition ancestry for relevant Features. [canonical-delta.json](canonical-delta.json)
lists the nine changed existing resources. No element/relationship, Feature,
Capability, Topic instance, Home, Package, Class, View or navigation allocation is
created, deleted, reparented or renamed. New conceptual concerns are expressed in
the existing definitions and their responsibility boundaries; not every conceptual
grouping warrants an independent native element. Standalone logical allocation is
left open where Final.002 explicitly leaves it open.

| Existing element | Reconciliation |
| --- | --- |
| Business IU `10284034c8534b639284386b064f2c54` | Same conceptual IU as logical Class; one UUID/representation, explicit authoritative persisted revision; revised information/operational boundary and preserved qualified history. |
| Logical IU Class `74ec609de1ae498fbf9d13e4ef8c6cbe` | Preserve Class identity, Package/Composition and names. Show conceptual information allocation and associated OperationalMetadata outside IU constituency; old four-context diagram remains bounded superseded history and supported interface projection. |
| IU Metadata `c05b7e37278141488ecc90b9a9ef96eb` | Retain logical DataObject; express scoped information/operational allocation, itinerary, version/temporal distinctions and neutral-selector boundaries without allocating wrappers or new metadata Classes. |
| InformationRepresentation `b9891b88150840ada3daefdec6f26c35` | Preserve exactly-one structure; clarify portable Content, Container/Content type placement, no mandatory child UUIDs and all revision distinctions. |
| Topic model `9e2ffa9b4a1d4422a33c2a4bbcfc86ef` | Reuse common model identity; immutable typed/sequenced runtime Topics, stable full UUID, distinct names and no Topic.Version. No current specialised Topic instance is changed. |
| TopicMask model `bb489157a6bf48be926c5a601bb0a6c3` | Reuse model identity and existing Association. Immutable mask UUID/definition; exclusive name/canonical basis, element-wise qualifier/name regex, Exact/Inclusive and neutral matching. |
| Mneme-Standard-Cache `cfe4e0dbe6894a14841452b731ab9cb8` | Revise semantic allocation while preserving accepted Summary and associated operational façade views; no independent Summary authority, compulsory durability, or operational-search expansion. MC-01–MC-07 unchanged. |
| Mneme-side Store `9c680d56a6b943da8ff22f3264e19bc7` | Add scoped projection/operational boundary; preserve five contracts, MI-05 full coverage, actual authoritative revisions, Themis and durable outcomes. |
| Mnemosyne-side Façade `22bdac97fe0d48d1844f1e982d03d387` | Same compatibility/subject distinction without changing exposed contracts, ownership or service IDs. |

## Accepted semantic correspondence and qualifications

| Input assertion/decision | Canonical treatment and retained limit |
| --- | --- |
| Final.002 §§0–2; IU-R01/03/05; Appendix A IU-01/02/06/09 | Same BO/Class IU, persistent IU UUID and separate Topic UUID; no ManagedIU, nested IU, extra representation or mandatory Container/Content UUID. IU authoritative revision stays with the IU; operational revision is not an authoritative counter. |
| Final.002 §3; IU-R10/18; IU-IT01 | Four coordinates unchanged. InformationItinerary has its own conceptual meaning in existing Metadata: participation/actual-or-intended passage, separate from coordinates, origin, business Journey and provenance evidence. Participant names are illustrative; exact historical/intended allocation and update representation remain open. No new native Class/Topic is inferred. |
| Final.002 §3.1; IU-R17/19 | Parcel carries its own complete itinerary instance for its handoff; complete structure is not full knowledge. Earlier history is not retroactively changed. Boundary recorded only in IU Metadata; Petasos/Parcel definitions and machinery untouched. Reopen after operational Mneme/Mnemosyne 0.5 quality. |
| OM-06 / OM-R04 | Mneme operational Version conceptually in associated OperationalMetadata. Mneme concurrency, optimistic/stale-write protection and required façade visibility unchanged. |
| OM-07 / OM-R05–R06 | Active Generation/cache-working/residency/reference facts conceptually in OperationalMetadata, relative to their IU manifestation. Generation is not IU identity, revision or representation. Embedded/reference/hybrid and transient/durable distinctions remain. |
| OM-08 / OM-R07–R08 | Persistence operation/handshake/stage/correlation/timing facts conceptually in OperationalMetadata. Preserve request/accept/commit/completion, truthful factual Outcome, durability/recovery/retention and required interface views; no new state machine. |
| OM-09 / OM-R01–R03/R10–R15 | IU identity, Topic designation, authoritative persisted revision, four coordinates, information security, optional supplied relationships, ContainerMetadata, ContentMetadata and native Content stay with information. No ownership transfer between Features. |
| OM-10 / OM-R09 | Governed information disposition/hold/archive/retirement stays with IU information lifecycle; does not become transient persistence-handshake status. RETIRE is not physical destruction. |
| OM-R16; Final.002 §§4/7 | Kleio AuditEvent capture, established Provenance/Client Library, Dokimasia assurance and operational logging remain distinct. OperationalMetadata can correlate evidence; cannot replace it or dispose of required evidence on eviction. |
| Final.002 §§2.2/5; predecessor §§9/16; Interaction AV-04/MI-06 | Reconcile predecessor's three-version wording with its already established authoritative IU revision. Structure version, authoritative persisted IU revision, Mneme operational revision and optional native Content instance version differ. Active Generation is manifestation identity. No synthetic native version, guaranteed meta.versionId equivalence, extra counter or payload mutation. |
| Final.002 §§5/7; IU-02 | Exactly one representation; textual/binary/by-reference portable Content. Java objects are working manifestations; no sole Java-object interchange/persistence contract, universal JSON or wire/storage schema. Governed transformation creates another IU with appropriate provenance. |
| Topic Final.001 §2 | Explicit canonical sequence and typed qualifiers retained; gaps/duplicates/ambiguous order rejected, absent qualifier normalised by absolute position. Derived runtime names are distinct from persisted canonical entries. Runtime name/registry-scope and Continuum canonicalId/displayName roles are not asserted equivalent. No Topic.Version or current Topic rename/ID change. |
| Topic Final.001 §3 | Immutable TopicMask; stable UUID independent of match; exactly one naming basis. Name-based matches each unqualified name; canonical-based independently matches qualifier/name. Exact requires same depth, Inclusive admits descendants. Explicit order survives interchange. Default unrestricted intent preserved; exact unrestricted/root encoding and regex dialect/anchoring/invalid-pattern wire behaviour remain detailed design. |
| Final.002 §6; Topic §§3–4 | IU/operational/parcel selectors are separable neutral concepts; compose/reference TopicMasks where Topic-valued. No invented higher-mask field catalogue, operators, cardinalities, absence/snapshot semantics, inheritance into Passport or Petasos subscription implementation. |
| IU-04/05/07/08/10–IU-12 | Four coordinates independent of security, optional supplied relationships/local validation and remote non-validation retained. No universal IU operation catalogue; existing capability ancestry/Features/Motivation remain, no new historical seven-Feature catalogue. |
| Accepted Interaction/Cache; IU-11 | Five services and every service definition/ID remain byte-unchanged. Summary excludes exactly Content, its opaque reference stays in ContentMetadata; required associated operational façade facts remain available. No implicit addition of arbitrary operational observations to authoritative search. Full/summary access authorisation, no-loss work, coherent same-IU version and multi-site durable mutation Success remain. MI-05 remains complete required coverage by the union of eligible available criteria, not overlap. |

## Materiality, axioms and review boundary

This is material information architecture reconciliation, not Minutiae. AX-03/04
preserve native models/portable boundaries and reuse machinery; AX-05/06 distinguish
active/durable/information authority; AX-07 preserves Themis/disclosure and audit;
AX-08/09 preserve meaningful evidence and scoped operational retention; AX-10/11
preserve distributed correctness/no-loss work without weakening guarantees;
AX-12/13 preserve façade access and external boundaries; AX-14/15 distinguish
revisions, generations, outcomes and uncertainty; AX-17 preserves qualified inputs,
unresolved allocation and actual-result acceptance; AX-18 prevents routing or
observation from conferring domain authority. No axioms or Feature allocation change.

No genuine unresolved semantic contradiction between the bounded accepted inputs
was established. Older metadata homes, three-version wording and governed-renaming
phrases are explicitly reconciled under the newly accepted decisions; affected
canonical excerpts remain unchanged inside scoped supersession wrappers. Original
accepted DRPs, earlier candidate Topic wording and all published history are intact.
The retained sources' pending/approved inconsistencies are disclosed provenance,
with the operative Task supplying the bounded acceptance rather than an invented
retrospective edit. The Solution Architect must confirm this exact interpretation
and actual result through the HCRL.

Detailed design remains deferred: standalone Class/association allocations for new
concepts; exact Metadata/API projection/storage and migration; Origin/itinerary/
provenance evidential relationships; higher-mask fields and observation/absence
rules; regex/interchange conventions; native version-assignment algorithms and
provider layouts; all inherited MC/AV/FC/MI qualifications. No Java, persistence
schema, Junie dispatch, Petasos framework, immutable publication, release allocation,
commit or push is part of this candidate activity.

## Preservation-evidence qualification

Final scope verification found that pre-existing tracked `Topic.java` was recorded
in the initial dirty inventory but omitted from the original user-file hash set
because that capture stripped a leading porcelain-status space before path slicing.
All **29 pinned user files** remain hash-identical; this does not establish a
byte-for-byte before/after comparison for that additional tracked file. No Java edit
was performed. The original before-state evidence is unchanged; no missing hash is
reconstructed. [Qualification](user-work-preservation-qualification.json) preserves
the exact limit for Solution Architect treatment, and [final Git scope](final-git-state.json)
compares against both the actual recorded dirty inventory and the bounded candidate.
