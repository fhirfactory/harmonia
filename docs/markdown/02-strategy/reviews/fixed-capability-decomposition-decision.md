<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Fixed Capability Decomposition — Governed Metamodel Decision

**Date:** 2026-10-13. **Decision status:** Approved human architectural decision,
supplied explicitly in the task instruction; this record persists that decision
and its bounded documentation reconciliation. It requests no further approval.

This uses the existing canonical review/decision-record pattern. The normative
rules reside in the [Business Architecture metamodel §1.1](../../03-business-architecture/metamodel/business-architecture-metamodel.md#11-capability-decomposition-and-terminology)
and its [identifier grammar §8.2](../../03-business-architecture/metamodel/business-architecture-metamodel.md#82-typed-canonical-identifier-grammar),
with the Strategy interpretation in the [Capability decomposition model](../capability-maps/capability-tier-model.md).
This record explains their authority, rationale and reconciliation consequences;
it does not replace central axioms or create a new governance mechanism.

## 1. Decision and semantic roles

Harmonia SHALL use the fixed Capability decomposition:

```text
CT1 Capability
  └── CT2 Capability
       └── CT3 Capability
            └── Feature (FT)
```

| Tier / type | Architectural role |
| :--- | :--- |
| CT1 — Capability Tier 1 | Establishes the broad Capability frame within Harmonia's architectural scope. |
| CT2 — Capability Tier 2 | Decomposes the CT1 frame into constituent Capabilities. |
| CT3 — Capability Tier 3 | Establishes the finest Capability frame required by Harmonia and the Capability context within which Features are defined. |
| FT — Feature | Expresses required system-enabled behaviour within its owning CT3 Capability context. |

Every Feature SHALL be framed by CT3. Every CT3 SHALL have a CT2 parent; every
CT2 SHALL have a CT1 parent. Capability tiers SHALL NOT be skipped. Complete
CT1 → CT2 → CT3 ancestry is required for a fully reconciled Feature.

> A Feature cannot compensate for incomplete Capability decomposition. If
> Harmonia requires a Feature, the CT3 Capability that frames that Feature must
> first be identified. Failure to identify a meaningful CT3 indicates incomplete
> upstream Capability modelling, not permission to skip the tier.

This is a modelling obligation, not authority to fabricate architectural
elements. CT3 Capabilities SHALL NOT be manufactured for structural symmetry.
Existing Features without established CT3 frames SHALL be preserved with
Capability ancestry/decomposition explicitly requiring separate reconciliation.

## 2. Rationale and axiom consistency

Fixed decomposition provides consistent semantic framing for Features. CT3 is
the Capability boundary immediately above a Feature, so Feature behaviour has
the same kind of governing Capability context across the model. Permitting
Features at arbitrary Capability depths gives nominally equivalent Feature
statements inconsistent architectural meaning and weakens traceability.
Missing CT3 therefore exposes incomplete upstream modelling; it is not an
exception to the target hierarchy.

Harmonia intentionally stops at CT3. Its Capability Model supports the level
needed to derive and govern Harmonia, rather than attempting the complete
Enterprise Architecture of a hospital, health service provider, health
department or other healthcare enterprise. CT4/CT5 tiers are consequently
unnecessary within Harmonia; CT4, CT5 and deeper Capability tiers SHALL NOT be
defined. Reference healthcare landscape coverage does not require exhaustive
enterprise decomposition.

The materially relevant axioms are:

- [AX-01](../../governance/architectural-axioms.md#ax-01): the hierarchy remains bounded to Harmonia's health-information purpose, without acquiring exhaustive healthcare-enterprise scope.
- [AX-14](../../governance/architectural-axioms.md#ax-14): Capability Tier, Feature type, Architectural Layer and Derivation Stage remain semantically distinct.
- [AX-17](../../governance/architectural-axioms.md#ax-17): the explicitly approved target model supersedes the prior permissive decision, while unestablished identities, tiers, parents and identifiers remain explicit uncertainties.

No axiom amendment or application/component allocation follows. Existing
implementation is not evidence of Capability ancestry. The grammar can support
mechanical enforcement of complete Feature ancestry, but current tooling
predates the decision and its enforcement gap remains explicit (§5).

## 3. Supersession and retained semantics

| Prior position | Current governing position |
| :--- | :--- |
| Decomposition MAY proceed CT1 → CT2 → CT3 → FT, without materialising every lower tier. | Fixed ancestry is required for every fully reconciled Feature. Missing CT2/CT3 is an unresolved modelling obligation. |
| A Feature may be framed directly by CT1 or CT2. | CT3 is the mandatory immediate frame; CT1 → FT and CT2 → FT are prohibited in the reconciled target model. |
| Generic Capability paths may precede Feature tokens in complete Canonical IDs. | A Feature path requires CT1, CT2 and CT3 tokens before the unchanged Feature token. |
| Individual Care Delivery's L3 decomposition is not currently required. | Its CT3 decomposition is not yet architecturally established; meaningful CT3 frames must first be established where Features are subsequently required. |

The [G1 identification prerequisite §10.2](../../04-information-architecture/reviews/package2-g1-review.md#102-ancestry-and-behavioural-anchoring)
retains the old lower-tier-omission wording as explicitly superseded historical
decision evidence. [K9](../../04-information-architecture/reviews/package2-g1-review.md#11-k9--capability-metamodel-typing)
remains closed with its original evidence and uncertainty conclusions intact.
The earlier five-region reconciliation record retains its dated decision and
scope; its preservation of the then-current decomposition rules is historical,
not authority over this superseding metamodel decision.

**Capability Specialisation** is retained as the Feature semantic classification:
a Feature inherits Capability semantics while expressing required system-enabled
behaviour. That classification does not make FT a fourth Capability Tier or
justify arbitrary-depth Feature framing. CT1 → CT2 and CT2 → CT3 are Capability
decomposition relationships, not primarily specialisation. No broader Feature
semantic redefinition is made.

Functions, Services and Processes may still anchor directly to an established
Capability or Feature responsibility. This decision does not require intervening
Features or manufactured Capability detail merely to provide behavioural
anchoring, and does not require every Capability to have Features.

`Layer ≠ Capability Tier ≠ Derivation Stage`. L1/L2/L3 remain deprecated as
canonical classifications. Domain02's five-stage derivation progression is
unchanged in meaning and SHALL NOT be converted into Capability tiers.

## 4. Preserved catalogue evidence and unresolved consequences

No existing unresolved Capability receives a CT assignment. Document nesting,
numbering, contextual views and catalogue presentation establish neither
Capability identity nor ancestry and SHALL NOT allocate Canonical IDs.
Local Feature tokens such as `FEAT-EM-01` retain their spelling and do not
establish complete structural Canonical IDs.

`Client Administration → Person Identity → Identifier Resolution [FT /
FEAT-EM-01]` remains an established partial responsibility chain. The owning
Capability's tier, complete ancestry and structural ID remain unresolved; the
fixed hierarchy neither changes that owner nor supplies a missing CT3.

Individual Care Delivery's six established children remain Individual Health
Assessment, Clinical Determination, Individual Care Planning, Clinical
Intervention, Individual Health Monitoring and Individual Care Review. Their
legacy L2 labels supply no CT allocation. Care settings and illustrative
clinical activities do not automatically become CT3 Capabilities. CT3
decomposition remains unestablished and is not performed by this task.

The reference candidate capture retains all 137 established FT Features and
their local identifiers, without any allocated CT tiers or complete structural
Canonical IDs. Required Feature framing remains unresolved. The 69 enabling
catalogue descriptions with unresolved standalone identity remain unresolved;
none is promoted to CT3 to satisfy the target rule. Established responsibility
may continue to support downstream derivation under K9 without a claim of
complete structural reconciliation.

## 5. Verification and stop boundary

The existing [RADS capture documentation](../../03-business-architecture/reference-model/README.md#validation-and-navigation)
identifies the validator/schema's former CT1/CT2 Feature permission and source
fingerprint/locator reconciliation consequences. Updating that tooling or the
JSON capture is outside this documentation task. A representation PASS alone
does not prove conformance to the fixed target hierarchy.

The [completion report](../../../../.junie/reports/2026-10-13-fixed-capability-decomposition.md)
records exact changed files, residual searches, source/capture preservation
checks and bounded validation results. It is execution evidence, not
architectural authority.

No Capability, Feature, Function, Service, Process, Canonical ID, instance
ancestry relationship or application/component allocation is created. No
Business or Business Enabling Capability redesign, 69-description adjudication,
RADS JSON reconciliation or Domain05 B2.2 activity is authorised or performed.
Work stops after this metamodel/documentation reconciliation.
