<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
# Design Review Proposal — PetasosParcel and PetasosParcelMask

**Standing:** Final 001 - Approved by Solution Architect; conceptual, not incorporated  
**Baseline:** Harmonia accepted Architecture Continuum 1.1.24  
**Date:** 15 November 2026

## 1. Purpose and scope

Distinguish **information being conveyed** from **the particular act of conveying it**. This draft draws on the recent Solution Architect discussion and the earlier *working* `DesignNote-Mneme-Publication-Subscription-and-Petasos-Handoff-Architecture.md`. That design note is informative, not an accepted substitute for current canonical Petasos/Ponos/Pragma contracts. This document does not redesign the queue, retries, workflow execution or Petasos durability obligations.

## 2. PetasosParcel — conceptual subject

A PetasosParcel represents an individually identifiable conveyance/handoff associated with an InformationUnit. It is not itself a second governed Content representation of the IU, nor evidence that Petasos owns the IU or its authoritative content.

```text
PetasosParcel
├── id                       : UUID
├── informationUnitSummary   : InformationUnitSummary / governed IU reference
└── metadata                 : PetasosParcelMetadata
    ├── source               : provenance of this conveyance
    ├── intendedTarget       : destination/target if known
    ├── origin               : [meaning to reconcile]
    └── ...                  : parcel-specific conveyance characteristics
```

The conversation initially described `Petasos.InformationUnit`, `Petasos.Source`, `Petasos.Origin (target if known)`, `Petasos.State`, and `Petasos.Id`. In subsequent discussion `Origin` was also discussed as an information provenance property. **Do not silently equate Petasos `Origin`, `Source` and `IntendedTarget`.** Confirm terminology and roles before prescribing fields. Parcel source is about a *particular distribution occurrence* and is not automatically the InformationUnit's origin. An IU may be conveyed in many parcels from different sources and to different targets.

`InformationUnitSummary` is the information-visible portion required for selection without mandatory retrieval of Content. It is not a separate IU identity, separately governed representation or substitute for later authorised acquisition. The exact amount available in a summary is governed by the IU summary contract and security restrictions; a parcel may instead carry a reference where appropriate.

**Operational state:** The parcel may have delivery, retry or handoff coordination requirements. Their operational semantics are **not automatically part of parcel descriptive metadata**; they are candidates for the peer `OperationalMetadata` model. Business state remains in the Content of a resulting IU. Petasos's existing mandatory delivery/recovery obligations are preserved, including any state that needs to survive recovery.

## 3. PetasosParcelMask

A PetasosParcelMask selects parcels using **conveyance characteristics**, not by duplicating IU Content selection.

```text
PetasosParcelMask
├── [parcel discrimination criteria]
│   ├── source criterion
│   ├── intended-target criterion
│   └── other parcel-specific criteria
└── [TopicMask values wherever a parcel characteristic is Topic-valued]
```

The specific class fields, optionality, identity, lifecycle, operator set and universal defaults of PetasosParcelMask remain to be decided. A candidate design uses conjunctive evaluation across applicable independent dimensions; this is a **proposal**, not an accepted exhaustive mask language. An absent optional source/target must not silently match a restrictive mask. A universal criterion may accept absence where the governing context permits that dimension to be absent.

The parcel's IU reference or Summary does not make PetasosParcelMask responsible for analysing `ContainerMetadata`, `ContentMetadata`, IU SecurityContext or Content. Those belong to `InformationUnitMask`; operational activity details belong to `OperationalMetadataMask`.

## 4. Subscription and publication relationship

```text
SubscriptionStatement
├── contentMask       : InformationUnitMask
├── routingMask       : PetasosParcelMask
└── operationalMask   : OperationalMetadataMask [optional]
```

A subscription is conceptually a statement about publication of information under conveyance circumstances, not a subclass of either mask or a property inherited from Topic. It may compare information criteria and parcel criteria **against the same publication/handoff context**. Where both apply, selection is conjunctive. Publication eligibility, subscription interest and Themis disclosure authorisation remain **separate decisions**; matching does not automatically enqueue, disclose or commit delivery.

`PublicationStatement` and `FilterStatement` likewise may compose relevant masks; their detailed attributes and semantics are not in this DRP. Mneme's existing information-management and subscription-selection responsibilities, Petasos's reliable conveyance/handoff responsibilities, and Ponos/Ergon execution responsibilities remain distinct.

## 5. Explicit exclusions and retained obligations

- No requirement that Petasos transport arbitrary non-IU payload types if all Harmonia-managed information is represented by IUs. **The universal 'everything is an IU' premise is architectural direction, not a reason to erase established work/control contracts.**
- No uncontrolled by-value transport of large Content; preserve the existing pass-by-reference constraint and information security/disclosure obligations.
- No promotion of `PetasosParcel.State` to business processing state, and no assumption that routing masks should select delivery lifecycle state.
- No new authority for a subscriber to receive IU data simply because routing and information masks match.
- No second InformationRepresentation, copy-on-publication requirement or new IU identity per parcel.

## 6. Reconciliation register

| Subject | Disposition |
|---|---|
| Parcel UUID, summary, source and target/origin distinction | Agreed conceptual direction; canonical names and detailed role semantics to reconcile |
| Parcel separate from IU information state | Agreed conceptual separation |
| PetasosParcelMetadata peer to InformationMetadata / OperationalMetadata | Agreed conceptual model |
| PetasosParcelMask as separate discrimination subject | Agreed conceptual model; fields and match language not final |
| Subscription contentMask/routingMask composition | Agreed conceptual direction |
| Delivery/retry/recovery state allocation | Remains governed by current Petasos/Artemis/Ponos contracts; coordinate with OperationalMetadata DRP |
| Canonical Petasos/Pragma/Artemis Class and Feature IDs | Must be inspected/reused during ADD; no new name is presumed canonical |

## 7. Review and disposition

This is a review proposal, **not** a publication approval or Codex work order. Reconcile existing Continuum identities, the working October design note and delivery-state ownership before incorporation. Do not infer new implementation classes solely from this conceptual UML.
