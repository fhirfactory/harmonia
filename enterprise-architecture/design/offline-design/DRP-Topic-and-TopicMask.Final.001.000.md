<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
# Design Review Proposal — Foundational Topic and TopicMask

**Standing:** Final 001 - Solution Architect reviewed and approved; not incorporated into the Architecture Continuum  
**Baseline:** Harmonia accepted 1.1.24, commit `626d72fdb2c26768114a6c7f0a2f5845875e5b2f`  
**Date:** 15 November 2026  
**Scope:** Topic and TopicMask only; no subscriptions, publishing, security policy or IU implementation

## 1. Purpose and authority

Define Harmonia's common, reusable and immutable identification/classification and matching constructs. The accepted `DRP-Information-Unit-Framework.Final.001.md` establishes element-wise TopicMask discrimination and Topic use throughout Harmonia. The earlier `DRP-Topic-Identity-InformationCoordinates-and-Information-Security.001.md` remains a candidate, to be reconciled rather than silently promoted. This draft captures the more recent Solution Architect discussion. It does not itself supersede the accepted Continuum or authorise ADD.

## 2. Topic

A Topic is an immutable, independently identifiable, typed, hierarchically contained named concept. It is not a subscription, permission, Java-specific type, persistence endpoint or the native object identified by its name.

```text
Topic
├── id                : UUID                          [persisted]
├── canonicalName     : Map<SequenceNumber, Entry>    [persisted]
│   └── Entry
│       ├── qualifier : String
│       └── name      : String
├── name              : String                        [derived]
├── friendlyName      : String                        [derived]
└── relativeName      : String                        [derived]
```

`canonicalName` is an **explicitly sequenced** root-to-leaf set of qualified entries. Sequence numbers must be retained in exchange and storage; never infer ordering solely from Java List, JSON array or map iteration. Sequence gaps, duplicates and ambiguous order must be rejected rather than silently repaired. An absent qualifier is normalised to `N0`, `N1`, … `Nn` according to its absolute sequence position. The examples use zero-based positions; precise interchange encoding is implementation design, subject to deterministic reconstruction.

For `{0: Country=Australia, 1: StateTerritory=ACT, 2: City=Canberra}` the derived `name` is `Australia.ACT.Canberra`, `friendlyName` is `Canberra`, and `relativeName` is `City=Canberra`. The **qualifier** is a first-class discriminator of type, not display decoration.

The `TopicID` is a full UUID; an eight-hex-digit prefix may be displayed in logs/UI, extended on collision, but never substitutes for identity. Topic identity, containment, qualifiers and names **cannot be mutated**. A substantive replacement is a different Topic with a different UUID; historic references retain their original meaning. `Topic.name` is globally unique within the applicable Topic registry, barring a separately authorised redefinition of registry scope; do not silently introduce Topic versions to evade uniqueness.

**No Topic.Version property.** The earlier draft that placed version on Topic, and version regex on TopicMask, has been superseded by the subsequent conceptual decision. IU authoritative revision, Mneme operational revision, Content Structure Version and optional native content version belong to their respective information contracts, not to Topic. Earlier version-bearing prose in the candidate Topic draft must be removed/reconciled during incorporation.

Topic implementations should offer useful, stable, safe human-readable audit/log display helpers without substituting display names for full identities. Exact helper APIs, presentation and Java code are left to implementation design.

## 3. TopicMask

A TopicMask is a separately identified, **immutable** Topic discriminator. Its identity does not affect which Topics match it.

```text
TopicMask
├── id           : UUID
├── type         : TopicMaskType
│   ├── TopicNameBased
│   └── TopicCanonicalNameBased
├── maskType     : MaskType
│   ├── Exact
│   └── Inclusive
└── patterns     : ONE OF
    ├── TopicNamePattern
    │   └── entries : Map<SequenceNumber, NameRegex>
    └── TopicCanonicalNamePattern
        └── entries : Map<SequenceNumber, EntryPattern>
            └── EntryPattern
                ├── qualifierRegex : String
                └── nameRegex      : String
```

`TopicNameBased` matches the unqualified **individual name at each position**. `TopicCanonicalNameBased` independently evaluates **qualifier and name at each position**. One mask has exactly one naming basis, not an AND/OR combination of the two. Each pattern is a regular expression for a *single element*; it is never an uncontrolled whole-path regex.

| `maskType` | Meaning |
|---|---|
| `Exact` | Each specified level matches and the candidate Topic has exactly that containment depth. |
| `Inclusive` | Each specified level matches and any further descendant levels are included. |

Illustratively, an inclusive name-based mask for `Australia.ACT` matches that Topic and `Australia.ACT.Canberra`; the exact form matches only the two-level Topic. Qualifier masks can discriminate `City=.*` independently of a particular city name. An unrestricted mask is semantically inclusive with an unrestricted pattern, using `.*` as the actual regex expression; `*` may be human shorthand but is not valid standalone Java regex. The default constructor/configuration is expected to produce an unrestricted mask, not null criteria with accidental wildcard semantics. Exact representation for an unrestricted/root match needs a deterministic interchange convention.

TopicMask definitions are immutable: any change of type, containment behaviour or patterns creates another mask with another UUID. References to old masks remain meaningful. Pattern evaluation must produce repeatable results across supported language runtimes; formal regex dialect, escaping, anchoring, invalid regex behaviour and wire format are bounded implementation contracts requiring evidence.

## 4. Architectural boundaries

- A TopicMask is a **neutral matching criterion**. A successful match is not permission to disclose, publish, execute, persist or route information.
- `InformationUnitMask`, `PetasosParcelMask` and `OperationalMetadataMask` **contain or reference TopicMasks** when a subject has Topic-bearing properties; they do not require TopicMask to know those subject schemas.
- `SubscriptionStatement`, `PublicationStatement` and `FilterStatement` may contain higher-level masks. They **do not inherit from TopicMask**.
- Themis may use matching machinery for subject requirements and authorisation criteria. Policy evaluation, actions and Passport semantics remain separate and must not be reduced to TopicMask inheritance.
- IU Content versions and Content Structure Versions must **not** be inferred from Topic names or introduced as a special `TopicMask.versionRegex`.

## 5. Cross-source reconciliation

1. Preserve accepted IU-05 element-wise matching and existing canonical Topic/TopicMask identities; refine basis, qualifier matching, exact/inclusive depth and immutable UUID semantics without generating duplicates.
2. Reconcile the earlier Topic Identity DRP's `canonicalId`, `canonicalName`, `friendlyName` and `displayName` vocabulary with this `id`, `canonicalName`, `name`, `friendlyName`, `relativeName` model. Do not silently assert identical meanings.
3. Existing `model.topic.Topic` and `TopicSubscription` Java classes may implement narrower message-classification concerns. Their class names are not proof that they already satisfy this foundational contract.
4. No separate Topic versioning; preserve the distinct accepted IU/content version concepts and review the current IU-version refinement separately.
5. Topic name derivation/registry and the accepted IU Topic naming pattern (`harmonia.mneme.iu.<compressed-uuid>`) must be reconciled without equating IU UUID and TopicID.

## 6. Review and disposition

**Agreed in discussion:** immutable Topic and TopicMask identities, explicit canonical sequencing and qualifier semantics, derived Topic names, no Topic.Version, element-wise regex, exact/inclusive, mask UUID and independent matching semantics.  
**Requires incorporation reconciliation:** current Continuum identities, original name vocabularies, default universal-mask encoding and cross-language pattern conformance.  
**Not authorised:** repository edits, automatic ADD, implementation or change to accepted releases. Solution Architect approval of this complete draft is pending.
