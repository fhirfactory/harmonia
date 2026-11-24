<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
# Design Review Proposal — OperationalMetadata and OperationalMetadataMask

**Standing:** Draft 001-001 for Solution Architect review; conceptual proposal, not incorporated  
**Baseline:** Harmonia accepted Architecture Continuum 1.1.24  
**Date:** 15 November 2026

## 1. Purpose

Identify Harmonia's middleware-operational information as a **distinct metadata concern**, separate from governed information properties and Petasos conveyance characteristics. Establish its own matching subject without overloading `InformationUnit`, `ContentMetadata` or `PetasosParcelMetadata`. No new state machine, generic framework property bag, database, service or Java type is authorised by this draft.

## 2. Peer metadata model

```text
Metadata concerns (peers; not one mandatory object containment tree)
├── InformationMetadata          : description/governance of information
├── PetasosParcelMetadata        : description of a particular conveyance
└── OperationalMetadata          : Harmonia middleware operation/execution
```

The types share a conceptual metadata framework **without forcing common ownership, identical persistence, common lifecycle or mandatory co-location**. OperationalMetadata can be associated with an IU, a parcel, a processing activity, an Ergon, a Ponos execution, an Artemis work item or other relevant operational subjects; this does not declare any single host or component to be their universal owner. The responsible Feature owns the semantics of each operational attribute.

**Business versus middleware state is fundamental.** An Ergon may produce a *new* InformationUnit whose Content carries the business outcome or domain processing state. That Content state is not a middleware execution status. Conversely, scheduling, handoff, queueing, retries, checkpointing, operational execution and cache activity concern Harmonia middleware operation even when their effects serve business outcomes. `ProcessingState` must not become an ambiguous universal property covering both.

## 3. OperationalMetadata — conceptual responsibilities

Candidate concerns, all subject to ownership/preservation reconciliation:

| Concern | Candidate operational home | Important boundary |
|---|---|---|
| Mneme active-cache generation, cache references and residency | OperationalMetadata associated with IU/cache manifestation | Not the IU's immutable identity or Mnemosyne authoritative IU version |
| Mneme operational revision | OperationalMetadata associated with IU active management | Not Content Structure Version or resource-native revision |
| Mneme–Mnemosyne request/accept/commit/complete handshaking | OperationalMetadata associated with operation/IU | Durable acceptance/completion evidence must be preserved; not a universal business lifecycle |
| Petasos delivery, retry, queue and handoff progress | OperationalMetadata associated with parcel/conveyance | Not the originating IU's business state; exact existing state ownership retained |
| Ponos/Ergon/Artemis activity state | OperationalMetadata associated with processing/work activity | Not automatically ContentMetadata or Petasos routing identity |

This is a **candidate relocation and classification table**, not approval to remove accepted `InformationUnit.Metadata.ManagementContext` or `LifecycleContext`. These contexts are currently accepted within IU Metadata by IU-03, IU-09 and associated DRPs. A move to peer OperationalMetadata requires explicit continuity of access, lifecycle, concurrency, persistence, resilience and evidence contracts. A logical association can preserve existing contracts even when physical storage is outside an IU; do not assume every operational datum is transient.

OperationalMetadata shall not claim ownership of: business states encoded as governed Content; information classification/type; IU semantic relationships; InformationCoordinates; required information SecurityContext; native Content identity/version; Content Structure Version; immutable IU identity; or Petasos parcel identity/source/target descriptors.

## 4. OperationalMetadataMask

An OperationalMetadataMask is a neutral predicate/discriminator applied to the **operational properties available in a specified context**.

```text
OperationalMetadataMask
└── criteria : governed operational dimensions
    └── [may contain TopicMask for Topic-valued criteria]
```

**Conceptually accepted:** a distinct OperationalMetadataMask can be combined with other subject masks, without making one mask a subclass of another. **Not yet settled:** exact fields, identity/immutability policy, missing-value semantics, matching operators, state vocabularies, time-window selection, observational consistency, snapshot timing, or whether all operational dimensions can and should be subscribed to. Do not invent these in the Architecture Continuum as if accepted.

Operational predicates must be meaningful relative to a subject and observation point. For example, a parcel retry state is not automatically a property of the IU, and an Ergon's execution status is not necessarily stable across the lifetime of an IU. If an evaluation occurs at time *t*, the governing feature must specify which observed state is evaluated and what happens if the state changes after matching; the mask does not itself guarantee delivery or event capture.

## 5. Subscription composition and ownership

```text
SubscriptionStatement
├── contentMask     : InformationUnitMask
├── routingMask     : PetasosParcelMask
└── operationalMask : OperationalMetadataMask [optional]
```

A SubscriptionStatement may include an operational criterion; **absence of operationalMask means no operational restriction** (subject to a defined default/serialization contract), not a new operational state or implied authorisation. Where applicable the three domains combine conjunctively. A consumer must not require operational criteria for every subscription or force processing state into the InformationUnit Summary merely to enable subscription selection.

Themis remains responsible for authorisation. A positive operational match cannot override the IU's security requirements or the Identity Passport. AuditEvents, operational logs and provenance are distinct; a log entry or operational match is not automatically durable audit evidence. OperationalMetadata should allow correlation to relevant identities and events without disclosing whole Content in routine logs.

## 6. Consistency and resilience

- Retain established persistence-before-ack and no-loss-of-governed-work obligations where applicable.
- Operational metadata can be **transient or durable by characteristic and contract**. No blanket assumption either way.
- Authoritative IU version supplied by Mnemosyne remains an information version, not an operational retry counter.
- `InformationUnit.Metadata.ManagementContext` and `LifecycleContext` relocation requires a governed compatibility path for Mneme, Mnemosyne and their façades.
- Any operational metadata exposed in subscriptions must have defined disclosure boundaries; not every runtime diagnostic is suitable as a publicly subscribable IU characteristic.

## 7. Reconciliation register

| Subject | Status / instruction |
|---|---|
| Rename `HarmoniaOperationalMetadata` to `OperationalMetadata` | Agreed |
| Metadata concerns are peers, not obligatorily siblings inside one object | Agreed |
| Dedicated `OperationalMetadataMask` | Agreed conceptually |
| Optional SubscriptionStatement.operationalMask | Agreed conceptually |
| Split business Content state from middleware operational state | Agreed |
| Relocation of existing IU ManagementContext/LifecycleContext fields | **Candidate only**; evaluate individually and preserve existing accepted contracts |
| Identity, exact state lists, mask evaluation, publisher-specific lifecycle | **Not yet settled** |
| Existing Ponos/Pragma/Artemis/Petasos/Mneme/Mnemosyne authority | Preserve; semantic reconciliation required |

## 8. Review/disposition

This draft is design input for the Solution Architect. Acceptance requires explicit disposition of any desired IU-metadata movement and applicable existing operational state contracts. No Codex ADD or implementation is authorised by the draft alone.
